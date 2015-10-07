/***********************************************************
 * Software: instrument server
 * Module:   Can message handler class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 30.4.2013
 *
 ***********************************************************/

#include <string>
#include <map>

#include <stdint.h>
#include<pthread.h>
#include <uuid/uuid.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "yami++.h"
#include "lincan.h"
#include "can_message.h"
#include "can_buffer.h"
#include "messages.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

/* Function implements constructor for class
 *
 * Parameters:
 * dev: reference to can device object
 *
 * Returns:
 *
 */

can_message::can_message(can_base&  dev) : device(dev), log(Category::getInstance("message")),
		rx_buffer(100) {
	log.info("can message handler started: %s", device.get_bus_name().c_str());

	if (pthread_mutex_init(&fsm_mutex, NULL) != 0) {
		log.error("mutex init failed");
	}

	command_map.insert(make_pair("CheckOnLine", &can_message::cmdCheckOnLine));
	command_map.insert(make_pair("MeasurementStart", &can_message::cmdMeasurementStart));
	command_map.insert(make_pair("MeasurementStop", &can_message::cmdMeasurementStop));
	command_map.insert(make_pair("MeasurementState", &can_message::cmdMeasurementState));
	command_map.insert(make_pair("MeasurementUpdate", &can_message::cmdMeasurementUpdate));
}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

can_message::~can_message() {

}

/* Function adds message handling functions
 *
 * Parameters:
 * msg_in: incoming message
 *
 * Returns:
 *
 */

void can_message::handle_msg(auto_ptr<IncomingMsg> msg_in) {
	string cmd;
	auto_ptr<ParamSet> params;
	ParamSet return_params(1);

	source_ip = msg_in->getSourceAddr();

	// set default response
	return_params.setString(0, SERVER_COMMAND_FAIL_RESP);

	try {
		// get parameters
		params = msg_in->getParameters();
		// get device name
		params->getString(0, cmd);
		log.debug("command: %s", cmd.c_str());

		// try to find command call back function
		if(command_map.find(cmd) == command_map.end()) {
			// not found
			ParamSet return_params(1);
			log.warn("command handler %s not implemented", cmd.c_str());
		} else {
			// execute function
			msg_in->reply((*this.*command_map[cmd])(params));
		}

		// TODO paluu viestissä pitää olla error ja status mukana
	} catch(exception ex) {
		log.error("Failed to get parameters from message");
		// send default reply if parameter parsing failed
		msg_in->reply(return_params);
	}
}

/* Function returns configuration of device
 *
 * Parameters:
 *
 * Returns:
 * map containing device information
 *
 */

map<string, string> *can_message::get_config(void) {
	map<string, string> *config = new map<string, string>();

	// construct configuration info
	config->insert(pair<string, string>("type", device.get_bus_name() + ":name:" + device.get_driver_name()));

	return(config);
}

/* Function implements caalback for disconnect message
 *
 * Parameters:
 * client_ip: connected clients ip address
 *
 * Returns:
 * true if disconnect and clear ok
 *
 */

bool can_message::disconnect(string client_ip) {
	// get iterator
	map<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;

	// lock command map
	pthread_mutex_lock(&fsm_mutex);

	// check capture command
	if((cmd_iterator = command_list.find(make_pair(client_ip, CAPTURE))) != command_list.end()) {
		// remove from command set
		command_list.erase(cmd_iterator);
		// remove old messages
		rx_buffer.clear();
		log.debug("CAPTURE command removed ok for client %s", client_ip.c_str());
	}

	// check generate command
	while((cmd_iterator = command_list.find(make_pair(client_ip, GENERATE))) != command_list.end()) {
		// check if application data pointer is initialized
		if((*cmd_iterator).second->p != NULL) {
			// remove memory allocation
			delete(static_cast<CAN_MSG*>((*cmd_iterator).second->p));
		}

		// remove from command set
		command_list.erase(cmd_iterator);
		// remove old messages
		rx_buffer.clear();
		log.debug("GENERATE command removed ok for client %s", client_ip.c_str());
	}

	// unlock command map
	pthread_mutex_unlock(&fsm_mutex);

	return(false);
}

/* Function implements state machine
 *
 * Parameters:
 *
 * Returns:
 *
 */

