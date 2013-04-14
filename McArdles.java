// specify the package
// This file is in the default package

// system imports
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.ImageIcon;
import java.util.Locale;
import java.util.ResourceBundle;


// project imports
import event.Event;
import event.EventLog;
import common.PropertyFile;

import model.InventoryMaster;
import userinterface.MainFrame;
import userinterface.WindowPosition;

/**The class containing the main program for the Scout Troop 209 Tree Sales application */
//====================================================================================
public class McArdles {

	private InventoryMaster myInventoryMaster;     // the main behavior (main interface agent) for the application

	/** Main frame of the application */
	private MainFrame mainFrame;


	// constructor for this class, the main application object
	//--------------------------------------------------------
	public McArdles(){
		System.out.println("McArdle's Inventory Management System Version 1.0");

		//figure out the desired look and feel
		String LookNFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

		// See if you can set the look and feel requested, if not indicate error
		try{
			UIManager.setLookAndFeel(LookNFeel);
		}
		catch(Exception e){
			LookNFeel = UIManager.getCrossPlatformLookAndFeelClassName();
			try{
				UIManager.setLookAndFeel(LookNFeel);
			}
			catch(Exception f){
				System.err.println("McArdles.<init> - Unable to set look and feel");
				//new Event(Event.getLeafLevelClassName(this), "FastTrax.<init>",
				//		"Unable to set look and feel", Event.ERROR);
				//f.printStackTrace();
			}
		}

		//Create the top-level container (main frame) and add contents to it.
		mainFrame = MainFrame.getInstance("McArdle's Inventory Management System v1.0");

		// put in icon for window border and toolbar

		Toolkit myToolKit = Toolkit.getDefaultToolkit();

		File iconFile = new File("mcardlesheader3.jpg");

		if (iconFile.exists() == true){
			Image myImage = myToolKit.getImage("mcardlesheader3.jpg"); // DO WE HAVE AN ICON/JPG SYMBOLIZING THE TROOP?
			mainFrame.setIconImage(myImage);
		}

		// Finish setting up the frame, and show it.
		//mainFrame.addWindowListener(new WindowAdapter(){
			// event handler for window close events
		//	public void windowClosing(WindowEvent event){
				//System.exit(0);

		//	}
		//});

		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		try{
			myInventoryMaster = new InventoryMaster(mainFrame);
		}
		catch(Exception exc){
			System.err.println("McArdles.<init> - could not create Inventory Master!");
			new Event(Event.getLeafLevelClassName(this), "McArdles.<init>",
					"Unable to create Inventory Master object", Event.ERROR);
			exc.printStackTrace();
		}

		mainFrame.pack();

		WindowPosition.placeCenter(mainFrame);

		mainFrame.setVisible(true);
	}

	/** The "main" entry point for the application. Carries out actions to
	 *  set up the application */
	//---------------------------------------------------------------------------
	public static void main(String[] args){
		//crank up an instance of this object
		try{
			new McArdles();
		}
		catch(Exception e){
			new Event("McArdles", "McArdles.main", "Unhandled Exception: " + e, Event.FATAL);
			e.printStackTrace();
		}
	}
}

//----------------------------------------------------------------------------------------
// Revision History
//
