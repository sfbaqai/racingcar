/**
 *
 */
package net.sourceforge.jFuzzyLogic.membership;

import java.util.Random;

/**
 * @author kokichi3000
 *
 */
public class GaussianPerturbation extends Perturbation {

	final static long seed=2007;
	final static Random random = new Random(seed);
	/**
	 * @param index
	 * @param no
	 */
	public GaussianPerturbation(int no) {
		super(no);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#checkParamters(java.lang.StringBuffer)
	 */
	@Override
	public boolean checkParamters(StringBuffer errors) {
		boolean ok = true;

		if( parameters[1] < 0 ) {
			ok = false;
			if( errors != null ) errors.append("Parameter 'stdev' should be greater than zero : " + parameters[1] + "\n");
		}

		return ok;

	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#estimateUniverse()
	 */
	@Override
	public void estimateUniverse() {
		// TODO Auto-generated method stub
//		 Are universeMin and universeMax already setted? => nothing to do
		if( (!Double.isNaN(universeMin)) && (!Double.isNaN(universeMax)) ) return;
		universeMin = parameters[0] - 4.0 * parameters[1];
		universeMax = parameters[0] + 4.0 * parameters[1];
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#membership(double)
	 */
	@Override
	public final double membership(double in) {
		// TODO Auto-generated method stub
		return scale*random.nextGaussian();
	}

	public final double get(double time){
		double ind = (time-startTime)/step -1;
		int i = (int)ind;
		return (i!=ind) ? scale*random.nextGaussian() : lookup[i];
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer(getName()).append(" : ").append(parameters[0]).append(" , ").append(parameters[1]).toString();
	}


	/* (non-Javadoc)
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#toStringFCL()
	 */
	@Override
	public String toStringFCL() {
		// TODO Auto-generated method stub
		return new StringBuffer("gauss ").append(parameters[0]).append(" ").append(parameters[1]).toString();
	}

}
