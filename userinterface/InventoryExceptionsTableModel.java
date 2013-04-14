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

package userinterface;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

//==============================================================================
public class InventoryExceptionsTableModel extends AbstractTableModel implements TableModel
{
	private Vector myState;

	public InventoryExceptionsTableModel(Vector exceptionData)
	{
		myState = exceptionData;
	}

	//--------------------------------------------------------------------------
	public int getColumnCount()
	{
		return 7;
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
			return "Id";
		else
		if(columnIndex == 1)
			return "Exception Date";
		else
		if(columnIndex == 2)
			return "Exception Time";
		else
		if(columnIndex == 3)
			return "Worker Name";
		else
		if(columnIndex == 4)
			return "Barcode to Use";
		else
		if(columnIndex == 5)
			return "Barcode Actually Used";
		else
		if(columnIndex == 6)
			return "Reason";
		else
			return "??";
	}
}
