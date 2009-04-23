package net.sourceforge.jFuzzyLogic.membership;

import it.unimi.dsi.fastutil.doubles.DoubleArrays;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.sourceforge.jFuzzyLogic.PlotWindow;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


/**
 * Base continuous membership function
 * @author pcingola@users.sourceforge.net
 */
public abstract class MembershipFunctionContinuous extends MembershipFunction {

	/**
	 * Default constructor
	 */
	ObjectArrayList<Perturbation>[] perturbations = null;
	double[] lookup=null;
	double time=0.0d;

	public MembershipFunctionContinuous() {
		super();
		discrete = false;
	}

	public final void addPerturbation(int index,Perturbation p){
		if (index<0) return;
		if (perturbations==null)
			perturbations = new ObjectArrayList[index+1];
		if (perturbations.length<=index){
			ObjectArrayList<Perturbation>[] pp = new ObjectArrayList[index+1];
			System.arraycopy(perturbations, 0, pp, 0, perturbations.length);
			perturbations = pp;
		}
		if (perturbations[index]==null)
			perturbations[index] = new ObjectArrayList<Perturbation>(7);
		perturbations[index].add(p);
	}

	public void setTime(double time,boolean isOutput) {
//		if (this.time==time) return;
		this.time = time;

		if (time==0.0d) return;
//		System.out.println(12345532);
		if (perturbations==null) return;

		if (oldParams==null)
			oldParams = DoubleArrays.copy(parameters);

		for (int i=0;i<perturbations.length;++i){
			ObjectArrayList<Perturbation> l = (perturbations[i]);
			double mean=oldParams[i];
			if (l!=null)
			for (Perturbation p : l){
				mean += p.get(time);
			}
			parameters[i]=mean;
		}

		if (isOutput) {
			preCalculate();
		}
		else {
			lookup=null;
		}
	}


	/**
	 * @return the time
	 */
	public final double getTime() {
		return time;
	}

	/**
	 * Create a membership function chart
	 * @param title : Title to show (if null => show membership function name)
	 * @param showIt : If true, plot is displayed
	 */
	@Override
	public JFreeChart chart(String title, boolean showIt) {
		if( title == null ) title = getName();

		// Sanity check
		if( Double.isNaN(universeMin) || Double.isInfinite(universeMax) ) estimateUniverse();

		// Evaluate membership function and add points to dataset
		XYSeries series = new XYSeries(title);
		double xx = universeMin;
		for( int i = 0; i < numberOfPoints; i++, xx += step ) {
			series.add(xx, membership(xx));
		}
		XYDataset xyDataset = new XYSeriesCollection(series);

		// Create plot and show it
		JFreeChart chart = ChartFactory.createXYLineChart(title, "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
		if( showIt ) PlotWindow.showIt(title, chart);

		return chart;
	}

	public final double[] getLookup(){
		return lookup;
	}

	public void preCalculate(){
		if (lookup==null) lookup = new double[numberOfPoints+1];
		double[] lookup=this.lookup;

		double step = this.step,x=0.0d;

		for (int i=0,len=lookup.length;i<len;++i,x+=step){
			lookup[i]=membership(x);
		}
	}
}
