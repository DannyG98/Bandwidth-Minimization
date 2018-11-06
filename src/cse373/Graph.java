package cse373;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Graph {

    private LinkedList<LinkedList<Integer>> adjacencyList;
    private static final String DATA_PATH = "data";
    private int numVertices;
    private int numEdges;

    public Graph(String filePath) {
        File selectedFile = new File(DATA_PATH + "/" + filePath);
        adjacencyList = new LinkedList<>();

        try {
            //Read the file
            Scanner reader = new Scanner(selectedFile);
            numVertices = Integer.parseInt(reader.nextLine());
            numEdges = Integer.parseInt(reader.nextLine());

            //Create entries for each vertice in adjacency list
            for (int i = 0; i < numVertices; i++)
                adjacencyList.add(new LinkedList<Integer>());

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

    public LinkedList<Integer> getAdjacent(int n) {
        return adjacencyList.get(n-1);
    }

    public int getNumVertices() {
        return numVertices;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public LinkedList<LinkedList<Integer>> getAdjList() {
        return adjacencyList;
    }

    public boolean isEdge(int a, int b) {
        return getAdjacent(a).contains(b);
    }

}
