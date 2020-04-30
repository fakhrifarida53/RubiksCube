import java.util.LinkedList;

public class CubeSolver {
	public static void solveWhiteCross(Cube theCube) {
		int impossibleCount = 0;
		Face[][][]  tempFace = theCube.getFaces();
		// W:0, R:1, B:2, O:3, G:4, Y:5
		// While the white cross does not exist.
		while (!(tempFace[0][0][1].getColorInt() == 0
			&& tempFace[0][1][0].getColorInt() == 0
			&& tempFace[0][1][2].getColorInt() == 0
			&& tempFace[0][2][1].getColorInt() == 0)) {
			if (impossibleCount == 50) {
				System.out.println("solveWhiteCross() -- FAIL");
				return;
			}

			//rotate through each face connection to white
			for (int face = 1; face <= 5; face++) {
				tempFace = theCube.getFaces();
				if (face == 1) { // Red Face
					// Check for white on right edge
					if (tempFace[face][1][0].getColorInt() == 0) {
						if (openWhite(theCube, 1, 0)) theCube.turn(4, true);
					}
					// Check for white on left edge
					else if (tempFace[face][1][2].getColorInt() == 0) {
						if (openWhite(theCube, 1, 2)) theCube.turn(2, false);
					} // Check for white on top edge
					else if (tempFace[face][0][1].getColorInt() == 0) {
						theCube.turn(face, true);
						theCube.turn(4, true);
						theCube.turn(face, false);
					} // Check for white on bottom edge
					else if (tempFace[face][2][1].getColorInt() == 0) {
						theCube.turn(face, false);
						theCube.turn(4, true);
						theCube.turn(face, true);
					}

				} else if (face == 2) { // Blue Face
					// Check for white on right edge
					if (tempFace[face][1][0].getColorInt() == 0) {
						if (openWhite(theCube, 2, 1)) theCube.turn(1, true);
					}
					// Check for white on left edge
					else if (tempFace[face][1][2].getColorInt() == 0) {
						if (openWhite(theCube, 0, 1)) theCube.turn(3, false);
					} // Check for white on top edge
					else if (tempFace[face][0][1].getColorInt() == 0) {
						theCube.turn(face, true);
						theCube.turn(1, true);
						theCube.turn(face, false);
					} // Check for white on bottom edge
					else if (tempFace[face][2][1].getColorInt() == 0) {
						theCube.turn(face, false);
						theCube.turn(1, true);
						theCube.turn(face, true);
					}

				} else if (face == 3) { // Orange Face
					// Check for white on right edge
					if (tempFace[face][1][0].getColorInt() == 0) {
						if (openWhite(theCube, 1, 2)) theCube.turn(2, true);
					}
					// Check for white on left edge
					else if (tempFace[face][1][2].getColorInt() == 0) {
						if (openWhite(theCube, 1, 0)) theCube.turn(4, false);
					} // Check for white on top edge
					else if (tempFace[face][0][1].getColorInt() == 0) {
						theCube.turn(face, true);
						theCube.turn(2, true);
						theCube.turn(face, false);
					} // Check for white on bottom edge
					else if (tempFace[face][2][1].getColorInt() == 0) {
						theCube.turn(face, false);
						theCube.turn(2, true);
						theCube.turn(face, true);
					}
				} else if (face == 4) { // Green Face
					// Check for white on right edge
					if (tempFace[face][1][0].getColorInt() == 0) {
						if (openWhite(theCube, 0, 1)) theCube.turn(3, true);
					}
					// Check for white on left edge
					else if (tempFace[face][1][2].getColorInt() == 0) {
						if (openWhite(theCube, 2, 1)) theCube.turn(1, false);
					} // Check for white on top edge
					else if (tempFace[face][0][1].getColorInt() == 0) {
						theCube.turn(face, true);
						theCube.turn(3, true);
						theCube.turn(face, false);
					} // Check for white on bottom edge
					else if (tempFace[face][2][1].getColorInt() == 0) {
						theCube.turn(face, false);
						theCube.turn(3, true);
						theCube.turn(face, true);
					}
				}  else if (face == 5) { // Yellow Face
					if (tempFace[5][0][1].getColorInt() == 0) {
						if (openWhite(theCube, 2, 1)) {
							theCube.turn(1, true);
							theCube.turn(1, true);
						}
					} else if (tempFace[5][2][1].getColorInt() == 0) {
						if (openWhite(theCube, 0, 1)) {
							theCube.turn(3, true);
							theCube.turn(3, true);
						}
					} else if (tempFace[5][1][0].getColorInt() == 0) {
						if (openWhite(theCube, 1, 0)) {
							theCube.turn(4, true);
							theCube.turn(4, true);
						}
					} else if (tempFace[5][1][2].getColorInt() == 0) {
						if (openWhite(theCube, 1, 2)) {
							theCube.turn(2, true);
							theCube.turn(2, true);
						}
					}
					impossibleCount++;
				}
			}
		}
	}

