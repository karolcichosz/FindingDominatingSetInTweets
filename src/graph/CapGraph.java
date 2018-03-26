/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

/**
 * @author Karol Cichosz.
 * 
 *         For the warm up assignment, you must implement your Graph in a class
 *         named CapGraph. Here is the stub file.
 *
 */
public class CapGraph implements Graph {
	private HashMap<Integer, GraphVertex> vertexes;
	
	protected int numOfEdges;


	public CapGraph() {
		vertexes = new HashMap<Integer, GraphVertex>();
		setNumOfEdges(0);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#addVertex(int)
	 */
	@Override
	public void addVertex(int num) {

		if (!vertexes.containsKey(num)) {
			vertexes.put(num, new GraphVertex(num));
		}

	}

	public void removeVertex(int num) {
		if (vertexes.containsKey(num)) {
			vertexes.remove(num);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {

		if (vertexes.containsKey(from) && vertexes.containsKey(to)
				&& !vertexes.get(from).getSecondVertexes().contains(to)) {
			vertexes.get(from).getSecondVertexes().add(to);
			vertexes.get(to).getFirstVertexes().add(from);
			setNumOfEdges(getNumOfEdges() + 1);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		CapGraph result = new CapGraph();

		if (!vertexes.containsKey(center)) {
			return result;
		}

		GraphVertex egoNetCenter = vertexes.get(center);
		result.addVertex(center);

		for (Integer centerNeighbour : egoNetCenter.getSecondVertexes()) {
			result.addVertex(centerNeighbour);
		}

		for (Integer firstIndex : result.vertexes.keySet()) {
			for (Integer secondIndex : vertexes.get(firstIndex).getSecondVertexes()) {
				if (result.vertexes.containsKey(secondIndex)) {
					result.addEdge(firstIndex, secondIndex);
				}
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		// TODO Auto-generated method stub
		Stack<Integer> verticiesStack = new Stack<Integer>();
		verticiesStack.addAll(vertexes.keySet());

		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		Integer vertNum;

		while (!verticiesStack.empty()) {
			vertNum = verticiesStack.pop();

			if (!visited.contains(vertNum)) {
				dfsVisit(this, vertNum, visited, finished, null);
			}
		}

		verticiesStack = finished;
		finished = new Stack<Integer>();
		visited = new HashSet<Integer>();

		ArrayList<HashSet<Integer>> sccVertexes = new ArrayList<HashSet<Integer>>();

		while (!verticiesStack.empty()) {
			vertNum = verticiesStack.pop();

			if (!visited.contains(vertNum)) {
				sccVertexes.add(new HashSet<Integer>());
				sccVertexes.get(sccVertexes.size() - 1).add(vertNum);

				sccVertexes = dfsVisit(this.transpose(), vertNum, visited, finished, sccVertexes);
			}
		}

		return this.createGraphListFromSCCSets(sccVertexes);
	}

	private List<Graph> createGraphListFromSCCSets(ArrayList<HashSet<Integer>> sccVertexes) {
		// TODO Auto-generated method stub
		List<Graph> result = new ArrayList<Graph>();

		if (sccVertexes == null || sccVertexes.isEmpty()) {
			return result;
		}

		CapGraph graph;
		for (HashSet<Integer> sccSet : sccVertexes) {
			graph = new CapGraph();

			for (Integer vertNum : sccSet) {
				graph.addVertex(vertNum);
			}

			if (graph.vertexes.size() > 1) {
				for (Integer firstNum : sccSet) {
					for (Integer secondNum : this.vertexes.get(firstNum).getSecondVertexes()) {
						if (sccSet.contains(secondNum)) {
							graph.addEdge(firstNum, secondNum);
						}
					}
				}
			}

			result.add(graph);
		}

		return result;
	}

	/*
	 * private Stack<Integer> dfs(CapGraph graph, Stack<Integer> verticiesStack,
	 * List<Graph> sccGraphs) { HashSet<Integer> visited = new
	 * HashSet<Integer>(); Stack<Integer> finished = new Stack<Integer>();
	 * 
	 * while (!verticiesStack.empty()) { Integer vertNum = verticiesStack.pop();
	 * 
	 * if (!visited.contains(vertNum)) { dfsVisit(graph, vertNum, visited,
	 * finished, sccGraphs); } }
	 * 
	 * return finished; }
	 */
	private ArrayList<HashSet<Integer>> dfsVisit(CapGraph graph, Integer vertNum, HashSet<Integer> visited,
			Stack<Integer> finished, ArrayList<HashSet<Integer>> sccVertexes) {
		visited.add(vertNum);
		if (sccVertexes != null) {
			sccVertexes.get(sccVertexes.size() - 1).add(vertNum);
		}

		for (Integer neighbourNum : graph.vertexes.get(vertNum).getSecondVertexes()) {
			if (!visited.contains(neighbourNum)) {
				sccVertexes = dfsVisit(graph, neighbourNum, visited, finished, sccVertexes);
			}
		}

		finished.push(vertNum);

		return sccVertexes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {

		HashMap<Integer, HashSet<Integer>> result = new HashMap<Integer, HashSet<Integer>>();

		HashSet<Integer> secondVertexes;

		for (Integer firstVertex : vertexes.keySet()) {
			secondVertexes = new HashSet<Integer>();
			secondVertexes.addAll(vertexes.get(firstVertex).getSecondVertexes());
			result.put(firstVertex, secondVertexes);
		}

		return result;
	}
	
	public HashMap<Integer, GraphVertex> cloneVertexes() {
		HashMap<Integer, GraphVertex> result = new HashMap<Integer, GraphVertex>();
		for (Integer vertexKey : this.getVertexes().keySet()) {
			result.put(vertexKey,this.getVertexes().get(vertexKey).clone());
		}
		
		return result;
	}

	private CapGraph transpose() {
		CapGraph result = new CapGraph();

		HashMap<Integer, HashSet<Integer>> exportedGraph = this.exportGraph();

		for (Integer vertexNum : exportedGraph.keySet()) {
			result.addVertex(vertexNum);
		}

		for (Integer firstNum : exportedGraph.keySet()) {
			for (Integer secondNum : exportedGraph.get(firstNum)) {
				result.addEdge(secondNum, firstNum);
			}
		}

		return result;
	}
	
	public HashMap<Integer, GraphVertex> getVertexes() {
		return this.vertexes;
	}


	public int getNumOfEdges() {
		return numOfEdges;
	}


	protected void setNumOfEdges(int numOfEdges) {
		this.numOfEdges = numOfEdges;
	}

}
