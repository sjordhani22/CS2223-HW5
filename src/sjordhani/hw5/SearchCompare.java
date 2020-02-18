package sjordhani.hw5;

import edu.princeton.cs.algs4.*;

/**
 * Compute the BreadthFirst distance and DepthFirst distance for each vertex
 * from the initial vertex 0. Call these bfsDistTo[v] and dsfDistTo[v].
 * 
 * Observe that bfsDistTo[v] is always smaller than or equal to dfsDistTo[v].
 * Now define excess[v] = dfsDistTo[v] - bfsDistTo[v]. This assignment asks you
 * to compute the sum total of excess[v] for all vertices in the graph G.
 * 
 * Note that it is possible that some vertices are unreachable from s, and thus
 * the dfsDistTo[v] and bfsDistTo[v] would both be INFINITY.
 */
public class SearchCompare {

	public static int excess(Graph G, int s) {
	
		boolean[] bfsMark = new boolean[G.V()];
		boolean[] dfsMark = new boolean[G.V()];
		
		int[] dfsDistTo = new int[G.V()];
		int[] bfsDistTo = new int[G.V()];
		int[] dfsEdge = new int[G.V()];
		int[] bfsEdge = new int[G.V()];
		
		int excess = 0; //This will hold the value that we must print 

		bfs(G, s, bfsMark, bfsDistTo, bfsEdge); // These two function calls will run dfs and bfs for us (helpers)
		dfs(G, s, dfsMark, dfsEdge);
		for (int i = 0; i < G.V(); i++)
		{
			dfsDistTo[i] = dfsDistanceTo(G, s, i, dfsMark, dfsEdge); //helper function call 
		}
		for (int j = 0; j < G.V(); j++)
		{
			excess = excess + (dfsDistTo[j] - bfsDistTo[j]); // This is the formula we are given to calculate excess 
		}
		return excess;
	}
	
	
	/*
	 * final int V; int E;
	 * 
	 * Bag<Integer>[] adj; final int edgeTo[]; final boolean marked[];
	 * 
	 * if (!G.connected()) { return -1; } edgeTo = new int[G.V]; marked = new
	 * boolean[G.V]; if (G.V < 3) { return 0; } return dfs(G, 0, marked, edgeTo); }
	 * 
	 * static int dfs(Graph g, int v, boolean[] marked, int[] edgeTo) { boolean path
	 * = true; marked[v] = true; for (int w : g.adj(v)) { if (!marked[w]) {
	 * edgeTo[w] = v; int curr = dfs(g, w, marked, edgeTo); if (curr >= 0) return
	 * curr; path = false; } if (path) { return v; } } return -1; }
	 * 
	 * 
	 * void bfs(Graph g, int s) { Queue<Integer> q = new Queue<Integer>(); marked[s]
	 * = true; q.enqueue(s);
	 * 
	 * while (!q.isEmpty()) { int v = q.dequeue(); for (int w : g.adj(v)) { if
	 * (!marked[w]) { edgeTo[w] = v; marked[w] = true; q.enqueue(w); } } } }
	 * 
	 */

	 /**
	  * This is the helper function that will run depth first search which searches for paths between two 
	  * vertices blindly which mean it is not looking for shortest or longest path but more of a random one 
	  * @param G this is the graph we are performing dfs on 
	  * @param s this is the integer that represents the source vertex 
	  * @param dfsMark this is an array of boolean that tells us if a vertex has "been seen" already 
	  * @param dfsEdge this is an array of integers that holds the number of integers 
	  */
	public static void dfs(Graph G, int s, boolean[] dfsMark, int[] dfsEdge)
	{
		dfsMark[s] = true;
		for (int i : G.adj(s))
			if (!dfsMark[i])
			{
				dfsEdge[i] = s;
				dfs(G, i, dfsMark, dfsEdge);
			}
	}

