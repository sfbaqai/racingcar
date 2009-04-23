package net.sourceforge.jFuzzyLogic.ruleAggregation;

/**
 * Rule aggregation mathod: Sum
 * @author pcingola@users.sourceforge.net
 */
public class RuleAggregationMethodBoundedSum extends RuleAggregationMethod {

	public RuleAggregationMethodBoundedSum() {
		super();
		name = "bsum";
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod#aggregate(double, double)
	 */
	@Override
	public double aggregate(double defuzzifierValue, double valueToAggregate) {
		return Math.min(1.0, defuzzifierValue + valueToAggregate);
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod#toStringFCL()
	 */
	@Override
	public String toStringFCL() {
		return "ACCU : BSUM;";
	}
}
