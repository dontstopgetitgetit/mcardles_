package userinterface;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

//==============================================================================
public class InventoryItemTypeTableModel extends AbstractTableModel implements TableModel
{
	private Vector myState;

	public InventoryItemTypeTableModel(Vector itemTypeData)
	{
		myState = itemTypeData;
	}

	//--------------------------------------------------------------------------
	public int getColumnCount()
	{
		return 6;
	}

	//--------------------------------------------------------------------------
	public int getRowCount()
	{
		return myState.size();
	}

	//--------------------------------------------------------------------------
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Vector account = (Vector)myState.elementAt(rowIndex);
		return "    " + account.elementAt(columnIndex);
	}

	//--------------------------------------------------------------------------
	public String getColumnName(int columnIndex)
	{
		if(columnIndex == 0)
			return "Item Type Name";
		else
		if(columnIndex == 1)
			return "Barcode Prefix";
		else
		if(columnIndex == 2)
			return "Age Sensitive?";
		else
		if(columnIndex == 3)
			return "Validity Days";
		else
		if(columnIndex == 4)
			return "Notes";
		else
		if(columnIndex == 5)
			return "Status";
		else
			return "??";
	}
}
