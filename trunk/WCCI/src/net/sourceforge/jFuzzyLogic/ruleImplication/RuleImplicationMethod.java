package net.sourceforge.jFuzzyLogic.ruleImplication;

import net.sourceforge.jFuzzyLogic.defuzzifier.Defuzzifier;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierContinuous;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierDiscrete;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionContinuous;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionDiscrete;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * Rule inference method
 * Base abstract class
 * @author pcingola@users.sourceforge.net
 */
public abstract class RuleImplicationMethod {

	String name;

	public RuleImplicationMethod() {
		name = "Undefined name! (Please set it up in constructor)";
	}

	public String getName() {
		return name;
	}

	/**
	 * Implication sub method used for inference (see imply method)
	 * @param degreeOfSupport : degree of support for this point
	 * @param membership : membership for this point
	 * @return implication
	 */
	public abstract double imply(double degreeOfSupport, double membership);

	/**
	 * Inference method
	 * Add membershipfunction to deffuzifier (using 'min' as inference)
	 */
	public void imply(FuzzyRuleTerm fuzzyRuleTerm, double degreeOfSupport) {
		Variable variable = fuzzyRuleTerm.getVariable();
		Defuzzifier defuzzifier = variable.getDefuzzifier();
		MembershipFunction mf = fuzzyRuleTerm.getMembershipFunction();
		double membership, y, x, aggregated = 0;

		// Both are equal? (both discrete or both continuous?)
		if( mf.isDiscrete() != defuzzifier.isDiscrete() ) throw new RuntimeException("MembershipFunction and Defuzzifier are neither both discrete nor both continuous\n\tTerm: " + fuzzyRuleTerm + "\n\tMembership function: " + mf + "\n\tDefuzzifier: " + defuzzifier + "\n");

		if( mf.isDiscrete() ) {
			//---
			// Discrete case
			//---
			DefuzzifierDiscrete defuzzifierDiscrete = (DefuzzifierDiscrete) defuzzifier;
			MembershipFunctionDiscrete mfd = (MembershipFunctionDiscrete) mf;

			// Add membershipfunction to deffuzifier
			int i, size = mfd.size();
			for( i = 0; i < size; i++ ) {
				// Get 'x' value
				x = mfd.valueX(i);

				// Is term negated?
				if( fuzzyRuleTerm.isNegated() ) membership = 1 - mf.membership(x);
				else membership = mf.membership(x);

				y = imply(degreeOfSupport, membership); // Call to abstract implication method described above

				// Aggregate value
				aggregated = variable.getRuleAggregationMethod().aggregate(defuzzifierDiscrete.getDiscreteValue(x), y);
				defuzzifierDiscrete.setPoint(x, aggregated);
			}
		} else {
			//---
			// Continuous case
			//---
			DefuzzifierContinuous defuzzifierContinuous = (DefuzzifierContinuous) defuzzifier;
//			x = defuzzifierContinuous.getMin();
//			double step = defuzzifierContinuous.getStepSize();
//
//			// Do some sanitychecks
//			if( Double.isNaN(x) || Double.isInfinite(x) ) throw new RuntimeException("Universe minimum not calculated for term '" + fuzzyRuleTerm.getTermName() + "' : " + x);
//			if( Double.isNaN(step) || Double.isInfinite(step) ) throw new RuntimeException("Step not calculated for term '" + fuzzyRuleTerm.getTermName() + "' : " + step);

			if (mf instanceof MembershipFunctionContinuous){
				MembershipFunctionContinuous mfg = (MembershipFunctionContinuous)mf;
				//mfg.preCalculate();
				double[] values = mfg.getLookup();				
				if (values==null) {
					mfg.preCalculate();
					values = mfg.getLookup();
				}
				//defuzzifierContinuous.reset();
				double[] out = defuzzifierContinuous.getValues();

				// Add membershipfunction to deffuzifier
				for(int i = 0,length=out.length; i < length; i++ ) {
					// Is term negated?					
					membership = ( fuzzyRuleTerm.isNegated() ) ? 1 - values[i]: values[i];
					//out[i] = imply(degreeOfSupport, membership); // Call to abstract implication method described above
					// Aggregate value
					//if (Double.isNaN(out[i])) System.out.println(i);
					//System.out.println( membership);
					out[i] = variable.getRuleAggregationMethod().aggregate(out[i], imply(degreeOfSupport, membership));

					//defuzzifierContinuous.setValue(i, aggregated);
					//values[i]=aggregated;
				}
			}
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	/** Printable version */
	public String toString() {
		return this.getClass().getName();
	}

	/** Printable FCL version */
	public abstract String toStringFCL();
}
