package labredes;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Router {
	private final int ROUTER_PORT = 60000;
	private final int PACKET_SIZE = 1024;
	private RoutingTable routingTable;
	private DatagramSocket socket;
	
	public Router() throws SocketException {
		this.socket = new DatagramSocket(ROUTER_PORT);
		this.routingTable = new RoutingTable();
	}
	
	public void start() {
		
		while (true) {
			try {
				// Create packet
				DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
				//
				socket.receive(packet);
				new Thread( () -> {	handlePacket(packet); }).start();
				
			} catch (Exception e) {
				System.out.println("Something went terribly wrong.");
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void handlePacket(DatagramPacket packet) {
		try {
			Message message = new Message(packet);
			System.out.println("Received the following packet:");
			System.out.println(message);
			
			InetAddress.getLocalHost();
			
		} catch (UnknownHostException e) {
			System.out.println("Failed to decode packet.");
			System.out.println(e.getMessage());
		}
	}
}
