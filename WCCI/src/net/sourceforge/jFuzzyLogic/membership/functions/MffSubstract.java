package net.sourceforge.jFuzzyLogic.membership.functions;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Membership function that is a (simple) mathematical funcion
 * Function: Substract(x1,x2,x3....xn) = x1 - x2 - x3 - .... - xn  
 * 
 * @author pcingola@users.sourceforge.net
 */
public class MffSubstract extends MffFunction {

	/**
	 * Constructor
	 */
	public MffSubstract(FuzzyRuleSet fuzzyRuleSet, Object terms[]) {
		super(fuzzyRuleSet, terms);
	}

	@Override
	protected double evaluateFunction() {
		double sum = Double.NaN;
		for( int i = 0; i < values.length; i++ ) {
			if( !Double.isNaN(values[i]) ) {
				if( Double.isNaN(sum) ) sum = values[i]; // Set first (non-NaN) value 
				else sum -= values[i]; // Substract other values from first one
			}
		}

		// Only one term => it's negative (e.g. "- 5" )
		if( values.length == 1 ) sum *= -1;

		return sum;
	}

	public String toString() {
		if( terms == null ) return "";
		
		// Only one term? => It's negative (e.g. "- 5") 
		if( terms.length == 1 ) return "( - " + terms[0].toString() + " )";

		// Show every term
		String out = "";
		for( int i = 0; i < terms.length; i++ ) {
			out += terms[i].toString();
			if( i < terms.length - 1 ) out += " - ";
		}

		return "( " + out + " )";
	}

}
