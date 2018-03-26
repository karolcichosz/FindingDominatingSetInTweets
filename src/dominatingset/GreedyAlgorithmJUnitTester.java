package dominatingset;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import graph.CapGraph;
// @author Karol Cichosz
public class GreedyAlgorithmJUnitTester {
	private CapGraph graphNull, graphEmpty, graph1Node, graph1Node1Edge, graph2Node;
	private CapGraph graphA, graphB;
	private GreedyAlgorithm algo;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		graphNull = null;
		graphEmpty = new CapGraph();
		graph1Node = new CapGraph();
		graph1Node.addVertex(0);
		graph1Node1Edge = new CapGraph();
		graph1Node1Edge.addVertex(0);
		graph1Node.addEdge(0, 0);
		graph2Node = new CapGraph();
		graph2Node.addVertex(0);
		graph2Node.addVertex(1);
		graph2Node.addEdge(0, 1);
		graphA = new CapGraph();
		graphA.addVertex(0);
		graphA.addVertex(1);
		graphA.addVertex(2);
		graphA.addVertex(3);
		graphA.addVertex(4);
		graphA.addEdge(0, 1);
		graphA.addEdge(0, 2);
		graphA.addEdge(0, 3);
		graphA.addEdge(1, 2);
		graphA.addEdge(3, 2);
		graphA.addEdge(3, 4);
		graphA.addEdge(4, 1);
		graphA.addEdge(4, 2);
		graphA.addEdge(4, 3);
		graphB = new CapGraph();
		graphB.addVertex(0);
		graphB.addVertex(1);
		graphB.addVertex(2);
		graphB.addVertex(3);
		graphB.addVertex(4);
		graphB.addEdge(0, 1);
		graphB.addEdge(0, 2);
		graphB.addEdge(0, 3);
		graphB.addEdge(0, 4);
		graphB.addEdge(1, 2);
		graphB.addEdge(3, 2);
		graphB.addEdge(3, 4);
		graphB.addEdge(4, 1);
		graphB.addEdge(4, 2);
		graphB.addEdge(4, 3);
	}

	@Test
	public void TestNullGraph() {
		algo = new GreedyAlgorithm(graphNull);
		assertEquals("Checking Naive Algo with null graph", null, algo.getDominatingSetByEasyAlgo());
		for (int i = 0; i < algo.getAlgos().length; i++) {
			assertEquals("Checking " + algo.getAlgos()[i] + " method with null graph", null,
					algo.getDominatingSetBySelectedAlgo(algo.getAlgos()[i]));
		}
	}

	@Test
	public void TestEmptyGraph() {
		algo = new GreedyAlgorithm(graphEmpty);
		assertEquals("Checking Naive Algo with empty graph", 0, algo.getDominatingSetByEasyAlgo().size());
		for (int i = 0; i < algo.getAlgos().length; i++) {
			assertEquals("Checking " + algo.getAlgos()[i] + " method with null empty", 0,
					algo.getDominatingSetBySelectedAlgo(algo.getAlgos()[i]).size());
		}
	}

	@Test
	public void Test1NodeGraph() {
		algo = new GreedyAlgorithm(graph1Node);
		assertEquals("Checking Naive Algo with 1 node graph", 1, algo.getDominatingSetByEasyAlgo().size());
		for (int i = 0; i < algo.getAlgos().length; i++) {
			assertEquals("Checking " + algo.getAlgos()[i] + " method with 1 node", 1,
					algo.getDominatingSetBySelectedAlgo(algo.getAlgos()[i]).size());
		}
	}

	@Test
	public void Test1Node1EdgeGraph() {
		algo = new GreedyAlgorithm(graph1Node1Edge);
		assertEquals("Checking Naive Algo with 1 node and 1 edge graph", 1, algo.getDominatingSetByEasyAlgo().size());
		for (int i = 0; i < algo.getAlgos().length; i++) {
			assertEquals("Checking " + algo.getAlgos()[i] + " method with 1 node and 1 edge", 1,
					algo.getDominatingSetBySelectedAlgo(algo.getAlgos()[i]).size());
		}
	}

	@Test
	public void Test2NodesGraph() {
		algo = new GreedyAlgorithm(graph2Node);
		Iterator<Integer> iterator = algo.getDominatingSetByEasyAlgo().iterator();
		assertEquals("Checking Naive Algo with 2 nodes graph", 0, iterator.next().intValue());
		for (int i = 0; i < algo.getAlgos().length; i++) {
			iterator = algo.getDominatingSetBySelectedAlgo(algo.getAlgos()[i]).iterator();
			assertEquals("Checking " + algo.getAlgos()[i] + " method with 2 nodes", 0, iterator.next().intValue());
		}
	}

	@Test
	public void TestGraphA() {
		algo = new GreedyAlgorithm(graphA);
		assertEquals("Checking Naive Algo with graphA", 2, algo.getDominatingSetByEasyAlgo().size());
		assertEquals("Checking " + GreedyAlgorithm.AlgoSelectionMethod.TOP_EDGE + " method with graphA", 2,
				algo.getDominatingSetBySelectedAlgo(GreedyAlgorithm.AlgoSelectionMethod.TOP_EDGE).size());
		assertEquals("Checking " + GreedyAlgorithm.AlgoSelectionMethod.TOP_RANDOM_EDGE + " method with graphA", 2,
				algo.getDominatingSetBySelectedAlgo(GreedyAlgorithm.AlgoSelectionMethod.TOP_RANDOM_EDGE).size());
		assertEquals("Checking " + GreedyAlgorithm.AlgoSelectionMethod.ONEFORTH_OF_TOP_EDGES + " method with graphA", 2,
				algo.getDominatingSetBySelectedAlgo(GreedyAlgorithm.AlgoSelectionMethod.ONEFORTH_OF_TOP_EDGES).size());

	}
	
	@Test
	public void TestGraphB() {
		algo = new GreedyAlgorithm(graphB);
		assertEquals("Checking " + GreedyAlgorithm.AlgoSelectionMethod.TOP_EDGE + " method with graphB", 1,
				algo.getDominatingSetBySelectedAlgo(GreedyAlgorithm.AlgoSelectionMethod.TOP_EDGE).size());
		assertEquals("Checking " + GreedyAlgorithm.AlgoSelectionMethod.TOP_RANDOM_EDGE + " method with graphB", 1,
				algo.getDominatingSetBySelectedAlgo(GreedyAlgorithm.AlgoSelectionMethod.TOP_RANDOM_EDGE).size());
		assertEquals("Checking " + GreedyAlgorithm.AlgoSelectionMethod.ONEFORTH_OF_TOP_EDGES + " method with graphB", 1,
				algo.getDominatingSetBySelectedAlgo(GreedyAlgorithm.AlgoSelectionMethod.ONEFORTH_OF_TOP_EDGES).size());

	}
}
