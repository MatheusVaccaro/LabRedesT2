package labredes;

public class RoutingTable {
	
	private Address owner;
		
	public RoutingTable(Address owner) {
		this.owner = owner;
	}
	
	public Address getGatewayForAddress(Address address) {
		if (address.ip.equals(owner.ip)) {
			return address;
		} else {
			return Router.getRouterAddress();
		}	
	}
}
