package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Day18 {
    static List<String> knownKeys;
    static HashSet<String> globalKeys = new HashSet<>();

    public static void main(String[] args) throws IOException {

        List<String> day18_1 = FileUtil.readFile("day18_21");
        List<String> day18_2 = FileUtil.readFile("day18_22");
        List<String> day18_3 = FileUtil.readFile("day18_23");
        List<String> day18_4 = FileUtil.readFile("day18_24");

        knownKeys = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
                "k", "l", "m", "n", "o", "p", "r", "s", "t", "u",
                "v", "w", "x", "y", "z", "q"));


        final DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph1 = graph(day18_1, "39,39,@");
        final DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph2 = graph(day18_2, "39,1,@");
        final DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph3 = graph(day18_3, "1,39,@");
        final DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph4 = graph(day18_4, "1,1,@");

        ConnectivityInspector<String, DefaultWeightedEdge> connectivityInspector = new ConnectivityInspector<>(graph1);
        final Set<String> graph1Keys = connectivityInspector.connectedSets().get(0).stream()
                .filter(s -> s.matches(".*[a-z]"))
                .map(s -> s.charAt(s.length() - 1) + "")
                .collect(Collectors.toSet());
        connectivityInspector = new ConnectivityInspector<>(graph2);
        final Set<String> graph2Keys = connectivityInspector.connectedSets().get(0).stream()
                .filter(s -> s.matches(".*[a-z]"))
                .map(s -> s.charAt(s.length() - 1) + "")
                .collect(Collectors.toSet());
        connectivityInspector = new ConnectivityInspector<>(graph3);
        final Set<String> graph3Keys = connectivityInspector.connectedSets().get(0).stream()
                .filter(s -> s.matches(".*[a-z]"))
                .map(s -> s.charAt(s.length() - 1) + "")
                .collect(Collectors.toSet());
        connectivityInspector = new ConnectivityInspector<>(graph4);
        final Set<String> graph4Keys = connectivityInspector.connectedSets().get(0).stream()
                .filter(s -> s.matches(".*[a-z]"))
                .map(s -> s.charAt(s.length() - 1) + "")
                .collect(Collectors.toSet());


        final double loop3 = loop(graph3, new HashSet<>(), "1,39,@", 0.0, 0, graph3Keys);
        final double loop2 = loop(graph2, new HashSet<>(), "39,1,@", 0.0, 0, graph2Keys);
        final double loop4 = loop(graph4, new HashSet<>(), "1,1,@", 0.0, 0, graph4Keys);
        final double loop1 = loop(graph1, new HashSet<>(), "39,39,@", 0.0, 0, graph1Keys);

        System.err.println(loop1 + loop2 + loop3 + loop4);

    }

    static double min = Integer.MAX_VALUE;
    static int count = 0;

    static double loop(DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph, Set<String> inventory, String start, double weight, int depth, Set<String> knownKeys) {
        count++;
        if (count % 1000000 == 0) {
            System.err.println(count);
        }
        if (weight > min) {
            return weight;
        }
        if (inventory.size() == knownKeys.size()) {
            if (weight < min) {
                min = weight;
            }
            System.out.println(weight);
            return weight;
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
                        if (!globalKeys.contains(Character.toLowerCase(c) + "")) {
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
                    return weight;
                }
                final Set<String> newInventory = new HashSet<>(inventory);
                newInventory.add(knownKey);
                if (depth == knownKeys.size()) {
                    globalKeys.addAll(knownKeys);
                    return weight;
                }
                loop(graph, newInventory, vertex, weight, depth + 1, knownKeys);
            }
        }
        if (inventory.size() == knownKeys.size())
        globalKeys.addAll(inventory);
        return weight;
    }

    static Map<String, ShortestPathAlgorithm.SingleSourcePaths<String, DefaultWeightedEdge>> CACHE = new HashMap<>();

    private static GraphPath<String, DefaultWeightedEdge> getPath(DijkstraShortestPath<String, DefaultWeightedEdge> di, String start, String vertex) {
        if (!CACHE.containsKey(start)) {
            final ShortestPathAlgorithm.SingleSourcePaths<String, DefaultWeightedEdge> path = di.getPaths(start);
            CACHE.put(start, path);
        }
        return CACHE.get(start).getPath(vertex);
    }

    private static DefaultDirectedWeightedGraph<String, DefaultWeightedEdge> graph(List<String> day18, String start) {
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
                        directedGraph.addEdge(i + "," + j + "," + chars[j], i + "," + (j - 1) + "," + chars[j - 1]);
                    }
                    if (j < chars.length - 1 && chars[j + 1] != '#') {
                        if (!directedGraph.containsVertex(i + "," + (j + 1) + "," + chars[j + 1])) {
                            directedGraph.addVertex(i + "," + (j + 1) + "," + chars[j + 1]);
                        }
                        directedGraph.addEdge(i + "," + j + "," + chars[j], i + "," + (j + 1) + "," + chars[j + 1]);
                    }
                    if (i > 0 && day18.get(i - 1).toCharArray()[j] != '#') {
                        if (!directedGraph.containsVertex((i - 1) + "," + j + "," + day18.get(i - 1).toCharArray()[j])) {
                            directedGraph.addVertex((i - 1) + "," + j + "," + day18.get(i - 1).toCharArray()[j]);
                        }
                        directedGraph.addEdge(i + "," + j + "," + chars[j], (i - 1) + "," + j + "," + day18.get(i - 1).toCharArray()[j]);
                    }
                    if (i < day18.size() - 1 && day18.get(i + 1).toCharArray()[j] != '#') {
                        if (!directedGraph.containsVertex((i + 1) + "," + j + "," + day18.get(i + 1).toCharArray()[j])) {
                            directedGraph.addVertex((i + 1) + "," + j + "," + day18.get(i + 1).toCharArray()[j]);
                        }
                        directedGraph.addEdge(i + "," + j + "," + chars[j], (i + 1) + "," + j + "," + day18.get(i + 1).toCharArray()[j]);
                    }
                }
            }
        }

        final Set<String> vertices = directedGraph.vertexSet();

        final List<String> keys = knownKeys.stream()
                .map(s -> s.charAt(0))
                .map(s -> findVertex(vertices, s))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        final List<String> doors = knownKeys.stream()
                .map(s -> s.charAt(0))
                .map(Character::toUpperCase)
                .map(s -> findVertex(vertices, s))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        final DijkstraShortestPath<String, DefaultWeightedEdge> di = new DijkstraShortestPath<>(directedGraph);

        doors.addAll(keys);
        doors.add(start);

        for (String door : doors) {
            if (start.equals(door)) continue;
            try {
                final GraphPath<String, DefaultWeightedEdge> path = di.getPath(start, door);
                if (path != null) {
                    final long length = path.getVertexList().stream()
                            .filter(s -> !s.endsWith("."))
                            .count();
                    if (length == 2) {
                        final double weight = path.getWeight();
                        final DefaultWeightedEdge defaultWeightedEdge = directedGraph.addEdge(start, door);
                        directedGraph.setEdgeWeight(defaultWeightedEdge, weight);
                    }
                }
            } catch (Exception e) {}
        }

        for (String s : doors) {
            for (String door : doors) {
                if (s.equals(door)) continue;
                try {
                    final GraphPath<String, DefaultWeightedEdge> path = di.getPath(s, door);
                    if (path != null) {

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
                    }
                } catch (Exception e) {}
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