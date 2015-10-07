/***********************************************************
 * Software: instrument server
 * Module:   can device interface base class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 14.12.2012
 *
 ***********************************************************/

#include <string>

#include <stdint.h>
#include <string.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>
#include "canmsg.h"
#include "can_base.h"

namespace instrument_server {

/* Function implements constructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

can_base::can_base() : log(Category::getInstance("canbus")), fd(0), device_prefix("/dev/") {

}

/* Function implements constructor for class
 *
 * Parameters:
 * driver: name of can bus driver
 *
 * Returns:
 *
 */

can_base::can_base(string driver) : log(Category::getInstance("canbus")), fd(0), device_prefix("/dev/") {
	driver_name = driver;
}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

can_base::~can_base() {

}

/* Function return file descriptor
 *
 * Parameters:
  *
 * Returns:
 * file descriptor
 *
 */

int can_base::get_file_handle() {
	return(fd);
}

/* Function returns bus name
 *
 * Parameters:
 *
 * Returns:
 * string containing bus name
 *
 */

string can_base::get_bus_name(void) {
	return(device_name);
}

/* Function returns device driver name
 *
 * Parameters:
 *
 * Returns:
 * string containing device driver name
 *
 */

string can_base::get_driver_name(void) {
	return(driver_name);
}

/* Function is used to check if data is ready for reading
 *
 * Parameters:
 *
 * Returns:
 * true when data available
 *
 */

bool can_base::isReady(void) {
    bool result = false;
    fd_set sready;
    struct timeval nowait;

    // setup file descriptor set
    FD_ZERO(&sready);
    FD_SET(fd, &sready);

    memset((char *)&nowait, 0, sizeof(nowait));

    // check file descriptor state
    select(fd + 1, &sready, NULL, NULL, &nowait);
    if(FD_ISSET(fd, &sready)) {
        result = true;
    } else {
        result = false;
    }

    return(result);
}
}
