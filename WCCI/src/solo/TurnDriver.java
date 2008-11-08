/**
 * 
 */
package solo;

import com.graphbuilder.geom.Geom;

import it.unimi.dsi.fastutil.objects.ObjectSortedSet;

/**
 * @author kokichi3000
 *
 */
public class TurnDriver extends BaseDriver {
	int num = 0 ;
	public static double radius = 47;
	/* (non-Javadoc)
	 * @see solo.BaseDriver#drive(java.lang.String)
	 */
	
	BaseStateDriver<NewCarState, CarControl> msd;
	
	public TurnDriver() {		
		// TODO Auto-generated constructor stub
		msd = new CircleDriver2();
		
	}

	public double steerToPoint(NewCarState state,Vector2D point){
		double angle = state.angle;
		if (point==null) return angle/SimpleDriver.steerLock;
		double posX = state.posX;
		double posY = state.posY;
		double cx = state.cx;
		double cy = state.cy;
		Vector2D a = new Vector2D(cx-posX,cy-posY);
		double steer = angle-Math.PI/2+a.angle(point);		
		return (steer)/SimpleDriver.steerLock;
	}
	
	public double steerAtRadius(NewCarState state,double targetRadius){
		double angle = state.angle;		
		double posX = state.posX;
		double posY = state.posY;
		double speed = state.getSpeed();
		double cx = state.cx;
		double cy = state.cy;
		double d = Geom.distance(posX, posY, cx, cy);
		Vector2D p = new Vector2D(cx-posX,cy-posY).orthogonal().normalised().rotated(angle);
		double closeDist = speed*0.04/3.6;
		double[] r = null;
		double rd = 0;
		
		Vector2D dir = new Vector2D(cx-posX,cy-posY);
		Vector2D v = dir.orthogonal().normalised().rotated(-angle);
		Vector2D o = new Vector2D(posX,posY);
		Vector2D point = null;
		if (d<targetRadius-25){					
			r = Geom.getLineCircleIntersection(posX, posY, posX+v.x, posY+v.y, cx, cy, targetRadius);
			if (r!=null) point = new Vector2D(r[0],r[1]);
			else point = new Vector2D(posX+v.x, posY+v.y);			
		} else if (d>targetRadius){			
			Vector2D[] points = Geom.ptTangentLine(posX, posY, cx, cy, targetRadius);
			point = points[0];						
		} else {			
			point = o.plus(new Vector2D(dir.orthogonal()));		
		}
		
		if (r!=null) rd = Geom.distance(r[0], r[1], posX, posY);		
		return  steerToPoint(state, point.minus(o));		
	}

	
	@Override
	public String drive(String sensors) {
		// TODO Auto-generated method stub
		CarControl cc = null;
		try{
			
		NewCarState cs = new NewCarState(sensors);
		if (num++<=0){
			double steer = steerAtRadius(cs, radius);
			int gear = (num>=500) ? 2 : 1;
			cc = new CarControl(1.0d,0,gear,steer,0);
			return cc.toString();
		}
		cc = msd.wDrive(cs);
		//if (cs!=null && cc!=null) System.out.println(cs.getRpm()+"   "+cc.getAccel()+"    "+cc.getGear()+"    "+cs.getDistanceRaced());
		if (cc==null) return new CarControl(0,0,0,0,1).toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cc.toString();
	}

	public void onShutdown(){
		num = 0;		
	};
	
	public void onRestart(){
		num = 0;
	};


}
