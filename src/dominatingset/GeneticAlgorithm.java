package dominatingset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import graph.CapGraph;
import graph.GraphVertex;
import util.ResultReaderWriter;

// @author Karol Cichosz

public class GeneticAlgorithm extends GreedyAlgorithm {

	private String startPopulationFilePath = "data/startPopulation.txt";

	private PriorityQueue<Set<Integer>> population;
	private static int populationSize = 8;
	private List<Set<Integer>> initDominatingSet;
	private static float mutationRatio = 0.05f;

	// x times
	// select population to crossing
	// make crossing
	// mutateIndywiduals.

	public GeneticAlgorithm(CapGraph graph) {
		super(graph);
		population = null;
		initDominatingSet = null;
	}

	public GeneticAlgorithm(CapGraph graph, boolean createPopulationFromScratch) {
		this(graph);

		if (createPopulationFromScratch) {
			this.startPopulationFilePath = "";
		}
	}

	public GeneticAlgorithm(CapGraph graph, String initDominatingSetFilePath) {
		this(graph);
		this.startPopulationFilePath = initDominatingSetFilePath;
		if (!startPopulationFilePath.equals("")) {
			this.initDominatingSet = ResultReaderWriter.readResultsFromFile(initDominatingSetFilePath);
		}
	}

	//dedicated genetic algoritm
	public Set<Integer> getMinimumDominatingSet(int maxNumOfLoops, int deltaInSolutions) {
		System.out.println("Genetic algorithm lunched. Max number of generations: " + maxNumOfLoops
				+ ". Searched delta: " + deltaInSolutions + ".");

		//boundary cases
		if (maxNumOfLoops <= 0 || deltaInSolutions < 0) {
			throw new IllegalArgumentException("Wrong arguments");
		}

		if (graph == null) {
			return null;
		}
		if (graph.getVertexes().size() == 1) {
			Set<Integer> result = new HashSet<Integer>();
			result.add(graph.getVertexes().keySet().iterator().next());
			return result;
		}

		// generate start population
		population = createStartPopulation(populationSize);
		
		//boundary case
		if ((population.peek().size() == 1) || (population.peek().size() == 0)) {
			return population.peek();
		}
		Set<Integer> firstSolution;
		int loopNumber = 0;

		firstSolution = population.peek();
		System.out.println("Generation nb: " + loopNumber + " - best solution: " + firstSolution.size() + " nodes");

		//for each generation
		while ((++loopNumber <= maxNumOfLoops)
				|| (firstSolution.size() - population.peek().size() >= deltaInSolutions)) {

			//created next population
			population = nextGeneration(population);
			System.out.println(
					"Generation nb: " + loopNumber + " - best solution: " + population.peek().size() + " nodes");
		}

		return population.peek();

	}

	// initialising population
	private PriorityQueue<Set<Integer>> createStartPopulation(int sizeOfPopulation) {
		PriorityQueue<Set<Integer>> population = new PriorityQueue<Set<Integer>>(sizeOfPopulation,
				new DominatingSetComparator());
		
		boolean solutionFound = false;
		
		System.out.println("Generating start population");
		//if startPopulation must be red from file
		if (!startPopulationFilePath.equals("")) {
			this.initDominatingSet = ResultReaderWriter.readResultsFromFile(startPopulationFilePath);
			System.out.println("Population of " + this.initDominatingSet.size() + " dominating sets imported");
		} else {
			//generating start population each improved greedy algorithm method
			Set<Integer> initDS;
			this.initDominatingSet = new ArrayList<Set<Integer>>();
			for (int i = 0; i < this.getAlgos().length && i < sizeOfPopulation && !solutionFound; i++) {
				initDS = this.getDominatingSetBySelectedAlgo(this.getAlgos()[i]);
				if (initDS.size() == 1) {
					solutionFound = true; //for boundary case
				}
				this.initDominatingSet.add(initDS);
			}
		}

		int popCounter = this.initDominatingSet.size();
		Set<Integer> ds;

		//for greater population
		while ((popCounter++ < sizeOfPopulation) && !solutionFound){

			//use improved greedy algorithm random method
			ds = this.getDominatingSetBySelectedAlgo(	
					this.getAlgos()[randGenerator.nextInt(this.getAlgos().length - 1) + 1]);
			this.initDominatingSet.add(ds);

			System.out.println(
					new Integer(popCounter).toString() + "th individual included - size: " + ds.size() + " nodes");
		}

		population.addAll(this.initDominatingSet);
		return population;
	}

