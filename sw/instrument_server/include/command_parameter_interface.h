/***********************************************************
 * Software: instrument server
 * Module:   command parameter interface includes
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 15.10.2013
 *
 ***********************************************************/

#ifndef COMMAND_PARAMETER_INTERFACE_H_
#define COMMAND_PARAMETER_INTERFACE_H_

namespace instrument_server {

using namespace std;
using namespace YAMI;

class command_parameter_interface {
public:
	virtual ~command_parameter_interface() {/* empty */}
	virtual bool parameter_callback(auto_ptr<ParamSet> p, void *data) { return(true); }
	virtual bool parameter_delete(void *data) { return(true); }
protected:
private:

};
}
#endif /* COMMAND_PARAMETER_INTERFACE_H_ */
