/***********************************************************
 * Software: instrument client
 * Module:   Agilent E4421 instrument constants class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 23.1.2014
 *
 ***********************************************************/
package instruments.agilent4421b;

public class E4421Constants {

    public static final String MessageOutputSate = "Output";
    public static final String MessageModulationOutput = "Modulation";
    public static final String MessageFrequency = "Frequency";
    public static final String MessageReadFrequency = "ReadFrequency";
    public static final String MessageAmplitude = "Amplitude";
    public static final String MessageReadAmplitude = "ReadAmplitude";
    public static final String MessageModulationState = "ModulationState";
    public static final String MessageModulationWaveform = "ModulationWaveform";
    public static final String MessageModulationSource = "ModulationSource";
    public static final String MessageModulationValue = "ModulationValue";
    public static final String MessageModulationRate = "ModulationRate";
    
    public static final String PARAM_AMPLITUDE = "amplitude";
    public static final String PARAM_FREQUENCY = "frequency";
    public static final String PARAM_MAIN_SWEEP = "main_sweep";
    public static final String PARAM_MAIN_SWEEP_START = "main_sweep_start";
    public static final String PARAM_MAIN_SWEEP_END = "main_sweep_end";
    public static final String PARAM_MAIN_SWEEP_STEP = "main_sweep_step";
    public static final String PARAM_NESTED_SWEEP = "nested_sweep";
    public static final String PARAM_NESTED_SWEEP_START = "nested_sweep_start";
    public static final String PARAM_NESTED_SWEEP_END = "nested_sweep_end";
    public static final String PARAM_NESTED_SWEEP_STEP = "nested_sweep_step";
    public static final String PARAM_TIMER_STEP = "timer_step";
    public static final String PARAM_NESTED_SWEEP_ENABLED = "nested_sweep_enabled";
    public static final int SWEEP_STOP = 0;
    public static final int SWEEP_AMPLITUDE = 1;
    public static final int SWEEP_FREQUENCY = 2;
    public static final int SWEEP_AMPLITUDE_NESTED_FREQUENCY = 3;
    public static final int SWEEP_FREQUENCY_NESTED_AMPLITUDE = 4;
    public static final int frequencyIndex = 0;
    public static final int amplitudeIndex = 1;
    public static final double frequencyMin = 10000.0D;
    public static final double frequencyMax = 30000000000.0D;
    public static final double amplitudeMin = -131.0D;
    public static final double amplitudeMax = 20.0D;
    public static final int MODE_AM_1 = 0;
    public static final int MODE_AM_2 = 1;
    public static final int MODE_FM_1 = 2;
    public static final int MODE_FM_2 = 3;
    public static final int MODE_M_1 = 4;
    public static final int MODE_M_2 = 5;
    //public static final int MODE_PULSE = 0;
}
