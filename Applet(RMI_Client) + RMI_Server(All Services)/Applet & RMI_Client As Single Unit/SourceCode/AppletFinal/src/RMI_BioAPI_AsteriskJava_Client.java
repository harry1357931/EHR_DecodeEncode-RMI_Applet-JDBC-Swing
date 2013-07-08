/* RMI_BioAPI_AsteriskJava_Client
 * 1) This is the Client Side and it's constructor looks for name 'RMI_BioAPI_AsteriskJava'
 *    on local registry of 'host_ip' 
 * 2) Once, it finds the 'RMI_BioAPI_AsteriskJava', it then creates 'service' instance of
 *    'RMI_BioAPI_AsteriskJava_Interface', which then is used to call Server side
 *     method: RPC_FileRead.
 * 3) Also Check ReadMe file     
 * 
 * @author GurpreetSingh
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class RMI_BioAPI_AsteriskJava_Client {
	String ReceiveDecodedOrConvertedFile = "";
    
	// Constructor
    public RMI_BioAPI_AsteriskJava_Client(String fileName, String fileToConvert, String ConversionService, String host_ip, String Service_UID)
    {   
	    System.out.println("Host IP: "+host_ip);
    	try {
    		RMI_BioAPI_AsteriskJava_Interface service = (RMI_BioAPI_AsteriskJava_Interface)             
				Naming.lookup("rmi://" + host_ip + "/RMI_BioAPI_AsteriskJava");
    		
    		// Creates Copy of Uploaded file on RMI Server Side
    		service.RPC_ExtractAndSaveFileFromArgument(fileName, fileToConvert, new FileToVariable(fileToConvert).fileInOneLine);   // or use "fileName" temporarily
    		
    		// Actually Decode by Calling by creating decoding instances
    		service.RPC_DecodeCCDAorCCD_ToText(fileName, fileToConvert, ConversionService);
    		
    		// Reads Decoded file and sends that file back to RMI_Client
    		ReceiveDecodedOrConvertedFile = service.RPC_FileSendBackAsVariable(fileName, fileToConvert , Service_UID, ConversionService);                  
    	    
    		 try {
    			 String ReceivedFileName ;
	           	  if(ConversionService.equalsIgnoreCase("CCDAtoCCD")) {
	           		ReceivedFileName =    "CCDA_To_CCD_Converted_" + fileName;                  //"CCDA_To_CCD_Converted_" + fileToConvert;
	           	  }else {
	           		ReceivedFileName = fileName.replace(".xml", ".text");
	           	  }

            	 FileOutputStream out3 = new FileOutputStream(ReceivedFileName);      //f.getAbsolutePath()+"\\src\\OutgoingFiles\\"  + fName);
            	 out3.write(ReceiveDecodedOrConvertedFile.getBytes());
				 out3.close();
		     } catch (IOException e) {
					System.out.println(ReceiveDecodedOrConvertedFile+"asdfasfasf");
					e.printStackTrace();
			  }
    	
    	
    	} catch (Exception e) {
                System.out.println(e);
    		System.out.println("RMI_BioAPI_AsteriskJava Naming lookup fails!");
    	}
    }
    
	public static void main(String[] args) throws Exception {
	    
		// Check for hostname argument
		if (args.length != 5)
		{
			System.out.println
			("Syntax - java RMI_BioAPI_AsteriskJava_Client <fileName> <NameOfFileToConvert> <ConversionService> <Asterisk_Java_Server_host_IP> <Service_UID> ");
			System.exit(1);
		}
        
		// Currently NOT in use
	    String NameOfFileToConvert =  "example4.xml";      //"BlueButtonPlusSample.xml";  
		String ConversionServ = "CCDAtoCCD";
		String AsterieskJava_ServerIP = "localhost";    
		String Service_UID = "my_transact"; 

		// Instance of Client		
		//new RMI_BioAPI_AsteriskJava_Client(fileName, NameOfFileToConvert, ConversionServ, AsterieskJava_ServerIP, Service_UID);
		new RMI_BioAPI_AsteriskJava_Client(args[0], args[1], args[2], args[3], args[4]);		
	}

}
