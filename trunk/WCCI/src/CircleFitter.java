import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import solo.EdgeDetector;
import solo.TrackSegment;
import solo.Vector2D;

import cern.colt.list.DoubleArrayList;
import jaolho.data.lma.LMA;
import jaolho.data.lma.LMAFunction;
import jaolho.data.lma.LMAMultiDimFunction;


/**
 * 
 */

/**
 * @author kokichi3000
 *
 */
public final class CircleFitter extends LMAMultiDimFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4026823680780498218L;
		
//
	/**
	 * @param args
	 */
			
	private LMA cf = null;
	private double[] initialParams = null;
	public CircleFitter(double[] initialGuess,double[] xx,double[] yy){
		int len = xx.length;
		double[][] data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,xx[i],yy[i]};
		}		
		initialParams = initialGuess;
		cf = new LMA(this,initialGuess,data);
	}
	
	public CircleFitter(double[] initialGuess,Vector2D[] v){
		int len = v.length;
		double[][] data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,v[i].x(),v[i].y()};
		}
		initialParams = initialGuess;
		cf = new LMA(this,initialGuess,data);
	}
	
	
	public void setData(Vector2D[] v){
		int len = v.length;
		double[][] data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,v[i].x(),v[i].y()};
		}		
		cf = new LMA(this,initialParams,data);
	}
	
	public void setData(double[] xx,double[] yy){
		int len = xx.length;
		double[][] data = new double[len][3];
		for (int i=0;i<len;++i){
			data[i]=new double[]{0,xx[i],yy[i]};
		}		
		cf = new LMA(this,initialParams,data);
	}
	/**
	 * @return the initialParams
	 */
	public double[] getInitialParams() {
		return initialParams;
	}

	/**
	 * @param initialParams the initialParams to set
	 */
	public void setInitialParams(double[] initialParams) {
		this.initialParams = initialParams;
	}

	public double[] fit(){
		cf.fit();
		return cf.parameters.clone();
	}
	
	public double[] fit(double lambda,double minDeltaChi2,int maxIterations){		
		cf.fit(lambda, minDeltaChi2, maxIterations);
		return cf.parameters.clone();
	}
	
	public double getEstimatedCenterX(){
		return cf.parameters[0];
	}
	
	public double getEstimatedCenterY(){
		return cf.parameters[1];
	}

	public double getEstimatedRadius(){
		return cf.parameters[2];
	}
	
	public Vector2D getEstimatedCenter(){
		return new Vector2D(cf.parameters[0],cf.parameters[1]);
	}
	
	public LMA getLMAObject(){
		return cf;
	}
	
	@Override
	public double getPartialDerivate(double[] x, double[] a, int parameterIndex) {
		// TODO Auto-generated method stub
		if (parameterIndex==2)
			return -1;
		double dx = a[0]-x[0];
		double dy = a[1]-x[1];
		double d = Math.sqrt(dx*dx+dy*dy);
		switch (parameterIndex){
		case 0:return dx/d;
		case 1:return dy/d;		
		}
		return -1;
	}



	@Override
	public double getY(double[] x, double[] a) {
		// TODO Auto-generated method stub
		double dx = x[0]-a[0];
		double dy = x[1]-a[1];
		return Math.sqrt(dx*dx+dy*dy)-a[2];
	}

	final static int NUM = 300;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		double x0 =10;
		double y0 = 15;
		double r = 110;		
//		double[] xx = new double[]{1,2,5,7,9,3};
//		double[] yy= new double[]{7,6,8,7,5,7};
		double[] xx = new double[NUM];
		double[] yy = new double[NUM];
		int i=0;
		XYSeries series = new XYSeries("Test");
		for (i =0;i<NUM;++i){
			double w = i*2+Math.random()*20;
			xx[i] = x0+r*Math.sin(w)+Math.random();
			yy[i] = y0+r*Math.cos(w)+Math.random();			
			series.add(xx[i],yy[i]);
		}
				
		double[] initialParams = new double[]{x0,y0,20};
		CircleFitter cf = new CircleFitter(initialParams,xx,yy);		
		
		try{
			cf.fit();						
			double xx0 = cf.getEstimatedCenterX();
			double yy0 = cf.getEstimatedCenterY();
			double rr0 = cf.getEstimatedRadius();
			System.out.println("("+xx0+","+yy0+")"+"\t"+rr0);
			System.out.println(cf.getLMAObject().getMeanRelativeError());
			TrackSegment.circle(xx0, yy0, rr0, series);
			XYDataset xyDataset = new XYSeriesCollection(series);
			final JFreeChart chart = ChartFactory.createScatterPlot("Data", "x", "Membership", xyDataset, PlotOrientation.VERTICAL, false, true, false );
			chart.getXYPlot().getDomainAxis().setRange(-200.0,200.0);
			chart.getXYPlot().getRangeAxis().setRange(-200.0,200.0);
			JFrame jf = new JFrame();
			
			ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new java.awt.Dimension(500, 500));
			jf.setContentPane(chartPanel);
			jf.setMinimumSize(new Dimension(600,800));
			jf.setPreferredSize(new Dimension(600,800));
			jf.setVisible(true);
			
			
			

		} catch (Exception e) {			
			// TODO: handle exception
			e.printStackTrace();
		}
	}


}
