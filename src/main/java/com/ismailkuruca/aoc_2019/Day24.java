package com.ismailkuruca.aoc_2019;

import java.io.IOException;
import java.util.*;

public class Day24 {
    private static Map<Integer, char[][]> dimensions = new HashMap<>();

    public static void main(String[] args) throws IOException {
        part1();
        part2();
    }

    private static void part1() {
        Set<String> layouts = new HashSet<>();
        List<String> input = Arrays.asList(
                "#..#.",
                ".....",
                ".#..#",
                ".....",
                "#.#..");

        layouts.add(String.join("\n", input));

        while (true) {
            final List<String> output = iterate(input);
            final String out = String.join("\n", output);
            if (layouts.contains(out)) {
                System.err.println(out);
                input = output;
                break;
            }
            layouts.add(out);
            System.out.println(out);
            input = output;
        }

        int pow = 0;
        int sum = 0;
        for (int i = 0; i < input.size(); i++) {
            final char[] chars = input.get(i).toCharArray();
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == '#') {
                    sum += Math.pow(2, pow);
                }
                pow++;
            }
        }
        System.out.println(sum);
    }

    private static List<String> iterate(List<String> input) {
        final List<String> output = new ArrayList<>(input);
        for (int j = 0; j < input.size(); j++) {
            final char[] chars = input.get(j).toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char left, right, up, down;
                if (i == 0) {
                    left = '.' ;
                } else {
                    left = chars[i - 1];
                }
                if (i == chars.length - 1) {
                    right = '.' ;
                } else {
                    right = chars[i + 1];
                }
                if (j == 0) {
                    up = '.' ;
                } else {
                    up = input.get(j - 1).toCharArray()[i];
                }
                if (j == input.size() - 1) {
                    down = '.' ;
                } else {
                    down = input.get(j + 1).toCharArray()[i];
                }

                int adjacentBugs = 0;
                adjacentBugs += up == '#' ? 1 : 0;
                adjacentBugs += down == '#' ? 1 : 0;
                adjacentBugs += left == '#' ? 1 : 0;
                adjacentBugs += right == '#' ? 1 : 0;


                final char[] outputLine = output.get(j).toCharArray();
                if (chars[i] == '#' && adjacentBugs != 1) {
                    outputLine[i] = '.' ;
                    output.set(j, new String(outputLine));
                }
                if (chars[i] == '.' && (adjacentBugs == 1 || adjacentBugs == 2)) {
                    outputLine[i] = '#' ;
                    output.set(j, new String(outputLine));
                }
            }
        }
        return output;
    }

    public static void part2() {
        char[][] grid = new char[][]{
                {'#', '.', '.', '#', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '#', '.', '.', '#'},
                {'.', '.', '.', '.', '.'},
                {'#', '.', '#', '.', '.'}
        };
        for (int i = -200; i <= 200; i++) {
            final char[][] arr = new char[grid.length][grid[0].length];
            for (int j = 0; j < arr.length; j++) {
                Arrays.fill(arr[j], '.');
            }
            dimensions.put(i, arr);
        }
        dimensions.put(0, grid);
        for (int i = 0; i < 200; i++) {
            final Map<Integer, char[][]> dimensions = new HashMap<>();
            for (int dimension = -200; dimension <= 200; dimension++) {
                char[][] nextGen = new char[grid.length][];
                for (int g = 0; g < grid.length; g++) {
                    nextGen[g] = Arrays.copyOf(Day24.dimensions.get(dimension)[g], grid[g].length);
                }

                for (int a = 0; a < 5; a++) {
                    for (int b = 0; b < 5; b++) {
                        if (a != 2 || b != 2) {
                            iterate(dimension, nextGen, b, a);
                        }
                    }
                }
                dimensions.put(dimension, nextGen);
            }
            Day24.dimensions = dimensions;
        }
        System.out.println(dimensions.values().stream().mapToInt(Day24::countBugs).sum());
    }

    private static int countBugs(char[][] grid) {
        int sum = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                sum += bug(grid, i, j);
            }
        }
        return sum;
    }

    private static void iterate(int dimension, char[][] nextGen, int x, int y) {
        int neighboringBugs = neighbors(dimension, x, y);
        if (dimensions.get(dimension)[y][x] == '#' && neighboringBugs != 1) {
            nextGen[y][x] = '.' ;
        } else if (dimensions.get(dimension)[y][x] == '.' && (neighboringBugs == 1 || neighboringBugs == 2)) {
            nextGen[y][x] = '#' ;
        }
    }

    private static int neighbors(int dimension, int x, int y) {
        int result = 0;
        Point[] neighbors = new Point[]{new Point(2, 1), new Point(1, 2), new Point(2, 3), new Point(3, 2)};
        if (dimensions.containsKey(dimension + 1)) {
            for (int i = 0; i < 4; i++) {
                if (i == 0 && y == 0 || i == 1 && x == 0 || i == 2 && y == 4 || i == 3 && x == 4) {
                    result += bug(dimensions.get(dimension + 1), neighbors[i].x, neighbors[i].y);
                }
            }
        }
        if (dimensions.containsKey(dimension - 1)) {
            for (int i = 0; i < 4; i++) {
                if (neighbors[i].x == x && neighbors[i].y == y) {
                    for (int j = 0; j < 5; j++) {
                        Point p = null;
                        if (i == 0) p = new Point(j, 0);
                        if (i == 1) p = new Point(0, j);
                        if (i == 2) p = new Point(j, 4);
                        if (i == 3) p = new Point(4, j);
                        result += bug(dimensions.get(dimension - 1), p.x, p.y);
                    }
                }
            }
        }
        return result + neighbors(dimensions.get(dimension), x, y);
    }

    public static int neighbors(char[][] grid, int x, int y) {
        return bug(grid, x, y - 1) +
                bug(grid, x, y + 1) +
                bug(grid, x - 1, y) +
                bug(grid, x + 1, y);
    }

    private static int bug(char[][] grid, int x, int y) {
        if (x < 0 || y < 0 || x >= grid.length || y >= grid.length) return 0;
        return grid[y][x] == '#' ? 1 : 0;
    }

    public static char[][] copy(char[][] grid) {
        char[][] g2 = new char[grid.length][];
        for (int i = 0; i < grid.length; i++) g2[i] = Arrays.copyOf(grid[i], grid[i].length);
        return g2;
    }
}
