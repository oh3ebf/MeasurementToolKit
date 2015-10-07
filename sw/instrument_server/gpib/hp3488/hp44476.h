/***********************************************************
 * Software: instrument server
 * Module:   hp44476 microwave switch headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 4.10.2012
 *
 ***********************************************************/

#ifndef HP44476_H_
#define HP44476_H_

#include "hp3488_option_base.h"

namespace instrument_server {

class hp44476: public instrument_server::hp3488_option_base {
public:
	hp44476();
	virtual ~hp44476();
};

}

#endif /* HP44476_H_ */
