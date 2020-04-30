public class Point3D {
	private double x;
	private double y;
	private double z;

	public Point3D(double theX, double theY, double theZ) {
		x = theX;
		y = theY;
		z = theZ;
	}

	public void setX(double theX) {
		x = theX;
	}

	public void setY(double theY) {
		y = theY;
	}

	public void setZ(double theZ) {
		z = theZ;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public String toString() {
		return "Point (" + x + ", " + y + ", " + z + ")";
	}

	public Point3D copy() {
		return new Point3D(x, y, z);
	}

}
