package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day22 {

    private static List<Integer> deck;
    static int size = 10007;

    static {
        deck = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            deck.add(i);
        }
    }

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part2() {
        final List<String> day22 = FileUtil.readFile("day22");

        BigInteger offset = BigInteger.ZERO;
        BigInteger increment = BigInteger.ONE;
        final BigInteger cards = BigInteger.valueOf(119315717514047L);
        final BigInteger repeat = BigInteger.valueOf(101741582076661L);

        BigInteger pos = BigInteger.valueOf(2020);
        for (String entry : day22) {
            if (entry.startsWith("cut")) {
                final int i = Integer.parseInt(entry.split(" ")[1]);
                offset = offset.add(BigInteger.valueOf(i).multiply(increment)).mod(cards);
            } else if (entry.startsWith("deal with")) {
                final int i = Integer.parseInt(entry.split(" ")[3]);
                increment = increment.multiply(BigInteger.valueOf(i).modInverse(cards)).mod(cards);
            } else {
                increment = increment.negate().mod(cards);
                offset = offset.add(increment).mod(cards);
            }
        }
        final BigInteger totalIncrement = increment.modPow(repeat, cards);
        final BigInteger totalOffset = offset
                .multiply(BigInteger.ONE.subtract(totalIncrement))
                .multiply(BigInteger.ONE.subtract(increment).modInverse(cards))
                .mod(cards);

        System.out.println(totalOffset.add(pos.multiply(totalIncrement)).mod(cards));
    }

    private static void part1() {
        final List<String> day22 = FileUtil.readFile("day22");
        day22.forEach(entry -> {
            if (entry.startsWith("cut")) {
                cut(Integer.parseInt(entry.split(" ")[1]));
            } else if (entry.startsWith("deal with")) {
                deal(Integer.parseInt(entry.split(" ")[3]));
            } else {
                stack();
            }
        });
        System.out.println(deck.indexOf(2019));
    }

    private static void cut(int pos) {
        if (pos < 0) {
            final List<Integer> cut = deck.subList(deck.size() - Math.abs(pos), deck.size());
            deck = deck.subList(0, deck.size() - Math.abs(pos));
            cut.addAll(deck);
            deck = cut;
        } else {
            final List<Integer> cut = deck.subList(0, pos);
            deck = deck.subList(pos, deck.size());
            deck.addAll(cut);
        }
    }

    private static void stack() {
        Collections.reverse(deck);
    }

    private static void deal(int num) {
        int pos = 0;
        final int[] table = new int[size];
        while (deck.size() > 0) {
            table[pos] = deck.get(0);
            deck.remove(deck.get(0));
            pos += num;
            pos %= size;
        }
        deck = new ArrayList<>();
        for (int value : table) {
            deck.add(value);
        }
    }
}
