package solo;
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
		
//
	/**
	 * @param args
	 */
			
	private LMA cf = null;
	private double[] initialParams = null;
	public CircleFitter(double[] initialGuess,double[] xx,double[] yy){
		int len = xx.length;
		double[][] data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,xx[i],yy[i]};
		}		
		initialParams = initialGuess;
		cf = new LMA(this,initialGuess,data);		
	}
	
	public CircleFitter(double[] initialGuess,Vector2D[] v){
		int len = v.length;
		double[][] data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,v[i].x(),v[i].y()};
		}
		initialParams = initialGuess;
		cf = new LMA(this,initialGuess,data);
	}
	
	public CircleFitter(double[] initialGuess,double[] xx,double[] yy,int fromIndex){
		int len = xx.length;
		double[][] data = new double[len][3];
		for (int i=fromIndex;i<len;++i){
			data[i-fromIndex]=new double[]{0,xx[i],yy[i]};
		}		
		initialParams = initialGuess;
		cf = new LMA(this,initialGuess,data);
	}
	
	public CircleFitter(double[] initialGuess,Vector2D[] v,int fromIndex,int endIndex){
		int len = endIndex-fromIndex+1;
		double[][] data = new double[len][3];
		int j = 0;
		for (int i=0;i<len;++i){
			int k = i+fromIndex; 
			if (v[k]!=null) data[j++]=new double[]{0,v[k].x(),v[k].y()};
		}
		initialParams = initialGuess;
		cf = new LMA(this,initialGuess,data);
	}

	
	
	public void setData(Vector2D[] v){
		int len = v.length;
		double[][] data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,v[i].x(),v[i].y()};
		}		
		cf = new LMA(this,initialParams,data);
	}
		
	
	public void setData(double[] xx,double[] yy){
		int len = xx.length;
		double[][] data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,xx[i],yy[i]};
		}		
		cf = new LMA(this,initialParams,data);
	}
	/**
	 * @return the initialParams
	 */
	public double[] getInitialParams() {
		return initialParams;
	}

	/**
	 * @param initialParams the initialParams to set
	 */
	public void setInitialParams(double[] initialParams) {
		this.initialParams = initialParams;
	}

	public double[] fit(){
		cf.fit();		
		return cf.parameters.clone();
	}
	
	public double[] fit(double lambda,double minDeltaChi2,int maxIterations){		
		cf.fit(lambda, minDeltaChi2, maxIterations);
		return cf.parameters.clone();
	}
	
	public double getEstimatedCenterX(){
		return cf.parameters[0];
	}
	
	public double getEstimatedCenterY(){
		return cf.parameters[1];
	}

	public double getEstimatedRadius(){
		return cf.parameters[2];
	}
	
	public Vector2D getEstimatedCenter(){
		return new Vector2D(cf.parameters[0],cf.parameters[1]);
	}
	
	public LMA getLMAObject(){
		return cf;
	}
	
	@Override
	public double getPartialDerivate(double[] x, double[] a, int parameterIndex) {
		// TODO Auto-generated method stub
		if (parameterIndex==2)
			return -1;
		double dx = a[0]-x[0];
		double dy = a[1]-x[1];
		double d = Math.sqrt(dx*dx+dy*dy);
		switch (parameterIndex){
		case 0:return dx/d;
		case 1:return dy/d;		
		}
		return -1;
	}



	@Override
	public double getY(double[] x, double[] a) {
		// TODO Auto-generated method stub
		double dx = x[0]-a[0];
		double dy = x[1]-a[1];
		return Math.sqrt(dx*dx+dy*dy)-a[2];
	}
	
}
