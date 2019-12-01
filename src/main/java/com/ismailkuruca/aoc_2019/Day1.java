package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.List;
import java.util.stream.Collectors;

public class Day1 {

    public static void main(String[] args) {
        final List<String> day1 = FileUtil.readFile("day1");

        List<Integer> input = day1.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        part1(input);
        part2(input);
    }

    private static void part1(List<Integer> input) {
        final int sum = reducer(input)
                .stream()
                .mapToInt(value -> value).sum();
        System.out.println(sum);
    }

    private static void part2(List<Integer> input) {
        int total = 0;
        while(true) {
            input = reducer(input);
            final int sum = input
                    .stream()
                    .mapToInt(value -> value).sum();
            if (sum == 0) {
                break;
            }
            total += sum;
        }
        System.out.println(total);
    }

    private static List<Integer> reducer(List<Integer> original) {
        return original.stream()
                .map(integer -> integer / 3)
                .map(integer -> integer - 2)
                .map(integer -> integer < 0 ? 0 : integer)
                .collect(Collectors.toList());
    }

}
