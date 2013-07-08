EMR_DecodeEncode-RMI_Applet
===========================

This repository contains a single program with two sides, One is RMI Server side (EMR-DecodeEncode) and other is 
RMI Client Side (GUI-Applet). The user on the RMI Client side(Applet) choose the Service Provider(on RMI Server Side), 
then chooses the Specific Service and then the relevant XML file (for Encoding/Decoding /or Conversion). Then 
RMI Client makes Remote Procedure Call and send the chosen file to the Service Provider(on RMI_Server Side), and then 
Service provider do their job and send the Decoded/Encoded/Converted file back to RMI Client and then back to applet.

Check ReadMe files in each folder for more info.
