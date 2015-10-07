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

#include <string>

#include <stdint.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "hp44472.h"

namespace instrument_server {

/* Function implements constructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44472::hp44472() {
	// initialize card variables
	this->id = 44472;
	this->name = "hp44472";
	this->description = "dual 4-1 VHF switch";

}

/* Function implements destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44472::~hp44472() {
	// TODO Auto-generated destructor stub
}

}
