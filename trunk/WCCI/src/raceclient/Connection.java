package raceclient;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 26, 2008
 * Time: 12:21:31 PM
 */
public interface Connection {

    public void init (DatagramSocket socket, InetAddress remote, int port);

    public String sendAndReceive(String toSend);

}
