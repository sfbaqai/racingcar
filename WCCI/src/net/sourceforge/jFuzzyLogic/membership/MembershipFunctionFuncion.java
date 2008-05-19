package net.sourceforge.jFuzzyLogic.membership;


import java.util.Iterator;

import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.membership.functions.MffAbs;
import net.sourceforge.jFuzzyLogic.membership.functions.MffCos;
import net.sourceforge.jFuzzyLogic.membership.functions.MffDivide;
import net.sourceforge.jFuzzyLogic.membership.functions.MffExp;
import net.sourceforge.jFuzzyLogic.membership.functions.MffFunction;
import net.sourceforge.jFuzzyLogic.membership.functions.MffLn;
import net.sourceforge.jFuzzyLogic.membership.functions.MffLog;
import net.sourceforge.jFuzzyLogic.membership.functions.MffModulus;
import net.sourceforge.jFuzzyLogic.membership.functions.MffNop;
import net.sourceforge.jFuzzyLogic.membership.functions.MffPow;
import net.sourceforge.jFuzzyLogic.membership.functions.MffSin;
import net.sourceforge.jFuzzyLogic.membership.functions.MffSubstract;
import net.sourceforge.jFuzzyLogic.membership.functions.MffSum;
import net.sourceforge.jFuzzyLogic.membership.functions.MffTan;
import net.sourceforge.jFuzzyLogic.membership.functions.MffTimes;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;
import it.unimi.dsi.lang.MutableString;
import antlr.collections.AST;

/**
 * Membership function that is a (simple) mathematical funcion (the result is a singleton)
 *
 * @author pcingola@users.sourceforge.net
 */
public class MembershipFunctionFuncion extends MembershipFunctionDiscrete {

	/** Function implemented by this node */
	protected MffFunction function;

	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------

	/**
	 * Constructor for a whole AST (tree)
	 */
	public MembershipFunctionFuncion(FuzzyRuleSet fuzzyRuleSet, AST tree) {
		super();

		// Add paramters (use first parameter as X value and second as Y value (same as MembershipFunctionSingleton)
		parameters = new double[2];

		//---
		// Create function tree
		//---
		Object fun = createFuncionTree(fuzzyRuleSet, tree);

		// First item (tree's root) is a function? => Ok add tree
		if( fun instanceof MffFunction ) {
			function = (MffFunction) fun;
		} else {
			// First item is NOT a function (e.g. a variable or a double)?
			// => Make it a function (usin 'Nop')
			Object args[] = new Object[1];
			args[0] = fun;
			function = new MffNop(fuzzyRuleSet, args);
		}
	}

	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------

	@Override
	public boolean checkParamters(StringBuffer errors) {
		// Gpr.warn("NOT IMPLEMENTED!");
		return true;
	}

	/**
	 * Create a tree o functions (MffFunction)
	 * @param fuzzyRuleSet Fuzzy Set for this function
	 * @param tree : AST (tree) to parse
	 * @return A tree of MffFunctions. Each leave can be either a function, a value (Double), a Variable or a Variable's name.
	 */
	private Object createFuncionTree(FuzzyRuleSet fuzzyRuleSet, AST tree) {
		Gpr.debug(FuzzyRuleSet.debug, "createFunctionTree: " + tree.toStringTree());
		String treeName = tree.getText().toUpperCase();

		// Select appropiate funcion (and create it)
		if( treeName.equals("+") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffSum(fuzzyRuleSet, terms);
		} else if( treeName.equals("-") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffSubstract(fuzzyRuleSet, terms);
		} else if( treeName.equals("*") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffTimes(fuzzyRuleSet, terms);
		} else if( treeName.equals("/") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffDivide(fuzzyRuleSet, terms);
		} else if( treeName.equals("^") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffPow(fuzzyRuleSet, terms);
		} else if( treeName.equals("%") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffModulus(fuzzyRuleSet, terms);
		} else if( treeName.equalsIgnoreCase("exp") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffExp(fuzzyRuleSet, terms);
		} else if( treeName.equalsIgnoreCase("ln") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffLn(fuzzyRuleSet, terms);
		} else if( treeName.equalsIgnoreCase("log") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffLog(fuzzyRuleSet, terms);
		} else if( treeName.equalsIgnoreCase("sin") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffSin(fuzzyRuleSet, terms);
		} else if( treeName.equalsIgnoreCase("cos") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffCos(fuzzyRuleSet, terms);
		} else if( treeName.equalsIgnoreCase("tan") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffTan(fuzzyRuleSet, terms);
		} else if( treeName.equalsIgnoreCase("abs") ) {
			Object terms[] = parseTerms(fuzzyRuleSet, tree);
			return new MffAbs(fuzzyRuleSet, terms);
		} else {
			// Try to parse it as a 'double'
			try {
				double dval = Double.parseDouble(treeName);
				// Ok, it's a double
				return new Double(dval);
			} catch(Throwable e) {
				// Assume it's a variable's name (original name, not upper case)
				return new MutableString(tree.getText());
			}
		}
	}

	@Override
	public void estimateUniverse() {
		double val = 0;
		if( function != null ) val = function.evaluate();
		universeMin = universeMax = val;
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunctionDiscrete#iterator()
	 */
	public Iterator<Double> iterator() {
		return new Iterator<Double>() {

			int i = 0;

			public boolean hasNext() {
				return (i == 0);
			}

			public Double next() {
				return Double.valueOf(parameters[0]);
			}

			public void remove() {}
		};
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunction#membership(double)
	 */
	public double membership(double in) {
		return ( in == parameters[0] ) ? parameters[1]:0;
	}

	/**
	 * @see net.sourceforge.jFuzzyLogic.membership.MembershipFunctionDiscrete#membership(int)
	 */
	public double membership(int index) {
		return ( index == 0 )?parameters[1]:0;

	}

	/**
	 * Parse each term (from tree) creating appropiate functions.
	 *
	 * @param fuzzyRuleSet Fuzzy Set for this function
	 * @param tree : AST (tree) to parse
	 * @return An array of objects (terms[])
	 */
	private Object[] parseTerms(FuzzyRuleSet fuzzyRuleSet, AST tree) {
		AST child = tree.getFirstChild();
		int numberOfChilds = tree.getNumberOfChildren();
		Object terms[] = new Object[numberOfChilds];
		for( int i = 0; i < numberOfChilds; i++ ) {
			terms[i] = createFuncionTree(fuzzyRuleSet, child);
			child = child.getNextSibling();
		}
		return terms;
	}

	/** It's only one singleton */
	@Override
	public int size() {
		return 1;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName() + ": " + function.toString();
	}

	/** FCL representation */
	public String toStringFCL() {
		return "FUNCTION " + function.toString();
	}

	@Override
	public double valueX(int index) {
		if( index == 0 ) {
			// Evaluate function
			Gpr.debug(debug, "Evaluation Begin: " + function);
			double eval = function.evaluate();
			Gpr.debug(debug, "Evaluation End: " + eval);
			// Update 'singleton' value
			parameters[0] = eval;
			parameters[1] = 1.0;
			return eval;
		}
		throw new RuntimeException("Array index out of range: " + index);
	}
}
