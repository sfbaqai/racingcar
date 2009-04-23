/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import com.graphbuilder.geom.Geom;

/**
 * @author kokichi3000
 *
 */
public final class CircleDriver extends BaseStateDriver<NewCarState,CarControl> {

	double targetRadius = 	180;
	double maxSpeed = 0;
	double AllowTime = 60;
	/**
	 * 
	 */
	final static int MAX_GEARS = 10;
	double x =0 ;
	double y = 0;
	final static int GEAR_OFFSET = 1;
	final static double enginerpmRedLine = 890.118;//rpm;
	final static double[] wheelRadius= new double[]{0.3179,0.3179,0.3276,0.3276};
	final static double[] wheelRadiusInInch= new double[]{8.5,8.5,9,9};
	final static double WIDTHDIV = 1;
	final static int[] gearUp = new int[]{5000,6000,6000,6500,7000,7500};
	final static int[] gearDown = new int[]{0,2500,3000,3000,3250,3500};
	final static double[] gearRatio = new double[]{-9.0,0,11.97,8.01,5.85,4.5,3.33,2.7,0,0};
	double sp = 0;
	double r = 0;
	double st = 0;
	
	double[] shift = new double[MAX_GEARS];
	
	public CircleDriver() {
		// TODO Auto-generated constructor stub
		ignoredExisting = true;
		storeNewState = false;
		int i = 0;
		int j = 0;
		for (i = 0; i < MAX_GEARS; i++) {
			j = i + 1;
			if (j < MAX_GEARS) {
				if ((gearRatio[j] != 0) && (gearRatio[i] != 0)) {				
					shift[i] = enginerpmRedLine * .95  * wheelRadius[2] / gearRatio[i];
					/* GfOut("   Gear %d: shift %d km/h\n", i, (int)(shiftThld[idx][i] * 3.6)); */
				} else {
					shift[i] = 10000.0;
				}
			} else {
				shift[i] = 10000.0;
			}				
		}
	}
	
	public int getGear(CarState cs) {
		// TODO Auto-generated method stub
		int gear = cs.gear;
		double speedX = cs.speedX/3.6;

		// if gear is 0 (N) or -1 (R) just return 1 
		if (gear<1)
			return 1;

		if (speedX > shift[gear+ GEAR_OFFSET]) {
			gear++;
		} else if ((gear > 1) && (speedX < (shift[gear-1+GEAR_OFFSET] - 10.0))) {
			gear--;
		}
		if (gear <= 0) {
			gear++;
		}

		return gear;
	}
	
	public double radiusAtSpeed2(double x){
		double x2 = x*x;
		double x3 = x2*x;
		double x4 = x3*x;
		double x5 = x4*x;
		if (x>260)
			return 4759722.8775742+-52518.6677526835*x+231.936199457280*x2+-0.474532967657544*x3+0.000374800520419819*x4-2740429563478.46/x3;												
		
		return 1.97157219188452-0.0380301186187391*x+0.00617982060631129*x2+1.92780989373261e-06*x3-9.85584049833827e-08*x4+1.76376838517705e-10*x5;		
	}
	
	
	public double speedAtRadius2(double x){
		double x2 = x*x;
		double x3 = x2*x;
		double x4 = x3*x;
		double x5 = x4*x;
		if (x>203)
			return -751.9618007523+16.0592470777102*x-0.105355292602886*x2+0.000360802882499374*x3-6.33808987579942e-07*x4+4.52719911613057e-10*x5;
						
		return 56.0002692614677+1.02197055318988*x-0.00103091318164713*x2-1.00265316544859e-06*x3+2.49298521633964e-08*x4+2.1741710332926e-11*x5-486.679144717839/x+2914.00391917569/x2-7049.70569671068/x3;	
	}

	public double steerAtSpeed2(double x){
		x = 1/x;
		return -0.0351310657545812+11.7204375174216*x+174.282269175752*x*x+11306.4262474072*x*x*x-251021.111952890*x*x*x*x;
	}
	
