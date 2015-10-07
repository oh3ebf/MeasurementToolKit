/***********************************************************
 * Software: instrument server
 * Module:   can device message includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 17.12.2012
 *
 ***********************************************************/

#ifndef _CANMSG_T_H
#define _CANMSG_T_H

namespace instrument_server {

/* Number of data bytes in one CAN message */
#define CAN_MSG_LENGTH 8

typedef struct timeval canmsg_tstamp_t;
typedef unsigned long canmsg_id_t;

/**
 * struct canmsg_t - structure representing CAN message
 * @flags:  message flags
 *      %MSG_RTR .. message is Remote Transmission Request,
 *	%MSG_EXT .. message with extended ID,
 *      %MSG_OVR .. indication of queue overflow condition,
 *	%MSG_LOCAL .. message originates from this node.
 * @cob:    communication object number (not used)
 * @id:     ID of CAN message
 * @timestamp: not used
 * @length: length of used data
 * @data:   data bytes buffer
 *
 * Header: canmsg.h
 */

typedef struct canmsg_t {
	int             flags;
	int             cob;
	canmsg_id_t     id;
	canmsg_tstamp_t timestamp;
	unsigned short  length;
	unsigned char   data[CAN_MSG_LENGTH];
} CAN_MSG;

/**
 * struct canfilt_t - structure for acceptance filter setup
 * @flags:  message flags
 *  %MSG_RTR .. message is Remote Transmission Request,
 *	%MSG_EXT .. message with extended ID,
 *  %MSG_OVR .. indication of queue overflow condition,
 *	%MSG_LOCAL .. message originates from this node.
 *	there are corresponding mask bits
 *	%MSG_RTR_MASK, %MSG_EXT_MASK, %MSG_LOCAL_MASK.
 *	%MSG_PROCESSLOCAL enables local messages processing in the
 *	combination with global setting
 * @queid:  CAN queue identification in the case of the multiple
 *	    queues per one user (open instance)
 * @cob:    communication object number (not used)
 * @id:     selected required value of cared ID id bits
 * @mask:   select bits significant for the comparison;
 *          1 .. take care about corresponding ID bit, 0 .. don't care
 *
 * Header: canmsg.h
 */

typedef struct canfilt_t {
	int		flags;
	int		queid;
	int		cob;
	canmsg_id_t	id;
	canmsg_id_t	mask;
} CAN_FILT;


/* Definitions to use for canmsg_t and canfilt_t flags */
#define MSG_RTR   (1 << 0)
#define MSG_OVR   (1 << 1)
#define MSG_EXT   (1 << 2)
#define MSG_LOCAL (1 << 3)

/* If you change above lines, check canque_filtid2internal function */

/* Additional definitions used for canfilt_t only */
#define MSG_FILT_MASK_SHIFT   8
#define MSG_RTR_MASK   (MSG_RTR << MSG_FILT_MASK_SHIFT)
#define MSG_EXT_MASK   (MSG_EXT << MSG_FILT_MASK_SHIFT)
#define MSG_LOCAL_MASK (MSG_LOCAL << MSG_FILT_MASK_SHIFT)
#define MSG_PROCESSLOCAL (MSG_OVR << MSG_FILT_MASK_SHIFT)

/* Can message ID mask */
#define MSG_ID_MASK ((1l << 29) - 1)

}
#endif /*_CANMSG_T_H*/