void can_message::fsm(data_stream_interface *d) {
	CAN_MSG msg;
	char buf[26];
	uint16_t index = 1;

	// any commands available
	if(!command_list.empty()) {
		// lock command map
		pthread_mutex_lock(&fsm_mutex);

		// get iterator
		map<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;
		// loop all available commands
		for(cmd_iterator = command_list.begin(); cmd_iterator != command_list.end(); ++cmd_iterator) {
			if((*cmd_iterator).second->active == true) {
				// select command
				switch((*cmd_iterator).second->cmd) {
				case CAPTURE:
					if(device.isReady()) {
						device.can_read(&msg);
						rx_buffer.insert(msg);
					}

					// repeat timer handling
					if((*cmd_iterator).second->repeat) {
						// decrement repeat counter
						(*cmd_iterator).second->repeat_timer--;
						if((*cmd_iterator).second->repeat_timer == 0) {
							// load new value and execute capture code
							(*cmd_iterator).second->repeat_timer = (*cmd_iterator).second->repeat_cycle;
						} else {
							// not yet time to get data
							break;
						}
					}

					// is any messages in buffer
					if(rx_buffer.is_empty()) {
						// 1 base parameter + data buffers
						ParamSet params(rx_buffer.get_size() + 1);

						// TODO ei voi ajaa 10ms syklillä
						log.debug("number of message parameters %d", params.getParamCount());

						// send data back to client
						params.setString(0, SERVER_DATA_OK_STATUS);
						//TODO lisää väylä  id
						//log.debug("buffer size %d before %d", rx_buffer.get_size(), sizeof(long));
						while(rx_buffer.is_empty()) {
							rx_buffer.pop_back_bytes(buf);
							params.setBinary(index++, buf, sizeof(buf));
							/*for(int j = 0;j < sizeof(buf);j++) {
							printf("%02X " , buf[j] & 0xFF);
						}
						printf("\n");*/
							//log.debug("buffer size %d", rx_buffer.get_size());
						}

						d->send_data((*cmd_iterator).second->ip, "Browser"/*device.get_name()*/, params);
						// TODO ota kiinni Overflow poikkeus
					}
					break;
				case GENERATE:
					// repeat timer handling
					if((*cmd_iterator).second->repeat) {
						// decrement repeat counter
						(*cmd_iterator).second->repeat_timer--;
						if((*cmd_iterator).second->repeat_timer == 0) {
							// load new value and execute capture code
							(*cmd_iterator).second->repeat_timer = (*cmd_iterator).second->repeat_cycle;
						} else {
							// not yet time to get data
							break;
						}
					}
					// TODO pitääkö tehdä else haara???
					if((*cmd_iterator).second->p != NULL) {
						log.debug("can data generation %s", (*cmd_iterator).second->unique_id.c_str());
						device.can_send((static_cast<CAN_MSG*>((*cmd_iterator).second->p)));
					}
					break;
				default:
					break;
				}

				// remove command from list if single shot
				if((*cmd_iterator).second->repeat == false) {
					command_list.erase(cmd_iterator);
				}
			}
		}

		// unlock command map
		pthread_mutex_unlock(&fsm_mutex);
	}

	// tähän tarkastelu puskurin tyhjentämisestä
}

/* Function implements command call back for self test
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet can_message::cmdCheckOnLine(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // is device found on bus
  //if(device.isConnected()) {
    // set command response
    return_params.setString(0, SERVER_COMMAND_OK_RESP);
  //} else {
    // set fail command response
    //return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
  //}
  return(return_params);
}

/* Function implements command call back for capture start
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet can_message::cmdMeasurementStart(auto_ptr<ParamSet> params) {
	ParamSet return_params(2);
	fsm_command *cmd = new fsm_command();
	uuid_t uu;
	char uuid[37];

	// generate unique id for command
	uuid_generate(uu);
	uuid_unparse(uu, uuid);

	// lock command map
	pthread_mutex_lock(&fsm_mutex);

	// get measurement command from message
	uint8_t measure_cmd = params->getByte(1);

	// check active command
	if(measure_cmd == CAPTURE) {
		// check if capture is already in list
		if(!(command_list.find(make_pair(source_ip, CAPTURE)) == command_list.end())) {
			// unlock command map
			pthread_mutex_unlock(&fsm_mutex);

			// set fail default response
			return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
			return(return_params);
		}
	}

	//if(command_list.find(make_pair(source_ip, (fsm_states)measure_cmd)) == command_list.end()) {
	// set command for measurement
	cmd->cmd = (fsm_states)measure_cmd;
	cmd->unique_id = string(uuid);
	cmd->ip = source_ip;
	cmd->channels = params->getByte(2);
	cmd->repeat = !!params->getInt(3);
	cmd->active = !!params->getInt(4);

	// only if repeat is used
	if(params->getParamCount() > 5) {
		// load counter
		cmd->repeat_cycle = params->getInt(5) / 10;
		cmd->repeat_timer = params->getInt(5) / 10;
	}

	// only if generator command is used
	if(params->getParamCount() > 6) {
		// parse can frame information
		cmd->p = new CAN_MSG();
		static_cast<CAN_MSG*>(cmd->p)->id = params->getInt(6);
		static_cast<CAN_MSG*>(cmd->p)->flags = params->getInt(7);
		static_cast<CAN_MSG*>(cmd->p)->length = params->getInt(8);
		params->getBinaryValue(9, (char *)&(static_cast<CAN_MSG*>(cmd->p)->data));
	}

	// channel count not used here yet
	cmd->ch_cnt = 0;

	// add command to queue
	command_list.insert(pair<pair<string, fsm_states>, fsm_command*>(make_pair(source_ip, (fsm_states)measure_cmd), cmd));
	log.debug("command %d insert ok %s id: %s", cmd->cmd, cmd->ip.c_str(), cmd->unique_id.c_str());
	// unlock command map
	pthread_mutex_unlock(&fsm_mutex);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return_params.setString(1, cmd->unique_id.c_str());
	//} else {
	// set fail default response
	//return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
	//}

	return(return_params);
}

/* Function implements command call back for capture stop
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet can_message::cmdMeasurementStop(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);
	string id;
	// get measurement command from message
	uint8_t measure_cmd = params->getByte(1);

	// get command id from message
	params->getString(2, id);

	// get iterator
	map<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;

	// check active command
	//if((cmd_iterator = command_list.find(make_pair(source_ip, CAPTURE))) != command_list.end()) {
	//if((cmd_iterator = command_list.find(make_pair(source_ip, (fsm_states)measure_cmd))) != command_list.end()) {


	// lock command map
	pthread_mutex_lock(&fsm_mutex);

	// loop all available commands
	for(cmd_iterator = command_list.begin(); cmd_iterator != command_list.end(); ++cmd_iterator) {
		// find correct command
		if((*cmd_iterator).second->unique_id.compare(id) == 0) {
			// check if application data pointer is initialized
			if((*cmd_iterator).second->p != NULL) {
				// remove memory allocation
				delete(static_cast<CAN_MSG*>((*cmd_iterator).second->p));
			}

			// remove from command set
			command_list.erase(cmd_iterator);
			log.debug("command removed ok");

			break;
		}
	}

	// unlock command map
	pthread_mutex_unlock(&fsm_mutex);
	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for client disconnect
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */
#if 0
ParamSet can_message::cmdDisconnect(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);
	// get iterator
	map<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;

	// check active command
	if((cmd_iterator = command_list.find(make_pair(source_ip, CAPTURE))) != command_list.end()) {
		// remove from command set
		command_list.erase(cmd_iterator);
		log.debug("command removed ok");
	}

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}
#endif


