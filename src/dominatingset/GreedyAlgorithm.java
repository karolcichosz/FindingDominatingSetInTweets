package dominatingset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import graph.Graph;
import graph.GraphVertex;
import util.RandSet;
import graph.CapGraph;

// @author Karol Cichosz

public class GreedyAlgorithm {
	protected CapGraph graph;

	protected AlgoSelectionMethod[] algos;
	
	protected HashMap<Integer, HashSet<Integer>> graphVertexesByNumOfEdges;

	private int methodRequestCounter;

	protected Random randGenerator;

	//5 versions of improved greedy algorithm
	public enum AlgoSelectionMethod {
		TOP_EDGE, ONEFORTH_OF_TOP_EDGES, RANDOM_EDGE, ONCE_TOP_ONCE_RANDOM_EDGE, TOP_RANDOM_EDGE;
	}

	public GreedyAlgorithm() {
		algos = new AlgoSelectionMethod[] { AlgoSelectionMethod.TOP_EDGE, AlgoSelectionMethod.ONEFORTH_OF_TOP_EDGES,
				AlgoSelectionMethod.RANDOM_EDGE, AlgoSelectionMethod.ONCE_TOP_ONCE_RANDOM_EDGE,
				AlgoSelectionMethod.TOP_RANDOM_EDGE };
		methodRequestCounter = 0;
		randGenerator = new Random(System.currentTimeMillis());
		graphVertexesByNumOfEdges = null;
	}

	public GreedyAlgorithm(CapGraph graph) {
		this();
		this.setGraph(graph);
	}

	//key: numOfEdges  object: set of Nodes with numOfEdges
	private HashMap<Integer, HashSet<Integer>> getVertexesByNumOfEdges() {
		HashMap<Integer, HashSet<Integer>> result = new HashMap<Integer, HashSet<Integer>>();
		if (graph != null) {
			for (GraphVertex vertex : graph.getVertexes().values()) {
				//if first element added in the particular numOfEdge set
				if (!result.containsKey(vertex.getNumberOfEdges())) {
					//create set
					result.put(vertex.getNumberOfEdges(), new HashSet<Integer>());
				}
				result.get(vertex.getNumberOfEdges()).add(vertex.getVertexNumber());
			}
		}

		return result;
	}

	//finding dominating set by Naive algorithm
	public Set<Integer> getDominatingSetByEasyAlgo() {
		Set<Integer> result = new HashSet<Integer>();
		
		if (graph == null) {
			return null;
		}
		
		if (graph.getVertexes().size() == 1) {
			result.add(graph.getVertexes().keySet().iterator().next());
			return result;
		}
	
		//deep copy of the graph, which will be changed
		HashMap<Integer, GraphVertex> vertexesForDS = graph.cloneVertexes();
		if (graphVertexesByNumOfEdges == null) {
			graphVertexesByNumOfEdges = getVertexesByNumOfEdges();
		}
		
		//finding nodes by numOfEdges
		HashMap<Integer, HashSet<Integer>> vertexesByNumOfEdges = copyVertexesByNumOfEdges(graphVertexesByNumOfEdges);

		//sorting numOfEdges to find the greater one
		ArrayList<Integer> maxEdgesOrder = new ArrayList<Integer>(vertexesByNumOfEdges.keySet());
		Collections.sort(maxEdgesOrder);

		int maxNumOfEdges;

		int numOfSelectedVertex;
		while (!vertexesForDS.isEmpty()) {
			//finding greatest edgeNum
			maxNumOfEdges = maxEdgesOrder.get(maxEdgesOrder.size() - 1);

			//geting first node with max edgeNum
			numOfSelectedVertex = vertexesByNumOfEdges.get(maxNumOfEdges).iterator().next();

			// for every neighbour(edge) of selected vertex
			for (Integer neighbour : vertexesForDS.get(numOfSelectedVertex).getSecondVertexes()) {
				if (vertexesForDS.containsKey(neighbour)) {
					//remove node(neighbour) from numEdges HashMap
					vertexesByNumOfEdges.get(vertexesForDS.get(neighbour).getNumberOfEdges()).remove(neighbour);
					if (vertexesByNumOfEdges.get(vertexesForDS.get(neighbour).getNumberOfEdges()).isEmpty()) {
						maxEdgesOrder.remove(new Integer(vertexesForDS.get(neighbour).getNumberOfEdges()));
					}
					//remove node(neighbour) from deep copy of the graph
					vertexesForDS.remove(neighbour);
				}
			}
			
			//remove selected node from numEdges HashMap
			vertexesByNumOfEdges.get(vertexesForDS.get(numOfSelectedVertex).getNumberOfEdges())
					.remove(numOfSelectedVertex);
			if (vertexesByNumOfEdges.get(vertexesForDS.get(numOfSelectedVertex).getNumberOfEdges()).isEmpty()) {
				maxEdgesOrder.remove(new Integer(vertexesForDS.get(numOfSelectedVertex).getNumberOfEdges()));
			}
			
			//remove selected node from deep copy of the graph
			vertexesForDS.remove(numOfSelectedVertex);

			//adding to solutions set
			result.add(numOfSelectedVertex);
		}

		return result;
	}

