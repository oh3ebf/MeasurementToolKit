/***********************************************************
 * Software: instrument server
 * Module:   Keithley 2015 multimeter class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 1.4.2013
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
#include "keithley2015.h"
#include "message_interface.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

/* Function implements constructor for keithley2015 class
 *
 * Parameters:
 *
 * Returns:
 *
 */

keithley2015::keithley2015(string& dev_name, int32_t interface, int32_t pad, int32_t sad)
	: log(Category::getInstance("gpib")), mode(MODE_DC_VOLTAGE) {

  log.debug("Agilent keithley2015 initializing");

  // store gpib name for later use
  gpib_name = dev_name;
  device_name = "keithley2015";

#ifndef GPIB_TEST
  // open device
  if(this->open_device(interface, pad, sad)) {
    log.info("Agilent keithley2015 found at address %d", pad);
  } else {
    log.error("Agilent keithley2015 not found at address %d", pad);
    return;
  }
#endif

}

/* Function implements constructor for keithley2015 class
 *
 * Parameters:
 *
 * Returns:
 *
 */

keithley2015::keithley2015(string& dev_name) : log(Category::getInstance("gpib")), mode(MODE_DC_VOLTAGE) {
  log.debug("Agilent keithley2015 initializing");

  // store gpib name for later use
  gpib_name = dev_name;
  device_name = "keithley2015";

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

keithley2015::~keithley2015() {

}

/* Function returns equipment id
 *
 * Parameters:
 *
 * Returns:
 * string containing equipment id
 *
 */

string *keithley2015::get_id(void) {
  // read instrument id
  string query = "ID?\n";
  this->write_string(query);
  return(this->read_string(64));
}

/* Function runs self test in keithley2015
 *
 * Parameters:
 *
 * Returns:
 *
 */

void keithley2015::test(void) {
  string query = "TEST\n";
  this->write_string(query);
}

/* Function initialize instrument to default values
 *
 * Parameters:
 *
 * Returns:
 *
 */

void keithley2015::clear_device(void) {
  // send clear to bus
  this->clear(1000000);

  /* verify the instrument id is what we expect */
  string query = "ID?\n";
  this->write_string(query);

  // read response
  string *tmpbuf = this->read_string(64);

  log.debug("device: ", tmpbuf->c_str());
  // remove allocated string
  delete(tmpbuf);
  /*if (strncmp(tmpbuf, "HP3488A", 7) != 0) {
        fprintf(stderr, "%s: device id %s != %s\n", prog, tmpbuf, "HP3488A");
        exit(1);
    }*/
}

/* Function resets device power on state
 *
 * Parameters:
 *
 * Returns:
 *
 */

void keithley2015::reset() {
  /* verify the instrument id is what we expect */
  string query = "RESET\n";
  this->write_string(query);
}

/* Function reads status register
 *
 * Parameters:
 *
 * Returns:
 * status in uint8
 *
 */

uint8_t keithley2015::read_status(void) {
  uint8_t value = 0;

  // verify the instrument id is what we expect
  string query = "STATUS\n";
  this->write_string(query);

  // read status byte from device
  string *tmpbuf = this->read_string(64);
  // convert to integer
  value = atoi(tmpbuf->c_str());
  delete(tmpbuf);

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

uint8_t keithley2015::read_error(void) {
  uint8_t error = 0;

  // verify the instrument id is what we expect
  string query = "ERROR\n";
  this->write_string(query);

  // read status byte from device
  string *tmpbuf = this->read_string(64);
  // parse error code
  error = atoi(tmpbuf->c_str());
  delete(tmpbuf);

  return(error);
}

/* Function sets measurement mode
 *
 * Parameters:
 * mode: selected measurement mode
 *
 * Returns:
 * error in uint8
 *
 */

int8_t keithley2015::set_mode(uint8_t m) {
  int8_t status = 0;
  string cmd = ":CONFIGURE:";

  mode = (measurement_mode)m;

  switch(m) {
  case MODE_DC_VOLTAGE:
	  cmd += "VOLTAGE:DC\n";
	  break;
  case MODE_AC_VOLTAGE:
	  cmd += "VOLTAGE:AC\n";
	  break;
  case MODE_DC_CURRENT:
	  cmd += "CURRENT:DC\n";
	  break;
  case MODE_AC_CURRENT:
	  cmd += "CURRENT:AC\n";
	  break;
  case MODE_2_RESISTANCE:
	  cmd += "RESISTANCE\n";
	  break;
  case MODE_4_RESISTANCE:
	  cmd += "FRESISTANCE\n";
	  break;
  case MODE_PERIOD:
	  cmd += "PERIOD\n";
	  break;
  case MODE_FREQUENCY:
	  cmd += "FREQUENCY\n";
	  break;
  case MODE_TEMPERATURE:
	  cmd += "TEMPERATURE\n";
	  break;
  case MODE_DIODE:
	  cmd += "DIODE\n";
	  break;
  case MODE_CONTINUITY:
	  cmd += "CONTINUITY\n";
	  break;
  case MODE_DISTORTION:
	  cmd += "DISTORTION\n";
	  break;
  default:
	  log.warn("mode not supported: %d", m);
	  return(-1);
	  break;
  }

  // write command to target
  this->write_string(cmd);

  return(status);
}

/* Function returns measurement mode from device
 *
 * Parameters:
 *
 * Returns:
 * error in uint8
 *
 */

uint8_t keithley2015::get_mode(void) {
  return(mode);
}

/* Function makes single measurement at device
 *
 * Parameters:
 *
 * Returns:
 * value in double
 *
 */

double keithley2015::get_value(void) {
	double value = 0.0F;
	string cmd = ":READ?\n";
	string *result;

#if GPIB_TEST
  result = new string("+5.00000000E-006");
#else
  // write command to target
  this->write_string(cmd);

  // read latest measured value from device
  result = this->read_string(256);
#endif

  // check read success
  if(result == NULL) {
	  log.error("failed to read measurement value");
	  return(0.0F);
  }

  // convert to double
  value = strtod(result->c_str(), NULL);
  delete(result);

  return(value);
}
}
