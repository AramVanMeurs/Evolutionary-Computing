import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.*;
import java.util.Random;
import java.util.Properties;

public class player7 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;
    private int evaluations_limit_;
	
	public player7()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algortihms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }
    
	public void run()
	{
		// Run your algorithm here
        
        int evals = 0;

        // init population
        Population population = new Population(rnd_,0,10,10,0.01,200);

        for(int i = 0; i < population.group.length; i++){
            population.group[i].setFitness((Double) evaluation_.evaluate(population.group[i].value));
            evals++;
        }

        Individual copy = new Individual(rnd_,0,10,10,0.01);

        for(int i = 0; i < 10; i++)
            copy.value[i] = population.group[0].value[i];

        System.out.println(population.group[0].equals(copy));
        System.out.println(population.group[0] == copy);

        //Arrays.sort(population.group);

        //for(int i = 0; i < population.group.length; i++){
        //    System.out.println(population.group[i].fitness);
        //}

        //System.out.println(population.group[0].value[0]);

        //double[] probs = {0.25,0.5,0.25,0.25};
        //int[] myArray = Selection.uniform(rnd_,100,5);

        //for(int i = 0; i < myArray.length; i++)
        //    System.out.println(myArray[i]);

        // calculate fitness
        while(evals<evaluations_limit_){
            // Select parents
            // Apply crossover / mutation operators
            Mutation.simpleGaussian(this.rnd_, population.group);

            //System.out.println(population.group[0].value[0]);

            double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            // Check fitness of unknown fuction
            Double fitness = (double) evaluation_.evaluate(child);

            //System.out.println("Fitness: " + fitness);

            evals++;
            // Select survivors
        }

        System.out.println("Done");

	}
}