	public static void cleanWhiteCross(Cube theCube) {
		int impossibleCount = 0;
		Face[][][]  tempFaces = theCube.getFaces();
		while (!(tempFaces[1][0][1].getColorInt() == 1
			&& tempFaces[2][0][1].getColorInt() == 2
			&& tempFaces[3][0][1].getColorInt() == 3
			&& tempFaces[4][0][1].getColorInt() == 4)) {
			if (impossibleCount == 50) {
				System.out.println("cleanWhiteCross() -- FAIL");
				return;
			}
			tempFaces = theCube.getFaces();
			if (tempFaces[1][0][1].getColorInt() == 1 && tempFaces[2][0][1].getColorInt() == 2) {
				flipWhites(theCube, 3, 4);
			}
			else if (tempFaces[2][0][1].getColorInt() == 2 && tempFaces[3][0][1].getColorInt() == 3) {
				flipWhites(theCube, 1, 4);
			}
			else if(tempFaces[3][0][1].getColorInt() == 3 && tempFaces[4][0][1].getColorInt() == 4) {
				flipWhites(theCube, 1, 2);
			}
			else if (tempFaces[4][0][1].getColorInt() == 4 && tempFaces[1][0][1].getColorInt() == 1) {
				flipWhites(theCube, 2, 3);
			}
			else if (tempFaces[1][0][1].getColorInt() == 1 && tempFaces[3][0][1].getColorInt() == 3) {
				flipWhites(theCube, 2, 4);
			}
			else if (tempFaces[2][0][1].getColorInt() == 2 && tempFaces[4][0][1].getColorInt() == 4) {
				flipWhites(theCube, 1, 3);
			} else theCube.turn(0, false);
			impossibleCount++;
		}
	}

