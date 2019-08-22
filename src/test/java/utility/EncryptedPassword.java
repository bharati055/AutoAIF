package utility;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class EncryptedPassword {

	public static void main(String[] args) {
		String strPassword=null;
		String encPassword = null;
		strPassword= JOptionPane.showInputDialog("Enter password to encrypt!!");
		encPassword= CommonFunctions.encryptPassword(strPassword);
		
		 JTextArea textarea= new JTextArea(encPassword);
		 textarea.setEditable(true);
		 JOptionPane.showMessageDialog(null, textarea  ,"Password Encryption", JOptionPane.PLAIN_MESSAGE);
	}

}
