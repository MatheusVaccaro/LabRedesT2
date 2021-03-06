package labredes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Router {
	public static final int ROUTER_PORT = 60000;
	public static final int PACKET_SIZE = 1024;
	
	private RoutingTable routingTable;
	private Address address;
	private DatagramSocket socket;
	
	public Router() throws SocketException {
		this.address = Router.getRouterAddress();
		this.routingTable = new RoutingTable(address);
		this.socket = new DatagramSocket(ROUTER_PORT);		
	}
	
	public void start() {
		System.out.println("Starting router on " + address);
		System.out.println();
		routingTable.print();
		System.out.println();
		System.out.println("Waiting for messages...");
		
		while (true) {
			try {
				// Create packet
				DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
				// Receive and handle packet
				socket.receive(packet);
				new Thread( () -> {	
					handlePacket(packet);
				}).start();
				
			} catch (Exception e) {
				System.out.println("Something went terribly wrong.");
				System.out.println(e.getMessage());
			}
		}
	}
	
	private void handlePacket(DatagramPacket packet) {
		try {
			Message message = new Message(packet);

			if (this.address.compare(message.recipientAddress)) {
				// Display message if its for the router
				displayMessage(message);
			} else {
				// If not, display message and forward it
				Address forwardAddress = routingTable.getGatewayForAddress(message.recipientAddress);
				System.out.println("Received the following packet: ");
				System.out.println(message.encode());
				System.out.println("Forwarding it to its destination.");
				sendMessage(message, forwardAddress);
			}
			
		} catch (UnknownHostException e) {
			System.out.println("Failed to decode packet.");
			System.out.println(e.getMessage());
		}
	}
	
	private void sendMessage(Message message, Address destination) {
		byte[] encodedMessage = message.encode().getBytes();
		DatagramPacket packet = new DatagramPacket(encodedMessage, encodedMessage.length, destination.ip, destination.port);
		
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.out.println("Unable to send packet.");
			e.printStackTrace();
		}
		
	}
	
	private void displayMessage(Message message) {
		System.out.println("\n------------");
		System.out.println("New message!");
		System.out.println(message);
		System.out.println("------------");
	}
	
	public static Address getRouterAddress() {
		try { 
			return new Address(InetAddress.getLocalHost(), Router.ROUTER_PORT); 
		} catch (UnknownHostException e) { 
			e.printStackTrace();
			return null;
		}
	}
	
}
