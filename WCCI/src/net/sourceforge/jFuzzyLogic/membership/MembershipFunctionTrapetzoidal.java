package net.sourceforge.jFuzzyLogic.membership;

/**
 * Paralelogram membership function
 * @author pcingola@users.sourceforge.net
 */
public class MembershipFunctionTrapetzoidal extends MembershipFunctionContinuous {

	/**
	 * Constructor
	 * @param min : Begining of trapetzoidal
	 * @param midLow : Lower midium point of trapetzoidal
	 * @param midHigh : Higher midium point of trapetzoidal
	 * @param max : End of trapetzoidal
	 */
	public MembershipFunctionTrapetzoidal(double min, double midLow, double midHigh, double max) {
		super();

		// Initialize
		this.parameters = new double[4];
		this.parameters[0] = min;
		this.parameters[1] = midLow;
		this.parameters[2] = midHigh;
		this.parameters[3] = max;

		// Check parameters
		StringBuffer errors = new StringBuffer();
		if( !checkParamters(errors) ) throw new RuntimeException(errors.toString());
	}

	@Override
	public boolean checkParamters(StringBuffer errors) {
		boolean ok = true;

		if( parameters[0] > parameters[1] ) {
			ok = false;
			if( errors != null ) errors.append("Parameter midLow is out of range (should stisfy: min <= midLow): " + parameters[0] + " > " + parameters[1] + "\n");
		}

		if( parameters[1] > parameters[2] ) {
			ok = false;
			if( errors != null ) errors.append("Parameter midHigh is out of range (should stisfy: midLow <= midHigh): " + parameters[1] + " > " + parameters[2] + "\n");
		}

		if( parameters[2] > parameters[3] ) {
			ok = false;
			if( errors != null ) errors.append("Parameter max is out of range (should stisfy: midHigh <= max): " + parameters[2] + " > " + parameters[3] + "\n");
		}

		return ok;
	}

	@Override
	public void estimateUniverse() {
		// Are universeMin and universeMax already setted? => nothing to do
		if( (!Double.isNaN(universeMin)) && (!Double.isNaN(universeMax)) ) return;
		universeMin = parameters[0];
		universeMax = parameters[3];
		step = (universeMax - universeMin) / (numberOfPoints);
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#membership(double)
	 */
	@Override
	public double membership(double in) {
		double a=parameters[0],b=parameters[1],c=parameters[2],d=parameters[3];
		/*if( (in <= a) || (in >= d) ) return 0;
		if( (in >= b) && (in <= c) ) return 1;
		if( in < b ) return ((in - a) / (b - a));
		return 1 - ((in - c) / (d - c));*/
		
		return ( (in <= a) || (in >= d) ) ? 0 : ( (in >= b) && (in <= c) ) ? 1 :
			( in < b ) ? ((in - a) / (b - a)) : 1 - ((in - c) / (d - c));
	}
	
	@Override
	public final void preCalculate(){		
		if (lookup==null) lookup = new double[numberOfPoints+1];
		double[] lookup=this.lookup;
		double x=universeMin,a=parameters[0],b=parameters[1],c=parameters[2],d=parameters[3];
		double step = this.step;

		for (int i=0,len=lookup.length;i<len;++i,x+=step){
			lookup[i]=( (x <= a) || (x >= d) ) ? 0 : ( (x >= b) && (x <= c) ) ? 1 :
				( x < b ) ? ((x - a) / (b - a)) : 1 - ((x - c) / (d - c));
		}
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + " : " + parameters[0] + " , " + parameters[1] + " , " + parameters[2] + " , " + parameters[3];
	}

	/** FCL representation */
	@Override
	public String toStringFCL() {
		return "trape " + parameters[0] + " " + parameters[1] + " " + parameters[2] + " " + parameters[3];
	}
}
