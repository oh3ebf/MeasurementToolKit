/***********************************************************
 * Software: instrument server
 * Module:   device message base class includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 23.9.2013
 *
 ***********************************************************/

#ifndef MESSAGE_BASE_H_
#define MESSAGE_BASE_H_

#include "message_interface.h"
#include "command_parameter_interface.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

class message_base : public message_interface {

public:
	message_base();
	virtual ~message_base();
	virtual map<string, string> *get_config(void) {return(NULL);}
	virtual bool disconnect(string client_ip);
	virtual void handle_msg(auto_ptr<IncomingMsg> msg_in);
	virtual void fsm(data_stream_interface *d) {};
	virtual void set_callback(command_parameter_interface *cmd);

protected:
	typedef ParamSet (message_base::*ActionFunction)(auto_ptr<ParamSet> params);

	string source_ip;
	pthread_mutex_t fsm_mutex;
	map<string, ActionFunction> command_map;
	Category &log;
	volatile message_interface::fsm_states state;
	multimap<pair<string, fsm_states>, fsm_command *> command_list;
	command_parameter_interface *user_parameter;

	virtual ParamSet cmdMeasurementStart(auto_ptr<ParamSet> params);
	virtual ParamSet cmdMeasurementStop(auto_ptr<ParamSet> params);
	virtual ParamSet cmdMeasurementState(auto_ptr<ParamSet> params);
	virtual ParamSet cmdMeasurementUpdate(auto_ptr<ParamSet> params);
private:
};

}

#endif /* MESSAGE_BASE_H_ */
