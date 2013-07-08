package Services.CCDA_Decode;
/* Class CCDADecode
 * Performs decoding of CCDA file(XML file) into Text format
 * All CCDA files which are supposed to be decoded should be in src\IncomingFiles\ 
 * Save the Text file (or decoded file) into appropriate directory(src\Outgoing files\)
 *@author Gurpreet Singh 
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.Component3;
import org.openhealthtools.mdht.uml.cda.Guardian;
import org.openhealthtools.mdht.uml.cda.LanguageCommunication;
import org.openhealthtools.mdht.uml.cda.Organization;
import org.openhealthtools.mdht.uml.cda.Patient;
import org.openhealthtools.mdht.uml.cda.PatientRole;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.hl7.datatypes.AD;
import org.openhealthtools.mdht.uml.hl7.datatypes.ADXP;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.datatypes.ON;
import org.openhealthtools.mdht.uml.hl7.datatypes.TEL;
import org.openhealthtools.mdht.uml.hl7.vocab.PostalAddressUse;

public class CCDADecode {
	
	static String ToFile="";          
	/* Calls Constructor of Class CCDADecode
	 */
	public static void main(String[] args){
		  String fileName2 = "SampleBlueButtonPlusCCDA.xml";
		  CCDADecode ccda_decInstance = new CCDADecode(fileName2); 
		  /*
		  if (args.length != 1)
		    {
		            System.out.println("Syntax 1 - java CCDADecode <InputFilename>");
		            System.out.println("Use Syntax 1: Make sure input file is in src\\IncomingFiles\\ ");
		            System.out.println("Syntax 2 - java CCDADecode default");
		            System.out.println("Use Syntax 2: If you want to decode SampleBlueButtonPlusCCDA.xml file \n " +
		            		"that is already present in src\\IncomingFiles\\");
		            System.exit(1);
		   }
		  else if (args[0].equalsIgnoreCase("default")){
			     try{
				     CCDADecode ccda_decInstance = new CCDADecode(fileName2);
				  }catch (Exception ex) {
					  ex.printStackTrace();  
				  }  
		  }
		  else {
			  fileName2 = args[0];
			  System.out.println("Not Warning, Neither Error: Advice:: Make sure Input file is in src\\IncomingFiles\\  directory.");
			  try{
			     CCDADecode ccda_decInstance = new CCDADecode(fileName2);
			  }catch (Exception ex) {
				  ex.printStackTrace();  
			  }
		  }
		  */
		  
	}     // main method ends here
	
	/*  Constructor
	 *  Call appropriate methods required for the Decode of a CCDA file into text format
	 *  Saves file in src\\OutgoingFiles\\
	 *  @fileName  name of file which is going to be decoded
	 *  @author Gurpreet Singh 
	 */ 
	public CCDADecode(String fileName) {
		
		try{
		      	
		    File f = new File("");
		    String pathToFile =  f.getAbsolutePath();
		    pathToFile = pathToFile + "\\src\\IncomingFiles\\"  + fileName;
			
			FileInputStream f_in = new FileInputStream(fileName);               //pathToFile);
            ClinicalDocument ccdaDoc=(ClinicalDocument) CDAUtil.load(f_in );
            
            String fName = fileName;
            fName = fName.replace(".xml", ".text");
             
            
            // Printing First Line
            ToFile = ToFile +  "-----"+ccdaDoc.getCode().getDisplayName()+"-----" ;
            ToFile = ToFile + "\n";
            ToFile = ToFile +  "-----"+ccdaDoc.getTitle().getText()+ "-----\n";
            ToFile = ToFile + "\n";
            ToFile = ToFile + "\n";
            
             // ---------- Header------------
             PatientRoleDecoder(ccdaDoc);
             
             // ------------Body-------------
             
             // All Components of Body like Allergies, Medications etc....
             for (Component3 component_instance: ccdaDoc.getComponent().getStructuredBody().getComponents() ) {
            	 ToFile = ToFile + "\n";
            	try{	
            	   
            	 if(component_instance.getSection() != null) {
            		 // Printing Title of Each Major Section...
            		 ToFile = ToFile +  component_instance.getSection().getTitle().getText();   // Printing Title of Components...
            		 ToFile = ToFile + "\n";
            		 int length = component_instance.getSection().getTitle().getText().length();
                     for(int i=0; i< length; i++){
            		     ToFile = ToFile +  "=";	 
                     }
                     ToFile = ToFile + "\n";
                     ExtractTableCode2(component_instance.getSection().getTitle().getText(), fileName);
                     ExtractTableCode3(component_instance.getSection().getTitle().getText(), fileName);
                     ToFile = ToFile + "\n";
                     ToFile = ToFile + "\n";
                     
            	 }// if ends here...
               }catch (Exception ex){
            	   ex.printStackTrace();
               }	 
            
                 
             } // for loop for body ends here...
             
              
             ToFile = ToFile +  "--------End Of Document--------";
             ToFile = ToFile + "\n";
             System.out.println(ToFile);
            
             
             try {
            	 FileOutputStream out3 = new FileOutputStream(fName);             //f.getAbsolutePath()+"\\src\\OutgoingFiles\\"  + fName);
            	 out3.write(ToFile.getBytes());
				 out3.close();
		       } catch (IOException e) {
					
					e.printStackTrace();
			    }
             
		   }catch(Exception ex){
			   
			   ex.printStackTrace();
		   }  

   	 
    }


	// Extract Items that are in list in some sections...list Hospital Discharge...
	
