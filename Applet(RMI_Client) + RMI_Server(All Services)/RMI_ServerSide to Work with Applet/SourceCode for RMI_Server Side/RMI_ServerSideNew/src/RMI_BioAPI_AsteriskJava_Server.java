/*
 * RMI_BioAPI_Asterisk_Server - Main Class  -- do following things
 *   1) Start registry at port 1099
 *   2) Create instance of RMI_BioAPI_Asterisk_Server
 *   3) Bind this to localhost
 *   4) And then Server Starts
 * Services Provided on Server Side
 *   1) Convert CCDA to Text   -- Use Conversion Code: ConvertCCDA
 *   2) Convert CCD to Text    -- Use Conversion Code: ConvertCCD
 *   3) Convert CCDA to CCD    -- Use Conversion Code: CCDAtoCCD
 *   
 * RMI_BioAPI_AsteriskJava_Server
 * RMI: Remote Method Invocation
 * Read 'Read Me' file for instructions on setting up the server 
 * and client side 
 * Description:
 *   1) How it works ? Read Comments above main method.  
 *   2) This file goes in RMI_Server folder
 *   3) This class contains code for RMI_Server side.
 *      Also, this class contain methods which can be offered as a
 *      Service to Clients, Clients can call these methods from remote 
 *      locations. The remote locations can be on the same network or 
 *      it can be on the different network anywhere around the world.  
 *   4) Example. of Service Method: RPC_FileRead(...)...
 *      This method read specific file (Specified by Client) on Server Side
 *      and then send that file through a Socket connection with Client
 *   5) This class file is Eclipse friendly as well as Command Line Friendly.
 *      On Eclipse: 
 *         a) Just specify the default run time argument: 2099
 *            here 2099 is the port number, where RMI_Server listens
 *         b) By default, the registry is started at port 1099
 *         c) By default, The instance of RMI_Server is binded to localhost
 *            and listens at 2099, though you have to specify port number 2099   
 *        
 * @author GurpreetSingh
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.net.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.rmi.Naming;
import Services.CCDA_Decode.CCDADecode;
import Services.CCDA_Decode.TextFileInput;
import Services.CCDA_TO_CCD.ccdaConverter;
import Services.CCD_Decode.ccdDecode;

public class RMI_BioAPI_AsteriskJava_Server extends UnicastRemoteObject
implements RMI_BioAPI_AsteriskJava_Interface {
    // Constructor
    public RMI_BioAPI_AsteriskJava_Server(int port) throws RemoteException
    {
        super(port);
    } 
	 // Decodes the Argument and Save that Argument as a CCD or CCDA file
    @Override 
    public void RPC_ExtractAndSaveFileFromArgument(String fileName, String fileToConvert, String fileInOneLine) 
			throws RemoteException {
		// On Server Side decoding Variable back to XML file
			try { 
				  FileOutputStream f_out = new FileOutputStream(fileName);     //fileToConvert);   //"C:\\Users\\Harry\\Desktop\\"+fileName
			     
			       try {
					    f_out.write(fileInOneLine.getBytes());
					    f_out.close();
			       } catch (IOException e) {
						
						e.printStackTrace();
				    }
			     
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  
	 }
	 
	 // Decode CCDA/CCD to Text...
	 public void RPC_DecodeCCDAorCCD_ToText(String fileName, String fileToConvert, String ConversionService) throws RemoteException 
	 {
			try {
				if (ConversionService.equalsIgnoreCase("ConvertCCDA")){
				        // Create new Instance of CCDADecode with "fileName" as argument
					CCDADecode ccdadecInstance = new CCDADecode(fileName);      //fileToConvert);
				}else if (ConversionService.equalsIgnoreCase("ConvertCCD")) {
					   // Create new Instance of ccdDecode with "fileName" as argument
					     ccdDecode ccdDecInst = new ccdDecode(fileName);       //fileToConvert);
				}else if (ConversionService.equalsIgnoreCase("CCDAtoCCD")) {
					   // Create new Instance of ccdDecode with "fileName" as argument
					    ccdaConverter ccdaToccdDecInst = new ccdaConverter(fileName);       //fileToConvert);
				}
              
			} catch (Exception e) {      
				 e.printStackTrace();
			}
	  
	 }
	 
	/* (non-Javadoc)       
	 * @see RMI_BioAPI_AsteriskJava_Interface#RPC_FileRead(java.lang.String, java.lang.String)
	 */
	@Override
	public String RPC_FileSendBackAsVariable(String fileName, String fileToConvert, String Service_UID, String ConversionService) 
			throws RemoteException {
		
          try {
        	  System.out.println("Name of RemoteFileName: " + fileToConvert);      // Should print name of XML file
        	  String InputFile ;
        	  if(ConversionService.equalsIgnoreCase("CCDAtoCCD")) {
        		  InputFile = "CCDA_To_CCD_Converted_" + fileName;      //fileToConvert;
        	  }else {
        		  InputFile =   fileName.replace(".xml", ".text");        //fileToConvert.replace(".xml", ".text");
        	  }
     
   	          return new FileToVariable(InputFile).fileInOneLine;
   			   
           }catch (Exception e) 
           {
        	    throw new RuntimeException(e);
           }
	}  // method RPC_FileRead()

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
	    if (args.length != 2)
	    {
	            System.out.println
	                ("Syntax - java RMI_BioAPI_AsteriskJava_Server <IP to which to bind Server> <host_port>");
	            System.exit(1);
	    }
			
		try {
			LocateRegistry.createRegistry(1099);
			System.out.println("java RMI registry created. Listening on Port 1099");
		} catch (RemoteException e) {
			System.out.println("java RMI registry already exists.");
		}
		
        // Create an instance of our service server ...		
	    RMI_BioAPI_AsteriskJava_Server svr = new RMI_BioAPI_AsteriskJava_Server(Integer.parseInt(args[1]));
	    //
            System.out.println("RmiRegistry listens at port 1099 ");
            System.out.println("AsteriskJava BSP Server is ready to listen on " + args[0]);  
             System.out.println("Host Name: "+InetAddress.getLocalHost()); 			 
	         //Naming.bind("RMI_BioAPI_AsteriskJava", svr);          			 
            Naming.rebind("rmi://"+ args[0] +"/RMI_BioAPI_AsteriskJava", svr);                   // need to make some changes here....
			System.out.println("BioAPI AsteriskJava RMI server starts ... ");
	}

}