	public double speedAtSteer2(double x){
		x = 1/x;
		return 23.9002544012561+9.75117045792484*x-0.201635977871448*x*x+0.00208755378801424*x*x*x-7.80922017545535e-06*x*x*x*x;					
	}
	
	public double steerAtRadius2(double x){
		x=1/x;
		return -0.0254079807304042+9.0297650687339*x-108.447289295954*x*x+897.064010910304*x*x*x-2452.73027547944*x*x*x*x;
	}
	
	public double radiusAtSteer2(double x){
		x = 1/x;
		return -6.8769056985998+7.58244480442587*x-0.0998087896256923*x*x+0.000277294845100101*x*x*x+2.54016337557857e-06*x*x*x*x;
	}
	
	public double steerAtSpeed3(double x){
		if (x>=216)
			return 0.405956540491453-0.00402398400604155*x+1.46983630854209e-05*x*x-1.88619693222940e-08*x*x*x;
		if (x>180)
			return -6.40122354928717+0.100933617693404*x-0.000519289942888996*x*x+8.79221413082921e-07*x*x*x;
		if (x>140)
			return 1.31349334978100-0.0158496396133958*x+6.53860943510914e-05*x*x-8.64166930377492e-08*x*x*x;		
		return 1.00309570103181-0.0115995398441519*x+4.70782598803805e-05*x*x-6.49876327849716e-08*x*x*x;	
	}
	
	public double steerAtRadius3(double x){
		if (x>=100)
			return 0.0810250510703418-0.000750740530082075*x+2.89854924506364e-06*x*x-3.95924129765923e-09*x*x*x;
		if (x>=70)
			return -1.36459470940743+0.0582094289481451*x-0.000746679722815475*x*x+3.04328497896615e-06*x*x*x;
		if (x>=50)
			return -0.485794442078747+0.0364424208448416*x-0.000642936741947271*x*x+3.44706692338737e-06*x*x*x;
			
		return 1.02589841472933-0.0615964400025362*x+0.00163249602462468*x*x-1.46149044885214e-05*x*x*x;		
	}
	
	public double radiusAtSpeed3(double speedkmh){
		if (speedkmh<=205)
			return 0.0820619978719424+0.00541730524763449*speedkmh+0.00464210551818003*speedkmh*speedkmh-1.20686439387299e-05*speedkmh*speedkmh*speedkmh; 
		if (speedkmh<274)
			return -1192.18464416069+16.6397488904336*speedkmh-0.0740555508240852*speedkmh*speedkmh+0.000114411488519385*speedkmh*speedkmh*speedkmh;
		if (speedkmh<285)
			return 125181.064069584-1325.79717496569*speedkmh+4.67785276024091*speedkmh*speedkmh-0.00549045392467163*speedkmh*speedkmh*speedkmh;			
			
		return -83508.7895089672+887.064242540164*speedkmh-3.14306813772447*speedkmh*speedkmh+0.00372277142586774*speedkmh*speedkmh*speedkmh;
	}
	
	public double speedAtRadius3(double x){
		if (x>=170)
			return 85.5247084519036+2.01905340962707*x-0.00646416884272083*x*x+7.29293433335612e-06*x*x*x;
		if (x>=100)
			return -139.760581424782+6.04756169365199*x-0.0303044500612258*x*x+5.3961109358751e-05*x*x*x;
		if (x>=60)
			return 202.745158728743-4.48739317463141*x+0.0755898471248578*x*x-0.00029146122705404*x*x*x;
		if (x>=20)
			return 26.7657503764833+2.55982229567786*x-0.0171415320257696*x*x+0.000108671654261182*x*x*x;		
		return -0.00728601622351035+7.7247995418915*x+-0.360701254346654*x*x+0.00788103445639814*x*x*x;		
	}
	
