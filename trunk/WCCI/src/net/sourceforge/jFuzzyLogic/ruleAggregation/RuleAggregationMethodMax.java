package net.sourceforge.jFuzzyLogic.ruleAggregation;

/**
 * Rule aggregation mathod: Max
 * @author pcingola@users.sourceforge.net
 */
public class RuleAggregationMethodMax extends RuleAggregationMethod {

	public RuleAggregationMethodMax() {
		super();
		name = "max";
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod#aggregate(double, double)
	 */
	@Override
	public double aggregate(double defuzzifierValue, double valueToAggregate) {
		return (defuzzifierValue>valueToAggregate)?defuzzifierValue:valueToAggregate;
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod#toStringFCL()
	 */
	@Override
	public String toStringFCL() {
		return "ACCU : MAX;";
	}

}
