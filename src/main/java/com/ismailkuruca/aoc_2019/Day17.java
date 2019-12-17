package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Day17 {

    //L10, L8, R8, L8, R6,
    //L10, L8, R8, L8, R6,
    //R6, R8, R8, R6, R6, L8, L10
    //R6, R8, R8, R6, R6, L8, L10
    //R6, R8, R8, R6, R6, L8, L10
    //R6, R8, R8,
    //L10, L8, R8, L8, R6
    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part2() {
        //A
        //L10, L8, R8, L8, R6
        //B
        //R6, R8, R8, R6, R6, L8, L10
        //C
        //R6, R8, R8
        //A,A,B,B,B,C,B

//        char[] A = {'L', 10, }
    }

    private static void part1() {
        final List<String> day17 =
                FileUtil.readFile("day17");
        final List<String> day17Values = Arrays.asList(day17.get(0).split(","));

        final List<Long> input = day17Values.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        final IntCodeMachine17 intCodeMachine17 = new IntCodeMachine17(input);

        String output = "";
        final List<Long> run = intCodeMachine17.run();
        for (Long aLong : run) {
            output += (char) aLong.intValue();
        }

        final String[] lines = output.split("\\n");
        String newoutput = "";
        int weight = 0;
        for (int i = 0; i < lines.length; i++) {
            final char[] chars = lines[i].toCharArray();
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == '#') {
                    if (j < chars.length - 2 && j > 0 && chars[j + 1] == '#' && chars[j - 1] == '#') {
                        if (i > 0 && i < lines.length - 2 && lines[i - 1].toCharArray()[j] == '#' && lines[i + 1].toCharArray()[j] == '#') {
                            weight += i * j;
                            chars[j] = 'O' ;
                        }
                    }
                }
            }
            System.out.println(new String(chars));

        }
        System.out.println(weight);
        System.out.println(newoutput);
    }
}

//34 11, 34 31, 38 17
class IntCodeMachine17 {
    public Long input;
    public List<Long> output = new ArrayList<>();
    Map<Long, Long> program = new HashMap<>();
    long pc = 0;
    long rel = 0;
    boolean finished = false;

    public IntCodeMachine17(List<Long> input) {
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