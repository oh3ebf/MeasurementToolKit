/***********************************************************
 * Software: instrument server
 * Module:   Agilent E4421B RF generator message handler class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 31.1.2013
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
#include "e4421b.h"
#include "e4421b_message.h"
#include "messages.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

/* Function implements constructor for class
 *
 * Parameters:
 * dev: reference to e4421b device object
 *
 * Returns:
 *
 */

e4421b_message::e4421b_message(e4421b& dev) : device(dev)/*, log(Category::getInstance("message"))*/ {
	log.debug("e4421b message handler started: %s", ((gpib)device).get_device_name().c_str());

	if (pthread_mutex_init(&fsm_mutex, NULL) != 0) {
		log.error("mutex init failed");
	}

	command_map.insert(make_pair("MeasurementStart", &e4421b_message::cmdMeasurementStart));
	command_map.insert(make_pair("MeasurementStop",  &e4421b_message::cmdMeasurementStop));
	command_map.insert(make_pair("MeasurementState", &e4421b_message::cmdMeasurementState));
	command_map.insert(make_pair("MeasurementUpdate", &e4421b_message::cmdMeasurementUpdate));

	command_map.insert(make_pair("CheckOnLine", static_cast<message_base::ActionFunction>(&e4421b_message::cmdCheckOnLine)));
	command_map.insert(make_pair("Test", static_cast<message_base::ActionFunction>(&e4421b_message::cmdTest)));
	command_map.insert(make_pair("Reset", static_cast<message_base::ActionFunction>(&e4421b_message::cmdReset)));
	command_map.insert(make_pair("Frequency", static_cast<message_base::ActionFunction>(&e4421b_message::cmdFrequency)));
	command_map.insert(make_pair("ReadFrequency", static_cast<message_base::ActionFunction>(&e4421b_message::cmdReadFrequency)));
	command_map.insert(make_pair("Amplitude", static_cast<message_base::ActionFunction>(&e4421b_message::cmdAmplitude)));
	command_map.insert(make_pair("ReadAmplitude", static_cast<message_base::ActionFunction>(&e4421b_message::cmdReadAmplitude)));
	command_map.insert(make_pair("Output", static_cast<message_base::ActionFunction>(&e4421b_message::cmdOutputState)));
	command_map.insert(make_pair("Modulation", static_cast<message_base::ActionFunction>(&e4421b_message::cmdModulationOutputState)));
	command_map.insert(make_pair("ModulationWaveform", static_cast<message_base::ActionFunction>(&e4421b_message::cmdModulationWaveform)));
	command_map.insert(make_pair("ModulationSource", static_cast<message_base::ActionFunction>(&e4421b_message::cmdModulationSource)));
	command_map.insert(make_pair("ModulationValue", static_cast<message_base::ActionFunction>(&e4421b_message::cmdModulationValue)));
	command_map.insert(make_pair("ModulationRate", static_cast<message_base::ActionFunction>(&e4421b_message::cmdModulationRate)));

	set_callback(static_cast<command_parameter_interface *>(this));
}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

e4421b_message::~e4421b_message() {

}

/* Function returns configuration of device
 *
 * Parameters:
 *
 * Returns:
 * map containing device information
 *
 */

