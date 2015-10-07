/***********************************************************
 * Software: instrument server
 * Module:   gpib manager headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 25.9.2012
 *
 ***********************************************************/

#ifndef GPIB_MANAGER_H_
#define GPIB_MANAGER_H_

#include "yami++.h"
#include "gpib.h"
#include "message_interface.h"
#include "data_stream_interface.h"
#include "thread_interface.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace config4cpp;
using namespace YAMI;

class gpib_manager : public thread_interface {
public:
  gpib_manager(Configuration *cfg);
  virtual ~gpib_manager();
  bool start_worker(data_stream_interface *d);
  void exit_thread();
  map<string, message_interface *>& get_msg_list(void);
protected:
private:
  volatile bool fsm_worker_exit;
  Category &log;
  map<string, gpib *> device_list;
  map<string, message_interface *> msg_list;
  data_stream_interface *data_stream;

  void internal_thread_run();
};

}
#endif /* GPIB_MANAGER_H_ */
