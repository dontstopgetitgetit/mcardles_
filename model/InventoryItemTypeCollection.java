package model;

// system imports
import java.util.Collections;
import java.util.Properties;
import java.util.Vector;

// project imports
import exception.InvalidPrimaryKeyException;
import impresario.IView;
import model.EntityBase;

//==============================================================
public class InventoryItemTypeCollection extends EntityBase implements IView
{
	protected Properties mySchema;
	private Properties dependencies;

	private static final String myTableName = "InventoryItemType";

	// Search results
	private Vector inventoryItemTypeCollection;

	// Error and Message strings
	private String inventoryItemTypeCollectionErrorMessage = "";

	/*
	 * This constructor below searches for InventoryItemType objects that match
	 * the sent search criteria
	 */
	//----------------------------------------------------------
	public InventoryItemTypeCollection(Properties props) throws InvalidPrimaryKeyException
	{
		super(myTableName);

		// Set the dependencies
		setDependencies();

		// Refresh the inventory item type collection
		inventoryItemTypeCollection = new Vector();

		String itemTypeName = props.getProperty("ItemTypeName");
		String barcodePrefix = props.getProperty("BarcodePrefix");
		
		String query = "SELECT * FROM " + myTableName;
		
		if ((itemTypeName != null) && (itemTypeName.length() > 0))
		{
			query += " WHERE ((ItemTypeName LIKE '%" + itemTypeName + "%')";
			if ((barcodePrefix != null) && (barcodePrefix.length() > 0))
			{
				query += " AND (BarcodePrefix LIKE '%" + barcodePrefix + "%'))";
			}
			else
			{
				query += ")";
			}
		}
		else
		if ((barcodePrefix != null) && (barcodePrefix.length() > 0))
		{
			query += " WHERE ((BarcodePrefix LIKE '%" + barcodePrefix + "%'))";
		}
		
		query += " ORDER BY BarcodePrefix";
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextItemTypeData = (Properties)allDataRetrieved.elementAt(cnt);

				InventoryItemType nextItemType = new InventoryItemType(nextItemTypeData, false);

				if (nextItemType != null)
				{
					inventoryItemTypeCollection.addElement(nextItemType);
				}
			}
		}
		else
		{
			throw new InvalidPrimaryKeyException("No inventory item types found for name like: "
					+ itemTypeName + " and barcode prefix like: " + barcodePrefix);
		}
	
	}

	/*
	 * This constructor below searches for all Inventory Item Type Names
	 */
	//----------------------------------------------------------
	public InventoryItemTypeCollection() throws InvalidPrimaryKeyException
	{
		super(myTableName);

		// Set the dependencies
		setDependencies();

		// Refresh the inventory item type collection
		inventoryItemTypeCollection = new Vector();

		String query = "SELECT * FROM " + myTableName + " ORDER BY ItemTypeName ASC";
		
		Vector allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null)
		{

			for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
			{
				Properties nextItemTypeData = (Properties)allDataRetrieved.elementAt(cnt);

				InventoryItemType nextItemType = new InventoryItemType(nextItemTypeData, false);

				if (nextItemType != null)
				{
					inventoryItemTypeCollection.addElement(nextItemType);
				}
			}
		}
		else
		{
			throw new InvalidPrimaryKeyException("No inventory item types found");
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
	public InventoryItemType retrieve(Properties prps)
	{
		InventoryItemType retValue = null;

		String itemTypeName = prps.getProperty("ItemTypeName");
		String barcodePrefix = prps.getProperty("BarcodePrefix");

		for (int cnt = 0; cnt < inventoryItemTypeCollection.size(); cnt++)
		{
			InventoryItemType nextType = (InventoryItemType) 
					inventoryItemTypeCollection.elementAt(cnt);
			String nextTypeName = (String) nextType.getState("ItemTypeName");
			String nextTypeBPfix = (String) nextType.getState("BarcodePrefix");
		
			if ((itemTypeName != null) && (itemTypeName.length() > 0))
			{
				if (nextTypeName.equals(itemTypeName))
				{
					if ((barcodePrefix != null) && (barcodePrefix.length() > 0))
					{
						if (nextTypeBPfix.equals(barcodePrefix))
						{
							retValue = nextType;
							break;
						}
					}
					else
					{
						retValue = nextType;
						break;
					}
				}
			}
			else
			{
				if ((barcodePrefix != null) && (barcodePrefix.length() > 0))
				{
					if (nextTypeBPfix.equals(barcodePrefix))
					{
						retValue = nextType;
						break;
					}
				}
			}
			
		}

		return retValue;
	}


	// Remove the inventory item type from the collection
	//----------------------------------------------------------
	public void remove(InventoryItemType itemType)
	{
		inventoryItemTypeCollection.remove(itemType);
	}

	//----------------------------------------------------------
	public Object getState(String key)
	{
		// ADDED MITRA 4/3/2013
		if(key.equals("InventoryItemTypeNames"))
		{
			Vector typeNames = new Vector();
			if (inventoryItemTypeCollection != null)
				for (int cnt = 0; cnt < inventoryItemTypeCollection.size(); cnt++)
				{
					InventoryItemType nextItemType = 
							(InventoryItemType)inventoryItemTypeCollection.elementAt(cnt);
					String name = (String)nextItemType.getState("ItemTypeName");
					typeNames.addElement(name);
				}
			Collections.sort(typeNames);
			return typeNames;
		}
		// END MITRA 4/3/2013
		else
		if(key.equals("InventoryItemTypeList"))
			return inventoryItemTypeCollection;
		else
		if(key.equals("InventoryItemTypeCollection"))
			return this;

		else
		if(key.equals("InventoryItemTypeCollectionErrorMessage"))
			return inventoryItemTypeCollectionErrorMessage;

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
