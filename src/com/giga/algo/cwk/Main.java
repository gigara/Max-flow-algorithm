/* *****************************************************************************
 *  Name:    Chamupathi Gigara Hettige
 *
 *  Description:  Prints the maximum flow value of the generated network.
 *                Used algorithm: ford-fulkerson.
 *
 **************************************************************************** */
package com.giga.algo.cwk;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import lib.StdDraw;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {

        Graph g = new Graph(ThreadLocalRandom.current().nextInt(4, 12));
        g.generate();
        int[][] graph1 = g.getGraph();

        StdDraw.setCanvasSize(300, 500);
        StdDraw.setXscale(0, 10);
        StdDraw.setYscale(-5, 5);

        // show generated matric
        Font H1 = new Font("SansSerif", Font.BOLD, 24);
        Font H2 = new Font("SansSerif", Font.PLAIN, 14);
        StdDraw.setFont(H2);
        showMetrix(graph1);

        // Show max FLow value and time elapsed
        MaxFlow maxFl = new MaxFlow(graph1);
        int maxFlow = maxFl.getMaxFlow();
        String timeElapsed = maxFl.getTimeElapsed() + " ms";

        StdDraw.setFont(H1);
        StdDraw.textLeft(1, -4, "Max Flow: " + maxFlow);
        StdDraw.setFont(H2);
        StdDraw.textLeft(1, -4.5, "Time Elapsed: " + timeElapsed);

        // draw graphs
        Draw draw = new Draw(graph1);
        VisualizationViewer vv = draw.getFlowNetwork();
        vv.setBackground(Color.LIGHT_GRAY);
        VisualizationViewer vf = draw.getwMaxFlowNetowrk(maxFl);
        vf.setBackground(Color.ORANGE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        // create JPanels and set back colors
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        panel1.setBackground(Color.DARK_GRAY);
        panel2.setBackground(Color.DARK_GRAY);
        panel3.setBackground(Color.DARK_GRAY);

        // Show Headings
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        //panel1.add(new JLabel("<html><font color='white'>Generated Flow Network (Data Structure)</font></html>"));
        panel2.add(new JLabel("<html><font color='white'>Generated Flow Network (Graph)</font></html>"));
        panel3.add(new JLabel("<html><font color='white'>Maximum Flow</font></html>"));
        vf.add(new JLabel("<html><font color='black'>Most efficient path is shown in</font>" +
                "<font color='red'> red</font> <font color='black'>color</font></html>"));

        panel1.add(StdDraw.frame.getContentPane());
        panel2.add(vv);
        panel3.add(vf);

        container.add(panel1);
        container.add(panel2);
        container.add(panel3);

        JFrame frame = new JFrame();
        frame.setSize(1500, 700);
        frame.add(container);
        frame.setVisible(true);
        frame.setTitle("Find Max Flow | Chamupathi Gigara Hettige");

        //find the most efficient path
        Comparator<PathAndFlow> comparator = Comparator.comparing(PathAndFlow::getFlow);
        Collections.sort(maxFl.getPathsAndFlows(), comparator.reversed());

    }

    // generate random flow network
    private static int[][] generateFlowNetwork() {
        // generate a flow network with at least 4 and at most 10 nodes
        int length = ThreadLocalRandom.current().nextInt(4, 12);
        int graph[][] = new int[length][length];

        // capacities on the edges
        for (int u = 0; u < length - 1; u++) {
            for (int v = 1; v < length; v++) {
                if (u == v || (u == 0 && v == length - 1)) {
                    graph[u][v] = 0;

                } else {
                    graph[u][v] = ThreadLocalRandom.current().nextInt(5, 20);
                }

            }
        }

        return graph;
    }

    // show generated metric in GUI
    private static void showMetrix(int graph[][]) {
        double x = 0.5;
        double y = 3.6;
        int length = graph.length;
        // print header
        for (int p = 0; p < length; p++) {
            if (p == 0) {
                StdDraw.textRight(x += .8, y, "S");

            } else if (p == length - 1) {
                StdDraw.textRight(x += .8, y, "t");
            } else {
                StdDraw.textRight(x += .8, y, String.valueOf(p));
            }
        }
        StdDraw.line(0.5, 3.4, x + .3, 3.4);
        // print left
        x = .5;
        y = 3.5;
        for (int p = 0; p < length; p++) {
            if (p == 0) {
                StdDraw.textRight(x, y -= .5, "S");

            } else if (p == length - 1) {
                StdDraw.textRight(x, y -= .5, "t");
            } else {
                StdDraw.textRight(x, y -= .5, String.valueOf(p));
            }
        }
        StdDraw.line(.8, 3.5, .8, y - .2);

        // print flow network
        StringBuilder sb = new StringBuilder();
        for (int[] s1 : graph) {
            for (int s2 : s1) {
                sb.append(s2).append(' ');
            }
        }
        String table = sb.toString();

        x = .5;
        y = 3;
        int count = 0;
        for (String capacity : table.split(" ")) {
            StdDraw.textRight(x += .8, y, capacity);
            count++;
            if (count == length) {
                y -= .5;
                x = .5;
                count = 0;
            }
        }
    }

//    public static int fordFulkerson(int[][] caps, int source, int sink) {
//
//        int n = caps.length;
//        int [] visited = new int[n];
//        boolean [] minCut = new boolean[n];
//
//        for(int maxFlow = 0;;) {
//
//            // Try to find an augmenting path from source to sink
//            int flow = dfs(caps, visited, source, sink, Integer.MAX_VALUE);
//            visitedToken++;
//
//            maxFlow += flow;
//            if (flow == 0) {
//
//                return maxFlow;
//
//            }
//        }
//    }
//
//    private static int dfs(int[][] caps, int[] visited, int node, int sink, int flow) {
//
//        // Found sink node, return flow thus far
//        if (node == sink) return flow;
//
//        int[] cap = caps[node];
//        visited[node] = visitedToken;
//
//        for (int i = 0; i < cap.length; i++) {
//            if (visited[i] != visitedToken && cap[i] > 0) {
//
//                if (cap[i] < flow) flow = cap[i];
//                int dfsFlow = dfs(caps, visited, i, sink, flow);
//
//                if (dfsFlow > 0) {
//                    caps[node][i] -= dfsFlow;
//                    caps[i][node] += dfsFlow;
//                    return dfsFlow;
//                }
//
//            }
//        }
//
//        return 0;
//
//    }
}