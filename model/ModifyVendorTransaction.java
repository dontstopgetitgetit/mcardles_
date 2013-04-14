//************************************************************
// COPYRIGHT 2013, Sandeep Mitra and Students, The College at Brockport
// - ALL RIGHTS RESERVED
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
public class ModifyVendorTransaction extends Transaction
{
	private String VendorModifiedMessage = "";
	private String VendorCollectionMessage = "";
	private Vendor existingVendor = null;
	private VendorCollection vendorColl = null;
	//----------------------------------------------------------
	public ModifyVendorTransaction () throws Exception
	{
		super();
	}
	//----------------------------------------------------------
	public ModifyVendorTransaction (String vName, String phoneNumber) throws Exception
	{
		super();

		try
		{
			existingVendor = new Vendor(vName, phoneNumber);
		}
		catch (InvalidPrimaryKeyException ex)
		{
			existingVendor = null;
		}
	}
	//----------------------------------------------------------
	public ModifyVendorTransaction (Vendor et) throws Exception
	{
		super();

		existingVendor = et;
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty( "SaveVendor", "VendorModifiedMessage");
		dependencies.setProperty( "SearchModifyVendor", "VendorCollectionMessage");
		dependencies.setProperty( "Cancel", "CancelTransaction");
		myRegistry.setDependencies( dependencies );
	}
	//-----------------------------------------------------------
	public Object getState( String key )
	{
		if (key.equals("VendorModifiedMessage"))
		{
			return VendorModifiedMessage;
		}
		else
			if (key.equals("VendorCollectionMessage"))
			{
				return VendorCollectionMessage;
			}
			else
				if (key.equals("VendorList"))
				{
					if (vendorColl != null)
					{
						return vendorColl.getState("VendorList");
					}
				}	
				else
					if (existingVendor != null)
					{
						return existingVendor.getState(key);
					}

		return null;
	}
	//-----------------------------------------------------------
	public void stateChangeRequest( String key, Object value )
	{
		// DEBUG System.out.println("ModifyInventoryItemTypeTransaction.sCR: key = " + key + "; value = " + value);
		VendorModifiedMessage = "";
		VendorCollectionMessage = "";
		if ( key.equals( "DoYourJob" ) )
		{
			// as the sequence diagram states, create and show a view here
			doYourJob();
		}
		else if ( key.equals( "SaveVendor" ) )
		{
			Properties sentData = (Properties)value;
			boolean vendorExists = checkIfVendorExists(sentData.getProperty("Name"),
					sentData.getProperty("PhoneNumber"));
			
			if (vendorExists == false)
			{
				processVendorData (sentData);
			}
			else
			{

				VendorModifiedMessage = "ERROR: Vendor with specified name and phone number doesnt exists";
			}
		}
		else if ( key.equals( "VendorSelected" ) )
		{
			String vendorName = (String)value;
			Properties props = new Properties();
			props.setProperty("Name", vendorName);

			existingVendor = vendorColl.retrieve(props);

			createAndShowModifyVendorView();

		}
		else if ( key.equals( "SearchVendor" ) )
		{
			try
			{
				Properties props = (Properties)value;
				vendorColl = new VendorCollection(props);

				createAndShowVendorCollectionView();
			}
			catch (InvalidPrimaryKeyException ex)
			{
				VendorCollectionMessage = "No item types matching sent criteria found";
			}
		}
		else if ( key.equals( "CancelVendorList" ) )
		{
			View v = createView();
			swapToView(v);
		}
		myRegistry.updateSubscribers ( key, this );
	}
	//----------------------------------------------------------
	private boolean checkIfVendorExists(String vName, String phoneNumber)
	{
		Vendor duplicateVendor = null;
		String existingVendorName = (String)existingVendor.getState("Name");
		String existingPhoneNumber = (String)existingVendor.getState("PhoneNumber");

		try
		{
			duplicateVendor = new Vendor(vName, phoneNumber);
			if ((existingVendorName.equals(vName)) || 
					(existingPhoneNumber.equals(phoneNumber)))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		catch (InvalidPrimaryKeyException ex)
		{
			return false;
		}
	}

	// This method handles the sending of set of data associated with the Inventory Item Type to be added
	// to this object. It ASSUMES that the type name and barcode prefix sent are already present in the database,
	// AND DOES NOT CHECK FOR ITS EXISTENCE.
	//----------------------------------------------------------
	private void processVendorData(Properties values)
	{
		boolean vendorDataOK = false;
		String vName = values.getProperty("Name");
		String phoneNumber = values.getProperty("PhoneNumber");


		if ( ((vName != null) && (vName.length() > 0)) &&
				((phoneNumber != null) && (phoneNumber.length() > 0)) )
		{
			existingVendor.stateChangeRequest("Name", vName);
			existingVendor.stateChangeRequest("PhoneNumber", phoneNumber);
			existingVendor.stateChangeRequest("Status", "Active"); //active now

			try
			{
				//existingVendor.stateChangeRequest("Status", "Inactive");
				existingVendor.save();
				VendorModifiedMessage = "Vendor with name: " + vName + 
						" successfully Modified!";
				
			}
			catch (Exception ex3)
			{
				VendorModifiedMessage = "ERROR: Unexpected error in saving vendor";
				new Event(Event.getLeafLevelClassName(this), "processVendorData",
						"Unexpected Error in saving Vendor data: " + ex3.toString(), Event.ERROR);
			}
		}
	}


	//----------------------------------------------------------------
	// Create the view of this class. 
	//---------------------------------------------------------------
	protected View createView()
	{
		// create our initial view
		View localView = ViewFactory.createView ( "SearchModifyVendorView", this );
		return localView;
	}

	//----------------------------------------------------------------
	// Create the inventory item type collection view. 
	//---------------------------------------------------------------
	protected void createAndShowVendorCollectionView()
	{
		// create our initial view
		View localView = ViewFactory.createView ( "VendorCollectionView", this );
		swapToView(localView);
	}

	//----------------------------------------------------------------
	// Create the modify inventory item type view. 
	//---------------------------------------------------------------
	protected void createAndShowModifyVendorView()
	{
		// create our initial view
		View localView = ViewFactory.createView ( "ModifyVendorView", this );
		swapToView(localView);
	}
}