package raceclient;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 5, 2008
 * Time: 5:12:25 PM
 */
public class SimpleSoloController implements Controller {

    final double targetSpeed = 100;
    final int stepsBetweenGearShift = 20;
    final double lowerGearRPM = 5000;
    final double higherGearRPM = 8000;
    private int stepsSinceGearShift = 0;
    private int gear = 1;

    public Action control(SensorModel sensors) {
        Action action = new Action ();

        // decide whether to accelerate
        final double speed = sensors.getSpeed ();
        if (speed < targetSpeed) {
            action.accelerate = true;
        }

        // decide whether to turn towards the middle of the road
        // and, if we're about to leave the road, don't go to fast!
        double targetAngleToTrackAxis = 0;
        double trackPosition = sensors.getTrackPosition();
        if (Math.abs (trackPosition) > 0.3) {
            targetAngleToTrackAxis = trackPosition;
            if (speed > targetSpeed / 2) {
                action.accelerate = false;
            }
        }

        // decide how to steer
        final double steeringDifference = sensors.getAngleToTrackAxis() - targetAngleToTrackAxis;
        if (Math.abs (steeringDifference) < 0.01) {
            action.steering = 0;
        }
        else {
            action.steering = steeringDifference;
            // limit how sharp we turn
            action.steering = Math.min(action.steering, 0.1);
            action.steering = Math.max(action.steering, -0.1);
        }

        // shift gears
        final double rpm = sensors.getRPM();
        if (stepsSinceGearShift == 0) {
            if (rpm < lowerGearRPM && gear > 1) {
                gear--;
                stepsSinceGearShift = stepsBetweenGearShift;
            }
            else if (rpm > higherGearRPM && gear < 6) {
                gear++;
                stepsSinceGearShift = stepsBetweenGearShift; 
            }
        }
        else {
            stepsSinceGearShift--;
        }
        action.gear = gear;
        System.out.println (sensors.getGear () + " " + action.gear + " " + rpm + " " + speed +
                " " + steeringDifference + " " + action.steering);
        return action;
    }

    public void reset() {}

}
