/* *****************************************************************************
 *  Name:    Chamupathi Gigara Hettige
 *
 *  Description:  generate tht graph - gui
 *
 **************************************************************************** */
package com.giga.algo.cwk;// Created by Gigara on 2019-03-24

import java.util.concurrent.ThreadLocalRandom;

public class Graph {
    private int length;
    private int[][] graph;

    public Graph(int length) {
        this.length = length;

        graph = new int[length][length];

    }

    public void generate() {
        // capacities on the edges
        for (int u = 0; u < length - 1; u++) {
            for (int v = 1; v < length; v++) {
                if (u == v || (u == 0 && v == length - 1)) {
                    removeEdge(u, v);

                } else {
                    int capacity = ThreadLocalRandom.current().nextInt(5, 20);
                    addEdge(u, v, capacity);
                }

            }
        }
    }

    public void addEdge(int u, int v, int capacity) {
        graph[u][v] = capacity;
    }

    public void removeEdge(int u, int v) {
        graph[u][v] = 0;
    }

    public boolean isConnected(int u, int v) {
        return graph[u][v] != 0;
    }

    public int[][] getGraph() {
        return graph;
    }

    public int getLength() {
        return length;
    }
}
