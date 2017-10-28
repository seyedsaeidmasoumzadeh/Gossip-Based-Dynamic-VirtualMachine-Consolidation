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

/**
 *
 * @author Omid
 */
public class Debug {
    
    public static List<Integer> log1 = new ArrayList<>();
    public static List<Integer> log2 = new ArrayList<>();
    
    
    

    public static void loger1()
    {
        StringBuilder debug = new StringBuilder();
    
            for (int i = 0; i<log1.size(); i++)
            {
            debug.append(String.format("%d", log1.get(i)));
            debug.append("\n");
            writeDataRow(debug.toString(), "resultpaper/"+ "debug1.csv");
            
     
            }
    }
    
     public static void loger2()
    {
        StringBuilder debug = new StringBuilder();
    
            for (int i = 0; i<log2.size(); i++)
            {
            debug.append(String.format("%d", log2.get(i)));
            debug.append("\n");
            writeDataRow(debug.toString(), "resultpaper/"+ "debug2.csv");
            
     
            }
    }
    
    //-----------------------
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
    
}
