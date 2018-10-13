import java.util.Random;
import java.util.Properties;

public class Recombination
{
    /* Implements the Recombination class for an Evolutionary algorithm.
     * This class contains recombination methods which can be used to
     * produce the offspring of an array of Individual objects.
     *
     * TODO:
     * - Incorporate adaptive step size into recombination?
     * - Implement other recombination operators not in the book.
     */

    /* Implements simple arithmetic recombination.
     * Use crossPoint = 0 for whole arithmetic recombination
     */
    static Population simpleArithmetic(Individual[] matingPool, double alpha, int crossPoint){
        Individual[] offspring = new Individual[matingPool.length];

        double minVal = matingPool[0].minValue;
        double maxVal = matingPool[0].maxValue;
        int length = matingPool[0].value.length;

        double crossVal;
        double crossDev;

        for(int i = 0; i < matingPool.length; i += 2){
            offspring[i] = new Individual(minVal,maxVal,length);
            offspring[i+1] = new Individual(minVal,maxVal,length);

            // Copy values into offspring until crossover point
            for(int j = 0; j < crossPoint; j++){
                offspring[i].value[j] = matingPool[i].value[j];
                offspring[i+1].value[j] = matingPool[i+1].value[j];

                offspring[i].std[j] = matingPool[i].std[j];
                offspring[i+1].std[j] = matingPool[i+1].std[j];
            }

            // Apply arithmetic average from crossover point onwards
            for(int j = crossPoint; j < length; j++){
                // Child 1
                crossVal = alpha * matingPool[i+1].value[j] + (1-alpha) * matingPool[i].value[j];
                offspring[i].value[j] = crossVal;

                crossDev = alpha * matingPool[i+1].std[j] + (1-alpha) * matingPool[i].std[j];
                offspring[i].std[j] = crossDev;

                // Child 2
                crossVal = alpha * matingPool[i].value[j] + (1-alpha) * matingPool[i+1].value[j];
                offspring[i+1].value[j] = crossVal;

                crossDev = alpha * matingPool[i].std[j] + (1-alpha) * matingPool[i+1].std[j];
                offspring[i+1].std[j] = crossDev;
            }
        }

        return new Population(offspring);
    }

    /* Implements single arithmetic recombination */
    static Population singleArithmetic(Random rnd, Individual[] matingPool, double alpha){
        Individual[] offspring = new Individual[matingPool.length];

        double minVal = matingPool[0].minValue;
        double maxVal = matingPool[0].maxValue;
        int length = matingPool[0].value.length;

        double crossVal;
        double crossDev;
        int allele;

        for(int i = 0; i < matingPool.length; i += 2){
            offspring[i] = new Individual(minVal,maxVal,length);
            offspring[i+1] = new Individual(minVal,maxVal,length);

            // Copy values into offspring
            for(int j = 0; j < length; j++){
                offspring[i].value[j] = matingPool[i].value[j];
                offspring[i+1].value[j] = matingPool[i+1].value[j];

                offspring[i].std[j] = matingPool[i].std[j];
                offspring[i+1].std[j] = matingPool[i+1].std[j];
            }

            // Apply arithmetic average on allele of parents for allele of offspring
            allele = rnd.nextInt(length);

            // Child 1
            crossVal = alpha * matingPool[i+1].value[allele] + 
                       (1 - alpha) * matingPool[i].value[allele];

            offspring[i].value[allele] = crossVal;

            crossDev = alpha * matingPool[i+1].std[allele] + 
                       (1 - alpha) * matingPool[i].std[allele];

            offspring[i].std[allele] = crossDev;

            // Child 2
            crossVal = alpha * matingPool[i].value[allele] + 
                       (1 - alpha) * matingPool[i+1].value[allele];

            offspring[i+1].value[allele] = crossVal;

            crossDev = alpha * matingPool[i].std[allele] + 
                       (1 - alpha) * matingPool[i+1].std[allele];

            offspring[i+1].std[allele] = crossDev;
        }

        return new Population(offspring);
    }

    /* Implements blend crossover */
    static Population blendCrossover(Random rnd, Individual[] matingPool, double alpha){
        Individual[] offspring = new Individual[matingPool.length];

        double minVal = matingPool[0].minValue;
        double maxVal = matingPool[0].maxValue;
        int length = matingPool[0].value.length;

        double gamma;
        double crossVal;
        double crossDev;

        for(int i = 0; i < matingPool.length; i += 2){
            offspring[i] = new Individual(minVal,maxVal,length);
            offspring[i+1] = new Individual(minVal,maxVal,length);

            // Sample blend parameter
            gamma = (1 + 2 * alpha) * rnd.nextDouble() - alpha;

            // Apply arithmetic average using gamma
            for(int j = 0; j < length; j++){
                // Child 1
                crossVal = gamma * matingPool[i+1].value[j] + (1-gamma) * matingPool[i].value[j];
                offspring[i].value[j] = crossVal;

                crossDev = gamma * matingPool[i+1].std[j] + (1-gamma) * matingPool[i].std[j];
                offspring[i].std[j] = crossDev;

                // Child 2
                crossVal = gamma * matingPool[i].value[j] + (1-gamma) * matingPool[i+1].value[j];
                offspring[i+1].value[j] = crossVal;

                crossDev = gamma * matingPool[i].std[j] + (1-gamma) * matingPool[i+1].std[j];
                offspring[i+1].std[j] = crossDev;
            }
        }

        return new Population(offspring);
    }

}