	public static void solveWhiteCorners(Cube theCube) {
		int impossibleCount = 0;
		Face[][][] tempFaces = theCube.getFaces();

		// Loop until all corners are in the correct socket and orientation

		while (!(checkTopCorner(tempFaces, 1, 2, 0, 1, 2, 0) && tempFaces[0][2][2].getColorInt() == 0
			&& checkTopCorner(tempFaces, 2, 3, 0, 2, 3, 0) && tempFaces[0][0][2].getColorInt() == 0
			&& checkTopCorner(tempFaces, 3, 4, 0, 3, 4, 0) && tempFaces[0][0][0].getColorInt() == 0
			&& checkTopCorner(tempFaces, 4, 1, 0, 4, 1, 0) && tempFaces[0][2][0].getColorInt() == 0)) {
			if (impossibleCount == 50) {
				System.out.println("solveWhiteCorners() -- FAIL");
				return;
			}

			tempFaces = theCube.getFaces();
		// Check for corners in the correct socket, but wrong orientations
		//		- Corner Rotation until it is in the correct orientation

			// Checking Red/Blue/White for misoriented corner
			if (checkTopCorner(tempFaces, 1, 2, 0, 1, 2, 0) && tempFaces[0][2][2].getColorInt() != 0) {
				while (tempFaces[0][2][2].getColorInt() != 0) {
					cornerRotation(theCube, 5, 2);
					if (impossibleCount++ == 50) {
						System.out.println("solveWhiteCorners() -- FAIL");
						return;
					}
				}
			}
			// Checking Blue/Orange/White for misoriented corner
			else if (checkTopCorner(tempFaces, 2, 3, 0, 2, 3, 0) && tempFaces[0][0][2].getColorInt() != 0) {
				while (tempFaces[0][0][2].getColorInt() != 0) {
					cornerRotation(theCube, 5, 3);
					if (impossibleCount++ == 50) {
						System.out.println("solveWhiteCorners() -- FAIL");
						return;
					}
				}
			}
			// Checking Orange/Green/White for misoriented corner
			else if (checkTopCorner(tempFaces, 3, 4, 0, 3, 4, 0) && tempFaces[0][0][0].getColorInt() != 0) {
				while (tempFaces[0][0][0].getColorInt() != 0) {
					cornerRotation(theCube, 5, 4);
					if (impossibleCount++ == 50) {
						System.out.println("solveWhiteCorners() -- FAIL");
						return;
					}
				}
			}
			// Checking Green/Red/White for misoriented corner
			else if (checkTopCorner(tempFaces, 4, 1, 0, 4, 1, 0) && tempFaces[0][2][0].getColorInt() != 0) {
				while (tempFaces[0][2][0].getColorInt() != 0) {
					cornerRotation(theCube, 5, 1);
					if (impossibleCount++ == 50) {
						System.out.println("solveWhiteCorners() -- FAIL");
						return;
					}
				}
			}

		// Check for corners below the correct socket
		//		- Corner rotation until it is in the correct orientation

			// Checking if Red/Blue/White corner is below the proper socket
			else if (checkBottomCorner(tempFaces, 1, 2, 5, 1, 2, 0)) {
				while (!checkTopCorner(tempFaces, 1, 2, 0, 1, 2, 0) || tempFaces[0][2][2].getColorInt() != 0) {
					cornerRotation(theCube, 5, 2);
					if (impossibleCount++ == 50) {
						System.out.println("solveWhiteCorners() -- FAIL");
						return;
					}
				}
			}

			// Checking if Blue/Orange/White corner is below the proper socket
			else if (checkBottomCorner(tempFaces, 2, 3, 5, 2, 3, 0)) {
				while (!checkTopCorner(tempFaces, 2, 3, 0, 2, 3, 0) || tempFaces[0][0][2].getColorInt() != 0) {
					cornerRotation(theCube, 5, 3);
					if (impossibleCount++ == 50) {
						System.out.println("solveWhiteCorners() -- FAIL");
						return;
					}
				}
			}

			// Checking if Orange/Green/White corner is below the proper socket
			else if (checkBottomCorner(tempFaces, 3, 4, 5, 3, 4, 0)) {
				while (!checkTopCorner(tempFaces, 3, 4, 0, 3, 4, 0) || tempFaces[0][0][0].getColorInt() != 0) {
					cornerRotation(theCube, 5, 4);
					if (impossibleCount++ == 50) {
						System.out.println("solveWhiteCorners() -- FAIL");
						return;
					}
				}
			}

			// Checking if Green/Red/White corner is below the proper socket
			else if (checkBottomCorner(tempFaces, 4, 1, 5, 4, 1, 0)) {
				while (!checkTopCorner(tempFaces, 4, 1, 0, 4, 1, 0) || tempFaces[0][2][0].getColorInt() != 0) {
					cornerRotation(theCube, 5, 1);
					if (impossibleCount++ == 50) {
						System.out.println("solveWhiteCorners() -- FAIL");
						return;
					}
				}
			}


		// Check for corners in the wrong socket that contain white
		//		- Corner rotation once

			// Checking to Red/Blue/White corner for invalid corner
			else if (!checkTopCorner(tempFaces, 1, 2, 0, 1, 2, 0) && topRightContainsWhite(tempFaces, 1)) {
				cornerRotation(theCube, 5, 2);
				theCube.turn(5, false);

			}

			// Checking to Blue/Orange/White corner for invalid corner
			else if (!checkTopCorner(tempFaces, 2, 3, 0, 2, 3, 0) && topRightContainsWhite(tempFaces, 2)) {
				cornerRotation(theCube, 5, 3);
				theCube.turn(5, false);
			}

			// Checking to Orange/Green/White corner for invalid corner
			else if (!checkTopCorner(tempFaces, 3, 4, 0, 3, 4, 0) && topRightContainsWhite(tempFaces, 3)) {
				cornerRotation(theCube, 5, 4);
				theCube.turn(5, false);
			}

			// Checking to Green/Red/White corner for invalid corner
			else if (!checkTopCorner(tempFaces, 4, 1, 0, 4, 1, 0) && topRightContainsWhite(tempFaces, 4)) {
				cornerRotation(theCube, 5, 1);
				theCube.turn(5, false);
			}

		// Rotate Yellow clockwise if all checks fail
			else theCube.turn(5, false);
			impossibleCount++;
		}
	}

