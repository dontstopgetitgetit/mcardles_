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

/** The class contains the Invoice for the BoyScout application */
//==============================================================

public class Invoice extends EntityBase
{
	private static final String myTableName = "Invoice";

	protected Properties dependencies;
	
	private String updateStatusMessage = "";
	private String deleteStatusMessage = "";

	//------------------------------------------------------------------------------
	public Invoice ( String invoiceId ) 
			throws InvalidPrimaryKeyException
	{
		super( myTableName );

		setDependencies();

		String query = "SELECT * FROM " + myTableName + " WHERE Id = " + 
				invoiceId + "";

		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved.size() != 0)
		{
			int size = allDataRetrieved.size();

			// Because we are running the query with a primary key
			// in the WHERE clause, there should be exactly one item.
			// This one is impossible to, b/c ID is auto-incremental.
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple invoices matching the " +
						"id: " + invoiceId + " found");
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
		// If no invoices found, throw en axception. We'll never get there, hopefully.
		else
		{
			throw new InvalidPrimaryKeyException("No invoice matching the " +
					"sent id:" + invoiceId + " found!");
			
		}
	}// end of Invoice constructor with the id
	
	// Create a blank new item type
	//----------------------------------------------------------------------
	public Invoice (  )
	{
		super( myTableName );

		setDependencies();

		persistentState = new Properties();	
		initializeState();
	}
	
	// Create a new or old item type with a Properties object
	//----------------------------------------------------------------------
	public Invoice ( Properties props )
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

	} // end of Invoice constructor with the supplied data
		
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
		persistentState.setProperty("VendorInvoiceNumber", "");

		Calendar rightNow = Calendar.getInstance();
		Date todayDate = rightNow.getTime();

		String todayDateString = convertToDefaultDateFormat( todayDate );
		persistentState.setProperty("DateReceived", todayDateString);
		
		persistentState.setProperty("WorkerId", "");
	}
	
	//----------------------------------------------------------------
	public Vector getEntryListView()
	{
		Vector v = new Vector();
		v.addElement(persistentState.getProperty("Id"));
	
		try
		{
			Vendor vend = new Vendor(Integer.parseInt(persistentState.getProperty("VendorId")));
			v.addElement((String)vend.getState("Name"));
		}
		catch (Exception ex)
		{ // SHOULD BE A RARE OCCURRENCE
			v.addElement("Unknown Vendor");
		}
		
		v.addElement(persistentState.getProperty("VendorInvoiceNumber"));
		
		String dateValueString = persistentState.getProperty("DateReceived");
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
	
		try
		{
			Worker w = new Worker(Integer.parseInt(persistentState.getProperty("WorkerId")));
			v.addElement((String)w.getState("Name"));
		}
		catch (Exception ex)
		{ // SHOULD BE A RARE OCCURRENCE
			v.addElement("Unknown Worker");
		}
		
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
			String invoiceId = persistentState.getProperty("Id");
			
			
			if (invoiceId != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("Id",  persistentState.getProperty("Id"));

				updatePersistentState( mySchema, persistentState, whereClause );

				updateStatusMessage = "Invoice updated successfully!";
			}
			else
			{
				Integer newInvoiceId = insertAutoIncrementalPersistentState(mySchema, persistentState);

				persistentState.setProperty("Id", "" + newInvoiceId.intValue());
				
				updateStatusMessage = "New Invoice saved successfully!";
			}
		}
		catch (SQLException ex)
		{
			updateStatusMessage = "Error in installing Invoice data in database!";
			throw ex;
		}
		
	} // end of the method updateStateInDatabase()


	//-----------------------------------------------------------
	public void set(String key, String value)
	{
		persistentState.setProperty(key, value);
	}
	
	//-----------------------------------------------------------
	//public void delete() throws SQLException
	//{
	//	deleteStateInDatabase();
	//}
	
	//-------------------------------------------------------------------
	// MODEL MAPPING: 
	//------------------------------------------------------------------
	// SHOULD NEVER INVOKE THIS METHOD
	private void deleteStateInDatabase( ) throws SQLException
	{
		
		try
		{
			Properties whereClause = new Properties();
			whereClause.setProperty ( "Id", persistentState.getProperty( "Id" ) );
			deletePersistentState( mySchema, whereClause );
			deleteStatusMessage = "Invoice with id: " + persistentState.getProperty( "Id" )
														   + " was DELETED successfully!";		


		}
		catch ( SQLException ex )
		{
			deleteStatusMessage = "Error in deleting invoice with id: " + 
						persistentState.getProperty( "Id" ) + " in database!";
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
			return null;
		}
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