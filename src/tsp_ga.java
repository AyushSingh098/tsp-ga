import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class tsp_ga 
{
    private static int populationSize = 100;
    private static int elitism = 1;
    private static int maxGenerations = 1000;
    private static Random random = new Random();
    private static int numberOfCities = 11;
    private static double[][] distance;

    private static int[][] currentPopulation;
    private static int[][] intermediatePopulation;
    private static double[] fitness;
    private static int bestSolutionSoFar;
    private static double bestFitnessSoFar;
    private static int[] referenceSolution;
 
    public static void main(String[] s) 
    {
        int generation;
        distance = new double[numberOfCities][numberOfCities];
        currentPopulation = new int[populationSize][numberOfCities];
        intermediatePopulation = new int[populationSize][numberOfCities];
        fitness = new double[populationSize];
        referenceSolution = new int[numberOfCities];
 
        //Read the cost matrix from file
        readTheDistanceMatrix();
        
        //Form the first set of population
        initialiseThePopulation();
        
        System.out.println("__________________ ANALYSIS OF THE INITIAL POPULATION _____________");
        evaluateThePopulation();
        
        generation = 1;
 
        while (generation < maxGenerations) 
        {
        	System.out.println();
            System.out.println("_________________________ GENERATION : " + generation + " _________________________");
            System.out.println();
            produceTheNextGeneration();
            evaluateThePopulation();
            generation++;
        }
    }
    
    private static void readTheDistanceMatrix() 
    {
    	Scanner scanner;
        distance = new double[numberOfCities][numberOfCities];
        try 
        {
        	//Input the file path
        	scanner = new Scanner(new File("/Users/ayushsingh/eclipse-workspace/TSPGA/sp11.txt"));
			//Cost Matrix
			for(int i = 0; i < numberOfCities; i++)
			{
				for(int j = 0; j < numberOfCities && scanner.hasNextInt(); j++)
				{
					int nxtInt = scanner.nextInt();
					//city[i][j] --> -1 : Not connected
					if(nxtInt < 0)							
						distance[i][j] = 99999;
					else
						distance[i][j] = nxtInt;
				}
			}
        }
        catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
        //Print the Cost Matrix
        System.out.println("___________________The Distance Matrix_______________________");
        System.out.println();
        for(int a = 0;a<numberOfCities;a++)
        {
          for(int b = 0;b<numberOfCities;b++)
          {
          	System.out.print(distance[a][b]+ " ");
          }
          System.out.println();
        }
        System.out.println();
    }
 
    private static void initialiseThePopulation() 
    {
        int chromosome, i, thisCity, refLength;
        
        //Initial Best solution is set to 0.0
        bestFitnessSoFar = 0.0;
 
        //For each chromosome in the population
        for (chromosome = 0; chromosome < populationSize; chromosome++) 
        {
            // we will now efficiently generate a random permutation of 
            // the integers  0, 1, ..., N-1  where N is number_of_cities
 
            // this starts with a 'reference' permutation that is simply 0, 1, 2, ..., N-1
            for (i = 0; i < numberOfCities; i++) 
            {
                referenceSolution[i] = i;
            }
 
            // ref_length gives the current length of the ref solution; it will gradually reduce
         
            //refLength = numberOfCities;
            refLength=referenceSolution.length;
//making changes now(1)
            thisCity = 0;//gets a random number between 0 and integer less than refLength
            currentPopulation[chromosome][0] = referenceSolution[0];
            referenceSolution[0] = referenceSolution[refLength - 1];
//changes(1) success
            // now repeatedly take random genes from the reference population and build the current chromosome
            for (i = 1; i < numberOfCities; i++) 
            {
                // this_city = rand() % ref_length;
                thisCity = getRandomNumberBetween(1, refLength);//gets a random number between 0 and integer less than refLength
                currentPopulation[chromosome][i] = referenceSolution[thisCity];
                referenceSolution[thisCity] = referenceSolution[refLength - 1];
                refLength--;
            }
        }
        System.out.println("INITIAL POPULATION FORMED : ");
        System.out.println();
        System.out.println();
    }
 
 
    private static void evaluateThePopulation() 
    {
        int chromosome, city, city1, city2;
        double total_distance;
        //For each chromosome in the population
        for (chromosome = 0; chromosome < populationSize; chromosome++) 
        {
            //evaluate this chromosome;
            total_distance = 0;
            
            //Find the total distance of that candidate solution
            for (city = 0; city < numberOfCities; city++) 
            {
                city1 = currentPopulation[chromosome][city];
                city2 = currentPopulation[chromosome][(city + 1) % numberOfCities];
                total_distance += distance[city1][city2];
            }
            if (total_distance == 0) 
            {
                System.out.println("ERROR");
                System.exit(0);
            }
 
            //FITNESS FUNCTION
            // Using Roulette Wheel selection in this GA. 
            //Fitness Function = 1/distance: (because we need to maximise the fitness value
            fitness[chromosome] = 1.0 / total_distance;
 
            //Keep track of best so far
            if (fitness[chromosome] >= bestFitnessSoFar) 
            {
                bestFitnessSoFar = fitness[chromosome];
                bestSolutionSoFar = chromosome;
            }
        }
 
        // print the population and fitnesses
        for (chromosome = 0; chromosome < populationSize; chromosome++) 
        {
            if (chromosome == bestSolutionSoFar) 
            {
                System.out.print("  BEST CHROMOSOME SO FAR : ");
            }
            for (city1 = 0; city1 < numberOfCities; city1++) 
            {
                System.out.print(currentPopulation[chromosome][city1] + " ");
            }
 
            System.out.print("FITNESS VALUE - " + fitness[chromosome] + " (DISTANCE : " + Math.round(1.0 / fitness[chromosome]) + ")\n");
 
        }
        System.out.print("\n");
    }
 
    
    //position-based crossover. 
    private static int[] crossover(int parent1, int parent2, int[] child) 
    {
        int[] genes_present, chosen;
        genes_present = new int[numberOfCities];
        chosen = new int[numberOfCities];
        int i, thisgene, place = 0;
 
        int rnd = getRandomNumberBetween(0, 1000);
        for (i = 0; i < numberOfCities; i++) 
        {
            if (rnd < 500) 
            {
                chosen[i] = 1;
            } else 
            {
                chosen[i] = 0;
            }
        }
 
        // set child to be same as parent1 in these positions
        for (i = 0; i < numberOfCities; i++) 
        {
            if (chosen[i] == 1) 
            {
                child[i] = currentPopulation[parent1][i];
            }
        }
 
        // now record the genes that the child already has
        // (first need to initialise this array)
        for (i = 0; i < numberOfCities; i++) 
        {
            genes_present[i] = 0;
        }
        for (i = 0; i < numberOfCities; i++) 
        {
            if (chosen[i] == 1) 
            {
                genes_present[child[i]] = 1;
            }
        }
 
        // now collect the remaining genes from parent2
        for (i = 0; i < numberOfCities; i++) 
        {
            thisgene = currentPopulation[parent2][i];
            if (genes_present[thisgene] == 0) 
            { 
            	//place it in the next unchosen position of the child
                while (chosen[place] == 1) 
                {
                    place++;
                }
                child[place] = thisgene;
                genes_present[thisgene] = 1;
                chosen[place] = 1;
            }
        }
        return child;
    }
 
    private static void produceTheNextGeneration() 
    {
        int newCandidate, parent1, parent2, gene;
        int child[] = new int[numberOfCities];
 
        if (elitism == 1) 
        {
        	//The best chromosome from the previous population is put at the top of current population
            for (gene = 0; gene < numberOfCities; gene++) 
            {
                intermediatePopulation[0][gene] = currentPopulation[bestSolutionSoFar][gene];
            }
        }
 
        for (newCandidate = elitism; newCandidate < populationSize; newCandidate++) 
        {
        	//SELECTION
            parent1 = rouletteWheelSelect();
            parent2 = rouletteWheelSelect();
            //CROSSOVER
            child = crossover(parent1, parent2, child);
            //MUTATION
            child = mutate(child);
 
            for (gene = 0; gene < numberOfCities; gene++) 
            {
                intermediatePopulation[newCandidate][gene] = child[gene];
            }
        }
 
        //Merge the previously selected best population and the new Population formed by selection, crossover and mutation
        for (newCandidate = 0; newCandidate < populationSize; newCandidate++) 
        {
            for (gene = 0; gene < numberOfCities; gene++) 
            {
                currentPopulation[newCandidate][gene] = intermediatePopulation[newCandidate][gene];
            }
        }
    }
    
    //Mutation : Take any two adjacent genes, and swap them 
    private static int[] mutate(int[] child) 
    {
        //change 2 start
        int g = getRandomNumberBetween(1, numberOfCities);
        if(numberOfCities!=(g+1))
        {
            int temp = child[g];
            child[g] = child[(g + 1) % numberOfCities];
            child[(g + 1) % numberOfCities] = temp;         
        }
        return child;
    }
 
    private static int rouletteWheelSelect() 
    {
        double fitTotal, pointer, accumulatingFitness, randReal;
        int chromosome, randint, selected = 0;
 
        fitTotal = 0.0;
        //Find the total fitness of all the chromosomes in that population
        for (chromosome = 0; chromosome < populationSize; chromosome++) 
        {
            fitTotal += fitness[chromosome];
        }
        
        randint = getRandomNumberFrom(0, 1000000);
        randReal = randint / 1000000.0;
        
        //Make the pointer point to any specified range
        pointer = fitTotal * randReal;
        
        //Cumulative fitness of chromosomes selected till now
        accumulatingFitness = 0.0;
 
        while (selected < populationSize) 
        {
            accumulatingFitness += fitness[selected];
            if (pointer < accumulatingFitness) 
            {
                break;
            }
            if (selected != populationSize - 1) 
            {
                selected++;
            }
        }
        return selected;
    }
 
    private static int getRandomNumberBetween(int min, int max) 
    {
    	//finds a random number between (min) and (max-1)
        return random.nextInt(max - min) + min;
    }
 
    public static int getRandomNumberFrom(int min, int max) 
    {
    	//finds a random number between (min) and (max)
        return getRandomNumberBetween(min, max+1);
    }
}