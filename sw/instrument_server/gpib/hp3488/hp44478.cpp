/***********************************************************
 * Software: instrument server
 * Module:   hp44478 1.3GHz multiplexer class
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

#include "hp44478.h"

namespace instrument_server {

/* Function implements constructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44478::hp44478() {
  // initialize card variables
  this->id = 44478;
  this->name = "hp44478";
  this->description = "1.3GHz multiplexer";

}

/* Function implements destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp44478::~hp44478() {
  // TODO Auto-generated destructor stub
}

}
