/***********************************************************
 * Software: instrument server
 * Module:   hp3488 switching control unit message handler class
 * Version:  0.2
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 15.10.2012
 *
 ***********************************************************/

#include <string>
#include <map>

#include <stdint.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "yami++.h"
#include "hp3488.h"
#include "hp3488_message.h"
#include "messages.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

/* Function implements constructor for class
 *
 * Parameters:
 * dev: reference to hp3488 device object
 *
 * Returns:
 *
 */

hp3488_message::hp3488_message(hp3488& dev) : device(dev), log(Category::getInstance("message")) {
	log.debug("hp3488 message handler started: %s", ((gpib)device).get_device_name().c_str());

	command_map.insert(make_pair("CheckOnLine", &hp3488_message::cmdCheckOnLine));
	command_map.insert(make_pair("Test", &hp3488_message::cmdTest));
	command_map.insert(make_pair("Reset", &hp3488_message::cmdReset));
	command_map.insert(make_pair("Open", &hp3488_message::cmdOpen));
	command_map.insert(make_pair("Close", &hp3488_message::cmdClose));
}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp3488_message::~hp3488_message() {

}

/* Function adds message handling functions
 *
 * Parameters:
 * msg_in: incoming message
 *
 * Returns:
 *
 */

void hp3488_message::handle_msg(auto_ptr<IncomingMsg> msg_in) {
	string cmd;
	auto_ptr<ParamSet> params;
	ParamSet return_params(1);

	string ip = msg_in->getSourceAddr();

	// set default response
	return_params.setString(0, SERVER_COMMAND_FAIL_RESP);

	try {
		// get parameters
		params = msg_in->getParameters();
		// get device name
		params->getString(0, cmd);

		// try to find command call back function
		if(command_map.find(cmd) == command_map.end()) {
			// not found
			ParamSet return_params(1);
			log.warn("command handler %s not implemented", cmd.c_str());
		} else {
			// execute function
			return_params = (*this.*command_map[cmd])(params);
		}

		// TODO paluu viestissä pitää olla error ja status mukana

		// send default reply if command not found
		msg_in->reply(return_params);
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

map<string, string> *hp3488_message::get_config(void) {
	map<string, string> *config = new map<string, string>();
	string name = device.get_gpib_name();

	// construct configuration info
	config->insert(pair<string, string>("type", name + ":name:" + ((gpib)device).get_device_name()));
	config->insert(pair<string, string>("slot1", name + ":slot1:" + device.get_card(0).getName()));
	config->insert(pair<string, string>("slot2", name + ":slot2:" + device.get_card(1).getName()));
	config->insert(pair<string, string>("slot3", name + ":slot3:" + device.get_card(2).getName()));
	config->insert(pair<string, string>("slot4", name + ":slot4:" + device.get_card(3).getName()));
	config->insert(pair<string, string>("slot5", name + ":slot5:" + device.get_card(4).getName()));

	return(config);
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

ParamSet hp3488_message::cmdCheckOnLine(auto_ptr<ParamSet> params) {
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

ParamSet hp3488_message::cmdTest(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);

	// run self test
	device.test();
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

ParamSet hp3488_message::cmdReset(auto_ptr<ParamSet> params) {
	string modeStr;
	ParamSet return_params(1);

	// reset device
	device.reset();
	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back channel open
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp3488_message::cmdOpen(auto_ptr<ParamSet> params) {
	string ch;
	ParamSet return_params(1);

	// send open command to device
	params->getString(1, ch);
	device.open_ch(ch);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}

/* Function implements command call back channel open
 *
 * Parameters:
 * params: message parameters
 *
 * Returns:
 * command response parameter set
 *
 */

ParamSet hp3488_message::cmdClose(auto_ptr<ParamSet> params) {
	string ch;
	ParamSet return_params(1);

	// send close command to device
	params->getString(1, ch);
	device.close_ch(ch);

	// set command response
	return_params.setString(0, SERVER_COMMAND_OK_RESP);
	return(return_params);
}
}
