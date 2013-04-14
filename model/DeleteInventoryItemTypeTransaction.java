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

//=====================================================================================
public class DeleteInventoryItemTypeTransaction extends Transaction{

	private String itemTypeDeletedMessage = "";
	private String itemTypeCollectionMessage = "";

	private InventoryItemType existingItemType = null;
	private InventoryItemTypeCollection itemTypeColl = null;

	//----------------------------------------------------------
	public DeleteInventoryItemTypeTransaction() throws Exception
	{
		super();
	}
	
	//----------------------------------------------------------
	public DeleteInventoryItemTypeTransaction(String itemTypeName, String barcodePrefix) throws Exception
	{
		super();
			
			try
			{
				existingItemType = new InventoryItemType(itemTypeName, barcodePrefix);
			}
			catch (InvalidPrimaryKeyException ex)
			{
				existingItemType = null;
			}
	}
	//----------------------------------------------------------------

	public DeleteInventoryItemTypeTransaction (InventoryItemType et) throws Exception
	{	
		super();
	
		existingItemType = et;
	}
	//----------------------------------------------------------
	
	protected void setDependencies()
	{
		dependencies = new Properties();

		dependencies.setProperty( "SaveItemType", "ItemTypeDeletedMessage");
		dependencies.setProperty( "SearchItemType", "ItemTypeCollectionMessage");
		dependencies.setProperty( "Cancel", "CancelTransaction");

		myRegistry.setDependencies( dependencies );
	}
	//-----------------------------------------------------------
	
	public Object getState( String key )
	{
		if (key.equals("ItemTypeDeletedMessage"))
		{
			return itemTypeDeletedMessage;
		}
		else
		if (key.equals("ItemTypeCollectionMessage"))
		{
			return itemTypeCollectionMessage;
		}
		else
		if (key.equals("InventoryItemTypeList"))
		{
			if (itemTypeColl != null)
			{
				return itemTypeColl.getState("InventoryItemTypeList");
			}
		}	
		else
		if (existingItemType != null)
		{
			return existingItemType.getState(key);
		}
		
		return null;
	}
	//-----------------------------------------------------------
	
	public void stateChangeRequest( String key, Object value )
	{
		// DEBUG System.out.println("ModifyInventoryItemTypeTransaction.sCR: key = " + key + "; value = " + value);

		itemTypeDeletedMessage = "";
		itemTypeCollectionMessage = "";

		if ( key.equals( "DoYourJob" ) )
		{
			// as the sequence diagram states, create and show a view here
			doYourJob();
		}
		else if ( key.equals( "SaveItemType" ) )
		{
			Properties sentData = (Properties)value;
			boolean itemTypeExists = checkIfItemTypeExists(sentData.getProperty("ItemTypeName"),
					sentData.getProperty("BarcodePrefix"));
			if (itemTypeExists == false)
			{
				processInventoryItemTypeData (sentData);
			}
			else
			{
				itemTypeDeletedMessage = "ERROR: Item Type with specified name and barcode already exists";
			}
		}
		else if ( key.equals( "ItemTypeSelected" ) )
		{
			String itemTypeName = (String)value;
			Properties props = new Properties();
			props.setProperty("ItemTypeName", itemTypeName);
			
			existingItemType = itemTypeColl.retrieve(props);
			
			createAndShowDeleteInventoryItemView();
			
		}
		else if ( key.equals( "SearchItemType" ) )
		{
			try
			{
				Properties props = (Properties)value;
				itemTypeColl = new InventoryItemTypeCollection(props);
				
				createAndShowItemTypeCollectionView();
			}
			catch (InvalidPrimaryKeyException ex)
			{
				itemTypeCollectionMessage = "No item types matching sent criteria found";
			}
		}
		else if ( key.equals( "CancelInventoryItemTypeList" ) )
		{
			View v = createView();
			swapToView(v);
		}

		myRegistry.updateSubscribers ( key, this );
	}
	//-------------------------------------------------------------------------------
	
