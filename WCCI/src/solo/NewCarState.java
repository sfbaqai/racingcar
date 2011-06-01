package solo;

import java.io.Serializable;

public final class NewCarState extends CarState implements Serializable{	
	/**
	 * 
	 */		
	private static final String[] keys = new String[]{"angle",
		"curLapTime",
		"damage",
		"distFromStart",
		"distRaced",
		"fuel",
		"gear",
		"lastLapTime",
		"opponents",
		"racePos",
		"rpm",
		"speedX",
		"speedY",
		"speedZ",//change to fit server2011
		"track",
		"trackPos",
		"wheelSpinVel",
		"z",//change to fit 2011
		"focus",//change to fit 2011
		"posX",
		"posY",
		"posZ",
		"ax",
		"ay",
		"az",
		"cx",
		"cy",
		"cz",
		"v0x",
		"v0y",
		"v1x",
		"v1y",
		"v2x",
		"v2y",
		"v3x",
		"v3y",
		"length",
		"radius",
		"radiusl",
		"radiusr",
		"allTypes",
		"allDist",
		"allX",
		"allY",
		"allRadiusL",
		"allRadiusR",
		"allArc",
		"allLength",
		"allSXL",
		"allSYL",
		"allSXR",
		"allSYR",
		"allEXL",
		"allEXR",
		"allEYL",
	"allEYR"};
	private static final int numKeys = keys.length;
	private static final  double[][] table = new double[numKeys][];
	private static final  int[][] tableInt = new int[numKeys][];
	//	private static final char infinity[] = { 'I', 'n', 'f', 'i', 'n', 'i', 't', 'y' };
	//	private static final char notANumber[] = { 'N', 'a', 'N' };
	//	private static final char zero[] = { '0', '0', '0', '0', '0', '0', '0', '0' };
	private static final int	bigDecimalExponent = 324; // i.e. abs(minDecimalExponent)
	private static final int maxDecimalDigits = 15;
	private static final int maxDecimalExponent = 308;
	private static final int minDecimalExponent = -324;
	private static final int	intDecimalDigits = 9;
	private static final long   highbyte = 0xff00000000000000L;
	private static final long   lowbytes = ~highbyte;
	private static final long   signMask = 0x8000000000000000L;
	private static final long   expMask  = 0x7ff0000000000000L;
	private static final long   fractMask= ~(signMask|expMask);
	private static final int    expShift = 52;
	private static final int    expBias  = 1023;
	private static final long   fractHOB = ( 1L<<expShift ); // assumed High-Order bit
	private static FDBigInteger b5p[];
	//	private static final long   expOne   = ((long)expBias)<<expShift; // exponent of 1.0

	private static final int small5pow[] = {
		1,
		5,
		5*5,
		5*5*5,
		5*5*5*5,
		5*5*5*5*5,
		5*5*5*5*5*5,
		5*5*5*5*5*5*5,
		5*5*5*5*5*5*5*5,
		5*5*5*5*5*5*5*5*5,
		5*5*5*5*5*5*5*5*5*5,
		5*5*5*5*5*5*5*5*5*5*5,
		5*5*5*5*5*5*5*5*5*5*5*5,
		5*5*5*5*5*5*5*5*5*5*5*5*5
	};

	private static final long long5pow[] = {
		1L,
		5L,
		5L*5,
		5L*5*5,
		5L*5*5*5,
		5L*5*5*5*5,
		5L*5*5*5*5*5,
		5L*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
		5L*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5*5,
	};

	/*
	 * All the positive powers of 10 that can be
	 * represented exactly in double/float.
	 */
	private static final int int10pow[] = {
		1,10,100,1000,10000,100000,1000000,10000000,100000000,1000000000	            
	};

	private static final long long10pow[] = {
		1,10,100,1000,10000,100000,1000000,10000000,100000000,1000000000,10000000000L,10000000000L,100000000000L,1000000000000L,10000000000000L,
		100000000000000L,1000000000000000L,10000000000000000L,100000000000000000L,1000000000000000000L
	};


	private static final double small10pow[] = {
		1.0e0,
		1.0e1, 1.0e2, 1.0e3, 1.0e4, 1.0e5,
		1.0e6, 1.0e7, 1.0e8, 1.0e9, 1.0e10,
		1.0e11, 1.0e12, 1.0e13, 1.0e14, 1.0e15,
		1.0e16, 1.0e17, 1.0e18, 1.0e19, 1.0e20,
		1.0e21, 1.0e22
	};

	//	private static final float singleSmall10pow[] = {
	//		1.0e0f,
	//		1.0e1f, 1.0e2f, 1.0e3f, 1.0e4f, 1.0e5f,
	//		1.0e6f, 1.0e7f, 1.0e8f, 1.0e9f, 1.0e10f
	//	};

	private static final double big10pow[] = {
		1e16, 1e32, 1e64, 1e128, 1e256 };
	private static final double tiny10pow[] = {
		1e-16, 1e-32, 1e-64, 1e-128, 1e-256 };

	private static final int maxSmallTen = small10pow.length-1;

