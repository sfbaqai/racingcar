package net.sourceforge.jFuzzyLogic.test;

import it.unimi.dsi.lang.MutableString;

import java.util.Collection;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.membership.GaussianPerturbation;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionContinuous;
import net.sourceforge.jFuzzyLogic.membership.Perturbation;
import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * Test parsing an FCL file
 * @author pcingola@users.sourceforge.net
 */
public class TestTipper {

	public static void main(String[] args) throws Exception {
		// Load from 'FCL' file
		String fileName = "fcl/xor.fcl";
		FIS fis = FIS.load(fileName, true);
		if( fis == null ) { // Error while loading?
			System.err.println("Can't load file: '" + fileName + "'");
			return;
		}

		// Show ruleset
		long start=System.currentTimeMillis();
		FuzzyRuleSet fuzzyRuleSet = fis.getFuzzyRuleSet();


		Variable input1 = fuzzyRuleSet.getVariable(new MutableString("input1"));
		Variable input2 = fuzzyRuleSet.getVariable(new MutableString("input2"));
		Variable output = fuzzyRuleSet.getVariable(new MutableString("output"));
		/*input1.setUniverseMax(1);
		input1.setUniverseMin(0);
		input2.setUniverseMax(1);
		input2.setUniverseMin(0);
		output.setUniverseMax(1);
		output.setUniverseMin(0);//*/
		//fuzzyRuleSet.chart();
		//input2.getMembershipFunction("delicious").chart("OK", true);
		// Set inputs
		//for (int k=0;k<10;++k)
		/*for (double i=input1.getUniverseMin();i<input1.getUniverseMax();++i){
			for (double j=input2.getUniverseMin();j<input2.getUniverseMax();++j){
				fuzzyRuleSet.setVariable("service", i);
				fuzzyRuleSet.setVariable("food", j);
				// Evaluate fuzzy set
				fuzzyRuleSet.evaluate();
				//System.out.println(output.getLatestDefuzzifiedValue());

				// Show output variable's chart
				//fuzzyRuleSet.getVariable("service").chart(true);

			}
		}*/
		int n=10000;
		Perturbation[] p=new Perturbation[6];
		for (int i=0;i<p.length;++i){
			p[i] = new GaussianPerturbation(n);
			p[i].setScale(0.05);
			p[i].prepareToStart();
		}

		Collection<Variable> cv =fuzzyRuleSet.getVariables().values();
		int i=0;
		for (Variable v:cv){
			Collection<LinguisticTerm> lt=v.getLinguisticTerms().values();
			for (LinguisticTerm l : lt){
				MembershipFunctionContinuous mfc = (MembershipFunctionContinuous)l.getMembershipFunction();
				mfc.addPerturbation(1, p[i++]);
			}
		}//*/

		input1.setValue(0.25);
		input2.setValue(0.75);

//		double[] rs = new double[n];
//		double sum=0;
//		boolean showit=false;

//		for (int i =1;i<=n;++i){
//			input1.setTime((double)i);
//			input2.setTime((double)i);
//			output.setTime((double)i);
//			output.chart(showit);
//			fuzzyRuleSet.evaluate();
//			rs[i-1] = output.getLatestDefuzzifiedValue();
//			sum+=rs[i-1];
//		}
//		fuzzyRuleSet.chart();
		for (int k=0;k<1;++k)
			fuzzyRuleSet.evaluate(1, n, 1);
//			fuzzyRuleSet.evaluate();
		System.out.println(output.getLatestDefuzzifiedValue());
		output.chart(true,30);

		//output.chartDefuzzifier(true);
		//System.out.println(fuzzyRuleSet);
		//fuzzyRuleSet.chart();
		System.out.println((System.currentTimeMillis()-start));

		// Print ruleSet
		//System.out.println(fuzzyRuleSet);
	}
}
