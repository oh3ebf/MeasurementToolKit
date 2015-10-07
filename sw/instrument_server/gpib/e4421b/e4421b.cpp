/***********************************************************
 * Software: instrument server
 * Module:   Agilent E4421B RF generator class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 31.1.2013
 *
 ***********************************************************/

#include <string>
#include <stdint.h>
#include <stdlib.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "yami++.h"
#include "gpib.h"
#include "e4421b.h"
#include "message_interface.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

/* Function implements constructor for e4421b class
 *
 * Parameters:
 *
 * Returns:
 *
 */

e4421b::e4421b(string& dev_name, int32_t interface, int32_t pad, int32_t sad) : log(Category::getInstance("gpib")) {

	log.debug("Agilent E4421B initializing");

	// store gpib name for later use
	gpib_name = dev_name;
	device_name = "E4421B";

#ifndef GPIB_TEST
	// open device
	if(this->open_device(interface, pad, sad)) {
		log.info("Agilent E4421B found at address %d", pad);
	} else {
		log.error("Agilent E4421B not found at address %d", pad);
		return;
	}
#endif

}

/* Function implements constructor for e4421b class
 *
 * Parameters:
 *
 * Returns:
 *
 */

e4421b::e4421b(string& dev_name) : log(Category::getInstance("gpib")) {
	log.debug("Agilent E4421B initializing");

	// store gpib name for later use
	gpib_name = dev_name;
	device_name = "E4421B";

#ifndef GPIB_TEST
	// open named device
	if(this->open_board(gpib_name)) {
		log.info("%s found", device_name.c_str());
	} else {
		log.error("%s not found", device_name.c_str());
		return;
	}
#endif

}

/* Function implements class destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

e4421b::~e4421b() {

}

/* Function returns equipment id
 *
 * Parameters:
 *
 * Returns:
 * string containing equipment id
 *
 */
/*
string *e4421b::get_id(void) {
  // read instrument id
  string query = "ID?\n";
  this->write_string(query);
  return(this->read_string(64));
}
 */

/* Function runs self test in e4421b
 *
 * Parameters:
 *
 * Returns:
 *
 */
/*
void e4421b::test(void) {
  string query = "TEST\n";
  this->write_string(query);
}
 */
/* Function initialize instrument to default values
 *
 * Parameters:
 *
 * Returns:
 *
 */

void e4421b::clear_device(void) {
	/* set device to preset values */
	string query = ":SYSTEM:PRESET\n";
	this->write_string(query);
}

/* Function resets device power on state
 *
 * Parameters:
 *
 * Returns:
 *
 */

//void e4421b::reset() {
/* reset device */
//string query = "RESET\n";
//this->write_string(query);
//}

/* Function reads status register
 *
 * Parameters:
 *
 * Returns:
 * status in uint8
 *
 */

uint8_t e4421b::read_status(void) {
	uint8_t value = 0;

	// query device status
	string query = "STATUS\n";
	this->write_string(query);

	// read status byte from device
	string *result = this->read_string(64);

	// convert to integer
	value = atoi(result->c_str());
	delete(result);

	return(value);
}

/* Function reads error register
 *
 * Parameters:
 *
 * Returns:
 * error in uint8
 *
 */

uint8_t e4421b::read_error(void) {
	uint8_t error = 0;

	// query error state from device
	string query = "ERROR\n";
	this->write_string(query);

	// read status byte from device
	string *result = this->read_string(64);
	// parse error code
	error = atoi(result->c_str());
	delete(result);

	return(error);
}

/* Function sets rf output state
 *
 * Parameters:
 * state:
 *
 * Returns:
 *
 */

void e4421b::set_rf_output_state(bool state) {
	string cmd1 = "OUTP:STATE ON\n";
	string cmd2 = "OUTP:STATE OFF\n";

	if(state) {
		this->write_string(cmd1);
	} else {
		this->write_string(cmd2);
	}
}

/* Function gets rf output state
 *
 * Parameters:
 *
 * Returns:
 * true if output on, otherwise false
 *
 */

bool e4421b::get_rf_output_state(void) {
	string cmd = "OUTP:STATE?\n";

	this->write_string(cmd);

#ifndef GPIB_TEST
	// read frequency from device
	string *result = this->read_string(8);
#else
	string *result = new string("OFF");
#endif

	if(result->compare("ON") == 0) {
		return(true);

	} else {
		return(false);
	}
}

