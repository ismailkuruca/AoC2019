package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Day20 {

    static List<String> day20 = FileUtil.readFile("day20");


    public static void main(String[] args) throws IOException {

        part1();
        part2();
    }


    private static void part2() {
        int depth = 100;

        final DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph = graph(day20, depth);
        HashMap<String, String> portals = new HashMap<>();
        //outer, inner
        portals.put("0,35", "45,76");
        portals.put("0,41", "82,47");
        portals.put("0,45", "82,49");
        portals.put("0,57", "26,31");
        portals.put("0,63", "49,76");
        portals.put("0,65", "63,76");
        portals.put("57,0", "82,69");
        portals.put("37,0", "43,26");
        portals.put("43,0", "82,51");
        portals.put("79,0", "69,76");
        portals.put("45,0", "26,67");
        portals.put("73,0", "49,26");
        portals.put("67,0", "59,26");
        portals.put("59,102", "26,51");
        portals.put("31,102", "39,76");
        portals.put("69,102", "63,26");
        portals.put("73,102", "71,26");
        portals.put("39,102", "82,43");
        portals.put("47,102", "82,65");
        portals.put("53,102", "35,76");
        portals.put("108,35", "26,37");
        portals.put("108,47", "26,53");
        portals.put("108,49", "26,63");
        portals.put("108,39", "82,33");
        portals.put("108,57", "73,76");
        portals.put("108,69", "37,26");
        portals.put("108,61", "77,26");


        for (int i = 1; i < depth; i++) {
            int finalI = i;
            portals.forEach((outer, inner) -> {
                graph.addEdge(finalI + "," + outer + ",.", (finalI - 1) + "," + inner + ",.");
                graph.addEdge((finalI - 1) + "," + inner + ",.", finalI + "," + outer + ",.");

            });
        }

//        39, 102 > 108, 53 ?

        final DijkstraShortestPath<String, DefaultWeightedEdge> di = new DijkstraShortestPath<>(graph);

        final GraphPath<String, DefaultWeightedEdge> path = di.getPath("0,33,102,.", "0,108,53,.");
        System.out.println(path.getWeight());
    }


    private static void part1() {
        final DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph = graph(day20, 1);

        HashMap<String, String> portals = new HashMap<>();
        portals.put("0,35", "45,76");
        portals.put("0,41", "82,47");
        portals.put("0,45", "82,49");
        portals.put("0,57", "26,31");
        portals.put("0,63", "49,76");
        portals.put("0,65", "63,76");
        portals.put("26,37", "108,35");
        portals.put("26,51", "59,102");
        portals.put("26,53", "108,47");
        portals.put("26,63", "108,49");
        portals.put("26,67", "45,0");
        portals.put("37,0", "43,26");
        portals.put("37,26", "108,69");
        portals.put("43,0", "82,51");
        portals.put("49,26", "73,0");
        portals.put("57,0", "82,69");
        portals.put("59,26", "67,0");
        portals.put("63,26", "69,102");
        portals.put("71,26", "73,102");
        portals.put("77,26", "108,61");
        portals.put("79,0", "69,76");
        portals.put("82,33", "108,39");
        portals.put("73,76", "108,57");
        portals.put("82,43", "39,102");
        portals.put("82,65", "47,102");
        portals.put("35,76", "53,102");
        portals.put("39,76", "31,102");

//        39, 102 > 108, 53 ?

        portals.forEach((s, s2) -> {
            graph.addEdge(0 + "," + s + ",.", 0 + "," + s2 + ",.");
            graph.addEdge(0 + "," + s2 + ",.", 0 + "," + s + ",.");
        });

        final DijkstraShortestPath<String, DefaultWeightedEdge> di = new DijkstraShortestPath<>(graph);

        final GraphPath<String, DefaultWeightedEdge> path = di.getPath("0,33,102,.", "0,108,53,.");
        System.out.println(path.getWeight());
    }

    private static DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph(List<String> day20, int depth) {
        DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> directedGraph
                = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        for (int r = 0; r < depth; r++) {
            for (int i = 0; i < day20.size(); i++) {
                final String s = day20.get(i);
                final char[] chars = s.toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    if (chars[j] == '.') {
                        if (!directedGraph.containsVertex(r + "," + i + "," + j + "," + chars[j])) {
                            directedGraph.addVertex(r + "," + i + "," + j + "," + chars[j]);
                        }
                        if (j > 0 && chars[j - 1] == '.') {
                            if (!directedGraph.containsVertex(r + "," + i + "," + (j - 1) + "," + chars[j - 1])) {
                                directedGraph.addVertex(r + "," + i + "," + (j - 1) + "," + chars[j - 1]);
                            }
                            directedGraph.addEdge(r + "," + i + "," + j + "," + chars[j], r + "," + i + "," + (j - 1) + "," + chars[j - 1]);
                        }
                        if (j < chars.length - 1 && chars[j + 1] == '.') {
                            if (!directedGraph.containsVertex(r + "," + i + "," + (j + 1) + "," + chars[j + 1])) {
                                directedGraph.addVertex(r + "," + i + "," + (j + 1) + "," + chars[j + 1]);
                            }
                            directedGraph.addEdge(r + "," + i + "," + j + "," + chars[j], r + "," + i + "," + (j + 1) + "," + chars[j + 1]);
                        }
                        if (i > 0 && day20.get(i - 1).toCharArray()[j] == '.') {
                            if (!directedGraph.containsVertex(r + "," + (i - 1) + "," + j + "," + day20.get(i - 1).toCharArray()[j])) {
                                directedGraph.addVertex(r + "," + (i - 1) + "," + j + "," + day20.get(i - 1).toCharArray()[j]);
                            }
                            directedGraph.addEdge(r + "," + i + "," + j + "," + chars[j], r + "," + (i - 1) + "," + j + "," + day20.get(i - 1).toCharArray()[j]);
                        }
                        if (i < day20.size() - 1 && day20.get(i + 1).toCharArray()[j] == '.') {
                            if (!directedGraph.containsVertex(r + "," + (i + 1) + "," + j + "," + day20.get(i + 1).toCharArray()[j])) {
                                directedGraph.addVertex(r + "," + (i + 1) + "," + j + "," + day20.get(i + 1).toCharArray()[j]);
                            }
                            directedGraph.addEdge(r + "," + i + "," + j + "," + chars[j], r + "," + (i + 1) + "," + j + "," + day20.get(i + 1).toCharArray()[j]);
                        }
                    }
                }
            }
        }

        return directedGraph;
    }
}