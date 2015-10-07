/***********************************************************
 * Software: instrument server
 * Module:   hp44477 form C relay headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 4.10.2012
 *
 ***********************************************************/

#ifndef HP44477_H_
#define HP44477_H_

#include "hp3488_option_base.h"

namespace instrument_server {

class hp44477: public instrument_server::hp3488_option_base {
public:
	hp44477();
	virtual ~hp44477();
};

}

#endif /* HP44477_H_ */
