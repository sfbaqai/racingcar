package net.sourceforge.jFuzzyLogic.ruleImplication;

/**
 * Rule inference method : Minimum
 * Base abstract class
 * @author pcingola@users.sourceforge.net
 */
public class RuleImplicationMethodMin extends RuleImplicationMethod {

	public RuleImplicationMethodMin() {
		super();
		name = "min";
	}

	@Override
	public double imply(double degreeOfSupport, double membership) {
		return Math.min(degreeOfSupport, membership);
	}

	/** Printable FCL version */
	@Override
	public String toStringFCL() {
		return "ACT : MIN;";
	}
}
