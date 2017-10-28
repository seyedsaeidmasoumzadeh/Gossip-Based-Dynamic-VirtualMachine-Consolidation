/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vmconsolidation;

import java.util.Random;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 *
 * @author Omid
 */
public class RandomWorkload implements Control {
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

     public RandomWorkload(String prefix)
    {
     protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
    }
     
    @Override
     public boolean execute() {
        

        Random rn = new Random ();
        
         for (int i = 0; i < Network.size(); ++i) {
            Host host = (Host) Network.get(i).getProtocol(protocolID);
            
            for (int j = 0; j<host.vmList.size(); j++)
            {
                int random = rn.nextInt(host.vmList.get(j).getAllocatedVmMips());
                host.vmList.get(j).setRequestedVmMips(random);
                
            }

            
            
         }
         return false;
         
     }
}
