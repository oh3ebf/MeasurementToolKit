/*
 * simulated_lpt.h
 *
 *  Created on: Sep 25, 2012
 *      Author: kim
 */

#ifndef SIMULATED_LPT_H_
#define SIMULATED_LPT_H_

#include "lpt_interface.h"

namespace instrument_server {

class simulated_lpt: public lpt_interface {
public:
	simulated_lpt();
	virtual ~simulated_lpt();
	virtual int data_write(uint8_t data);
	virtual int data_read(uint8_t *data);
	virtual int addr_read(uint8_t *addr);
	virtual int addr_write(uint8_t addr);
protected:
private:
	uint8_t accessed_addr;
};

}

#endif /* SIMULATED_LPT_H_ */
