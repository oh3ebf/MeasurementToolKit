/***********************************************************
 * Software: instrument server
 * Module:   hp44473 4x4 matrix switch headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 4.10.2012
 *
 ***********************************************************/

#ifndef HP44473_H_
#define HP44473_H_

#include "hp3488_option_base.h"

namespace instrument_server {

class hp44473: public instrument_server::hp3488_option_base {
public:
	hp44473();
	virtual ~hp44473();
};

}

#endif /* HP44473_H_ */
