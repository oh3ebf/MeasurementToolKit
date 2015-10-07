/***********************************************************
 * Software: instrument server
 * Module:   Agilent E4421B RF generator headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 31.1.2013
 *
 ***********************************************************/

#ifndef E4421B_H_
#define E4421B_H_

#include "gpib_488_2.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

class e4421b : public gpib_488_2 {
public:
	enum modulation {
		AM = 0,
		FM,
		PM
	};

	enum modulation_sweep_trigger {
		SWEEP_TRIGGER_IMMEDIATE = 0,
		SWEEP_TRIGGER_BUS,
		SWEEP_TRIGGER_EXTERNAL,
		SWEEP_TRIGGER_KEY
	};

	enum modulation_source {
		SOURCE_INTERNAL = 0,
		SOURCE_EXTERNAL_1,
		SOURCE_EXTERNAL_2
	};

	enum modulation_waveform {
		WAVEFORM_SINE = 0,
		WAVEFORM_TRIANGLE,
		WAVEFORM_SQUARE,
		WAVEFORM_RAMP,
		WAVEFORM_NOISE,
		WAVEFORM_DUAL_SINE,
		WAVEFORM_SWEPT_SINE
	};

	e4421b(string& dev_name, int32_t interface, int32_t pad, int32_t sad);
	e4421b(string& dev_name);
	virtual ~e4421b();
	//string *get_id(void);
	//void test(void);
	void clear_device(void);
	//void reset(void);
	uint8_t read_status(void);
	uint8_t read_error(void);

	// general commands
	void set_rf_output_state(bool state);
	bool get_rf_output_state(void);
	void set_modulation_output_state(bool state);
	bool get_modulation_output_state(void);
	void set_frequency(uint32_t freq);
	void set_frequency(double freq);
	uint32_t get_frequency(void);
	void set_amplitude(double amplitude);
	double get_amplitude(void);

	// generic modulation commands
	int8_t set_modulation_source(modulation mod, uint8_t path, modulation_source s);
	int8_t set_modulation_state(modulation mod, uint8_t path, bool state);
	int8_t set_modulation_coupling(modulation mod, uint8_t path, uint8_t source, bool coupling);
	int8_t set_internal_modulation_source_rate(modulation mod, uint8_t path, double rate);
	int8_t set_internal_modulation_sweep_time(modulation mod, uint8_t path, double time);
	int8_t set_internal_modulation_sweep_trigger(modulation mod, uint8_t path, modulation_sweep_trigger trig);
	int8_t set_internal_modulation_waveform(modulation mod, uint8_t path, modulation_waveform wave);

	// am modulation commands
	int8_t set_am_modulation_depth(uint8_t path, double depth);
	int8_t set_am_modulation_tracking(uint8_t path, bool track);

	// fm modulation commands


	// pm modulation commands
	int8_t set_pm_modulation_bandwith(uint8_t path, bool bw);

	// pm and fm modulation commands
	int8_t set_modulation_tracking(modulation mod, uint8_t path, bool coupling);
	int8_t set_modulation_deviation(modulation mod, uint8_t path, double deviation);

	//string& get_device_name(void);
protected:

private:
	Category &log;
};
}

#endif /* E4421B_H_ */
