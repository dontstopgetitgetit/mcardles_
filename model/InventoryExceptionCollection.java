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

package model;
//system imports
import java.util.Properties;
import java.util.Vector;

//project imports
import exception.InvalidPrimaryKeyException;
import impresario.IView;
import model.EntityBase;

//==============================================================
public class InventoryExceptionCollection extends EntityBase implements IView
{
	protected Properties mySchema;
	private Properties dependencies;

	private static final String myTableName = "InventoryException";

	// Search results
	private Vector inventoryExceptionCollection;

	// Error and Message strings
	private String inventoryExceptionCollectionErrorMessage = "";
	
	
	/*
	 * This constructor below searches for InventoryException objects that match
	 * the sent search criteria
	 */
	//----------------------------------------------------------
	public InventoryExceptionCollection(Properties props) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		// Set the dependencies
		setDependencies();

		// Refresh the inventoryException collection
		inventoryExceptionCollection = new Vector();

		String inventoryExceptionDate = props.getProperty("ExceptionDate");
		
		String query = "SELECT * FROM " + myTableName;
		
		if ((inventoryExceptionDate != null) && (inventoryExceptionDate.length() > 0))
		{
			query += " WHERE (ExceptionDate >= '" + inventoryExceptionDate + "')";
		}
		
		query += " ORDER BY ExceptionDate DESC"; // CHANGED TO "DESC" - MITRA 4/6/13
	
		Vector allDataRetrieved = getSelectQueryResult(query);
		
		if (allDataRetrieved.size() != 0)
		{
			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextInventoryExceptionData = (Properties)allDataRetrieved.elementAt(cnt);

				InventoryException nextInventoryException = new InventoryException(nextInventoryExceptionData);

				if (nextInventoryException != null)
				{
					inventoryExceptionCollection.addElement((InventoryException)nextInventoryException);
				}
				
			}
		}
		else
		{
			
			throw new InvalidPrimaryKeyException("No inventoryExceptions found for date: "
					+ inventoryExceptionDate);
		}
	
	}	
	
	//----------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();
			
		myRegistry.setDependencies(dependencies);
	}
	
	// Retrieve an inventoryException from our scout collection
	//----------------------------------------------------------
	public InventoryException retrieve(Properties prps)
	{
		InventoryException retValue = null;

		String inventoryExceptionId = prps.getProperty("Id");

		for (int cnt = 0; cnt < inventoryExceptionCollection.size(); cnt++)
		{
			InventoryException nextInventoryException = (InventoryException) 
					inventoryExceptionCollection.elementAt(cnt);
			String nextInventoryExceptionName = (String) nextInventoryException.getState("Id");
		
			if ((inventoryExceptionId != null) && (inventoryExceptionId.length() > 0))
			{
				if (nextInventoryExceptionName.equals(inventoryExceptionId))
				{	
					retValue = nextInventoryException;
					break;
				}
			}
		}

		return retValue;
	}
	
	// Remove the inventoryException from the collection
	//----------------------------------------------------------
	public void remove(InventoryException inventoryException)
	{
		inventoryExceptionCollection.remove(inventoryException);
	}
	
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if(key.equals("InventoryExceptionList"))
			return inventoryExceptionCollection;
		else
		if(key.equals("InventoryExceptionCollection"))
			return this;

		else
		if(key.equals("InventoryExceptionCollectionErrorMessage"))
			return inventoryExceptionCollectionErrorMessage;

		return null;
	}
	
	/** Called via the IView relationship */
	//----------------------------------------------------------
	public void updateState(String key, Object value)
	{
		stateChangeRequest(key, value);
	}
	
	//----------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		myRegistry.updateSubscribers(key, this);
	}

	//----------------------------------------------------------
	protected void initializeSchema(String tableName) 
	{
		if (mySchema == null) {
			mySchema = getSchemaInfo(tableName);
		}
	}
}