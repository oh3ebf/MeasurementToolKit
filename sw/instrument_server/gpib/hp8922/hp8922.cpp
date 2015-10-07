/***********************************************************
 * Software: instrument server
 * Module:   HP 8922 GSM test set class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 27.8.2013
 *
 ***********************************************************/

#include <string>
#include <stdint.h>
#include <stdlib.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "yami++.h"
#include "gpib_488_2.h"
#include "hp8922.h"
#include "message_interface.h"
#include "tokenizer.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;
using namespace YAMI;

/* Function implements constructor for hp8922 class
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp8922::hp8922(string& dev_name, int32_t interface, int32_t pad, int32_t sad) : log(Category::getInstance("gpib")) {

	log.debug("HP8922 initializing");

	// store gpib name for later use
	gpib_name = dev_name;
	device_name = "HP8922";

#ifndef GPIB_TEST
  // open device
  if(this->open_device(interface, pad, sad)) {
    log.info("HP8922 found at address %d", pad);
  } else {
    log.error("HP8922 not found at address %d", pad);
    return;
  }
#endif
}

/* Function implements constructor for hp8922 class
 *
 * Parameters:
 *
 * Returns:
 *
 */
hp8922::hp8922(string & dev_name) :log(Category::getInstance("gpib")) {
	log.debug("HP 8922 initializing");
	// store gpib name for later use
	gpib_name = dev_name;
	device_name = "HP8922";

#ifndef GPIB_TEST
  // open named device
  if(this->open_board(gpib_name)) {
    log.info("%s found", device_name.c_str());
  } else {
    log.error("%s not found", device_name.c_str());
    return;
  }
#endif
}

/* Function implements class destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */
hp8922::~hp8922() {
}

/* Function returns equipment id
 *
 * Parameters:
 *
 * Returns:
 * string containing equipment id
 *
 */
/*
string *hp8922::get_id(void) {
  // read instrument id
  string query = "ID?\n";
  this->write_string(query);
  return(this->read_string(64));
}
 */
/* Function runs self test in hp8922
 *
 * Parameters:
 *
 * Returns:
 *
 */
/*
void hp8922::test(void) {
  string query = "TEST\n";
  this->write_string(query);
}
 */
/* Function initialize instrument to default values
 *
 * Parameters:
 *
 * Returns:
 *
 */
//void hp8922::clear_device(void) {
	// send clear to bus
	//this->clear(1000000);
	/* verify the instrument id is what we expect */
	//string query = "ID?\n";
	//this->write_string(query);

	// read response
	//string *tmpbuf = this->gpib_488_2::get_id();
	//log.debug("device: ", tmpbuf->c_str());

	// remove allocated string
	//delete (tmpbuf);
//}

/* Function reads error register
 *
 * Parameters:
 *
 * Returns:
 * error in uint8
 *
 */
uint8_t hp8922::read_error(void) {
	uint8_t error = 0;
	// verify the instrument id is what we expect
	string query = "SYSTEM:ERROR?\n";
	this->write_string(query);
	// read status byte from device
	string *tmpbuf = this->read_string(256);

	// parse error code
	error = atoi(tmpbuf->c_str());

	delete (tmpbuf);
	return (error);
}

/* Function sets rf generator level on rf in/out port
 *
 * Parameters:
 * level: reference level value as double, units in dBm
 *
 * Returns:
 *
 */

void hp8922::set_amplitude_rf_inout(double level) {
	const char *cmd = "RFGENERATOR:AMPLITUDE1";
	// write command to target
	this->writef("%s %fdBm\n", cmd, level);
}

/* Function sets rf generator level on rf in/out port
 *
 * Parameters:
 *
 * Returns:
 * level value as double, units in dBm
 *
 */

double hp8922::get_amplitude_rf_inout(void) {
	// read rf generator output level
	return (read_dvalue("RFGENERATOR:AMPLITUDE1?"));

}

/* Function sets rf generator level on rf aux port
 *
 * Parameters:
 * level: reference level value as double, units in dBm
 *
 * Returns:
 *
 */

