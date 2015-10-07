/***********************************************************
 * Software: instrument server
 * Module:   hp44478 1.3GHz multiplexer headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 4.10.2012
 *
 ***********************************************************/

#ifndef HP44478_H_
#define HP44478_H_

#include "hp3488_option_base.h"

namespace instrument_server {

class hp44478: public instrument_server::hp3488_option_base {
public:
	hp44478();
	virtual ~hp44478();
};

}

#endif /* HP44478_H_ */
