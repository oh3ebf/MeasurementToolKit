/***********************************************************
 * Software: instrument server
 * Module:   data stream interface includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 23.1.2013
 *
 ***********************************************************/

#ifndef STREAM_INTERFACE_H_
#define STREAM_INTERFACE_H_

namespace instrument_server {

using namespace std;
using namespace YAMI;

class data_stream_interface {
public:
	virtual ~data_stream_interface() {/* empty */}
	virtual void send_data(string ip, string msg, ParamSet p)  = 0;
	virtual string *get_domain(string ip) = 0;
protected:
private:

};
}
#endif /* STREAM_INTERFACE_H_ */