/* Function gets modulation output state
 *
 * Parameters:
 *
 * Returns:
 * true if modulation on, otherwise false
 *
 */

bool e4421b::get_modulation_output_state(void) {
	string cmd = "OUTP:MODULATION:STATE?\n";

	this->write_string(cmd);

#ifndef GPIB_TEST
	// read frequency from device
	string *result = this->read_string(8);
#else
	string *result = new string("OFF");
#endif

	if(result->compare("ON") == 0) {
		return(true);

	} else {
		return(false);
	}
}

/* Function sets modulation output state
 *
 * Parameters:
 * state:
 *
 * Returns:
 *
 */

void e4421b::set_modulation_output_state(bool state) {
	string cmd1 = "OUTP:MODULATION:STATE ON\n";
	string cmd2 = "OUTP:MODULATION:STATE OFF\n";

	if(state) {
		this->write_string(cmd1);
	} else {
		this->write_string(cmd2);
	}
}

/* Function sets output frequency
 *
 * Parameters:
 * freq: new frequency in MHz
 *
 * Returns:
 *
 */

void e4421b::set_frequency(uint32_t freq) {
	const char *cmd = "FREQUENCY";

	// write command to target
	this->writef("%s %d MHz\n", cmd, freq);
}

/* Function sets output frequency
 *
 * Parameters:
 * freq: new frequency in MHz
 *
 * Returns:
 *
 */

void e4421b::set_frequency(double freq) {
	const char *cmd = "FREQUENCY";

	// write command to target
	this->writef("%s %.0lf Hz\n", cmd, freq);
}

/* Function gets output frequency
 *
 * Parameters:
 *
 * Returns:
 * output frequency
 *
 */

uint32_t e4421b::get_frequency(void) {
	uint32_t f = 0;
	string cmd = "FREQUENCY?\n";

	// write command to target
	this->write_string(cmd);

#ifndef GPIB_TEST
	// read frequency from device
	string *result = this->read_string(64);
#else
	string *result = new string("3000");
#endif

	// parse error code
	f = atoi(result->c_str());

	delete (result);
	return (f);
}

/* Function sets output frequency
 *
 * Parameters:
 * state:
 * Returns:
 *
 */

void e4421b::set_amplitude(double amplitude) {
	const char *cmd = "POWER:AMPLITUDE";

	// write command to target
	this->writef("%s %.2f dBm\n", cmd, amplitude);
}

/* Function gets output frequency
 *
 * Parameters:
 *
 * Returns:
 * output frequency
 *
 */

double e4421b::get_amplitude(void) {
	double amplitude = 0;
	string cmd = "POWER:AMPLITUDE?\n";

	// write command to target
	this->write_string(cmd);

#ifndef GPIB_TEST
	// read frequency from device
	string *result = this->read_string(64);
#else
	string *result = new string("-131.0");
#endif

	// parse error code
	amplitude = strtod(result->c_str(), NULL);

	delete (result);
	return (amplitude);
}

/* Function sets modulation source
 *
 * Parameters:
 * mod: modulation to use
 * path: 1 / 2
 * modulation_source: source selection
 *
 * Returns:
 * error
 */

int8_t e4421b::set_modulation_source(modulation mod, uint8_t path, modulation_source s) {
	int8_t error = -1;
	string modualtionStr;
	string modeStr;

	// check valid parameters
	if(path < 3) {
		switch(mod) {
		case AM:
			modualtionStr = "AM";
			break;
		case FM:
			modualtionStr = "FM";
			break;
		case PM:
			modualtionStr = "PM";
			break;
		}

		switch(s) {
		case SOURCE_INTERNAL:
			modeStr = "INT1";
			break;
		case SOURCE_EXTERNAL_1:
			modeStr = "EXT1";
			break;
		case SOURCE_EXTERNAL_2:
			modeStr = "EXT2";
			break;
		}

		// write to target
		writef(":%s%d:SOURCE %s\n", modualtionStr.c_str(), path, modeStr.c_str());
	}

	return(error);
}

/* Function sets modulation state
 *
 * Parameters:
 * mod: modulation to use
 * path: 1 / 2
 * state: false = OFF, true = ON
 *
 * Returns:
 * error
 */