map<string, string> *e4421b_message::get_config(void) {
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

void e4421b_message::fsm(data_stream_interface *d) {
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
			case SWEEP: {
				if((*cmd_iterator).second->active) {

					// handle repeat condition
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

					// select sweep to run
					switch(mode) {
					case FREQUENCY:
						frequency_sweep();
						break;
					case FREQUENCY_NESTED_AMPLITUDE:
						frequency_nested_amplitude_sweep();
						break;
					case AMPLITUDE:
						amplitude_sweep();
						break;
					case AMPLITUDE_NESTED_FREQUENCY:
						amplitude_nested_frequency_sweep();
						break;
					default:
						log.error("sweep %d does'n exist in configuration", mode);
					}
				}

				// get measurement reading from device
				//value = device.get_value();
				// 2 base parameters
				//ParamSet params(2);
				//log.debug("number of message parameters %d", params.getParamCount());

				// send data back to client
				//params.setString(0, SERVER_DATA_OK_STATUS);
				//params.setDouble(1, value);
				//d->send_data((*cmd_iterator).second->ip, device.get_gpib_name(), params);

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

bool e4421b_message::parameter_callback(auto_ptr<ParamSet> p, void *data) {
	try {
	// sweep type
	mode = (sweep_modes)p->getByte(6);

	switch(mode) {
	case AMPLITUDE:
		// amplitude parameters
		amplitude.start = p->getDouble(7);
		amplitude.end = p->getDouble(8);
		amplitude.step = p->getDouble(9);
		amplitude.value = amplitude.start;
		break;
	case FREQUENCY:
		// frequency parameters
		frequency.start = p->getDouble(7);
		frequency.end = p->getDouble(8);
		frequency.step = p->getDouble(9);
		frequency.value = frequency.start;
		break;
	case AMPLITUDE_NESTED_FREQUENCY:
		// frequency parameters as main sweep
		frequency.start = p->getDouble(7);
		frequency.end = p->getDouble(8);
		frequency.step = p->getDouble(9);
		frequency.value = frequency.start;

		// amplitude parameters as nested sweep
		amplitude.start = p->getDouble(10);
		amplitude.end = p->getDouble(11);
		amplitude.step = p->getDouble(12);
		amplitude.value = amplitude.start;
		break;
	case FREQUENCY_NESTED_AMPLITUDE:
		// amplitude parameters as main sweep
		amplitude.start = p->getDouble(7);
		amplitude.end = p->getDouble(8);
		amplitude.step = p->getDouble(9);
		amplitude.value = amplitude.start;

		// frequency parameters as nested sweep
		frequency.start = p->getDouble(10);
		frequency.end = p->getDouble(11);
		frequency.step = p->getDouble(12);
		frequency.value = frequency.start;
		break;
	case STOP:
	default:
		break;
	}
	} catch(exception ex) {
		log.error("failed to parse sweep parameters in user call back");
	}
	/*


	if(frequency.start >= frequency.end) {
		log.warn("not valid frequency start value %lf", frequency.start);
		return(false);
	}

	if(frequency.step <= 0) {
		log.warn("not valid frequency step value %lf", frequency.step);
		return(false);
	}
*/

	/*
	if(amplitude.start >= amplitude.end) {
		log.warn("not valid amplitude start value %lf", amplitude.start);
		return(false);
	}

	if(frequency.step <= 0) {
		log.warn("not valid amplitude step value %lf", amplitude.step);
		return(false);
	}
*/
	return(true);
}

/* Function runs amplitude sweep
 *
 * Parameters:
 *
 * Returns:
 *
 */

void e4421b_message::amplitude_sweep(void) {
	// sweep configured if step value is set
	if(amplitude.step > 0) {
		// set new value to device
		device.set_amplitude(amplitude.value);
		// get next value
		amplitude.value += amplitude.step;

		// check value range
		if(amplitude.value > amplitude.end) {
			amplitude.value = amplitude.start;
		}
	}
}

/* Function runs amplitude sweep with nested frequency sweep
 *
 * Parameters:
 *
 * Returns:
 *
 */

void e4421b_message::amplitude_nested_frequency_sweep(void) {
	// sweep configured if step value is set
	if((amplitude.step > 0) && frequency.step > 0) {
		// set new nested sweep value to device
		device.set_amplitude(amplitude.value);
		// get next value
		amplitude.value += amplitude.step;

		// check value range
		if(amplitude.value > amplitude.end) {
			amplitude.value = amplitude.start;

			// set new main sweep value
			device.set_frequency(frequency.value);

			frequency.value += frequency.step;
			// check value range
			if(frequency.value > frequency.end) {
				frequency.value = frequency.start;
			}
		}
	}
}

/* Function runs frequency sweep
 *
 * Parameters:
 *
 * Returns:
 *
 */

void e4421b_message::frequency_sweep(void) {
	// sweep configured if step value is set
	if(frequency.step > 0) {
		// set new value to device
		device.set_frequency(frequency.value);
		// get next value
		frequency.value += frequency.step;

		// check value range
		if(frequency.value > frequency.end) {
			frequency.value = frequency.start;
		}
	}
}

/* Function runs frequency sweep with nested amplitude sweep
 *
 * Parameters:
 *
 * Returns:
 *
 */

void e4421b_message::frequency_nested_amplitude_sweep(void) {
	// sweep configured if step value is set
	if((amplitude.step > 0) && frequency.step > 0) {
		// set new nested sweep value to device
		device.set_frequency(frequency.value);
		// get next value
		frequency.value += frequency.step;

		// check value range
		if(frequency.value > frequency.end) {
			frequency.value = frequency.start;

			// set new main sweep value
			device.set_amplitude(amplitude.value);

			amplitude.value += amplitude.step;
			// check value range
			if(amplitude.value > amplitude.end) {
				amplitude.value = amplitude.start;
			}
		}
	}
}

/* Function clear application parameters
 *
 * Parameters:
 * data: pointer to application specific data
 *
 * Returns:
 * true if ok
 *
 */

bool e4421b_message::parameter_delete(void *data) {
	// not used in this device
	return(true);
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

ParamSet e4421b_message::cmdCheckOnLine(auto_ptr<ParamSet> params) {
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

ParamSet e4421b_message::cmdTest(auto_ptr<ParamSet> params) {
	string modeStr;
	bool result = false;
	ParamSet return_params(2);

	// run self test
	result = device.gpib_488_2::self_test_query();

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

ParamSet e4421b_message::cmdReset(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);

	// reset device
	device.gpib_488_2::reset();

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for frequency setting
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet e4421b_message::cmdFrequency(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);
	double freq = params->getDouble(1);

	// set new frequency value
	device.set_frequency(freq);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for frequency reading
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet e4421b_message::cmdReadFrequency(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(3);
	uuid_t uu;
	char uuid[37];

	// generate unique id for command
	uuid_generate(uu);
	uuid_unparse(uu, uuid);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return_params.setString(1, string(uuid));
	return_params.setInt(2, device.get_frequency());

	return(return_params);
}

/* Function implements command call back for amplitude setting
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet e4421b_message::cmdAmplitude(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);
	double ampl = params->getDouble(1);

	// set new amplitude value
	device.set_amplitude(ampl);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for amplitude reading
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet e4421b_message::cmdReadAmplitude(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(3);
	uuid_t uu;
	char uuid[37];

	// generate unique id for command
	uuid_generate(uu);
	uuid_unparse(uu, uuid);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return_params.setString(1, string(uuid));
	return_params.setDouble(2, device.get_amplitude());

	return(return_params);
}

/* Function implements command call back for output state setting
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet e4421b_message::cmdOutputState(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);
	bool state = !!params->getInt(1);

	// set new amplitude value
	device.set_rf_output_state(state);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for modulation state setting
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet e4421b_message::cmdModulationOutputState(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);
	bool state = !!params->getInt(1);

	// set new amplitude value
	device.set_modulation_output_state(state);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for modulation state setting
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet e4421b_message::cmdModulationState(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);
	bool state = !!params->getInt(1);

	// set new modulation mode
	device.set_modulation_output_state(state);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back for modulation waveform setting
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet e4421b_message::cmdModulationWaveform(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);
	e4421b::modulation mod = (e4421b::modulation)params->getByte(1);
	uint8_t path = params->getByte(2);
	e4421b::modulation_waveform waveform = (e4421b::modulation_waveform)params->getByte(3);

	// set new modulation mode
	device.set_internal_modulation_waveform(mod, path, waveform);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);

	return(return_params);
}

/* Function implements command call back for modulation waveform setting
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet e4421b_message::cmdModulationSource(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);
	e4421b::modulation mod = (e4421b::modulation)params->getByte(1);
	uint8_t path = params->getByte(2);
	uint8_t source = params->getByte(3);

	switch(source) {
	case 0:
		// set new modulation mode
		device.set_modulation_source(mod, path, e4421b::SOURCE_INTERNAL);
		break;
	case 1:
		// set new modulation mode and coupling
		device.set_modulation_source(mod, path, e4421b::SOURCE_EXTERNAL_1);
		device.set_modulation_coupling(mod, path, 1, false);
		break;
	case 2:
		// set new modulation mode and coupling
		device.set_modulation_source(mod, path, e4421b::SOURCE_EXTERNAL_1);
		device.set_modulation_coupling(mod, path, 1, true);
		break;
	case 3:
		// set new modulation mode and coupling
		device.set_modulation_source(mod, path, e4421b::SOURCE_EXTERNAL_2);
		device.set_modulation_coupling(mod, path, 2, false);
		break;
	case 4:
		// set new modulation mode and coupling
		device.set_modulation_source(mod, path, e4421b::SOURCE_EXTERNAL_2);
		device.set_modulation_coupling(mod, path, 2, true);
		break;
	}

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);

	return(return_params);
}

/* Function implements command call back for modulation depth / setting
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet e4421b_message::cmdModulationValue(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);
	e4421b::modulation mod = (e4421b::modulation)params->getByte(1);
	uint8_t path = params->getByte(2);
	double value = params->getDouble(3);

	switch(mod) {
	case e4421b::AM:
		// set new modulation depth
		device.set_am_modulation_depth(path, value);
		break;
	case e4421b::FM:
	case e4421b::PM:
		// set new modulation deviation
		device.set_modulation_deviation(mod, path, value);
		break;
	}

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);

	return(return_params);
}

/* Function implements command call back for modulation rate
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet e4421b_message::cmdModulationRate(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);
	e4421b::modulation mod = (e4421b::modulation)params->getByte(1);
	uint8_t path = params->getByte(2);
	double value = params->getDouble(3);

	// set new modulation deviation
	device.set_internal_modulation_source_rate(mod, path, value);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);

	return(return_params);
}
}
