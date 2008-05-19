package net.sourceforge.jFuzzyLogic.membership;

/**
 * Triangular membership function
 * @author pcingola@users.sourceforge.net
 */
public class MembershipFunctionTriangular extends MembershipFunctionContinuous {

	/**
	 * Constructor
	 * @param min : Begining of triangular function
	 * @param mid : Midium of triangular function
	 * @param max : End of triangular function
	 */
	public MembershipFunctionTriangular(double min, double mid, double max) {
		super();

		// Initialize
		this.parameters = new double[3];
		this.parameters[0] = min;
		this.parameters[1] = mid;
		this.parameters[2] = max;

		// Check parameters
		StringBuffer errors = new StringBuffer();
		if( !checkParamters(errors) ) throw new RuntimeException(errors.toString());
	}

	@Override
	public boolean checkParamters(StringBuffer errors) {
		boolean ok = true;

		if( parameters[0] > parameters[1] ) {
			ok = false;
			if( errors != null ) errors.append("Parameter mid is out of range (should stisfy: min <= mid): " + parameters[0] + " > " + parameters[1] + "\n");
		}

		if( parameters[1] > parameters[2] ) {
			ok = false;
			if( errors != null ) errors.append("Parameter max is out of range (should stisfy: mid <= max): " + parameters[1] + " > " + parameters[2] + "\n");
		}

		return ok;
	}

	@Override
	public void estimateUniverse() {
		// Are universeMin and universeMax already setted? => nothing to do
		if( (!Double.isNaN(universeMin)) && (!Double.isNaN(universeMax)) ) return;
		universeMin = this.parameters[0];
		universeMax = this.parameters[2];
		step = (universeMax - universeMin) / ((double) numberOfPoints);
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#membership(double)
	 */
	public double membership(double in) {
		double a = this.parameters[0],b=this.parameters[1],c=this.parameters[2];
		/*if( (in <= a) || (in >= c) ) return 0;
		if( in < b ) return ((in - a) / (b - a));
		return 1 - ((in - b) / (c - b));*/		
		return ( (in <= a) || (in >= c) ) ? 0 : ( in < b ) ? ((in - a) / (b - a))
				: 1 - ((in - b) / (c - b));
	}
	
	public final void preCalculate(){
		if (lookup==null) lookup = new double[numberOfPoints+1];
		double[] lookup=this.lookup;
		double x=universeMin,a=parameters[0],b=parameters[1],c=parameters[2];
		double step = this.step;		

		for (int i=0,len=lookup.length;i<len;++i,x+=step){
			lookup[i]=( (x <= a) || (x >= c) ) ? 0 : ( x < b ) ? ((x - a) / (b - a))
					: 1 - ((x - b) / (c - b));
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName() + " : " + this.parameters[0] + " , " + this.parameters[1] + " , " + this.parameters[2];
	}

	/** FCL representation */
	public String toStringFCL() {
		return "trian " + parameters[0] + " " + parameters[1] + " " + parameters[2];
	}
}
