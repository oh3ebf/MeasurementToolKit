/***********************************************************
 * Software: instrument server
 * Module:   HP 8922 GSM test set message handler class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 27.8.2013
 *
 ***********************************************************/

#include <string>
#include <map>

#include <stdint.h>
#include <uuid/uuid.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "yami++.h"
#include "hp8922.h"
#include "hp8922_message.h"
#include "messages.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

/* Function implements constructor for class
 *
 * Parameters:
 * dev: reference to hp8922 device object
 *
 * Returns:
 *
 */

hp8922_message::hp8922_message(hp8922& dev) : device(dev)/*, log(Category::getInstance("message"))*/ {
	log.debug("hp8922 message handler started: %s", ((gpib)device).get_device_name().c_str());

	if (pthread_mutex_init(&fsm_mutex, NULL) != 0) {
		log.error("mutex init failed");
	}


	command_map.insert(make_pair("MeasurementStart", &hp8922_message::cmdMeasurementStart));
	command_map.insert(make_pair("MeasurementStop",  &hp8922_message::cmdMeasurementStop));
	command_map.insert(make_pair("MeasurementState", &hp8922_message::cmdMeasurementState));
	command_map.insert(make_pair("MeasurementUpdate", &hp8922_message::cmdMeasurementUpdate));

	command_map.insert(make_pair("CheckOnLine", static_cast<message_base::ActionFunction>(&hp8922_message::cmdCheckOnLine)));
	command_map.insert(make_pair("Test", static_cast<message_base::ActionFunction>(&hp8922_message::cmdTest)));
	command_map.insert(make_pair("Reset", static_cast<message_base::ActionFunction>(&hp8922_message::cmdReset)));
	//command_map.insert(make_pair("MeasurementStop",  static_cast<message_base::ActionFunction>(&hp8922_message::cmdMeasurementStop)));
}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp8922_message::~hp8922_message() {

}

/* Function returns configuration of device
 *
 * Parameters:
 *
 * Returns:
 * map containing device information
 *
 */

map<string, string> *hp8922_message::get_config(void) {
	map<string, string> *config = new map<string, string>();
	string name = device.get_gpib_name();

	// construct configuration info
	config->insert(pair<string, string>("type", name + ":name:" + ((gpib)device).get_device_name()));

	return(config);
}



/* Function implements state machine
 *
 * Parameters:
 *
 * Returns:
 *
 */

void hp8922_message::fsm(data_stream_interface *d) {
	double *data = NULL;

	// any commands available
	if(!command_list.empty()) {
		// lock command map
		pthread_mutex_lock(&fsm_mutex);

		// get iterator
		map<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;

		// loop all available commands
		for(cmd_iterator = command_list.begin(); cmd_iterator != command_list.end(); ++cmd_iterator) {
			log.debug("data send %s",(*cmd_iterator).second->unique_id.c_str());
			if((*cmd_iterator).second->active == true) {
				// select command
				switch((*cmd_iterator).second->cmd) {
				case CAPTURE:
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

					// get space for data
					data = new double[417];

					if(data != NULL) {
						// 1 base parameter + data buffers
						ParamSet params(418);

						// send data back to client
						params.setString(0, SERVER_DATA_OK_STATUS);
						// read trace from device
						device.read_trace(&data);

						// serialize data
						for(int i = 0; i < 417;i++) {
							params.setDouble(i + 1, data[i]);
						}

						d->send_data((*cmd_iterator).second->ip, device.get_gpib_name(), params);
						delete(data);
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
					//if((*cmd_iterator).second->p != NULL) {
					//log.debug("can data generation %s", (*cmd_iterator).second->unique_id.c_str());
					//device.can_send((static_cast<CAN_MSG*>((*cmd_iterator).second->p)));
					//}
					break;
				default:
					break;
				}
				log.debug("tila %s", ((*cmd_iterator).second->repeat)?"true":"false");
				// remove command from list if single shot
				if((*cmd_iterator).second->repeat == false) {
					log.debug("remove single shot command %s", (*cmd_iterator).second->unique_id.c_str());
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

ParamSet hp8922_message::cmdCheckOnLine(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // is device found on bus
  if(device.isConnected()) {
    // set command response
    return_params.setString(0, SERVER_COMMAND_OK_RESP);
  } else {
    // set fail command response
    return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
  }

  return(return_params);
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

ParamSet hp8922_message::cmdTest(auto_ptr<ParamSet> params) {
	//string modeStr;
	bool result = false;
	ParamSet return_params(1);

	// run self test
	device.gpib_488_2::self_test_query();

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	// convert boolean to byte
	if(result) {
		return_params.setByte(1, 0x01);
	} else {
		return_params.setByte(1, 0x00);
	}

	return(return_params);
}

/* Function implements command call back for reset
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp8922_message::cmdReset(auto_ptr<ParamSet> params) {
	//string modeStr;
	ParamSet return_params(1);

	// reset device
	device.gpib_488_2::reset();

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for display selection
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp8922_message::cmdDisplaySelect(auto_ptr<ParamSet> params) {
	//string modeStr;
	ParamSet return_params(1);

	// set selected display
	if(device.select_display((hp8922::display_mode)params->getInt(1))) {
		// set command response
		return_params.setString(0, SERVER_COMMAND_OK_RESP);
	} else {
		// set command failed response
		return_params.setString(0, SERVER_COMMAND_FAIL_RESP);

	}

	return(return_params);
}

}
