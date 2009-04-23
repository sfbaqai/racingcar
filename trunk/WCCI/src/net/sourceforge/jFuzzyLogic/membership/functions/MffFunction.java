package net.sourceforge.jFuzzyLogic.membership.functions;

import it.unimi.dsi.lang.MutableString;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * Membership function that is a (simple) mathematical funcion
 * (Mff stands for 'Membership Function type Function').
 *
 * Implements a mathematical funcion that depends on variables (should
 * be input variables only, to avoid some sort of 'race conditions').
 *
 * @author pcingola@users.sourceforge.net
 */
public abstract class MffFunction {

	/** Debug mode? */
	public static boolean debug = false;

	//-------------------------------------------------------------------------
	// Variables
	//-------------------------------------------------------------------------

	/** Fuzzy rule set related to this function (need it to look up variables) */
	FuzzyRuleSet fuzzyRuleSet;
	/** Funcion terms (they can be either MembershipFunctionFuncion or Variable) */
	protected Object terms[];
	/** Evaluated values for each term */
	protected double values[];

	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	/**
	 * Default (hidden) constructor
	 */
	public MffFunction(FuzzyRuleSet fuzzyRuleSet, Object terms[]) {
		this.fuzzyRuleSet = fuzzyRuleSet;
		this.terms = terms;
		this.values = new double[terms.length];
	}

	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------

	/**
	 * Evaluate each function's term
	 */
	protected void evaluateTerms() {
		// Evaluate each term
		for( int i = 0; i < terms.length; i++ ) {
			if( terms[i] == null ) {
				// Null function => Nothing to do
				values[i] = Double.NaN;
			} else if( terms[i] instanceof Variable ) {
				// Variable's value
				Variable var = ((Variable) terms[i]);
				if( var.isOutputVarable() ) throw new RuntimeException("Can't use an output variable '" + var.getName() + "' for a function (It may create a race condition)");
				values[i] = var.getValue();
			} else if( terms[i] instanceof MutableString ) {
				// Variable's name
				MutableString name = (MutableString) terms[i];
				// Look it up
				Variable var = fuzzyRuleSet.getVariable(name);
				if( var == null ) throw new RuntimeException("Can't find variable '" + name + "' (Undefined variable or function)");
				if( var.isOutputVarable() ) throw new RuntimeException("Can't use an output variable '" + name + "' for a function (It may create a race condition)");
				// Get value
				values[i] = var.getValue();
			} else if( terms[i] instanceof Double ) {
				// Constant's value
				values[i] = ((Double) terms[i]).doubleValue();
			} else if( terms[i] instanceof MffFunction ) {
				// Function's value
				MffFunction mff = (MffFunction) terms[i]; // Get function
				mff.evaluateTerms(); // Evaluate function's terms (recurse into tree)
				values[i] = mff.evaluateFunction(); // Evaluate function
				Gpr.debug(debug, "Evaluated: " + mff + " = " + values[i]);
			}
			Gpr.debug(debug, "Term[" + i + "]: " + values[i]);
		}
	}

	/**
	 * Evaluate this function
	 * @return double
	 */
	protected abstract double evaluateFunction();

	/**
	 * Get function's result (after evaluation)
	 * @return A double (function's result)
	 */
	public double evaluate() {
		evaluateTerms();
		return evaluateFunction();
	}

	@Override
	public String toString() {
		if( terms == null ) return "";
		String out = this.getClass().getSimpleName() + "(";
		for( int i = 0 ; i < terms.length ; i++ ) {
			out += terms[i].toString();
			if( i < terms.length -1 ) out += ", ";
 		}

		return out + ")";
	}

}
