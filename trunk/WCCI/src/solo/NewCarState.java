package solo;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

import java.io.Serializable;

public class NewCarState extends CarState implements Serializable{	
	double radius;
	double radiusl;
	double radiusr;
	double posX;
	double posY;
	double posZ;
	double ax;
	double ay;
	double az;
	double length;
	double cx;
	double cy;
	double cz;
	/**
	 * Copy Constructor
	 *
	 * @param newCarState a <code>NewCarState</code> object
	 */
	public NewCarState(NewCarState newCarState) 
	{
		this.angle = newCarState.angle;
		this.curLapTime = newCarState.curLapTime;
		this.damage = newCarState.damage;
		this.distFromStart = newCarState.distFromStart;
		this.distRaced = newCarState.distRaced;
		this.fuel = newCarState.fuel;
		this.gear = newCarState.gear;
		this.lastLapTime = newCarState.lastLapTime;
		this.opponents = newCarState.opponents;
		this.racePos = newCarState.racePos;
		this.rpm = newCarState.rpm;
		this.speedX = newCarState.speedX;
		this.speedY = newCarState.speedY;
		this.track = newCarState.track;
		this.trackPos = newCarState.trackPos;
		this.wheelSpinVel = newCarState.wheelSpinVel;
		this.radius = newCarState.radius;
		this.radiusl = newCarState.radiusl;
		this.radiusr = newCarState.radiusr;
		this.posX = newCarState.posX;
		this.posY = newCarState.posY;
		this.posZ = newCarState.posZ;
		this.ax = newCarState.ax;
		this.ay = newCarState.ay;
		this.az = newCarState.az;
		this.length = newCarState.length;
		this.cx = newCarState.cx;
		this.cy = newCarState.cy;
		this.cz = newCarState.cz;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double getRadiusl() {
		return radiusl;
	}
	public void setRadiusl(double radiusl) {
		this.radiusl = radiusl;
	}
	public double getRadiusr() {
		return radiusr;
	}
	public void setRadiusr(double radiusr) {
		this.radiusr = radiusr;
	}
	public double getPosX() {
		return posX;
	}
	public void setPosX(double posX) {
		this.posX = posX;
	}
	public double getPosY() {
		return posY;
	}
	public void setPosY(double posY) {
		this.posY = posY;
	}
	public double getPosZ() {
		return posZ;
	}
	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}
	public double getAx() {
		return ax;
	}
	public void setAx(double ax) {
		this.ax = ax;
	}
	public double getAy() {
		return ay;
	}
	public void setAy(double ay) {
		this.ay = ay;
	}
	public double getAz() {
		return az;
	}
	public void setAz(double az) {
		this.az = az;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getCx() {
		return cx;
	}
	public void setCx(double cx) {
		this.cx = cx;
	}
	public double getCy() {
		return cy;
	}
	public void setCy(double cy) {
		this.cy = cy;
	}
	public double getCz() {
		return cz;
	}
	public void setCz(double cz) {
		this.cz = cz;
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

		retValue = "NewCarState ( "
			+ super.toString() + TAB
			+ "radius = " + this.radius + TAB
			+ "radiusl = " + this.radiusl + TAB
			+ "radiusr = " + this.radiusr + TAB
			+ "posX = " + this.posX + TAB
			+ "posY = " + this.posY + TAB
			+ "posZ = " + this.posZ + TAB
			+ "ax = " + this.ax + TAB
			+ "ay = " + this.ay + TAB
			+ "az = " + this.az + TAB
			+ "length = " + this.length + TAB
			+ "cx = " + this.cx + TAB
			+ "cy = " + this.cy + TAB
			+ "cz = " + this.cz + TAB
			+ " )";

		return retValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof NewCarState))
			return false;
		final NewCarState other = (NewCarState) obj;
		if (Double.doubleToLongBits(ax) != Double.doubleToLongBits(other.ax))
			return false;
		if (Double.doubleToLongBits(ay) != Double.doubleToLongBits(other.ay))
			return false;
		if (Double.doubleToLongBits(az) != Double.doubleToLongBits(other.az))
			return false;
		if (Double.doubleToLongBits(cx) != Double.doubleToLongBits(other.cx))
			return false;
		if (Double.doubleToLongBits(cy) != Double.doubleToLongBits(other.cy))
			return false;
		if (Double.doubleToLongBits(cz) != Double.doubleToLongBits(other.cz))
			return false;
		if (Double.doubleToLongBits(length) != Double
				.doubleToLongBits(other.length))
			return false;
		if (Double.doubleToLongBits(posX) != Double
				.doubleToLongBits(other.posX))
			return false;
		if (Double.doubleToLongBits(posY) != Double
				.doubleToLongBits(other.posY))
			return false;
		if (Double.doubleToLongBits(posZ) != Double
				.doubleToLongBits(other.posZ))
			return false;
		if (Double.doubleToLongBits(radius) != Double
				.doubleToLongBits(other.radius))
			return false;
		if (Double.doubleToLongBits(radiusl) != Double
				.doubleToLongBits(other.radiusl))
			return false;
		if (Double.doubleToLongBits(radiusr) != Double
				.doubleToLongBits(other.radiusr))
			return false;
		return true;
	}

	public NewCarState(String sensors) {
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
		
		str = (String)(mp.getReading("posX"));
		posX = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("posY"));
		posY = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("posZ"));
		posZ = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("ax"));
		ax = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("ay"));
		ay = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("az"));
		az = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("cx"));
		cx = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("cy"));
		cy = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("cz"));
		cz = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("radius"));
		radius = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("radiusl"));
		radiusl = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("radiusr"));
		radiusr = (str==null)?0:Double.parseDouble(str);
		
		str = (String)(mp.getReading("length"));
		length = (str==null)?0:Double.parseDouble(str);
	}

}
