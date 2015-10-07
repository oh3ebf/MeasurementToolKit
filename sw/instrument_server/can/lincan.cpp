/***********************************************************
 * Software: instrument server
 * Module:   lincan device interface class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 14.12.2012
 *
 ***********************************************************/

#include <string>

#include <unistd.h>
#include <stdint.h>
#include <errno.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>
#include "canmsg.h"
#include "lincan.h"

namespace instrument_server {

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

lincan::lincan() : can_base("lincan") {
	log.info("using lincan driver interface");
}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

lincan::~lincan() {
	// TODO Auto-generated destructor stub
}

/* Function opens can device
 *
 * Parameters:
 * name: device name of can interface
 *
 * Returns:
 * file descriptor if ok, otherwise -1
 *
 */

int32_t lincan::can_open(string name, int32_t baudrate) {
	CAN_BAUDPARAMS params;

	log.info("open interface %s", name.c_str());
	device_name = name;

	// try to pen device
	fd = open((device_prefix + name).c_str(), O_RDWR);

	// check any errors
	if(fd < 0) {
		log.error("failed to open interface %s", name.c_str());
		return(-1);
	}

	// Make the file descriptor asynchronous
	if(fcntl(fd, F_SETFL, FASYNC) == -1) {
		log.error("failed to set interface %s non blocking mode", name.c_str());
		return(-1);
	}

	// set baud rate
	params.baudrate = baudrate;

	// use driver defaults
	params.flags = -1;
	params.sjw = -1;
	params.sample_pt = -1;

	if(ioctl(fd, CONF_BAUDPARAMS, &params) < 0)	{
		log.error("%s (lincan): IOCTL set speed failed", name.c_str());
	}

	return(0);
}

/* Function close can device
 *
 * Parameters:
 * fd: file descriptor to device
 *
 * Returns:
 * 0 if ok, otherwise -1
 *
 */

int8_t lincan::can_close(void) {
	if(close(fd) < 0) {
		log.error("failed to close device %s", device_name.c_str());
		return(-1);
	}

	return(0);
}

/* Function translates string baud rate to number presentation
 *
 * Parameters:
 * baud rate: string to translate
 *
 * Returns:
 * baud rate if ok, otherwise -1
 *
 */

int32_t lincan::translate_baud_rate(string baudrate) {
	// try to find baud rate
	if(!baudrate.compare("1M") == 0)
		return 1000000;

	if(!baudrate.compare("500K") == 0)
		return 500000;

	if(!baudrate.compare("250K") == 0)
		return 250000;

	if(!baudrate.compare("125K") == 0)
		return 125000;

	if(!baudrate.compare("100K") == 0)
		return 100000;

	if(!baudrate.compare("50K") == 0)
		return 50000;

	if(!baudrate.compare("20K") == 0)
		return 20000;

	if(!baudrate.compare("none") == 0) {
		log.info("no baud rate defined");
		return 0;
	}

	log.warn("%s is not valid baud rate.", baudrate.c_str());

	return -1;
}

/* Function writes can message to driver
 *
 * Parameters:
 * msg: pointer to message structure
 *
 * Returns:
 * 0 if ok, otherwise -1
 *
 */

int8_t lincan::can_send(CAN_MSG *msg) {

	msg->flags = 0;

	if(msg->id >= 0x800){
		msg->flags |= MSG_EXT;
	}
	log.debug("can id: %d",msg->id);
	if(write(fd, msg, sizeof(canmsg_t)) != sizeof(canmsg_t)) {
		return(-1);
	}

	return(0);
}

/* Function read can message from driver
 *
 * Parameters:
 * msg: pointer to message structure
 *
 * Returns:
 * 0 if ok, otherwise -1
 *
 */

int32_t lincan::can_read(CAN_MSG *msg) {
	// Ensure standard receive, not required for LinCAN>=0.3.1
	msg->flags = 0;
	int32_t response = 0;

	//do {
		// messages from device
		response = read(fd, msg, sizeof(canmsg_t));

		//TODO tämä looppi jumittaa
		//if((response < 0) && (errno == -EAGAIN)) {
			//response = 0;
		//}
	//} while(response == 0);

	// No new message
	if(response != sizeof(canmsg_t))
		return(-1);

	if(msg->flags & MSG_EXT){
		/* extended message */;
	}

	if(msg->flags & MSG_OVR){
		/* queue overflow condition */
		log.warn("queue overflow");
	}

	if(msg->flags & MSG_RTR){
		/* message is Remote Transmission Request */;
	}

	return(0);
}
}
