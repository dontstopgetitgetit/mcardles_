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
public class AddWorkerTransaction extends Transaction
{
	private String workerAddedMessage = "";
	private String nameAlreadyExistsMessage = "";

	private Worker newWorker = null;

	//----------------------------------------------------------
	public AddWorkerTransaction () throws Exception
	{
		super();
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();

		dependencies.setProperty( "SaveWorker", "WorkerAddedMessage");
		dependencies.setProperty( "Name", "NameAlreadyExistsMessage");
		dependencies.setProperty( "Cancel", "CancelTransaction");

		myRegistry.setDependencies( dependencies );
	}


	//-----------------------------------------------------------
	public Object getState( String key )
	{
		if (key.equals("WorkerAddedMessage"))
		{
			return workerAddedMessage;
		}
		else if (key.equals("NameAlreadyExistsMessage")) 
		{
				return nameAlreadyExistsMessage;
		}
		return null;
	}

	//-----------------------------------------------------------
	public void stateChangeRequest( String key, Object value )
	{
		// DEBUG System.out.println("AddWorkerTransaction.sCR: key = " + key + "; value = " + value);

		if ( key.equals( "DoYourJob" ) )
		{
			// as the sequence diagram states, create and show a view here
			doYourJob();
		}
		else if ( key.equals( "SaveWorker" ) )
		{
			processWorkerData ((Properties) value);
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
		Worker existingWorker = null;
		try
		{
			existingWorker = new Worker(uName);
			nameAlreadyExistsMessage = "ERROR: Worker with username: " + uName + " already exists.\n" +
					" Please use the 'Modify Worker' button to change information about an existing worker.";
		}
		catch (InvalidPrimaryKeyException ex)
		{
			nameAlreadyExistsMessage = "Entered worker name acceptable.";
		}
	}

	// This method handles the sending of set of data associated with the Worker to be added
	// to this object. It ASSUMES that the username sent is not already present in the database,
	// AND DOES NOT CHECK FOR ITS NON-EXISTENCE.
	//----------------------------------------------------------
	private void processWorkerData(Properties values)
	{
		boolean workerDataOK = false;
		String userName = values.getProperty("Name");
		String pwd = values.getProperty("WorkerPassword");
		String creds = values.getProperty("Credentials");
		
		if ( ((userName != null) && (userName.length() > 0)) &&
				((pwd != null) && (pwd.length() > 0)) &&
				((creds != null) && (creds.length() > 0)) )
		{
			Properties prop = new Properties();
			prop.setProperty("Name", userName);
			prop.setProperty("WorkerPassword", pwd);
			prop.setProperty("Credentials", creds);
			prop.setProperty("Status", "Active");
			
			newWorker = new Worker(prop);
			
			try
			{
				newWorker.save();
				workerAddedMessage = "Worker: " + userName + " successfully added!";
			}
			catch (SQLException ex)
			{
				workerAddedMessage = "ERROR: Worker data could not be saved";
				new Event(Event.getLeafLevelClassName(this), "processWorkerData",
						"Error in saving Worker data: " + ex.toString(), Event.ERROR);
			}
		}
		else
		{
			workerAddedMessage = "ERROR: Worker data missing or invalid!";
		}
	}
	
	//----------------------------------------------------------------
	// Create the view of this class. 
	//---------------------------------------------------------------
	protected View createView()
	{

		// create our initial view
		View localView = ViewFactory.createView ( "AddWorkerView", this );


		return localView;
	}

}
