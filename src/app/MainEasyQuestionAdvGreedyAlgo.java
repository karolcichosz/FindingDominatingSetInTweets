/* CONSOLE OUTPUT
 * 
Lunching Advanced Greedy Algorithm Methods for searching Dominating Set for given tweeter network
Loading graph
Graph loaded: 256491 nodes and 328132 edges
Finding dominating set (easy question): TOP_EDGE method: 217804 nodes - total time: 72s
Finding dominating set (easy question): ONEFORTH_OF_TOP_EDGES method: 217803 nodes - total time: 147s
Finding dominating set (easy question): RANDOM_EDGE method: 219955 nodes - total time: 81s
Finding dominating set (easy question): ONCE_TOP_ONCE_RANDOM_EDGE method: 218974 nodes - total time: 64s
Finding dominating set (easy question): TOP_RANDOM_EDGE method: 217718 nodes - total time: 88s 
 */

package app;

 // @author Karol Cichosz

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dominatingset.GreedyAlgorithm;
import graph.CapGraph;
import graph.Graph;
import util.GraphLoader;
import util.ResultReaderWriter;

public class MainEasyQuestionAdvGreedyAlgo {

	private static final String resultFilePath = "";
	// OR
	// public static final String resultFilePath =
	// "data/results/resultAdvAlgo.txt";

	public static void main(String[] args) {
		System.out.println(
				"Lunching Advanced Greedy Algorithm Methods for searching Dominating Set for given tweeter network");

		//loading graph
		Graph graph = new CapGraph();
		System.out.println("Loading graph");
		GraphLoader.loadGraph(graph, "data/higgs-retweet_network.edgelist");
		System.out.println("Graph loaded: " + ((CapGraph) graph).getVertexes().size() + " nodes and "
				+ new Integer(((CapGraph) graph).getNumOfEdges()) + " edges");
		GreedyAlgorithm greedyAlgo = new GreedyAlgorithm((CapGraph) graph);

		long startTime;
		Set<Integer> dominatingSet;
		List<Set<Integer>> dominatingSets = new ArrayList<Set<Integer>>();
		
		//presenting improved greedy algorithm in 5 versions
		for (int i = 0; i < greedyAlgo.getAlgos().length; i++) {
			System.out.print("Finding dominating set (easy question): " + greedyAlgo.getAlgos()[i] + " method");
			startTime = System.currentTimeMillis();
			dominatingSet = greedyAlgo.getDominatingSetBySelectedAlgo(greedyAlgo.getAlgos()[i]);
			System.out.println(": " + dominatingSet.size() + " nodes - total time: "
					+ new Integer((int) (System.currentTimeMillis() - startTime) / 1000).toString() + "s");

			dominatingSets.add(dominatingSet);
		}
		if (!resultFilePath.equals("")) {
			ResultReaderWriter.writeResultsToFile(dominatingSets, resultFilePath);
		}
	}
}
