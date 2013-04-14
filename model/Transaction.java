package model;

//system imports
import java.awt.BorderLayout;
import java.util.Hashtable;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JPanel;
import event.Event;

//project imports
import impresario.*;
import userinterface.MainFrame;
import userinterface.View;

//======================================================================
// This class is like a template for all the delegates/transactions in
// the program, i.e it is the superclass.
// It provides the implementation for common methods and
// also declares the methods that are "common, but not exactly the same"
// for all the subclasses. 
//======================================================================
abstract public class Transaction implements IView, IModel, ISlideShow
{

	// For Impresario
	protected Properties dependencies;
	protected ModelRegistry myRegistry;

	protected JFrame myFrame;
	protected Hashtable myViews;

	// GUI Components

	/**
	 * Constructor for this class.
	 */
	//----------------------------------------------------------
	protected Transaction () throws Exception
	{

		myFrame = MainFrame.getInstance();
		
		// Uncommented by aozgo1 to go one step back from some screens
		myViews = new Hashtable(); 
		//myCust = cust;

		myRegistry = new ModelRegistry( "Transaction" );
		
		if(myRegistry == null)
		{
			new Event(Event.getLeafLevelClassName(this), "Transaction",
				"Could not instantiate Registry", Event.ERROR);
		}
		setDependencies();

	}

	//----------------------------------------------------------
	protected abstract void setDependencies();

	//---------------------------------------------------------
	protected abstract View createView();

	//-------------------------------------------------------------
	// This is a Template Pattern/method: method call swapToView is 
	// the "hook" method call, which is implemented by each 
	// individual Transaction class
	//-------------------------------------------------------------
	protected void doYourJob()
	{
		try
		{
			View currentView = createView ();
			
			//if (currentView ==null) 
				//System.out.println("Transaction.doYourJob: createView() returns null");
			
			swapToView ( currentView );
			
			//System.out.println("Transaction.doYourJob: Getting past swap to view");

		}
		catch (Exception ex)
		{
				//System.out.println("Transaction.doYourJob: exception: " + ex.toString());
				ex.printStackTrace();
		}
	}

	// forward declarations
	//-----------------------------------------------------------
	public abstract Object getState(String key);

	//-----------------------------------------------------------
	public abstract void stateChangeRequest(String key, Object value);

	/** Called via the IView relationship
	 * Re-define in sub-class, if necessary
	 */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
	}

	/** Register objects to receive state updates. */
	//----------------------------------------------------------
	public void subscribe( String key, IView subscriber )
	{
		// DEBUG: System.out.println("Cager[" + myTableName + "].subscribe");
		// forward to our registry
		myRegistry.subscribe( key, subscriber );
	}

	/** Unregister previously registered objects. */
	//----------------------------------------------------------
	public void unSubscribe(String key, IView subscriber)
	{
		// DEBUG: System.out.println("Cager.unSubscribe");
		// forward to our registry
		myRegistry.unSubscribe(key, subscriber);
	}

	//----------------------------------------------------------------------------
	private void swapToPanelView( JPanel otherView )
	{
		JPanel currentView = (JPanel)myFrame.getContentPane().getComponent( 2 );
		// and remove it
		if ( currentView != null )
		{
			myFrame.getContentPane().remove( currentView );
		}
		
		// add our view		
		myFrame.getContentPane().add( otherView, BorderLayout.CENTER );
		//pack the frame and show it
		myFrame.pack();
		
		//Place in center
		myFrame.setLocationRelativeTo( null );
	}

	//-----------------------------------------------------------------------------
	public void swapToView( IView otherView )
	{

		if ( otherView == null )
		{
			new Event(Event.getLeafLevelClassName(this), "swapToView",
				"Missing view for display ", Event.ERROR);
			return;
		}

		if ( otherView instanceof JPanel )
		{
			swapToPanelView(( JPanel )otherView );
		}
		else
		{
			new Event(Event.getLeafLevelClassName(this), "swapToView",
				"Non-displayable view object sent ", Event.ERROR);
		} 

	} //end of swapToView
}

