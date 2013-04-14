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
public class DeleteWorkerView extends ModifyWorkerView
{

	
	//----------------------------------------------------------------------------------
	public DeleteWorkerView(IModel transaction)
	{
		super(transaction);

		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("WorkerDeletedMessage", this);
	}
	
	
	//----------------------------------------------------------------
	// Create this View Title with the string specified and formatted
	// to this program standard in the superclass View
	//-----------------------------------------------------------------
	protected JPanel createTitle()
	{		
		return formatViewTitle ("DELETE A WORKER");
	}
	
	// Create the navigation buttons
	//-------------------------------------------------------------
	protected JPanel createNavigationButtons()
	{
		JPanel buttonsPanel = new JPanel();	
		buttonsPanel.setBackground( blue );
		
		// create "raw" JButtons and call superclass View to format 
		// the buttons to the program's standard, add them to the panel
		submitButton = new JButton( "Delete" );
		submitButton.setEnabled(false);
		buttonsPanel.add( formatButtonSmall ( submitButton ));

		cancelButton = new JButton( "Main Menu" );
		buttonsPanel.add( formatButtonSmall ( cancelButton ));
		
		return buttonsPanel;
	}
		
	//----------------------------------------------------------	
	public void updateState(String key, Object value)
	{
		// DEBUG System.out.println("DeleteWorkerView.updateState(): key = " + key + "; value = " + value);
		
		if(key.equals("NameDoesNotExistMessage") == true)
		{
			String message = (String)value;
			if (message.startsWith("ERROR") == false)
			{
				displayMessage(message);
				submitButton.setEnabled(true);
				switchFields(false);
				submitButton.setText("Confirm Delete");
			}
			else
			{
				JOptionPane.showMessageDialog(MainFrame.getInstance(), message, 
						"Worker name Does Not Exists", JOptionPane.ERROR_MESSAGE);
				
				userName.setText("");
				workerPassword.setText("");
				credentials.setSelectedItem("Ordinary");
				switchFields(true);
			}
		}
		else
		if(key.equals("WorkerDeletedMessage") == true)
		{
			String message = (String)value;
			if (message.startsWith("ERROR") == false)
			{
				displayMessage(message);
				userName.setText("");
				workerPassword.setText("");
				credentials.setSelectedItem("Ordinary");
				switchFields(true);
				submitButton.setEnabled(false);
			}
			else
			{
				displayErrorMessage((String)value);
			}
			
		}
		else
			super.updateState(key, value);
	}
}
