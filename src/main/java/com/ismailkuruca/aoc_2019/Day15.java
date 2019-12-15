package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Day15 {

    public static void main(String[] args) {
        final List<String> day15 =
                FileUtil.readFile("day15");
        final List<String> day15Values = Arrays.asList(day15.get(0).split(","));

        final List<Long> input = day15Values.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        final IntCodeMachine15 intCodeMachine15 = new IntCodeMachine15(input);

        final char[][] plane = new char[50][50];

        //0 north 1 south 2 west 3 east
        final int[][] direction = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        int x = 25;
        int y = 25;
        intCodeMachine15.input = 1L;
        plane[25][25] = '.' ;
        Set<String> coords = new HashSet<>();
        coords.add("2525");
        boolean loop = true;
        DefaultDirectedGraph<String, DefaultEdge> directedGraph
                = new DefaultDirectedGraph<>(DefaultEdge.class);
        directedGraph.addVertex(x + "" + y);
        while (loop) {
            final int[] values = direction[Math.toIntExact(intCodeMachine15.input - 1)];
            final List<Long> run = intCodeMachine15.run();
            final int output = Math.toIntExact(run.get(0));
            switch (output) {
                case 0: {
                    plane[x + values[0]][y + values[1]] = '#' ;
                    break;
                }
                case 1: {
                    plane[x + values[0]][y + values[1]] = '.' ;
                    x += values[0];
                    y += values[1];
                    if (!directedGraph.containsVertex(x + "" + y)) {
                        directedGraph.addVertex(x + "" + y);
                        coords.add(x + "" + y);
                    }
                    directedGraph.addEdge(x + "" + y, (x - values[0]) + "" + (y - values[1]));
                    break;
                }
                case 2: {
                    plane[x + values[0]][y + values[1]] = 'D' ;
                    directedGraph.addVertex("D");
                    coords.add("D");
                    directedGraph.addEdge((x - values[0]) + "" + (y - values[1]), "D");
                    loop = false;
                    break;
                }
            }

            intCodeMachine15.input = (long) ThreadLocalRandom.current().nextInt(1, 5);
        }
        print(plane);

        part1(directedGraph);
        part2(directedGraph, coords);
    }

    private static void part2(DefaultDirectedGraph<String, DefaultEdge> directedGraph, Set<String> coords) {
        final DijkstraShortestPath<String, DefaultEdge> di = new DijkstraShortestPath<>(directedGraph);

        double maxWeight = 0;
        for (String coord : coords) {
            final double weight = di.getPathWeight(coord, "D");
            if (weight > maxWeight) {
                maxWeight = weight;
            }
        }

        System.out.println(maxWeight + 1);
    }

    private static void part1(DefaultDirectedGraph<String, DefaultEdge> directedGraph) {
        final DijkstraShortestPath<String, DefaultEdge> di = new DijkstraShortestPath<>(directedGraph);
        final double allPaths = di.getPathWeight("2525", "D");

        System.out.println(allPaths + 1);
    }

    private static void print(char[][] plane) {
        for (char[] chars : plane) {
            for (char aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }
    }

}

class IntCodeMachine15 {
    public Long input;
    public List<Long> output = new ArrayList<>();
    Map<Long, Long> program = new HashMap<>();
    long pc = 0;
    long rel = 0;
    boolean finished = false;

    public IntCodeMachine15(List<Long> input) {
        for (int i = 0; i < input.size(); i++) {
            writeToMem(i, input.get(i), 1);
        }
    }

    public List<Long> run() {
        output = new ArrayList<>();
        while (true) {
            final int operation = Math.toIntExact(program.get(pc));
            final int opcode = operation % 100;
            if (opcode == 1) {
                final long firstOperand = readFromMem(1, getNthDigit(operation, 3));
                final long secondOperand = readFromMem(2, getNthDigit(operation, 4));
                writeToMem(pc + 3, firstOperand + secondOperand, getNthDigit(operation, 5));
                pc += 4;
            } else if (opcode == 2) {
                final long firstOperand = readFromMem(1, getNthDigit(operation, 3));
                final long secondOperand = readFromMem(2, getNthDigit(operation, 4));
                writeToMem(pc + 3, firstOperand * secondOperand, getNthDigit(operation, 5));
                pc += 4;
            } else if (opcode == 3) {
                writeToMem(pc + 1, input, getNthDigit(operation, 3));
                pc += 2;
            } else if (opcode == 4) {
                final long firstOperand = readFromMem(1, getNthDigit(operation, 3));
                pc += 2;
                output.add(firstOperand);
                return output;
            } else if (opcode == 5) {
                final long firstOperand = readFromMem(1, getNthDigit(operation, 3));
                final long secondOperand = readFromMem(2, getNthDigit(operation, 4));

                if (!(firstOperand == 0)) {
                    pc = secondOperand;
                } else {
                    pc += 3;
                }
            } else if (opcode == 6) {
                final long firstOperand = readFromMem(1, getNthDigit(operation, 3));
                final long secondOperand = readFromMem(2, getNthDigit(operation, 4));

                if (firstOperand == 0) {
                    pc = secondOperand;
                } else {
                    pc += 3;
                }
            } else if (opcode == 7) {
                final long firstOperand = readFromMem(1, getNthDigit(operation, 3));
                final long secondOperand = readFromMem(2, getNthDigit(operation, 4));

                writeToMem(pc + 3, firstOperand < secondOperand ? 1 : 0, getNthDigit(operation, 5));
                pc += 4;
            } else if (opcode == 8) {
                final long firstOperand = readFromMem(1, getNthDigit(operation, 3));
                final long secondOperand = readFromMem(2, getNthDigit(operation, 4));

                writeToMem(pc + 3, firstOperand == secondOperand ? 1 : 0, getNthDigit(operation, 5));
                pc += 4;
            } else if (opcode == 9) {
                final long firstOperand = readFromMem(1, getNthDigit(operation, 3));

                rel += firstOperand;
                pc += 2;
            } else if (opcode == 99) {
                finished = true;
                return output;
            } else {
                throw new RuntimeException();
            }
        }
    }

    private long readFromMem(int pos, int operandMode) {
        final long operand;
        if (operandMode == 0) {
            operand = readFromMem(readFromMem(pc + pos));
        } else if (operandMode == 1) {
            operand = readFromMem(pc + pos);
        } else {
            operand = readFromMem(readFromMem(pc + pos) + rel);
        }
        return operand;
    }

    public void writeToMem(long target, long value, int operandMode) {
        if (operandMode == 0) {
            program.put(readFromMem(target), value);
        } else if (operandMode == 1) {
            program.put(target, value);
        } else {
            program.put(readFromMem(target) + rel, value);
        }
    }

    private long readFromMem(long target) {
        program.putIfAbsent(target, (long) 0);
        return program.get(target);
    }

    public static int getNthDigit(long number, int n) {
        return (int) ((number / Math.pow(10, n - 1)) % 10);
    }


}