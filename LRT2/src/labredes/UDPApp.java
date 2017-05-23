package labredes;

import java.net.SocketException;

public class UDPApp {

	public static void main(String args[]) {
		
		if (args.length > 0 && args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("router")) {
			setUpRouter();
		} else {
			setUpClient();
		}
		
	}
	
	private static void setUpRouter() {
		try {
			Router router = new Router();
			router.start();
		} catch (SocketException e) {
			System.out.println("Error creating a router application.");
			System.out.println("There's probably a router application already running on this machine.");
			System.out.println(e.getMessage());
			System.out.println("The program will now exit.");
			System.exit(0); // Explicit exit
		}
	}
	
	private static void setUpClient() {
		Client client = new Client();
	}
	
}