public static void ExtractTableCode3(String Title, String fileName) {
		
		
		String navigate2 = "<title>"+Title+"</title>";
		
		File f = new File("");
	    String pathToFile =  f.getAbsolutePath();
	    pathToFile = pathToFile + "\\src\\IncomingFiles\\"  + fileName;
		
		TextFileInput tfi = new TextFileInput(fileName) ;         //pathToFile);
		String line = tfi.readLine(); 
		
	    try {
			
		
	    int count = 1;
	    while(line != null)
		{  if(line.contains(navigate2)) {
			  			  
			  while(line != null && (!line.contains("</section>"))) {
				 if(line.contains("<list")){
					 ToFile = ToFile + "\n     ";
					  while(line!=null && (!line.contains("</section>"))){
					      if(line.contains("<item>") && line.contains("</item>")) { 
							  
							  line.trim();
							  ToFile = ToFile + count++ + ") ";
							  ToFile = ToFile + ExtractBetween(line, ">" , "</item>" );
							  ToFile = ToFile + "\n     ";
						  }	 // if ends here
					     
					      line=tfi.readLine();
					  } // while ends here
				   }   // if ends here..
				   line=tfi.readLine();
				  
			   }
			  	  
			  
			}   // if loop ends here....
		
			line=tfi.readLine();
			
		}// while ends here
	    
	    }catch(Exception ex){
	    	
	    }
		
		
	}  // function ends here...
	

	
	/* Prints Text Sections of Each Section like Allergies, Medications etc...
	 * @Title  Title of Sections of Body
	 * fileName name of Input CCDA XML file 
	 */

	
