package net.sourceforge.jFuzzyLogic.ruleAggregation;


/**
 * Rule aggregation mathod: Sum
 * @author pcingola@users.sourceforge.net
 */
public class RuleAggregationMethoNormedSum extends RuleAggregationMethod {

	public RuleAggregationMethoNormedSum() {
		super();
		name = "nsum";
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod#aggregate(double, double)
	 */
	@Override
	public double aggregate(double defuzzifierValue, double valueToAggregate) {
		return ( defuzzifierValue + valueToAggregate )  / Math.max(1.0, defuzzifierValue + valueToAggregate);
	}
	
	/**
	 * @see net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod#toStringFCL()
	 */
	@Override
	public String toStringFCL() {
		return "ACCU : NSUM;";
	}
}
