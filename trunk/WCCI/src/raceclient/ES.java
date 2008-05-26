package raceclient;

import wox.serial.Easy;

/**
 * Created by IntelliJ IDEA.
 * User: jtogel
 * Date: 24-Oct-2006
 * Time: 01:59:35
 */
public class ES implements Constants {

    static final int popsize = 30;
    static final int elite = popsize / 2;
    static final int evaluationRepetitions = 1;
    static Evolvable[] population = new Evolvable[popsize];
    static int generations = 200;
    static double[] fitness = new double[popsize];
    static Evaluator evaluator = null;

    public static void main (String[] args) throws Exception {
        evaluator = new SoloDistanceEvaluator (port, address);
        Evolvable initialController;
        if (args.length > 0) {
            initialController = (Evolvable) Utils.load (args[0]);
        }
        else {
            initialController = new MLPController ();
        }
        if (args.length > 1) {
            generations = Integer.parseInt (args[1]);
        }
        for (int i = 0; i < population.length; i++) {
            population[i] = initialController.copy ();
        }
        double lastBestFitness = 0;
        for (int i = 0; i < generations; i++) {
            oneMoreGeneration ();
            if (fitness[0] > lastBestFitness) {
                Easy.save (population[0], "evolved.xml");
                lastBestFitness = fitness[0];
            }
            System.out.printf ("Gen %d fitness %.4f\n", i, fitness[0]);
        }
    }

    public static void oneMoreGeneration () {
        for (int i = 0; i < elite; i++) {
            evaluate (i);
        }
        for (int i = elite; i < population.length; i++) {
            population[i] = population[i - elite].copy ();
            population[i].mutate ();
            evaluate (i);
        }
        shufflePopulationBeforeSorting();
        sortPopulationByFitness ();
    }

    private static void evaluate (int which) {
        fitness[which] = 0;
        for (int i = 0; i < evaluationRepetitions; i++) {
            ((Controller)population[which]).reset ();
            fitness[which] += evaluator.evaluate((Controller)population[which]);                    
        }
        fitness[which] = fitness[which] / evaluationRepetitions;
    }

    private static void shufflePopulationBeforeSorting () {
        for (int i = 0; i < population.length; i++) {
            int j = (int) (Math.random () * population.length);
            swap (i, j);
        }
    }

    private static void sortPopulationByFitness () {
        for (int i = 0; i < population.length; i++) {
            for (int j = i + 1; j < population.length; j++) {
                if (fitness[i] < fitness[j]) {
                    swap (i, j);
                }
            }
        }
    }

    private static void swap (int i, int j) {
                    double cache = fitness[i];
                    fitness[i] = fitness[j];
                    fitness[j] = cache;
                    Evolvable gcache = population[i];
                    population[i] = population[j];
                    population[j] = gcache;
    }
}
