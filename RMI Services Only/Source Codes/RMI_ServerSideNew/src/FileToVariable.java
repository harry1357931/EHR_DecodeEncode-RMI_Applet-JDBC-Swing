/* FileToVariable
 * Description:
 *   Read and Save any file to a String Variable
 * @param fileInOneLine Saves the whole file in this String variable 	 
 * @author GurpreetSingh
 */
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileToVariable {  
	static String fileInOneLine= "";
	
	// Constructor
	public FileToVariable(String fileName){        // Constructor....on Client Side
		TextFileInput tfi = new TextFileInput(fileName);
		String line = tfi.readLine(); 
		while(line!=null)
		{	fileInOneLine = fileInOneLine + line + "\n"; 
			line=tfi.readLine();
		}// while
	} 

} // Class FileToVariable ends here...
