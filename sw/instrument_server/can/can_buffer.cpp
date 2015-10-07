/***********************************************************
 * Software: instrument server
 * Module:   Can bus message buffer headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 3.5.2013
 *
 ***********************************************************/

#include <stdint.h>
#include <string.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "canmsg.h"
#include "can_buffer.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;

/* Function implements constructor for class
 *
 * Parameters:
 * size: size of buffer
 *
 * Returns:
 *
 */

can_buffer::can_buffer(uint32_t size) : log(Category::getInstance("can")),
		buffer_max(size), rx_index(0), read_index(0), buffer_full(false), size(0) {
	log.debug("can buffer instance created");

	// resize buffer
	rx_buffer.resize(buffer_max);
}

/* Function implements destructor for class
 *
 * Parameters:
 *
 * Returns:
 *
 */

can_buffer::~can_buffer() {
	// TODO Auto-generated destructor stub
}

/* Function inserts message to buffer
 *
 * Parameters:
 * msg: message to insert
 *
 * Returns:
 * true if ok, otherwise false
 *
 */

bool can_buffer::insert(CAN_MSG msg) {
	// add message to buffer
	rx_buffer[rx_index] = msg;
	// next location
	rx_index++;

	// check bounds
	if(rx_index >= buffer_max) {
		rx_index = 0;
	}

	// reach read pointer, mark full
	if(rx_index == read_index) {
		buffer_full = true;
	}

	// increment size if buffer is not full
	if(buffer_full == false) {
		size++;
	}

	//log.debug("%ld.%06ld: %d %x %x %x %x %x %x %x %x", msg.timestamp.tv_sec, msg.timestamp.tv_usec, msg.id,
	//						msg.data[0],msg.data[1],msg.data[2],msg.data[3],msg.data[4],msg.data[5],msg.data[6],msg.data[7]);
	//log.debug("%8x.%8x: %4x %x %x %x %x %x %x %x %x", msg.timestamp.tv_sec, msg.timestamp.tv_usec, msg.id,
	//						msg.data[0],msg.data[1],msg.data[2],msg.data[3],msg.data[4],msg.data[5],msg.data[6],msg.data[7]);


	return(true);
}

/* Function returns message from index
 *
 * Parameters:
 * idx: vector index
 *
 * Returns:
 * one message
 *
 */

CAN_MSG& can_buffer::operator[](uint32_t idx) {
	return(rx_buffer[idx]);
}

/* Function returns message from index
 *
 * Parameters:
 * idx: vector index
 *
 * Returns:
 * one message
 *
 */

const CAN_MSG& can_buffer::operator[](uint32_t idx) const {
	return(rx_buffer[idx]);
}

/* Function returns message from end of buffer
 *
 * Parameters:
 *
 * Returns:
 * one message
 *
 */

CAN_MSG can_buffer::pop_back(void) {
	// read message from end of buffer
	CAN_MSG m = rx_buffer[read_index++];

	// decrement size
	size--;
	if(size < 0) {
		size = 0;
	}

	// check array bounds
	if(read_index >= buffer_max) {
		read_index = 0;
	}

	return(m);
}

/* Function returns message from end of buffer
 *
 * Parameters:
 * buf: pointer to data buffer
 *
 * Returns:
 * one message in char buffer
 *
 */

char* can_buffer::pop_back_bytes(char *buf) {
	// read message from end of buffer
	CAN_MSG m = rx_buffer[read_index++];

	// decrement size
	size--;

	// mark not full
	if(size < buffer_max) {
		buffer_full = false;
	}

	// lower minimum limit
	if(size < 0) {
		size = 0;
	}

	// check array bounds
	if(read_index >= buffer_max) {
		read_index = 0;
	}

	// copy data to buffer
	memcpy((void*)buf, (void*)&m.timestamp.tv_sec, sizeof(long));
	buf += sizeof(long);
	memcpy((void*)buf, (void*)&m.timestamp.tv_usec, sizeof(long));
	buf += sizeof(long);
	memcpy((void*)buf, (void*)&m.id, sizeof(canmsg_id_t));
	buf += sizeof(canmsg_id_t);
	memcpy((void*)buf, (void*)&m.flags, sizeof(int32_t));
	buf += sizeof(int32_t);
	memcpy((void*)buf, (void*)&m.length, sizeof(uint16_t));
	buf += sizeof(uint16_t);
	memcpy((void*)buf, (void*)&m.data, sizeof(uint8_t) * 8);

	return(buf);
}

/* Function returns buffer state
 *
 * Parameters:
 *
 * Returns:
 * true if any messages left, otherwise false
 *
 */

bool can_buffer::is_empty(void) {
	// return buffer status
	if(size > 0)
		return(true);

	return(false);
}

/* Function returns buffer size
 *
 * Parameters:
 *
 * Returns:
 * current size
 *
 */

uint32_t can_buffer::get_size(void) {
	return(size);
}

/* Function clears buffer
 *
 * Parameters:
 *
 * Returns:
 *
 */

void can_buffer::clear(void) {
	rx_buffer.clear();
}

}
