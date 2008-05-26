package raceclient;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 5, 2008
 * Time: 11:56:40 AM
 */
public class Utils {

    private InetAddress remote;
    private int port;
    private DatagramSocket socket;

    public Utils (DatagramSocket socket, InetAddress remote, int port) {
        this.remote = remote;
        this.port = port;
        this.socket = socket;
    }

    public static Controller load(String name) {
        Controller controller;
        try {
            controller = (Controller) (Object) Class.forName(name).newInstance();
        }
        catch (ClassNotFoundException e) {
            System.out.println(name + " is not a class name; trying to load a wox definition with that name.");
            controller = (Controller) wox.serial.Easy.load(name);
        }
        catch (Exception e) {
            e.printStackTrace();
            controller = null;
            System.exit(0);
        }
        return controller;
    }

    public String sendAndReceive(String toSend) {
        try {
            byte[] outBuffer = toSend.getBytes();
            socket.send(new DatagramPacket(outBuffer, outBuffer.length, remote, port));
            //System.out.println("Sent");
            byte[] inBuffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            //System.out.println(received);
            return received;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