	public static void solveLayerTwoEdges(Cube theCube) {
		int impossibleCount = 0;
		Face[][][] tempFaces = theCube.getFaces();
		// Loop while until all layer two edges are in the correct place
		while (tempFaces[1][1][0].getColorInt() != 1 || tempFaces[1][1][2].getColorInt() != 1
				|| tempFaces[2][1][0].getColorInt() != 2 || tempFaces[2][1][2].getColorInt() != 2
				|| tempFaces[3][1][0].getColorInt() != 3 || tempFaces[3][1][2].getColorInt() != 3
				|| tempFaces[4][1][0].getColorInt() != 4 || tempFaces[4][1][2].getColorInt() != 4) {
				if (impossibleCount == 50) {
					System.out.println("solveLayerTwoEdges() -- FAIL");
					return;
				}



			// Check to see if any bottom edges are available
			//	- Bottom center to left center or bottom center to right center

			if (tempFaces[1][2][1].getColorInt() == 1 && bottomCenterEdgeMatch(tempFaces, 1, 4, 2)) {
				if (tempFaces[5][0][1].getColorInt() == 4) bottomCenterToLeft(theCube, 1);
				else if (tempFaces[5][0][1].getColorInt() == 2) bottomCenterToRight(theCube, 1);
			}

			else if (tempFaces[2][2][1].getColorInt() == 2 && bottomCenterEdgeMatch(tempFaces, 2, 1, 3)) {
				if (tempFaces[5][1][2].getColorInt() == 1) bottomCenterToLeft(theCube, 2);
				else if (tempFaces[5][1][2].getColorInt() == 3) bottomCenterToRight(theCube, 2);
			}

			else if (tempFaces[3][2][1].getColorInt() == 3 && bottomCenterEdgeMatch(tempFaces, 3, 2, 4)) {
				if (tempFaces[5][2][1].getColorInt() == 2) bottomCenterToLeft(theCube, 3);
				else if (tempFaces[5][2][1].getColorInt() == 4) bottomCenterToRight(theCube, 3);
			}

			else if (tempFaces[4][2][1].getColorInt() == 4 && bottomCenterEdgeMatch(tempFaces, 4, 3, 1)) {
				if (tempFaces[5][1][0].getColorInt() == 3) bottomCenterToLeft(theCube, 4);
				else if (tempFaces[5][1][0].getColorInt() == 1) bottomCenterToRight(theCube, 4);
			}




			// If no possible piece exists on the bottom
			else if((tempFaces[1][2][1].getColorInt() == 5 || tempFaces[5][0][1].getColorInt() == 5)
					&& (tempFaces[2][2][1].getColorInt() == 5 || tempFaces[5][1][2].getColorInt() == 5)
					&& (tempFaces[3][2][1].getColorInt() == 5 || tempFaces[5][2][1].getColorInt() == 5)
					&& (tempFaces[4][2][1].getColorInt() == 5 || tempFaces[5][1][0].getColorInt() == 5)) {
				// Check for incorrect or disoriented edges on the right side of a face
				if (tempFaces[1][1][2].getColorInt() != 1 || tempFaces[2][1][0].getColorInt() != 2) {
					 bottomCenterToRight(theCube, 1);
				}

				else if (tempFaces[2][1][2].getColorInt() != 2 || tempFaces[3][1][0].getColorInt() != 3) {
					bottomCenterToRight(theCube, 2);
				}

				else if (tempFaces[3][1][2].getColorInt() != 3 || tempFaces[4][1][0].getColorInt() != 4) {
					 bottomCenterToRight(theCube, 3);
				}

				else if (tempFaces[4][1][2].getColorInt() != 4 || tempFaces[1][1][0].getColorInt() != 1) {
					 bottomCenterToRight(theCube, 4);
				}
			}

			else theCube.turn(5, false);
			impossibleCount++;
		}
	}

	public static void solveYellowCross(Cube theCube) {
		int impossibleCount = 0;
		Face[][][] tempFaces = theCube.getFaces();

		while (tempFaces[5][0][1].getColorInt() != 5 || tempFaces[5][1][0].getColorInt() != 5
				&& tempFaces[5][1][2].getColorInt() != 5 || tempFaces[5][2][1].getColorInt() != 5) {

			if (impossibleCount == 50) {
				System.out.println("solveYellowCross() -- FAIL");
				return;
			}

			tempFaces = theCube.getFaces();

			if (tempFaces[5][0][1].getColorInt() != 5 && tempFaces[5][1][0].getColorInt() != 5
				&& tempFaces[5][1][2].getColorInt() != 5 && tempFaces[5][2][1].getColorInt() != 5) {
				yellowCrossAlgorithm(theCube);
			}

			// Check if there is a yellow V

			else if (tempFaces[5][2][1].getColorInt() == 5 && tempFaces[5][1][2].getColorInt() == 5
				&& tempFaces[5][1][1].getColorInt() == 5) {
				yellowCrossAlgorithm(theCube);
			}

			// Check if there is a horizontal line

			else if (tempFaces[5][1][0].getColorInt() == 5 && tempFaces[5][1][1].getColorInt() == 5
				&& tempFaces[5][1][2].getColorInt() == 5) {
				yellowCrossAlgorithm(theCube);
			}

			// Otherwise, rotate yellow clockwise
			else theCube.turn(5, false);
			impossibleCount++;
		}
	}

