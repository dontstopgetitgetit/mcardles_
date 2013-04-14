// tabs=4
//************************************************************
//	COPYRIGHT 2010 Sandeep Mitra and students, The
//    College at Brockport, State University of New York. - 
//	  ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot 
// be reproduced, copied, or used in any shape or form without 
// the express written consent of The College at Brockport.
//************************************************************
//
// specify the package
package userinterface;

// system imports
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;
import java.util.EventObject;
import java.util.Properties;

// project imports
import common.PropertyFile;
import impresario.IModel;

/** The class containing the Login View  for the Comm Dept. application */
//==============================================================
public class LoginView extends View
{

	// GUI stuff
	private JButton loginButton;
	private JButton cancelButton;
	
	private JTextField userName;
	private JPasswordField password;

	// For showing error message
	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public LoginView( IModel sec)
	{

		super(sec, "LoginView");
		
		setBackground ( blue );
		
		// set the layout for this panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// create our GUI components, add them to this panel
		add(createTitle());
		add(createDataEntryFields());
		add(createNavigationButtons());

		// Error message area
		add(createStatusLog("                                     "));
		
		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("LoginError", this);
		
		// populateFields();

	}

	// Overide the paint method to ensure we can set the focus when made visible
	//-------------------------------------------------------------
	public void paint(Graphics g)
	{
		super.paint(g);
		userName.requestFocus();

	}

	// Create the labels and fields
	//-------------------------------------------------------------
	protected JPanel createTitle()
	{
		return formatViewTitle ( "LOGIN" );
	}

	// Create the main data entry fields
	//-------------------------------------------------------------
	private JPanel createDataEntryFields()
	{
		JPanel temp = new JPanel();
		// set the layout for this panel
		temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
		temp.setBackground( blue );

		userName = new JTextField(25);
		temp.add(formatCurrentPanelCenter("Username", userName));

		password = new JPasswordField(25);
		password.addActionListener(this);
		temp.add(formatCurrentPanelCenter("Password", password));
		
		temp.add(Box.createRigidArea(new Dimension(200,50)));
		
		return temp;
	}

	// Create the navigation buttons
	//-------------------------------------------------------------
	private JPanel createNavigationButtons()
	{
		JPanel temp = new JPanel(new FlowLayout(FlowLayout.CENTER));		// default FlowLayout is fine
		temp.setBackground(blue);
		
		// create the buttons, listen for events, add them to the panel
		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		temp.add(formatButton(loginButton));
		
		temp.add(new JLabel("                "));
		
		cancelButton = new JButton("Exit Application");
		cancelButton.addActionListener(this);
		temp.add(formatButton(cancelButton));

		return temp;
	}

	// Create the status log field
	//-------------------------------------------------------------
	private JPanel createStatusLog(String initialMessage)
	{

		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	/**
	 *
	 * This simply checks to see that the Strings are in proper
	 * FORMAT - i.e., not empty
	 */
	//-------------------------------------------------------------
	private boolean validate(String userName, String password)
	{
		if ((userName == null) || (userName.length() == 0))
		{
			displayErrorMessage("Please enter a valid user name!");
			return false;
		}
		if ((password == null) || (password.length() == 0))
		{
			displayErrorMessage("Please enter a valid password!");
			return false;
		}
		
		return true;
	}

	// IMPRESARIO: Note how we use this method name instead of 'actionPerformed()'
	// now. This is because the super-class View has methods for both action and
	// focus listeners, and both of them delegate to this method. So this method
	// is called when you either have an action (like a button click) or a loss
	// of focus (like tabbing out of a textfield, moving your cursor to something
	// else in the view, etc.)
	// process events generated from our GUI components
	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
//		DEBUG: System.out.println("LoginView.processAction()");

		// Always clear the status log for a new action
		clearErrorMessage();

		if (evt.getSource() == cancelButton)
		{
			myModel.stateChangeRequest("ExitSystem", "");
		}
		else
		if ((evt.getSource() == loginButton) || (evt.getSource() == password) ||
				(evt.getSource() == userName))
		{
			// STEP 1: Gather and VALIDATE user entered data
			String uName = userName.getText();
			char[] pwdChars = password.getPassword();
			
			String pwd = "";
			if (pwdChars != null)
			{
				pwd = new String(pwdChars);
			}
			boolean flag = validate(uName, pwd);
			if (flag == true)
			{
				// STEP 1.1: Create the OBJECT in which you 
				// will send data over to the model.
				Properties props = new Properties();
				props.setProperty("Name", uName);
				props.setProperty("Password", pwd);
				
				userName.setText("");
				password.setText("");
				
				// Call the method in the Model (Secretary) to process the login data,
				// sending the Properties object above as an actual parameter with it
				// The view's registry makes sure it gets to the right model who has
				// subscribed to it.
				myModel.stateChangeRequest("Login", props);
			}
			
		}
		
	}

	/**
	 * Update view methods - methods invoked to call the view back when something
	 * needs to be updated on it. For example, if you want to update the message
	 * field with a "Login Error" message if the password and user name don't match.
	 * Should the method(s) written here follow the Observer pattern?
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// STEP 6: Be sure to finish the end of the 'perturbation'
		// by indicating how the view state gets updated.
		if (key.equals("LoginError") == true)
		{
			// display the passed text
			displayErrorMessage((String)value);
		}
		
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
