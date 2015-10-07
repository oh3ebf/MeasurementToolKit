/***********************************************************
 * Software: instrument server
 * Module:   intrument frame device manager class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 11.10.2012
 *
 ***********************************************************/

#include <string>

#include <stdint.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>
#include <config4cpp/Configuration.h>

#include "device_manager.h"

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

device_manager::device_manager(Configuration *cfg) : fsm_worker_exit(true), log(Category::getInstance("device")) {

	try {
		// get parameters
		//server_name = cfg->lookupString("server", "name");
		//server_port = cfg->lookupInt("server", "port");
	} catch(const ConfigurationException & ex) {
	    log.error(ex.c_str());
	  }

	/* start worker */
	if(!this->start_internal_thread()) {
		log.error("failed to start device handling thread");
	}
}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

device_manager::~device_manager() {

}

/* Function forces worker thread exit
 *
 * Parameters:
 *
 * Returns:
 *
 */

void device_manager::exit_thread(void) {
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

void device_manager::internal_thread_run(void) {
	log.info("message handling thread started");

	while(fsm_worker_exit) {
		log.debug("run thread");
		//TODO mittalaite
		usleep(10000);
	}

	log.debug("exiting device handling thread");
	this->internal_thread_exit();
}
}