	//update network structures after removing selected Vertex
	private void updateNetwork(Integer numOfSelectedVertex, HashMap<Integer, GraphVertex> vertexesForDS,
			HashMap<Integer, HashSet<Integer>> vertexesByNumOfEdges) {
		ArrayList<Integer> vertexToRemove = new ArrayList<Integer>();
		vertexToRemove.add(numOfSelectedVertex);
		vertexToRemove.addAll(vertexesForDS.get(numOfSelectedVertex).getSecondVertexes());

		Iterator<Integer> it = vertexToRemove.iterator();

		do {
			removeVertexFromNetwork(it.next(), vertexesForDS, vertexesByNumOfEdges);
		} while (it.hasNext());
	}

	//selecting node as part of dominating set using indicated method
	private int chooseNum(AlgoSelectionMethod method, HashMap<Integer, HashSet<Integer>> vertexesByNumOfEdges) {
		ArrayList<Integer> maxEdgesOrder;
		int maxNumOfEdges;
	
		switch (method) {
		case TOP_EDGE:
			maxEdgesOrder = new ArrayList<Integer>(vertexesByNumOfEdges.keySet());
			Collections.sort(maxEdgesOrder);
			maxNumOfEdges = maxEdgesOrder.get(maxEdgesOrder.size() - 1);
	
			//first in top edge list
			return vertexesByNumOfEdges.get(maxNumOfEdges).iterator().next();
	
		case ONEFORTH_OF_TOP_EDGES:
			maxEdgesOrder = new ArrayList<Integer>(vertexesByNumOfEdges.keySet());
			Collections.sort(maxEdgesOrder);
	
			int sel = maxEdgesOrder.size() - 1;
	
			if (maxEdgesOrder.size() > 3) {
				sel -= randGenerator.nextInt(maxEdgesOrder.size() / 4);
			}
	
			//finding numOfEdge among 25% of greatest 
			int selectedNumOfEdges = maxEdgesOrder.get(sel);
	
			HashSet<Integer> vertexedBySelectedEdge = vertexesByNumOfEdges.get(selectedNumOfEdges);
			//getting first node in selected numEdge		
			return vertexedBySelectedEdge.iterator().next();
	
		case RANDOM_EDGE:
			ArrayList<Integer> selectedEdges = new ArrayList<Integer>(vertexesByNumOfEdges.keySet());
			//getting random edge
			return vertexesByNumOfEdges.get(selectedEdges.get(randGenerator.nextInt(selectedEdges.size()))).iterator()
					.next();
	
		case ONCE_TOP_ONCE_RANDOM_EDGE:
			if ((++methodRequestCounter) % 2 == 1) {
				//once top_edge method
				return chooseNum(AlgoSelectionMethod.TOP_EDGE, vertexesByNumOfEdges);
			} else {
				//once random_edge method
				return chooseNum(AlgoSelectionMethod.RANDOM_EDGE, vertexesByNumOfEdges);
			}
	
		case TOP_RANDOM_EDGE:
			//finding top numOfEdges
			maxEdgesOrder = new ArrayList<Integer>(vertexesByNumOfEdges.keySet());
			Collections.sort(maxEdgesOrder);
			maxNumOfEdges = maxEdgesOrder.get(maxEdgesOrder.size() - 1);
	
			//lack of edges doesn't matter which one
			if (maxNumOfEdges == 0) {
				return vertexesByNumOfEdges.get(maxNumOfEdges).iterator().next();
			} else {
				//get random vertex
				return RandSet.getRandomElement(vertexesByNumOfEdges.get(maxNumOfEdges));
			}
	
		default:
			return -1;
		}
	}

