/***********************************************************
 * Software: instrument server
 * Module:   hp44475 breadboard headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 4.10.2012
 *
 ***********************************************************/

#ifndef HP44475_H_
#define HP44475_H_

#include "hp3488_option_base.h"

namespace instrument_server {

class hp44475: public instrument_server::hp3488_option_base {
public:
	hp44475();
	virtual ~hp44475();
};

}

#endif /* HP44475_H_ */
