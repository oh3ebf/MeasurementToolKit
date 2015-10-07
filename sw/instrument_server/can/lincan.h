/***********************************************************
 * Software: instrument server
 * Module:   lincan device interface class includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 14.12.2012
 *
 ***********************************************************/

#ifndef LINCAN_H_
#define LINCAN_H_

#include "can_base.h"

namespace instrument_server {

using namespace std;

class lincan: public instrument_server::can_base {
public:
	/* CAN ioctl magic number */
	#define CAN_IOC_MAGIC 'd'

	typedef unsigned long bittiming_t;
	typedef unsigned short channel_t;
	typedef struct {
			long flags;
			long baudrate;
			long sjw;
			long sample_pt;
		} CAN_BAUDPARAMS;

	/* CAN ioctl functions */
	#define CAN_DRV_QUERY _IO(CAN_IOC_MAGIC, 0)
	#define CAN_DRV_QRY_BRANCH    0	/* returns driver branch value - "LINC" for LinCAN driver */
	#define CAN_DRV_QRY_VERSION   1	/* returns driver version as (major<<16) | (minor<<8) | patch */
	#define CAN_DRV_QRY_MSGFORMAT 2	/* format of canmsg_t structure */

	#define CMD_START _IOW(CAN_IOC_MAGIC, 1, channel_t)
	#define CMD_STOP _IOW(CAN_IOC_MAGIC, 2, channel_t)
	//#define CMD_RESET 3

	#define CONF_BAUD _IOW(CAN_IOC_MAGIC, 4, bittiming_t)
	//#define CONF_ACCM
	//#define CONF_XTDACCM
	//#define CONF_TIMING
	//#define CONF_OMODE
	#define CONF_FILTER _IOW(CAN_IOC_MAGIC, 8, unsigned char)

	//#define CONF_FENABLE
	//#define CONF_FDISABLE

	#define STAT _IO(CAN_IOC_MAGIC, 9)
	#define CANQUE_FILTER _IOW(CAN_IOC_MAGIC, 10, struct canfilt_t)
	#define CANQUE_FLUSH  _IO(CAN_IOC_MAGIC, 11)
	#define CONF_BAUDPARAMS  _IOW(CAN_IOC_MAGIC, 11, CAN_BAUDPARAMS)
	#define CANRTR_READ  _IOWR(CAN_IOC_MAGIC, 12, struct canmsg_t)

	lincan();
	virtual ~lincan();
	//virtual int get_file_handle(void);
	virtual int32_t can_open(string name, int32_t baudrate);
	virtual int8_t can_close(void);
	virtual int32_t translate_baud_rate(string baudrate);
	virtual int8_t can_send(CAN_MSG *m);
	virtual int32_t can_read(CAN_MSG *m);
protected:
private:
};

}

#endif /* LINCAN_H_ */
