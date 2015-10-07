/***********************************************************
 * Software: instrument server
 * Module:   hp44477 form C relay class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 4.10.2012
 *
 ***********************************************************/

#include <string>

#include <stdint.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "hp44477.h"

namespace instrument_server {

/* Function implements constructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44477::hp44477() {
	// initialize card variables
	this->id = 44477;
	this->name = "hp44477";
	this->description = "form C relay";

}

/* Function implements destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44477::~hp44477() {
	// TODO Auto-generated destructor stub
}

}
