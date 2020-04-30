import java.util.*;
import java.lang.*;

 class GeneticRubik {
	private String moves;
	private double fitness;
	private Cube cubesolve;
	private Cube initialCube;
	
	public GeneticRubik(String moves, Cube cubesolve){
        this.moves = moves;
		this.cubesolve = cubesolve.copy();
		this.initialCube = cubesolve.copy();
		Genetic.parseSequence(this.cubesolve, this.moves);
	    this.fitness = cubesolve.getPercentSolved();
		//Genetic.calculateFitness(this.cubesolve);
	}
    
    public String getMoves(){
        return this.moves;
    }
    
    public double getFitness(){
        return this.fitness;
    }
    
	public Cube getCube(){
        return this.cubesolve;
    }
	
	public Cube getInitialCube(){
		return initialCube;
	}
	
    public GeneticRubik reproduceWith(GeneticRubik b){
        GeneticRubik a = this;
	    GeneticRubik [] parents = {a, b};
		Cube cubesolve = parents[0].getInitialCube().copy(); // Initial cube is the scrambled cube on which we will apply the moves
		int length[] = new int[5];
        int moves_length  = a.getMoves().split(" ").length;
		int moves_length1 = b.getMoves().split(" ").length;
		length[0] = moves_length1;
		length[1] = moves_length;
		length[2] = (moves_length + moves_length1)/2;
		length[3] = length[(int)(2 * Math.random())] + (int)(Math.abs(length[0] - length[1]) * Math.random());
		length[3] = Math.min(Genetic.DNA_length, length[3]);
		length[4] = length[(int)(2 * Math.random())] - (int)(Math.abs(length[0] - length[1]) * Math.random());
		length[4] = Math.max(0, length[4]);
	    double select = Math.random();
		StringBuilder sb = new StringBuilder();
		String childGene;
	    int childMoveLength = length[(int)(5 * Math.random())];
		int maxParentLength = Math.max(length[0], length[1]);
        int mutationIndices[] = new int[(int)((Genetic.mutationSize / 100) * childMoveLength)];
		
        for(int i = 0; i < mutationIndices.length; i++)
		{
			mutationIndices[i] = (int)(childMoveLength * Math.random());
		}
        for(int i = 0; i < childMoveLength; i++){
            int coinToss = (int)(Math.random() > 0.5 ? 1 : 0);
			if(coinToss == 0)
			{
				String moves[] = a.getMoves().split(" ");
				int index = (int)(moves.length * Math.random());
				childGene = moves[index]; 
			}
			else 
			{
				String moves[] = b.getMoves().split(" ");
				int index = (int)(moves.length * Math.random());
				childGene = moves[index]; 
			}	
			boolean was_mutated = false;
            //Mutation
            for(int j = 0; j < mutationIndices.length; j++){
                if(mutationIndices[j] == i){
                    //Check if this guy is lucky
                    if(100 * Math.random() < Genetic.mutationRate){
                        //Mutate
                        childGene = Genetic.mutateString(childGene);
						was_mutated = true;
                    }
                    break;
                }
            }
			if(!was_mutated && i >= maxParentLength){
				//This is a new gene, try to mutate it more.
				if(100 * Math.random() < (Genetic.mutationRate * 2)){
					//Mutate
					childGene = Genetic.mutateString(childGene);
					was_mutated = true;
				}
			}
            sb.append(childGene + " ");
        }
		// System.out.println("Mutated Child: " + sb.toString());
		Scanner sc = new Scanner(System.in);
		// sc.nextLine();
        GeneticRubik child = new GeneticRubik(sb.toString().trim(),cubesolve);
        return child;
    }
}
	
class SortByFitness implements Comparator<GeneticRubik> 
{ 
    // Used for sorting in descending order of 
    // fitness 
    public int compare(GeneticRubik a, GeneticRubik b) 
		{ 
		  return (int)b.getFitness() - (int)a.getFitness(); 
		} 
}

public class Genetic extends Thread
{
    static int DNA_length = 300;
    public static int mutationSize = 10;
    public static double mutationRate = 10;
    public static int mutation_delta = 11;
	static Cube myCube;
	
