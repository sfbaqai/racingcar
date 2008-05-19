package net.sourceforge.jFuzzyLogic.ruleConnection;

/**
 * Methods used to connect rule's antecedents
 * 
 * Connection type: OR
 * Connection Method: Maximum
 * 
 * @author pcingola@users.sourceforge.net
 */
public class RuleConnectionMethodOrMax extends RuleConnectionMethod {

	public RuleConnectionMethodOrMax() {
		super();
		name = "or";
	}

	public double connect(double antecedent1, double antecedent2) {
		return Math.max(antecedent1, antecedent2);
	}
	
	public String toStringFCL() {
		return "OR: MAX;";
	}
}
