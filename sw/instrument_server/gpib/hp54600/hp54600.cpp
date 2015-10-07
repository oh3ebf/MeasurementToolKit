/***********************************************************
 * Software: instrument server
 * Module:   hp54600 oscilloscope unit class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 9.10.2012
 *
 ***********************************************************/

#include <string>
#include <algorithm>

#include <stdint.h>
#include <stdlib.h>
#include <log4cpp/Category.hh>
#include <log4cpp/FileAppender.hh>
#include <log4cpp/BasicLayout.hh>

#include "gpib.h"
#include "hp54600.h"
#include "tokenizer.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;

// supported scope models
const hp54600::scope_info hp54600::models[7] = {
    {"HP54600", hp54600::MODEL_HP54600, 2},
    {"HP54601", hp54600::MODEL_HP54601, 4},
    {"HP54602", hp54600::MODEL_HP54602, 4},
    {"HP54603", hp54600::MODEL_HP54603, 2},
    {"HP54610", hp54600::MODEL_HP54610, 2},
    {"HP54615", hp54600::MODEL_HP54615, 2},
    {"HP54616", hp54600::MODEL_HP54616, 2}
};

/* Function implements constructor for hp54600 class
 *
 * Parameters:
 * dev_name:
 * interface:
 * pad:
 * sad:
 *
 * Returns:
 *
 */

hp54600::hp54600(string& dev_name, scope_model m, int32_t interface, int32_t pad, int32_t sad) : model_number(0) {
  log.debug("HP546xx initializing");

  // store gpib name for later use
  model_number = m;
  gpib_name = dev_name;
  device_name = hp54600::models[model_number].name;

#ifndef GPIB_TEST
  // open device
  if(this->open_device(interface, pad, sad)) {
    log.info("%s found at address %d", device_name.c_str(), pad);
  } else {
    log.error("%s not found at address %d", device_name.c_str(), pad);
    return;
  }
#endif

  for(int i = 0; i < this->get_channel_cnt(); i++) {
    // get channel state, initialize only active channels
    if(get_channel_state(i + 1)) {
      // read channel parameters
      get_channel_parameters(i + 1);
      // select channel
      set_waveform_source(i + 1);
      // read current waveform parameters
      get_waveform_parameters(i + 1);
    }
  }

  // read trigger parameters
  get_trigger_parameters();
  // read time base parameters
  get_timebase_parameters();
}

/* Function implements constructor for hp54600 class
 *
 * Parameters:
 * dev_name: device gpib name
 *
 * Returns:
 *
 */

hp54600::hp54600(string& dev_name, scope_model m) : model_number(0) {
  log.debug("HP546xx initializing");

  // store gpib name for later use
  model_number = m;
  gpib_name = dev_name;
  device_name = models[model_number].name;

#ifndef GPIB_TEST
  // open named device
  if(this->open_board(gpib_name)) {
    log.info("%s found", device_name.c_str());
  } else {
    log.error("%s not found", device_name.c_str());
    return;
  }
#endif

  for(int i = 0; i < this->get_channel_cnt(); i++) {
    // get channel state, initialize only active channels
    if(get_channel_state(i + 1)) {
      // read channel parameters
      get_channel_parameters(i + 1);
      // select channel
      set_waveform_source(i + 1);
      // read current waveform parameters
      get_waveform_parameters(i + 1);
    }
  }

  // read trigger parameters
  get_trigger_parameters();
  // read time base parameters
  get_timebase_parameters();
}

/* Function implements constructor for hp54600 class
 *
 * Parameters:
 * dev_name:
 * model:
 *
 * Returns:
 *
 */

hp54600::hp54600(string& dev_name, const string& model) : model_number(0) {
  log.debug("HP546xx initializing");

  // make copy of original value
  string m = model;

  // store gpib name for later use
  gpib_name = dev_name;
  device_name = models[model_number].name;

  // to upper case
  std::transform(m.begin(), m.end(), m.begin(), ::toupper);

  // find model if supported
  for(uint8_t i = 0;i < 7; i++) {
    if(m == models[i].name) {
      model_number = i;
      log.info("using scope model %s", models[i].name.c_str());
      break;
    }
  }

#ifndef GPIB_TEST
  // open named device
  if(this->open_board(gpib_name)) {
    log.info("%s found", device_name.c_str());
  } else {
    log.error("%s not found", device_name.c_str());
    return;
  }
#endif

  for(int i = 0; i < this->get_channel_cnt(); i++) {
    // get channel state, initialize only active channels
    if(get_channel_state(i + 1)) {
      // read channel parameters
      get_channel_parameters(i + 1);
      // select channel
      set_waveform_source(i + 1);
      // read current waveform parameters
      get_waveform_parameters(i + 1);
    }

  }

  // read trigger parameters
  get_trigger_parameters();
  // read time base parameters
  get_timebase_parameters();
}

/* Function implements class destructor
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp54600::~hp54600() {
  // TODO Auto-generated destructor stub
}

/* Function get number of channels in scope
 *
 * Parameters:
 *
 * Returns:
 * number of channels
 *
 */

uint8_t hp54600::get_channel_cnt(void) {
  return(models[model_number].channel_cnt);
}

/* Function get scope model
 *
 * Parameters:
 *
 * Returns:
 * model enumeration
 *
 */

hp54600::scope_model hp54600::get_scope_model(void) {
  return(models[model_number].model);
}

/* Function get scope model
 *
 * Parameters:
 * m: model of scope
 *
 * Returns:
 *
 */

void hp54600::set_scope_model(scope_model m) {
  // set model
  model_number = m;
}

/* Function auto scales scope
 *
 * Parameters:
 *
 * Returns:
 *
 */

void hp54600::autoscale(void) {
  // autoscale command
  string cmd = ":AUTOSCALE\n";

  this->write_string(cmd);
}

/* Function sets scope to run state
 *
 * Parameters:
 *
 * Returns:
 *
 */

void hp54600::run(void) {
  // run command
  string cmd = ":RUN\n";

  this->write_string(cmd);
}

/* Function sets scope to stop state
 *
 * Parameters:
 *
 * Returns:
 *
 */

void hp54600::stop(void) {
  // stop command
  string cmd = ":STOP\n";

  this->write_string(cmd);
}

/* Function captures waveform to scope memory
 *
 * Parameters:
 * ch: bit 0 = ch1, bit 1 = ch2, bit 2 = ch3, bit 3 = ch4
 *
 * Returns:
 * used channel count
 *
 */