int8_t e4421b::set_modulation_state(modulation mod, uint8_t path, bool state) {
	int8_t error = -1;
	string modualtionStr;
	string modeStr;

	// check valid parameters
	if(path < 3) {
		switch(mod) {
		case AM:
			modualtionStr = "AM";
			break;
		case FM:
			modualtionStr = "FM";
			break;
		case PM:
			modualtionStr = "PM";
			break;
		}

		if(state) {
			modeStr = "ON";
		} else {
			modeStr = "OFF";
		}

		// write to target
		writef(":%s%d:STATE %s\n", modualtionStr.c_str(), path, modeStr.c_str());
	}

	return(error);
}

/* Function sets fm modulation coupling
 *
 * Parameters:
 * path: 1 / 2
 * source: 1 / 2
 * coupling: false = DC, true = AC
 *
 * Returns:
 * error
 *
 */

int8_t e4421b::set_modulation_coupling(modulation mod, uint8_t path, uint8_t source, bool coupling) {
	int8_t error = -1;
	string modualtionStr;
	string modeStr;

	// check valid parameters
	if((mod < 3) && (path < 3)) {
		switch(mod) {
		case AM:
			modualtionStr = "AM";
			break;
		case FM:
			modualtionStr = "FM";
			break;
		case PM:
			modualtionStr = "PM";
			break;
		}

		if(coupling) {
			modeStr = "AC";
		} else {
			modeStr = "DC";
		}

		// write to target
		writef(":%s%d:EXTERNAL%d:COUPlING %s\n", modualtionStr.c_str(), path, source, modeStr.c_str());
	}

	return(error);
}

/* Function sets modulation source rate
 *
 * Parameters:
 * mod: modulation
 * path: 1 / 2
 * rate:
 *
 * Returns:
 * error
 */

int8_t e4421b::set_internal_modulation_source_rate(modulation mod, uint8_t path, double rate) {
	int8_t error = -1;
	string modualtionStr;

	// check valid parameters
	if(path < 3) {
		switch(mod) {
		case AM:
			modualtionStr = "AM";
			break;
		case FM:
			modualtionStr = "FM";
			break;
		case PM:
			modualtionStr = "PM";
			break;
		}

		// write to target
		writef(":%s%d:INTERNAL1:FREQUENCY %fHz\n", modualtionStr.c_str(), path, rate);
	}

	return(error);
}

/* Function sets modulation sweep time for a swept-sine, internally-generated signal
 *
 * Parameters:
 * mod: modulation
 * path: 1 / 2
 * time:
 *
 * Returns:
 * error
 */

int8_t e4421b::set_internal_modulation_sweep_time(modulation mod, uint8_t path, double time) {
	int8_t error = -1;
	string modualtionStr;

	// check valid parameters
	if(path < 3) {
		switch(mod) {
		case AM:
			modualtionStr = "AM";
			break;
		case FM:
			modualtionStr = "FM";
			break;
		case PM:
			modualtionStr = "PM";
			break;
		}

		// write to target
		writef(":%s%d:INTERNAL1:SWEEP:TIME %f\n", modualtionStr.c_str(), path, time);
	}

	return(error);
}

/* Function sets modulation trigger for the frequency modulation sweep
 *
 * Parameters:
 * mod: modulation
 * path: 1 / 2
 * time:
 *
 * Returns:
 * error
 */

int8_t e4421b::set_internal_modulation_sweep_trigger(modulation mod, uint8_t path, modulation_sweep_trigger trig) {
	int8_t error = -1;
	string modualtionStr;
	string modeStr;

	// check valid parameters
	if(path < 3) {
		switch(mod) {
		case AM:
			modualtionStr = "AM";
			break;
		case FM:
			modualtionStr = "FM";
			break;
		case PM:
			modualtionStr = "PM";
			break;
		}

		switch(trig) {
		case SWEEP_TRIGGER_IMMEDIATE:
			modeStr = "IMMEDIATE";
			break;
		case SWEEP_TRIGGER_EXTERNAL:
			modeStr = "EXTERNAL";
			break;
		case SWEEP_TRIGGER_BUS:
			modeStr = "BUS";
			break;
		case SWEEP_TRIGGER_KEY:
			modeStr = "KEY";
			break;
		}

		// write to target
		writef(":%s%d:INTERNAL1:SWEEP:TRIGGER %s\n", path, modeStr.c_str());
	}

	return(error);
}

/* Function sets modulation waveform
 *
 * Parameters:
 * mod: modulation
 * path: 1 / 2
 * wave: selected waveform
 *
 * Returns:
 * error
 */

