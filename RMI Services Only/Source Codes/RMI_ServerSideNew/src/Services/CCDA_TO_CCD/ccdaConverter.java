package Services.CCDA_TO_CCD;
/* Class ccdConverter
 * Performs Conversion from CCD file(XML file) to CCDA (XML file)
 * All CCD files which are supposed to be Converted should be in src\IncomingFiles\ 
 * Save the CCDA file into appropriate directory(src\Outgoing files\)
 * @authorMinor Gurpreet Singh
 * @authorMajor Tatsiana
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.management.Query;
import javax.tools.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.openhealthtools.mdht.uml.cda.Act;
import org.openhealthtools.mdht.uml.cda.CDAFactory;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.EntryRelationship;
import org.openhealthtools.mdht.uml.cda.Observation;
import org.openhealthtools.mdht.uml.cda.Participant2;
import org.openhealthtools.mdht.uml.cda.ParticipantRole;
import org.openhealthtools.mdht.uml.cda.Patient;
import org.openhealthtools.mdht.uml.cda.PatientRole;
import org.openhealthtools.mdht.uml.cda.PlayingEntity;
import org.openhealthtools.mdht.uml.cda.Section;
import org.openhealthtools.mdht.uml.cda.ccd.AlertObservation;
import org.openhealthtools.mdht.uml.cda.ccd.AlertStatusObservation;
import org.openhealthtools.mdht.uml.cda.ccd.AlertsSection;
import org.openhealthtools.mdht.uml.cda.ccd.CCDFactory;
import org.openhealthtools.mdht.uml.cda.ccd.CCDPackage;
import org.openhealthtools.mdht.uml.cda.ccd.ContinuityOfCareDocument;
import org.openhealthtools.mdht.uml.cda.ccd.ProblemAct;
import org.openhealthtools.mdht.uml.cda.ccd.ReactionObservation;
import org.openhealthtools.mdht.uml.cda.impl.ClinicalDocumentImpl;
import org.openhealthtools.mdht.uml.cda.util.BasicValidationHandler;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.hl7.datatypes.AD;
import org.openhealthtools.mdht.uml.hl7.datatypes.ADXP;
import org.openhealthtools.mdht.uml.hl7.datatypes.ANY;
import org.openhealthtools.mdht.uml.hl7.datatypes.CD;
import org.openhealthtools.mdht.uml.hl7.datatypes.CE;
import org.openhealthtools.mdht.uml.hl7.datatypes.CS;
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory;
import org.openhealthtools.mdht.uml.hl7.datatypes.ENXP;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.datatypes.PN;
import org.openhealthtools.mdht.uml.hl7.datatypes.TS;
import org.openhealthtools.mdht.uml.hl7.datatypes.impl.PQImpl;
import org.openhealthtools.mdht.uml.hl7.vocab.EntityClassRoot;
import org.openhealthtools.mdht.uml.hl7.vocab.ParticipationType;
import org.openhealthtools.mdht.uml.hl7.vocab.PostalAddressUse;
import org.openhealthtools.mdht.uml.hl7.vocab.RoleClassRoot;
import org.openhealthtools.mdht.uml.hl7.vocab.x_ActRelationshipEntryRelationship;
import Services.CCD_Decode.ccdDecode;

public class ccdaConverter {
    
    // Constructor	
    public ccdaConverter(String fileName) {
     CCDPackage.eINSTANCE.eClass();
	 try {	
		File f = new File("");
		String pathToFile =  f.getAbsolutePath();
	    pathToFile = pathToFile + "\\src\\IncomingFiles\\"  + fileName;
		FileInputStream f_in = new FileInputStream(fileName);      //pathToFile);
		ClinicalDocument ccdDocumentIn=(ClinicalDocument) CDAUtil.load(f_in );
		
		System.out.println();
		System.out.println("--- Consolidated CD to ContinuityOfCareDocument ---");
		System.out.println();
		String lastName1="";
		String firstName1="";
		String street="";
		String city="";
		String state="" ;
		String zipcode="";
		String gender="";
		String birthDate="";
		String PostAddressUse="";
		for (PatientRole patient_role: ccdDocumentIn.getPatientRoles() ) {
			 lastName1 = patient_role.getPatient().getNames().get(0).getFamilies().get(0).getText();
			 firstName1 = patient_role.getPatient().getNames().get(0).getGivens().get(0).getText();
			//System.out.println("Patient full name: " + lastName1 + ", " + firstName1);
			
			if (!patient_role.getAddrs().get(0).getUses().isEmpty())
				PostAddressUse = patient_role.getAddrs().get(0).getUses().get(0).getName();
			 street = patient_role.getAddrs().get(0).getStreetAddressLines().get(0).getText();
			 city = patient_role.getAddrs().get(0).getCities().get(0).getText();
			 state = patient_role.getAddrs().get(0).getStates().get(0).getText();
			 zipcode = patient_role.getAddrs().get(0).getPostalCodes().get(0).getText();
			 if(patient_role.getPatient().getAdministrativeGenderCode() != null) 
                gender=patient_role.getPatient().getAdministrativeGenderCode().getCode();
             if(!patient_role.getPatient().getBirthTime().getValue().isEmpty())
            	birthDate      = patient_role.getPatient().getBirthTime().getValue();
      
		}
		// create and initialize an instance of the ContinuityOfCareDocument class
		ContinuityOfCareDocument ccdDocument = CCDFactory.eINSTANCE.createContinuityOfCareDocument().init();
		
		// create a patient role object and add it to the document
		PatientRole patientRole = CDAFactory.eINSTANCE.createPatientRole();
		ccdDocument.addPatientRole(patientRole);
		II id = DatatypesFactory.eINSTANCE.createII();
		patientRole.getIds().add(id);
		id.setRoot("996-756-495");
		id.setExtension("2.16.840.1.113883.19.5");

		// create an address object and add it to patient role
		AD addr = DatatypesFactory.eINSTANCE.createAD();
		patientRole.getAddrs().add(addr);
		addr.getUses().add(PostalAddressUse.getByName(PostAddressUse));
		addr.addStreetAddressLine(street);
		addr.addCity(city);
		addr.addState(state);
		addr.addPostalCode(zipcode);

		// create a patient object and add it to patient role
		Patient patient = CDAFactory.eINSTANCE.createPatient();
		patientRole.setPatient(patient);
		PN name = DatatypesFactory.eINSTANCE.createPN();
		patient.getNames().add(name);
		name.addGiven(firstName1);
		name.addFamily(lastName1);

		CE administrativeGenderCode = DatatypesFactory.eINSTANCE.createCE();
		patient.setAdministrativeGenderCode(administrativeGenderCode);
		administrativeGenderCode.setCode(gender);
		administrativeGenderCode.setCodeSystem("2.16.840.1.113883.5.1");

		TS birthTime = DatatypesFactory.eINSTANCE.createTS();
		patient.setBirthTime(birthTime);
		birthTime.setValue(birthDate );
		// create and initialize the CCD alerts section
		AlertsSection alertsSection = CCDFactory.eINSTANCE.createAlertsSection().init();
		ccdDocument.addSection(alertsSection);

		// set up the narrative (human-readable) text portion of the alerts section
		ArrayList<String> substance = new ArrayList<String>(); 
		ArrayList<String> reaction = new ArrayList<String>(); 
		ArrayList<String> status = new ArrayList<String>(); 
		StringBuffer buffer = new StringBuffer();
		
		
//this part is not ready yet
		for (Section section: ccdDocumentIn.getSections()) {
			se Reactions, Alerts")){
			if (section.getCode()!=null){
				if (section.getCode().getCode().equals("48765-2")){  //codeSystemName="LOINC" 
					// set up the narrative (human-readable) text portion of the alerts section
					buffer.append("<table border=\"1\" width=\"100%\">");
					buffer.append("<thead>");
					buffer.append("<tr>");
					buffer.append("<th>Substance</th>");
					buffer.append("<th>Reaction</th>");
					buffer.append("<th>Status</th>");
					buffer.append("</tr>");
					buffer.append("</thead>");
					buffer.append("<tbody>");
					buffer.append("<tr>");
					buffer.append("<td>Penicillin</td>");
					buffer.append("<td>Hives</td>");
					buffer.append("<td>Active</td>");
					buffer.append("</tr>");
							
				for (Act act: section.getActs() ) {
					for (EntryRelationship er: act.getEntryRelationships() ) {
						for (ANY val: er.getObservation().getValues() ) {
							System.out.println(((CD) val).getDisplayName()); //=((CD) val).getDisplayName() ;
						}
						for (Participant2 participant: er.getObservation().getParticipants() ) {
							System.out.println(participant.getParticipantRole().getPlayingEntity().getCode().getDisplayName()); 
						}
						for (EntryRelationship er_val: er.getObservation().getEntryRelationships() ){
							if (!((CD) er_val.getObservation().getValues().get(0)).getDisplayName().isEmpty())
								System.out.println(((CD) er_val.getObservation().getValues().get(0)).getDisplayName());
						}
						//System.out.println(observation+participant1+observation2);
					}
					
				}
				buffer.append("</tbody>");
				buffer.append("</table>");
				alertsSection.createStrucDocText(buffer.toString());
			}}
		}
		alertsSection.createStrucDocText(buffer.toString());

		// create and initialize a CCD problem act
		ProblemAct problemAct = CCDFactory.eINSTANCE.createProblemAct().init();
		alertsSection.addAct(problemAct);

		id = DatatypesFactory.eINSTANCE.createII();
		problemAct.getIds().add(id);
		id.setRoot(UUID.randomUUID().toString());

		// create and initialize an alert observation within the problem act
		AlertObservation alertObservation = CCDFactory.eINSTANCE.createAlertObservation().init();
		problemAct.addObservation(alertObservation);
		((EntryRelationship) alertObservation.eContainer()).setTypeCode(x_ActRelationshipEntryRelationship.SUBJ);

		id = DatatypesFactory.eINSTANCE.createII();
		alertObservation.getIds().add(id);
		id.setRoot(UUID.randomUUID().toString());

		CD code = DatatypesFactory.eINSTANCE.createCD();
		alertObservation.setCode(code);
		code.setCode("ASSERTION");
		code.setCodeSystem("2.16.840.1.113883.5.4");

		CS statusCode = DatatypesFactory.eINSTANCE.createCS();
		alertObservation.setStatusCode(statusCode);
		statusCode.setCode("completed");

		CD value = DatatypesFactory.eINSTANCE.createCD();
		alertObservation.getValues().add(value);
		value.setCode("282100009");
		value.setCodeSystem("2.16.840.1.113883.6.96");
		value.setDisplayName("Adverse reaction to substance");

		// playing entity contains coded information on the substance
		Participant2 participant = CDAFactory.eINSTANCE.createParticipant2();
		alertObservation.getParticipants().add(participant);
		participant.setTypeCode(ParticipationType.CSM);

		ParticipantRole participantRole = CDAFactory.eINSTANCE.createParticipantRole();
		participant.setParticipantRole(participantRole);
		participantRole.setClassCode(RoleClassRoot.MANU);

		PlayingEntity playingEntity = CDAFactory.eINSTANCE.createPlayingEntity();
		participantRole.setPlayingEntity(playingEntity);
		playingEntity.setClassCode(EntityClassRoot.MMAT);

		CE playingEntityCode = DatatypesFactory.eINSTANCE.createCE();
		playingEntity.setCode(playingEntityCode);
		playingEntityCode.setCode("70618");
		playingEntityCode.setCodeSystem("2.16.840.1.113883.6.88");
		playingEntityCode.setDisplayName("Penicillin");

		// reaction observation contains coded information on the adverse reaction
		ReactionObservation reactionObservation = CCDFactory.eINSTANCE.createReactionObservation().init();
		alertObservation.addObservation(reactionObservation);
		((EntryRelationship) reactionObservation.eContainer()).setTypeCode(x_ActRelationshipEntryRelationship.MFST);
		((EntryRelationship) reactionObservation.eContainer()).setInversionInd(Boolean.TRUE);

		code = DatatypesFactory.eINSTANCE.createCD();
		reactionObservation.setCode(code);
		code.setCode("ASSERTION");
		code.setCodeSystem("2.16.840.1.113883.5.4");

		statusCode = DatatypesFactory.eINSTANCE.createCS();
		reactionObservation.setStatusCode(statusCode);
		statusCode.setCode("completed");

		value = DatatypesFactory.eINSTANCE.createCD();
		reactionObservation.getValues().add(value);
		value.setCode("247472004");
		value.setCodeSystem("2.16.840.1.113883.6.96");
		value.setDisplayName("Hives");
		// alert status contains information about whether allergy is currently active
		AlertStatusObservation alertStatusObservation = CCDFactory.eINSTANCE.createAlertStatusObservation().init();
		alertObservation.addObservation(alertStatusObservation);
		((EntryRelationship) alertStatusObservation.eContainer()).setTypeCode(x_ActRelationshipEntryRelationship.REFR);

		CE alertStatusObservationValue = DatatypesFactory.eINSTANCE.createCE();
		alertStatusObservation.getValues().add(alertStatusObservationValue);
		alertStatusObservationValue.setCode("55561003");
		alertStatusObservationValue.setCodeSystem("2.16.840.1.113883.6.96");
		alertStatusObservationValue.setDisplayName("Active");
		// write the document out to the console
			CDAUtil.save(ccdDocument, System.out);
			// Write to disk with FileOutputStream
			FileOutputStream f_out = new 
				FileOutputStream("CCDA_To_CCD_Converted_" + fileName );    //f.getAbsolutePath()+"\\src\\OutgoingFiles\\"  + fileName);
			CDAUtil.save(ccdDocument, f_out);
			f_out.close();
	    }catch(Exception ex){
	    	
	    }
	}

	public static void main(String[] args) throws Exception{
		
		ccdaConverter ccd_DecInstance = new ccdaConverter("BlueButtonPlusSample.xml");
		
	}     
	
}
