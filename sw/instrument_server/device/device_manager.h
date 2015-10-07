/***********************************************************
 * Software: instrument server
 * Module:   intrument frame device manager includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 11.10.2012
 *
 ***********************************************************/
#ifndef DEVICE_MANAGER_H_
#define DEVICE_MANAGER_H_

#include "thread_interface.h"
#include "yami++.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace config4cpp;

class device_manager : public thread_interface {
public:
	device_manager(Configuration *cfg);
	virtual ~device_manager();
	void exit_thread();
protected:
private:
	volatile bool fsm_worker_exit;
	Category &log;
	void internal_thread_run();
};

}

#endif /* DEVICE_MANAGER_H_ */
