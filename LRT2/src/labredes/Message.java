package labredes;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Message {
	
	public Address senderAddress;
	public Address recipientAddress;
	public String message;
	
	public Message(InetAddress sender, int senderPort, InetAddress recipient, int recipientPort, String message) {
		this.senderAddress = new Address(sender, senderPort);
		this.recipientAddress = new Address(recipient, recipientPort);
		
		this.message = message.replaceAll("\\t", " "); // Replace all tabs with white space, we use tabs to encode the msg later
	}
	
	public Message(Address senderAddress, Address recipientAddress, String message) {
		this.senderAddress = senderAddress;
		this.recipientAddress = recipientAddress;
		this.message = message.replaceAll("\\t", " ");
	}
	
	public Message(DatagramPacket packet) throws UnknownHostException {
		String data = new String(packet.getData());
		//System.out.println(data);
		String[] fields = data.split("\\t"); // We should get 3 fields here: [0]sender info, [1]recipient info and [2]message
		
		String[] senderInfo = fields[0].split(":"); // Sender info is <ip>:<port> so we split it in two
		senderAddress = new Address();
		senderAddress.ip = InetAddress.getByName(senderInfo[0]);
		senderAddress.port = Integer.parseInt(senderInfo[1]);
		
		String[] recipientInfo = fields[1].split(":"); // Same as sender info
		recipientAddress = new Address();
		recipientAddress.ip = InetAddress.getByName(recipientInfo[0]);
		recipientAddress.port = Integer.parseInt(recipientInfo[1]);
		
		this.message = fields[2]; // Message doesn't have to be split
	}
	
	public String encode() {
		String senderInfo = senderAddress.ip.getHostAddress() + ":" + senderAddress.port;
		String recipientInfo = recipientAddress.ip.getHostAddress() + ":" + recipientAddress.port;
		String encodedMessage = senderInfo + "\t" + recipientInfo + "\t" + message; // Master tab encoding
		return encodedMessage;
	}
	
	public String toString() {
		return "Sender: " + senderAddress + "\tRecipient: " + recipientAddress + "\nMessage: " + message;
	}
	
}