	//created hard copy of hashmap for future changes
	protected HashMap<Integer, HashSet<Integer>> copyVertexesByNumOfEdges(HashMap<Integer, HashSet<Integer>> toCopyVertexesByNumOfEdges) {
		HashMap<Integer, HashSet<Integer>> result = new HashMap<Integer, HashSet<Integer>>();
		
		for(Integer toCopyKey : toCopyVertexesByNumOfEdges.keySet()) {
			result.put(toCopyKey, new HashSet<Integer>(toCopyVertexesByNumOfEdges.get(toCopyKey)));
		}
		
		return result;
	}
	
	//implementation of improved greedy algorithm
	public Set<Integer> getDominatingSetBySelectedAlgo(AlgoSelectionMethod method) {
		//int resultCounter = 0;
		Set<Integer> result = new HashSet<Integer>();
		
		//boundary cases
		if (graph == null) {
			return null;
		}
		if (graph.getVertexes().size() == 1) {
			result.add(graph.getVertexes().keySet().iterator().next());
			return result;
		}

		//create deep copy of the graph for future change
		HashMap<Integer, GraphVertex> vertexesForDS = graph.cloneVertexes();
		if (graphVertexesByNumOfEdges == null) {
			graphVertexesByNumOfEdges = getVertexesByNumOfEdges();
		}
		
		//create deep copy of Hashmap (key: numOfEdges object: setOfVertexes with numOfEdges)
		HashMap<Integer, HashSet<Integer>> vertexesByNumOfEdges = copyVertexesByNumOfEdges(graphVertexesByNumOfEdges);

		int numOfSelectedVertex;

		//until remaining graph has nodes
		while (!vertexesForDS.isEmpty()) {
			
			numOfSelectedVertex = chooseNum(method, vertexesByNumOfEdges);
			updateNetwork(numOfSelectedVertex, vertexesForDS, vertexesByNumOfEdges);

			result.add(numOfSelectedVertex);
			/*if (++resultCounter % 50000 == 0) {
				System.out.println(resultCounter + " results; network has:" + vertexesForDS.size() + " elements");
			}*/
		}

		return result;
	}

	//remove vertex and related edges from network structures
	private void removeVertexFromNetwork(Integer numOfVertexToRemove, HashMap<Integer, GraphVertex> network,
			HashMap<Integer, HashSet<Integer>> vertexesByNumOfEdges) {
		if (network.containsKey(numOfVertexToRemove)) {
			int numOfEdges;
			//updating EdgesFrom Set
			for (Integer firstPartOfEdge : network.get(numOfVertexToRemove).getFirstVertexes()) {
				numOfEdges = network.get(firstPartOfEdge).getNumberOfEdges();
				network.get(firstPartOfEdge).getSecondVertexes().remove(numOfVertexToRemove);

				// update vertexesByNumOfEdges
				vertexesByNumOfEdges.get(numOfEdges).remove(firstPartOfEdge);
				if (vertexesByNumOfEdges.get(numOfEdges).isEmpty()) {
					vertexesByNumOfEdges.remove(numOfEdges);
				}
				if (!vertexesByNumOfEdges.containsKey(numOfEdges - 1)) {
					vertexesByNumOfEdges.put(numOfEdges - 1, new HashSet<Integer>());
				}
				vertexesByNumOfEdges.get(numOfEdges - 1).add(firstPartOfEdge);

			}
			
			//updating edgesTo set
			for (Integer secondPartOfEdge : network.get(numOfVertexToRemove).getSecondVertexes()) {
				network.get(secondPartOfEdge).getFirstVertexes().remove(numOfVertexToRemove);
			}

			numOfEdges = network.get(numOfVertexToRemove).getNumberOfEdges();
			network.remove(numOfVertexToRemove);

			// update vertexesByNumOfEdges
			vertexesByNumOfEdges.get(numOfEdges).remove(numOfVertexToRemove);
			if (vertexesByNumOfEdges.get(numOfEdges).isEmpty()) {
				vertexesByNumOfEdges.remove(numOfEdges);
			}

		}
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(CapGraph graph) {
		this.graph = graph;
	}

	public AlgoSelectionMethod[] getAlgos() {
		return this.algos;
	}
}
