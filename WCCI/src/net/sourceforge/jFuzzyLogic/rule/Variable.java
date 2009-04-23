package net.sourceforge.jFuzzyLogic.rule;

import gnu.trove.THashMap;
import gnu.trove.TObjectProcedure;
import it.unimi.dsi.lang.MutableString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.PlotWindow;
import net.sourceforge.jFuzzyLogic.defuzzifier.Defuzzifier;
import net.sourceforge.jFuzzyLogic.defuzzifier.DefuzzifierContinuous;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionContinuous;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionDiscrete;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionGaussian;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethod;
import net.sourceforge.jFuzzyLogic.ruleAggregation.RuleAggregationMethodSum;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Fuzzy variable
 * @author pcingola@users.sourceforge.net
 */
public class Variable implements Comparable,TObjectProcedure<LinguisticTerm> {

	final static int numberOfPoints = PlotWindow.DEFAULT_CHART_NUMBER_OF_POINTS;
	double step;
	double time=0;
	boolean isEvaluated = false;

	/** Default value, when no change */
	double defaultValue;
	/** Defuzzifier class */
	Defuzzifier defuzzifier;
	/** Latest defuzzified value */
	double latestDefuzzifiedValue;
	/** Terms for this variable */
	THashMap<MutableString, LinguisticTerm> linguisticTerms;
	/** Variable name */
	MutableString name;
	/** Rule aggregation method */
	RuleAggregationMethod ruleAggregationMethod;
	/** Universe max (range max) */
	double universeMax;
	/** Universe min (range min) */
	double universeMin;
	/** Variable's value */
	double value;

	/**
	 * Default constructor
	 * @param name : Variable's name
	 */
	public Variable(MutableString name) {
		if( name == null ) throw new RuntimeException("Variable's name can't be null");
		this.name = name;
		this.linguisticTerms = new THashMap<MutableString, LinguisticTerm>(7,new MutableStringStrategy());
		//this.linguisticTerms = new Object2ObjectArrayMap<String, LinguisticTerm>(7);
		this.defaultValue = Double.NaN;
		this.universeMin = Double.NaN;
		this.universeMax = Double.NaN;
		value = Double.NaN;
		ruleAggregationMethod = new RuleAggregationMethodSum(); // Default aggregation method: Sum
		reset(); // Reset values
	}

	/**
	 * Default constructor
	 * @param name : Variable's name
	 */
	public Variable(MutableString name, double universeMin, double universeMax) {
		if( name == null ) throw new RuntimeException("Variable's name can't be null");
		if( universeMax < universeMin ) throw new RuntimeException("Parameter error in variable \'" + name + "\' universeMax < universeMin");
		this.name = name;
		this.linguisticTerms = new THashMap<MutableString, LinguisticTerm>(7,new MutableStringStrategy());
		this.universeMin = universeMin;
		this.universeMax = universeMax;
		value = Double.NaN;
		ruleAggregationMethod = new RuleAggregationMethodSum(); // Default aggregation method: Sum
		reset(); // Reset values
	}

	/**
	 * Adds a termName to this variable
	 * @param linguisticTerm : Linguistic term to add
	 * @return this variable
	 */
	public final Variable add(LinguisticTerm linguisticTerm) {
		this.linguisticTerms.put(linguisticTerm.termName, linguisticTerm);
		linguisticTerm.membershipFunction.setUniverseMax(universeMax);
		linguisticTerm.membershipFunction.setUniverseMin(universeMin);
		return this;
	}

	/**
	 * Adds a termName to this variable
	 * @param termName : FuzzyRuleTerm name
	 * @param membershipFunction : membershipFunction for this termName
	 * @return this variable
	 */
	public final Variable add(String termName, MembershipFunction membershipFunction) {
		this.add(new LinguisticTerm(new MutableString(termName), membershipFunction));
		membershipFunction.setUniverseMin(universeMin);
		membershipFunction.setUniverseMax(universeMax);
		return this;
	}

	/**
	 * Adds a termName to this variable
	 * @param termName : FuzzyRuleTerm name
	 * @param membershipFunction : membershipFunction for this termName
	 * @return this variable
	 */
	public final Variable add(MutableString termName, MembershipFunction membershipFunction) {
		this.add(new LinguisticTerm(termName, membershipFunction));
		membershipFunction.setUniverseMin(universeMin);
		membershipFunction.setUniverseMax(universeMax);
		return this;
	}


