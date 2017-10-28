/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vmconsolidation;

/**
 *
 * @author Omid
 */
public class Vm implements Comparable<Vm> {
    
    //protected long destinationHostId;
    //protected long sourceHostId;
    protected int vmId;
    protected int TTL;
    protected int allocatedVmMips;
    protected double allocatedVmMemory;
    protected double allocatedVmNetworkBandwidth;
    protected double allocatedVmStorage;
    protected int requestedVmMips;
    protected double requestedVmMemory;
    protected double requestedVmNetworkBandwidth;
    protected double requestedVmStorage;
    
// constructor
  public Vm(int vmId, int allocatedVmMips, double allocatedVmMemory, double allocatedVmNetworkBandwidth, double allocatedVmStorage) {
      //this.destinationHostId = destinationHostId;
      //this.sourceHostId = sourceHostId;
      this.vmId = vmId;
      this.allocatedVmMips = allocatedVmMips;
      this.allocatedVmMemory = allocatedVmMemory;
      this.allocatedVmNetworkBandwidth = allocatedVmNetworkBandwidth;
      this.allocatedVmStorage = allocatedVmStorage;
     
      
   }
  
  /*public long getDestinationHostId() {
         return destinationHostId;
    }
    public void setDestinationHostId(long destinationHostId) {
	this.destinationHostId = destinationHostId;
    }
    public long getSourceHostId() {
	return sourceHostId;
    }
    public void setSourceHostId(long sourceHostId) {
	this.sourceHostId = sourceHostId;
    }
    public int getVmId() {
	return vmId;
    }*/
    public void setVmId(int vmId) {
 	this.vmId = vmId;
    }
    
    public void setTTL(int TTL){
        this.TTL = TTL;
    }
    
    public int getRequestedVmMips() {
	return requestedVmMips;
    }
    public void setRequestedVmMips(int requestedVmMips) {
 	this.requestedVmMips = requestedVmMips;
    }	

    public double getRequestedMemory() {
	return requestedVmMemory;
    }
    public void setRequestedVmMemory(double requestedVmMemory) {
 	this.requestedVmMemory = requestedVmMemory;
    }
    
      public double getRequestedVmNetworkBandwidth() {
	return requestedVmNetworkBandwidth;
    }
    public void setRequestedVmNetworkBandwidth(double requestedVmNetworkBandwidth) {
 	this.requestedVmNetworkBandwidth = requestedVmNetworkBandwidth;
    }
      public double getRequestedVmStorage() {
	return requestedVmStorage;
    }
    public void setRequestedVmStorage(double requestedVmStorage) {
 	this.requestedVmStorage = requestedVmStorage;
    }	
   //----------------------------------------------------------------
  	
    
    public int getAllocatedVmMips() {
	return allocatedVmMips;
    }
    
    public int getTTL(){
        return TTL;
    }
    public void setAllocatedVmMips(int allocatedVmMips) {
 	this.allocatedVmMips = allocatedVmMips;
    }	

    public double getAllocatedMemory() {
	return allocatedVmMemory;
    }
    public void setAllocatedVmMemory(double allocatedVmMemory) {
 	this.allocatedVmMemory = allocatedVmMemory;
    }
    
      public double getAllocatedVmNetworkBandwidth() {
	return allocatedVmNetworkBandwidth;
    }
    public void setAllocatedVmNetworkBandwidth(double allocatedVmNetworkBandwidth) {
 	this.allocatedVmNetworkBandwidth = allocatedVmNetworkBandwidth;
    }
      public double getAllocatedVmStorage() {
	return allocatedVmStorage;
    }
    public void setAllocatedVmStorage(double allocatedVmStorage) {
 	this.allocatedVmStorage = allocatedVmStorage;
    }	
    
    @Override
    public int compareTo(Vm compareVm){
          
        int compareage=((Vm)compareVm).getAllocatedVmMips();
        return compareage-this.allocatedVmMips;
      }


  
   

     
      
     
}
