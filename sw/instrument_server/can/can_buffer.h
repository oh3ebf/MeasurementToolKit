/***********************************************************
 * Software: instrument server
 * Module:   Can bus message buffer headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 3.5.2013
 *
 ***********************************************************/

#ifndef CAN_BUFFER_H_
#define CAN_BUFFER_H_

namespace instrument_server {

using namespace std;
using namespace log4cpp;

class can_buffer {
public:
	can_buffer(uint32_t size);
	virtual ~can_buffer();
	bool insert(CAN_MSG msg);
	CAN_MSG& operator[](uint32_t idx);
	const CAN_MSG& operator[](uint32_t idx) const;
	CAN_MSG pop_back(void);
	char* pop_back_bytes(char *buf);
	bool is_empty();
	uint32_t get_size(void);
	void clear(void);
protected:
private:
	Category &log;
	vector<CAN_MSG> rx_buffer;
	uint32_t buffer_max;
	uint32_t rx_index;
	uint32_t read_index;
	bool buffer_full;
	volatile uint32_t size;
};

}

#endif /* CAN_BUFFER_H_ */
