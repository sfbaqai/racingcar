package net.sourceforge.jFuzzyLogic.rule;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.Iterator;

import net.sourceforge.jFuzzyLogic.ruleConnection.RuleConnectionMethod;
import net.sourceforge.jFuzzyLogic.ruleConnection.RuleConnectionMethodAndMin;


/**
 * General rule expression term
 *
 * E.g.: "(temp IS hot AND pressure IS high) OR pressure IS low"
 *
 * @author pcingola@users.sourceforge.net
 */
public class FuzzyRuleExpression {

	/** Is it negated? */
	boolean negated;
	/** How are term1 and term2 connected? */
	RuleConnectionMethod ruleConnectionMethod;
	/** Term1 can be a either a 'FuzzyRuleTerm' or 'FuzzyRuleExpression' */
	Object term1;
	/** Term2 can be a either a 'FuzzyRuleTerm' or 'FuzzyRuleExpression' */
	Object term2;

	/**
	 * Default Constructor
	 */
	public FuzzyRuleExpression() {
		this.term1=null;
		this.term2=null;
		this.ruleConnectionMethod = new RuleConnectionMethodAndMin(); // Default connection method: AND (minimum)
	}

	/**
	 * Constructor
	 * @param term1 : term 1 (can be a either a 'FuzzyRuleTerm' or 'FuzzyRuleExpression')
	 * @param term2 : term 2 (can be a either a 'FuzzyRuleTerm' or 'FuzzyRuleExpression')
	 * @param ruleConnectionMethod : connection method between terms (which method is used for each 'and', 'or', 'not'...)
	 */
	public FuzzyRuleExpression(Object term1, Object term2, RuleConnectionMethod ruleConnectionMethod) {
		if( (!(term1 instanceof FuzzyRuleExpression) && !(term1 instanceof FuzzyRuleTerm ))
				|| (!(term2 instanceof FuzzyRuleExpression) && !(term2 instanceof FuzzyRuleTerm )))
			throw new RuntimeException("Invalid object for term1. Only 'FuzzyRuleTerm' or 'FuzzyRuleExpression' are accepted.");

		this.term1=term1;
		this.term2=term2;
		this.ruleConnectionMethod = ruleConnectionMethod;
	}

	/**
	 * Add a new term (using default AND method)
	 * @param fuzzyRuleTerm : term to add
	 */
	public void add(FuzzyRuleTerm fuzzyRuleTerm) {
		// Can add it in term1? => add it
		if( term1 == null ) this.term1 =fuzzyRuleTerm;
		// Can add it in term2? => add it
		else if( term2 == null ) this.term2 =fuzzyRuleTerm;
		// Is term1 an expresion? => recurse
		else if( term1 instanceof FuzzyRuleExpression ) ((FuzzyRuleExpression) term1).add(fuzzyRuleTerm);
		// Is term2 an expresion? => recurse
		else if( term2 instanceof FuzzyRuleExpression ) ((FuzzyRuleExpression) term2).add(fuzzyRuleTerm);
		// ...there's nothing else I can do
		else throw new RuntimeException("Can't add term!");
		return;
	}

	/**
	 * Add every variable to this list
	 * @param linkedListVariables
	 */
	private void addVariables(ObjectArrayList linkedListVariables) {
		// Term1: is it a 'term'? => add variables
		if( term1 instanceof FuzzyRuleTerm ) linkedListVariables.add(((FuzzyRuleTerm) term1).getVariable());
		// Term1: is it an 'expression'? => recurse
		else if( term1 instanceof FuzzyRuleExpression ) ((FuzzyRuleExpression) term1).addVariables(linkedListVariables);

		// Term2: is it a 'term'? => add variables
		if( term2 instanceof FuzzyRuleTerm  ) linkedListVariables.add(((FuzzyRuleTerm) term2).getVariable());
		// Term2: is it an 'expression'? => recurse
		else if( term2 instanceof FuzzyRuleExpression  ) ((FuzzyRuleExpression) term2).addVariables(linkedListVariables);
	}

