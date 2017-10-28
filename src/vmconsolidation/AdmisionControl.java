/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vmconsolidation;

import java.util.Random;
import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 *
 * @author Omid
 */
public class AdmisionControl implements Control {
    
   
    
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
    private static Random random;
    private static int vmId;
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

     public AdmisionControl(String prefix)
    {
     protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
     random = new Random();
     vmId = 0;
    }
     
     
     
     
     
     protected int userRequestRandomGenerator()
     {
         int rn = 0;
         double L = 0.016; //Knuth's Algorithm
         
         
         rn =  (int) (- Math.log(1.0 - random.nextDouble()) / L);
         
         //Nothing!! Just for debuging
         Debug.log2.add(rn);
         
         
         return rn;
     }
     
     
     protected int samplePowerLawTTLGentarator (int exponent, int minTimeUnit, int maxTimeUnit)
     {
   
      
      double unirandom = Math.random();
      double samplePowerLaw = 0;
      
      
      //samplePowerLaw = minTimeUnit + (maxTimeUnit - minTimeUnit) * Math.pow(random, (1/(-exponent +1)));
      double temp1= Math.pow(maxTimeUnit, (exponent+1));
      double temp2= Math.pow(minTimeUnit, (exponent+1));
      double temp3= temp1 - temp2;
      double temp4= temp3 * unirandom;
      double temp5= temp4 + temp2;
      double temp6 = (double) 1/(exponent+1);
      samplePowerLaw = maxTimeUnit - Math.pow(temp5, temp6);
      
      return (int) samplePowerLaw;
      
      
     }
     
     
    @Override
     public boolean execute() { 
        
        
         int random_vmtype = 0;
         int numberOfUserRequestRecieved = 0;
         int exponent = 2;
         int minTimeUnit = (CDState.getCycle() * 300) + 300;
         //int maxTimeUnit = (int) CDState.getEndTime() *300;
         double endTime =  CDState.getEndTime();
         double rightNow = CDState.getCycle();
         int maxTimeUnit = (int) (endTime + rightNow) * 300 * 2;//300*1.5
         int i1 =0;
         int i2 = 0;
      
         int k = 0;
         
         
         boolean createVm = true;
         Vm vm = null;
         
                
	 numberOfUserRequestRecieved = userRequestRandomGenerator();	 
                 
                 
          for (k = 0; k<numberOfUserRequestRecieved;k++ )
          {
              
            while (i1 < Network.size()) {
           
                
                Host host = (Host) Network.get(i1).getProtocol(protocolID);

                if(createVm == true)
                {
                    random_vmtype = random.nextInt(4) + 1;

                    switch (random_vmtype){
                        //High-Cpu Medium Instance
                        case 1:
                        vm = new Vm(vmId,2500,0.85,8,100);
                        break;
                        //Extera-Large Instance    
                        case 2:
                        vm = new Vm(vmId,2000,3.75,20,100);
                        break;
                        //Small Instance    
                        case 3:
                        vm = new Vm(vmId,1000,1.7,20,100);
                        break; 
                        //Mini Instance                    
                        case 4:
                        vm = new Vm(vmId,500,0.6,20,100);
                        break;

                        }

                    vmId++;
                }
                createVm = false;
                // if the host is wakeup  and if it has CPU MIPS to allocate and if it has memmory to allocate 
                    if(host.getState() && vm.getAllocatedVmMips() <= host.getTotalHostCpuMips() - host.getTotalHostCpuMipsAllocated() && vm.getAllocatedMemory() <= host.getTotalHostMemory() - host.getTotalHostMemoryAllocated() )

                    {
                        vm.TTL = samplePowerLawTTLGentarator(exponent,minTimeUnit,maxTimeUnit);
                        host.vmList.add(vm);
                        i1 = 0;
                        createVm = true;
                        break;

                    }
                    else 
                        i1++;

            }
            
            //if all the host dont have enough place to allocate VM , so wake the first host up
            while (i2 < Network.size()) {
                    Host host = (Host) Network.get(i2).getProtocol(protocolID);
                    
                    if (!host.getState())
                    {
                    vm.TTL = samplePowerLawTTLGentarator(exponent,minTimeUnit,maxTimeUnit);
                        host.vmList.add(vm);
                        host.setState(true);
                        host.CurrentWakeup = 1;
                        break;
                    }
                    
                    i2++;
            }
          }
          
    
      return false;
    }
    
    
 
    

}
