/**
 * 
 */
package solo;

/**
 * @author kokichi3000
 *
 */
public abstract class SimpleDriver extends BaseDriver {
	final static int[] gearUp = new int[]{5000,6000,6000,6500,7000,0};
	final static int[] gearDown = new int[]{0,2500,3000,3000,3500,3500};
	int meta = 0;
	final static double[] SIN_LK = new double[]{0.0d,
		0.17364817766693041,0.3420201433256688,0.5d,0.6427876096865394,
		0.766044443118978,0.8660254037844386,0.9396926207859083,0.984807753012208,1.0,
		0.984807753012208,0.9396926207859083,0.8660254037844387,0.766044443118978,
		0.6427876096865395,0.5,0.3420201433256688,0.17364817766693064,
		0.0d};
	
	final static double[] COS_LK = new double[]{-1.0,-0.984807753012208,
		-0.9396926207859083,-0.8660254037844387,-0.766044443118978,-0.6427876096865393,
		-0.5,-0.34202014332566877,-0.17364817766693036,0.0,0.17364817766693036,
		0.34202014332566877,0.5,0.6427876096865394,0.7660444431189779,
		0.8660254037844387,0.9396926207859083,0.984807753012208,1.0};
	
	final static double[] ANGLE_LK = new double[]{0.0,0.17453292519943295,0.3490658503988659,0.5235987755982988,0.6981317007977318,
		0.8726646259971648,1.0471975511965976,1.2217304763960306,1.3962634015954636,1.5707963267948966,1.7453292519943295,
		1.9198621771937623,2.0943951023931953,2.2689280275926285,2.443460952792061,2.617993877991494,2.792526803190927,
		2.9670597283903604,3.141592653589793};

	final static double PI_18 = Math.PI/18;
	/* Stuck constants*/
	final static int stuckTime = 25;
	final static double stuckAngle = 0.523598775; //PI/6
	
	/* Accel and Brake final staticants*/
	final static double maxSpeedDist=70;
	double maxSpeed=Double.MAX_VALUE;
	final static double sin10 = 0.17365;
	final static double cos10 = 0.98481;
	
	/* Steering final staticants*/
	final static double steerLock=0.785398;
	final static double steerSensitivityOffset=80.0;
	final static double wheelSensitivityCoeff=1;
	
	/* ABS Filter final staticants */
	final static double[] wheelRadius= new double[]{0.3179,0.3179,0.3276,0.3276};
	final static double absSlip=2.0;
	final static double absRange=3.0;
	final static double absMinSpeed=3.0;
	double speedX,speedY,distRaced,curPos,curAngle,distFromStartLine;
	double[] tracks,wheelSpinVel,opponents;
	double curLapTime,damage,fuel,lastLapTime,rpm;
	int stuck=0;
	int gear,racePos;
	CarState carState = null;
	/**
	 * 
	 */
	public SimpleDriver() {
		// TODO Auto-generated final staticructor stub
	}

	/* (non-Javadoc)
	 * @see solo.BaseDriver#drive(java.lang.String)
	 */
	@Override
	public String drive(String sensors) {
		// TODO Auto-generated method stub		
		CarState cs = new CarState(sensors);
		carState = cs;
		curAngle = cs.getAngle();
		speedX = cs.getSpeedX();
		speedY = cs.getSpeedY();
		distRaced = cs.getDistRaced();
		distFromStartLine = cs.getDistFromStart();
		curPos = -cs.getTrackPos();
		tracks = cs.getTrack();
		curLapTime = cs.getCurLapTime();
		damage = cs.getDamage();
		fuel = cs.getFuel();
		gear = cs.getGear();
		lastLapTime = cs.getLastLapTime();
		racePos = cs.getRacePos();
		rpm = cs.getRpm();
		wheelSpinVel = cs.getWheelSpinVel();
		opponents = cs.getOpponents();
		CarControl cc = wDrive(cs);
			 
		return cc.toString();	
	}
	
	abstract double filterABS(double brake);
	abstract double getAccel();
	abstract int getGear();
	abstract double getSteer();
	public void init(){
		
	}
	
	

	CarControl wDrive(CarState cs){
	// check if car is currently stuck
		init();
		double angle = cs.getAngle();
		if ( Math.abs(angle) > stuckAngle ) {
			// update stuck counter
	        stuck++;
	    } else {
	    	// if not stuck reset stuck counter
	        stuck = 0;
	    }
	
		// after car is stuck for a while apply recovering policy
	    if (stuck > stuckTime)
	    {
	    	/* set gear and sterring command assuming car is 
	    	 * pointing in a direction out of track */
	    	
	    	// to bring car parallel to track axis
	        double steer = - angle / steerLock; 
	        int gear=-1; // gear R
	        
	        // if car is pointing in the correct direction revert gear and steer  
	        if (angle*cs.getTrackPos()>0){
	            gear = 1;
	            steer = -steer;
	        }
	        // build a CarControl variable and return it
	        
	        return new CarControl(1.0,0.0,gear,steer,meta);
	    } else {// car is not stuck
	    	// compute accel/brake command
	        double accel_and_brake = getAccel();
	        // compute gear 
	        int gear = getGear();
	        // compute steering
	        double steer = getSteer();
	        
	
	        // normalize steering
	        if (steer < -1)
	            steer = -1;
	        if (steer > 1)
	            steer = 1;
	        
	        // set accel and brake from the joint accel/brake command 
	        double accel,brake;
	        if (accel_and_brake>0)        {
	            accel = accel_and_brake;
	            brake = 0;
	        } else {
	            accel = 0;
	            // apply ABS to brake
	            brake = filterABS(-accel_and_brake);
	        }
	        // build a CarControl variable and return it
	        
	        return new CarControl(accel,brake,gear,steer,meta);
	    }
	}


}
