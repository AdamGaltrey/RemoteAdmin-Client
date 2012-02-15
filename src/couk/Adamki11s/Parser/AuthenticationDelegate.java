package couk.Adamki11s.Parser;

import javax.swing.JOptionPane;

import couk.Adamki11s.RemoteAdmin.RemoteAdmin;
import couk.Adamki11s.Threads.RAServer;

public class AuthenticationDelegate {
	
	public static boolean isAuthenticated = false;

	public static void parseAuthentication(String auth, RAServer server) {
		int level = Integer.parseInt(auth.split(":")[1]);
		switch (level) {

		case 0:
			isAuthenticated = false;
			JOptionPane.showMessageDialog(RemoteAdmin.frmRemoteAdmin, "Failed to connect to server. Invalid password.", "Invalid Password", JOptionPane.ERROR_MESSAGE);
			modifyButtonState();
			break;
		case 1:
			isAuthenticated = true;
			JOptionPane.showMessageDialog(RemoteAdmin.frmRemoteAdmin, "Connected to server successfully.", "Connection Successfull", JOptionPane.INFORMATION_MESSAGE);
			modifyButtonState();
			break;
		case 2:
			isAuthenticated = false;
			JOptionPane.showMessageDialog(RemoteAdmin.frmRemoteAdmin, "Failed to connect to server. Another Client is already connected.", "Connection Failure",
					JOptionPane.ERROR_MESSAGE);
			modifyButtonState();
			break;
		case 3:
			isAuthenticated = false;
			JOptionPane.showMessageDialog(RemoteAdmin.frmRemoteAdmin, "Disconnected from Server.", "Disconnected",
					JOptionPane.INFORMATION_MESSAGE);
			modifyButtonState();
			break;
		default:
			isAuthenticated = false;
			JOptionPane.showMessageDialog(RemoteAdmin.frmRemoteAdmin, "Unknown connection state.", "Connection State Error", JOptionPane.ERROR_MESSAGE);
			modifyButtonState();
			break;
		}
	}
	
	public static void modifyButtonState(){
		if(isAuthenticated){
			RemoteAdmin.connectionButton.setText("Disconnect");
			RemoteAdmin.hostField.setEnabled(false);
			RemoteAdmin.portField.setEnabled(false);
			RemoteAdmin.passwordField.setEnabled(false);
		} else {
			RemoteAdmin.connectionButton.setText("Connect");
			RemoteAdmin.hostField.setEnabled(true);
			RemoteAdmin.portField.setEnabled(true);
			RemoteAdmin.passwordField.setEnabled(true);
		}
	}

}
