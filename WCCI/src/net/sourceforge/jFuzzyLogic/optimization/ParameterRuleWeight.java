package net.sourceforge.jFuzzyLogic.optimization;

import it.unimi.dsi.lang.MutableString;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRule;

/**
 * Representation of a paramater (that may be optimized using some optimization algorithm)
 * This partucular parameter is a rule's weight parameter
 *
 * @author pcingola@users.sourceforge.net
 */
public class ParameterRuleWeight extends Parameter {

	FuzzyRule fuzzyRule;

	/**
	 * Default constructor
	 * @param name : Parameter's name
	 * @param epsilon : A small number (e.g used to calculate derivates)
	 * @param fuzzyRule : Fuzzy rule this parameter refers to.
	 */
	public ParameterRuleWeight(MutableString name, double epsilon, FuzzyRule fuzzyRule) {
		super(name, epsilon);
		this.fuzzyRule = fuzzyRule;
	}

	@Override
	public double get() {
		return fuzzyRule.getWeight();
	}

	public FuzzyRule getFuzzyRule() {
		return fuzzyRule;
	}

	@Override
	public boolean set(double value) {
		boolean ok = true;

		if( value > 1.0 ) { // Out of range?
			value = 1.0;
			ok = false;
		} else if( value < 0 ) { // Out of range?
			value = 0;
			ok = false;
		}

		fuzzyRule.setWeight(value);
		return ok;
	}

	public void setFuzzyRule(FuzzyRule fuzzyRule) {
		this.fuzzyRule = fuzzyRule;
	}

}