/* Function implements command call back for measurement event state change
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet can_message::cmdMeasurementState(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);
	string id;

	// get command id from message
	params->getString(1, id);

	// get iterator
	map<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;

	// lock command map
	pthread_mutex_lock(&fsm_mutex);

	// loop all available commands
	for(cmd_iterator = command_list.begin(); cmd_iterator != command_list.end(); ++cmd_iterator) {
		// find correct command
		if((*cmd_iterator).second->unique_id.compare(id) == 0) {
			// set new state
			(*cmd_iterator).second->active = !!params->getInt(2);
			log.info("command %s state changed %d", (*cmd_iterator).second->unique_id.c_str(),
					(*cmd_iterator).second->active);
			break;
		}
	}

	// unlock command map
	pthread_mutex_unlock(&fsm_mutex);
	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for measurement event update
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet can_message::cmdMeasurementUpdate(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);
	string id;

	// get command id from message
	params->getString(10, id);

	// get iterator
	map<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;

	// lock command map
	pthread_mutex_lock(&fsm_mutex);

	// loop all available commands
	for(cmd_iterator = command_list.begin(); cmd_iterator != command_list.end(); ++cmd_iterator) {
		// find correct command
		if((*cmd_iterator).second->unique_id.compare(id) == 0) {
			(*cmd_iterator).second->channels = params->getByte(2);
			(*cmd_iterator).second->repeat = !!params->getInt(3);
			(*cmd_iterator).second->active = !!params->getInt(4);

			// only if repeat is used
			if(params->getParamCount() > 5) {
				// load counter
				(*cmd_iterator).second->repeat_cycle = params->getInt(5) / 10;
				(*cmd_iterator).second->repeat_timer = params->getInt(5) / 10;
			}

			// only if generator command is used
			if(params->getParamCount() > 6) {
				// parse can frame information
				(*cmd_iterator).second->p = new CAN_MSG();
				static_cast<CAN_MSG*>((*cmd_iterator).second->p)->id = params->getInt(6);
				static_cast<CAN_MSG*>((*cmd_iterator).second->p)->flags = params->getInt(7);
				static_cast<CAN_MSG*>((*cmd_iterator).second->p)->length = params->getInt(8);
				params->getBinaryValue(9, (char *)&(static_cast<CAN_MSG*>((*cmd_iterator).second->p)->data));
			}

			log.debug("command %d updated ok %s id: %s", (*cmd_iterator).second->cmd,
					(*cmd_iterator).second->ip.c_str(), (*cmd_iterator).second->unique_id.c_str());
		}
	}

	// unlock command map
	pthread_mutex_unlock(&fsm_mutex);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

}
