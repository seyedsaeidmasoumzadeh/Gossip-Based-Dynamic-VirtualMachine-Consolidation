/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vmconsolidation;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 *
 * @author Omid
 */
public class HostsInitialization implements Control {
    // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------

    /**
     * The protocol to operate on.
     * 
     * @config
     */
    private static final String PAR_PROT = "protocol";//protocolID
    protected static final String PAR_UPTHR = "UPTHR";// Upper Threshold
    //protected static final String PAR_LOWTHR = "LOWTHR";// Lower Threshold

    
    
 


    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------

    /** Value obtained from config property {@link #PAR_PROT}. */
    private final double upperThreshold;
    //private final double lowerThreshold;
    //private final int initNumVm;
    //private final int vmMips;
  
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

     public HostsInitialization(String prefix)
    {
        protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
        upperThreshold = (Configuration.getDouble(prefix + "." + PAR_UPTHR));
        //lowerThreshold = (Configuration.getDouble(prefix + "." + PAR_LOWTHR));
        
        
      
        
        
    }
     
      @Override
    public boolean execute() {
          
      int totalNumberOfVm = 0;
      int totalNumberofHost = 0;
       
        for (int i = 0; i < Network.size(); ++i) {
            Host host = (Host) Network.get(i).getProtocol(protocolID);
            
            host.vmList = new ArrayList<Vm>();
            host.vmSelectedList = new ArrayList<Vm>();
            host.vmPlacementList = new ArrayList<Vm>();
            host.compareUtil = new ArrayList<Double>();
            host.hostUtilizationHistory = new ArrayList<Double>();
            
           
            
            
            host.upperThr = upperThreshold;
            //host.lowerThr = lowerThreshold;
            host.hostId = Network.get(i).getID();
            host.hostUtilization = 0;
            host.CurrentEC = 0;
            host.CurrentPDM = 0;
            host.CurrentPDO= 0;
            host.CurrentNumMigVM = 0;
            host.CurrentShutdown = 0;
            host.CurrentWakeup = 0;
            host.overloadingTime = 0;
            
            if ( i % 2 ==0)
                host.hosttype = 2; // HP Proliant G4
            else 
                host.hosttype = 1; // HP Proliant G5 
            
            } 
        
        
        
        initialServerConfiguration();
        //initialFFDDynamicAdmisionControl();
        initialFFDStaticAdmisionControl();
        initialRandomWorkload();
        initialFixedTTL();
        //initialRandomTTL();
        
        
        totalNumberofHost = Network.size();
        
        for (int i = 0; i < Network.size(); ++i) {
            Host host = (Host) Network.get(i).getProtocol(protocolID);
            
           totalNumberOfVm = totalNumberOfVm + host.vmList.size();
                
        }
        
        System.out.print("Initialization Done....");
        System.out.print("Data Center Comprises: \n");
        System.out.print(totalNumberofHost + " Physical Host Nodes \n");
        System.out.print(totalNumberOfVm + " Virtual Machines  \n");
        
        
        
        
            
        
       
      
        return false;
    }
            
      
  protected int samplePowerLawTTLGentarator (int exponent, int minTimeUnit, int maxTimeUnit)
  {
   
      double random = 0;
      random = Math.random();
      double samplePowerLaw = 0;
      
      
      //samplePowerLaw = minTimeUnit + (maxTimeUnit - minTimeUnit) * Math.pow(random, (1/(-exponent +1)));
      double temp1= Math.pow(maxTimeUnit, (exponent+1));
      double temp2= Math.pow(minTimeUnit, (exponent+1));
      double temp3= temp1 - temp2;
      double temp4= temp3 * random;
      double temp5= temp4 + temp2;
      double temp6 = (double) 1/(exponent+1);
      samplePowerLaw = maxTimeUnit - Math.pow(temp5, temp6);
      
      return (int) samplePowerLaw;
      
      
  }
    
