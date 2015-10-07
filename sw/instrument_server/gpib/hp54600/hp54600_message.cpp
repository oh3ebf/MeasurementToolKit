/***********************************************************
 * Software: instrument server
 * Module:   hp54600 oscilloscope message handler class
 * Version:  0.2
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 28.11.2012
 *
 ***********************************************************/

#include <string>
#include <map>

#include <stdint.h>
#include <stdlib.h>
#include <pthread.h>
#include <uuid/uuid.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "yami++.h"
#include "hp54600.h"
#include "hp54600_message.h"
#include "messages.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

/* Function implements constructor for class
 *
 * Parameters:
 * dev: reference to hp54600 device object
 *
 * Returns:
 *
 */

hp54600_message::hp54600_message(hp54600& dev) : device(dev)/*, log(Category::getInstance("message")), state(IDLE)*/ {
  log.debug("hp54600 message handler started: %s", ((gpib)device).get_device_name().c_str());

	if (pthread_mutex_init(&fsm_mutex, NULL) != 0) {
		log.error("mutex init failed");
	}

	// measurement control functions
	command_map.insert(make_pair("MeasurementStart", &hp54600_message::cmdMeasurementStart));
	command_map.insert(make_pair("MeasurementStop",  &hp54600_message::cmdMeasurementStop));
	command_map.insert(make_pair("MeasurementState", &hp54600_message::cmdMeasurementState));
	command_map.insert(make_pair("MeasurementUpdate", &hp54600_message::cmdMeasurementUpdate));

	command_map.insert(make_pair("CheckOnLine", static_cast<message_base::ActionFunction>(&hp54600_message::cmdCheckOnLine)));
  command_map.insert(make_pair("Test", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTest)));
  command_map.insert(make_pair("Reset", static_cast<message_base::ActionFunction>(&hp54600_message::cmdReset)));
  command_map.insert(make_pair("Autoscale", static_cast<message_base::ActionFunction>(&hp54600_message::cmdAutoscale)));
  command_map.insert(make_pair("Run", static_cast<message_base::ActionFunction>(&hp54600_message::cmdRun)));
  command_map.insert(make_pair("Stop", static_cast<message_base::ActionFunction>(&hp54600_message::cmdStop)));

  // channel call backs
  command_map.insert(make_pair("ChannelStatus", static_cast<message_base::ActionFunction>(&hp54600_message::cmdChannelStatus)));
  command_map.insert(make_pair("ChannelStatusRead", static_cast<message_base::ActionFunction>(&hp54600_message::cmdChannelStatusRead)));
  command_map.insert(make_pair("ChannelParametersRead", static_cast<message_base::ActionFunction>(&hp54600_message::cmdChannelParametersRead)));
  command_map.insert(make_pair("ChannelCoupling", static_cast<message_base::ActionFunction>(&hp54600_message::cmdChannelCouplingSet)));
  command_map.insert(make_pair("ChannelBwLimit", static_cast<message_base::ActionFunction>(&hp54600_message::cmdChannelBwLimitSet)));
  command_map.insert(make_pair("ChannelInvert", static_cast<message_base::ActionFunction>(&hp54600_message::cmdChannelInvertSet)));
  command_map.insert(make_pair("ChannelVernier", static_cast<message_base::ActionFunction>(&hp54600_message::cmdChannelVernierSet)));
  command_map.insert(make_pair("ChannelProbe", static_cast<message_base::ActionFunction>(&hp54600_message::cmdChannelProbeSet)));

  // trigger call backs
  command_map.insert(make_pair("TrigMode", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigMode)));
  command_map.insert(make_pair("TrigSource", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigSource)));
  command_map.insert(make_pair("TrigSlope", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigSlope)));
  command_map.insert(make_pair("TrigCouple", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigCouple)));
  command_map.insert(make_pair("TrigReject", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigReject)));
  command_map.insert(make_pair("TrigNoiseReject", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigNoiseReject)));
  command_map.insert(make_pair("TrigPolarity", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigPolarity)));
  command_map.insert(make_pair("TrigTVMode", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigTVMode)));
  command_map.insert(make_pair("TrigTVHFReject", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigTVHFReject)));
  command_map.insert(make_pair("TrigHoldOff", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigHoldOff)));
  command_map.insert(make_pair("TrigLevel", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigLevel)));
  command_map.insert(make_pair("TrigParametersRead", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTrigParametersRead)));

  command_map.insert(make_pair("VoltageScale", static_cast<message_base::ActionFunction>(&hp54600_message::cmdVoltageScale)));
  command_map.insert(make_pair("VoltageOffset", static_cast<message_base::ActionFunction>(&hp54600_message::cmdVoltageOffset)));

  // time base call backs
  command_map.insert(make_pair("TimebaseRange", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTimebaseRange)));
  command_map.insert(make_pair("TimebaseParametersRead", static_cast<message_base::ActionFunction>(&hp54600_message::cmdTimebaseParametersRead)));
  command_map.insert(make_pair("WaveformPreambleRead", static_cast<message_base::ActionFunction>(&hp54600_message::cmdWaveformPreambleRead)));

  // register parameter interface callback
  set_callback(static_cast<command_parameter_interface *>(this));
}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp54600_message::~hp54600_message() {

}

