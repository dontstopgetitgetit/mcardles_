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
import userinterface.View;
import userinterface.ViewFactory;

//==============================================================
public class AddInventoryItemTypeTransaction extends Transaction
{
	private String itemTypeAddedMessage = "";
	private String itemTypeAlreadyExistsMessage = "";

	private InventoryItemType newItemType = null;

	//----------------------------------------------------------
	public AddInventoryItemTypeTransaction () throws Exception
	{
		super();
	}

	//----------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();

		dependencies.setProperty( "SaveItemType", "ItemTypeAddedMessage");
		dependencies.setProperty( "FindItemType", "ItemTypeAlreadyExistsMessage");
		dependencies.setProperty( "Cancel", "CancelTransaction");

		myRegistry.setDependencies( dependencies );
	}


	//-----------------------------------------------------------
	public Object getState( String key )
	{
		if (key.equals("ItemTypeAddedMessage"))
		{
			return itemTypeAddedMessage;
		}
		else if (key.equals("ItemTypeAlreadyExistsMessage")) 
		{
				return itemTypeAlreadyExistsMessage;
		}
		return null;
	}

	//-----------------------------------------------------------
	public void stateChangeRequest( String key, Object value )
	{
		// DEBUG System.out.println("AddInventoryItemTypeTransaction.sCR: key = " + key + "; value = " + value);

		itemTypeAddedMessage = "";
		itemTypeAlreadyExistsMessage = "";
		
		if ( key.equals( "DoYourJob" ) )
		{
			// as the sequence diagram states, create and show a view here
			doYourJob();
		}
		else if ( key.equals( "SaveItemType" ) )
		{
			processInventoryItemTypeData ((Properties) value);
		}
		else if (key.equals("FindItemType") )
		{
			processItemTypeName((Properties)value);
		}

		myRegistry.updateSubscribers ( key, this );
	}

	// This method handles the sending of a Worker's user name to this object. If the user name
	// already exists in the database, this method displays an error message popup.
	//----------------------------------------------------------
	private void processItemTypeName(Properties props)
	{
		InventoryItemType existingItemType = null;
		try
		{
			String itemTypeName = props.getProperty("ItemTypeName");
			
			existingItemType = new InventoryItemType(itemTypeName);
			itemTypeAlreadyExistsMessage = "ERROR: Inventory Item Type with name: " + 
					itemTypeName + " already exists.\n" +
					" Please use the 'Modify Inventory Item Type' button to change information about an existing worker.";
		}
		catch (InvalidPrimaryKeyException ex)
		{
			itemTypeAlreadyExistsMessage = "";
		}
	}

	// This method handles the sending of set of data associated with the Inventory Item Type to be added
	// to this object. It ASSUMES that the type name and barcode prefix sent are not already present in the database,
	// AND DOES NOT CHECK FOR ITS NON-EXISTENCE.
	//----------------------------------------------------------
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
							itemTypeAddedMessage = "ERROR: Validity days must be -1 for a non-age-sensitive item";
						}
						else
						{
							String notesString = values.getProperty("Notes");
							
							Properties prop = new Properties();
							prop.setProperty("ItemTypeName", itemTypeName);
							prop.setProperty("BarcodePrefix", barcodePrefix);
							prop.setProperty("Units", units);
							prop.setProperty("UnitMeasure", unitsMeasure);
							prop.setProperty("ReorderPoint", reorderPoint);
							prop.setProperty("AgeSensitive", ageSensitive);
							prop.setProperty("ValidityDays", validityDays);
							prop.setProperty("Notes", notesString);
							prop.setProperty("Status", "Active");
							
							newItemType = new InventoryItemType(prop);
							
							try
							{
								newItemType.save();
								itemTypeAddedMessage = "Item Type with name: " + itemTypeName + 
										" successfully saved!";
							}
							catch (SQLException ex)
							{
								itemTypeAddedMessage = "ERROR: Item Type data could not be saved: " + ex.getMessage();
								new Event(Event.getLeafLevelClassName(this), "processInventoryItemTypeData",
										"Error in saving Inventory Item Type data: " + ex.toString(), Event.ERROR);
							}	
							catch (Exception ex3)
							{
								itemTypeAddedMessage = "ERROR: Unexpected error in saving item type";
								new Event(Event.getLeafLevelClassName(this), "processInventoryItemTypeData",
										"Unexpected Error in saving Inventory Item Type data: " + ex3.toString(), Event.ERROR);
							}
						}
					}
					catch (Exception ex2)
					{
						itemTypeAddedMessage = "ERROR: Validity days value must be an integer";
						ex2.printStackTrace();
					}
				}
				catch (Exception ex1)
				{
					itemTypeAddedMessage = "ERROR: Re-order point value must be a number";
				}
			}
			catch (Exception ex)
			{
				itemTypeAddedMessage = "ERROR: Units received value is not an integer";
			}
		
		}
		else
		{
			itemTypeAddedMessage = "ERROR: Some Inventory Item Type data missing or invalid!";
		}
	}
	
	//----------------------------------------------------------------
	// Create the view of this class. 
	//---------------------------------------------------------------
	protected View createView()
	{

		// create our initial view
		View localView = ViewFactory.createView ( "AddInventoryItemTypeView", this );

		return localView;
	}

}
