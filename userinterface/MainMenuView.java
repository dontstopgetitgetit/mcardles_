// tabs=4
//************************************************************
//	COPYRIGHT 2013 Sandeep Mitra and Students, The
//    College at Brockport, State University of New York. -
//	  ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//************************************************************
//
// specify the package
package userinterface;

// system imports
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.util.EventObject;
import java.util.Vector;

// project imports
import impresario.IModel;


/** The class containing the Main Menu View  for the Scout Troop 209 Tree Sales application */
//==============================================================
public class MainMenuView extends View
{

	// GUI stuff
	private JButton cancelButton;

	private JButton processInvoiceButton;
	private JButton completeExistingInvoiceButton;
	private JButton takeItemFromInventoryButton;
	private JButton fullCurrentInventoryButton;
	private JButton obtainReorderPointButton;
	
	// Inventory Item buttons
	private JButton modifyInventoryItemButton;
	private JButton seeItemInvoiceButton;
	
	// Worker functionality buttons
	private JButton addWorkerButton;
	private JButton modifyWorkerButton;
	private JButton deleteWorkerButton;
	
	// Inventory Item Type functionality buttons
	private JButton addItemTypeButton;
	private JButton modifyItemTypeButton;
	private JButton deleteItemTypeButton;
	
	// Inventory Vendor functionality buttons
	private JButton addVendorButton;
	private JButton modifyVendorButton;
	private JButton deleteVendorButton;
	private JButton seeAllVendorItemsButton;
	
	// Vendor Inventory Item Type functionality buttons
	private JButton addVendorInventoryItemTypeButton;
	private JButton modifyVendorInventoryItemTypeButton;
	private JButton deleteVendorInventoryItemTypeButton;
	
	private JButton browseInventoryExceptionsButton;
	
	private JPanel navigationPanel;

	// Other state info needed
	private String loggedinWorkerCredentials;
	
	// For showing error message
	private MessageView statusLog;

	// constructor for this class -- takes a model object
	//----------------------------------------------------------
	public MainMenuView( IModel inventoryMaster )
	{

		super( inventoryMaster, "MainMenuView" );

		setBackground ( blue );

		// set the layout for this panel
		setLayout( new BorderLayout () );

		// create our GUI components, add them to this panel
		add( createTitle(), BorderLayout.NORTH );
		add( createNavigationButtons(), BorderLayout.CENTER );

		// Error message area, even if don't expect it to be used
		add( createStatusLog("                          "), BorderLayout.SOUTH );

		populateFields();

		
	}

	// Overide the paint method to ensure we can set the focus when made visible
	//-------------------------------------------------------------
	public void paint(Graphics g)
	{
		super.paint(g);

	}

	//-----------------------------------------------------------
	private void populateFields()
	{
		loggedinWorkerCredentials = (String)myModel.getState("Credentials");
		if (loggedinWorkerCredentials.startsWith("Ordinary") == true)
		{
			modifyInventoryItemButton.setEnabled(false);
			seeItemInvoiceButton.setEnabled(false);
			processInvoiceButton.setEnabled(false);
			completeExistingInvoiceButton.setEnabled(false);
			addWorkerButton.setEnabled(false);
			modifyWorkerButton.setEnabled(false);
			deleteWorkerButton.setEnabled(false);
			addItemTypeButton.setEnabled(false);
			modifyItemTypeButton.setEnabled(false);
			deleteItemTypeButton.setEnabled(false);
			addVendorButton.setEnabled(false);
			modifyVendorButton.setEnabled(false);
			deleteVendorButton.setEnabled(false);
			addVendorInventoryItemTypeButton.setEnabled(false);
			modifyVendorInventoryItemTypeButton.setEnabled(false);
			deleteVendorInventoryItemTypeButton.setEnabled(false);
			browseInventoryExceptionsButton.setEnabled(false);
		}
	}

	//----------------------------------------------------------------
	// Create this View Title with the string specified and formatted
	// to this program standard in the superclass View
	//-----------------------------------------------------------------
	private JPanel createTitle()
	{
		return formatViewTitle ( "MAIN MENU (Logged in as: " + myModel.getState("Name") + ")");
	}

