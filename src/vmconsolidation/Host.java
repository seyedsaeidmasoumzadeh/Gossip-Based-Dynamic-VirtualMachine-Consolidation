/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vmconsolidation;

import java.util.List;
import peersim.core.*;

/**
 *
 * @author Omid
 */
public class Host implements Protocol
{
    

  
  
  protected List<Vm> vmList;
  protected List<Vm> vmSelectedList;
  protected List<Vm> vmPlacementList;
  protected List<Double> compareUtil;
  protected List<Double> hostUtilizationHistory;
  protected double overloadingTime;
  protected double CurrentPDO;
  protected double CurrentPDM;
  protected double CurrentNumMigVM;
  protected double CurrentEC;
  protected int    CurrentShutdown;
  protected int    CurrentWakeup;
  
  //protected List<Integer> ShutDown;
  

  
  protected long hostId;
  protected int hosttype;
  protected int hostMipsPerCore;
  protected int hostNumCores;
  protected double hostMemory;
  protected double hostStorage;
  protected double hostNetworkBandwidth;
  //protected int hostVmMips;
  //protected int hostNumVm;
  protected double hostUtilization;
  protected double avgUtilization;
  protected double upperThr;
  protected double lowerThr;
  protected boolean powerState = true; //true = wakeup , false = sleep 
  
  
  
  
  
 
  public Host(String prefix)
{
 
}
  
    @Override
  public Object clone()
{
	Host svh=null;
	try { svh=(Host)super.clone(); }
	catch( CloneNotSupportedException e ) {} // never happens
	return svh;
}
    
   
   //------------------------------------------- 
    
   public double getCpuUtilization()
    {
      
         
         double totalRequestedVmMips = 0;
       for (Vm vm : vmList)
       {
           totalRequestedVmMips = totalRequestedVmMips + vm.getRequestedVmMips();
           
       }
        
        
        hostUtilization = totalRequestedVmMips / getTotalHostCpuMips(); 
      
        return hostUtilization;
    }
     
  //------------------------------------------- 
    public int getTotalHostCpuMips()
    {
        return hostMipsPerCore * hostNumCores;
    }
    
   //-------------------------------------------
    public int getTotalHostCpuMipsAllocated()
    
    {
        int totalallocated = 0;
        for (Vm vm : vmList)
       {
           totalallocated = totalallocated + vm.getAllocatedVmMips();
           
       }
        
        return totalallocated;
    }        
   //-------------------------------------------
    public double getTotalHostMemory()
    {
      return hostMemory;     
    }
   //-------------------------------------------
    public double getTotalHostMemoryAllocated()
    {
     double totalallocated = 0;
        for (Vm vm : vmList)
       {
           totalallocated = totalallocated + vm.getAllocatedMemory();
           
       }
        
        return totalallocated;   
    }        
   //-------------------------------------------
    public double getUpThrValue()
    {
     return upperThr;
    }
    
     public double getLowThrValue()
    {
     return lowerThr;
    }
    
   //-------------------------------------------
    public boolean getState()
    {
     return powerState;   
    }
    
   //-------------------------------------------
 
    public void setLowThrValue(double lowthr)
    {
        this.lowerThr= lowthr;
    }
    
    public void setUpThrValue(double upthr)
    {
        this.upperThr= upthr;
    }
    
   //-------------------------------------------
    public void setState(boolean mode)
    {
        this.powerState= mode;
    }
    
   //-------------------------------------------

  

    
   //------------------------------------------
     
    
  
    
    
}
