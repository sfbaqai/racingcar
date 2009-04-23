package net.sourceforge.jFuzzyLogic.defuzzifier;

import java.util.HashMap;
import java.util.Iterator;

import net.sourceforge.jFuzzyLogic.PlotWindow;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/**
 * Generic discrete defuzzifier (a defuzzifier for continuous membership functions)
 * @author pcingola@users.sourceforge.net
 */
public abstract class DefuzzifierDiscrete extends Defuzzifier {

	/** 
	 * Funcion values: A generic discrete function 
	 * 			x = [x_1, x_2, .... , x_n]
	 * 			y = [y_1, y_2, .... , y_n]
	 * 			y_i = f[x_i]
	 * Values are stored in 'discreteValues' hash
	 */
	HashMap<Double, Double> discreteValues;

	public DefuzzifierDiscrete(Variable variable) {
		super(variable);
		discrete = true;
		discreteValues = new HashMap<Double, Double>();
	}

	/**
	 * Create a defuzzifier's chart 
	 * @param title : Title to show (if null => show membership function name)
	 * @param showIt : If true, plot is displayed
	 */
	@Override
	public JFreeChart chart(String title, boolean showIt) {
		if( title == null ) title = getName();

		// Create a serie and add values[] points
		XYSeries series = new XYSeries(title);
		for( Iterator it = iterator(); it.hasNext(); ) {
			double xx = (Double) it.next();
			double yy = getDiscreteValue(xx);
			series.add(xx, yy);
		}
		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		JFreeChart chart = ChartFactory.createScatterPlot(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false);
		if( showIt ) PlotWindow.showIt(title, chart);

		return chart;
	}

	/** Deffuzification function */
	@Override
	public abstract double defuzzify();

	/** Get a point's 'y' value */
	public double getDiscreteValue(double x) {
		Double y = discreteValues.get(x);
		if( y == null ) return 0;
		return y;
	}

	/** Get an iterator (on discreteValues' keys) */
	public Iterator iterator() {
		return discreteValues.keySet().iterator();
	}

	/** Reset values */
	@Override
	public void reset() {
		if( discreteValues != null ) {
			for( Iterator it = iterator(); it.hasNext(); ) {
				Double key = (Double) it.next();
				discreteValues.put(key, Double.valueOf(0));
			}
		}
	}

	/** Set a point */
	public void setPoint(double x, double y) {
		discreteValues.put(x, y);
	}

	/** How many points are there in this defuzzifier */
	public int size() {
		return discreteValues.size();
	}

}