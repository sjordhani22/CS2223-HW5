package sjordhani.hw5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import algs.days.day12.SeparateChainingHashST;

// Note that the Day18 implementation of AVL removes <Key,Value> and only uses <Key>
import algs.days.day18.AVL;
import edu.princeton.cs.algs4.Digraph;

/**
 * Modify this class for problem 1 on HW5.
 */
public class WordLadder {

	/**
	 * Represent the mapping of (uniqueID, 4-letter word)
	 */
	static SeparateChainingHashST<String, Integer> table = new SeparateChainingHashST<String, Integer>();
	static SeparateChainingHashST<Integer, String> reverse = new SeparateChainingHashST<Integer, String>();

	/**
	 * Determine if the two same-sized words are off by just a single character.
	 */
	public static boolean offByOne(String w1, String w2) {
		int count = 0; // keeps count of similar characters in the word

		// We are assuming as the homework states that each word is exactly 4 characters
		// long
		for (int i = 0; i < 4; i++) {
			if (w1.charAt(i) == w2.charAt(i)) { // if they are same letter, add 1 to the count
				count++;
			}
		}
		if (count == 3) { // This is checking if 3 of the letters are the same
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Main method to execute.
	 * 
	 * From console, enter the start and end of the word ladder.
	 */
	public static void main(String[] args) throws FileNotFoundException {

// Use this to contain all four-letter words that you find in dictionary
		AVL<String> avl = new AVL<String>();

// create a graph where each node represents a four-letter word.
// Also construct avl tree of all four-letter words.
// Note: you will have to copy this file into your project to access it, unless
// you
// are already writing your code within the SedgewickAlgorithms4ed project.

		Scanner scan = new Scanner(new File("words.english.txt")); // opens text file
		while (scan.hasNext()) {
			String s = scan.next();
			if (s.length() == 4) {
				avl.insert(s); // add all 4 letter words from the dictionary text file into an avl
			}
		}
		scan.close(); // we have to make sure we close the scanner

// now construct graph, where each node represents a word, and an edge exists
// between
// two nodes if their respective words are off by a single letter. Hint: use the
// keys() method provided by the AVL tree.

		Queue<String> q = (Queue<String>) avl.keys();
		int Qsize = q.size();
		// At first I was working with a regular graph however, I realized that we need
		// a digraph
		// in order to get the correct direction between words. Otherwise for example,
		// the last
		// word in the chain could be connected to the second word.
		Digraph diG = new Digraph(Qsize);

		String[] checked = new String[Qsize];

		for (int i = 0; i < Qsize; i++) {
			String value = q.dequeue(); // gets the first value from a queue

			Queue<String> q2 = (Queue<String>) avl.keys();
			for (int j = 0; j < Qsize; j++) {
				String check = q2.dequeue();  

				if (check != null) {

					// validate if they are off by one character difference, and if they are then
					// connect them with an edge
					if (offByOne(check, value)) {
						diG.addEdge(i, j);
					}
				}

			}

			checked[i] = value;
			table.put(value, i);
			reverse.put(i, value);

		}

		StdOut.println("Enter word to start from (all in lower case):");
		String start = StdIn.readString().toLowerCase();
		StdOut.println("Enter word to end at (all in lower case):");
		String end = StdIn.readString().toLowerCase();

// need to validate that these are both actual four-letter words in the
// dictionary
		if (!avl.contains(start)) {
			StdOut.println(start + " is not a valid word in the dictionary.");
			System.exit(-1);
		}
		if (!avl.contains(end)) {
			StdOut.println(end + " is not a valid word in the dictionary.");
			System.exit(-1);
		}

// Once both words are known to exist in the dictionary, then create a search
// that finds shortest distance (should it exist) between start and end.
// be sure to output the words in the word ladder, IN ORDER, from the start to
// end.

		int str = table.get(start);
		int fin = table.get(end);
		BreadthFirstDirectedPaths Search = new BreadthFirstDirectedPaths(diG, str);    
		// make a directed graph that starts at "str" (the first pos)

		if (Search.hasPathTo(fin)) {

			for (int v : Search.pathTo(fin)) {
				System.out.print(reverse.get(v) + " ");
			}
			System.out.println();
		} else {
			System.out.println("There is no path");
		}

	}
}