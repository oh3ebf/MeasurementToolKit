/***********************************************************
 * Software: instrument client
 * Module:   HP8922 scope instances wave data model class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 20.9.2013
 *
 ***********************************************************/
package instruments.hp8922;

import lib.gui.models.DefaultWaveModel;

public class HP8922WaveModel extends DefaultWaveModel {

    /*private double yIncrement = 0.0D;
    private double yReference = 0.0D;
    private double yOrigin = 0.0D;
    private double xIncrement = 0.0D;
    private double xReference = 0.0D;
    private double xOrigin = 0.0D;
*/
    public HP8922WaveModel() {
        super(0);
    }

    /** Function sets waveform preamble variable
     * 
     * @param xInc
     * @param xRef
     * @param xOrig
     * @param yInc
     * @param yRef
     * @param yOrig
     */
 /*   public void setPreambleVariables(double xInc, double xRef, double xOrig, double yInc, double yRef, double yOrig) {
        xIncrement = xInc;
        xReference = xRef;
        xOrigin = xOrig;

        yIncrement = yInc;
        yReference = yRef;
        yOrigin = yOrig;

        testData2();
    }*/

    /** Function updates measured data 
     * 
     * @param b byte array of new data
     */
    @Override
    public void updateData(byte b[]) {
        int j = 0;
        data.clear();

       /* for (int i = 0; i < b.length; i++) {
            //data.add(-(b[i] - yReference) * yIncrement + yOrigin);
            if (b[i] > 0) {
                data.add((double) -(b[i] - 127) * yIncrement + yOrigin);
            } else {
                data.add((double) -(b[i] + 128) * yIncrement + yOrigin);
            }
        }*/
    }
/*
    public void testData2() {
        byte[] b = {
            -55, -55, -55, -54, -55, -55, -54, -54, -54, -54, -53, -53, -53, -53, -53, -52, -52, -52, -52, -52, -51, -52, -52, -51, -51, -52, -51, -51, -52, -51, -52, -52, -52, -51, -51, -52, -51, -52, -53, -53, -53, -53, -54, -54, -54, -54, -54, -54, -54, -54, -55, -55, -55, -56, -55, -56, -57, -56, -56, -58, -58, -58, -58, -59, -59, -59, -60, -60, -61, -62, -61, -62, -63, -63, -63, -65, -64, -65, -65, -66, -66, -67, -67, -67, -68, -69, -70, -70, -71, -71,
            -72, -73, -74, -74, -74, -75, -76, -76, -77, -78, -79, -80, -80, -81, -82, -82, -83, -84, -85, -85, -86, -87, -88, -88, -89, -90, -91, -91, -92, -93, -94, -94, -96, -97, -98, -98, -99, -101, -101, -102, -103, -104, -105, -105, -106, -108, -108, -109, -110, -111, -111, -112, -113, -114, -115, -116, -117, -118, -119, -120, -120, -121, -122, -124, -125, -126, -126, -128, -128, 127, 125, 126, 123, 123, 123, 121, 120, 119, 118, 118, 116, 116, 114, 113, 112, 112, 110, 110, 108, 108, 107, 106, 105, 104, 103, 103, 102, 101, 99, 99,
            98, 97, 97, 96, 95, 94, 93, 91, 91, 90, 89, 89, 88, 86, 86, 85, 85, 83, 83, 82, 81, 80, 79, 79, 78, 78, 77, 76, 75, 73, 73, 72, 72, 71, 70, 69, 69, 69, 67, 67, 66, 65, 64, 64, 64, 63, 62, 62, 61, 60, 59, 59, 58, 58, 57, 56, 56, 55, 55, 55, 54, 53, 53, 53, 52, 52, 51, 51, 51, 50, 50, 49, 48, 48, 48, 47, 47, 46, 46, 46, 45, 46, 46, 45, 44, 44, 44, 44, 44, 44, 43, 43, 43, 42, 42, 43, 42, 42, 42, 42,
            42, 41, 41, 41, 41, 41, 41, 41, 41, 41, 41, 40, 41, 41, 41, 41, 41, 41, 42, 41, 41, 42, 41, 41, 42, 42, 42, 42, 42, 43, 43, 43, 43, 44, 43, 44, 44, 44, 45, 45, 45, 45, 46, 46, 47, 47, 48, 48, 48, 49, 49, 50, 50, 51, 51, 51, 51, 51, 52, 53, 54, 54, 54, 55, 54, 56, 56, 57, 58, 58, 58, 59, 60, 60, 61, 62, 62, 63, 64, 64, 65, 65, 66, 67, 68, 69, 69, 69, 70, 71, 72, 73, 73, 74, 75, 76, 77, 77, 78, 79,
            79, 80, 81, 82, 83, 83, 84, 85, 86, 87, 88, 88, 89, 90, 91, 92, 92, 93, 95, 95, 96, 98, 98, 99, 99, 101, 101, 102, 103, 104, 105, 105, 106, 108, 108, 110, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 122, 123, 124, 125, 127, 127, -128, -127, -126, -125, -124, -123, -122, -121, -121, -120, -119, -118, -116, -116, -115, -114, -114, -112, -112, -111, -110, -108, -108, -108, -106, -105, -105, -103, -103, -103, -101, -99, -99, -98, -97, -97, -96, -95, -94, -93, -93, -92, -91, -91, -89,
            -88, -88, -87, -87, -85, -85, -84, -83, -83, -81, -81, -81, -80, -79, -78, -77, -77, -76, -76, -74, -74, -74, -73, -73, -72, -72, -70, -70, -69, -68, -69, -68, -67, -66, -66, -66, -65, -65, -64, -64, -63, -63, -63, -62, -61, -61, -61, -60, -60, -59, -59, -58, -58, -58, -58, -57, -57, -57, -57, -57, -56, -56, -55, -55, -55, -54, -55, -54, -54, -54, -54, -53, -53, -53, -53, -53, -53, -53, -52, -52, -52, -52, -52, -52, -52, -52, -51, -51, -51, -51, -52, -52, -52, -51, -53, -52, -52, -53, -53, -53,
            -53, -53, -53, -54, -53, -54, -54, -54, -55, -55, -55, -55, -55, -56, -56, -56, -57, -56, -57, -57, -57, -57, -58, -59, -59, -59, -60, -60, -61, -61, -62, -62, -62, -62, -64, -64, -64, -65, -65, -65, -66, -66, -67, -68, -68, -69, -69, -69, -71, -71, -71, -72, -72, -74, -74, -75, -76, -76, -77, -78, -78, -79, -80, -81, -81, -82, -83, -83, -85, -85, -86, -87, -87, -88, -89, -90, -91, -91, -91, -94, -94, -95, -96, -96, -97, -98, -98, -100, -101, -101, -103, -103, -104, -105, -105, -107, -108, -109, -109, -110,
            -111, -111, -113, -113, -115, -116, -117, -117, -118, -119, -120, -121, -122, -123, -124, -125, -126, -127, -128, 127, 126, 125, 125, 124, 123, 121, 121, 120, 119, 118, 117, 116, 115, 114, 113, 112, 111, 110, 109, 108, 108, 107, 106, 105, 104, 103, 102, 101, 101, 99, 99, 98, 98, 96, 95, 95, 93, 93, 92, 90, 90, 89, 89, 87, 86, 86, 85, 84, 83, 82, 81, 81, 80, 79, 78, 77, 76, 76, 75, 74, 74, 73, 72, 72, 70, 70, 69, 68, 68, 66, 66, 65, 65, 65, 64, 63, 62, 62, 61, 61,
            60, 59, 58, 58, 57, 58, 56, 56, 56, 55, 54, 54, 54, 53, 52, 52, 51, 51, 51, 50, 50, 49, 49, 48, 48, 48, 48, 47, 46, 47, 46, 46, 45, 45, 44, 45, 44, 43, 44, 43, 43, 43, 43, 43, 43, 42, 43, 42, 42, 42, 41, 41, 42, 41, 41, 41, 41, 41, 41, 41, 41, 40, 41, 41, 41, 40, 41, 41, 41, 41, 42, 41, 42, 42, 42, 42, 42, 42, 43, 43, 44, 43, 43, 44, 44, 44, 45, 45, 44, 45, 45, 46, 46, 46, 47, 47, 48, 47, 48, 48,
            49, 49, 50, 50, 50, 50, 51, 52, 52, 53, 53, 54, 54, 54, 54, 55, 56, 57, 57, 58, 59, 59, 60, 60, 60, 61, 62, 62, 64, 64, 64, 65, 66, 67, 67, 68, 69, 69, 70, 71, 72, 72, 73, 75, 75, 75, 75, 77, 77, 78, 80, 80, 81, 82, 82, 83, 85, 85, 86, 86, 87, 89, 89, 89, 91, 91, 92, 93, 94, 95, 96, 97, 97, 99, 99, 100, 101, 102, 103, 104, 105, 105, 106, 107, 108, 109, 110, 111, 111, 113, 113, 114, 116, 116, 117, 118, 119, 120, 121, 122,
            123, 124, 125, 126, 127, 127, -128, -127, -126, -124, -123, -123, -121, -120, -120, -119, -118, -117, -117, -116, -114, -114, -112, -112, -111, -110, -110, -108, -108, -106, -106, -105, -104, -103, -102, -101, -101, -100, -99, -99, -97, -96, -96, -94, -94, -93, -92, -91, -90, -89, -89, -88, -88, -86, -86, -86, -84, -83, -82, -82, -81, -81, -80, -79, -79, -78, -77, -76, -76, -75, -74, -74, -74, -72, -72, -71, -71, -70, -70, -69, -68, -68, -67, -67, -66, -66, -65, -65, -65, -65, -64, -63, -63, -63, -62, -62, -61, -60, -60, -60,
            -59, -59, -58, -58, -58, -58, -57, -57, -57, -57, -56, -55, -55, -55, -55, -54, -55, -55, -54, -53, -53, -53, -54, -53, -53, -53, -53, -52, -52, -53, -52, -52, -52, -52, -52, -52, -51, -52, -52, -51, -52, -52, -52, -53, -52, -52, -52, -52, -53, -53, -53, -53, -53, -53, -53, -54, -55, -54, -55, -55, -55, -55, -55, -56, -55, -56, -56, -56, -56, -57, -58, -58, -58, -59, -59, -59, -59, -60, -60, -61, -62, -61, -62, -63, -64, -63, -64, -65, -65, -66, -66, -67, -67, -68, -68, -68, -69, -70, -70, -71,
            -71, -72, -73, -73, -74, -75, -75, -76, -77, -77, -78, -79, -79, -80, -81, -81, -83, -83, -84, -85, -85, -86, -87, -88, -89, -90, -90, -91, -92, -93, -94, -94, -95, -96, -97, -98, -98, -99, -100, -101, -102, -103, -103, -104, -106, -106, -107, -108, -109, -110, -111, -112, -113, -114, -114, -116, -116, -117, -117, -119, -120, -120, -121, -122, -124, -124, -126, -127, -127, 127, 127, 126, 124, 123, 123, 122, 121, 120, 119, 118, 117, 116, 115, 115, 114, 113, 112, 111, 110, 109, 108, 107, 107, 105, 104, 103, 102, 101, 101, 100,
            99, 98, 97, 97, 96, 94, 93, 93, 92, 91, 90, 89, 89, 88, 86, 86, 85, 84, 83, 83, 82, 81, 80, 80, 78, 77, 77, 76, 75, 75, 74, 73, 72, 71, 71, 70, 69, 68, 68, 68, 66, 66, 65, 64, 64, 63, 62, 62, 61, 60, 60, 59, 59, 58, 57, 57, 57, 56, 55, 55, 55, 54, 53, 53, 53, 52, 51, 51, 50, 50, 50, 49, 49, 49, 48, 48, 48, 47, 46, 46, 46, 46, 46, 46, 45, 45, 45, 44, 43, 44, 43, 43, 43, 42, 43, 42, 42, 42, 42, 42,
            42, 42, 42, 41, 41, 41, 41, 41, 41, 40, 41, 40, 41, 40, 40, 41, 41, 41, 41, 42, 41, 41, 42, 41, 41, 42, 42, 41, 42, 42, 43, 43, 43, 43, 44, 44, 44, 44, 44, 45, 45, 45, 46, 46, 46, 47, 47, 48, 48, 48, 48, 49, 50, 50, 50, 51, 51, 52, 52, 53, 53, 53, 55, 54, 55, 56, 56, 57, 57, 57, 58, 59, 59, 60, 61, 61, 61, 62, 63, 64, 64, 65, 65, 66, 67, 68, 69, 69, 70, 70, 71, 73, 74, 73, 74, 76, 76, 77, 78, 78,
            79, 80, 81, 81, 83, 83, 83, 84, 85, 86, 87, 88, 89, 90, 90, 91, 92, 93, 93, 96, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 105, 106, 107, 108, 109, 110, 110, 111, 112, 114, 115, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 125, 127, 127, -127, -126, -126, -125, -123, -123, -121, -120, -120, -119, -119, -117, -117, -116, -115, -114, -113, -112, -111, -110, -109, -109, -107, -107, -106, -105, -104, -103, -102, -101, -101, -100, -99, -98, -97, -96, -96, -95, -94, -94, -92, -92, -91, -91,
            -89, -88, -87, -87, -86, -85, -84, -84, -83, -82, -81, -81, -80, -79, -79, -78, -77, -76, -76, -76, -75, -74, -73, -73, -72, -72, -71, -70, -70, -69, -68, -68, -68, -67, -67, -66, -66, -65, -65, -64, -64, -63, -63, -62, -62, -61, -61, -61, -60, -60, -59, -59, -59, -58, -58, -58, -58, -57, -57, -56, -56, -56, -56, -55, -55, -55, -55, -53, -54, -54, -53, -54, -54, -54, -53, -53, -52, -53, -52, -53, -51, -52, -51, -52, -52, -51, -51, -52, -51, -51, -52, -51, -51, -52, -52, -52, -52, -53, -53, -52,
            -53, -53, -53, -53, -53, -53, -54, -54, -54, -54, -55, -55, -55, -55, -55, -56, -56, -56, -57, -57, -57, -58, -58, -58, -59, -59, -59, -60, -60, -60, -62, -62, -62, -63, -63, -63, -64, -65, -65, -66, -66, -66, -67, -68, -68, -68, -69, -70, -70, -71, -72, -72, -73, -73, -74, -75, -75, -76, -77, -78, -78, -79, -79, -79, -81, -81, -82, -83, -84, -85, -85, -87, -87, -88, -89, -89, -90, -91, -93, -93, -94, -94, -95, -96, -97, -97, -99, -99, -100, -101, -102, -102, -104, -104, -105, -106, -107, -108, -109, -110,
            -111, -112, -112, -113, -114, -115, -116, -117, -118, -119, -121, -120, -121, -123, -124, -124, -126, -126, -127, 127, 126, 126, 125, 123, 123, 122, 121, 120, 119, 118, 117, 116, 116, 114, 113, 113, 112, 111, 110, 109, 108, 107, 106, 105, 105, 104, 103, 103, 101, 100, 99, 99, 97, 96, 96, 94, 94, 93, 93, 91, 90, 90, 88, 88, 87, 86, 85, 84, 84, 83, 82, 81, 81, 80, 79, 78, 77, 77, 75, 75, 74, 73, 72, 72, 71, 70, 69, 69, 68, 68, 67, 66, 65, 65, 64, 64, 63, 62, 61, 61,
            60, 59, 60, 58, 58, 57, 56, 57, 56, 55, 54, 54, 54, 53, 53, 52, 52, 51, 51, 50, 50, 50, 49, 48, 48, 48, 47, 46, 47, 47, 46, 46, 45, 45, 45, 44, 44, 44, 44, 44, 43, 43, 43, 43, 42, 43, 42, 42, 42, 42, 42, 42, 41, 41, 41, 41, 41, 41, 40, 41, 40, 40, 41, 41, 41, 41, 41, 40, 41, 41, 41, 41, 41, 41, 42, 42, 42, 42, 42, 42, 42, 43, 43, 43, 43, 44, 44, 44, 45, 45, 45, 46, 46, 46, 46, 46, 47, 47, 48, 48,
            48, 49, 50, 50, 51, 51, 51, 52, 51, 52
        };

        for (int i = 0; i < b.length; i++) {
            updateData(b);
            //System.out.println("value: " + (b[i] - yReference) * yIncrement + yOrigin);        
        }
    }*/
}