	public static void cleanYellowCross(Cube theCube) {
		int impossibleCount = 0;
		Face[][][]  tempFaces = theCube.getFaces();
		while (tempFaces[1][2][1].getColorInt() != 1
			|| tempFaces[2][2][1].getColorInt() != 2
			|| tempFaces[3][2][1].getColorInt() != 3
			|| tempFaces[4][2][1].getColorInt() != 4) {

			if (impossibleCount == 50) {
				System.out.println("cleanYellowCross() -- FAIL");
				return;
			}

			tempFaces = theCube.getFaces();

			// Check to see if there are successfully set side pieces on the back and right
			if (tempFaces[4][2][1].getColorInt() == 4 && tempFaces[3][2][1].getColorInt() == 3) {
				// Edge Flip
				edgeFlip(theCube);
			}

			// Check to see if there are successfully set side pieces on the right and back
			if (tempFaces[3][2][1].getColorInt() == 3 && tempFaces[2][2][1].getColorInt() == 2) {
				theCube.turn(5, false);
				// Edge Flip
				edgeFlip(theCube);
			}

			// Check to see if there are successfully set side pieces on the back and left
			if (tempFaces[2][2][1].getColorInt() == 2 && tempFaces[1][2][1].getColorInt() == 1) {
				theCube.turn(5, false);
				theCube.turn(5, false);
				// Edge Flip
				edgeFlip(theCube);
			}

			// Check to see if there are successfully set side pieces on the left and front
			if (tempFaces[1][2][1].getColorInt() == 1 && tempFaces[4][2][1].getColorInt() == 4) {
				theCube.turn(5, true);
				// Edge Flip
				edgeFlip(theCube);
			}

			// Check to see if there are side pieces opposite of eachother but not fully cleaned
			else if (tempFaces[2][2][1].getColorInt() == 2 && tempFaces[4][2][1].getColorInt() == 4) {
				// Edge Flip
				edgeFlip(theCube);
			}

			// Otherwise, rotate the yellow face clockwise
			else theCube.turn(5, false);
			impossibleCount++;
		}
	}

	// Put the yellow corners into the correct sockets
	public static void socketYellowCorners(Cube theCube) {

		int impossibleCount = 0;

		Face[][][] theFaces = theCube.getFaces();
		// Loop while all sockets are not satisfied
		while (!(checkBottomCorner(theFaces, 1, 2, 5, 1, 2, 5)
				&& checkBottomCorner(theFaces, 2, 3, 5, 2, 3, 5)
				&& checkBottomCorner(theFaces, 3, 4, 5, 3, 4, 5)
				&& checkBottomCorner(theFaces, 4, 1, 5, 4, 1, 5))) {

				if (impossibleCount == 50) {
					System.out.println("socketYellowCorners() -- FAIL");
					return;
				}

			// Check front right for filled socket
			//	- resocket yellow corners
			if (checkBottomCorner(theFaces, 1, 2, 5, 1, 2, 5)) {
				theCube.turn(5, true);
				shiftYellowCorners(theCube);
			}

			// Check back right for filled socket
			//	- resocket yellow corners
			else if (checkBottomCorner(theFaces, 2, 3, 5, 2, 3, 5)) {
				theCube.turn(5, false);
				theCube.turn(5, false);
				shiftYellowCorners(theCube);
			}

			// Check back left for filled socket
			//	- resocket yellow corners
			else if (checkBottomCorner(theFaces, 3, 4, 5, 3, 4, 5)) {
				theCube.turn(5, false);
				shiftYellowCorners(theCube);
			}

			// Check front left for filled socket
			//	- resocket yellow corners
			else if (checkBottomCorner(theFaces, 4, 1, 5, 4, 1, 5)) {
				shiftYellowCorners(theCube);
			}

			// Otherwise turn yellow face clockwise
			else {
				shiftYellowCorners(theCube);
			}
			impossibleCount++;
		}
	}

	// orient the final corners
	public static void orientFinalCorners(Cube theCube) {
		int impossibleCount = 0;
		Face[][][] tempFaces = theCube.getFaces();
		while (theCube.getPercentSolved() != 100) {
			if (impossibleCount == 50) {
					System.out.println("orientFinalCorners() -- FAIL");
					return;
			}
			while (tempFaces[5][0][0].getColorInt() != 5) {
				cornerRotation(theCube, 0, 4);
			}
			impossibleCount++;

			theCube.turn(5, false);
		}
	}

