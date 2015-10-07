/*
 * lptBase.h
 *
 *  Created on: Sep 24, 2012
 *      Author: kim
 */

#ifndef LPTBASE_H_
#define LPTBASE_H_

#include <stdint.h>

namespace instrument_server {

class lpt_interface {
public:
	lpt_interface() {};
	virtual ~lpt_interface() {};
	virtual int data_write(uint8_t data) = 0;
	virtual int data_read(uint8_t *data) = 0;
	virtual int addr_read(uint8_t *addr) = 0;
	virtual int addr_write(uint8_t addr) = 0;
protected:
private:
};
}

#endif /* LPTBASE_H_ */
