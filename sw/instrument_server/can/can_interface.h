/***********************************************************
 * Software: instrument server
 * Module:   can device driver HAL includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 14.12.2012
 *
 ***********************************************************/

#ifndef CAN_INTERFACE_H_
#define CAN_INTERFACE_H_

namespace instrument_server {

uint8_t canSend(CAN_PORT port, Message *m);
CAN_PORT canOpen(s_BOARD *board, CO_Data * d);
int8_t canClose(CO_Data * d);
uint8_t canChangeBaudRate(CAN_PORT port, char* baud);
}

#endif /* CAN_INTERFACE_H_ */
