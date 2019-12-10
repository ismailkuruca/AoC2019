package com.ismailkuruca.aoc_2019.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntCodeMachine {
    public long input;
    public long output;
    Map<Long, Long> program = new HashMap<>();
    long pc = 0;
    long rel = 0;

    public IntCodeMachine(List<Long> input) {
        for (int i = 0; i < input.size(); i++) {
            writeToMem(i, input.get(i), 1);
        }
    }

    public long run() {
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
                output = firstOperand;
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

    private void writeToMem(long target, long value, int operandMode) {
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