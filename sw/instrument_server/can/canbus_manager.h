/***********************************************************
 * Software: instrument server
 * Module:   intrument frame can bus manager includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 12.12.2012
 *
 ***********************************************************/

#ifndef CANBUS_MANAGER_H_
#define CANBUS_MANAGER_H_

#include "thread_interface.h"
#include "canmsg.h"
#include "can_base.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace config4cpp;

class canbus_manager : public thread_interface {
public:
	canbus_manager(Configuration *cfg);
	virtual ~canbus_manager();
	bool start_worker(data_stream_interface *d);
	void exit_thread();
	map<string, message_interface *>& get_msg_list(void);
protected:
private:
	volatile bool fsm_worker_exit;
	Category &log;
	map<string, can_base *> bus_list;
	map<string, message_interface *> msg_list;
	data_stream_interface *data_stream;

	void internal_thread_run();
};

}

#endif /* CANBUS_MANAGER_H_ */
