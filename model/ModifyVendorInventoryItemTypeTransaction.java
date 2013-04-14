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
import userinterface.MainFrame;
import userinterface.View;
import userinterface.ViewFactory;

//==============================================================
public class ModifyVendorInventoryItemTypeTransaction extends Transaction
{
	private String VendorItemTypeAlreadyExistsMessage = "";
	private String vendorItemTypeAddedMessage = "";

	
	private VendorInventoryItemType existingVendorItemType;
	private String vendorName;
	

	private InventoryItemTypeCollection itemTypeColl = null;

	//----------------------------------------------------------
	public ModifyVendorInventoryItemTypeTransaction () throws Exception
	{
		super();
	}


	//----------------------------------------------------------
	public ModifyVendorInventoryItemTypeTransaction (VendorInventoryItemType et) throws Exception
	{
		super();
		
		existingVendorItemType = et;
	}
	
	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();

		dependencies.setProperty( "FindVendorInventoryItemType", "VendorItemTypeAlreadyExistsMessage");
		dependencies.setProperty( "SaveVendorItemType", "VendorItemTypeAddedMessage");
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
			return vendorItemTypeAddedMessage;
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
		// DEBUG System.out.println("ModifyVendorInventoryItemTypeTransaction.sCR: key = " + key + "; value = " + value);

		VendorItemTypeAlreadyExistsMessage = "";


		if ( key.equals( "DoYourJob" ) )
		{
			// as the sequence diagram states, create and show a view here
			doYourJob();
		}
		else if ( key.equals( "SaveVendorItemType" ) )
		{
			Properties sentData = (Properties)value;
			processVendorInventoryItemTypeData (sentData);
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

	
	
	// This method handles the sending of set of data associated with the Inventory Item Type to be added
	// to this object. It ASSUMES that the type name and barcode prefix sent are already present in the database,
	// AND DOES NOT CHECK FOR ITS EXISTENCE.
	//----------------------------------------------------------
	private void processVendorInventoryItemTypeData(Properties values)
	{
		
		boolean itemTypeDataOK = false;
		String vendorId = values.getProperty("VendorId");
		String itemTypeName = values.getProperty("InventoryItemTypeName");
		String vendorsDescription = values.getProperty("VendorDescription");
		String currentPrice = values.getProperty("CurrentPrice");
		String priceUnitIndicator = values.getProperty("PriceUnitIndicator");
		String dateOfLastUpdate = values.getProperty("DateOfLastUpdate");
		String vendorsItemNumber = values.getProperty("VendorsItemNumber");
		
		if ( ((vendorId != null) && (vendorId.length() > 0)) &&
				((itemTypeName != null) && (itemTypeName.length() > 0)) &&
				((currentPrice != null) && (currentPrice.length() > 0)) )
		{
			try
			{
				double currentPriceVal = Double.parseDouble(currentPrice);
				// DEBUG System.out.println("current price:" + currentPriceVal);
				String oldPriceValString = (String) existingVendorItemType.getState("CurrentPrice");
				double oldPriceVal = Double.parseDouble(oldPriceValString);
				// DEBUG System.out.println("old price:" + oldPriceVal);
				//Check to see how much the price changed
				if(currentPriceVal > oldPriceVal)
				{
					if(currentPriceVal > (oldPriceVal + (oldPriceVal * 0.60) ))
					{
						// DEBUG System.out.println("old price threshold:" + (oldPriceVal + (oldPriceVal * 0.60)));
						String message = "WARNING: The new price entered is 60% or more higher than the current price of $ " 
								+ oldPriceVal + ".\n " + "Please confirm the new price.";
						JOptionPane.showMessageDialog(MainFrame.getInstance(), message, 
								"New Price Warning", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				else
				if (currentPriceVal < oldPriceVal)
				{
					if(currentPriceVal < (oldPriceVal - (oldPriceVal * .60) ))
					{
							String message = "WARNING: The new price entered is 60% or more lower than the current price of $ " 
								+ oldPriceVal + ".\n " + "Please confirm the new price.";
							JOptionPane.showMessageDialog(MainFrame.getInstance(), message, 
									"New Price Warning", JOptionPane.ERROR_MESSAGE);
							return;
					}
				}
					
				existingVendorItemType.stateChangeRequest("VendorId", vendorId); // MITRA 4/6/13: REALLY NECESSARY?
				existingVendorItemType.stateChangeRequest("InventoryItemTypeName", itemTypeName); // MITRA 4/6/13: REALLY NECESSARY?
				existingVendorItemType.stateChangeRequest("VendorsDescription", vendorsDescription);
				existingVendorItemType.stateChangeRequest("CurrentPrice", currentPrice);
				existingVendorItemType.stateChangeRequest("PriceUnitIndicator", priceUnitIndicator);
				existingVendorItemType.stateChangeRequest("DateOfLastUpdate", dateOfLastUpdate);
				existingVendorItemType.stateChangeRequest("VendorsItemNumber", vendorsItemNumber);		
				
				try
				{
					existingVendorItemType.save();
					vendorItemTypeAddedMessage = "Vendor Pricing info for item: " + itemTypeName + 
							" successfully saved!";
				}
				catch (SQLException ex)
				{
					vendorItemTypeAddedMessage = "ERROR: Vendor Pricing info could not be saved";
					new Event(Event.getLeafLevelClassName(this), "processInventoryItemTypeData",
							"Error in saving Vendor Pricing info: " + ex.toString(), Event.ERROR);
				}	
				catch (Exception ex3)
				{
					vendorItemTypeAddedMessage = "ERROR: Unexpected error in saving Vendor Pricing info";
					new Event(Event.getLeafLevelClassName(this), "processInventoryItemTypeData",
							"Unexpected Error in saving Vendor Pricing info: " + ex3.toString(), Event.ERROR);
				}
			}
			catch (NumberFormatException ex)
			{
				vendorItemTypeAddedMessage  = "ERROR: Current Price received value is not valid";
			}
		
		}
		else
		{
			vendorItemTypeAddedMessage  = "ERROR: Some Vendor Pricing data missing or invalid!";
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

	
	//-----------------------------------------------------------------------------------------
	private void processVendorAndItemType(Properties props)
	{
		vendorName = props.getProperty("vName");
	
		
		existingVendorItemType = null;
		try
		{
			
			String vendorItemID = props.getProperty("VendorID");
			String inventoryItemTypeName = props.getProperty("ItemTypeName");
			String vName = props.getProperty("vName");
			
			existingVendorItemType = new VendorInventoryItemType(vendorItemID, inventoryItemTypeName);
		
		
			createAndShowModifyVendorInventoryItemView();
			
		}
		catch (InvalidPrimaryKeyException ex)
		{
			VendorItemTypeAlreadyExistsMessage = "ERROR: Unexpected error in retrieving VendorInventoryItemType";
		}
		catch (VendorInventoryItemTypeNotFoundException ex2)
		{
			
			VendorItemTypeAlreadyExistsMessage = "ERROR: Pricing info for this vendor/item combination does not exist.\n " + 
					" Please use the 'Add Vendor Item Pricing' button to add \n " +
					" information about a new Vendor Pricing info for an Item.";
		}
	}
	//----------------------------------------------------------------
	// Create the modify inventory item type view. 
	//---------------------------------------------------------------
	protected void createAndShowModifyVendorInventoryItemView()
	{

		// create our initial view
		View localView = ViewFactory.createView ( "ModifyVendorInventoryItemTypeView", this );

		swapToView(localView);
	}

}
