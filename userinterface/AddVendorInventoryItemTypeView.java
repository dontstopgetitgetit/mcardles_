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
public class AddVendorInventoryItemTypeView extends View
{
	// GUI components
	protected JTextField vendorName;
	//protected JTextField vendorID;
	protected JTextField vendorItemName;
	protected JTextField vendorCurPrice;
	protected JComboBox priceUnitIndicator;
	protected JTextField vendorDescription;
	protected JTextField vendorsItemNumber;
	protected JLabel vendorIDLabel;
	
	//protected JTextField dateOfLastUpdate;

	protected JButton submitButton;
	protected JButton cancelButton;
		
	// For showing error message
	protected MessageView statusLog;
	
	private Vector vendorCollection;
	
	protected String receivedVendorId = "";
	
	//----------------------------------------------------------------------------------
	public AddVendorInventoryItemTypeView(IModel transaction)
	{
		super(transaction, "AddVendorInventoryItemTypeView");
		
		// set the layout for this panel
		setLayout( new BorderLayout () );

		// create our GUI components, add them to this panel
		add( createTitle(), BorderLayout.NORTH );
		add( createForm(), BorderLayout.CENTER ); 

		// Error message area, even if don't expect it to be used
		add( createStatusLog("                                     "), BorderLayout.SOUTH );

		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("VendorItemTypeAddedMessage", this);
		myModel.subscribe("VendorItemTypeAlreadyExistsMessage", this);
		
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
		return formatViewTitle ("ADD A VENDOR INVENTORY ITEM TYPE");
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
		
		JPanel vendorIdInfoPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		vendorIdInfoPanel.setBackground( blue );	
		
		vendorName = new JTextField();
		
		//vendorIDLabel = new JLabel("Vendor ID:");
		
		//vendorID = new JTextField(16);
		//vendorID.addActionListener(this);
		//vendorID.addFocusListener(this);

		//vendorID.setEditable(false);
		vendorName.setEditable(false);
		vendorIdInfoPanel.add(formatCurrentPanelLarge("Vendor Name: ", vendorName));
		entryFieldsPanel.add(vendorIdInfoPanel);
	   
		JPanel vendorItemNamePanel = new JPanel(new FlowLayout( FlowLayout.LEFT));
		vendorItemNamePanel.setBackground( blue );	
		
		vendorItemName = new JTextField(16);
		vendorItemName.setEditable(false);
		

		vendorItemNamePanel.add(formatCurrentPanelLarge("Vendor Item Name:",  vendorItemName));
		entryFieldsPanel.add(vendorItemNamePanel);
			
		JPanel currentPricePanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		currentPricePanel.setBackground( blue );	
		
		vendorCurPrice = new JTextField(16);
		
		currentPricePanel.add( formatMoneyPanelLarge ( "Vendor Current Price:", vendorCurPrice ) );			
		entryFieldsPanel.add(currentPricePanel);
		
		JPanel priceUnitPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		priceUnitPanel.setBackground( blue );	
		
		String [] priceUnitIndicatorString = { "PerUnit", "Gross" };
		
		priceUnitIndicator = new JComboBox(priceUnitIndicatorString);
		
		priceUnitPanel.add( formatCurrentPanelLarge ( "Price Unit Indicator:", priceUnitIndicator ) );			
		entryFieldsPanel.add(priceUnitPanel);
		
		JPanel descriptionPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		descriptionPanel.setBackground( blue );	
		
		vendorDescription = new JTextField(16);
	
		descriptionPanel.add( formatCurrentPanelLarge ( "Vendor Description:", vendorDescription));
		entryFieldsPanel.add(descriptionPanel);
		
		JPanel vendorsItemNumberPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		vendorsItemNumberPanel.setBackground( blue );	
		
		vendorsItemNumber = new JTextField(16);
	
		vendorsItemNumberPanel.add( formatCurrentPanelLarge ( "Vendors Item Number:", vendorsItemNumber));
		entryFieldsPanel.add(vendorsItemNumberPanel);
		
		return entryFieldsPanel;
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
		Properties stuff = (Properties) myModel.getState("vProps");
		String vId = stuff.getProperty("vId");
		String iName = stuff.getProperty("iName");
		String vName = stuff.getProperty("vName");
		
		receivedVendorId = vId;
		//System.out.println(vId);
		vendorName.setText(vName);
		vendorItemName.setText(iName);
		vendorCurPrice.setText("");
		priceUnitIndicator.setSelectedIndex(0);
		vendorDescription.setText("");
		vendorsItemNumber.setText("");
		//dateOfLastUpdate.setText("-1");
		
	}
	
	
	//-------------------------------------------------------------
	protected void processData()
	{
		Properties props = new Properties();
		
		props.setProperty("VendorId", receivedVendorId);
		props.setProperty("InventoryItemTypeName", vendorItemName.getText());
		props.setProperty("CurrentPrice", vendorCurPrice.getText());
		props.setProperty("PriceUnitIndicator", priceUnitIndicator.getSelectedItem().toString());
		props.setProperty("VendorsDescription", vendorDescription.getText());
		props.setProperty("VendorsItemNumber", vendorsItemNumber.getText());
		Date currentDate = new Date();
		String dateFormat = new SimpleDateFormat("yyyy/MM/dd").format(currentDate);
		// DEBUG System.out.println(dateFormat);
		props.setProperty("DateOfLastUpdate", dateFormat);
		
		myModel.stateChangeRequest("SaveVendorItemType", props);
	}
	
	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
		clearErrorMessage();
		
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
						"Vendor Item Type name Already Exists", JOptionPane.ERROR_MESSAGE);
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
