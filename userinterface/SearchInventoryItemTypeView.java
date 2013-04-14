package userinterface;

// system imports
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Properties;

// project imports
import impresario.IModel;
import model.InventoryItemType;
import model.InventoryItemTypeCollection;

//==============================================================
public class SearchInventoryItemTypeView extends View
{
	// GUI components
	protected JTextField itemTypeNameField;
	protected JTextField barcodePrefixField;
	
	protected JButton submitButton;
	protected JButton cancelButton;

	// For showing error message
	protected MessageView statusLog;

	//----------------------------------------------------------
	public SearchInventoryItemTypeView(IModel transaction)
	{
		super(transaction, "SearchInventoryItemTypeView");

		// set the layout for this panel
		setLayout( new BorderLayout () );

		// create our GUI components, add them to this panel
		add( createTitle(), BorderLayout.NORTH );
		add( createForm(), BorderLayout.CENTER );

		// Error message area, even if don't expect it to be used
		add( createStatusLog("                          "), BorderLayout.SOUTH );

		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("ItemTypeCollectionMessage", this);

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
		return formatViewTitle ("SEARCH FOR AN INVENTORY ITEM TYPE");
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
			new JLabel("Leave all the fields empty to list all item types.");
		searchInfo.setFont(new Font("Arial", Font.BOLD, 16));
		searchInfo.setForeground(Color.blue);
		searchInfoPanel.add(searchInfo);
		searchInfoPanel.setAlignmentY(LEFT_ALIGNMENT);
		entryFieldsPanel.add(searchInfoPanel);

		JPanel itemTypeNamePanel = new JPanel();
		itemTypeNamePanel.setLayout( new FlowLayout( FlowLayout.LEFT ));
		itemTypeNamePanel.setBackground( blue );

		itemTypeNameField = new JTextField(16);
		itemTypeNameField.addActionListener(this);
		itemTypeNamePanel.add( formatCurrentPanel("Item Type Name:", itemTypeNameField));

		itemTypeNameField.addActionListener(this);

		entryFieldsPanel.add(itemTypeNamePanel);

		JPanel barcodePrefixPanel = new JPanel(new FlowLayout( FlowLayout.LEFT ));
		barcodePrefixPanel.setBackground( blue );

		barcodePrefixField = new JTextField(16);
		barcodePrefixField.addActionListener(this);
		barcodePrefixPanel.add(formatCurrentPanel("Barcode Prefix:", barcodePrefixField));
	
		entryFieldsPanel.add(barcodePrefixPanel);

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
		
	}

	//-------------------------------------------------------------
	private void processSearch(String typeName, String barcodePrefix)
	{
		Properties props = new Properties();

		// If the data is not provided, do not pass it to the ScoutCollection
		// First check for item type name
		if ((typeName != null) && (typeName.length() > 0))
		{
			props.setProperty("ItemTypeName", typeName);
		}
		
		// then check for barcode prefix
		if ((barcodePrefix != null) && (barcodePrefix.length() > 0))
		{
			props.setProperty("BarcodePrefix", barcodePrefix);
		}
		
		myModel.stateChangeRequest("SearchItemType", props);
	}

	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
		// Always clear the status log for a new action
		// If update status has already retrieved, do not clear it.

		clearErrorMessage();

		if ((evt.getSource() == submitButton) || (evt.getSource() == itemTypeNameField) ||
				(evt.getSource() == barcodePrefixField))
		{
			String itemTypeNameEntered = itemTypeNameField.getText();
			String barcodePrefixEntered = barcodePrefixField.getText();
			
			processSearch(itemTypeNameEntered, barcodePrefixEntered);
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
		if(((String) value).equals("ItemTypeCollectionMessage"))
		{
			displayErrorMessage((String) value);
		}
		
	}

}
