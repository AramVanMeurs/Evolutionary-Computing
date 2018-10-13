import java.util.*;
import java.util.Random;
import java.util.Properties;

public class Individual implements Comparable<Individual>
{

    /* Implements the Individual class for an Evolutionary algorithm.
     * This class is meant to keep track of the parameters per individual.
     * This class can also be used to implement different initialization methods.
     *
     * TODO:
     * - Implement parameters and parameter iniialization as they become needed.
     * - TBD
     */

    double minValue;
    double maxValue;

    double[] value;

    // Standard deviation per element used for mutation
    double[] std;

    int age;
    double fitness;

    // Number of wins used for round robin tournament
    int wins;

    // Basic constructor
    Individual() {}

    /* Minimal Individual constructor */
    Individual(double minVal, double maxVal, int length){
        this.minValue = minVal;
        this.maxValue = maxVal;

        // Initialize individual value array
        this.value = new double[length];

        // Initialize standard deviation array
        this.std = new double[length];

        // Initialize fitness to minVal until evaluated
        this.fitness = minVal;

        // Initialize age
        this.age = 0;
    }

    /* Minimal Individual constructor
     * Implements basic initialization
     */
    Individual(Random rnd, double minVal, double maxVal, int length){
        this(minVal,maxVal,length);

        // Initialize individual value using Uniform distribution
        for(int i = 0; i < length; i++) 
            this.value[i] = (maxVal - minVal) * rnd.nextDouble() + minVal;

    }

    /* Invidivdual constructor with standard deviation specified */
    Individual(Random rnd, double minVal, double maxVal, int length, double std){
        this(rnd,minVal,maxVal,length);

        // Initialize standard deviation per element
        for(int i = 0; i < length; i++) 
            this.std[i] = std;
    }

    // Sets the fitness of the individual
    void setFitness(double fitness){
        this.fitness = fitness;
    }

    /* Returns arraylist of Individual objects */
    static ArrayList<Individual> getArrayList(Individual[] group){
        ArrayList<Individual> groupList = new ArrayList<Individual>();

        for(int i = 0; i < group.length; i++)
            groupList.add(group[i]);

        return groupList;
    }

    /* Comparison functions */

    public static int compareWins(Individual first, Individual second){
        if(first.wins > second.wins) return 1;
        else if (first.wins < second.wins) return -1;
        else return 0;
    }

    public static int compareAges(Individual first, Individual second){
        if(first.age > second.age) return 1;
        else if (first.age < second.age) return -1;
        else return 0;
    }

    /* Overriding methods from parent method */

    // Compares two individuals using their fitnesses
    public int compareTo(Individual that){
        if(this.fitness > that.fitness) return 1;
        else if (this.fitness < that.fitness) return -1;
        else return 0;
    }

    // Checks if to individuals have identical solutions
    public boolean equals(Individual that){
        if(Arrays.equals(this.value,that.value)) return true;
        else return false;
    }
}
