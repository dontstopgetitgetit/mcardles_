package model;

import impresario.IView;

import java.util.Properties;
import java.util.Vector;

import exception.InvalidPrimaryKeyException;

public class VendorCollection extends EntityBase implements IView
{
	protected Properties mySchema;
	private Properties dependencies;

	private static final String myTableName = "Vendor";

	// Search results
	private Vector vendorCollection;

	// Error and Message strings
	private String vendorCollectionErrorMessage = "";

	/*
	 * This constructor below searches for InventoryItemType objects that match
	 * the sent search criteria
	 */
	//----------------------------------------------------------
	public VendorCollection(Properties props) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		// Set the dependencies
		setDependencies();

		// Refresh the Vendor collection
		vendorCollection = new Vector();

		String vName = props.getProperty("Name");
		String vNumber = props.getProperty("PhoneNumber");
		
		
		String query = "SELECT * FROM " + myTableName;  //+ 
				//" Where ((Status = 'Active'))";
		
		if ((vName != null) && (vName.length() > 0))
		{
			query += " WHERE ((Name LIKE '%" + vName + "%')";
			if ((vNumber != null) && (vNumber.length() > 0))
			{
				query += " AND (PhoneNumber LIKE '%" + vNumber + "%'))";
			}
			else
			{
				query += ")";
			}
		}
		else
		if ((vNumber != null) && (vNumber.length() > 0))
		{
			query += " WHERE ((PhoneNumber LIKE '%" + vNumber + "%'))";
		}
		query += " Order By Name";
		
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextVendorData = (Properties)allDataRetrieved.elementAt(cnt);

				Vendor nextVendor = new Vendor(nextVendorData);

				if (nextVendor != null)
				{
					vendorCollection.addElement(nextVendor);
				}
			}
		}
		else
		{
			throw new InvalidPrimaryKeyException("No vendors found.");
		}
	
	}
	
	//For the AddVendorInventoryItemType Stuff
	//-----------------------------
	public VendorCollection() throws InvalidPrimaryKeyException
	{
		super(myTableName);

		// Set the dependencies
		setDependencies();

		// Refresh the Vendor collection
		vendorCollection = new Vector();


		
		
		String query = "SELECT * FROM " + myTableName+ " ORDER BY Name ASC";  //+ 
				//" Where ((Status = 'Active'))";
		
	
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextVendorData = (Properties)allDataRetrieved.elementAt(cnt);

				Vendor nextVendor = new Vendor(nextVendorData);

				if (nextVendor != null)
				{
					vendorCollection.addElement(nextVendor);
				}
			}
		}
		else
		{
			throw new InvalidPrimaryKeyException("No vendors found.");
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
	public Vendor retrieve(Properties prps)
	{
		Vendor retValue = null;

		String vName = prps.getProperty("Name");
		String phoneNumber = prps.getProperty("PhoneNumber");
		
		for (int cnt = 0; cnt < vendorCollection.size(); cnt++)
		{
			Vendor nextVendor = (Vendor) 
					vendorCollection.elementAt(cnt);
			String nextVName = (String) nextVendor.getState("Name");
			String nextPhone = (String) nextVendor.getState("PhoneNumber");
		
			if ((vName != null) && (vName.length() > 0))
			{
				if (nextVName.equals(vName))
				{
					if ((phoneNumber != null) && (phoneNumber.length() > 0))
					{
						if (nextPhone.equals(phoneNumber))
						{
							retValue = nextVendor;
							break;
						}
					}
					else
					{
						retValue = nextVendor;
						break;
					}
				}
			}
			else
			{
				if ((phoneNumber != null) && (phoneNumber.length() > 0))
				{
					if (nextPhone.equals(phoneNumber))
					{
						retValue = nextVendor;
						break;
					}
				}
			}
			
		}
	

		return retValue;
	}


	// Remove the inventory item type from the collection
	//----------------------------------------------------------
	public void remove(Vendor vendor)
	{
		vendorCollection.remove(vendor);
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		if(key.equals("VendorList"))
			return vendorCollection;
		else
		if(key.equals("VendorCollection"))
			return this;

		else
		if(key.equals("VendorCollectionErrorMessage"))
			return vendorCollectionErrorMessage;

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
