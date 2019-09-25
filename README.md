# tsp-ga
Using Genetic Algorithm to find a solution to the Traveling Salesman Problem (TSP). 

This method returns an approximate solution to the NP-hard traveling sales person problem.

# Genetic Algorithms
Genetic algorithms attempt to mimic real life evolution and are commonly used in artificial intelligence and optimization problems. Consider our traveling salesperson problem (TSP). An individual is a single solution to the TSP. 

A population consists of several individuals, all of which are different and need not be unique. Each individual is given a score based on a fitness function. In the TSP, the score is the length of the tour/path. The lower the score an individual has, the better of a solution it is.

Given our starting population, we evolve the population. This requires selecting two parents (from the population of individuals) and crossing them. We take some attributes from parent 1 and some from parent 2 to create a new child. This child is then scored and then placed back into the population. 

The more fit parents are more likely to reproduce and thus the next generation of children should be better off than the parents, that is, they are better solutions to the TSP.

We can introduce mutations, that is, random events that change each individual (with regards to the TSP, it swaps the order of two cities at random). We continue to evolve the population until our population becomes the same solution or we reach some threshold or time limit.

# Parameters
Population Size: This is number of individuals in our population.

Number of Evolution Iterations/Generations: Number of times to advance the population and create offspring/mutations.

Elite : The population to deem as elite individuals. Elite individuals have very high fitness scores (or low path costs).