	//creating children
	private List<Set<Integer>> crossingAndMutation(Set<Integer> firstIndividual, Set<Integer> secondIndividual) {
		List<Set<Integer>> children = new ArrayList<Set<Integer>>();

		List<Integer> shorterParent, longerParent;

		// who is shorter
		if (firstIndividual.size() < secondIndividual.size()) {
			shorterParent = new ArrayList<Integer>(firstIndividual);
			longerParent = new ArrayList<Integer>(secondIndividual);
		} else {
			shorterParent = new ArrayList<Integer>(secondIndividual);
			longerParent = new ArrayList<Integer>(firstIndividual);
		}

		// finding common items
		Set<Integer> commonSet = new HashSet<Integer>();
		System.out.println("Crossing - finding common nodes: ");
		System.out.print("Progress: ");
		int progressCounter = 0;
		long startTime = System.currentTimeMillis();
		//by going through shorter dominating set
		for (int i = 0; i < shorterParent.size(); i++) {
			if (longerParent.contains(shorterParent.get(i))) {
				commonSet.add(shorterParent.get(i));
			}

			//information status every 10%
			if ((shorterParent.size()>10) && (i % (shorterParent.size() / 10) == 0)) {
				System.out.print(new Integer(progressCounter++ * 10).toString() + "%  ");
			}
		}

		System.out.println(
				" Complete: " + new Integer((int) ((System.currentTimeMillis() - startTime) / 1000)).toString() + "s");

		// common items are transfered to the next generation
		children.add(commonSet);
		children.add(commonSet);
		//linkedList is more efficient then ArrayList as regards to removing elements
		shorterParent = new LinkedList<Integer>(shorterParent);
		longerParent = new LinkedList<Integer>(longerParent);

		shorterParent.removeAll(commonSet);
		longerParent.removeAll(commonSet);

		// distrubute parents nodes among children
		int crossingSize = ((int) (1 - mutationRatio) * shorterParent.size() / 2);
		System.out.println(
				"Exchanging " + new Integer((int) ((1 - mutationRatio) * 100)).toString() + "% nodes randomely");
		System.out.print("Progress: ");

		progressCounter = 0;

		for (int i = 0; i < crossingSize; i++) {
			children.get(0).add(shorterParent.remove(randGenerator.nextInt(shorterParent.size())));
			children.get(0).add(longerParent.remove(randGenerator.nextInt(longerParent.size())));
			children.get(1).add(shorterParent.remove(randGenerator.nextInt(shorterParent.size())));
			children.get(1).add(longerParent.remove(randGenerator.nextInt(longerParent.size())));

			//information
			if ((crossingSize > 10) && (i % (crossingSize / 10) == 0)) {
				System.out.print(new Integer(progressCounter++ * 10).toString() + "%  ");
			}
		}
		System.out.println(" Complete");

		// implement mutation
		children.set(0, mutateChild(children.get(0)));
		children.set(1, mutateChild(children.get(1)));

		return children;
	}

	//finding dominating set in remaining part of graph
	private Set<Integer> mutateChild(Set<Integer> child) {
		System.out.print("Mutation: creating the remaining graph of nodes not attached to child: ");
		long startTime = System.currentTimeMillis();
		//remove nodes which are already in the solution
		CapGraph childGraph = removeVertexesFromGraph(child);
		System.out.println(new Integer((int) ((System.currentTimeMillis() - startTime) / 1000)).toString() + "s");

		//find dominating set by greedy algoritm
		GreedyAlgorithm greedy = new GreedyAlgorithm(childGraph);

		startTime = System.currentTimeMillis();
		//random method
		System.out.print(
				"Mutation: finding dominating set for remaining graph nodes using random greedy algorithm method: ");
		child.addAll(greedy.getDominatingSetBySelectedAlgo(this.algos[randGenerator.nextInt(this.algos.length)]));
		System.out.println(new Integer((int) ((System.currentTimeMillis() - startTime) / 1000)).toString() + "s");

		return child;
	}

	//removing unnecessary nodes and edges 
	private CapGraph removeVertexesFromGraph(Set<Integer> vertexesToRemove) {
		CapGraph result = new CapGraph();

		for (GraphVertex vertex : this.graph.getVertexes().values()) {
			if (!vertexesToRemove.contains(vertex.getVertexNumber())) {
				result.addVertex(vertex.getVertexNumber());
				for (Integer secondVertex : vertex.getSecondVertexes()) {
					if (!vertexesToRemove.contains(secondVertex)) {
						result.addVertex(secondVertex);
						result.addEdge(vertex.getVertexNumber(), secondVertex);
					}
				}
			}
		}

		return result;
	}

	//creating next generation of given population
	private PriorityQueue<Set<Integer>> nextGeneration(PriorityQueue<Set<Integer>> generation) {
		LinkedList<Set<Integer>> generationList = new LinkedList<Set<Integer>>(generation);

		Set<Integer> firstToCross, secondToCross;

		//selecting pairs of parents
		while (!generationList.isEmpty()) {
			firstToCross = generationList.remove(randGenerator.nextInt(generationList.size()));
			secondToCross = generationList.remove(randGenerator.nextInt(generationList.size()));

			System.out.println("Individuals to crossing and mutation randomly selected. firstDS: " + firstToCross.size()
					+ " nodes  secondDS: " + secondToCross.size() + " nodes");

			//create 2 children of selected 2 parents
			generation.addAll(crossingAndMutation(firstToCross, secondToCross));
		}

		//saving 50% of best individuals for next generation
		PriorityQueue<Set<Integer>> nextGeneration = new PriorityQueue<Set<Integer>>(populationSize,
				new DominatingSetComparator());
		for (int i = 0; i < populationSize; i++) {
			nextGeneration.add(generation.poll());
		}
		return nextGeneration;
	}
}
