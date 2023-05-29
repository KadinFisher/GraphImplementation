package implementation;

import java.util.*;

/**
 * Implements a graph. We use two maps: one map for adjacency properties 
 * (adjancencyMap) and one map (dataMap) to keep track of the data associated 
 * with a vertex. Therefore this implementation of a graph is with an 
 * adjacency map instead of an adjacency matrix. Additionally, this is a weighted
 * directed graph.
 * 
 * @author cmsc132
 * 
 * @param <E> The data you would like each vertex to hold.
 */
public class Graph<E> {
	/* You must use the following maps in your implementation */
	private HashMap<String, HashMap<String, Integer>> adjacencyMap;
	private HashMap<String, E> dataMap;
	
	/**
	 * Constructor that initializes the adjacencyMap and the dataMap.
	 */
	public Graph() {
		this.adjacencyMap = new HashMap<String, HashMap<String, Integer>>();
		this.dataMap = new HashMap<String, E>();
	}
	
	/**
	 * Adds a vertice to the graph dataStructure. Each Vertice has a String name
	 * and then data that is attached to it. The data in each vertice will be put
	 * in the dataMap that serves almost as a key for each vertice. The 
	 * adjacency map will be updated accordingly in order to hold future edges
	 * of the vertex.
	 * 
	 * @param vertexName The name of the vertex.
	 * @param data		 The data associated with the vertex.
	 */
	public void addVertex(String vertexName, E data) {
		dataMap.put(vertexName, data);
		adjacencyMap.put(vertexName, new HashMap<String, Integer>());
	}
	
	/**
	 * Connectects two verticies with a directed edge that has a specific cost.
	 * 
	 * @param startVertexName The first vertex you are connecting.
	 * @param endVertexName   The second vertex you are connecting.
	 * @param cost			  The cost it takes to travel the edge.
	 * 
	 * @throws IllegalArgumentException If the user enters a vertex name that is
	 *                                  not in the map.
	 */
	public void addDirectedEdge(String startVertexName, String endVertexName, 
			int cost) {
		if (dataMap.get(startVertexName) == null || dataMap.get(endVertexName) == null) {
			throw new IllegalArgumentException("One or both of your verticies do not exist.");
		}
		Map<String, Integer> startVertexAdjacencyMap = 
				adjacencyMap.get(startVertexName);
		startVertexAdjacencyMap.put(endVertexName, cost);
	}
	
	/**
	 * Returns a string with information about the Graph. Vertices 
	 * are printed in sorted order and information about adjacent edges is 
	 * printed in sorted order (by vertex name).
	 * 
	 * @return The graph as a String.
	 */
	@Override
	public String toString() {
		String answer = "";
		TreeMap<String, E> dataMap = new TreeMap<String, E>();
		dataMap.putAll(this.dataMap);
		TreeMap<String, Integer> adjacencyMapForAVertex = new TreeMap<String, Integer>();
		answer += "Vertices: " + dataMap.keySet() + "\n";
		answer += "Edges:\n";
		for (String vertexName : dataMap.keySet()) {
			answer += "Vertex(" + vertexName + ")--->";
			adjacencyMapForAVertex.putAll(this.adjacencyMap.get(vertexName));
			answer += adjacencyMapForAVertex + "\n";
			adjacencyMapForAVertex.clear();
		}
		return answer;
	}
	
	/**
	 * Gets a map with all the adjacent vertices of the provided vertexName
	 * parameter and the associated weight of the edge to them. 
	 * 
	 * @param vertexName The vertex you'd like to get the adjacent vertices of.
	 * 
	 * @return A Map with all of the adjacent vertices of the provided vertex 
	 * 	       name. If no adjacent vertices then it will just be an empty Map.
	 */
	public Map<String, Integer> getAdjacentVertices(String vertexName) {
		Map<String, Integer> answer = new HashMap<String, Integer>();
		answer.putAll(adjacencyMap.get(vertexName));
		return answer;
	}
	
	/**
	 * Gets the associated cost to travel between two vertices connected by an
	 * edge.
	 * 
	 * @param startVertex The starting vertex of the directed edge.
	 * @param endVertex   The ending vertex of the directed edge.
	 * 
	 * @return The cost of the edge.
	 * 
	 * @throws IllegalArgumentException If any of the two vertices don't exist 
	 * 									in the graph.
	 */
	public int getCost(String startVertex, String endVertex) {
		if (dataMap.get(startVertex) == null || dataMap.get(endVertex) == null) {
			throw new IllegalArgumentException("One or both vertices don't exist");
		}
		int answer = 0;
		Map<String, Integer> adjacentVertices = this.adjacencyMap.get(startVertex);
		answer = adjacentVertices.get(endVertex);
		return answer;
	}
	
	/**
	 * Returns a Set containing all the vertices name's of the graph.
	 * 
	 * @return A set with all the vertices.
	 */
	public Set<String> getVertices() {
		HashSet<String> answer = new HashSet<String>();
		answer.addAll(dataMap.keySet());
		return answer;
	}
	
