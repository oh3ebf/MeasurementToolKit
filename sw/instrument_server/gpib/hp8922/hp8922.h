/***********************************************************
 * Software: instrument server
 * Module:   HP 8922 GSM test set headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 27.8.2013
 *
 ***********************************************************/

#ifndef HP8922_H_
#define HP8922_H_

#include "gpib_488_2.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

class hp8922 : public gpib_488_2 {
public:
	enum display_mode {
		AUDIO_ANALYSER = 0,
		BIT_ERROR,
		BIT_ERROR_1,
		BIT_ERROR_2,
		CELL,
		CELL_1,
		CELL_2,
		CELL_CONFIG,
		CONFIGURE,
		CW_AUDIO_ANALYSER,
		CW_ANALYSER,
		DUAL_BAND,
		DDEMOD,
		DSP_ANALYSER,
		IO_CONFIGURE,
		FAST_BIT_ERROR,
		FTCPOWER,
		HELP,
		HOP_CONTROL,
		LOGGING,
		MESSAGES,
		MEASUREMENT_SYNC,
		MS_INFO,
		OUTPUT_RF_SPECTRUM,
		OSCILLOSSCOPE,
		PULSE,
		RF_ANALYSER,
		RF_GENERATOR,
		SPECTRUM_ANALYZER,
		SERVICE,
		SMS_CELL_BROADCAST,
		TCONFIGURE,
		TEST,
		TFREQ,
		TSPEC,
		TSEQ,
		TPAR,
		TIB
	};

	enum cell_config {

	};

	enum visual_band_width {

	};

	hp8922(string& dev_name, int32_t interface, int32_t pad, int32_t sad);
	hp8922(string& dev_name);
	virtual ~hp8922();
	//string *get_id(void);
	//void test(void);
	//void clear_device(void);
	void reset(void);
	//uint8_t read_status(void);
	uint8_t read_error(void);

	// signal generator commands
	void set_amplitude_rf_inout(double level);
	double get_amplitude_rf_inout(void);
	void set_amplitude_rf_aux(double level);
	double get_amplitude_rf_aux(void);
	void set_frequency(double f);
	double get_frequency(void);
	void set_rf_output(bool output);
	bool get_rf_output(void);

	// spectrum analyzer commands
	void set_span(double span);
	double get_span(void);
	void set_center_frequency(double f);
	double get_center_frequency(void);
	void set_visual_band_width();
	void get_visual_band_width();
	void set_reference_level_rf_inout(double level);
	double get_reference_level_rf_inout(void);
	void set_reference_level_rf_aux(double level);
	double get_reference_level_rf_aux(void);
	int32_t read_trace(double **data);

	// display commands
	bool select_display(display_mode m);
	//string& get_device_name(void);
protected:

private:
	Category &log;

	double read_dvalue(string query);
};
}

#endif /* HP8922_H_ */
