package cse373;

import java.util.ArrayList;
import java.util.Collections;

public class BackTracker {

    private Graph usingGraph;
    public int minBandwidth;
    private ArrayList<Integer> bestSolution;

    public BackTracker(Graph g) {
        usingGraph = g;
        minBandwidth = g.getNumVertices() - 1;
    }

    public ArrayList<Integer> findOptimalBandwidth() {
        ArrayList<Integer> solutionArray = new ArrayList<>(Collections.nCopies(usingGraph.getNumVertices(), 0));

        backtrack(solutionArray, 0);

        return bestSolution;
    }

    public void backtrack(ArrayList<Integer> solutionArray, int currentIndex) {
        
        if(isASolution(solutionArray, currentIndex)) {
            processSolution(solutionArray);
            return;
        }

        ArrayList<Integer> candidateList = createCandidates(solutionArray, currentIndex);
        for (int i = 0; i < candidateList.size(); i++) {
            if (getBandwidthPenalty(solutionArray, currentIndex, candidateList.get(i)) < minBandwidth) {
                solutionArray.set(currentIndex, candidateList.get(i));
                backtrack(solutionArray, currentIndex+1);
            
            }
        }
    }

    private boolean isASolution(ArrayList<Integer> solutionArray, int currentIndex) {
        return currentIndex == usingGraph.getNumVertices();
    }

    private void processSolution(ArrayList<Integer> solutionArray) {
        int bandwidth = findBandwidth(solutionArray);
        if (bandwidth < minBandwidth) {
            minBandwidth = bandwidth;
            bestSolution = new ArrayList<>(solutionArray);
        }
    }

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


    private ArrayList<Integer> createCandidates(ArrayList<Integer> solutionArray, int currentIndex) {
        ArrayList<Integer> candidateArray = new ArrayList<>();

        for (int i = 1; i <= usingGraph.getNumVertices(); i++) {
            candidateArray.add(i);
        }

        for (int i = 0; i <= currentIndex; i++) {
            candidateArray.remove(solutionArray.get(i));
        }

        return candidateArray;
    }


    //TODO: Test this function
    private int getBandwidthPenalty(ArrayList<Integer> solutionArray, int index, int num) {
        int currentBandwidth = 0;

        for (int i = 0; i < index; i++) {
            if (usingGraph.isEdge(solutionArray.get(i), num))
                if (currentBandwidth < index - i)
                    currentBandwidth = index - i;
        }

        return currentBandwidth;
    }

}



