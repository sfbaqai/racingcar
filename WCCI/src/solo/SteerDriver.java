/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public class SteerDriver extends BaseStateDriver<NewCarState,CarControl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2390261840257478234L;
	private BufferedWriter writer;
	/**
	 * 
	 */	
	double brake = 1.0d;
	double targetSpeed = 360;
	double targetSteer = -0.011;
	double maxSpeed = 0;
	double smaxSpeed = 0;
	double x1,y1,x2,y2,x3,y3;
	double avgSpeed = 0;
	double savgSpeed = 0;
	double savgSteer = 0;
	double savgAngle = 0;
	double avgSteer = 0;
	double avgAngle = 0;
	double savgRadius = 0;
	double avgRadius = 0;	
	double distanceRaced = 0;
	double speedAtChange = 0;
	int no = 0;
	double maxDist =0;
	double maxAtChange;
	double maxRPM;
	double px = -10000;
	double py = -10000;
	int currentGear = 2;

	public static final int n = 1;
//	public static double[] gearUp = {0,8657-2000,8746,8902,8736,8427,10000};
//	public static double[] gearDown = {0,2094,6300,6350,6300,6300,6300};
//	public static double[] gearUp = {0,8780,8730,8000,8900,8900,10000};//3117.03
	public static double[] gearUp = new double[]{0,7000,8400,7800,7800,9000,10000};//3117.03
