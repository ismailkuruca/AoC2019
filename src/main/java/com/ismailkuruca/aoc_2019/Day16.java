package com.ismailkuruca.aoc_2019;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Day16 {

    final static Map<Integer, int[]> COEFFS = new HashMap<>();

    public static void main(String[] args) {
        part1();
        part2();
    }

    public static void part2() {
        String input = "59708372326282850478374632294363143285591907230244898069506559289353324363446827480040836943068215774680673708005813752468017892971245448103168634442773462686566173338029941559688604621181240586891859988614902179556407022792661948523370366667688937217081165148397649462617248164167011250975576380324668693910824497627133242485090976104918375531998433324622853428842410855024093891994449937031688743195134239353469076295752542683739823044981442437538627404276327027998857400463920633633578266795454389967583600019852126383407785643022367809199144154166725123539386550399024919155708875622641704428963905767166129198009532884347151391845112189952083025";
        input = String.join("", Collections.nCopies(10000, input));
        final int[] digits = input.chars()
                .map(i -> i - 48)
                .toArray();
        int offset = Integer.parseInt(input.substring(0, 7));
        for (int phase = 0; phase < 100; phase++) {
            for (int i = input.length() - 2; i > offset - 5; i--) {
                int number = digits[i] + digits[i + 1];
                digits[i] = Math.abs(number % 10);
            }
        }
        for (int i = offset; i < offset + 8; i++) {
            System.out.print(digits[i]);
        }
    }

    public static void part1() {
        String input = "59708372326282850478374632294363143285591907230244898069506559289353324363446827480040836943068215774680673708005813752468017892971245448103168634442773462686566173338029941559688604621181240586891859988614902179556407022792661948523370366667688937217081165148397649462617248164167011250975576380324668693910824497627133242485090976104918375531998433324622853428842410855024093891994449937031688743195134239353469076295752542683739823044981442437538627404276327027998857400463920633633578266795454389967583600019852126383407785643022367809199144154166725123539386550399024919155708875622641704428963905767166129198009532884347151391845112189952083025";
        for (int phase = 0; phase < 100; phase++) {
            String output = "";
            final char[] chars = input.toCharArray();
            for (int i = 0; i < input.length(); i++) {
                int sum = 0;
                for (int j = 0; j < input.length(); j++) {
                    final int el = Integer.parseInt(String.valueOf(chars[j]));
                    final int newEl = el * coeffs(0, input.length(), i + 1)[j];
                    sum += newEl;
                }
                output += Math.abs(sum) % 10;
            }
            input = output;
        }

        System.out.println(input.substring(0, 8));

    }

    private static int[] coeffs(int start, int length, int a) {
        int[] base = {0, 1, 0, -1};

        if (COEFFS.containsKey(a)) {
            return COEFFS.get(a);
        }

        int[] phase = new int[length + 2];
        try {
            int b = 0;
            for (int i = start; i < length + 2; i += a) {
                for (int j = 0; j < a; j++) {
                    phase[i + j + 1] = base[b % 4];
                }
                b++;
            }
        } catch (Exception e) {
        }


        phase = Arrays.copyOfRange(phase, start + 2, length + 2);
        COEFFS.put(a, phase);
        return phase;
    }
}
