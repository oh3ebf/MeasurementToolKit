/***********************************************************
 * Software: instrument server
 * Module:   Agilent E4421B RF generator message handler headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 31.1.2013
 *
 ***********************************************************/

#ifndef E4421B_MESSAGE_H_
#define E4421B_MESSAGE_H_

#include "message_base.h"
#include "command_parameter_interface.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

class e4421b_message : public message_base, command_parameter_interface {
public:
	e4421b_message(e4421b& dev);
	virtual ~e4421b_message();
	virtual map<string, string> *get_config(void);
	virtual void fsm(data_stream_interface *d);
	virtual bool parameter_callback(auto_ptr<ParamSet> p, void *data);
	virtual bool parameter_delete(void *data);

protected:
	typedef ParamSet (e4421b_message::*ActionFunction)(auto_ptr<ParamSet> params);

private:
	typedef enum {
		STOP = 0,
		AMPLITUDE = 1,
		FREQUENCY,
		AMPLITUDE_NESTED_FREQUENCY,
		FREQUENCY_NESTED_AMPLITUDE
	} sweep_modes;

	typedef struct sweep_struct {
	public:
		double start;
		double end;
		double step;
		double value;
		sweep_struct():start(0.0F), end(0.0F), step(0.0F), value(0.0F) {}
	} sweep_parameters;

	e4421b&  device;
	sweep_modes mode;
	sweep_parameters amplitude;
	sweep_parameters frequency;

	void amplitude_sweep(void);
	void amplitude_nested_frequency_sweep(void);
	void frequency_sweep(void);
	void frequency_nested_amplitude_sweep(void);

	ParamSet cmdCheckOnLine(auto_ptr<ParamSet> params);
	ParamSet cmdTest(auto_ptr<ParamSet> params);
	ParamSet cmdReset(auto_ptr<ParamSet> params);
	ParamSet cmdFrequency(auto_ptr<ParamSet> params);
	ParamSet cmdReadFrequency(auto_ptr<ParamSet> params);
	ParamSet cmdAmplitude(auto_ptr<ParamSet> params);
	ParamSet cmdReadAmplitude(auto_ptr<ParamSet> params);
	ParamSet cmdOutputState(auto_ptr<ParamSet> params);
	ParamSet cmdModulationOutputState(auto_ptr<ParamSet> params);

	ParamSet cmdModulationState(auto_ptr<ParamSet> params);
	ParamSet cmdModulationWaveform(auto_ptr<ParamSet> params);
	ParamSet cmdModulationSource(auto_ptr<ParamSet> params);
	ParamSet cmdModulationValue(auto_ptr<ParamSet> params);
	ParamSet cmdModulationRate(auto_ptr<ParamSet> params);
};

}

#endif /* E4421B_MESSAGE_H_ */
