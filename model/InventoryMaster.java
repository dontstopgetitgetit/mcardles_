// tabs=4
//************************************************************
//	COPYRIGHT 2011 Sandeep Mitra and students, The
//    College at Brockport, State University of New York. -
//	  ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//************************************************************
//
//package
package model;

// project imports
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

// system imports
import java.awt.BorderLayout;
import java.util.Hashtable;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JPanel;
import event.Event;

/** Overall class InventoryMaster represents the System controller**/
/** MODEL MAPPING: InventoryMaster - Main Interface Agent */
//==============================================================
public class InventoryMaster implements IView, IModel, ISlideShow
{

	// Constants

	// Impresario dependencies
	private Properties dependencies;
	private ModelRegistry myRegistry;

	// Keep track of views this Controller manages
	protected Hashtable myViews;
	// Access to singleton main display frame
	protected JFrame myFrame;

	// Factory used to reduce coupling between the Controller and the views it manages
	protected ViewFactory myFactory;

	// FOR USING IMPRESARIO: Declare the State variables held in this class
	// Much of the state can be held in 'persistableState', a Properties object
	// holding data got from the database. Since this is not a Persistable object,
	// you hold the state in other instance variables, like the one below.
	protected Worker loggedinWorker;

	// To manage updates of the managed views -- messages displayed on the views are held in these attributes
	protected String loginErrorMessage = "";

	//---------------------------------------------------------------------
	public InventoryMaster(JFrame frm)
	{

		myFrame = frm;
		myViews = new Hashtable();

		myFactory = new ViewFactory();


		// STEP 3.1: Create the Registry object - if you inherit from
		// EntityBase, this is done for you. Otherwise, you do it yourself
		myRegistry = new ModelRegistry("InventoryMaster");
		if(myRegistry == null)
		{
			new Event(Event.getLeafLevelClassName(this), "InventoryMaster",
					"Could not instantiate Registry", Event.ERROR);
		}

		// STEP 3.2: Be sure to set the dependencies correctly
		setDependencies();

		// Set up the initial view
		// MODEL MAPPING: Transition from initial state to Main Menu shown in Menu state diagram
		// NOTE: The display of the first view has to be done differently
		createAndShowLoginView();

	}

	/**
	 * Required by the Impresario framework
	 */
	//-----------------------------------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();

		dependencies.setProperty("Login", "LoginError");

