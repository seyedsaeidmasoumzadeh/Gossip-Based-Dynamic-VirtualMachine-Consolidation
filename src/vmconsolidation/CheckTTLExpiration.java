/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vmconsolidation;

import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 *
 * @author Omid
 */
public class CheckTTLExpiration implements Control{
    
    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The protocol to operate on.
     * 
     * @config
     */
    private static final String PAR_PROT = "protocol"; // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Value obtained from config property {@link #PAR_PROT}. */
  
    private final int protocolID; 
    // ------------------------------------------------------------------------
    // Initialization
    // ------------------------------------------------------------------------
    /**
     * Standard constructor that reads the configuration parameters. Invoked by
     * the simulation engine.
     * 
     * @param prefix
     *            the configuration prefix for this class.
     */
   
    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------

     public CheckTTLExpiration(String prefix)
    {
     protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
    }
     
    @Override
     public boolean execute() {
        
     for (int i = 0; i < Network.size(); ++i) {
            Host host = (Host) Network.get(i).getProtocol(protocolID);
            int j = 0;
           while(j < host.vmList.size())
           {
                 
                 if  ( host.vmList.get(j).TTL < CDState.getCycle() * 300)
                 {
                     host.vmList.remove(j);
                     
                 }
                 else
                    j++;
           }
     }
        
        
        
        
     return false;
    }
    
}
