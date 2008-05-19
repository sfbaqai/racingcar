package net.sourceforge.jFuzzyLogic.rule;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.lang.MutableString;

import java.util.Iterator;

import net.sourceforge.jFuzzyLogic.ruleImplication.RuleImplicationMethod;

/**
 * Fuzzy rule
 *
 * Rule:	If (x1 is termX1) AND (x2 is termX2) ....  Then (y1 is termY1) AND (y2 is termY2) [weight: 1.0]
 * Notes:
 * 	- "If" clause is called "antecedent"
 * 	- "then" clause is called "consequent"
 *  - There may be 1 or more antecedents connected using a 'RuleConnectionMethod' (e.g. AND, OR)
 *  - As there are many ways to implement 'AND' and 'OR' connectors, you can customize them
 * @author pcingola@users.sourceforge.net
 */
public class FuzzyRule {

	/** Rule antecedent ('if' part) */
	FuzzyRuleExpression antecedents;
	/** Rule consequent ('then' part) */
	ObjectArrayList<FuzzyRuleTerm> consequents;
	/** Degree of support */
	double degreeOfSupport;
	/** Rule's name */
	String name;
	/** Rule's weight */
	double weight;

	/**
	 * Default constructor
	 * Default weight: 1.0
	 * Default connection method: AND (minimum)
	 */
	public FuzzyRule(String name) {
		this.antecedents = new FuzzyRuleExpression();
		this.consequents = new ObjectArrayList<FuzzyRuleTerm>(7);
		this.weight = 1.0; // Default weight: 1.0
		this.name = name;
	}

	/**
	 * Add a condition "... AND ( variable is termName)" to this rule
	 * @param variable : Variable to evaluate
	 * @param termName : FuzzyRuleTerm for this condition
	 * @return this FuzzyRule
	 */
	public FuzzyRule addAntecedent(Variable variable, MutableString termName, boolean negated) {
		if( variable.getMembershipFunction(termName) == null ) throw new RuntimeException("FuzzyRuleTerm '" + termName + "' does not exists in variable '" + variable.getName() + "'");
		FuzzyRuleTerm fuzzyRuleTerm = new FuzzyRuleTerm(variable, termName, negated);
		antecedents.add(fuzzyRuleTerm);
		return this;
	}

	/**
	 * Add consequent "( variable is termName)" to this rule
	 * @param variable : Variable to evaluate
	 * @param termName : FuzzyRuleTerm for this condition
	 * @return this FuzzyRule
	 */
	public FuzzyRule addConsequent(Variable variable, MutableString termName, boolean negated) {
		if( variable.getMembershipFunction(termName) == null ) throw new RuntimeException("FuzzyRuleTerm '" + termName + "' does not exists in variable '" + variable.getName() + "'");
		consequents.add(new FuzzyRuleTerm(variable, termName, negated));
		return this;
	}

	/**
	 * Evaluate this rule using 'RuleImplicationMethod'
	 * @param ruleImplicationMethod : Rule implication method to use
	 */
	public void evaluate(RuleImplicationMethod ruleImplicationMethod) {
		//---
		// Evaluate antecedents (using 'and')
		//---
		degreeOfSupport = antecedents.evaluate();
		
		// Apply weight
		degreeOfSupport *= weight;

		//---
		// Imply rule consequents: Apply degreeOfSupport to consequent linguisticTerms
		//---
//		for(  int i=0,len=consequents.size();i<len;++i ){
//			FuzzyRuleTerm fuzzyRuleTerm = consequents.get(i);
//			ruleImplicationMethod.imply(fuzzyRuleTerm, degreeOfSupport);
//		}

		for(  FuzzyRuleTerm fuzzyRuleTerm : consequents)
			ruleImplicationMethod.imply(fuzzyRuleTerm, degreeOfSupport);			

	}

	public FuzzyRuleExpression getAntecedents() {
		return antecedents;
	}

	public ObjectArrayList<FuzzyRuleTerm> getConsequents() {
		return consequents;
	}

	public double getDegreeOfSupport() {
		return degreeOfSupport;
	}

	public String getName() {
		return name;
	}

	public double getWeight() {
		return weight;
	}

	public void setAntecedents(FuzzyRuleExpression antecedents) {
		this.antecedents = antecedents;
	}

	public void setConsequents(ObjectArrayList<FuzzyRuleTerm> consequents) {
		this.consequents = consequents;
	}

	public void setDegreeOfSupport(double degreeOfSupport) {
		this.degreeOfSupport = degreeOfSupport;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String toString() {
		String strAnt = "", strCon = "";

		// Show antecedents
		strAnt = antecedents.toString();

		// Show consequents
		Iterator itc = consequents.iterator();
		while(itc.hasNext()) {
			FuzzyRuleTerm term = (FuzzyRuleTerm) itc.next();
			if( strCon.length() > 0 ) strCon += " , ";
			strCon += term.toString();
		}

		return name + "\t(" + degreeOfSupport + ")\tif " + strAnt + " then " + strCon + " [weight: " + weight + "]";
	}

	public String toStringFCL() {
		String strAnt = "", strCon = "";

		// Show antecedents
		strAnt = antecedents.toString();

		// Show consequents
		Iterator itc = consequents.iterator();
		while(itc.hasNext()) {
			FuzzyRuleTerm term = (FuzzyRuleTerm) itc.next();
			if( strCon.length() > 0 ) strCon += " , ";
			strCon += term.toString();
		}

		return "IF " + strAnt + " THEN " + strCon + (weight != 1.0 ? " WITH " + weight : "") + ";";
	}

}
