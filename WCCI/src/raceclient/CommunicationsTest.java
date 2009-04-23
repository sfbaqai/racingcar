package raceclient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Feb 1, 2008
 * Time: 4:56:58 PM
 */
public class CommunicationsTest {

    final static int port = 3001;
    final static String address = "127.0.0.1";

    public static void main(String[] args) throws Exception {
        System.out.println("Starting");
        int i = 0;
        DatagramSocket socket = new DatagramSocket();
        InetAddress remote = InetAddress.getByName(address);
        String toSend = "wcci2008";
        byte[] outBuffer = toSend.getBytes();
        socket.send(new DatagramPacket  (outBuffer, outBuffer.length, remote, port));
        while (true) {
            toSend = "(accel 0) (brake 0) (gear 0) (steer 0) (meta 0)";
            outBuffer = toSend.getBytes();
            socket.send(new DatagramPacket  (outBuffer, outBuffer.length, remote, port));
            System.out.println("Sent");
            byte[] inBuffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket (inBuffer, inBuffer.length);
            socket.receive (packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println(i + "   " + received);
            MessageParser parser = new MessageParser (received);
            parser.printAll ();
            i++;
        }
    }


}
