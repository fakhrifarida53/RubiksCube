import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;


public class Face implements Comparable<Face> {
	private static final Color ORANGE = new Color(255, 88, 0);
	private static final Color BLUE = new Color(255 - 184, 255 - 138, 255 - 0);
	private static final Color GREEN = new Color(255 - 255, 255 - 82, 255 - 255);
	private double myScale;
	private int myWidth;
	private int myHeight;
	private Point3D[] myPoints;
	private Color myColor;
	private Polygon myPolygon;
	private Polygon mySmallPolygon;

	public Face(Color theColor, Point3D[] thePoints, int theWidth, int theHeight,
		double theScale) {
		myColor = theColor;
		myPoints = thePoints;
		myWidth = theWidth;
		myHeight = theHeight;
		myScale = theScale;
	}

	private void setupPolygon() {
		int myXs[] = new int[4];
		myXs[0] = ((myWidth / 2) + (int) (myScale * myPoints[0].getX()));
		myXs[1] = ((myWidth / 2) + (int) (myScale * myPoints[1].getX()));
		myXs[2] = ((myWidth / 2) + (int) (myScale * myPoints[2].getX()));
		myXs[3] = ((myWidth / 2) + (int) (myScale * myPoints[3].getX()));

		int myYs[] = new int[4];
		myYs[0] = (myHeight / 2) + (int)(myScale * -myPoints[0].getY());
		myYs[1] = (myHeight / 2) + (int)(myScale * -myPoints[1].getY());
		myYs[2] = (myHeight / 2) + (int)(myScale * -myPoints[2].getY());
		myYs[3] = (myHeight / 2) + (int)(myScale * -myPoints[3].getY());


		// Artificial Depth perception
		for (int i = 0; i < 4; i++) {
			if (myPoints[i].getY() > 0) {
				myYs[i] -= myScale * (myPoints[i].getZ() * myPoints[i].getY()) * 0.002;
			}
			if (myPoints[i].getY() < 0) {
				myYs[i] += myScale * (myPoints[i].getZ() * myPoints[i].getY()) * -0.002;
			}
			if (myPoints[i].getX() > 0) {
				myXs[i] -= myScale * (myPoints[i].getZ() * myPoints[i].getX()) * -0.002;
			}
			if (myPoints[i].getX() < 0) {
				myXs[i] += myScale * (myPoints[i].getZ() * myPoints[i].getX()) * 0.002;
			}
		}
		myPolygon = new Polygon(myXs, myYs, 4);
	}

	private void setupSmallPolygon() {

		Point3D[] mySmallPoints = new Point3D[4];

		for (int i = 0; i < 4; i++) {
			mySmallPoints[i] = new Point3D(myPoints[i].getX(),
				myPoints[i].getY(), myPoints[i].getZ());
		}

		int myXs[] = new int[4];
		myXs[0] = ((7 * myWidth / 8) + (int) (0.25 * mySmallPoints[0].getX()));
		myXs[1] = ((7 * myWidth / 8) + (int) (0.25 * mySmallPoints[1].getX()));
		myXs[2] = ((7 * myWidth / 8) + (int) (0.25 * mySmallPoints[2].getX()));
		myXs[3] = ((7 * myWidth / 8) + (int) (0.25 * mySmallPoints[3].getX()));

		int myYs[] = new int[4];
		myYs[0] = (1 * myWidth / 8) + (int)(0.25 * -mySmallPoints[0].getY());
		myYs[1] = (1 * myWidth / 8) + (int)(0.25 * -mySmallPoints[1].getY());
		myYs[2] = (1 * myWidth / 8) + (int)(0.25 * -mySmallPoints[2].getY());
		myYs[3] = (1 * myWidth / 8) + (int)(0.25 * -mySmallPoints[3].getY());

		// Artificial Depth perception
		for (int i = 0; i < 4; i++) {
			if (mySmallPoints[i].getY() > 0) {
				myYs[i] -= 0.25 * (mySmallPoints[i].getZ() * mySmallPoints[i].getY()) * 0.002;
			}
			if (mySmallPoints[i].getY() < 0) {
				myYs[i] += 0.25 * (mySmallPoints[i].getZ() * mySmallPoints[i].getY()) * -0.002;
			}
			if (mySmallPoints[i].getX() > 0) {
				myXs[i] -= 0.25 * (mySmallPoints[i].getZ() * mySmallPoints[i].getX()) * -0.002;
			}
			if (mySmallPoints[i].getX() < 0) {
				myXs[i] += 0.25 * (mySmallPoints[i].getZ() * mySmallPoints[i].getX()) * 0.002;
			}
		}
		mySmallPolygon = new Polygon(myXs, myYs, 4);
	}

