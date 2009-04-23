package net.sourceforge.jFuzzyLogic.defuzzifier;

import net.sourceforge.jFuzzyLogic.Gpr;
import net.sourceforge.jFuzzyLogic.PlotWindow;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Generic continuous defuzzifier (a defuzzifier for continuous membership functions)
 * @author pcingola@users.sourceforge.net
 */
public abstract class DefuzzifierContinuous extends Defuzzifier {

	/** Default number of points for 'values[]' */

	/** Where function ends */
	double max;
	/** Where function begins */
	double min;
	/**
	 * Step size between each element in 'values[]'
	 * 			stepSize = (max - min) / values.length
	 */
	double stepSize;
	/**
	 * Funcion values: A generic continuous function
	 * 			y = f(x)
	 * where x : [min, max]
	 * Values are stored in 'values[]' array.
	 * Array's index is calculated as:
	 * 			index = (x - min) / (max - min) * (values.length)
	 */
	double values[];
	int numberOfPoints = DEFAULT_NUMBER_OF_POINTS;

	public DefuzzifierContinuous(Variable variable) {
		super(variable);
		discrete = false;
		variable.estimateUniverse();
		this.values = new double[numberOfPoints+1];
		this.min = variable.getUniverseMin();
		this.max = variable.getUniverseMax();
		this.stepSize = (max - min) / numberOfPoints;
	}

	public DefuzzifierContinuous(Variable variable,int num) {
		super(variable);
		discrete = false;
		variable.estimateUniverse();
		numberOfPoints = num;
		this.values = new double[num+1];
		this.min = variable.getUniverseMin();
		this.max = variable.getUniverseMax();
		this.stepSize = (max - min) / num;
	}

	public final void addValue(int index, double value) {
		values[index] += value;
	}

	/**
	 * Create a defuzzifier's chart
	 * @param title : Title to show (if null => show membership function name)
	 * @param showIt : If true, plot is displayed
	 */
	@Override
	public JFreeChart chart(String title, boolean showIt) {
		// Default title
		if( title == null ) title = getName();

		// Sanity check
		if( Double.isNaN(min) || Double.isInfinite(max) ) {
			Gpr.debug("Limits not calculated yet: [" + min + ", " + max + "]");
			return null;
		}

		// Create a serie and add values[] points
		XYSeries series = new XYSeries(title);
		int numberOfPoints = values.length;
		double xx = min;
		double step = (max - min) / (numberOfPoints);
		for( int i = 0; i < numberOfPoints; i++, xx += step )
			series.add(xx, values[i]);
		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		JFreeChart chart = ChartFactory.createXYLineChart(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false);

		if( showIt ) PlotWindow.showIt(title, chart);

		return chart;
	}

	/** Deffuzification function */
	@Override
	public abstract double defuzzify();


	/** Calculate function's area */
	public final double getArea() {
		double sum = 0;
		for( int i = 0; i < values.length; i++ )
			sum += values[i];
		return sum * stepSize;
	}

	/** Get 'values[]' index */
	public final int getIndex(double d) {
		if( (d < min) || (d > max) ) throw new RuntimeException("Value out of range: " + d);
		return (int) ((d - min) / stepSize);
	}

	public final int getLength() {
		return numberOfPoints;
	}

	public final double getMax() {
		return max;
	}

	public final double getMin() {
		return min;
	}

	public final double getStepSize() {
		return stepSize;
	}

	/** Get a value from 'values[]' using a double as index */
	public final double getValue(double x) {
		if( (x < min) || (x > max) ) throw new RuntimeException("Value out of range: " + x);
		return values[(int) ((x - min) / stepSize)];
	}

	public final double getValue(int index) {
		return values[index];
	}

	public final double[] getValues() {
		return values;
	}

	/**
	 * Initialize
	 * @param min : Minimum
	 * @param max : Maximum
	 * @param numberOfPoints
	 */
	public final void init(double min, double max) {
//		 Go on only if min & max are setted
		// Check parameters
		//if( min >= max ) throw new RuntimeException("Parameter max is out of range (should satisfy: min < max). min: " + min + "\tmax: " + max);

		// Initialize
		this.min = min;
		this.max = max;
		this.stepSize = (max - min) / numberOfPoints;
	}

	@Override
	public final boolean isDiscrete() {
		return discrete;
	}

	/** Reset values (in 'values[] array) */
	@Override
	public final void reset() {
		if( values != null ) {
			values =null;
			values = new double[numberOfPoints];
		}
	}

	@Override
	public void setDiscrete(boolean discrete) {
		this.discrete = discrete;
	}

	public void setMax(double max) {
		this.max = max;
		this.stepSize = (max - min) / numberOfPoints;
	}

	public void setMin(double min) {
		this.min = min;
		this.stepSize = (max - min) / numberOfPoints;
	}


	public void setValue(double valueX, double valueY) {
		values[getIndex(valueX)] = valueY;
	}

	public void setValue(int index, double value) {
		values[index] = value;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	/**
	 * @return the numberOfPoints
	 */
	public int getNumberOfPoints() {
		return numberOfPoints;
	}

	/**
	 * @param numberOfPoints the numberOfPoints to set
	 */
	public void setNumberOfPoints(int numberOfPoints) {
		if (this.numberOfPoints == numberOfPoints) return;
		this.numberOfPoints = numberOfPoints;
		this.values = null;
		this.values = new double[numberOfPoints];
		this.stepSize = (max - min) / numberOfPoints;
	}

}