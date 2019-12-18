package com.ismailkuruca.aoc_2019;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;
import java.util.stream.Collectors;

public class Day18 {
    static DijkstraShortestPath<String, DefaultEdge> di;

    public static void main(String[] args) {
        String input = "########################\n" +
                "#...............b.C.D.f#\n" +
                "#.######################\n" +
                "#.....@.a.B.c.d.A.e.F.g#\n" +
                "########################";
        List<String> day18 = Arrays.asList(input.split("\\n"));

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
        final List<String> knownKeys = getKeys(new ArrayList<>(vertices));

        gates.forEach(System.out::println);
        System.out.println("====");
        Set<String> keys = new HashSet<>();
//        go(keys, vertices, "36@", 'F');
        String start = "48@";
        char target = 'p';
        int weight = 0;
        while (true) {
            System.out.println("traveling to : " + findVertex(vertices, target) + " from: " + start);
            final GraphPath<String, DefaultEdge> path = di.getPath(start, findVertex(vertices, target));
            final List<String> vertexList = path.getVertexList();
            boolean lockedDoor = false;
            char nextDoor;
            for (int i = 0; i < vertexList.size(); i++) {
                final String v = vertexList.get(i);
                if (v.matches(".*[A-Z]")) {
                    nextDoor = v.charAt(v.length() - 1);
                    if (!keys.contains(String.valueOf(Character.toUpperCase(nextDoor)))) {
                        lockedDoor = true;
                        target = Character.toLowerCase(nextDoor);
                        System.out.println("door is locked: " + nextDoor + " going for key: " + target);
                        break;
                    }
                }
            }

            if (!lockedDoor) {
                start = findVertex(vertices, target);
                target = findLowHangingFruit(knownKeys, vertices, keys, start).charAt(0);
                weight += path.getWeight() - 1;
                System.out.println("everyhing was unlocked: " + path.getWeight());
                final List<String> collectedKeysOnTheWay = path.getVertexList().stream()
                        .filter(s -> s.matches(".*[a-z]"))
                        .map(s -> String.valueOf(s.charAt(s.length() - 1)))
                        .map(s -> Character.toUpperCase(s.charAt(0)) + "")
                        .collect(Collectors.toList());
                collectedKeysOnTheWay.forEach(s -> System.out.println("collected key: " + s));
                keys.addAll(collectedKeysOnTheWay);
                knownKeys.removeAll(collectedKeysOnTheWay);
                if (knownKeys.isEmpty()) {
                    break;
                }
                System.out.println("going next door: " + target);
            } else {
                final GraphPath<String, DefaultEdge> move = di.getPath(start, findVertex(vertices, target));
                final List<String> collectedKeysOnTheWay = move.getVertexList().stream()
                        .filter(s -> s.matches(".*[a-z]"))
                        .map(s -> String.valueOf(s.charAt(s.length() - 1)))
                        .map(s -> Character.toUpperCase(s.charAt(0)) + "")
                        .collect(Collectors.toList());
                collectedKeysOnTheWay.forEach(s -> System.out.println("collected key: " + s));
                keys.addAll(collectedKeysOnTheWay);
                knownKeys.removeAll(collectedKeysOnTheWay);
                weight += move.getWeight() - 1;
                final Character nextObjective = findLowHangingFruit(knownKeys, vertices, keys, start).charAt(0);
                System.out.println("traveled to: " + findVertex(vertices, target) + " weight: " + move.getWeight() + " next target: " + nextObjective);
                start = findVertex(vertices, target);
                if (nextObjective == null) {
                    break;
                } else {
                    target = nextObjective;
                }

            }
            System.out.println(weight);
        }
    }

    static Character getNextObjective(List<String> vertices, char current) {
        return vertices.stream()
                .map(s -> s.charAt(s.length() - 1))
                .distinct()
                .filter(character -> character > Character.toLowerCase(current))
                .min((o1, o2) -> o1 < o2 ? -1 : 1)
                .orElse(null);
    }

    static String findLowHangingFruit(List<String> remainingKeys, Set<String> vertices, Set<String> keys, String pos) {
        String next = "";
        double weight = Integer.MAX_VALUE;
        for (String remainingKey : remainingKeys) {
            try {
                final GraphPath<String, DefaultEdge> graph = di.getPath(pos, findVertex(vertices, remainingKey.charAt(0)));
                if (graph.getWeight() < weight) {
                    final List<String> keysToTheDoorsInTheWay = graph.getVertexList().stream()
                            .filter(s -> s.matches(".*[A-Z]"))
                            .map(s -> s.charAt(s.length() - 1))
                            .map(String::valueOf)
                            .map(String::toLowerCase)
                            .collect(Collectors.toList());
                    if (keys.containsAll(keysToTheDoorsInTheWay)) {
                        weight = graph.getWeight();
                        next = remainingKey;
                    }
                }
            } catch (Exception e) {

            }

        }
        if (next.equals("")) return "p";
        return next;
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

    static List<String> getKeys(List<String> edgeList) {
        final ArrayList<String> gates = new ArrayList<>();
        edgeList.forEach(s -> {
            if (s.matches(".*[a-z]")) {
                gates.add(String.valueOf(s.charAt(s.length() - 1)));
            }
        });
        return gates;
    }
}
