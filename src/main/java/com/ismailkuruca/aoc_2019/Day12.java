package com.ismailkuruca.aoc_2019;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.apache.commons.math3.util.ArithmeticUtils.lcm;

public class Day12 {

    public static void main(String[] args) {

//<x = 0, y = 4, z = 0 >
//<x = -10, y = -6, z = -14 >
//<x = 9, y = -16, z = -3 >
//<x = 6, y = -1, z = 2 >

        final Moon moon1 = new Moon(0, 4, 0, 0, 0, 0);
        final Moon moon2 = new Moon(-10, -6, -14, 0, 0, 0);
        final Moon moon3 = new Moon(9, -16, -3, 0, 0, 0);
        final Moon moon4 = new Moon(6, -1, 2, 0, 0, 0);

//        final Moon moon1 = new Moon(-1, 0, 2, 0, 0, 0);
//        final Moon moon2 = new Moon(2, -10, -7, 0, 0, 0);
//        final Moon moon3 = new Moon(4, -8, 8, 0, 0, 0);
//        final Moon moon4 = new Moon(3, 5, -1, 0, 0, 0);

//        final Moon moon1 = new Moon(-8, -10, 0, 0, 0, 0);
//        final Moon moon2 = new Moon(5, 5, 10, 0, 0, 0);
//        final Moon moon3 = new Moon(2, -7, 3, 0, 0, 0);
//        final Moon moon4 = new Moon(9, -8, -3, 0, 0, 0);

        List<Moon> moons = asList(moon1, moon2, moon3, moon4);

        final List<Integer> X = moons.stream()
                .map(moon -> moon.x)
                .collect(Collectors.toList());
        final List<Integer> Y = moons.stream()
                .map(moon -> moon.y)
                .collect(Collectors.toList());
        final List<Integer> Z = moons.stream()
                .map(moon -> moon.z)
                .collect(Collectors.toList());

        boolean perx = false, pery = false, perz = false;
        long px = 1, py = 1, pz = 1;

        long step = 0;
        while (true) {
            round(moons);

            if (X.equals(moons.stream()
                    .map(moon -> moon.x)
                    .collect(Collectors.toList()))) {
                perx = true;
            }

            if (Y.equals(moons.stream()
                    .map(moon -> moon.y)
                    .collect(Collectors.toList()))) {
                pery = true;
            }

            if (Z.equals(moons.stream()
                    .map(moon -> moon.z)
                    .collect(Collectors.toList()))) {
                perz = true;
                System.out.println(Z);
            }
            px += perx ? 0 : 1;
            py += pery ? 0 : 1;
            pz += perz ? 0 : 1;

            if (perx && pery && perz) {
                System.err.println(px + " " + py + " " + pz) ;
                System.out.println(lcm(px + 1, lcm(py + 1, pz + 1)));
                break;
            }
        }

        System.out.println(step);

        int en = 0;
        for (Moon moon : moons) {
            System.out.println(moon);
            int pot = Math.abs(moon.x) + Math.abs(moon.y) + Math.abs(moon.z);
            int kin = Math.abs(moon.vx) + Math.abs(moon.vy) + Math.abs(moon.vz);
            System.out.println(pot + " " + kin);
            en += pot * kin;
        }
        System.out.println(en);
    }


    private static List<Moon> round(List<Moon> moons) {
        final List<Moon> temps = new ArrayList<>(moons);
        for (int m = 0; m < moons.size(); m++) {
            final Moon moonP = moons.get(m);
            final Moon tempP = temps.get(m);
            for (int n = m; n < moons.size(); n++) {
                if (m != n) {
                    final Moon moonsS = moons.get(n);
                    final Moon tempS = temps.get(n);
                    if (moonP.x > moonsS.x) {
                        tempP.vx -= 1;
                        tempS.vx += 1;
                    } else if (moonP.x < moonsS.x) {
                        tempP.vx += 1;
                        tempS.vx -= 1;
                    }
                    if (moonP.y > moonsS.y) {
                        tempP.vy -= 1;
                        tempS.vy += 1;
                    } else if (moonP.y < moonsS.y) {
                        tempP.vy += 1;
                        tempS.vy -= 1;
                    }
                    if (moonP.z > moonsS.z) {
                        tempP.vz -= 1;
                        tempS.vz += 1;
                    } else if (moonP.z < moonsS.z) {
                        tempP.vz += 1;
                        tempS.vz -= 1;
                    }
                }
            }

        }

        for (Moon moon : temps) {
            moon.x += moon.vx;
            moon.y += moon.vy;
            moon.z += moon.vz;
        }

        return temps;
    }
}


class Moon {
    @Override
    public String toString() {
        return "Moon{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", vx=" + vx +
                ", vy=" + vy +
                ", vz=" + vz +
                '}' ;
    }

    public Moon(Moon m) {
        this.x = m.x;
        this.y = m.y;
        this.z = m.z;
        this.vx = m.vx;
        this.vz = m.vz;
        this.vy = m.vy;
    }

    int x;
    int y;
    int z;
    int vx;
    int vy;
    int vz;

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

    public Moon(int x, int y, int z, int vx, int vy, int vz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;


    }


}

class Hash {
    int i1;
    int i2;
    int i3;
    int i4;

    public Hash(List<Moon> moons) {
        i1 = moons.get(0).hashCode();
        i2 = moons.get(1).hashCode();
        i3 = moons.get(2).hashCode();
        i4 = moons.get(3).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hash hash = (Hash) o;
        return i1 == hash.i1 &&
                i2 == hash.i2 &&
                i3 == hash.i3 &&
                i4 == hash.i4;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i1, i2, i3, i4);
    }
}