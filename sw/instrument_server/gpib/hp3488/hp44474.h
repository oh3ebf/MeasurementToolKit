/***********************************************************
 * Software: instrument server
 * Module:   hp44474 digital I/O headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 4.10.2012
 *
 ***********************************************************/

#ifndef HP44474_H_
#define HP44474_H_

#include "hp3488_option_base.h"

namespace instrument_server {

class hp44474: public instrument_server::hp3488_option_base {
public:
	hp44474();
	virtual ~hp44474();
};

}

#endif /* HP44474_H_ */
