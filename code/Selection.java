import java.util.*;
import java.util.Random;
import java.util.Properties;

class Selection
{

    /* Implements the Selection class for an Evolutionary algorithm.
     * This class contains methods for both Parent selection and Survivor selection.
     *
     * As of now, all selection methods are planned to return an array of indices to
     * pass to the Population class or a Population object for constructing a new group.
     */

    /* Implements roulette sampling for 
     * an arbitrary number of random numbers 
     */
    static int[] sampleRoulette(Random rnd, double[] probs, int num){
        int i, member;
        double inv,r,cumDist;

        int[] matingPool = new int[num];
        Integer[] tempPool = new Integer[num];

        inv = 1.0 / (double) num;
        r = inv * rnd.nextDouble();
        cumDist = 0.0;

        i = member = 0;
        while(member < num){

            // Update cumulative distribution
            cumDist += probs[i];

            while(r <= cumDist){
                tempPool[member] = i;
                r += inv;
                member += 1;
            }

            i += 1;
        }

        // Shuffle the ordered random samples
        Collections.shuffle(Arrays.asList(tempPool),rnd);

        // Put values into a primitive array
        for(int j = 0; j < num; j++){
            matingPool[j] = tempPool[j].intValue();
        }

        return matingPool;
    }

    /* Implements parent tournament selection */
    static Individual[] parentTournament(Random rnd, Population pop, int tourSize, int numAccept, int numChildren){
        Individual[] matingPool = new Individual[numChildren];
        Individual[] candidates = new Individual[tourSize];

        for(int i = 0; i < numChildren; i += numAccept){
            // Sample candidates
            for(int j = 0; j < tourSize; j++)
                candidates[j] = pop.group[rnd.nextInt(pop.group.length)];

            // Choose fittest candidates
            Arrays.sort(candidates);
            for(int j = 0; j < numAccept; j++){
                if(i+j < matingPool.length)
                    matingPool[i+j] = candidates[tourSize - 1 - j];
            }
        }

        return matingPool;
    }

    /* Implements linear ranking parent selection */
    static Individual[] parentLinearRanking(Random rnd, Population pop, double s, int numChildren){
        Individual[] matingPool = new Individual[numChildren];

        int[] samples;

        double[] probs = new double[pop.group.length];
        double mu = probs.length;

        // Sort group based on fitness for ranking
        Arrays.sort(pop.group);

        // Compute probabilities from ranking
        for(int i = 0; i < probs.length; i++)
            probs[i] = (2-s) / mu + 2*i*(s-1) / (mu*(mu-1));

        samples = sampleRoulette(rnd,probs,numChildren);

        for(int i = 0; i < samples.length; i++){
            matingPool[i] = pop.group[samples[i]];
        }

        return matingPool;
    }

    /* Implements exponential ranking parent selection */
    static Individual[] parentExponentialRanking(Random rnd, Population pop, int numChildren){
        Individual[] matingPool = new Individual[numChildren];

        int[] samples;

        double[] probs = new double[pop.group.length];
        double normFactor = 0;

        // Sort group based on fitness for ranking
        Arrays.sort(pop.group);

        // Compute probabilities from ranking
        for(int i = 0; i < probs.length; i++){
            probs[i] = 1 - Math.exp(-i);
            normFactor += probs[i];
        }

        // Normalize probabilities
        for(int i = 0; i < probs.length; i++){
            probs[i] /= normFactor;
        }

        samples = sampleRoulette(rnd,probs,numChildren);

        for(int i = 0; i < samples.length; i++){
            matingPool[i] = pop.group[samples[i]];
        }

        return matingPool;
    }

    /* Implements uniform parent selection */
    static Individual[] parentUniform(Random rnd, Population pop, int numChildren){
        Individual[] matingPool = new Individual[numChildren];

        for(int i = 0; i < numChildren; i++)
            matingPool[i] = pop.group[rnd.nextInt(pop.group.length)];

        return matingPool;
    }

    /* Implements Replace Worst selection for survivor selection.
     * Replaces the worst of the population with the best of the offspring.
     * Probably not the same as the GENITOR algorithm described in the book.
     */
    static void survivorReplaceWorst(Population population, Population offspring, 
                                     int numReplace){

        // Sort individuals based on fitness
        Arrays.sort(population.group);
        Arrays.sort(offspring.group);

        // Replace worst of population with best from offspring
        for(int i = 0; i < numReplace; i++){
            population.group[i] = offspring.group[offspring.group.length - numReplace + i];
        }

        return;
    }

    /* Implements round robin tournament selection for survivor selection */
    static void survivorRoundRobin(Random rnd, Population population, 
                                   Population offspring, int numRivals){

        int count, result;
        Individual candidate, rival;

        // Get merged populations
        Population[] pops = {population,offspring};
        Population merged = Population.merge(pops);

        // Get list to shuffle
        ArrayList<Individual> mergedList = Individual.getArrayList(merged.group);

        // Determine wins in tournament
        for(int i = 0; i < merged.group.length; i++){
            // Randomize rivals
            Collections.shuffle(mergedList,rnd);

            // Initialize candidate for tournament
            candidate = merged.group[i];
            candidate.wins = 0;

            count = 0;
            for(int j = 0; j < merged.group.length; j++){
                rival = mergedList.get(j);

                // Candidate does not battle itself
                if(rival != candidate){
                    candidate.wins += (candidate.fitness > rival.fitness ? 1 : 0);
                    count++;
                }

                if(count >= numRivals) break;
            }
        }

        // Sort based on wins
        Arrays.sort(merged.group, (a,b) -> Individual.compareWins(a,b));

        // Insert survivors into population
        for(int i = 0; i < population.group.length; i++){
            population.group[i] = merged.group[merged.group.length - population.group.length + i];
        }

        return;
    }

    /* Implements (mu + lambda) survivor selection */
    static void survivorMergeRanked(Population population, Population offspring){

        Population[] mergePops = {population,offspring};
        Population mergedPopulation;

        // Merge population and offspring
        mergedPopulation = Population.merge(mergePops);

        // Insert best individuals back into population
        Arrays.sort(mergedPopulation.group);
        for(int i = 0; i < population.group.length; i++){
            population.group[i] = mergedPopulation.group[mergedPopulation.group.length - 
                                                         population.group.length + i];
        }

        return;
    }

    /* Implements (mu, lambda) survivor selection */
    static void survivorGenerationalRanked(Population population, Population offspring){

        // Replace full population with best offspring
        Arrays.sort(offspring.group);
        for(int i = 0; i < population.group.length; i++){
            population.group[i] = offspring.group[offspring.group.length - population.group.length + i];
        }       

        return;
    }
}
