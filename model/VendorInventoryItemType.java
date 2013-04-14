// specify the package
//************************************************************
//	COPYRIGHT 2011 Sandeep Mitra and students, The
//    College at Brockport, State University of New York. -
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

//project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.VendorInventoryItemTypeNotFoundException;
import database.*;

import impresario.IView;

/** The class contains the VendorInventoryItemType for the BoyScout application */
//==============================================================

public class VendorInventoryItemType extends EntityBase
{
	private static final String myTableName = "VendorInventoryItemType";

	protected Properties dependencies;
	
	private String updateStatusMessage = "";
	private String deleteStatusMessage = "";

	//------------------------------------------------------------------------------
	public VendorInventoryItemType ( String vendorID, String inventoryItemTypeName ) 
			throws InvalidPrimaryKeyException, VendorInventoryItemTypeNotFoundException
	{
		super( myTableName );

		setDependencies();
	
		String query = "SELECT * FROM " + myTableName + " WHERE VendorId = '" + 
				vendorID + "' AND InventoryItemTypeName = '" + inventoryItemTypeName + "'";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// Because we are running the query with a primary key
			// in the WHERE clause, there should be exactly one item.
			// This one is impossible to, b/c ID is auto-incremental.
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple vendor inventory item types matching the " +
						"sent vendorId and inventory item type");
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
			}
		}
		// If no items find, throw en axception. We'll never get there.
		else
		{
			throw new VendorInventoryItemTypeNotFoundException("No vendor inventory item types matching with the " +
					"sent vendorId and inventory item type name found!");
		}

	}// end of VendorInventoryItemType constructor with the barcode
	
	
	// Create a blank new item type
	//----------------------------------------------------------------------
	public VendorInventoryItemType (  )
	{
		super( myTableName );

		setDependencies();
		
		persistentState = new Properties();
		
		initializeState();
	}
	
	// Create a new or old item type with a Properties object
	//----------------------------------------------------------------------
	public VendorInventoryItemType ( Properties props )
	{
		super( myTableName );

		setDependencies();
		

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

	} // end of VendorInventoryItemType constructor with the supplied data

		
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
	
		persistentState.setProperty("VendorId", "");
		persistentState.setProperty("InventoryItemTypeName", "");
		persistentState.setProperty("VendorsDescription", "");
		persistentState.setProperty("CurrentPrice", "");
		persistentState.setProperty("PriceUnitIndicator", "");
		
		Calendar rightNow = Calendar.getInstance();
		Date todaysDate = rightNow.getTime();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String todayDateString = formatter.format(todaysDate);
		
		persistentState.setProperty("DateOfLastUpdate", todayDateString);
		persistentState.setProperty("VendorsItemNumber", "");

	}
	
	//----------------------------------------------------------------
	public Vector getEntryListView()
	{
		Vector v = new Vector();
		v.addElement(persistentState.getProperty("VendorId"));
		v.addElement(persistentState.getProperty("InventoryItemTypeName"));
		v.addElement(persistentState.getProperty("VendorsDescription"));
		v.addElement(persistentState.getProperty("CurrentPrice"));
		v.addElement(persistentState.getProperty("PriceUnitIndicator"));
		v.addElement(persistentState.getProperty("DateOfLastUpdate"));
		v.addElement(persistentState.getProperty("VendorsItemNumber"));
		
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
			String id = persistentState.getProperty("Id");
			if(id != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("Id",  persistentState.getProperty("Id"));

				updatePersistentState( mySchema, persistentState, whereClause );

				updateStatusMessage = "Vendor pricing data updated successfully!";
			}
			else
			{
				Integer newId = insertAutoIncrementalPersistentState(mySchema, persistentState);

				persistentState.setProperty("Id", newId.intValue() + "");

				updateStatusMessage = "New Vendor Pricing data saved successfully!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing vendor pricing data in database!";
			throw ex;
		}
		
	} // end of the method updateStateInDatabase()


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

			whereClause.setProperty ( "InventoryItemTypeName", persistentState.getProperty( "InventoryItemTypeName" ) );
			whereClause.setProperty ("VendorId", persistentState.getProperty( "VendorId" ) );

			deletePersistentState( mySchema, whereClause );

			deleteStatusMessage = "Vendor Pricing information was DELETED successfully!";		


		}
		catch ( SQLException ex )
		{
			deleteStatusMessage = "Error in deleting vendor pricing information in database!";
		}

	} // end of the method deleteStateInDatabase() 

	//-----------------------------------------------------------
	public Object getState ( String key )
	{
		if ( key.equals( "UpdateStatusMessage" )) 
		{
			return updateStatusMessage;
		}

		return persistentState.getProperty ( key );
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