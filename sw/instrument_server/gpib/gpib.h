/***********************************************************
 * Software: instrument server
 * Module:   gpib 488.1 base headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 27.9.2012
 *
 ***********************************************************/

#ifndef GPIB_H_
#define GPIB_H_

namespace instrument_server {

using namespace log4cpp;
using namespace std;

class gpib {
public:
  gpib();
  virtual ~gpib();
  string& get_gpib_name(void);
  string& get_device_name(void);
  bool isConnected(void);

protected:
  Category &log;
  string gpib_name;
  string device_name;
  // TODO pitääkö kaikkien virheesen päättyvien palauttaa arvo?

  bool open_device(int32_t board, int32_t pad, int32_t sad);
  bool open_device(void);
  bool open_board(string& name/*const char *name*/);
  void close(void);
  bool scan_device(void);
  int32_t set_timeout(double sec);
  int32_t set_eos(int32_t c);
  int32_t set_eot(int32_t flag);
  int32_t set_reos(bool flag);
  int32_t response(string& status);
  int32_t trigger(void);
  int32_t clear(uint32_t usec);
  int32_t go_local(void);
  int32_t query(string& str, char *buf, int len);
  int32_t writef(const char *fmt, ...);
  int32_t write_string(string& str);
  int32_t write(void *buf, int len);
  int32_t readf(char *fmt, ...);
  string *read_string(uint32_t len);
  int32_t read(char *buf, int len);

private:

  /* Callback function to interpret results of serial poll.
   * Return value: -1=!ready, 0=success, >0=fatal error
   */
  //typedef int (*serial_poll_callback)(gd_t gd, unsigned char status_byte, char *msg);

  int32_t handle;
  uint32_t interface;
  uint32_t pad;
  uint32_t sad;
  uint32_t timeout_value;
  bool connection_status;
  pthread_mutex_t gpib_mutex;

  int32_t bus_write(char *buf, int len);
  int32_t bus_read(char *buf, int len);
  void strip();
};

}

#endif /* GPIB_H_ */