	public double radiusAtSpeed(double x){
		double x2 = x*x;
		double x3 = x2*x;
		double x4 = x3*x;
		double x5 = x4*x;
		if (x>=240)
			return -62928534.9760697+785858.308875644*x-4413.4525439081*x2+13.2113085704168*x3-0.0205852939864750*x4+1.31893299944692e-05*x5+213081050935.215/x2;			
			
		return 3691.25064715183-45.2440875835028*x+0.341931648472089*x2-0.00149210322507684*x3+3.5279050744347e-06*x4-3.50744762562337e-09*x5-178615.31239954/x+4694532.43709498/x2-51414212.7178153/x3;
	}
	
	public double speedAtRadius(double x){
		double x2 = x*x;
		double x3 = x2*x;
		double x4 = x3*x;
		double x5 = x4*x;

		if (x>150)
			return -115431.216892959+834.957333227655*x-3.71025373015644*x2+0.00999768673957289*x3-1.50103486982362e-05*x4+9.64495639130066e-09*x5+9519812.76010439/x-414947104.101293/x2+6808805657.1795/x3;		
		return 5.2604417632244+2.93221790821321*x-0.0308952361016195*x2+0.00027900914172188*x3-1.30914988151668e-06*x4+2.81513214824391e-09*x5+358.981440260822/x-3125.65260706368/x2+7536.84482849135/x3;		
	}
	
	public double steerAtRadius(double x){
		double x2 = x*x;
		double x3 = x2*x;
		double x4 = x3*x;
		double x5 = x4*x;
		return -0.182889573695719+0.00471278827366446*x-5.84925200008168e-05*x2+3.44516936579348e-07*x3+-9.50277644808094e-10*x4+9.93293240224858e-13*x5+7.2816188056559/x+169.817270507861/x2-7054.45142199328/x3+94114.88898132/x4-422854.326366947/x5;	
	}
	
	public double steerAtSpeed(double x){
		double x2 = x*x;
		double x3 = x2*x;
		double x4 = x3*x;
		double x5 = x4*x;
		return 367.449886076200-2.44454065787193*x+0.0107062396342743*x2+-2.95582914415686e-05*x3+4.65037598707888e-08*x4-3.17082285704565e-11*x5-36834.3779574756/x+2435072.62486989/x2-101492745.974010/x3+2410306517.41059/x4-24757598555.9949/x5;		
	}
	
	public double speedAtSteer(double x){
		double x2 = x*x;
		double x3 = x2*x;
		double x4 = x3*x;
		double x5 = x4*x;
		return 20161.4325461023-80976.5641032407*x+247596.223709903*x2-570985.722256194*x3+773463.410503563*x4-451233.527431834*x5+424.06496974133/x-6.6261353313125/x2+0.0756342769256019/x3-0.000487869930944558/x4+1.30773405330843e-06/x5+7631.86091567512*Math.log(x);
	}
	
	public double radiusAtSteer(double x){
		double x2 = x*x;
		double x3 = x2*x;
		double x4 = x3*x;
		double x5 = x4*x;
		return 47167.3195081294-195140.396199159*x+623292.360451555*x2-1490780.96491522*x3+2074153.18024627*x4-1230874.05932753*x5+925.321753904883/x-13.7625864682675/x2+0.148997008234320/x3-0.000908614675173261/x4+2.30769349796921e-06/x5+17610.2179686997*Math.log(x);	
	}

