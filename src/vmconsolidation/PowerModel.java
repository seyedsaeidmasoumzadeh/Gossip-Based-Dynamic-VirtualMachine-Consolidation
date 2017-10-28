/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vmconsolidation;

/**
 *
 * @author Omid
 */
public class PowerModel {
    
    
    static public double getpower(int serverType , double utilization)
    {
       
       double utilization1 =0;
       double utilization2 = 0;
       double power1 =0;
       double power2 =0;
       
       double EnergyPerCycle = 0;
       double EnergyPerHour = 0; 
        
        
        
        
          utilization1 =  (int) Math.floor(utilization *10 );
          utilization2 =   (int) Math.ceil(utilization *10 );
          power1 = GetDiscretePower(serverType, utilization1);
          power2 = GetDiscretePower(serverType, utilization2);
          
          double delta = (power2 - power1) / 10;
          double power = power1 + delta * (utilization - (double) utilization1 / 10) * 100;
          
          // each cycle = 300 seconds
          EnergyPerCycle = power * 300;
          EnergyPerHour = EnergyPerCycle /3600;
          //wattPerHour = ((0.7 * 250) + ((0.3 * 250)* avgCpuUtil))/(3600/300);
          
          return EnergyPerHour/1000;  
    }
    
    
    static protected double GetDiscretePower(int serverType, double utilizationIndex)
            
    {
        double power = 0; 
        if (serverType == 1)
        {
            if (utilizationIndex == 0.0 )
               power =  86;
            else if (utilizationIndex == 1)
               power = 89.4;
            else if (utilizationIndex == 2)
               power = 92.6;
            else if (utilizationIndex == 3)
               power = 96;
            else if (utilizationIndex == 4)
               power = 99.5;
            else if (utilizationIndex == 5)
               power = 102;
            else if (utilizationIndex == 6)
               power = 106;
            else if (utilizationIndex == 7)
               power = 108;
            else if (utilizationIndex == 8)
               power = 112;
            else if (utilizationIndex == 9)
               power = 114;
            else 
               power = 117;
        }
        
        if (serverType == 2)
        {
            if (utilizationIndex == 0.0 )
               power =  93.7;
            else if (utilizationIndex == 1)
               power = 97;
            else if (utilizationIndex == 2)
               power = 101;
            else if (utilizationIndex == 3)
               power = 105;
            else if (utilizationIndex == 4)
               power = 110;
            else if (utilizationIndex == 5)
               power = 116;
            else if (utilizationIndex == 6)
               power = 121;
            else if (utilizationIndex == 7)
               power = 125;
            else if (utilizationIndex == 8)
               power = 129;
            else if (utilizationIndex == 9)
               power = 133;
            else 
               power = 135;
        }
        
        return power;
    }
    
}
