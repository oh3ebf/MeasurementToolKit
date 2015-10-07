/***********************************************************
 * Software: instrument server
 * Module:   gpib manager class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 25.9.2012
 *
 ***********************************************************/

#include <string>
#include <map>

#include <stdint.h>
#include <unistd.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>
#include <config4cpp/Configuration.h>

#include "yami++.h"
#include "message_interface.h"
#include "gpib_manager.h"
#include "hp3488.h"
#include "hp54600.h"
#include "e4421b.h"
#include "hp8922.h"
#include "keithley2015.h"
#include "hp3488_message.h"
#include "hp54600_message.h"
#include "e4421b_message.h"
#include "keithley2015_message.h"
#include "hp8922_message.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace config4cpp;
using namespace YAMI;

/* Function implements constructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

gpib_manager::gpib_manager(Configuration *cfg) : fsm_worker_exit(true), log(Category::getInstance("gpib")) {
  char* gpib_dev = NULL;
  string gpib_scope = "gpib";
  string gpib_name = "/dev/gpib";
  StringVector devices;

  log.debug("gpib manager started");

  try {
	  // get interface and make full device name
	  gpib_name = cfg->lookupString(gpib_scope.c_str(), "interface_name");
	  gpib_dev = (char *)cfg->lookupString(gpib_scope.c_str(), "interface_0");
	  gpib_name += gpib_dev;
  } catch(const ConfigurationException & ex) {
	  log.error(ex.c_str());
	  return;
  }

  log.debug("trying to use device %s", gpib_name.c_str());

#ifndef GPIB_TEST
  // first check existence of gpib device
  if(access(gpib_name.c_str(), F_OK|R_OK) < 0) {
    log.error("no gpib%d device descriptor found",0);
    return;
  }
#endif

  try {
      // get configured devices
      cfg->lookupList(gpib_scope.c_str(), "devices", devices);

      for(uint8_t i = 0; i < devices.length(); i++) {
    	  // get one item
    	  string device = devices[i];

    	  log.info("found device: %s", device.c_str());

    	  // get device specific configuration
    	  string device_scope = gpib_scope + "." + device;
    	  string device_name = cfg->lookupString(device_scope.c_str(),"name");

    	  // check for hp3488 switching control unit
    	  if(device_name == "hp3488") {
    		  hp3488 *swcu = new hp3488(device);
    		  // check if device is active on bus
    		  if(swcu->isConnected()) {
    			  // add new device to list
    			  device_list.insert(pair<string, gpib *>(device, (gpib *)swcu));
    			  // add new message handler
    			  msg_list.insert(pair<string, message_interface *>(device, (message_interface *)new hp3488_message(*swcu)));
    		  }
    	  }

    	  // check for hp54600 oscilloscope
    	  if(device_name == "hp54600") {
    		  hp54600 *oscope = new hp54600(device, device_name);

    		  // check if device is active on bus
    		  if(oscope->isConnected()) {
    			  // add new device to list
    			  device_list.insert(pair<string, gpib *>(device, (gpib *)oscope));
    			  // add new message handler
    			  msg_list.insert(pair<string, message_interface *>(device, (message_interface *)new hp54600_message(*oscope)));
    		  }
    	  }

    	  // check for e4421b rf generator
    	  if(device_name == "e4421b") {
    		  e4421b *rf_gen = new e4421b(device);
    		  // check if device is active on bus
    		  if(rf_gen->isConnected()) {
    			  // add new device to list
    			  device_list.insert(pair<string, gpib *>(device, (gpib *)rf_gen));
    			  // add new message handler
    			  msg_list.insert(pair<string, message_interface *>(device, (message_interface *) new e4421b_message(*rf_gen)));
    		  }
    	  }

    	  // check for keithley 2015 multimeter
    	  if(device_name == "k2015l") {
    		  keithley2015 *multimeter = new keithley2015(device);
    		  // check if device is active on bus
    		  if(multimeter->isConnected()) {
    			  // add new device to list
    			  device_list.insert(pair<string, gpib *>(device, (gpib *)multimeter));
    			  // add new message handler
    			  msg_list.insert(pair<string, message_interface *>(device, (message_interface *) new keithley2015_message(*multimeter)));
    		  }
    	  }

    	  // check for hp8922 gsm test set
    	  if(device_name == "hp8922") {
    		  hp8922 *gsm = new hp8922(device);
    		  // check if device is active on bus
    		  if(gsm->isConnected()) {
    			  // add new device to list
    			  device_list.insert(pair<string, gpib *>(device, (gpib *)gsm));
    			  // add new message handler
    			  msg_list.insert(pair<string, message_interface *>(device, (message_interface *) new hp8922_message(*gsm)));
    		  }
    	  }
      }
    } catch(const ConfigurationException & ex) {
      log.error(ex.c_str());
      return;
    }

}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

gpib_manager::~gpib_manager() {
// TODO pitäisi purkaa muistivaraukset
}

/* Function starts internal thread and sets streaming service
 *
 * Parameters:
 * d: data streaming service
 *
 * Returns:
 * true on ok, otherwise false
 *
 */

bool gpib_manager::start_worker(data_stream_interface *d) {
	if(d == NULL) {
		log.warn("no data streaming server available");
		return(false);
	}

	// set streaming
	data_stream = d;

	/* start gpib worker */
	if(!this->start_internal_thread()) {
		log.error("failed to start gpib thread");
		return(false);
	}

	return(true);
}

/* Function returns reference to message handlers in a map
 *
 * Parameters:
 *
 * Returns:
 * reference to message handler map
 *
 */

map<string, message_interface *>& gpib_manager::get_msg_list(void) {
	return(msg_list);
}

/* Function forces worker thread exit
 *
 * Parameters:
 *
 * Returns:
 *
 */

void gpib_manager::exit_thread() {
  fsm_worker_exit = false;
  this->wait_internal_thread_exit();
}

/* Function implements thread worker
 *
 * Parameters:
 *
 * Returns:
 *
 */

void gpib_manager::internal_thread_run(void) {
  log.info("gpib handling thread started");

  while(fsm_worker_exit) {
	  // get iterator
	  map<string, message_interface *>::iterator dev_iterator;
	  // loop all data stream handlers
	  for(dev_iterator = msg_list.begin(); dev_iterator != msg_list.end(); ++dev_iterator) {
		  // run state machine
		  (*dev_iterator).second->fsm(data_stream);
	  }
	  // TODO serial poll tänne
	// 10 ms loop
    usleep(10000);
  }

  log.debug("exiting gpib handling thread");
  this->internal_thread_exit();
}

}
