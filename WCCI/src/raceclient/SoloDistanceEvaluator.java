package raceclient;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Mar 8, 2008
 * Time: 6:43:22 PM
 */
public class SoloDistanceEvaluator implements Evaluator {

    final int numberOfTrials = 1;
    final int numberOfTimesteps = 10000;
    final int maxAllowedDamage = 5000;
    final Connection connection;

    public SoloDistanceEvaluator (int port, String address) throws Exception {
        address = "127.0.0.1";
        System.out.print ("Connecting to " + address + ":" + port + "... ");
        DatagramSocket socket = new DatagramSocket();
        InetAddress remote = InetAddress.getByName(address);
        //connection = new PausingConnection (socket, remote, port);
        connection = new InterruptingConnection (socket, remote, port);
        //connection = new SimpleConnection (socket, remote, port);
        String reply = connection.sendAndReceive("wcci2008");
        if (!reply.contains ("identified")) {
            throw new Exception ("Odd reply from server after connecting: " + reply);
        }
        System.out.println ("OK");
    }

    public double evaluate(Controller controller) {
        double totalTravelledDistance = 0;
        for (int trial = 0; trial < numberOfTrials; trial++) {
            controller.reset();
            Action action = new Action ();
//            action.restartRace = true;
            String reply = connection.sendAndReceive (action.toString ());
            action.restartRace = false;
            // the first reply should be a ***restart***
            while (reply.contains("restart")) {
                //System.out.println("--  no restart yet");
                try {
                    Thread.sleep (10);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                
                reply = connection.sendAndReceive (action.toString ());
//                System.out.println(reply);
            }//end of while
//            try {
//                    Thread.sleep (500);
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            reply = connection.sendAndReceive ("wcci2008");
//            if (!reply.contains ("identified")) {
//                throw new RuntimeException ("Odd reply from server after connecting: " + reply);
//            } else System.out.println("Here");

            SensorModel model = null;
            double currentDistanceRaced = 0;
            for (int step = 0; step < numberOfTimesteps; step++) {
               // do {
                    reply = connection.sendAndReceive (action.toString ());
                //} while (reply.contains ("restart"));
                MessageParser message = new MessageParser (reply);
                model = new MessageBasedSensorModel (message);
//                System.out.println(model);
                action = controller.control(model);
                if (currentDistanceRaced == 0 ||
                        Math.abs (currentDistanceRaced - model.getDistanceRaced ()) < 100) {
                    currentDistanceRaced = model.getDistanceRaced ();
                }
                if (model.getDamage() > maxAllowedDamage) {
                    break;
                }
                //System.out.print(model.getDistanceRaced () + "        ");
               // System.out.print(model.getDistanceFromStartLine () + "       ");
            }
            totalTravelledDistance += currentDistanceRaced;
        }
        return totalTravelledDistance / numberOfTrials;
    }
}
