package solo;

import java.io.Serializable;

/**
 * @author kokichi3000
 *
 */
public final class CarControl implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7556980105726757107L;	
	private static final int PRECISION_DIGIT = 6;
	private static final int PRECISION = 1000000;

	private static final int int10pow[] = {
		1,10,100,1000,10000,100000,1000000,10000000,100000000,1000000000	            
	};

	private static final long long10pow[] = {
		1,
		10,
		100,
		1000,
		10000,
		100000,
		1000000,
		10000000,
		100000000,
		1000000000,
		10000000000L,
		100000000000L,
		1000000000000L,
		10000000000000L,
		100000000000000L,
		1000000000000000L,
		10000000000000000L,
		100000000000000000L,
		1000000000000000000L		
	};
	
//	private static final byte[] buf = new char[200];
	public final static byte[] output = new byte[200];
	private final static byte[] acc = {'(','a','c','c','e','l',' '};
	private final static byte[] brk = {')','(','b','r','a','k','e',' '};
	private final static byte[] gr = {')','(','g','e','a','r',' '};
	private final static byte[] st = {')','(','s','t','e','e','r',' '};
	private final static byte[] mt = {')','(','m','e','t','a',' '};
	public double accel;
	public double brake;
	public int gear;
	public double steer;
	public int meta;//1 = RESTART, 2= SHUTDOWN

	static{
		System.arraycopy(acc, 0, output, 0, acc.length);
	}

	/**
	 * Copy Constructor
	 *
	 * @param carControl a <code>CarControl</code> object
	 */
	public CarControl(CarControl carControl) 
	{
		this.accel = carControl.accel;
		this.brake = carControl.brake;
		this.gear = carControl.gear;
		this.steer = carControl.steer;
		this.meta = carControl.meta;
	}

	//	public CarControl(String s){	
	//		MessageParser mp = new MessageParser(s);
	//		String str = (String)(mp.getReading("accel"));
	//		accel = (str==null)?0:Double.parseDouble(str);
	//		str = (String)(mp.getReading("brake"));
	//		brake = (str==null)?0:Double.parseDouble(str);
	//		str = (String)(mp.getReading("gear"));
	//		gear = (str==null)?0:Integer.parseInt(str);
	//		str = (String)(mp.getReading("steer"));
	//		steer = (str==null)?0:Double.parseDouble(str);
	//		str = (String)(mp.getReading("meta"));
	//		meta = (str==null)?0:Integer.parseInt(str);		
	//	}

	public CarControl(){
		meta = 1;			
	}

	public CarControl(double accel, double brake, int gear, double steer,
			int meta) {		
		this.accel = accel;
		this.brake = brake;
		this.gear = gear;
		this.steer = steer;
		this.meta = meta;		
	}

	public CarControl(double accel, double brake, int gear, double steer) {		
		this.accel = accel;
		this.brake = brake;
		this.gear = gear;
		this.steer = steer;
		meta = 0;
	}


	//	public void fromString(String s){
	//		MessageParser mp = new MessageParser(s);
	//		String str = (String)(mp.getReading("accel"));
	//		accel = (str==null)?0:Double.parseDouble(str);
	//		str = (String)(mp.getReading("brake"));
	//		brake = (str==null)?0:Double.parseDouble(str);
	//		str = (String)(mp.getReading("gear"));
	//		gear = (str==null)?0:Integer.parseInt(str);
	//		str = (String)(mp.getReading("steer"));
	//		steer = (str==null)?0:Double.parseDouble(str);
	//		str = (String)(mp.getReading("meta"));
	//		meta = (str==null)?0:Integer.parseInt(str);
	//	}

	public double getAccel() {
		return accel;
	}

	public void setAccel(double accel) {
		this.accel = accel;
	}

	public double getBrake() {
		return brake;
	}

	public void setBrake(double brake) {
		this.brake = brake;
	}

	public int getGear() {
		return gear;
	}

	public void setGear(int gear) {
		this.gear = gear;
	}

	public double getSteer() {
		return steer;
	}

	public void setSteer(double steer) {
		this.steer = steer;
	}

	public int getMeta() {
		return meta;
	}

	public void setMeta(int meta) {
		this.meta = meta;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(80);
		builder.append("(accel ");
		builder.append(accel);
		builder.append(")( brake ");
		builder.append(brake);
		builder.append(")( gear ");
		builder.append(gear);
		builder.append(")( steer ");
		builder.append(steer);
		builder.append(")( meta ");
		builder.append(meta);		
		builder.append(")");		
		return builder.toString();
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */

	public final int toBytes() {			
		byte[] output = CarControl.output;		
		int len = acc.length;
		len = double2string(accel, output, len);
		System.arraycopy(brk, 0, output, len, brk.length);
		len+=brk.length;
		len = double2string(this.brake, output, len);
		System.arraycopy(gr, 0, output, len, gr.length);
		len+=gr.length;
		len = int2string(gear, output, len);
		System.arraycopy(st, 0, output, len, st.length);
		len+=st.length;
		len = double2string(this.steer, output, len);
		System.arraycopy(mt, 0, output, len, mt.length);
		len+=mt.length;
		len = int2string(meta, output, len);
		output[len++] =')';
//		System.out.println(new String(buf,0,len));
//		System.out.println("toString() : "+toString());				
		return len;
	}


	
	private static final int int2string(int ivalue,byte[] s,int from){
		if (ivalue==0){
			s[from++]='0';
			return from;
		}
		int ndigits = 0;	               
		if (ivalue<0) {
			s[from++] ='-';
			ivalue = -ivalue;
		}
		while (ivalue>=int10pow[ndigits]) ndigits++;
		int digitno = from+ndigits-1;
		int c =ivalue%10;       
		while ( ivalue != 0){
			s[digitno--] = (byte)(c+'0');              
			ivalue /= 10;
			c = ivalue%10;

		}          
		return from+ndigits;
	}


	
	private static final int double2string(double val,byte[] s,int from){		
		long lval = Math.round(val*PRECISION);
		if (lval==0) {
			s[from++]='0';
			return from;
		}
		if (lval<0){
			s[from++] = '-';
			lval = -lval;
		}
		int ndigits = 0;
		int rt = 0;
		int dotIndex;
		if (lval<=1000000000){
			int ivalue = (int)lval;
			while (ivalue>=int10pow[ndigits]) ndigits++;
			dotIndex = ndigits-PRECISION_DIGIT;
			if (dotIndex<=0) {
				s[from++]='0';
				s[from++]='.';
				while (dotIndex++<0) s[from++]='0';				
				int digitno = from+ndigits-1;
				rt = digitno;
				rt++;
				int c =ivalue%10;       								
				while ( c == 0 && ivalue>0){
					digitno--;
					rt--;
					ivalue /= 10;
					c = ivalue%10;
				}
				while ( ivalue != 0){
					s[digitno--] = (byte)(c+'0');              
					ivalue /= 10;
					c = ivalue%10;
				}   
			} else {								
				int c =ivalue%10;       					
				while ( c == 0 && ndigits>dotIndex){
					ndigits--;				
					ivalue /= 10;
					c = ivalue%10;
					
				}				
				int digitno = from+ndigits;
				rt = digitno+1;
				if (ndigits==dotIndex) {					
					digitno--;
					rt--;
					s[digitno--] = (byte)(c+'0');
					ivalue /= 10;
					c = ivalue%10;
					while ( ivalue != 0){						
						s[digitno--] = (byte)(c+'0');              
						ivalue /= 10;
						c = ivalue%10;
					}   
					return rt;
				}
				while ( ivalue != 0){
					if (ndigits==dotIndex) {
						s[digitno--] = '.';	
						ndigits--;
					}
					ndigits--;
					s[digitno--] = (byte)(c+'0');              
					ivalue /= 10;
					c = ivalue%10;

				}   
			}
		} else {
			while (lval>=long10pow[ndigits]) ndigits++;
			dotIndex = ndigits-PRECISION_DIGIT;
			if (dotIndex<=0) {
				s[from++]='0';
				s[from++]='.';
				while (dotIndex++<0) s[from++]='0';				
				int digitno = from+ndigits-1;
				rt = digitno;
				rt++;
				int c =(int)(lval%10L);       								
				while ( c == 0 && lval>0){
					digitno--;
					rt--;
					lval /= 10;
					c =(int)(lval%10L);
				}
				while ( lval != 0){
					s[digitno--] = (byte)(c+'0');              
					lval /= 10;
					c =(int)(lval%10L);
				}   
			} else {								
				int c =(int)(lval%10L);      					
				while ( c == 0 && ndigits>dotIndex){
					ndigits--;				
					lval /= 10;
					c =(int)(lval%10L);
					
				}				
				int digitno = from+ndigits;
				rt = digitno+1;
				
				if (lval<=Integer.MAX_VALUE){
					int ivalue = (int)lval;
					if (ndigits==dotIndex) {					
						digitno--;
						rt--;
						s[digitno--] = (byte)(c+'0');
						ivalue /= 10;
						c = ivalue%10;
						while ( ivalue != 0){						
							s[digitno--] = (byte)(c+'0');              
							ivalue /= 10;
							c = ivalue%10;
						}   
						return rt;
					}
					while ( ivalue != 0){
						if (ndigits==dotIndex) {
							s[digitno--] = '.';	
							ndigits--;
						}
						ndigits--;
						s[digitno--] = (byte)(c+'0');              
						ivalue /= 10;
						c = ivalue%10;

					}   

				} else {
					if (ndigits==dotIndex) {					
						digitno--;
						rt--;
						s[digitno--] = (byte)(c+'0');
						lval /= 10;
						c =(int)(lval%10L);
						while ( lval != 0){						
							s[digitno--] = (byte)(c+'0');              
							lval /= 10;
							c =(int)(lval%10L);
						}   
						return rt;
					}
					while ( lval != 0){
						if (ndigits==dotIndex) {
							s[digitno--] = '.';	
							ndigits--;
						}
						ndigits--;
						s[digitno--] = (byte)(c+'0');              
						lval /= 10;
						c =(int)(lval%10L);
	
					}

				}
			}

		}
		return rt;
	}



}
