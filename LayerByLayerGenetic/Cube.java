import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.awt.GradientPaint;

//
//                  [BACK]
//                    |
//              (2)___|_______(3)
// 		        |\    v       |\
//              | \    [UP]   | \
//    [LEFT]----> (0)_________|_(1)
//              |  |          |  |
//             (6)_|_________(7) |  <----[RIGHT]
//              \  |          \  |
//               \ |  [FRONT]  \ |
//                \|            \|
//                (4)_^_________(5)
//                    |
//                    |
//	               [BOTTOM]
//

public class Cube {
	private int myRadius = 99;
	private Point3D[][][] myPoints;
	private Face[][][] myFaces;
	private int myWidth;
	private int myHeight;
	private double myStep;
	private int myDimensions;
	private double myScale;
	private LinkedList<String> myMoves;
	private boolean mySmall;
	private Cube mySmallCube;

	public Cube(int theWidth, int theHeight, double theStep, int theScale, int theDimensions, boolean theSmall) {
		mySmall = theSmall;
		myWidth = theWidth;
		myHeight = theHeight;
		myDimensions = theDimensions;
		while (myRadius % myDimensions != 0) myRadius++;
		myPoints = new Point3D[6][myDimensions * 2][myDimensions * 2];
		myFaces = new Face[6][myDimensions][myDimensions];
		myScale = theScale;
		myMoves = new LinkedList<String>();
		setPoints();
		setFaces();
		if (!mySmall) mySmallCube = new Cube(theWidth, theHeight, theStep, theScale, theDimensions, true);
		myStep = 45;
		rotateRight();
		rotateUp();
		if (mySmall) {
			myStep = 180;
			rotateUp();
			// rotateClockwise();

		}
		myStep = theStep;

	}

	private void setFaces() {
		int x = 0, y = 0;
		Color[] color = {Color.WHITE, Color.RED, new Color(71, 117, 255),
				new Color(255, 88, 0), new Color(0, 173, 0), Color.YELLOW};
		for (int face = 0; face < 6; face++) {
			y = 0;
			for (int i = 0; i < myDimensions; i++) {
				for (int j = 0; j < myDimensions; j++) {
					Point3D[] tempPoints = {myPoints[face][y][x], myPoints[face][y][x + 1],
							myPoints[face][y + 1][x + 1],  myPoints[face][y + 1][x]};
							x += 2;
					myFaces[face][i][j] = new Face(color[face], tempPoints, myWidth, myHeight, myScale);
				}
				x = 0;
				y += 2;
			}
		}
	}

	private void setFaces(Face[][][] theFaces) {
		myFaces = theFaces;
	}

	private void setPoints() {
		int x = 0, y = 0, z = 0;
		for (int face = 0; face < 6; face++) {
			if (face == 0) {        // WHITE(X,Z)
				x = -myRadius;
				y = myRadius;
				z = -myRadius;
			} else if (face == 1) { // RED(X,Y)
				x = -myRadius;
				y = myRadius;
				z = myRadius;
			} else if (face == 2) { // BLUE(Y,Z)
				x = myRadius;
				y = myRadius;
				z = myRadius;
			} else if (face == 3) { // ORANGE(X,Y)
				x = myRadius;
				y = myRadius;
				z = -myRadius;
			} else if (face == 4) { // GREEN(Y,Z);
				x = -myRadius;
				y = myRadius;
				z = -myRadius;
			} else if (face == 5) { // YELLOW(X,Z)
				x = -myRadius;
				y = -myRadius;
				z = myRadius;
			}

			for (int i = 0; i < 2 * myDimensions; i++) {
				for (int j = 0; j < 2 * myDimensions; j++) {
					myPoints[face][i][j] = new Point3D(x, y, z);
					if (j % 2 == 0) {
						if (face == 0) x += (2 * myRadius) / myDimensions;
						else if (face == 1) x += (2 * myRadius) / myDimensions;
						else if (face == 2) z -= (2 * myRadius) / myDimensions;
						else if (face == 3) x -= (2 * myRadius) / myDimensions;
						else if (face == 4) z += (2 * myRadius) / myDimensions;
						else if (face == 5) x += (2 * myRadius) / myDimensions;
					}
				}

				if (face == 0) {
					x = -myRadius; // Reset x for every new z value.
					if (i % 2 == 0) z += (2 * myRadius) / myDimensions;
				} else if (face == 1) {
					x = -myRadius; // Reset x for every new y value.
					if (i % 2 == 0) y -= (2 * myRadius) / myDimensions;
				} else if (face == 2) {
					z = myRadius; // Reset z for every new y value.
					if (i % 2 == 0) y -= (2 * myRadius) / myDimensions;
				} else if (face == 3) {
					x = myRadius; // Reset x for every new y value.
					if (i % 2 == 0) y -= (2 * myRadius) / myDimensions;
				} else if (face == 4) {
					z = -myRadius; // Reset z for every new y value.
					if (i % 2 == 0) y -= (2 * myRadius) / myDimensions;
				} else if (face == 5) {
					x = -myRadius; // Reset x for every new z value.
					if (i % 2 == 0) z -= (2 * myRadius) / myDimensions;
				}
			}
		}
	}

