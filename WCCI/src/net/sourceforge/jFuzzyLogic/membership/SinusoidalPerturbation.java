package net.sourceforge.jFuzzyLogic.membership;

import java.util.Random;

public final class SinusoidalPerturbation extends Perturbation {


	final static long seed=2007;
	final static Random random = new Random(seed);

	public SinusoidalPerturbation(int no) {
		super(no);
		// TODO Auto-generated constructor stub
	}

	public SinusoidalPerturbation(int no, double scale) {
		super(no, scale);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean checkParamters(StringBuffer errors) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void estimateUniverse() {
		// TODO Auto-generated method stub
		if( (!Double.isNaN(universeMin)) && (!Double.isNaN(universeMax)) ) return;
		universeMin = -1;
		universeMax = 1;
	}

	@Override
	public double membership(double in) {
		// TODO Auto-generated method stub
		return Math.sin(parameters[0]*in+random.nextGaussian());
	}

	@Override
	public String toStringFCL() {
		// TODO Auto-generated method stub
		return new StringBuffer("sinu ").append(parameters[0]).toString();
	}

}
