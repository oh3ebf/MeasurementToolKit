/***********************************************************
 * Software: instrument client
 * Module:   instrument setup xml key for data object class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 19.12.2013
 *
 ***********************************************************/

package components;


public class ChannelKey {

    private String keyA;
    private String keyB;
    
    public ChannelKey(String k1, String k2) {
        keyA = k1;
        keyB = k2;
    }
    
    /** Function compares given key 
     * 
     * @param o object to compare
     * @return result of comparison
     */
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (!(o instanceof ChannelKey)) {
            return false;
        }
        
        ChannelKey k = (ChannelKey) o;
        return (k.keyA.equals(keyA) && k.keyB.equals(keyB));
    }

    /** Function calculates hash code for comparison
     * 
     * @return hashcode
     */
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.keyA != null ? this.keyA.hashCode() : 0);
        hash = 37 * hash + (this.keyB != null ? this.keyB.hashCode() : 0);
        return hash;
    }
}