uint8_t hp54600::capture(uint8_t ch) {
  uint8_t cnt = 0;
  bool used_once = false;

  // waveform capture command
  string cmd = ":DIGITIZE ";

  if(ch & 0x01) {
    // add channel if selected
    cmd += "CHANNEL1";
    cnt++;
    used_once = true;
  }

  if(ch & 0x02) {
    // add separator if needed
    if(used_once) {
      cmd += ",";
    }

    // add channel if selected
    cmd += "CHANNEL2";
    cnt++;
    used_once = true;
  }

  // only for four channel models
  if(models[model_number].channel_cnt == 4) {
    if(ch & 0x04) {
      // add separator if needed
      if(used_once) {
        cmd += ",";
      }

      // add channel if selected
      cmd += "CHANNEL3";
      cnt++;
      used_once = true;
    }

    if(ch & 0x08) {
      // add separator if needed
      if(used_once) {
        cmd += ",";
      }

      // add channel if selected
      cmd += "CHANNEL4";
      cnt++;
    }
  }

  // line end
  cmd += "\n";
  this->write_string(cmd);

  return(cnt);
}

/* Function sets channel active
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 * state: true if in use, otherwise false
 *
 * Returns:
 *
 */

void hp54600::set_channel_state(uint8_t ch, bool state) {
  // channel activate / deactivate commands
  const char *cmd1 = ":VIEW CHANNEL";
  const char *cmd2 = ":BLANK CHANNEL";

  if(state) {
    // write command to target
    this->writef("%s%d%s\n", cmd1, ch);
  } else {
    // write command to target
    this->writef("%s%d%s\n", cmd2, ch);
  }
}

/* Function gets channel in use status
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 *
 * Returns:
 * true if in use, otherwise false
 *
 */

bool hp54600::get_channel_state(uint8_t ch) {
  string *result;
  bool status = false;

  // channel activate / deactivate commands
  const char *cmd = ":STATUS? CHANNEL";

  // write command to target
  this->writef("%s%d\n", cmd, ch);

#ifdef GPIB_TEST
  if(ch == 1) {
    result = new string("ON");
  } else {
    result = new string("OFF");
  }
#else
  result = this->read_string(10);
#endif

  // null happens when read fails
  if(result != NULL ) {
    // strip line ends and other scrap
    result->erase(result->find_last_not_of(" \n\r\t") + 1);
    log.debug("channel state: %s", result->c_str());

    // check what was read
    if(result->compare("ON") == 0) {
      status = true;
    }

    // remove data allocation
    delete(result);
  } else {
    log.error("failed to read channel state: ch%d" + ch);
  }

  return(status);
}

/* Function sets voltage scale to given channel
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 * v: voltage scale / div
 *
 * Returns:
 *
 */

void hp54600::set_voltage_scale(uint8_t ch, double v) {
  // voltage scale command
  const char *cmd1 = ":CHANNEL";
  const char *cmd2 = ":RANGE";

  // write command to target
  this->writef("%s%d%s %.2e\n", cmd1, ch, cmd2, v * 8.0F);

  // update waveform parameters
  set_waveform_source(ch);
  get_waveform_parameters(ch);
}

/* Function sets voltage offset to given channel
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 * v: voltage offset
 *
 * Returns:
 *
 */

void hp54600::set_voltage_offset(uint8_t ch, double v) {
  // voltage offset command
  const char *cmd1 = ":CHANNEL";
  const char *cmd2 = ":OFFSET";

  // write command to target
  this->writef("%s%d%s %.2e\n", cmd1, ch, cmd2, v);
}

/* Function sets channel coupling
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 * state: coupling mode
 *
 * Returns:
 *
 */

void hp54600::set_channel_coupling(uint8_t ch, uint8_t state) {
  // channel coupling command
  const char *cmd1 = ":CHANNEL";
  string modeStr;

  switch(state) {
  case 0:
    modeStr = ":COUPLING AC";
    break;
  case 1:
    modeStr = ":COUPLING DC";
    break;
  case 2:
    modeStr = ":COUPLING GND";
    break;
  }

  // write command to target
  this->writef("%s%d%s\n", cmd1, ch, modeStr.c_str());
}

/* Function sets channel bandwidth limit usage
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 * state: coupling on/off
 *
 * Returns:
 *
 */

void hp54600::set_channel_bwlimit(uint8_t ch, uint8_t state) {
  // channel bandwidth limit command
  const char *cmd1 = ":CHANNEL";
  string modeStr;

  switch(state) {
  case 0:
    modeStr = ":BWLIMIT OFF";
    break;
  case 1:
    modeStr = ":BWLIMIT ON";
    break;
  }

  // write command to target
  this->writef("%s%d%s\n", cmd1, ch, modeStr.c_str());
}

/* Function sets channel invert
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 * state: invert on/off
 *
 * Returns:
 *
 */

void hp54600::set_channel_invert(uint8_t ch, uint8_t state) {
  // channel invert command
  const char *cmd1 = ":CHANNEL";
  string modeStr;

  switch(state) {
  case 0:
    modeStr = ":INVERT OFF";
    break;
  case 1:
    modeStr = ":INVERT ON";
    break;
  }

  // write command to target
  this->writef("%s%d%s\n", cmd1, ch, modeStr.c_str());
}

/* Function sets channel vernier
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 * state: vernier on/off
 *
 * Returns:
 *
 */

void hp54600::set_channel_vernier(uint8_t ch, uint8_t state) {
  // channel vernier command
  const char *cmd1 = ":CHANNEL";
  string modeStr;

  switch(state) {
  case 0:
    modeStr = ":VERNIER OFF";
    break;
  case 1:
    modeStr = ":VERNIER ON";
    break;
  }

  // write command to target
  this->writef("%s%d%s\n", cmd1, ch, modeStr.c_str());
}

/* Function sets channel probe
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 * state: probe selection
 *
 * Returns:
 *
 */

void hp54600::set_channel_probe(uint8_t ch, uint8_t state) {
  // channel probe setting command
  const char *cmd1 = ":CHANNEL";
  string modeStr;

  switch(state) {
  case 0:
    modeStr = ":PROBE X1";
    break;
  case 1:
    modeStr = ":PROBE X10";
    break;
  case 2:
    modeStr = ":PROBE X20";
    break;
  case 3:
    modeStr = ":PROBE X100";
    break;
  }

  // write command to target
  this->writef("%s%d%s\n", cmd1, ch, modeStr.c_str());
}

/* Function reads channel setup from device
 *
 * Parameters:
 * ch: number of channel number 1,2 (,3,4)
 *
 * Returns:
 * channel setup in a struct
 *
 */

