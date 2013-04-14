package userinterface;

import impresario.IModel;


//==============================================================================
public class ViewFactory {

	public static View createView( String viewName, IModel model )
	{
		//---------------------------------------------------
		// Inventory Master
		//---------------------------------------------------
		if( viewName.equals( "LoginView" ) == true)
		{
			return new LoginView(model);
		}
		else
		if( viewName.equals( "MainMenuView" ) == true)
		{
			return new MainMenuView(model);
		}	
		else
		if( viewName.equals( "AddWorkerView" ) == true)
		{
			return new AddWorkerView(model);
		}	
		else
		if( viewName.equals( "ModifyWorkerView" ) == true)
		{
			return new ModifyWorkerView(model);
		}
		else
		if( viewName.equals( "DeleteWorkerView" ) == true)
		{
			return new DeleteWorkerView(model);
		}
		else
		if( viewName.equals( "AddInventoryItemTypeView" ) == true)
		{
			return new AddInventoryItemTypeView(model);
		}	
		else
		if( viewName.equals( "ModifyInventoryItemTypeView" ) == true)
		{
			return new ModifyInventoryItemTypeView(model);
		}
		else
		if( viewName.equals( "SearchInventoryItemTypeView" ) == true)
		{
			return new SearchInventoryItemTypeView(model);
		}
		else
		if( viewName.equals( "InventoryItemTypeCollectionView" ) == true)
		{
			return new InventoryItemTypeCollectionView(model);
		}
		else
		if(viewName.equals("DeleteInventoryItemTypeView") == true)
		{
			return new DeleteInventoryItemTypeView(model);
		}
		else
		if( viewName.equals( "AddVendorView" ) == true)
		{
			return new AddVendorView(model);
		}	
		else
		if( viewName.equals( "ModifyVendorView" ) == true)
		{
			return new ModifyVendorView(model);
		}
		else
		if( viewName.equals( "DeleteVendorView" ) == true)
		{
			return new DeleteVendorView(model);
		}
		else
		if(viewName.equals("VendorCollectionView") == true)
		{
			return new VendorCollectionView(model);
		}
		else
		if(viewName.equals("SearchVendorView") == true)
		{
			return new SearchVendorView(model);
		}
		else
		if( viewName.equals( "SearchModifyVendorView" ) == true)
		{
			return new SearchModifyVendorView(model);
		}
		
		else
		if(viewName.equals("AddVendorInventoryItemTypeView") == true)
		{
			return new AddVendorInventoryItemTypeView(model);
		}
		else
		if(viewName.equals("SearchVendorInventoryItemTypeView") == true)
		{
				return new SearchVendorInventoryItemTypeView(model);
		}
		
		else
		if(viewName.equals("ModifyVendorInventoryItemTypeView") == true)
		{
				return new ModifyVendorInventoryItemTypeView(model);
		}
		
		else
		if(viewName.equals("DeleteVendorInventoryItemTypeView") == true)
		{
			return new DeleteVendorInventoryItemTypeView(model);
		}
		else
		if(viewName.equals("BrowseInventoryExceptionsView") == true)
		{
			return new BrowseInventoryExceptionsView(model);
		}
		else
		if(viewName.equals("BrowseInventoryExceptionsCollectionView") == true)
		{
			return new BrowseInventoryExceptionsCollectionView(model);
		}
		else return null;
		
		
			
	}


}
