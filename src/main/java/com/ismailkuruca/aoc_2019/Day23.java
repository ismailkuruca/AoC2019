package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.ismailkuruca.aoc_2019.Day23.*;

public class Day23 {
    static Map<Integer, IntCodeMachine23> network = new HashMap<>();
    static Packet NATPacket = null;
    static Set<Packet> sentNATPackets = new HashSet<>();

    public static void main(String[] args) throws InterruptedException {
        final List<String> day23 =
                FileUtil.readFile("day23");
        final List<String> day23Values = Arrays.asList(day23.get(0).split(","));

        final List<Long> code = day23Values.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        for (int i = 0; i < 50; i++) {
            network.put(i, new IntCodeMachine23(new ArrayList<>(code), i));
        }

        final ExecutorService executorService = Executors.newFixedThreadPool(51);

        List<Callable<Object>> machines = new ArrayList<>();
        for (Integer integer : network.keySet()) {
            machines.add(() -> network.get(integer).run());
        }
        machines.add(new NAT());

        executorService.invokeAll(machines);

        executorService.awaitTermination(15, TimeUnit.SECONDS);

    }


}

class NAT implements Callable<Object> {
    @Override
    public Object call() throws Exception {
        while (true) {
            Thread.sleep(5);
            if (NATPacket != null && System.nanoTime() % 1000 < 3) {
                boolean allIdle = true;
                int allIdleCount = 0; //debouncing lol
                while (allIdleCount < 100) {
                    for (Integer integer : network.keySet()) {
                        final boolean idle = network.get(integer).input.isEmpty();
                        if (!idle) {
                            System.out.println(integer + " is not halted");
                        }
                        allIdle = allIdle && idle;
                        Thread.sleep(1);
                    }
                    allIdleCount++;
                }

                if (allIdle) {
                    System.out.println("NAT routing packet " + NATPacket);
                    network.get(0).input.add(NATPacket);
                    final int previousSize = sentNATPackets.size();
                    sentNATPackets.add(NATPacket);
                    if (previousSize == sentNATPackets.size()) {
                        System.out.println(NATPacket);
                        System.exit(0);
                    }
                }
            }

        }
    }
}

class Packet {
    long x;
    long y;

    Packet(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Packet packet = (Packet) o;
        return x == packet.x &&
                y == packet.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Packet{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

class IntCodeMachine23 {
    public List<Packet> input = new CopyOnWriteArrayList<>();
    List<Long> buffer = new ArrayList<>();
    public List<Long> output = new CopyOnWriteArrayList<>();
    Map<Long, Long> program = new HashMap<>();
    int ip = 0;
    long pc = 0;
    long rel = 0;
    boolean halted = false;

    public IntCodeMachine23(List<Long> input, int ip) {
        for (int i = 0; i < input.size(); i++) {
            writeToMem(i, input.get(i), 1);
        }
        this.ip = ip;
        buffer.add((long) ip);
    }

    public List<Long> run() throws InterruptedException {
        output = new ArrayList<>();
        while (true) {
            Thread.sleep(5);
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
                Packet currentPacket;
                if (!input.isEmpty()) {
                    halted = false;
                    currentPacket = input.remove(0);
                    buffer.add(currentPacket.x);
                    buffer.add(currentPacket.y);
                    System.out.println(ip + " received packet " + currentPacket.x + " " + currentPacket.y);
                } else {
                    buffer.add(-1L);
                    halted = true;
                }
                writeToMem(pc + 1, buffer.get(0), getNthDigit(operation, 3));
                buffer.remove(0);
                pc += 2;
            } else if (opcode == 4) {
                halted = false;
                final long firstOperand = readFromMem(1, getNthDigit(operation, 3));
                pc += 2;
                output.add(firstOperand);
                if (output.size() == 3) {
                    final int addr = output.get(0).intValue();
                    final Packet packet = new Packet(output.get(1), output.get(2));
                    if (addr == 255) {
                        NATPacket = packet;
                    } else {
                        final IntCodeMachine23 targetMachine = network.get(addr);
                        targetMachine.input.add(packet);
                    }
                    System.out.println(ip + " produced packet " + packet.x + " " + packet.y + " towards " + addr);
                    output.clear();
                }
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