package apps.framework.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class CmFunction {
	
	 
	/**
	 * YHCHOI : 현재 서버 아이피 가지고 오기
	 * @return
	 */	 
	 public static String getLocalServerIp()
	 {
	 	try
	 	{
	 	    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
	 	    {
	 	        NetworkInterface intf = en.nextElement();
	 	        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
	 	        {
	 	            InetAddress inetAddress = enumIpAddr.nextElement();
	 	            if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())
	 	            {
	 	            	return inetAddress.getHostAddress().toString();
	 	            }
	 	        }
	 	    }
	 	}
	 	catch (SocketException ex) {}
	 	return null;
	 }
	 	
}