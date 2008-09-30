/**
 * 
 */
package solo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;

/**
 * @author kokichi3000
 *
 */
public class RPMDriver extends BaseStateDriver<CarRpmState,CarControl> {
	
	private BufferedWriter writer;
	/**
	 * 
	 */	
	double brake = 1.0d;
	public RPMDriver() {
		// TODO Auto-generated constructor stub
		File file = new File("brake-results.txt");
		ignoredExisting = true;
		storeNewState = false;
		try{
			writer = new BufferedWriter(new FileWriter(file));			
		} catch (Exception e){
			e.printStackTrace();
		}
		try{
			writer.write("OldState\tAction\t\tNewState");
			writer.newLine();
			writer.flush();
		} catch (Exception e){
			e.printStackTrace();			
		}//*/
	}

	/**
	 * @param name
	 */
	public RPMDriver(String name) {
		super(name);
		ignoredExisting = true;
		storeNewState = false;
		// TODO Auto-generated constructor stub
		File file = new File(name+"-brake-results.txt");
		try{
			writer = new BufferedWriter(new FileWriter(file,true));			
		} catch (Exception e){
			e.printStackTrace();
		}
		try{
			writer.write("OldState\tAction\t\tNewState");
			writer.newLine();
			writer.flush();
		} catch (Exception e){
			e.printStackTrace();			
		}
	}
	
	

	@Override
	public void init() {
		// TODO Auto-generated method stub
		if (target!=null)
			try{
				writer.write(target.state.getSpeed()+"\t\t"+target.state.getRPM()+"\t\t");
				//System.out.println(target.state.getRPM());
			} catch (Exception e){
				e.printStackTrace();
			}
	}
	
	
	@Override
	public ObjectList<CarControl> drive(State<CarRpmState, CarControl> state) {
		// TODO Auto-generated method stub
		double steer = state.state.getAngleToTrackAxis()/SimpleDriver.steerLock;
		double speed = state.state.getSpeed();		
		ObjectList<CarControl> ol = new ObjectArrayList<CarControl>();		

		/*if (state.state.getSpeed()>=296.8 || state.state.gear==6){
			for (int i =0;i<1;++i){
				CarControl cc = new CarControl(1.0d,0,6,steer,0);
				ol.add(cc);
			}
		} else if (state.state.getSpeed()>=225 || state.state.gear==5){
			for (int i =0;i<1;++i){
				CarControl cc = new CarControl(1.0d,0,5,steer,0);
				ol.add(cc);
			}
		} else if (state.state.getSpeed()>=176 || state.state.gear==4){
			for (int i =0;i<1;++i){
				CarControl cc = new CarControl(1.0d,0,4,steer,0);
				ol.add(cc);
			}
		} else if (state.state.getSpeed()>=126 || state.state.gear==3){
			for (int i =0;i<1;++i){
				CarControl cc = new CarControl(1.0d,0,3,steer,0);
				ol.add(cc);
			}
		} else if (state.state.getSpeed()>=83){
			for (int i =0;i<1;++i){
				CarControl cc = new CarControl(1.0d,0,2,steer,0);
				ol.add(cc);
			}
		} else 	{
			CarControl cc = new CarControl(1.0d,0,1,steer,0);
			ol.add(cc);
		}//*/
		if (speed>225){
			CarControl cc = new CarControl(0,1,4,steer,0);
			ol.add(cc);
		} else if (speed>126){
			CarControl cc = new CarControl(0,0.569,3,steer,0);
			ol.add(cc);
		} else if (speed>83){
			CarControl cc = new CarControl(0,0.363,2,steer,0);
			ol.add(cc);
		} else {
			CarControl cc = new CarControl(0,0.36,1,steer,0);
			ol.add(cc);
		}
		if (speed<226.541 && speed>200){
			ol.clear();
			CarControl cc = new CarControl(0,0.6,4,steer,0);
			ol.add(cc);
		}
		//}//*/ 
		/*if (speed<3*3.6){
			CarControl cc = new CarControl(0,1.0d,0,steer,0);
			ol.add(cc);
			brake = 1;
			return ol;
		}
		double[] wheelVel = state.state.getWheelSpinVel();
		// convert speed to m/s
				
		// when spedd lower than min speed for abs do nothing		

		// compute the speed of wheels in m/s
		double slip = 0.0d;	    
		for (int i = 0; i < 4; i++)	{
			slip += wheelVel[i] * SimpleDriver.wheelRadius[i]/speed;			
		}
		slip/=4;			
		if (slip==0){
			brake = 0.25;
		} if (slip < 0.1) 
			brake = 0.4+slip;
		else brake=1;
		if (brake<=0.09) brake=1;
		brake = Math.min(1.0, brake);
		System.out.println(slip+"   "+brake);
		CarControl cc = new CarControl(0,brake,0,steer,0);
		ol.add(cc);//*/
		return ol;
	}

	@Override
	public CarControl restart() {
		// TODO Auto-generated method stub
		
		try{		
			writer.write(current.state.getDistanceRaced()+"\t\t\t");
			writer.newLine();
			writer.flush();//*/
			pathToTarget = null;
			current = null;
			action = null;			
		} catch (Exception e){
			e.printStackTrace();
		}
		return new CarControl(0,0,0,0,1);
	}

	@Override
	public CarControl shutdown() {
		// TODO Auto-generated method stubrt
		try{
			writer.write(current.state.getDistanceRaced()+"\t\t\t");
			writer.newLine();			
			writer.close();
			System.out.println((current.state.getDistanceRaced()-start.state.getDistanceRaced())+"    "+current.state.getDistanceRaced()+"    "+current.state.getCurLapTime());
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
	public boolean stopCondition(State<CarRpmState, CarControl> state) {
		// TODO Auto-generated method stub
		//System.out.println(state.state.getLastLapTime());
		//System.out.println(Runtime.getRuntime().freeMemory());
		//System.out.println(state.state.getLastLapTime());
//		return (state.state.getLastLapTime()>=40);		
//		return (state.num>=target.num+1);
//		return (start!=null) ? (state.num>=start.num+50): (state.num>=3);
		return (state.num>start.num && state.state.getSpeed()<0.5);
	}
	
	public static double round(double v){
		return ((int)(v*10000))/10000.0d;
	}

	@Override
	public void storeSingleAction(CarRpmState input, CarControl action,
			CarRpmState output) {
		
		// TODO Auto-generated method stub
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