	// Removed redundancy in the list of moves
	public LinkedList<String> removeRedundancy(LinkedList<String> theList) {
		String s1 = null, s2 = null, s3 = null, s4 = null;
		int i1 = 0, i2 = -1, i3 = -2, i4 = -3;

		for (String s : theList) {
			s4 = s3;
			i4 = i3;
			s3 = s2;
			i3 = i2;
			s2 = s1;
			i2 = i1;
			s1 = s;
			i1++;
			if (s1.equals(s2) && s2.equals(s3) && s3.equals(s4)) {
				theList.remove(i4);
				theList.remove(i3);
				theList.remove(i2);
				theList.remove(i1);
				s4 = null;
				s3 = null;
				s2 = null;
				s1 = null;
				i1 -= 4;
				i2 -= 4;
				i3 -= 4;
				i4 -= 4;
			} else if (!s1.equals(s2) && s2.equals(s3) && s3.equals(s4)) {
				theList.remove(i4);
				theList.remove(i3);
				theList.remove(i2);
				if (s2.contains("i")) {
					theList.add(i2, s2.substring(0, s2.length() - 1));
				}

				s4 = null;
				s3 = null;
				s2 = null;
				s1 = null;
				i1 -= 2;
				i2 -= 2;
				i3 -= 2;
				i4 -= 2;
			}
		}
		return theList;
	}

	// Resocket the yellow corners , rotateing the back right, back left, and front left
	// U R Ui Li U Ri Ui L
	private static void shiftYellowCorners(Cube theCube) {
		int impossibleCount = 0;
		theCube.turn(5, false);
		theCube.turn(4, false);
		theCube.turn(5, true);
		theCube.turn(2, true);
		theCube.turn(5, false);
		theCube.turn(4, true);
		theCube.turn(5, true);
		theCube.turn(2, false);
		Face[][][] tempFaces = theCube.getFaces();
		while(tempFaces[1][2][1].getColorInt() != 1) {
			theCube.turn(5, true);
			if (impossibleCount++ == 50) {
				System.out.println("shiftYellowCorners() -- FAIL");
				return;
			}
		}
	}

	// Flips the bottom front and left yellow edges
	// R U Ri U R U U Ri
	private static void edgeFlip(Cube theCube) {
		theCube.turn(4, false);
		theCube.turn(5, false);
		theCube.turn(4, true);
		theCube.turn(5, false);
		theCube.turn(4, false);
		theCube.turn(5, false);
		theCube.turn(5, false);
		theCube.turn(4,true);
	}

	// Dot, Line, V, Cross algorthm, only in terms of Red : 1
	// F R U Ri Ui Fi
	private static void yellowCrossAlgorithm(Cube theCube) {
		theCube.turn(1, false); // F - Red
		theCube.turn(4, false); // R - Green
		theCube.turn(5, false); // U - Yellow
		theCube.turn(4, true); // Ri - Green Inverted
		theCube.turn(5, true); // Ui - Yellow Inverted
		theCube.turn(1, true); // Fi - Red Inverted
	}

	private static boolean bottomCenterEdgeMatch(Face[][][] theFaces, int theF, int color1, int color2) {
		if (theF == 1) {
			return (theFaces[5][0][1].getColorInt() == color1
			|| theFaces[5][0][1].getColorInt() == color2);
		} else if (theF == 2) {
			return (theFaces[5][1][2].getColorInt() == color1
			|| theFaces[5][1][2].getColorInt() == color2);
		} else if (theF == 3) {
			return (theFaces[5][2][1].getColorInt() == color1
			|| theFaces[5][2][1].getColorInt() == color2);
		} else if (theF == 4) {
			return (theFaces[5][1][0].getColorInt() == color1
			|| theFaces[5][1][0].getColorInt() == color2);
		}
		return false;
	}

	// Ui Li U L U F Ui Fi
	private static void bottomCenterToRight(Cube theCube, int theF) {
		int l = -1;
		if (theF == 1) l = 2; // 2
		else if (theF == 2) l = 3; // 3
		else if (theF == 3) l = 4; // 4
		else if (theF == 4) l = 1; // 1
		theCube.turn(5, true); // Ui
		theCube.turn(l, true); // Li
		theCube.turn(5, false); // U
		theCube.turn(l, false); // L
		theCube.turn(5, false); // U
		theCube.turn(theF, false); // F
		theCube.turn(5, true); // Ui
		theCube.turn(theF, true); // Fi
	}

	// U R Ui Ri Ui Fi U F
	private static void bottomCenterToLeft(Cube theCube, int theF) {
		int r = -1;
		if (theF == 1) r = 4;
		else if (theF == 2) r = 1;
		else if (theF == 3) r = 2;
		else if (theF == 4) r = 3;
		theCube.turn(5, false); // U
		theCube.turn(r, false); // R
		theCube.turn(5, true); // Ui
		theCube.turn(r, true); // Ri
		theCube.turn(5, true); // Ui
		theCube.turn(theF, true); // Fi
		theCube.turn(5, false); // U
		theCube.turn(theF, false); // F
	}

