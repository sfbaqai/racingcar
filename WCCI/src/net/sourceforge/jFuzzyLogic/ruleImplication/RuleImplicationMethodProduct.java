package net.sourceforge.jFuzzyLogic.ruleImplication;

/**
 * Rule inference method: Product
 * Base abstract class
 * @author pcingola@users.sourceforge.net
 */
public class RuleImplicationMethodProduct extends RuleImplicationMethod {

	public RuleImplicationMethodProduct() {
		super();
		name = "product";
	}

	@Override
	public double imply(double degreeOfSupport, double membership) {
		return degreeOfSupport * membership;
	}

	/** Printable FCL version */
	@Override
	public String toStringFCL() {
		return "ACT : PROD;";
	}
}
