package net.sourceforge.jFuzzyLogic.ruleConnection;

/**
 * Methods used to connect rule's antecedents
 * 
 * Connection type: OR
 * Connection Method: Probabilistic OR
 * 
 * @author pcingola@users.sourceforge.net
 */
public class RuleConnectionMethodOrProbOr extends RuleConnectionMethod {

	public RuleConnectionMethodOrProbOr() {
		super();
		name = "or";
	}

	@Override
	public double connect(double antecedent1, double antecedent2) {
		return (antecedent1 + antecedent2 - antecedent1 * antecedent2);
	}
	
	@Override
	public String toStringFCL() {
		return "OR: ASUM;";
	}
}
