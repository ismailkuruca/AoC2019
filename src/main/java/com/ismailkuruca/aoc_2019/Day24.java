package com.ismailkuruca.aoc_2019;

import java.util.*;

public class Day24 {

    public static void main(String[] args) {
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
}
