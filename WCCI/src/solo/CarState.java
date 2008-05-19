/**
 * 
 */
package solo;

import java.util.Arrays;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;


/**
 * @author kokichi3000
 *
 */
public class CarState {
	final static int OPPONENTS_SENSORS_NUM = 18;
	
	double angle;
	double curLapTime;
    double damage;
    double distFromStart;
    double distRaced;
    double fuel;
    int   gear;
    double lastLapTime;    
    double[] opponents = new double[OPPONENTS_SENSORS_NUM];
    int   racePos;
    double   rpm;
    double speedX;
    double speedY;
    double[] track = new double[OPPONENTS_SENSORS_NUM];
    double trackPos;
    double[] wheelSpinVel = new double[4];
	/**
	 * Copy Constructor
	 *
	 * @param carState a <code>CarState</code> object
	 */
	public CarState(CarState carState) 
	{
	    this.angle = carState.angle;
	    this.curLapTime = carState.curLapTime;
	    this.damage = carState.damage;
	    this.distFromStart = carState.distFromStart;
	    this.distRaced = carState.distRaced;
	    this.fuel = carState.fuel;
	    this.gear = carState.gear;
	    this.lastLapTime = carState.lastLapTime;
	    this.opponents = carState.opponents;
	    this.racePos = carState.racePos;
	    this.rpm = carState.rpm;
	    this.speedX = carState.speedX;
	    this.speedY = carState.speedY;
	    this.track = carState.track;
	    this.trackPos = carState.trackPos;
	    this.wheelSpinVel = carState.wheelSpinVel;
	}
	/**
	 * 
	 */
	public CarState() {
		
	}
	
	public CarState(String sensors) {
		fromString(sensors);
	}
		
	
	public void fromString(String s){
		MessageParser mp = new MessageParser(s);		
		String str = (String)(mp.getReading("angle"));
		angle = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("curLapTime"));
		curLapTime = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("damage"));
		damage = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("distFromStart"));
		distFromStart = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("distRaced"));
		distRaced = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("fuel"));
		fuel = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("gear"));
		gear = (str==null)?0:Integer.parseInt(str);
		
		str = (String)(mp.getReading("lastLapTime"));
		lastLapTime = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("racePos"));
		racePos = (str==null)?0:Integer.parseInt(str);
		
		str = (String)(mp.getReading("rpm"));
		rpm = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("speedX"));
		speedX = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("speedY"));
		speedY = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("trackPos"));
		trackPos = (str==null)?0:Double.parseDouble(str);
		
		DoubleArrayList dal = (DoubleArrayList)(mp.getReading("opponents"));
		opponents = (dal==null)?null:dal.toDoubleArray();
		
		dal = (DoubleArrayList)(mp.getReading("track"));
		track = (dal==null)?null:dal.toDoubleArray();
		
		dal = (DoubleArrayList)(mp.getReading("wheelSpinVel"));
		wheelSpinVel = (dal==null)?null:dal.toDoubleArray();
	}
	
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public double getCurLapTime() {
		return curLapTime;
	}
	public void setCurLapTime(double curLapTime) {
		this.curLapTime = curLapTime;
	}
	public double getDamage() {
		return damage;
	}
	public void setDamage(double damage) {
		this.damage = damage;
	}
	public double getDistFromStart() {
		return distFromStart;
	}
	public void setDistFromStart(double distFromStart) {
		this.distFromStart = distFromStart;
	}
	public double getDistRaced() {
		return distRaced;
	}
	public void setDistRaced(double distRaced) {
		this.distRaced = distRaced;
	}
	public double getFuel() {
		return fuel;
	}
	public void setFuel(double fuel) {
		this.fuel = fuel;
	}
	public int getGear() {
		return gear;
	}
	public void setGear(int gear) {
		this.gear = gear;
	}
	public double getLastLapTime() {
		return lastLapTime;
	}
	public void setLastLapTime(double lastLapTime) {
		this.lastLapTime = lastLapTime;
	}
	public double[] getOpponents() {
		return opponents;
	}
	public void setOpponents(double[] opponents) {
		this.opponents = opponents;
	}
	public int getRacePos() {
		return racePos;
	}
	public void setRacePos(int racePos) {
		this.racePos = racePos;
	}
	public double getRpm() {
		return rpm;
	}
	public void setRpm(double rpm) {
		this.rpm = rpm;
	}
	public double getSpeedX() {
		return speedX;
	}
	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}
	public double getSpeedY() {
		return speedY;
	}
	public void setSpeedY(double speedY) {
		this.speedY = speedY;
	}
	public double[] getTrack() {
		return track;
	}
	public void setTrack(double[] track) {
		this.track = track;
	}
	public double getTrackPos() {
		return trackPos;
	}
	public void setTrackPos(double trackPos) {
		this.trackPos = trackPos;
	}
	public double[] getWheelSpinVel() {
		return wheelSpinVel;
	}
	public void setWheelSpinVel(double[] wheelSpinVel) {
		this.wheelSpinVel = wheelSpinVel;
	}
	
	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "CarState ( "
	        + super.toString() + TAB
	        + "angle = " + this.angle + TAB
	        + "curLapTime = " + this.curLapTime + TAB
	        + "damage = " + this.damage + TAB
	        + "distFromStart = " + this.distFromStart + TAB
	        + "distRaced = " + this.distRaced + TAB
	        + "fuel = " + this.fuel + TAB
	        + "gear = " + this.gear + TAB
	        + "lastLapTime = " + this.lastLapTime + TAB
	        + "opponents = " + this.opponents + TAB
	        + "racePos = " + this.racePos + TAB
	        + "rpm = " + this.rpm + TAB
	        + "speedX = " + this.speedX + TAB
	        + "speedY = " + this.speedY + TAB
	        + "track = " + this.track + TAB
	        + "trackPos = " + this.trackPos + TAB
	        + "wheelSpinVel = " + this.wheelSpinVel + TAB
	        + " )";
	
	    return retValue;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(angle);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(curLapTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(damage);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(distFromStart);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(distRaced);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(fuel);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + gear;
		temp = Double.doubleToLongBits(lastLapTime);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(opponents);
		result = prime * result + racePos;
		temp = Double.doubleToLongBits(rpm);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(speedX);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(speedY);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(track);
		temp = Double.doubleToLongBits(trackPos);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(wheelSpinVel);
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CarState))
			return false;
		final CarState other = (CarState) obj;
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
	}
    
	
    
}