	//-------------------------------------------------------------
	// Create the navigation buttons
	//-------------------------------------------------------------
	private JPanel createNavigationButtons()
	{
		navigationPanel = new JPanel();

		navigationPanel.setLayout( new BoxLayout ( navigationPanel, BoxLayout.Y_AXIS ));
		navigationPanel.setBackground( blue );

		JPanel topPanel = new JPanel();
		topPanel.setBackground( blue );

		// create "raw" JButtons and call superclass View to format
		// the button to the program's standard, add them to the panel
		processInvoiceButton = new JButton( "PROCESS NEW INVOICE" );
		topPanel.add( formatButton ( processInvoiceButton ));
		
		completeExistingInvoiceButton = new JButton( "COMPLETE EXISTING INVOICE" );
		topPanel.add( formatButton ( completeExistingInvoiceButton ));

		takeItemFromInventoryButton = new JButton( "TAKE ITEM FROM INVENTORY" );
		topPanel.add( formatButton ( takeItemFromInventoryButton ));
		
		navigationPanel.add( topPanel );
		
		JPanel topPanel2 = new JPanel();
		topPanel2.setBackground( blue );
		
		fullCurrentInventoryButton = new JButton( "SEE FULL INVENTORY" );
		topPanel2.add( formatButton ( fullCurrentInventoryButton ));
		
		obtainReorderPointButton = new JButton( "GET ITEMS TO RE-ORDER" );
		topPanel2.add( formatButton ( obtainReorderPointButton ));
		
		navigationPanel.add(topPanel2);
		
		navigationPanel.add( Box.createRigidArea( size ));

		// the separation line provided by Dr. Mitra
		JPanel separatorPanel = new JPanel( new FlowLayout( FlowLayout.CENTER ));
		separatorPanel.setBackground( blue );

		LineLabel leftLine = new LineLabel( 150 );
		leftLine.setForeground( Color.red.darker().darker() );
		separatorPanel.add( leftLine );

		JLabel adminFuncLabel = new JLabel( "Other (Administrative) Choices" );
		adminFuncLabel.setForeground( Color.red.darker().darker() );
		adminFuncLabel.setFont( myTitleFont );

		separatorPanel.add( adminFuncLabel );

		LineLabel rightLine = new LineLabel( 150 );
		rightLine.setForeground( Color.red.darker().darker() );
		separatorPanel.add( rightLine );

		navigationPanel.add( separatorPanel );
		// end of separation line

		JPanel itemPanel = new JPanel();
		itemPanel.setBackground( blue );
		itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel itemFuncLabel = new JLabel("          Inventory Item: ");
		itemFuncLabel.setFont(myComponentsFont);
		itemPanel.add(   itemFuncLabel );
				
		modifyInventoryItemButton = new JButton( "MODIFY" );
		itemPanel.add( formatButtonSmall ( modifyInventoryItemButton ));
		
		seeItemInvoiceButton= new JButton( "GET ITEM'S INVOICE" );
		itemPanel.add( formatButton ( seeItemInvoiceButton));
		
		navigationPanel.add( itemPanel );
		
		JPanel itemTypeFuncPanel = new JPanel();
		itemTypeFuncPanel.setBackground( blue );
		itemTypeFuncPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel itemTypeFuncLabel = new JLabel("Inventory Item Type: ");
		itemTypeFuncLabel.setFont(myComponentsFont);
		itemTypeFuncPanel.add(   itemTypeFuncLabel );
		
		addItemTypeButton = new JButton( "ADD" );
		itemTypeFuncPanel.add( formatButtonSmall ( addItemTypeButton ));
		
		modifyItemTypeButton = new JButton( "MODIFY" );
		itemTypeFuncPanel.add( formatButtonSmall ( modifyItemTypeButton ));
		
		deleteItemTypeButton = new JButton( "DELETE" );
		itemTypeFuncPanel.add( formatButtonSmall ( deleteItemTypeButton ));
		
		navigationPanel.add( itemTypeFuncPanel );
		
		JPanel vendorFuncPanel = new JPanel();
		vendorFuncPanel.setBackground( blue );
		vendorFuncPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel vendorFuncLabel = new JLabel("                       Vendor: ");
		vendorFuncLabel.setFont(myComponentsFont);
		vendorFuncPanel.add(   vendorFuncLabel );
		
		addVendorButton = new JButton( "ADD" );
		vendorFuncPanel.add( formatButtonSmall ( addVendorButton ));
		
		modifyVendorButton = new JButton( "MODIFY" );
		vendorFuncPanel.add( formatButtonSmall ( modifyVendorButton ));
		
		deleteVendorButton = new JButton( "DELETE" );
		vendorFuncPanel.add( formatButtonSmall ( deleteVendorButton ));
		
		seeAllVendorItemsButton = new JButton( "SEE VENDOR'S ITEMS" );
		vendorFuncPanel.add( formatButton ( seeAllVendorItemsButton ));
		
		navigationPanel.add( vendorFuncPanel );
		
		JPanel vendorInvItemTypeFuncPanel = new JPanel();
		vendorInvItemTypeFuncPanel.setBackground( blue );
		vendorInvItemTypeFuncPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel vendorInvItemTypeFuncLabel = new JLabel("Vendor Item Pricing: ");
		vendorInvItemTypeFuncLabel.setFont(myComponentsFont);
		vendorInvItemTypeFuncPanel.add(   vendorInvItemTypeFuncLabel );
		
		addVendorInventoryItemTypeButton = new JButton( "ADD" );
		vendorInvItemTypeFuncPanel.add( formatButtonSmall ( addVendorInventoryItemTypeButton ));
		
		modifyVendorInventoryItemTypeButton = new JButton( "MODIFY" );
		vendorInvItemTypeFuncPanel.add( formatButtonSmall ( modifyVendorInventoryItemTypeButton ));
		
		deleteVendorInventoryItemTypeButton = new JButton( "DELETE" );
		vendorInvItemTypeFuncPanel.add( formatButtonSmall ( deleteVendorInventoryItemTypeButton ));
		
		navigationPanel.add( vendorInvItemTypeFuncPanel );
		
		JPanel workerFuncPanel = new JPanel();
		workerFuncPanel.setBackground( blue );
		workerFuncPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel workerFuncLabel = new JLabel("                      Worker: ");
		workerFuncLabel.setFont(myComponentsFont);
		workerFuncPanel.add(   workerFuncLabel );
		
		addWorkerButton = new JButton( "ADD" );
		workerFuncPanel.add( formatButtonSmall ( addWorkerButton ));
		
		modifyWorkerButton = new JButton( "MODIFY" );
		workerFuncPanel.add( formatButtonSmall ( modifyWorkerButton ));
		
		deleteWorkerButton = new JButton( "DELETE" );
		workerFuncPanel.add( formatButtonSmall ( deleteWorkerButton ));
		
		navigationPanel.add( workerFuncPanel );
		
		JPanel iExcepPanel = new JPanel();
		iExcepPanel.setBackground( blue );
		iExcepPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			
		browseInventoryExceptionsButton = new JButton( "BROWSE INV. EXCEPTIONS" );
		iExcepPanel.add( formatButton ( browseInventoryExceptionsButton ));
		
		navigationPanel.add( iExcepPanel );
		
		
		// Do the same what was done for the top row buttons:
		// create "raw" JButtons and call superclass View to format
		// the button to the program's standard, add them to the panel
		JPanel lowerPanel = new JPanel();
		lowerPanel.setBackground( blue );

		lowerPanel.setLayout( new BoxLayout ( lowerPanel, BoxLayout.Y_AXIS ));
		lowerPanel.add( Box.createRigidArea( size ));

		cancelButton = new JButton( "LOGOUT" );
		lowerPanel.add( formatButton ( cancelButton ));
		
		navigationPanel.add( lowerPanel );

		return navigationPanel;
	}