	private static void flipWhites(Cube theCube, int theColor1, int theColor2) {
		int impossibleCount = 0;
		theCube.turn(theColor1, false);
		theCube.turn(theColor1, false);
		theCube.turn(theColor2, false);
		theCube.turn(theColor2, false);
		int tempSide = theColor1;
		while (tempSide != theColor2) {
			if (tempSide == 4) tempSide = 1;
			else tempSide++;
			theCube.turn(5, false);
			if (impossibleCount++ == 50) {
				System.out.println("solveLayerTwoEdges() -- FAIL");
			return;
			}
		}
		theCube.turn(theColor2, false);
		theCube.turn(theColor2, false);
		while (tempSide != theColor1) {
			if (tempSide == 1) tempSide = 4;
			else tempSide--;
			theCube.turn(5, true);
			if (impossibleCount++ == 50) {
				System.out.println("solveLayerTwoEdges() -- FAIL");
				return;
			}
		}



		tempSide = theColor2;
		while (tempSide != theColor1) {
			if (tempSide == 4) tempSide = 1;
			else tempSide++;
			theCube.turn(5, false);
			if (impossibleCount++ == 50) {
				System.out.println("solveLayerTwoEdges() -- FAIL");
				return;
			}
		}
		theCube.turn(theColor1, false);
		theCube.turn(theColor1, false);
	}

	private static boolean openWhite(Cube theCube, int i, int j) {
		Face[][][] tempFace;
		for (int count = 0; count < 4; count++) {
			tempFace = theCube.getFaces();
			if (tempFace[0][i][j].getColorInt() != 0) return true;
			else {
				theCube.turn(0, false);
			}
		}
		return false;

	}

	private static void cornerRotation(Cube theCube, int theD, int theR) {
		theCube.turn(theR, true); // Right Inverted
		theCube.turn(theD, true); // Down Inverted
		theCube.turn(theR, false); // Right
		theCube.turn(theD, false); // Down
	}

	// Checks to see if a corner contains white
	private static boolean topRightContainsWhite(Face[][][] theFaces, int theF) {
		if (theF == 1) {
			return theFaces[1][0][2].getColorInt() == 0
			|| theFaces[2][0][0].getColorInt() == 0
			|| theFaces[0][2][2].getColorInt() == 0;
		} else if (theF == 2) {
			return theFaces[2][0][2].getColorInt() == 0
			|| theFaces[3][0][0].getColorInt() == 0
			|| theFaces[0][0][2].getColorInt() == 0;
		} else if (theF == 3) {
			return theFaces[3][0][2].getColorInt() == 0
			|| theFaces[4][0][0].getColorInt() == 0
			|| theFaces[0][0][0].getColorInt() == 0;
		} else if (theF == 2) {
			return theFaces[4][0][2].getColorInt() == 0
			|| theFaces[1][0][0].getColorInt() == 0
			|| theFaces[0][2][0].getColorInt() == 0;
		}
		return false;
	}

