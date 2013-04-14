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
public class  DeleteVendorInventoryItemTypeView extends AddVendorInventoryItemTypeView
{

	//----------------------------------------------------------------------------------
	public DeleteVendorInventoryItemTypeView(IModel transaction)
	{
		super(transaction);
	}
	
	
	//----------------------------------------------------------------
	// Create this View Title with the string specified and formatted
	// to this program standard in the superclass View
	//-----------------------------------------------------------------
	protected JPanel createTitle()
	{		
		return formatViewTitle ("DELETE A VENDOR INVENTORY ITEM TYPE");
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
		
		String Name = (String) myModel.getState("VendorName");
		receivedVendorId = (String) myModel.getState("VendorId");
		vendorName.setText(Name);
		vendorName.setEditable(false);
		vendorItemName.setText((String) myModel.getState("InventoryItemTypeName"));
		vendorItemName.setEditable(false);
		vendorCurPrice.setText((String) myModel.getState("CurrentPrice"));
		vendorCurPrice.setEditable(false);
		String puIndicator = (String)myModel.getState("PriceUnitIndicator");
		priceUnitIndicator.setSelectedItem(puIndicator);
		priceUnitIndicator.setEnabled(false);
		vendorDescription.setText((String) myModel.getState("VendorsDescription"));
		vendorDescription.setEditable(false);
		vendorsItemNumber.setText((String) myModel.getState("VendorsItemNumber"));
		vendorsItemNumber.setEditable(false);
		//dateOfLastUpdate.setText("-1");
	}
	
	
	//-------------------------------------------------------------
	protected void processData()
	{
		Properties props = new Properties();
		
		// MITRA 4/6/13
		// WHY SEND ANYTHING? ALL YOU NEED IS SAY DELETE THE EXISTING VENDOR
		myModel.stateChangeRequest("DeleteVendorInventoryItemType", "");
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
				submitButton.setEnabled(false);
				
			}
			else
			{
				displayErrorMessage((String)value);
			}
			
		}
	}
	
}
