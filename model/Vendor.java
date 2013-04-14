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

/** The class contains the Vendor for the McArdle's application */
//==============================================================

public class Vendor extends EntityBase
{
	private static final String myTableName = "Vendor";

	protected Properties dependencies;
	
	private String updateStatusMessage = "";
	private String deleteStatusMessage = "";

	//------------------------------------------------------------------------------
	public Vendor ( int id ) 
			throws InvalidPrimaryKeyException
	{
		super( myTableName );

		setDependencies();

		String query = "SELECT * FROM " + myTableName + " WHERE Id  = " + 
				id + "";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// Because we are running the query with a primary key
			// in the WHERE clause, there should be exactly one item.
			// This one is impossible to, b/c ID is auto-incremental.
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple Vendors matching the " +
						"sent id: " + id + " found");
			}
			else
			{
				Properties retrievedVendorData = (Properties) allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedVendorData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String) allKeys.nextElement();
					String nextValue = retrievedVendorData.getProperty(nextKey);

					if (nextValue != null)
					{
						persistentState.setProperty(nextKey, nextValue);
					}
				}
			}
		}
		// If no vendors found, throw an axception. Hopefully, we'll rarely get there.
		else
		{
			throw new InvalidPrimaryKeyException("No Vendors matching the " +
						"sent id: " + id + " found");
		}

	}// end of Vendor constructor with the id only
		
	//------------------------------------------------------------------------------
	public Vendor ( String vendorName, String phone ) 
			throws InvalidPrimaryKeyException
	{
		super( myTableName );

		setDependencies();
	
		String query = "SELECT * FROM " + myTableName + " WHERE Name = '" + 
				vendorName + "' AND PhoneNumber = '" + phone + "'";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// Because we are running the query with a primary key
			// in the WHERE clause, there should be exactly one item.
			// This one is impossible to, b/c ID is auto-incremental.
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple vendors matching the " +
						"sent name and Id found");
			}
			else
			{
				Properties retrievedVendorData = (Properties) allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedVendorData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String) allKeys.nextElement();
					String nextValue = retrievedVendorData.getProperty(nextKey);

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
			throw new InvalidPrimaryKeyException("No vendors matching with the " +
					"sent name and id found!");
		}

	}// end of vendor constructor with the Id
	
	//------------------------------------------------------------------------------
	public Vendor ( String Name ) 
			throws InvalidPrimaryKeyException
	{
		super( myTableName );

		setDependencies();

		String query = "SELECT * FROM " + myTableName + " WHERE Name  = '" + 
				Name + "'";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// Because we are running the query with a primary key
			// in the WHERE clause, there should be exactly one item.
			// This one is impossible to, b/c ID is auto-incremental.
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple Vendors matching the " +
						"sent name: " + Name + " found");
			}
			else
			{
				Properties retrievedVendorData = (Properties) allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration allKeys = retrievedVendorData.propertyNames();
				while (allKeys.hasMoreElements() == true)
				{
					String nextKey = (String) allKeys.nextElement();
					String nextValue = retrievedVendorData.getProperty(nextKey);

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
			throw new InvalidPrimaryKeyException("No Vendors matching the " +
						"sent name: " + Name + " found");
		}

	}// end of InventoryVendor constructor with the item type name only

	// Create a blank new item type
	//----------------------------------------------------------------------
	public Vendor (  )
	{
		super( myTableName );

		setDependencies();

		persistentState = new Properties();
		
		initializeState();
	}
	
	// Create a new or old Vendor with a Properties object
	//----------------------------------------------------------------------
	public Vendor ( Properties props)
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

	} // end of InventoryVendor constructor with the supplied data

	
		
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
		persistentState.setProperty("Name", "");
		persistentState.setProperty("PhoneNumber", "");
		persistentState.setProperty("Status", "Active");
	}
	
	//----------------------------------------------------------------
	public Vector getEntryListView()
	{
		Vector v = new Vector();
		v.addElement(persistentState.getProperty("Name"));
		//v.addElement(persistentState.getProperty("Id"));
		v.addElement(persistentState.getProperty("PhoneNumber"));
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
			String vendorId = persistentState.getProperty("Id");
			
			
			if(vendorId != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("Id",  persistentState.getProperty("Id"));

				updatePersistentState( mySchema, persistentState, whereClause );

				updateStatusMessage = "Vendor updated successfully!";
			}
			else
			{
				Integer newVendorId = insertAutoIncrementalPersistentState(mySchema, persistentState);

				persistentState.setProperty("Id", "" + newVendorId.intValue());
				
				updateStatusMessage = "New Vendor saved successfully!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing Vendor data in database!";
			throw ex;
		}
		
	} // end of the method updateStateInDatabase()


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
	
	//----------------------------------------------------------------------
	public void setAttribute(String key, String value)
	{
		persistentState.setProperty( key, value );
	}	
	//----------------------------------------------------------------------

}