package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day18_2 {
    static List<String> knownKeys;

    static List<String> day18;

    static Map<String, DefaultDirectedGraph<String, DefaultEdge>> CACHE = new HashMap<>();
    static Map<String, Map<String, GraphPath<String, DefaultEdge>>> WEIGHTS = new HashMap<>();

    public static void main(String[] args) throws IOException {

        day18 = FileUtil.readFile("day18");
        knownKeys = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "r", "s", "t", "u",
                "v", "w", "x", "y", "z", "q"));


        loop(graph(day18, new HashSet<>()), new HashSet<>(), "40,40,@", 0.0, 0);

    }

    static double min = Integer.MAX_VALUE;
    static int count = 0;

    static double loop(DefaultDirectedGraph<String, DefaultEdge> graph, Set<String> inventory, String start, double weight, int depth) {
        count++;
        if (count % 1000000 == 0) {
            System.err.println(count);
        }
        if (weight > min) {
            return Integer.MAX_VALUE;
        }
        if (inventory.size() == 26) {
            if (weight < min) {
                min = weight;
            }
            System.out.println(weight);
            return weight;
        }
        final Set<String> vertices = graph.vertexSet();
        DijkstraShortestPath<String, DefaultEdge> di = new DijkstraShortestPath<>(graph);
        for (String knownKey : knownKeys) {
            final String vertex = findVertex(vertices, knownKey.charAt(0));
            final GraphPath<String, DefaultEdge> path = getPath(di, inventory, start, vertex);
            if (path != null && !inventory.contains(knownKey)) {
                if (weight + path.getWeight() > min) {
                    return weight + path.getWeight();
                }
                final Set<String> newInventory = new HashSet<>(inventory);
                newInventory.add(knownKey);
//                System.out.println(depth + " " + String.join("", newInventory) + " now: " + start + " next: " + vertex + " " + weight);
                final DefaultDirectedGraph<String, DefaultEdge> newGraph = graph(day18, newInventory);
                loop(newGraph, newInventory, vertex, weight + path.getWeight(), depth + 1);
            }
        }
        return weight;
    }

    private static GraphPath<String, DefaultEdge> getPath(DijkstraShortestPath<String, DefaultEdge> di, Set<String> keys, String start, String end) {
        final String graphKey = new ArrayList<>(keys)
                .stream()
                .sorted((o1, o2) -> o1.charAt(0) > o2.charAt(0) ? -1 : 1)
                .collect(Collectors.joining(""));
        final String pathKey = start + "," + end;
        final String pathKeyInverted = end + "," + start;
        GraphPath<String, DefaultEdge> graphPath = WEIGHTS.get(graphKey).get(pathKey);
        if (graphPath == null) {
            graphPath = WEIGHTS.get(graphKey).get(pathKeyInverted);
            if (graphPath == null) {
                graphPath = di.getPath(start, end);
                WEIGHTS.get(graphKey).put(pathKey, graphPath);
            }
        }
        return graphPath;
    }

    private static DefaultDirectedGraph<String, DefaultEdge> graph(List<String> day18, Set<String> keys) {
        final String key = new ArrayList<>(keys)
                .stream()
                .sorted((o1, o2) -> o1.charAt(0) > o2.charAt(0) ? -1 : 1)
                .collect(Collectors.joining(""));
        if (CACHE.containsKey(key)) {
            return CACHE.get(key);
        }
        DefaultDirectedGraph<String, DefaultEdge> directedGraph
                = new DefaultDirectedGraph<>(DefaultEdge.class);

        for (int i = 0; i < day18.size(); i++) {
            final String s = day18.get(i);
            final char[] chars = s.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] != '#') {
                    if (!directedGraph.containsVertex(i + "," + j + "," + chars[j])) {
                        directedGraph.addVertex(i + "," + j + "," + chars[j]);
                    }
                    if (j > 0 && chars[j - 1] != '#') {
                        if (!directedGraph.containsVertex(i + "," + (j - 1) + "," + chars[j - 1])) {
                            directedGraph.addVertex(i + "," + (j - 1) + "," + chars[j - 1]);
                        }
                        if (Character.isUpperCase(chars[j - 1])) {
                            if (keys.contains(String.valueOf(Character.toLowerCase(chars[j - 1])))) {
                                directedGraph.addEdge(i + "," + j + "," + chars[j], i + "," + (j - 1) + "," + chars[j - 1]);
                            }
                        } else {
                            directedGraph.addEdge(i + "," + j + "," + chars[j], i + "," + (j - 1) + "," + chars[j - 1]);
                        }
                    }
                    if (j < chars.length - 1 && chars[j + 1] != '#') {
                        if (!directedGraph.containsVertex(i + "," + (j + 1) + "," + chars[j + 1])) {
                            directedGraph.addVertex(i + "," + (j + 1) + "," + chars[j + 1]);
                        }
                        if (Character.isUpperCase(chars[j + 1])) {
                            if (keys.contains(String.valueOf(Character.toLowerCase(chars[j + 1])))) {
                                directedGraph.addEdge(i + "," + j + "," + chars[j], i + "," + (j + 1) + "," + chars[j + 1]);
                            }
                        } else {
                            directedGraph.addEdge(i + "," + j + "," + chars[j], i + "," + (j + 1) + "," + chars[j + 1]);
                        }
                    }
                    if (i > 0 && day18.get(i - 1).toCharArray()[j] != '#') {
                        if (!directedGraph.containsVertex((i - 1) + "," + j + "," + day18.get(i - 1).toCharArray()[j])) {
                            directedGraph.addVertex((i - 1) + "," + j + "," + day18.get(i - 1).toCharArray()[j]);
                        }
                        if (Character.isUpperCase(day18.get(i - 1).toCharArray()[j])) {
                            if (keys.contains(String.valueOf(Character.toLowerCase(day18.get(i - 1).toCharArray()[j])))) {
                                directedGraph.addEdge(i + "," + j + "," + chars[j], (i - 1) + "," + j + "," + day18.get(i - 1).toCharArray()[j]);
                            }
                        } else {
                            directedGraph.addEdge(i + "," + j + "," + chars[j], (i - 1) + "," + j + "," + day18.get(i - 1).toCharArray()[j]);
                        }
                    }
                    if (i < day18.size() - 1 && day18.get(i + 1).toCharArray()[j] != '#') {
                        if (!directedGraph.containsVertex((i + 1) + "," + j + "," + day18.get(i + 1).toCharArray()[j])) {
                            directedGraph.addVertex((i + 1) + "," + j + "," + day18.get(i + 1).toCharArray()[j]);
                        }
                        if (Character.isUpperCase(day18.get(i + 1).toCharArray()[j])) {
                            if (keys.contains(String.valueOf(Character.toLowerCase(day18.get(i + 1).toCharArray()[j])))) {
                                directedGraph.addEdge(i + "," + j + "," + chars[j], (i + 1) + "," + j + "," + day18.get(i + 1).toCharArray()[j]);
                            }
                        } else {
                            directedGraph.addEdge(i + "," + j + "," + chars[j], (i + 1) + "," + j + "," + day18.get(i + 1).toCharArray()[j]);
                        }
                    }
                }
            }
        }
        CACHE.put(key, directedGraph);
        WEIGHTS.put(key, new HashMap<>());
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