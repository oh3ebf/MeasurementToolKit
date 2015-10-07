/***********************************************************
 * Software: instrument server
 * Module:   hp3488 switching control unit class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 1.10.2012
 *
 ***********************************************************/

#include <string>
#include <stdint.h>
#include <stdlib.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "yami++.h"
#include "gpib.h"
#include "hp3488.h"
#include "message_interface.h"
#include "hp3488_option_base.h"
#include "hp44470.h"
#include "hp44471.h"
#include "hp44472.h"
#include "hp44473.h"
#include "hp44474.h"
#include "hp44475.h"
#include "hp44476.h"
#include "hp44477.h"
#include "hp44478.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

/* Function implements constructor for hp3488 class
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp3488::hp3488(string& dev_name, int32_t interface, int32_t pad, int32_t sad) : log(Category::getInstance("gpib")) {

  log.debug("HP3488 initializing");

  // store gpib name for later use
  gpib_name = dev_name;
  device_name = "HP3488";

#ifndef GPIB_TEST
  // open device
  if(this->open_device(interface, pad, sad)) {
    log.info("HP3488 found at address %d", pad);
  } else {
    log.error("HP3488 not found at address %d", pad);
    return;
  }
#endif

  // probe installed cards
  scan_slots();
}

/* Function implements constructor for hp3488 class
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp3488::hp3488(string& dev_name) : log(Category::getInstance("gpib")) {
  log.debug("HP3488 initializing");

  // store gpib name for later use
  gpib_name = dev_name;
  device_name = "HP3488";

#ifndef GPIB_TEST
  // open named device
  if(this->open_board(gpib_name)) {
    log.info("%s found", device_name.c_str());
  } else {
    log.error("%s not found", device_name.c_str());
    return;
  }
#endif

  // probe installed cards
  scan_slots();

}

/* Function implements class destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp3488::~hp3488() {

  // cleanup all card instances
  for(int i = 0; i < 5;i++) {
    if(slot_config[i] != NULL) {
      delete(slot_config[i]);
    }
  }
}

/* Function returns reference to slot configuration
 *
 * Parameters:
 * slot: card slot number
 *
 * Returns:
 * reference to slot description
 *
 */

hp3488_option_base& hp3488::get_card(uint8_t slot) {
  return(*slot_config[slot]);
}

/* Function returns equipment id
 *
 * Parameters:
 *
 * Returns:
 * string containing equipment id
 *
 */

string *hp3488::get_id(void) {
  // read instrument id
  string query = "ID?\n";
  this->write_string(query);
  return(this->read_string(64));
}

/* Function runs self test in hp3488
 *
 * Parameters:
 *
 * Returns:
 *
 */

void hp3488::test(void) {
  string query = "TEST\n";
  this->write_string(query);
}

/* Function initialize instrument to default values
 *
 * Parameters:
 *
 * Returns:
 *
 */

void hp3488::clear_device(void) {
  // send clear to bus
  this->clear(1000000);

  /* verify the instrument id is what we expect */
  string query = "ID?\n";
  this->write_string(query);

  // read response
  string *tmpbuf = this->read_string(64);

  log.debug("device: ", tmpbuf->c_str());
  // remove allocated string
  delete(tmpbuf);
  /*if (strncmp(tmpbuf, "HP3488A", 7) != 0) {
        fprintf(stderr, "%s: device id %s != %s\n", prog, tmpbuf, "HP3488A");
        exit(1);
    }*/
}

/* Function resets device power on state
 *
 * Parameters:
 *
 * Returns:
 *
 */

void hp3488::reset() {
  /* verify the instrument id is what we expect */
  string query = "RESET\n";
  this->write_string(query);
}

/* Function resets single card to power on state
 *
 * Parameters:
 * slot: slot number to send reset
 *
 * Returns:
 *
 */

void hp3488::card_reset(uint8_t slot) {
  this->writef((char *)"CRESET %d\n", slot);
}

/* Function returns card type from selected slot
 *
 * Parameters:
 * slot: slot number to query
 *
 * Returns:
 * card type as number
 *
 */

uint32_t hp3488::get_card_type(uint8_t slot) {
  int n = 44470;

#ifndef GPIB_TEST
  // write card type request
  this->writef((char *)"CTYPE %d\n", slot);

  // read card type from device
  string *tmpbuf = this->read_string(64);

  if(tmpbuf == NULL) {
	  // set default card type
	  log.error("failed to read card type from device");
	return(0);
  }

  // take number part of string
  string number = tmpbuf->substr(tmpbuf->size() - 7, 5);
  // convert to integer
  n = atoi(number.c_str());
  log.debug("Slot %d: card type %s", slot, tmpbuf->c_str());

  // remove data allocation
  delete(tmpbuf);
#endif

  return(n);
}

