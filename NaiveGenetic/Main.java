import javax.swing.JFrame;

public class Main {
	private static Grid myGrid;
	private static JFrame myFrame;
	private static boolean myRunning;
	public static int max_generations;
	public static void main(String[] theArgs) {
		Main.max_generations = Integer.parseInt(theArgs[0]);
		System.out.println("Max Generations: " + Main.max_generations);
		init();
		myFrame.setUndecorated(true);
		myFrame.setContentPane(myGrid);
		myFrame.pack();
		myFrame.setLocationRelativeTo(null);
		myFrame.setVisible(true);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		begin();
	}

	private static void init() {
		myFrame = new JFrame("Eli's \"Cubik's Rube\"");
		myGrid = new Grid();
		myRunning = true;
	}

	private static void begin() {
		while (myRunning) {
			try {
					Thread.sleep(15);
					if (myGrid.getAuto()) myGrid.scroll();
					if (myGrid.getTurnReady()) myGrid.poke();
				} catch (Exception ex) {
					System.out.println(ex + " lol");
				}
		}
	}
}
