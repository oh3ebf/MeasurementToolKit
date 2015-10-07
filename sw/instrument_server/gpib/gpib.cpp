/***********************************************************
 * Software: instrument server
 * Module:   gpib 488.1 base class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 27.9.2012
 *
 ***********************************************************/

#include <string>
#include <stdint.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>
#include <gpib/ib.h>

#include "gpib.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;

/* Function implements class constructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

gpib::gpib() : log(Category::getInstance("gpib")), handle(-1), interface(0), pad(0), sad(0),
    timeout_value(T3s), connection_status(false) {

  if (pthread_mutex_init(&gpib_mutex, NULL) != 0) {
    log.error("mutex init failed");
  }

#ifdef GPIB_TEST
  connection_status = true;
#endif

}

/* Function implements class destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

gpib::~gpib() {

}

/* Function opens device on gpib bus
 *
 * Parameters:
 * interface: gpib board minor number
 * pad: primary gpib address
 * sad: secondary gpib address
 *
 * Returns:
 *
 */

bool gpib::open_device(int32_t interface, int32_t pad, int32_t sad) {
  /* default: board=0, sad=0, tmo=T30s, eot=1, bin=0, reos=0, xeos=0, eos=0xa */

  // set local variables
  this->interface = interface;
  this->pad = pad;
  this->sad = sad;

  // set empty name
  gpib_name = "";

  // try to open device
  handle = ibdev(interface, pad, sad, T30s, 1, 0x0a);

  if(handle < 0) {
    log.error("ibdev(%d,%d,0x%x) failed: no GPIB support\n", interface, pad, sad);
    return(false);
  } else {
    // check presence on bus
    if(scan_device()) {
      connection_status = true;
    }

    return(connection_status);
  }
}

/* Function opens device on gpib bus with existing parameters
 *
 * Parameters:
 *
 * Returns:
 * true on success, otherwise fail
 *
 */

bool gpib::open_device(void) {
  /* default: board=0, sad=0, tmo=T30s, eot=1, bin=0, reos=0, xeos=0, eos=0xa */

  // set empty name
  gpib_name = "";

  // try to open device
  handle = ibdev(interface, pad, sad, T30s, 1, 0x0a);

  if(handle < 0) {
    log.error("ibdev(%d,%d,0x%x) failed: no GPIB support\n", interface, pad, sad);
    return(false);
  } else {
    // check presence on bus
    if(scan_device()) {
      connection_status = true;
    }

    return(connection_status);
  }
}

/* Function opens named device on gpib bus
 *
 * Parameters:
 * name: name of device or board to open
 *
 * Returns:
 * true on success, otherwise fail
 *
 */

bool gpib::open_board(string& name) {
  int32_t status = 0;

  log.info( "trying to open board named '%s'", name.c_str() );
  gpib_name = name;

  // get device by name
  handle = ibfind(name.c_str());

  if(handle < 0) {
    log.error("Failed to open board %s", name.c_str());
    return(false);
  } else {
    // get device primary address
    ibask(handle, IbaPAD, &status);
    log.debug("PAD: %d", status);
    pad = status;

    // get device primary address
    ibask(handle, IbaSAD, &status);
    log.debug("SAD: %d", status);
    sad = status;

    // check presence on bus
    if(scan_device()) {
      connection_status = true;
    }

    return(connection_status);  }
}

/* Function close gpib device
 *
 * Parameters:
 *
 * Returns:
 *
 */

void gpib::close(void) {
  // close open connection if found
  if((connection_status == true) || (handle != -1)) {
    ibonl(handle, 0);
    connection_status = false;
    handle = -1;
    log.debug("Closed connection to %d", pad);
  }
}

/* Function returns connection status
 *
 * Parameters:
 *
 * Returns:
 * true if device is connected, otherwise false
 *
 */

bool gpib::isConnected(void) {
  return(connection_status);
}

/* Function returns equipment gpib name
 *
 * Parameters:
 *
 * Returns:
 * string containing equipment gpib name
 *
 */

