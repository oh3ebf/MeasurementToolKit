/***********************************************************
 * Software: instrument server
 * Module:   hp44473 4x4 matrix switch class
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

#include "hp44473.h"

namespace instrument_server {

/* Function implements constructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44473::hp44473() {
	// initialize card variables
	this->id = 44473;
	this->name = "hp44473";
	this->description = "4x4 matrix switch";
}

/* Function implements destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44473::~hp44473() {
	// TODO Auto-generated destructor stub
}

}
