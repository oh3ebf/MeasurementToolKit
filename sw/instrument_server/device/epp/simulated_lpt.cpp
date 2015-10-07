/*
 * simulated_lpt.cpp
 *
 *  Created on: Sep 25, 2012
 *      Author: kim
 */

#include <iostream>
#include <inttypes.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include "simulated_lpt.h"


namespace instrument_server {
using namespace std;

simulated_lpt::simulated_lpt() {
	accessed_addr = 0;

}

simulated_lpt::~simulated_lpt() {
	/* set stdin nonblocking mode */
	  if(fcntl(STDIN_FILENO, F_SETFL, O_NONBLOCK) < 0);
	    //return(-1);
}

/*
 *
 *
 *
 *
 *
 */

int simulated_lpt::data_write(uint8_t data) {
/*
  switch(accessed_addr) {
  case COUNTER_CNTR_REG_0:
    cout << "Counter control register 0 " << accessed_addr << ":" << data <<" write" << endl;
    break;
  case COUNTER_TRIG_WIN_REG:
    cout << "Counter trig window register " << accessed_addr << ":" << data << " write" << endl;
    break;
  case COUNTER_CLK_DIV_REG:
    cout << "Counter clock divider register " << accessed_addr << ":" << data << " write" << endl;
    break;
  case COUNTER_CNTR_REG_1:
    cout << "Counter control register 1 " <<  accessed_addr << ":" << data << " write" << endl;
    break;
  case COUNTER_STAT_REG:
    cout << "Counter status register " << accessed_addr << ":" << data " write" << endl;
    break;
  case RAM_EXT_TRIG_REG_0:
    break;
  case RAM_EXT_TRIG_REG_1:
    break;
  case RAM_EXT_TRIG_REG_2:
    break;
  case RAM_EXT_TRIG_REG_3:
    break;
  default:
    cout << "Not defined address " << accessed_addr << endl;
    break;
  }
*/
  return(1);
}

/*
 *
 *
 *
 *
 */

int simulated_lpt::data_read(uint8_t *data) {

  switch(getchar()) {
  case '1':
    *data = 0x20;
    break;
  }

/*
  switch(accessed_addr) {
  case COUNTER_STAT_REG:
    cout << "Counter status register " << accessed_addr << ":" << *data << " read" << endl;
    break;
  case COUNTER_CNTR_REG_0:
  case COUNTER_TRIG_WIN_REG:
  case COUNTER_CLK_DIV_REG:
  case COUNTER_CNTR_REG_1:
  case RAM_EXT_TRIG_REG_0:
  case RAM_EXT_TRIG_REG_1:
  case RAM_EXT_TRIG_REG_2:
  case RAM_EXT_TRIG_REG_3:
    cout << "Access to write only register" << endl;
    break;
  default:
    cout << "Not defined address " << accessed_addr << endl;
    break;
  }
*/
  return(1);
}

/*
 *
 *
 *
 *
 */

int simulated_lpt::addr_read(uint8_t *addr) {
  /* value of last accessed memory address */
  *addr = accessed_addr;


  return(1);
}

/*
 *
 *
 *
 *
 *
 */

int simulated_lpt::addr_write(uint8_t addr) {
  /* save accessed memory address */
  accessed_addr = addr;

  return(1);
}

}
