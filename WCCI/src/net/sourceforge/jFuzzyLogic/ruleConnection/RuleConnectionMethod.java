package net.sourceforge.jFuzzyLogic.ruleConnection;

/**
 * Base for methods used to connect rule's antecedents
 * @author pcingola@users.sourceforge.net
 */
public abstract class RuleConnectionMethod {

	String name;

	public RuleConnectionMethod() {
		name = "Undefined name! (Please set it up in constructor)";
	}

	public abstract double connect(double antecedent1, double antecedent2);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract String toStringFCL();
}
