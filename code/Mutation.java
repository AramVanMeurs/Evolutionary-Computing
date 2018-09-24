import java.util.Random;
import java.util.Properties;

public class Mutation
{

    static double min_value = 0.0;
    static double max_value = 10.0;

    static void Uniform(Random rnd, double prob, double[][] children){
        for(int i = 0; i < children.length; i++){
            for(int j = 0; j < children[i].length; j++){
                if(rnd.nextDouble() <= prob){
                    children[i][j] = (Mutation.max_value - Mutation.min_value) * 
                                      rnd.nextDouble() + Mutation.min_value;
                }
            }
        }

        return;
    }

    static void SimpleGaussian(Random rnd, double sigma, double[][] children){
        for(int i = 0; i < children.length; i++){
            for(int j = 0; j < children[i].length; j++){
                children[i][j] += sigma * rnd.nextGaussian();

                // Clip value to the domain of the problem
                if(children[i][j] < Mutation.min_value) children[i][j] = Mutation.min_value;
                if(children[i][j] > Mutation.max_value) children[i][j] = Mutation.max_value;
            }
        }

        return;
    }

}
