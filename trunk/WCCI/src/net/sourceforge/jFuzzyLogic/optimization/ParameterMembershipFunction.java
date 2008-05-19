package net.sourceforge.jFuzzyLogic.optimization;

import it.unimi.dsi.lang.MutableString;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * Representation of a paramater (that may be optimized using some optimization algorithm)
 * This partucular parameter is a membership funcion's parameter
 *
 * @author pcingola@users.sourceforge.net
 */
public class ParameterMembershipFunction extends Parameter {

	/** Which membership funcion we refer */
	MembershipFunction membershipFunction;
	/** Which parameter in that membership funcion we refer */
	int parameterIndex;
	/** Variable's whose linguistic term 'membershipFunction' belongs to */
	Variable variable;

	/**
	 * Default constructor
	 * @param name : Parameter's name
	 * @param epsilon : A small number (e.g used to calculate derivates)
	 * @param membershipFunction : Which membership funcion we refer
	 * @param parameterIndex : Which parameter in that membership funcion we refer
	 */
	public ParameterMembershipFunction(MutableString name, double epsilon, Variable variable, MembershipFunction membershipFunction, int parameterIndex) {
		super(name, epsilon);
		this.variable = variable;
		this.membershipFunction = membershipFunction;
		this.parameterIndex = parameterIndex;
	}

	@Override
	public double get() {
		return membershipFunction.getParameter(parameterIndex);
	}

	public MembershipFunction getMembershipFunction() {
		return membershipFunction;
	}

	public int getParameterIndex() {
		return parameterIndex;
	}

	public Variable getVariable() {
		return variable;
	}

	@Override
	public boolean set(double value) {
		// Can't set parameter outside universe
		if( value < variable.getUniverseMin() ) return false;
		if( value > variable.getUniverseMax() ) return false;

		// Store old parameter
		double oldParam = membershipFunction.getParameter(parameterIndex);

		// Change to new one
		membershipFunction.setParameter(parameterIndex, value);

		// Check parameters: still ok?
		if( !membershipFunction.checkParamters(null) ) {
			// Error => reset to old value
			membershipFunction.setParameter(parameterIndex, oldParam);
			return false;
		}
		return true;
	}

	public void setMembershipFunction(MembershipFunction membershipFunction) {
		this.membershipFunction = membershipFunction;
	}

	public void setParameterIndex(int parameterIndex) {
		this.parameterIndex = parameterIndex;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}

}
