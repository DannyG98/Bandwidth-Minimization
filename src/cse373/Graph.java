package cse373;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Graph class for parsing and representing the files. Graph is represented using
 * a HashMap where the key is a vertice and the value is a HashSet of adjacent vertices.
 * @author Dan
 */
public class Graph {

    private HashMap<Integer, HashSet<Integer>> adjacencyList;
    private static final String DATA_PATH = "data";
    private int numVertices;
    private int numEdges;

    public Graph(String filePath) {
        File selectedFile = new File(DATA_PATH + "/" + filePath);
        adjacencyList = new HashMap<>();

        try {
            //Read the file
            Scanner reader = new Scanner(selectedFile);
            numVertices = Integer.parseInt(reader.nextLine());
            numEdges = Integer.parseInt(reader.nextLine());

            //Create entries for each vertice in adjacency list
            for (int i = 1; i <= numVertices; i++)
                adjacencyList.put(i, new HashSet<>());

            //Begin populating the adjacency list
            while (reader.hasNext()) {
                String[] currentEdge = reader.nextLine().split(" +");

                //Adds the edge to the adjacency list of both vertices
                getAdjacent(Integer.parseInt(currentEdge[0])).add(Integer.parseInt(currentEdge[1]));
                getAdjacent(Integer.parseInt(currentEdge[1])).add(Integer.parseInt(currentEdge[0]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("INVALID FILE PATH");
        }
    }

    public HashSet getAdjacent(int n) {
        return adjacencyList.get(n);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public HashMap<Integer, HashSet<Integer>> getAdjList() {
        return adjacencyList;
    }

    public boolean isEdge(int a, int b) {
        return getAdjacent(a).contains(b);
    }

}
