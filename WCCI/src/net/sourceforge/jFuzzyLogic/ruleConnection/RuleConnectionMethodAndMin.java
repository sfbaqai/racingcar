package net.sourceforge.jFuzzyLogic.ruleConnection;

/**
 * Methods used to connect rule's antecedents
 * 
 * Connection type: AND
 * Connection Method: Minimum
 * 
 * @author pcingola@users.sourceforge.net
 */
public class RuleConnectionMethodAndMin extends RuleConnectionMethod {

	public RuleConnectionMethodAndMin() {
		super();
		name = "and";
	}

	@Override
	public double connect(double antecedent1, double antecedent2) {
		return Math.min(antecedent1, antecedent2);
	}
	
	@Override
	public String toStringFCL() {
		return "AND : MIN;";
	}
}
