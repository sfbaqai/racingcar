package net.sourceforge.jFuzzyLogic.membership;

import net.sourceforge.jFuzzyLogic.PlotWindow;

import org.jfree.chart.JFreeChart;

/**
 * Base membership function
 * @author pcingola@users.sourceforge.net
 */
public abstract class MembershipFunction {

	final static int numberOfPoints = PlotWindow.DEFAULT_CHART_NUMBER_OF_POINTS;
	double step;

	/** Debug mode for this class? */
	public static boolean debug = false;

	//-------------------------------------------------------------------------
	// Variables
	//-------------------------------------------------------------------------

	boolean discrete;
	/** Function's parameters */
	double parameters[];
	double oldParams[];
	/** Universe max (range max) */
	double universeMax;
	/** Universe min (range min) */
	double universeMin;
	//-------------------------------------------------------------------------
	// Constructor
	//-------------------------------------------------------------------------

	/** Default Constructor */
	MembershipFunction() {
		step = universeMax = universeMin = Double.NaN;
	}



	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------
	/**
	 * Create a membership function chart
	 * @param title : Title to show (if null => show membership function name)
	 * @param showIt : If true, plot is displayed
	 */
	public abstract JFreeChart chart(String title, boolean showIt);

	public abstract boolean checkParamters(StringBuffer errors);

	/** Try to guess the universe (if not setted) */
	public abstract void estimateUniverse();

	/** Short name */
	public String getName() {
		String str = this.getClass().getName();
		String mfStr = "MembershipFunction";
		int ind = str.lastIndexOf('.');
		if( ind >= 0 ) {
			str = str.substring(ind + 1);
			if( str.startsWith(mfStr) ) str = str.substring(mfStr.length());
		}
		return str;
	}

	public double getParameter(int i) {
		return parameters[i];
	}

	public int getParametersLength() {
		return (parameters != null ? parameters.length : 0);
	}

	public final double getUniverseMax() {
		return universeMax;
	}

	public final double getUniverseMin() {
		return universeMin;
	}

	public final boolean isDiscrete() {
		return discrete;
	}

	/**
	 * Get membership function's value.
	 * @param in : Variable's 'x' value
	 * Note: Output mu be in range [0,1]
	 */
	public abstract double membership(double in);

	public void setDiscrete(boolean discrete) {
		this.discrete = discrete;
	}

	public void setParameter(int i, double value) {
		this.parameters[i] = value;
		if (oldParams!=null) this.oldParams[i] = value;
	}

	public final void setUniverseMax(double universeMax) {
		this.universeMax = universeMax;
		step = (universeMax - universeMin) / (numberOfPoints);
	}

	public final void setUniverseMin(double universeMin) {
		this.universeMin = universeMin;
		step = (universeMax - universeMin) / (numberOfPoints);
	}

	@Override
	public String toString() {
		return getName();
	}

	public abstract String toStringFCL();
}
