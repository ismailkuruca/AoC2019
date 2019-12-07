package com.ismailkuruca.aoc_2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day7 {

    private static int[] buffer = {0, 0, 0, 0, 0};

    public static void main(String[] args) {
        final List<String> day7 =
//                FileUtil.readFile("day7");
                Collections.singletonList("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5");
        //Collections.singletonList("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99");

        final List<String> day7Values = Arrays.asList(day7.get(0).split(","));

        final List<Integer> input = day7Values.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        final List<Integer> phases = Arrays.asList(9, 8, 7, 6, 5);

        int output = 0;
        try {
            while (true) {
                for (int i = 0; i < phases.size(); i++) {
                    final Integer phase = phases.get(i);
                    output = amp(input, phase, output);
                    System.err.println(output);
                }
            }
        } catch (Exception e) {
            System.err.println(output);
        }
//        int maxamp = 0;
//        for (int j = 0; j < 100000; j++) {
//            Collections.shuffle(phases);
//            for (int i = 0; i < phases.size(); i++) {
//                final int amp = amp(input, phases.get(0), amp(input, phases.get(1), amp(input, phases.get(2), amp(input, phases.get(3), amp(input, phases.get(4), 0)))));
////                System.out.println(amp);
//                if (amp > maxamp) {
//                    maxamp = amp;
//                }
//            }
//        }
//
//        System.out.println(maxamp);


    }

    private static int amp(List<Integer> input, int phase, int signal) {
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
                if (phase != -99) {
                    input.set(input.get(pc + 1), phase);
                    phase = -99;
                } else {
                    input.set(input.get(pc + 1), signal);
                }
                pc += 2;
            } else if (opcode == 4) {
                final int firstOperandMode = getNthDigit(operation, 3);

                final Integer operand1 = firstOperandMode == 0 ? input.get(input.get(pc + 1)) : input.get(pc + 1);
                pc += 2;
                return operand1;
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
                throw new RuntimeException();
            }
        }
    }


    private static int getNthDigit(int number, int n) {
        return (int) ((number / Math.pow(10, n - 1)) % 10);
    }

    static List<List<Integer>> permute(List<Integer> arr, int k) {
        List<List<Integer>> output = new ArrayList();
        for (int i = k; i < arr.size(); i++) {
            java.util.Collections.swap(arr, i, k);
            permute(arr, k + 1);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() - 1) {
            output.add(arr);
        }
        return output;
    }
}