	// Checks the top right corner of the front face
	public static boolean checkTopCorner(Face[][][] theFaces, int theF, int theR, int theT, int theColor1, int theColor2, int theColor3) {
		if (theF == 1 && theT == 0) {
			return (theFaces[theF][0][2].getColorInt() == theColor1
				|| theFaces[theF][0][2].getColorInt() == theColor2
				|| theFaces[theF][0][2].getColorInt() == theColor3)
			&& (theFaces[theR][0][0].getColorInt() == theColor1
				|| theFaces[theR][0][0].getColorInt() == theColor2
				|| theFaces[theR][0][0].getColorInt() == theColor3)
			&& (theFaces[theT][2][2].getColorInt() == theColor1
				|| theFaces[theT][2][2].getColorInt() == theColor2
				|| theFaces[theT][2][2].getColorInt() == theColor3);
		} else if (theF == 2 && theT == 0) {
			return (theFaces[theF][0][2].getColorInt() == theColor1
				|| theFaces[theF][0][2].getColorInt() == theColor2
				|| theFaces[theF][0][2].getColorInt() == theColor3)
			&& (theFaces[theR][0][0].getColorInt() == theColor1
				|| theFaces[theR][0][0].getColorInt() == theColor2
				|| theFaces[theR][0][0].getColorInt() == theColor3)
			&& (theFaces[theT][0][2].getColorInt() == theColor1
				|| theFaces[theT][0][2].getColorInt() == theColor2
				|| theFaces[theT][0][2].getColorInt() == theColor3);
		} else if (theF == 3 && theT == 0) {
			return (theFaces[theF][0][2].getColorInt() == theColor1
				|| theFaces[theF][0][2].getColorInt() == theColor2
				|| theFaces[theF][0][2].getColorInt() == theColor3)
			&& (theFaces[theR][0][0].getColorInt() == theColor1
				|| theFaces[theR][0][0].getColorInt() == theColor2
				|| theFaces[theR][0][0].getColorInt() == theColor3)
			&& (theFaces[theT][0][0].getColorInt() == theColor1
				|| theFaces[theT][0][0].getColorInt() == theColor2
				|| theFaces[theT][0][0].getColorInt() == theColor3);
		} else if (theF == 4 && theT == 0) {
			return (theFaces[theF][0][2].getColorInt() == theColor1
				|| theFaces[theF][0][2].getColorInt() == theColor2
				|| theFaces[theF][0][2].getColorInt() == theColor3)
			&& (theFaces[theR][0][0].getColorInt() == theColor1
				|| theFaces[theR][0][0].getColorInt() == theColor2
				|| theFaces[theR][0][0].getColorInt() == theColor3)
			&& (theFaces[theT][2][0].getColorInt() == theColor1
				|| theFaces[theT][2][0].getColorInt() == theColor2
				|| theFaces[theT][2][0].getColorInt() == theColor3);
		}
		return false;
	}

	// Checks the bottom right corner of the front face
	public static boolean checkBottomCorner(Face[][][] theFaces, int theF, int theR, int theD, int theColor1, int theColor2, int theColor3) {
		if (theF == 1 && theD == 5) {
			return (theFaces[theF][2][2].getColorInt() == theColor1
				|| theFaces[theF][2][2].getColorInt() == theColor2
				|| theFaces[theF][2][2].getColorInt() == theColor3)
			&& (theFaces[theR][2][0].getColorInt() == theColor1
				|| theFaces[theR][2][0].getColorInt() == theColor2
				|| theFaces[theR][2][0].getColorInt() == theColor3)
			&& (theFaces[theD][0][2].getColorInt() == theColor1
				|| theFaces[theD][0][2].getColorInt() == theColor2
				|| theFaces[theD][0][2].getColorInt() == theColor3);
		} else if (theF == 2 && theD == 5) {
			return (theFaces[theF][2][2].getColorInt() == theColor1
				|| theFaces[theF][2][2].getColorInt() == theColor2
				|| theFaces[theF][2][2].getColorInt() == theColor3)
			&& (theFaces[theR][2][0].getColorInt() == theColor1
				|| theFaces[theR][2][0].getColorInt() == theColor2
				|| theFaces[theR][2][0].getColorInt() == theColor3)
			&& (theFaces[theD][2][2].getColorInt() == theColor1
				|| theFaces[theD][2][2].getColorInt() == theColor2
				|| theFaces[theD][2][2].getColorInt() == theColor3);
		} else if (theF == 3 && theD == 5) {
			return (theFaces[theF][2][2].getColorInt() == theColor1
				|| theFaces[theF][2][2].getColorInt() == theColor2
				|| theFaces[theF][2][2].getColorInt() == theColor3)
			&& (theFaces[theR][2][0].getColorInt() == theColor1
				|| theFaces[theR][2][0].getColorInt() == theColor2
				|| theFaces[theR][2][0].getColorInt() == theColor3)
			&& (theFaces[theD][2][0].getColorInt() == theColor1
				|| theFaces[theD][2][0].getColorInt() == theColor2
				|| theFaces[theD][2][0].getColorInt() == theColor3);
		} else if (theF == 4 && theD == 5) {
			return (theFaces[theF][2][2].getColorInt() == theColor1
				|| theFaces[theF][2][2].getColorInt() == theColor2
				|| theFaces[theF][2][2].getColorInt() == theColor3)
			&& (theFaces[theR][2][0].getColorInt() == theColor1
				|| theFaces[theR][2][0].getColorInt() == theColor2
				|| theFaces[theR][2][0].getColorInt() == theColor3)
			&& (theFaces[theD][0][0].getColorInt() == theColor1
				|| theFaces[theD][0][0].getColorInt() == theColor2
				|| theFaces[theD][0][0].getColorInt() == theColor3);
		}
		return false;
	}

	private static void errorPrint(Face[][][] theFaces) {
		for (int face = 0; face < 6; face++) {
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					System.out.print(theFaces[face][i][j].getColorInt());
				}
				System.out.println();
			}
			System.out.println();
		}

	}
}
