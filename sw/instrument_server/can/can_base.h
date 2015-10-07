/***********************************************************
 * Software: instrument server
 * Module:   can device interface base class includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 14.12.2012
 *
 ***********************************************************/

#ifndef CAN_BASE_H_
#define CAN_BASE_H_

#include "canmsg.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;

class can_base {
public:
	can_base();
	can_base(string driver);
	virtual ~can_base();
	virtual int get_file_handle(void);
	virtual string get_bus_name(void);
	virtual string get_driver_name(void);
	virtual int32_t can_open(string name, int32_t baudrate) = 0;
	virtual int8_t can_close(void) = 0;
	virtual int8_t can_send(CAN_MSG *m) = 0;
	virtual int32_t can_read(CAN_MSG *m) = 0;
	virtual bool isReady(void);
	//virtual uint8_t can_set_baud_rate(CAN_PORT port, char* baud);
protected:
	Category &log;
	int32_t fd;
	string device_name;
	string device_prefix;
	string driver_name;
private:

};

}

#endif /* CAN_BASE_H_ */
