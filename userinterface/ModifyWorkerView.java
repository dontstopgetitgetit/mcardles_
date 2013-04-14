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
public class ModifyWorkerView extends AddWorkerView
{

	
	//----------------------------------------------------------------------------------
	public ModifyWorkerView(IModel transaction)
	{
		super(transaction);
	
		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("WorkerModifiedMessage", this);
		myModel.subscribe("Name", this);
		myModel.subscribe("WorkerPassword", this);
		myModel.subscribe("Credentials", this);
		myModel.subscribe("NameDoesNotExistMessage", this);
		
	}
	
	
	//----------------------------------------------------------------
	// Create this View Title with the string specified and formatted
	// to this program standard in the superclass View
	//-----------------------------------------------------------------
	protected JPanel createTitle()
	{		
		return formatViewTitle ("MODIFY A WORKER");
	}
	
	//----------------------------------------------------------	
	public void updateState(String key, Object value)
	{
		// DEBUG System.out.println("ModifyWorkerView.updateState(): key = " + key + "; value = " + value);
		
		if (key.equals("Name") == true)
		{	
			userName.setText((String)value);
		}
		else
		if (key.equals("WorkerPassword") == true)
		{
			workerPassword.setText((String)value);
		}	
		else
		if (key.equals("Credentials") == true)
		{
			credentials.setSelectedItem((String)value);
		}	
		else
		if(key.equals("NameDoesNotExistMessage") == true)
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
						"Worker name Does Not Exist", JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		if(key.equals("WorkerModifiedMessage") == true)
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
}
