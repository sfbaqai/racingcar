package raceclient;

import solo.MyDriver;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 10, 2008
 * Time: 4:58:14 PM
 */
public class Play implements Constants {
    
 public static void main (String[] args) throws Exception {
        Evaluator evaluator = new SoloDistanceEvaluator (port, address);
        Controller controller = new MyDriver();
        System.out.print ("Evaluating... ");
        double fitness = evaluator.evaluate(controller);
        System.out.print (fitness);
    }

}
