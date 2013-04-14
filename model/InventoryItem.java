// specify the package
//************************************************************
//	COPYRIGHT 2013 Sandeep Mitra and students (Andrew Allen,
//  Randy Pawlikowski, Cory Beer-Cunningham, Evan Fleischer,
//  Edward Mallow, Kevin Kelly, Philip Kneupfer, Jonathan H Shields,
//  Kyle Root, Jason La Mendola, Anthony Morse, Brian Humphrey,
//  Loi Truong, Kevin Murphy) 
//    The College at Brockport, State University of New York. -
//	  ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//************************************************************
//
package model;

//system imports
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;
import java.text.SimpleDateFormat;
import java.text.ParseException;

//project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;

/** The class contains the InventoryItem for the BoyScout application */
//==============================================================

public class InventoryItem extends EntityBase
{
	private static final String myTableName = "InventoryItem";

	protected Properties dependencies;
	
	protected InventoryItemType myItemType = null;
	protected Invoice myInvoice = null;
	
	protected boolean inventoryItemNew = false;

	private String updateStatusMessage = "";
	private String deleteStatusMessage = "";

	//------------------------------------------------------------------------------
	public InventoryItem ( String barcode ) 
			throws InvalidPrimaryKeyException
	{
		super( myTableName );

		setDependencies();
		inventoryItemNew = false;

		String query = "SELECT * FROM " + myTableName + " WHERE Barcode = '" + 
				barcode + "'";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// Because we are running the query with a primary key
			// in the WHERE clause, there should be exactly one item.
			// This one is impossible to, b/c ID is auto-incremental.
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple inventory items matching the " +
						"sent barcode: " + barcode + " found");
			}
			else
			{
				Properties retrievedItemTypeData = (Properties) allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedItemTypeData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String) allKeys.nextElement();
					String nextValue = retrievedItemTypeData.getProperty(nextKey);

					if (nextValue != null)
					{
						persistentState.setProperty(nextKey, nextValue);
					}
				}
				
				// Instantiate the associated inventory item type
				try
				{
					myItemType = new InventoryItemType(persistentState.getProperty("ItemTypeName"));
				}
				catch (InvalidPrimaryKeyException ex)
				{
					// SHOULD NEVER HAPPEN
				}
				
				// Instantiate the associated invoice
				try
				{
					myInvoice = new Invoice(persistentState.getProperty("InvoiceId"));
				}
				catch (InvalidPrimaryKeyException ex)
				{
					// SHOULD NEVER HAPPEN
				}
			}
		}
		// If no items find, throw en axception. We'll never get there, hopefully.
		else
		{
			throw new InvalidPrimaryKeyException("No inventory items matching the " +
					"sent barcode:" + barcode + " found!");
			
		}

	}// end of InventoryItem constructor with the barcode
	
	// Create a blank new item type
	//----------------------------------------------------------------------
	public InventoryItem (  )
	{
		super( myTableName );

		setDependencies();
		inventoryItemNew = true;

		persistentState = new Properties();
		
		initializeState();
	}
	
	// Create a new or old item type with a Properties object
	//----------------------------------------------------------------------
	public InventoryItem ( Properties props , boolean itemTypeNew)
	{
		super( myTableName );

		setDependencies();
		inventoryItemNew = itemTypeNew;

		persistentState = new Properties();
		initializeState();

		Enumeration allKeys = props.propertyNames();
		while (allKeys.hasMoreElements() == true)
		{
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			if (nextValue != null)
			{
				persistentState.setProperty(nextKey, nextValue);
			}
		}

	} // end of InventoryItem constructor with the supplied data

	// create a new inventory item type
	//----------------------------------------------------------------------
	public InventoryItem ( Properties props)
	{
		this (props, true);
	}
		
	//----------------------------------------------------------------
	protected void setDependencies()
	{
		dependencies = new Properties();
		// YOUR CODE HERE (IF ANY)
		myRegistry.setDependencies(dependencies);
	}
	
	//----------------------------------------------------------------
	public void initializeState()
	{
		persistentState.setProperty("Barcode", "");
		persistentState.setProperty("ItemTypeName", "");
		persistentState.setProperty("InvoiceId", "");
		persistentState.setProperty("BrandName", "");
		persistentState.setProperty("ActualUnitsReceived", "");
		persistentState.setProperty("CurrentPrice", "");
		persistentState.setProperty("PriceUnitIndicator", "PerUnit");
		Calendar rightNow = Calendar.getInstance();
		Date todayDate = rightNow.getTime();

		String todayDateString = convertToDefaultDateFormat( todayDate );
		persistentState.setProperty("DateOfLastUse", todayDateString);
		
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String nowTimeString = formatter.format(todayDate);
		persistentState.setProperty("TimeOfLastUse", nowTimeString);
		
		persistentState.setProperty("CurrentLevelOfUnits", "");
		persistentState.setProperty("UsageStatus", "Available");
		persistentState.setProperty("LastUseWorkerId", "");
		persistentState.setProperty("Notes", "");
	}
	
	//----------------------------------------------------------------
	public Vector getEntryListView()
	{
		Vector v = new Vector();
		v.addElement(persistentState.getProperty("Barcode"));
		v.addElement(persistentState.getProperty("ItemTypeName"));
		v.addElement(persistentState.getProperty("BrandName"));
		v.addElement(persistentState.getProperty("ActualUnitsReceived"));
		v.addElement(persistentState.getProperty("CurrentLevelOfUnits"));
		
		String dateValueString = persistentState.getProperty("DateOfLastUse");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
		String dateToDisplayString = "";
		
		try
		{
			Date d = sdf1.parse(dateValueString);
			SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
			dateToDisplayString = sdf2.format(d);
		}
		catch (ParseException ex)
		{
			dateToDisplayString = "Unknown Date";
		}
		v.addElement(dateToDisplayString);
	
		String timeValueString = persistentState.getProperty("TimeOfLastUse");
		SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
		String timeToDisplayString = "";
		
		try
		{
			Date d = sdf3.parse(timeValueString);
			SimpleDateFormat sdf4 = new SimpleDateFormat("hh:mm a");
			timeToDisplayString = sdf4.format(d);
		}
		catch (ParseException ex)
		{
			timeToDisplayString = "Unknown Time";
		}
		v.addElement(timeToDisplayString);
		
		v.addElement(persistentState.getProperty("UsageStatus"));
		v.addElement(persistentState.getProperty("Notes"));
		
		return v;
	}
		
	//----------------------------------------------------------------
	public void save() throws SQLException
	{
		updateStateInDatabase();
	}
		
	//----------------------------------------------------------------
	protected void updateStateInDatabase() throws SQLException
	{
		try
		{
			if(inventoryItemNew == false)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("Barcode",  persistentState.getProperty("Barcode"));

				updatePersistentState( mySchema, persistentState, whereClause );

				updateStatusMessage = "Inventory Item updated successfully!";
			}
			else
			{
				insertPersistentState(mySchema, persistentState);

				inventoryItemNew = false;

				updateStatusMessage = "New Inventory Item saved successfully!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing inventory item data in database!";
			throw ex;
		}
		
	} // end of the method updateStateInDatabase()


	//-----------------------------------------------------------
	public void set(String key, String value)
	{
		persistentState.setProperty(key, value);
	}
	
	//-----------------------------------------------------------
	public void delete() throws SQLException
	{
		deleteStateInDatabase();
	}
	
	//-------------------------------------------------------------------
	// MODEL MAPPING: 
	//------------------------------------------------------------------
	private void deleteStateInDatabase( ) throws SQLException
	{
		
		try
		{
			Properties whereClause = new Properties();
			whereClause.setProperty ( "Barcode", persistentState.getProperty( "Barcode" ) );
			deletePersistentState( mySchema, whereClause );
			deleteStatusMessage = "Inventory item with barcode " + persistentState.getProperty( "Barcode" )
														   + " was DELETED successfully!";		


		}
		catch ( SQLException ex )
		{
			deleteStatusMessage = "Error in deleting inventory item with barcode: " + 
						persistentState.getProperty( "Barcode" ) + " in database!";
		}

	} // end of the method deleteStateInDatabase() 

	//-----------------------------------------------------------
	public Object getState ( String key )
	{
		if ( key.equals( "UpdateStatusMessage" )) 
		{
			return updateStatusMessage;
		}

		String retVal = persistentState.getProperty ( key );
		
		if (retVal != null)
			return retVal;
		else
		{
			if (myItemType != null)
			{
				return myItemType.getState(key);
			}
			else
			if (myInvoice != null)
			{
				return myInvoice.getState(key);
			}
		}
		
		return null;
	}

	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState( String key, Object value )
	{
		stateChangeRequest( key, value );
	}


	//-----------------------------------------------------------
	public void stateChangeRequest( String key, Object value )
	{
		if (value instanceof String)
		{
			persistentState.setProperty(key, (String)value);
		}

		myRegistry.updateSubscribers( key, this );
	} // end of stateChangeRequest method

	//----------------------------------------------------------------
	protected void initializeSchema( String tableName )
	{
		if ( mySchema == null )
		{
			mySchema = getSchemaInfo( tableName );
		}
	} // end of initializeSchema method

}