package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Day20 {

    public static void main(String[] args) throws IOException {

        List<String> day20 = FileUtil.readFile("day20");

        final DijkstraShortestPath<String, DefaultWeightedEdge> di = new DijkstraShortestPath<>(graph(day20));
        final GraphPath<String, DefaultWeightedEdge> path = di.getPath("33,102,.", "108,53,.");
        System.out.println(path.getWeight());

    }

    private static DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph(List<String> day20) {
        DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> directedGraph
                = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        for (int i = 0; i < day20.size(); i++) {
            final String s = day20.get(i);
            final char[] chars = s.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == '.') {
                    if (!directedGraph.containsVertex(i + "," + j + "," + chars[j])) {
                        directedGraph.addVertex(i + "," + j + "," + chars[j]);
                    }
                    if (j > 0 && chars[j - 1] == '.') {
                        if (!directedGraph.containsVertex(i + "," + (j - 1) + "," + chars[j - 1])) {
                            directedGraph.addVertex(i + "," + (j - 1) + "," + chars[j - 1]);
                        }
                        directedGraph.addEdge(i + "," + j + "," + chars[j], i + "," + (j - 1) + "," + chars[j - 1]);
                    }
                    if (j < chars.length - 1 && chars[j + 1] == '.') {
                        if (!directedGraph.containsVertex(i + "," + (j + 1) + "," + chars[j + 1])) {
                            directedGraph.addVertex(i + "," + (j + 1) + "," + chars[j + 1]);
                        }
                        directedGraph.addEdge(i + "," + j + "," + chars[j], i + "," + (j + 1) + "," + chars[j + 1]);
                    }
                    if (i > 0 && day20.get(i - 1).toCharArray()[j] == '.') {
                        if (!directedGraph.containsVertex((i - 1) + "," + j + "," + day20.get(i - 1).toCharArray()[j])) {
                            directedGraph.addVertex((i - 1) + "," + j + "," + day20.get(i - 1).toCharArray()[j]);
                        }
                        directedGraph.addEdge(i + "," + j + "," + chars[j], (i - 1) + "," + j + "," + day20.get(i - 1).toCharArray()[j]);
                    }
                    if (i < day20.size() - 1 && day20.get(i + 1).toCharArray()[j] == '.') {
                        if (!directedGraph.containsVertex((i + 1) + "," + j + "," + day20.get(i + 1).toCharArray()[j])) {
                            directedGraph.addVertex((i + 1) + "," + j + "," + day20.get(i + 1).toCharArray()[j]);
                        }
                        directedGraph.addEdge(i + "," + j + "," + chars[j], (i + 1) + "," + j + "," + day20.get(i + 1).toCharArray()[j]);
                    }
                }
            }
        }

        HashMap<String, String> portals = new HashMap<>();
//        portals.put("6,0", "4,7");
//        portals.put("11,0", "8,4");
//        portals.put("13,0", "10,9");


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
//            final String[] split1 = s.split(",");
//            final String[] split2 = s2.split(",");
//            String sx = (Integer.parseInt(split1[0]) + 1) + "," + (Integer.parseInt(split1[1]) + 1);
//            String s2x = (Integer.parseInt(split2[0]) + 1) + "," + (Integer.parseInt(split2[1]) + 1);
            final DefaultWeightedEdge defaultWeightedEdge = directedGraph.addEdge(s + ",.", s2 + ",.");
            final DefaultWeightedEdge defaultWeightedEdge1 = directedGraph.addEdge(s2 + ",.", s + ",.");

        });

//        for (int i = 0; i < day20.size(); i++) {
//            final String s = day20.get(i);
//            final char[] chars = s.toCharArray();
//            for (int j = 0; j < chars.length; j++) {
//                if (Character.isLetter(chars[j])) {
//                    String portal = String.valueOf(chars[j]);
//                    if (j > 0 && Character.isLetter(chars[j - 1])) {
//                        portal += String.valueOf(chars[j - 1]);
//                    }
//                    if (j < chars.length - 1 && Character.isLetter(chars[j + 1])) {
//                        portal += String.valueOf(chars[j + 1]);
//                    }
//                    if (i > 0 && Character.isLetter(day20.get(i - 1).toCharArray()[j])) {
//                        portal += String.valueOf(day20.get(i - 1).toCharArray()[j]);
//                    }
//                    if (i < day20.size() - 1 && Character.isLetter(day20.get(i + 1).toCharArray()[j])) {
//                        portal += String.valueOf(day20.get(i + 1).toCharArray()[j]);
//                    }
//
//                }
//            }
//        }


        return directedGraph;
    }


    static String findVertex(Set<String> vertices, char c) {
        for (String vertex : vertices) {
            if (vertex.endsWith(String.valueOf(c))) {
                return vertex;
            }
        }
        return null;
    }
}