//************************************************************
//    COPYRIGHT 2013, Sandeep Mitra and Students, 
//		The College at Brockport - ALL RIGHTS RESERVED
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

import impresario.IModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.util.EventObject;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//===============================================================
public class SearchModifyVendorView extends View
{
	// GUI components
	protected JTextField vNameField;
	protected JTextField phoneNumberField;
	
	protected JButton submitButton;
	protected JButton cancelButton;

	// For showing error message
	protected MessageView statusLog;

	//----------------------------------------------------------
	public SearchModifyVendorView(IModel transaction)
	{
		super(transaction, "SearchModifyVendorView");

		// set the layout for this panel
		setLayout( new BorderLayout () );

		// create our GUI components, add them to this panel
		add( createTitle(), BorderLayout.NORTH );
		add( createForm(), BorderLayout.CENTER );

		// Error message area, even if don't expect it to be used
		add( createStatusLog("                          "), BorderLayout.SOUTH );

		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("VendorCollectionMessage", this);

		populateFields();
	}

	// Override the paint method to ensure we can set the focus when made visible
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
		return formatViewTitle ("SEARCH FOR A VENDOR TO MODIFY");
	}

	//---------------------------------------------------------------------
	// Create the actual form that allows the user to input the new data
	// needed to search
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
		entryFieldsPanel.setLayout( new BoxLayout ( entryFieldsPanel,
													BoxLayout.Y_AXIS ));

		JPanel searchInfoPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		searchInfoPanel.setBackground( blue );

		JLabel searchInfo =
			new JLabel("Leave all the fields empty to list all vendors");
		searchInfo.setFont(new Font("Arial", Font.BOLD, 16));
		searchInfo.setForeground(Color.blue);
		searchInfoPanel.add(searchInfo);
		searchInfoPanel.setAlignmentY(LEFT_ALIGNMENT);
		entryFieldsPanel.add(searchInfoPanel);

		JPanel vNamePanel = new JPanel();
		vNamePanel.setLayout( new FlowLayout( FlowLayout.LEFT ));
		vNamePanel.setBackground( blue );

		vNameField = new JTextField(16);
		vNameField.addActionListener(this);
		vNamePanel.add( formatCurrentPanel("Vendor Name:", vNameField));

		vNameField.addActionListener(this);

		entryFieldsPanel.add(vNamePanel);

		JPanel phoneNumberPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		phoneNumberPanel.setBackground( blue );

		phoneNumberField = new JTextField(16);
		phoneNumberField.addActionListener(this);
		phoneNumberPanel.add(formatCurrentPanel("Phone Number:", phoneNumberField));
	
		entryFieldsPanel.add(phoneNumberPanel);

		return entryFieldsPanel;
	}

	// Create the navigation buttons
	//-------------------------------------------------------------
	protected JPanel createNavigationButtons()
	{
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground( blue );

		submitButton = new JButton( "Submit" );
		buttonsPanel.add( formatButtonSmall ( submitButton ));

		cancelButton = new JButton( "Cancel" );
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
		
	}

	//-------------------------------------------------------------
	private void processSearch(String vName, String phoneNumber)
	{
		Properties props = new Properties();

		// If the data is not provided, do not pass it to the ScoutCollection
		// First check for item type name
		if ((vName != null) && (vName.length() > 0))
		{
			props.setProperty("Name", vName);
		}
		
		// then check for barcode prefix
		if ((phoneNumber != null) && (phoneNumber.length() > 0))
		{
			props.setProperty("PhoneNumber", phoneNumber);
		}
		
		myModel.stateChangeRequest("SearchVendor", props);
	}

	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
		// Always clear the status log for a new action
		// If update status has already retrieved, do not clear it.

		clearErrorMessage();

		if ((evt.getSource() == submitButton) || (evt.getSource() == vNameField) ||
				(evt.getSource() == phoneNumberField))
		{
			String vNameEntered = vNameField.getText();
			String phoneNumberEntered = phoneNumberField.getText();
			
			processSearch(vNameEntered, phoneNumberEntered);
		}
		else
		if (evt.getSource() == cancelButton)
		{
			// Call method in Clerk to exit system
			myModel.stateChangeRequest("Cancel", null);
		}
		
	}


	//-------------------------------------------------------------
	public void processListSelection(EventObject evt)
	{
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

	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// Nothing found
		if(key.equals("VendorCollectionMessage"))
		{
			displayErrorMessage((String) value);
		}

}
	
}

