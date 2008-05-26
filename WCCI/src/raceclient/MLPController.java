package raceclient;

/**
 * Created by IntelliJ IDEA.
 * User: jtogel
 * Date: 12-Oct-2006
 * Time: 19:07:59
 */
public class MLPController implements Controller, Evolvable {

    MLP driveMLP;


    public MLPController () {
        this.driveMLP = new MLP (4, 4, 2);
    }

    private MLPController (MLP mlp) {
        this.driveMLP = mlp;
    }

    public Action control (SensorModel model) {        
        Action action = new Action ();
        double[] inputs = {1, model.getSpeed (), model.getAngleToTrackAxis(), model.getTrackPosition()};
        double[] outputs = driveMLP.propagate (inputs);
        // This controller does not shift gear at all. Always drives on first gear.
        action.gear = 1;
        action.accelerate = (outputs[0] > 0.1);
        action.steering = outputs[1];
        return action;
    }

    public void mutate () {
        driveMLP.mutate ();
    }

    public Evolvable copy () {
        return new MLPController (driveMLP.copy ());
    }

    public void reset () {
        // Not needed here, as we have a reactive controller
    }
}
