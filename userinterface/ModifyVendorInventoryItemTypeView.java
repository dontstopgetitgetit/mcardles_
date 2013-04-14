//************************************************************
//    COPYRIGHT 2013, Sandeep Mitra, Andrew Allen, Kevin Kelly and Students, 
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.Properties;
import java.util.Vector;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import exception.InvalidPrimaryKeyException;

import model.Vendor;
import model.VendorCollection;

// project imports
import impresario.IModel;

//==============================================================
public class ModifyVendorInventoryItemTypeView extends AddVendorInventoryItemTypeView
{
	
	
	//----------------------------------------------------------------------------------
	public ModifyVendorInventoryItemTypeView(IModel transaction)
	{
		super(transaction);
		
	}
	
	//----------------------------------------------------------------
	// Create this View Title with the string specified and formatted
	// to this program standard in the superclass View
	//-----------------------------------------------------------------
	protected JPanel createTitle()
	{		
		return formatViewTitle ("MODIFY A VENDOR INVENTORY ITEM TYPE");
	}
	

	//-------------------------------------------------------------
	protected void populateFields()
	{
		
		String Name = (String) myModel.getState("VendorName");
		receivedVendorId = (String) myModel.getState("VendorId");
		vendorName.setText(Name);
		vendorName.setEditable(false);
		vendorItemName.setText((String) myModel.getState("InventoryItemTypeName"));
		vendorItemName.setEditable(false);
		vendorCurPrice.setText((String) myModel.getState("CurrentPrice"));
		String puIndicator = (String)myModel.getState("PriceUnitIndicator");
		priceUnitIndicator.setSelectedItem(puIndicator);
		vendorDescription.setText((String) myModel.getState("VendorsDescription"));
		vendorsItemNumber.setText((String) myModel.getState("VendorsItemNumber"));
		//dateOfLastUpdate.setText("-1");
	}
	

	
	//----------------------------------------------------------	
	public void updateState(String key, Object value)
	{
		if(key.equals("VendorItemTypeAlreadyExistsMessage") == true)
		{
			String message = (String)value;
			if (message.startsWith("ERROR") == false)
			{
				displayMessage(message);
			}
			else
			{
				JOptionPane.showMessageDialog(MainFrame.getInstance(), message, 
						"Vendor Pricing information does not exist", JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		if(key.equals("VendorItemTypeAddedMessage") == true)
		{
			String message = (String)value;
			if (message.startsWith("ERROR") == false)
			{
				displayMessage(message);
				
			}
			else
			{
				displayErrorMessage((String)value);
			}
			
		}
	}
	
}
