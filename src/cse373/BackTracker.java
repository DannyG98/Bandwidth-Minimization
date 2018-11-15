package cse373;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.plaf.metal.MetalTheme;

/**
 * The backtracking class that handles all the backtracking nuances.
 * @author Dan
 */
public class BackTracker {

    private Graph usingGraph;
    public int minBandwidth;
    private ArrayList<Integer> bestSolution;
    private int lowerBound;
    private boolean abort;

    public BackTracker(Graph g) {
        usingGraph = g;
        bestSolution = usingGraph.parseCM();
        minBandwidth = findBandwidth(bestSolution);
        lowerBound = findLowerBound();
        abort = false;
        
    }
    
    /**
     * Initializes the backtracking algorithm
     * @return returns the optimal bandwidth sequence
     */
    public ArrayList<Integer> findOptimalBandwidth() {
        ArrayList<Integer> solutionArray = new ArrayList<>(Collections.nCopies(usingGraph.getNumVertices(), 0));
        abort = false;
        
        backtrack(solutionArray, 0);

        return bestSolution;
    }

    /**
     * Recursive backtracking algorithm
     * @param solutionArray the solution
     * @param currentIndex the current index being filled
     */
    public void backtrack(ArrayList<Integer> solutionArray, int currentIndex) {
        //Checks if signal to kill recursion has been given
        if (abort) return;
        
        
        if(isASolution(solutionArray, currentIndex)) {
            processSolution(solutionArray);
            return;
        }

        //Creates the candidate list
        ArrayList<Integer> candidateList = createCandidates(solutionArray, currentIndex);
        
        //Loops through the candidate list
        for (int i = 0; i < candidateList.size(); i++) {
            //Checks if the signal to kill recursion has been given
            if (abort) return;
            
            
            //Finds the cost of adding the current candidate to the list
            int penalty = getBandwidthPenalty(solutionArray, currentIndex, candidateList.get(i));
            
            //If the cost is less than the bandwidth of the current best solution, continue
            if (penalty < minBandwidth) {
                solutionArray.set(currentIndex, candidateList.get(i));
                backtrack(solutionArray, currentIndex+1);
            
            }
            //If not, we prune the branch
            else{
                solutionArray.set(currentIndex, 0);
                return;
            }
        }
        solutionArray.set(currentIndex, 0);
    }

    /**
     * Checks to see if the current solution array is a solution
     * @param solutionArray the solution array
     * @param currentIndex the current index
     * @return 
     */
    private boolean isASolution(ArrayList<Integer> solutionArray, int currentIndex) {
        return currentIndex == usingGraph.getNumVertices();
    }

    /**
     * Processes the completed solution array. If the completed solution is
     * better than the current solution, replace the current solution and update
     * the minimum bandwidth found.
     * @param solutionArray the completed solution array
     */
    private void processSolution(ArrayList<Integer> solutionArray) {
        int bandwidth = findBandwidth(solutionArray);
        
        if (bandwidth < minBandwidth) {
            minBandwidth = bandwidth;
            bestSolution = new ArrayList<>(solutionArray);
        }
        
        if (bandwidth == lowerBound) {
            abort = true;
        }
    }

    /**
     * Finds the bandwidth of the current array
     * @param solutionArray the solution array
     * @return returns the bandwidth as an int
     */
    private int findBandwidth(ArrayList<Integer> solutionArray) {
        int bandwidth = 0;

        for (int i = 0; i < solutionArray.size()-1; i++) {
            for (int j = i; j < solutionArray.size(); j++) {
                if (usingGraph.isEdge(solutionArray.get(i), solutionArray.get(j)))
                    if (bandwidth < j - i)
                        bandwidth = j - i;
            }
            }
        return bandwidth;
    }


    /**
     * Creates a list of candidates for the current index. Ignores already placed
     * vertices. The candidates are shuffled to ensure ordering will not hinder
     * the algorithm.
     * @param solutionArray the solution array
     * @param currentIndex the current index
     * @return returns an ArrayList of candidates
     */
    private ArrayList<Integer> createCandidates(ArrayList<Integer> solutionArray, int currentIndex) {
        ArrayList<Integer> candidateArray = new ArrayList<>();
        
        for (int i = 1; i <= usingGraph.getNumVertices(); i++) {
                candidateArray.add(i);
        }

//        for (int i = 0; i < currentIndex; i++) {
//            candidateArray.remove(solutionArray.get(i));
//        }
        
        candidateArray.removeAll(solutionArray);
       
        //Shuffles the candidate array before returning
        Collections.shuffle(candidateArray);
        
        //Prioritize vertexes adjacent to the previous index
        if (currentIndex != 0) {
            ArrayList<Integer> adjacents = new ArrayList<>();
            HashSet<Integer> toAdd = usingGraph.getAdjacent(solutionArray.get(currentIndex - 1));

            if (toAdd != null) {
                adjacents.addAll(toAdd);
            } else {
                return candidateArray;
            }
            
            candidateArray.removeAll(adjacents);
            adjacents.removeAll(solutionArray);
            
            adjacents.addAll(candidateArray);
            
            return adjacents;
            
        }
        
        return candidateArray;
    }


    /**
     * Calculates the bandwidth cost for adding num to solutionArray[index]
     * @param solutionArray the solution array
     * @param index the index we are currently filling in
     * @param num the number we want to place in
     * @return returns the bandwidth cost as an int
     */
    private int getBandwidthPenalty(ArrayList<Integer> solutionArray, int index, int num) {
        int currentBandwidth = 0;

        for (int i = 0; i < index; i++) {
            if (usingGraph.isEdge(solutionArray.get(i), num))
                if (currentBandwidth < index - i) {
                    currentBandwidth = index - i;
                }
                    
        }
        return currentBandwidth;
    }
    
    /**
     * Determines the lowest bound for the graph 
     * @return returns the lowest bound as an int
     */
    private int findLowerBound() {
        int greatestDegree = 0;
        HashMap<Integer, HashSet<Integer>> adjList = usingGraph.getAdjList();
        
        for (Integer x: adjList.keySet()) {
            if (adjList.get(x).size() > greatestDegree)
                greatestDegree = adjList.get(x).size();
        }
        
        return (int) Math.ceil(greatestDegree/2);
    }
    
}



