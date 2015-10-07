/***********************************************************
 * Software: instrument server
 * Module:   hp3488 option card base class
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

#include "hp3488_option_base.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;

/* Function implements constructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp3488_option_base::hp3488_option_base() : id(0), name("empty"), description("empty slot"),
    log(Category::getInstance("gpib")) {
}

/* Function implements destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp3488_option_base::~hp3488_option_base() {

}

/* Function returns string description of option card
 *
 * Parameters:
 *
 * Returns:
 * description of card as string
 *
 */

string hp3488_option_base::getDescription() const {
  return description;
}

/* Function returns option card id
 *
 * Parameters:
 *
 * Returns:
 * id number of option card
 *
 */

uint32_t hp3488_option_base::getId() const {
  return id;
}

/* Function returns option card name
 *
 * Parameters:
 *
 * Returns:
 * card name
 *
 */

string hp3488_option_base::getName() const {
  return name;
}

/* Function sets option  card id
 *
 * Parameters:
 * id: card id number
 *
 * Returns:
 *
 */

void hp3488_option_base::setId(uint32_t id) {
  this->id = id;
}

/* Function returns command string to option card
 *
 * Parameters:
 * str: command string
 * ch: channel to command
 *
 * Returns:
 * true if valid values, false otherwise
 *
 */

bool hp3488_option_base::get_cmd(string& str, uint8_t ch) {
	return(true);
}

}
