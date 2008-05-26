package raceclient;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 12, 2008
 * Time: 4:40:55 PM
 */
public class RMLPController implements Evolvable {

    RMLP rmlp;
    int currentGear = 1;

    public RMLPController () {
        this.rmlp = new RMLP (24, 10, 3);
    }

    private RMLPController (RMLP rmlp) {
        this.rmlp = rmlp;
    }

    public Action control (SensorModel model) {
        // this controller uses lots of information and tries do everything, including gear-shifting
        // in theory it should be able to evolve nice stuff, though we haven't seen this yet!
        Action action = new Action ();
        double[] inputs = new double[24];
        inputs[0] = 1;
        inputs[1] = model.getSpeed () / 100;
        inputs[2] = model.getAngleToTrackAxis();
        inputs[3] = model.getTrackPosition();
        inputs[4] = model.getRPM() / 1000;
        double[] trackSensors = model.getTrackEdgeSensors();
        System.arraycopy(trackSensors, 0, inputs, 5, trackSensors.length);
        double[] outputs = rmlp.propagate (inputs);
        action.accelerate = (outputs[0] > 0.1);
        action.steering = outputs[1];
        if (outputs[2] > 0.3 && currentGear < 6) {
            currentGear++;
        }
        if (outputs[2] < -0.3 && currentGear > -1) {
            currentGear--;
        }
        action.gear = currentGear;
        return action;
    }

    public void mutate () {
        rmlp.mutate ();
    }

    public Evolvable copy () {
        return new RMLPController (rmlp.copy ());
    }

    public void reset () {
        rmlp.reset ();
        currentGear = 0;
    }

}
