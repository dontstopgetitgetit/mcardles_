//************************************************************
//    COPYRIGHT 2013, Sandeep Mitra, Jason La Mendola and Students, 
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
public class BrowseInventoryExceptionsView extends View
{
	static final int YEAR_OF_FIRST_DEPLOYMENT = 2013;
	
	// GUI components
	private JComboBox monthCBox;
	private JComboBox dayCBox;
	private JComboBox yearCBox;
	
	private Vector years;
	private String [] months = new String [13];
	private	String [] days = new String [32];
	private String theDate;
	private boolean searchAll;
	
	protected JButton submitButton;
	protected JButton cancelButton;
	

	// For showing error message
	protected MessageView statusLog;

	//----------------------------------------------------------
	public BrowseInventoryExceptionsView(IModel transaction)
	{
		super(transaction, "BrowseInventoryExceptionsView");

		// set the layout for this panel
		setLayout( new BorderLayout () );

		// create our GUI components, add them to this panel
		add( createTitle(), BorderLayout.NORTH );
		add( createForm(), BorderLayout.CENTER );

		// Error message area, even if don't expect it to be used
		add( createStatusLog("                          "), BorderLayout.SOUTH );

		// STEP 0: Be sure you tell your model what keys you are interested in
		myModel.subscribe("InventoryExceptionSearchMessage", this);

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
		return formatViewTitle ("BROWSE INVENTORY EXCEPTIONS");
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
			new JLabel("Leave all fields empty to list all inventory exceptions.");
		searchInfo.setFont(new Font("Arial", Font.BOLD, 16));
		searchInfo.setForeground(Color.blue);
		searchInfoPanel.add(searchInfo);
		searchInfoPanel.setAlignmentY(LEFT_ALIGNMENT);
		entryFieldsPanel.add(searchInfoPanel);
		
		fillComboBoxes();

		monthCBox = new JComboBox(months);
		monthCBox.addActionListener(this);

		dayCBox = new JComboBox(days);
		dayCBox.addActionListener(this);

		yearCBox = new JComboBox(years);
		yearCBox.addActionListener(this);

		JPanel sessionDatePanel = new BluePanel();
		sessionDatePanel.add( formatCurrentPanel ("Date:", monthCBox, dayCBox, yearCBox ));
		entryFieldsPanel.add(sessionDatePanel);

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
	
	//-------------------------------------------------------------
	private void fillComboBoxes()
	{
		// Get current year
		Calendar calendar = Calendar.getInstance();
		int thisYear = calendar.get(Calendar.YEAR);
		
		years = new Vector<String>();

		years.insertElementAt("YYYY", 0);
		months[0] = "MM";
		days[0] = "DD";

		int cnt = 1;
		while(thisYear >= YEAR_OF_FIRST_DEPLOYMENT) {
			years.insertElementAt(Integer.toString(thisYear--), cnt++);
		}

		cnt = 1;
		for(int i = 0; i < 12; i++) {
			months[i+1] = mapMonthToString(i);
		}

		cnt = 1;
		for(int i = 1; i < 10; i++) {
			days[i] = "0" + Integer.toString(cnt++);
		}

		cnt = 10;
		for(int i = 10; i < 32; i++) {
			days[i] = Integer.toString(cnt++);
		}
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
	private void processSearch()
	{
		Properties props = new Properties();

		if (searchAll)
		{
			//props.setProperty("ExceptionDate", "");
		}
		else 
		{
			props.setProperty("ExceptionDate", theDate);
		}
		
		myModel.stateChangeRequest("SearchExceptions", props);
	}
	
	//-------------------------------------------------------------
	private boolean validateCalendarData()
	{
		String month = (String)monthCBox.getSelectedItem();

		if(month.equals("January") )
			month = "01";
		else if(month.equals("February") )
			month = "02";
		else if(month.equals("March") )
			month = "03";
		else if(month.equals("April") )
			month = "04";
		else if(month.equals("May") )
			month = "05";
		else if(month.equals("June") )
			month = "06";
		else if(month.equals("July") )
			month = "07";
		else if(month.equals("August") )
			month = "08";
		else if(month.equals("September") )
			month = "09";
		else if(month.equals("October") )
			month = "10";
		else if(month.equals("November") )
			month = "11";
		else if(month.equals("December") )
			month = "12";
		
		theDate = (String) yearCBox.getSelectedItem() + "/" +
				month + "/" +
				(String) dayCBox.getSelectedItem();
		
		//Checks if all fields were blank - if so, search -all- exceptions
		if (theDate.contains("YYYY") && theDate.contains("MM") && theDate.contains("DD"))
		{
			searchAll = true;
			return true;
		}
		//If one of the boxes is filled in but others are not, give an error as this is not allowed.
		else if (theDate.contains("YYYY") || theDate.contains("MM") || theDate.contains("DD"))
		{
			displayErrorMessage("Date not selected properly! (Must all be blank or all be filled in)");
			return false;
		}
		//Date accepted, but not searching all exceptions.
		searchAll = false;
		return true;
	}

	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
		// Always clear the status log for a new action
		// If update status has already retrieved, do not clear it.

		clearErrorMessage();

		if ((evt.getSource() == submitButton) )
		{
			if(validateCalendarData())
			{
				processSearch();
			}			
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
		if(key.equals("InventoryExceptionSearchMessage"))
		{
			displayErrorMessage((String) value);
		}
		
	}

}