	/**
	 * @param name
	 */
	public CircleDriver(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	//point is relative to car
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
		if (d<targetRadius-5){					
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


	double oldSpeed = 0;
	double accel = 0;
	double avgSpeed = 0;
	double avgRadius = 0;
	double avgSteer = 0;
	int count =0 ;
	int n = 0;
	boolean ok = true;
	double mr=0;
	double startTime=10;
	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#drive(solo.State)
	 */
	@Override	
	public ObjectList<CarControl> drive(State<NewCarState,CarControl> state) {
		// TODO Auto-generated method stub		
		ObjectList<CarControl> ol = new ObjectArrayList<CarControl>();
		double speed = state.state.getSpeed();
		double time = state.state.getLastLapTime();
		double posX = state.state.posX;
		double posY = state.state.posY;
		double cx = state.state.cx;
		double cy = state.state.cy;
		double d = Geom.distance(posX, posY, cx, cy);		
		int gear = state.state.getGear();
		double brake = 0.0d;		
		double acc = 1;
		NewCarState cs = state.state;
		
		double steer = steerAtRadius(state.state,targetRadius);
		if (time>60+startTime && time<61+startTime){
			maxSpeed = Math.max(speedAtRadius(targetRadius)/1.5d,maxSpeed-5);
		}
				
		if (time>=60+startTime){			
			n++;
			if (n>0){
				avgSpeed += speed;
				avgRadius += d;			
				avgSteer += steer;
				double r = avgRadius/n;
				if (time>=60+startTime && mr<d) mr = d;
			}
			if (n>200) {				
				sp = avgSpeed/n;
				r = avgRadius/n;
				st = avgSteer/n;
//				System.out.println(maxSpeed+"\t\t"+speed+"\t\t"+r+"\t\t"+targetRadius+"\t\t"+gear);
				if (r<=targetRadius+0.5){
//					System.out.println(maxSpeed+"\t\t"+speed+"\t\t"+r+"\t\t"+targetRadius+"\t\t"+count);
					if (maxSpeed>speed+20)
						count++;
					else if (maxSpeed<speed){
						maxSpeed=speed;						
//						count =0;						
					}
//					System.out.println(maxSpeed+"\t\t"+speed+"\t\t"+sp+"\t\t"+r+"\t\t"+targetRadius+"\t\t"+ok);
										
					if (ok) {
						maxSpeed+=20;						
					}
					if (!ok) count=21;					
//					System.out.println(maxSpeed+"\t"+speed+"\t"+count);
				} else {
//					if (!ok) 
//						maxSpeed--; 
//					else maxSpeed-=0.5;										
//					if (ok) count++;
					count++;
					maxSpeed--;
					ok = false;					
//					System.out.println(maxSpeed+"\t\t"+speed+"\t\t"+sp+"\t\t"+r+"\t\t"+targetRadius+"\t\t"+ok);
				}					
				n = -100;
				avgSpeed = 0;
				avgRadius = 0;
				avgSteer = 0;
//				System.out.println(n+"   "+maxSpeed+"   "+sp+"   "+acc);			
			}			
		}		
		
//		else if (time>=0 && time % AllowTime<0.015){			
//			if (n>0) {
//				double sp = avgSpeed/n;
//				double r = avgRadius/n;
//				System.out.println(maxSpeed+"\t"+radiusAtSpeed(sp)+"\t"+sp+"\t"+r+"\t"+targetRadius);				
//			}
//			n = 0;
//			avgSpeed = 0;
//			avgRadius = 0;
//			targetRadius+=5;
//		}	
		if (maxSpeed<20) maxSpeed = 20;
		if (speed<=maxSpeed-1)
			acc = 1;
		else acc = 2/(1+Math.exp(speed - maxSpeed-1)) - 1;
//		System.out.println(acc+"   "+maxSpeed+"    "+speed);

//		System.out.println(n+"   "+maxSpeed+"   "+acc);
		gear = getGear(cs);
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
//		maxSpeed = sp-0.5;
				
		count = 0;
		ok = true;
		mr = 0;		
		maxSpeed = 20;
		startTime = 0;
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
//		double posX = state.state.posX;
//		double posY = state.state.posY;
//		double cx = state.state.cx;
//		double cy = state.state.cy;
//		return Geom.distance(posX, posY, cx, cy)<2;
//		return state.state.getLastLapTime()>=AllowTime*2000+1;
		return (count>20);
	}
	
	@Override
	public boolean shutdownCondition(State<NewCarState, CarControl> state){
		return (targetRadius>=300);
	}

	/* (non-Javadoc)
	 * @see solo.BaseStateDriver#restart()
	 */
	@Override
	public CarControl restart() {
		// TODO Auto-generated method stub
		targetRadius+=2.5;
		System.out.println(sp+"\t\t"+r+"\t\t"+st+"\t\t"+speedAtSteer(-st)+"\t\t"+radiusAtSteer(-st));
		return new CarControl(0,0,0,0,1);
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
