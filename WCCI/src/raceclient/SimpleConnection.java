package raceclient;

import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 21, 2008
 * Time: 12:27:24 PM
 */
public class SimpleConnection implements Connection {

    private InetAddress remote;
    private int port;
    private DatagramSocket socket;

    public SimpleConnection(DatagramSocket socket, InetAddress remote, int port) {
        init (socket, remote, port);    
    }

    public void init(DatagramSocket socket, InetAddress remote, int port) {
        this.remote = remote;
        this.port = port;
        this.socket = socket;
    }

    public String sendAndReceive(String toSend) {
        //System.out.println("sending: " + toSend);
        try {
            byte[] outBuffer = toSend.getBytes();
            socket.send(new DatagramPacket(outBuffer, outBuffer.length, remote, port));
            byte[] inBuffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
            socket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
           // System.out.println("received: " + received);
            return received;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
