/***********************************************************
 * Software: instrument server
 * Module:   Keithley 2015 multimeter headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 1.4.2013
 *
 ***********************************************************/

#ifndef KEITHLEY2015_H_
#define KEITHLEY2015_H_

#include "gpib_488_2.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

class keithley2015 : public gpib_488_2 {
public:
	enum measurement_mode {
		MODE_DC_VOLTAGE = 0,
		MODE_AC_VOLTAGE,
		MODE_DC_CURRENT,
		MODE_AC_CURRENT,
		MODE_2_RESISTANCE,
		MODE_4_RESISTANCE,
		MODE_PERIOD,
		MODE_FREQUENCY,
		MODE_TEMPERATURE,
		MODE_DIODE,
		MODE_CONTINUITY,
		MODE_DISTORTION
	};

	keithley2015(string& dev_name, int32_t interface, int32_t pad, int32_t sad);
	keithley2015(string& dev_name);
	virtual ~keithley2015();
	string *get_id(void);
	void test(void);
	void clear_device(void);
	void reset(void);
	uint8_t read_status(void);
	uint8_t read_error(void);
	int8_t set_mode(uint8_t mode);
	uint8_t get_mode(void);
	double get_value(void);

protected:

private:
	Category &log;
	measurement_mode mode;
};
}

#endif /* KEITHLEY2015_H_ */
