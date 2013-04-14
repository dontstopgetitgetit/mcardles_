// specify the package
package model;

//system imports
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.JOptionPane;

//project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import userinterface.View;
import userinterface.ViewFactory;

//==============================================================
public class DeleteWorkerTransaction extends Transaction
{
	private String workerDeletedMessage = "";
	private String nameDoesNotExistMessage = "";

	private Worker existingWorker = null;
	private Worker loggedinWorker = null;

	//----------------------------------------------------------
	public DeleteWorkerTransaction (Worker loggedinWorker) throws Exception
	{
		super();
		this.loggedinWorker = loggedinWorker;
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();

		dependencies.setProperty( "SaveWorker", "WorkerDeletedMessage");
		dependencies.setProperty( "Name", "WorkerPassword,Credentials,NameDoesNotExistMessage");
		dependencies.setProperty( "Cancel", "CancelTransaction");

		myRegistry.setDependencies( dependencies );
	}

	//-----------------------------------------------------------
	public Object getState( String key )
	{
		if (key.equals("WorkerDeletedMessage"))
		{
			return workerDeletedMessage;
		}
		else if (key.equals("NameDoesNotExistMessage")) 
		{
				return nameDoesNotExistMessage;
		}
		else
		if (existingWorker != null)
		{
			Object val = existingWorker.getState(key);
			if (val != null)
			{
				return val;
			}
		}
		return null;
	}

	//-----------------------------------------------------------
	public void stateChangeRequest( String key, Object value )
	{
		// DEBUG System.out.println("DeleteWorkerTransaction.sCR: key = " + key + "; value = " + value);

		if ( key.equals( "DoYourJob" ) )
		{
			// as the sequence diagram states, create and show a view here
			doYourJob();
		}
		else if ( key.equals( "SaveWorker" ) )
		{
			processWorkerData ();
		}
		else if (key.equals("Name") )
		{
			processUserName((String)value);
		}

		myRegistry.updateSubscribers ( key, this );
	}

	// This method handles the sending of a Worker's user name to this object. If the user name
	// already exists in the database, this method displays an error message popup.
	//----------------------------------------------------------
	private void processUserName(String uName)
	{
		if (uName.equals((String)loggedinWorker.getState("Name")) == true)
		{
			nameDoesNotExistMessage = "ERROR: Worker: " + uName + " cannot delete self!\n"
					+ " Please choose another worker to delete!";
			return;
		}
		try
		{
			existingWorker = new Worker(uName);
			nameDoesNotExistMessage = "Worker name exists in database!";
		}
		catch (InvalidPrimaryKeyException ex)
		{
			nameDoesNotExistMessage = "ERROR: Worker with username: " + uName + " does not exist.\n" +
							" Please use the 'Add Worker' button to add a new worker.";
		}
	}

	// 
	//----------------------------------------------------------
	private void processWorkerData()
	{
		if (existingWorker != null)
		{
			existingWorker.setAttribute("Status", "Inactive");
			
			try
			{
				existingWorker.delete();
				workerDeletedMessage = "Worker: " + existingWorker.getState("Name") + 
						" successfully de-activated!";
			}
			catch (SQLException ex)
			{
				workerDeletedMessage = "ERROR: Worker data could not be deleted";
				new Event(Event.getLeafLevelClassName(this), "processWorkerData",
							"Error in deleting Worker data: " + ex.toString(), Event.ERROR);
			}
		}
		else
		{
			workerDeletedMessage = "ERROR: Worker data missing or invalid!";
		}
	}
	
	//----------------------------------------------------------------
	// Create the view of this class. 
	//---------------------------------------------------------------
	protected View createView()
	{
		// create our initial view
		View localView = ViewFactory.createView ( "DeleteWorkerView", this );

		return localView;
	}

}
