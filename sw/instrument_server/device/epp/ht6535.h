/*
 * ht6535.h
 *
 *  Created on: Sep 24, 2012
 *      Author: kim
 */

#ifndef HT6535_H_
#define HT6535_H_

#include "lpt_interface.h"

namespace instrument_server {

class ht6535 : public lpt_interface {

public:
	ht6535(uint16_t lpt_base);
	virtual ~ht6535();
	virtual int data_write(uint8_t data);
	virtual int data_read(uint8_t *data);
	virtual int addr_read(uint8_t *addr);
	virtual int addr_write(uint8_t addr);
protected:
private:
	/* register defitions for HT6535 EPP port */

#define SPP_DATA_REG    0
#define SPP_STAT_REG    1
#define SPP_CNTR_REG    2
#define EPP_ADDR_REG    3
#define EPP_DATA_REG    4

	/* bit definitions */

#define EPP_TIME_OUT    0x01
#define EPP_INIT_BYTE   0x04

	/* SPP control register bits:
	 * strobe      -------------|
	 * auto_feed   ------------||
	 * init        -----------|||
	 * select_out  ----------||||
	 * irq_enable  ---------|||||
	 * bi_dir_ps2  --------||||||
	 *             -------|||||||
	 *             ------||||||||
	 *                   76543210
	 */

	/* SPP status register bits:
	 *           -------------|
	 *           ------------||
	 *           -----------|||
	 * error     ----------||||
	 * select_in ---------|||||
	 * paper_end --------||||||
	 * ack       -------|||||||
	 * busy      ------||||||||
	 *                 76543210
	 */

#define IO_READ(port)		\
		inb(port)

#define IO_WRITE(port,data)	\
		outb(data,port)

	int get_io_perm(void);

	int lpt_base_addr;
};

}
#endif /* HT6535_H_ */
