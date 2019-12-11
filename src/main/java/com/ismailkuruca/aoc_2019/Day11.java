package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.Arrays;
import java.util.List;

public class Day11 {

    public static void main(String[] args) {
        final List<String> day11 =
//                Collections.singletonList("109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99");
                FileUtil.readFile("day11");
        final List<String> day11Values = Arrays.asList(day11.get(0).split(","));

    }

}