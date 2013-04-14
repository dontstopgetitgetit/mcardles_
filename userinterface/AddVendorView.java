//************************************************************
//    COPYRIGHT Sandeep Mitra and Students, The College at Brockport, 2013 - ALL RIGHTS RESERVED
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
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
public class AddVendorView extends View
{
	// GUI components
	protected JTextField vendorName;	
	
	//-------------------------------
	
	protected JTextField txtAreaCode;
	protected JTextField txtFirstThree;
	protected JTextField txtLastFour;
	//-------------------------------

	protected JButton submitButton;
	protected JButton cancelButton;

	// For showing error message
	protected MessageView statusLog;
	
	//----------------------------------------------------------------------------------
	public AddVendorView(IModel transaction)
	{
		super(transaction, "AddVendorView");
		
		// set the layout for this panel
		setLayout( new BorderLayout () );

		// create our GUI components, add them to this panel
		add( createTitle(), BorderLayout.NORTH );
		add( createForm(), BorderLayout.CENTER ); 

		// Error message area, even if don't expect it to be used
		add( createStatusLog("                                     "), BorderLayout.SOUTH );

		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("VendorAddedMessage", this);
		myModel.subscribe("NameAlreadyExistsMessage", this);
		
		populateFields();
	}
	
	// Overide the paint method to ensure we can set the focus when made visible -- DON'T DO IT!!!
	//-------------------------------------------------------------
	public void paint(Graphics g)
	{
		super.paint(g);
		
	}
	
	//----------------------------------------------------------------
	// Create this View Title with the string specified and formatted
	// to this program standard in the superclass View
	//-----------------------------------------------------------------
	protected JPanel createTitle()
	{		
		return formatViewTitle ("ADD A VENDOR");
	}
	
	//---------------------------------------------------------------------
	// Create the actual form that allows the user to input the new data
	// regarding a new worker
	//---------------------------------------------------------------------
	protected JPanel createForm()
	{	
		JPanel formPanel = new JPanel ();
		formPanel.setLayout( new BoxLayout ( formPanel, BoxLayout.Y_AXIS ));
		formPanel.setBackground ( blue );
		
		formPanel.add(createDataEntryFields());
		
		formPanel.add(Box.createRigidArea( size ));
		formPanel.add(createNavigationButtons());
		
		formPanel.setBorder (BorderFactory.createEmptyBorder( 10, 10, 10, 10 ));
		
		return formPanel;		
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
		
		
		return entryFieldsPanel;
	}
	 
	// Depending on the mode, makes fields editable or not. 
	//-------------------------------------------------------------
	protected void switchFields(boolean mode)
	{
		if(mode == true)
		{
			vendorName.setEnabled(true);		
			txtAreaCode.setEnabled(true);
			txtFirstThree.setEnabled(true);
			txtLastFour.setEnabled(true);
				
			vendorName.setFocusable(true);	
			txtAreaCode.setFocusable(true);
			txtFirstThree.setFocusable(true);
			txtLastFour.setFocusable(true);		
		}
		
		else
		{
			vendorName.setEnabled(false);	
			txtAreaCode.setEnabled(false);
			txtFirstThree.setEnabled(false);
			txtLastFour.setEnabled(false);
			
			vendorName.setFocusable(false);
			txtAreaCode.setFocusable(false);
			txtFirstThree.setFocusable(false);
			txtLastFour.setFocusable(false);
		}
	}
	
	// Create the navigation buttons
	//-------------------------------------------------------------
	protected JPanel createNavigationButtons()
	{
		JPanel buttonsPanel = new JPanel();	
		buttonsPanel.setBackground( blue );
		
		// create "raw" JButtons and call superclass View to format 
		// the buttons to the program's standard, add them to the panel
		submitButton = new JButton( "Save" );
		
		buttonsPanel.add( formatButtonSmall ( submitButton ));

		cancelButton = new JButton( "Main Menu" );
		buttonsPanel.add( formatButtonSmall ( cancelButton ));
		
		return buttonsPanel;
	}

	// Create the status log field
	//-------------------------------------------------------------
	protected JPanel createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	protected void populateFields()
	{
		vendorName.setText("");
		txtAreaCode.setText("585");
		txtFirstThree.setText("");
		txtLastFour.setText("");
		
		vendorName.requestFocus();
	}
	
	//-------------------------------------------------------------
	protected boolean getAndValidateData() 
	{		
		String unEntered = vendorName.getText();
		String area = txtAreaCode.getText();
		String first = txtFirstThree.getText();
		String last = txtLastFour.getText();
		
		
		if(unEntered == null || unEntered.equals(""))
		{
			displayErrorMessage("Vendor name cannot be empty!");
			vendorName.requestFocus(true);
			return false;
		}
		else
		if(area == null || first == null || last== null 
		|| first.equals("") || area.equals("")|| last.equals(""))
		{
			displayErrorMessage("Fill in all three phone number fields");
			txtFirstThree.requestFocus(true);
			return false;
		}
		
		return  true;
	}
	
	//-------------------------------------------------------------
	protected void processData(String vn, String phone)
	{
		Properties props = new Properties();		
		props.setProperty("Name", vn);
		props.setProperty("PhoneNumber", phone);
		
		myModel.stateChangeRequest("SaveVendor", props);
	}
	
	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
		clearErrorMessage();
		
		if(evt.getSource() == vendorName)
		{
			String unEntered = vendorName.getText();
			
			if ((unEntered != null) && (unEntered.length() > 0))
			{
				myModel.stateChangeRequest("Name", unEntered);
			}
		}
		else
		if(evt.getSource() == submitButton)
		{
			if(getAndValidateData())
			{
				String vnEntered = vendorName.getText();
				String phoneConcat = txtAreaCode.getText() + "-" + txtFirstThree.getText() + "-" + txtLastFour.getText();
				
				processData(vnEntered, phoneConcat);
			}
		}
		else
		if (evt.getSource() == cancelButton)
		{
			// Call method in Clerk to exit system
			myModel.stateChangeRequest("Cancel", null);
		}
	}
	
	//----------------------------------------------------------	
	public void updateState(String key, Object value)
	{
		if(key.equals("NameAlreadyExistsMessage") == true)
		{
			String message = (String)value;
			if (message.startsWith("ERROR") == false)
			{
				displayMessage(message);
				vendorName.setEditable(false);
			}
			else
			{
				JOptionPane.showMessageDialog(MainFrame.getInstance(), message, 
						"Vendor name Already Exists", JOptionPane.ERROR_MESSAGE);
				vendorName.setText("");
				txtFirstThree.setText("");
				txtLastFour.setText("");
				vendorName.setEditable(true);
			}
		}
		else
		if(key.equals("VendorAddedMessage") == true)
		{
			String message = (String)value;
			if (message.startsWith("ERROR") == false)
			{
				displayMessage(message);
				vendorName.setText("");
				txtFirstThree.setText("");
				txtLastFour.setText("");
				vendorName.setEditable(true);
			}
			else
			{
				displayErrorMessage((String)value);
			}
			
		}
	}
	
	/**
	 * Display message
	 */
	//----------------------------------------------------------	
	public void displayMessage(String message)
	{
		statusLog.displayMessage(message);
	}

	/**
	 * Display error message
	 */
	//----------------------------------------------------------
	public void displayErrorMessage(String message)
	{
		statusLog.displayErrorMessage(message);
	}

	/**
	 * Clear error message
	 */
	//----------------------------------------------------------
	public void clearErrorMessage()
	{
		statusLog.clearErrorMessage();
	}
	

	//-----------------------------------------------------------
	protected void processListSelection(EventObject evt){}
	
}

