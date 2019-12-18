package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day18 {
    static DijkstraShortestPath<String, DefaultEdge> di;

    public static void main(String[] args) {
        final List<String> day18 = FileUtil.readFile("day18");

        DefaultDirectedGraph<String, DefaultEdge> directedGraph
                = new DefaultDirectedGraph<>(DefaultEdge.class);

        for (int i = 0; i < day18.size(); i++) {
            final String s = day18.get(i);
            final char[] chars = s.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] != '#') {
                    if (!directedGraph.containsVertex(i + "" + j + "" + chars[j])) {
                        directedGraph.addVertex(i + "" + j + "" + chars[j]);
                    }
                    if (j > 0 && chars[j - 1] != '#') {
                        if (!directedGraph.containsVertex(i + "" + (j - 1) + "" + chars[j - 1])) {
                            directedGraph.addVertex(i + "" + (j - 1) + "" + chars[j - 1]);
                        }
                        directedGraph.addEdge(i + "" + j + "" + chars[j], i + "" + (j - 1) + "" + chars[j - 1]);
                    }
                    if (j < chars.length - 1 && chars[j + 1] != '#') {
                        if (!directedGraph.containsVertex(i + "" + (j + 1) + "" + chars[j + 1])) {
                            directedGraph.addVertex(i + "" + (j + 1) + "" + chars[j + 1]);
                        }
                        directedGraph.addEdge(i + "" + j + "" + chars[j], i + "" + (j + 1) + "" + chars[j + 1]);
                    }
                    if (i > 0 && day18.get(i - 1).toCharArray()[j] != '#') {
                        if (!directedGraph.containsVertex((i - 1) + "" + j + "" + day18.get(i - 1).toCharArray()[j])) {
                            directedGraph.addVertex((i - 1) + "" + j + "" + day18.get(i - 1).toCharArray()[j]);
                        }
                        directedGraph.addEdge(i + "" + j + "" + chars[j], (i - 1) + "" + j + "" + day18.get(i - 1).toCharArray()[j]);
                    }
                    if (i < day18.size() - 1 && day18.get(i + 1).toCharArray()[j] != '#') {
                        if (!directedGraph.containsVertex((i + 1) + "" + j + "" + day18.get(i + 1).toCharArray()[j])) {
                            directedGraph.addVertex((i + 1) + "" + j + "" + day18.get(i + 1).toCharArray()[j]);
                        }
                        directedGraph.addEdge(i + "" + j + "" + chars[j], (i + 1) + "" + j + "" + day18.get(i + 1).toCharArray()[j]);
                    }

                }
            }
        }

        final Set<String> vertices = directedGraph.vertexSet();
        di = new DijkstraShortestPath<>(directedGraph);

        final List<String> gates = getGates(new ArrayList<>(vertices));
        gates.forEach(System.out::println);
        System.out.println("====");
        Set<String> keys = new HashSet<>();
//        go(keys, vertices, "36@", 'F');
        String start = "36@";
        String target = findVertex(vertices, 'F');
        while (true) {

            final GraphPath<String, DefaultEdge> path = di.getPath(start, target);
            final List<String> vertexList = path.getVertexList();
            boolean lockedDoor = false;
            char nextDoor = 0;
            for (int i = 0; i < vertexList.size() && !lockedDoor; i++) {
                final String v = vertexList.get(i);
                if (v.matches(".*[a-z]")) {
                    start = v;
                    keys.add(String.valueOf(Character.toUpperCase(v.charAt(v.length() - 1))));
                } else if (v.matches(".*[A-Z]")) {
                    nextDoor = v.charAt(v.length() - 1);
                    if (!keys.contains(String.valueOf(nextDoor))) {
                        lockedDoor = true;
                    }
                } else {
                    start = v;
                }
            }
            if (nextDoor == 0) {
                target++;
            }
        }

//        int weight = 0;
//        Set<Character> keys = new HashSet<>();
//        String currentStart = "36@";
//        for (char i = 'A' ; i < 'G' ; i++) {
//            if (!keys.contains(Character.toLowerCase(i))) {
//                final String vertex = findVertex(vertices, Character.toLowerCase(i));
//                final double v = di.getPath(currentStart, vertex).getWeight();
//                weight += v;
//                keys.add(Character.toLowerCase(i));
//                currentStart = vertex;
//                System.out.println(weight);
//                System.out.println(currentStart);
//            }
//            final String vertex = findVertex(vertices, i);
//            if (vertex == null) {
//                continue;
//            }
//            final double v = di.getPath(currentStart, vertex).getWeight();
//            weight += v;
//            currentStart = vertex;
//            System.out.println(weight);
//            System.out.println(currentStart);
//        }
//
//        System.out.println(weight);


    }

    static String go(Set<String> keys, Set<String> vertices, String current, char t) {
        final String target = findVertex(vertices, t);
        System.out.println("current: " + current + " target: " + target);
        final GraphPath<String, DefaultEdge> path = di.getPath(current, target);
        final List<String> gates = getGates(path.getVertexList());
        gates.removeAll(keys);
        if (gates.isEmpty()) {
            current = findVertex(vertices, t);
        } else {
            gates.sort((o1, o2) -> o1.charAt(0) < o2.charAt(0) ? -1 : 1);
            final char next = gates.get(0).charAt(0);
            System.out.println(next);
            current = go(keys, vertices, current, Character.toLowerCase(next));
            keys.add(String.valueOf(Character.toUpperCase(t)));
        }
        return current;
    }

    static String findVertex(Set<String> vertices, char c) {
        for (String vertex : vertices) {
            if (vertex.endsWith(String.valueOf(c))) {
                return vertex;
            }
        }
        return null;
    }

    static List<String> getGates(List<String> edgeList) {
        final ArrayList<String> gates = new ArrayList<>();
        edgeList.forEach(s -> {
            if (s.matches(".*[A-Z]")) {
                gates.add(String.valueOf(s.charAt(s.length() - 1)));
            }
        });
        return gates;
    }
}