	Cube applicationCube;
	public Genetic(Cube applicationCube){
		this.applicationCube = applicationCube;
		
	}
	
	public void run(){
		
		Genetic.SolveCube(this.applicationCube); //application cube is the cube which will be visible to us flickering
		
	}
	
    public static void SolveCube(Cube puzzle) {
		int initial_population = 100;
		int generation = 0;
		int max_generations = Main.max_generations;
		boolean moves_applied = false;
		String previousMove = "";
		myCube = puzzle.copy();                       // myCube will  be the scrambled cube.
		ArrayList<GeneticRubik> population = new ArrayList<GeneticRubik>();
		// initialize population
		for(int i = 0; i < initial_population; i++){
		    GeneticRubik temp = new GeneticRubik(Genetic.getAlphaNumericString((int)(DNA_length * Math.random())),myCube);
		    population.add(temp);
		}
		Collections.sort(population, new SortByFitness());
		for(int i = 0; i < max_generations ; i++){  // population.get(0).getFitness() < 320
		    //Selection
		    while(population.size() > initial_population / 2){
		        population.remove(population.size() - 1);
		    }
		    while(population.size() <= initial_population){
		        //Reproduce
		        //Choose two parents
		        int parent_index_1, parent_index_2;
		        parent_index_2 = parent_index_1 = (int)(population.size() * Math.random());
		        while(parent_index_1 == parent_index_2){
		            parent_index_2 = (int)(population.size() * Math.random());
		        }
				// System.out.println(parent_index_1 + ", " + parent_index_2);
				// System.out.println(population.get(parent_index_1).getMoves());
				// System.out.println(population.get(parent_index_2).getMoves());
				// Scanner sc = new Scanner(System.in);
				// sc.nextLine();
		        GeneticRubik a = population.get(parent_index_1);
		        GeneticRubik b = population.get(parent_index_2);
		        population.add(a.reproduceWith(b));
		    }
			Collections.sort(population, new SortByFitness());
			generation++;
			if(moves_applied && previousMove != null) {
					Genetic.parseSequenceReverse(puzzle, previousMove);    // undo the moves applied on the application cube by previous generation best fit moves
					                                                       // and apply new best fit moves on the application cube.
				}
				Genetic.parseSequence(puzzle, population.get(0).getMoves());
				moves_applied = true;
				previousMove = population.get(0).getMoves();
			
			if(i % 100 == 0){
				System.out.println("Generation :" + (generation) + " Finshed, Population:");
				Genetic.displayPopulation(population, false);
				// if(moves_applied && previousMove != null) {
					// Genetic.parseSequenceReverse(puzzle, previousMove);
				// }
				// Genetic.parseSequence(puzzle, population.get(0).getMoves());
				// moves_applied = true;
				// previousMove = population.get(0).getMoves();
			}
		}
		
	}
	static int calculateFitness(Cube fitnesscube)
	{
	   int score = 0;
	   int current_score = 0;
	   
	   current_score = checkWhiteCross(fitnesscube);
	   score += current_score;
	   if(current_score == 0) return score;

	   current_score = checkcleanWhiteCross(fitnesscube);
	   score += current_score;
	   if(current_score == 0) return score;

	   current_score = checkWhiteCorners(fitnesscube);
	   score += current_score;
	   if(current_score == 0) return score;
	   
	   current_score = checkLayerTwoEdges(fitnesscube);
	   score += current_score;
	   if(current_score == 0) return score;
	   
	   current_score = checkYellowCross(fitnesscube);
	   score += current_score;
	   if(current_score == 0) return score;
	   
	   current_score = checkcleanYellowCross(fitnesscube);
	   score += current_score;
	   if(current_score == 0) return score;
	   
	   current_score = checksocketYellowCorners(fitnesscube);
	   score += current_score;
	   if(current_score == 0) return score;
	   
	   current_score = checkorientFinalCorners(fitnesscube);
	   score += current_score;

	   return score;
	}
	