	public void draw(Graphics2D g, boolean theSmall) {
		if (theSmall) {
			setupSmallPolygon();
			g.setPaint(new GradientPaint(mySmallPolygon.xpoints[1], mySmallPolygon.ypoints[1],
				myColor, mySmallPolygon.xpoints[0], mySmallPolygon.ypoints[0], Color.DARK_GRAY));
			g.fillPolygon(mySmallPolygon);
	        g.setColor(myColor);
			g.drawPolygon(mySmallPolygon);
		} else {
			setupPolygon();
			g.setPaintMode();
			g.setPaint(new GradientPaint(myPolygon.xpoints[1], myPolygon.ypoints[1],
				myColor, myPolygon.xpoints[0], myPolygon.ypoints[0], Color.DARK_GRAY));
			g.fillPolygon(myPolygon);
	        g.setColor(myColor);
			g.drawPolygon(myPolygon);
		}
	}

	public void setColor(Color theColor) {
		myColor = theColor;
	}

	public int getColorInt() {
		if (myColor.equals(Color.WHITE)) {
			return 0;
		} else if (myColor.equals(Color.RED)) {
			return 1;
		} else if (myColor.equals(new Color(71, 117, 255))) {
			return 2;
		} else if (myColor.equals(new Color(255, 88, 0))) {
			return 3;
		} else if (myColor.equals(new Color(0, 173, 0))) {
			return 4;
		} else if (myColor.equals(Color.YELLOW)) {
			return 5;
		}
		return -1;
	}

	public Color getColor() {
		return myColor;
	}

	public void select() {
		if (myColor.equals(Color.WHITE)) myColor = Color.RED;
		else if (myColor.equals(Color.RED)) myColor = BLUE;
		else if (myColor.equals(BLUE)) myColor = ORANGE;
		else if (myColor.equals(ORANGE)) myColor = GREEN;
		else if (myColor.equals(GREEN)) myColor = Color.YELLOW;
		else if (myColor.equals(Color.YELLOW)) myColor = Color.WHITE;
	}

	public boolean containsPoint(int theX, int theY) {
		setupPolygon();
		return myPolygon.contains(theX, theY);
	}

	public double getAbsoluteZ() {
		double total = 0.0;
		for (Point3D point : myPoints) {
			total += point.getZ();
		}
		return total;
	}

	public void scale(String theScaling) {
		if (theScaling.equals("up")) {
			if (myScale < 1.5) myScale += 0.05;
		} else if (theScaling.equals("down")) {
			if (myScale > 0.25) myScale -= 0.05;
		}
	}

	@Override
	public int compareTo(Face theFace) {
		if (theFace.getAbsoluteZ() > this.getAbsoluteZ()) return -1;
		else if (theFace.getAbsoluteZ() < this.getAbsoluteZ()) return 1;
		return 0;
	}

	public Face copy() {
		Point3D[] tempPoints = {new Point3D(myPoints[0].getX(), myPoints[0].getY(), myPoints[0].getZ()),
								new Point3D(myPoints[1].getX(), myPoints[1].getY(), myPoints[1].getZ()),
								new Point3D(myPoints[2].getX(), myPoints[2].getY(), myPoints[2].getZ()),
								new Point3D(myPoints[3].getX(), myPoints[3].getY(), myPoints[3].getZ())};
		return new Face(myColor, tempPoints, myWidth, myHeight, myScale);
	}
}
