/***********************************************************
 * Software: instrument server
 * Module:   gpib 488.2 standard implementation class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 26.11.2012
 *
 ***********************************************************/

#include <string>

#include <stdint.h>
#include <stdlib.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "gpib.h"
#include "gpib_488_2.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;

gpib_488_2::gpib_488_2() {
	// TODO Auto-generated constructor stub

}

gpib_488_2::~gpib_488_2() {
	// TODO Auto-generated destructor stub
}

/* Function returns equipment id
 *
 * Parameters:
 *
 * Returns:
 * string containing equipment id
 *
 */

string *gpib_488_2::get_id(void) {
  // query for device id
  string query = "IDN\n";

  this->write_string(query);

  return(this->read_string(64));
}

/* Function reset scope to default state
 *
 * Parameters:
 *
 * Returns:
 *
 */

void gpib_488_2::reset(void) {
	// reset device
	string query = "*RST\n";

	this->write_string(query);
}

/* Function performs device self test
 *
 * Parameters:
 *
 * Returns:
 * true if ok, otherwise false
 *
 */

bool gpib_488_2::self_test_query(void) {
	// self test command
	string query = "*TST?\n";

	this->write_string(query);

	// read status byte from device
	string *tmpbuf = this->read_string(10);

	// read value 0 if ok
	if(tmpbuf->compare("0") == 0) {
		return(true);
	} else {
		return(false);
	}
}

/* Function sets the operation complete bit
 *
 * Parameters:
 *
 * Returns:
 *
 */

void gpib_488_2::operation_complete(void) {
	// synchronize operation complete
	string query = "*OPC\n";

	this->write_string(query);
}

/* Function queries from device if all commands are processed
 *
 * Parameters:
 *
 * Returns:
 * true when all commands are processed, otherwise false
 *
 */

bool gpib_488_2::operation_complete_query(void) {
	// query operation complete
	string query = "*OPC?\n";

	this->write_string(query);

	return(true);
}

/* Function wait until all pending commands are processed
 *
 * Parameters:
 *
 * Returns:
 *
 */

void gpib_488_2::wait_to_complete(void) {
	// wait operation complete
	string query = "*WAI\n";

	this->write_string(query);
}

/* Function clear hole device status structure
 *
 * Parameters:
 *
 * Returns:
 *
 */

void gpib_488_2::clear_status(void) {
	// clear device status registers
	string query = "*CLS\n";

	this->write_string(query);
}

/* Function modifies event status register
 *
 * Parameters:
 * mask:
 *
 * Returns:
 *
 */

void gpib_488_2::event_status(uint8_t mask) {
	// event status enable
	string query = "*ESE\n";

	this->write_string(query);
}

/* Function returns events status enable register content
 *
 * Parameters:
 *
 * Returns:
 * register content as uint8
 *
 */

uint8_t gpib_488_2::event_status_query(void) {
	// event status enable query
	string query = "*ESE?\n";

	this->write_string(query);

	return(0);
}

/* Function returns events status register content
 *
 * Parameters:
 *
 * Returns:
 *
 */

uint8_t gpib_488_2::event_status_register_query(void) {
	// query event status register
	string query = "*ESR?\n";

	this->write_string(query);

	return(0);
}

/* Function modifies service request enable register
 *
 * Parameters:
 * value:
 *
 * Returns:
 *
 */

void gpib_488_2::service_request(uint8_t value) {
	// service request enable
	string query = "*SRE\n";

	this->write_string(query);
}

/* Function returns request enable register value
 *
 * Parameters:
 *
 * Returns:
 * register value as uint8
 */

uint8_t gpib_488_2::service_request_query(void) {
	// query service request
	string query = "*SRE?\n";

	this->write_string(query);

	return(0);
}

/* Function returns status byte register value
 *
 * Parameters:
 *
 * Returns:
 * register value as uint8
 *
 */

uint8_t gpib_488_2::read_status(void) {
	// read status register
	string query = "*STB?\n";

	this->write_string(query);

	return(0);
}
}
