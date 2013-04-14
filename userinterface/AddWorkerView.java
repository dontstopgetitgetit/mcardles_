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
public class AddWorkerView extends View
{
	// GUI components
	protected JTextField userName;
	protected JPasswordField workerPassword;
	protected JComboBox credentials;

	protected JButton submitButton;
	protected JButton cancelButton;
	
	private String[] credValues = {"Ordinary", "Administrator"};
	
	// For showing error message
	protected MessageView statusLog;
	
	//----------------------------------------------------------------------------------
	public AddWorkerView(IModel transaction)
	{
		super(transaction, "AddWorkerView");
		
		// set the layout for this panel
		setLayout( new BorderLayout () );

		// create our GUI components, add them to this panel
		add( createTitle(), BorderLayout.NORTH );
		add( createForm(), BorderLayout.CENTER ); 

		// Error message area, even if don't expect it to be used
		add( createStatusLog("                                     "), BorderLayout.SOUTH );

		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("WorkerAddedMessage", this);
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
		return formatViewTitle ("ADD A WORKER");
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
		
		JPanel userNamePanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		userNamePanel.setBackground( blue );	
		
		userName = new JTextField(16);
		userName.addActionListener(this);
		userName.addFocusListener(this);
		userNamePanel.add(formatCurrentPanel("Worker Name:", userName));
		entryFieldsPanel.add(userNamePanel);	
		
		JPanel pwdPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		pwdPanel.setBackground( blue );	
		
		workerPassword = new JPasswordField(16);
		pwdPanel.add(formatCurrentPanel("Password:", workerPassword));
		entryFieldsPanel.add(pwdPanel);	
		
		JPanel credentialsPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		credentialsPanel.setBackground( blue );	
		
		credentials = new JComboBox(credValues);
		credentialsPanel.add( formatCurrentPanel ( "Credentials:", credentials ) );	
		credentials.setPreferredSize(new Dimension(110, 25));
		credentials.setMaximumSize(new Dimension(110, 25));
		
		entryFieldsPanel.add(credentialsPanel);
		
		return entryFieldsPanel;
	}
	 
	// Depending on the mode, makes fields editable or not. 
	//-------------------------------------------------------------
	protected void switchFields(boolean mode)
	{
		if(mode == true)
		{
			userName.setEnabled(true);
			workerPassword.setEnabled(true);
			credentials.setEnabled(true);
			
			userName.setFocusable(true);
			workerPassword.setFocusable(true);
			credentials.setFocusable(true);
		}
		
		else
		{
			userName.setEnabled(false);
			workerPassword.setEnabled(false);
			credentials.setEnabled(false);
			
			userName.setFocusable(false);
			workerPassword.setFocusable(false);
			credentials.setFocusable(false);
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
		userName.setText("");
		workerPassword.setText("");
		credentials.setSelectedItem("Ordinary");
		userName.requestFocus();
	}
	
	//-------------------------------------------------------------
	protected boolean getAndValidateData() 
	{		
		String unEntered = userName.getText();
		String pwdEntered = workerPassword.getText();
		
		if(unEntered == null || unEntered.equals(""))
		{
			displayErrorMessage("User name cannot be empty!");
			userName.requestFocus(true);
			return false;
		}
		else
		if(pwdEntered == null || pwdEntered.equals(""))
		{
			displayErrorMessage("You must provide a password!");
			workerPassword.requestFocus(true);
			return false;
		}
		
		return  true;
	}
	
	//-------------------------------------------------------------
	protected void processData(String un, String pwd, String cred)
	{
		Properties props = new Properties();
		
		props.setProperty("Name", un);
		props.setProperty("WorkerPassword", pwd);
		props.setProperty("Credentials", cred);
		
		myModel.stateChangeRequest("SaveWorker", props);
	}
	
	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
		clearErrorMessage();
		
		if(evt.getSource() == userName)
		{
			String unEntered = userName.getText();
			
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
				String unEntered = userName.getText();
				String pwdEntered = workerPassword.getText();
				String credChosen = (String)credentials.getSelectedItem();
				
				processData(unEntered, pwdEntered, credChosen);
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
				userName.setEditable(false);
			}
			else
			{
				JOptionPane.showMessageDialog(MainFrame.getInstance(), message, 
						"Worker name Already Exists", JOptionPane.ERROR_MESSAGE);
				userName.setText("");
				workerPassword.setText("");
				credentials.setSelectedItem("Ordinary");
				userName.setEditable(true);
			}
		}
		else
		if(key.equals("WorkerAddedMessage") == true)
		{
			String message = (String)value;
			if (message.startsWith("ERROR") == false)
			{
				displayMessage(message);
				userName.setText("");
				workerPassword.setText("");
				credentials.setSelectedItem("Ordinary");
				userName.setEditable(true);
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
