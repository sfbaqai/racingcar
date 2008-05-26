package raceclient;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 5, 2008
 */
public class BasicClient {

    final static int port = 3001;
    final static String address = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        Controller controller = Utils.load (args[0]);
        DatagramSocket socket = new DatagramSocket();
        InetAddress remote = InetAddress.getByName(address);
        Connection connection = new PausingConnection(socket, remote, port);
        String reply = connection.sendAndReceive("wcci2008");
        if (!reply.contains("identified"))
            throw new Exception ("Weird reply: " + reply);
        reply = connection.sendAndReceive(new Action ().toString());
        while (true) {
            MessageParser message = new MessageParser (reply);
            SensorModel model = new MessageBasedSensorModel (message);
            Action action = controller.control(model);
            reply = connection.sendAndReceive (action.toString ());
        }
    }

}
