package userinterface;

import impresario.IModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.util.EventObject;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.InventoryItemType;
import model.InventoryItemTypeCollection;
import model.Vendor;
import model.VendorCollection;
import exception.InvalidPrimaryKeyException;

//================================================================
public class SearchVendorInventoryItemTypeView extends View
{
	// GUI components
	protected JComboBox vNameField;
	protected JComboBox inventoryItemTypeNameField;
	
	protected JButton submitButton;
	protected JButton cancelButton;
	
	protected Vector vendorCollection;

	// For showing error message
	protected MessageView statusLog;

	//----------------------------------------------------------
	public SearchVendorInventoryItemTypeView(IModel transaction)
	{
		super(transaction, "SearchVendorInventoryItemTypeView");

		// set the layout for this panel
		setLayout( new BorderLayout () );

		// create our GUI components, add them to this panel
		add( createTitle(), BorderLayout.NORTH );
		add( createForm(), BorderLayout.CENTER );

		// Error message area, even if don't expect it to be used
		add( createStatusLog("                          "), BorderLayout.SOUTH );

		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("VendorItemTypeAlreadyExistsMessage", this);

		populateFields();
	}

	// Override the paint method to ensure we can set the focus when made visible
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
		return formatViewTitle ("SELECT FOR VENDOR PRICING INFORMATION");
	}

	//---------------------------------------------------------------------
	// Create the actual form that allows the user to input the new data
	// needed to search
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
		entryFieldsPanel.setLayout( new BoxLayout ( entryFieldsPanel,
													BoxLayout.Y_AXIS ));

		JPanel searchInfoPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		searchInfoPanel.setBackground( blue );

		JLabel searchInfo =
			new JLabel("Select a vendor name and item type name");
		searchInfo.setFont(new Font("Arial", Font.BOLD, 16));
		searchInfo.setForeground(Color.blue);
		searchInfoPanel.add(searchInfo);
		searchInfoPanel.setAlignmentY(LEFT_ALIGNMENT);
		entryFieldsPanel.add(searchInfoPanel);

		JPanel vNamePanel = new JPanel();
		vNamePanel.setLayout( new FlowLayout( FlowLayout.LEFT ));
		vNamePanel.setBackground( blue );

		vNameField = new JComboBox();
		
		vNamePanel.add( formatCurrentPanel("Vendor Name:", vNameField));

		entryFieldsPanel.add(vNamePanel);

		JPanel inventoryItemTypeNamePanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		inventoryItemTypeNamePanel.setBackground( blue );

		inventoryItemTypeNameField = new JComboBox();

		inventoryItemTypeNamePanel.add(formatCurrentPanel("Item Type Name:", inventoryItemTypeNameField));
	
		entryFieldsPanel.add(inventoryItemTypeNamePanel);

		return entryFieldsPanel;
	}

	// Create the navigation buttons
	//-------------------------------------------------------------
	protected JPanel createNavigationButtons()
	{
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground( blue );

		submitButton = new JButton( "Submit" );
		buttonsPanel.add( formatButtonSmall ( submitButton ));

		cancelButton = new JButton( "Cancel" );
		buttonsPanel.add( formatButtonSmall ( cancelButton ));

		return buttonsPanel;
	}

	//-------------------------------------------------------------------
	@SuppressWarnings("deprecation")
	protected void populateFields()
	{
		JFrame mf = MainFrame.getInstance();
		
		mf.setCursor(Cursor.WAIT_CURSOR);
		
		try
		{
			VendorCollection vc = new VendorCollection();
			
			vendorCollection = (Vector) vc.getState("VendorList");
			// DEBUG  System.out.println(vendorCollection.size());
			 for (int cnt = 0; cnt < vendorCollection.size(); cnt++)
			 {
				 Vendor v = (Vendor) vendorCollection.elementAt(cnt);
				 // DEBUG System.out.println(v.getState("Name"));
				
				 String nextName = (String) v.getState("Name");
				
				 vNameField.addItem(nextName);
				
			 }
			 
		}
		catch (InvalidPrimaryKeyException ex1)
		{
			
		}
		
		try {
			InventoryItemTypeCollection iitc = new InventoryItemTypeCollection();
			
			 Vector inventoryCollection = (Vector) iitc.getState("InventoryItemTypeList");
			
			 for (int cnt = 0; cnt < inventoryCollection.size(); cnt++)
			 {
				 InventoryItemType iit = (InventoryItemType) inventoryCollection.elementAt(cnt);
				
				 String nextName = (String) iit.getState("ItemTypeName");
				
				 inventoryItemTypeNameField.addItem(nextName);
			
			 }
			 
		} catch (InvalidPrimaryKeyException e) {
			// TODO Auto-generated catch block
			//Show error message
		}
		mf.setCursor(Cursor.DEFAULT_CURSOR);
		
	}
	// Create the status log field
	//-------------------------------------------------------------
	protected JPanel createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}



	//-------------------------------------------------------------
	private void processSearch(String vId, String vName, String inventoryItemTypeName)
	{
		Properties props = new Properties();

		if ((vId != null) && (vId.length() > 0))
		{
			props.setProperty("VendorID", vId);
		}
		
		if ((vName != null) && (vName.length() > 0))
		{
			props.setProperty("vName", vName);
		}
		
		if ((inventoryItemTypeName != null) && (inventoryItemTypeName.length() > 0))
		{
			props.setProperty("ItemTypeName", inventoryItemTypeName);
		}
		
		myModel.stateChangeRequest("FindVendorInventoryItemType", props);
	}

	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
		// Always clear the status log for a new action
		// If update status has already retrieved, do not clear it.

		clearErrorMessage();

		if ((evt.getSource() == submitButton))
		{
			int vendorIndexSelected =  vNameField.getSelectedIndex();
			Vendor selectedVendor = (Vendor)vendorCollection.elementAt(vendorIndexSelected);
			String vendorId = (String)selectedVendor.getState("Id");
			String vendorName = (String)selectedVendor.getState("Name");
			
			String inventoryItemTypeNameEntered = inventoryItemTypeNameField.getSelectedItem().toString();
			
			processSearch(vendorId, vendorName, inventoryItemTypeNameEntered);
		}
		else
		if (evt.getSource() == cancelButton)
		{
			// Call method in Clerk to exit system
			myModel.stateChangeRequest("Cancel", null);
		}
		
	}


	//-------------------------------------------------------------
	public void processListSelection(EventObject evt)
	{
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

	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// Nothing found
		String message = (String)value;
		if(key.equals("VendorItemTypeAlreadyExistsMessage"))
		{
			if ((message != null) && (message.length() > 0))
				JOptionPane.showMessageDialog(MainFrame.getInstance(), message, 
					"Vendor Pricing data status", JOptionPane.ERROR_MESSAGE);
		}

}
	
}

