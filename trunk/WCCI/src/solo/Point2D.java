package solo;


import java.util.Collection;
import java.util.Iterator;

public class Point2D extends Vector2D {
	/**
	 * Interprets the first component of the vector as x and the
	 * second as y.
	 **/

	public Point2D(Vector2D vector) {
		super(vector);
	}
	/**
	 * Interprets the first component of the array as x and the second
	 * as y.
	 **/
	public Point2D(double[] position) {
		super(position[0], position[1]);
	}
	public Point2D(double x, double y) {
		super(x, y);
	}

	/**
	 * Returns the distance between this and the given
	 * <code>position</code> sqrt(dx2 - dy2).
	 **/
	public double distanceTo(Point2D that) {
		return distanceTo(that.x, that.y);
	}


	/**
	 * This returns the maximum of between the x axis difference and
	 * the y-axis difference between the two points.  It can be used
	 * as a cheap but not very accurate distance measurement.
	 */
	public double maxCoordinateDistanceTo(Point2D that) {
		double dx = Math.abs(this.x - that.x);
		double dy = Math.abs(this.y - that.y);
		return dx > dy ? dx : dy;
	}

	/**
	 * Returns the linear distance between this and the position
	 * represented by the given <code>x</code> and <code>y</code>.
	 **/
	public double distanceTo(double x, double y) {
		double dx = this.x - x;
		double dy = this.y - y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Indicates <code>that</code> Point2D is contained in a
	 * rectangle centered around <code>this</code> and of width
	 * <code>2 * maxDifferenceX</code> and length <code>2 &
	 * maxDifferenceY</code>.
	 */
	public boolean near(Point2D that, double maxDifferenceX, double maxDifferenceY) {
		double dx = Math.abs(this.x - that.x);
		double dy = Math.abs(this.y - that.y);
		return dx <= maxDifferenceX && dy <= maxDifferenceY;
	}

	/**
	 * Returns the angle in radians from this position to the given
	 * <code>position</code>.  For reference, the x axis is 0.0, the y
	 * axis is PI/2, the negative x axis is -PI, and the negative y
	 * axis is -3PI/2.
	 **/
	public double headingTo(Point2D position) {
		return headingTo(position.x, position.y);
	}

	/**
	 * Returns the angle in radians from this position to the given
	 * position represented by the given <code>x</code> and
	 * <code>y</code>.  For reference, the x axis is 0.0, the y axis
	 * is PI/2, the negative x axis is -PI, and the negative y axis is
	 * -3PI/2.
	 **/
	public double headingTo(double x, double y) {
		double dx = x - this.x;
		double dy = y - this.y;
		return Math.atan2(dy, dx);
	}


	/**
	 * Returns a new Point2D that is offset from this position by the
	 * given <code>distance</code> in the direction of the given
	 * <code>angle</code>.
	 *
	 * @param distance from this point by which the returned point is offset
	 * @param angle in radians measured from the x axis toward y of
	 * the direction of the offset.
	 **/
	public Point2D offsetByPolar(double distance, double angle) {
		return new Point2D(this.plus(new Vector2D(distance, angle, true)));
	}


	/**
	 * This returns the result of adding the given <code>offset</code>
	 * to this as a new Point2D.  This is really the same as the
	 * <code>Vector2D.plus</code> function but because java sucks so bad
	 * about not allowing multiple return types, this is necessary.
	 **/
	public Point2D offsetBy(Vector2D offset) {
		return new Point2D(this.plus(offset));
	}

	/**
	 * Returns a position the ratio amount along a line from this
	 * position to the distant position.
	 *
	 * @param distant Point2D between which the returned point
	 * @param ratio
	 **/
	public Point2D toward(Point2D distant, double ratio) {
		return new Point2D(this.plus(distant.minus(this).times(ratio)));
	}

	/**
	 * Indicates that the given position is within the circle defined
	 * by the given <code>position</code> and <code>radius</code>.  It
	 * is inclusive of the bounds of the circle.
	 **/
	public boolean isInCircle(Point2D position, double radius) {
		return distanceTo(position) <= radius;
	}

	/**
	 * Indicates that the given position is within the rectangle where
	 * the distance from the center of the rectangle to the +/- x
	 * limit is <code>halfWidth</code> and the distance from the
	 * center of the rectangle to the +- y limit is
	 * <code>halfHeight</code>.  It is inclusive of the bounds of the
	 * rectangle.
	 **/
	public boolean isInRectangle(Point2D position, double halfWidth, double halfHeight) {
		return Math.abs(x - position.x) <= halfWidth && Math.abs(y - position.y) <= halfHeight;
	}

	/**
	 * Returns the point that is in the given <code>collection</code>
	 * and is within <code>slop</code> distance from this.
	 *
	 * @return position that is within slop distance to this or null if none
	 **/
	public Point2D in(java.util.Collection<Point2D> collection, double slop) {
		java.util.Iterator<Point2D> iter = collection.iterator();
		while (iter.hasNext()) {
			Point2D position = (Point2D) iter.next();
			if (distanceTo(position) < slop) return position;
		}
		return null;
	}

	/**
	 * Returns the point that is in the given <code>collection</code>
	 * and is within <code>xSlop</code> distance from this.x and
	 * <code>ySlop</code> distance from this.y.  In other words
	 * <code>xSlop</code> and <code>ySlop</code> define a rectangle.
	 * This is faster than <code>findIn(Collection,double)</code>
	 * because it doesn't have to do a square and square root.
	 *
	 * @return position that is within slop distance to this or null if none
	 **/
	public Point2D in(java.util.Collection<Point2D> collection, double xSlop, double ySlop) {
		java.util.Iterator<Point2D> iter = collection.iterator();
		while (iter.hasNext()) {
			Point2D position = (Point2D) iter.next();
			if (this.isInRectangle(position, xSlop, ySlop)) return position;
		}
		return null;
	}


	/**
	 * Returns the position in <code>positions</code> that is closest
	 * to <code>this</code>.
	 */
	public Point2D nearest(Point2D[] positions) {
		Point2D closest = null;
		double closestDistance = java.lang.Double.MAX_VALUE;
		for (int i = 0; i < positions.length; ++i) {
			double distance = this.distanceTo(positions[i]);
			if (closest == null || distance < closestDistance) {
				closest = positions[i];
				closestDistance = distance;
			}
		}

		return closest;
	}

	/**
	 * Returns the position in <code>positions</code> that is closest
	 * to <code>this</code>.
	 */
	public Point2D nearest(Collection<Point2D> positions) {
		Point2D closest = null;
		double closestDistance = java.lang.Double.MAX_VALUE;
		Iterator<Point2D> iter = positions.iterator();
		while (iter.hasNext()) {
			Point2D position = (Point2D) iter.next();
			double distance = this.distanceTo(position);
			if (closest == null || distance < closestDistance) {
				closest = position;
				closestDistance = distance;
			}
		}

		return closest;
	}

}