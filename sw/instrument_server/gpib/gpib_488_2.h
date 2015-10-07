/***********************************************************
 * Software: instrument server
 * Module:   gpib 488.2 standard implementation headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 26.11.2012
 *
 ***********************************************************/

#ifndef GPIB_488_2_H_
#define GPIB_488_2_H_

#include "gpib.h"

// TODO maskit ja bitti kuvaukset

namespace instrument_server {

using namespace std;
using namespace log4cpp;

class gpib_488_2: public gpib {
public:
	gpib_488_2();
	virtual ~gpib_488_2();
	string *get_id(void);
	void reset(void);
	bool self_test_query(void);
	void operation_complete(void);
	bool operation_complete_query(void);
	void wait_to_complete(void);
	void clear_status(void);
	void event_status(uint8_t mask);
	uint8_t event_status_query(void);
	uint8_t event_status_register_query(void);
	void service_request(uint8_t mask);
	uint8_t service_request_query(void);
	uint8_t read_status(void);
protected:
private:

};

}

#endif /* GPIB_488_2_H_ */
