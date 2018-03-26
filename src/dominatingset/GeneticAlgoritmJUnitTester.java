package dominatingset;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import graph.CapGraph;

// @author Karol Cichosz

public class GeneticAlgoritmJUnitTester {
	private CapGraph graphNull, graphEmpty, graph1Node, graph1Node1Edge, graph2Node;
	private CapGraph graphA, graphB;
	private GeneticAlgorithm algo;

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
		algo = new GeneticAlgorithm(graphNull, true);
		assertEquals("Checking Genetic Algo with null graph", null, algo.getMinimumDominatingSet(1, 1));
	}

	@Test
	public void TestEmptyGraph() {
		algo = new GeneticAlgorithm(graphEmpty, true);
		assertEquals("Checking Genetic Algo with empty graph", 0, algo.getMinimumDominatingSet(1, 1).size());
	}

	@Test
	public void Test1NodeGraph() {
		algo = new GeneticAlgorithm(graph1Node, true);
		assertEquals("Checking Genetic Algo with 1 node graph", 1, algo.getMinimumDominatingSet(1, 1).size());
	}

	@Test
	public void Test1Node1EdgeGraph() {
		algo = new GeneticAlgorithm(graph1Node1Edge, true);
		assertEquals("Checking Genetic Algo with 1 node and 1 edge graph", 1,
				algo.getMinimumDominatingSet(1, 1).size());
	}

	@Test
	public void Test2NodesGraph() {
		algo = new GeneticAlgorithm(graph2Node, true);
		Iterator<Integer> iterator = algo.getMinimumDominatingSet(1, 1).iterator();
		assertEquals("Checking Genetic Algo with 2 nodes graph", 0, iterator.next().intValue());
	}

	@Test
	public void TestWrongParamenters() {
		algo = new GeneticAlgorithm(graph2Node, true);
		try {
			algo.getMinimumDominatingSet(-1, -1);
			fail("Check out arguments");
		} catch (IllegalArgumentException e) {

		}
		
		try {
			algo.getMinimumDominatingSet(-1, 100);
			fail("Check out arguments");
		} catch (IllegalArgumentException e) {

		}
		
		try {
			algo.getMinimumDominatingSet(5, -100);
			fail("Check out arguments");
		} catch (IllegalArgumentException e) {

		}
	}

	@Test
	public void TestGraphA() {
		algo = new GeneticAlgorithm(graphA, true);
		assertEquals("Checking Genetic Algo with graphA", 2, algo.getMinimumDominatingSet(2, 1).size());
	}

	@Test
	public void TestGraphB() {
		algo = new GeneticAlgorithm(graphB, true);
		assertEquals("Checking Genetic Algo with graphB", 1, algo.getMinimumDominatingSet(2, 1).size());
	}
}
