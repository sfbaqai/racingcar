package net.sourceforge.jFuzzyLogic.rule;

import it.unimi.dsi.lang.MutableString;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;


/**
 * A fuzzy logic term for a 'FuzzyRule'
 * @author pcingola@users.sourceforge.net
 */
public class FuzzyRuleTerm {

	/** Is it negated? */
	boolean negated;
	/** FuzzyRuleTerm's name */
	MutableString termName;
	/** Varible */
	Variable variable;

	/**
	 * Constructor
	 * @param variable
	 * @param term
	 * @param negated
	 */
	public FuzzyRuleTerm(Variable variable, MutableString term, boolean negated) {
		this.variable = variable;
		this.termName = term;
		this.negated = negated;
	}

	public final double getMembership() {
		double memb = variable.getMembership(termName);
		return( negated ) ?1.0 - memb:memb;
	}

	public MembershipFunction getMembershipFunction() {
		return variable.getMembershipFunction(termName);
	}

	public MutableString getTermName() {
		return termName;
	}

	public Variable getVariable() {
		return variable;
	}

	public boolean isNegated() {
		return negated;
	}

	public void setNegated(boolean negated) {
		this.negated = negated;
	}

	public void setTermName(MutableString term) {
		this.termName = term;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}

	public String toString() {
		String is = "is";
		if( negated ) is = " is not";
		return variable.getName() + " " + is + " " + termName;
	}

}
