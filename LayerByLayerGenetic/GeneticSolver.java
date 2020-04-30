import java.util.*;

public class GeneticSolver {
	static final int mutationRate = 5; //percent
	static final int mutationSize = 10; //percent
	static final int mutationDelta = 12; //absolute Value
	static final int mutationDeltaLength = 100; // absolute Value
	static final int mutationRateMultiplierLength = 2; //multiplier
	static final int maxMoves = 100;
	static final int minMoves = 0;
	private int maxPopulation = 100;
	private Cube puzzleCube;
	private int maxGenerations;
	private FitnessCalculator fitnessCalculator;
	
	public GeneticSolver(Cube puzzleCube, FitnessCalculator fitnessCalculator, int maxGenerations, int maxPopulation) {
		this.puzzleCube = puzzleCube;
		this.maxGenerations = maxGenerations;
		this.maxPopulation = maxPopulation;
		this.fitnessCalculator = fitnessCalculator;
	}
	
	class SortByFitness implements Comparator<CubeSolution> 
	{ 
		// Used for sorting in descending order of 
		// fitness 
		public int compare(CubeSolution a, CubeSolution b) 
		{ 
			return (int)b.getFitness() - (int)a.getFitness(); 
		} 
	} 
	
	public ArrayList<CubeSolution> solve(int n) {
		ArrayList<CubeSolution> population = new ArrayList<CubeSolution>();
		//Create initial population
		for(int i = 0; i < maxPopulation; i++) {
			population.add(new CubeSolution(puzzleCube, generateRandomMoves((int)(Math.random() * (GeneticSolver.maxMoves + 1))), fitnessCalculator));
		}
		//Initial Sorting
		Collections.sort(population, new SortByFitness());
		for(int i = 0; i < maxGenerations; i++){
			//Kill half the population based on fitness
			population.subList(population.size() / 2, population.size() - 1).clear();
			ArrayList<CubeSolution> children = new ArrayList<CubeSolution>();
			while((population.size() + children.size()) < maxPopulation) {
				int parent_a = (int)(Math.random() * population.size());
				int parent_b = parent_a;
				while(parent_a == parent_b){
					parent_b = (int)(Math.random() * population.size());
				}
				children.add(population.get(parent_a).reproduceWith(population.get(parent_b)));
			}
			population.addAll(children);
			Collections.sort(population, new SortByFitness());
			
			//Check if solution reached
			int solvedCount = 0;
			for(CubeSolution person : population) {
				if(person.getFitness() >= fitnessCalculator.getFitnessLimit()){
					solvedCount++;
				}
				if(solvedCount >= n){
					break;
				}
			}
			if(solvedCount >= n){
				break;
			}
			if(i % 100 == 0) {
				System.out.println("Population at Generation :" + i);
				printGenerationInformationMinimal(population);
			}
		}
		System.out.println("Evolution ended");
		printGenerationInformationMinimal(population);
		return new ArrayList<CubeSolution>(population.subList(0, n));
	}
	
	public void printGenerationInformationMinimal(ArrayList<CubeSolution> population) {
		int i = 1;
		for(CubeSolution person : population.subList(0, 10)) {
			System.out.println((i++) + ". " + person.getFitness());
			//System.out.println((i++) + ". " + person.getMoves().size());
		}
	}
	
	private ArrayList<String> generateRandomMoves(int n){
		String [] moveDictionary = {"R", "G", "B", "Y", "O", "W", "IR", "IG", "IB", "IY", "IO", "IW"};
		ArrayList<String> moves = new ArrayList<String>();
		for(int i = 0; i < n; i++) {
			moves.add(moveDictionary[(int)(Math.random() * moveDictionary.length)]);
		}
		return moves;
	}
	
	public static void applyMovesToCube(Cube cube, ArrayList<String> moves) {
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
}