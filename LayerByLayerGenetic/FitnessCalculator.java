import java.util.*;

public interface FitnessCalculator {
	abstract double calculateFitness(Cube cube);
	abstract double getFitnessLimit();
}