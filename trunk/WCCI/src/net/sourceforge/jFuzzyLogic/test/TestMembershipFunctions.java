package net.sourceforge.jFuzzyLogic.test;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Test: Show several membership functions
 * @author pcingola@users.sourceforge.net
 */
public class TestMembershipFunctions {

	public static void main(String[] args) throws Exception {
		// Load from 'FCL' file?
		FIS fis = FIS.load("fcl/membershipFunctionsDemo.fcl", false);
		if( fis == null ) return;

		// Show ruleset
		FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();
		fuzzyRuleSet.chart();
	}
}
