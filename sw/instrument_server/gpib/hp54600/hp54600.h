/***********************************************************
 * Software: instrument server
 * Module:   hp54600 oscilloscope unit headers
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 9.10.2012
 *
 ***********************************************************/

#ifndef HP54600_H_
#define HP54600_H_

#include "gpib_488_2.h"

namespace instrument_server {

using namespace std;
using namespace log4cpp;

class hp54600: public gpib_488_2 {
public:
  enum scope_model {
    MODEL_HP54600 = 0,
    MODEL_HP54601,
    MODEL_HP54602,
    MODEL_HP54603,
    MODEL_HP54610,
    MODEL_HP54615,
    MODEL_HP54616
  };

  enum waveform_format {
    FORMAT_ASCII = 0,
    FORMAT_WORD,
    FORMAT_BYTE
  };

  enum waveform_type {
    TYPE_AVERAGE = 0,
    TYPE_NORMAL,
    TYPE_PEAK
  };

  enum waveform_size {
    SIZE_100 = 100,
    SIZE_200 = 200,
    SIZE_250 = 250,
    SIZE_400 = 400,
    SIZE_500 = 500,
    SIZE_800 = 800,
    SIZE_1000 = 1000,
    SIZE_2000 = 2000,
    SIZE_4000 = 4000,
    SIZE_5000 = 5000
  };

  enum channel_coupling {
    COUPLING_AC = 0,
    COUPLING_DC,
    COUPLING_GND
  };

  enum channel_probe {
    PROBE_1X,
    PROBE_10X,
    PROBE_20X,
    PROBE_100X
  };

  enum trigger_mode {
    TRIGGER_AUTOLEVEL = 0,
    TRIGGER_AUTO,
    TRIGGER_NORMAL,
    TRIGGER_SINGLE,
    TRIGGER_TV
  };

  enum trigger_source {
    TRIGGER_CHAN_1 = 0,
    TRIGGER_CHAN_2,
    TRIGGER_CHAN_3,
    TRIGGER_CHAN_4,
    TRIGGER_EXT,
    TRIGGER_LINE
  };

  enum trigger_reject {
    TRIGGER_REJ_OFF = 0,
    TRIGGER_REJ_LF,
    TRIGGER_REJ_HF
  };

  enum trigger_tv_mode {
    TRIGGER_FIELD1 = 0,
    TRIGGER_FIELD2,
    TRIGGER_TV_LINE,
    TRIGGER_TV_VERT
  };

  enum timebase_mode {
    TIMEBASE_NORMAL = 0,
    TIMEBASE_DELAYED,
    TIMEBASE_XY
  };

  typedef struct {
    string name;
    scope_model model;
    uint8_t channel_cnt;
  } scope_info;

  typedef struct {
    waveform_format format;
    waveform_type type;
    waveform_size bytes;
    uint8_t count;
    float x_increment;
    float x_origin;
    uint32_t x_ref;
    float y_increment;
    float y_origin;
    uint32_t y_ref;
  } waveform_preamble;


  typedef struct {
    double voltage_scale;
    double voltage_offset;
    channel_coupling coupling;
    bool bwlimit;
    bool invert;
    bool vernier;
    channel_probe probe;
  } channel_info;

  typedef struct {
    trigger_mode mode;
    trigger_source source;
    double level;
    double hold_off;
    bool slope;
    bool coupling;
    trigger_reject reject;
    bool nreject;
    bool polarity;
    trigger_tv_mode tv_mode;
    bool tv_hf_reject;
  } trigger_info;

  typedef struct {
    timebase_mode mode;
    double timescale;
    double delay;
    bool reference;
    bool vernier;
  } timebase_info;

  hp54600(string& dev_name, scope_model m, int32_t interface, int32_t pad, int32_t sad);
  hp54600(string& dev_name, scope_model m);
  hp54600(string& dev_name, const string& model);
  virtual ~hp54600();

  uint8_t get_channel_cnt(void);
  scope_model get_scope_model(void);
  void set_scope_model(scope_model m);

  // generic functions
  void autoscale(void);
  void run(void);
  void stop(void);
  uint8_t capture(uint8_t ch);

  // input channel functions
  void set_channel_state(uint8_t ch, bool state);
  bool get_channel_state(uint8_t ch);
  void set_voltage_scale(uint8_t ch, double v);
  void set_voltage_offset(uint8_t ch, double v);
  void set_channel_coupling(uint8_t ch, uint8_t state);
  void set_channel_bwlimit(uint8_t ch, uint8_t state);
  void set_channel_invert(uint8_t ch, uint8_t state);
  void set_channel_vernier(uint8_t ch, uint8_t state);
  void set_channel_probe(uint8_t ch, uint8_t state);
  channel_info get_channel_parameters(uint8_t ch);

  // time base functions
  void set_timebase_delay(string str); // TODO
  void set_timebase_mode(string str); // TODO
  void set_timebase_range(double time);
  void set_timebase_reference(string str); // TODO
  void set_timebase_vernier(string str); // TODO
  timebase_info get_timebase_parameters(void);

  // trigger functions
  void set_trigger_mode(uint8_t mode);
  void set_trigger_source(uint8_t mode);
  void set_trigger_slope(uint8_t mode);
  void set_trigger_couple(uint8_t mode);
  void set_trigger_reject(uint8_t mode);
  void set_trigger_noise_reject(uint8_t mode);
  void set_trigger_polarity(uint8_t mode);
  void set_trigger_tv_mode(uint8_t mode);
  void set_trigger_tv_hf_reject(uint8_t mode);
  void set_trigger_level(double v);
  void set_trigger_hold_off(double v);
  trigger_info get_trigger_parameters(void);

  // waveform functions
  void set_waveform_format(waveform_format f);
  void set_waveform_byte_order(bool b);
  void set_waveform_size(waveform_size size);
  void set_waveform_source(uint8_t ch);
  uint32_t get_waveform_data(uint8_t ch, char **data);
  waveform_preamble get_latest_waveform_parameters(uint8_t ch);
  waveform_preamble get_waveform_parameters(uint8_t ch);

protected:
private:
  const static scope_info models[7];
  waveform_preamble wave_params[4];
  channel_info chan_params[4];
  uint8_t model_number;
  trigger_info trigger_params;
  timebase_info timebase_params;
};

}

#endif /* HP54600_H_ */
