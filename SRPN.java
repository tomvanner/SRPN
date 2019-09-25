/**
 * The main method for SRPN. A Reverse Polish Notation calculator with the added features of:
 * input saturation, i.e. user input and calculation cannot exceed the Integer's maximum or minimum value 
 * an octal to decimal calculator, in which if the user input's first number is a 0, then any succeeding numbers are treated as an octal number and converted to decimal
 * ability to handle comments, i.e. it will ignore any input after a '#' is entered
 * 
 * @author Thomas Vanner
 * @version 1.0
 * @release 25/11/2016
 * @See SrpnProcessInput.java
 */

import java.io.*;

public class SRPN {
	
	/**
	 * Main method which gets user input, and passes it to 'SrpnProcessInput' class.
	 */
	public static void main(String[] args) {
		// Create SrpnProcessInput object
		SrpnProcessInput srpn = new SrpnProcessInput();
	
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			//Keep on accepting input from the command-line
			while(true) {
				// Stores user input in 'command' variable
				String command = reader.readLine();
				
				//Close on an End-of-file (EOF) (Ctrl-D on the terminal)
				if(command == null)
				{
					//Exit code 0 for a graceful exit
					System.exit(0);
				}

				//Otherwise, (attempt to) process the character
				srpn.processCommand(command);          
			}
		} catch(IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}
