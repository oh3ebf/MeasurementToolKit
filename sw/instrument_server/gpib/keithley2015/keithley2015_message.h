/***********************************************************
 * Software: instrument server
 * Module:   Keithley 2015 multimeter message handler headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 1.4.2013
 *
 ***********************************************************/

#ifndef KEITHLEY2015_MESSAGE_H_
#define KEITHLEY2015_MESSAGE_H_

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

class keithley2015_message : public message_base {
public:
	keithley2015_message(keithley2015& dev);
	virtual ~keithley2015_message();
	virtual map<string, string> *get_config(void);
	virtual void fsm(data_stream_interface *d);

protected:
	typedef ParamSet (keithley2015_message::*ActionFunction)(auto_ptr<ParamSet> params);

private:
	keithley2015&  device;

	ParamSet cmdCheckOnLine(auto_ptr<ParamSet> params);
	ParamSet cmdTest(auto_ptr<ParamSet> params);
	ParamSet cmdReset(auto_ptr<ParamSet> params);
	ParamSet cmdSetMode(auto_ptr<ParamSet> params);
	ParamSet cmdReadValue(auto_ptr<ParamSet> params);
};

}

#endif /* KEITHLEY2015_MESSAGE_H_ */