hp54600::channel_info hp54600::get_channel_parameters(uint8_t ch) {
  string *result, tmp;
  uint8_t ch_tmp = ch - 1;

  // send command and read answer
  this->writef(":CHANNEL%d:SETUP?\n",ch);

#if GPIB_TEST
  result = new string("CHAN1:RANGE +1.60000000E+001;OFFSET +0.00000000E+000;COUP DC;BWLIMIT OFF;INVERT OFF;VERNIER OFF;PROBE X1");
#else
  result = this->read_string(256);
#endif

  // check result and return existing values if read failed
  if(result == NULL) {
    log.error("failed to read channel parameters from device ch%d", ch);
    return(chan_params[ch_tmp]);
  }

  // get string tokens
  tokenizer *token = new tokenizer(*result, " ");

  // parse voltage range
  if(token->next_token(" ")) {
    // remove scrap from beginning
    token->get_token();
    // change separator
    token->next_token(";");
    chan_params[ch_tmp].voltage_scale = strtof(token->get_token().c_str(),NULL);
  }

  // parse voltage offset
  if(token->next_token(" ")) {
    // remove scap from beginning
    token->get_token().c_str();
    // change separator
    token->next_token(";");
    chan_params[ch_tmp].voltage_offset = strtof(token->get_token().c_str(),NULL);
  }

  // parse channel coupling
  if(token->next_token(";")) {
    tmp = token->get_token();

    if(tmp.compare("COUP AC") == 0)
      chan_params[ch_tmp].coupling = COUPLING_AC;
    if(tmp.compare("COUP DC") == 0)
      chan_params[ch_tmp].coupling = COUPLING_DC;
    if(tmp.compare("COUP GND") == 0)
      chan_params[ch_tmp].coupling = COUPLING_GND;
  }

  // parse channel band with limit
  if(token->next_token(";")) {
    tmp = token->get_token();
    if(tmp.compare("BWLIMIT OFF") == 0)
      chan_params[ch_tmp].bwlimit = false;
    else
      chan_params[ch_tmp].bwlimit = true;
  }

  // parse channel invert
  if(token->next_token(";")) {
    tmp = token->get_token();
    if(tmp.compare("INVERT OFF") == 0)
      chan_params[ch_tmp].invert = false;
    else
      chan_params[ch_tmp].invert = true;
  }

  // parse channel vernier
  if(token->next_token(";")) {
    tmp = token->get_token();
    if(tmp.compare("VERNIER OFF") == 0)
      chan_params[ch_tmp].vernier = false;
    else
      chan_params[ch_tmp].vernier = true;
  }

  // parse channel probe settings
  if(token->next_token(";")) {
    tmp = token->get_token();

    if(tmp.compare("PROBE X1") == 0)
      chan_params[ch_tmp].probe = PROBE_1X;
    if(tmp.compare("PROBE X10") == 0)
      chan_params[ch_tmp].probe = PROBE_10X;
    if(tmp.compare("PROBE X20") == 0)
      chan_params[ch_tmp].probe = PROBE_20X;
    if(tmp.compare("PROBE X100") == 0)
      chan_params[ch_tmp].probe = PROBE_100X;
  }

  // free allocated space
  delete(token);
  delete(result);

  log.debug("ch%d voltage scale %lf", ch, chan_params[ch_tmp].voltage_scale);
  log.debug("ch%d voltage offset %lf", ch, chan_params[ch_tmp].voltage_offset);
  log.debug("ch%d coupling %d", ch, chan_params[ch_tmp].coupling);
  log.debug("ch%d band with limit %d", ch, chan_params[ch_tmp].bwlimit);
  log.debug("ch%d vernier %d", ch, chan_params[ch_tmp].vernier);
  log.debug("ch%d pobe settings %d", ch, chan_params[ch_tmp].probe);

  return(chan_params[ch_tmp]);
}


/* Function sets time base range
 *
 * Parameters:
 * t: timebase range value
 *
 * Returns:
 *
 */

void hp54600::set_timebase_range(double time) {
  const char *cmd = ":TIMEBASE:RANGE";

  // write command to target
  this->writef("%s %.2e\n", cmd, time * 10.0F);
}

/* Function reads time base setup
 *
 * Parameters:
 *
 * Returns:
 *
 */

hp54600::timebase_info hp54600::get_timebase_parameters(void) {
  string *result, tmp;
  //string limit = " ";

  // send command and read answer
  string cmd =":TIMEBASE:SETUP?\n";
  this->write_string(cmd);

#if GPIB_TEST
  result = new string("TIMEBASE:MODE NORM;RANGE +1.00000000E-003;DELAY +0.00000000E+000;REF CENT;VERN OFF");
#else
  result = this->read_string(256);
#endif

  // check result and return existing values if read failed
  if(result == NULL) {
    log.error("failed to read channel parameters from device ");
    return(timebase_params);
  }

  unsigned start = result->find_first_of(":");
  string a = result->substr(start + 1);

  // get string tokens
  tokenizer *token = new tokenizer(a, ";");

  // parse timebase mode
  if(token->next_token(";")) {
    // mode line
    tmp = token->get_token();
    log.debug("%s",tmp.c_str());

    if(tmp.compare("MODE NORM") == 0)
      timebase_params.mode = TIMEBASE_NORMAL;
    if(tmp.compare("MODE DEL") == 0)
      timebase_params.mode = TIMEBASE_DELAYED;
    if(tmp.compare("MODE XY") == 0)
      timebase_params.mode = TIMEBASE_XY;
  }

  // parse time scale
  if(token->next_token(" ")) {
    // remove scrap from beginning
    token->get_token();
    // change separator
    token->next_token(";");
    timebase_params.timescale = strtof(token->get_token().c_str(),NULL);
  }

  // parse time scale
  if(token->next_token(" ")) {
    // remove scrap from beginning
    token->get_token();
    // change separator
    token->next_token(";");
    timebase_params.delay = strtof(token->get_token().c_str(),NULL);
  }

  // parse channel reference
  if(token->next_token(";")) {
    tmp = token->get_token();
    if(tmp.compare("REF CENT") == 0)
      timebase_params.reference = true;
    else
      timebase_params.reference = false;
  }

  // parse channel vernier
  if(token->next_token(";")) {
    tmp = token->get_token();
    if(tmp.compare("VERN OFF") == 0)
      timebase_params.vernier = false;
    else
      timebase_params.vernier = true;
  }

  // free allocated space
  delete(token);
  delete(result);

  log.debug("timebase mode %d", timebase_params.mode);
  log.debug("timebase range %f", timebase_params.timescale);
  log.debug("timebase delay %f", timebase_params.delay);
  log.debug("timebase reference %d", timebase_params.reference);
  log.debug("timebase vernier %d", timebase_params.vernier);

  return(timebase_params);
}

