//************************************************************
//    COPYRIGHT 2013, Sandeep Mitra, Jason La Mendola and Students, 
//		The College at Brockport - ALL RIGHTS RESERVED
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
import exception.PasswordMismatchException;
import database.*;

import impresario.IView;

/** The class contains the InventoryException for the McArdle's application */
//==============================================================

public class InventoryException extends EntityBase implements IView
{
	private static final String myTableId = "InventoryException";

	protected Properties dependencies;

	private String updateStatusMessage = "";

	private String deleteStatusMessage = "";

	//--------------------------------------------------------------
	// This constructor is invoked by AddInventoryExceptionTransaction 
	//---------------------------------------------------------------
	public InventoryException ( Properties newInventoryException )
	{
		super( myTableId );

		setDependencies();

		// copy data from newInventoryException parameter into persistentState
		preparePersistentState ( newInventoryException );
		
	} // end of InventoryException constructor with new user-supplied data


	//----------------------------------------------------------------------
	// MODEL MAPPING: 
	// ----------------------------------------------------------------------
	public InventoryException ( String id ) throws InvalidPrimaryKeyException
	{
		super( myTableId );

		setDependencies();


		String query = "SELECT * FROM " + myTableId + " WHERE " +
				"(Id = '" + id + "')";

		Vector allDataRetrieved = getSelectQueryResult( query );

		// Throw the Exception if no InventoryException with the specified barcode is found in the DB
		if ( allDataRetrieved == null || allDataRetrieved.size() == 0 )
		{
			throw new InvalidPrimaryKeyException ( "ERROR: No InventoryException matching Id: "
					+ id + " found.");

		}
		// Throw the Exception if multiple InventoryExceptions match the supplied barcode in the DB
		else if ( allDataRetrieved.size() != 1 )
		{
			throw new InvalidPrimaryKeyException( "ERROR: Multiple InventoryExceptions matching Id: "
					+ id + " found.");
		}
		else
		{
			Properties retrievedInventoryExceptionData = ( Properties )allDataRetrieved.elementAt( 0 );

			// copy data from retrievedInventoryExceptionData into persistentState
			preparePersistentState ( retrievedInventoryExceptionData );
		}
	} // end of InventoryException constructor with barcode supplied
	
	//========== Section 1: methods related to Observer Pattern ==================== 
	//----------------------------------------------------------
	// Called via the IView relationship 
	//----------------------------------------------------------
	public void updateState( String key, Object value )
	{
		stateChangeRequest( key, value );
	}

	//----------------------------------------------------------------------
	public void stateChangeRequest( String key, Object value )
	{
		setAttribute( key, (String) value );
		
		myRegistry.updateSubscribers( key, this );

	} // end of stateChangeRequest method	

	//----------------------------------------------------------------------
	public void setAttribute(String key, String value)
	{
		persistentState.setProperty( key, value );
	}
	
	//----------------------------------------------------------------
	// Give back via Observer pattern the information needed to the
	// models/controllers of this object ( such as AddInventoryExceptionTransaction,
	// UpdateInventoryExceptionTransaction, DeleteInventoryExceptionTransaction)
	//----------------------------------------------------------------
	public Object getState ( String key )
	{
		// AddInventoryExceptionTransaction and UpdateInventoryExceptionTransaction need the statusMassage
		if ( key.equals( "Save") == true)
		{
			return updateStatusMessage;
		}
		// UpdateInventoryExceptionTransaction needs the most recent data 
		else if ( key.equals( "InventoryExceptionData" ))
		{
			return persistentState;			
		}
		// DeleteInventoryExceptionTransaction needs the statusMessage
		else if ( key.equals( "Delete" ))
		{
			return deleteStatusMessage;
		}
		else
		{
			String theReturn = persistentState.getProperty ( key );

			return theReturn;
		}
	}
	//========== End of Section 1: methods related to Observer Pattern =========

	//-------------------------------------------------------------------
	public void save() throws SQLException
	{
		updateStateInDatabase();
	}
	
