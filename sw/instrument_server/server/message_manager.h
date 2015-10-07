/***********************************************************
 * Software: instrument server
 * Module:   yami message manager includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 9.10.2012
 *
 ***********************************************************/
#ifndef MESSAGE_MANAGER_H_
#define MESSAGE_MANAGER_H_

#include "thread_interface.h"
#include "message_interface.h"
#include "data_stream_interface.h"
#include "yami++.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace config4cpp;
using namespace YAMI;

class message_manager : public data_stream_interface, thread_interface {
public:
	message_manager(Configuration *cfg);
	virtual ~message_manager();
	void exit_thread();
	void add_handler(map<string, message_interface *>& handler);
	virtual void send_data(string ip, string msg, ParamSet p);
	virtual string *get_domain(string ip);
protected:
private:
	typedef struct {
		string domain;
		string object;
		string ip;
		int port;
		Agent stream_server;
	} ClientStruct;

	volatile bool fsm_worker_exit;
	Category &log;
	uint32_t server_port;
	string server_name;
	uint32_t status_timer;
	Agent *msg_server;
	map<string, ClientStruct *> clients;
	map<string, message_interface *> msg_handlers;

	void internal_thread_run();
	void server_status(void);
};

}

#endif /* MESSAGE_MANAGER_H_ */
