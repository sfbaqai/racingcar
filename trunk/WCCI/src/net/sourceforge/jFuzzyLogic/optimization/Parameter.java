package net.sourceforge.jFuzzyLogic.optimization;

import it.unimi.dsi.lang.MutableString;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRule;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * Representation of a paramater (that may be optimized using some optimization algorithm)
 *
 * @author pcingola@users.sourceforge.net
 */
public abstract class Parameter {

	/** How we get 'epsilon' (to be used to calculate partial deriavetes) from universe */
	public static double UNIVERSE_TO_EPSILON_RATIO = 1000;

	/** A small number (e.g used to calculate derivates) */
	double epsilon;
	/** Paramter's name */
	MutableString name;

	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	/**
	 * Default constructor
	 * @param name : Parameter's name
	 * @param epsilon : A small number (e.g used to calculate derivates)
	 */
	public Parameter(MutableString name, double epsilon) {
		this.name = name;
		this.epsilon = epsilon;
	}

	//-------------------------------------------------------------------------
	// Static methods
	//-------------------------------------------------------------------------

	/**
	 * Create a list of parameters
	 * @return A list of parameters
	 */
	public static ArrayList<Parameter> parameterList(FuzzyRuleSet fuzzyRuleSet) {
		ArrayList<Parameter> parameterList = new ArrayList();

		//---
		// Add variable's membership functions' parameters
		//---
		// Iterate over each variable
		for( Iterator<Variable> itVar = fuzzyRuleSet.variablesIterator(); itVar.hasNext(); ) {
			Variable variable = itVar.next();
			parameterListAddVariable(parameterList, variable);
		}

		//---
		// Add rule's weights
		//---
		for( Iterator it = fuzzyRuleSet.getRules().iterator(); it.hasNext(); ) {
			FuzzyRule rule = (FuzzyRule) it.next();
			parameterListAddRule(parameterList, rule);
		}
		return parameterList;
	}

	/**
	 * Add rule's weight parameters to a list
	 * @param parameterList : Paramter's list
	 * @param fuzzyRule : FuzzyRule whose parameters will be added to the list
	 */
	public static void parameterListAddRule(ArrayList parameterList, FuzzyRule fuzzyRule) {
		double epsilon = 0.01; // Default 'epsilon' for rules is 1/100
		MutableString paramName = new MutableString("Rule_").append(fuzzyRule.getName());		
		ParameterRuleWeight param = new ParameterRuleWeight(paramName, epsilon, fuzzyRule);
		parameterList.add(param);
	}

	/**
	 * Add variable's membership functions' parameters to a list
	 * @param parameterList : Paramter's list
	 * @param variable : Variable whose parameters will be added to the list
	 */
	public static void parameterListAddVariable(ArrayList parameterList, Variable variable) {
		double epsilon;

		//---
		// Add variable's membership functions' parameters
		//---
		// Iterate over each linguistic term		
		for( Iterator<MutableString> it = variable.getLinguisticTerms().keySet().iterator(); it.hasNext(); ) {
			MutableString termName = it.next();//			
			LinguisticTerm linguisticTerm = variable.getLinguisticTerm(termName);
			// Get membership function
			MembershipFunction membershipFunction = linguisticTerm.getMembershipFunction();

			// Guesstimate epsilon
			membershipFunction.estimateUniverse();
			double delta = membershipFunction.getUniverseMax() - membershipFunction.getUniverseMin();
			if( delta == 0 ) delta = variable.getUniverseMax() - variable.getUniverseMin();
			epsilon = delta / UNIVERSE_TO_EPSILON_RATIO;
			
			// Iterate over each membership funciotn's parameter
			
			for( int i = 0; i < membershipFunction.getParametersLength(); i++ ) {
				MutableString paramName = new MutableString(variable.getName()).append("_").append(linguisticTerm.getTermName()).append("_").append(membershipFunction.getName())
							.append("_").append(i);				
				ParameterMembershipFunction param = new ParameterMembershipFunction(paramName, epsilon, variable, membershipFunction, i);				
				parameterList.add(param);
			}
		}
		
	}

	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------

	/** Get a paramter's value */
	public abstract double get();

	public double getEpsilon() {
		return epsilon;
	}

	public MutableString getName() {
		return name;
	}

	/**
	 * Sets parameter to 'value'
	 * @param value
	 * @return 'true' if setted ok, 'false if can't be setted (e.g. due to consistenci errors, out of range, etc.)
	 */
	public abstract boolean set(double value);

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public void setName(MutableString name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name + ": " + get();
	}

}
