/***********************************************************
 * Software: instrument server
 * Module:   hp44476 microwave switch class
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

#include "hp44476.h"

namespace instrument_server {

/* Function implements constructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44476::hp44476() {
	// initialize card variables
	this->id = 44476;
	this->name = "hp44476";
	this->description = "microwave switch";

}

/* Function implements destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44476::~hp44476() {
	// TODO Auto-generated destructor stub
}

}
