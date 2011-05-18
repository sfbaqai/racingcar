/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.ObjectSortedSet;


/**
 * @author kokichi3000
 *
 */
public class NewTestDriver extends BaseDriver {

	/**
	 * 
	 */
//	RPMDriver msd = new RPMDriver("speed-snd-init.txt");
//	RPMDriver msd = new RPMDriver("speed-third-init.txt");
//	RPMDriver msd = new RPMDriver("speed-forth-init.txt");
//	RPMDriver msd = new RPMDriver("speed-fifth-init.txt");
	RPMDriver msd = new RPMDriver("all-speed.txt");
//	RPMDriver msd = new RPMDriver();
	int num = 0 ;
	public NewTestDriver() {		
		// TODO Auto-generated constructor stub
		CarRpmState state = null;
		
		ObjectSortedSet<CarRpmState> s = msd.map.keySet();
		/*for (CarRpmState ss:s){
			System.out.print(ss.getRPM()+"   ");
		}
		System.out.println();//*/		
		for (CarRpmState st:s){			
//			if (st.getSpeed()>87 && st.getSpeed()<130)
//			System.out.println(st.getSpeed());
			if (st.getSpeed()<250.2 && st.getSpeed()>250.15){				
				msd.addStartState(st);
				//msd.start = msd.map.get(st);
			}
		}//*/
		
	}

	/* (non-Javadoc)
	 * @see solo.BaseDriver#drive(java.lang.String)
	 */
	@Override
	public String drive(String sensors) {
		// TODO Auto-generated method stub
		CarControl cc = null;
		try{
			if (num++<=100){
				cc = new CarControl(0.0d,0,0,0,0);
				return cc.toString();
			}
		CarRpmState cs = new CarRpmState(sensors);		
		cc = msd.wDrive(cs);
		//if (cs!=null && cc!=null) System.out.println(cs.getRpm()+"   "+cc.getAccel()+"    "+cc.getGear()+"    "+cs.getDistanceRaced());
		if (cc==null) return new CarControl(0,0,0,0,1).toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return cc.toString();
	}

	@Override
	public void onShutdown(){
		num = 0;		
	};
	
	@Override
	public void onRestart(){
		num = 0;
	};
	
}