int8_t e4421b::set_internal_modulation_waveform(modulation mod, uint8_t path, modulation_waveform wave) {
	int8_t error = -1;
	string modualtionStr;
	string modeStr;

	// check valid parameters
	if(path < 3) {
		switch(mod) {
		case AM:
			modualtionStr = "AM";
			break;
		case FM:
			modualtionStr = "FM";
			break;
		case PM:
			modualtionStr = "PM";
			break;
		}

		switch(wave) {
		case WAVEFORM_SINE:
			modeStr = "SINE";
			break;
		case WAVEFORM_TRIANGLE:
			modeStr = "TRIANGLE";
			break;
		case WAVEFORM_SQUARE:
			modeStr = "SQUARE";
			break;
		case WAVEFORM_RAMP:
			modeStr = "RAMP";
			break;
		case WAVEFORM_NOISE:
			modeStr = "NOISE";
			break;
		case WAVEFORM_DUAL_SINE:
			modeStr = "DUALSINE";
			break;
		case WAVEFORM_SWEPT_SINE:
			modeStr = "SWEPTSINE";
			break;
		}

		// write to target
		writef(":%s%d:INTERNAL1:FUNCTION:SHAPE %s\n", modualtionStr.c_str(), path, modeStr.c_str());
	}

	return(error);
}

/* Function sets am modulation depth
 *
 * Parameters:
 * path: 1 / 2
 * depth: depth of modulation 0.1% - 100%
 *
 * Returns:
 * error
 *
 */

int8_t e4421b::set_am_modulation_depth(uint8_t path, double depth) {
	int8_t error = -1;

	// check valid parameters
	if(path < 3) {
		// write to target
		writef(":AM%d:DEPTH %.2f%\n", path, depth);
	}

	return(error);
 }

/* Function sets am modulation depth tracking
 *
 * Parameters:
 * path: 1 / 2
 * deviation: depth of deviation
 *
 * Returns:
 * error
 *
 */

int8_t e4421b::set_am_modulation_tracking(uint8_t path, bool bw) {
	int8_t error = -1;
	string modeStr;

	// check valid parameters
	if(path < 3) {
		if(bw) {
			modeStr = "ON";
		} else {
			modeStr = "OFF";
		}

		// write to target
		writef(":AM%d:DEPTH:TRACK %s\n", path, modeStr.c_str());
	}

	return(error);
}

/* Function sets fm modulation deviation
 *
 * Parameters:
 * path: 1 / 2
 * deviation: depth of deviation
 *
 * Returns:
 * error
 *
 */

int8_t e4421b::set_modulation_deviation(modulation mod, uint8_t path, double deviation) {
	int8_t error = -1;
	string modualtionStr;

	// check valid parameters
	if(path < 3) {
		switch(mod) {
		case AM:
			return(error);
			break;
		case FM:
			modualtionStr = "FM";
			break;
		case PM:
			modualtionStr = "PM";
			break;
		}

		// write to target
		writef(":%s%d:DEVIATION %fMHz\n",modualtionStr.c_str(), path, deviation);
	}

	return(error);
}

/* Function sets pm modulation band with configuration
 *
 * Parameters:
 * path: 1 / 2
 * deviation: depth of deviation
 *
 * Returns:
 * error
 *
 */

int8_t e4421b::set_pm_modulation_bandwith(uint8_t path, bool bw) {
	int8_t error = -1;
	string modeStr;

	// check valid parameters
	if(path < 3) {
		if(bw) {
			modeStr = "HIGH";
		} else {
			modeStr = "NORMAL";
		}

		// write to target
		writef(":PM%d:BANDWIDTH %s\n", path, modeStr.c_str());
	}

	return(error);
}

int8_t e4421b::set_modulation_tracking(modulation mod, uint8_t path, bool track) {
	int8_t error = -1;
	string modualtionStr;
	string modeStr;

	// check valid parameters
	if(path < 3) {
		switch(mod) {
		case AM:
			return(error);
		case FM:
			modualtionStr = "FM";
			break;
		case PM:
			modualtionStr = "PM";
			break;
		}

		if(track) {
			modeStr = "ON";
		} else {
			modeStr = "OFF";
		}

		// write to target
		writef(":%s%d:DEVIATION:TRACK %s\n", modualtionStr.c_str(), path, modeStr.c_str());
	}

	return(error);
}
}
