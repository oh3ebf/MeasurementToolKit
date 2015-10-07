/***********************************************************
 * Software: instrument server
 * Module:   hp44475 breadboard class
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
#include "hp44475.h"

namespace instrument_server {

/* Function implements constructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44475::hp44475() {
	// initialize card variables
	this->id = 44475;
	this->name = "hp44475";
	this->description = "breadboard";
}

/* Function implements destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44475::~hp44475() {
	// TODO Auto-generated destructor stub
}

}
