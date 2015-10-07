/*
 * LptInterface.h
 *
 *  Created on: Sep 24, 2012
 *      Author: kim
 */

#ifndef LPTINTERFACE_H_
#define LPTINTERFACE_H_

namespace instument_server {

class epp {
	/* lpt port definitions */
	#define LPT_1        0x3BC
	#define LPT_2        0x378
	#define LPT_3        0x278
	#define LPT_4        0x268

	#define DUMMY_ADDRESS            0xff

	typedef struct _config {
	  uint16_t lpt_base;
	  //uint8_t card_number;
	  //uint8_t ram_card_base[PODS_MAX];
	} config;

public:
	virtual ~epp();
	epp(config *cfg, uint8_t mode);
protected:

private:


#define EPP_DATA_WRITE(data)     \
   if(epp_data_write(data) < 0)  \
     return(-1);

#define EPP_ADDR_WRITE(addr)     \
   if(epp_addr_write(addr) < 0)  \
     return(-1);
};

}
#endif /* LPTINTERFACE_H_ */
