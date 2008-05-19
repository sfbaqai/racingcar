package solo;

/**
 * @author kokichi3000
 *
 */
public class CarControl {
	double accel;
	double brake;
	int gear;
	double steer;
	int meta;
	
	/**
	 * Copy Constructor
	 *
	 * @param carControl a <code>CarControl</code> object
	 */
	public CarControl(CarControl carControl) 
	{
	    this.accel = carControl.accel;
	    this.brake = carControl.brake;
	    this.gear = carControl.gear;
	    this.steer = carControl.steer;
	    this.meta = carControl.meta;
	}
	
	public CarControl(String s){	
		MessageParser mp = new MessageParser(s);
		String str = (String)(mp.getReading("accel"));
		accel = (str==null)?0:Double.parseDouble(str);
		str = (String)(mp.getReading("brake"));
		brake = (str==null)?0:Double.parseDouble(str);
		str = (String)(mp.getReading("gear"));
		gear = (str==null)?0:Integer.parseInt(str);
		str = (String)(mp.getReading("steer"));
		steer = (str==null)?0:Double.parseDouble(str);
		str = (String)(mp.getReading("meta"));
		meta = (str==null)?0:Integer.parseInt(str);		
	}

	public CarControl(double accel, double brake, int gear, double steer,
			int meta) {
		super();
		this.accel = accel;
		this.brake = brake;
		this.gear = gear;
		this.steer = steer;
		this.meta = meta;		
	}

	public CarControl(double accel, double brake, int gear, double steer) {
		super();
		this.accel = accel;
		this.brake = brake;
		this.gear = gear;
		this.steer = steer;
	}
		
	
	public void fromString(String s){
		MessageParser mp = new MessageParser(s);
		String str = (String)(mp.getReading("accel"));
		accel = (str==null)?0:Double.parseDouble(str);
		str = (String)(mp.getReading("brake"));
		brake = (str==null)?0:Double.parseDouble(str);
		str = (String)(mp.getReading("gear"));
		gear = (str==null)?0:Integer.parseInt(str);
		str = (String)(mp.getReading("steer"));
		steer = (str==null)?0:Double.parseDouble(str);
		str = (String)(mp.getReading("meta"));
		meta = (str==null)?0:Integer.parseInt(str);
	}

	public double getAccel() {
		return accel;
	}

	public void setAccel(double accel) {
		this.accel = accel;
	}

	public double getBrake() {
		return brake;
	}

	public void setBrake(double brake) {
		this.brake = brake;
	}

	public int getGear() {
		return gear;
	}

	public void setGear(int gear) {
		this.gear = gear;
	}

	public double getSteer() {
		return steer;
	}

	public void setSteer(double steer) {
		this.steer = steer;
	}

	public int getMeta() {
		return meta;
	}

	public void setMeta(int meta) {
		this.meta = meta;
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
	    final String TAB = ")(";
	    
	    String retValue = "";
	    
	    retValue = "(accel " + this.accel + TAB
	        + "brake " + this.brake + TAB
	        + "gear " + this.gear + TAB
	        + "steer " + this.steer + TAB
	        + "meta " + this.meta + ")";
	
	    return retValue;
	}
	
	
	
}
