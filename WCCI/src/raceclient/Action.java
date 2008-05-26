package raceclient;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 4, 2008
 * Time: 3:35:31 PM
 */
public class Action {

    public boolean accelerate = false;
    public boolean brake = false;
    public int gear = 0; // -1..6
    public double steering = 0;  // -1..1
    public boolean restartRace = false;

    public String toString () {
        return "(accel " + (accelerate ? 1 : 0) + ") " +
               "(brake " + (brake ? 1 : 0) + ") " +
               "(gear " + gear + ") " +
               "(steer " + steering + ") " +
               "(meta " + (restartRace ? 1 : 0) + ")";
    }

}