	/**
	 * Create a chart showing each linguistic term
	 * @param showIt : If true, plot is displayed
	 */
	public JFreeChart chart(boolean showIt) {
		boolean discrete = true;

		// Sanity check
		if( Double.isNaN(universeMin) || Double.isInfinite(universeMax) ) estimateUniverse();

		// Create a dataset
		XYSeriesCollection xyDataset = new XYSeriesCollection();

		// For each linguistic term...
		int j = 0;
		for( Iterator<MutableString> it = linguisticTerms.keySet().iterator(); it.hasNext(); j++ ) {
			// Add this linguistic term to dataset
			MutableString termName =  it.next();
			MembershipFunction membershipFunction = linguisticTerms.get(termName).membershipFunction;
			discrete &= membershipFunction.isDiscrete();

			// Create a series and add points
			XYSeries series = new XYSeries(termName);
			if( membershipFunction.isDiscrete() ) {
				// Discrete case: Evaluate membership function and add points to dataset
				MembershipFunctionDiscrete membershipFunctionDiscrete = (MembershipFunctionDiscrete) membershipFunction;
				int numberOfPoints = membershipFunctionDiscrete.size();
				for( int i = 0; i < numberOfPoints; i++ )
					series.add(membershipFunctionDiscrete.valueX(i), membershipFunctionDiscrete.membership(i));
			} else {
				// Continuous case: Add every membershipfunction's point

				double xx = universeMin;
				//System.out.println(universeMin+"  v  "+universeMax);
				for( int i = 0; i < numberOfPoints; i++, xx += step )
					series.add(xx, membershipFunction.membership(xx));
			}

			// Add series to dataSet
			xyDataset.addSeries(series);
		}

		// Create chart and show it
		JFreeChart chart;
		if( !discrete ) chart = ChartFactory.createXYLineChart(name.toString(), "x", "Membership", xyDataset, PlotOrientation.VERTICAL, true, true, false);
		else chart = ChartFactory.createScatterPlot(name.toString(), "x", "Membership", xyDataset, PlotOrientation.VERTICAL, true, true, false);
		if( showIt ) PlotWindow.showIt(name.toString(), chart);

		return chart;
	}
	
	public JFreeChart chart(boolean showIt,int n) {
		boolean discrete = true;

		// Sanity check
		if( Double.isNaN(universeMin) || Double.isInfinite(universeMax) ) estimateUniverse();

		// Create a dataset
		XYSeriesCollection xyDataset = new XYSeriesCollection();

		// For each linguistic term...
		int j = 0;
		for( Iterator<MutableString> it = linguisticTerms.keySet().iterator(); it.hasNext(); j++ ) {
			// Add this linguistic term to dataset
			MutableString termName =  it.next();
			MembershipFunction membershipFunction = linguisticTerms.get(termName).membershipFunction;
			discrete &= membershipFunction.isDiscrete();

			// Create a series and add points
			XYSeries series = new XYSeries(termName);
			if( membershipFunction.isDiscrete() ) {
				// Discrete case: Evaluate membership function and add points to dataset
				MembershipFunctionDiscrete membershipFunctionDiscrete = (MembershipFunctionDiscrete) membershipFunction;
				int numberOfPoints = membershipFunctionDiscrete.size();
				for( int i = 0; i < numberOfPoints; i++ )
					series.add(membershipFunctionDiscrete.valueX(i), membershipFunctionDiscrete.membership(i));
			} else {
				// Continuous case: Add every membershipfunction's point

				double xx = universeMin;
				//System.out.println(universeMin+"  v  "+universeMax);
				MembershipFunctionContinuous mfc = (MembershipFunctionContinuous)membershipFunction;
				for (int k=1;k<=n;++k){
					mfc.setTime(k, this.defuzzifier!=null);
					for( int i = 0; i < numberOfPoints; i++, xx += step )
						series.add(xx, mfc.membership(xx));
				}
			}

			// Add series to dataSet
			xyDataset.addSeries(series);
		}

		// Create chart and show it
		JFreeChart chart;
		if( !discrete ) chart = ChartFactory.createXYLineChart(name.toString(), "x", "Membership", xyDataset, PlotOrientation.VERTICAL, true, true, false);
		else chart = ChartFactory.createScatterPlot(name.toString(), "x", "Membership", xyDataset, PlotOrientation.VERTICAL, true, true, false);
		if( showIt ) PlotWindow.showIt(name.toString(), chart);

		return chart;
	}


	/**
	 * Create a chart showing defuzzifier
	 * @param showIt : If true, plot is displayed
	 */
	public JFreeChart chartDefuzzifier(boolean showIt) {
		return defuzzifier.chart(name + " : " + latestDefuzzifiedValue + "(" + defuzzifier.getName() + ")", showIt);
	}

