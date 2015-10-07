/***********************************************************
 * Software: instrument server
 * Module:   hp3488 option card base headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 3.10.2012
 *
 ***********************************************************/

#ifndef HP3488_OPTION_BASE_H_
#define HP3488_OPTION_BASE_H_

namespace instrument_server {

using namespace log4cpp;
using namespace std;


class hp3488_option_base {
public:
  hp3488_option_base();
  virtual ~hp3488_option_base();
  string getDescription() const;
  uint32_t getId() const;
  string getName() const;
  void setId(uint32_t id);
  virtual bool get_cmd(string& str, uint8_t ch);
protected:
  uint32_t id;
  string name;
  string description;
  Category &log;
private:
};
}
#endif /* HP3488_OPTION_BASE_H_ */