void hp8922::set_amplitude_rf_aux(double level) {
	const char *cmd = "RFGENERATOR:AMPLITUDE2";
	// write command to target
	this->writef("%s %fdBm\n", cmd, level);
}

/* Function gets rf generator level on rf aux port
 *
 * Parameters:
 *
 * Returns:
 * level value as double, units in dBm
 *
 */

double hp8922::get_amplitude_rf_aux(void) {
	// read rf generator output level
	return (read_dvalue("RFGENERATOR:AMPLITUDE2?"));
}

/* Function sets RF generator frequency
 *
 * Parameters:
 * f: frequency in MHz
 *
 * Returns:
 *
 *
 */

void hp8922::set_frequency(double f) {
	const char *cmd = "RFGENERATOR:FREQUENCY";
	// write command to target
	this->writef("%s %fHz\n", cmd, f);
}

/* Function gets RF generator frequency
 *
 * Parameters:
 *
 * Returns:
 * frequency in Hz
 *
 */

double hp8922::get_frequency(void) {
	// read frequency
	return (read_dvalue("RFGENERATOR:FREQUENCY?"));
}

/* Function sets RF generator output terminal
 *
 * Parameters:
 * output: true -> RF IN/OUT connector; false AUX RF OUT connector
 *
 * Returns:
 *
 */

void hp8922::set_rf_output(bool output) {
	string cmd1 = "RFGENERATOR:OUTPUT RF IN/OUT";
	string cmd2 = "RFGENERATOR:OUTPUT AUX RFOUT";

	// set rf output
	if(output) {
		this->write_string(cmd1);
	} else {
		this->write_string(cmd2);
	}
}

/* Function gets RF generator output terminal
 *
 * Parameters:
  *
 * Returns:
 * true -> RF IN/OUT connector; false AUX RF OUT connector
 *
 */

bool hp8922::get_rf_output(void) {
	string *result = NULL;

	string query = "RFGENERATOR:OUTPUT?\n";
	this->write_string(query);

#ifdef GPIB_TEST
	result = new string("RF IN/OUT");
#else
	result = this->read_string(16);
#endif

	if(result != NULL) {
		// find out rf output terminal
		if(result->compare("RF IN/OUT") == 0) {
			return(true);
		} else {
			return(false);
		}
	} else {
		log.error("failed to parse RF output info");
		return(false);
	}
}

/* Function sets spectrum analyzer span
 *
 * Parameters:
 * span: span in Hz
 *
 * Returns:
 *
 *
 */

void hp8922::set_span(double span) {
	const char *cmd = "SANALYZER:SPAN";
	// write command to target
	this->writef("%s %fHz\n", cmd, span);
}

/* Function gets spectrum analyzer span
 *
 * Parameters:
 *
 * Returns:
 * span value as double, units in MHz
 *
 */

double hp8922::get_span(void) {
	// read span
	return (read_dvalue("SANALYZER:SPAN?"));
}

/* Function sets spectrum analyzer center frequency
 *
 * Parameters:
 * f: center frequency in Hz
 *
 * Returns:
 *
 *
 */

void hp8922::set_center_frequency(double f) {
	const char *cmd = "SANALYZER:CFREQUENCY";
	// write command to target
	this->writef("%s %fHz\n", cmd, f);
}

/* Function gets spectrum center frequency
 *
 * Parameters:
 *
 * Returns:
 * center frequency value as double, units in MHz
 *
 */

double hp8922::get_center_frequency(void) {
	// read center frequency
	return (read_dvalue("SANALYZER:CFREQUENCY?"));
}

/* Function sets reference level to rf in/out port
 *
 * Parameters:
 * level: reference level value as double, units in dBm
 *
 * Returns:
 *
 */

void hp8922::set_reference_level_rf_inout(double level) {
	const char *cmd = "SANALYZER:RLEVEL1";
	// write command to target
	this->writef("%s %fdBm\n", cmd, level);
}

/* Function gets reference level from rf in/out port
 *
 * Parameters:
 *
 * Returns:
 * reference level value as double, units in dBm
 *
 */

