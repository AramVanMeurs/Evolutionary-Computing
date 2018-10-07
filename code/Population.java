import java.util.*;
import java.util.Random;
import java.util.Properties;

public class Population
{

    /* Implements the Population class for an Evolutionary algorithm.
     * This class is meant to keep track a group of individuals and
     * its interactions with other groups of individuals.
     *
     * TODO:
     * - Implement integration of survivor selection and population updates
     * - Implement general population management functions
     * - Implement population interaction for island/cellular/metapopulation models
     */

    Individual[] group;

    // Basic constructor
    Population() {}

    // Basic constructor initializing the group array
    Population(int popSize){
        this.group = new Individual[popSize];
    }

    /* Basic Population constructor
     * Uses basic initialization of individuals
     */
    Population(Random rnd, double minVal, double maxVal, int length, int popSize){
        this.group = new Individual[popSize];
        for(int i = 0; i < popSize; i++)
            group[i] = new Individual(rnd,minVal,maxVal,length);
    }

    /* Basic Population constructor with specified standard deviation values */
    Population(Random rnd, double minVal, double maxVal, int length, double std, int popSize){
        this.group = new Individual[popSize];
        for(int i = 0; i < popSize; i++)
            group[i] = new Individual(rnd,minVal,maxVal,length,std);
    }

    /* Basic Population constructor
     * Uses existing Individual array as input
     */
    Population(Individual[] group){
        this.group = group;
    }

    /* Sets fitnesses of individuals when
     * given an array with fitness values.
     */
    void setFitnesses(double[] fitnesses){
        assert this.group.length == fitnesses.length;

        for(int i = 0; i < fitnesses.length; i++)
            this.group[i].setFitness(fitnesses[i]);
    }

    static Population mergePopulations(Population[] populations){
        Individual[] mergedGroups;

        List mergedList = new ArrayList(Arrays.asList(populations[0].group));

        for(int i = 1; i < populations.length; i++)
            mergedList.addAll(Arrays.asList(populations[i].group));

        mergedGroups = (Individual[]) mergedList.toArray();

        return new Population(mergedGroups);
    }
}
