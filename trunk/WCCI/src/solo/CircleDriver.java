/**
 * 
 */
package solo;

import com.graphbuilder.geom.Geom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

/**
 * @author kokichi3000
 *
 */
public final class CircleDriver extends BaseStateDriver<NewCarState,CarControl> {

	double targetRadius = 50;
	/**
	 * 
	 */
	public CircleDriver() {
		// TODO Auto-generated constructor stub
		ignoredExisting = true;
		storeNewState = false;
	}

	/**
	 * @param name
	 */
	public CircleDriver(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	
	public double steerToPoint(State<NewCarState,CarControl> state,Vector2D point){
		double angle = state.state.angle;
		double posX = state.state.posX;
		double posY = state.state.posY;
		double cx = state.state.cx;
		double cy = state.state.cy;
		Vector2D a = new Vector2D(point.x-posX,point.y-posY);
		Vector2D b = (new Vector2D(cx-posX,cy-posY)).orthogonal();
		if (a.dot(b)<0) b = b.negated();
		double alpha = b.angle(a);
		return (angle+alpha)/SimpleDriver.steerLock;
	}
	
	
	public double[] choosePoint(double posX,double posY,Vector2D dir,Vector2D p1,Vector2D p2){
		Vector2D a1 = new Vector2D(posX-p1.x,posY-p1.y);
		Vector2D a2 = new Vector2D(posX-p2.x,posY-p2.y);
		double[] r = new double[2];		
		double d1 = a1.dot(dir);
		double d2 = a2.dot(dir);
//		double d12 = a1.dot(a2);
		if (d1*d2>=0){
			if (Math.abs(d2)>=Math.abs(d1)){
				r[0] = p1.x;
				r[1] = p1.y;
			} else {
				r[0] = p2.x;
				r[1] = p2.y;
			}						
		} else if (d2>=0){
			r[0] = p1.x;
			r[1] = p1.y;
		} else {
			r[0] = p2.x;
			r[1] = p2.y;
		}
		return r;
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#drive(solo.State)
	 */
	@Override
	public ObjectList<CarControl> drive(State<NewCarState,CarControl> state) {
		// TODO Auto-generated method stub
		ObjectList<CarControl> ol = new ObjectArrayList<CarControl>();
		double angle = state.state.angle;
		double speed = state.state.getSpeed();
		double posX = state.state.posX;
		double posY = state.state.posY;
		double cx = state.state.cx;
		double cy = state.state.cy;
		double d = Geom.distance(posX, posY, cx, cy);
		Vector2D p = new Vector2D(cx-posX,cy-posY).orthogonal().normalised().rotated(angle);
		double closeDist = speed*0.04/3.6;
		double[] r = null;
		double rd = 0;
		
		if (d<targetRadius-1){
			r = Geom.getLineCircleIntersection(posX, posY, p.x, p.y, cx, cy, targetRadius);			
		} else if (d>targetRadius){
			r = Geom.getLineCircleIntersection(posX, posY, p.x, p.y, cx, cy, targetRadius);
			if (r==null){
				Vector2D[] points = Geom.ptTangentLine(posX, posY, cx, cy, targetRadius);
				r = choosePoint(posX, posY, p, points[0], points[1]);				
			}				
		} else {
			double[] points = Geom.getCircleCircleIntersection(posX, posY, closeDist, cx, cy, targetRadius);
			if (points.length==4) 
				r = choosePoint(posX, posY, p, new Vector2D(points[0],points[1]), new Vector2D(points[2],points[3]));
			else if (points.length==2) r = points;
		}
		
		if (r!=null) rd = Geom.distance(r[0], r[1], posX, posY);
		
		double steer = 0;
//		if (rd>closeDist)
		System.out.println(Geom.distance(cx, cy, r[0], r[1])+"     "+d);
		steer = steerToPoint(state, new Vector2D(r[0],r[1]));
		
		int gear = state.state.getGear();
		double brake = 0.0d;
		double dist = state.state.getDistanceRaced();
		double rpm = state.state.getRpm();
		double acc = 1;
		gear = 1;
		

		CarControl cc = new CarControl(acc,brake,gear,steer,0);
		ol.add(cc);
		return ol;
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#restart()
	 */
	
	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#stopCondition(solo.State)
	 */
	@Override
	public boolean stopCondition(State<NewCarState,CarControl> state) {
		// TODO Auto-generated method stub
		double posX = state.state.posX;
		double posY = state.state.posY;
		double cx = state.state.cx;
		double cy = state.state.cy;
//		return Geom.distance(posX, posY, cx, cy)<2;
		return state.state.getLastLapTime()>=5;
	}
	
	public boolean shutdownCondition(State<NewCarState, CarControl> state){
		return (stopCondition(state));
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#restart()
	 */
	@Override
	public CarControl restart() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#shutdown()
	 */
	@Override
	public CarControl shutdown() {
		// TODO Auto-generated method stub
		return new CarControl(0,0,0,0,2);
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#storeSingleAction(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void storeSingleAction(NewCarState input, CarControl action,
			NewCarState output) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#storeSingleAction(java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	
	
	

}
