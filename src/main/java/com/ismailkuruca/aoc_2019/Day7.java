package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.ismailkuruca.aoc_2019.Day7.getNthDigit;

public class Day7 {


    public static void main(String[] args) {
        final List<String> day7 =
                FileUtil.readFile("day7");
//                Collections.singletonList("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5");
        //Collections.singletonList("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99");

        final List<String> day7Values = Arrays.asList(day7.get(0).split(","));

        final List<Integer> input = day7Values.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        final List<Integer> phases = Arrays.asList(9, 8, 7, 6, 5);

        int maxamp = 0;
        for (int j = 0; j < 100000; j++) {
            Collections.shuffle(phases);
            final Machine machineA = new Machine();
            machineA.phase = phases.get(0);
            machineA.program = new ArrayList<>(input);
            final Machine machineB = new Machine();
            machineB.phase = phases.get(1);
            machineB.program = new ArrayList<>(input);
            final Machine machineC = new Machine();
            machineC.phase = phases.get(2);
            machineC.program = new ArrayList<>(input);
            final Machine machineD = new Machine();
            machineD.phase = phases.get(3);
            machineD.program = new ArrayList<>(input);
            final Machine machineE = new Machine();
            machineE.phase = phases.get(4);
            machineE.program = new ArrayList<>(input);

            try {
                while (true) {
                    machineB.input = machineA.run();
                    machineC.input = machineB.run();
                    machineD.input = machineC.run();
                    machineE.input = machineD.run();
                    machineA.input = machineE.run();
                }
            } catch (Exception e) {
                if (maxamp < machineE.output) {
                    maxamp = machineE.output;
                }
            }
        }
        System.err.println(maxamp);
    }


    public static int getNthDigit(int number, int n) {
        return (int) ((number / Math.pow(10, n - 1)) % 10);
    }
}

class Machine {
    public int input;
    public int output;
    List<Integer> program;
    public int phase;
    public int pc = 0;

    public int run() {
        while (true) {
            final Integer operation = program.get(pc);
            final int opcode = operation % 100;
            if (opcode == 1) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);

                final Integer operand1 = firstOperandMode == 0 ? program.get(program.get(pc + 1)) : program.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? program.get(program.get(pc + 2)) : program.get(pc + 2);
                program.set(program.get(pc + 3), operand1 + operand2);
                pc += 4;
            } else if (opcode == 2) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);

                final Integer operand1 = firstOperandMode == 0 ? program.get(program.get(pc + 1)) : program.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? program.get(program.get(pc + 2)) : program.get(pc + 2);
                program.set(program.get(pc + 3), operand1 * operand2);
                pc += 4;
            } else if (opcode == 3) {
                if (phase != -99) {
                    program.set(program.get(pc + 1), phase);
                    phase = -99;
                } else {
                    program.set(program.get(pc + 1), input);
                }
                pc += 2;
            } else if (opcode == 4) {
                final int firstOperandMode = getNthDigit(operation, 3);

                final Integer operand1 = firstOperandMode == 0 ? program.get(program.get(pc + 1)) : program.get(pc + 1);
                pc += 2;
                output = operand1;
                return output;
            } else if (opcode == 5) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);
                final Integer operand1 = firstOperandMode == 0 ? program.get(program.get(pc + 1)) : program.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? program.get(program.get(pc + 2)) : program.get(pc + 2);

                if (!operand1.equals(0)) {
                    pc = operand2;
                } else {
                    pc += 3;
                }
            } else if (opcode == 6) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);
                final Integer operand1 = firstOperandMode == 0 ? program.get(program.get(pc + 1)) : program.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? program.get(program.get(pc + 2)) : program.get(pc + 2);

                if (operand1.equals(0)) {
                    pc = operand2;
                } else {
                    pc += 3;
                }
            } else if (opcode == 7) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);
                final Integer operand1 = firstOperandMode == 0 ? program.get(program.get(pc + 1)) : program.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? program.get(program.get(pc + 2)) : program.get(pc + 2);

                program.set(program.get(pc + 3), operand1 < operand2 ? 1 : 0);
                pc += 4;
            } else if (opcode == 8) {
                final int firstOperandMode = getNthDigit(operation, 3);
                final int secondOperandMode = getNthDigit(operation, 4);
                final Integer operand1 = firstOperandMode == 0 ? program.get(program.get(pc + 1)) : program.get(pc + 1);
                final Integer operand2 = secondOperandMode == 0 ? program.get(program.get(pc + 2)) : program.get(pc + 2);

                program.set(program.get(pc + 3), operand1.equals(operand2) ? 1 : 0);
                pc += 4;
            } else {
                throw new RuntimeException();
            }
        }
    }
}
