package raceclient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 26, 2008
 * Time: 12:39:00 PM
 */
public class PausingConnection implements Connection {

    private InetAddress remote;
    private int port;
    private DatagramSocket socket;
    final int iterationsToWaitForServer = 500;
    private byte[] outBuffer = null;
    Thread senderThread;

    public PausingConnection(DatagramSocket socket, InetAddress remote, int port) {
        init (socket, remote, port);
    }

    public void init(DatagramSocket socket, InetAddress remote, int port) {
        this.remote = remote;
        this.port = port;
        this.socket = socket;
        Sender sender = new Sender ();
        senderThread = new Thread (sender);
        senderThread.start ();
    }

    public String sendAndReceive(String toSend) {
        //System.out.println("sending: " + toSend);
        try {
            outBuffer = toSend.getBytes();
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
            int numberOfIterationsSinceSent = 0;
            while (true) {
                try {
                    if (outBuffer==null) {
                        numberOfIterationsSinceSent = 0;
                    }
                    else {
                        if (numberOfIterationsSinceSent % iterationsToWaitForServer == 0) {
                            socket.send(new DatagramPacket(outBuffer, outBuffer.length, remote, port));
                            if (numberOfIterationsSinceSent > 0) {
                                System.out.println("resending "+numberOfIterationsSinceSent);
                            }
                        }
                        numberOfIterationsSinceSent++;
                    }
                    Thread.sleep (1);
                } catch (Exception e) {
                    e.printStackTrace ();
                }

            }
        }
    }
}
