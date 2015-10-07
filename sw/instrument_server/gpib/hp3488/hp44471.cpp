/***********************************************************
 * Software: instrument server
 * Module:   hp44471 general purpose relay card class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 3.10.2012
 *
 ***********************************************************/

#include <string>

#include <stdint.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "hp44471.h"

namespace instrument_server {

/* Function implements constructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44471::hp44471() {
	// initialize card variables
		this->id = 44471;
		this->name = "hp44471";
		this->description = "general purpose relay";
}

/* Function implements destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44471::~hp44471() {
	// TODO Auto-generated destructor stub
}

}
