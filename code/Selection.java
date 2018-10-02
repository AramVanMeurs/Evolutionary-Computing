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
     * (Any suggestions for change or improvement?)
     *
     * TODO:
     * - Implement tournament selection
     * - Implement ranking based selection
     * - Implement survivor selection methods
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

    /* Implements tournament sampling used for tournament selection */
    static int[] sampleTournament(Random rnd, Individual[] group, int tourSize, int numChildren){
        int[] matingPool = new int[numChildren];
        int[] candidates = new int[tourSize];
        int bestCandidate;

        for(int i = 0; i < numChildren; i++){
            // Sample candidates
            for(int j = 0; j < tourSize; j++)
                candidates[j] = rnd.nextInt(group.length);

            // Choose fittest candidate
            bestCandidate = candidates[0];
            for(int j = 1; j < tourSize; j++){
                if(group[candidates[j]].fitness > group[bestCandidate].fitness){
                    bestCandidate = candidates[j];
                }
            }

            matingPool[i] = bestCandidate;
        }

        return matingPool;
    }

    /* Implements ranking parent selection */
    static void ranking(Random rnd){
        return;
    }

    /* Implements tournament parent selection */
    static void tournament(Random rnd){
        return;
    }

    /* Implements uniform parent selection */
    static int[] uniform(Random rnd, int popSize, int num){
        int[] parents = new int[num];

        for(int i = 0; i < num; i++)
            parents[i] = rnd.nextInt(popSize);

        return parents;
    }

    void replaceWorst(Random rnd){
        return;
    }
}