/* Function sets trigger mode
 *
 * Parameters:
 * mode: mode selection
 *
 * Returns:
 *
 */

void hp54600::set_trigger_mode(uint8_t mode) {
  string cmd =":TRIGGER:MODE ";

  switch(mode) {
  case TRIGGER_AUTOLEVEL:
    cmd += "AUTLEVEL";
    break;
  case TRIGGER_AUTO:
    cmd += "AUTO";
    break;
  case TRIGGER_NORMAL:
    cmd += "NORMAL";
    break;
  case TRIGGER_SINGLE:
    cmd += "SINGLE";
    break;
  case TRIGGER_TV:
    cmd += "TV";
    break;
  }

  cmd += "\n";
  this->write_string(cmd);
}

/* Function sets trigger source
 *
 * Parameters:
 * mode: source select
 *
 * Returns:
 *
 */

void hp54600::set_trigger_source(uint8_t mode) {
  string cmd =":TRIGGER:SOURCE ";

  switch(mode) {
  case TRIGGER_CHAN_1:
    cmd += "CHANNEL1";
    break;
  case TRIGGER_CHAN_2:
    cmd += "CHANNEL2";
    break;
  case TRIGGER_CHAN_3:
    // TODO lis�� tarkistus skoopin tyypist�
    cmd += "CHANNEL3";
    break;
  case TRIGGER_CHAN_4:
    cmd += "CHANNEL4";
    break;
  case TRIGGER_EXT:
    cmd += "EXTERNAL";
    break;
  case TRIGGER_LINE:
    cmd += "LINE";
    break;
  }

  cmd += "\n";
  this->write_string(cmd);
}

/* Function sets trigger slope
 *
 * Parameters:
 * str: slope select string
 *
 * Returns:
 *
 */

void hp54600::set_trigger_slope(uint8_t mode) {
  string cmd =":TRIGGER:SLOPE ";

  switch(mode) {
  case 0:
    cmd += "POSITIVE";
    break;
  case 1:
    cmd += "NEGATIVE";
    break;
  }

  cmd+= "\n";
  this->write_string(cmd);
}

/* Function sets trigger coupling
 *
 * Parameters:
 * str: coupling select string
 *
 * Returns:
 *
 */

void hp54600::set_trigger_couple(uint8_t mode) {
  string cmd =":TRIGGER:COUPLING ";

  switch(mode) {
  case 0:
    cmd += "AC";
    break;
  case 1:
    cmd += "DC";
    break;
  }

  cmd += "\n";
  this->write_string(cmd);
}

/* Function sets trigger reject
 *
 * Parameters:
 * str: reject string
 *
 * Returns:
 *
 */

void hp54600::set_trigger_reject(uint8_t mode) {
  string cmd =":TRIGGER:REJECT ";

  switch(mode) {
  case TRIGGER_REJ_OFF:
    cmd += "OFF";
    break;
  case TRIGGER_REJ_LF:
    cmd += "LF";
    break;
  case TRIGGER_REJ_HF:
    cmd += "HF";
    break;
  }

  cmd += "\n";
  this->write_string(cmd);
}

/* Function sets trigger noise reject
 *
 * Parameters:
 * str: noise reject string
 *
 * Returns:
 *
 */

void hp54600::set_trigger_noise_reject(uint8_t mode) {
  string cmd =":TRIGGER:NREJECT ";

  switch(mode) {
  case 0:
    cmd += "OFF";
    break;
  case 1:
    cmd += "ON";
    break;
  }

  cmd += "\n";
  this->write_string(cmd);
}

/* Function sets trigger polarity
 *
 * Parameters:
 * str: polarity string
 *
 * Returns:
 *
 */

void hp54600::set_trigger_polarity(uint8_t mode) {
  string cmd =":TRIGGER:POLARITY ";

  switch(mode) {
  case 0:
    cmd += "POSITIVE";
    break;
  case 1:
    cmd += "NEGATIVE";
    break;
  }

  cmd += "\n";
  this->write_string(cmd);
}

/* Function sets trigger tv mode
 *
 * Parameters:
 * str: tv mode string
 *
 * Returns:
 *
 */

void hp54600::set_trigger_tv_mode(uint8_t mode) {
  string cmd =":TRIGGER:TVMODE ";

  switch(mode) {
  case TRIGGER_TV_LINE:
    cmd += "LINE";
    break;
  case TRIGGER_FIELD1:
    cmd += "FIELD1";
    break;
  case TRIGGER_FIELD2:
    cmd += "FIELD2";
    break;
  case TRIGGER_TV_VERT:
    cmd += "VERTICAL";
    break;
  }

  cmd += "\n";
  this->write_string(cmd);
}

/* Function sets trigger tv hf reject
 *
 * Parameters:
 * str: tv hf reject string
 *
 * Returns:
 *
 */

void hp54600::set_trigger_tv_hf_reject(uint8_t mode) {
  string cmd =":TRIGGER:TVHFREJ ";

  switch(mode) {
  case 0:
    cmd += "OFF";
    break;
  case 1:
    cmd += "ON";
    break;
  }

  cmd += "\n";
  this->write_string(cmd);
}

/* Function sets trigger reject
 *
 * Parameters:
 * v: trigger level
 *
 * Returns:
 *
 */

void hp54600::set_trigger_level(double v) {
  const char *cmd = ":TRIGGER:LEVEL";

  // write command to target
  this->writef("%s %.2e\n", cmd, v);
}

/* Function sets trigger hold off
 *
 * Parameters:
 * v: trigger level
 *
 * Returns:
 *
 */

void hp54600::set_trigger_hold_off(double v) {
  const char *cmd = ":TRIGGER:HOLDOFF";

  // write command to target
  this->writef("%s %.2e\n", cmd, v);
}

/* Function reads trigger setup from device
 *
 * Parameters:
 *
 * Returns:
 * trigger paramemters as structure
 *
 */

