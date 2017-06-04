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
		
		while (true) {
			System.out.println("Waiting for message...");
			try {
				// Create packet
				DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
				// Receive and handle packet
				socket.receive(packet);
				new Thread( () -> {	handlePacket(packet); }).start();
				
			} catch (Exception e) {
				System.out.println("Something went terribly wrong.");
				System.out.println(e.getMessage());
			}
		}
	}
	
	private void handlePacket(DatagramPacket packet) {
		try {
			Message message = new Message(packet);
			displayMessage(message);
			
			Address forwardAddress = routingTable.getGatewayForAddress(message.recipientAddress);
			
			sendMessage(message, forwardAddress);
		} catch (UnknownHostException e) {
			System.out.println("Failed to decode packet.");
			System.out.println(e.getMessage());
		}
	}
	
	private void sendMessage(Message message, Address destination) {
		byte[] encodedMessage = message.encode().getBytes();
		DatagramPacket packet = new DatagramPacket(encodedMessage, 0, PACKET_SIZE, destination.ip, destination.port);
		
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.out.println("Unable to send packet.");
			e.printStackTrace();
		}
		
	}
	
	private void displayMessage(Message message) {
		System.out.println("Received the following packet:");
		System.out.println(message);
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
