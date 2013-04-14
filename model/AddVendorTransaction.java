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
public class AddVendorTransaction extends Transaction
{
	private String vendorAddedMessage = "";
	private String nameAlreadyExistsMessage = "";

	private Vendor newVendor = null;

	//----------------------------------------------------------
	public AddVendorTransaction () throws Exception
	{
		super();
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();

		dependencies.setProperty( "SaveVendor", "VendorAddedMessage");
		dependencies.setProperty( "Name", "NameAlreadyExistsMessage");
		dependencies.setProperty( "Cancel", "CancelTransaction");

		myRegistry.setDependencies( dependencies );
	}


	//-----------------------------------------------------------
	public Object getState( String key )
	{
		if (key.equals("VendorAddedMessage"))
		{
			return vendorAddedMessage;
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
		else if ( key.equals( "SaveVendor" ) )
		{
			processVendorData ((Properties) value);
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
		Vendor existingVendor = null;
		try
		{
			existingVendor = new Vendor(uName);
			nameAlreadyExistsMessage = "ERROR: Vendor with name: " + uName + " already exists.\n" +
					" Please use the 'Modify Vendor' button to change information about an existing worker.";
		}
		catch (InvalidPrimaryKeyException ex)
		{
			nameAlreadyExistsMessage = "Entered vendor name acceptable.";
		}
	}

	// This method handles the sending of set of data associated with the Worker to be added
	// to this object. It ASSUMES that the vendor sent is not already present in the database,
	// AND DOES NOT CHECK FOR ITS NON-EXISTENCE.
	//----------------------------------------------------------
	private void processVendorData(Properties values)
	{
		boolean vendorDataOK = false;
		String vendorName = values.getProperty("Name");
		String pwd = values.getProperty("PhoneNumber");	
		
		if ( ((vendorName != null) && (vendorName.length() > 0)) &&
				((pwd != null) && (pwd.length() > 0)) )
		{
			Properties prop = new Properties();
			prop.setProperty("Name", vendorName);
			prop.setProperty("PhoneNumber", pwd);
			prop.setProperty("Status", "Active");
			
			newVendor = new Vendor(prop);
			
			try
			{
				newVendor.save();
				vendorAddedMessage = "Vendor: " + vendorName + " successfully added!";
			}
			catch (SQLException ex)
			{
				vendorAddedMessage = "ERROR: Vendor data could not be saved";
				new Event(Event.getLeafLevelClassName(this), "processVendorData",
						"Error in saving Vendor data: " + ex.toString(), Event.ERROR);
			}
		}
		else
		{
			vendorAddedMessage = "ERROR: Vendor data missing or invalid!";
		}
	}
	
	//----------------------------------------------------------------
	// Create the view of this class. 
	//---------------------------------------------------------------
	protected View createView()
	{

		// create our initial view
		View localView = ViewFactory.createView ( "AddVendorView", this );


		return localView;
	}

}
