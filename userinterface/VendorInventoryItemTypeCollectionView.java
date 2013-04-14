package userinterface;

import impresario.IModel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import model.InventoryItemType;
import model.Vendor;

public class VendorInventoryItemTypeCollectionView extends View implements ActionListener, MouseListener
{
	protected JTable tableOfVendors;
	protected JButton cancelButton;
	protected JButton submitButton;

	protected MessageView statusLog;

	protected Vector vendorsVector;

	//--------------------------------------------------------------------------
	public VendorInventoryItemTypeCollectionView(IModel iitc)
	{
		super(iitc, "VendorInventoryItemTypeCollectionView");

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		vendorsVector = new Vector();

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
		vendorsVector.removeAllElements();

		try
		{
			Vector vendorList = (Vector)myModel.getState("VendorList");

			Enumeration entries = vendorList.elements();

			while (entries.hasMoreElements() == true)
			{
				Vendor nextVendor = (Vendor)entries.nextElement();
				Vector view = nextVendor.getEntryListView();

				// add this list entry to the list
				vendorsVector.add(view);
			}
		}
		catch (Exception e) {//SQLException e) {
			// Need to handle this exception
		}
	}

	//--------------------------------------------------------------------------
	protected JPanel createTitle()
	{
		return formatViewTitle ("LIST OF FOUND VENDORS");
	}

	//--------------------------------------------------------------------------
	protected JPanel createDataEntryFields()
	{
		JPanel entries = new JPanel();
		entries.setLayout(new BoxLayout(entries, BoxLayout.Y_AXIS));
		entries.setBackground ( blue );

		JPanel tablePan = new JPanel();
		tablePan.setLayout(new FlowLayout(FlowLayout.CENTER));

		TableModel myData = new VendorTableModel(vendorsVector);

		tableOfVendors = new JTable(myData);
		tableOfVendors.addMouseListener(this);
		tableOfVendors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tableOfVendors.setPreferredScrollableViewportSize(new Dimension(700,200));

		TableColumn column;
		for(int i = 0; i < myData.getColumnCount(); i++)
		{
			column = tableOfVendors.getColumnModel().getColumn(i);
			if (i == 0)
				column.setPreferredWidth(250);
			else
				column.setPreferredWidth(100);
		}
		
		tablePan.add(tableOfVendors);

		JScrollPane scrollPane = new JScrollPane(tableOfVendors);
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
			myModel.stateChangeRequest("CancelVendorList", null);
		}
		if(evt.getSource() == submitButton)
		{
			processVendorSelected();
		}
	}

	//--------------------------------------------------------------------------
	protected void processVendorSelected()
	{
		int selectedIndex = tableOfVendors.getSelectedRow();
		if(selectedIndex >= 0)
		{
			Vector selectedVendor = (Vector)vendorsVector.elementAt(selectedIndex);

			myModel.stateChangeRequest("VendorSelected", selectedVendor.elementAt(0));
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
			processVendorSelected();
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

