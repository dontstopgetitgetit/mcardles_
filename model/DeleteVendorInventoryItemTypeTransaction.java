//************************************************************
//    COPYRIGHT 2013, Sandeep Mitra and Students, The College at Brockport
//                         - ALL RIGHTS RESERVED
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
import exception.VendorInventoryItemTypeNotFoundException;
import userinterface.View;
import userinterface.ViewFactory;

//==============================================================
public class DeleteVendorInventoryItemTypeTransaction extends Transaction
{
	private String VendorItemTypeAlreadyExistsMessage = "";
	private String VendorItemTypeAddedMessage = "";
	
	private String vendorName;

	private VendorInventoryItemType existingVendorItemType = null;

	//----------------------------------------------------------
	public DeleteVendorInventoryItemTypeTransaction () throws Exception
	{
		super();
	}

	//----------------------------------------------------------
	public DeleteVendorInventoryItemTypeTransaction (VendorInventoryItemType et) throws Exception
	{
		super();
		
		existingVendorItemType = et;
	}
	
	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();

		dependencies.setProperty( "FindVendorInventoryItemType", "VendorItemTypeAlreadyExistsMessage");
		dependencies.setProperty( "DeleteVendorInventoryItemType", "VendorItemTypeAddedMessage");
		dependencies.setProperty( "Cancel", "CancelTransaction");

		myRegistry.setDependencies( dependencies );
	}


	//-----------------------------------------------------------
	public Object getState( String key )
	{
		if (key.equals("VendorItemTypeAlreadyExistsMessage"))
		{
			return VendorItemTypeAlreadyExistsMessage;
		}
		else
		if (key.equals("VendorItemTypeAddedMessage"))
		{
			return VendorItemTypeAddedMessage;
		}
		else
		if(key.equals("VendorName"))
		{
			return vendorName;
		}
		else
		if (existingVendorItemType != null)
		{
			return existingVendorItemType.getState(key);
		}
		
		return null;
	}

	//-----------------------------------------------------------
	public void stateChangeRequest( String key, Object value )
	{
		// DEBUG System.out.println("ModifyInventoryItemTypeTransaction.sCR: key = " + key + "; value = " + value);

		VendorItemTypeAlreadyExistsMessage = "";

		if ( key.equals( "DoYourJob" ) )
		{
			// as the sequence diagram states, create and show a view here
			doYourJob();
		}
		else if ( key.equals( "DeleteVendorInventoryItemType" ) )
		{
			processInventoryItemTypeData ();
		}
		else if (key.equals("FindVendorInventoryItemType") )
		{
			processVendorAndItemType((Properties)value);
		}
		else if ( key.equals( "CancelInventoryItemTypeList" ) )
		{
			View v = createView();
			swapToView(v);
		}

		myRegistry.updateSubscribers ( key, this );
	}

	
	//----------------------------------------------------------
	private void processInventoryItemTypeData()
	{
		// CHANGED BY MITRA 4/6/13
		// BETTER WAY OF DOING IT

		if (existingVendorItemType != null)
		{
			try
			{
				existingVendorItemType.delete();
				VendorItemTypeAddedMessage = "Vendor Pricing Information" +
						" successfully deleted!";
			}
			catch (SQLException ex)
			{
				VendorItemTypeAddedMessage = "ERROR: Vendor Pricing information could not be deleted";
				new Event(Event.getLeafLevelClassName(this), "processInventoryItemTypeData",
						"Error in deleting Vendor Pricing information: " + ex.toString(), Event.ERROR);
			}	
			catch (Exception ex3)
			{
				VendorItemTypeAddedMessage = "ERROR: Unexpected error in deleting vendor pricing information";
				new Event(Event.getLeafLevelClassName(this), "processInventoryItemTypeData",
						"Unexpected Error in deleting Vendor Pricing Information: " + ex3.toString(), Event.ERROR);
			}
		}
	}
	
	//----------------------------------------------------------------------------------------
	private void processVendorAndItemType(Properties props)
	{
		vendorName = props.getProperty("vName");
		
		existingVendorItemType = null;
		try
		{
			
			String vendorItemID = props.getProperty("VendorID");
			String inventoryItemTypeName = props.getProperty("ItemTypeName");
			
			existingVendorItemType = new VendorInventoryItemType(vendorItemID, inventoryItemTypeName);
		
			createAndShowDeleteVendorInventoryItemView();
			
		}
		catch (InvalidPrimaryKeyException ex)
		{
			VendorItemTypeAlreadyExistsMessage = "ERROR: Unexpected error in retrieving VendorInventoryItemType";
		}
		catch (VendorInventoryItemTypeNotFoundException ex2)
		{
		
			VendorItemTypeAlreadyExistsMessage = "ERROR: Pricing info for this vendor/item combination does not exist.\n " + 
					" Please use the 'Add Vendor Item Pricing' button to add \n " +
					" information about new Vendor Pricing information.";
		}
	}
	
	//----------------------------------------------------------------
	// Create the view of this class. 
	//---------------------------------------------------------------
	protected View createView()
	{
		// create our initial view
		View localView = ViewFactory.createView ( "SearchVendorInventoryItemTypeView", this );

		return localView;
	}


	//----------------------------------------------------------------
	// Create the modify inventory item type view. 
	//---------------------------------------------------------------
	protected void createAndShowDeleteVendorInventoryItemView()
	{

		// create our initial view
		View localView = ViewFactory.createView ( "DeleteVendorInventoryItemTypeView", this );

		swapToView(localView);
	}

}
