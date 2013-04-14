//************************************************************
//    COPYRIGHT 2013, Sandeep Mitra and Students, The College at Brockport
//                         - ALL RIGHTS RESERVED
//
// This file is the product of Sandeep Mitra and Students, 
// The College at Brockport, and is
// provided for unrestricted use provided that this legend
// is included on all tape media and as part of the software
// program in whole or part. Users may copy or modify this
// file without charge, but are not authorized to license or
// distribute it to anyone else except as part of a product or
// program developed by the user.
//*************************************************************
package userinterface;

//system imports
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.util.Calendar;
import java.util.EventObject;
import java.util.Properties;
import java.util.Vector;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

// project imports
import impresario.IModel;

//==============================================================
public class ModifyVendorView extends AddVendorView
{
	
	//----------------------------------------------------------------------------------
	public ModifyVendorView(IModel transaction)
	{
		super(transaction);
		
		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("VendorModifiedMessage", this);
	}
	
	//----------------------------------------------------------------
	// Create this View Title with the string specified and formatted
	// to this program standard in the superclass View
	//-----------------------------------------------------------------
	protected JPanel createTitle()
	{		
		return formatViewTitle ("Modify A Vendor");
	}
	
	//-----------------------------------------------------------------
		protected JPanel createDataEntryFields()
		{
			JPanel entryFieldsPanel = new JPanel();
			entryFieldsPanel.setBackground(blue);	
			entryFieldsPanel.setLayout( new BoxLayout ( entryFieldsPanel, BoxLayout.Y_AXIS ));
			entryFieldsPanel.setBorder (BorderFactory.createEmptyBorder( 0,100, 0, 100 ));
			
			JPanel vendorNamePanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
			vendorNamePanel.setBackground( blue );	
			
			vendorName = new JTextField(16);
			vendorName.addActionListener(this);
			vendorName.addFocusListener(this);
			vendorNamePanel.add(formatCurrentPanel("Vendor Name:", vendorName));
			entryFieldsPanel.add(vendorNamePanel);	
			
			JPanel numberPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
			numberPanel.setBackground( blue );	
			
			
			txtAreaCode = new NumericTextField(3,3);		
			txtFirstThree = new NumericTextField(3,3);		
			txtLastFour = new NumericTextField(4,4);
			
			JPanel phonePanel = formatCurrentPanel("Phone Number", txtAreaCode, txtFirstThree, txtLastFour);
			phonePanel.setLayout(new FlowLayout( FlowLayout.LEFT ));
			numberPanel.add(phonePanel);
			txtAreaCode.setText("585");
			entryFieldsPanel.add(numberPanel);	
			
			JPanel statusPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		
			
			return entryFieldsPanel;
		}
		 
	
	
	//-------------------------------------------------------------
	protected void populateFields()
	{
		
		vendorName.setText((String)myModel.getState("Name"));
		vendorName.setEditable(true);
		String phone = (String)myModel.getState("PhoneNumber");
		
		if ((phone != null) && (phone.length() > 0))
		{
			txtAreaCode.setText(phone.substring(0, 3));
			txtFirstThree.setText(phone.substring(4, 7));
			txtLastFour.setText(phone.substring(8, 12));
		}
	}
	
	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
		clearErrorMessage();
		
		if(evt.getSource() == submitButton)
		{
			if ( deletionIsConfirmed ())
			{					
				Properties props = new Properties();
				String phoneConcat = txtAreaCode.getText() + "-" + txtFirstThree.getText() + "-" + txtLastFour.getText();
				
//				props.setProperty("Name", (String)myModel.getState("Name"));
//				props.setProperty("PhoneNumber", (String)myModel.getState("PhoneNumber"));
				props.setProperty("Name", vendorName.getText());
				props.setProperty("PhoneNumber", phoneConcat);
				props.setProperty("Status", "Active"); //eflei1 modified so want active now				
				myModel.stateChangeRequest("SaveVendor", props);
				
			}
		}
		else
		if (evt.getSource() == cancelButton)
		{
			// Call method in Clerk to exit system
			myModel.stateChangeRequest("Cancel", null);
		}
		
	}
//-----------------------------------------------------------------------
	boolean deletionIsConfirmed ()
	{
		int response = JOptionPane.showConfirmDialog(
				this, "Are you sure you wish to Modify this Vendor?",
				"Confirm Deletion",
				JOptionPane.YES_NO_OPTION );

		if ( response == JOptionPane.YES_OPTION )
		{
			return true;
		}

		return false;
	}
//--------------------------------------------------------------------------

	//----------------------------------------------------------	
	public void updateState(String key, Object value)
	{
		
		if(key.equals("VendorModifiedMessage") == true)
		{
			
			String message = (String)value;
			if (message.startsWith("ERROR") == false)
			{
				displayMessage(message);
				
			}
			else
			{
				//JOptionPane.showMessageDialog(MainFrame.getInstance(), message, 
				//		"Item Type Modification Error", JOptionPane.ERROR_MESSAGE);
				displayErrorMessage(message);
			}
			
		}
	}
	
}
