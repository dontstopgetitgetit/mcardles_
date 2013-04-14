package userinterface;

// System imports
import java.awt.Toolkit;
import java.util.regex.Pattern;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

//==============================================================
public class NumericTextField extends JTextField
{	
	private int limit;
	
	//----------------------------------------------------------
	public NumericTextField(int columns, int limit)
	{
		super();
		setColumns(columns);
		this.limit = limit;
	}
	
	//----------------------------------------------------------
    protected Document createDefaultModel()
    {
        return new NumericDocument();
    }

    //----------------------------------------------------------
    private class NumericDocument extends PlainDocument
    {
        // '\d' is the regex for digit (\\d* means zero or more digits)
        private final Pattern DIGITS = Pattern.compile("\\d*");

    	//----------------------------------------------------------
        public void insertString(int offset, String str, AttributeSet a) 
        	throws BadLocationException
        {
            /* If the string is not null, and if it matches 
        	 * the regex defined above, insert the string. */
            if ((str != null && DIGITS.matcher(str).matches()) &&
                ((getLength() + str.length()) <= limit))
            {
                super.insertString(offset, str, a);
            }
            else
            {
  			  Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
