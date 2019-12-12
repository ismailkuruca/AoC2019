package com.ismailkuruca.aoc_2019;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

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

//        final Moon moon1 = new Moon(-8 ,-10, 0, 0, 0, 0);
//        final Moon moon2 = new Moon(5, 5, 10, 0, 0, 0);
//        final Moon moon3 = new Moon(2, -7, 3, 0, 0, 0);
//        final Moon moon4 = new Moon(9, -8, -3, 0, 0, 0);


        List<Moon> moons = asList(moon1, moon2, moon3, moon4);
        List<Moon> temps = asList(moon1, moon2, moon3, moon4);

        for (int i = 0; i < 1000; i++) {

            moons.forEach(System.out::println);
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

            moons = new ArrayList<>(temps);

            for (Moon moon : moons) {
                moon.x += moon.vx;
                moon.y += moon.vy;
                moon.z += moon.vz;
            }
            System.out.println("Step " + i);
        }

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

    int x;
    int y;
    int z;
    int vx;
    int vy;
    int vz;

    public Moon(int x, int y, int z, int vx, int vy, int vz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }
}