	static int checkWhiteCross(Cube fitnesscube)
	{
		int score = 0;
		Face[][][]  tempFace = fitnesscube.getFaces();
	    // W:0, R:1, B:2, O:3, G:4, Y:5
	    if(    tempFace[0][0][1].getColorInt() == 0
			&& tempFace[0][1][0].getColorInt() == 0
			&& tempFace[0][1][2].getColorInt() == 0
			&& tempFace[0][2][1].getColorInt() == 0)
			{
			  score = 40;
			}
		
		      return score;
		   
	}
	static int checkcleanWhiteCross(Cube fitnesscube)
	{
		int score = 0;
		Face[][][]  tempFaces = fitnesscube.getFaces();
	    // W:0, R:1, B:2, O:3, G:4, Y:5
	    if(    tempFaces[1][0][1].getColorInt() == 1
			&& tempFaces[2][0][1].getColorInt() == 2
			&& tempFaces[3][0][1].getColorInt() == 3
			&& tempFaces[4][0][1].getColorInt() == 4)
			{
			  score = 40;
			}
		
		      return score;
    
	}
	static int checkWhiteCorners(Cube fitnesscube)
	{
		int score = 0;
		Face[][][]  tempFaces = fitnesscube.getFaces();
	    // W:0, R:1, B:2, O:3, G:4, Y:5
	    if(    CubeSolver.checkTopCorner(tempFaces, 1, 2, 0, 1, 2, 0) && tempFaces[0][2][2].getColorInt() == 0
			&& CubeSolver.checkTopCorner(tempFaces, 2, 3, 0, 2, 3, 0) && tempFaces[0][0][2].getColorInt() == 0
			&& CubeSolver.checkTopCorner(tempFaces, 3, 4, 0, 3, 4, 0) && tempFaces[0][0][0].getColorInt() == 0
			&& CubeSolver.checkTopCorner(tempFaces, 4, 1, 0, 4, 1, 0) && tempFaces[0][2][0].getColorInt() == 0)
			{
			  score = 40;
			}
		
		      return score;
    }
	static int checkLayerTwoEdges(Cube fitnesscube)
	{
		int score = 0;
		Face[][][]  tempFaces = fitnesscube.getFaces();
	    // W:0, R:1, B:2, O:3, G:4, Y:5
	    if(    !(  tempFaces[1][1][0].getColorInt() != 1 || tempFaces[1][1][2].getColorInt() != 1
				|| tempFaces[2][1][0].getColorInt() != 2 || tempFaces[2][1][2].getColorInt() != 2
				|| tempFaces[3][1][0].getColorInt() != 3 || tempFaces[3][1][2].getColorInt() != 3
				|| tempFaces[4][1][0].getColorInt() != 4 || tempFaces[4][1][2].getColorInt() != 4 ))
			{
			  score = 40;
			}
		
		      return score;
    }
	static int checkYellowCross(Cube fitnesscube)
	{
		int score = 0;
		Face[][][]  tempFaces = fitnesscube.getFaces();
	    // W:0, R:1, B:2, O:3, G:4, Y:5
	    if(      !(tempFaces[5][0][1].getColorInt() != 5 || tempFaces[5][1][0].getColorInt() != 5
				&& tempFaces[5][1][2].getColorInt() != 5 || tempFaces[5][2][1].getColorInt() != 5 ))
			{
			  score = 40;
			}
		
		      return score;
    }
	static int checkcleanYellowCross(Cube fitnesscube)
	{
		int score = 0;
		Face[][][]  tempFaces = fitnesscube.getFaces();
	    // W:0, R:1, B:2, O:3, G:4, Y:5
	    if(  !(tempFaces[1][2][1].getColorInt() != 1
			|| tempFaces[2][2][1].getColorInt() != 2
			|| tempFaces[3][2][1].getColorInt() != 3
			|| tempFaces[4][2][1].getColorInt() != 4))
			{
			  score = 40;
			}
		
		      return score;
    }
	static int checksocketYellowCorners(Cube fitnesscube)
	{
		int score = 0;
		Face[][][]  theFaces = fitnesscube.getFaces();
	    // W:0, R:1, B:2, O:3, G:4, Y:5
	    if(     CubeSolver.checkBottomCorner(theFaces, 1, 2, 5, 1, 2, 5)
		     && CubeSolver.checkBottomCorner(theFaces, 2, 3, 5, 2, 3, 5)
		     && CubeSolver.checkBottomCorner(theFaces, 3, 4, 5, 3, 4, 5)
		     && CubeSolver.checkBottomCorner(theFaces, 4, 1, 5, 4, 1, 5))
			{
			  score = 40;
			}
	
		      return score;
    }
	static int checkorientFinalCorners(Cube fitnesscube)
	{
		int score = 0;
		// W:0, R:1, B:2, O:3, G:4, Y:5
		if  (!(fitnesscube.getPercentSolved() != 100 ))
			{
			  score = 40;
			}
		
		      return score;
    }
	static void parseSequence(Cube cube, String seq) {
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
	
	static public void parseSequenceReverse(Cube cube, String seq){
		String moves_reverse[] = seq.split(" ");
		ArrayList<String> moves = new ArrayList<String>(Arrays.asList(moves_reverse));
		Collections.reverse(moves);
		for(String move : moves){
			if(move.equals("R")){
				cube.turnRedInverse();
			}
			else if(move.equals("IR")){
				cube.turnRed();
			}
			else if(move.equals("O")){
				cube.turnOrangeInverse();
			}
			else if(move.equals("IO")){
				cube.turnOrange();
			}
			else if(move.equals("B")){
				cube.turnBlueInverse();
			}
			else if(move.equals("IB")){
				cube.turnBlue();
			}			
			else if(move.equals("G")){
				cube.turnGreenInverse();
			}
			else if(move.equals("IG")){
				cube.turnGreen();
			}			
			else if(move.equals("W")){
				cube.turnWhiteInverse();
			}
			else if(move.equals("IW")){
				cube.turnWhite();
			}
			else if(move.equals("Y")){
				cube.turnYellowInverse();
			}
			else if(move.equals("IY")){
				cube.turnYellow();
			}
		}
	}
	
	static void displayPopulation(ArrayList<GeneticRubik> population, boolean is_final){
	    System.out.println("Population Start:");
		int i = 1;
	    for(GeneticRubik m : population)
			{
				if(!is_final) {
					System.out.println( i + ". Fitness: " + m.getFitness());
					String moves_cube[] =  m.getMoves().split(" ");
					System.out.println( i + ". Moves: " + moves_cube.length);
				} else {
					System.out.println( i + ". Fitness: " + m.getFitness() + " Moves: " + m.getMoves());
					
				}
				i++;
				if(i == 15) {
					break;
				}
			}
	    System.out.println("Population End");
	}
	
	static String getAlphaNumericString(int n) 
    { 
        // chose a Character random from this String 
        String AlphaNumericString = "R B O G Y W IR IY IB IO IG IW";  
        // create StringBuffer size of AlphaNumericString 
        StringBuilder sb = new StringBuilder(n); 
        String initialmoves[] = AlphaNumericString.split(" ");
		// System.out.println(initialmoves.length);
		// for (String move : initialmoves){
			// System.out.println(move);
		// }
        for (int i = 0; i < n; i++)
			{ 
				// generate a random number between 
				// 0 to AlphaNumericString variable length 
				int index = (int)(initialmoves.length * Math.random()); 
				// add Character one by one in end of sb 
				sb.append(initialmoves[index] + " "); 
			} 
  
        return sb.toString().trim(); 
    }
    
    public static String mutateString(String a){
        String AlphaNumericString = "R B O G Y W IR IY IB IO IG IW";
		String initialmoves[] = AlphaNumericString.split(" ");
		int index = -1;
        for (int i = 0; i < initialmoves.length; i++)
			{
				if (initialmoves[i].equals(a)){
					index = i;
					break;
				}
			}
		int delta = (int)(Genetic.mutation_delta * Math.random()) * (Math.random() < 0.5 ? -1 : 1);
        index = (index + delta) % initialmoves.length;
        if(index < 0){
            index += initialmoves.length;
        }
        return initialmoves[index];
    }
}

