package networking;

/*
 * netConnect.java
 *
 * Created on January 5, 2004, 5:46 PM
 */

/**
 *
 * @author  operator
 */

import java.net.*;
import java.io.*;
import sun.net.*;

public class netConnect {
    private Socket sock;
    private BufferedReader inB;
    private DataOutputStream out;    
    private boolean StatusFlag = false;
    
    /** Creates a new instance of netConnect */
    public netConnect(String NetAddress,int NetSocket) {
        // valid parameters only accepted
        if((NetAddress != null) && (NetSocket > 1024) && (NetSocket < 65535)) {
            // create emty socket
            sock = new Socket();
            
            try{
                // try to connect with timeout
                sock.connect(new InetSocketAddress(NetAddress, NetSocket), 500);
                
                // create streams
                inB = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                out = new DataOutputStream(sock.getOutputStream());
                // connection created
                StatusFlag = true;
            } catch (IOException e) {
                System.out.println("Socket creation failure");
                sock = null;
            } 
            
        } else {
            // parameters are not valid
            throw new NullPointerException();
        }               
    }
    

    
    /* Function closes open socket and its streams
     *
     * No parameters
     */
    
    public void netClose() {
        if(inB != null && out != null && sock != null) {
            try{
                //in.close();
                inB.close();
                out.close();
                sock.close();
            } catch (IOException e) {
                System.out.println("Socket close failure");
                //System.exit(1);
            }
        } else {
            // trying to close null connection
            throw new NullPointerException();
        }        
    }
    
    /* Function return current status of network connection
     *
     * True: valid connection
     * False: No connection or error
     *
     */
    
    public boolean netStatus() {
        // current state of connection
        return(StatusFlag);
    }
    
    
    /* Function reads line from socket input stream
     *
     * returns:
     * String: read from socket
     *
     */
    
    public String netRead() {
        String response = new String();
        if(inB != null) {
            try{
                // is stream ready to be read
                if(inB.ready() == true) {
                    response = inB.readLine();
                    System.out.println("readline:" + response);
                } else {
                    System.out.println("readline: null");
                    return(null);
                }
            } catch (IOException e) {
                System.out.println("Socket reading failure: readLine");
                return(null);
            }
        } else {
            // trying to read from nowhere
            return(null);
        }
        
        return(response);        
    }
    
    /* Function reads binary packet from socket input stream
     *
     * size: number of bytes to read
     *
     * returns:
     * byte Array: read from socket input stream
     *
     */
    
    public byte[] netReadPacket(int size) {
        char[] response = new char[2 * size];
        byte[] value = new byte[size];
        int status = 0, i = 0;

        if(inB != null) {
            try{                    
                status = inB.read(response, 0, size);  
                System.out.println(status + " bytes read");
                //while(status != -1) {
                //    status = inB.read();
                //    i++;
                //}
            } catch (IOException e) {
                System.out.println("Socket reading failure: read");
            }
            for(i = 0; i < size;i++)
                value[i] = (byte)response[i];
            return(value);
        } else {
            return(null);
        }
    }
    
    /* Function writes binary packet to socket output stream
     *
     * byte Array: packet to write
     *
     * returns:
     *
     */
    
    public boolean netWrite(byte[] b) {
        if(out != null) {
            try {
                //while(flag == true);
                out.write(b);
                //flag = true;
                return(true);
            } catch(IOException e) {
                System.out.println("Socket write failure: write");
                return(false);
            }
        }
        // no connection to write
        return(false);
    }
    
}
