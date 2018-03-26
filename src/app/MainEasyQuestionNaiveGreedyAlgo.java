/*	CONSOLE OUTPUT
 * 
Lunching Naive Greedy Algorithm for searching Dominating Set for given tweeter network
Loading graph
Graph loaded: 256491 nodes and 328132 edges
Finding dominating set (easy question): NAIVE algorithm: 220780 nodes - total time: 30s
 */

package app;

// @author Karol Cichosz

import java.util.Set;

import dominatingset.GreedyAlgorithm;
import graph.CapGraph;
import graph.Graph;
import util.GraphLoader;
import util.ResultReaderWriter;

public class MainEasyQuestionNaiveGreedyAlgo {

	private static final String resultFilePath = "";
	// OR
	// public static final String resultFilePath =
	// "data/results/resultNaiveAlgo.txt";

	public static void main(String[] args) {
		System.out.println("Lunching Naive Greedy Algorithm for searching Dominating Set for given tweeter network");

		//loading graph
		Graph graph = new CapGraph();
		System.out.println("Loading graph");
		GraphLoader.loadGraph(graph, "data/higgs-retweet_network.edgelist");
		System.out.println("Graph loaded: " + ((CapGraph) graph).getVertexes().size() + " nodes and "
				+ new Integer(((CapGraph) graph).getNumOfEdges()) + " edges");
		GreedyAlgorithm greedyAlgo = new GreedyAlgorithm((CapGraph) graph);

		//presenting Naive greedy algorithm
		System.out.print("Finding dominating set (easy question): NAIVE algorithm");
		long startTime = System.currentTimeMillis();
		Set<Integer> dominatingSet = greedyAlgo.getDominatingSetByEasyAlgo();
		System.out.println(": " + dominatingSet.size() + " nodes - total time: "
				+ new Integer((int) (System.currentTimeMillis() - startTime) / 1000).toString() + "s");

		if (!resultFilePath.equals("")) {
			ResultReaderWriter.writeResultToFile(dominatingSet, resultFilePath);
		}
	}
}
