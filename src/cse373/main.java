package cse373;

import java.util.ArrayList;

public class main {

    public static void main(String[] args) {
        BackTracker g1 = new BackTracker(new Graph("g-bt-14-13"));
        ArrayList<Integer> g1Optimal = g1.findOptimalBandwidth();

        printArrayList(g1Optimal);
        System.out.println();
        System.out.println(g1.minBandwidth);


//        Graph g1 = new Graph("g-bt-10-9");
//        System.out.println(g1.isEdge(4, 9));


    }

    public static void printArrayList(ArrayList<Integer> a) {
        for (Integer i: a) {
            System.out.print(i + " ");
        }
    }

}
