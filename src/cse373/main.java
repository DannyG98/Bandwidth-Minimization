package cse373;

import java.util.ArrayList;

public class main {

    public static void main(String[] args) {
        String fileName = "g-gg-24-38";
       
        BackTracker g1 = new BackTracker(new Graph(fileName));
        System.out.println("Running: " + fileName);
        
        ArrayList<Integer> g1Optimal = g1.findOptimalBandwidth();
        
        printArrayList(g1Optimal);
        System.out.println();
        
        System.out.println("Optimal Bandwidth Number:");
        System.out.println(g1.minBandwidth);

    }

    public static void printArrayList(ArrayList<Integer> a) {
        System.out.println("Optimal Bandwidth Sequence:");
        for (Integer i: a) {
            System.out.print(i + " ");
        }
    }

}
