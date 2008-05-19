/**
 *
 */
package net.sourceforge.jFuzzyLogic.membership;

import java.util.Random;

/**
 * @author sint
 *
 */
public final class UniformPerturbation extends Perturbation {

	final static long seed=2007;
	final static Random random = new Random(seed);
	double universeMin=-1;
	double universeMax=1;
	/**
	 * @param no
	 */
	public UniformPerturbation(int no) {
		super(no);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param no
	 * @param scale
	 */
	public UniformPerturbation(int no, double scale) {
		super(no, scale);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#checkParamters(java.lang.StringBuffer)
	 */
	@Override
	public boolean checkParamters(StringBuffer errors) {
		boolean ok = true;

		if( parameters[0] >parameters[1] ) {
			ok = false;
			if( errors != null ) errors.append("Parameter 'stdev' should be greater than zero : " + parameters[1] + "\n");
		}

		return ok;	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#estimateUniverse()
	 */
	@Override
	public void estimateUniverse() {
		// TODO Auto-generated method stub
		if( (!Double.isNaN(universeMin)) && (!Double.isNaN(universeMax)) ) return;
		universeMin = (parameters[0]>parameters[1])?parameters[1]:parameters[0];
		universeMax = (parameters[0]<parameters[1])?parameters[1]:parameters[0];
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#membership(double)
	 */
	@Override
	public double membership(double in) {
		// TODO Auto-generated method stub
		return scale*(random.nextDouble()*(universeMax-universeMin)+universeMin);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#toStringFCL()
	 */
	@Override
	public String toStringFCL() {
		// TODO Auto-generated method stub
		return new StringBuffer("unif ").append(parameters[0]).append(" ").append(parameters[1]).toString();
	}

}
