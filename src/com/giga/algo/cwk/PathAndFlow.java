/* *****************************************************************************
 *  Name:    Chamupathi Gigara Hettige
 *
 *  Description:  this object type is used to save the augmenting path and its flow value
 *
 **************************************************************************** */
package com.giga.algo.cwk;// Created by Gigara on 2019-03-30

import java.util.Arrays;

public class PathAndFlow {
    private int[] path;
    private int flow;

    public PathAndFlow(int[] path, int flow) {
        this.path = path;
        this.flow = flow;
    }

    public int[] getPath() {
        return path;
    }

    public int getFlow() {
        return flow;
    }

    @Override
    public String toString() {
        return "PathAndFlow{" +
                "path=" + Arrays.toString(path) +
                ", flow=" + flow +
                '}';
    }
}
