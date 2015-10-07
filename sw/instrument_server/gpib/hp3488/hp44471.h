/***********************************************************
 * Software: instrument server
 * Module:   hp44471 general purpose relay card headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 3.10.2012
 *
 ***********************************************************/

#ifndef HP44471_H_
#define HP44471_H_

#include "hp3488_option_base.h"

namespace instrument_server {

class hp44471: public instrument_server::hp3488_option_base {
public:
	hp44471();
	virtual ~hp44471();
protected:
private:
};

}

#endif /* HP44471_H_ */
