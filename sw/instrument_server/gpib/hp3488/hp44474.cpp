/***********************************************************
 * Software: instrument server
 * Module:   hp44474 digital I/O class
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

#include "hp44474.h"

namespace instrument_server {

/* Function implements constructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44474::hp44474() {
	// initialize card variables
	this->id = 44474;
	this->name = "hp44474";
	this->description = "digital I/O";
}

/* Function implements destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44474::~hp44474() {
	// TODO Auto-generated destructor stub
}

}
