/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.ObjectSortedSet;

/**
 * @author kokichi3000
 *
 */
public class TurnDriver extends BaseDriver {
	int num = 0 ;
	/* (non-Javadoc)
	 * @see solo.BaseDriver#drive(java.lang.String)
	 */
	
	BaseStateDriver<NewCarState, CarControl> msd;
	
	public TurnDriver() {		
		// TODO Auto-generated constructor stub
		msd = new SteerDriver();
		
	}

	
	@Override
	public String drive(String sensors) {
		// TODO Auto-generated method stub
		CarControl cc = null;
		try{
			if (num++<=100){
				cc = new CarControl(0.0d,0,0,0,0);
				return cc.toString();
			}
		NewCarState cs = new NewCarState(sensors);		
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
