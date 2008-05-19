package net.sourceforge.jFuzzyLogic.membership.functions;

import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Membership function that is a (simple) mathematical funcion
 * Function: Substract(x1,x2,x3....xn) = x1 * x2 * x3 * ... * xn  
 * 
 * @author pcingola@users.sourceforge.net
 */
public class MffTimes extends MffFunction {

	/**
	 * Constructor
	 */
	public MffTimes(FuzzyRuleSet fuzzyRuleSet, Object terms[]) {
		super(fuzzyRuleSet, terms);
	}

	@Override
	protected double evaluateFunction() {
		double calc = 1.0;
		for( int i = 0; i < values.length; i++ )
			if( !Double.isNaN(values[i]) ) calc *= values[i];
		return calc;
	}

	public String toString() {
		if( terms == null ) return "";
		String out = "";
		for( int i = 0 ; i < terms.length ; i++ ) {
			out += terms[i].toString();
			if( i < terms.length -1 ) out += " * ";
 		}
		
		return "( " + out + " )";
	}

}
