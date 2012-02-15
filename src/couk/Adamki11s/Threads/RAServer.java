package couk.Adamki11s.Threads;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import couk.Adamki11s.Cryptography.HashEncoder;
import couk.Adamki11s.Parser.ActionParser;

public class RAServer extends Thread {

	final int port;

	private boolean active = false;

	private final HashEncoder encoder = new HashEncoder();

	final String password;

	InetAddress netAddress;

	byte[] receive_data = new byte[8048];

	public RAServer(String host, int port, String password) {
		try {
			this.netAddress = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
		this.password = this.encoder.computeSHA2_256BitHash(password);
	}

	@Override
	public void run() {
		this.recieveData();
	}

	public void recieveData() {
		try {

			DatagramSocket server_socket = new DatagramSocket(5100);

			System.out.println("UDPServer Waiting for client on port 5100");

			while (this.isActive()) {

				DatagramPacket receive_packet = new DatagramPacket(receive_data, receive_data.length);

				server_socket.receive(receive_packet);

				String data = new String(receive_data, 0, receive_packet.getLength());

				ActionParser.parseAction(data, this); //Parse the String and perform the relevant action on it
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sendData(String send, InetAddress ip) {
		try {
			DatagramSocket client_socket = new DatagramSocket();
			DatagramPacket send_packet = new DatagramPacket(send.getBytes(), send.getBytes().length, ip, 5100);
			client_socket.send(send_packet);
			client_socket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return this.active;
	}

}
