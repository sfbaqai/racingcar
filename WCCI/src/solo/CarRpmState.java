/**
 * 
 */
package solo;

import java.io.Serializable;
import java.util.Arrays;

import raceclient.SensorModel;

/**
 * @author kokichi3000
 *
 */
public class CarRpmState extends CarState implements Comparable<CarRpmState>,Serializable{

	/**
	 * @param carState
	 */
	public CarRpmState(CarState carState) {
		super(carState);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param carState
	 */
	public CarRpmState(SensorModel carState) {
		super(carState);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public CarRpmState() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param sensors
	 */
	public CarRpmState(String sensors) {
		super(sensors);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(CarRpmState arg0) {
		// TODO Auto-generated method stub
		double rs = getSpeed()-arg0.getSpeed();
		if (rs==0) rs = getRPM()-arg0.getRPM();
		return (rs<0)?-1:(rs>0)?1:0;
	}

	public boolean equals(Object obj){
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CarRpmState))
			return false;
		final CarRpmState other = (CarRpmState) obj;
		if (Double.doubleToLongBits(angle) != Double
				.doubleToLongBits(other.angle))
			return false;
		if (Double.doubleToLongBits(curLapTime) != Double
				.doubleToLongBits(other.curLapTime))
			return false;
		if (Double.doubleToLongBits(damage) != Double
				.doubleToLongBits(other.damage))
			return false;
		if (Double.doubleToLongBits(distFromStart) != Double
				.doubleToLongBits(other.distFromStart))
			return false;
		if (Double.doubleToLongBits(distRaced) != Double
				.doubleToLongBits(other.distRaced))
			return false;
		if (Double.doubleToLongBits(fuel) != Double
				.doubleToLongBits(other.fuel))
			return false;
		if (gear != other.gear)
			return false;
		if (Double.doubleToLongBits(lastLapTime) != Double
				.doubleToLongBits(other.lastLapTime))
			return false;
		if (!Arrays.equals(opponents, other.opponents))
			return false;
		if (racePos != other.racePos)
			return false;
		if (Double.doubleToLongBits(rpm) != Double.doubleToLongBits(other.rpm))
			return false;
		if (Double.doubleToLongBits(speedX) != Double
				.doubleToLongBits(other.speedX))
			return false;
		if (Double.doubleToLongBits(speedY) != Double
				.doubleToLongBits(other.speedY))
			return false;
		if (!Arrays.equals(track, other.track))
			return false;
		if (Double.doubleToLongBits(trackPos) != Double
				.doubleToLongBits(other.trackPos))
			return false;
		if (!Arrays.equals(wheelSpinVel, other.wheelSpinVel))
			return false;
		return true;

	}//*/
	
	
}
