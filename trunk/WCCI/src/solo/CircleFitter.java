package solo;
import com.graphbuilder.geom.Geom;

import jaolho.data.lma.LMA;
import jaolho.data.lma.LMAMultiDimFunction;


/**
 * 
 */

/**
 * @author kokichi3000
 *
 */
public final class CircleFitter extends LMAMultiDimFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4026823680780498218L;
	public static boolean changed = true;
	private static double avrgR = 0;
	private static double dJdx = 0;
	private static double dJdy = 0;
		
//
	/**
	 * @param args
	 */
			
	private MyLMA cf = null;
	private double[] initialParams = null;
	private double [][] data = null;
	public CircleFitter(double[] initialGuess,double[] xx,double[] yy){
		int len = xx.length;
		data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,xx[i],yy[i]};
		}		
		initialParams = initialGuess;		
		cf = new MyLMA(this,initialGuess,data);		
	}
	
	public CircleFitter(double[] initialGuess,Vector2D[] v){
		int len = v.length;
		data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,v[i].x,v[i].y};
		}
		initialParams = initialGuess;		
		cf = new MyLMA(this,initialGuess,data);
	}
	
	public CircleFitter(double[] initialGuess,double[] xx,double[] yy,int fromIndex){
		int len = xx.length;
		data = new double[len][3];
		for (int i=fromIndex;i<len;++i){
			data[i-fromIndex]=new double[]{0,xx[i],yy[i]};
		}		
		initialParams = initialGuess;		
		cf = new MyLMA(this,initialGuess,data);
	}
	
	public CircleFitter(double[] initialGuess,Vector2D[] v,int fromIndex,int endIndex){
		int len = endIndex-fromIndex+1;
		data = new double[len][3];
		int j = 0;
		for (int i=0;i<len;++i){
			int k = i+fromIndex; 
			if (v[k]!=null) data[j++]=new double[]{0,v[k].x,v[k].y};
		}
		initialParams = initialGuess;		
		cf = new MyLMA(this,initialGuess,data);
	}
	
	public void setData(Vector2D[] v){
		int len = v.length;
		data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,v[i].x,v[i].y};
		}		
		cf = new MyLMA(this,initialParams,data);
	}
		
	
	public final void setData(double[] xx,double[] yy){
		int len = xx.length;
		data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,xx[i],yy[i]};
		}		
		cf = new MyLMA(this,initialParams,data);
	}
	/**
	 * @return the initialParams
	 */
	public final double[] getInitialParams() {
		return initialParams;
	}

	/**
	 * @param initialParams the initialParams to set
	 */
	public final void setInitialParams(double[] initialParams) {
		this.initialParams = initialParams;
	}

	public final double[] fit(){		
		if (data.length==3){
			double[] r = new double[3];
			Geom.getCircle(data[0][1], data[0][2], data[1][1],data[1][2],data[2][1],data[2][2], r);
			cf.parameters[0] = r[0];
			cf.parameters[1] = r[1];
			return cf.parameters.clone();
		}
		cf.maxIterations = 15;
		cf.minDeltaChi2 = 1e-8;
		cf.fit();		
		return cf.parameters.clone();
	}
	
	public final double[] fit(double lambda,double minDeltaChi2,int maxIterations){
		if (data.length==3){			
			double[] r =new double[3];
			Geom.getCircle(data[0][1], data[0][2], data[1][1],data[1][2],data[2][1],data[2][2], r);
			cf.parameters[0] = r[0];
			cf.parameters[1] = r[1];
			return cf.parameters.clone();
		}
		cf.fit(lambda, minDeltaChi2, maxIterations);
		return cf.parameters.clone();
	}
	
	public final double getEstimatedCenterX(){
		return cf.parameters[0];
	}
	
	public final double getEstimatedCenterY(){
		return cf.parameters[1];
	}

	public final double getEstimatedRadius(){
		return getAverageR(initialParams);
	}
	
	public final Vector2D getEstimatedCenter(){
		return new Vector2D(cf.parameters[0],cf.parameters[1]);
	}
	
	public final LMA getLMAObject(){
		return cf;
	}
	
	@Override
	public final double getPartialDerivate(double[] x, double[] a, int parameterIndex) {
		// TODO Auto-generated method stub
		if (parameterIndex==2)
			return -1;		
		double dx = a[0]-x[0];
		double dy = a[1]-x[1];
		double d = Math.sqrt(dx*dx+dy*dy);
		
		if (changed){
			double total = 0;
			double totalX = 0;
			double totalY = 0;
			int n = data.length;		
			
			for (int i=0;i<data.length;++i){
				double xx  = a[0]-data[i][1];
				double y  = a[1]-data[i][2];
				double dd = Math.sqrt(xx*xx+y*y);
				totalX +=  xx/dd;
				totalY +=  y/dd;
				total +=  dd;
			}
			avrgR = total/n;
			dJdx = totalX/n;
			dJdy = totalY/n;
			changed = false;
		}
		
		switch (parameterIndex){
//		case 0:return dx/d;
//		case 1:return dy/d;
		case 0:return dx/d-dJdx;
		case 1:return dy/d-dJdy;		
		}
		return -1;
	}
	
	private final double getAverageR(double[] a){
		if (!changed) return avrgR;
		double total = 0;
		double totalX = 0;
		double totalY = 0;
		int n = data.length;		
		
		for (int i=0;i<data.length;++i){
			double x  = a[0]-data[i][1];
			double y  = a[1]-data[i][2];
			double d = Math.sqrt(x*x+y*y);
			totalX +=  x/d;
			totalY +=  y/d;
			total +=  d;
		}
		avrgR = total/n;
		dJdx = totalX/n;
		dJdy = totalY/n;
		changed = false;
		return avrgR;
	}

	@Override
	public final double getY(double[] x, double[] a) {
		// TODO Auto-generated method stub
		double dx = x[0]-a[0];
		double dy = x[1]-a[1];
		return Math.sqrt(dx*dx+dy*dy)-getAverageR(a);
	}
	
}