double hp8922::get_reference_level_rf_inout(void) {
	// read center frequency
	return (read_dvalue("SANALYZER:RLEVEL1?"));
}

/* Function sets reference level to aux in port
 *
 * Parameters:
 * level: reference level value as double, units in dBm
 *
 * Returns:
 *
 */

void hp8922::set_reference_level_rf_aux(double level) {
	const char *cmd = "SANALYZER:RLEVEL2";
	// write command to target
	this->writef("%s %fdBm\n", cmd, level);
}

/* Function gets reference level from aux in port
 *
 * Parameters:
 *
 * Returns:
 * reference level value as double, units in dBm
 *
 */

double hp8922::get_reference_level_rf_aux(void) {
	// read center frequency
	return (read_dvalue("SANALYZER:RLEVEL2?"));
}

/* Function reads spectrum analyzer trace
 *
 * Parameters:
 *
 * Returns:
 *
 *
 */

int32_t hp8922::read_trace(double **data) {
	string *result = NULL;
	string limit = ",";
	int i = 0;

	string query = "MEASURE:SANALYZER:TRACE?\n";
	this->write_string(query);

#ifdef GPIB_TEST
	result = new string("-8.22549342E+001,-8.11611842E+001,-8.26217105E+001,-7.81447368E+001,-8.11611842E+001,-7.81447368E+001,-7.62434211E+001,-7.85279605E+001,-8.00164474E+001,-8.36726974E+001,-7.96842105E+001,-8.04062500E+001,-8.08092105E+001,-7.96842105E+001,-7.96842105E+001,-8.11611842E+001,-7.92861842E+001,-7.96842105E+001,-7.77582237E+001,-8.43322368E+001,-7.92861842E+001,-8.18832237E+001,-8.15674342E+001,-8.43322368E+001,-8.50164474E+001,-8.04062500E+001,-8.33059211E+001,-7.50756579E+001,-7.81447368E+001,-7.92861842E+001,-7.85279605E+001,-7.88799342E+001,-8.11611842E+001,-8.22549342E+001,-8.33059211E+001,-7.96842105E+001,-8.29375000E+001,-8.00164474E+001,-7.92861842E+001,-8.33059211E+001,-7.74194079E+001,-7.62434211E+001,-8.22549342E+001,-7.81447368E+001,-8.26217105E+001,-7.81447368E+001,-8.11611842E+001,-8.00164474E+001,-7.77582237E+001,-8.04062500E+001,-8.18832237E+001,-8.15674342E+001,-7.77582237E+001,-8.22549342E+001,-7.96842105E+001,-8.04062500E+001,-7.81447368E+001,-8.36726974E+001,-8.00164474E+001,-8.04062500E+001,");
	result->append("-7.77582237E+001,-7.96842105E+001,-7.74194079E+001,-7.77582237E+001,-8.11611842E+001,-8.11611842E+001,-8.18832237E+001,-7.77582237E+001,-7.70082237E+001,-8.00164474E+001,-7.54736842E+001,-7.92861842E+001,-7.85279605E+001,-8.08092105E+001,-7.92861842E+001,-8.39769737E+001,-7.96842105E+001,-8.15674342E+001,-8.22549342E+001,-8.29375000E+001,-7.88799342E+001,-8.15674342E+001,-8.18832237E+001,-8.29375000E+001,-7.77582237E+001,-7.96842105E+001,-7.92861842E+001,-8.04062500E+001,-8.43322368E+001,-8.15674342E+001,-8.22549342E+001,-8.15674342E+001,-8.08092105E+001,-8.15674342E+001,-7.77582237E+001,-7.92861842E+001,-7.96842105E+001,-8.11611842E+001,-7.50756579E+001,-8.15674342E+001,-8.36726974E+001,-8.08092105E+001,-8.04062500E+001,-7.85279605E+001,-7.43256579E+001,-8.36726974E+001,-8.08092105E+001,-8.15674342E+001,-8.08092105E+001,-8.00164474E+001,-8.22549342E+001,-7.92861842E+001,-7.70082237E+001,-8.61101974E+001,-7.92861842E+001,-8.29375000E+001,-8.00164474E+001,-8.33059211E+001,-7.85279605E+001,-7.88799342E+001,");
	result->append("-8.18832237E+001,-7.77582237E+001,-7.96842105E+001,-7.85279605E+001,-7.66562500E+001,-8.11611842E+001,-8.04062500E+001,-7.81447368E+001,-8.04062500E+001,-7.92861842E+001,-8.29375000E+001,-7.88799342E+001,-8.39769737E+001,-8.18832237E+001,-8.15674342E+001,-8.18832237E+001,-8.11611842E+001,-8.04062500E+001,-8.29375000E+001,-8.15674342E+001,-8.15674342E+001,-8.08092105E+001,-8.08092105E+001,-7.74194079E+001,-7.85279605E+001,-7.96842105E+001,-8.33059211E+001,-7.92861842E+001,-7.77582237E+001,-8.15674342E+001,-8.43322368E+001,-7.92861842E+001,-8.26217105E+001,-8.08092105E+001,-7.96842105E+001,-8.11611842E+001,-8.61101974E+001,-8.15674342E+001,-7.92861842E+001,-8.11611842E+001,-7.92861842E+001,-8.11611842E+001,-7.70082237E+001,-7.58256579E+001,-8.04062500E+001,-8.11611842E+001,-8.00164474E+001,-7.81447368E+001,-7.88799342E+001,-7.66562500E+001,-7.58256579E+001,-8.22549342E+001,-8.18832237E+001,-8.11611842E+001,-8.08092105E+001,-8.15674342E+001,-7.62434211E+001,-7.74194079E+001,-7.92861842E+001,-7.96842105E+001,");
	result->append("-8.00164474E+001,-7.96842105E+001,-8.22549342E+001,-8.18832237E+001,-8.36726974E+001,-8.04062500E+001,-8.11611842E+001,-8.15674342E+001,-8.33059211E+001,-8.00164474E+001,-7.85279605E+001,-8.18832237E+001,-8.00164474E+001,-8.04062500E+001,-7.96842105E+001,-8.33059211E+001,-8.04062500E+001,-7.96842105E+001,-7.74194079E+001,-8.18832237E+001,-8.00164474E+001,-7.88799342E+001,-8.26217105E+001,-7.96842105E+001,-7.70082237E+001,-8.18832237E+001,-8.29375000E+001,-7.70082237E+001,-8.08092105E+001,-7.70082237E+001,-7.81447368E+001,-7.96842105E+001,-7.81447368E+001,-7.85279605E+001,-7.81447368E+001,-7.74194079E+001,-7.58256579E+001,-8.08092105E+001,-8.33059211E+001,-8.04062500E+001,-7.85279605E+001,-7.74194079E+001,-8.15674342E+001,-8.00164474E+001,-8.36726974E+001,-8.50164474E+001,-8.08092105E+001,-7.74194079E+001,-8.29375000E+001,-8.08092105E+001,-8.29375000E+001,-7.66562500E+001,-7.85279605E+001,-7.88799342E+001,-7.88799342E+001,-8.18832237E+001,-8.22549342E+001,-7.62434211E+001,-7.88799342E+001,-7.70082237E+001,");
	result->append("-8.26217105E+001,-7.77582237E+001,-7.66562500E+001,-7.92861842E+001,-8.15674342E+001,-8.11611842E+001,-7.70082237E+001,-7.81447368E+001,-7.96842105E+001,-7.85279605E+001,-8.11611842E+001,-8.15674342E+001,-8.29375000E+001,-7.70082237E+001,-7.70082237E+001,-7.81447368E+001,-8.18832237E+001,-7.96842105E+001,-8.22549342E+001,-7.77582237E+001,-7.92861842E+001,-7.70082237E+001,-8.22549342E+001,-8.29375000E+001,-7.66562500E+001,-8.08092105E+001,-8.08092105E+001,-8.11611842E+001,-7.96842105E+001,-8.08092105E+001,-7.85279605E+001,-8.08092105E+001,-8.00164474E+001,-8.26217105E+001,-7.96842105E+001,-8.26217105E+001,-8.43322368E+001,-8.22549342E+001,-7.85279605E+001,-7.66562500E+001,-7.96842105E+001,-7.66562500E+001,-8.08092105E+001,-7.92861842E+001,-8.08092105E+001,-7.46759868E+001,-8.36726974E+001,-8.15674342E+001,-7.96842105E+001,-8.15674342E+001,-8.18832237E+001,-8.18832237E+001,-8.11611842E+001,-7.66562500E+001,-7.96842105E+001,-8.36726974E+001,-8.22549342E+001,-7.88799342E+001,-7.85279605E+001,-8.15674342E+001,");
	result->append("-7.70082237E+001,-8.22549342E+001,-8.50164474E+001,-8.15674342E+001,-8.04062500E+001,-7.81447368E+001,-7.74194079E+001,-7.92861842E+001,-7.81447368E+001,-8.46957237E+001,-8.18832237E+001,-7.92861842E+001,-7.81447368E+001,-8.08092105E+001,-8.33059211E+001,-8.11611842E+001,-7.77582237E+001,-7.54736842E+001,-7.70082237E+001,-7.92861842E+001,-8.50164474E+001,-7.81447368E+001,-8.22549342E+001,-8.00164474E+001,-7.92861842E+001,-8.22549342E+001,-8.39769737E+001,-8.00164474E+001,-7.92861842E+001,-7.96842105E+001,-8.00164474E+001,-7.96842105E+001,-7.70082237E+001,-7.85279605E+001,-7.74194079E+001,-7.88799342E+001,-8.29375000E+001,-7.62434211E+001,-7.96842105E+001,-7.92861842E+001,-7.96842105E+001,-7.66562500E+001,-7.88799342E+001,-7.96842105E+001,-7.96842105E+001,-7.85279605E+001,-7.85279605E+001,-8.50164474E+001,-7.92861842E+001,-8.00164474E+001,-7.88799342E+001,-8.18832237E+001,-7.77582237E+001,-7.92861842E+001,-8.04062500E+001,-8.08092105E+001,-8.22549342E+001,-7.96842105E+001,-8.08092105E+001,-7.74194079E+001,");
	result->append("-7.54736842E+001,-8.22549342E+001,-8.00164474E+001,-7.85279605E+001,-8.08092105E+001,-8.15674342E+001,-7.96842105E+001,-8.08092105E+001,-8.39769737E+001,-7.92861842E+001,-7.85279605E+001,-8.39769737E+001,-8.11611842E+001,-7.92861842E+001,-8.04062500E+001,-7.58256579E+001,-7.85279605E+001,-7.70082237E+001,-8.72187500E+001,-8.18832237E+001,-8.11611842E+001,-7.92861842E+001,-7.96842105E+001,-8.22549342E+001,-8.00164474E+001,-8.00164474E+001,-7.96842105E+001,-7.81447368E+001,-8.43322368E+001,-8.00164474E+001,-8.11611842E+001,-8.00164474E+001,-7.31759868E+001,-8.29375000E+001,-7.96842105E+001,-8.08092105E+001,-8.11611842E+001,-8.00164474E+001,-8.04062500E+001,-7.96842105E+001,-7.70082237E+001,-7.50756579E+001,-7.96842105E+001,-7.85279605E+001,-8.33059211E+001,-7.50756579E+001,-7.88799342E+001,-8.00164474E+001,-7.92861842E+001,-7.85279605E+001,-8.08092105E+001,-7.96842105E+001,-7.81447368E+001,-8.00164474E+001,-8.00164474E+001,-8.26217105E+001,-7.96842105E+001");
#else
	  // read back string , normally 7089 bytes add some extra space
	  result = this->read_string(8192);
#endif

	// check result and return existing values if read failed
	if(result == NULL) {
		log.error("failed to read spectrum analyzer span value from device ");
		return(-1);
	} else {
		// get string tokens
		tokenizer *token = new tokenizer(*result, limit);

		while(token->next_token(limit)) {
			(*data)[i++] = strtod(token->get_token().c_str(), NULL);
			//log.debug("value %f",d);
		}

		// free reserved memory
		delete(result);
		return(i);
	}
}

