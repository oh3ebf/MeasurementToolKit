/***********************************************************
 * Software: instrument client
 * Module:   Unit number formatter class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 20.2.2013
 *
 ***********************************************************/

package components;

import java.text.DecimalFormat;
import java.text.ParseException;
import javax.swing.text.NumberFormatter;

public class UnitNumberFormatter extends NumberFormatter {
    private String unit;
    
    public UnitNumberFormatter(String unit, String format) {     
        // set formmatting pattern
        super(new DecimalFormat(format));
        // save unit info
        this.unit = unit;        
    }
    
    /**
     * 
     * @param o
     * @return
     * @throws java.text.ParseException
     */
    
    @Override
    public String valueToString(Object o)  throws ParseException {               
            Number number = (Number) o;
            // add unit to string
            return( super.valueToString(number) + "" + unit);
    }

    /**
     * 
     * @param s
     * @return
     * @throws java.text.ParseException
     */
    
    @Override
    public Object stringToValue(String s) throws ParseException {
        Number number = (Number) super.stringToValue(s);
        return number;
    }
}