string& gpib::get_gpib_name(void) {
  return(gpib_name);
}

/* Function returns equipment manufacturer name
 *
 * Parameters:
 *
 * Returns:
 * string containing equipment manufacturer name
 *
 */

string& gpib::get_device_name(void) {
  return(device_name);
}

/* Function detects presence of single devise on bus
 *
 * Parameters:
 * pad: device primary address
 *
 * Returns:
 * true if device found, othervise false
 *
 */

bool gpib::scan_device(void) {
  short status = 0;

  // try to find device from bus
  ibln(handle, pad, sad, &status);

  if(status != 0) {
    return(true);
  }

  return(false);
}

/* Function implements low level write functionality
 *
 * Parameters:
 * buf: buffer containing data
 * len: buffer size
 *
 * Returns:
 *
 */

int32_t gpib::bus_write(char *buf, int len) {
  // lock gpib
  pthread_mutex_lock(&gpib_mutex);

  ibwrt(handle, buf, len);

  // unlock gpib
  pthread_mutex_unlock(&gpib_mutex);

  if(ibsta & TIMO) {
    log.error("ibwrt timeout");
    return(-1);
  }

  if(ibsta & ERR) {
    log.error("ibwrt error %d\n", iberr);
    return(-1);
  }

  return(0);
}

/* Function function implements low level read functionality
 *
 * Parameters:
 * buf: buffer to read data
 * len: number of bytes to read
 *
 * Returns:
 * read byte count
 *
 */

int32_t gpib::bus_read(char *buf, int len) {
  int32_t count = 0;

  // lock gpib
  pthread_mutex_lock(&gpib_mutex);

  do {
    ibrd(handle, buf + count, len - count);
    if(ibsta & TIMO) {
      log.error("ibrd timeout");

      // unlock gpib
      pthread_mutex_unlock(&gpib_mutex);

      return(-1);
    }

    if(ibsta & ERR) {
      log.error("ibrd error %d", iberr);

      // unlock gpib
      pthread_mutex_unlock(&gpib_mutex);
      return(-1);
    }

    count += ibcnt;
  } while (count < len && !(ibsta & END));

  // unlock gpib
  pthread_mutex_unlock(&gpib_mutex);

  if(!(ibsta & END)) {
    log.error("read buffer too small");
    return(-1);
  }

  log.debug("bus read: %d bytes from device: %d", count, pad);

  return(count);
}

/* Function implements simple data read
 *
 * Parameters:
 *  buf: buffer to read data
 *  len: number of bytes to read
 *
 * Returns:
 * count of bytes read
 *
 */

int32_t	gpib::read(char *buf, int len) {
  int count = 0;

  // TODO temp looppi datan syöttöön
  count = bus_read(buf, len);
  log.debug("target read %d bytes", count);

  //_serial_poll(gd, "gpib_rd");

  return count;
}

/* Function implements string read
 *
 * Parameters:
 * len: byte count to read
 *
 * Returns:
 * pointer to read string
 *
 */

string *gpib::read_string(uint32_t len) {
  int count = 0;
  char buf[len];

  // read raw data and add string end mark
  count = bus_read(buf, len - 1);

  // check result
  if(count == -1)
    return(NULL);

  buf[count] = '\0';

  string *tmp = new string(buf);

  //_zap_trailing_terminators(buf);
  log.debug("target read: %s", buf);

  //_serial_poll(gd, "gpib_rdstr");
  return(tmp);
}

/* Function implements formatted read
 *
 * Parameters:
 * fmt: formatting arguments
 *
 * Returns:
 * byte count read
 *
 */
// TODO mitä tämän pitäisi tehdä / palauttaa
int32_t	gpib::readf(char *fmt, ...) {
  va_list ap;
  char buf[1024];
  int count = 0;
  int n;

  // read data
  count = bus_read(buf, sizeof(buf) - 1);
  // check result
  if(count == -1)
    return(count);

  buf[count] = '\0';
  //_zap_trailing_terminators(buf);

  va_start(ap, fmt);
  n = vsscanf(buf, fmt, ap);
  va_end(ap);

  log.error("target read %s");
  //_serial_poll(gd, "gpib_rdf");

  return n;
}

