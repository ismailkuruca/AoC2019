package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

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

        System.out.println(deck);
        System.err.println(deck.indexOf(2019));
    }

    private static void cut(int pos) {
        if (pos < 0) {
            final List<Integer> cut = deck.subList(deck.size() - Math.abs(pos), deck.size());
            deck = deck.subList(0, deck.size() -  Math.abs(pos));
            cut.addAll(deck);
            deck = cut;
        } else {
            final List<Integer> cut = deck.subList(0, pos);
            deck = deck.subList(pos, deck.size());
            deck.addAll(cut);
        }
        System.out.println();
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
        for (int i = 0; i < table.length; i++) {
            deck.add(table[i]);
        }
    }
}
