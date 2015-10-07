/***********************************************************
 * Software: instrument server
 * Module:   hp3488 switching control unit message handler headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 15.10.2012
 *
 ***********************************************************/

#ifndef HP3488_MESSAGE_H_
#define HP3488_MESSAGE_H_

#include "yami++.h"
#include "hp3488.h"
#include "message_interface.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

class hp3488_message : public message_interface {
public:
	hp3488_message(hp3488& dev);
	virtual ~hp3488_message();
	virtual void handle_msg(auto_ptr<IncomingMsg> msg_in);
	virtual map<string, string> *get_config(void);

protected:
	typedef ParamSet (hp3488_message::*ActionFunction)(auto_ptr<ParamSet> params);

private:
	hp3488&  device;
	Category &log;
	map<string, ActionFunction> command_map;

	ParamSet cmdCheckOnLine(auto_ptr<ParamSet> params);
	ParamSet cmdTest(auto_ptr<ParamSet> params);
	ParamSet cmdReset(auto_ptr<ParamSet> params);
	ParamSet cmdOpen(auto_ptr<ParamSet> params);
	ParamSet cmdClose(auto_ptr<ParamSet> params);
};

}

#endif /* HP3488_MESSAGE_H_ */
