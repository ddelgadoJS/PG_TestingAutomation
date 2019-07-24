package components;

import java.io.IOException;

public class PingID {
	
	public static void main(String[] args) 
    { 
        try
        { 
            // Command to create an external process 
            String command = "C:\\Program Files (x86)\\Ping Identity\\PingID\\PingID.exe"; 
  
            // Running the above command 
            Runtime run  = Runtime.getRuntime(); 
            Process proc = run.exec(command);
        } 
  
        catch (IOException e) 
        { 
            e.printStackTrace(); 
        } 
    } 

}