public static void ExtractTableCode2(String Title, String fileName) {
		
		
		String navigate2 = "<title>"+Title+"</title>";
		
		File f = new File("");
	    String pathToFile =  f.getAbsolutePath();
	    pathToFile = pathToFile + "\\src\\IncomingFiles\\"  + fileName;
		
		TextFileInput tfi = new TextFileInput(fileName);    //pathToFile);
		String line = tfi.readLine(); 
	    int numOfCol=0, numOfRows = 1; 
	    String[] ColumnNames = new String[50];
	    String[][] RowValues;
	    int lengthOfColNames[] = new int[50];
	    try {
			ToFile = ToFile + "\n            ";
		
	
	    while(line != null)
		{  if(line.contains(navigate2)) {
			 
			  while(line != null && (!line.contains("</thead>")) && (!line.contains("</section>"))) {
				  if(line.contains("</th>")) { 
					  if(numOfCol != 0)
					      ToFile = ToFile +  "            ";
					  line.trim();
					  ColumnNames[numOfCol] = ExtractBetween(line, ">" , "</th>" );
					  ToFile = ToFile +  ColumnNames[numOfCol];
					  
					  lengthOfColNames[numOfCol] = ColumnNames[numOfCol].length();
					  
					  numOfCol++;
					  //ToFile = ToFile +  "            "); 
				  }	 // if ends here
				  
				  line=tfi.readLine();
				  
			   } 
			  if(numOfCol == 0) {
				  break;
			  }
			  ToFile = ToFile + "\n";
			  
			  ToFile = ToFile +  "            ";
			  for(int i=0; i< numOfCol; i++){
				  
				  for(int j=0; j < lengthOfColNames[i]; j++ ){
					  ToFile = ToFile +  "-";      
				  }
				  ToFile = ToFile +  "            ";
			  }
			  
			  ToFile = ToFile + "\n";
			  ToFile = ToFile +  "            ";
			  int count = 0;
		       
			  //for(int i=0; i<2; i++)
			    //  ToFile = ToFile +  lengthOfColNames[i]);
			  // Printing Rows
			  while(line != null && (!line.contains("</tbody>"))) {
				 
				  if(line.contains("</td>")) { 
					  //line.trim();
					  String term = ExtractBetween(line, ">" , "</td>" );
					  
					  if(term.length() < 16){
					      ToFile = ToFile +  term;
					      System.out.println(term);
					      for(int i=0; i< (12+lengthOfColNames[count] - term.length()); i++)
							     ToFile = ToFile +  " ";
					  }    
					  else{
						  System.out.println(term);
						  if(term.contains("<content")){
							  term = term + "</content>";
							  term = ExtractBetween(term, ">" , "</content>" ); 
						  }
						  
						  if(term.length() < 16) {
							  ToFile = ToFile +  term;
						  }
						  else {
							  ToFile = ToFile +  term.substring(0,15)+"...";  
						  }
						  
						  for(int i=0; i< 12+lengthOfColNames[count] - 16-3; i++)
						        ToFile = ToFile +  " ";
					  }
					  
					  count++;
					 
				  }	 // if ends here
				  if(count == numOfCol){
					  ToFile = ToFile + "\n";
					  ToFile = ToFile +  "            ";
					  count = 0;
				  }
					  
				  line=tfi.readLine();
				  
			   }
			  
			  
			}   // if loop ends here....
		
			line=tfi.readLine();
			
		}// while ends here
	    
	    }catch(Exception ex){
	    	
	    }
		
		
	}  // function ends here...
	
	
	public static String ExtractBetween(String line, String start, String end){
		
		String value = "";
		try{
			value = line.substring(line.indexOf(start)+1, line.indexOf(end) );
			if(value.contains("</content>")) 
			{   value = "&"+value;
				value = ExtractBetween(value, "&", "</content>");
			}
		}catch(Exception ex){
			
		}
		return value;
	}
	
	/*
	 * Decode the Patient Role Section of CCDA XML file
	 * @ccdaDoc input ccdaDoc
	 */
		
	public static void PatientRoleDecoder(ClinicalDocument ccdaDoc) {
		
        try{
			
           ToFile = ToFile +  "Patient Profile";
           ToFile = ToFile + "\n";
           ToFile = ToFile +  "===============\n";
           ToFile = ToFile + "\n";
           String spaceInBw = "           ";
           
       
           for (PatientRole patient_role: ccdaDoc.getPatientRoles() ) {
           	
           	ToFile = ToFile +  "   Patient";
           	ToFile = ToFile + "\n";
           	ToFile = ToFile +  "   -------";
           	ToFile = ToFile + "\n";   
           	// Patient First and Last Name
           	String lastName = patient_role.getPatient().getNames().get(0).getFamilies().get(0).getText();
               String firstName = patient_role.getPatient().getNames().get(0).getGivens().get(0).getText();
               ToFile = ToFile +  "      First name:" +spaceInBw + firstName;
               ToFile = ToFile + "\n";
               ToFile = ToFile +  "      Last name: " + spaceInBw + lastName;
               ToFile = ToFile + "\n";
               // Id of Patient
               int id_num = 1;
               for (II id_instance: patient_role.getIds()) {
               	ToFile = ToFile +  "      ID#"+id_num++ +":";
				    if(id_instance.getExtension() != null){
				        if(!id_instance.getExtension().isEmpty()) { 
						   ToFile = ToFile +  "      "+spaceInBw + id_instance.getExtension();
						   ToFile = ToFile + "\n";
				        }
				     }
				    else {
				    	ToFile = ToFile + "\n";
				    }
				 }
               
               // Telephone of Patient
               int numTel = 1;
               for (TEL tel_instance: patient_role.getTelecoms()) {
               	ToFile = ToFile +  "      Phone#"+numTel++ +":";
				    if(!tel_instance.getValue().isEmpty()) {
				    	ToFile = ToFile +  "   "+spaceInBw + tel_instance.getValue();
				    	if(!tel_instance.getUses().isEmpty()){
				    		ToFile = ToFile +  " "+tel_instance.getUses();
				    	    ToFile = ToFile + "\n";
				    	} 
				    } 		  
						   
				}
               
               
               // Patient Address
               for (AD address_instance: patient_role.getAddrs()) {
               	ToFile = ToFile +  "      Address:"+spaceInBw;
					for (PostalAddressUse use: address_instance.getUses() ){ 
						   ToFile = ToFile +  "   H(ome)/W(ork): " + use.getName();
						   ToFile = ToFile + "\n";
						   ToFile = ToFile +  "                    "+spaceInBw;
					}		   
					for (ADXP str: address_instance.getStreetAddressLines() )	
					 	   ToFile = ToFile +  "   "+str.getText() + ", ";
					for (ADXP c: address_instance.getCities() ) 
						   ToFile = ToFile +  c.getText() + ", ";
					for (ADXP state: address_instance.getStates() ) 
						   ToFile = ToFile +  state.getText() + " " ;
					for (ADXP postalcode: address_instance.getPostalCodes()){ 
						  ToFile = ToFile +  postalcode.getText() ;
						  ToFile = ToFile + "\n";
					}  
				}
               
               // Patient Info  -- assuming that there must be patient in Patient Role...
               Patient patient = patient_role.getPatient();        
               
               // Gender of Patient
               if(patient.getAdministrativeGenderCode() != null) {
                 if(patient.getAdministrativeGenderCode() != null) {
                     ToFile = ToFile +  "      Gender:"+spaceInBw+ "    "+patient.getAdministrativeGenderCode().getCode();
                     ToFile = ToFile + "\n";   
                    }
                 }else{               
             	    ToFile = ToFile +  "      Gender:"+spaceInBw+ "  Info Not Provided";
             	   ToFile = ToFile + "\n";
                 }
               
               // birthTime
               if(patient.getBirthTime() != null) {
                   String birthDate;
                   
					if(patient.getBirthTime().hasContent()){ 
                   	birthDate = patient.getBirthTime().getValue();
                   	String year = birthDate.substring(0, 4);
                       String month = birthDate.substring(4, 6) ;
                       String dd = birthDate.substring(6, 8);
                       ToFile = ToFile +  "      Birth Time:"+spaceInBw+ month+"/"+dd+"/"+year;
                       ToFile = ToFile + "\n";
					}    
                }else{               
               	    ToFile = ToFile +  "      Birth Time:"+spaceInBw+ "Info Not Provided";
               	    ToFile = ToFile + "\n";
                }
               
               // maritalStatusCode
               if(patient.getMaritalStatusCode() != null) {
                   if(patient.getMaritalStatusCode()!= null) 
                   {
                     ToFile = ToFile +  "      Marital Status:"+spaceInBw.substring(4) + patient.getMaritalStatusCode().getDisplayName();
                     ToFile = ToFile + "\n";
                   }
                 }else{               
             	    ToFile = ToFile +  "      Marital Status:"+spaceInBw.substring(4)+ "Info Not Provided";
             	    ToFile = ToFile + "\n";
                 }
               
               // religiousAffliationCode
               if(patient.getReligiousAffiliationCode() != null) {
                    if(patient.getReligiousAffiliationCode()!= null) {
                      ToFile = ToFile +  "      Religious Affil:"+spaceInBw.substring(5) + patient.getReligiousAffiliationCode().getDisplayName();
                      ToFile = ToFile + "\n";
                    }
                 }else{               
             	    ToFile = ToFile +  "      Religious Affil:"+spaceInBw.substring(5)+ "Info Not Provided";
             	    ToFile = ToFile + "\n";
                 }
               
              // raceCode
               if(patient.getRaceCode() != null) {
                 if(patient.getRaceCode()!= null) 
                  {   ToFile = ToFile +  "      Race:"+spaceInBw +"      " + patient.getRaceCode().getDisplayName();
                     ToFile = ToFile + "\n"; 
                  }
               }else{               
             	    ToFile = ToFile +  "      Race:"+spaceInBw + "      Info Not Provided";
             	    ToFile = ToFile + "\n";
               }
               
              // ethnicGroupCode
               if(patient.getEthnicGroupCode() != null) {
                 if(patient.getEthnicGroupCode()!= null) 
                 {   ToFile = ToFile +  "      Ethnicity:"+spaceInBw.substring(5) + "      " + patient.getEthnicGroupCode().getDisplayName();
                     ToFile = ToFile + "\n";
                  }
                }else{               
             	    ToFile = ToFile +  "      Ethnicity:"+spaceInBw+ " Info Not Provided";
             	   ToFile = ToFile + "\n";
                }
               
               
              // Language Code
               int lang_num = 1;
               for (LanguageCommunication lang_instance: patient.getLanguageCommunications() ) {
               	ToFile = ToFile +  "      Language#"+lang_num++ +":";
				    boolean flag = true;
               	if(lang_instance.getLanguageCode() != null)  {
				    	if(lang_instance.getLanguageCode().getDisplayName() != null){
				    	    ToFile = ToFile +  spaceInBw + lang_instance.getLanguageCode().getDisplayName();
				    	} 
				        else if(lang_instance.getLanguageCode().getCode() != null){
				    		ToFile = ToFile +  spaceInBw + lang_instance.getLanguageCode().getCode().toUpperCase();
				    	}
				    	else {
				    		ToFile = ToFile +  spaceInBw + "Info Not Provided";  
				     	    flag = false;
				    	}
				    	
				    	if(flag) {
				    		if(lang_instance.getModeCode()!= null)     
								 ToFile = ToFile +  "  (Spoke/Write:  "+lang_instance.getModeCode().getDisplayName()+")";
						    if(lang_instance.getPreferenceInd() != null)    
								 ToFile = ToFile +  ",  Preferred: "+ lang_instance.getPreferenceInd().getValue();
						    if(lang_instance.getProficiencyLevelCode() != null)      
								 ToFile = ToFile +  ",  Language Proficiency: "+ lang_instance.getProficiencyLevelCode().getDisplayName();
				    	}
				    	
				    	
				    	
               	} // if ends here...      
				  } // for loop		 
				    
                 
                  // Guardians under Patient---under Patient Role
                    ToFile = ToFile + "\n";
                   int guradian_num = 1;
	                try {
                     for (Guardian guardian_instance: patient.getGuardians()) {
		                   ToFile = ToFile +  "      Guardian#"+guradian_num++ +":";
		                   if(guardian_instance.getGuardianPerson() != null) {
		                	   ToFile = ToFile +  spaceInBw + guardian_instance.getGuardianPerson().getNames().get(0).getFamilies().get(0).getText();  
		                	   ToFile = ToFile +  " " + guardian_instance.getGuardianPerson().getNames().get(0).getGivens().get(0).getText();  
		                   }	
		                   // Relation to Patient
		                    ToFile = ToFile +  " ("+guardian_instance.getCode().getDisplayName()+") of Patient";
		                    ToFile = ToFile + "\n";   
			                // Telephone of Guardian Who is relative of Patient  
			                for (TEL tel_instance: patient_role.getTelecoms()) {   
			   				    if(!tel_instance.getValue().isEmpty()) {
			   				    	ToFile = ToFile +  "                 "+spaceInBw + tel_instance.getValue();
			   				    	if(!tel_instance.getUses().isEmpty()){
			   				    		ToFile = ToFile +  ""+tel_instance.getUses();
			   				    		ToFile = ToFile + "\n";
			   				    	}
			   				    } 		    
			   			    } // for loop ends here
			                
			             // Address of Guardian
			                for (AD address_instance: guardian_instance.getAddrs()) {
			                	
								for (ADXP str: address_instance.getStreetAddressLines() )	
								 	   ToFile = ToFile +  spaceInBw + "                 "+ str.getText() + ", ";  
								for (ADXP c: address_instance.getCities() ) 
									   ToFile = ToFile +  c.getText() + ", ";
								for (ADXP state: address_instance.getStates() ) 
									   ToFile = ToFile +  state.getText() + " " ;
								for (ADXP postalcode: address_instance.getPostalCodes()) 
									  ToFile = ToFile +  postalcode.getText() ;
								for (PostalAddressUse use: address_instance.getUses() ) 
									   ToFile = ToFile +  ", H(ome)/W(ork): " + use.getName();
							}
			                
			                
	                   
					    }   // Guardian case ends here...
	                  }
	                  catch(Exception ex) {
	                	  // Just to Catch Exceptions caused by Guardian  
	                  }
	                
	                 // BirthPlace of Patient...
	                 ToFile = ToFile + "\n";
	                 if(patient.getBirthplace() != null) {
	                	if(patient.getBirthplace().getPlace() != null) {
	                		 ToFile = ToFile +  "      BirthPlace:" + spaceInBw ;
	                		 for (AD address_instance: patient.getBirthplace().getPlace().getAddrs()) {
				                	
									for (ADXP str: address_instance.getStreetAddressLines() )	
									 	   ToFile = ToFile +  str.getText() + ", ";  
									for (ADXP c: address_instance.getCities() ) 
										   ToFile = ToFile +  c.getText() + ", ";
									for (ADXP state: address_instance.getStates() ) 
										   ToFile = ToFile +  state.getText() + " ";
									for (ADXP postalcode: address_instance.getPostalCodes()) 
										  ToFile = ToFile +  postalcode.getText() ;
									for (PostalAddressUse use: address_instance.getUses() ) 
										   ToFile = ToFile +  ", H(ome)/W(ork): " + use.getName();
								}
	                	} 
	                 }  // Birthplace ends here...
	                 
	               
	                 //  Care Provider under Patient Role
	                 ToFile = ToFile + "\n";
	                 ToFile = ToFile + "\n";
	                    
		                try {
		                  	
	                     Organization CareProvider;
						 if( (CareProvider = patient_role.getProviderOrganization()) != null) {
							   ToFile = ToFile +  "   Care Provider";
							   ToFile = ToFile + "\n";
							   ToFile = ToFile +  "   -------------";
							   ToFile = ToFile + "\n";
				               int primaryCareNum = 1;
							   
				               for (ON names_instance: CareProvider.getNames()) {   
								   ToFile = ToFile +  "      Primary Care#"+ primaryCareNum++ +":"+spaceInBw.substring(4); 
								   if(!names_instance.getText().isEmpty()) {
									   ToFile = ToFile +  names_instance.getText(); 	
				   				    } 		    
				   			    } // for loop ends here
							   
				               ToFile = ToFile + "\n";
				                // Telephone of Guardian Who is relative of Patient  
				                for (TEL tel_instance: CareProvider.getTelecoms()) {   
				   				    if(!tel_instance.getValue().isEmpty()) {
				   				    	ToFile = ToFile +  "      Phone:     ";
				   				    	ToFile = ToFile +  spaceInBw + tel_instance.getValue();
				   				    	if(!tel_instance.getUses().isEmpty())
				   				    	{		ToFile = ToFile +  ""+tel_instance.getUses();  
				   				    	        ToFile = ToFile + "\n";
				   				    	}
				   				    } 		    
				   			    } // for loop ends here
				                
				             // Address of Guardian
				                for (AD address_instance: CareProvider.getAddrs()) {
				                	ToFile = ToFile +  "      Address:"+spaceInBw + "   " ;
									for (ADXP str: address_instance.getStreetAddressLines() )	
									 	   ToFile = ToFile +  str.getText() + ", ";  
									for (ADXP c: address_instance.getCities() ) 
										   ToFile = ToFile +  c.getText() + ", ";
									for (ADXP state: address_instance.getStates() ) 
										   ToFile = ToFile +  state.getText() + " " ;
									for (ADXP postalcode: address_instance.getPostalCodes()) 
										  ToFile = ToFile +  postalcode.getText() ;
									for (PostalAddressUse use: address_instance.getUses() ) 
										   ToFile = ToFile +  ", H(ome)/W(ork): " + use.getName();
								}
				                
				                
		                   
						    }   // Care Provider case ends here...
		                  }
		                  catch(Exception ex) {
		                	  // Just to Catch Exceptions caused by Guardian  
		                  }    
		                ToFile = ToFile + "\n";
		                ToFile = ToFile + "\n";
		                ToFile = ToFile + "\n";
		                ToFile = ToFile + "\n";
	                 
               
               
               
               String PostAddressUse="";
               if (!patient_role.getAddrs().get(0).getUses().isEmpty())
                   PostAddressUse = patient_role.getAddrs().get(0).getUses().get(0).getName();
               String street = patient_role.getAddrs().get(0).getStreetAddressLines().get(0).getText();
               String city = patient_role.getAddrs().get(0).getCities().get(0).getText();
               String state = patient_role.getAddrs().get(0).getStates().get(0).getText();
               String zipcode = patient_role.getAddrs().get(0).getPostalCodes().get(0).getText();
               //ToFile = ToFile +  "Address Use Code: " + PostAddressUse + 
                 //      " Complete address: " + street + ", " + city + ", " + state + " " + zipcode);	
           
           }    // Patient Role ends here...
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}   // function ends here




} // Class CCDA decode ends here...