//	public static double[] gearUp = {0,8910,8840,8810,8610,8460,10000};//3117.03
//	public static double[] gearUp = new double[]{0,8800,8800,8600,8600,8900,10000};//3117.03
	public static double[] minRPM = {9000,9000,9000,9000,9000,9000,9000};
	public static double[] maxRPMAtGear = {0,0,0,0,0,0,0};
	public static double[] speedChange = {0,0,0,0,0,0,0};
	public static double[] gearDown = {0,0,0,0,0,0,0};
	public SteerDriver() {
		// TODO Auto-generated constructor stub
		File file = new File("rpm-results-1st.txt");
		boolean exists = file.exists();
		ignoredExisting = true;
		storeNewState = false;		
		setRepeatNum(n);
		try{

			writer = new BufferedWriter(new FileWriter(file,true));								
			if (!exists) {
//				writer.write("Speed\t\tAngle\t\tSteer\t\tRadius");
				writer.write("Steer\t\tGear\t\tRPM\t\tSpeed\t\tDistance\t\tMin RPM\t\tMax speed\t\tRadius");
				writer.newLine();
			}

			writer.flush();
		} catch (Exception e){
			e.printStackTrace();			
		}//*/
	}

	/**
	 * @param name
	 */
	public SteerDriver(String name) {
		super(name);
		ignoredExisting = true;
		storeNewState = false;
		// TODO Auto-generated constructor stub
		File file = new File(name+"-brake-results.txt");
		boolean exists = file.exists();
		ignoredExisting = true;
		storeNewState = false;		
		setRepeatNum(n);
		try{

			writer = new BufferedWriter(new FileWriter(file,true));								
			if (!exists) {
				writer.write("Gear\t\tSpeed\t\tAngle\t\tSteer\t\tRadius");
				writer.newLine();
			}

			writer.flush();
		} catch (Exception e){
			e.printStackTrace();			
		}//*/
	}



	@Override
	public void init() {
		// TODO Auto-generated method stub
		avgSteer = 0;
		avgSpeed = 0;
		avgAngle = 0;
		avgRadius = 0;
		no = 0;

	}

	boolean ok =false;

	@Override
	public ObjectList<CarControl> drive(State<NewCarState, CarControl> state) {
		// TODO Auto-generated method stub
		double steer = state.state.getAngleToTrackAxis()/SimpleDriver.steerLock-state.state.getTrackPos();
		double speed = state.state.getSpeed();		
		ObjectList<CarControl> ol = new ObjectArrayList<CarControl>();
		int gear = state.state.getGear();
		double brake = 0.0d;
//		double dist = state.state.getDistanceRaced();
		double rpm = state.state.getRpm();
		steer = targetSteer;

		if (minRPM[gear]>rpm) minRPM[gear] = rpm;
		if (maxRPMAtGear[gear]<rpm) maxRPMAtGear[gear] = rpm;
		if (rpm>gearUp[gear] && gear<=currentGear){
			gear++;
			if (gear==currentGear+1) {
				speedAtChange = speed;				
			}
		} else if (rpm<=gearDown[gear])
			gear--;

		if (maxSpeed<speed) maxSpeed = speed;

//		System.out.println(rpm+"   "+maxSpeed+"   "+gear);
		/*if (state.state.getSpeed()>=296.8 || (state.state.gear==6 && speed>=291)){
			gear = 6;
			System.out.println(rpm+"   "+speed+"   "+gear);
		} else if (state.state.getSpeed()>=225 || (state.state.gear==5 && speed>=221)){			
			gear = 5;
			System.out.println(rpm+"   "+speed+"   "+gear);
		} else if (state.state.getSpeed()>=176 || (state.state.gear==4 && speed>=174)){			
			gear = 4;
			System.out.println(rpm+"   "+speed+"   "+gear);
		} else if (state.state.getSpeed()>=126 || (state.state.gear==3 && speed>=125)){			
			gear = 3;
			System.out.println(rpm+"   "+speed+"   "+gear);
		} else if (state.state.getSpeed()>=83 || (state.state.gear==2 && speed>=82)){
			gear = 2;
			System.out.println(rpm+"   "+speed+"   "+gear);
		} else 	{
			gear = 1;
			System.out.println(rpm+"   "+speed+"   "+gear);
		}//*/

		double acc = 0;
		acc = 1;
//		distanceRaced = dist;
		if (px!=-10000 || py!=-10000)
			distanceRaced += Geom.distance(px, py, state.state.posX, state.state.posY);
		px = state.state.posX;
		py = state.state.posY;

//		System.out.println(distanceRaced+"    "+dist);
//		if (speed<targetSpeed) 
//		acc = 1.0d;
//		else acc = 2/(1+Math.exp(speed - targetSpeed-1)) - 1;


		double time = state.state.getLastLapTime(); 
		if (time>=45 && time<=45.05){
			x1 = state.state.posX;
			y1 = state.state.posY;
		}

//		if (dist>=700)
//		steer = targetSteer;

		if (time>=50 && time<=50.05){
			x2 = state.state.posX;
			y2 = state.state.posY;
		}


		if (time>53){
			x3 = state.state.posX;
			y3 = state.state.posY;
			double[] r = new double[3];
			Geom.getCircle(x1, y1, x2, y2, x3, y3, r);			

			no++;
			avgSpeed += speed;
			avgAngle += state.state.angle;
			avgSteer += steer;			
			avgRadius += Math.sqrt(r[2]);
		}

//		if (speed>=110) gear = 3;
//		steer = targetSteer;

		CarControl cc = new CarControl(acc,brake,gear,steer,0);		
		ol.add(cc);
		return ol;
	}
	
	public void reset(){		
		currentGear=2;
		targetSteer += (targetSpeed>=-0.025) ? 0.0005 : (targetSteer>=-0.1) ? 0.005 : 0.05;		
		maxDist = 0;		
		distanceRaced = 0;		
		for (int i=0;i<minRPM.length;++i){
			minRPM[i] = 9000;							
			maxRPMAtGear[i] = 0;
		}

		maxSpeed = 0;
		avgSpeed = 0;		
		gearUp = new double[]{0,7000,8400,7800,7800,9000,10000};//3117.03
		TurnDriver.radius = round(savgRadius/no);
	}

	@Override
	public CarControl restart() {
		// TODO Auto-generated method stub
//		targetSpeed += 0;
//		targetSteer += 0.09;
//		System.out.println(currentGear+"\t\t"+gearUp[currentGear]+"\t\t"+speedAtChange+"\t\t"+distanceRaced);
		px = -10000;
		py = -10000;
		

		if (maxDist<distanceRaced){
			maxDist = distanceRaced;
			maxAtChange = speedAtChange;
			maxRPM = gearUp[currentGear];
			speedChange[currentGear] = speedAtChange;
			smaxSpeed = avgSpeed;
			savgSpeed = avgSpeed;
//			savgSteer = savgSteer;
			savgRadius = avgRadius;
			savgAngle = avgAngle;
			if (currentGear>2 && Math.abs(speedAtChange-speedChange[currentGear-1])==0){				
				reset();
				return new CarControl(0,0,0,0,1);				
			}
		}
//		System.out.println(new DoubleArrayList(gearUp));
//		System.out.println(new DoubleArrayList(maxRPMAtGear));
//		System.out.println();

//		if (maxRPMAtGear[currentGear]<gearUp[currentGear]){			
//			try{				
//				System.out.println(targetSteer+"\t\t"+currentGear+"\t\t"+maxRPM+"\t\t"+maxAtChange+"\t\t"+round(maxDist)+"\t\t"+minRPM[currentGear]+"\t\t"+round(savgSpeed/no)+"\t\t"+round(savgRadius/no));
//				writer.write(targetSteer+"\t\t"+currentGear+"\t\t"+maxRPM+"\t\t"+maxAtChange+"\t\t"+round(maxDist)+"\t\t"+minRPM[currentGear]+"\t\t"+round(savgSpeed/no)+"\t\t"+round(savgRadius/no));
//				writer.newLine();
//				writer.flush();//*/
//			} catch (Exception e){
//				e.printStackTrace();
//			}
//			reset();
//			return new CarControl(0,0,0,0,1);
//		}

		if (gearUp[currentGear]>8900 ){
			try{				
				//			System.out.println(round(avgSpeed/no)+"\t\t"+round(avgAngle/no)+"\t\t"+round(avgSteer/no)+"\t\t"+round(avgRadius/no)+"\t\t"+distanceRaced);
				//			writer.write(round(avgSpeed/no)+"\t\t"+round(avgAngle/no)+"\t\t"+round(avgSteer/no)+"\t\t"+round(avgRadius/no)+"\t\t"+distanceRaced);
//				System.out.println(gearUp[1]+"\t\t"+speedAtChange+"\t\t"+distanceRaced);
//				writer.write(gearUp[1]+"\t\t"+speedAtChange+"\t\t"+distanceRaced);
				System.out.println(targetSteer+"\t\t"+currentGear+"\t\t"+maxRPM+"\t\t"+maxAtChange+"\t\t"+round(maxDist)+"\t\t"+minRPM[currentGear]+"\t\t"+round(savgSpeed/3.6/no)+"\t\t"+round(savgRadius/no));
				writer.write(targetSteer+"\t\t"+currentGear+"\t\t"+maxRPM+"\t\t"+maxAtChange+"\t\t"+round(maxDist)+"\t\t"+minRPM[currentGear]+"\t\t"+round(savgSpeed/3.6/no)+"\t\t"+round(savgRadius/no));
				writer.newLine();
				writer.flush();//*/
				pathToTarget = null;
				current = null;
				action = null;
				gearUp[currentGear] = maxRPM;
				currentGear++;
				if (currentGear>6) {
					reset();
				}				
				distanceRaced = 0;
			} catch (Exception e){
				e.printStackTrace();
			}
			maxDist = 0;
			maxSpeed = 0;			
//			gearUp[currentGear] = 8300;			
			return new CarControl(0,0,0,0,1);
		}


		gearUp[currentGear] += 200;
		distanceRaced = 0;
		maxSpeed = 0;		//		
		return new CarControl(0,0,0,0,1);
	}

	@Override
	public CarControl shutdown() {
		// TODO Auto-generated method stubrt
		try{
//			System.out.println(round(avgSpeed/no)+"\t\t"+round(avgAngle/no)+"\t\t"+round(avgSteer/no)+"\t\t"+round(avgRadius/no)+"\t\t"+distanceRaced);
//			writer.write(round(avgSpeed/no)+"\t\t"+round(avgAngle/no)+"\t\t"+round(avgSteer/no)+"\t\t"+round(avgRadius/no)+"\t\t"+distanceRaced);
//			System.out.println(gearUp[1]+"\t\t"+speedAtChange+"\t\t"+distanceRaced);
//			writer.write(gearUp[1]+"\t\t"+speedAtChange+"\t\t"+distanceRaced);
//			for (int i=0;i<6;++i)
//			System.out.println(gearUp[i]);

//			System.out.println(targetSteer+"\t\t"+maxRPM+"\t\t"+maxAtChange+"\t\t"+maxDist);
//			writer.write(targetSteer+"\t\t"+maxRPM+"\t\t"+maxAtChange+"\t\t"+maxDist);
//			writer.newLine();			
			writer.close();
//			System.out.println((current.state.getDistanceRaced()-start.state.getDistanceRaced())+"    "+current.state.getDistanceRaced()+"    "+current.state.getCurLapTime());
			//save("all-speed.txt");
			/*ObjectSortedSet<CarRpmState> ss = map.keySet();
			for (CarRpmState st:ss){							
				if (st.getSpeed()<=285) map.remove(st);
			}
//
			save("speed-fifth-init.txt");
//			System.out.println(current.num);
//			save("rpm.txt",current);
			/*RPMDriver msd = new RPMDriver("speed-snd-init.txt");			
			ObjectSortedSet<CarRpmState> s = msd.map.keySet();
			for (CarRpmState st:s){							
				System.out.println(st.getSpeed());
			}
			System.out.println();//*/
		} catch (Exception e){
			e.printStackTrace();
		}
		return new CarControl(0,0,0,0,2);
	}

	@Override
	public boolean shutdownCondition(State<NewCarState, CarControl> state){
		return (targetSteer>=-0.005 && stopCondition(state));
	}

	@Override
	public boolean stopCondition(State<NewCarState, CarControl> state) {
		// TODO Auto-generated method stub
		//System.out.println(state.state.getLastLapTime());
		//System.out.println(Runtime.getRuntime().freeMemory());
		//System.out.println(state.state.getLastLapTime());
		return (state.state.getLastLapTime()>=60);		
//		return (state.num>=target.num+1);
//		return (start!=null) ? (state.num>=start.num+50): (state.num>=3);
//		return (state.num>start.num && state.state.getSpeed()<0.5);
//		return (state.state.getDistanceRaced()>2000);
	}

	public static double round(double v){
		return ((int)(v*1000))/1000.0d;
	}

	@Override
	public void storeSingleAction(NewCarState input, CarControl action,
			NewCarState output) {

		// TODO Auto-generated method stub
		System.out.println(input+"   "+action+"    "+output);
		try{			
			if (input!=null && action!=null && output!=null){
				double[] wheels = input.getWheelSpinVel();
				double inputAvg = 0;
				for (int i=0;i<wheels.length;++i)
					inputAvg+=wheels[i];
				inputAvg /=wheels.length;

				wheels = output.getWheelSpinVel();
				double outputAvg = 0;
				for (int i=0;i<wheels.length;++i)
					outputAvg+=wheels[i];
				outputAvg /=wheels.length;
				System.out.println(input.getSpeed()+"\t\t"+new DoubleArrayList(input.getWheelSpinVel())+"\t\t"+round(action.getBrake())+"\t\t"+output.getSpeed()+"\t\t"+new DoubleArrayList(output.getWheelSpinVel())+"\t\t"+output.getDistanceRaced());
				writer.write(round(input.getSpeed())+"\t\t"+round(action.getBrake())+"\t\t"+round(output.getSpeed()));
				writer.newLine();
				writer.flush();
			}
		} catch (Exception e){
			e.printStackTrace();
		}//*/
	}


}
