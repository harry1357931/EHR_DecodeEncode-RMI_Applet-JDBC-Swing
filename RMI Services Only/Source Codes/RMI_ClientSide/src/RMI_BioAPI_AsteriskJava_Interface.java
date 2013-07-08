/* RMI_BioAPI_AsteriskJava_Interface
 * Interface for RMI_Server & Client Side
 * @author bon
 * @CoAuthor GurpreetSingh
 */
import java.rmi.RemoteException;

 public interface RMI_BioAPI_AsteriskJava_Interface extends java.rmi.Remote {

	  public String RPC_FileSendBackAsVariable(String fileToConvert, String Service_UID, String ConversionService) throws RemoteException;
		
	  public void RPC_ExtractAndSaveFileFromArgument(String fileToConvert, String fileInOneLine) throws RemoteException	;
	  public void RPC_DecodeCCDAorCCD_ToText(String fileToConvert, String ConversionService) throws RemoteException;
	  
 }