hp54600::trigger_info hp54600::get_trigger_parameters(void) {
  string *result, tmp;
  //string limit = " ";

  // send command and read answer
  string cmd =":TRIGGER:SETUP?\n";
  this->write_string(cmd);

#if GPIB_TEST
  result = new string("TRIG:MODE AUTL;SOURCE CHAN1;LEVEL -1.25000000E-001;HOLD +2.00000000E-007;SLOPE POS;COUP AC;REJ OFF;NREJ OFF;POL NEG;TVMODE FIELD1;TVHF OFF");
#else
  result = this->read_string(256);
#endif

  // check result and return existing values if read failed
  if(result == NULL) {
    log.error("failed to read channel parameters from device ");
    return(trigger_params);
  }

  unsigned start = result->find_first_of(":");
  string a = result->substr(start + 1);

  // get string tokens
  tokenizer *token = new tokenizer(a, ";");

  // parse trigger mode
  if(token->next_token(";")) {
    // mode line
    tmp = token->get_token();

    // find out current mode
    if(tmp.compare("MODE AUTL") == 0)
      trigger_params.mode = TRIGGER_AUTOLEVEL;
    if(tmp.compare("MODE AUTO") == 0)
      trigger_params.mode = TRIGGER_AUTO;
    if(tmp.compare("MODE NORM") == 0)
      trigger_params.mode = TRIGGER_NORMAL;
    if(tmp.compare("MODE SING") == 0)
      trigger_params.mode = TRIGGER_SINGLE;
    if(tmp.compare("MODE TV") == 0)
      trigger_params.mode = TRIGGER_TV;

  }

  // parse trigger source
  if(token->next_token(";")) {
    // mode line
    tmp = token->get_token();

    // find out current trigger source
    if(tmp.compare("SOURCE CHAN1") == 0)
      trigger_params.source = TRIGGER_CHAN_1;
    if(tmp.compare("SOURCE CHAN2") == 0)
      trigger_params.source = TRIGGER_CHAN_2;
    if(tmp.compare("SOURCE CHAN3") == 0)
      trigger_params.source = TRIGGER_CHAN_3;
    if(tmp.compare("SOURCE CHAN4") == 0)
      trigger_params.source = TRIGGER_CHAN_4;
    if(tmp.compare("SOURCE EXT") == 0)
      trigger_params.source = TRIGGER_EXT;
    if(tmp.compare("SOURCE LINE") == 0)
      trigger_params.source = TRIGGER_LINE;
  }

  // parse trigger level
  if(token->next_token(";")) {
    tmp = token->get_token();
    tmp = tmp.substr(tmp.find_first_of(" ") + 1);

    trigger_params.level = strtof(tmp.c_str(), NULL);
  }

  // parse trigger hold off
  if(token->next_token(";")) {
    tmp = token->get_token();
    tmp = tmp.substr(tmp.find_first_of(" ") + 1);

    trigger_params.hold_off = strtof(tmp.c_str(), NULL);
  }

  // parse trigger slope
  if(token->next_token(";")) {
    // mode line
    tmp = token->get_token();

    // find out current trigger slope
    if(tmp.compare("SLOPE POS") == 0)
      trigger_params.slope = true;
    else
      trigger_params.slope = false;
  }

  // parse trigger coupling
  if(token->next_token(";")) {
    // mode line
    tmp = token->get_token();

    // find out current trigger coupling
    if(tmp.compare("COUP AC") == 0)
      trigger_params.coupling = true;
    else
      trigger_params.coupling = false;
  }

  // parse trigger reject
  if(token->next_token(";")) {
    // mode line
    tmp = token->get_token();

    // find out current trigger reject
    if(tmp.compare("REJ OFF") == 0)
      trigger_params.reject = TRIGGER_REJ_OFF;
    if(tmp.compare("REJ LF") == 0)
      trigger_params.reject = TRIGGER_REJ_LF;
    if(tmp.compare("REJ HF") == 0)
      trigger_params.reject = TRIGGER_REJ_HF;
  }

  // parse trigger noise reject
  if(token->next_token(";")) {
    // mode line
    tmp = token->get_token();

    // find out current trigger noise reject
    if(tmp.compare("NREJ OFF") == 0)
      trigger_params.nreject = false;
    else
      trigger_params.nreject = true;
  }

  // parse trigger polarity
  if(token->next_token(";")) {
    // mode line
    tmp = token->get_token();

    // find out current trigger polarity
    if(tmp.compare("POL NEG") == 0)
      trigger_params.polarity = false;
    else
      trigger_params.polarity = true;
  }

  // parse trigger tv mode
  if(token->next_token(";")) {
    // mode line
    tmp = token->get_token();

    // find out current trigger tv mode
    if(tmp.compare("TVMODE FIELD1") == 0)
      trigger_params.tv_mode = TRIGGER_FIELD1;
    if(tmp.compare("TVMODE FIELD2") == 0)
      trigger_params.tv_mode = TRIGGER_FIELD2;
    if(tmp.compare("TVMODE LINE") == 0)
      trigger_params.tv_mode = TRIGGER_TV_LINE;
    if(tmp.compare("TVMODE VERT") == 0)
      trigger_params.tv_mode = TRIGGER_TV_VERT;
  }

  // parse trigger tv HF reject
  if(token->next_token(";")) {
    // mode line
    tmp = token->get_token();

    // find out current trigger tv hf reject
    if(tmp.compare("TVHF OFF") == 0)
      trigger_params.tv_hf_reject = false;
    else
      trigger_params.tv_hf_reject = true;
  }

  // free allocated space
  delete(token);
  delete(result);

  log.debug("trigger mode %d", trigger_params.mode);
  log.debug("trigger source %d", trigger_params.source);
  log.debug("trigger level %lf", trigger_params.level);
  log.debug("trigger hold off %lf", trigger_params.hold_off);
  log.debug("trigger slope %d", trigger_params.slope);
  log.debug("trigger coupling %d", trigger_params.coupling);
  log.debug("trigger reject %d", trigger_params.reject);
  log.debug("trigger noise reject %d", trigger_params.nreject);
  log.debug("trigger polarity %d", trigger_params.polarity);
  log.debug("trigger tv mode %d", trigger_params.tv_mode);
  log.debug("trigger tv HF reject %d", trigger_params.tv_hf_reject);

  return(trigger_params);
}

void hp54600::set_waveform_format(waveform_format f) {

}
void hp54600::set_waveform_byte_order(bool b) {

}
void hp54600::set_waveform_size(waveform_size size) {

}

/* Function write waveform source to device
 *
 * Parameters:
 * ch: source channel number 1,2 (,3,4)
 *
 * Returns:
 *
 */

void hp54600::set_waveform_source(uint8_t ch) {
  const char *cmd = ":WAVEFORM:SOURCE CHANNEL";

  // write command to target
  this->writef("%s%d\n", cmd, ch);
}

/* Function reads waveform from device
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 * data: pointer to data storage pointer
 *
 * Returns:
 * number of bytes read
 *
 */