/* Function writes data buffer to device
 *
 * Parameters:
 * buf: buffer containing data
 * len: size of buffer
 *
 * Returns:
 *
 */

int32_t gpib::write(void *buf, int len) {
  int32_t response = bus_write((char *)buf, len);

  log.debug("target write %s, %d bytes", buf, len);

  //_serial_poll(gd, "gpib_wrt");
  return(response);
}

/* Function writes string to device
 *
 * Parameters:
 * str: reference to string
 *
 * Returns:
 *
 */

int32_t gpib::write_string(string& str) {
  int32_t response = bus_write((char *)str.c_str(), str.length());

  log.debug("target write: %s", str.c_str());
  //_serial_poll(gd, "gpib_wrtstr");

  return(response);
}

/* Function writes formatted string to device
 *
 * Parameters:
 * fmt: fomatting arguments and data
 *
 * Returns:
 *
 */

int32_t gpib::writef(const char *fmt, ...) {
  va_list ap;
  char *tmp;
  string str;
  int32_t response = 0;

  va_start(ap, fmt);
  vasprintf(&tmp, fmt, ap);
  str = tmp;
  va_end(ap);

  log.debug("target write: %s", str.c_str());

  // write date to gpib device
  response = bus_write((char *)str.c_str(), str.length());
  //_serial_poll(gd, "gpib_wrtf");

  return(response);
}

/* Function
 *
 * Parameters:
 *
 * Returns:
 *
 */
//TODO onko tämä todella tarpeellinen???
int32_t gpib::query(string& str, char *buf, int len) {
  int count;

  bus_write((char *)str.c_str(), str.length());
  log.debug("target write: %s", str.c_str());

  // TODO pitäisikö käyttää dynaamisempaa tietotyyppiä
  count = bus_read(buf, len);

  if(count < len && ((char *)buf)[count - 1] != '\0')
    ((char *)buf)[count++] = '\0';

  log.debug("target read: %s", buf);

  //_serial_poll(gd, "gpib_qry");

  return count;
}

/* Function puts device to local mode
 *
 * Parameters:
 *
 * Returns:
 *
 */

int32_t gpib::go_local(void) {
  int32_t response = 0;

  // go local mode
  ibloc(handle);

  // check status
  if((ibsta & ERR)) {
    log.error("ibloc error %d", iberr);
    response = -1;
  }

  log.debug("target %d local mode", pad);

  //  _serial_poll(gd, "gpib_loc");

  return(response);
}

/* Function send clear to device
 *
 * Parameters:
 * usec: timeout value
 *
 * Returns:
 *
 */


int32_t gpib::clear(uint32_t usec) {
  int32_t response = 0;

  // clear device
  ibclr(handle);

  if((ibsta & TIMO)) {
    log.error("ibclr timeout");
    response = -1;
  }

  if ((ibsta & ERR)) {
    log.error("ibclr error %d", iberr);
    response = -1;
  }

  log.debug("target device %d cleared", pad);

  //_serial_poll(gd, "gpib_clr");

  return(response);
}

/* Function triggers device
 *
 * Parameters:
 *
 * Returns:
 *
 */

int32_t gpib::trigger(void) {
  int32_t response = 0;

  // trigger device
  ibtrg(handle);

  if((ibsta & ERR)) {
    log.error("ibtrg error %d", iberr);
    response = -1;
  }

  log.debug("target %d triggered", pad);

  //_serial_poll(gd, "gpib_trg");
  return(response);
}

/* Function makes serial poll to device
 *
 * Parameters:
 * status: reference to status string
 *
 * Returns:
 *
 */

