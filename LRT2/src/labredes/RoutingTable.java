package labredes;

//import java.net.InetAddress;
//import java.util.HashMap;

public class RoutingTable {
	
	private Address owner;
	private boolean isRouter;
	//private HashMap<Address, Address> table;
		
	public RoutingTable(Address owner) {
		this.owner = owner;
		
		this.isRouter = owner.compare(Router.getRouterAddress());	
	}
	
	public Address getGatewayForAddress(Address address) {
		
		if (isRouter) {
			if (address.ip.equals(owner.ip)) {
				return address;
			} else {
				return new Address(address.ip, Router.ROUTER_PORT);
			}		
		} 
		
		if (address.ip.equals(owner.ip)) {
			return address;
		} else {
			return Router.getRouterAddress();
		}	
	}
	
	public void print() {
		if (isRouter) {
			System.out.println(owner.ip.getHostAddress() + "\t->\tDirect");
			System.out.println("0.0.0.0\t\t->\t0.0.0.0:" + Router.ROUTER_PORT);			
		} else {
			System.out.println(owner.ip.getHostAddress() + "\t->\tDirect");
			System.out.println("0.0.0.0\t\t->\t" + Router.getRouterAddress());
		}
	}
}
