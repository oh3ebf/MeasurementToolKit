/***********************************************************
 * Software: instrument server
 * Module:   hp44470 relay multiplexer card headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 3.10.2012
 *
 ***********************************************************/

#ifndef HP44470_H_
#define HP44470_H_

#include "hp3488_option_base.h"

namespace instrument_server {

class hp44470: public hp3488_option_base {
public:
	hp44470();
	virtual ~hp44470();
protected:
private:
};

}

#endif /* HP44470_H_ */