	/**
	 * Helper function that computes the distance from one vertex to another blindly 
	 * @param G this is the graph we are finding the dfs distance of 
	 * @param s this is the source vertex 
	 * @param v this represents our current vertex 
	 * @param dfsMark array of boolean that determines which postions in the graph have already been seen 
	 * @param dfsEdge array of integers that holds the number of edges 
	 * @return int this returns the distance between the source vertex and our destination 
	 */
	public static int dfsDistanceTo(Graph G, int s, int curr, boolean[] dfsMark, int[] dfsEdge){
		int length = -1;
		if (!dfsMark[curr]){ //If the current position in the graph hasnt already been seen 
			return Integer.MAX_VALUE;
		}
		Stack<Integer> paths = new Stack<Integer>(); //used to determine the path of the search 
		
		for (int i = curr; i != s; i = dfsEdge[i]){ // looping through graph 
			paths.push(i); 
		}
		paths.push(s);
		while (!paths.isEmpty()){ 
			paths.pop(); //When we pop a value we are also keeping a count and going until the stack is empty which 
						// theoretically means we have reached our destination 
			length++;
		}
		return length;
	}
	
	
	
	/**
	 * This is the helper method that performs bfs in excess method 
	 * @param G this is the graph we are performing bfs on 
	 * @param s this is the source vertex 
	 * @param bfsMark array of boolean that tells us which integers/postions in the graph have been marked 
	 * @param bfsDistTo array of integers that hold the distances between vertices 
	 * @param bfsEdge array of integers that hold the number of edges 
	 */
	public static void bfs(Graph G, int s, boolean[] bfsMark, int[] bfsDistTo, int[] bfsEdge){
		
		Queue<Integer> q = new Queue<Integer>();  
		
		for (int i = 0; i < G.V(); i++)
		{
			bfsDistTo[i] = Integer.MAX_VALUE;
		}
		bfsMark[s] = true;
		bfsDistTo[s] = 0;
		q.enqueue(s);
		while (!q.isEmpty()) 
		{
			int val = q.dequeue();
			for (int i : G.adj(val))
			{
				if (!bfsMark[i])
				{
					bfsEdge[i] = val;
					bfsDistTo[i] = bfsDistTo[val] + 1;
					bfsMark[i] = true;
					q.enqueue(i);

				}

			}

		}

	}
	
/*
	public static int bfsDistanceTo(Graph G, int s, int curr, boolean[] bfsMark, int[] bfsEdge){
		int length = -1;
		if (!bfsMark[curr]){ //If the current position in the graph hasnt already been seen 
			return Integer.MAX_VALUE;
		}
		Stack<Integer> paths = new Stack<Integer>(); //used to determine the path of the search 
		
		for (int i = curr; i != s; i = bfsEdge[i]){ // looping through graph 
			paths.push(i); 
		}
		paths.push(s);
		while (!paths.isEmpty()){ 
			paths.pop(); //When we pop a value we are also keeping a count and going until the stack is empty which 
						// theoretically means we have reached our destination 
			length++;
		}
		return length;
	}
	*/

	public static void main(String[] args) {
		String input;
		if (args.length != 0) {
			input = args[0];
		} else {
			input = "tinyG.txt";
		}
		In in = new In(input);
		Graph g = new Graph(in);

		// Compute and report Excess on tinyG.txt by default
		System.out.println("Excess:" + SearchCompare.excess(g, 0));
		System.out.println("N" + '\t' + "Excess");

		for (int N = 4; N <= 1024; N *= 2) {
			System.out.print(N + "\t");
			for (double p = 0.5; p <= 1.0; p += 0.5) {
				Graph gr = new Graph(N);

				// every possible edge exists with probability p
				for (int i = 0; i < N - 1; i++) {
					for (int j = i + 1; j < N; j++) {
						if (Math.random() < p) {
							gr.addEdge(i, j);
						}
					}
				}

				System.out.print('\t' + SearchCompare.excess(gr, 0));
			}
			System.out.println();

		}
	}

}