int32_t	gpib::response(string& status) {
  int response = 0;
  char str[64];

  // do serial poll to device, modifies ibcnt
  ibrsp(handle, str);

  if((ibsta & ERR)) {
    log.error("ibrsp error %d\n", iberr);
  }

  // set status to string value
  status = str;
  response = (ibsta & RQS);

  return(response);
}

/* Function sets string termination flag on reads
 *
 * Parameters:
 * flag:
 *
 * Returns:
 *
 */

int32_t gpib::set_reos(bool flag) {
  // set string termination by flag
  ibconfig(handle, IbcEOSrd, flag ? REOS : 0);

  if((ibsta & ERR)) {
    log.error("ibconfig IbcEOSrd failed: %d", iberr);
    return(-1);
  }

  return(0);
}

/* Function sets EOI line behaviour
 *
 * Parameters:
 * flag: if non zero EOI is asse4rted on last byte on writes
 *
 * Returns:
 *
 */

int32_t gpib::set_eot(int32_t flag) {
  // set end of transfer behaviour
  ibconfig(handle, IbcEOT, flag);

  if ((ibsta & ERR)) {
    log.error("ibconfig IbcEOT failed: %d", iberr);
    return(-1);
  }

  return(0);
}

/* Function sets end of string byte
 *
 * Parameters:
 * c: new end of string byte
 *
 * Returns:
 *
 */

int32_t gpib::set_eos(int32_t c) {
  // set end of string
  ibconfig(handle, IbcEOSchar, c);

  if((ibsta & ERR)) {
    log.error("ibconfig IbcEOSchar failed: %d", iberr);
    return(-1);
  }

  return(0);
}

/* Function sets timeout for i/o operations
 *
 * Parameters:
 * sec: timeout value
 *
 * Returns:
 *
 */

int32_t gpib::set_timeout(double sec) {
  string str;
  timeout_value = T1000s;

  // check timoeut value bounds
  if(sec < 0 || sec > 1000) {
    log.error("gpib_set_timeout: timeout out of range");
    return(-1);
  }

  if(sec == 0) {
    timeout_value =  TNONE;
    str = "TNONE";
  } else if(sec <= 0.00001) {
    timeout_value =  T10us;
    str = "T10us";
  } else if(sec <= 0.00003) {
    timeout_value =  T30us;
    str = "T30us";
  } else if(sec <= 0.00010) {
    timeout_value =  T100us;
    str = "T100us";
  } else if(sec <= 0.00030) {
    timeout_value =  T300us;
    str = "T300us";
  } else if(sec <= 0.00100) {
    timeout_value =  T1ms;
    str = "T1ms";
  } else if(sec <= 0.00300) {
    timeout_value =  T3ms;
    str = "T3ms";
  } else if(sec <= 0.01000) {
    timeout_value =  T10ms;
    str = "T10ms";
  } else if(sec <= 0.03000) {
    timeout_value =  T30ms;
    str = "T30ms";
  } else if(sec <= 0.10000) {
    timeout_value =  T100ms;
    str = "T100ms";
  } else if(sec <= 0.30000) {
    timeout_value =  T300ms;
    str = "T300ms";
  } else if(sec <= 1.00000) {
    timeout_value =  T1s;
    str = "T1s";
  } else if(sec <= 3.00000) {
    timeout_value =  T3s;
    str = "T3s";
  } else if(sec <= 10.00000) {
    timeout_value =  T10s;
    str = "T10s";
  } else if(sec <= 30.00000) {
    timeout_value =  T30s;
    str = "T30s";
  } else if(sec <= 100.00000) {
    timeout_value =  T100s;
    str = "T100s";
  } else if(sec <= 300.00000) {
    timeout_value =  T300s;
    str = "T300s";
  } else if(sec <= 1000.00000) {
    timeout_value =  T1000s;
    str = "T1000s";
  }

  // set new timeout value
  ibtmo(handle, timeout_value);

  if ((ibsta & ERR)) {
    log.error("ibtmo failed: %d", iberr);
    return(-1);
  }

  return(0);
}

}
