/***********************************************************
 * Software: instrument client
 * Module:   sweep control interface class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 18.2.2013
 *
 ***********************************************************/

package interfaces;

public interface sweepContolBoxInterface {
    public String sweepValues(String id, boolean state, int cycle, 
            double mainStart, double mainEnd, double mainStep,
            double nestedStart, double nestedEnd, double nestedStep);
}