	/**
	 * Returns the data associated with the specific node passed in as the 
	 * parameter.
	 * 
	 * @param vertex The name of the vertex you want the data from.
	 * 
	 * @return The data associated with the specified vertex.
	 * 
	 * @throws IllegalArgumentException If the the vertex doesn't exist in the 
	 * 									graph.
	 */
	public E getData(String vertex) {
		try {
			E answer = dataMap.get(vertex);
			return answer;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Vertex doesn't exist in graph");
		}
	}
	
	/**
	 * Performs the passed in processing to apply to each vertex. Processing 
	 * occurs in a breadth first order.
	 * 
	 * @param startVertexName The starting vertex of the breadthFirst processing.
	 * @param callback		  Represents the processing to be called on each vertex.
	 * 
	 * @throws IllegalArgumentException If the starting vertex is not in the 
	 * 									graph.
	 */
	public void doBreadthFirstSearch(String startVertexName, CallBack<E> callback) {
		if (dataMap.get(startVertexName) == null) {
			throw new IllegalArgumentException("Start Vertex Doesn't exist");
		}
		Set<String> visitedSet = new HashSet<String>();
		Queue<String> discoveredQueue = new LinkedList<String>();
		discoveredQueue.add(startVertexName);
		while (!(discoveredQueue.isEmpty())) {
			String vertex = discoveredQueue.remove();
			if (!(visitedSet.contains(vertex))) {
				callback.processVertex(vertex, dataMap.get(vertex));
				visitedSet.add(vertex);
				for (String adjacentVertex : this.adjacencyMap.get(vertex).keySet()) {
					if (!(visitedSet.contains(adjacentVertex))) {
						discoveredQueue.add(adjacentVertex);
					}
				}
			}
		}
	}
	
	/**
	 * Performs the passed in processing to apply to each vertex. Processing 
	 * occurs in a depth first order.
	 * 
	 * @param startVertexName The starting vertex of the breadthFirst processing.
	 * @param callback		  Represents the processing to be called on each vertex.
	 * 
	 * @throws IllegalArgumentException If the starting vertex is not in the 
	 * 									graph.
	 */
	public void doDepthFirstSearch(String startVertexName, CallBack<E> callback) {
		if (dataMap.get(startVertexName) == null) {
			throw new IllegalArgumentException("Start Vertex Doesn't exist");
		}
		Set<String> visitedSet = new HashSet<String>();
		Stack<String> discoveredStack = new Stack<String>();
		discoveredStack.add(startVertexName);
		while (!(discoveredStack.isEmpty())) {
			String vertex = discoveredStack.pop();
			if (!(visitedSet.contains(vertex))) {
				callback.processVertex(vertex, dataMap.get(vertex));
				visitedSet.add(vertex);
				for (String adjacentVertex : this.adjacencyMap.get(vertex).keySet()) {
					if (!(visitedSet.contains(adjacentVertex))) {
						discoveredStack.push(adjacentVertex);
					}
				}
			}
		}
	}
	
	/**
	 * Computes the shortest path and shortest path cost using Dijkstras's 
	 * algorithm. It initializes shortestPath with the names of the vertices 
	 * corresponding to the shortest path. If there is no shortest path, 
	 * shortestPath will be have entry "None".
	 * 
	 * @param startVertexName The vertex you'd like to begin traversal at.
	 * @param endVertexName   The vertex you'd like to end traversal at.
	 * @param shortestPath    Initialized with the method to the shortest path or "none".
	 * 
	 * @return The cost of the shortest path.
	 * 
	 * @throws IllegalArgumentException If the startVertexName or endVertexName 
	 * 								    don't exist.
	 */
	public int doDijkstras(String startVertexName, String endVertexName, ArrayList<String> shortestPath) {
		if (dataMap.get(startVertexName) == null || dataMap.get(endVertexName) == null) {
			throw new IllegalArgumentException("One or both vertexes are not in the graph");
		}
		Map<String, Integer> distances = new HashMap<>();
		for (String vertex : dataMap.keySet()) {
			distances.put(vertex, Integer.MAX_VALUE);
		}
		distances.put(startVertexName, 0);
		Queue<String> queue = new PriorityQueue<String>(Comparator.comparingInt(distances::get));
	    queue.add(startVertexName);
	    Map<String, String> previous = new HashMap<String, String>();
	    for (String vertex : dataMap.keySet()) {
	        previous.put(vertex, null);
	    }
	    while (!queue.isEmpty()) {
	        String current = queue.poll();
	        if (current.equals(endVertexName)) {
	            String v = current;
	            while (v != null) {
	                shortestPath.add(0, v);
	                v = previous.get(v);
	            }
	            return distances.get(current);
	        }
	        for (String adjacentVertex : adjacencyMap.get(current).keySet()) {
	            String neighbor = adjacentVertex;
	            int newDistance = distances.get(current) + adjacencyMap.get(current).get(adjacentVertex);
	            if (newDistance < distances.get(neighbor)) {
	                distances.put(neighbor, newDistance);
	                previous.put(neighbor, current);
	                queue.add(neighbor);
	            }
	        }
	    }
	    shortestPath.add("None");
	    return -1;
	}

	
}