/***********************************************************
 * Software: instrument server
 * Module:   hp44472 dual 4-1 VHF switch class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 4.10.2012
 *
 ***********************************************************/

#ifndef HP44472_H_
#define HP44472_H_

#include "hp3488_option_base.h"

namespace instrument_server {

class hp44472: public instrument_server::hp3488_option_base {
public:
	hp44472();
	virtual ~hp44472();
};

}

#endif /* HP44472_H_ */
