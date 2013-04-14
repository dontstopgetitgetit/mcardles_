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

import impresario.IModel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import model.InventoryException;
import model.InventoryExceptionCollection;

//==============================================================================
public class BrowseInventoryExceptionsCollectionView extends View implements ActionListener//, MouseListener
{
	protected JTable tableOfExceptions;
	protected JButton cancelButton;
	//protected JButton submitButton;

	protected MessageView statusLog;

	protected Vector exceptionsVector;

	//--------------------------------------------------------------------------
	public BrowseInventoryExceptionsCollectionView(IModel biec)
	{
		super(biec, "BrowseInventoryExceptionsCollectionView");

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		exceptionsVector = new Vector();

		add(createTitle());
		add(createDataEntryFields());
		add(createNavigationButtons());

		// Error message area
		add(createStatusLog("                                     "));

		populateFields();
	}

	//--------------------------------------------------------------------------
	protected void populateFields()
	{
		getEntryTableModelValues();
	}

	//--------------------------------------------------------------------------
	protected void getEntryTableModelValues()
	{
		exceptionsVector.removeAllElements();

		try
		{
			Vector exceptionsList = (Vector)myModel.getState("InventoryExceptionList");

			Enumeration entries = exceptionsList.elements();

			while (entries.hasMoreElements() == true)
			{
				InventoryException nextException = (InventoryException)entries.nextElement();
				Vector view = nextException.getEntryListView();

				// add this list entry to the list
				exceptionsVector.add(view);
			}
		}
		catch (Exception e) {//SQLException e) {
			// Need to handle this exception
		}
	}

	//--------------------------------------------------------------------------
	protected JPanel createTitle()
	{
		return formatViewTitle ("LIST OF EXCEPTIONS");
	}

	//--------------------------------------------------------------------------
	protected JPanel createDataEntryFields()
	{
		JPanel entries = new JPanel();
		entries.setLayout(new BoxLayout(entries, BoxLayout.Y_AXIS));
		entries.setBackground ( blue );

		JPanel tablePan = new JPanel();
		tablePan.setLayout(new FlowLayout(FlowLayout.CENTER));

		TableModel myData = new InventoryExceptionsTableModel(exceptionsVector);

		tableOfExceptions = new JTable(myData);
		//tableOfExceptions.addMouseListener(this);
		tableOfExceptions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tableOfExceptions.setPreferredScrollableViewportSize(new Dimension(700,200));

		TableColumn column;
		for(int i = 0; i < myData.getColumnCount(); i++)
		{
			column = tableOfExceptions.getColumnModel().getColumn(i);
			if (i == 6)
				column.setPreferredWidth(250);
			else
			if (i == 0)
				column.setPreferredWidth(25);
			else
				column.setPreferredWidth(100);
		}
		
		tablePan.add(tableOfExceptions);

		JScrollPane scrollPane = new JScrollPane(tableOfExceptions);
		tablePan.add(scrollPane);

		entries.add(tablePan);
		return entries;
	}

	//--------------------------------------------------------------------------
	protected JPanel createNavigationButtons()
	{
		JPanel temp = new JPanel();
		FlowLayout f1 = new FlowLayout(FlowLayout.CENTER);
		f1.setVgap(5);
		f1.setHgap(25);
		temp.setLayout(f1);
		temp.setBackground ( blue );

		/*submitButton = new JButton("Submit");
		temp.add(formatButtonSmall(submitButton));*/

		cancelButton = new JButton("Done");
		temp.add(formatButtonSmall(cancelButton));

		return temp;
	}

	//--------------------------------------------------------------------------
	public void updateState(String key, Object value)
	{
	}

	//--------------------------------------------------------------------------
	protected void processAction(EventObject evt)
	{
		statusLog.clearErrorMessage();
		if(evt.getSource() == cancelButton)
		{
			myModel.stateChangeRequest("Cancel", null);
		}
		
	}

	//--------------------------------------------------------------------------
	protected JPanel createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//-------------------------------------------------------------
	public void processListSelection(EventObject evt)
	{
	}
}
