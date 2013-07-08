/* DecodingApplet
 * Description:
 *    1) This is an applet, where you can decode/encode CCDA and CCD files into text files
 *       and from CCD to CCDA Conversion. 
 *    2) The Services like CCDA to Text, CCD to Text and CCD to CCDA are provided as RMI Server Side services,
 *    3) You can choose any service Provider by mentioning the IP address of Service provider in the Applet 
 * @author GurpreetSingh
 */
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class DecodingApplet extends JApplet {
	JPanel ControlPanel;
	JTextArea MainPanelHomeArea;
	boolean isStandalone = false;   	
 	private static final long serialVersionUID = 1L;
 	static String FileValue = "";
 	static String ChosedFile = "No file Chosen yet...";
	static File FileChosed;
	
	 public String getParameter(String key, String def) {
 	    if (isStandalone) {
 	      return System.getProperty(key, def);
 	    }
 	   if (getParameter(key) != null) {
 	      return getParameter(key);
 	   }
 	    return def;
}
  
  public void init() {
 	    try  { 
 	       ControlPanel = new JPanel(new XYLayout());
      	   ControlPanel.setPreferredSize(new Dimension(200, 580));
      	   ControlPanel.setBackground(new Color(250, 250, 250));
      	   ControlPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
 	
 	       MainPanelHomeArea = new JTextArea("\n   Welcome to Health Care Services Admin!");
  		   MainPanelHomeArea.append("\n\n  What you can do here...\n   1) Convert CCDA to Text \n   2) " +
  		   		" Convert CCD to Text \n   3) Convert CCDA to CCD");
  		  
  		   MainPanelHomeArea.setBackground(new Color(250, 250, 250));
  		   getContentPane().setLayout(new XYLayout());            // XY Layout
  		   getContentPane().add(new JScrollPane(MainPanelHomeArea), new XYConstraints(210, 5, 820, 650));
  		   getContentPane().add(new JScrollPane(ControlPanel), new XYConstraints(5, 5, 200, 650 ));
		   ShowServicesComboBoxInControlPanel();
 			setName("Health Care Services: CCDA/CCD to Text, CCDA to CCD");
			setSize(1080, 660);   // width, height
		    setLocation(200,30);
		    setVisible(true);	        
 	    }
 	    catch (Exception e) {
 	      e.printStackTrace();
 	    }
   }
 	  
	public void ShowServicesComboBoxInControlPanel(){
 	 
		 ControlPanel.removeAll();    	     	   
		 JButton Choose_File = new JButton("Choose File");        // refresh button for Internet Connection...
		 Choose_File.addActionListener(new java.awt.event.ActionListener()
		 {
		        public void actionPerformed(ActionEvent e)
		        {   JFileChooser chooser = new JFileChooser();
		 	        FileNameExtensionFilter filter = new FileNameExtensionFilter(
		 	 	           "CCDA/CCD XML Files Only", "xml");
		 	 	       chooser.setFileFilter(filter);
		 	 	       int returnVal = chooser.showOpenDialog(getParent());
		 	 	       if(returnVal == JFileChooser.APPROVE_OPTION) {
		 	 	          System.out.println("You chose to open this file: " +
		 	 	               chooser.getSelectedFile().getName());
		 	 	          
		 	 	          MainPanelHomeArea.append("You chose to open this file: " +
		 	 	 	               chooser.getSelectedFile().getName());	 	 	         
		 	 	     }
		 	 	     FileChosed = chooser.getSelectedFile();
		 	 	      ChosedFile =  chooser.getSelectedFile().getName();
		        	ShowServicesComboBoxInControlPanel();
		        }
		  } 
		  );
			  
		 ControlPanel.add(Choose_File, new XYConstraints(5, 220, 110, 22));  
		 JLabel choseFile = new JLabel(ChosedFile);
		 choseFile.setOpaque(true);
		 ControlPanel.add(choseFile, new XYConstraints(5, 245, 150, 22));
		 
		 JLabel LabelTables = new JLabel("All Services:", JLabel.CENTER); 
		 LabelTables.setBackground(new Color(35, 90, 150));           // 100, 100, 100
		 LabelTables.setFont(new Font("Dialog", 1, 13)); 
		 LabelTables.setForeground(new Color(250, 250, 250));
		 LabelTables.setOpaque(true);
			 Border border =  BorderFactory.createLineBorder(new Color(170, 170, 170));       //BorderFactory.createLineBorder(Color.blue);
			 LabelTables.setBorder(border);
			 ControlPanel.add(LabelTables, new XYConstraints(10, 150, 100, 20)); 
		 
		 String[] services = {"ConvertCCDA", "ConvertCCD", "CCDAtoCCD"};    
		final JComboBox AllServices = new JComboBox(services); 
		AllServices.setSelectedIndex(0);
		AllServices.setBackground(new Color(255, 255, 255));
		ControlPanel.add(AllServices, new XYConstraints(10, 177, 160, 25));
		JLabel server_IP_Label = new JLabel("RMI Server IP");
		ControlPanel.add(server_IP_Label, new XYConstraints(10, 10, 180, 22));
		final JTextField server_ip = new JTextField("localhost");
		ControlPanel.add(server_ip, new XYConstraints(10, 35, 180, 22)); 
		
		AllServices.addActionListener(new java.awt.event.ActionListener()
		{
					public void actionPerformed(ActionEvent e)
					{  
						String fileName2 = "";
						try {
							fileName2 = FileChosed.getCanonicalPath();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
						
						String serv = (String)(AllServices.getSelectedItem());
						String NameOfFileToConvert =  "example4.xml";      //"BlueButtonPlusSample.xml";
						String ConversionServ =  serv;             //"CCDAtoCCD";
						String AsterieskJava_ServerIP =  server_ip.getText();        //"localhost";    
						String Service_UID = "my_transact"; 
						//AsterieskJava_ServerIP.trim();
						System.out.println(server_ip.getText());
						// Instance of Client
						
						RMI_BioAPI_AsteriskJava_Client client_inst = new RMI_BioAPI_AsteriskJava_Client(FileChosed.getName(), fileName2, ConversionServ, AsterieskJava_ServerIP, Service_UID);
						String filenamme = FileChosed.getName();
						
						String ReceivedFileName ;
						  if(serv.equalsIgnoreCase("CCDAtoCCD")) {
							ReceivedFileName =    "CCDA_To_CCD_Converted_" + filenamme;                  //"CCDA_To_CCD_Converted_" + fileToConvert;
						  }else {
							ReceivedFileName = filenamme.replace(".xml", ".text");
						  }
						MainPanelHomeArea.setText("");
						MainPanelHomeArea.append(client_inst.ReceiveDecodedOrConvertedFile);

						JOptionPane.showMessageDialog(null, "Here in the right window, the decoded " +
								"file may or may not be displayed well....\n So Use Text Editor " +
								"like Notepad++ to view the decoded file. \n " +
								"The decoded file is saved in the " +
								"same folder as the Input file.");
						
						MainPanelHomeArea.setVisible(false);
						MainPanelHomeArea.setVisible(true);
						System.out.println("Service Chosen");
					}
			 }
		 ); 
		 
		 
		 JTextArea message1 = new JTextArea(" You can choose any Service\n" +
					   " just by selecting it! \n Choose FILE first and then \n Service...");
		 message1.setBackground(new Color(250, 250, 250));
		 ControlPanel.add(message1, new XYConstraints(10, 70, 200, 80)); 
		 
		 JTextArea message2 = new JTextArea(" The decode file will \n" +
				   " be saved at the same place \n from where you chose\n" +
				   " Input file. " +
				   " Although Extension of \n Decoded file may vary.\n\n" +
				   " Must open the decoded \n" +
				   " file in Notepad++ \n" +
				   " or some better editor \n" +
				   "to evaluate the quality \n" +
				   "of decode file");
		  message2.setBackground(new Color(250, 250, 250));
		  ControlPanel.add(message2, new XYConstraints(10, 300, 200, 200)); 

		 
		 ControlPanel.setVisible(false);
		 ControlPanel.setVisible(true);
								 
	  }

		  /**
		   * start
		   */
		  public void start() {
			  
		  
		   }    


		  /**
		   * stop
		   */
		  public void stop() {
		  }

		  /**
		   * destroy
		   */
		  public void destroy() {
		  }

		  /**
		   * getAppletInfo
		   * @return java.lang.String
		   */
		  public String getAppletInfo() {
			return "Applet Information";
		  }

		  /**
		   * getParameterInfo
		   * @return java.lang.String[][]
		   */
		  public String[][] getParameterInfo() {
			return null;
		  }
	
}  // Decoding Applet
