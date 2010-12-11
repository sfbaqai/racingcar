/**
 * 
 */
package solo;

import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public final class TurnDriver extends BaseDriver {
	int num = 0 ;
	public static double radius = Double.MAX_VALUE;
	/* (non-Javadoc)
	 * @see solo.BaseDriver#drive(java.lang.String)
	 */

//	public static final BaseStateDriver<NewCarState, CarControl> msd = new CircleDriver2();
	public static final CircleDriver2 msd = new CircleDriver2();
	final static double[] r = new double[6];
	final static NewCarState cs = new NewCarState();
	final static CarControl cc = new CarControl();
	public TurnDriver() {		
		// TODO Auto-generated constructor stub
		//		msd = new CircleDriver2();

	}

//	private static final double steerToPoint(NewCarState state,Vector2D point){
//		double angle = state.angle;
//		if (point==null) return angle/SimpleDriver.steerLock;
//		double posX = state.posX;
//		double posY = state.posY;
//		double cx = state.cx;
//		double cy = state.cy;
//		Vector2D a = new Vector2D(cx-posX,cy-posY);
//		double steer = angle-Math.PI/2+a.angle(point);		
//		return (steer)/SimpleDriver.steerLock;
//	}

	/*private static final double steerAtRadius(NewCarState state,double targetRadius){
		long ti = System.nanoTime();
		double angle = state.angle;		
		double posX = state.posX;
		double posY = state.posY;
		System.out.println("SteerAtRad :  "+(System.nanoTime()-ti));
//		double speed = state.getSpeed();
		double cx = state.cx;
		double cy = state.cy;
		double d = Geom.distance(posX, posY, cx, cy);
		System.out.println("SteerAtRad :  "+(System.nanoTime()-ti));
//		Vector2D p = new Vector2D(cx-posX,cy-posY).orthogonal().normalised().rotated(angle);
//		double closeDist = speed*0.04/3.6;
		double[] r = null;
//		double rd;

		System.out.println("SteerAtRad :  "+(System.nanoTime()-ti));
		Vector2D dir = new Vector2D(cx-posX,cy-posY);
		Vector2D v = dir.orthogonal().normalised().rotated(-angle);
		Vector2D o = new Vector2D(posX,posY);
		Vector2D point = null;
		System.out.println("SteerAtRad :  "+(System.nanoTime()-ti));
		
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

//		if (r!=null) rd = Geom.distance(r[0], r[1], posX, posY);				
		return  steerToPoint(state, point.minus(o));		
	}//*/


	@Override
	public final int drive(int[] sensors,int len) {
		// TODO Auto-generated method stub				
		cs.fromString(sensors,len);
		if (num++<=0){			
			double angle = cs.angle;		
			double posX = cs.posX;
			double posY = cs.posY;			
//			double speed = state.getSpeed();
			double cx = cs.cx;
			double cy = cs.cy;
			double dx = cx-posX;
			double dy = cy-posY;
			double d = Math.sqrt(dx*dx+dy*dy);			
//			Vector2D p = new Vector2D(cx-posX,cy-posY).orthogonal().normalised().rotated(angle);
//			double closeDist = speed*0.04/3.6;			
//			double rd;
			double vx = -dy;
			double vy = dx;			
			double sin = Math.sin(-angle);
			double cos = (sin==1.0 || sin==-1.0) ? 0.0 : Math.cos(-angle);				
			if (cos==-1.0) {
				vx = -vx;
				vy = -vy;
			} else if (cos!=1){
				double tmpx = vx;
				vx = vx * cos - vy * sin;
				vy = tmpx * sin + vy * cos;
			}
			vx/=d;
			vy/=d;
			double ox = posX;
			double oy = posY;
			double px = 0;
			double py = 0;			
			if (d<radius-25){					
				int sz = Geom.getLineCircleIntersection(posX, posY, posX+vx, posY+vy, cx, cy, radius,r);
				if (sz!=0) {
					px = r[0];
					py = r[1];
				} else {
					px = posX+vx-posX;
					py = posY+vy-posY;			
				}
			} else if (d>radius){			
				Geom.ptTangentLine(posX, posY, cx, cy, radius,r);
				px = r[0]-ox;
				py = r[1]-oy;									
			} else {			
				px = -dy;
				py = dx;
			}

//			if (r!=null) rd = Geom.distance(r[0], r[1], posX, posY);				
						
			double steer =(px==0 && py==0) ? angle/EdgeDetector.steerLock:angle-Math.PI/2-Math.atan2(py, px)+Math.atan2(dy, dx);		
			steer /= EdgeDetector.steerLock;

//			double steer =steerToPoint(cs, point.minus(o));
//			double steer = TurnDriver.steerAtRadius(cs, radius);
			int gear = (num>=500) ? 2 : 1;			
			cc.accel = 1;
			cc.brake = 0;
			cc.gear = gear;
			cc.steer = steer;
			cc.meta = 0;
			
			return cc.toBytes();
		}
		msd.drive(cs,cc);
//		if (cs!=null && cc!=null) System.out.println(cs.getRpm()+"   "+cc.getAccel()+"    "+cc.getGear()+"    "+cs.getDistanceRaced());
//		if (cc==null) return new CarControl(0,0,0,0,1).toBytes();
		System.out.println(cc);
		return cc.toBytes();
	}

	@Override
	public final void onShutdown(){
		num = 0;		
	};

	@Override
	public final void onRestart(){
		num = 0;
	};


}