	public final int compareTo(Object anotherVariable) {
		if( anotherVariable == null ) return 1;
		Variable var = (Variable) anotherVariable;
		return name.compareTo(var.name);
	}

	/** Defuzzify this (output) variable */
	public final double defuzzify() {
		double ldv = defuzzifier.defuzzify();

		// Only assign valid defuzzifier's result
		if( !Double.isNaN(ldv) ) latestDefuzzifiedValue = ldv;
		return latestDefuzzifiedValue;
	}

	class LTProc implements TObjectProcedure<LinguisticTerm>{
		double umin;
		double umax;

		public LTProc(double umin,double umax){
			this.umax=umax;
			this.umin=umin;
		}

		public boolean execute(LinguisticTerm lt) {
			MembershipFunction membershipFunction = lt.membershipFunction;
			membershipFunction.setUniverseMin(umin);
			membershipFunction.setUniverseMax(umax);
			return true;
		}
	};

	class TTT implements TObjectProcedure<LinguisticTerm>{
		double umin = Double.POSITIVE_INFINITY;
		double umax = Double.NEGATIVE_INFINITY;
		public boolean execute(LinguisticTerm arg0) {
			MembershipFunction membershipFunction =arg0.membershipFunction;
			membershipFunction.estimateUniverse();
			double min,max;
			umin = ((min =membershipFunction.getUniverseMin()) >umin)?umin:min;
			umax = ((max =membershipFunction.getUniverseMax()) <umax)?umax:max;
			return true;
		}
	}

	/** Estimate unverse */
	public void estimateUniverse() {

		// Are universeMin and universeMax already setted? => nothing to do
		if( (!Double.isNaN(universeMin)) && (!Double.isNaN(universeMax)) ) return;

		// Calculate max / min on every membership function

		TTT proc = new TTT();
		linguisticTerms.forEachValue(proc);

		double umin = proc.umin;
		double umax = proc.umax;
		// Set parameters (if not setted)
		if( Double.isNaN(universeMin) ) universeMin = umin;
		if( Double.isNaN(universeMax) ) universeMax = umax;
		linguisticTerms.forEachValue(new LTProc(umin,umax));

		step = (umax - umin) / (numberOfPoints);
		if (defuzzifier!=null && !defuzzifier.isDiscrete()){
			DefuzzifierContinuous dfc = (DefuzzifierContinuous)defuzzifier;
			dfc.init(umin, umax);
		}
	}

	public double getDefaultValue() {
		return defaultValue;
	}

	public Defuzzifier getDefuzzifier() {
		return defuzzifier;
	}

	public double getLatestDefuzzifiedValue() {
		return latestDefuzzifiedValue;
	}

	/** Get 'termName' linguistic term */
	public LinguisticTerm getLinguisticTerm(MutableString termName) {
		LinguisticTerm lt = linguisticTerms.get(termName);
		if( lt == null ) throw new RuntimeException("No such linguistic term: '" + termName + "'");
		return lt;
	}

	public THashMap<MutableString, LinguisticTerm> getLinguisticTerms() {
		return linguisticTerms;
	}

	/** Evaluate 'termName' membershipfunction at 'value' */
	public double getMembership(MutableString termName) {		
		MembershipFunction mf = linguisticTerms.get(termName).membershipFunction;
		if( mf == null ) throw new RuntimeException("No such termName: \"" + termName + "\"");
		return mf.membership(this.value);
	}

	/** Get 'termName' membershipfunction */
	public MembershipFunction getMembershipFunction(MutableString termName) {
		LinguisticTerm lt =  linguisticTerms.get(termName);
		if( lt == null ) throw new RuntimeException("No such linguistic term: '" + termName + "'");
		return lt.membershipFunction;
	}

	/** Get 'termName' membershipfunction */
	public MembershipFunction getMembershipFunction(String termName) {
		LinguisticTerm lt =  linguisticTerms.get(new MutableString(termName));
		if( lt == null ) throw new RuntimeException("No such linguistic term: '" + termName + "'");
		return lt.membershipFunction;
	}

	public MutableString getName() {
		return name;
	}

	public RuleAggregationMethod getRuleAggregationMethod() {
		return ruleAggregationMethod;
	}

	public double getUniverseMax() {
		return universeMax;
	}

	public double getUniverseMin() {
		return universeMin;
	}

	public double getValue() {
		return value;
	}

	/** Return 'true' if this is an output variable */
	public boolean isOutputVarable() {
		return (defuzzifier != null); // Only output variables have defuzzyfiers
	}

