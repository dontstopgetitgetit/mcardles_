


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

//project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import database.*;

import impresario.IView;

/** The class contains the Worker for the McArdle's application */
//==============================================================

public class Worker extends EntityBase implements IView
{
	private static final String myTableName = "Worker";

	protected Properties dependencies;

	private String updateStatusMessage = "";

	private String deleteStatusMessage = "";

	//--------------------------------------------------------------
	// This constructor is invoked by AddWorkerTransaction 
	//---------------------------------------------------------------
	public Worker ( Properties newWorker )
	{
		super( myTableName );

		setDependencies();

		// copy data from newWorker parameter into persistentState
		preparePersistentState ( newWorker );
		
		String workerStatus = persistentState.getProperty("Status");
		if (workerStatus == null)
		{
			persistentState.setProperty("Status", "Active");
		}

	} // end of Worker constructor with new user-supplied data


	//----------------------------------------------------------------------
	// MODEL MAPPING: 
	// ----------------------------------------------------------------------
	public Worker ( String name ) throws InvalidPrimaryKeyException
	{
		super( myTableName );

		setDependencies();


		String query = "SELECT * FROM " + myTableName + " WHERE " +
				"(Name = '" + name + "')";

		Vector allDataRetrieved = getSelectQueryResult( query );

		// Throw the Exception if no Worker with the specified barcode is found in the DB
		if ( allDataRetrieved == null || allDataRetrieved.size() == 0 )
		{
			throw new InvalidPrimaryKeyException ( "ERROR: No Worker matching Name: "
					+ name + " found.");

		}
		// Throw the Exception if multiple Workers match the supplied barcode in the DB
		else if ( allDataRetrieved.size() != 1 )
		{
			throw new InvalidPrimaryKeyException( "ERROR: Multiple Workers matching Name: "
					+ name + " found.");
		}
		else
		{
			Properties retrievedWorkerData = ( Properties )allDataRetrieved.elementAt( 0 );

			// copy data from retrievedWorkerData into persistentState
			preparePersistentState ( retrievedWorkerData );
		}
	} // end of Worker constructor with barcode supplied
	

	//----------------------------------------------------------------------
	// MODEL MAPPING: 
	// ----------------------------------------------------------------------
	public Worker ( int id ) throws InvalidPrimaryKeyException
	{
		super( myTableName );

		setDependencies();


		String query = "SELECT * FROM " + myTableName + " WHERE " +
				"(Id = " + id + ")";

		Vector allDataRetrieved = getSelectQueryResult( query );

		// Throw the Exception if no Worker with the specified barcode is found in the DB
		if ( allDataRetrieved == null || allDataRetrieved.size() == 0 )
		{
			throw new InvalidPrimaryKeyException ( "ERROR: No Worker matching Id: "
					+ id + " found.");

		}
		// Throw the Exception if multiple Workers match the supplied barcode in the DB
		else if ( allDataRetrieved.size() != 1 ) // SHOULD NEVER HAPPEN
		{
			throw new InvalidPrimaryKeyException( "ERROR: Multiple Workers matching id: "
					+ id + " found.");
		}
		else
		{
			Properties retrievedWorkerData = ( Properties )allDataRetrieved.elementAt( 0 );

			// copy data from retrievedWorkerData into persistentState
			preparePersistentState ( retrievedWorkerData );
		}
	} // end of Worker constructor with barcode supplied
	

	//----------------------------------------------------------------------
	// MODEL MAPPING: 
	// ----------------------------------------------------------------------
	public Worker ( String name, String pwd ) throws InvalidPrimaryKeyException,
		PasswordMismatchException
	{
		this(name);
		String workerStatus = persistentState.getProperty("Status");
		if (workerStatus.equals("Inactive") == true)
			throw new InvalidPrimaryKeyException("Worker with name: " + name + " is inactive");
		String retrievedPassword = persistentState.getProperty("WorkerPassword");
		if (retrievedPassword.equals(pwd) == false)
			throw new PasswordMismatchException("Password mismatch: Login failure");
	}
	
	
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
	// models/controllers of this object ( such as AddWorkerTransaction,
	// UpdateWorkerTransaction, DeleteWorkerTransaction)
	//----------------------------------------------------------------
	public Object getState ( String key )
	{
		// AddWorkerTransaction and UpdateWorkerTransaction need the statusMassage
		if ( key.equals( "Save") == true)
		{
			return updateStatusMessage;
		}
		// UpdateWorkerTransaction needs the most recent data 
		else if ( key.equals( "WorkerData" ))
		{
			return persistentState;			
		}
		// DeleteWorkerTransaction needs the statusMessage
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
			String workerId = persistentState.getProperty("Id");
			
			if ( workerId != null ) 
			{
				Properties whereClause = new Properties();

				whereClause.setProperty ( "Id", workerId);

				updatePersistentState( mySchema, persistentState, whereClause );

				updateStatusMessage = "Worker with name: " + persistentState.getProperty ( "Name" ) 
														   + " UPDATED successfully in database!";
			}
			else 
			{
				Calendar rightNow = Calendar.getInstance();
				Date todayDate = rightNow.getTime();

				String todayDateString = convertToDefaultDateFormat( todayDate );
				persistentState.setProperty( "DateAdded", todayDateString );

				Integer newWorkerId = insertAutoIncrementalPersistentState( mySchema, persistentState );
				persistentState.setProperty("Id", "" + newWorkerId.intValue());

				updateStatusMessage = "New Worker with name " + persistentState.getProperty ( "Name" ) 
															   + " INSERTED successfully in database!";				
			}
		}
		catch ( SQLException ex )
		{
			updateStatusMessage = "Error in saving Worker data into database!";
			throw ex;
		}

	} // end of the method updateStateInDatabase() 

	//-------------------------------------------------------------------
	// MODEL MAPPING: 
	//------------------------------------------------------------------
	private void deleteStateInDatabase( ) throws SQLException
	{
		
		persistentState.setProperty("Status", "Inactive");
			
		Properties whereClause = new Properties();

		whereClause.setProperty ( "Id", persistentState.getProperty( "Id" ) );

		updateStateInDatabase();

		if (updateStatusMessage.startsWith("Error") == false)
			deleteStatusMessage = "Worker with Name: " + persistentState.getProperty( "Name" )
														   + " successfully rendered inactive!";
		else
			deleteStatusMessage = updateStatusMessage;

	} // end of the method updateStateInDatabase() 



	//-----------------------------------------------------------------------------------
	private void preparePersistentState ( Properties newState )
	{
		// copy all of the supplied info from Properties newState 
		// into persistentState, so the entire Worker object is prepared 
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