	private static final long serialVersionUID = -2057264500803788862L;
	double radius;
	double radiusl;
	double radiusr;
	double posX;
	double posY;
	double posZ;
	double ax;
	double ay;
	double az;
	double length;
	double cx;
	double cy;
	double cz;	
	public static int sz;
	public static final Vector2D[] vertex = new Vector2D[4];
	public static final Vector2D[] center = new Vector2D[50];	
	private static final double [] vx0 = new double[1];
	private static final double [] vx1 = new double[1];
	private static final double [] vx2 = new double[1];
	private static final double [] vx3 = new double[1];
	private static final double [] vy0 = new double[1];
	private static final double [] vy1 = new double[1];
	private static final double [] vy2 = new double[1];
	private static final double [] vy3 = new double[1];
	private static final double[] ang = new double[1];
	private static final double[] curLapTi = new double[1];
	private static final double[] dam = new double[1];
	private static final double[] distFrStart = new double[1];
	private static final double[] distR = new double[1];
	private static final double[] fl = new double[1];
	private static final int[]   gr = new int[1];
	private static final double[] lastLapTi = new double[1];
	private static final double[] trackP = new double[1];    
	private static final int[]   raceP = new int[1];
	private static final double[]   zs = new double[1];
	private static final double[]   speedZs = new double[1];
	private static final double[]   focuss = new double[5];
	private static final double[]   rpmin = new double[1];
	private static final double[] spX = new double[1];
	private static final double[] spY = new double[1];
	private static final double[] trk = new double[19];
	private static final double[] opps = new double[36];
	private static final double[] wSVel = new double[4];
	private static final double[] rad = new double[1];
	private static final double[] radl = new double[1];
	private static final double[] radr = new double[1];
	private static final double[] pX = new double[1];
	private static final double[] pY = new double[1];
	private static final double[] pZ = new double[1];
	private static final double[] a_x = new double[1];
	private static final double[] a_y = new double[1];
	private static final double[] a_z = new double[1];
	private static final double[] len = new double[1];
	private static final double[] c_x = new double[1];
	private static final double[] c_y = new double[1];
	private static final double[] c_z = new double[1];
	public static final double[] dist = new double[50];
	public static final double[] l = new double[50];
	public static final double[] arc = new double[50];
	public static final double[] aX = new double[50];
	public static final double[] aY = new double[50];
	public static final int[] type = new int[50];
	public static final double[] radiusL = new double[50];
	public static final double[] radiusR = new double[50];
	public static final double[] sXL = new double[50];
	public static final double[] sXR = new double[50];
	public static final double[] sYL = new double[50];
	public static final double[] sYR = new double[50];
	public static final double[] eXL = new double[50];
	public static final double[] eXR = new double[50];
	public static final double[] eYL = new double[50];
	public static final double[] eYR = new double[50];

	static {
		for (int i=center.length-1;i>=0;--i) center[i] = new Vector2D();		
		for (int i=3;i>=0;--i) vertex[i] = new Vector2D();		
		//		Arrays.sort(keys);
		for (int i = 0;i<numKeys;++i){
			String s = keys[i];
			Object o = getObject(s);			
			switch (i){
			case 6:
			case 9:
			case 40://change from 37				
				tableInt[i] = (int[])o;					
				break;
			default:
				table[i] = (double[])o;
				break;
			}

		}				
	}

	double toMiddle;		
	public NewCarState(){
		track = NewCarState.trk;
		opponents = NewCarState.opps;
		wheelSpinVel = NewCarState.wSVel;
		//		char[] c = {'-','4','5','1','4','5','1','4','5','1','4','5','1','4','5','1','4','5','1','4','5','1','4','5','1','4','5','1','4','5','1',')'};
		//				char[] c = {'-','0','0','1','2','3',' ',' ','5','6',' ','8',')'};
		//				int[] tmp = new int[10];
		//		int d=toInt(c, 0, c.length);
		//				double i=toIntArray(c, 0, ' ',')',tmp);
		//				System.out.println(i);
		//		for (String s:table.keySet()){
		//			if (s.length()==7) System.out.println(s+"    "+s.length());
		//		}
	};
	/**
	 * Copy Constructor
	 *
	 * @param newCarState a <code>NewCarState</code> object
	 */

	private final static Object getObject(String name){		
		return (name=="z") ? NewCarState.zs :
			(name=="speedZ") ? NewCarState.speedZs :
			(name=="focus") ? NewCarState.focuss :
				(name=="allArc") ? NewCarState.arc :
			(name=="allDist") ? NewCarState.dist :
				(name=="allEXL") ? NewCarState.eXL :
					(name=="allEXR") ? NewCarState.eXR :
						(name=="allEYL") ? NewCarState.eYL :
							(name=="allEYR") ? NewCarState.eYR :
								(name=="allLength") ? NewCarState.l :
									(name=="allRadiusL") ? NewCarState.radiusL :
										(name=="allRadiusR") ? NewCarState.radiusR :
											(name=="allSXL") ? NewCarState.sXL :
												(name=="allSXR") ? NewCarState.sXR :
													(name=="allSYL") ? NewCarState.sYL :
														(name=="allSYR") ? NewCarState.sYR :
															(name=="allTypes") ? NewCarState.type :
																(name=="allX") ? NewCarState.aX :
																	(name=="allY") ? NewCarState.aY :
																		(name=="angle") ? NewCarState.ang :
																			(name=="ax") ? NewCarState.a_x :
																				(name=="ay") ? NewCarState.a_y :
																					(name=="az") ? NewCarState.a_z :
																						(name=="curLapTime") ? NewCarState.curLapTi :
																							(name=="cx") ? NewCarState.c_x :
																								(name=="cy") ? NewCarState.c_y :
																									(name=="cz") ? NewCarState.c_z :
																										(name=="damage") ? NewCarState.dam :
																											(name=="distFromStart") ? NewCarState.distFrStart :
																												(name=="distRaced") ? NewCarState.distR :
																													(name=="fuel") ? NewCarState.fl :
																														(name=="gear") ? NewCarState.gr :
																															(name=="lastLapTime") ? NewCarState.lastLapTi :
																																(name=="length") ? NewCarState.len :
																																	(name=="opponents") ? NewCarState.opps :
																																		(name=="posX") ? NewCarState.pX :
																																			(name=="posY") ? NewCarState.pY :
																																				(name=="posZ") ? NewCarState.pZ :
																																					(name=="racePos") ? NewCarState.raceP :
																																						(name=="radius") ? NewCarState.rad :
																																							(name=="radiusl") ? NewCarState.radl :
																																								(name=="radiusr") ? NewCarState.radr :
																																									(name=="rpm") ? NewCarState.rpmin :
																																										(name=="speedX") ? NewCarState.spX :
																																											(name=="speedY") ? NewCarState.spY :
																																												(name=="track") ? NewCarState.trk :
																																													(name=="trackPos") ? NewCarState.trackP :
																																														(name=="v0x") ? NewCarState.vx0 :
																																															(name=="v0y") ? NewCarState.vy0 :
																																																(name=="v1x") ? NewCarState.vx1 :
																																																	(name=="v1y") ? NewCarState.vy1 :
																																																		(name=="v2x") ? NewCarState.vx2 :
																																																			(name=="v2y") ? NewCarState.vy2 :
																																																				(name=="v3x") ? NewCarState.vx3 :
																																																					(name=="v3y") ? NewCarState.vy3 :
																																																						(name=="wheelSpinVel") ? NewCarState.wSVel : null;
	}

