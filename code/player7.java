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
        // Run parameters
        int evals = 0;
        int epochs = 0;

        // Island model parameters
        int islands = 1;
        int migration = 25;
        int numSwap = 2;
        String policy = "best";
        boolean shuffle = false;

        // Individual parameters
        double minVal = -5;
        double maxVal = 5;
        double std = 1.0;
        int dim = 10;

        // Population parameters
        int popSize = 10;

        // Island run parameters
        Individual[] matingPool;
        Population offspring;
        Population island;

        // Parent selection parameters
        double rankingParam = 1.5;
        int numParents = 30;

        // Recombination parameters
        double alpha = 0.5;
        int allele = 5;

        // Mutation parameters
        double mProb = 0.1;
        double lr1 = 1 / Math.sqrt(2.0 * dim);
        double lr2 = 1 / Math.sqrt(2.0 * Math.sqrt(dim));
        double eps = 0.001;

        // Survivor selection parameters
        int numReplace = 5;
        int numRivals = 5;

        // Initialize archipelago
        Population[] archipelago = new Population[islands];
        for(int i = 0; i < islands; i++){
            archipelago[i] = new Population(this.rnd_,minVal,maxVal,dim,std,popSize);

            for(int j = 0; j < archipelago[i].group.length; j++){
                archipelago[i].group[j].setFitness((Double) evaluation_.evaluate(archipelago[i].group[j].value));
                evals++;
            }
        }

        // calculate fitness
        while(evals<evaluations_limit_){

            // Run Evolution Algorithm for each island
            for(int i = 0; i < islands; i++){
                // Set current island
                island = archipelago[i];

                // Select parents
                matingPool = Selection.parentLinearRanking(this.rnd_,island,rankingParam,numParents);

                // Apply crossover operator
                offspring = Recombination.blendCrossover(this.rnd_,matingPool,alpha);

                // Apply mutation operator
                Mutation.uncorrelatedAdaptiveGaussian(this.rnd_,offspring.group,lr1,lr2,eps);

                // Check fitness of unknown fuction
                for(int j = 0; j < offspring.group.length; j++){
                    offspring.group[j].setFitness((Double) evaluation_.evaluate(offspring.group[j].value));
                    evals++;
                }

                // Select survivors
                Selection.survivorRoundRobin(this.rnd_,island,offspring,numRivals);

                // Increment ages of survivors by 1
                island.incrementAges(1);
            }

            // Migrate individuals between islands after an amount of epochs
            if(++epochs == migration && islands > 1){
                Population.migrate(this.rnd_,archipelago,numSwap,policy,shuffle);
                epochs = 0;
            }

            // Break if too many evals needed for next generation
            if(evals >= (evaluations_limit_ - numParents*islands)){
                break;
            }
        }
	}
}
