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
        Individual[] matingPool;
        Population offspring;
        Population population = new Population(this.rnd_,-5,5,10,0.01,10);

        // Declare test population
        Individual[] test;

        for(int i = 0; i < population.group.length; i++){
            population.group[i].setFitness((Double) evaluation_.evaluate(population.group[i].value));
            evals++;
        }

        // calculate fitness
        while(evals<evaluations_limit_){
            // Select parents
            matingPool = Selection.parentLinearRanking(this.rnd_,population,1.5,30);

            // Apply crossover / mutation operators
            offspring = Recombination.simpleArithmetic(matingPool,0.5,5);
            Mutation.uniform(this.rnd_,0.05,offspring.group);
            //Mutation.simpleGaussian(this.rnd_, population.group);
            //Mutation.creepMutation(this.rnd_, 0.05, 0.15, 2, population.group);

            // Check fitness of unknown fuction
            for(int i = 0; i < offspring.group.length; i++){
                offspring.group[i].setFitness((Double) evaluation_.evaluate(offspring.group[i].value));
                evals++;
            }

            // Select survivors
            Selection.survivorGenerationalRanked(population,offspring);

            // Increment ages of survivors by 1
            population.incrementAges(1);
        }

        System.out.println("Done");

	}
}
