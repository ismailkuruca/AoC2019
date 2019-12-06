package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashSet;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;

public class Day6 {

    public static void main(String[] args) {
        final List<String> day6 = FileUtil.readFile("day6");

        DefaultDirectedGraph<String, DefaultEdge> directedGraph
                = new DefaultDirectedGraph<>(DefaultEdge.class);

        final HashSet<String> nodes = new HashSet<>();
        day6.forEach(s -> {
            final String[] orbit = s.split("\\)");
            if (!directedGraph.containsVertex(orbit[0])) {
                directedGraph.addVertex(orbit[0]);
            }
            if (!directedGraph.containsVertex(orbit[1])) {
                directedGraph.addVertex(orbit[1]);
            }
            directedGraph.addEdge(orbit[0], orbit[1]);

            //for part2
//            directedGraph.addEdge(orbit[1], orbit[0]);
            nodes.add(orbit[0]);
            nodes.add(orbit[1]);
        });
        nodes.remove("COM");

        part1(directedGraph, nodes);
        part2(directedGraph);
    }

    private static void part2(DefaultDirectedGraph<String, DefaultEdge> directedGraph) {
        final DijkstraShortestPath<String, DefaultEdge> di = new DijkstraShortestPath<>(directedGraph);
        final double allPaths = di.getPathWeight("SAN", "YOU");

        System.out.println(allPaths - 2);
    }

    private static void part1(DefaultDirectedGraph<String, DefaultEdge> directedGraph, HashSet<String> nodes) {
        final AllDirectedPaths<String, DefaultEdge> di = new AllDirectedPaths<>(directedGraph);
        final List<GraphPath<String, DefaultEdge>> allPaths = di.getAllPaths(nodes, nodes, true, MAX_VALUE);
        System.out.println(allPaths.size());
    }
}