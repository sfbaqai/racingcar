package net.sourceforge.jFuzzyLogic.membership;


/**
 * Sigmoidal membership function
 * @author pcingola@users.sourceforge.net
 */
public class MembershipFunctionSigmoidal extends MembershipFunctionContinuous {

	/**
	 * Constructor
	 * @param gain : Mean
	 * @param t0 : Standardt deviation
	 */
	public MembershipFunctionSigmoidal(double gain, double t0) {
		super();

		// Initialize
		this.parameters = new double[2];
		this.parameters[0] = gain;
		this.parameters[1] = t0;

		// Check parameters
		StringBuffer errors = new StringBuffer();
		if( !checkParamters(errors) ) throw new RuntimeException(errors.toString());
	}

	@Override
	public boolean checkParamters(StringBuffer errors) {
		boolean ok = true;
		// No checking needed
		return ok;
	}

	@Override
	public void estimateUniverse() {
		// Are universeMin and universeMax already setted? => nothing to do
		if( (!Double.isNaN(universeMin)) && (!Double.isNaN(universeMax)) ) return;
		universeMin = parameters[1] - 9.0 / Math.abs(parameters[0]);
		universeMax = parameters[1] + 9.0 / Math.abs(parameters[0]);
		step = (universeMax - universeMin) / ((double) numberOfPoints);
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#membership(double)
	 */
	public double membership(double in) {		
		return (1.0 / (1.0 + Math.exp(-this.parameters[0] * (in - this.parameters[1]))));
	}
	
	public final void preCalculate(){
		if (lookup==null) lookup = new double[numberOfPoints+1];
		double[] lookup=this.lookup;		
		double x=universeMin,gain=this.parameters[0],t0=this.parameters[1];
		double step = this.step;

		for (int i=0,len=lookup.length;i<len;++i,x+=step){
			lookup[i]=(1.0 / (1.0 + Math.exp(-gain * (x - t0))));
		}
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName() + " : " + parameters[0] + " , " + parameters[1];
	}

	/** FCL representation */
	public String toStringFCL() {
		return "sigm " + parameters[0] + " " + parameters[1];
	}
}
