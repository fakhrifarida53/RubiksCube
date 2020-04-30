import java.util.*;

class CubeSolution {
	double fitness;
	ArrayList<String> moves;
	Cube initialCube; //We will not touch this, this will represent the problem/puzzle cube.
	Cube solutionCube; //We will apply the current solution to this cube.
	public FitnessCalculator fitnessCalculator;
	
	public CubeSolution(Cube initialCube, ArrayList<String> moves, FitnessCalculator fitnessCalculator) {
		this.initialCube = initialCube.copy();
		this.moves = new ArrayList<String>();
		for(String move : moves){
			this.moves.add(move);
		}
		this.solutionCube = initialCube.copy();
		this.fitnessCalculator = fitnessCalculator;
		GeneticSolver.applyMovesToCube(this.solutionCube, this.moves);
		this.fitness = this.fitnessCalculator.calculateFitness(this.solutionCube);
	}
	
	//reproduction
	public CubeSolution reproduceWith(CubeSolution b) {
		CubeSolution parents[] = {this, b};
		int lengths[] = new int[2];
		int childLength;
		lengths[0] = parents[0].getMoves().size();
		lengths[1] = parents[1].getMoves().size();
		childLength = lengths[(int)(Math.random() * 2)];
		if(((int)Math.random() * 100) < (GeneticSolver.mutationRate * GeneticSolver.mutationRateMultiplierLength)) {
			childLength += (Math.random() > 0.5 ? -1 : 1) * ((int)Math.random() * GeneticSolver.mutationDeltaLength);
			childLength = childLength > GeneticSolver.maxMoves ? GeneticSolver.maxMoves : childLength;
			childLength = childLength < GeneticSolver.minMoves ? GeneticSolver.minMoves : childLength;
		}
		ArrayList<String> childMoves = new ArrayList<String>();
		int mutationLimit = (int) ((GeneticSolver.mutationSize / 100) * childLength);
		int numberOfMutations = 0;
		for(int i = 0; i < childLength; i++) {
			int parent_index = (int) (Math.random() * 2);
			int gene_index;
			String childMove;
			int mutationRate;
			if(parents[parent_index].getMoves().size() <= i){
				if(parent_index == 1) parent_index = 0;
				if(parent_index == 0) parent_index = 1;
			}
			if(parents[parent_index].getMoves().size() <= i){
				mutationRate = 100;
				childMove = "R";                                                           
				mutationLimit = childLength + 1;
			} else {
				childMove = parents[parent_index].getMoves().get(i);
				mutationRate = GeneticSolver.mutationRate;
			}
			if((int)(Math.random() * 100) < mutationRate && numberOfMutations < mutationLimit) {
				childMove = mutate(childMove);
				numberOfMutations++;
			}
			childMoves.add(childMove);
		}
		return new CubeSolution(getInitialCube(), childMoves, fitnessCalculator);
	}
	
	//mutation
	public String mutate(String move) {
		String [] moveDictionary = {"R", "G", "B", "Y", "O", "W", "IR", "IG", "IB", "IY", "IO", "IW"};
		int currentIndex = 0, mutatedIndex;
		for(int i = 0; i < moveDictionary.length; i++) {
			if(move.equals(moveDictionary[i])) {
				currentIndex = i;
				break;
			}
		}
		mutatedIndex = currentIndex + (Math.random() > 0.5 ? -1 : 1) * ((int)Math.random() * GeneticSolver.mutationDelta);
		mutatedIndex %= moveDictionary.length;
		if(mutatedIndex < 0) {
			mutatedIndex += moveDictionary.length;
		}
		return moveDictionary[mutatedIndex];
	}
	
	Cube getInitialCube() {
		return initialCube;
	}
	
	Cube getSolutionCube() {
		return solutionCube;
	}
	
	ArrayList<String> getMoves() {
		return moves;
	}
	
	double getFitness() {
		return fitness;
	}
}