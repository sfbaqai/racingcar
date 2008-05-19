package net.sourceforge.jFuzzyLogic.membership;

import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

/**
 * Gaussian membership function
 * @author pcingola@users.sourceforge.net
 */
public class MembershipFunctionGaussian extends MembershipFunctionContinuous {

	/**
	 * Constructor
	 * @param mean : Mean
	 * @param stdev : Standardt deviation
	 */

	double e=0.0d;
	double mean,stdev;


	public MembershipFunctionGaussian(double mean, double stdev) {
		super();

		// Initialize
		this.parameters = new double[2];
		this.parameters[0] = mean;
		this.parameters[1] = stdev;
		this.mean = mean;
		this.stdev = stdev;

		// Check parameters
		StringBuffer errors = new StringBuffer();
		if( !checkParamters(errors) ) throw new RuntimeException(errors.toString());
		lookup = new double[numberOfPoints+1];
		DoubleArrays.fill(lookup,Double.NaN);
		//preCalculate();
	}


	/**
	 * @param time the time to set
	 */
	public final void setTime(double time,boolean isOutput) {
//		if (this.time==time) return;

		this.time = time;
//		System.out.println(mean+"     "+stdev);
		if (time==0.0d) return;

		if (perturbations==null) return;

		double mean = parameters[0];
		double stdev = parameters[1];
		if (perturbations.length>0 && perturbations[0]!=null){
			ObjectArrayList<Perturbation> l = (ObjectArrayList<Perturbation>)(perturbations[0]);
			if (l!=null){
				for (Perturbation p : l){
					mean += p.get(time);
				}
			}
		}

		if (perturbations.length>1 && perturbations[1]!=null){
			ObjectArrayList<Perturbation> l = (ObjectArrayList<Perturbation>)(perturbations[1]);
			if (l!=null ){
				for (Perturbation p : l){
					stdev += p.get(time);
				}
			}
		}

		this.mean=mean;
		this.stdev=stdev;

		double[] lookup=this.lookup;
		if (isOutput) {
			double x=universeMin;
			double step = this.step;

			for (int i=0,len=lookup.length;i<len;++i,x+=step){
				lookup[i]=Math.exp(-(x - mean) * (x - mean) / (2 * stdev * stdev));
			}
		}
		else {
			DoubleArrays.fill(lookup,Double.NaN);
		}
	}


	public void reset(){
		DoubleArrays.fill(lookup,Double.NaN);
	}

	public final void preCalculate(){
		double[] lookup=this.lookup;
		double x=universeMin,mean=this.mean,stdev=this.stdev;
		double step = this.step;

		for (int i=0,len=lookup.length;i<len;++i,x+=step){
			lookup[i]=Math.exp(-(x - mean) * (x - mean) / (2 * stdev * stdev));
		}
	}

	@Override
	public boolean checkParamters(StringBuffer errors) {
		boolean ok = true;

		if( parameters[1] < 0 ) {
			ok = false;
			if( errors != null ) errors.append("Parameter 'stdev' should be greater than zero : " + parameters[1] + "\n");
		}

		return ok;
	}

	@Override
	public final void estimateUniverse() {
		// Are universeMin and universeMax already setted? => nothing to do
		if( (!Double.isNaN(universeMin)) && (!Double.isNaN(universeMax)) ) return;
		universeMin = parameters[0] - 4.0 * parameters[1];
		universeMax = parameters[0] + 4.0 * parameters[1];
		step = (universeMax - universeMin) / ((double) numberOfPoints);
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#membership(double)
	 */
	public final double membership(double in) {
		double mean=this.mean,stdev=this.stdev;
		double index = (in-universeMin)/step;
		int i = (int)(index);
		double value;
		return (i!=index || in>universeMax || in<universeMin)? ((value = Math.exp(-(in - mean) * (in - mean) / (2 * stdev * stdev)))<0)?0:(value>1)?1:value
				:(Double.isNaN(lookup[i]))?lookup[i]=((value = Math.exp(-(in - mean) * (in - mean) / (2 * stdev * stdev)))<0)?0:(value>1)?1:value:lookup[i];//*/
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName() + " : " + this.parameters[0] + " , " + this.parameters[1];
	}

	/** FCL representation */
	public String toStringFCL() {
		return "gauss " + this.parameters[0] + " " + this.parameters[1];
	}
}
