package net.sourceforge.jFuzzyLogic.ruleAggregation;

/**
 * Rule aggregation mathod: Probabilistic or
 * @author pcingola@users.sourceforge.net
 */
public class RuleAggregationMethodProbOr extends RuleAggregationMethod {

	public RuleAggregationMethodProbOr() {
		super();
		name = "probOr";
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod#aggregate(double, double)
	 */
	@Override
	public double aggregate(double defuzzifierValue, double valueToAggregate) {
		return defuzzifierValue + valueToAggregate - defuzzifierValue * valueToAggregate;
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod#toStringFCL()
	 */
	@Override
	public String toStringFCL() {
		return "ACCU : PROBOR;";
	}

}
