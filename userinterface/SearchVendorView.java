//************************************************************
//    COPYRIGHT 2013, Sandeep Mitra and Students, 
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.util.EventObject;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//===============================================================
public class SearchVendorView extends SearchModifyVendorView
{
	

	//----------------------------------------------------------
	public SearchVendorView(IModel transaction)
	{
		super(transaction);
	}


	//----------------------------------------------------------------
	// Create this View Title with the string specified and formatted
	// to this program standard in the superclass View
	//-----------------------------------------------------------------
	protected JPanel createTitle()
	{
		return formatViewTitle ("SEARCH FOR A VENDOR TO DELETE");
	}

	
	
}

