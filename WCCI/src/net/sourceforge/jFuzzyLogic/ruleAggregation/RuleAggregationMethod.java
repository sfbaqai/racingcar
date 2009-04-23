package net.sourceforge.jFuzzyLogic.ruleAggregation;

/**
 * Rule aggregation base method
 * @author pcingola@users.sourceforge.net
 */
public abstract class RuleAggregationMethod {

	String name;

	public RuleAggregationMethod() {
		super();
		name = "Undefined name! (Please set it up in constructor)";
	}

	/**
	 * Aggregate a 'valueToAggregate' to a 'defuzzifierValue'  
	 * @param defuzzifierValue : defuzzifier's current value
	 * @param valueToAggregate : value to aggregate
	 * @return new defuzzifier's value
	 */
	public abstract double aggregate(double defuzzifierValue, double valueToAggregate);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}

	/** FCL string to represent this agrregation method */
	public abstract String toStringFCL();

}
