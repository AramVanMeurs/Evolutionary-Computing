import java.util.Random;
import java.util.Properties;

public class Individual
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

    double fitness;

    /* Minimal Individual constructor
     * Implements basic initialization
     */
    Individual(Random rnd, double minVal, double maxVal, int length){
        this.minValue = minVal;
        this.maxValue = maxVal;

        // Initialize individual value using Uniform distribution
        this.value = new double[length];
        for(int i = 0; i < length; i++) 
            this.value[i] = (maxVal - minVal) * rnd.nextDouble() + minVal;

        // Initialize fitness to minVal until evaluated
        this.fitness = minVal;
    }

    /* Invidivdual constructor with standard deviation specified */
    Individual(Random rnd, double minVal, double maxVal, int length, double std){
        this(rnd,minVal,maxVal,length);

        // Initialize standard deviation per element
        this.std = new double[length];
        for(int i = 0; i < length; i++) 
            this.std[i] = std;
    }

    // Sets the fitness of the individual
    void setFitness(double fitness){
        this.fitness = fitness;
    }

}
