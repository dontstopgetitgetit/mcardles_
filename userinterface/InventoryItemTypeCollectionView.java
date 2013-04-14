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

import model.InventoryItemType;
import model.InventoryItemTypeCollection;

//==============================================================================
public class InventoryItemTypeCollectionView extends View implements ActionListener, MouseListener
{
	protected JTable tableOfItemTypes;
	protected JButton cancelButton;
	protected JButton submitButton;

	protected MessageView statusLog;

	protected Vector itemTypesVector;

	//--------------------------------------------------------------------------
	public InventoryItemTypeCollectionView(IModel iitc)
	{
		super(iitc, "InventoryItemTypeCollectionView");

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		itemTypesVector = new Vector();

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
		itemTypesVector.removeAllElements();

		try
		{
			Vector itemTypeList = (Vector)myModel.getState("InventoryItemTypeList");

			Enumeration entries = itemTypeList.elements();

			while (entries.hasMoreElements() == true)
			{
				InventoryItemType nextItemType = (InventoryItemType)entries.nextElement();
				Vector view = nextItemType.getEntryListView();

				// add this list entry to the list
				itemTypesVector.add(view);
			}
		}
		catch (Exception e) {//SQLException e) {
			// Need to handle this exception
		}
	}

	//--------------------------------------------------------------------------
	protected JPanel createTitle()
	{
		return formatViewTitle ("LIST OF FOUND INVENTORY ITEM TYPES");
	}

	//--------------------------------------------------------------------------
	protected JPanel createDataEntryFields()
	{
		JPanel entries = new JPanel();
		entries.setLayout(new BoxLayout(entries, BoxLayout.Y_AXIS));
		entries.setBackground ( blue );

		JPanel tablePan = new JPanel();
		tablePan.setLayout(new FlowLayout(FlowLayout.CENTER));

		TableModel myData = new InventoryItemTypeTableModel(itemTypesVector);

		tableOfItemTypes = new JTable(myData);
		tableOfItemTypes.addMouseListener(this);
		tableOfItemTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tableOfItemTypes.setPreferredScrollableViewportSize(new Dimension(700,200));

		TableColumn column;
		for(int i = 0; i < myData.getColumnCount(); i++)
		{
			column = tableOfItemTypes.getColumnModel().getColumn(i);
			if (i == 0)
				column.setPreferredWidth(250);
			else
				column.setPreferredWidth(100);
		}
		
		tablePan.add(tableOfItemTypes);

		JScrollPane scrollPane = new JScrollPane(tableOfItemTypes);
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

		submitButton = new JButton("Submit");
		temp.add(formatButtonSmall(submitButton));

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
			myModel.stateChangeRequest("CancelInventoryItemTypeList", null);
		}
		if(evt.getSource() == submitButton)
		{
			processItemTypeSelected();
		}
	}

	//--------------------------------------------------------------------------
	protected void processItemTypeSelected()
	{
		int selectedIndex = tableOfItemTypes.getSelectedRow();
		if(selectedIndex >= 0)
		{
			Vector selectedItemType = (Vector)itemTypesVector.elementAt(selectedIndex);

			myModel.stateChangeRequest("ItemTypeSelected", selectedItemType.elementAt(0));
		}
	}

	//--------------------------------------------------------------------------
	protected JPanel createStatusLog(String initialMessage)
	{
		statusLog = new MessageView(initialMessage);

		return statusLog;
	}

	//--------------------------------------------------------------------------
	public void mouseClicked(MouseEvent click)
	{
		if(click.getClickCount() >= 2)
		{
			processItemTypeSelected();
		}
	}

	//----------- These are not used ------------------------------
	public void mousePressed(MouseEvent click) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	//-------------------------------------------------------------
	public void processListSelection(EventObject evt)
	{
	}
}