/* Function returns configuration of device
 *
 * Parameters:
 *
 * Returns:
 * map containing device information
 *
 */

map<string, string> *hp54600_message::get_config(void) {
  char buf[20];

  map<string, string> *config = new map<string, string>();
  string name = device.get_gpib_name();

  // convert channel count to string
  sprintf(buf, "%d", device.get_channel_cnt());

  // construct configuration info
  config->insert(pair<string, string>("type", name + ":name:" + device.get_device_name()));
  config->insert(pair<string, string>("ch_count", name + ":ch_count:" + buf));

  return(config);
}

/* Function implements state machine
 *
 * Parameters:
 *
 * Returns:
 *
 */

void hp54600_message::fsm(data_stream_interface *d) {
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
        char *data = NULL;
        int32_t len = 0;

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

        // TODO voiko tässä olla vain yhden kanavan luenta...
        // get current waveform preamble info
        hp54600::waveform_preamble pre = device.get_latest_waveform_parameters(1);
        log.debug("data count to read %d", pre.bytes);

        // get space for data
        data = new char[pre.bytes + 11];

        if(data != NULL) {
          int index = 0;

          // 2 base parameters and number of used channels
          ParamSet params(2 + device.capture((*cmd_iterator).second->channels) * 2);
          log.debug("number of message parameters %d", params.getParamCount());

          // send data back to client
          params.setString(index++, SERVER_DATA_OK_STATUS);
          params.setInt(index++, len);

          // start data capture on scope
          for(int i = 1; i <= device.get_channel_cnt(); i++) {
            // find active channels and read data
            if(((*cmd_iterator).second->channels & (1 << (i - 1)))) {
              // set data source
              device.set_waveform_source(i);

              // read waveform, add ch and data to message
              len = device.get_waveform_data(i, &data);
              params.setInt(index++, i);
              params.setBinary(index++, data, len);
            }
          }

          d->send_data((*cmd_iterator).second->ip, device.get_gpib_name(), params);
          delete(data);
        } else {
          ParamSet params(1);
          // set default response
          params.setString(0, SERVER_DATA_FAIL_STATUS);
          d->send_data((*cmd_iterator).second->ip, device.get_gpib_name(), params);
        }

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

/* Function parse application sweep parameters from message
 *
 * Parameters:
 * p: parameters
 * data: pointer to application specific data
 *
 * Returns:
 * true if ok
 *
 */

bool hp54600_message::parameter_callback(auto_ptr<ParamSet> p, void **data) {
	*data = new scope_struct();

	((scope_struct*) *data)->ch_cnt = p->getByte(2);

	return(true);
}

/* Function frees application allocated resources from message
 *
 * Parameters:
 * p: parameters
 * data: pointer to application specific data
 *
 * Returns:
 * true if ok
 *
 */

bool hp54600_message::parameter_delete(void **data) {
	log.debug("testi");
	if(*data != NULL) {
		delete((scope_struct*) *data);

		return(true);
	}
	log.debug("testi 2");
	return(false);
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

ParamSet hp54600_message::cmdCheckOnLine(auto_ptr<ParamSet> params) {
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

ParamSet hp54600_message::cmdTest(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // run self test
  //device.test();
  // set command response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
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

ParamSet hp54600_message::cmdReset(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // reset device
  device.reset();

  // set command response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for auto scale
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdAutoscale(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // auto scale to device
  device.autoscale();

  // set command response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for run command
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdRun(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // execute run command
  device.run();

  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for stop command
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdStop(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // execute stop command
  device.stop();

  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for channel status command
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdChannelStatus(auto_ptr<ParamSet> params) {
  ParamSet return_params(1);

  uint8_t ch = params->getInt(1);
  bool state = params->getInt(2);

  device.set_channel_state(ch, state);

  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);

  return(return_params);
}

/* Function implements command call back for channel status read command
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdChannelStatusRead(auto_ptr<ParamSet> params) {
  uint8_t state = 0;
  ParamSet return_params(3);
  uint8_t ch_cnt = device.get_channel_cnt();
  uint8_t ch = params->getInt(1);
  uuid_t uu;
	char uuid[37];

	// generate unique id for command
	uuid_generate(uu);
	uuid_unparse(uu, uuid);

	// check valid channel count
  if(ch <= ch_cnt) {
    // set default response
    return_params.setString(0, SERVER_COMMAND_OK_RESP);
    return_params.setString(1, string(uuid));

    // get channel status and add to response message
    if(device.get_channel_state(ch)) {
      state = 1;
    } else {
      state = 0;
    }
    log.debug("state : %d", state);
    return_params.setByte(2, state);
  } else {
	  //TODO failed response missing
  }

  return(return_params);
}

/* Function implements command call back for reading channel parameters
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdChannelParametersRead(auto_ptr<ParamSet> params) {
	ParamSet return_params(9);
	uuid_t uu;
	char uuid[37];
	uint8_t ch_cnt = device.get_channel_cnt();
	uint8_t ch = params->getInt(1);

	// generate unique id for command
	uuid_generate(uu);
	uuid_unparse(uu, uuid);

	// check valid channel count
	if(ch <= ch_cnt) {
		// get current channel parameters
		hp54600::channel_info ch_info = device.get_channel_parameters(ch);

		// set default response
		return_params.setString(0, SERVER_COMMAND_OK_RESP);
		return_params.setString(1, string(uuid));
		return_params.setDouble(2, ch_info.voltage_scale);
		return_params.setDouble(3, ch_info.voltage_offset);
		return_params.setByte(4, ch_info.coupling);
		return_params.setByte(5, ch_info.bwlimit);
		return_params.setByte(6, ch_info.invert);
		return_params.setByte(7, ch_info.vernier);
		return_params.setByte(8, ch_info.probe);
	} else {
		//TODO failed response missing
	}

	return(return_params);
}

/* Function implements command call back for setting channel coupling
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdChannelCouplingSet(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);

	// coupling mode to device
	device.set_channel_coupling(params->getInt(1) + 1, params->getInt(2));

	// set default response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for setting channel band width limit
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdChannelBwLimitSet(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);

	// band width limit to device
	device.set_channel_bwlimit(params->getInt(1) + 1, params->getInt(2));

	// set default response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for setting channel invert
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdChannelInvertSet(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);

	// invert to device
	device.set_channel_invert(params->getInt(1) + 1, params->getInt(2));

	// set default response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for setting channel vernier
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdChannelVernierSet(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);

	// vernier to device
	device.set_channel_vernier(params->getInt(1) + 1, params->getInt(2));

	// set default response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for setting channel probe
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdChannelProbeSet(auto_ptr<ParamSet> params) {
	ParamSet return_params(1);

	// probe select to device
	device.set_channel_probe(params->getInt(1) + 1, params->getInt(2));

	// set default response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for trigger mode selection
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigMode(auto_ptr<ParamSet> params) {
  ParamSet return_params(1);

  // trigger mode to device
  device.set_trigger_mode(params->getInt(1));
  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for trigger source selection
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigSource(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // trigger source to device
  device.set_trigger_source(params->getInt(1));
  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for trigger slope selection
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigSlope(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // trigger slope to device
  device.set_trigger_slope(params->getInt(1));
  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for trigger couple selection
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigCouple(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);

	// trigger coupling to device
	device.set_trigger_couple(params->getInt(1));
	// set default response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for trigger reject selection
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigReject(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // trigger reject to device
  device.set_trigger_reject(params->getInt(1));
  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for trigger noise reject selection
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigNoiseReject(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // trigger noise reject to device
  device.set_trigger_noise_reject(params->getInt(1));
  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for trigger polarity selection
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigPolarity(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // trigger noise reject to device
  device.set_trigger_polarity(params->getInt(1));
  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for trigger TV mode selection
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigTVMode(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // trigger reject to device
  device.set_trigger_tv_mode(params->getInt(1));
  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for trigger TV HF reject selection
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigTVHFReject(auto_ptr<ParamSet> params) {
  string modeStr;
  ParamSet return_params(1);

  // trigger noise reject to device
  device.set_trigger_tv_hf_reject(params->getInt(1));
  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for trigger level
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigLevel(auto_ptr<ParamSet> params) {
  double level;
  ParamSet return_params(1);

  // set new trigger level
  level = params->getDouble(1);
  //log.debug("trigger level %s",level.c_str());
  device.set_trigger_level(level);

  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for trigger hold off setting
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigHoldOff(auto_ptr<ParamSet> params) {
  double level;
  ParamSet return_params(1);

  // set new trigger holf off level
  level = params->getDouble(1);
  device.set_trigger_hold_off(level);

  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for reading trigger parameters
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTrigParametersRead(auto_ptr<ParamSet> params) {
  ParamSet return_params(13);
  uuid_t uu;
	char uuid[37];

	// generate unique id for command
	uuid_generate(uu);
	uuid_unparse(uu, uuid);

  // get current trigger parameters
   hp54600::trigger_info trigger_info = device.get_trigger_parameters();

  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return_params.setString(1, string(uuid));
  return_params.setByte(2, trigger_info.mode);
  return_params.setByte(3, trigger_info.source);
  return_params.setDouble(4, trigger_info.level);
  return_params.setDouble(5, trigger_info.hold_off);
  return_params.setByte(6, trigger_info.slope);
  return_params.setByte(7, trigger_info.coupling);
  return_params.setByte(8, trigger_info.reject);
  return_params.setByte(9, trigger_info.nreject);
  return_params.setByte(10, trigger_info.polarity);
  return_params.setByte(11, trigger_info.tv_mode);
  return_params.setByte(12, trigger_info.tv_hf_reject);

  return(return_params);
}

/* Function implements command call back for trigger level
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdVoltageScale(auto_ptr<ParamSet> params) {
	int ch;
	double scale;
	ParamSet return_params(1);
	uint8_t ch_cnt = device.get_channel_cnt();

	ch = params->getInt(1);
	scale = params->getDouble(2);

	// check valid channel count
	if(ch <= ch_cnt) {
		//log.debug("ch %d voltage scale %e", ch, scale);
		device.set_voltage_scale(ch, scale);

		// set default response
		return_params.setString(0, SERVER_COMMAND_OK_RESP);
	}else {
		// set error response
		return_params.setString(0, SERVER_COMMAND_FAIL_RESP);

	}

	return(return_params);
}

/* Function implements command call back for trigger level
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdVoltageOffset(auto_ptr<ParamSet> params) {
	int ch;
	double offset;
	ParamSet return_params(1);
	uint8_t ch_cnt = device.get_channel_cnt();

	ch = params->getInt(1);
	offset = params->getDouble(2);

	// check valid channel count
	if(ch <= ch_cnt) {
		//log.debug("ch %d voltage offset %e",ch, offset);
		device.set_voltage_offset(ch, offset);

		// set default response
		return_params.setString(0, SERVER_COMMAND_OK_RESP);
	} else {
		// set error response
		return_params.setString(0, SERVER_COMMAND_FAIL_RESP);

	}

	return(return_params);
}

/* Function implements command call back for setting time base
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTimebaseRange(auto_ptr<ParamSet> params) {
  double scale = 0;
  ParamSet return_params(1);

  //set time base value
  device.set_timebase_range(params->getDouble(1));

  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return(return_params);
}

/* Function implements command call back for reading time base parameters
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdTimebaseParametersRead(auto_ptr<ParamSet> params) {
  ParamSet return_params(7);
  uuid_t uu;
	char uuid[37];

	// generate unique id for command
	uuid_generate(uu);
	uuid_unparse(uu, uuid);


  // get current time base info
   hp54600::timebase_info time_info = device.get_timebase_parameters();

  // set default response
  return_params.setString(0, SERVER_COMMAND_OK_RESP);
  return_params.setString(1, string(uuid));
  return_params.setByte(2, time_info.mode);
  return_params.setDouble(3, time_info.timescale);
  return_params.setDouble(4, time_info.delay);
  return_params.setByte(5, time_info.reference);
  return_params.setByte(6, time_info.vernier);

  return(return_params);
}

/* Function implements command call back for reading waveform parameters
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp54600_message::cmdWaveformPreambleRead(auto_ptr<ParamSet> params) {

	uuid_t uu;
	char uuid[37];
	uint8_t ch_cnt = device.get_channel_cnt();
	uint8_t ch = params->getInt(1);

	// generate unique id for command
	uuid_generate(uu);
	uuid_unparse(uu, uuid);

	// check valid channel count
	if(ch <= ch_cnt) {
		ParamSet return_params(11);

		// get current waveform preamble info
		hp54600::waveform_preamble pre = device.get_latest_waveform_parameters(ch);

		// set default response
		return_params.setString(0, SERVER_COMMAND_OK_RESP);
		return_params.setString(1, string(uuid));
		return_params.setInt(2, pre.format);
		return_params.setInt(3, pre.type);
		return_params.setInt(4, pre.bytes);
		return_params.setDouble(5, pre.x_increment);
		return_params.setDouble(6, pre.x_origin);
		return_params.setDouble(7, pre.x_ref);
		return_params.setDouble(8, pre.y_increment);
		return_params.setDouble(9, pre.y_origin);
		return_params.setDouble(10, pre.y_ref);
		return(return_params);
	} else {
		ParamSet return_params(1);
		// set error response
		return_params.setString(0, SERVER_COMMAND_FAIL_RESP);
		return(return_params);
	}
}
}
