package cse373;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

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
        minBandwidth = g.getNumVertices();
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
        
        backtrack(solutionArray, 0, -1, solutionArray.size(), false);

        return bestSolution;
    }

    /**
     * Recursive backtracking algorithm
     * @param solutionArray the solution
     * @param currentIndex the current index being filled
     */
    public void backtrack(ArrayList<Integer> solutionArray, int currentIndex, int leftBound, int rightBound, boolean flipped) {
        //Checks if signal to kill recursion has been given
        if (abort) return;
        
        
        if(isASolution(solutionArray, currentIndex, leftBound, rightBound)) {
            processSolution(solutionArray);
            return;
        }

        //Creates the candidate list
        ArrayList<Integer> candidateList = createCandidates(solutionArray, currentIndex, leftBound, rightBound, flipped);
        
        //Loops through the candidate list
        for (int i = 0; i < candidateList.size(); i++) {
            //Checks if the signal to kill recursion has been given
            if (abort) return;
            
            solutionArray.set(currentIndex, candidateList.get(i));
            
            //Finds the cost of adding the current candidate to the list
            //int penalty = getBandwidthPenalty(solutionArray, currentIndex, leftBound, rightBound, candidateList.get(i));
            int penalty = findBandwidth(solutionArray);
            
            //If the cost is less than the bandwidth of the current best solution, continue
            if (penalty < minBandwidth) {
                if (flipped == false)
                    backtrack(solutionArray, rightBound-1, leftBound+1, rightBound, true);
                else
                    backtrack(solutionArray, leftBound+1, leftBound, rightBound-1, false);
            }
            //If not, we prune the branch
//            else{
//                return;
//            }
        }
        solutionArray.set(currentIndex, 0);
    }

    /**
     * Checks to see if the current solution array is a solution
     * @param solutionArray the solution array
     * @param currentIndex the current index
     * @return 
     */
    private boolean isASolution(ArrayList<Integer> solutionArray, int currentIndex, int leftBound, int rightBound) {
        return (currentIndex >= rightBound) || (currentIndex <= leftBound);
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
    private ArrayList<Integer> createCandidates(ArrayList<Integer> solutionArray, int currentIndex, int leftBound, int rightBound, boolean flipped) {
        ArrayList<Integer> candidateArray = new ArrayList<>();
        
        if (currentIndex == solutionArray.size()-1) {
            for (int i = solutionArray.get(0) + 1; i <= usingGraph.getNumVertices(); i++) {
                candidateArray.add(i);
            }
            return candidateArray;
        }
        
        for (int i = 1; i <= usingGraph.getNumVertices(); i++) {
                candidateArray.add(i);
        }
        for (int i = 0; i <= leftBound; i++) {
            candidateArray.remove(solutionArray.get(i));
        }
        for (int i = solutionArray.size()-1; i >= rightBound ; i--) {
            candidateArray.remove(solutionArray.get(i));
        }
        
        //Prioritize Adjacents
        if (currentIndex != 0 && currentIndex != solutionArray.size()-1) {
            ArrayList<Integer> adjacents = new ArrayList<>();
            int adjacentNum;
            
            if (flipped == false) {
                adjacentNum = solutionArray.get(currentIndex-1);
            } else {
                adjacentNum = solutionArray.get(currentIndex-1);
            }
            
            HashSet<Integer> toAdd = usingGraph.getAdjacent(adjacentNum);
            
            if (toAdd != null)
                adjacents.addAll(toAdd);
            else
                return candidateArray;

            for (Integer x: adjacents) {
                candidateArray.remove(x);
            }
            
            adjacents.removeAll(solutionArray);
            
            adjacents.addAll(candidateArray);
            
            return adjacents;
        }
        
        
        //Shuffles the candidate array before returning
        //Collections.shuffle(candidateArray);
        
        return candidateArray;
    }


    /**
     * Calculates the bandwidth cost for adding num to solutionArray[index]
     * @param solutionArray the solution array
     * @param index the index we are currently filling in
     * @param num the number we want to place in
     * @return returns the bandwidth cost as an int
     */
    private int getBandwidthPenalty(ArrayList<Integer> solutionArray, int currentIndex, int leftBound, int rightBound, int num) {
        int currentBandwidth = 0;

        for (int i = 0; i <= leftBound; i++) {
            if (usingGraph.isEdge(solutionArray.get(i), num))
                if (currentBandwidth < currentIndex - i) {
                    currentBandwidth = currentIndex - i;
                }
        }
        
        for (int i = solutionArray.size()-1; i >= rightBound; i--) {
            if (usingGraph.isEdge(solutionArray.get(i), num))
                if (currentBandwidth < i - currentIndex) {
                    currentBandwidth = i - currentIndex;
                }
        }
        
        return currentBandwidth;
    }
    
    private int getCurrentBandwidth(ArrayList<Integer> solutionArray, int currentIndex, int leftBound, int rightBound) {
        ArrayList<Integer> indexes = new ArrayList<>();
        int currentBandwidth = 0;
        
        indexes.add(currentIndex);
        
        for (int i = 0; i <= leftBound; i++) {
            indexes.add(i);
        }
        
        indexes.add(currentIndex);
        
        for (int i = solutionArray.size()-1; i >= rightBound; i--) {
            indexes.add(i);
        }
        
        for (int i = 0; i < indexes.size(); i++) {
            for (int j = i+1; i < indexes.size(); j++) {
                int pos1 = indexes.get(i);
                int pos2 = indexes.get(j);
                
                if (usingGraph.isEdge(solutionArray.get(pos1), solutionArray.get(pos2))) {
                    int bandwidth = indexes.get(j) - indexes.get(i);
                    if (bandwidth > currentBandwidth)
                        currentBandwidth = bandwidth;
                }
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



