package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Day13 {

    public static void main(String[] args) {
        final List<String> day13 =
                FileUtil.readFile("day13");
        final List<String> day13Values = Arrays.asList(day13.get(0).split(","));

        final List<Long> input = day13Values.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        part2(input);

    }

    private static void part2(List<Long> input) {
        final IntCodeMachine13 intCodeMachine = new IntCodeMachine13(input);
        intCodeMachine.writeToMem(0, 2, 1);
        int next = 0;
        char[][] board = null;
        while (true) {
            intCodeMachine.input = (long) next;
            final List<Long> run = intCodeMachine.run();
            board = print(run, board);
            if (intCodeMachine.finished) break;
            next = getNextMove(board);
            System.out.println(next);
        }
    }

    private static void part1(List<Long> input) {
        final IntCodeMachine13 intCodeMachine = new IntCodeMachine13(input);
        final List<Long> output = intCodeMachine.run();
        final char[][] board = print(output, null);

        System.out.println(count(board, '-'));
    }

    private static int count(char[][] board, char c) {
        int count = 0;
        for (char[] chars : board) {
            for (char aChar : chars) {
                if (aChar == c) count++;
            }
        }
        return count;
    }

    private static int getNextMove(char[][] board) {
        int paddle = 0;
        int ball = 0;
        for (int i = 0; i < board.length; i++) {
            final char[] chars = board[i];
            for (int j = 0; j < chars.length; j++) {
                if (board[i][j] == '_') {
                    paddle = j;
                }
                if (board[i][j] == 'O') {
                    ball = j;
                }
            }
        }
        return Integer.compare(ball, paddle);
    }

    private static char[][] print(List<Long> output, char[][] board) {
        if (board == null) {
            board = new char[26][35];
        }

        for (int i = 0; i < output.size(); i += 3) {
            final int x = Math.toIntExact(output.get(i));
            final int y = Math.toIntExact(output.get(i + 1));
            final int id = Math.toIntExact(output.get(i + 2));

            if (x == -1) {
                System.out.println("Score: " + id);
                continue;
            }
            switch (id) {
                case 0: {
                    board[y][x] = ' ';
                    break;
                }
                case 1: {
                    board[y][x] = '#';
                    break;
                }
                case 2: {
                    board[y][x] = '-';
                    break;
                }
                case 3: {
                    board[y][x] = '_';
                    break;
                }
                case 4: {
                    board[y][x] = 'O';
                    break;
                }
            }

        }

        for (int i = 0; i < board.length; i++) {
            final char[] chars = board[i];
            System.out.print(i < 10 ? i + "  " : i + " ");
            for (char aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }
        return board;
    }

}

class IntCodeMachine13 {
    public Long input;
    public List<Long> output = new ArrayList<>();
    Map<Long, Long> program = new HashMap<>();
    long pc = 0;
    long rel = 0;
    boolean finished = false;

    public IntCodeMachine13(List<Long> input) {
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
                if (input == null) return output;
                writeToMem(pc + 1, input, getNthDigit(operation, 3));
                input = null;
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