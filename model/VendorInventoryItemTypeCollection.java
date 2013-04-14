package model;

import impresario.IView;

import java.util.Properties;
import java.util.Vector;

import exception.InvalidPrimaryKeyException;

//==================================================================================
public class VendorInventoryItemTypeCollection extends EntityBase implements IView
{
	protected Properties mySchema;
	private Properties dependencies;

	private static final String myTableName = "VendorInventoryItemType";

	// Search results
	private Vector VendorInventoryItemTypeCollection;

	// Error and Message strings
	private String VendorInventoryItemTypeCollectionErrorMessage = "";

	/*
	 * the sent search criteria
	 */
	//----------------------------------------------------------
	public VendorInventoryItemTypeCollection(Properties props) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		// Set the dependencies
		setDependencies();

		// Refresh the inventory item type collection
		VendorInventoryItemTypeCollection = new Vector();

		String vId = props.getProperty("VendorId");
		String iTypeName = props.getProperty("ItemTypeName");
		
		
		String query = "SELECT * FROM " + myTableName;
		
		if ((vId != null) && (vId.length() > 0))
		{
			query += " WHERE ((VendorId LIKE '%" + vId + "%')";
			if ((iTypeName != null) && (iTypeName.length() > 0))
			{
				query += " AND (ItemTypeName LIKE '%" + iTypeName + "%'))";
			}
			else
			{
				query += ")";
			}
		}
		else
		if ((iTypeName != null) && (iTypeName.length() > 0))
		{
			query += " WHERE ((ItemTypeName LIKE '%" + iTypeName + "%'))";
		}
		
		
		 
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextVendorInventoryItemTypeData = (Properties)allDataRetrieved.elementAt(cnt);

				VendorInventoryItemType nextVendorInventoryItemType = new VendorInventoryItemType(nextVendorInventoryItemTypeData);

				if (nextVendorInventoryItemType != null)
				{
					VendorInventoryItemTypeCollection.addElement(nextVendorInventoryItemType);
				}
			}
		}
		else
		{
			throw new InvalidPrimaryKeyException("No VendorInventoryItemTypes found.");
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
	public VendorInventoryItemType retrieve(Properties prps)
	{
		VendorInventoryItemType retValue = null;

		String vId = prps.getProperty("Id");
		String iTypeName = prps.getProperty("ItemTypeName");
		
		for (int cnt = 0; cnt < VendorInventoryItemTypeCollection.size(); cnt++)
		{
			VendorInventoryItemType nextVendorInventoryItemType = (VendorInventoryItemType) 
					VendorInventoryItemTypeCollection.elementAt(cnt);
			String nextvId = (String) nextVendorInventoryItemType.getState("Id");
			String nextiTypeName = (String) nextVendorInventoryItemType.getState("ItemTypeName");
		
			if ((vId != null) && (vId.length() > 0))
			{
				if (nextvId.equals(vId))
				{
					if ((iTypeName != null) && (iTypeName.length() > 0))
					{
						if (nextiTypeName.equals(iTypeName))
						{
							retValue = nextVendorInventoryItemType;
							break;
						}
					}
					else
					{
						retValue = nextVendorInventoryItemType;
						break;
					}
				}
			}
			else
			{
				if ((iTypeName != null) && (iTypeName.length() > 0))
				{
					if (nextiTypeName.equals(iTypeName))
					{
						retValue = nextVendorInventoryItemType;
						break;
					}
				}
			}
			
		}
	

		return retValue;
	}


	// Remove the inventory item type from the collection
	//----------------------------------------------------------
	public void remove(VendorInventoryItemType VendorInventoryItemType)
	{
		VendorInventoryItemTypeCollection.remove(VendorInventoryItemType);
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		if(key.equals("VendorInventoryItemTypeList"))
			return VendorInventoryItemTypeCollection;
		else
		if(key.equals("VendorInventoryItemTypeCollection"))
			return this;

		else
		if(key.equals("VendorInventoryItemTypeCollectionErrorMessage"))
			return VendorInventoryItemTypeCollectionErrorMessage;

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