		myRegistry.setDependencies(dependencies);
	}

	/**
	 * Method called from client to get the value of a particular field
	 * held by the objects encapsulated by this object.
	 *
	 * @param	key	Name of database column (field) for which the client wants the value
	 *
	 * @return	Value associated with the field
	 */
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("LoginError") == true)
		{
			return loginErrorMessage;
		}
		else
		{
			if (loggedinWorker != null)
			{
				return loggedinWorker.getState(key);
			}
		}

		return null;
	}

	/**
	 * Method called from client if the full state of the object is desired - i.e.,
	 * the values associated with all fields.
	 *
	 * @return	null, since there is no 'persistent state' associated with this class.
	 */
	//----------------------------------------------------------
	public Properties getCompleteState()
	{
		return null;
	}

	//--------------------------------------------------------------------------
	//
	//----------------------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		// if any Transaction was Canceled, then
		// Clerk knows it's time to put up MainMenuView
		if ( key.equals( "CancelTransaction" ))
		{

			createAndShowMainMenuView();
		}
		else
		if ( key.equals( "Login" ))
		{
			loginErrorMessage = "";

			Properties props = (Properties)value;
			String loginName = props.getProperty("Name");
			String pwd = props.getProperty("Password");

			try
			{
				loggedinWorker = new Worker(loginName, pwd);
				createAndShowMainMenuView();
			}
			catch (Exception ex)
			{
				loginErrorMessage = ex.getMessage();
			}
		}
		else
		if ( key.equals( "AddWorker" ))
		{
			try
			{
				Transaction t = new AddWorkerTransaction();
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.AddWorker",
						"Error in creating AddWorkerTransaction: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if ( key.equals( "ModifyWorker" ))
		{
			try
			{
				Transaction t = new ModifyWorkerTransaction(loggedinWorker);
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.AddWorker",
						"Error in creating ModifyWorkerTransaction: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if ( key.equals( "DeleteWorker" ))
		{
			try
			{
				Transaction t = new DeleteWorkerTransaction(loggedinWorker);
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.AddWorker",
						"Error in creating ModifyWorkerTransaction: " + ex.toString(), Event.ERROR);
			}	
		}
		else
		if ( key.equals( "AddInventoryItemType" ))
		{
			try
			{
				Transaction t = new AddInventoryItemTypeTransaction();
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.AddInventoryItemType",
						"Error in creating AddInventoryItemTypeTransaction: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if ( key.equals( "ModifyInventoryItemType" ))
		{
			try
			{
				Transaction t = new ModifyInventoryItemTypeTransaction();
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.ModifyInventoryItemType",
						"Error in creating ModifyInventoryItemTypeTransaction: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if(key.equals("DeleteInventoryItemType"))
		{
			try
			{
				Transaction t = new DeleteInventoryItemTypeTransaction();
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");		
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.DeleteInventoryItemType",
						"Error in creating DeleteInventoryItemTypeTransaction: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if ( key.equals( "AddVendor" ))
		{
			try
			{
				Transaction t = new AddVendorTransaction();
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.AddVendor",
						"Error in creating AddVendorTransaction: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if ( key.equals( "ModifyVendor" ))
		{
			
			try
			{
				Transaction t = new ModifyVendorTransaction();
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.ModifyVendor",
						"Error in creating ModifyVendorTransaction: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if ( key.equals( "DeleteVendor" ))
		{
			
			try
			{
				Transaction t = new DeleteVendorTransaction();
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.DeleteVendor",
						"Error in creating DeleteVendorTransaction: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if ( key.equals( "DeleteVendorInventoryItemType" ))
		{
			try
			{
				Transaction t = new DeleteVendorInventoryItemTypeTransaction();
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.DeleteVendorInventoryItemType",
						"Error in creating  DeleteVendorInventoryItemTypeTransaction: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if ( key.equals( "AddVendorInventoryItemType" ))
		{
			try
			{
				Transaction t = new AddVendorInventoryItemTypeTransaction();
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.AddVendorInventoryItemType",
						"Error in creating  AddVendorInventoryItemTypeTransaction: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if ( key.equals( "ModifyVendorInventoryItemType" ))
		{
			try
			{
				Transaction t = new ModifyVendorInventoryItemTypeTransaction();
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.ModifyVendorInventoryItemType",
						"Error in creating  ModifyVendorInventoryItemTypeTransaction: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if ( key.equals( "BrowseInventoryExceptions" ))
		{
			try
			{
				Transaction t = new BrowseInventoryExceptionsTransaction();
				t.subscribe("CancelTransaction", this);
				t.stateChangeRequest("DoYourJob", "");
			}
			catch (Exception ex)
			{
				new Event(Event.getLeafLevelClassName(this), "stateChangeRequest.BrowseInventoryExceptions",
						"Error in creating  BrowseInventoryExceptions: " + ex.toString(), Event.ERROR);
			}
		}
		else
		if ( key.equals( "ExitMainMenu" ))
		{
			loggedinWorker = null;

			createAndShowLoginView();
		}
		else
		// MODEL MAPPING: Receipt of "Dismiss Window" user action shown on
		// Menu State Diagram (from Main Menu Screen to final state)
		if ( key.equals( "ExitSystem" ) )
		{
			exitSystem();
		}

		myRegistry.updateSubscribers( key, this );
	}


	/**
	 * Method called from external client (typically, a view). This method will
	 * cause an exit from the system, and is likely to be called as a result of
	 * a click on a button labeled something like 'Exit Application'.
	 */
	//-----------------------------------------------------------------------------------
	protected void exitSystem()
	{
		System.exit(0);
	}

	/** Register objects to receive state updates. */
	//----------------------------------------------------------
	public void subscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager[" + myTableName + "].subscribe");
		// forward to our registry
		myRegistry.subscribe(key, subscriber);
	}

	/** Unregister previously registered objects. */
	//----------------------------------------------------------
	public void unSubscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager.unSubscribe");
		// forward to our registry
		myRegistry.unSubscribe(key, subscriber);
	}

	//-----------------------------------------------------------------
	public void updateState(String key, Object value)
	{
		// Every update will be handled in stateChangeRequest
		stateChangeRequest(key, value);
	}

	//-----------------------------------------------------------------------------
	protected void createAndShowLoginView()
	{
		View localView = ViewFactory.createView("LoginView", this); // USE VIEW FACTORY

		// swap the contents of current frame to this view
		swapToView(localView);
	}


	//-----------------------------------------------------------------------------
	protected void createAndShowMainMenuView()
	{
		View localView = (View)myViews.get("MainMenuView");

		if (localView == null)
		{
			// create the view for the first time
			localView = ViewFactory.createView("MainMenuView", this); // USE VIEW FACTORY

			//myViews.put("MainMenuView", localView);

			// swap the contents of current frame to this view
			swapToView(localView);
		}
		else
		{
			swapToView(localView);
		}
	}

	//----------------------------------------------------------------------------
	private void swapToPanelView(JPanel otherView)
	{
		JPanel currentView = (JPanel)myFrame.getContentPane().getComponent( 2 );
		// and remove it
		if (currentView != null)
		{
			myFrame.getContentPane().remove(currentView);
		}

		// add our view into the CENTER of the MainFrame
		myFrame.getContentPane().add( otherView, BorderLayout.CENTER );

		//pack the frame and show it
		myFrame.pack();

		//Place in center
		WindowPosition.placeCenter(myFrame);
	}

	//-----------------------------------------------------------------------------
	public void swapToView( IView otherView )
	{

		if (otherView == null)
		{
			new Event(Event.getLeafLevelClassName(this), "swapToView",
					"Missing view for display ", Event.ERROR);
			return;
		}

		if (otherView instanceof JPanel)
		{
			swapToPanelView((JPanel)otherView);
		}//end of SwapToView
		else
		{
			new Event(Event.getLeafLevelClassName(this), "swapToView",
					"Non-displayable view object sent ", Event.ERROR);
		}

	}


}//end of class