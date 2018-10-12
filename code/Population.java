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

    /* Increments the age of all individuals in the population */
    void incrementAges(int n){
        for(int i = 0; i < this.group.length; i++)
            this.group[i].age += n;

        return;
    }

    /* Returns arraylist of Population objects */
    static ArrayList<Population> getArrayList(Population[] archipelago){
        ArrayList<Population> popList = new ArrayList<Population>();

        for(int i = 0; i < archipelago.length; i++)
            popList.add(archipelago[i]);

        return popList;
    }

    static Population merge(Population[] populations){
        Individual[] mergedGroups;

        ArrayList<Individual> mergedList = new ArrayList(Arrays.asList(populations[0].group));

        // Add elements of all populations together in the ArrayList
        for(int i = 1; i < populations.length; i++)
            mergedList.addAll(Arrays.asList(populations[i].group));

        // Cast back to array of Individuals
        mergedGroups = new Individual[mergedList.size()];
        mergedGroups = mergedList.toArray(mergedGroups);

        return new Population(mergedGroups);
    }

    /* Implements migration in a ring topology or using a random pairwise topology.
     * Migration policies include swapping best, worst and random solutions.
     */
    static void migrate(Random rnd, Population[] archipelago, int numSwap, String type, boolean shuffle){
        Population[] islands = new Population[archipelago.length];

        // Cache array to store individuals for migration to next island
        Individual[] cache = new Individual[numSwap];

        // Choose if ring topology or random pairwise topology
        if(shuffle){
            // Create and shuffle island list 
            ArrayList<Population> islandList = Population.getArrayList(archipelago);
            Collections.shuffle(islandList,rnd);

            // Transfer back to array
            islands = islandList.toArray(islands);
        }
        else{
            islands = archipelago;
        }

        // Sort or shuffle groups per island
        for(int i = 0; i < islands.length; i++){
            if(type.equals("random")){
                Collections.shuffle(Arrays.asList(islands[i].group),rnd);
            }
            else if(type.equals("best") || type.equals("worst")){
                Arrays.sort(islands[i].group);
            }
            else return;
        }

        // Swaps individuals between islands
        for(int i = 0; i < islands.length; i++){
            int j = (i+1) % islands.length;
            int popSize = islands[i].group.length;

            for(int k = 0; k < numSwap; k++){
                int idx = k;
                Individual temp;

                // Calculate individual index according to migration policy
                if(type.equals("best")){
                    idx = popSize - numSwap + k;
                }

                // Start cache values from i == 0
                if(i == 0){
                    cache[k] = islands[i].group[idx];
                }

                // Set individual and update cache
                temp = islands[j].group[idx];
                islands[j].group[idx] = cache[k];
                cache[k] = temp;
            }
        }

        return;

    }
}