	//-------------------------------------------------------------------
	public void delete() throws SQLException
	{
		deleteStateInDatabase();
	}
		
	//-------------------------------------------------------------------
	// MODEL MAPPING: 
	//------------------------------------------------------------------
	private void updateStateInDatabase( ) throws SQLException
	{
		try
		{
			String exceptionId = persistentState.getProperty("Id");
			
			if ( exceptionId != null ) 
			{
				Properties whereClause = new Properties();

				whereClause.setProperty ( "Id", exceptionId);

				updatePersistentState( mySchema, persistentState, whereClause );

				updateStatusMessage = "InventoryException with id: " + persistentState.getProperty ( "Id" ) 
														   + " UPDATED successfully in database!";
			}
			else 
			{
				Calendar rightNow = Calendar.getInstance();
				Date todayDate = rightNow.getTime();

				String todayDateString = convertToDefaultDateFormat( todayDate );
				persistentState.setProperty( "ExceptionDate", todayDateString );

				Integer newInventoryExceptionId = insertAutoIncrementalPersistentState( mySchema, persistentState );
				persistentState.setProperty("Id", "" + newInventoryExceptionId.intValue());

				updateStatusMessage = "New InventoryException with id " + persistentState.getProperty ( "Id" ) 
															   + " INSERTED successfully in database!";				
			}
		}
		catch ( SQLException ex )
		{
			updateStatusMessage = "Error in saving InventoryException data into database!";
			throw ex;
		}

	} // end of the method updateStateInDatabase() 

	//-------------------------------------------------------------------
	// MODEL MAPPING: 
	//------------------------------------------------------------------
	private void deleteStateInDatabase( ) throws SQLException
	{
			
		Properties whereClause = new Properties();
		whereClause.setProperty ( "Id", persistentState.getProperty( "Id" ) );
		int deleted = deletePersistentState(mySchema, whereClause);
		if (deleted == 1)
			deleteStatusMessage = "InventoryException with Id: " + persistentState.getProperty( "Id" )
														   + " successfully removed from database!";
		else
			deleteStatusMessage = "ERROR: Inventory Exception with Id: " + persistentState.getProperty( "Id" )
														   + " could NOT be removed from database!";

	} // end of the method updateStateInDatabase() 

	//----------------------------------------------------------------
	public Vector getEntryListView()
	{
		Vector v = new Vector();
		v.addElement(persistentState.getProperty("Id"));
		String dateValueString = persistentState.getProperty("ExceptionDate");
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
		String timeValueString = persistentState.getProperty("ExceptionTime");
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
		
		try
		{
			Worker w = new Worker(Integer.parseInt(persistentState.getProperty("WorkerId")));
			v.addElement((String)w.getState("Name"));
		}
		catch (Exception ex)
		{ // SHOULD BE A RARE OCCURRENCE
			v.addElement("Unknown Worker");
		}
		v.addElement(persistentState.getProperty("BarcodeToUse"));
		v.addElement(persistentState.getProperty("BarcodeActuallyUsed"));
		v.addElement(persistentState.getProperty("Reason"));
			
		return v;
	}

	//-----------------------------------------------------------------------------------
	private void preparePersistentState ( Properties newState )
	{
		// copy all of the supplied info from Properties newState 
		// into persistentState, so the entire InventoryException object is prepared 
		// to be inserted/updated into DB
		Enumeration allKeys = newState.propertyNames();

		while ( allKeys.hasMoreElements() == true )
		{
			String nextKey = ( String )allKeys.nextElement ();

			String nextValue = newState.getProperty( nextKey );

			if ( nextValue != null )
			{
				persistentState.setProperty( nextKey, nextValue );
			}
		} // end of while has more key

	}

	//------------------------------------------------------------------------------------
	protected void initializeSchema( String tableName )
	{
		if ( mySchema == null )
		{
			mySchema = getSchemaInfo( tableName );
		}
	} // end of initializeSchema method


	//-----------------------------------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();
		myRegistry.setDependencies(dependencies);
	}

}