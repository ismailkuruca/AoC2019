package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 {

    public static void main(String[] args) {
        final List<String> day1 = FileUtil.readFile("day2");
        final List<String> day1Values = Arrays.asList(day1.get(0).split(","));

        final List<Integer> input = day1Values.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());


        final int part1Result = part1(input, 12, 2);
        System.err.println(part1Result);
        final int part2Result = part2(day1Values);
        System.err.println(part2Result);
    }

    private static int part1(List<Integer> input, int pos1, int pos2) {
        int pc = 0;

        input.set(1, pos1);
        input.set(2, pos2);

        while (true) {
            final Integer opcode = input.get(pc);

            if (opcode == 1) {
                final Integer operand1 = input.get(input.get(pc + 1));
                final Integer operand2 = input.get(input.get(pc + 2));
                input.set(input.get(pc + 3), operand1 + operand2);
            } else if (opcode == 2) {
                final Integer operand1 = input.get(input.get(pc + 1));
                final Integer operand2 = input.get(input.get(pc + 2));
                input.set(input.get(pc + 3), operand1 * operand2);
            } else {
                break;
            }
            pc += 4;
        }
        return input.get(0);
    }

    private static int part2(List<String> day) {
        //19690720
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                final List<Integer> input = day.stream()
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());

                final int result = part1(input, i, j);
                if (result == 19690720) {
                    return 100 * i + j;
                }
            }
        }
        return -1;
    }

}