uint32_t hp54600::get_waveform_data(uint8_t ch, char **data) {
  uint32_t result = 0;

  // check current data format
  if(wave_params[ch - 1].format != FORMAT_BYTE) {
    // use byte format
    set_waveform_format(FORMAT_BYTE);
  }

  // send command and read answer
  string cmd =":WAVEFORM:DATA?\n";
  this->write_string(cmd);

#ifdef GPIB_TEST
  for(int i = 0; i < 1011;i++) {
    int x[] = {0,0,0,0,0,0,0,0,0,0,0,
        -66, -65, -65, -65, -66, -66, -65, -66, -66, -66, -66, -66, -66, -66, -66, -67, -66, -67, -67, -67, -67, -68, -68, -68, -68, -68, -68, -69, -69, -69, -69, -70, -70, -70, -70, -70, -70, -71, -71, -71, -71, -72, -72, -73, -73, -73, -73, -74, -74, -75, -74, -75, -76, -76, -76, -77, -77, -77, -78, -78, -79, -79, -80, -80, -81, -81, -81, -82, -82, -83, -84, -83, -84, -85, -85, -86, -87, -86, -87, -88, -88, -89, -90, -90, -90, -91, -92, -92, -93, -94, -94, -95, -95, -96, -97, -97, -98, -98, -99, -100,
        -100, -100, -100, -102, -103, -103, -104, -105, -105, -105, -106, -107, -108, -108, -109, -109, -110, -111, -111, -112, -113, -113, -114, -115, -115, -117, -116, -117, -118, -118, -120, -120, -121, -122, -122, -123, -123, -124, -125, -126, -127, -127, -128, 127, 127, 126, 126, 125, 125, 123, 123, 122, 121, 121, 120, 119, 118, 118, 117, 116, 116, 115, 115, 114, 113, 113, 111, 111, 111, 109, 109, 109, 108, 107, 107, 107, 105, 105, 105, 103, 103, 102, 102, 101, 101, 100, 99, 99, 99, 97, 97, 96, 96, 95, 95, 94, 93, 93, 92, 92,
        91, 91, 90, 90, 89, 89, 88, 88, 86, 87, 86, 85, 85, 85, 84, 83, 83, 82, 82, 81, 81, 81, 80, 80, 79, 80, 79, 79, 78, 77, 77, 77, 77, 76, 75, 75, 75, 75, 75, 74, 73, 73, 73, 73, 73, 72, 72, 72, 72, 71, 71, 71, 70, 70, 70, 70, 69, 69, 69, 69, 69, 69, 68, 68, 68, 68, 68, 68, 68, 68, 67, 68, 67, 67, 68, 66, 67, 67, 67, 66, 67, 66, 66, 66, 66, 66, 66, 67, 67, 67, 66, 67, 67, 67, 67, 67, 67, 67, 67, 67,
        68, 68, 68, 68, 69, 68, 69, 69, 69, 69, 69, 69, 69, 70, 70, 71, 70, 71, 71, 71, 72, 72, 72, 72, 73, 72, 74, 74, 74, 74, 75, 75, 75, 76, 76, 76, 77, 77, 77, 78, 79, 79, 79, 79, 80, 80, 81, 81, 82, 82, 83, 83, 84, 83, 85, 84, 85, 85, 86, 87, 87, 87, 88, 88, 89, 90, 91, 90, 91, 93, 93, 93, 94, 94, 95, 96, 96, 97, 97, 98, 99, 99, 99, 100, 101, 102, 103, 103, 103, 104, 105, 105, 106, 106, 107, 107, 109, 109, 110, 110,
        110, 112, 112, 112, 113, 114, 114, 115, 117, 117, 117, 118, 119, 119, 119, 121, 121, 123, 123, 123, 124, 125, 125, 126, 127, -128, -127, -127, -126, -125, -125, -124, -124, -123, -122, -122, -121, -120, -120, -118, -118, -118, -116, -116, -115, -114, -114, -114, -112, -112, -112, -110, -110, -110, -109, -108, -108, -107, -107, -105, -105, -104, -104, -104, -102, -102, -101, -101, -100, -100, -99, -98, -98, -98, -97, -96, -96, -95, -94, -94, -93, -92, -92, -91, -91, -91, -89, -89, -89, -88, -88, -87, -86, -86, -85, -85, -85, -84, -84, -83,
        -83, -82, -82, -81, -81, -80, -80, -80, -79, -79, -79, -78, -77, -77, -77, -76, -76, -76, -76, -75, -75, -75, -74, -73, -73, -73, -72, -72, -72, -71, -71, -72, -71, -70, -70, -70, -70, -70, -69, -69, -68, -68, -68, -68, -68, -68, -68, -68, -67, -67, -67, -67, -67, -67, -67, -66, -66, -66, -66, -66, -66, -66, -66, -66, -66, -65, -66, -66, -65, -65, -66, -66, -65, -66, -66, -65, -66, -66, -66, -66, -67, -66, -67, -67, -67, -67, -67, -67, -68, -68, -68, -67, -68, -68, -69, -69, -68, -69, -69, -69,
        -70, -69, -70, -70, -71, -71, -71, -72, -72, -72, -73, -73, -73, -73, -74, -74, -75, -75, -75, -76, -76, -77, -77, -77, -77, -78, -79, -80, -79, -79, -80, -81, -81, -82, -82, -82, -83, -83, -83, -85, -85, -86, -86, -87, -87, -87, -88, -88, -89, -89, -90, -90, -92, -92, -92, -93, -94, -94, -94, -95, -96, -96, -97, -98, -98, -99, -99, -100, -101, -101, -102, -102, -103, -104, -105, -105, -105, -107, -107, -107, -108, -109, -110, -111, -111, -112, -112, -112, -113, -114, -115, -115, -116, -117, -118, -118, -119, -119, -121, -121,
        -122, -122, -122, -123, -125, -125, -126, -127, -127, -128, 127, 127, 126, 126, 125, 124, 123, 123, 122, 121, 121, 119, 119, 119, 118, 117, 117, 116, 115, 114, 114, 113, 113, 112, 112, 110, 110, 109, 109, 108, 107, 106, 106, 106, 105, 105, 103, 103, 102, 102, 101, 100, 100, 99, 99, 98, 98, 97, 97, 95, 95, 95, 95, 93, 93, 92, 92, 91, 91, 90, 89, 89, 88, 88, 87, 87, 86, 85, 85, 85, 85, 84, 83, 83, 82, 82, 82, 81, 81, 80, 80, 80, 79, 79, 78, 78, 77, 77, 77, 76,
        75, 76, 75, 75, 75, 75, 74, 74, 73, 73, 73, 72, 72, 72, 72, 72, 71, 70, 70, 70, 70, 70, 69, 69, 69, 69, 69, 69, 68, 69, 68, 68, 68, 68, 68, 67, 68, 68, 67, 67, 67, 67, 67, 66, 67, 66, 66, 66, 67, 66, 66, 66, 70, 67, 66, 66, 67, 67, 67, 67, 66, 67, 67, 67, 68, 67, 67, 68, 68, 68, 68, 68, 69, 69, 69, 69, 69, 69, 69, 70, 69, 70, 70, 71, 71, 72, 71, 71, 72, 72, 72, 73, 73, 74, 74, 74, 74, 75, 75, 76,
        76, 76, 76, 76, 77, 78, 78, 78, 78, 80, 79, 80, 80, 81, 81, 82, 83, 83, 83, 83, 84, 84, 85, 85, 86, 86, 87, 88, 88, 87, 89, 90, 90, 90, 91, 92, 92, 93, 93, 94, 95, 95, 96, 96, 97, 97, 99, 98, 99, 99, 99, 101, 101, 102, 103, 103, 104, 105, 106, 106, 107, 108, 108, 109, 109, 110, 111, 111, 111, 112, 113, 113, 114, 115, 116, 116, 116, 117, 118, 119, 119, 120, 120, 122, 123, 123, 124, 125, 125, 126, 126, 127, 127, -127, -126, -126, -125, -125, -124, -124,
        -122, -122, -121, -121, -120, -119, -119, -117, -117, -116, -116, -115, -114, -114, -113, -113, -112, -111, -111, -110, -110, -109, -108, -107, -108, -106, -106, -105, -104, -104, -103, -102, -102, -102, -101, -100, -100, -99, -98, -97, -97, -97, -96, -96, -95, -94, -94, -93, -92, -92, -92, -91, -90, -89, -89, -89, -88, -87, -87, -86, -86, -85, -85, -84, -84, -84, -83, -83, -82, -81, -81, -81, -80, -80, -80, -79, -79, -78, -78, -78, -77, -76, -76, -76, -76, -75, -75, -75, -75, -74, -73, -73, -73, -72, -73, -72, -71, -71, -71, -71,
        -71, -70, -70, -69, -69, -69, -69, -69, -68, -69, -69, -68, -68, -68, -68, -68, -67, -67, -67, -67, -67, -67, -66, -67, -66, -67, -66, -66, -66, -66, -66, -66, -65, -65, -66, -66, -65, -65, -65, -66, -66, -66, -66, -66, -66, -66, -66, -66, -66, -67, -67, -67, -67, -67, -67, -68, -67, -68, -68, -68, -68, -69, -69, -70, -69, -69, -69, -70, -70, -70, -71, -71, -72, -71, -72, -73, -72, -73, -74, -74, -74, -74, -74, -75, -75, -76, -76, -76, -77, -77, -77, -78, -78, -79, -80, -80, -80, -80, -81, -81,
        -82, -82, -83, -83, -83, -84, -84, -85, -85, -86, -86, -88, -88, -88, -88, -89, -89, -90, -91, -91, -92, -92, -93, -94, -94, -95, -95, -96, -96, -98, -98, -98, -99, -100, -100, -101, -102, -103, -102, -103, -104, -104, -105, -106, -107, -108, -107, -109, -109, -110, -110, -111, -112, -113, -112, -114, -114, -115, -116, -117, -117, -117, -118, -119, -119, -120, -121, -122, -122, -123, -124, -124, -125, -125, -127, -128, -128, 127, 126, 125, 125, 124, 123, 123, 122, 121, 122, 120, 120, 119, 118, 117, 117, 116, 115, 115, 114, 113, 112, 113,
        111, 111, 110, 110, 108, 108, 108, 107, 106, 105, 105, 105, 104, 103, 103, 102, 102, 101, 100, 99, 99, 98, 98, 97, 97, 96, 95, 95, 94, 94, 93, 92, 92, 91, 91, 90, 90, 90, 89, 88, 87, 87, 86, 86, 85, 85, 85, 84, 83, 84, 83, 82, 82, 82, 81, 80, 81, 80, 79, 79, 79, 78, 77, 77, 77, 77, 76, 76, 75, 76, 75, 75, 74, 73, 74, 74, 73, 73, 72, 72, 72, 71, 71, 71, 71, 71, 70, 70, 69, 69, 69, 69, 69, 69, 69, 69, 68, 68, 68, 68,
        68, 68, 68, 67, 68, 67, 67, 67, 67, 66, 67, 67, 66, 66, 66, 66, 66, 66, 66, 66, 67, 66, 67, 67, 67, 67, 67, 67, 67, 67, 67, 67, 68, 68, 68, 68, 68, 68, 68, 68, 69, 69, 68, 69, 69, 69, 70, 69, 70, 71, 71, 70, 71, 71, 72, 72, 72, 73, 73, 73, 74, 74, 74, 74, 75, 75, 76, 75, 76, 77, 76, 77, 77, 78, 79, 79, 79, 79, 80, 81, 81, 81, 81, 83, 83, 83, 83, 84, 84, 85, 86, 86, 87, 87, 87, 88, 88, 89, 90, 90,
        91, 91, 92, 92, 93, 93, 94, 95, 95, 95, 97, 97, 98, 98, 99, 99, 100, 101, 101, 102, 103, 103, 103, 104, 105, 105, 106, 107, 108, 108, 109, 109, 110, 111, 111, 112, 113, 114, 114, 115, 115, 116, 116, 117, 117, 118, 119, 120, 120, 121, 122, 122, 123, 124, 124, 125, 126, 126, 127, -128, -127, -127, -126, -125, -124, -124, -123, -123, -121, -121, -121, -120, -119, -118, -118, -117, -116, -116, -115, -114, -114, -114, -112, -112, -111, -110, -110, -109, -109, -108, -107, -106, -106, -106, -105, -104, -103, -102, -103, -102,
        -101, -100, -99, -99, -99, -97, -97, -97, -97, -96, -95, -94, -94, -93, -92, -92, -91, -91, -91, -90, -90, -89, -88, -88, -87, -87, -87, -86, -86, -84, -85, -84, -83, -83, -82, -82, -82, -81, -80, -80, -80, -80, -79, -79, -78, -77, -77, -77, -77, -77, -75, -76, -75, -74, -74, -74, -74, -73, -73, -73, -73, -73, -72, -72, -72, -71, -71, -71, -70, -70, -69, -70, -69, -69, -69, -69, -68, -68, -68, -68, -68, -69, -67, -67, -68, -67, -67, -67, -67, -66, -67, -66, -66, -66, -66, -66, -66, -65, -66, -66,
        -65, -66, -66, -65, -66, -66, -66, -67, -66, -66, -66, -67, -67, -67, -67, -67, -67, -67, -67, -68, -67, -67, -68, -68, -68, -68, -68, -68, -68, -69, -69, -69, -70, -70, -70, -70, -70, -71, -71, -72, -72, -72, -71, -72, -73, -73, -73, -74, -74, -75, -75, -75, -76, -76, -76, -76, -77, -78, -77, -78, -79, -79, -80, -80, -80, -81, -81, -81, -82, -82, -83, -84, -84, -85, -85, -85, -86, -87, -87, -88, -88, -89, -90, -90, -91, -90, -92, -92, -93, -93, -94, -94, -95, -95, -96, -97, -97, -98, -99, -99,
        -100, -100, -101, -102, -103, -103, -104, -105, -104, -105, -107, -107, -108, -109, -108, -109, -110, -111, -111, -112, -112, -113, -114, -114, -116, -116, -116, -117, -117, -118, -118, -119, -121, -121, -122, -123, -123, -124, -124, -125, -126, -127, -128, 127, 127, 126, 125, 125, 124, 123, 123, 122, 121, 121, 120, 120, 119, 118, 117, 116, 116, 115, 114, 115, 114, 113, 112, 111, 111, 110, 109, 109, 109, 107, 107, 106, 106, 104, 105, 103, 103, 103, 102, 101, 101, 100, 99, 99, 99, 98, 97, 96, 96, 95, 94, 95, 93, 93, 92, 93,
        91, 90, 90, 89, 89, 89, 88, 88, 87, 86, 86, 86, 85, 84, 84, 84, 83, 83, 82, 82, 81, 81, 81, 80, 80, 79, 79, 79, 78, 77, 77, 76, 77, 76, 76, 75, 75, 75, 74, 74, 74, 73, 73, 73, 72, 72, 72, 72, 71, 71, 71, 70, 70, 70, 69, 70, 69, 69, 69, 69, 69, 68, 68, 69, 68, 68, 68, 68, 68, 68, 68, 68, 67, 67, 67, 67, 67, 67, 66, 67, 66, 66, 66, 66, 66, 66, 66, 67, 66, 66, 67, 66, 66, 67, 67, 67, 67, 67, 67, 67
    };
    (*data)[i] = x[i];
  }

  result = 1011;

#else
  // read back byte data
  result = this->read((*data), wave_params[ch - 1].bytes + 11);
#endif

  return(result);
}

