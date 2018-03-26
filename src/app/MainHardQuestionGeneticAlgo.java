/* CONSOLE OUTPUT
 * 
Lunching Genetic Algorithm for searching Dominating Set for given tweeter network
Loading graph
Graph loaded: 256491 nodes and 328132 edges
Genetic algorithm lunched. Max number of generations: 1. Searched delta: 100.
Generating start population
Population of 8 dominating sets imported
Generation nb: 0 - best solution: 217715 nodes
Individuals to crossing and mutation randomly selected. firstDS: 217804 nodes  secondDS: 218991 nodes
Crossing - finding common nodes: 
Progress: 0%  10%  20%  30%  40%  50%  60%  70%  80%  90%  100%   Complete: 244s
Exchanging 95% nodes randomely
Progress:  Complete
Mutation: creating the remaining graph of nodes not attached to child: 0s
Mutation: finding dominating set for remaining graph nodes using random greedy algorithm method: 2s
Mutation: creating the remaining graph of nodes not attached to child: 0s
Mutation: finding dominating set for remaining graph nodes using random greedy algorithm method: 0s
Individuals to crossing and mutation randomly selected. firstDS: 219983 nodes  secondDS: 219997 nodes
Crossing - finding common nodes: 
Progress: 0%  10%  20%  30%  40%  50%  60%  70%  80%  90%  100%   Complete: 246s
Exchanging 95% nodes randomely
Progress:  Complete
Mutation: creating the remaining graph of nodes not attached to child: 0s
Mutation: finding dominating set for remaining graph nodes using random greedy algorithm method: 2s
Mutation: creating the remaining graph of nodes not attached to child: 0s
Mutation: finding dominating set for remaining graph nodes using random greedy algorithm method: 0s
Individuals to crossing and mutation randomly selected. firstDS: 217806 nodes  secondDS: 217800 nodes
Crossing - finding common nodes: 
Progress: 0%  10%  20%  30%  40%  50%  60%  70%  80%  90%  100%   Complete: 275s
Exchanging 95% nodes randomely
Progress:  Complete
Mutation: creating the remaining graph of nodes not attached to child: 0s
Mutation: finding dominating set for remaining graph nodes using random greedy algorithm method: 1s
Mutation: creating the remaining graph of nodes not attached to child: 0s
Mutation: finding dominating set for remaining graph nodes using random greedy algorithm method: 0s
Individuals to crossing and mutation randomly selected. firstDS: 218994 nodes  secondDS: 217715 nodes
Crossing - finding common nodes: 
Progress: 0%  10%  20%  30%  40%  50%  60%  70%  80%  90%  100%   Complete: 331s
Exchanging 95% nodes randomely
Progress:  Complete
Mutation: creating the remaining graph of nodes not attached to child: 0s
Mutation: finding dominating set for remaining graph nodes using random greedy algorithm method: 2s
Mutation: creating the remaining graph of nodes not attached to child: 0s
Mutation: finding dominating set for remaining graph nodes using random greedy algorithm method: 0s
Generation nb: 1 - best solution: 217715 nodes
 */
package app;

import java.util.Set;

import dominatingset.GeneticAlgorithm;
import graph.CapGraph;
import graph.Graph;
import util.GraphLoader;
import util.ResultReaderWriter;

// @author Karol Cichosz

public class MainHardQuestionGeneticAlgo {

	private static final String resultFilePath = "";
	// OR
	// public static final String resultFilePath =
	// "data/results/resultGeneticAlgo.txt";

	private static final String initDSFilePath = "data/startPopulation.txt";
	// OR
	// private static final String initDSFilePath = "";

	public static void main(String[] args) {
		System.out.println("Lunching Genetic Algorithm for searching Dominating Set for given tweeter network");

		//loading graph
		Graph graph = new CapGraph();
		System.out.println("Loading graph");
		GraphLoader.loadGraph(graph, "data/higgs-retweet_network.edgelist");
		System.out.println("Graph loaded: " + ((CapGraph) graph).getVertexes().size() + " nodes and "
				+ new Integer(((CapGraph) graph).getNumOfEdges()) + " edges");
		GeneticAlgorithm geneticAlgo = new GeneticAlgorithm((CapGraph) graph, initDSFilePath);

		//presenting genetic algorithm
		int numOfGenerations = 1;
		int requiredBetterSolution = 100;
		Set<Integer> result = geneticAlgo.getMinimumDominatingSet(numOfGenerations, requiredBetterSolution);

		if (!resultFilePath.equals("")) {
			ResultReaderWriter.writeResultToFile(result, resultFilePath);
		}
	}
}
