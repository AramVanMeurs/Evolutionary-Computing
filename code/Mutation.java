import java.util.Random;
import java.util.Properties;

public class Mutation
{
    /* Implements the Mutation class for an Evolutionary algorithm.
     * This class contains mutation methods which can be applied to
     * the values of an array of Individual objects.
     *
     * All methods apply the mutations in place.
     *
     * TODO:
     * - Uncorrelated Nonuniform mutation methods with step size
     * - Correlated Nonuniform mutation methods
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

    // Implements simple Gaussian mutation with a static variance
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
    
    //Implements creep mutation: add small (positive/negative) value to gene value, with higher probability of small changes rather than large changes
    static void creepMutation(Random rnd, double occurProb, double largeChangeProb, double changeMultiplier, Individual[] children){
    	for(int i=0; i<children.length; i++){
    		for(int j=0; j<children[i].value.length; j++){
    			double random1 = rnd.nextDouble();
    			if(random1 <= occurProb){
    				double random2 = rnd.nextDouble();
    				double change = rnd.nextDouble() * 2 - 1; //Random number between -1 and 1
    				if(random2 <= largeChangeProb){
    					//Large change
    					children[i].value[j] += change * changeMultiplier;
    				}else{
    					//Small change
    					children[i].value[j] += change * (1/changeMultiplier);
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
