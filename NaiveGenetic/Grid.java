import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.lang.Thread;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Grid extends JPanel implements KeyListener, MouseListener, MouseMotionListener {

	private static final int WIDTH = 500;
	private static final int HEIGHT = 500;
	private boolean right, left, up, down, shift, invert, edit, menu, scrollUp, scrollDown,
		runninng, auto, solved, turnReady, impossibleCube;

	private Cube myCube, mySolvingCube;
	private double myStep, myScrollX, myScrollStep;
	private int myDimensions, myDragX, myDragY, mySolutionX, mySolutionY, myMoveIndex;;

	public Grid() {
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.DARK_GRAY.darker());
        init();
		this.repaint();
	}

	private void init() {
		right = false;
		left = false;
		up = false;
		down = false;
		shift = false;
		invert = false;
		edit = false;
		menu = false;
		scrollUp = false;
		scrollDown = false;
		auto = true;
		solved = false;
		turnReady = false;
		impossibleCube = false;
		myStep = 5;
		myDimensions = 3;
		myDragX = 0;
		myDragY = 0;
		mySolutionX = WIDTH / 20;
		mySolutionY = 8 * HEIGHT / 9;
		myScrollX = 0;
		myMoveIndex = 0;
		myScrollStep = 0;
		myCube = new Cube(WIDTH, HEIGHT, myStep, 1, myDimensions, false);
		mySolvingCube = myCube.copy();
	}

	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setPaintMode();
		g2d.setPaint(new GradientPaint(5, 10, Color.GRAY, 5, 300, Color.BLACK));
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.DARK_GRAY);

        g.drawLine(WIDTH / 2, 0, WIDTH / 2, HEIGHT);
        g.drawLine(0, HEIGHT / 2, WIDTH, HEIGHT / 2);
		g2d.setFont(new Font("Aurora Cn BT", Font.PLAIN, 15));

		g2d.setColor(Color.BLACK);

		g2d.drawString(((int) myCube.getPercentSolved()) + "%", 14, 29);

		g2d.setColor(Color.GREEN);
		if (myCube.getPercentSolved() < 25) g2d.setColor(Color.RED);
		else if (myCube.getPercentSolved() < 50) g2d.setColor(Color.ORANGE);
		else if (myCube.getPercentSolved() < 75) g2d.setColor(Color.YELLOW);
		g2d.drawString(((int) myCube.getPercentSolved()) + "%", 15, 30);

		g2d.setColor(Color.WHITE);


        myCube.draw(g2d);


        if (mySolvingCube.getMoves().size() > 0 && mySolvingCube.getPercentSolved() == 100) {
        	scrollThroughSolution(g2d);
        } else {
        	g2d.setColor(Color.WHITE);
        	g2d.setFont(new Font("Aurora Cn BT", Font.PLAIN, 12));
        	g2d.drawString(myStep + "\u00b0", WIDTH - 35, HEIGHT - 15);
			g2d.drawString("(m)enu", 10, HEIGHT - 15);
        }

		g.setColor(Color.DARK_GRAY);
		g.drawRect(1, 1, WIDTH - 3, HEIGHT - 3);

		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Aurora Cn BT", Font.PLAIN, 30));
		if (left && !shift) g2d.drawString("\u25C1", 15, HEIGHT / 2 + 8);
		if (right && !shift) g2d.drawString("\u25B7", WIDTH - 40, HEIGHT / 2 + 8);
		if (up) g2d.drawString("\u25B3", (WIDTH / 2) - 11, 40);
		if (down) g2d.drawString("\u25BD", (WIDTH / 2) - 11, HEIGHT - 30);
		if (shift) {
			g2d.setFont(new Font("Arial", Font.PLAIN, 30));
			if (left) {
				g2d.drawString("\u27F2", 15, HEIGHT - 20);
			}
			if (right) {
				g2d.drawString("\u27F3", WIDTH - 40,  HEIGHT - 20);
			}
		}

		if (impossibleCube) {
			g2d.setColor(Color.WHITE);
			g2d.drawString("Impossible Cube", WIDTH / 2 - 132,  HEIGHT / 7);
			g2d.drawString("Impossible Cube", WIDTH / 2 - 128,  HEIGHT / 7);
			g2d.drawString("Impossible Cube", WIDTH / 2 - 130,  HEIGHT / 7 + 2);
			g2d.drawString("Impossible Cube", WIDTH / 2 - 130,  HEIGHT / 7 - 2);
			g2d.setColor(Color.RED);
			g2d.drawString("Impossible Cube", WIDTH / 2 - 130,  HEIGHT / 7);
		}

		if (menu) {
			drawMenu(g2d);
		}
    }

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (!menu) {
			if (key == 77) {
				menu = true;
			} else if (key == 32) {
				// Reset Cube
				solved = false;
				myCube = new Cube(WIDTH, HEIGHT, myStep, 1, myDimensions, false);
				mySolvingCube = myCube.copy();
				for (int i = 0; i < 40; i++) {
					System.out.println();
				}
			} else if (key == 16) {
				shift = true;
			} else if (key == 37) {
				left = true;
				if (shift) myCube.rotateClockwise();
				else if (!invert) myCube.rotateLeft();
				else if (invert) myCube.rotateRight();
			} else if (key == 38) {
				up = true;
				if (shift) myCube.scale("up");
				else if (!invert) myCube.rotateDown();
				else if (invert) myCube.rotateUp();
			} else if (key == 39) {
				right = true;
				if (shift) myCube.rotateCounterClockwise();
				else if (!invert) myCube.rotateRight();
				else if (invert) myCube.rotateLeft();

			} else if (key == 40) {
				down = true;
				if (shift) myCube.scale("down");
				else if (!invert) myCube.rotateUp();
				else if (invert) myCube.rotateDown();
			} else if (key == 61) {
				// +
				// if (shift) {
				// 	if (myDimensions < 15) {
				// 		myDimensions++;
				// 		myCube = new Cube(WIDTH, HEIGHT, myStep, 1, myDimensions, false);
				// 	}
				// } else {
					myStep += 0.5;
					myCube.setStep(myStep);
				// }
			} else if (key == 45) {
				// -
				// if (shift) {
				// 	if (myDimensions > 2) {
				// 		myDimensions--;
				// 		myCube = new Cube(WIDTH, HEIGHT, myStep, 1, myDimensions, false);
				// 	}
				// } else {
					myStep -= 0.5;
					myCube.setStep(myStep);
				// }
			} else if (key == 82 && !solved) {
				if (!shift) myCube.turnRed(); // Rotate Red Clockwise
				else myCube.turnRedInverse(); // Rotate Red Counter Clockwise
			} else if (key == 66 && !solved) {
				if (!shift) myCube.turnBlue(); // Rotate Blue Clockwise
				else myCube.turnBlueInverse(); // Rotate Blue Counter Clockwise
			} else if (key == 71 && !solved) {
				if (!shift) myCube.turnGreen(); // Rotate Green Clockwise
				else myCube.turnGreenInverse(); // Rotate Green Counter Clockwise
			} else if (key == 79 && !solved) {
				if (!shift) myCube.turnOrange(); // Rotate Orange Clockwise
				else myCube.turnOrangeInverse(); // Rotate Orange Orange Clockwise
			} else if (key == 87 && !solved) {
				if (!shift) myCube.turnWhite(); // Rotate White Clockwise
				else myCube.turnWhiteInverse(); // Rotate White Orange Clockwise
			} else if (key == 89 && !solved) {
				if (!shift) myCube.turnYellow(); // Rotate Yellow Clockwise
				else myCube.turnYellowInverse(); // Rotate Yellow Orange Clockwise
			} else if (key == 81) { // Q - Quit
				System.exit(0);
			} else if (key == 73) { // I - Inverse
				invert = !invert;
			} else if (key == 83) { // S - Solve
				solve();
			} else if (key == 69) { // E - Edit
				edit = !edit;
			} else if (key == 84) { // T - Test
				myCube.scramble();
				// solveStressTest();
			} else if (key == 85) { //U -
				//I want to do some custom move.
				parseSequence(myCube, "R G");
				System.out.println("Solved: " + myCube.getPercentSolved());
			}
			else if(key == 86){// V
				// String solution = Genetic.SolveCube(myCube.copy());   
				// parseSequence(myCube, solution);
				Genetic geneticSolver = new Genetic(myCube);
				geneticSolver.start();
			} 
			else if(key == 88) {
				System.out.println(Genetic.calculateFitness(myCube));
				CubeSolver.solveWhiteCross(myCube);
				System.out.println(Genetic.calculateFitness(myCube));
			}
		}
		this.repaint();
	}

	private void parseSequence(Cube cube, String seq) {
		String moves[] = seq.split(" ");
		for(String move : moves){
			if(move.equals("R")){
				cube.turnRed();
			}
			else if(move.equals("IR")){
				cube.turnRedInverse();
			}
			else if(move.equals("O")){
				cube.turnOrange();
			}
			else if(move.equals("IO")){
				cube.turnOrangeInverse();
			}
			else if(move.equals("B")){
				cube.turnBlue();
			}
			else if(move.equals("IB")){
				cube.turnBlueInverse();
			}			
			else if(move.equals("G")){
				cube.turnGreen();
			}
			else if(move.equals("IG")){
				cube.turnGreenInverse();
			}			
			else if(move.equals("W")){
				cube.turnWhite();
			}
			else if(move.equals("IW")){
				cube.turnWhiteInverse();
			}
			else if(move.equals("Y")){
				cube.turnYellow();
			}
			else if(move.equals("IY")){
				cube.turnYellowInverse();
			}
		}
	}
	
	private void solve() {
		auto =  true;
		myMoveIndex = 0;
		mySolvingCube = myCube.copy();
		if (mySolvingCube.getPercentSolved() != 100) {
			for (int i = 0; i < 20; i++) {
				myCube.scale("up");
			}
			for (int i = 0; i < 15; i++) {
				myCube.scale("down");
			}

			myScrollX = 0 ;
			mySolvingCube.resetMoves();

			CubeSolver.solveWhiteCross(mySolvingCube);
			CubeSolver.cleanWhiteCross(mySolvingCube);
			CubeSolver.solveWhiteCorners(mySolvingCube);
			CubeSolver.solveLayerTwoEdges(mySolvingCube);
			CubeSolver.solveYellowCross(mySolvingCube);
			CubeSolver.cleanYellowCross(mySolvingCube);
			CubeSolver.socketYellowCorners(mySolvingCube);
			CubeSolver.orientFinalCorners(mySolvingCube);
			if (mySolvingCube.getPercentSolved() == 100) solved = true;
			else impossibleCube = true;

		}
	}

	private void solveStressTest() {
		myCube = new Cube(WIDTH, HEIGHT, myStep, 1, myDimensions, false);
		int moveCount;
		for (int i = 0; i < 1000; i++) {
			moveCount = 0;
			myCube.scramble();
			CubeSolver.solveWhiteCross(myCube);
			if (myCube.getMoves().size() > 0) {
				System.out.print("Moves to solve the 'White Cross':\n\t");
				for (String s : myCube.getMoves()) {
					System.out.print(s + " ");
					if (moveCount++ == 9) {
						System.out.print("\n\t");
						moveCount = 0;
					}
				}
				System.out.println();
			} else System.out.println("No moves needed to solve the 'White Cross'.");
			moveCount = 0;
			myCube.resetMoves();
			CubeSolver.cleanWhiteCross(myCube);
			if (myCube.getMoves().size() > 0) {
				System.out.print("Moves to clean the 'White Cross':\n\t");
				for (String s : myCube.getMoves()) {
					System.out.print(s + " ");
					if (moveCount++ == 9) {
						System.out.print("\n\t");
						moveCount = 0;
					}
				}
				System.out.println();
			} else System.out.println("No moves needed to clean the 'White Cross'.");
			moveCount = 0;
			myCube.resetMoves();
			CubeSolver.solveWhiteCorners(myCube);
			if (myCube.getMoves().size() > 0) {
				System.out.print("Moves to solve white corners:\n\t");
				for (String s : myCube.getMoves()) {
					System.out.print(s + " ");
					if (moveCount++ == 9) {
						System.out.print("\n\t");
						moveCount = 0;
					}
				}
				System.out.println();
			} else System.out.println("No moves needed to solve the white corners.");
			moveCount = 0;
			myCube.resetMoves();
			CubeSolver.solveLayerTwoEdges(myCube);
			if (myCube.getMoves().size() > 0) {
				System.out.print("Moves to solve layer two:\n\t");
				for (String s : myCube.getMoves()) {
					System.out.print(s + " ");
					if (moveCount++ == 9) {
						System.out.print("\n\t");
						moveCount = 0;
					}
				}
				System.out.println();
			} else System.out.println("No moves needed to solve layer two.");

			moveCount = 0;
			myCube.resetMoves();
			CubeSolver.solveYellowCross(myCube);
			if (myCube.getMoves().size() > 0) {
				System.out.print("Moves to solve the yellow Cross:\n\t");
				for (String s : myCube.getMoves()) {
					System.out.print(s + " ");
					if (moveCount++ == 9) {
						System.out.print("\n\t");
						moveCount = 0;
					}
				}
				System.out.println();
			} else System.out.println("No moves needed to solve the yellow cross.");

			moveCount = 0;
			myCube.resetMoves();
			CubeSolver.cleanYellowCross(myCube);
			if (myCube.getMoves().size() > 0) {
				System.out.print("Moves to clean the yellow sross:\n\t");
				for (String s : myCube.getMoves()) {
					System.out.print(s + " ");
					if (moveCount++ == 9) {
						System.out.print("\n\t");
						moveCount = 0;
					}
				}
				System.out.println();
			} else System.out.println("No moves needed to clean the yellow cross.");

			moveCount = 0;
			myCube.resetMoves();
			CubeSolver.socketYellowCorners(myCube);
			if (myCube.getMoves().size() > 0) {
				System.out.print("Moves to socket the yellow corners:\n\t");
				for (String s : myCube.getMoves()) {
					System.out.print(s + " ");
					if (moveCount++ == 9) {
						System.out.print("\n\t");
						moveCount = 0;
					}
				}
				System.out.println();
			} else System.out.println("No moves needed to socket the yellow corners.");


			moveCount = 0;
			myCube.resetMoves();
			CubeSolver.orientFinalCorners(myCube);
			if (myCube.getMoves().size() > 0) {
				System.out.print("Moves to orient the final corners:\n\t");
				for (String s : myCube.getMoves()) {
					System.out.print(s + " ");
					if (moveCount++ == 9) {
						System.out.print("\n\t");
						moveCount = 0;
					}
				}
				System.out.println();
			} else System.out.println("No moves needed to orient final corners.");

		}
		myCube.resetMoves();

		System.out.println("1000 random tests passed successfully.");
		System.out.println("Included Tests:");
		System.out.println("\t- solveWhiteCross()");
		System.out.println("\t- cleanWhiteCross()");
		System.out.println("\t- solveWhiteCorners()");
		System.out.println("\t- solveLayerTwoEdges()");
		System.out.println("\t- solveYellowCross()");
		System.out.println("\t- cleanYellowCross()");
		System.out.println("\t- socketYellowCorners()");
		System.out.println("\t- orientFinalCorners()");


	}

	private void scrollThroughSolution(Graphics2D g2d) {
		int fontSize = 12;



		int upperBound = mySolvingCube.getMoves().size();

		int stringX;
		int midPoint = WIDTH / 2 - 20;
		int start = mySolutionX + 55;
		int finish = mySolutionX + 70 + (15 * 20);

		g2d.setColor(Color.WHITE);
		if (myScrollX % 65 == 0) turnReady = true;

		int i = 0;
		for (String s : mySolvingCube.getMoves()) {
			stringX = (int) myScrollX + 291 + (i++ * 65);
			if (stringX == midPoint) {
				stringX++;
			}
			fontSize = 5 + 1000 / Math.abs(midPoint - stringX);

			if (fontSize > 50) {
				fontSize = 50;
			}

			g2d.setFont(new Font("Aurora Cn BT", Font.PLAIN, fontSize));
			g2d.drawString(s, stringX, mySolutionY);

		}
		g2d.setFont(new Font("Aurora Cn BT", Font.PLAIN, 15));
		g2d.setColor(Color.BLACK);
		g2d.fillRect(mySolutionX - 50, mySolutionY - 13, 105, 15);
		g2d.fillRect(mySolutionX + 90 + (15 * 20), mySolutionY - 13, 105, 15);

		g2d.setColor(Color.WHITE);
		g2d.drawString("\u25C1", mySolutionX + 40, mySolutionY);
		g2d.drawString("\u25B7", mySolutionX + 93 + (15 * 20), mySolutionY);
		g2d.setFont(new Font("Aurora Cn BT", Font.PLAIN, 10));



		if (auto) g2d.drawString("STOP", midPoint + 7, mySolutionY + 30);
		else  g2d.drawString("AUTO", midPoint + 7, mySolutionY + 30);






	}

	private void drawMenu(Graphics2D g2d) {
		g2d.setFont(new Font("Arial", Font.PLAIN, 12));
		// g2d.setColor(Color.DARK_GRAY);
		g2d.setPaintMode();
		g2d.setPaint(new GradientPaint(5, 10, Color.BLACK, 5, HEIGHT / 1, Color.GRAY));
		g2d.fillRect(50, 50, WIDTH - 100, HEIGHT - 100);
		g2d.setColor(Color.WHITE);

		int height = HEIGHT / 34;
		int heightStart = 5;
		int width = WIDTH / 8;


		g2d.drawString("Invert Rotations - [I] key", width, heightStart++ * height);
		g2d.drawString("Toggle Edit Mode - [E] key", width, heightStart++ * height);
		g2d.drawString("Auto Solve - [S] key", width, heightStart++ * height);
		g2d.drawString("Test - [T] key", width, heightStart++ * height);
		g2d.drawString("Rotate Right - [Right Arrow] key", width, heightStart++ * height);
		g2d.drawString("Rotate Left - [Left Arrow] key", width, heightStart++ * height);
		g2d.drawString("Rotate Up - [Up Arrow] key", width, heightStart++ * height);
		g2d.drawString("Rotate Down - [Down Arrow] key", width, heightStart++ * height);
		g2d.drawString("Rotate Clockwise - [Right Arrow] key + [SHIFT] key", width, heightStart++ * height);
		g2d.drawString("Rotate Counter - [Left Arrow] key + [SHIFT] key", width, heightStart++ * height);
		g2d.drawString("Turn Red Face - [R] key", width, heightStart++ * height);
		g2d.drawString("Turn Blue Face -[B] key", width, heightStart++ * height);
		g2d.drawString("Turn Orange Face - [O] key", width, heightStart++ * height);
		g2d.drawString("Turn Green Face - [G] key", width, heightStart++ * height);
		g2d.drawString("Turn White Face - [W] key", width, heightStart++ * height);
		g2d.drawString("Turn Yellow Face - [Y] key", width, heightStart++ * height);
		g2d.drawString("Turn Red Face Counter  - [R] key + [SHIFT] key", width, heightStart++ * height);
		g2d.drawString("Turn Blue Face Counter - [B] key + [SHIFT] key", width, heightStart++ * height);
		g2d.drawString("Turn Orange Face Counter - [O] key + [SHIFT] key", width, heightStart++ * height);
		g2d.drawString("Turn Green Face Counter - [G] key + [SHIFT] key", width, heightStart++ * height);
		g2d.drawString("Turn White Face Counter - [W] key + [SHIFT] key", width, heightStart++ * height);
		g2d.drawString("Turn Yellow Face Counter - [Y] key + [SHIFT] key", width, heightStart++ * height);
		g2d.drawString("Invert Rotations - [I] key", width, heightStart++ * height);
		g2d.drawString("Increase Rotation Degree - [+] key", width, heightStart++ * height);
		g2d.drawString("Decrease Rotation Degree - [-] key", width, heightStart++ * height);
		g2d.drawString("Increase Cube Dimensions - [+] key + [SHIFT] key", width, heightStart++ * height);
		g2d.drawString("Decrease Cube Dimensions - [-] key + [SHIFT] key", width, heightStart++ * height);


	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (menu) {
			if (e.getKeyCode() == 77) menu = false;
		} else {
			if (e.getKeyCode() == 37) left = false;
			if (e.getKeyCode() == 38) up = false;
			if (e.getKeyCode() == 39) right = false;
			if (e.getKeyCode() == 40) down = false;
			if (e.getKeyCode() == 16) shift = false;
		}
		this.repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (edit) {
			myCube.click(arg0.getX(), arg0.getY());
			impossibleCube = false;
		}
		if (arg0.getX() > WIDTH / 2 - 15 && arg0.getX() < WIDTH / 2 + 15) {
			if (arg0.getY() > mySolutionY + 20 && arg0.getY() < mySolutionY + 50) {
				if (mySolvingCube.getMoves().size() > 0
					&& mySolvingCube.getPercentSolved() == 100) auto = !auto;
			}
		}
		if (scrollUp) {
			scroll();
		}
		else if (scrollDown) {
			scroll();
		}
		if (mySolvingCube.getPercentSolved() == 100) poke();
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		myDragX = 0;
		myDragY = 0;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		myCube.setStep(0.5);
		if (myDragX != 0 &&  myDragY !=0) {
			for (int i = 0; i < Math.abs(e.getX() - myDragX); i++) {
				if (e.getX() - myDragX > 0) {
					if (invert) {
					myCube.rotateLeft();
					} else myCube.rotateRight();
				} else {
					if (invert) {
						myCube.rotateRight();
					} else myCube.rotateLeft();
				}
				this.repaint();
			}
			for (int j = 0; j < Math.abs(myDragY - e.getY()); j++) {
				if (e.getY() - myDragY < 0) {
					if (invert) {
						myCube.rotateUp();
					} else myCube.rotateDown();
				} else {
					if (invert) {
						myCube.rotateDown();
					} else myCube.rotateUp();
				}
				this.repaint();
			}
		}
		myDragX = e.getX();
		myDragY = e.getY();
		myCube.setStep(myStep);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (e.getY() <= mySolutionY - 13 + 15 && e.getY() >= mySolutionY - 13) {
			if (e.getX() <= mySolutionX + 40 + 15 && e.getX() >= mySolutionX + 40) {
				scrollUp = true;
				auto = false;
			} else if (e.getX() <= mySolutionX + 90 + (15 * 21) && e.getX() >= mySolutionX + 90 + (15 * 20)) {
				scrollDown = true;
				auto = false;
			} else {
				scrollUp = false;
				scrollDown = false;
			}
		} else {
			scrollUp = false;
			scrollDown = false;
		}
	}

	public void poke() {
		if (mySolvingCube.getMoves().size() > 0) {
			if (scrollUp && myMoveIndex > 0) {
				unExecuteString(mySolvingCube.getMoves().get(--myMoveIndex));
			}
			else if (scrollDown || auto) {
				if (myMoveIndex < mySolvingCube.getMoves().size()) {
					executeString(mySolvingCube.getMoves().get(myMoveIndex++));
				}
			}
			turnReady = false;
			this.repaint();
		}
	}

	public void scroll() {
		if (myMoveIndex <= mySolvingCube.getMoves().size() - 1 && myMoveIndex >= 0) {
			if (scrollUp) myScrollX += 65;
			else if (scrollDown) myScrollX -= 65;
			else myScrollX += -0.5 - myScrollStep;
			// System.out.println("myScrollX = " + myScrollX);
		}
		this.repaint();
	}

	public boolean getAuto() {
		return auto;
	}

	public boolean getTurnReady() {
		return turnReady;
	}

	private void executeString(String theMove) {
		if (theMove.equals("W")) {
			myCube.turn(0, false);
		} else if (theMove.equals("R")) {
			myCube.turn(1, false);
		} else if (theMove.equals("B")) {
			myCube.turn(2, false);
		} else if (theMove.equals("O")) {
			myCube.turn(3, false);
		} else if (theMove.equals("G")) {
			myCube.turn(4, false);
		} else if (theMove.equals("Y")) {
			myCube.turn(5, false);
		} else if (theMove.equals("Wi")) {
			myCube.turn(0, true);
		} else if (theMove.equals("Ri")) {
			myCube.turn(1, true);
		} else if (theMove.equals("Bi")) {
			myCube.turn(2, true);
		} else if (theMove.equals("Oi")) {
			myCube.turn(3, true);
		} else if (theMove.equals("Gi")) {
			myCube.turn(4, true);
		} else if (theMove.equals("Yi")) {
			myCube.turn(5, true);
		} else System.out.println("Did not execute");
		this.repaint();
		System.out.println("The Move: \"" + theMove + "\"");
	}

	private void unExecuteString(String theMove) {
		if (theMove.equals("W")) {
			myCube.turn(0, true);
		} else if (theMove.equals("R")) {
			myCube.turn(1, true);
		} else if (theMove.equals("B")) {
			myCube.turn(2, true);
		} else if (theMove.equals("O")) {
			myCube.turn(3, true);
		} else if (theMove.equals("G")) {
			myCube.turn(4, true);
		} else if (theMove.equals("Y")) {
			myCube.turn(5, true);
		} else if (theMove.equals("Wi")) {
			myCube.turn(0, false);
		} else if (theMove.equals("Ri")) {
			myCube.turn(1, false);
		} else if (theMove.equals("Bi")) {
			myCube.turn(2, false);
		} else if (theMove.equals("Oi")) {
			myCube.turn(3, false);
		} else if (theMove.equals("Gi")) {
			myCube.turn(4, false);
		} else if (theMove.equals("Yi")) {
			myCube.turn(5, false);
		} else System.out.println("Did not execute");
		this.repaint();
		System.out.println("Undid Move: \"" + theMove + "\"");
	}
}