/* Function select display screen
 *
 * Parameters:
 * m: screen enumeration
 *
 * Returns:
 *
 *
 */
bool hp8922::select_display(display_mode m) {
	string query = "DISPLAY:SCREEN ";
	switch (m){
	case AUDIO_ANALYSER:
		query += "AFANALYZER\n";
		break;
	case BIT_ERROR:
		query += "BER\n";
		break;
	case BIT_ERROR_1:
		query += "BER1\n";
		break;
	case BIT_ERROR_2:
		query += "BER2\n";
		break;
	case CELL:
		query += "CELL\n";
		break;
	case CELL_1:
		query += "CELL1\n";
		break;
	case CELL_2:
		query += "CELL2\n";
		break;
	case CELL_CONFIG:
		query += "CCONFIGURE\n";
		break;
	case CONFIGURE:
		query += "CONFIGURE\n";
		break;
	case CW_AUDIO_ANALYSER:
		query += "CWAFANALYZER\n";
		break;
	case CW_ANALYSER:
		query += "CWANALYZER\n";
		break;
	case DUAL_BAND:
		query += "DBAND\n";
		break;
	case DDEMOD:
		query += "DDEMOD\n";
		break;
	case DSP_ANALYSER:
		query += "DSPANALYzER\n";
		break;
	case IO_CONFIGURE:
		query += "IOCONFIGURE\n";
		break;
	case FAST_BIT_ERROR:
		query += "FBER\n";
		break;
	case FTCPOWER:
		query += "FTCPOWER\n";
		break;
	case HELP:
		query += "HELP\n";
		break;
	case HOP_CONTROL:
		query += "HOPCONTROL\n";
		break;
	case LOGGING:
		query += "LOGGING\n";
		break;
	case MESSAGES:
		query += "MESSAGES\n";
		break;
	case MEASUREMENT_SYNC:
		query += "MSYNC\n";
		break;
	case MS_INFO:
		query += "MSINFO\n";
		break;
	case OUTPUT_RF_SPECTRUM:
		query += "ORFSPECTRUM\n";
		break;
	case OSCILLOSSCOPE:
		query += "OSCILLOSCOPE\n";
		break;
	case PULSE:
		query += "PULSE\n";
		break;
	case RF_ANALYSER:
		query += "RFANALYZER\n";
		break;
	case RF_GENERATOR:
		query += "RFGENERATOR\n";
		break;
	case SPECTRUM_ANALYZER:
		query += "SANALYZER\n";
		break;
	case SERVICE:
		query += "SERVICE\n";
		break;
	case SMS_CELL_BROADCAST:
		query += "SMSCB\n";
		break;
	case TCONFIGURE:
		query += "TCONFIGURE\n";
		break;
	case TEST:
		query += "TESTS\n";
		break;
	case TFREQ:
		query += "TFREQ\n";
		break;
	case TSPEC:
		query += "TSPEC\n";
		break;
	case TSEQ:
		query += "TSEQ\n";
		break;
	case TPAR:
		query += "TPAR\n";
		break;
	case TIB:
		query += "TIB\n";
		break;
	default:
		return(false);
	break;
	}

	this->write_string(query);
	return(true);
}



/* Function reads double value from system
 *
 * Parameters:
 * query: screen enumeration
 *
 * Returns:
 * double value read from device
 *
 */
double hp8922::read_dvalue(string query) {
	string *result;
	double value = 0.0F;

	// write query to device
	this->write_string(query);

#ifdef GPIB_TEST
	result = new string("1000.00");
#else
	  // read back string
	  result = this->read_string(20);
#endif

	// check result and return existing values if read failed
	if(result == NULL) {
		log.error("failed to read query %s from device", query.c_str());
	} else {
		// convert to double
		value = strtod(result->c_str(), NULL);
		// free reserved memory
		delete(result);
	}
	return(value);
}


}
