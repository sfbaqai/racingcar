package raceclient;

import wox.serial.Easy;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 10, 2008
 * Time: 6:27:26 PM
 */
public class HillClimber implements Constants {

    static final int evaluationRepetitions = 1;
    static int generations = 5000;

    public static void main (String[] args) throws Exception {
        Evaluator evaluator = new SoloDistanceEvaluator (port, address);
        Evolvable controller;
        if (args.length > 0) {
            controller = (Evolvable) Utils.load (args[0]);
        }
        else {
            controller = new MLPController ();
        }
        if (args.length > 1) {
            generations = Integer.parseInt (args[1]);
        }
        for (int generation = 0; generation < generations; generation++){
            System.out.print (generation + ", current ");
            double fitness = evaluator.evaluate (controller);
            System.out.print (fitness + ", challenger ");
            Evolvable challenger = controller.copy ();
            challenger.mutate ();
            double challengerFitness = evaluator.evaluate (challenger);
            System.out.println(challengerFitness);
            if (challengerFitness >= fitness) {
                controller = challenger;
                if (challengerFitness > fitness) {
                    Easy.save (controller, "best-climbed.xml");
                }
            }
        }
    }

}
