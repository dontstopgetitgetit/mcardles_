package userinterface;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class VendorTableModel extends AbstractTableModel implements TableModel
{
	private Vector myState;

	public VendorTableModel(Vector itemTypeData)
	{
		myState = itemTypeData;
	}

	//--------------------------------------------------------------------------
	public int getColumnCount()
	{
		return 3;
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
			return "Vendor Name";
		else
		if(columnIndex == 1)
			return "Phone Number";
		else
			if(columnIndex == 2)
				return "Status";
		else
			return "??";
	}
}

