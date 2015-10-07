/*
 * ht6535.cpp
 *
 *  Created on: Sep 24, 2012
 *      Author: kim
 */


#include <sys/io.h>
#include <inttypes.h>
#include "ht6535.h"

namespace instrument_server {

using namespace std;


ht6535::ht6535(uint16_t lpt_base) {
	/* set base address of lpt port */
	lpt_base_addr = lpt_base;

	/* give access rights to registers */
	if(get_io_perm() != 0);
	//return(-1);

	/* reset target device */
	IO_WRITE(SPP_CNTR_REG + lpt_base_addr, EPP_INIT_BYTE);
	/* reset time out bit */
	IO_WRITE(SPP_STAT_REG + lpt_base_addr, 0x00);

	/* check time out status */
	if((IO_READ(SPP_STAT_REG + lpt_base_addr) & EPP_TIME_OUT) == 1);
	//return(-1);
}

ht6535::~ht6535() {
	// TODO Auto-generated destructor stub
}

/* Function sets I7O permissions to LPT port registers
 *
 * Parameters:
 *
 * Returns:
 * 1: on success
 * -1: on failure
 *
 */

int ht6535::get_io_perm(void) {

	/* enable access to data register */
	if(ioperm(SPP_DATA_REG + lpt_base_addr, 1, 1))
		return(-1);
	/* enable access to status register */
	if(ioperm(SPP_STAT_REG + lpt_base_addr, 1, 1) < 0)
		return(-1);
	/* enable access to control register */
	if(ioperm(SPP_CNTR_REG + lpt_base_addr, 1, 1) < 0)
		return(-1);
	/* enable access to address register */
	if(ioperm(EPP_ADDR_REG + lpt_base_addr, 1, 1) < 0)
		return(-1);
	/* enable access to data register */
	if(ioperm(EPP_DATA_REG + lpt_base_addr, 1, 1) < 0)
		return(-1);

	return(1);
}

/* Function writes data byte to lpt port
 *
 * Parameters:
 * data: byte to write
 *
 * Returns:
 * 1: on success
 * -1: on failure
 */

int ht6535::data_write(uint8_t data) {
	/* check timeout status from lpt port */
	if((IO_READ(SPP_STAT_REG + lpt_base_addr) & EPP_TIME_OUT) == 1)
		return(-1);
	else
		/* write data to lpt port */
		IO_WRITE(EPP_DATA_REG + lpt_base_addr,data);
	return(1);
}

/* Function reads data byte from EPP device
 *
 * Parameters:
 * data: pointer to data byte strorage
 *
 * Returns:
 * 1: on success
 * -1: on failure
 */

int ht6535::data_read(uint8_t *data) {
	/* check timeout status from lpt port */
	if((IO_READ(SPP_STAT_REG + lpt_base_addr) & EPP_TIME_OUT) == 1)
		return(-1);
	else
		/* read data from lpt port */
		*data = IO_READ(EPP_DATA_REG + lpt_base_addr);

	return(1);
}

/* Function reads address byte from lpt port
 *
 * Parameters:
 * addr: pointer to address byte strorage
 *
 * Returns:
 * 1: on success
 * -1: on failure
 */

int ht6535::addr_read(uint8_t *addr) {
	/* check timeout status from lpt port */
	if((IO_READ(SPP_STAT_REG + lpt_base_addr) & EPP_TIME_OUT) == 1)
		return(-1);
	else
		/* read address byte from lpt port */
		*addr = IO_READ(EPP_ADDR_REG + lpt_base_addr);

	return(1);
}

/* Function writes address byte to lpt port
 *
 * Parameters:
 *
 * Returns:
 * 1: on success
 * -1: on failure
 */

int ht6535::addr_write(uint8_t addr) {
	/* check timeout status from lpt port */
	if((IO_READ(SPP_STAT_REG + lpt_base_addr) & EPP_TIME_OUT) == 1)
		return(-1);
	else
		/* write address byte to lpt port */
		IO_WRITE(EPP_ADDR_REG + lpt_base_addr,addr);

	return(1);
}
}