	/**
	 * Evaluate this expression
	 * @return evaluation's result
	 */
	public double evaluate() {
//		 No values? => return NaN
		if( (term1 == null) && (term2 == null) ) return Double.NaN;
		
		// Results for each term
		double resTerm1 = ( term1 instanceof FuzzyRuleExpression ) ? ((FuzzyRuleExpression) term1).evaluate()
				: ( term1 instanceof FuzzyRuleTerm ) ? ((FuzzyRuleTerm) term1).getMembership()
				:  Double.NaN;
				

		double resTerm2 = ( term2 instanceof FuzzyRuleExpression ) ? ((FuzzyRuleExpression) term2).evaluate()
				: ( term2 instanceof FuzzyRuleTerm ) ? ((FuzzyRuleTerm) term2).getMembership()
				:  Double.NaN;
				
		// Evaluate term1: if it's an expression => recurse
//		if( term1 instanceof FuzzyRuleExpression ) resTerm1 = ((FuzzyRuleExpression) term1).evaluate();
//		else if( term1 instanceof FuzzyRuleTerm ) resTerm1 = ((FuzzyRuleTerm) term1).getMembership();
//		else if( term1 == null ) resTerm1 = Double.NaN;

		// Evaluate term2: if it's an expression => recurse
//		if( term2 instanceof FuzzyRuleExpression ) resTerm2 = ((FuzzyRuleExpression) term2).evaluate();
//		else if( term2 instanceof FuzzyRuleTerm ) resTerm2 = ((FuzzyRuleTerm) term2).getMembership();
//		else if( term2 == null ) resTerm2 = Double.NaN;
				
		double result = ( term1 == null ) ? resTerm2 : ( term2 == null ) ? resTerm1
				: ruleConnectionMethod.connect(resTerm1, resTerm2);
		// if we only have 1 term => just return that result
//		if( term1 == null ) result = resTerm2;
//		else if( term2 == null ) result = resTerm1;
//		// Ok, we've got 2 values => connect these 2 values
//		else result = ruleConnectionMethod.connect(resTerm1, resTerm2);

		// Is this clause negated?
		return ( negated ) ?1 - result:result;
	}

	public RuleConnectionMethod getRuleConnectionMethod() {
		return ruleConnectionMethod;
	}

	public Object getTerm1() {
		return term1;
	}

	public Object getTerm2() {
		return term2;
	}

	/**
	 * Is this term an expression ('FuzzyRuleExpression')
	 * @param term : term to analize
	 * @return true if it's an 'FuzzyRuleExpression', false otherwise
	 */
	public boolean isFuzzyRuleExpression(Object term) {
		if( term == null ) return false;
		if( term.getClass().getName().equals("net.sourceforge.jFuzzyLogic.rule.FuzzyRuleExpression") ) return true;
		return false;
	}

	/**
	 * Is this term a FuzzyRuleTerm
	 * @param term : term to analize
	 * @return true if it's an 'FuzzyRuleTerm', false otherwise
	 */
	public boolean isFuzzyRuleTerm(Object term) {
		if( term == null ) return false;
		if( term.getClass().getName().equals("net.sourceforge.jFuzzyLogic.rule.FuzzyRuleTerm") ) return true;
		return false;
	}

	public boolean isNegated() {
		return negated;
	}

	/**
	 * Is this a valid term? (only a few objects are acceped as 'terms')
	 * @param term : Term to evaluate
	 */
	public boolean isValidTerm(Object term) {
		if( term == null ) return true;
		if( isFuzzyRuleExpression(term) ) return true;
		if( isFuzzyRuleTerm(term) ) return true;
		return false;
	}

	/**
	 * Iterate on every variable
	 * @return a 'variables' iterator
	 */
	public Iterator iteratorVariables() {
		ObjectArrayList llvars = new ObjectArrayList<Variable>(7);
		addVariables(llvars);
		return llvars.iterator();
	}

	public void setNegated(boolean negated) {
		this.negated = negated;
	}

	public void setRuleConnectionMethod(RuleConnectionMethod ruleConnectionMethod) {
		this.ruleConnectionMethod = ruleConnectionMethod;
	}

	public void setTerm1(Object term1) {
		if( !isValidTerm(term1) ) throw new RuntimeException("Invalid object for term1. Only 'FuzzyRuleTerm' or 'FuzzyRuleExpression' are accepted. Class: " + term1.getClass().getName());
		this.term1 = term1;
	}

	public void setTerm2(Object term2) {
		if( !isValidTerm(term2) ) throw new RuntimeException("Invalid object for term2. Only 'FuzzyRuleTerm' or 'FuzzyRuleExpression' are accepted. Class: " + term2.getClass().getName());
		this.term2 = term2;
	}

	public String toString() {
		String str = new String();

		String connector = " " + ruleConnectionMethod.getName() + " ";
		if( (term1 == null) || (term2 == null) ) connector = "";

		if( isFuzzyRuleExpression(term1) ) str += "(" + ((FuzzyRuleExpression) term1).toString() + ")";
		else if( isFuzzyRuleTerm(term1) ) str += ((FuzzyRuleTerm) term1).toString();

		str += connector;

		if( isFuzzyRuleExpression(term2) ) str += "(" + ((FuzzyRuleExpression) term2).toString() + ")";
		else if( isFuzzyRuleTerm(term2) ) str += ((FuzzyRuleTerm) term2).toString();

		if( negated ) str = "not " + str;

		return str;
	}
}
