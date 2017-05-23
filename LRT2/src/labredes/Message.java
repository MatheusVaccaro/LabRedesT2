package labredes;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Message {
	
	private InetAddress senderAddress;
	private int 		senderPort;
	
	private InetAddress recipientAddress;
	private int 		recipientPort;
	
	private String message;
	
	public Message(InetAddress sender, int senderPort, InetAddress recipient, int recipientPort, String message) {
		this.senderAddress = sender;
		this.senderPort = senderPort;
		
		this.recipientAddress = recipient;
		this.recipientPort = recipientPort;
		
		this.message = message.replaceAll("\\t", " "); // Replace all tabs with white space, we use tabs to encode the msg later
	}
	
	public Message(DatagramPacket packet) throws UnknownHostException {
		String data = new String(packet.getData());
		String[] fields = data.split("\\t"); // We should get 3 fields here: [0]sender info, [1]recipient info and [2]message
		
		String[] senderInfo = fields[0].split(":"); // Sender info is <ip>:<port> so we split it in two
		this.senderAddress = InetAddress.getByName(senderInfo[0]);
		this.senderPort = Integer.parseInt(senderInfo[1]);
		
		String[] recipientInfo = fields[1].split(":"); // Same as sender info
		this.recipientAddress = InetAddress.getByName(recipientInfo[0]);
		this.recipientPort = Integer.parseInt(recipientInfo[1]);
		
		this.message = fields[2]; // Message doesn't have to be split
	}
	
	public String encode() {
		String senderInfo = senderAddress.getHostAddress() + ":" + senderPort;
		String recipientInfo = recipientAddress.getHostAddress() + ":" + recipientPort;
		String encodedMessage = senderInfo + "\t" + recipientInfo + "\t" + message; // Master tab encoding
		return encodedMessage;
	}
	
	public String toString() {
		return "Sender: " + senderAddress + "\tRecipient: " + recipientAddress + "\nMessage: " + message;
	}
	
}
