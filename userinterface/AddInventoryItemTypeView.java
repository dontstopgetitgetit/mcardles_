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
public class AddInventoryItemTypeView extends View
{
	// GUI components
	protected JTextField itemTypeName;
	protected JTextField barcodePrefix;
	protected JTextField units;
	protected JTextField unitsMeasure;
	protected JTextField reorderPoint;
	protected JRadioButton ageSensitiveYes;
	protected JRadioButton ageSensitiveNo;
	protected JTextField validityDays;
	protected JTextArea notes;
	

	protected JButton submitButton;
	protected JButton cancelButton;
		
	protected String dataValidationErrorMessage = "";
	
	// For showing error message
	protected MessageView statusLog;
	
	//----------------------------------------------------------------------------------
	public AddInventoryItemTypeView(IModel transaction)
	{
		super(transaction, "AddInventoryItemTypeView");
		
		// set the layout for this panel
		setLayout( new BorderLayout () );

		// create our GUI components, add them to this panel
		add( createTitle(), BorderLayout.NORTH );
		add( createForm(), BorderLayout.CENTER ); 

		// Error message area, even if don't expect it to be used
		add( createStatusLog("                                     "), BorderLayout.SOUTH );

		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("ItemTypeAddedMessage", this);
		myModel.subscribe("ItemTypeAlreadyExistsMessage", this);
		
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
		return formatViewTitle ("ADD AN INVENTORY ITEM TYPE");
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
		
		JPanel itemTypeIdInfoPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		itemTypeIdInfoPanel.setBackground( blue );	
		
		itemTypeName = new JTextField(16);
		itemTypeName.addActionListener(this);
		itemTypeName.addFocusListener(this);
		
		barcodePrefix = new JTextField(16);
		
		JLabel barcodePrefixLabel = new JLabel("  Barcode Prefix: ");
		barcodePrefixLabel.setFont( myComponentsFont );
		barcodePrefixLabel.setPreferredSize( sizeLabel );
		
		itemTypeIdInfoPanel.add(formatCurrentPanel("Item Type Name:", itemTypeName, barcodePrefixLabel,
				barcodePrefix));
		entryFieldsPanel.add(itemTypeIdInfoPanel);	
			
		
		JPanel unitsPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		unitsPanel.setBackground( blue );	
		
		units = new JTextField(16);
		
		JLabel unitsMeasureLabel = new JLabel("  Units Measure: ");
		unitsMeasureLabel.setFont( myComponentsFont );
		unitsMeasureLabel.setPreferredSize( sizeLabel );
		
		unitsMeasure = new JTextField(16);
		unitsPanel.add( formatCurrentPanel ( "Units:", units, unitsMeasureLabel, unitsMeasure ) );			
		entryFieldsPanel.add(unitsPanel);
		
		JPanel reorderPointPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		reorderPointPanel.setBackground( blue );	
		
		reorderPoint = new JTextField(16);
		reorderPointPanel.add( formatCurrentPanel ( "Reorder Point:", reorderPoint));			
		entryFieldsPanel.add(reorderPointPanel);
		
		JPanel ageSensitivePanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		ageSensitivePanel.setBackground( blue );	
		
		ageSensitiveYes = new JRadioButton("Yes");
		ageSensitiveYes.setBackground( blue );
		ageSensitiveNo = new JRadioButton("No");
		ageSensitiveNo.setBackground( blue );
		
		ButtonGroup group = new ButtonGroup();
		group.add(ageSensitiveYes);
		group.add(ageSensitiveNo);
		ageSensitiveYes.addActionListener(this);
		ageSensitiveNo.addActionListener(this);
		
		JLabel ageSensitiveLabel = new JLabel("  Age Sensitive: ");
		ageSensitiveLabel.setFont( myComponentsFont );
		ageSensitiveLabel.setPreferredSize( sizeLabel );
		ageSensitivePanel.add(ageSensitiveLabel);
		
		ageSensitivePanel.add(ageSensitiveYes);
		ageSensitivePanel.add(ageSensitiveNo);
		
		entryFieldsPanel.add(ageSensitivePanel);
		
		JPanel validityDaysPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		validityDaysPanel.setBackground( blue );	
		
		validityDays = new JTextField(16);
		validityDaysPanel.add( formatCurrentPanel ( "Validity Days:", validityDays) );			
		entryFieldsPanel.add(validityDaysPanel);
		
		JPanel notesPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		notesPanel.setBackground( blue );	
		
		notes = new JTextArea(5, 10);
		notesPanel.add( formatCurrentPanel ( "Notes:", notes) );			
		entryFieldsPanel.add(notesPanel);
		
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
		itemTypeName.setText("");
		barcodePrefix.setText("");
		units.setText("");
		unitsMeasure.setText("");
		reorderPoint.setText("");
		validityDays.setText("-1");
		notes.setText("");
		ageSensitiveNo.setSelected(true);
		
		itemTypeName.requestFocus();
	}
	
