package net.sourceforge.jFuzzyLogic.membership;

import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Generalized bell membership function
 * @author pcingola@users.sourceforge.net
 */
public class MembershipFunctionGenBell extends MembershipFunctionContinuous {

	/**
	 * Constructor
	 * @param a : 'a' param
	 * @param b : 'b' param
	 * @param mean : Mean
	 */

	double[] lookup = new double[numberOfPoints+1];

	public MembershipFunctionGenBell(double a, double b, double mean) {
		super();

		// Initialize
		this.parameters = new double[3];
		this.parameters[0] = mean;
		this.parameters[1] = a;
		this.parameters[2] = b;

		// Check parameters
		StringBuffer errors = new StringBuffer();
		if( !checkParamters(errors) ) throw new RuntimeException(errors.toString());
		DoubleArrays.fill(lookup, 0, lookup.length,Double.NaN);
	}

	@Override
	public boolean checkParamters(StringBuffer errors) {
		boolean ok = true;

		if( parameters[2] < 0 ) {
			ok = false;
			if( errors != null ) errors.append("Parameter b should be greater than zero: " + parameters[2] + "\n");
		}

		return ok;
	}

	@Override
	public void estimateUniverse() {
		// Are universeMin and universeMax already setted? => nothing to do
		if( (!Double.isNaN(universeMin)) && (!Double.isNaN(universeMax)) ) return;

		// When is membership <= 0.001 ?
		double delta = Math.pow(999, 1 / (2 * parameters[2])) * parameters[1];
		universeMin = parameters[0] - delta;
		universeMax = parameters[0] + delta;
		step = (universeMax - universeMin) / ((double) numberOfPoints);
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#membership(double)
	 */
	public double membership(double in) {
		/*double t = Math.abs((in - parameters[0]) / parameters[1]);
		t = Math.pow(t, 2.0 * parameters[2]);
		return (1.0 / (1.0 + t));//*/
		double index = (in-universeMin)/step;
		int i = (int)(index);

		if (i==index && !Double.isNaN(lookup[i])) return lookup[i];
		double t = Math.abs((in - parameters[0]) / parameters[1]);
		t = Math.pow(t, 2.0 * parameters[2]);
		return (Double.isNaN(lookup[i])) ? lookup[i]=(1.0 / (1.0 + t)) : (1.0 / (1.0 + t));
		//*/
	}

	public final void preCalculate(){
		double[] lookup=this.lookup;
		double x=universeMin,a=this.parameters[0],b=this.parameters[1],c=this.parameters[2];
		double step = this.step;
		double t;


		for (int i=0,len=lookup.length;i<len;++i,x+=step){
			t = Math.abs((x - a) / b);
			t = Math.pow(t, c+c);
			lookup[i]=1.0 / (1.0 + t) ;
		}
	}

	public final void setTime(double time,boolean isOutput) {
		if (this.time==time) return;
		this.time = time;

		if (time==0.0d) return;
		if (perturbations==null) return;

		if (oldParams==null)
			oldParams = DoubleArrays.copy(parameters);

		for (int i=0;i<perturbations.length;++i){
			ObjectArrayList<Perturbation> l = (ObjectArrayList<Perturbation>)(perturbations[i]);
			double mean=oldParams[i];
			if (l!=null)
			for (Perturbation p : l){
				mean += p.get(time);
			}
			parameters[i]=mean;
		}

		double[] lookup=this.lookup;
		if (isOutput) {
			double x=universeMin,a=this.parameters[0],b=this.parameters[1],c=this.parameters[2];
			double step = this.step;
			double t;

			for (int i=0,len=lookup.length;i<len;++i,x+=step){
				t = Math.abs((x - a) / b);
				t = Math.pow(t, c+c);
				lookup[i]=1.0 / (1.0 + t) ;
			}

		} else {
			DoubleArrays.fill(lookup, 0, lookup.length,Double.NaN);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName() + " : " + " , " + parameters[0] + parameters[1] + " , " + parameters[2];
	}

	/** FCL representation */
	public String toStringFCL() {
		return "gbell " + parameters[0] + " " + parameters[1] + " " + parameters[2];
	}
}
