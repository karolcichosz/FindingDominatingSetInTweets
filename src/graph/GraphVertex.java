package graph;

import java.util.ArrayList;
//@author Karol Cichosz
public class GraphVertex {
	private ArrayList<Integer> secondVertexes; //edgesTo
	private ArrayList<Integer> firstVertexes;  //edgesFrom
	private int vertexNumber;
	
	private GraphVertex() {
		setSecondVertexes(new ArrayList<Integer>());
		setFirstVertexes(new ArrayList<Integer>());
	}
	
	public GraphVertex(int num) {
		this();
		setVertexNumber(num);
	}

	public int getVertexNumber() {
		return vertexNumber;
	}

	private void setVertexNumber(int vertexNumber) {
		this.vertexNumber = vertexNumber;
	}

	public ArrayList<Integer> getSecondVertexes() {
		return secondVertexes;
	}

	public void setSecondVertexes(ArrayList<Integer> secondVertexes) {
		this.secondVertexes = secondVertexes;
	}
	
	public int getNumberOfEdges() {
		return secondVertexes.size();
	}
	
	public GraphVertex clone() {
		GraphVertex newGraph = new GraphVertex(this.getVertexNumber());
		
		for(int number : this.getSecondVertexes()) {
			newGraph.getSecondVertexes().add(number);
		}
		
		for(int number : this.getFirstVertexes()) {
			newGraph.getFirstVertexes().add(number);
		}
			
		return newGraph;
	}

	public ArrayList<Integer> getFirstVertexes() {
		return firstVertexes;
	}

	public void setFirstVertexes(ArrayList<Integer> firstVertexes) {
		this.firstVertexes = firstVertexes;
	}
}
