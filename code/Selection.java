import java.util.*;
import java.util.Random;
import java.util.Properties;

class Selection
{

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
    static int[] sampleTournament(Random rnd, double[] fitnesses, int tourSize, int num){
        int[] matingPool = new int[num];
        int[] candidates = new int[tourSize];
        int bestCandidate;

        for(int i = 0; i < num; i++){
            // Sample candidates
            for(int j = 0; j < tourSize; j++)
                candidates[j] = rnd.nextInt(fitnesses.length);

            // Choose fittest candidate
            bestCandidate = candidates[0];
            for(int j = 1; j < tourSize; j++){
                if(fitnesses[bestCandidate] < fitnesses[candidates[j]]){
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
