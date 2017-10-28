/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vmconsolidation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import peersim.cdsim.CDState;
import peersim.config.Configuration;
import peersim.core.Control;
import peersim.core.Network;

/**
 *
 * @author Omid
 */
public class ESVObserver implements Control {
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

  
 
  private static StringBuilder data;
  private static int numberOfHost;
  private static List<Double> totalPDO;
  private static List<Double> totalNumMigVM;
  private static List<Double> totalEC;
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
     public ESVObserver(String prefix)
    {
     protocolID = Configuration.getPid(prefix + "." + PAR_PROT);
   
     
 
     File folder = new File("resultpaper");
			if (!folder.exists()) {
				folder.mkdir();
			}
     data = new StringBuilder();
     
     
            totalEC = new ArrayList<Double>();
            totalNumMigVM = new ArrayList<Double>();
            totalPDO = new ArrayList<Double>();
            numberOfHost = Network.size();
 
     
     
    }
     
    @Override
     public boolean execute() {
        
        double totalECPerCycle = 0;
        double totalPDOPerCycle = 0;
        //double totalPDMPerCycle = 0;
        double totalMigrationPerCycle = 0;
        int totalShutDownPerCycle =0;
        int totalWakeUpPerCycle =0;
        int totalVirtualMachineInDataCenter =0;
        double sumUtilizationPerCycle = 0;
        int totalNumShutDown = 0;
        //int numMigration = 0;
        for (int i = 0; i < Network.size(); ++i) {
            Host host = (Host) Network.get(i).getProtocol(protocolID);
      
           
                    
            
            if(!host.getState())
                totalNumShutDown++;
            else
                sumUtilizationPerCycle = sumUtilizationPerCycle + host.getCpuUtilization();

           totalECPerCycle = totalECPerCycle + host.CurrentEC;
           totalPDOPerCycle = totalPDOPerCycle + host.CurrentPDO;
           totalMigrationPerCycle = totalMigrationPerCycle +host.CurrentNumMigVM;
           totalShutDownPerCycle = totalShutDownPerCycle +host.CurrentShutdown;
           totalWakeUpPerCycle = totalWakeUpPerCycle +host.CurrentWakeup;
           totalVirtualMachineInDataCenter = totalVirtualMachineInDataCenter + host.vmList.size();
           
          
           host.CurrentEC = 0;
           host.CurrentPDO = 0; 
           host.CurrentNumMigVM = 0;
           host.CurrentShutdown = 0;
           host.CurrentWakeup = 0;
           
           
            
        }
     
                         
	   totalEC.add(totalECPerCycle);
           totalNumMigVM.add(totalMigrationPerCycle);
           totalPDO.add(totalPDOPerCycle); 
              		
             
         
             
      String delimeter = ",";
                        
      data.append(String.format("%.10f", totalECPerCycle) + delimeter);
      //data.append(String.format("%.10f", totalPDMPerCycle/(numOfHost*numOfVM)) + delimeter);
      data.append(String.format("%.10f", totalPDOPerCycle/(numberOfHost-totalNumShutDown)) + delimeter);
      data.append(String.format("%.10f", totalMigrationPerCycle/(totalVirtualMachineInDataCenter)) + delimeter);
      data.append(String.format("%d", totalShutDownPerCycle) + delimeter);
      data.append(String.format("%d", totalWakeUpPerCycle) + delimeter);
      data.append(String.format("%d", totalNumShutDown) + delimeter);
      data.append(String.format("%d", totalVirtualMachineInDataCenter) + delimeter);
      data.append(String.format("%.10f", sumUtilizationPerCycle/(numberOfHost-totalNumShutDown)) + delimeter);
      
      
      data.append("\n");
                        
      writeDataRow(data.toString(), "resultpaper/"+ "resultN1600.csv");
      System.out.println("total EC per cycle" + ": " + totalECPerCycle); 
      //System.out.println("total PDM per cycle" + ": " + totalPDMPerCycle/(numOfHost*numOfVM));
      System.out.println("total PDO per cycle" + ": " + totalPDOPerCycle/(numberOfHost-totalNumShutDown));
      System.out.println("Total PDM per cycle" + ": " + totalMigrationPerCycle/(totalVirtualMachineInDataCenter));
      System.out.println("Number of ShutDown per Cicle" + ": " + totalShutDownPerCycle);
      System.out.println("Number of Wakeup per Cicle" + ": " + totalWakeUpPerCycle);
      System.out.println("Total Number of Shutdowns" + ": " + totalNumShutDown);
      System.out.println("Total Number of VMs in DC" + ": " + totalVirtualMachineInDataCenter);
      System.out.println("Average Utilization in DC" + ": " + sumUtilizationPerCycle/(numberOfHost-totalNumShutDown));
      
 
      
      
      if (CDState.getCycle() == CDState.getEndTime()- 1.0 ){
          
            double sumEC = 0;
            double sumNumMigVM = 0;
            double sumPDO = 0;
            
            for ( int j =0; j< totalEC.size(); j++){
               sumEC = sumEC + totalEC.get(j); 
            }
            
            for ( int j =0; j<totalNumMigVM.size(); j++){
               sumNumMigVM = sumNumMigVM + totalNumMigVM.get(j); 
            }
            
            for ( int j =0; j<totalPDO.size(); j++){
               sumPDO = sumPDO + totalPDO.get(j); 
            }
        
      
      System.out.println("----------------------------");
      System.out.println("----------------------------");
      System.out.println("total EC " + ": " + sumEC + "(kWh)"); 
      System.out.println("total PDM" + ": " + (sumNumMigVM/(totalVirtualMachineInDataCenter * CDState.getEndTime())) * 100 + " %");
      System.out.println("total PDO" + ": " + (sumPDO/(numberOfHost * CDState.getEndTime())) * 100 +" %");
      //System.out.println("SLAV" + ": " + totalPDO/(numOfHost * 300 * CDState.getEndTime()) * totalPDM/(numOfHost * numOfVMPerHost * CDState.getEndTime()));
      //System.out.println("ESV" + ": " + totalPDO/(numOfHost * 300 * CDState.getEndTime()) * totalPDM/(numOfHost * numOfVMPerHost * CDState.getEndTime()) * totalEC);
      
      // debuging
      //Debug.loger1();
      //Debug.loger2();
      
      }
          
      
      
      //Gnuplot Output
            /*File dataFile = new File("resultfile/"+ "gnuresult.txt");
           
                FileDataSet p = null;
            try {
                p = new FileDataSet(dataFile);
            } catch (    IOException | NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                Logger.getLogger(ESVObserver.class.getName()).log(Level.SEVERE, null, ex);
            }
                JavaPlot Pl = new JavaPlot();
                Pl.addPlot("sin(x)");
                Pl.plot();*/
      
      
     
      
    return false;
    }
    
    
    
    
    
    public static void writeDataRow(String data, String outputPath) {
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
    
    
    
}
