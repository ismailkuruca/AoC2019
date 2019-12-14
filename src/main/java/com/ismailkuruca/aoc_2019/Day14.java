package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day14 {

    static Map<String, Reaction> reactions = new HashMap<>();
    static Map<String, Long> inventory = new HashMap<>();

    public static void main(String[] args) {
        parse(FileUtil.readFile("day14"));
        long orePerFuel = part1();
        part2(orePerFuel);
    }

    private static void part2(long orePerFuel) {
        inventory.keySet().forEach(s -> inventory.put(s, 0L));
        inventory.put("ORE", 1000000000000L);
        synthesize("FUEL", 1);

        while (true) {
            final long targetFuel = inventory.get("ORE") / orePerFuel;
            final boolean canSynthesize = synthesize("FUEL", targetFuel > 1 ? targetFuel : 1);
            if (!canSynthesize) break;
        }

        System.out.println(inventory.get("FUEL"));
    }

    private static long part1() {
        inventory.put("ORE", Long.MAX_VALUE);
        breakdown("FUEL", 1);

        long orePerFuel = Long.MAX_VALUE - inventory.get("ORE");
        System.out.println(orePerFuel);
        return orePerFuel;
    }

    private static boolean synthesize(String formula, long quantity) {
        if (formula.equals("ORE"))
            return false;

        final Reaction reaction = reactions.get(formula);
        final long reactionCount = (long) Math.ceil((double) quantity / reaction.getOutput().getQuantity());

        for (Ingredient input : reaction.getInputs()) {
            final boolean canBreakdown = breakdown(input.getFormula(), reactionCount * input.getQuantity());
            if (!canBreakdown)
                return false;
        }

        inventory.put(formula, inventory.get(formula) + reactionCount * reaction.getOutput().getQuantity());
        return true;
    }

    private static boolean breakdown(String formula, long quantity) {
        final boolean haveEnough = inventory.get(formula) >= quantity;
        final boolean finished = synthesize(formula, quantity - inventory.get(formula));
        if (!haveEnough && !finished)
            return false;

        inventory.put(formula, inventory.get(formula) - quantity);
        return true;
    }

    private static void parse(List<String> day14) {
        for (String s : day14) {
            final String[] split = s.split("=>");
            final String[] inputs = split[0].trim().split(",");

            final List<Ingredient> left = Arrays.stream(inputs).map(s1 -> {
                final String[] input = s1.trim().split(" ");
                final int inputQuantity = Integer.parseInt(input[0].trim());
                final String inputFormula = input[1].trim();
                inventory.put(inputFormula, 0L);
                return new Ingredient(inputQuantity, inputFormula);
            }).collect(Collectors.toList());

            final String[] output = split[1].trim().split(" ");
            final int outputQuantity = Integer.parseInt(output[0].trim());
            final String outputFormula = output[1].trim();
            inventory.put(outputFormula, 0L);
            final Ingredient right = new Ingredient(outputQuantity, outputFormula);
            reactions.put(right.getFormula(), new Reaction(left, right));
        }
    }

}

class Reaction {
    private List<Ingredient> inputs;
    private Ingredient output;

    public Reaction(List<Ingredient> inputs, Ingredient outputs) {
        this.inputs = inputs;
        this.output = outputs;
    }

    public List<Ingredient> getInputs() {
        return inputs;
    }

    public void setInputs(List<Ingredient> inputs) {
        this.inputs = inputs;
    }

    public Ingredient getOutput() {
        return output;
    }

    public void setOutput(Ingredient output) {
        this.output = output;
    }
}

class Ingredient {
    private int quantity;
    private String formula;

    public Ingredient(int quantity, String formula) {
        this.quantity = quantity;
        this.formula = formula;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}