/* Function scans card slots and fills internal table
 *
 * Parameters:
 *
 * Returns:
 *
 */

void hp3488::scan_slots(void) {
  // probe all card slots for configuration
  for(int i = 0; i < 5;i++) {
    // get card information valid slot values 1-5
    switch(get_card_type(i + 1)) {
    case 44470:
      slot_config[i] = new hp44470();
      break;
    case 44471:
      slot_config[i] = new hp44471();
      break;
    case 44472:
      slot_config[i] = new hp44472();
      break;
    case 44473:
      slot_config[i] = new hp44473();
      break;
    case 44474:
      slot_config[i] = new hp44474();
      break;
    case 44475:
      slot_config[i] = new hp44475();
      break;
    case 44476:
      slot_config[i] = new hp44476();
      break;
    case 44477:
      slot_config[i] = new hp44477();
      break;
    case 44478:
      slot_config[i] = new hp44478();
      break;
    default:
    	slot_config[i] = new hp3488_option_base();
    	break;
    }

    log.info("Slot %d found card: %s", i + 1, slot_config[i]->getName().c_str());
  }
}

/* Function close given channel
 *
 * Parameters:
 * slot: option card slot number (1-5)
 * ch: channel to close
 *
 * Returns:
 *
 */

void hp3488::close_ch(uint8_t slot, uint8_t ch) {
  char cmd[64];

  // get command string
  sprintf(cmd, "CLOSE %d0%d\n", slot, ch);
  // write command to device
  this->write(cmd, sizeof(cmd));
}

/* Function close given channel
 *
 * Parameters:
 * ch: channel to close
 *
 * Returns:
 *
 */

void hp3488::close_ch(string ch) {
  //char cmd[64];
  string cmd = "CLOSE " + ch;

  // get command string
  //sprintf(cmd, "CLOSE %s\n", ch.c_str());


  // write command to device
  //this->write(cmd, sizeof(cmd));
  this->write_string(cmd);
}

/* Function open given channel
 *
 * Parameters:
 * slot: option card slot number (1-5)
 * ch: channel to close
 *
 * Returns:
 *
 */

void hp3488::open_ch(uint8_t slot, uint8_t ch) {
  char cmd[64];

  // get command string
  sprintf(cmd, "OPEN %d0%d\n", slot, ch);
  // write command to device
  this->write(cmd, sizeof(cmd));
}

/* Function open given channel
 *
 * Parameters:
 * ch: channel to close
 *
 * Returns:
 *
 */

void hp3488::open_ch(string ch) {
  //char cmd[64];
  string cmd = "OPEN " + ch;

  // get command string
  //sprintf(cmd, "OPEN %s\n", ch.c_str());
  // write command to device
  //this->write(cmd, sizeof(cmd));
  this->write_string(cmd);
}

/* Function checks if given channel is open or closed
 *
 * Parameters:
 * slot: option card slot number (1-5)
 * ch: channel to check
 *
 * Returns:
 * true if channel is closed, false otherwise
 *
 */

bool hp3488::view_ch(uint8_t slot, uint8_t ch) {
  char cmd[64];

  // get command string
  sprintf(cmd, "VIEW %d0%d\n", slot, ch);
  // write command to device
  this->write(cmd, sizeof(cmd));
  // read card type from device
  string *tmpbuf = this->read_string(64);

  // find out if relay is closed
  if(tmpbuf->compare("CLOSED 0") == 0) {
    // then return true and release memory
    delete(tmpbuf);
    return(true);
  } else {
    // otherwise false and release memory
    delete(tmpbuf);
    return(false);
  }
}

/* Function reads status register
 *
 * Parameters:
 *
 * Returns:
 * status in uint8
 *
 */

uint8_t hp3488::read_status(void) {
  uint8_t value = 0;

  // verify the instrument id is what we expect
  string query = "STATUS\n";
  this->write_string(query);
  // read status byte from device
  string *tmpbuf = this->read_string(64);
  // convert to integer
  value = atoi(tmpbuf->c_str());
  delete(tmpbuf);

  return(value);
}

/* Function reads error register
 *
 * Parameters:
 *
 * Returns:
 * error in uint8
 *
 */

uint8_t hp3488::read_error(void) {
  uint8_t error = 0;

  // verify the instrument id is what we expect
  string query = "ERROR\n";
  this->write_string(query);
  // read status byte from device
  string *tmpbuf = this->read_string(64);
  // parse error code
  error = atoi(tmpbuf->c_str());
  delete(tmpbuf);

  return(error);
}

}
