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
public class DeleteVendorTransaction extends Transaction
{
	private String VendorDeletedMessage = "";
	private String VendorCollectionMessage = "";
	private Vendor existingVendor = null;
	private VendorCollection vendorColl = null;
	//----------------------------------------------------------
	public DeleteVendorTransaction () throws Exception
	{
		super();
	}
	//----------------------------------------------------------
	public DeleteVendorTransaction (String vName, String phoneNumber) throws Exception
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
	public DeleteVendorTransaction (Vendor et) throws Exception
	{
		super();

		existingVendor = et;
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty( "SaveVendor", "VendorDeletedMessage");
		dependencies.setProperty( "SearchVendor", "VendorCollectionMessage");
		dependencies.setProperty( "Cancel", "CancelTransaction");
		myRegistry.setDependencies( dependencies );
	}
	//-----------------------------------------------------------
	public Object getState( String key )
	{
		if (key.equals("VendorDeletedMessage"))
		{
			return VendorDeletedMessage;
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
		VendorDeletedMessage = "";
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

				VendorDeletedMessage = "ERROR: Vendor with specified name and barcode doesnt exists";
			}
		}
		else if ( key.equals( "VendorSelected" ) )
		{
			String vendorName = (String)value;
			Properties props = new Properties();
			props.setProperty("Name", vendorName);

			existingVendor = vendorColl.retrieve(props);

			createAndShowDeleteVendorView();

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
			if ((existingVendorName.equals(vName)) && 
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
			//existingVendor.stateChangeRequest("Status", "Active");

			try
			{
				existingVendor.stateChangeRequest("Status", "Inactive");
				existingVendor.updateStateInDatabase();
				VendorDeletedMessage = "Vendor with name: " + vName + 
						" successfully rendered inactive!";
				
			}
			catch (Exception ex3)
			{
				VendorDeletedMessage = "ERROR: Unexpected error in saving vendor";
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
		View localView = ViewFactory.createView ( "SearchVendorView", this );
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
	protected void createAndShowDeleteVendorView()
	{
		// create our initial view
		View localView = ViewFactory.createView ( "DeleteVendorView", this );
		swapToView(localView);
	}
}