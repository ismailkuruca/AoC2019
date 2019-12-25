package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Day25 {
    public static void main(String[] args) {
        part1();
    }

    private static void part1() {
        final List<String> day25 =
                FileUtil.readFile("day25");
        final List<String> day25Values = Arrays.asList(day25.get(0).split(","));

        final List<Long> input = day25Values.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        final IntCodeMachine25 intCodeMachine25 = new IntCodeMachine25(input);

        /*
                            Passages (spool of cat6)
                                 |
                             Corridor(infinite loop)
                                 |
                             Hallway(wreath) - Engineering (fixed point)
                                 |
                             Sick Bay   ---------   HullBreach - GiftWrapping (sand) - Kitchen (molten lava)
                                 |                      |
                                 |      CrewQuarters(ornament)
                        Arcade(giant electromagnet) - Warp Drive Maintenance(escape pod)- Observatory - Navigation
                                                         |                                                  |
                                                                                                        Storage(space law)
                                                                                                            |
                                                                                                        Science Lab (fuel cell)
                                                                                                            |
                                                                                                     -    Security Checkpoint
                                 |                                  |
                              Holodeck (candy cane)              Stables

         */

        List<String> commands = Arrays.asList(
                "east\n", "take sand\n",
                "west\n", "south\n", "take ornament\n",
                "north\n", "west\n", "north\n", "take wreath\n",
                "east\n", "take fixed point\n",
                "west\n", "north\n", "north\n", "take spool of cat6\n",
                "south\n", "south\n", "south\n", "south\n", "south\n", "take candy cane\n",
                "north\n", "east\n", "east\n", "east\n", "take space law space brochure\n",
                "south\n", "take fuel cell\n",
                "south\n", //checkpoint
                "inv\n",
                "drop spool of cat6\n",
                "drop fuel cell\n",
//                "drop fixed point\n",
//                "drop sand\n",
//                "drop wreath\n",
                "drop ornament\n",
                "drop candy cane\n",
//                "drop space law space brochure\n",
                "west\n"

        );


        List<Long> code = new ArrayList<>();
        commands.stream()
                .map(s -> s.chars().mapToObj(Long::new).collect(Collectors.toList()))
                .forEach(code::addAll);

        intCodeMachine25.input = code;

        final List<Long> run = intCodeMachine25.run();
        System.out.println(run);
    }

}

class IntCodeMachine25 {
    public List<Long> input;
    public List<Long> output = new ArrayList<>();
    Map<Long, Long> program = new HashMap<>();
    long pc = 0;
    long rel = 0;
    boolean finished = false;

    public IntCodeMachine25(List<Long> input) {
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