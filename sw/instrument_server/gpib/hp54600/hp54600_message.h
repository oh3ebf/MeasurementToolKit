/***********************************************************
 * Software: instrument server
 * Module:   hp54600 oscilloscope message handler headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 28.11.2012
 *
 ***********************************************************/

#ifndef HP54600_MESSAGE_H_
#define HP54600_MESSAGE_H_

//#include "yami++.h"
//#include "hp54600.h"
#include "message_base.h"
#include "message_interface.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

class hp54600_message : public message_base, command_parameter_interface {
public:
	hp54600_message(hp54600& dev);
	virtual ~hp54600_message();
	//virtual void handle_msg(auto_ptr<IncomingMsg> msg_in);
	virtual map<string, string> *get_config(void);
	virtual void fsm(data_stream_interface *d);
	virtual bool parameter_callback(auto_ptr<ParamSet> p, void **data);
	virtual bool parameter_delete(void **data);

protected:
	typedef ParamSet (hp54600_message::*ActionFunction)(auto_ptr<ParamSet> params);

private:
	typedef struct scope_struct {
		public:
		uint8_t ch_cnt;			// number of used channels
	} scope_parameters;

	hp54600& device;
	//Category &log;
	//map<string, ActionFunction> command_map;
	//volatile fsm_states state;
	//string source_ip;
	//map<pair<string, fsm_states>, fsm_command *> command_list;
	//pthread_mutex_t fsm_mutex;

	ParamSet cmdCheckOnLine(auto_ptr<ParamSet> params);
	ParamSet cmdTest(auto_ptr<ParamSet> params);
	ParamSet cmdReset(auto_ptr<ParamSet> params);
	ParamSet cmdAutoscale(auto_ptr<ParamSet> params);
	ParamSet cmdChannelStatus(auto_ptr<ParamSet> params);
	ParamSet cmdChannelStatusRead(auto_ptr<ParamSet> params);
	ParamSet cmdChannelParametersRead(auto_ptr<ParamSet> params);

	ParamSet cmdChannelCouplingSet(auto_ptr<ParamSet> params);
	ParamSet cmdChannelBwLimitSet(auto_ptr<ParamSet> params);
	ParamSet cmdChannelInvertSet(auto_ptr<ParamSet> params);
	ParamSet cmdChannelVernierSet(auto_ptr<ParamSet> params);
	ParamSet cmdChannelProbeSet(auto_ptr<ParamSet> params);

	ParamSet cmdTrigMode(auto_ptr<ParamSet> params);
	ParamSet cmdTrigSource(auto_ptr<ParamSet> params);
	ParamSet cmdTrigSlope(auto_ptr<ParamSet> params);
	ParamSet cmdTrigCouple(auto_ptr<ParamSet> params);
	ParamSet cmdTrigReject(auto_ptr<ParamSet> params);
	ParamSet cmdTrigNoiseReject(auto_ptr<ParamSet> params);
	ParamSet cmdTrigPolarity(auto_ptr<ParamSet> params);
	ParamSet cmdTrigTVMode(auto_ptr<ParamSet> params);
	ParamSet cmdTrigTVHFReject(auto_ptr<ParamSet> params);
	ParamSet cmdTrigLevel(auto_ptr<ParamSet> params);
	ParamSet cmdTrigHoldOff(auto_ptr<ParamSet> params);
	ParamSet cmdTrigParametersRead(auto_ptr<ParamSet> params);
	ParamSet cmdRun(auto_ptr<ParamSet> params);
	ParamSet cmdStop(auto_ptr<ParamSet> params);
	ParamSet cmdVoltageScale(auto_ptr<ParamSet> params);
	ParamSet cmdVoltageOffset(auto_ptr<ParamSet> params);
	//ParamSet cmdMeasurementStart(auto_ptr<ParamSet> params);
	ParamSet cmdTimebaseRange(auto_ptr<ParamSet> params);
	ParamSet cmdTimebaseParametersRead(auto_ptr<ParamSet> params);
	ParamSet cmdWaveformPreambleRead(auto_ptr<ParamSet> params);

};

}

#endif /* HP54600_MESSAGE_H_ */
