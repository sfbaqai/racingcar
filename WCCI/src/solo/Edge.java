/**
 * 
 */
package solo;

import it.unimi.dsi.fastutil.doubles.Double2DoubleSortedMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;

/**
 * @author kokichi3000
 *
 */
public class Edge {
	final static int NUM_POINTS=100;
	
	double[] leftEgdeX = null;
	double[] leftEgdeY = null;
	double[] rightEgdeX = null;
	double[] rightEgdeY = null;
	double[] leftRange = null;
	double[] rightRange = null;
	int numPointLeft = 0;
	int numPointRight = 0;
	public double pointx,pointy;
	public double angleToPoint;
	Double2DoubleSortedMap angleDistMap = null;
	Double2ObjectSortedMap<double[]> polar2Catesian = null;	
	

}
