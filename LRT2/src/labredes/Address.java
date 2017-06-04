package labredes;

import java.net.InetAddress;

public class Address {
	public InetAddress ip;
	public int port;
		
	public Address() {
		this.ip = null;
		this.port = -1;
	}
	
	public Address(InetAddress ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public String toString() {
		return ip.getHostAddress() + ":" + port;
	}
	
	public boolean compare(Address address) {
		return this.ip.getHostAddress().equals(address.ip.getHostAddress()) && this.port == address.port;
	}

}
