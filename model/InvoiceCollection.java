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

import impresario.IView;

import java.util.Properties;
import java.util.Vector;

import exception.InvalidPrimaryKeyException;

//=========================================================================
public class InvoiceCollection extends EntityBase implements IView
{
	protected Properties mySchema;
	private Properties dependencies;

	private static final String myTableName = "Invoice";

	// Search results
	private Vector invoiceCollection;

	// Error and Message strings
	private String invoiceCollectionErrorMessage = "";

	/*
	 * This constructor below searches for Invoice objects that match
	 * the sent search criteria
	 */
	//----------------------------------------------------------
	public InvoiceCollection(Properties props) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		// Set the dependencies
		setDependencies();

		// Refresh the Invoice collection
		invoiceCollection = new Vector();

		String vId = props.getProperty("VendorId");
		String vInNumber = props.getProperty("VendorInvoiceNumber");
		
		
		String query = "SELECT * FROM " + myTableName;  //+ 
				//" Where ((Status = 'Active'))";
		
		if ((vId != null) && (vId.length() > 0))
		{
			query += " WHERE ((VendorId = " + vId + ")";
			if ((vInNumber != null) && (vInNumber.length() > 0))
			{
				query += " AND (VendorInvoiceNumber LIKE '%" + vInNumber + "%'))";
			}
			else
			{
				query += ")";
			}
		}
		else
		if ((vInNumber != null) && (vInNumber.length() > 0))
		{
			query += " WHERE ((VendorInvoiceNumber LIKE '%" + vInNumber + "%'))";
		}
		query += " Order By DateReceived DESC";
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextInvoiceData = (Properties)allDataRetrieved.elementAt(cnt);

				Invoice nextInvoice = new Invoice(nextInvoiceData);

				if (nextInvoice != null)
				{
					invoiceCollection.addElement(nextInvoice);
				}
			}
		}
		else
		{
			throw new InvalidPrimaryKeyException("No invoices found.");
		}
	
	}
	
	//----------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();
		
		myRegistry.setDependencies(dependencies);
	}

	// Retrieve an inventory item type from our scout collection
	//----------------------------------------------------------
	public Invoice retrieve(Properties prps)
	{
		Invoice retValue = null;

		String vId = prps.getProperty("VendorId");
		String vInNumber = prps.getProperty("VendorInvoiceNumber");
		
		for (int cnt = 0; cnt < invoiceCollection.size(); cnt++)
		{
			Invoice nextInvoice = (Invoice) 
					invoiceCollection.elementAt(cnt);
			String nextVId = (String) nextInvoice.getState("VendorId");
			String nextVInNumber = (String) nextInvoice.getState("VendorInvoiceNumber");
		
			if ((vId != null) && (vId.length() > 0))
			{
				if (nextVId.equals(vId))
				{
					if ((vInNumber != null) && (vInNumber.length() > 0))
					{
						if (nextVInNumber.equals(vInNumber))
						{
							retValue = nextInvoice;
							break;
						}
					}
					else
					{
						retValue = nextInvoice;
						break;
					}
				}
			}
			else
			{
				if ((vInNumber  != null) && (vInNumber .length() > 0))
				{
					if (nextVInNumber.equals(vInNumber))
					{
						retValue = nextInvoice;
						break;
					}
				}
			}
			
		}
	

		return retValue;
	}


	// Remove the invoice from the collection
	//----------------------------------------------------------
	public void remove(Invoice invoice)
	{
		invoiceCollection.remove(invoice);
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		if(key.equals("InvoiceList"))
			return invoiceCollection;
		else
		if(key.equals("InvoiceCollection"))
			return this;

		else
		if(key.equals("InvoiceCollectionErrorMessage"))
			return invoiceCollectionErrorMessage;

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
	protected void initializeSchema(String tableName) {
		if (mySchema == null) {
			mySchema = getSchemaInfo(tableName);
		}
	}
}
