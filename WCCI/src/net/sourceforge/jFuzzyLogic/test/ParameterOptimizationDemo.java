package net.sourceforge.jFuzzyLogic.test;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.optimization.OptimizationDeltaJump;
import net.sourceforge.jFuzzyLogic.optimization.Parameter;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRule;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;

/**
 * Fuzzy rule set parameter optimization example
 * 
 * @author pcingola@users.sourceforge.net
 */
public class ParameterOptimizationDemo {

	//-------------------------------------------------------------------------
	// Main
	//-------------------------------------------------------------------------

	public static void main(String[] args) throws Exception {
		System.out.println("ParameterOptimizationDemo: Begin");

		//---
		// Load FIS (Fuzzy Inference System)
		//---
		FIS fis = FIS.load("fcl/qualify.fcl");
		FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();		

		//---
		// Create a list of parameter to optimize
		//---
		ArrayList<Parameter> parameterList = new ArrayList<Parameter>();
		// Add variables. 
		// Note: Fuzzy sets' parameters for these (scoring and credLimMul) variables will be optimized
		
		Parameter.parameterListAddVariable(parameterList, fuzzyRuleSet.getVariable("scoring"));
		//System.out.println(fuzzyRuleSet.getVariable("scoring"));
		Parameter.parameterListAddVariable(parameterList, fuzzyRuleSet.getVariable("credLimMul"));
		
		// Add every rule's weight
		for( Iterator it = fuzzyRuleSet.getRules().iterator(); it.hasNext(); ) {
			FuzzyRule rule = (FuzzyRule) it.next();
			Parameter.parameterListAddRule(parameterList, rule);
		}

		//---
		// Create an error function to be optimzed (i.e. minimized)
		//---
		ErrorFunctionQualify errorFunction = new ErrorFunctionQualify();

		//---
		// Optimize (using 'Delta jump optimization')
		//---
		
		OptimizationDeltaJump optimizationDeltaJump = new OptimizationDeltaJump(fuzzyRuleSet, errorFunction, parameterList);
		optimizationDeltaJump.setMaxIterations(20); // Number optimization of iterations
		optimizationDeltaJump.optimize(true);

		//---
		// Save optimized fuzzyRuleSet to file
		//---
		//System.out.println(fuzzyRuleSet.toStringFCL());
		Gpr.toFile("fcl/qualify_optimized.fcl", fuzzyRuleSet.toStringFCL());

		System.out.println("ParameterOptimizationDemo: End");
	}
}
