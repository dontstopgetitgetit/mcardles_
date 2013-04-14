// specify the package
//************************************************************
//	COPYRIGHT 2013 Sandeep Mitra and students, The
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
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

//project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;

/** The class contains the InventoryItemType for the BoyScout application */
//==============================================================

public class InventoryItemType extends EntityBase
{
	private static final String myTableName = "InventoryItemType";

	protected Properties dependencies;
	
	protected boolean inventoryItemTypeNew = false;

	private String updateStatusMessage = "";

	//------------------------------------------------------------------------------
	public InventoryItemType ( String itemTypeName, String barcodePrefix ) 
			throws InvalidPrimaryKeyException
	{
		super( myTableName );

		setDependencies();
		inventoryItemTypeNew = false;

		String query = "SELECT * FROM " + myTableName + " WHERE ItemTypeName = '" + 
				itemTypeName + "' AND BarcodePrefix = '" + barcodePrefix + "'";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// Because we are running the query with a primary key
			// in the WHERE clause, there should be exactly one item.
			// This one is impossible to, b/c ID is auto-incremental.
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple item types matching the " +
						"sent name and barcode prefix found");
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
			throw new InvalidPrimaryKeyException("No item types matching with the " +
					"sent name and barcode prefix found!");
		}

	}// end of InventoryItemType constructor with the barcode
	
	//------------------------------------------------------------------------------
	public InventoryItemType ( String itemTypeName ) 
			throws InvalidPrimaryKeyException
	{
		super( myTableName );

		setDependencies();
		inventoryItemTypeNew = false;

		String query = "SELECT * FROM " + myTableName + " WHERE ItemTypeName = '" + 
				itemTypeName + "'";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// Because we are running the query with a primary key
			// in the WHERE clause, there should be exactly one item.
			// This one is impossible to, b/c ID is auto-incremental.
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple item types matching the " +
						"sent name: " + itemTypeName + " found");
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
			throw new InvalidPrimaryKeyException("No item types matching the " +
						"sent name: " + itemTypeName + " found");
		}

	}// end of InventoryItemType constructor with the item type name only

	// Create a blank new item type
	//----------------------------------------------------------------------
	public InventoryItemType (  )
	{
		super( myTableName );

		setDependencies();
		inventoryItemTypeNew = true;

		persistentState = new Properties();
		
		initializeState();
	}
	
	// Create a new or old item type with a Properties object
	//----------------------------------------------------------------------
	public InventoryItemType ( Properties props , boolean itemTypeNew)
	{
		super( myTableName );

		setDependencies();
		inventoryItemTypeNew = itemTypeNew;

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

	} // end of InventoryItemType constructor with the supplied data

	// create a new inventory item type
	//----------------------------------------------------------------------
	public InventoryItemType ( Properties props)
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
		persistentState.setProperty("ItemTypeName", "");
		persistentState.setProperty("Units", "");
		persistentState.setProperty("UnitMeasure", "");
		persistentState.setProperty("ValidityDays", "");
		persistentState.setProperty("BarcodePrefix", "");
		persistentState.setProperty("ReorderPoint", "");
		persistentState.setProperty("AgeSensitive", "No");
		persistentState.setProperty("Notes", "");
		persistentState.setProperty("Status", "Active");
	}
	
	//----------------------------------------------------------------
	public Vector getEntryListView()
	{
		Vector v = new Vector();
		v.addElement(persistentState.getProperty("ItemTypeName"));
		v.addElement(persistentState.getProperty("BarcodePrefix"));
		v.addElement(persistentState.getProperty("AgeSensitive"));
		v.addElement(persistentState.getProperty("ValidityDays"));
		v.addElement(persistentState.getProperty("Notes"));
		v.addElement(persistentState.getProperty("Status"));
		
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
			if(inventoryItemTypeNew == false)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("ItemTypeName",  persistentState.getProperty("ItemTypeName"));

				updatePersistentState( mySchema, persistentState, whereClause );

				updateStatusMessage = "Item Type updated successfully!";
			}
			else
			{
				insertPersistentState(mySchema, persistentState);

				inventoryItemTypeNew = false;

				updateStatusMessage = "New Item Type saved successfully!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing item type data in database!";
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
		
		persistentState.setProperty("Status", "Inactive");
			
		Properties whereClause = new Properties();
		whereClause.setProperty ( "ItemTypeName", persistentState.getProperty( "ItemTypeName" ) );

		updateStateInDatabase();

		if (updateStatusMessage.startsWith("Error") == false)
			updateStatusMessage = "Inventory Item Type with Name: " + persistentState.getProperty( "ItemTypeName" )
															   + " successfully rendered inactive!";
		else
			updateStatusMessage = updateStatusMessage;

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