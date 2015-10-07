/***********************************************************
 * Software: instrument server
 * Module:   Keithley 2015 multimeter message handler class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 1.4.2013
 *
 ***********************************************************/

#include <string>
#include <map>

#include <stdint.h>
#include <pthread.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "yami++.h"
#include "message_base.h"
#include "message_interface.h"
#include "keithley2015.h"
#include "keithley2015_message.h"
#include "messages.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

/* Function implements constructor for class
 *
 * Parameters:
 * dev: reference to keithley2015 device object
 *
 * Returns:
 *
 */

keithley2015_message::keithley2015_message(keithley2015& dev) : device(dev)/*, log(Category::getInstance("message"))*/ {
  log.debug("keithley2015 message handler started: %s", ((gpib)device).get_device_name().c_str());

  if (pthread_mutex_init(&fsm_mutex, NULL) != 0) {
    log.error("mutex init failed");
  }

  // measurement control functions
  command_map.insert(make_pair("MeasurementStart", &keithley2015_message::cmdMeasurementStart));
  command_map.insert(make_pair("MeasurementStop",  &keithley2015_message::cmdMeasurementStop));
  command_map.insert(make_pair("MeasurementState", &keithley2015_message::cmdMeasurementState));
  command_map.insert(make_pair("MeasurementUpdate", &keithley2015_message::cmdMeasurementUpdate));

  command_map.insert(make_pair("CheckOnLine", static_cast<message_base::ActionFunction>(&keithley2015_message::cmdCheckOnLine)));
  command_map.insert(make_pair("Test", static_cast<message_base::ActionFunction>(&keithley2015_message::cmdTest)));
  command_map.insert(make_pair("Reset", static_cast<message_base::ActionFunction>(&keithley2015_message::cmdReset)));
  command_map.insert(make_pair("Mode", static_cast<message_base::ActionFunction>(&keithley2015_message::cmdSetMode)));
  command_map.insert(make_pair("Read", static_cast<message_base::ActionFunction>(&keithley2015_message::cmdReadValue)));

}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

keithley2015_message::~keithley2015_message() {

}

/* Function returns configuration of device
 *
 * Parameters:
 *
 * Returns:
 * map containing device information
 *
 */

map<string, string> *keithley2015_message::get_config(void) {
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

void keithley2015_message::fsm(data_stream_interface *d) {
  double value = 0.0F;

  // any commands available
  if(!command_list.empty()) {
    // lock command map
    pthread_mutex_lock(&fsm_mutex);

    // get iterator
    map<pair<string, fsm_states>, fsm_command *>::iterator cmd_iterator;
    // loop all available commands
    for(cmd_iterator = command_list.begin(); cmd_iterator != command_list.end(); ++cmd_iterator) {
      // select command
      switch((*cmd_iterator).second->cmd) {
      case CAPTURE: {
        //char *data = NULL;
        //int32_t len = 0;

        if((*cmd_iterator).second->repeat) {
          // decrement repeat counter
          (*cmd_iterator).second->repeat_timer--;
          if((*cmd_iterator).second->repeat_timer == 0) {
            // load new value and execute capture code
            (*cmd_iterator).second->repeat_timer = (*cmd_iterator).second->repeat_cycle;
          } else {
            // unlock command map
            pthread_mutex_unlock(&fsm_mutex);
            // not yet time to get data
            return;
          }
        }

        // get measurement reading from device
        value = device.get_value();
        // 2 base parameters
        ParamSet params(2);
        log.debug("number of message parameters %d", params.getParamCount());

        // send data back to client
        params.setString(0, SERVER_DATA_OK_STATUS);
        params.setDouble(1, value);
        d->send_data((*cmd_iterator).second->ip, device.get_gpib_name(), params);

        /*} else {
	          ParamSet params(1);
	          // set default response
	          params.setString(0, SERVER_DATA_FAIL_STATUS);
	          d->send_data((*cmd_iterator).second->ip, device.get_gpib_name(), params);
	        }*/

        // remove command from list
        if((*cmd_iterator).second->repeat == false) {
          command_list.erase(cmd_iterator);
        }
      }
      break;
      default:
        break;
      }
    }

    // unlock command map
    pthread_mutex_unlock(&fsm_mutex);
  }
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

ParamSet keithley2015_message::cmdCheckOnLine(auto_ptr<ParamSet> params) {
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

ParamSet keithley2015_message::cmdTest(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // is device found on bus
  if(device.isConnected()) {
    // run self test
    device.test();
    // set command response
    return_params.setString(0, SERVER_COMMAND_OK_RESP);
  } else {
    // set fail command response
    return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
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

ParamSet keithley2015_message::cmdReset(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // is device found on bus
  if(device.isConnected()) {
    // reset device
    device.reset();
    // set command response
    return_params.setString(0, SERVER_COMMAND_OK_RESP);
  } else {
    // set fail command response
    return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
  }

  return(return_params);
}

/* Function implements command call back for mode set
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet keithley2015_message::cmdSetMode(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  uint8_t mode = params->getInt(1);

  // is device found on bus
  if(device.isConnected()) {
    // set measurement mode
    device.set_mode(mode);

    // set command response
    return_params.setString(0, SERVER_COMMAND_OK_RESP);
  } else {
    // set fail command response
    return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
  }

  return(return_params);
}

/* Function implements command call back for read operation
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet keithley2015_message::cmdReadValue(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(2);
  double value = 0.0F;

  // make one measurement
  value = device.get_value();
  // is device found on bus
  if(device.isConnected()) {
    // set command response
    return_params.setString(0, SERVER_COMMAND_OK_RESP);
    return_params.setDouble(1, value);
  } else {
    // set fail command response
    return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
  }
  return(return_params);
}

}
