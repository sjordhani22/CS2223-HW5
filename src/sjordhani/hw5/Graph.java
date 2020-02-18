package sjordhani.hw5;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import java.util.Iterator;

import algs.days.day18.AVL;

/**
 * Standard undirected Graph implementation, as starting point for Q2 and Q3 on
 * HW5.
 */
public class Graph {

	final int V;
	int E;
	Bag<Integer>[] adj; // Adjacent vertex

	/**
	 * Initializes an empty graph with <tt>V</tt> vertices and 0 edges. param V the
	 * number of vertices
	 *
	 * @param V number of vertices
	 * @throws IllegalArgumentException if <tt>V</tt> < 0
	 */
	public Graph(int V) {
		if (V < 0)
			throw new IllegalArgumentException("Number of vertices must be nonnegative");
		this.V = V;
		this.E = 0;
		adj = (Bag<Integer>[]) new Bag[V];
		for (int v = 0; v < V; v++) {
			adj[v] = new Bag<Integer>();
		}
	}

	/** Added this method for day20 to load graph from file. */
	public Graph(In in) {
		this(in.readInt());
		int E = in.readInt();
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			addEdge(v, w);
		}
	}

	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	/** Adds the undirected edge v-w to this graph. */
	public void addEdge(int v, int w) {
		E++;
		adj[v].add(w);
		adj[w].add(v);
	}

	/** Returns the vertices adjacent to vertex <tt>v</tt>. */
	public Iterable<Integer> adj(int v) {
		return adj[v];
	}

	/** Returns the degree of vertex <tt>v</tt>. */
	public int degree(int v) {
		return adj[v].size();
	}

	/** Fill in this method to determine if undirected graph is connected. */
	public boolean connected() {
		for (int i = 0; i < V; i++) {
			BreadthFirstPaths breadth = new BreadthFirstPaths(this, i);
			for (int j = 0; j < V; j++) { //Loops through the whole graph (V being the current vertex 
				if (!breadth.hasPathTo(j)) { // This will tell us if there is a path between the two
					return false;
				}
			}
		}
		return true;
	}
  
	/**
	 * Helper Method created to assist diameter in determining the largest distance
	 * between two vertices
	 * 
	 * @param v The source vertex in which we are starting from
	 * @return int the number of max paths from the source vertex to the final
	 *         vertex
	 */
	int maxLen(int v) {
		BreadthFirstPaths search = new BreadthFirstPaths(this, v);
		int maxDist = 0;
		int trueDist = 0;

		for (int i = 0; i < V; i++) {
			//comparing current distance to the max to see if the max needs to be updated. 
			if (search.distTo(i) > maxDist && search.distTo(i) < Integer.MAX_VALUE) { 
				trueDist = search.distTo(i);
			}
		}
		return trueDist;
	}

	/**
	 * The diameter of graph is the maximum distance between any pair of vertices.
	 * 
	 * If a graph is not connected, then Integer.MAX_VALUE must be returned.
	 * 
	 * @return
	 */
	public int diameter() {
		if (!connected()) {
			return Integer.MAX_VALUE;
		}
		int di = 0;
		for (int v = 0; v < V; v++) {
			// BreadthFirstPaths breadth = new BreadthFirstPaths(this, v);
			int max = maxLen(v);
			if (max > di) {  // Determining which path taken from bfs is the longest path
				di = max;
			}
		}
		return di;
	}

	/**
	 * The status of a given vertex, v.
	 * 
	 * If the graph is not connected, then this method is not responsible for return
	 * value.
	 */
	public int status(int v) {
		/*
		if (!connected()) {   We would not need these two lines of code because we are assuming that if the graph is not connected then this fcn is not 
							  responsible to return anything
			return 0;
		}
*/
		Queue<Integer> q = new Queue<Integer>();
		q.enqueue(v);
		Queue<Integer> q2 = new Queue<Integer>();
		q2.enqueue(v);
		
		BreadthFirstPaths paths = new BreadthFirstPaths(this, v);
		
		int total = 0;
  
		while (!q.isEmpty()) {
			int vert = q.dequeue();

			int dist = paths.distTo(vert);
			if (vert != v) {  
				total += dist;
			}

			Iterable<Integer> adjVertex = adj(vert);

			Iterator<Integer> iter = adjVertex.iterator();

			while (iter.hasNext()) {
				Integer next = iter.next();

				boolean hasFound = false;

				for (int x = 0; x < q2.size() && !hasFound; x++) {
					int val = q2.dequeue();
					if (val == next) {
						hasFound = true;
					}
					q2.enqueue(val);
				}

				if (!hasFound) { //we use this to move down the queue searching for the values that we need 
					q.enqueue(next);
					q2.enqueue(next);
				}
			}
		}
		return total;
	}

	/*
	 * Determine if all status(v) values within the graph represent different
	 * values.
	 * 
	 */
	public boolean statusInjective() {
		AVL<Integer> tree = new AVL<Integer>();

		int length = this.adj.length;

		for (int i = 0; i < length; i++) {
			if (tree.contains(status(i))) { // checking to see if this item is inside the already existing avl tree
				return false;

			} else {
				tree.insert(status(i)); // If the item/integer is not already there, then we can insert that integer to
										// be checked later on
			}
		}
		return true;
	}

	/**
	 * Returns a string representation of this graph.
	 *
	 * @return the number of vertices <em>V</em>, followed by the number of edges
	 *         <em>E</em>, followed by the <em>V</em> adjacency lists
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " vertices, " + E + " edges " + "\n");
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (int w : adj[v]) {
				s.append(w + " ");
			}
			s.append("\n");
		}
		return s.toString();
	}

}