	// This method handles the sending of set of data associated with the Inventory Item Type to be added
	// to this object. It ASSUMES that the type name and barcode prefix sent are not already present in the database,
	// AND DOES NOT CHECK FOR ITS EXISTENCE.
	private void processInventoryItemTypeData(Properties values)
	{
		boolean itemTypeDataOK = false;
		String itemTypeName = values.getProperty("ItemTypeName");
		String barcodePrefix = values.getProperty("BarcodePrefix");
		String units = values.getProperty("Units");
		String unitsMeasure = values.getProperty("UnitMeasure");
		String reorderPoint = values.getProperty("ReorderPoint");
		String ageSensitive = values.getProperty("AgeSensitive");
		String validityDays = values.getProperty("ValidityDays");
		
		if ( ((itemTypeName != null) && (itemTypeName.length() > 0)) &&
				((barcodePrefix != null) && (barcodePrefix.length() > 0)) &&
				((unitsMeasure != null) && (unitsMeasure.length() > 0)) )
		{
			try
			{
				int unitsVal = Integer.parseInt(units);
				try
				{
					double reorderPointVal = Double.parseDouble(reorderPoint);
					try
					{
						double validityDaysVal = Integer.parseInt(validityDays);
						
						if ((ageSensitive.equals("No")) && (validityDaysVal != -1))
						{
							itemTypeDeletedMessage = "ERROR: Validity days must be -1 for a non-age-sensitive item";
						}
						else
						{
							String notesString = values.getProperty("Notes");
							
							existingItemType.stateChangeRequest("ItemTypeName", itemTypeName);
							existingItemType.stateChangeRequest("BarcodePrefix", barcodePrefix);
							existingItemType.stateChangeRequest("Units", units);
							existingItemType.stateChangeRequest("UnitMeasure", unitsMeasure);
							existingItemType.stateChangeRequest("ReorderPoint", reorderPoint);
							existingItemType.stateChangeRequest("AgeSensitive", ageSensitive);
							existingItemType.stateChangeRequest("ValidityDays", validityDays);
							existingItemType.stateChangeRequest("Notes", notesString);
							existingItemType.stateChangeRequest("Status", "Inactive");
							
							//existingItemType.stateChangeRequest("Status", "Active");
							
							try
							{
								//existingItemType.delete();
								existingItemType.save();
								itemTypeDeletedMessage = "Item Type with name: " + itemTypeName + 
										" successfully deleted!";
							}
							catch (SQLException ex)
							{
								itemTypeDeletedMessage = "ERROR: Item Type data could not be deleted";
								new Event(Event.getLeafLevelClassName(this), "processInventoryItemTypeData",
										"Error in deleting Inventory Item Type data: " + ex.toString(), Event.ERROR);
							}	
							catch (Exception ex3)
							{
								itemTypeDeletedMessage = "ERROR: Unexpected error in deleting item type";
								new Event(Event.getLeafLevelClassName(this), "processInventoryItemTypeData",
										"Unexpected Error in deleting Inventory Item Type data: " + ex3.toString(), Event.ERROR);
							}
						}
					}
					catch (Exception ex2)
					{
						itemTypeDeletedMessage = "ERROR: Validity days value must be an integer";
						ex2.printStackTrace();
					}
				}
				catch (Exception ex1)
				{
					itemTypeDeletedMessage = "ERROR: Re-order point value must be a number";
				}
			}
			catch (Exception ex)
			{
				itemTypeDeletedMessage = "ERROR: Units received value is not an integer";
			}	
		}
		else
		{
			itemTypeDeletedMessage = "ERROR: Some Inventory Item Type data missing or invalid!";
		}
	}
	//----------------------------------------------------------------
	
	private boolean checkIfItemTypeExists(String itemTypeName, String barcodePrefix)
	{
		InventoryItemType duplicateItemType = null;
		String existingItemTypeName = (String)existingItemType.getState("ItemTypeName");
		String existingItemTypeBarcodePrefix = (String)existingItemType.getState("BarcodePrefix");
		
		try
		{
			duplicateItemType = new InventoryItemType(itemTypeName, barcodePrefix);
			if ((existingItemTypeName.equals(itemTypeName)) && 
					(existingItemTypeBarcodePrefix.equals(barcodePrefix)))
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
	
	
	//----------------------------------------------------------------
	// Create the view of this class. 
	//---------------------------------------------------------------
	protected View createView()
	{
		// create our initial view
		View localView = ViewFactory.createView ( "SearchInventoryItemTypeView", this );
		return localView;
	}
	
	//----------------------------------------------------------------
	// Create the inventory item type collection view. 
	//---------------------------------------------------------------
	protected void createAndShowItemTypeCollectionView()
	{
		// create our initial view
		View localView = ViewFactory.createView ( "InventoryItemTypeCollectionView", this );
		swapToView(localView);
	}
	
	//----------------------------------------------------------------
	// Create the modify inventory item type view. 
	//---------------------------------------------------------------
	protected void createAndShowDeleteInventoryItemView()
	{
		// create our initial view
		View localView = ViewFactory.createView ( "DeleteInventoryItemTypeView", this );
		swapToView(localView);
	}
}