	/** Get a 'linguisticTerms' iterator (by name) */
	public Iterator iteratorLinguisticTermNamesSorted() {
		ArrayList al = new ArrayList(linguisticTerms.keySet());
		Collections.sort(al);
		return al.iterator();
	}

	/** Reset defuzzifier (if any) */
	public void reset() {
		if( defuzzifier != null ) {
			defuzzifier.reset();
			// Set default value for output variables (if any default value was defined)
			if( !Double.isNaN(defaultValue) ) value = defaultValue;
		}
		latestDefuzzifiedValue = Double.NaN;
	}

	public void setDefaultValue(double defualtValue) {
		this.defaultValue = defualtValue;
	}

	public void setDefuzzifier(Defuzzifier deffuzifier) {
		this.defuzzifier = deffuzifier;
	}

	public void setLatestDefuzzifiedValue(double latestDefuzzifiedValue) {
		this.latestDefuzzifiedValue = latestDefuzzifiedValue;
	}

	public void setLinguisticTerms(THashMap<MutableString, LinguisticTerm> linguisticTerms) {
		this.linguisticTerms = linguisticTerms;
		estimateUniverse();
	}

	public void setName(MutableString name) {
		this.name = name;
	}

	public void setRuleAggregationMethod(RuleAggregationMethod ruleAggregationMethod) {
		this.ruleAggregationMethod = ruleAggregationMethod;
	}

	public void setUniverseMax(double universeMax) {
		this.universeMax = universeMax;
		linguisticTerms.forEachValue(new LTProc(universeMin,universeMax));

		step = (universeMax - universeMin) / (numberOfPoints);
		if (defuzzifier!=null && !defuzzifier.isDiscrete()){
			DefuzzifierContinuous dfc = (DefuzzifierContinuous)defuzzifier;
			dfc.init(universeMin, universeMax);
		}
	}

	public void setUniverseMin(double universeMin) {
		this.universeMin = universeMin;
		linguisticTerms.forEachValue(new LTProc(universeMin,universeMax));

		step = (universeMax - universeMin) / (numberOfPoints);
		if (defuzzifier!=null && !defuzzifier.isDiscrete()){
			DefuzzifierContinuous dfc = (DefuzzifierContinuous)defuzzifier;
			dfc.init(universeMin, universeMax);
		}
	}

	public void setValue(double value) {
		if( (value < universeMin) || (value > universeMax) ) Gpr.warn("Value out of range?. Variable: '" + name + "', Universe: [" + universeMin + ", " + universeMax + "], Value: " + value);
		this.value = value;
	}

	public final boolean execute(LinguisticTerm l) {
		MembershipFunction mf=l.membershipFunction;
		if (mf instanceof MembershipFunctionContinuous){
			((MembershipFunctionContinuous)mf).setTime(time,defuzzifier!=null);
		}//*/
		return true;
	}

	public void setTime(double time){
//		if (this.time==time) return;
		this.time=time;
		linguisticTerms.forEachValue(this);
	}

	public void preCalculate(){
		if (isEvaluated) return;
		linguisticTerms.forEachValue(new TObjectProcedure<LinguisticTerm>(){
			public boolean execute(LinguisticTerm lt) {
				// TODO Auto-generated method stub
				MembershipFunction mf = lt.membershipFunction;
				if (mf instanceof MembershipFunctionGaussian) {
					MembershipFunctionGaussian mfg = (MembershipFunctionGaussian) mf;
					mfg.preCalculate();

				}
				return true;
			}
		});
		isEvaluated =true;
	}

	/**
	 * Printable string
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = name + " : \n";

		// Show defuzifier for "output" variables, value for "input" variables
		if( defuzzifier != null ) str += "\tDefuzzifier : " + defuzzifier.toString() + "\n\tLatest defuzzified value: " + latestDefuzzifiedValue + "\n";
		else str += "\tValue: " + value + "\n";

		if( ruleAggregationMethod != null ) str += "\tAggregation method: " + ruleAggregationMethod.getName() + "\n";
		if( !Double.isNaN(defaultValue) ) str += "\tDefault value: " + defaultValue + "\n";

		// Show each 'termName' and it's membership function
		for( Iterator<MutableString> it = linguisticTerms.keySet().iterator(); it.hasNext(); ) {
			MutableString key = it.next();
			LinguisticTerm linguisticTerm = linguisticTerms.get(key);
			str += "\t" + linguisticTerm.toString(value) + "\n";
		}
		return str;
	}
}
