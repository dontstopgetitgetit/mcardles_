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

import java.sql.SQLException;
import java.util.Properties;

import event.Event;
import exception.InvalidPrimaryKeyException;

import userinterface.View;
import userinterface.ViewFactory;

//============================================================================================
public class BrowseInventoryExceptionsTransaction extends Transaction{

	private String inventoryExceptionSearchMessage = "";
	private String inventoryExceptionCollectionMessage = "";

	private InventoryException existingException = null;
	private InventoryExceptionCollection inventoryExceptionColl = null;
	
	//----------------------------------------------------------------------------------
	protected BrowseInventoryExceptionsTransaction() throws Exception {
		super();
		
	}
	
	//----------------------------------------------------------
	public BrowseInventoryExceptionsTransaction (String Id) throws Exception
	{
		super();
		
		try
		{
			existingException = new InventoryException(Id);
		}
		catch (InvalidPrimaryKeyException ex)
		{
			existingException = null;
		}
	}

	//----------------------------------------------------------
	public BrowseInventoryExceptionsTransaction (InventoryException ie) throws Exception
	{
		super();
		
		existingException = ie;
	}

	//----------------------------------------------------------------------------------
	@Override
	protected void setDependencies() {
		dependencies = new Properties();

		dependencies.setProperty( "SearchExceptions", "InventoryExceptionSearchMessage");
		dependencies.setProperty( "Cancel", "CancelTransaction");

		myRegistry.setDependencies( dependencies );
	}

	//----------------------------------------------------------------------------------
	@Override
	protected View createView() {
		// create our initial view
		View localView = ViewFactory.createView ( "BrowseInventoryExceptionsView", this );

		return localView;
	}

	//----------------------------------------------------------------------------------
	@Override
	public Object getState(String key) {
		
		if (key.equals("InventoryExceptionList"))
		{
			if (inventoryExceptionColl != null)
			{
				return inventoryExceptionColl.getState("InventoryExceptionList");
			}
		}
		else if (key.equals("InventoryExceptionSearchMessage")) 
		{
				return inventoryExceptionSearchMessage;
		}
		if (existingException != null)
		{
			return existingException.getState(key);
		}
		
		return null;
	}

	//----------------------------------------------------------------------------------
	@Override
	public void stateChangeRequest(String key, Object value) {
		// DEBUG System.out.println("AddInventoryExceptionTransaction.sCR: key = " + key + "; value = " + value);

		inventoryExceptionSearchMessage = "";
		inventoryExceptionCollectionMessage = "";
		
		if ( key.equals( "DoYourJob" ) )
		{
			// as the sequence diagram states, create and show a view here
			doYourJob();
		}
		else if (key.equals("SearchExceptions") )
		{
			processExceptionSearch((Properties)value);
		}
		else if ( key.equals( "CancelInventoryExceptionList" ) )
		{
			View v = createView();
			swapToView(v);
		}

		myRegistry.updateSubscribers ( key, this );
	}

	//----------------------------------------------------------------------------------
	private void processExceptionSearch(Properties value) {
		try
		{
			Properties props = (Properties)value;
			inventoryExceptionColl = new InventoryExceptionCollection(props);

			createAndShowBrowseInventoryExceptionsCollectionView();
		}
		catch (InvalidPrimaryKeyException ex)
		{
			inventoryExceptionSearchMessage = "No inventory exceptions matching sent criteria found";
		}
	}

	//----------------------------------------------------------------
	// Create the inventory item type collection view. 
	//---------------------------------------------------------------
	protected void createAndShowBrowseInventoryExceptionsCollectionView()
	{

		// create our initial view
		View localView = ViewFactory.createView ( "BrowseInventoryExceptionsCollectionView", this );

		swapToView(localView);
	}
	

}
