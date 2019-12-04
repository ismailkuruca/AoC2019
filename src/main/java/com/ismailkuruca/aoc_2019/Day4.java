package com.ismailkuruca.aoc_2019;

public class Day4 {

    public static void main(String[] args) {
        int part1 = 0;
        int part2 = 0;
        for (int i = 271973; i <= 785961; i++) {
            final char[] chars = String.valueOf(i).toCharArray();
            boolean hasSame = false;
            boolean hasSameWithGroups = false;
            boolean descending = false;

            for (int j = 1; j < chars.length; j++) {
                if (chars[j] == chars[j - 1]) {
                    hasSame = true;

                    try {
                        if (chars[j + 1] != chars[j] && chars[j - 2] != chars[j]) {
                            hasSameWithGroups = true;
                        }
                    } catch (IndexOutOfBoundsException ignored) {
                    }

                }
                if (chars[j] < chars[j - 1]) {
                    descending = true;
                }

            }

            if (hasSame && !descending) {
                part1++;
                if (hasSameWithGroups) {
                    part2++;
                }
            }
        }

        System.err.println(part1);
        System.err.println(part2);
    }

}
