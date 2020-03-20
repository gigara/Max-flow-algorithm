/* *****************************************************************************
 *  Name:    Chamupathi Gigara Hettige
 *
 *  Description:  data structure
 *
 **************************************************************************** */
package com.giga.algo.cwk;// Created by Gigara on 2019-03-23


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MaxFlow {

    private static BigDecimal timeElapsed;
    private static int[][] residualGraph;
    private static List<PathAndFlow> pathsAndFlows = new ArrayList<>();
    private int[][] graph;

    public MaxFlow(int[][] graph) {
        this.graph = graph;
    }

    private static int maxFlow(int[][] graph) {
        long start = System.nanoTime();
        int size = graph.length;

        // initialize residual graph
        residualGraph = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                residualGraph[i][j] = graph[i][j];
            }
        }

        // create an array to store parent node to find the path
        int[] parent = new int[size];
        // set parent of source to -1
        parent[0] = -1;

        int maxFlow = 0;

        while (augmentingPath(residualGraph, parent)) {

            // find the maximum flow capacity of the selected path
            int pathMaxFlow = Integer.MAX_VALUE;
            for (int node = size - 1; node != 0; node = parent[node]) {
                pathMaxFlow = Math.min(pathMaxFlow, residualGraph[parent[node]][node]);
            }
            // add the maximum flow capacity of selected path to the network's flow capacity
            maxFlow += pathMaxFlow;

            // saving path and flow in to the arrayList
            int[] parentX = new int[size];
            for (int i = 0; i < size; i++) {
                parentX[i] = parent[i];
            }
            PathAndFlow pathAndFlow = new PathAndFlow(parentX, pathMaxFlow);
            pathsAndFlows.add(pathAndFlow);

            // update the residual graph
            for (int node = size - 1; node != 0; node = parent[node]) {
                residualGraph[parent[node]][node] -= pathMaxFlow;
            }
        }

        long finish = System.nanoTime();
        timeElapsed = new BigDecimal(String.valueOf(finish - start)).divide(new BigDecimal("1000000"), 4, RoundingMode.CEILING);
        return maxFlow;
    }

    // find whether is there any augmenting path to flow using breadth first search
    private static boolean augmentingPath(int[][] residualGraph, int[] parent) {
        int size = residualGraph.length;

        // create an array to store visited nodes
        boolean[] visited = new boolean[size];

        // create a queue to store nodes that needs to be visited
        LinkedList<Integer> queue = new LinkedList<>();

        // add source node to queue and make is as visited
        queue.add(0);
        visited[0] = true;

        while (queue.size() != 0) {
            int u = queue.poll();

            for (int v = 0; v < size; v++) {
                if (residualGraph[u][v] != 0 && !visited[v]) {
                    queue.add(v);
                    visited[v] = true;
                    parent[v] = u;
                }
            }
        }

        return visited[size - 1];
    }

    public int getMaxFlow() {
        return maxFlow(graph);
    }

    public BigDecimal getTimeElapsed() {
        return timeElapsed;
    }

    public int[][] getResidualGraph() {
        return residualGraph;
    }

    public List<PathAndFlow> getPathsAndFlows() {
        return pathsAndFlows;
    }
}
