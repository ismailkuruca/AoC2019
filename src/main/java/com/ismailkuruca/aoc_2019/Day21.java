package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Day21 {
    public static void main(String[] args) {
        part2();
    }

    private static void part2() {
        final List<String> day21 =
                FileUtil.readFile("day21");
        final List<String> day21Values = Arrays.asList(day21.get(0).split(","));

        final List<Long> input = day21Values.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        final IntCodeMachine21 intCodeMachine21 = new IntCodeMachine21(input);
        List<String> springScript = Arrays.asList(
                "NOT A J\n",
                "NOT B T\n",
                "OR T J\n",
                "NOT C T\n",
                "OR T J\n",
                "AND D J\n",
//                "NOT A J\n",
//                "NOT B T\n",
//                "AND T J\n",
//                "NOT C T\n",
//                "AND T J\n",
//                "AND D J\n",
                "WALK\n");

        List<Long> code = new ArrayList<>();
        springScript.stream()
                .map(s -> s.chars().mapToObj(Long::new).collect(Collectors.toList()))
                .forEach(code::addAll);
        intCodeMachine21.input = code;
        final List<Long> run = intCodeMachine21.run();
    }

}

class SpringDroid {
    boolean oneTile = false;
    boolean twotile = false;
    boolean threeTile = false;
    boolean fourTile = false;
    boolean temporary = false;
    boolean jump = false;
}

class IntCodeMachine21 {
    public List<Long> input;
    public List<Long> output = new ArrayList<>();
    Map<Long, Long> program = new HashMap<>();
    long pc = 0;
    long rel = 0;
    boolean finished = false;

    public IntCodeMachine21(List<Long> input) {
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
                if (firstOperand > 150) {
                    System.err.println(firstOperand);
                }
                System.out.print(Character.valueOf((char) firstOperand));
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
                System.out.println(pc);
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