/* Function returns current waveform preamble from memory
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 *
 * Returns:
 * waveform preamble info in a struct
 *
 */

hp54600::waveform_preamble hp54600::get_latest_waveform_parameters(uint8_t ch) {
  return(wave_params[ch - 1]);
}

/* Function reads waveform preamble header from device
 *
 * Parameters:
 * ch: channel number 1,2 (,3,4)
 *
 * Returns:
 * waveform preamble info in a struct
 *
 */

hp54600::waveform_preamble hp54600::get_waveform_parameters(uint8_t ch) {
  string *result;
  string limit = ",";
  uint8_t ch_tmp = ch - 1;

  if(ch_tmp < this->get_channel_cnt()) {
    // send command and read answer
    string cmd =":WAVEFORM:PREAMBLE?\n";
    this->write_string(cmd);

#ifdef GPIB_TEST
    result = new string("+1,+1,+2000,+1,4.999999987376214E-7,-5.000000237487257E-4,+0,+0.0625,+0.00000000E+000,+128");
#else
    result = this->read_string(256);
#endif

    // check result and return existing values if read failed
    if(result == NULL) {
      return(wave_params[ch_tmp]);
    }

    // get string tokens
    tokenizer *token = new tokenizer(*result, limit);

    // parse waveform format
    if(token->next_token(limit)) {
      wave_params[ch_tmp].format = (waveform_format)strtol(token->get_token().c_str(), NULL, 10);
    }

    // parse waveform type
    if(token->next_token(limit)) {
      wave_params[ch_tmp].type = (waveform_type)strtol(token->get_token().c_str(), NULL, 10);
    }

    // parse storage length of waveform
    if(token->next_token(limit)) {
      wave_params[ch_tmp].bytes = (waveform_size)strtol(token->get_token().c_str(), NULL, 10);
    }

    // always 1
    if(token->next_token(limit)) {
      wave_params[ch_tmp].count = (uint8_t)strtol(token->get_token().c_str(), NULL, 10);
    }

    // parse x increment
    if(token->next_token(limit)) {
      wave_params[ch_tmp].x_increment = strtof(token->get_token().c_str(), NULL);
    }

    // parse x origin
    if(token->next_token(limit)) {
      wave_params[ch_tmp].x_origin = strtof(token->get_token().c_str(), NULL);
    }

    // parse x reference
    if(token->next_token(limit)) {
      wave_params[ch_tmp].x_ref = (uint32_t)strtol(token->get_token().c_str(), NULL, 10);
    }

    // parse y increment
    if(token->next_token(limit)) {
      wave_params[ch_tmp].y_increment = strtof(token->get_token().c_str(), NULL);
    }

    // parse y origin
    if(token->next_token(limit)) {
      wave_params[ch_tmp].y_origin = strtof(token->get_token().c_str(), NULL);
    }

    // parse y reference
    if(token->next_token(limit)) {
      wave_params[ch_tmp].y_ref = (uint32_t)strtol(token->get_token().c_str(), NULL, 10);
    }

    // free allocated space
    delete(token);
    delete(result);

    log.debug("waveform format %d", wave_params[ch_tmp].format);
    log.debug("waveform type %d", wave_params[ch_tmp].type);
    log.debug("waveform length %d", wave_params[ch_tmp].bytes);
    log.debug("x increment %f", wave_params[ch_tmp].x_increment);
    log.debug("x origin %f", wave_params[ch_tmp].x_origin);
    log.debug("x reference %d", wave_params[ch_tmp].x_ref);
    log.debug("y increment %f", wave_params[ch_tmp].y_increment);
    log.debug("y origin %f", wave_params[ch_tmp].y_origin);
    log.debug("y reference %d", wave_params[ch_tmp].y_ref);

    log.debug("V/Div %fV", 32.0F * wave_params[ch_tmp].y_increment);
    log.debug("Offset %fV", (128 - wave_params[ch_tmp].y_ref) * wave_params[ch_tmp].y_increment + wave_params[ch_tmp].y_origin);
    log.debug("s / div %fs", wave_params[ch_tmp].bytes / wave_params[ch_tmp].x_origin * 10.0F);
    log.debug("Delay %f s", wave_params[ch_tmp].bytes / 2 - wave_params[ch_tmp].x_ref * wave_params[ch_tmp].x_origin + wave_params[ch_tmp].x_origin);
  }

  return(wave_params[ch_tmp]);
}
}
