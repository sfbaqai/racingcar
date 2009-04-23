package net.sourceforge.jFuzzyLogic.ruleAggregation;

/**
 * Rule aggregation mathod: Sum
 * @author pcingola@users.sourceforge.net
 */
public class RuleAggregationMethodSum extends RuleAggregationMethod {

	public RuleAggregationMethodSum() {
		super();
		name = "sum";
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod#aggregate(double, double)
	 */
	@Override
	public double aggregate(double defuzzifierValue, double valueToAggregate) {
		return defuzzifierValue + valueToAggregate;
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod#toStringFCL()
	 */
	@Override
	public String toStringFCL() {
		return "ACCU : SUM;";
	}
}
