package practice;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ipaddress {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		String hostAd = InetAddress.getLocalHost().getHostAddress();
		System.out.println(hostAd);
	}

}