  protected void initialFFDDynamicAdmisionControl()
    {
         int random = 0;
         int i = 0;
         int j = 0;
         boolean createVm = true;
         Vm vm = null;
         
         
         
       
         Random rn1 = new Random ();// for selecting the type of VM
         
        
        
            while (i < Network.size()) {
                Host host = (Host) Network.get(i).getProtocol(protocolID);

            if(createVm == true)
            {
                random = rn1.nextInt(4) + 1;

                switch (random){
                    //High-Cpu Medium Instance
                    case 1:
                    vm = new Vm(j,2500,0.85,8,100);
                    break;
                    //Extera-Large Instance    
                    case 2:
                    vm = new Vm(j,2000,3.75,20,100);
                    break;
                    //Small Instance    
                    case 3:
                    vm = new Vm(j,1000,1.7,20,100);
                    break; 
                    //Mini Instance                    
                    case 4:
                    vm = new Vm(j,500,0.6,20,100);
                    break;

                    }
                
                j++;
            }
            createVm = false;
                if(vm.getAllocatedVmMips() <= host.getTotalHostCpuMips() - host.getTotalHostCpuMipsAllocated() && vm.getAllocatedMemory() <= host.getTotalHostMemory() - host.getTotalHostMemoryAllocated() )
                    
                {
                    host.vmList.add(vm);
                    i = 0;
                    createVm = true;
                    
                }
                else 
                    i++;

        }
    
    }
  
//-------------------------------------
  protected void initialFFDStaticAdmisionControl() 
    {
        //In static admission control the Admision is based on reading a file 

         int i = 0;
         int j = 0;
         boolean createVm = true;
         Vm vm = null;
         ArrayList<String> vmTypeList = new ArrayList<>();
        
         
         
         try {
            vmTypeList = readDataRow("RandomWorkload/RandomVmInit.csv");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HostsInitialization.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        
        
        
            while (i < Network.size()) {
                Host host = (Host) Network.get(i).getProtocol(protocolID);

            if(createVm == true)
            {
               

               int type = Integer.parseInt(vmTypeList.get(j));
              

                switch (type){
                    //High-Cpu Medium Instance
                    case 1:
                    vm = new Vm(j,2500,0.85,8,100);
                    break;
                    //Extera-Large Instance    
                    case 2:
                    vm = new Vm(j,2000,3.75,20,100);
                    break;
                    //Small Instance    
                    case 3:
                    vm = new Vm(j,1000,1.7,20,100);
                    break; 
                    //Mini Instance                    
                    case 4:
                    vm = new Vm(j,500,0.6,20,100);
                    break;

                    }
                
                j++;
            }
            createVm = false;
                if(vm.getAllocatedVmMips() <= host.getTotalHostCpuMips() - host.getTotalHostCpuMipsAllocated() && vm.getAllocatedMemory() <= host.getTotalHostMemory() - host.getTotalHostMemoryAllocated() )
                    
                {
                    host.vmList.add(vm);
                    i = 0;
                    createVm = true;
                    
                }
                else 
                    i++;

        }
    
    }
//-------------------------------------  
  protected void initialRandomTTL()
  {
    int minTimeUnit = 300;
    //int MaxTimeUnit = (int) CDState.getEndTime()*300;
    int MaxTimeUnit = (int) CDState.getEndTime()* 300 * 2; 
    int exponent =2;
    int TTL =0;
    
    
         for (int i = 0; i < Network.size(); ++i) {
            Host host = (Host) Network.get(i).getProtocol(protocolID);
            
            for (int j = 0; j<host.vmList.size(); j++)
            {
    
                //TTL
                TTL = samplePowerLawTTLGentarator(exponent, minTimeUnit, MaxTimeUnit);
                host.vmList.get(j).TTL = TTL;
                
                
                // Nothing!! just for for debuging
                //Debug.log1.add(TTL);
            }
         }
  }
  //-------------------------------------
  protected void initialFixedTTL()
  {
      for (int i = 0; i < Network.size(); ++i) {
            Host host = (Host) Network.get(i).getProtocol(protocolID);
            
            for (int j = 0; j<host.vmList.size(); j++)
            {
    
                //TTL
              
                host.vmList.get(j).TTL = (int) CDState.getEndTime()* 300;// Simulation Time
                
                
                // Nothing!! just for for debuging
                //Debug.log1.add(TTL);
            }
         } 
  } 
  //--------------------------------------
  protected void initialRandomWorkload()
  {
    Random rn1 = new Random ();
    
  
        
         for (int i = 0; i < Network.size(); ++i) {
            Host host = (Host) Network.get(i).getProtocol(protocolID);
            
            for (int j = 0; j<host.vmList.size(); j++)
            {
                //Workload
                int random1 = rn1.nextInt(host.vmList.get(j).getAllocatedVmMips());
                host.vmList.get(j).setRequestedVmMips(random1);
                
                
                
            }

          host.avgUtilization = host.getCpuUtilization();  
            
         }
      
  }
  
  protected void initialServerConfiguration()
  {
      for (int i = 0; i < Network.size(); ++i) {
            Host host = (Host) Network.get(i).getProtocol(protocolID);
            
            
            if (host.hosttype ==1)
            // HP Proliant G5 Configuration
            {
            host.hostMipsPerCore = 2660;
            host.hostNumCores = 4;
            host.hostMemory = 32;
            host.hostStorage = 1024;
            host.hostNetworkBandwidth = 1024;
            }
            
            else
            { 
            // HP Proliant G4 Configuration
            host.hostMipsPerCore = 1860;
            host.hostNumCores = 4;
            host.hostMemory = 32;
            host.hostStorage = 1024;
            host.hostNetworkBandwidth = 1024; 
            }
      }
  }
  //----------------------------------------------//-----------------------------------
  protected  static void writeDataRow(String data, String outputPath) {
		File file = new File(outputPath);
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
  
  //--------------------------------------------------------
  protected static ArrayList<String> readDataRow(String inputPath) throws FileNotFoundException
  {
      
      
    ArrayList<String> list = new ArrayList<>();
         
         Scanner s = new Scanner(new File("RandomWorkload/RandomVmInit.csv")); 
                  
                    while (s.hasNext()){
                        list.add(s.next());
                    }
                    
                    return list;
  }
            
            
          

}