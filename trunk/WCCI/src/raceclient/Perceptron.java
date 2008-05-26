package raceclient;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: julian
 * Date: Jun 27, 2007
 * Time: 9:38:04 PM
 */
public class Perceptron {

    private double[][] weights;
    final double mutationMagnitude = 0.1;
    final double learningRate = 0.1;
    private final Random random = new Random ();
    private double[] inputs;
    private double[] outputs;

    public Perceptron (int from, int to) {
        inputs = new double[from];
        weights = new double[from][to];
        mutate ();
    }

    public Perceptron (double[][] weights) {
        this.weights = weights;
        this.inputs = new double[weights.length];
    }

    public double[] propagate (double[] inputIn) {
        if (inputs != inputIn) {
            System.arraycopy (inputIn, 0, this.inputs, 0, inputIn.length);
        }
        outputs = new double[weights[0].length];
        for (int from = 0; from < inputs.length; from++) {
            for (int to = 0; to < outputs.length; to++) {
                outputs[to] += inputs[from] * weights[from][to];
            }
        }
        return outputs;
    }

    public double learn (double[] targets) {
        double[] errors = new double[outputs.length];
        double sumError = 0;
        for (int out = 0; out < outputs.length; out++) {
            errors[out] = targets[out] - outputs[out];
            sumError += errors[out];
            for (int in = 0; in < inputs.length; in++) {
                weights[in][out] += learningRate * errors[out] * inputs[in];
            }
        }
        return sumError / outputs.length;
    }

    public void mutate () {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] += random.nextGaussian () * mutationMagnitude;
            }
        }
    }

    public Perceptron copy () {
        double[][] copiedWeights = new double[weights.length][weights[0].length];
        for (int i = 0; i < weights.length; i++) {
            System.arraycopy (weights[i], 0, copiedWeights[i], 0, weights[i].length);
        }
        return new Perceptron (copiedWeights);
    }

}
