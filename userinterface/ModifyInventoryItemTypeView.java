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
public class ModifyInventoryItemTypeView extends AddInventoryItemTypeView
{
	
	//----------------------------------------------------------------------------------
	public ModifyInventoryItemTypeView(IModel transaction)
	{
		super(transaction);
		
		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("ItemTypeModifiedMessage", this);	
	}
	
	//----------------------------------------------------------------
	// Create this View Title with the string specified and formatted
	// to this program standard in the superclass View
	//-----------------------------------------------------------------
	protected JPanel createTitle()
	{		
		return formatViewTitle ("MODIFY AN INVENTORY ITEM TYPE");
	}
	
	
	//-------------------------------------------------------------
	protected void populateFields()
	{
		itemTypeName.setText((String)myModel.getState("ItemTypeName"));
		barcodePrefix.setText((String)myModel.getState("BarcodePrefix"));
		units.setText((String)myModel.getState("Units"));
		unitsMeasure.setText((String)myModel.getState("UnitMeasure"));
		reorderPoint.setText((String)myModel.getState("ReorderPoint"));
		validityDays.setText((String)myModel.getState("ValidityDays"));
		notes.setText((String)myModel.getState("Notes"));
		String ageSensitiveStr = (String)myModel.getState("AgeSensitive");
		if (ageSensitiveStr.startsWith("Yes") == true)
			ageSensitiveYes.setSelected(true);
		else
			ageSensitiveNo.setSelected(true);
		
		units.requestFocus();
	}
	
	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
		clearErrorMessage();
		
		if(evt.getSource() == barcodePrefix)
		{
			// DO NOTHING HERE, AS REQUIRED BY SEQUENCE DIAGRAM
		}
		else
		if(evt.getSource() == submitButton)
		{
			// MODIFIED MITRA 3/27/13
			if (validateData() == true)
			{
				processData();
			}
			else
			{
				displayErrorMessage(dataValidationErrorMessage);
				dataValidationErrorMessage = "";
			}
		}
		else
		if (evt.getSource() == ageSensitiveYes)
		{
			if (ageSensitiveYes.isSelected() == true)
			{
				validityDays.setText("7");
			}
			else
			{
				validityDays.setText("-1");
			}
		}
		else
		if (evt.getSource() == ageSensitiveNo)
		{
			if (ageSensitiveNo.isSelected() == true)
			{
				validityDays.setText("-1");
			}
			else
			{
				validityDays.setText("7");
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
		if(key.equals("ItemTypeModifiedMessage") == true)
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
