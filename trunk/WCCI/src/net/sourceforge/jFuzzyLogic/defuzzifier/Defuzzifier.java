package net.sourceforge.jFuzzyLogic.defuzzifier;

import net.sourceforge.jFuzzyLogic.PlotWindow;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import org.jfree.chart.JFreeChart;

/**
 * Generic defuzzifier
 * @author pcingola@users.sourceforge.net
 */
public abstract class Defuzzifier {
	public static int DEFAULT_NUMBER_OF_POINTS = PlotWindow.DEFAULT_CHART_NUMBER_OF_POINTS;
	/** Discrete defuzzifier (e.g. for singletons) */
	boolean discrete;

	/** Constructor */
	public Defuzzifier(Variable variable) {}

	/**
	 * Create a defuzzifier's chart
	 * @param title : Title to show (if null => show membership function name)
	 * @param showIt : If true, plot is displayed
	 */
	public abstract JFreeChart chart(String title, boolean showIt);

	/**
	 * Deffuzification function
	 * Note: Has to return Double.NaN if no rule infered this variable
	 */
	public abstract double defuzzify();

	/** Short name */
	public String getName() {
		String str = this.getClass().getName();
		String dfStr = "Defuzzifier";
		int ind = str.lastIndexOf('.');
		if( ind >= 0 ) {
			str = str.substring(ind + 1);
			if( str.startsWith(dfStr) ) str = str.substring(dfStr.length());
		}
		return str;
	}

	public boolean isDiscrete() {
		return discrete;
	}

	/** Reset defuzzifier values, this method is invoked on every RuleSet.evaluate() */
	public abstract void reset();

	public void setDiscrete(boolean discrete) {
		this.discrete = discrete;
	}

	@Override
	public String toString() {
		return getName();
	}

	public abstract String toStringFCL();
}
