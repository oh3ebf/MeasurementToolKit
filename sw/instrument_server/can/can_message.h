/***********************************************************
 * Software: instrument server
 * Module:   Can bus message handler headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 30.4.2013
 *
 ***********************************************************/

#ifndef CAN_MESSAGE_H_
#define CAN_MESSAGE_H_

#include "yami++.h"
#include "can_base.h"
#include "message_interface.h"
#include "can_buffer.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

class can_message : public message_interface {
public:
	can_message(can_base&  dev);
	virtual ~can_message();
	virtual void handle_msg(auto_ptr<IncomingMsg> msg_in);
	virtual map<string, string> *get_config(void);
	virtual bool disconnect(string client_ip);
	virtual void fsm(data_stream_interface *d);
protected:
private:
	typedef ParamSet (can_message::*ActionFunction)(auto_ptr<ParamSet> params);

	can_base &device;
	Category &log;
	map<string, ActionFunction> command_map;
	volatile fsm_states state;
	string source_ip;
	multimap<pair<string, fsm_states>, fsm_command *> command_list;
	can_buffer rx_buffer;
	pthread_mutex_t fsm_mutex;

	ParamSet cmdCheckOnLine(auto_ptr<ParamSet> params);
	ParamSet cmdMeasurementStart(auto_ptr<ParamSet> params);
	ParamSet cmdMeasurementStop(auto_ptr<ParamSet> params);
	ParamSet cmdMeasurementState(auto_ptr<ParamSet> params);
	ParamSet cmdMeasurementUpdate(auto_ptr<ParamSet> params);
};

}

#endif /* CAN_MESSAGE_H_ */
