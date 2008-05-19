package net.sourceforge.jFuzzyLogic.membership;

import net.sourceforge.jFuzzyLogic.PlotWindow;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @author kokichi3000
 *
 */
public abstract class Perturbation extends MembershipFunctionContinuous {
	int numberOfInstants;
	double[] lookup = null;
	double startTime =0;
	double step = 1.0d;
	double scale=1.0;

	public Perturbation(int no) {
		super();
		this.numberOfInstants = no;
	}

	public Perturbation(int no,double scale) {
		super();
		this.numberOfInstants = no;
		this.scale = scale;
	}

	public final int getNumberOfInstants() {
		return numberOfInstants;
	}



	public final void setNumberOfInstants(int numberOfInstants) {
		this.numberOfInstants = numberOfInstants;
	}



	public final double getStartTime() {
		return startTime;
	}



	public final void setStartTime(double startTime) {
		this.startTime = startTime;
	}



	public final double getStep() {
		return step;
	}

	public final void setStep(double step) {
		this.step = step;
	}

	/**
	 * @return the scale
	 */
	public final double getScale() {
		return scale;
	}



	/**
	 * @param scale the scale to set
	 */
	public final void setScale(double scale) {
		this.scale = scale;
	}

	public final void prepareToStart(){
		if (lookup==null){
			lookup= new double[numberOfInstants+1];
		}
		try {
			//BufferedReader br = new BufferedReader(new FileReader("C:\\test.txt"));
			double t= startTime;
			for (int i=0;i<numberOfInstants;++i,t+=step){
				lookup[i] = membership(t);
				//lookup[i] = scale*Double.parseDouble(br.readLine());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public double get(double time){
		double ind = (time-startTime)/step -1;
		int i = (int)ind;
		return (i!=ind) ? membership(time) : lookup[i];
	}

	/**
	 * Create a membership function chart
	 * @param title : Title to show (if null => show membership function name)
	 * @param showIt : If true, plot is displayed
	 */
	public JFreeChart chart(String title, boolean showIt) {
		if( title == null ) title = getName();

		// Sanity check
		if( Double.isNaN(universeMin) || Double.isInfinite(universeMax) ) estimateUniverse();

		// Evaluate membership function and add points to dataset
		XYSeries series = new XYSeries(title);
		double xx = universeMin;
		for( int i = 0; i < numberOfInstants; i++, xx += step ) {
			series.add(xx, membership(xx));
		}
		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		JFreeChart chart = ChartFactory.createXYLineChart(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		if( showIt ) PlotWindow.showIt(title, chart);

		return chart;
	}

}