	// Create the status log field
	//-------------------------------------------------------------
	private JPanel createStatusLog(String initialMessage)
	{

		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	// IMPRESARIO: Note how we use this method name instead of 'actionPerformed()'
	// now. This is because the super-class View has methods for both action and
	// focus listeners, and both of them delegate to this method. So this method
	// is called when you either have an action (like a button click) or a loss
	// of focus (like tabbing out of a textfield, moving your cursor to something
	// else in the view, etc.)
	// process events generated from our GUI components
	//-------------------------------------------------------------
	public void processAction(EventObject evt)
	{
//		DEBUG: System.out.println("TransactionChoiceView.processAction()");

		// Always clear the status log for a new action
		clearErrorMessage();

		if ( evt.getSource() == cancelButton )
		{
			// Call method in Inventory Master to exit system
			myModel.stateChangeRequest( "ExitMainMenu", null );
		}
		else
		if ( evt.getSource() == processInvoiceButton )
		{
			myModel.stateChangeRequest( "", null );
		}
		else
		if ( evt.getSource() == completeExistingInvoiceButton )
		{
			myModel.stateChangeRequest( "", null );
		}
		else
		if ( evt.getSource() == takeItemFromInventoryButton )
		{
			myModel.stateChangeRequest( "", null );
		}
		else
		if ( evt.getSource() == fullCurrentInventoryButton )
		{
			myModel.stateChangeRequest( "", null );
		}
		else
		if ( evt.getSource() == obtainReorderPointButton )
		{
			myModel.stateChangeRequest( "", null );
		}
		else
		if ( evt.getSource() == addVendorButton )
		{
			myModel.stateChangeRequest( "AddVendor", null );
		}
		else
		if ( evt.getSource() == modifyVendorButton )
		{
			myModel.stateChangeRequest( "ModifyVendor", null );
		}
		else
		if ( evt.getSource() == deleteVendorButton )
		{
			myModel.stateChangeRequest( "DeleteVendor", null );
		}
		else
		if ( evt.getSource() == seeAllVendorItemsButton )
		{
			myModel.stateChangeRequest( "SeeAllVendorItems", null );
		}
		else
		if ( evt.getSource() == addVendorInventoryItemTypeButton )
		{
			myModel.stateChangeRequest( "AddVendorInventoryItemType", null );
		}
		else
		if ( evt.getSource() == modifyVendorInventoryItemTypeButton )
		{
			myModel.stateChangeRequest( "ModifyVendorInventoryItemType", null );
		}
		else
		if ( evt.getSource() == deleteVendorInventoryItemTypeButton )
		{
			myModel.stateChangeRequest( "DeleteVendorInventoryItemType", null );
		}
		else
		if ( evt.getSource() == addWorkerButton )
		{
			myModel.stateChangeRequest( "AddWorker", null );
		}
		else
		if ( evt.getSource() == modifyWorkerButton )
		{
			myModel.stateChangeRequest( "ModifyWorker", null );
		}
		else
		if ( evt.getSource() == deleteWorkerButton )
		{
			myModel.stateChangeRequest( "DeleteWorker", null );
		}
		else
		if ( evt.getSource() == addItemTypeButton )
		{
			myModel.stateChangeRequest( "AddInventoryItemType", null );
		}
		else
		if ( evt.getSource() == modifyItemTypeButton )
		{
	
			myModel.stateChangeRequest( "ModifyInventoryItemType", null );
		}
		else
		if ( evt.getSource() == deleteItemTypeButton )
		{
			myModel.stateChangeRequest( "DeleteInventoryItemType", null );
		}
		else
		if ( evt.getSource() == modifyInventoryItemButton )
		{
			myModel.stateChangeRequest( "ModifyInventoryItemButton", null );
		}
		else
		if ( evt.getSource() == seeItemInvoiceButton )
		{
			myModel.stateChangeRequest( "GetItemInvoice", null );
		}
		else
		
		if ( evt.getSource() == browseInventoryExceptionsButton )
		{
			myModel.stateChangeRequest( "BrowseInventoryExceptions", null );
		}
		else
		if ( evt.getSource() == addVendorButton )
		{
			myModel.stateChangeRequest( "AddVendor", null );
		}
		else
		if ( evt.getSource() == modifyVendorButton )
		{
			
			myModel.stateChangeRequest( "ModifyVendor", null );
		}
		else
		if ( evt.getSource() == deleteVendorButton )
		{
			
			myModel.stateChangeRequest( "DeleteVendor", null );
		}
		else
		if ( evt.getSource() == addVendorInventoryItemTypeButton )
		{
			myModel.stateChangeRequest( "AddVendorInventoryItemType", null );
		}
		else
		if ( evt.getSource() == modifyVendorInventoryItemTypeButton )
		{
			myModel.stateChangeRequest( "ModifyVendorInventoryItemType", null );
		}
		else
		if ( evt.getSource() == deleteVendorInventoryItemTypeButton )
		{
			myModel.stateChangeRequest( "DeleteVendorInventoryItemType", null );
		}
		
		
		
		
		
	}

	/**
	 * Update methods - to update views.
	 */
	//---------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// STEP 6: Be sure to finish the end of the 'perturbation'
		// by indicating how the view state gets updated.
		
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
