package couk.Adamki11s.RemoteAdmin;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import couk.Adamki11s.Cryptography.HashEncoder;
import couk.Adamki11s.Parser.AuthenticationDelegate;
import couk.Adamki11s.Threads.RAServer;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RemoteAdmin {

	public static JFrame frmRemoteAdmin;
	public static JButton connectionButton;

	public static JTextField hostField;
	public static JTextField portField;
	public static JSeparator separator;
	public static JLabel lblPassword;
	public static JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RemoteAdmin window = new RemoteAdmin();
					window.frmRemoteAdmin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RemoteAdmin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		RAServer server = new RAServer("localhost", 1234, "pasdfdfssword");
		server.setActive(true);
		server.start();
		frmRemoteAdmin = new JFrame();
		frmRemoteAdmin.setIconImage(Toolkit.getDefaultToolkit().getImage(RemoteAdmin.class.getResource("/couk/Adamki11s/Resources/Bukkit Icon.png")));
		frmRemoteAdmin.setTitle("Remote Admin");
		frmRemoteAdmin.setBounds(100, 100, 474, 264);
		frmRemoteAdmin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRemoteAdmin.getContentPane().setLayout(null);

		hostField = new JTextField();
		hostField.setBounds(10, 36, 216, 20);
		frmRemoteAdmin.getContentPane().add(hostField);
		hostField.setColumns(10);

		JLabel lblHost = new JLabel("Host Address");
		lblHost.setBounds(10, 11, 204, 14);
		frmRemoteAdmin.getContentPane().add(lblHost);

		portField = new JTextField();
		portField.setColumns(10);
		portField.setBounds(10, 92, 216, 20);
		frmRemoteAdmin.getContentPane().add(portField);

		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(10, 67, 108, 14);
		frmRemoteAdmin.getContentPane().add(lblPort);

		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.GRAY);
		separator.setBounds(236, 0, 10, 226);
		frmRemoteAdmin.getContentPane().add(separator);

		lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 123, 216, 14);
		frmRemoteAdmin.getContentPane().add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(10, 148, 216, 20);
		frmRemoteAdmin.getContentPane().add(passwordField);

		connectionButton = new JButton("Connect");
		connectionButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (!AuthenticationDelegate.isAuthenticated) {
					if (validateTextFields()) {
						try {
							// Need to authenticate the connection before we can
							// start sending packets

							byte[] send_data = new byte[8048];

							DatagramSocket client_socket = new DatagramSocket();

							InetAddress IPAddress = InetAddress.getByName(hostField.getText());

							String data = getPassword();

							send_data = data.getBytes();

							DatagramPacket send_packet = new DatagramPacket(send_data, send_data.length, IPAddress, Integer.parseInt(portField.getText()));

							client_socket.send(send_packet);

							client_socket.close();
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				} else {
					try {
						byte[] send_data = new byte[8048];

						DatagramSocket client_socket = new DatagramSocket();

						InetAddress IPAddress = InetAddress.getByName(hostField.getText());

						send_data = ("QUIT").getBytes();

						DatagramPacket send_packet = new DatagramPacket(send_data, send_data.length, IPAddress, Integer.parseInt(portField.getText()));

						client_socket.send(send_packet);

						client_socket.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		connectionButton.setBounds(10, 179, 216, 40);
		frmRemoteAdmin.getContentPane().add(connectionButton);

		JButton entityManager = new JButton("Entity Manager");
		entityManager.setBounds(244, 27, 204, 36);
		frmRemoteAdmin.getContentPane().add(entityManager);

		JButton playerManager = new JButton("Player Manager");
		playerManager.setBounds(244, 70, 204, 36);
		frmRemoteAdmin.getContentPane().add(playerManager);

		JButton pluginManager = new JButton("Plugin Manager");
		pluginManager.setBounds(244, 117, 204, 36);
		frmRemoteAdmin.getContentPane().add(pluginManager);

		JButton serverManager = new JButton("Server Manager");
		serverManager.setBounds(244, 164, 204, 36);
		frmRemoteAdmin.getContentPane().add(serverManager);
	}

	private String getPassword() {
		char[] password = this.passwordField.getPassword();
		StringBuilder pass = new StringBuilder();

		for (char c : password) {
			pass.append(c);
		}
		return new HashEncoder().computeSHA2_256BitHash(pass.toString());
	}

	private boolean validateTextFields() {
		int port = 1;
		try {
			port = Integer.parseInt(this.portField.getText());
			if (port <= 1024 || port > 65535) {
				JOptionPane.showMessageDialog(frmRemoteAdmin, "Port number must be larger than 1024 and less than 65535.", "Invalid Port", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(frmRemoteAdmin, "Port number must be an integer.", "Invalid Port", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		// x.x.x.x (Minimum IP)
		// xxx.xxx.xxx.xxx (Maximum IP)
		if ((this.hostField.getText().length() < 7 || this.hostField.getText().length() > 15) && !this.hostField.getText().equalsIgnoreCase("localhost")) {
			JOptionPane.showMessageDialog(frmRemoteAdmin, "Host Name must be a valid IP address.", "Invalid Host", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		char[] password = this.passwordField.getPassword();
		StringBuilder pass = new StringBuilder();

		for (char c : password) {
			pass.append(c);
		}

		if (pass.toString().length() < 4) {
			JOptionPane.showMessageDialog(frmRemoteAdmin, "Password must be 4 characters or longer", "Invalid Password", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}
}
