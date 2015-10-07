/***********************************************************
 * Software: instrument server
 * Module:   HP 8922 GSM test set message handler headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 31.1.2013
 *
 ***********************************************************/

#ifndef HP8922_MESSAGE_H_
#define HP8922_MESSAGE_H_

#include "message_base.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

class hp8922_message : public message_base {
public:
	hp8922_message(hp8922& dev);
	virtual ~hp8922_message();
	virtual map<string, string> *get_config(void);
	virtual void fsm(data_stream_interface *d);

protected:
	typedef ParamSet (hp8922_message::*ActionFunction)(auto_ptr<ParamSet> params);

private:
	hp8922&  device;

	ParamSet cmdCheckOnLine(auto_ptr<ParamSet> params);
	ParamSet cmdTest(auto_ptr<ParamSet> params);
	ParamSet cmdReset(auto_ptr<ParamSet> params);
	ParamSet cmdDisplaySelect(auto_ptr<ParamSet> params);
};

}

#endif /* HP8922_MESSAGE_H_ */
