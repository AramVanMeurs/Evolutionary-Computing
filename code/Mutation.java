import java.util.Random;
import java.util.Properties;

public class Mutation
{
    /* Implements the Mutation class for an Evolutionary algorithm.
     * This class contains mutation methods which can be applied to
     * the values of an array of Individual objects.
     *
     * All methods apply the mutations in place.
     */

    // Implements uniform mutation for real vector
    static void uniform(Random rnd, double prob, Individual[] children){
        for(int i = 0; i < children.length; i++){
            for(int j = 0; j < children[i].value.length; j++){
                if(rnd.nextDouble() <= prob){
                    children[i].value[j] = (children[i].maxValue - children[i].minValue) * 
                                           rnd.nextDouble() + children[i].minValue;
                }
            }
        }

        return;
    }

    // Implements simple Gaussian perturbation mutation using individual variance(s)
    static void simpleGaussian(Random rnd, Individual[] children){
        for(int i = 0; i < children.length; i++){
            for(int j = 0; j < children[i].value.length; j++){
                children[i].value[j] += children[i].std[j] * rnd.nextGaussian();

                // Clip value to the domain of the problem
                if(children[i].value[j] < children[i].minValue)
                    children[i].value[j] = children[i].minValue;
                if(children[i].value[j] > children[i].maxValue)
                    children[i].value[j] = children[i].maxValue;
            }
        }

        return;
    }

    // Implements Gaussian mutation with changing step sizes
    static void uncorrelatedAdaptiveGaussian(Random rnd, Individual[] children, double lr1, double lr2, double eps){
        double baseMutation;
        double stepFactor;

        // Apply mutation to std values
        for(int i = 0; i < children.length; i++){
            baseMutation = lr1 * rnd.nextGaussian();
            for(int j = 0; j < children[i].value.length; j++){
                stepFactor = Math.exp(baseMutation + lr2 * rnd.nextGaussian());
                children[i].std[j] = children[i].std[j] * stepFactor;

                if(children[i].std[j] < eps)
                    children[i].std[j] = eps;
            }
        }

        // Apply Gaussian perturbation
        simpleGaussian(rnd,children);
    }

    /* Implements creep mutation: add small (positive/negative) value to gene value, 
     * with higher probability of small changes rather than large changes 
     */
    static void creepMutation(Random rnd, double occurProb, double largeChangeProb, 
                              double changeMultiplier, Individual[] children){

        for(int i=0; i<children.length; i++){
            for(int j=0; j<children[i].value.length; j++){
                double random1 = rnd.nextDouble();
                if(random1 <= occurProb){
                    double random2 = rnd.nextDouble();
                    double change = rnd.nextDouble() * 2 - 1; //Random number between -1 and 1
                    if(random2 <= largeChangeProb){
                        //Large change
                        //System.out.println("Large before: " + children[i].value[j]);
                        children[i].value[j] += change * changeMultiplier;
                        //System.out.println("Large after: " + children[i].value[j]);
                    }else{
                        //Small change
                        //System.out.println("Small before: " + children[i].value[j]);
                        children[i].value[j] += change * (1/changeMultiplier);
                        //System.out.println("Small after: " + children[i].value[j]);
                    }
                    if(children[i].value[j] < children[i].minValue)
                        children[i].value[j] = children[i].minValue;
                    if(children[i].value[j] > children[i].maxValue)
                        children[i].value[j] = children[i].maxValue;
                }
            }
        }
        return;
    }

}
