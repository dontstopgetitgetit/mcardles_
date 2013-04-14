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
public class AddVendorInventoryItemTypeTransaction extends Transaction
{
	private String VendorItemTypeAlreadyExistsMessage = "";
	private String VendorItemTypeAddedMessage = "";

	private VendorInventoryItemType newItemType = null;
	private VendorInventoryItemType existingVendorItemType;
	
	private Properties vProps;
	//----------------------------------------------------------
	public AddVendorInventoryItemTypeTransaction () throws Exception
	{
		super();
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();

		dependencies.setProperty( "SaveVendorItemType", "VendorItemTypeAddedMessage");
		dependencies.setProperty( "FindVendorInventoryItemType", "VendorItemTypeAlreadyExistsMessage");
		dependencies.setProperty( "Cancel", "CancelTransaction");

		myRegistry.setDependencies( dependencies );
	}


	//-----------------------------------------------------------
	public Object getState( String key )
	{
		if (key.equals("VendorItemTypeAddedMessage"))
		{
			return VendorItemTypeAddedMessage;
		}
		else if (key.equals("VendorItemTypeAlreadyExistsMessage")) 
		{
				return VendorItemTypeAlreadyExistsMessage;
		}
		else if (key.equals("vProps"))
		{
			return vProps;
		}
		return null;
	
	}

	//-----------------------------------------------------------
	public void stateChangeRequest( String key, Object value )
	{
		// DEBUG System.out.println("AddInventoryItemTypeTransaction.sCR: key = " + key + "; value = " + value);

		VendorItemTypeAddedMessage = "";
		VendorItemTypeAlreadyExistsMessage = "";
		
		if ( key.equals( "DoYourJob" ) )
		{
			// as the sequence diagram states, create and show a view here
			doYourJob();
		}
		else if ( key.equals( "SaveVendorItemType" ) )
		{
			processVendorInventoryItemTypeData ((Properties) value);
		}
		else if (key.equals("FindVendorInventoryItemType") )
		{
			processVendorAndItemType((Properties)value);
		}
	

		myRegistry.updateSubscribers ( key, this );
	}

	// This method handles the sending of a Worker's user name to this object. If the user name
	// already exists in the database, this method displays an error message popup.
	//----------------------------------------------------------
	private void processVendorAndItemType(Properties props)
	{
		
		existingVendorItemType = null;
		try
		{
			
			String vendorItemID = props.getProperty("VendorID");
			String inventoryItemTypeName = props.getProperty("ItemTypeName");
			String vName = props.getProperty("vName");
			vProps = new Properties();
			vProps.setProperty("vId", vendorItemID);
			vProps.setProperty("iName", inventoryItemTypeName);
			vProps.setProperty("vName", vName);
			
			
			existingVendorItemType = new VendorInventoryItemType(vendorItemID, inventoryItemTypeName);
		
			VendorItemTypeAlreadyExistsMessage = "ERROR: Pricing info for this vendor/item combination already exists.\n " + 
					" Please use the 'Modify Vendor Item Pricing' button to change \n " +
					" information about an existing Vendor's pricing info for an Item.";
			
		}
		catch (InvalidPrimaryKeyException ex)
		{
			VendorItemTypeAlreadyExistsMessage = "ERROR: Unexpected error in retrieving VendorInventoryItemType";
		}
		catch (VendorInventoryItemTypeNotFoundException ex2)
		{
			
			VendorItemTypeAlreadyExistsMessage = "";
			createAndShowAddVendorInventoryItemTypeView();
		}
	}

	
	//----------------------------------------------------------
	private void processVendorInventoryItemTypeData(Properties values)
	{
		boolean VendorItemTypeDataOK = false;
		String vendorItemTypeId = values.getProperty("VendorId");
		String inventoryItemName = values.getProperty("InventoryItemTypeName");
		String vendorsDescription = values.getProperty("VendorsDescription");
		String currentPrice = values.getProperty("CurrentPrice");
		String priceUnitIndicator = values.getProperty("PriceUnitIndicator");
		String dateOfLastUpdate = values.getProperty("DateOfLastUpdate");
		String vendorsItemNumber = values.getProperty("VendorsItemNumber");

		if ( ((vendorItemTypeId != null) && (vendorItemTypeId.length() > 0)) &&
				((inventoryItemName != null) && (inventoryItemName.length() > 0)) &&
				((currentPrice != null) && (currentPrice.length() > 0)) )
		{
			try
			{
				double currentPriceVal = Double.parseDouble(currentPrice);
		
				Properties prop = new Properties();
				prop.setProperty("VendorId", vendorItemTypeId);
				prop.setProperty("InventoryItemTypeName", inventoryItemName);
				prop.setProperty("VendorsDescription", vendorsDescription);
				prop.setProperty("CurrentPrice", currentPrice);
				prop.setProperty("PriceUnitIndicator", priceUnitIndicator);
				prop.setProperty("DateOfLastUpdate", dateOfLastUpdate);
				prop.setProperty("VendorsItemNumber", vendorsItemNumber);
				
				newItemType = new VendorInventoryItemType(prop);
				
				// DEBUG System.out.println(newItemType);
				
				try
				{
					newItemType.save();
					VendorItemTypeAddedMessage = "Vendor Pricing info successfully saved!";
				}
				catch (SQLException ex)
				{
					VendorItemTypeAddedMessage = "ERROR: Vendor Pricing Info could not be saved: " + ex.getMessage();
					new Event(Event.getLeafLevelClassName(this), "processInventoryItemTypeData",
							"Error in saving Vendor Inventory Item Type data: " + ex.toString(), Event.ERROR);
				}	
			}
			catch (NumberFormatException ex)
			{
				VendorItemTypeAddedMessage = "ERROR: Current Price received value is not valid";
			}	
		}
		else
		{
			VendorItemTypeAddedMessage = "ERROR: Some Pricing Information missing or invalid!";
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
	protected void createAndShowAddVendorInventoryItemTypeView()
	{
		View localView = ViewFactory.createView ( "AddVendorInventoryItemTypeView", this);

		swapToView ( localView );

	}
	
	
}
