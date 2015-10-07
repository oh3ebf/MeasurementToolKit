/***********************************************************
 * Software: instrument server
 * Module:   intrument frame can bus manager class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 12.12.2012
 *
 ***********************************************************/

#include <string>

#include <stdint.h>
#include <unistd.h>
#include <sys/select.h>
#include <limits.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>
#include <config4cpp/Configuration.h>

#include "yami++.h"
#include "canmsg.h"
#include "lincan.h"
#include "message_interface.h"
#include "canbus_manager.h"
#include "can_message.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace config4cpp;

/* Function implements constructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

canbus_manager::canbus_manager(Configuration *cfg) : fsm_worker_exit(true), log(Category::getInstance("canbus")) {
	string can_scope = "can";
	StringVector devices;

	// get parameters
	try {
		// get configured devices
		cfg->lookupList(can_scope.c_str(), "devices", devices);

		for(uint8_t i = 0; i < devices.length(); i++) {
			// get one item
			string device = devices[i];

			log.info("found device: %s", device.c_str());

			// get baud rate
			string bus_scope = can_scope + "." + device;
			int baud = cfg->lookupInt(bus_scope.c_str(), "baudrate");
			log.info("bus speed %d", baud);

			// get driver type
			string type = cfg->lookupString(bus_scope.c_str(), "type");

			// install lincan device interfaces
			if(type == "lincan") {
				// create bus object
				lincan *can_bus = new lincan();

				if(can_bus->can_open(device, baud) == 0) {
					// add new bus interface to list
					bus_list.insert(pair<string, can_base *>(device, can_bus));
					// add new message handler
    			msg_list.insert(pair<string, message_interface *>(device, (message_interface *) new can_message(*can_bus)));
				} else {
					log.error("failed to initialize can bus %s", device.c_str());
					return;
				}
			}
		}
	} catch(const ConfigurationException & ex) {
		log.error(ex.c_str());
	}
}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

canbus_manager::~canbus_manager() {
	// get iterator
	map<string, can_base *>::iterator fd_iterator;

	// loop all handlers
	for (fd_iterator = bus_list.begin(); fd_iterator != bus_list.end(); ++fd_iterator) {
		// close devices
		(*fd_iterator).second->can_close();
	}
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

bool canbus_manager::start_worker(data_stream_interface *d) {
	if(d == NULL) {
		log.warn("no data streaming server available");
		return(false);
	}

	// set streaming
	data_stream = d;

	/* start can bus worker */
	if(!this->start_internal_thread()) {
		log.error("failed to start can thread");
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

map<string, message_interface *>& canbus_manager::get_msg_list(void) {
	return(msg_list);
}

/* Function forces worker thread exit
 *
 * Parameters:
 *
 * Returns:
 *
 */

void canbus_manager::exit_thread(void) {
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

void canbus_manager::internal_thread_run(void) {
	//CAN_MSG msg;

	log.info("can message handling thread started");
	// get iterator
	map<string, can_base *>::iterator can_iterator;

	while (fsm_worker_exit) {
		/*
		// loop all handlers
		for (can_iterator = bus_list.begin(); can_iterator != bus_list.end(); ++can_iterator) {
			// get file descriptor
			if((*can_iterator).second->isReady()) {
				(*can_iterator).second->can_read(&msg);
				log.debug("%ld.%06ld: %d %x %x %x %x %x %x %x %x", msg.timestamp.tv_sec, msg.timestamp.tv_usec, msg.id,
						msg.data[0],msg.data[1],msg.data[2],msg.data[3],msg.data[4],msg.data[5],msg.data[6],msg.data[7]);
			}
		}
*/
		// get iterator
		map<string, message_interface *>::iterator dev_iterator;
		// loop all data stream handlers
		for(dev_iterator = msg_list.begin(); dev_iterator != msg_list.end(); ++dev_iterator) {
			// run state machine
			(*dev_iterator).second->fsm(data_stream);
		}

		// 10 ms loop
		usleep(10000);
	}

	log.debug("exiting can message handling thread");
	this->internal_thread_exit();
}
}
