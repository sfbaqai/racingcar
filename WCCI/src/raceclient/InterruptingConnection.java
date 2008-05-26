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
public class InterruptingConnection implements Connection {

    private InetAddress remote;
    private int port;
    private DatagramSocket socket;
    private Thread senderThread = null;
    private Sender sender = null;
    final int msToWaitForServer = 1000;
    private byte[] outBuffer = null;


    public InterruptingConnection(DatagramSocket socket, InetAddress remote, int port) {
        init (socket, remote, port);
    }

    public void init(DatagramSocket socket, InetAddress remote, int port) {
        this.remote = remote;
        this.port = port;
        this.socket = socket;
        sender = new Sender ();
        senderThread = new Thread (sender);
        senderThread.start ();
    }

    public String sendAndReceive(String toSend) {
        //System.out.println("sending: " + toSend);
        try {
            outBuffer = toSend.getBytes();
            senderThread.interrupt(); // interrupt the wait, and so begin sending
            byte[] inBuffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
            socket.receive(packet);
            //System.out.println("Got it");
            outBuffer = null; // clear the outbuffer, and so stop sending
            String received = new String(packet.getData(), 0, packet.getLength());
           // System.out.println("received: " + received);
            return received;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private class Sender implements Runnable {

        // to be suspended etc
        public void run() {
            byte[] localBufferCopy = new byte[0];
            while (true) {
                if (outBuffer != null) {
                    try {
                        if (localBufferCopy == outBuffer) {
                            System.out.print (".");
                            //System.out.println("Resending " + new String (outBuffer));
                        }
                        localBufferCopy = outBuffer;
                        socket.send(new DatagramPacket(outBuffer, outBuffer.length, remote, port));
                    }
                    catch (Exception e) {
                        e.printStackTrace ();
                    }
                }
                try {
                    Thread.sleep (msToWaitForServer);
                }
                catch (Exception e) {}
            }
        }
    }

}
