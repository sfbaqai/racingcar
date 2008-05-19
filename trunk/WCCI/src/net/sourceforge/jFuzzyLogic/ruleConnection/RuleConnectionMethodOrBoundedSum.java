package net.sourceforge.jFuzzyLogic.ruleConnection;

/**
 * Methods used to connect rule's antecedents
 * 
 * Connection type: OR
 * Connection Method: Bounded sum
 * 
 * @author pcingola@users.sourceforge.net
 */
public class RuleConnectionMethodOrBoundedSum extends RuleConnectionMethod {

	public RuleConnectionMethodOrBoundedSum() {
		super();
		name = "or";
	}

	public double connect(double antecedent1, double antecedent2) {
		return Math.min(1, antecedent1 + antecedent2);
	}
	
	public String toStringFCL() {
		return "OR: BSUM;";
	}
}
