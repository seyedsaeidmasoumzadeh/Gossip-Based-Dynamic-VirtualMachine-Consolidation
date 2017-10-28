/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vmconsolidation;
import peersim.config.FastConfig;
import peersim.core.*;
import peersim.cdsim.CDProtocol;
import java.util.*;

/**
 *
 * @author Omid
 */
public class VmPlacement extends Host implements CDProtocol{
    
    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * Initial Threshold. Defaults to 0.8.
     * 
     * @config
     */
 
    //protected static final String PAR_PROT = "protocol";
 

    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** config value. Obtained from config property {@link #PAR_}. */
  
    //private final int protocolID;
    
    
    


    

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
    public VmPlacement(String prefix) {
        super(prefix);
       
       
        //protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
     
         
        }
        
       
   
   protected void MetricsCalculator()
   {
     //Capturing EC
            calculateEC();
     //Capturing PDO
            calculatePDO();
     //Capturing NumVmMigrated
            calculateNumMigVM();
            
   }
   
   /*protected void Overloadingmanagement(){
    double overloadedTime = 0;
    
   
            if(getState()){    
                while (isHostOverloadedTB())
                    {
                        // how long the host experinced the CPU utilization of 100%
                        if(getCpuUtilization() > 1.0)
                            overloadedTime = overloadedTime + 1;  
                        //Select VM
                        //vmMinimumSelection();
                        //vmMaximumSelection();
                        vmRandomSelection();
                            

                    }

            //Capturing PDO
            if(overloadedTime > 0)    
            calculatePDO(overloadedTime);
            
            //Capturing Number of migrated VM
            //if(vmSelectedList.isEmpty())
            //calculateNumMigVM();

            //Capturing PDM
            //calculatePDM();
            
          

            //Capturing EC
            calculateEC();


          }
   } 
   protected void Underloadingmanagement(){
  
   
        if(getState()){  
                if (isHostUnderloadedTB())
                    {

                        for (int i =0; i<vmList.size(); i++)
                            this.vmSelectedList.add(vmList.get(i));

                        this.vmList.clear();

                        this.setState(false);

                    }



        
        //Capturing Number of migrated VM
           if(!vmSelectedList.isEmpty())
            calculateNumMigVM();
       
        
       
           
        }
   }*/
    
    
    // The clone() method is inherited.

    // ------------------------------------------------------------------------
    // Methods
    // ------------------------------------------------------------------------

  
    
    @Override
    public void nextCycle(Node node, int protocolID) {
        
       
            
            double overloadedTime = 0;
            int linkableID = FastConfig.getLinkable(protocolID);
            Linkable linkable = (Linkable) node.getProtocol(linkableID); 
            
      //-----------------------------------Overloading Management 
     
        if(getState()){    
                while (isHostOverloadedTB())
                    {
                        // how long the host experinced the CPU utilization of 100%
                        if(getCpuUtilization() > 1.0)
                            overloadedTime = overloadedTime + 1;  
                        //Select VM
                        //vmMinimumSelection();
                        //vmMaximumSelection();
                        vmRandomSelection();
                            

                    }
         this.overloadingTime = overloadedTime;    
                  
        }    
    //-----------------------------------Underloading Management (Average Utilization)             
        if(getState()){
                if(isHostUnderloaded(linkable, protocolID))
                {
                    for (int i =0; i<vmList.size(); i++)
                            this.vmSelectedList.add(vmList.get(i));

                        this.vmList.clear();

                        this.setState(false);
                        
                        this.CurrentShutdown = 1;
                }
              } 
     //----------------------------------Underloading Management (Threshold-Based)  
     /*if(getState()){  
                if (isHostUnderloadedTB())
                    {

                        for (int i =0; i<vmList.size(); i++)
                            this.vmSelectedList.add(vmList.get(i));

                        this.vmList.clear();

                        this.setState(false);

                    }


           
        }*/           
     //----------------------------------VM Placement   
               if(!vmSelectedList.isEmpty())
                {
                   
                makeVmPlacementList();
                //FFDProcess(linkable, protocolID);
                PABFDProcess(linkable, protocolID);
               
                }
                

            
                
                
           
         }
       
    
  
    
    
    
    
    
    
    
    
    
    
  //----------------------------------------------  
    
    //Check Whether host is overloaded or not!
    protected boolean isHostOverloadedTB(){
        boolean overloaded = false;
        
        if(getCpuUtilization() > upperThr)
            overloaded = true;
        
        //Keep CPU utilization history to caculate Energy Consumption
        this.hostUtilizationHistory.add(getCpuUtilization());
        
        return overloaded;
    }
    
  //------------------------------------------
    protected boolean isHostUnderloadedTB(){
        boolean underloaded = false;
        
        if (getCpuUtilization() < lowerThr)
            underloaded = true;
        
        return underloaded;
        
    }
  //-----------------------------------------------------------  
  protected boolean isHostUnderloaded(Linkable linkable, int protocolID){
        boolean underloaded = false;
        
        
    
        
        
       for (int i = 0; i < linkable.degree(); ++i) {
                            double sumUtilization = 0;
                            Node peer = linkable.getNeighbor(i);
                            VmPlacement n = (VmPlacement) peer.getProtocol(protocolID);
                          
                            sumUtilization = n.avgUtilization + this.avgUtilization;
                           
                            
                            this.avgUtilization = sumUtilization / 2;
                            n.avgUtilization = sumUtilization / 2;
                                
       }
         
         if(hostUtilization < avgUtilization)
         underloaded = true;   
        
        return underloaded;
        
    }
  //------------------------------------------
    //Select a VM to migrate based on a selection strategy (here, random)
    protected void vmRandomSelection (){
        
        
        Random rn = new Random ();
        int random = rn.nextInt(vmList.size());
        Vm vm = vmList.get(random);
        this.vmSelectedList.add(vm);
        vmList.remove(random);
        
    }
    //------------------------------------------
    //Select a VM to migrate based on a selection strategy (here, MinU)
    protected void vmMinimumSelection (){
        
        
       double min = Double.MAX_VALUE;
       int index = 0;
       for ( int i = 0; i<vmList.size(); i++)
       {
           if (vmList.get(i).allocatedVmMips < min){
                
                min = vmList.get(i).allocatedVmMips;
                index = i;
                
           }
              
       }
       
                Vm vm = vmList.get(index);
                this.vmSelectedList.add(vm);
                vmList.remove(index);
       
        
    }
   //---------------------------------------------
        //------------------------------------------
    //Select a VM to migrate based on a selection strategy (here, MaxU)
    protected void vmMaximumSelection (){
        
        
       double max = 0;
       int index = 0;
       for ( int i = 0; i<vmList.size(); i++)
       {
           if (vmList.get(i).allocatedVmMips > max){
                
                max = vmList.get(i).allocatedVmMips;
                index = i;
                
           }
              
       }
       
                Vm vm = vmList.get(index);
                this.vmSelectedList.add(vm);
                vmList.remove(index);
       
       
        
    }
   //---------------------------------------------
    protected void makeVmPlacementList(){
        //Making vmPlacementList (including itself)
                    for (Vm vm : this.vmSelectedList)
                        this.vmPlacementList.add(vm);
                    
                    

                    
    }
   //---------------------------------------------
    protected void PABFDProcess(Linkable linkable, int protocolID){
    double vmUtil;
        double currentNeighborUtil;
        Collections.sort(vmPlacementList);// sort VMs (must be migrated) based on decreasing order
      
            int j = 0;
            boolean vmRemoved;
            double minPower;
            
            while (j < vmPlacementList.size()){
                
            vmRemoved = false;
            minPower = Double.MAX_VALUE;
            int index = -1;
            
                    for (int i = 0; i < linkable.degree(); ++i) {
                            Node peer = linkable.getNeighbor(i);
                            VmPlacement n = (VmPlacement) peer.getProtocol(protocolID);
                            
                            //check the neighbor is wakeup and doesn't want to initiate placement procideure itself
                            if(n.getState() && n.vmSelectedList.isEmpty()){
                                
                                currentNeighborUtil = n.getCpuUtilization();
                                vmUtil = (double) vmPlacementList.get(j).getRequestedVmMips()/n.getTotalHostCpuMips();
                                        //check the VM can be fitted inside the neighbor
                                        if(vmUtil + currentNeighborUtil <= n.upperThr && vmPlacementList.get(j).getRequestedMemory() < (n.getTotalHostMemory()-n.getTotalHostMemoryAllocated())  )
                                        {
                                            double power =  PowerModel.getpower(n.hosttype, vmUtil + currentNeighborUtil);
                                            //if so, if teh neighbor has minimum increasing in power consumption amongst other neighbor until now
                                            if(power < minPower){
                                                minPower = power;
                                                index = i; // if so, keep the index of the neighbor 
                                            }
                                        }

                               }
                            
                             }
                    // if the algorithm find a neighbor with the aformentiond conditions, do placement
                    if (index != -1){
                        Node peer = linkable.getNeighbor(index);
                        VmPlacement n = (VmPlacement) peer.getProtocol(protocolID);
                        n.vmList.add(vmPlacementList.get(j));
                                    vmPlacementList.remove(j);
                                    vmRemoved = true;
                    }
                        
                      
                                    
                    // if th the placement of the VM has been failed , go for another VM and dont worry !!!               
                    if (vmRemoved==false)
                    j++;
                        
              }
             //--------------------------------------------------  
                // we do this procidure if there is at least one faild VM, so the slept host must be waked up
                if (!vmPlacementList.isEmpty()){
                    for (int i = 0; i < linkable.degree(); ++i) {
                            Node peer = linkable.getNeighbor(i);
                            VmPlacement n = (VmPlacement) peer.getProtocol(protocolID);
                            // if there is a slept neigbor, harry up , wake it up!!
                            if (!n.getState())
                            {
                                n.setState(true);
                                n.CurrentWakeup = 1;
                                for (j = 0; j<vmPlacementList.size();j++)
                                    n.vmList.add(vmPlacementList.get(j));
                                    vmPlacementList.clear();
                                    break;

                            }
                    }
                }
                
           //--------------------------------------------------------     
               // oh my GOD, it means that you couldn't find any place for these poor VMs, you have to keep it
               if (!vmPlacementList.isEmpty()){
                   for (j = 0; j<vmPlacementList.size();j++)
                   this.vmList.add(vmPlacementList.get(j));
                   
                   vmPlacementList.clear();
                   if(!getState())
                   {
                       setState(true);
                       this.CurrentWakeup = 0;
                   }
               }
        
            
    } 
   //---------------------------------------------
    protected void FFDProcess(Linkable linkable, int protocolID){
        
        double vmUtil;
        double currentNeighborUtil;
        Collections.sort(vmPlacementList);
      
            int j = 0;
            boolean vmRemoved;
            
            while (j < vmPlacementList.size()){
            vmRemoved = false;
            
                    for (int i = 0; i < linkable.degree(); ++i) {
                            Node peer = linkable.getNeighbor(i);
                            VmPlacement n = (VmPlacement) peer.getProtocol(protocolID);
                            if(n.getState() && n.vmSelectedList.isEmpty()){
                            currentNeighborUtil = n.getCpuUtilization();
                            vmUtil = (double) vmPlacementList.get(j).getRequestedVmMips()/n.getTotalHostCpuMips();
                            
                                if(vmUtil + currentNeighborUtil <= n.upperThr)
                                {
                                   
                                     
                                    n.vmList.add(vmPlacementList.get(j));
                                    vmPlacementList.remove(j);
                                    vmRemoved = true;
                                    
                                    break;
                                    
                                }

                               }
                             }
               if (vmRemoved==false)
                   j++;
                        
                    }
            
            
            
            
            
          //--------------------------------------------------  
          //(4)
                if (!vmPlacementList.isEmpty()){
                    for (int i = 0; i < linkable.degree(); ++i) {
                            Node peer = linkable.getNeighbor(i);
                            VmPlacement n = (VmPlacement) peer.getProtocol(protocolID);
                            if (!n.getState())
                            {
                                n.setState(true);
                                n.CurrentWakeup =1;
                                for (j = 0; j<vmPlacementList.size();j++)
                                    n.vmList.add(vmPlacementList.get(j));
                                    vmPlacementList.clear();
                                    break;

                            }
                    }
                }
                
           //--------------------------------------------------------     
           //(5)
               if (!vmPlacementList.isEmpty()){
                   for (j = 0; j<vmPlacementList.size();j++)
                   vmList.add(vmPlacementList.get(j));
                   vmPlacementList.clear();
                   if(!getState())
                       setState(true);
                   this.CurrentWakeup =1;
               }
        
    }
   //---------------------------------------------
   protected void calculatePDO(){
       
   
         
       this.CurrentPDO = (overloadingTime/300);
       overloadingTime = 0;
   
      
   }
   
   /*protected void calculatePDM(){
    double sumAllocatedMips = 0;
    double sumVmUtilization =0;
    double numMigratedVm = 0;
    
    
    if(vmSelectedList.isEmpty())
           this.PDM.add(0.0);
       else{
       for (int i = 0; i<vmSelectedList.size(); i++){
           sumAllocatedMips = vmSelectedList.get(i).allocatedVmMips;
           numMigratedVm = i+1;
       }   
      sumVmUtilization =sumAllocatedMips/(hostVmMips*numMigratedVm);
     
      
      this.PDM.add((0.1 * sumVmUtilization));
      this.totalPDM.add((0.1 * sumVmUtilization));
       }
    
    
    
   }*/
   //--------------------------------
   
   
   protected void calculateNumMigVM(){
     

      this.CurrentNumMigVM = vmSelectedList.size();
      this.vmSelectedList.clear();
      
       
   } 
   //--------------------------------
   
   protected void calculateEC(){
       double sumUtil = 0;
       double number = 0;
       double avgCpuUtil = 0;
       double kWh = 0;
       
      for (int i =0; i< hostUtilizationHistory.size(); i++){
         sumUtil = sumUtil + hostUtilizationHistory.get(i); 
         number ++;
      }
      
      if (number == 0)
          this.CurrentEC = 0.0;
      
      else{
          
          avgCpuUtil = sumUtil/number;
          
          if (avgCpuUtil > 1)
              avgCpuUtil = 1;
          
          
          
          this.CurrentEC = PowerModel.getpower(hosttype, avgCpuUtil);
          
          hostUtilizationHistory.clear();
      }
         
   }
   
}