	public void rotateRight() {
		if (!mySmall) mySmallCube.rotateRight();
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < myDimensions * 2; i++) {
				for (int j = 0; j < myDimensions * 2; j++) {
					double tempX = myPoints[face][i][j].getX();
					double tempZ = myPoints[face][i][j].getZ();
					double radians = -degreesToRadians(myStep);
					myPoints[face][i][j].setX(tempX * Math.cos(radians) - tempZ * Math.sin(radians));
					myPoints[face][i][j].setZ(tempX * Math.sin(radians) + tempZ * Math.cos(radians));
				}
			}
		}

	}

	public void rotateLeft() {
		if (!mySmall) mySmallCube.rotateLeft();
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < myDimensions * 2; i++) {
				for (int j = 0; j < myDimensions * 2; j++) {
					double tempX = myPoints[face][i][j].getX();
					double tempZ = myPoints[face][i][j].getZ();
					double radians = degreesToRadians(myStep);
					myPoints[face][i][j].setX(tempX * Math.cos(radians) - tempZ * Math.sin(radians));
					myPoints[face][i][j].setZ(tempX * Math.sin(radians) + tempZ * Math.cos(radians));
				}
			}
		}
	}

	public void rotateDown() {
		if (!mySmall) mySmallCube.rotateDown();
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < myDimensions * 2; i++) {
				for (int j = 0; j < myDimensions * 2; j++) {
					double tempY = myPoints[face][i][j].getY();
					double tempZ = myPoints[face][i][j].getZ();
					double radians = -degreesToRadians(myStep);
					myPoints[face][i][j].setY(tempY * Math.cos(radians) - tempZ * Math.sin(radians));
					myPoints[face][i][j].setZ(tempY * Math.sin(radians) + tempZ * Math.cos(radians));
				}
			}
		}
	}

	public void rotateUp() {
		if (!mySmall) mySmallCube.rotateUp();
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < myDimensions * 2; i++) {
				for (int j = 0; j < myDimensions * 2; j++) {
					double tempY = myPoints[face][i][j].getY();
					double tempZ = myPoints[face][i][j].getZ();
					double radians = degreesToRadians(myStep);
					myPoints[face][i][j].setY(tempY * Math.cos(radians) - tempZ * Math.sin(radians));
					myPoints[face][i][j].setZ(tempY * Math.sin(radians) + tempZ * Math.cos(radians));
				}
			}
		}
	}

	public void rotateClockwise() {
		if (!mySmall) mySmallCube.rotateClockwise();
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < myDimensions * 2; i++) {
				for (int j = 0; j < myDimensions * 2; j++) {
					double tempY = myPoints[face][i][j].getY();
					double tempX = myPoints[face][i][j].getX();
					double radians = -degreesToRadians(myStep);
					myPoints[face][i][j].setY(tempY * Math.cos(radians) - tempX * Math.sin(radians));
					myPoints[face][i][j].setX(tempY * Math.sin(radians) + tempX * Math.cos(radians));
				}
			}
		}
	}

	public void rotateCounterClockwise() {
		if (!mySmall) mySmallCube.rotateCounterClockwise();
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < myDimensions * 2; i++) {
				for (int j = 0; j < myDimensions * 2; j++) {
					double tempY = myPoints[face][i][j].getY();
					double tempX = myPoints[face][i][j].getX();
					double radians = degreesToRadians(myStep);
					myPoints[face][i][j].setY(tempY * Math.cos(radians) - tempX * Math.sin(radians));
					myPoints[face][i][j].setX(tempY * Math.sin(radians) + tempX * Math.cos(radians));
				}
			}
		}
	}

	public void turn(int theFace, boolean theInversion) {
		switch(theFace) {
			case 0:
				if (theInversion) {
					turnWhiteInverse();
					myMoves.add("Wi");
				} else {
					turnWhite();
					myMoves.add("W");
				}
				break;
			case 1:
				if (theInversion) {
					turnRedInverse();
					myMoves.add("Ri");
				} else {
					turnRed();
					myMoves.add("R");
				}
				break;
			case 2:
				if (theInversion) {
					turnBlueInverse();
					myMoves.add("Bi");
				} else {
					turnBlue();
					myMoves.add("B");
				}
				break;
			case 3:
				if (theInversion) {
					turnOrangeInverse();
					myMoves.add("Oi");
				} else {
					turnOrange();
					myMoves.add("O");
				}
				break;
			case 4:
				if (theInversion) {
					turnGreenInverse();
					myMoves.add("Gi");
				} else {
					turnGreen();
					myMoves.add("G");
				}
				break;
			case 5:
				if (theInversion) {
					myMoves.add("Yi");
					turnYellowInverse();
				} else {
					turnYellow();
					myMoves.add("Y");
				}
				break;
		}
	}

	public LinkedList<String> getMoves() {
		return myMoves;
	}

	public void resetMoves() {
		myMoves = new LinkedList<String>();
	}

	public void turnRed() {
		turnRedInverse();
		turnRedInverse();
		turnRedInverse();
	}

	public void turnRedInverse() {
		if (!mySmall) mySmallCube.turnRedInverse();
		Color temp;

		turnFaceOnly(1);
		turnFaceOnly(1);
		turnFaceOnly(1);


		temp = myFaces[0][2][0].getColor();
		myFaces[0][2][0].setColor(myFaces[2][0][0].getColor());
		myFaces[2][0][0].setColor(myFaces[5][0][2].getColor());
		myFaces[5][0][2].setColor(myFaces[4][2][2].getColor());
		myFaces[4][2][2].setColor(temp);

		temp = myFaces[0][2][1].getColor();
		myFaces[0][2][1].setColor(myFaces[2][1][0].getColor());
		myFaces[2][1][0].setColor(myFaces[5][0][1].getColor());
		myFaces[5][0][1].setColor(myFaces[4][1][2].getColor());
		myFaces[4][1][2].setColor(temp);

		temp = myFaces[0][2][2].getColor();
		myFaces[0][2][2].setColor(myFaces[2][2][0].getColor());
		myFaces[2][2][0].setColor(myFaces[5][0][0].getColor());
		myFaces[5][0][0].setColor(myFaces[4][0][2].getColor());
		myFaces[4][0][2].setColor(temp);
	}

	public void turnBlue() {
		if (!mySmall) mySmallCube.turnBlue();
		Color temp;

		turnFaceOnly(2);

		temp = myFaces[0][2][2].getColor();
		myFaces[0][2][2].setColor(myFaces[1][2][2].getColor());
		myFaces[1][2][2].setColor(myFaces[5][2][2].getColor());
		myFaces[5][2][2].setColor(myFaces[3][0][0].getColor());
		myFaces[3][0][0].setColor(temp);

		temp = myFaces[0][1][2].getColor();
		myFaces[0][1][2].setColor(myFaces[1][1][2].getColor());
		myFaces[1][1][2].setColor(myFaces[5][1][2].getColor());
		myFaces[5][1][2].setColor(myFaces[3][1][0].getColor());
		myFaces[3][1][0].setColor(temp);

		temp = myFaces[0][0][2].getColor();
		myFaces[0][0][2].setColor(myFaces[1][0][2].getColor());
		myFaces[1][0][2].setColor(myFaces[5][0][2].getColor());
		myFaces[5][0][2].setColor(myFaces[3][2][0].getColor());
		myFaces[3][2][0].setColor(temp);
	}

	public void turnBlueInverse() {
		// if (!mySmall) mySmallCube.turnBlueInverse();
		turnBlue();
		turnBlue();
		turnBlue();
	}

	public void turnGreen() {
		if (!mySmall) mySmallCube.turnGreen();
		Color temp;

		turnFaceOnly(4);

		temp = myFaces[0][0][0].getColor();
		myFaces[0][0][0].setColor(myFaces[3][2][2].getColor());
		myFaces[3][2][2].setColor(myFaces[5][0][0].getColor());
		myFaces[5][0][0].setColor(myFaces[1][0][0].getColor());
		myFaces[1][0][0].setColor(temp);

		temp = myFaces[0][1][0].getColor();
		myFaces[0][1][0].setColor(myFaces[3][1][2].getColor());
		myFaces[3][1][2].setColor(myFaces[5][1][0].getColor());
		myFaces[5][1][0].setColor(myFaces[1][1][0].getColor());
		myFaces[1][1][0].setColor(temp);

		temp = myFaces[0][2][0].getColor();
		myFaces[0][2][0].setColor(myFaces[3][0][2].getColor());
		myFaces[3][0][2].setColor(myFaces[5][2][0].getColor());
		myFaces[5][2][0].setColor(myFaces[1][2][0].getColor());
		myFaces[1][2][0].setColor(temp);
	}

	public void turnGreenInverse() {
		// if (!mySmall) mySmallCube.turnGreenInverse();
		turnGreen();
		turnGreen();
		turnGreen();
	}

	public void turnOrange() {
		if (!mySmall) mySmallCube.turnOrange();
		Color temp;

		turnFaceOnly(3);

		temp = myFaces[0][0][2].getColor();
		myFaces[0][0][2].setColor(myFaces[2][2][2].getColor());
		myFaces[2][2][2].setColor(myFaces[5][2][0].getColor());
		myFaces[5][2][0].setColor(myFaces[4][0][0].getColor());
		myFaces[4][0][0].setColor(temp);

		temp = myFaces[0][0][1].getColor();
		myFaces[0][0][1].setColor(myFaces[2][1][2].getColor());
		myFaces[2][1][2].setColor(myFaces[5][2][1].getColor());
		myFaces[5][2][1].setColor(myFaces[4][1][0].getColor());
		myFaces[4][1][0].setColor(temp);

		temp = myFaces[0][0][0].getColor();
		myFaces[0][0][0].setColor(myFaces[2][0][2].getColor());
		myFaces[2][0][2].setColor(myFaces[5][2][2].getColor());
		myFaces[5][2][2].setColor(myFaces[4][2][0].getColor());
		myFaces[4][2][0].setColor(temp);
	}

	public void turnOrangeInverse() {
		// if (!mySmall) mySmallCube.turnOrangeInverse();
		turnOrange();
		turnOrange();
		turnOrange();
	}

	public void turnWhite() {
		if (!mySmall) mySmallCube.turnWhite();

		Color temp;

		turnFaceOnly(0);

		temp = myFaces[1][0][0].getColor();
		myFaces[1][0][0].setColor(myFaces[2][0][0].getColor());
		myFaces[2][0][0].setColor(myFaces[3][0][0].getColor());
		myFaces[3][0][0].setColor(myFaces[4][0][0].getColor());
		myFaces[4][0][0].setColor(temp);

		temp = myFaces[1][0][1].getColor();
		myFaces[1][0][1].setColor(myFaces[2][0][1].getColor());
		myFaces[2][0][1].setColor(myFaces[3][0][1].getColor());
		myFaces[3][0][1].setColor(myFaces[4][0][1].getColor());
		myFaces[4][0][1].setColor(temp);

		temp = myFaces[1][0][2].getColor();
		myFaces[1][0][2].setColor(myFaces[2][0][2].getColor());
		myFaces[2][0][2].setColor(myFaces[3][0][2].getColor());
		myFaces[3][0][2].setColor(myFaces[4][0][2].getColor());
		myFaces[4][0][2].setColor(temp);
	}

	public void turnWhiteInverse() {
		// if (!mySmall) mySmallCube.turnWhiteInverse();
		turnWhite();
		turnWhite();
		turnWhite();
	}

	public void turnYellow() {
		if (!mySmall) mySmallCube.turnYellow();
		Color temp;

		turnFaceOnly(5);

		temp = myFaces[1][2][2].getColor();
		myFaces[1][2][2].setColor(myFaces[4][2][2].getColor());
		myFaces[4][2][2].setColor(myFaces[3][2][2].getColor());
		myFaces[3][2][2].setColor(myFaces[2][2][2].getColor());
		myFaces[2][2][2].setColor(temp);

		temp = myFaces[1][2][1].getColor();
		myFaces[1][2][1].setColor(myFaces[4][2][1].getColor());
		myFaces[4][2][1].setColor(myFaces[3][2][1].getColor());
		myFaces[3][2][1].setColor(myFaces[2][2][1].getColor());
		myFaces[2][2][1].setColor(temp);

		temp = myFaces[1][2][0].getColor();
		myFaces[1][2][0].setColor(myFaces[4][2][0].getColor());
		myFaces[4][2][0].setColor(myFaces[3][2][0].getColor());
		myFaces[3][2][0].setColor(myFaces[2][2][0].getColor());
		myFaces[2][2][0].setColor(temp);
	}

	public void turnYellowInverse() {
		// if (!mySmall) mySmallCube.turnYellowInverse();
		turnYellow();
		turnYellow();
		turnYellow();
	}

	public void scramble() {
		Random rand = new Random();
		int randNum;
		for (int i = 0; i < 1000; i++) {
			randNum = rand.nextInt(6);
			turn(randNum, false);
			// System.out.println(randNum);
		}
		resetMoves();
	}

	public void setStep(double theStep) {
		if (!mySmall) mySmallCube.setStep(theStep);
		myStep = theStep;
	}

	private double degreesToRadians(double theDegrees) {
		return theDegrees * Math.PI / 180;
	}

	private void turnFaceOnly(int theFace) {

		Color temp;
		temp = myFaces[theFace][0][0].getColor();
		myFaces[theFace][0][0].setColor(myFaces[theFace][2][0].getColor());
		myFaces[theFace][2][0].setColor(myFaces[theFace][2][2].getColor());
		myFaces[theFace][2][2].setColor(myFaces[theFace][0][2].getColor());
		myFaces[theFace][0][2].setColor(temp);

		temp = myFaces[theFace][0][1].getColor();
		myFaces[theFace][0][1].setColor(myFaces[theFace][1][0].getColor());
		myFaces[theFace][1][0].setColor(myFaces[theFace][2][1].getColor());
		myFaces[theFace][2][1].setColor(myFaces[theFace][1][2].getColor());
		myFaces[theFace][1][2].setColor(temp);
	}

	public void click(int theX, int theY) {
		Face closestFace = null;
		int closestI = 2, closestJ = 2, closestF = 2;
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < myDimensions; i++) {
				for (int j = 0; j < myDimensions; j++) {
					if (myFaces[face][i][j].containsPoint(theX, theY)) {
						if (closestFace == null) {
							closestF = face;
							closestI = i;
							closestJ = j;
							closestFace = myFaces[face][i][j];
							// System.out.println("Face: " + face + " ; i: " + i + " ; j " + j);
						}
						if (closestFace.getAbsoluteZ() < myFaces[face][i][j].getAbsoluteZ()) {
							closestFace = myFaces[face][i][j];
							closestF = face;
							closestI = i;
							closestJ = j;
						}
					}
				}
			}
		}
		if (closestFace != null && !(closestI == 1 && closestJ == 1)) {
			closestFace.select();
			Face[][][] temp = mySmallCube.getFaces();
			temp[closestF][closestI][closestJ].select();
		}
	}

	public void scale(String theScaling) {
		if (!mySmall) mySmallCube.scale(theScaling);
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < myDimensions; i++) {
				for (int j = 0; j < myDimensions; j++) {
					myFaces[face][i][j].scale(theScaling);
				}
			}
		}
	}

	public Face[][][] getFaces() {
		return myFaces;
	}

	public double getPercentSolved() {
		double total = 0;
		double faceTotal = 0;
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < myDimensions; i++) {
				for (int j = 0; j < myDimensions; j++) {
					if (myFaces[face][i][j].getColorInt() == face) faceTotal++;
				}
			}
			total += faceTotal;
			faceTotal = 0;
		}
		return 100 * total / 54;
	}

	public void draw(Graphics2D g) {


		Face[] sortedFaces = new Face[6 * myDimensions * myDimensions];
		int index = 0;
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < myDimensions; i++) {
				for (int j = 0; j < myDimensions; j++) {
					sortedFaces[index++] = myFaces[face][i][j];
				}
			}
		}

		Arrays.sort(sortedFaces);

		for (Face face : sortedFaces) {
			face.draw(g, mySmall);
		}
		if (!mySmall) mySmallCube.draw(g);

	}

	public Cube copy() {
		Face[][][] thisFaces = getFaces();
		Face[][][] tempFaces = new Face[6][myDimensions][myDimensions];
		for (int faces = 0; faces < 6; faces++) {
			for (int i = 0; i < myDimensions; i++) {
				for (int j = 0; j < myDimensions; j++) {
					tempFaces[faces][i][j] = thisFaces[faces][i][j].copy();
				}
			}
		}
		Cube tempCube = new Cube(0, 0, 0, 0, myDimensions, false);
		tempCube.setFaces(tempFaces);
		return tempCube;
	}
}
