// tabs=4
//************************************************************
//	COPYRIGHT 2011 Sandeep Mitra and Students, The
//    College at Brockport, State University of New York. -
//	  ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//************************************************************
//
// Specify the package
package userinterface;

// system imports
import javax.swing.*;


import java.awt.*;
import java.net.URL;

//============================================================
public class LogoPanel extends JPanel
{

	//----------------------------------------------------------
	// Construct the Logo Panel once in the beginning of the
	// program run. It is instantiated by the MainFrame
	//----------------------------------------------------------
	public LogoPanel()
	{
		// custom color of the program
		Color blue = new Color ( 133, 195, 230 );

		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ));

		setBackground( blue );

		setBorder (BorderFactory.createEmptyBorder( 5, 5, 0, 5 ));

		// This is top panel of the Logo Panel
		JPanel titleTop = new JPanel();
		titleTop.setBackground( blue );
		titleTop.setLayout( new BoxLayout( titleTop, BoxLayout.X_AXIS ));

		// Create the Upper Title of the program with the Boy Scout Troop image
		JLabel leftLabel = new JLabel( "        INVENTORY MANAGEMENT SYSTEM        ",
									  new ImageIcon ( "mcardlesheader3.png" ),
                					  SwingConstants.LEADING );

		// Format the font of the Upper Title
		leftLabel.setFont( new Font( "Arial", Font.BOLD, 16 ) );

		// install the label into the title panel
		titleTop.add( leftLabel );

		// Add to the Upper Title the Boy Scout Troop image
		JLabel rightLabel = new JLabel( "", new ImageIcon ( "mcardlesheader3.png" ),
									    	SwingConstants.RIGHT );
		// install the label into the title panel
		titleTop.add( rightLabel );

		// This panel holds the sub title of the program
		JPanel titleLower = new JPanel();
		titleLower.setBackground( blue );

		JLabel lowerLabel = new JLabel( "                          " );

		// format the sub title
		lowerLabel.setFont( new Font( "Arial", Font.BOLD, 14 ) );

		// install the label into the lower panel
		titleLower.add( lowerLabel );

		add ( titleTop );

		add( titleLower );
	}

}

