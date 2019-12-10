package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;
import com.ismailkuruca.aoc_2019.util.IntCodeMachine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 {

    public static void main(String[] args) {
        final List<String> day9 =
//                Collections.singletonList("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99");
                FileUtil.readFile("day9");
        final List<String> day9Values = Arrays.asList(day9.get(0).split(","));

        final List<Long> input = day9Values.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        final IntCodeMachine intCodeMachine = new IntCodeMachine(input);

        intCodeMachine.input = 2;
        final long run = intCodeMachine.run();
        System.err.println("output: " + run);
    }

}