package labredes;

import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPApp {

	public static void main(String args[]) {
		if (args.length > 0 && (args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("router"))) {
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
		try {
			Client client = new Client();
			client.start();
		} catch (SocketException | UnknownHostException e) {
			System.out.println("Error creating a client application.");
			System.out.println("Most likely the randomly generated port number is already being used by another application.");
			System.out.println(e.getMessage());
			System.out.println("The program will now exit.");
			System.exit(0); // Explicit exit
		}
	}
	
}
