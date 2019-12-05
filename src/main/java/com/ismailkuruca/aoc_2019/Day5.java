package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day5 {

    private static final int SYSTEM_ID = 5;

    public static void main(String[] args) {
        final List<String> day5 = FileUtil.readFile("day5");
//                Collections.singletonList("3,3,1105,-1,9,1101,0,0,12,4,12,99,1");
        //Collections.singletonList("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99");

        final List<String> day1Values = Arrays.asList(day5.get(0).split(","));

        final List<Integer> input = day1Values.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());


        day5(input);
    }

    private static int day5(List<Integer> input) {
        int pc = 0;

        while (true) {
            final Integer operation = input.get(pc);
            final int opcode = operation % 100;
            if (opcode == 1) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);

                final Integer operand1 = firstOperandMode == 0 ? input.get(input.get(pc + 1)) : input.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? input.get(input.get(pc + 2)) : input.get(pc + 2);
                input.set(input.get(pc + 3), operand1 + operand2);
                pc += 4;
            } else if (opcode == 2) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);

                final Integer operand1 = firstOperandMode == 0 ? input.get(input.get(pc + 1)) : input.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? input.get(input.get(pc + 2)) : input.get(pc + 2);
                input.set(input.get(pc + 3), operand1 * operand2);
                pc += 4;
            } else if (opcode == 3) {
                input.set(input.get(pc + 1), SYSTEM_ID);
                pc += 2;
            } else if (opcode == 4) {
                final int firstOperandMode = getNthDigit(operation, 3);

                final Integer operand1 = firstOperandMode == 0 ? input.get(input.get(pc + 1)) : input.get(pc + 1);
                System.out.println(operand1);
                pc += 2;
            } else if (opcode == 5) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);
                final Integer operand1 = firstOperandMode == 0 ? input.get(input.get(pc + 1)) : input.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? input.get(input.get(pc + 2)) : input.get(pc + 2);

                if (!operand1.equals(0)) {
                    pc = operand2;
                } else {
                    pc += 3;
                }
            } else if (opcode == 6) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);
                final Integer operand1 = firstOperandMode == 0 ? input.get(input.get(pc + 1)) : input.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? input.get(input.get(pc + 2)) : input.get(pc + 2);

                if (operand1.equals(0)) {
                    pc = operand2;
                } else {
                    pc += 3;
                }
            } else if (opcode == 7) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);
                final Integer operand1 = firstOperandMode == 0 ? input.get(input.get(pc + 1)) : input.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? input.get(input.get(pc + 2)) : input.get(pc + 2);

                input.set(input.get(pc + 3), operand1 < operand2 ? 1 : 0);
                pc += 4;
            } else if (opcode == 8) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);
                final Integer operand1 = firstOperandMode == 0 ? input.get(input.get(pc + 1)) : input.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? input.get(input.get(pc + 2)) : input.get(pc + 2);

                input.set(input.get(pc + 3), operand1.equals(operand2) ? 1 : 0);
                pc += 4;
            } else {
                break;
            }
        }
        return input.get(0);
    }

//    private static int part2(List<String> day) {
//        //19690720
//        for (int i = 0; i < 100; i++) {
//            for (int j = 0; j < 100; j++) {
//                final List<Integer> input = day.stream()
//                        .map(Integer::parseInt)
//                        .collect(Collectors.toList());
//
//                final int result = part1(input, i, j);
//                if (result == 19690720) {
//                    return 100 * i + j;
//                }
//            }
//        }
//        return -1;
//    }


    private static int getNthDigit(int number, int n) {
        return (int) ((number / Math.pow(10, n - 1)) % 10);
    }
}
