package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Day19 {

    public static void main(String[] args) {
        part1();
    }

    private static void part1() {
        final List<String> day19 =
                FileUtil.readFile("day19");
        final List<String> day19Values = asList(day19.get(0).split(","));

        final List<Long> input = day19Values.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        final IntCodeMachine19 intCodeMachine19 = new IntCodeMachine19(input);

        final int[][] grid = new int[100][100];


        intCodeMachine19.input = new ArrayList(asList(0L, 0L));
        final List<Long> run = intCodeMachine19.run();
        System.out.println(run);

        int num = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                final IntCodeMachine19 machine19 = new IntCodeMachine19(input);
                machine19.input = new ArrayList<>(asList((long) (i + 948), (long) (j + 761)));
                machine19.run();
                grid[i][j] = Math.toIntExact(machine19.output.get(0));
                if(grid[i][j] == 1) num++;
            }
        }

        System.out.println(num);
        System.out.println(948 + 10000 * 761);

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

}

class IntCodeMachine19 {
    public List<Long> input;
    public List<Long> output = new ArrayList<>();
    Map<Long, Long> program = new HashMap<>();
    long pc = 0;
    long rel = 0;
    boolean finished = false;

    public IntCodeMachine19(List<Long> input) {
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
                writeToMem(pc + 1, input.get(0), getNthDigit(operation, 3));
                input.remove(0);
                pc += 2;
            } else if (opcode == 4) {
                final long firstOperand = readFromMem(1, getNthDigit(operation, 3));
                pc += 2;
                output.add(firstOperand);
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