	//-------------------------------------------------------------
	protected boolean validateData()
	{
		String barcodePrefixEntered = barcodePrefix.getText();
		try
		{
			int bp = Integer.parseInt(barcodePrefixEntered);
			String unitsEntered = units.getText();
			try
			{
				int units = Integer.parseInt(unitsEntered);
				String validityDaysEntered = validityDays.getText();
				try
				{
					int vd = Integer.parseInt(validityDaysEntered);
					String reorderPointEntered = reorderPoint.getText();
					try
					{
						// ERROR FIX MITRA 4/4/2013
						double rp = Double.parseDouble(reorderPointEntered);
						// END ERROR FIX MITRA 4/4/2013
						dataValidationErrorMessage = "";
						return true;
					}
					catch (NumberFormatException ex4)
					{
						dataValidationErrorMessage = "ERROR: Reorder point value must be a fractional number";
						return false;
					}
				}
				catch (NumberFormatException ex3)
				{
					dataValidationErrorMessage = "ERROR: Validity days value must be numeric";
					return false;
				}
			}
			catch (NumberFormatException ex2)
			{
				dataValidationErrorMessage = "ERROR: Units value must be numeric";
				return false;
			}
		}
		catch (NumberFormatException ex1)
		{
			dataValidationErrorMessage = "ERROR: Barcode prefix must be numeric";
			return false;
		}
		
	}
	
	//-------------------------------------------------------------
	protected void processData()
	{
		Properties props = new Properties();
		
		props.setProperty("ItemTypeName", itemTypeName.getText());
		props.setProperty("BarcodePrefix", barcodePrefix.getText());
		props.setProperty("Units", units.getText());
		props.setProperty("UnitMeasure", unitsMeasure.getText());
		props.setProperty("ValidityDays", validityDays.getText());
		props.setProperty("ReorderPoint", reorderPoint.getText());
		boolean isNoSelected = ageSensitiveNo.isSelected();
		boolean isYesSelected = ageSensitiveYes.isSelected();
		
		if (isYesSelected == true)
		{
			props.setProperty("AgeSensitive", "Yes");
		}
		else
		{
			props.setProperty("AgeSensitive", "No");
		}
		props.setProperty("Notes", notes.getText());
		
		myModel.stateChangeRequest("SaveItemType", props);
	}
	
	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
		clearErrorMessage();
		
		if(evt.getSource() == itemTypeName)
		{
			String itemTypeNameEntered = itemTypeName.getText();

			if ((itemTypeNameEntered != null) && (itemTypeNameEntered.length() > 0))
			{
				Properties props = new Properties();
				props.setProperty("ItemTypeName", itemTypeNameEntered);
				myModel.stateChangeRequest("FindItemType", props);		
			}
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
						"Item Type name Already Exists", JOptionPane.ERROR_MESSAGE);
				itemTypeName.setText("");
				itemTypeName.requestFocus();
			}
		}
		else
		if(key.equals("ItemTypeAddedMessage") == true)
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
