package cse373;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
        if(a == 0 || b == 0) 
            return false;
        return getAdjacent(a).contains(b);
    }

    /**
     * Apply the Cuthill-McKee heuristic algorithm
     * @return returns a solution array
     */
    public ArrayList<Integer> parseCM() {
        Set<Integer> keys = adjacencyList.keySet();
        ArrayList<Integer> heuristicSolution = new ArrayList<>();
        
        heuristicSolution.add(findLowestDegree(keys));
        
        for (int i = 0; i < adjacencyList.size(); i++) {
            ArrayList<Integer> adjacents = new ArrayList<>(getAdjacent(heuristicSolution.get(i)));
            
            adjacents.removeAll(heuristicSolution);
            adjacents.sort(new vertexDegreeComparator());
            
            heuristicSolution.addAll(adjacents);
        }
        
        return heuristicSolution;
    }

    private int findLowestDegree(Set<Integer> keys) {
        int lowest = Integer.MAX_VALUE;
        int vertex = -1;

        for (Integer x : keys) {
            int size = getAdjacent(x).size();
            if (size < lowest) {
                lowest = size;
                vertex = x;
            }
        }

        return vertex;
    }
    
    private class vertexDegreeComparator implements Comparator<Integer> {
            
            @Override
            public int compare(Integer a, Integer b) {
                int aSize = getAdjacent(a).size();
                int bSize = getAdjacent(b).size();
                
                if (aSize == bSize) return 0;
                else if (aSize < bSize) return -1;
                else return 1;
            }
        }
    
    //This method is only for testing purposes, but can replace the findBandwidth in the BackTracker class
    public int findBandwidth(ArrayList<Integer> solutionArray) {
        int bandwidth = 0;

        for (int i = 0; i < solutionArray.size()-1; i++) {
            for (int j = i; j < solutionArray.size(); j++) {
                if (isEdge(solutionArray.get(i), solutionArray.get(j)))
                    if (bandwidth < j - i)
                        bandwidth = j - i;
            }
            }
        return bandwidth;
    }

}