	public NewCarState(NewCarState newCarState) 
	{

		this.z = newCarState.z;
		this.focus = newCarState.focus;
		this.speedZ = newCarState.speedZ;
		//the first 3 line to fit 2011
		this.angle = newCarState.angle;
		this.curLapTime = newCarState.curLapTime;
		this.damage = newCarState.damage;
		this.distFromStart = newCarState.distFromStart;
		this.distRaced = newCarState.distRaced;
		this.fuel = newCarState.fuel;
		this.gear = newCarState.gear;
		this.lastLapTime = newCarState.lastLapTime;
		this.opponents = newCarState.opponents;
		this.racePos = newCarState.racePos;
		this.rpm = newCarState.rpm;
		this.speedX = newCarState.speedX;
		this.speedY = newCarState.speedY;
		this.track = newCarState.track;
		this.trackPos = newCarState.trackPos;
		this.wheelSpinVel = newCarState.wheelSpinVel;
		this.radius = newCarState.radius;
		this.radiusl = newCarState.radiusl;
		this.radiusr = newCarState.radiusr;
		this.posX = newCarState.posX;
		this.posY = newCarState.posY;
		this.posZ = newCarState.posZ;
		this.ax = newCarState.ax;
		this.ay = newCarState.ay;
		this.az = newCarState.az;
		this.length = newCarState.length;
		this.cx = newCarState.cx;
		this.cy = newCarState.cy;
		this.cz = newCarState.cz;
		this.toMiddle = newCarState.toMiddle;		
		//		this.vertex = newCarState.vertex.clone();
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double getRadiusl() {
		return radiusl;
	}
	public void setRadiusl(double radiusl) {
		this.radiusl = radiusl;
	}
	public double getRadiusr() {
		return radiusr;
	}
	public void setRadiusr(double radiusr) {
		this.radiusr = radiusr;
	}
	public double getPosX() {
		return posX;
	}
	public void setPosX(double posX) {
		this.posX = posX;
	}
	public double getPosY() {
		return posY;
	}
	public void setPosY(double posY) {
		this.posY = posY;
	}
	public double getPosZ() {
		return posZ;
	}
	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}
	public double getAx() {
		return ax;
	}
	public void setAx(double ax) {
		this.ax = ax;
	}
	public double getAy() {
		return ay;
	}
	public void setAy(double ay) {
		this.ay = ay;
	}
	public double getAz() {
		return az;
	}
	public void setAz(double az) {
		this.az = az;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
	public double getCx() {
		return cx;
	}
	public void setCx(double cx) {
		this.cx = cx;
	}
	public double getCy() {
		return cy;
	}
	public void setCy(double cy) {
		this.cy = cy;
	}
	public double getCz() {
		return cz;
	}
	public void setCz(double cz) {
		this.cz = cz;
	}
	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */




	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof NewCarState))
			return false;
		final NewCarState other = (NewCarState) obj;
		if (Double.doubleToLongBits(ax) != Double.doubleToLongBits(other.ax))
			return false;
		if (Double.doubleToLongBits(ay) != Double.doubleToLongBits(other.ay))
			return false;
		if (Double.doubleToLongBits(az) != Double.doubleToLongBits(other.az))
			return false;
		if (Double.doubleToLongBits(cx) != Double.doubleToLongBits(other.cx))
			return false;
		if (Double.doubleToLongBits(cy) != Double.doubleToLongBits(other.cy))
			return false;
		if (Double.doubleToLongBits(cz) != Double.doubleToLongBits(other.cz))
			return false;
		if (Double.doubleToLongBits(length) != Double
				.doubleToLongBits(other.length))
			return false;
		if (Double.doubleToLongBits(posX) != Double
				.doubleToLongBits(other.posX))
			return false;
		if (Double.doubleToLongBits(posY) != Double
				.doubleToLongBits(other.posY))
			return false;
		if (Double.doubleToLongBits(posZ) != Double
				.doubleToLongBits(other.posZ))
			return false;
		if (Double.doubleToLongBits(radius) != Double
				.doubleToLongBits(other.radius))
			return false;
		if (Double.doubleToLongBits(radiusl) != Double
				.doubleToLongBits(other.radiusl))
			return false;
		if (Double.doubleToLongBits(radiusr) != Double
				.doubleToLongBits(other.radiusr))
			return false;
		return true;
	}

	public NewCarState(int[] sensors,int len) {
		long ti = System.currentTimeMillis();
		track = NewCarState.trk;
		opponents = NewCarState.opps;
		wheelSpinVel = NewCarState.wSVel;
		fromString(sensors,len);
		if (CircleDriver2.debug) System.out.println("FromString :  "+(System.currentTimeMillis()-ti));
	}


	public void fromString(int[] chars,int len){
		double[] allX = NewCarState.aX;	
		double[] allY = NewCarState.aY;

		char[] ss = new char[len];
		for (int i = len-1;i>=0;--i)
			ss[i] = (char)(chars[i]);
//		String s = new String(ss);
//		System.out.println(s);
		final double[][] table = NewCarState.table;
		final int[][] tableInt = NewCarState.tableInt;
		final int DELIM = ' ';
		final int TERMINATE =')';

		int c;		
		int num = 0;		

		for (int i = 0;i<len;++i){			
//			System.out.print(ss[i]);
			if (chars[i]=='(')															
				for (++i;(c = chars[i])!=DELIM && c!=TERMINATE;++i);												
			i++;			
			if (i==len) break;
			switch (num){
			case 6:
			case 9:
			case 40: //change from 37 to 39 matches allTYpes
				int[] tmp = tableInt[num++];					
				i = toIntArray(chars, i, DELIM, TERMINATE, tmp);					
				break;
			default:
				double[] tmp0 = table[num++];			
				i = toDoubleArray(chars, i, DELIM, TERMINATE, tmp0);				
				break;
			}
		}
		int sz = NewCarState.sz;	
		this.angle = ang[0];
		this.curLapTime = curLapTi[0];
		this.damage = dam[0];
		this.distFromStart = distFrStart[0];
		this.distRaced = distR[0];
		this.fuel = fl[0];
		this.gear = gr[0];
		this.lastLapTime = lastLapTi[0];		
		this.racePos = raceP[0];
		this.rpm = rpmin[0];
		this.speedX = spX[0];
		this.speedY = spY[0];
		this.trackPos = trackP[0];		
		this.radius = rad[0];
		this.radiusl = radl[0];
		this.radiusr = radr[0];
		this.posX = pX[0];
		this.posY = pY[0];
		this.posZ = pZ[0];
		this.ax = a_x[0];				
		this.ay = a_y[0];
		this.az = a_z[0];
		this.length = NewCarState.len[0];
		this.cx = c_x[0];
		this.cy = c_y[0];
		this.cz = c_z[0];
		vertex[0].x = vx0[0];
		vertex[0].y =  vy0[0];
		vertex[1].x = vx1[0];
		vertex[1].y =  vy1[0];
		vertex[2].x = vx2[0];
		vertex[2].y =  vy2[0];
		vertex[3].x = vx3[0];
		vertex[3].y =  vy3[0];				

		for (int i=sz-1;i>=0;--i){
			center[i].x = allX[i];
			center[i].y = allY[i];
		}
		//		toMiddle =  ((double[]) (table.get("toMiddle")))[0];				
	}


	/*private static final int toInt(char[] s,int from,int to)
	{
		int result = 0;
		boolean negative = false;
		int i = from; 
		int digit;

		if (to > from) {
			switch ( s[from] ){
			case '-':
				negative = true;
				//FALLTHROUGH
			case '+':
				++i;			
			}
			if (i < to) {
				digit = Character.digit(s[i++],10);
				result = -digit;
			}
			while (i < to) {
				// Accumulating negatively avoids surprises near MAX_VALUE
				digit = Character.digit(s[i++],10);
				result *= 10;	
				result -= digit;
			}
		} 
		return (!negative) ? -result : result;			 	
	}//*/

	private static final int toIntArray(final int[] s,int from,final int DELIMITER,final int TERMINATE,int[] tmp)
	{
		int num = 0;
		int result = 0;
		boolean negative = false;
		int i = from; 
		int digit;
		int to = s.length;
		int c;
		c = s[from];
		if ( c == TERMINATE) throw new NumberFormatException("empty String");

		while (c!=TERMINATE && i<to){
			//			while (c==DELIMITER && i<to) c= s[++i];			
			negative = false;			
			result = 0;
			if (to > i) {
				switch ( c ){
				case '-':
					negative = true;
					//FALLTHROUGH
				case '+':
					c = s[++i];			
				}
				//				if (c==TERMINATE) break;
				//				if (c==DELIMITER) continue;
				if (i < to) {					
					digit = c-'0';
					result = -digit;
				}
				while (i < to && (c = s[++i])!=DELIMITER && c!=TERMINATE) {
					// Accumulating negatively avoids surprises near MAX_VALUE
					digit = c - '0';
					result *= 10;	
					result -= digit;
				}
			}
			tmp[num++] = (!negative) ? - result : result;
		}

		NewCarState.sz = num;
		return i;
	}



	private static final FDBigInteger
	big5pow( int p ){
		assert p >= 0 : p; // negative power of 5
		if ( b5p == null ){
			b5p = new FDBigInteger[ p+1 ];
		}else if (b5p.length <= p ){
			FDBigInteger t[] = new FDBigInteger[ p+1 ];
			System.arraycopy( b5p, 0, t, 0, b5p.length );
			b5p = t;
		}
		if ( b5p[p] != null )
			return b5p[p];
		else if ( p < small5pow.length )
			return b5p[p] = new FDBigInteger( small5pow[p] );
		else if ( p < long5pow.length )
			return b5p[p] = new FDBigInteger( long5pow[p] );
		else {
			// construct the value.
			// recursively.
			int q, r;
			// in order to compute 5^p,
			// compute its square root, 5^(p/2) and square.
			// or, let q = p / 2, r = p -q, then
			// 5^p = 5^(q+r) = 5^q * 5^r
			q = p >> 1;
		r = p - q;
		FDBigInteger bigq =  b5p[q];
		if ( bigq == null )
			bigq = big5pow ( q );
		if ( r < small5pow.length ){
			return (b5p[p] = bigq.mult( small5pow[r] ) );
		}else{
			FDBigInteger bigr = b5p[ r ];
			if ( bigr == null )
				bigr = big5pow( r );
			return (b5p[p] = bigq.mult( bigr ) );
		}
		}
	}


	private static final FDBigInteger
	multPow52( FDBigInteger v, int p5, int p2 ){
		if ( p5 != 0 ){
			if ( p5 < small5pow.length ){
				v = v.mult( small5pow[p5] );
			} else {
				v = v.mult( big5pow( p5 ) );
			}
		}
		if ( p2 != 0 ){
			v.lshiftMe( p2 );
		}
		return v;
	}


	private static final int
	countBits( long v ){           //
		// the strategy is to shift until we get a non-zero sign bit
		// then shift until we have no bits left, counting the difference.
		// we do byte shifting as a hack. Hope it helps.
		//
		if ( v == 0L ) return 0;

		while ( ( v & highbyte ) == 0L ){
			v <<= 8;
		}
		while ( v > 0L ) { // i.e. while ((v&highbit) == 0L )
			v <<= 1;
		}

		int n = 0;
		while (( v & lowbytes ) != 0L ){
			v <<= 8;
			n += 8;
		}
		while ( v != 0L ){
			v <<= 1;
			n += 1;
		}
		return n;
	}

	private static final FDBigInteger
	constructPow52( int p5, int p2 ){
		FDBigInteger v = new FDBigInteger( big5pow( p5 ) );
		if ( p2 != 0 ){
			v.lshiftMe( p2 );
		}
		return v;
	}

	/*
	 * Compute a number that is the ULP of the given value,
	 * for purposes of addition/subtraction. Generally easy.
	 * More difficult if subtracting and the argument
	 * is a normalized a power of 2, as the ULP changes at these points.
	 */
	private static final double
	ulp( double dval, boolean subtracting ){
		long lbits = Double.doubleToLongBits( dval ) & ~signMask;
		int binexp = (int)(lbits >>> expShift);
		double ulpval;
		if ( subtracting && ( binexp >= expShift ) && ((lbits&fractMask) == 0L) ){
			// for subtraction from normalized, powers of 2,
			// use next-smaller exponent
			binexp -= 1;
		}
		if ( binexp > expShift ){
			ulpval = Double.longBitsToDouble( ((long)(binexp-expShift))<<expShift );
		} else if ( binexp == 0 ){
			ulpval = Double.MIN_VALUE;
		} else {
			ulpval = Double.longBitsToDouble( 1L<<(binexp-1) );
		}
		if ( subtracting ) ulpval = - ulpval;

		return ulpval;
	}



	private static final int toDoubleArray( int[] in,int from,final int DELIMITER,final int TERMINATE,double[] tmp){
		boolean isNegative = false;		
		int decExponent = 0;
		int c;
		boolean	mustSetRoundDir = false;
		int num = 0;
		boolean decSeen = false;

		//		parseNumber:			
		// throws NullPointerException if null
		c = in[from];
		if ( c == TERMINATE) throw new NumberFormatException("empty String");

		int i = from;				
		while (c!=TERMINATE){
			while (c==DELIMITER) c= in[++i];
			if (c==TERMINATE) break;
			decExponent = 0;
			isNegative = false;
			decSeen = false;
			switch ( c ){
			case '-':
				isNegative = true;
				//FALLTHROUGH
			case '+':
				c = in[++i];			
			}

			/*****Ignore NAN for performance
			// Check for NaN and Infinity strings			
			if(c == 'N' || c == 'I') { // possible NaN or infinity
				boolean potentialNaN = false;
				char targetChars[] = null; // char arrary of "NaN" or "Infinity"

				if(c == 'N') {
					targetChars = notANumber;
					potentialNaN = true;
				} else {
					targetChars = infinity;
				}

				// compare Input string to "NaN" or "Infinity"
				int j = 1;
				while(c!=TERMINATE && j < targetChars.length) {
					if((c = in[++i]) == targetChars[j]) {
						j++;
					} else break;
					//						else // something is amiss, throw exception
					//							break parseNumber;
				}

				// For the candidate string to be a NaN or infinity,
				// all characters in input string and target char[]
				// must be matched ==> j must equal targetChars.length
				// and i must equal l
				if( (j == targetChars.length) ) { // return NaN or infinity
					tmp[num++] = (potentialNaN ? Double.NaN // NaN has no sign
							: isNegative?
									Double.NEGATIVE_INFINITY:
										Double.POSITIVE_INFINITY) ;
					while (c!=TERMINATE && c!=DELIMITER) c=in[++i];
					if (c==TERMINATE) break;
					c = in[++i];
					continue;
				} else { // something went wrong, throw exception
					while (c!=TERMINATE && c!=DELIMITER) c=in[++i];
					if (c==TERMINATE) break;					
					c = in[++i];
					continue;
				}

			} // else carry on with original code as before
			 ********/

			int startIndex = i;			
			int nDigits= 0;			
			int decPt = 0;
			int nLeadZero = 0;
			int nTrailZero= 0;

			int index = intDecimalDigits-1;
			int iValue = 0;
			int digit;
			long lValue;
			double dValue;			
			digitLoop:
				while ( (index>=0 && c!=TERMINATE)){
					switch ( c ){
					case '0':
						if ( nDigits > 0 ){
							nTrailZero ++;
						} else {
							nLeadZero ++;						
						}
						break; // out of switch.
					case '1':
					case '2':
					case '3':
					case '4':
					case '5':
					case '6':
					case '7':
					case '8':
					case '9':
						digit = c-(int)'0';
						iValue = (nTrailZero==0) ? iValue*10 : iValue*int10pow[nTrailZero+1];
						iValue+=digit;
						nDigits+=nTrailZero;
						nTrailZero=0;					
						nDigits++;
						--index;
						//					digits[nDigits++] = c;
						break; // out of switch.
					case '.':
						if ( decSeen ){
							// already saw one ., this is the 2nd.
							throw new NumberFormatException("multiple points");
						}
						decPt = i-startIndex;					
						decSeen = true;
						break; // out of switch.
					default:
						break digitLoop;
					}
					c = in[ ++i ];
				}

			lValue = (long)iValue;
			int kDigits = (index>=0 || c==TERMINATE) ? nDigits : maxDecimalDigits+1;
			if (index<0 && c!=TERMINATE){							
				index = kDigits - nDigits;			
				if (index-->0){																		
					digitLoop:
						while (index>=0 && c!=TERMINATE){
							switch ( c  ){
							case '0':
								if ( nDigits > 0 ){
									nTrailZero ++;
								} else {
									nLeadZero ++;						
								}
								break; // out of switch.
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								digit = c-(int)'0';
								lValue = (nTrailZero==0) ? lValue*10 : lValue*long10pow[nTrailZero+1];
								lValue+=digit;
								nDigits+=nTrailZero;
								nTrailZero=0;							
								nDigits++;
								--index;
								//						digits[nDigits++] = c;
								break; // out of switch.
							case '.':
								if ( decSeen ){
									// already saw one ., this is the 2nd.
									throw new NumberFormatException("multiple points");
								}
								decPt = i-startIndex;					
								decSeen = true;
								break; // out of switch.
							default:
								break digitLoop;
							}
							c = in[ ++i ];
						}					
				dValue = (double)lValue;
				if (index>=0 && c!=TERMINATE) kDigits = nDigits;
				if (index<0 && c!=TERMINATE){
					//We now continue scanning the rest until no digit are found
					digitLoop:
						while (c!=TERMINATE){
							switch ( c  ){
							case '0':
								if ( nDigits > 0 ){
									nTrailZero ++;
								} else {
									nLeadZero ++;						
								}
								break; // out of switch.
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':									
								nDigits+=nTrailZero;
								nTrailZero=0;						
								nDigits++;								
								//						digits[nDigits++] = c;
								break; // out of switch.
							case '.':
								if ( decSeen ){
									// already saw one ., this is the 2nd.
									throw new NumberFormatException("multiple points");
								}
								decPt = i-startIndex;					
								decSeen = true;
								break; // out of switch.
							default:
								break digitLoop;
							}
							c = in[ ++i ];
						}
				}
				} else dValue = (double)iValue;
			} else dValue = (double)iValue;
			/*
			 * At this point, we've scanned all the digits and decimal
			 * point we're going to see. Trim off leading and trailing
			 * zeros, which will just confuse us later, and adjust
			 * our initial decimal exponent accordingly.
			 * To review:
			 * we have seen i total characters.
			 * nLeadZero of them were zeros before any other digits.
			 * nTrailZero of them were zeros after any other digits.
			 * if ( decSeen ), then a . was seen after decPt characters
			 * ( including leading zeros which have been discarded )
			 * nDigits characters were neither lead nor trailing
			 * zeros, nor point
			 */
			/*
			 * special hack: if we saw no non-zero digits, then the
			 * answer is zero!
			 * Unfortunately, we feel honor-bound to keep parsing!
			 */

			if ( decSeen ){
				decExponent = decPt - nLeadZero;
			} else {
				decExponent = nDigits+nTrailZero;
			}
			if ( nDigits == 0 && lValue==0){
				//			digits = zero;
				//			nDigits = 1;
				//					if ( nLeadZero == 0 ){
				//						// we saw NO DIGITS AT ALL,
				//						// not even a crummy 0!
				//						// this is not allowed.
				//						break parseNumber; // go throw exception
				//					}
				tmp[num++] = 0;
				while (c!=TERMINATE && c!=DELIMITER) c=in[++i];
				if (c==TERMINATE) break;					
				c = in[++i];
				continue;
			}

			/* Our initial exponent is decPt, adjusted by the number of
			 * discarded zeros. Or, if there was no decPt,
			 * then its just nDigits adjusted by discarded trailing zeros.
			 */


			/*
			 * Look for 'e' or 'E' and an optionally signed integer.
			 */
			if ( (c!=TERMINATE) && (c =='e') || (c == 'E') ){
				int expSign = 1;
				int expVal = 0;
				int reallyBig = Integer.MAX_VALUE / 10;
				boolean expOverflow = false;
				switch( c = in[++i] ){
				case '-':
					expSign = -1;
					//FALLTHROUGH
				case '+':
					c = in[++i];
				}
				expLoop:
					while ( c!=TERMINATE ){
						if ( expVal >= reallyBig ){
							// the next character will cause integer
							// overflow.
							expOverflow = true;
						}
						switch ( c ){
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							expVal = expVal*10 + ( c - '0' );
							c = in[++i];
							continue;
						default:							
							break expLoop; // stop parsing exponent.
						}						
					}
				int expLimit = bigDecimalExponent+nDigits+nTrailZero;
				if ( expOverflow || ( expVal > expLimit ) ){
					//
					// The intent here is to end up with
					// infinity or zero, as appropriate.
					// The reason for yielding such a small decExponent,
					// rather than something intuitive such as
					// expSign*Integer.MAX_VALUE, is that this value
					// is subject to further manipulation in
					// doubleValue() and floatValue(), and I don't want
					// it to be able to cause overflow there!
					// (The only way we can get into trouble here is for
					// really outrageous nDigits+nTrailZero, such as 2 billion. )
					//
					decExponent = expSign*expLimit;
				} else {
					// this should not overflow, since we tested
					// for expVal > (MAX+N), where N >= abs(decExp)
					decExponent = decExponent + expSign*expVal;
				}

				// if we saw something not a digit ( or end of string )
				// after the [Ee][+-], without seeing any digits at all
				// this is certainly an error. If we saw some digits,
				// but then some trailing garbage, that might be ok.
				// so we just fall through in that case.
				// HUMBUG
				//					if ( i == expAt )
				//						break parseNumber; // certainly bad
			}
			/*
			 * We parsed everything we could.
			 * If there are leftovers, then this is not good input!
			 */
			//				if ( i < to &&
			//						((i != to - 1) ||
			//								(in[i] != 'f' &&
			//										in[i] != 'F' &&
			//										in[i] != 'd' &&
			//										in[i] != 'D'))) {
			//					break parseNumber; // go throw exception
			//				}		


			//convert to Double from here


			//		return new FloatingDecimal( isNegative, decExp, digits, nDigits, false );								


			double rValue;

			// First, check for NaN and Infinity values

			/*
			 * convert the lead kDigits to a long integer.
			 */
			// (special performance hack: start to do it using int)		

			int exp = decExponent-kDigits;
			/*
			 * lValue now contains a long integer with the value of
			 * the first kDigits digits of the number.
			 * dValue contains the (double) of the same.
			 */

			if ( nDigits <= maxDecimalDigits ){
				/*
				 * possibly an easy case.
				 * We know that the digits can be represented
				 * exactly. And if the exponent isn't too outrageous,
				 * the whole thing can be done with one operation,
				 * thus one rounding error.
				 * Note that all our constructors trim all leading and
				 * trailing zeros, so simple values (including zero)
				 * will always end up here
				 */
				if (exp == 0 || dValue == 0.0){
					tmp[num++] = (isNegative)? -dValue : dValue; // small floating integer
					while (c!=TERMINATE && c!=DELIMITER) c=in[++i];
					if (c==TERMINATE) break;					
					c = in[++i];
					continue;
				}else if ( exp >= 0 ){
					if ( exp <= maxSmallTen ){
						/*
						 * Can get the answer with one operation,
						 * thus one roundoff.
						 */
						rValue = dValue * small10pow[exp];
						if ( mustSetRoundDir ){
						}
						tmp[num++] = (isNegative)? -rValue : rValue;
						while (c!=TERMINATE && c!=DELIMITER) c=in[++i];
						if (c==TERMINATE) break;					
						c = in[++i];
						continue;
					}
					int slop = maxDecimalDigits - kDigits;
					if ( exp <= maxSmallTen+slop ){
						/*
						 * We can multiply dValue by 10^(slop)
						 * and it is still "small" and exact.
						 * Then we can multiply by 10^(exp-slop)
						 * with one rounding.
						 */
						dValue *= small10pow[slop];
						rValue = dValue * small10pow[exp-slop];

						if ( mustSetRoundDir ){
						}
						tmp[num++] = (isNegative)? -rValue : rValue;
						while (c!=TERMINATE && c!=DELIMITER) c=in[++i];
						if (c==TERMINATE) break;
						c = in[++i];
						continue;
					}
					/*
					 * Else we have a hard case with a positive exp.
					 */
				} else {
					if ( exp >= -maxSmallTen ){
						/*
						 * Can get the answer in one division.
						 */
						rValue = dValue / small10pow[-exp];
						if ( mustSetRoundDir ){
						}
						tmp[num++] = (isNegative)? -rValue : rValue;
						while (c!=TERMINATE && c!=DELIMITER) c=in[++i];					
						if (c==TERMINATE) break;
						c=in[++i];
						continue;
					}
					/*
					 * Else we have a hard case with a negative exp.
					 */
				}
			}

			/*
			 * Harder cases:
			 * The sum of digits plus exponent is greater than
			 * what we think we can do with one error.
			 *
			 * Start by approximating the right answer by,
			 * naively, scaling by powers of 10.
			 */
			if ( exp > 0 ){
				if ( decExponent > maxDecimalExponent+1 ){
					/*
					 * Lets face it. This is going to be
					 * Infinity. Cut to the chase.
					 */
					tmp[num++] = (isNegative)? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
					while (c!=TERMINATE && c!=DELIMITER) c=in[++i];
					if (c==TERMINATE) break;					
					c = in[++i];
					continue;
				}
				if ( (exp&15) != 0 ){
					dValue *= small10pow[exp&15];
				}
				if ( (exp>>=4) != 0 ){
					int j;
					for( j = 0; exp > 1; j++, exp>>=1 ){
						if ( (exp&1)!=0)
							dValue *= big10pow[j];
					}
					/*
					 * The reason for the weird exp > 1 condition
					 * in the above loop was so that the last multiply
					 * would get unrolled. We handle it here.
					 * It could overflow.
					 */
					double t = dValue * big10pow[j];
					if ( Double.isInfinite( t ) ){
						/*
						 * It did overflow.
						 * Look more closely at the result.
						 * If the exponent is just one too large,
						 * then use the maximum finite as our estimate
						 * value. Else call the result infinity
						 * and punt it.
						 * ( I presume this could happen because
						 * rounding forces the result here to be
						 * an ULP or two larger than
						 * Double.MAX_VALUE ).
						 */
						t = dValue / 2.0;
						t *= big10pow[j];
						if ( Double.isInfinite( t ) ){
							tmp[num++] = (isNegative)? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
							while (c!=TERMINATE && c!=DELIMITER) c=in[++i];
							if (c==TERMINATE) break;					
							c = in[++i];
							continue;
						}
						t = Double.MAX_VALUE;
					}
					dValue = t;
				}
			} else if ( exp < 0 ){
				exp = -exp;
				if ( decExponent < minDecimalExponent-1 ){
					/*
					 * Lets face it. This is going to be
					 * zero. Cut to the chase.
					 */
					tmp[num++] = (isNegative)? -0.0 : 0.0;
					while (c!=TERMINATE && c!=DELIMITER) c=in[++i];
					if (c==TERMINATE) break;					
					c = in[++i];
					continue;
				}
				if ( (exp&15) != 0 ){
					dValue /= small10pow[exp&15];
				}
				if ( (exp>>=4) != 0 ){
					int j;
					for( j = 0; exp > 1; j++, exp>>=1 ){
						if ( (exp&1)!=0)
							dValue *= tiny10pow[j];
					}
					/*
					 * The reason for the weird exp > 1 condition
					 * in the above loop was so that the last multiply
					 * would get unrolled. We handle it here.
					 * It could underflow.
					 */
					double t = dValue * tiny10pow[j];
					if ( t == 0.0 ){
						/*
						 * It did underflow.
						 * Look more closely at the result.
						 * If the exponent is just one too small,
						 * then use the minimum finite as our estimate
						 * value. Else call the result 0.0
						 * and punt it.
						 * ( I presume this could happen because
						 * rounding forces the result here to be
						 * an ULP or two less than
						 * Double.MIN_VALUE ).
						 */
						t = dValue * 2.0;
						t *= tiny10pow[j];
						if ( t == 0.0 ){
							tmp[num++] = (isNegative)? -0.0 : 0.0;
							while (c!=TERMINATE && c!=DELIMITER) c=in[++i];
							if (c==TERMINATE) break;					
							c = in[++i];
							continue;
						}
						t = Double.MIN_VALUE;
					}
					dValue = t;
				}
			}

			/*
			 * dValue is now approximately the result.
			 * The hard part is adjusting it, by comparison
			 * with FDBigInt arithmetic.
			 * Formulate the EXACT big-number result as
			 * bigD0 * 10^exp
			 */
			FDBigInteger bigD0 = new FDBigInteger( lValue, in, kDigits, nDigits );
			exp = decExponent - nDigits;

			correctionLoop:
				while(true){
					/* AS A SIDE EFFECT, THIS METHOD WILL SET THE INSTANCE VARIABLES
					 * bigIntExp and bigIntNBits
					 */


					long lbits = Double.doubleToLongBits( dValue ) & ~signMask;
					int binexp = (int)(lbits >>> expShift);
					lbits &= fractMask;
					if ( binexp > 0 ){
						lbits |= fractHOB;
					} else {
						assert lbits != 0L : lbits; // doubleToBigInt(0.0)
						binexp +=1;
						while ( (lbits & fractHOB ) == 0L){
							lbits <<= 1;
							binexp -= 1;
						}
					}
					binexp -= expBias;
					int nbits = countBits( lbits );
					/*
					 * We now know where the high-order 1 bit is,
					 * and we know how many there are.
					 */
					int lowOrderZeros = expShift+1-nbits;
					lbits >>>= lowOrderZeros;							
							int bigIntExp = binexp+1-nbits;
							int bigIntNBits = nbits;
							FDBigInteger bigB = new FDBigInteger( lbits );




							/*
							 * Scale bigD, bigB appropriately for
							 * big-integer operations.
							 * Naively, we multipy by powers of ten
							 * and powers of two. What we actually do
							 * is keep track of the powers of 5 and
							 * powers of 2 we would use, then factor out
							 * common divisors before doing the work.
							 */
							int B2, B5; // powers of 2, 5 in bigB
							int D2, D5; // powers of 2, 5 in bigD
							int Ulp2; // powers of 2 in halfUlp.
							if ( exp >= 0 ){
								B2 = B5 = 0;
								D2 = D5 = exp;
							} else {
								B2 = B5 = -exp;
								D2 = D5 = 0;
							}
							if ( bigIntExp >= 0 ){
								B2 += bigIntExp;
							} else {
								D2 -= bigIntExp;
							}
							Ulp2 = B2;
							// shift bigB and bigD left by a number s. t.
							// halfUlp is still an integer.
							int hulpbias;
							if ( bigIntExp+bigIntNBits <= -expBias+1 ){
								// This is going to be a denormalized number
								// (if not actually zero).
								// half an ULP is at 2^-(expBias+expShift+1)
								hulpbias = bigIntExp+ expBias + expShift;
							} else {
								hulpbias = expShift + 2 - bigIntNBits;
							}
							B2 += hulpbias;
							D2 += hulpbias;
							// if there are common factors of 2, we might just as well
							// factor them out, as they add nothing useful.
							int common2 = Math.min( B2, Math.min( D2, Ulp2 ) );
							B2 -= common2;
							D2 -= common2;
							Ulp2 -= common2;
							// do multiplications by powers of 5 and 2
							bigB = multPow52( bigB, B5, B2 );
							FDBigInteger bigD = multPow52( new FDBigInteger( bigD0 ), D5, D2 );
							//
							// to recap:
							// bigB is the scaled-big-int version of our floating-point
							// candidate.
							// bigD is the scaled-big-int version of the exact value
							// as we understand it.
							// halfUlp is 1/2 an ulp of bigB, except for special cases
							// of exact powers of 2
							//
							// the plan is to compare bigB with bigD, and if the difference
							// is less than halfUlp, then we're satisfied. Otherwise,
							// use the ratio of difference to halfUlp to calculate a fudge
							// factor to add to the floating value, then go 'round again.
							//
							FDBigInteger diff;
							int cmpResult;
							boolean overvalue;
							if ( (cmpResult = bigB.cmp( bigD ) ) > 0 ){
								overvalue = true; // our candidate is too big.
								diff = bigB.sub( bigD );
								if ( (bigIntNBits == 1) && (bigIntExp > -expBias) ){
									// candidate is a normalized exact power of 2 and
									// is too big. We will be subtracting.
									// For our purposes, ulp is the ulp of the
									// next smaller range.
									Ulp2 -= 1;
									if ( Ulp2 < 0 ){
										// rats. Cannot de-scale ulp this far.
										// must scale diff in other direction.
										Ulp2 = 0;
										diff.lshiftMe( 1 );
									}
								}
							} else if ( cmpResult < 0 ){
								overvalue = false; // our candidate is too small.
								diff = bigD.sub( bigB );
							} else {
								// the candidate is exactly right!
								// this happens with surprising fequency
								break correctionLoop;
							}
							FDBigInteger halfUlp = constructPow52( B5, Ulp2 );
							if ( (cmpResult = diff.cmp( halfUlp ) ) < 0 ){
								break correctionLoop;
							} else if ( cmpResult == 0 ){
								// difference is exactly half an ULP
								// round to some other value maybe, then finish
								dValue += 0.5*ulp( dValue, overvalue );
								break correctionLoop;
							} else {
								// difference is non-trivial.
								// could scale addend by ratio of difference to
								// halfUlp here, if we bothered to compute that difference.
								// Most of the time ( I hope ) it is about 1 anyway.
								dValue += ulp( dValue, overvalue );
								if ( dValue == 0.0 || dValue == Double.POSITIVE_INFINITY )
									break correctionLoop; // oops. Fell off end of range.
								continue; // try again.
							}

				}
			tmp[num++] = (isNegative)? -dValue : dValue;
			while (c!=TERMINATE && c!=DELIMITER) c=in[++i];
			if (c==TERMINATE) break;					
			c = in[++i];
			continue;
		}




		//			} catch ( StringIndexOutOfBoundsException e ){ }
		//			StringBuilder stringBuilder = new StringBuilder();
		//			stringBuilder.append("For input string: \"");
		//			stringBuilder.append(in);
		//			stringBuilder.append("\"");
		//			throw new NumberFormatException(stringBuilder.toString());		
		return i;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NewCarState [ax=");
		builder.append(ax);
		builder.append(", ay=");
		builder.append(ay);
		builder.append(", az=");
		builder.append(az);
		builder.append(", cx=");
		builder.append(cx);
		builder.append(", cy=");
		builder.append(cy);
		builder.append(", cz=");
		builder.append(cz);
		builder.append(", length=");
		builder.append(length);
		builder.append(", posX=");
		builder.append(posX);
		builder.append(", posY=");
		builder.append(posY);
		builder.append(", posZ=");
		builder.append(posZ);
		builder.append(", radius=");
		builder.append(radius);
		builder.append(", radiusl=");
		builder.append(radiusl);
		builder.append(", radiusr=");
		builder.append(radiusr);
		builder.append(", toMiddle=");
		builder.append(toMiddle);
		builder.append("]");
		return builder.toString();
	}



}


