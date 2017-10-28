/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vmconsolidation;


import peersim.config.*;
import peersim.core.*;
;

/**
 *
 * @author Omid
 */
public class CalculateMetrics implements Control {
    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The protocol to operate on.
     * 
     * @config
     */
    private static final String PAR_PROT = "protocol";
    
    // ------------------------------------------------------------------------
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

    public CalculateMetrics(String prefix)
    {
        protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
        
        
    }
    
    @Override
    public boolean execute() {
        

        for (int i = 0; i < Network.size(); ++i) {
            
            ((VmPlacement) Network.get(i).getProtocol(protocolID)).MetricsCalculator();
        }
        
        /*for (int i = 0; i < Network.size(); ++i) {
            
            ((VmPlacement) Network.get(i).getProtocol(protocolID)).Overloadingmanagement();
        }*/
        
       /*   for (int i = 0; i < Network.size(); ++i) {
            
            ((VmPlacement) Network.get(i).getProtocol(protocolID)).Underloadingmanagement();
        }*/

        return false;
    }
    
}
