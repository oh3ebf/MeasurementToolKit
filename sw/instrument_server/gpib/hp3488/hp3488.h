/***********************************************************
 * Software: instrument server
 * Module:   hp3488 switching control unit headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 1.10.2012
 *
 ***********************************************************/

#ifndef HP3488_H_
#define HP3488_H_

#include "gpib.h"
#include "hp3488_option_base.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

class hp3488 : public gpib {
public:
	/* Status byte values
	 * Also SRQ mask values (except rqs cannot be masked)
	 */
#define HP3488_STATUS_SCAN_DONE     0x01    /* end of scan sequence */
#define HP3488_STATUS_OUTPUT_AVAIL  0x02    /* output available */
#define HP3488_STATUS_SRQ_POWER     0x04    /* power on SRQ asserted */
#define HP3488_STATUS_SRQ_BUTTON    0x08    /* front panel SRQ asserted */
#define HP3488_STATUS_READY         0x10    /* ready for instructions */
#define HP3488_STATUS_ERROR         0x20    /* an error has occurred */
#define HP3488_STATUS_RQS           0x40    /* rqs */

	/* Error byte values */
#define HP3488_ERROR_SYNTAX         0x1     /* syntax error */
#define HP3488_ERROR_EXEC           0x2     /* exection error */
#define HP3488_ERROR_TOOFAST        0x4     /* hardware trigger too fast */
#define HP3488_ERROR_LOGIC          0x8     /* logic failure */
#define HP3488_ERROR_POWER          0x10    /* power supply failure */

	hp3488(string& dev_name, int32_t interface, int32_t pad, int32_t sad);
	hp3488(string& dev_name);
	virtual ~hp3488();
	string *get_id(void);
	void test(void);
	void clear_device(void);
	void reset(void);
	void card_reset(uint8_t slot);
	void close_ch(uint8_t slot, uint8_t ch);
	void close_ch(string ch);
	void open_ch(uint8_t slot, uint8_t ch);
	void open_ch(string ch);
	bool view_ch(uint8_t slot, uint8_t ch);
	uint8_t read_status(void);
	uint8_t read_error(void);
	string& get_device_name(void);
	uint32_t get_card_type(uint8_t slot);
	hp3488_option_base& get_card(uint8_t slot);

protected:
	hp3488_option_base *slot_config[5];

private:
	Category &log;
	void scan_slots(void);
};
}

#endif /* HP3488_H_ */
