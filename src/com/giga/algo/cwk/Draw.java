/* *****************************************************************************
 *  Name:    Chamupathi Gigara Hettige
 *
 *  Description:  show the graph in gui
 *
 **************************************************************************** */
package com.giga.algo.cwk;// Created by Gigara on 2019-03-25

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

public class Draw {
    private static DirectedSparseGraph g, maxFlowNetwork;
    private int[][] graph, flo;

    public Draw(int[][] graph) {
        this.graph = graph;
    }

    // draw nodes
    private static void drawNodes(DirectedSparseGraph g, int length) {

        for (int u = 0; u < length; u++) {
            if (u == 0) {
                g.addVertex("S");

            } else if (u == length - 1) {
                g.addVertex("T");

            } else {
                g.addVertex((Integer) u);
            }
        }

    }

    private static void drawEdges(DirectedSparseGraph g, int[][] graph, String prefix) {

        int e = 0;
        for (int u = 0; u < graph.length; u++) {
            for (int v = 0; v < graph.length; v++) {
                if (graph[u][v] != 0) {

                    if (u == 0) {
                        g.addEdge(prefix + e++ + "_" + graph[u][v], "S", v);

                    } else if (v == graph.length - 1) {
                        g.addEdge(prefix + e++ + "_" + graph[u][v], u, "T");

                    } else {
                        g.addEdge(prefix + e++ + "_" + graph[u][v], u, v);
                    }

                }
            }
        }
    }

    private static void renderItems(VisualizationViewer vv) {
        Transformer transformer = new Transformer() {
            @Override
            public String transform(Object o) {
                return "<html><font size=\"5\">" + o.toString();
            }
        };
        vv.getRenderContext().setVertexLabelTransformer(transformer);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.AUTO);

        transformer = new Transformer<String, String>() {
            @Override
            public String transform(String o) {
                return "<html><font color=\"blue\" size=\"5\">" + o.split("_")[1];
            }
        };
        vv.getRenderContext().setEdgeLabelTransformer(transformer);

        Transformer<String, Paint> colorTransformer = new Transformer<String, Paint>() {
            @Override
            public Paint transform(String e) {
                if (Pattern.matches("(EFI)\\w+", e.split("_")[0])) {
                    return Color.RED;

                } else if (Pattern.matches("(EF)\\w+", e.split("_")[0])) {
                    return Color.BLUE;
                }

                return Color.black;
            }
        };
        vv.getRenderContext().setArrowFillPaintTransformer(colorTransformer);
        vv.getRenderContext().setArrowDrawPaintTransformer(colorTransformer);
        vv.getRenderContext().setEdgeDrawPaintTransformer(colorTransformer);
    }

    // draw flow network
    public VisualizationViewer getFlowNetwork() {
        g = new DirectedSparseGraph();

        drawNodes(g, graph.length);
        drawEdges(g, graph, "E");

        VisualizationViewer vv = new VisualizationViewer(new CircleLayout(g), new Dimension(500, 700));
        renderItems(vv);

        return vv;
    }

    public VisualizationViewer getwMaxFlowNetowrk(MaxFlow maxFlow) {
        maxFlowNetwork = new DirectedSparseGraph();

        MaxFlow flow = new MaxFlow(graph);
        System.out.println(flow.getMaxFlow());

        int[][] res = flow.getResidualGraph();

        flo = new int[graph.length][graph.length];

        for (int u = 0; u < graph.length; u++) {
            for (int v = 0; v < graph.length; v++) {
                flo[u][v] = graph[u][v] - res[u][v];
            }
        }

        drawNodes(maxFlowNetwork, graph.length);
        drawEfficientPath(maxFlow);
        drawEdges(maxFlowNetwork, flo, "EF");

        VisualizationViewer vv = new VisualizationViewer(new CircleLayout(maxFlowNetwork), new Dimension(500, 700));
        renderItems(vv);

        return vv;
    }

    // draw the most efficient path
    public void drawEfficientPath(MaxFlow maxFl) {
        //find the most efficient path
        Comparator<PathAndFlow> comparator = Comparator.comparing(PathAndFlow::getFlow);
        Collections.sort(maxFl.getPathsAndFlows(), comparator.reversed());

        int[] path = maxFl.getPathsAndFlows().get(0).getPath();

        int x = 0;
        for (int i = path.length - 1; i > 0; i = path[i]) {
            if (i == path.length - 1) {
                maxFlowNetwork.addEdge("EFI" + (x++) + "_" + flo[path[i]][path.length - 1], path[i], "T");

            } else if (path[i] == 0) {
                maxFlowNetwork.addEdge("EFI" + (x++) + "_" + flo[0][i], "S", i);

            } else {
                maxFlowNetwork.addEdge("EFI" + (x++) + "_" + flo[path[i]][i], path[i], i);
            }
        }
    }
}
