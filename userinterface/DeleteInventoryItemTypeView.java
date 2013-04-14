//************************************************************
// COPYRIGHT 2013, Sandeep Mitra, Ed Mallow and Students
// The College at Brockport - ALL RIGHTS RESERVED
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

//project imports
import impresario.IModel;

//========================================================================
public class DeleteInventoryItemTypeView extends AddInventoryItemTypeView{

	public DeleteInventoryItemTypeView(IModel transaction)
	{
		super(transaction);
		
		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("ItemTypeDeletedMessage", this);	
	}
	
	//----------------------------------------------------------------
	// Create this View Title with the string specified and formatted
	// to this program standard in the superclass View
	//-----------------------------------------------------------------
	protected JPanel createTitle()
	{		
		return formatViewTitle ("DELETE AN INVENTORY ITEM TYPE");
	}

	// Create the navigation buttons
	//-------------------------------------------------------------
	protected JPanel createNavigationButtons()
	{
		JPanel buttonsPanel = new JPanel();	
		buttonsPanel.setBackground( blue );
		
		// create "raw" JButtons and call superclass View to format 
		// the buttons to the program's standard, add them to the panel
		submitButton = new JButton( "Confirm Delete" );
		
		buttonsPanel.add( formatButtonSmall ( submitButton ));

		cancelButton = new JButton( "Main Menu" );
		buttonsPanel.add( formatButtonSmall ( cancelButton ));
		
		return buttonsPanel;
	}
	
	//-------------------------------------------------------------
	protected void populateFields()
	{
		itemTypeName.setText((String)myModel.getState("ItemTypeName"));
		itemTypeName.setEditable(false);
		barcodePrefix.setText((String)myModel.getState("BarcodePrefix"));
		barcodePrefix.setEditable(false);
		units.setText((String)myModel.getState("Units"));
		units.setEditable(false);
		unitsMeasure.setText((String)myModel.getState("UnitMeasure"));
		unitsMeasure.setEditable(false);
		reorderPoint.setText((String)myModel.getState("ReorderPoint"));
		reorderPoint.setEditable(false);
		validityDays.setText((String)myModel.getState("ValidityDays"));
		validityDays.setEditable(false);
		notes.setText((String)myModel.getState("Notes"));
		notes.setEditable(false);
		String ageSensitiveStr = (String)myModel.getState("AgeSensitive");
		if (ageSensitiveStr.startsWith("Yes") == true)
		{
				ageSensitiveYes.setSelected(true);
				ageSensitiveYes.setEnabled(false);
				ageSensitiveNo.setEnabled(false);
		}
		else
		{
			ageSensitiveNo.setSelected(true);
			ageSensitiveYes.setEnabled(false);
			ageSensitiveNo.setEnabled(false);
		}
		
		//units.requestFocus();
	}
		
	//------------------------------------------------------------------
	public void processAction(EventObject evt)
	{
		clearErrorMessage();
		
		/*if(evt.getSource() == barcodePrefix)
		{
			// DO NOTHING HERE, AS REQUIRED BY SEQUENCE DIAGRAM
		}
		else*/
		if(evt.getSource() == submitButton)
		{
			processData();
		}
		else
		if (evt.getSource() == cancelButton)
		{
			// Call method in Clerk to exit system
			myModel.stateChangeRequest("Cancel", null);
		}
	}
	
	//-----------------------------------------------------------------------
	public void updateState(String key, Object value)
	{
		if(key.equals("ItemTypeAlreadyExistsMessage") == true)
		{
			String message = (String)value;
			if (message.startsWith("ERROR") == false)
			{
				displayMessage(message);
			}
			else
			{
				JOptionPane.showMessageDialog(MainFrame.getInstance(), message, 
						"Item Type name Does Not Exist", JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		if(key.equals("ItemTypeDeletedMessage") == true)
		{
			String message = (String)value;
			if (message.startsWith("ERROR") == false)
			{
				displayMessage(message);
				submitButton.setEnabled(false);
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
