package sjordhani.hw5;

import junit.framework.TestCase;

/**
 * Use these test cases to validate your algorithms for HW5 on a Graph.
 *
 */
public class TestGraph extends TestCase {
	
	/** This corresponds to the homework image
	 
	            [0]
	           /   \
	          [1]--[2]
	         /       \
	       [3]       [4]
	                   \
	                    [5]
	                      \
	                      [6]
	 
	 */
	public void testStatus() {
		Graph g = new Graph(7);
		g.addEdge(0, 1);
		g.addEdge(0, 2);
		g.addEdge(1, 3);
		g.addEdge(1, 2);
		g.addEdge(2, 4);
		g.addEdge(4, 5);
		g.addEdge(5, 6);
		
		assertEquals (13, g.status(0));
		assertEquals (12, g.status(1));
		assertEquals (10, g.status(2));
		assertEquals (19, g.status(6));
		assertTrue (g.connected());
		assertTrue (g.statusInjective());
		
		// diameter is path from [3] to [6] 
		assertEquals (5, g.diameter());
		
		// add edge [3,6] and no longer SIJ
		g.addEdge(3, 6);
		assertTrue (g.connected());
		assertFalse (g.statusInjective());
		assertEquals (3, g.diameter());  // diameter shrunk to 3
	}
	
	public void testDisconnected() { 
		Graph g = new Graph(7);
		g.addEdge(0, 2);
		g.addEdge(2, 4);
		g.addEdge(1, 3);
		
		assertFalse (g.connected());
		assertFalse (g.statusInjective());
		assertEquals (Integer.MAX_VALUE, g.diameter());
	}
}
