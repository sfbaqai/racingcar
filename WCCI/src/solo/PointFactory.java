package solo;
import com.graphbuilder.curve.Point;

public class PointFactory {

	public static Point createPoint(double x, double y) {
		final double[] arr = new double[] {x, y};

		return new Point() {
			public double[] getLocation() {
				return arr;
			}

			public void setLocation(double[] loc) {
				arr[0] = loc[0];
				arr[1] = loc[1];
			}
		};
	}
}