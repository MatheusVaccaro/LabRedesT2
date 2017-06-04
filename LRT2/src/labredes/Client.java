package labredes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class Client {
	private RoutingTable routingTable;
	private Address address;
	private DatagramSocket socket;
	
	private Scanner in = new Scanner(System.in);
	
	public Client() throws UnknownHostException, SocketException {
		int port = new Random().nextInt(10000) + 10000; // 10000-19999
		this.address = new Address(InetAddress.getLocalHost(), port);
		
		this.routingTable = new RoutingTable(address);
		
		this.socket = new DatagramSocket(port);
	}
	
	public void start() {
		System.out.println("Starting client on " + address);
		//Scanner in = new Scanner(System.in);
		
		new Thread( () -> {	
			
			while (true) {
				try {
					byte [] buf = new byte[Router.PACKET_SIZE];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					socket.receive(packet);
					
					Message message = new Message(packet);
					displayMessage(message);
					
					Thread.sleep(1000);
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		
		while (true) {
			
			Address recipientAddress = inquireRecipientAddress();
			
			System.out.println("Type your message in the line below: ");
			String messageContent = in.nextLine();
					
			Message message = new Message(this.address, recipientAddress, messageContent);	
			
			sendMessage(message);
		}
	}
	
	private void sendMessage(Message message) {
		// Get encoded message
		byte[] encodedMessage = message.encode().getBytes();
		
		Address forwardAddress = this.routingTable.getGatewayForAddress(message.recipientAddress);
		
		DatagramPacket packet = new DatagramPacket(encodedMessage, encodedMessage.length,forwardAddress.ip, forwardAddress.port);
		try {
			this.socket.send(packet);
			System.out.println("Message sent.");
		} catch (IOException e) {
			System.out.println("Unable to send packet.");
			e.printStackTrace();
		}
		
	}

	private Address inquireRecipientAddress() {
		do {
			System.out.print("Insert the recipient's IP address: ");
			String ip = in.nextLine();
			
			System.out.print("Insert the recipient's port: ");
			String port = in.nextLine();

			try {
				Address recipientAddress = new Address(InetAddress.getByName(ip), Integer.parseInt(port));
				return recipientAddress;
			} catch (NumberFormatException | UnknownHostException e) {
				System.out.println("Invalid address. Please try again.");
				//e.printStackTrace();
			}		
		} while(true);
	}
	
	private void displayMessage(Message message) {
		System.out.println("\n------------");
		System.out.println("New message!");
		System.out.println(message);
		System.out.println("------------");
	}
}
