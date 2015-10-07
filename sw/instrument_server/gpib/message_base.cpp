/***********************************************************
 * Software: instrument server
 * Module:   device message base class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 23.9.2013
 *
 ***********************************************************/

#include <map>

#include <stdint.h>
#include <uuid/uuid.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "yami++.h"
#include "message_interface.h"
#include "message_base.h"
#include "messages.h"

namespace instrument_server {

message_base::message_base() : log(Category::getInstance("message")), state(IDLE) {
	// TODO Auto-generated constructor stub

}

message_base::~message_base() {
	// TODO Auto-generated destructor stub
}

/* Function implements call back for disconnect message
 *
 * Parameters:
 * client_ip: connected clients ip address
 *
 * Returns:
 * true if disconnect and clear ok
 *
 */

bool message_base::disconnect(string client_ip) {
	// get iterator
	multimap<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;

	// lock command map
	pthread_mutex_lock(&fsm_mutex);

	// check capture command
	if((cmd_iterator = command_list.find(make_pair(client_ip, CAPTURE))) != command_list.end()) {
		// remove from command set
		command_list.erase(cmd_iterator);
		log.debug("CAPTURE command removed ok for client %s", client_ip.c_str());
	}

	// check generate command
	while((cmd_iterator = command_list.find(make_pair(client_ip, GENERATE))) != command_list.end()) {
		// check if application data pointer is initialized
		if((*cmd_iterator).second->p != NULL) {
			// TODO tässä pitäisi käyttää user_callbackkiä
			// remove memory allocation
			//delete(static_cast<CAN_MSG*>((*cmd_iterator).second->p));
		}

		// remove from command set
		command_list.erase(cmd_iterator);
		log.debug("GENERATE command removed ok for client %s", client_ip.c_str());
	}

	// check sweep command
	if((cmd_iterator = command_list.find(make_pair(client_ip, SWEEP))) != command_list.end()) {
		// remove from command set
		command_list.erase(cmd_iterator);
		log.debug("SWEEP command removed ok for client %s", client_ip.c_str());
	}

	// unlock command map
	pthread_mutex_unlock(&fsm_mutex);

	return(false);
}

/* Function adds message handling functions
 *
 * Parameters:
 * msg_in: incoming message
 *
 * Returns:
 *
 */

void message_base::handle_msg(auto_ptr<IncomingMsg> msg_in) {
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
			return_params.setString(0, SERVER_HANDLER_MISSING_RESP);
			log.warn("command handler %s not implemented", cmd.c_str());
			// send default reply if call back not found
			msg_in->reply(return_params);
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

/* Function is used to set call back for derived class access
 *
 * Parameters:
 * cmd: pointer to derived class implementing command parameter interface
 *
 * Returns:
 *
 */

void message_base::set_callback(command_parameter_interface *cmd) {
	user_parameter = cmd;
}

/* Function implements command call back for measurement start
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet message_base::cmdMeasurementStart(auto_ptr<ParamSet> params) {
	ParamSet return_params(2);
	message_interface::fsm_command *cmd;

	uuid_t uu;
	char uuid[37];

	// generate unique id for command
	uuid_generate(uu);
	uuid_unparse(uu, uuid);

	// lock command map
	pthread_mutex_lock(&fsm_mutex);

	multimap<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;

	try {
		// get measurement command from message
		uint8_t measure_cmd = params->getByte(1);

		// check active command from this client
		if(measure_cmd == message_interface::CAPTURE) {
			// check if capture is already in list
			if(!(command_list.find(make_pair(source_ip, message_interface::CAPTURE)) == command_list.end())) {
				// unlock command map
				pthread_mutex_unlock(&fsm_mutex);
				// TODO tässä pitäisi kyllä olla ok ja paluu arvo jo löytyy...
				// set fail default response
				return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
				return(return_params);
			}
		}

		// check active command for sweep function
		if(measure_cmd == message_interface::SWEEP) {
			for(cmd_iterator = command_list.begin(); cmd_iterator != command_list.end(); ++cmd_iterator) {
				// find correct command
				if((*cmd_iterator).second->cmd == SWEEP) {
					// unlock command map
					pthread_mutex_unlock(&fsm_mutex);

					// set fail default response
					return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
					return(return_params);
				}
			}
		}


		// set command for measurement
		cmd = new message_interface::fsm_command();
		cmd->cmd = (message_interface::fsm_states)measure_cmd;
		cmd->unique_id = string(uuid);
		cmd->ip = source_ip;
		cmd->channels = params->getByte(2);
		cmd->repeat = !!params->getInt(3);
		cmd->active = !!params->getInt(4);

		// only if repeat is used
		if(params->getParamCount() > 5) {
			// load counter, given in milliseconds. Actual cycle is 10ms so divide by 10
			cmd->repeat_cycle = params->getInt(5) / 10;
			cmd->repeat_timer = params->getInt(5) / 10;
		}

		// initialize default pointer
		cmd->p = NULL;

		log.debug("interval %d", cmd->repeat_cycle);

		// only if six or more parameters are given, use user call back
		if(params->getParamCount() > 6) {
			if(user_parameter != NULL) {
				if(!user_parameter->parameter_callback(params, &cmd->p)) {
					log.error("failed to allocate resources in parameter callback");

					// free allocated resources
					delete(cmd);
					// unlock command map
					pthread_mutex_unlock(&fsm_mutex);

					// set command response
					return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
					return(return_params);
				}
			}
		}

		// channel count not used here yet
		//cmd->ch_cnt = 0;

		// add command to queue
		command_list.insert(pair<pair<string, message_interface::fsm_states>, message_interface::fsm_command*>(make_pair(source_ip, (message_interface::fsm_states)measure_cmd), cmd));

	} catch(exception ex) {
		log.error("parameter parsing failed at %s", ex.what());
	}

	log.debug("command %d insert ok %s id: %s", cmd->cmd, cmd->ip.c_str(), cmd->unique_id.c_str());

	// unlock command map
	pthread_mutex_unlock(&fsm_mutex);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return_params.setString(1, cmd->unique_id.c_str());

	return(return_params);
}

/* Function implements command call back for measurement stop
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet message_base::cmdMeasurementStop(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);
	string id;

	try {
		// get measurement command from message
		uint8_t measure_cmd = params->getByte(1);

		// get command id from message
		params->getString(2, id);
	} catch(exception ex) {
		log.error("parameter parsing failed at %s", ex.what());
		// set command response
		return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
		return(return_params);
	}

	// get iterator
	multimap<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;

	// lock command map
	pthread_mutex_lock(&fsm_mutex);

	// loop all available commands
	for(cmd_iterator = command_list.begin(); cmd_iterator != command_list.end(); ++cmd_iterator) {
		// find correct command
		if((*cmd_iterator).second->unique_id.compare(id) == 0) {
			// check if application data pointer is initialized
			if((*cmd_iterator).second->p != NULL) {
				// remove memory allocation
				if(!user_parameter->parameter_delete(&(*cmd_iterator).second->p)) {
					log.error("failed to remove allocated resources in parameter delete");

					// unlock command map
					pthread_mutex_unlock(&fsm_mutex);

					// set command response
					return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
					return(return_params);
				}
			}

			// remove from command set
			command_list.erase(cmd_iterator);

			log.debug("command %d removed ok %s id: %s",
					(*cmd_iterator).second->cmd,
					(*cmd_iterator).second->ip.c_str(),
					(*cmd_iterator).second->unique_id.c_str());

			//TODO check if cmd is still allocated... memory leak
			break;
		}
	}

	// unlock command map
	pthread_mutex_unlock(&fsm_mutex);
	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for measurement event state change
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet message_base::cmdMeasurementState(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);
	string id;

	// get command id from message
	params->getString(1, id);

	// get iterator
	multimap<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;

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

ParamSet message_base::cmdMeasurementUpdate(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);
	string id;

	// get command id from message
	params->getString(10, id);

	// get iterator
	multimap<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;

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

			// only if six or more parameters are given, use user call back
			if(params->getParamCount() > 6) {
				if(user_parameter != NULL) {
					// TODO pitäisikö olla update funktio
					if(!user_parameter->parameter_callback(params, &(*cmd_iterator).second->p)) {
						log.error("failed to update allocated resources in ???");

						// unlock command map
						pthread_mutex_unlock(&fsm_mutex);

						// set command response
						return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
						return(return_params);
					}
				}
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
