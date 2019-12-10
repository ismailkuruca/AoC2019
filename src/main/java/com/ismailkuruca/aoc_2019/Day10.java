package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.*;

public class Day10 {

    public static void main(String[] args) {
        final List<String> day10 =
                FileUtil.readFile("day10");

// final List<Long> input = day9Values.stream()
// .map(Long::parseLong)
// .collect(Collectors.toList());

        System.out.println(day10.size());

        final int[][] graph = new int[day10.size()][day10.get(0).length()];
        for (int i = 0; i < day10.size(); i++) {
            final char[] chars = day10.get(i).toCharArray();
            for (int j = 0; j < chars.length; j++) {
                graph[i][j] = chars[j] == "#".charAt(0) ? 1 : 0;
            }
        }

        iterate(graph);

    }


    private static int[][] iterate(int[][] graph) {
        int max = 0;
        Map<Double, ArrayList<Point10>> astr = new HashMap<>();
        Point10 maxp = null;
        Map<Double, ArrayList<Point10>> finalAstr = new HashMap<>();

        int destroyed = 0;
        while (true) {
            Set<Double> angles = new HashSet<>();
            for (int i = 0; i < graph.length; i++) {
                for (int j = 0; j < graph[i].length; j++) {
                    angles = new HashSet<>();
                    astr = new HashMap<>();
                    if (graph[i][j] == 1) {
                        for (int m = 0; m < graph.length; m++) {
                            for (int n = 0; n < graph[m].length; n++) {
                                if (graph[m][n] == 1 && (m != i || n != j)) {
                                    double v = Math.atan2(n - j, m - i) - Math.PI / 2;
                                    double degrees = Math.toDegrees(v);
//                                    if (degrees < 0) degrees += 360;
                                    angles.add(v);
                                    astr.putIfAbsent(degrees, new ArrayList<>());
                                    astr.get(degrees).add(new Point10(m, n, degrees));
                                }
                            }
                        }
                        if (max < angles.size()) {
                            max = angles.size();
                            finalAstr = new HashMap<>(astr);
                            maxp = new Point10(i, j, 0);
                        }
                    }

                }
            }
            List<Point10> imm = new ArrayList<>();
            for (Double aDouble : finalAstr.keySet()) {
                final ArrayList<Point10> point10s = finalAstr.get(aDouble);
                Point10 finalMaxp = maxp;
                point10s.sort((o1, o2) -> {
                    final double dist1 = Math.sqrt(Math.pow(finalMaxp.x - (double) o1.x, 2) + Math.pow(finalMaxp.y - (double) o1.y, 2));
                    final double dist2 = Math.sqrt(Math.pow(finalMaxp.x - (double) o2.x, 2) + Math.pow(finalMaxp.y - (double) o2.y, 2));
                    return dist1 > dist2 ? 1 : -1;
                });
                imm.add(point10s.get(0));
            }
            System.err.println(finalAstr.size());
            System.err.println(maxp);

            imm.sort((o1, o2) -> o1.angle < o2.angle ? 1 : -1);
            imm.forEach(System.out::println);

            //lol cheating
            int cnt = imm.indexOf(new Point10(23, 17, 90));

            while (true) {
                final Point10 point10 = imm.get(cnt++ % imm.size());
                System.out.println(destroyed + " " + point10);
                destroyed++;
                if (destroyed == 200) {
                    System.out.println(point10.y + " " + point10.x);
                    return null;
                }
            }
        }

    }

}

class Point10 {
    int x;
    int y;
    double angle;

    public Point10(int x, int y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point10 point10 = (Point10) o;
        return
                Double.compare(point10.angle, angle) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(angle);
    }

    @Override
    public String toString() {
        return "Point10{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                '}';
    }
}