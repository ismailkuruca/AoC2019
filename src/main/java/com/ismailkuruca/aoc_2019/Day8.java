package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day8 {

    public static void main(String[] args) {
        final List<String> day8 = FileUtil.readFile("day8");

        int width = 25;
        int height = 6;

        final String input = day8.get(0);

        final ArrayList<String> layers = new ArrayList<>();
        for (int i = 0; i < input.length(); i += width * height) {
            final char[] string = Arrays.copyOfRange(input.toCharArray(), i, Math.min(input.length(), i + width * height));
            layers.add(new String(string));
        }

        part1(layers);
        part2(layers);
    }

    private static void part2(ArrayList<String> layers) {
        final int[] merged = new int[150];
        Arrays.fill(merged, 50);
        for (int i = 0; i < layers.size(); i++) {
            final char[] layer = layers.get(i).toCharArray();
            for (int j = 0; j < layer.length; j++) {
                if (merged[j] == 50) {
                    merged[j] = layer[j];
                }
            }
        }

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 25; j++) {
                final int val = merged[i * 25 + j];
                System.out.print(val == 49 ? "#" : " ");
            }
            System.out.println();
        }

    }

    private static void part1(ArrayList<String> layers) {
        int min = 100000;
        int ind = 0;
        for (int i = 0; i < layers.size(); i++) {
            final int zero = find(layers.get(i), 0);
            if (zero < min) {
                min = zero;
                ind = i;
            }
        }

        final String target = layers.get(ind);
        System.out.println(find(target, 1) * find(target, 2));
    }

    private static int find(String layer, int tar) {
        int count = 0;
        tar += 48;
        final char[] chars = layer.toCharArray();
        for (char aChar : chars) {
            if (aChar == tar) {
                count++;
            }
        }
        return count;
    }


}
