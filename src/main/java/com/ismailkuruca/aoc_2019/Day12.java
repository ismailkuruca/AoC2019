package com.ismailkuruca.aoc_2019;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;
import static java.util.Arrays.asList;
import static org.apache.commons.math3.util.ArithmeticUtils.lcm;

public class Day12 {

    private static final Moon MOON_1 = new Moon(0, 4, 0, 0, 0, 0);
    private static final Moon MOON_2 = new Moon(-10, -6, -14, 0, 0, 0);
    private static final Moon MOON_3 = new Moon(9, -16, -3, 0, 0, 0);
    private static final Moon MOON_4 = new Moon(6, -1, 2, 0, 0, 0);

    private static List<Moon> moonsForPart1 = asList(new Moon(MOON_1), new Moon(MOON_2), new Moon(MOON_3), new Moon(MOON_4));
    private static List<Moon> moonsForPart2 = asList(new Moon(MOON_1), new Moon(MOON_2), new Moon(MOON_3), new Moon(MOON_4));

    public static void main(String[] args) {
        part1(moonsForPart1);
        part2(moonsForPart2);
    }

    private static void part1(List<Moon> moons) {
        for (int i = 0; i < 1000; i++) {
            iterate(moons);
        }
        int en = 0;
        for (Moon moon : moons) {
            int pot = Math.abs(moon.x) + Math.abs(moon.y) + Math.abs(moon.z);
            int kin = Math.abs(moon.vx) + Math.abs(moon.vy) + Math.abs(moon.vz);
            en += pot * kin;
        }
        System.out.println("Part1: " + en);
    }

    private static void part2(List<Moon> moons) {
        final List<Moon> initial = asList(new Moon(MOON_1), new Moon(MOON_2), new Moon(MOON_3), new Moon(MOON_4));
        final List<Integer> X = initial.stream().map(moon -> moon.x).collect(Collectors.toList());
        final List<Integer> Y = initial.stream().map(moon -> moon.y).collect(Collectors.toList());
        final List<Integer> Z = initial.stream().map(moon -> moon.z).collect(Collectors.toList());

        boolean periodicX = false, periodicY = false, periodicZ = false;
        long periodX = 1, periodY = 1, periodZ = 1;

        while (true) {
            iterate(moons);

            periodX += periodicX ? 0 : 1;
            periodY += periodicY ? 0 : 1;
            periodZ += periodicZ ? 0 : 1;

            periodicX = X.equals(moons.stream().map(moon -> moon.x).collect(Collectors.toList())) || periodicX;
            periodicY = Y.equals(moons.stream().map(moon -> moon.y).collect(Collectors.toList())) || periodicY;
            periodicZ = Z.equals(moons.stream().map(moon -> moon.z).collect(Collectors.toList())) || periodicZ;

            if (periodicX && periodicY && periodicZ) {
                System.out.println("Part2: " + lcm(periodX, lcm(periodY, periodZ)));
                break;
            }
        }
    }

    private static void iterate(List<Moon> moons) {
        for (int m = 0; m < moons.size(); m++) {
            final Moon moon1 = moons.get(m);
            for (int n = m; n < moons.size(); n++) {
                if (m != n) {
                    final Moon moon2 = moons.get(n);
                    moon1.vx -= compare(moon1.x, moon2.x);
                    moon1.vy -= compare(moon1.y, moon2.y);
                    moon1.vz -= compare(moon1.z, moon2.z);

                    moon2.vx += compare(moon1.x, moon2.x);
                    moon2.vy += compare(moon1.y, moon2.y);
                    moon2.vz += compare(moon1.z, moon2.z);
                }
            }

        }
        for (Moon moon : moons) {
            moon.x += moon.vx;
            moon.y += moon.vy;
            moon.z += moon.vz;
        }
    }
}


class Moon {
    int x;
    int y;
    int z;
    int vx;
    int vy;
    int vz;

    Moon(Moon m) {
        this(m.x, m.y, m.z, m.vx, m.vy, m.vz);
    }

    Moon(int x, int y, int z, int vx, int vy, int vz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Moon moon = (Moon) o;
        return x == moon.x &&
                y == moon.y &&
                z == moon.z &&
                vx == moon.vx &&
                vy == moon.vy &&
                vz == moon.vz;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, vx, vy, vz);
    }


}
