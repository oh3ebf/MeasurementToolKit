/***********************************************************
 * Software: instrument server
 * Module:   message interface includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 12.10.2012
 *
 ***********************************************************/

#ifndef MESSAGE_INTEFACE_CLASS_H_
#define MESSAGE_INTEFACE_CLASS_H_

#include "data_stream_interface.h"

namespace instrument_server {

using namespace std;
using namespace YAMI;

class message_interface {
public:
	enum fsm_states {
			IDLE = 0,
			CAPTURE = 1,
			GENERATE = 2,
			SWEEP = 3
		};

	typedef struct {
			string ip;				// target to send results
			string unique_id;		// id of command
			fsm_states cmd;			// command from enumeration
			uint8_t channels;		// channels to execute command
			uint8_t ch_cnt;			// number of used channels
			bool repeat;			// for cyclic commands
			bool active;			// measurement active status
			uint32_t repeat_cycle;	// cycle in ms
			uint32_t repeat_timer;	// counter for cycle  timer in ms
			void *p;				// application specific data can be stored here
		} fsm_command;

	virtual ~message_interface() {/* empty */}
	virtual void handle_msg(auto_ptr<IncomingMsg> msg_in) {/* empty */}
	virtual map<string, string> *get_config(void) {return(NULL);}
	virtual bool disconnect(string client_ip) {return(false);}
	virtual void fsm(data_stream_interface *d) {/* empty */}
protected:
private:
};
}

#endif /* MESSAGE_INTEFACE_CLASS_H_ */
