package net.sourceforge.jFuzzyLogic.test.performance;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

public class TestExecutioner {

	public static boolean debug = true;

	public TestExecutioner() {}

	public List execute(String fileName, String[] inputVariables, int steps, int stepSize) {

		// Load FIS
		FIS fis = FIS.load(fileName, false);
		if( fis == null ) { // Error while loading?
			System.err.println("Can't load file: '" + fileName + "'");
			return null;
		}
		FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();

		// Time recording
		List timeRecords = new ArrayList();
		long startTime;
		int curStep = 0;

		// Test
		for( int i = 0; i <= steps; i++, curStep += stepSize ) {
			startTime = System.currentTimeMillis();

			for( int j = 0; j <= curStep; j++ ) {
				// Set inputs
				for( int k = 0; k < inputVariables.length; k++ )
					fuzzyRuleSet.setVariable(inputVariables[k], Math.random() * 5);

				// Evaluate fuzzy set
				fuzzyRuleSet.evaluate();
			}
			timeRecords.add(new Double(System.currentTimeMillis() - startTime));
			Gpr.debug(debug, "Evaluate " + fileName + "\ti:" + i + "\tcurStep: " + curStep);
		}
		return timeRecords;
	}
}
