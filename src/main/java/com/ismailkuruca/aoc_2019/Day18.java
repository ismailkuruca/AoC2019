package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day18 {
    static List<String> knownKeys;

    static List<String> day18;

    public static void main(String[] args) throws IOException {

        day18 = FileUtil.readFile("day18");
        knownKeys = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "r", "s", "t", "u",
                "v", "w", "x", "y", "z", "q"));


        loop(graph(day18), new HashSet<>(), "40,40,@", 0.0, 0);

    }

    static double min = Integer.MAX_VALUE;
    static int count = 0;

    static void loop(DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph, Set<String> inventory, String start, double weight, int depth) {
        count++;
        if (count % 1000000 == 0) {
            System.err.println(count);
        }
        if (weight > min) {
            return;
        }
        if (inventory.size() == 26) {
            if (weight < min) {
                min = weight;
            }
            System.out.println(weight);
            return;
        }
        final Set<String> vertices = graph.vertexSet();
        DijkstraShortestPath<String, DefaultWeightedEdge> di = new DijkstraShortestPath<>(graph);
        for (String knownKey : knownKeys) {
            if (inventory.contains(knownKey)) {
                continue;
            }
            final String vertex = findVertex(vertices, knownKey.charAt(0));
            final GraphPath<String, DefaultWeightedEdge> path = getPath(di, start, vertex);
            if (path != null) {
                boolean broken = false;
                List<String> tempKeys = new ArrayList<>();
                for (String ver : path.getVertexList()) {
                    final char c = ver.charAt(ver.length() - 1);
                    if (Character.isUpperCase(c)) {
                        if (!inventory.contains(Character.toLowerCase(c) + "")) {
                            broken = true;
                            break;
                        }
                    } else if (Character.isLowerCase(c)) {
                        inventory.add(c + "");
                        tempKeys.add(c + "");
                    }
                }
                if (broken) {
                    tempKeys.forEach(inventory::remove);
                    continue;
                }
                weight += path.getWeight();
                if (weight > min) {
                    return;
                }
                final Set<String> newInventory = new HashSet<>(inventory);
                newInventory.add(knownKey);
//                System.out.println(depth + " " + String.join("", newInventory) + " now: " + start + " next: " + vertex + " " + weight);
                if (depth == 26 || newInventory.size() == inventory.size()) return;
                loop(graph, newInventory, vertex, weight, depth + 1);
            }
        }
        if (inventory.size() == 26)
            System.out.println(depth + " " + inventory + " " + weight);
    }

    static Map<String, ShortestPathAlgorithm.SingleSourcePaths<String, DefaultWeightedEdge>> CACHE = new HashMap<>();

    private static GraphPath<String, DefaultWeightedEdge> getPath(DijkstraShortestPath<String, DefaultWeightedEdge> di, String start, String vertex) {
        if (!CACHE.containsKey(start)) {
            final ShortestPathAlgorithm.SingleSourcePaths<String, DefaultWeightedEdge> path = di.getPaths(start);
            CACHE.put(start, path);
        }
        return CACHE.get(start).getPath(vertex);
    }

    private static DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph(List<String> day18) {
        DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> directedGraph
                = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

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
                        final DefaultWeightedEdge defaultWeightedEdge = directedGraph.addEdge(i + "," + j + "," + chars[j], i + "," + (j - 1) + "," + chars[j - 1]);
//                        final DefaultWeightedEdge defaultWeightedEdge1 = directedGraph.addEdge(i + "," + (j - 1) + "," + chars[j - 1], i + "," + j + "," + chars[j]);
//                        directedGraph.setEdgeWeight(defaultWeightedEdge, 1);
//                        directedGraph.setEdgeWeight(defaultWeightedEdge1, 1);
                    }
                    if (j < chars.length - 1 && chars[j + 1] != '#') {
                        if (!directedGraph.containsVertex(i + "," + (j + 1) + "," + chars[j + 1])) {
                            directedGraph.addVertex(i + "," + (j + 1) + "," + chars[j + 1]);
                        }
                        final DefaultWeightedEdge defaultWeightedEdge = directedGraph.addEdge(i + "," + j + "," + chars[j], i + "," + (j + 1) + "," + chars[j + 1]);
//                        final DefaultWeightedEdge defaultWeightedEdge1 = directedGraph.addEdge(i + "," + (j + 1) + "," + chars[j + 1], i + "," + j + "," + chars[j]);
//                        directedGraph.setEdgeWeight(defaultWeightedEdge, 1);
//                        directedGraph.setEdgeWeight(defaultWeightedEdge1, 1);
                    }
                    if (i > 0 && day18.get(i - 1).toCharArray()[j] != '#') {
                        if (!directedGraph.containsVertex((i - 1) + "," + j + "," + day18.get(i - 1).toCharArray()[j])) {
                            directedGraph.addVertex((i - 1) + "," + j + "," + day18.get(i - 1).toCharArray()[j]);
                        }
                        final DefaultWeightedEdge defaultWeightedEdge = directedGraph.addEdge(i + "," + j + "," + chars[j], (i - 1) + "," + j + "," + day18.get(i - 1).toCharArray()[j]);
//                        final DefaultWeightedEdge defaultWeightedEdge1 = directedGraph.addEdge((i - 1) + "," + j + "," + day18.get(i - 1).toCharArray()[j], i + "," + j + "," + chars[j]);
//                        directedGraph.setEdgeWeight(defaultWeightedEdge, 1);
//                        directedGraph.setEdgeWeight(defaultWeightedEdge1, 1);
                    }
                    if (i < day18.size() - 1 && day18.get(i + 1).toCharArray()[j] != '#') {
                        if (!directedGraph.containsVertex((i + 1) + "," + j + "," + day18.get(i + 1).toCharArray()[j])) {
                            directedGraph.addVertex((i + 1) + "," + j + "," + day18.get(i + 1).toCharArray()[j]);
                        }
                        final DefaultWeightedEdge defaultWeightedEdge = directedGraph.addEdge(i + "," + j + "," + chars[j], (i + 1) + "," + j + "," + day18.get(i + 1).toCharArray()[j]);
//                        final DefaultWeightedEdge defaultWeightedEdge1 = directedGraph.addEdge((i + 1) + "," + j + "," + day18.get(i + 1).toCharArray()[j], i + "," + j + "," + chars[j]);
//                        directedGraph.setEdgeWeight(defaultWeightedEdge, 1);
//                        directedGraph.setEdgeWeight(defaultWeightedEdge1, 1);
                    }
                }
            }
        }

        final Set<String> vertices = directedGraph.vertexSet();

        final List<String> keys = knownKeys.stream()
                .map(s -> s.charAt(0))
                .map(s -> findVertex(vertices, s))
                .collect(Collectors.toList());

        final List<String> doors = knownKeys.stream()
                .map(s -> s.charAt(0))
                .map(Character::toUpperCase)
                .map(s -> findVertex(vertices, s))
                .collect(Collectors.toList());

        String start = "40,40,@";

        final DijkstraShortestPath<String, DefaultWeightedEdge> di = new DijkstraShortestPath<>(directedGraph);

        doors.addAll(keys);
        doors.add(start);

        for (String door : doors) {
            if (start.equals(door)) continue;
            final GraphPath<String, DefaultWeightedEdge> path = di.getPath(start, door);
            final long length = path.getVertexList().stream()
                    .filter(s -> !s.endsWith("."))
                    .count();
            if (length == 2) {
                final double weight = path.getWeight();
                final DefaultWeightedEdge defaultWeightedEdge = directedGraph.addEdge(start, door);
                directedGraph.setEdgeWeight(defaultWeightedEdge, weight);
            }
        }

        for (String s : doors) {
            for (String door : doors) {
                if (s.equals(door)) continue;
                final GraphPath<String, DefaultWeightedEdge> path = di.getPath(s, door);
                final long length = path.getVertexList().stream()
                        .filter(st -> !st.endsWith("."))
                        .count();
                if (length == 2) {
                    final double weight = path.getWeight();
                    final DefaultWeightedEdge defaultWeightedEdge = directedGraph.addEdge(s, door);
                    if (defaultWeightedEdge != null) {
                        directedGraph.setEdgeWeight(defaultWeightedEdge, weight);
                    }
                }
//                final DefaultWeightedEdge defaultWeightedEdge1 = directedGraph.addEdge(s, door);
//                directedGraph.setEdgeWeight(defaultWeightedEdge1, di.getPath(s, door).getWeight());
            }
        }

        directedGraph.vertexSet().stream()
                .filter(s -> s.endsWith("."))
                .collect(Collectors.toList())
                .forEach(s -> {
                    final Set<DefaultWeightedEdge> defaultWeightedEdges = directedGraph.edgesOf(s);
                    directedGraph.removeAllEdges(defaultWeightedEdges);
                    directedGraph.removeVertex(s);
                });

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