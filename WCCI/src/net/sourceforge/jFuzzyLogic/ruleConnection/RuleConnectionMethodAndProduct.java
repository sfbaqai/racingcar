package net.sourceforge.jFuzzyLogic.ruleConnection;

/**
 * Methods used to connect rule's antecedents
 * 
 * Connection type: AND
 * Connection Method: Product
 * 
 * @author pcingola@users.sourceforge.net
 */
public class RuleConnectionMethodAndProduct extends RuleConnectionMethod {

	public RuleConnectionMethodAndProduct() {
		super();
		name = "and";
	}

	@Override
	public double connect(double antecedent1, double antecedent2) {
		return (antecedent1 * antecedent2);
	}
	
	@Override
	public String toStringFCL() {
		return "AND : PROD;";
	}
}
