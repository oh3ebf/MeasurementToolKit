/***********************************************************
 * Software: instrument server
 * Module:   hp44470 relay multiplexer card class
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

#include "hp44470.h"

namespace instrument_server {


/* Function implements constructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44470::hp44470() {
	// initialize card variables
	this->id = 44470;
	this->name = "hp44470";
	this->description = "relay multiplexer";
}

/* Function implements destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44470::~hp44470() {

}

}
