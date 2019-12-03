package com.ismailkuruca.aoc_2019;

import com.ismailkuruca.aoc_2019.util.FileUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Day3 {

    public static void main(String[] args) {
        final List<String> inputs1 = Arrays.stream(FileUtil.readFile("day3_1").get(0).split(",")).collect(Collectors.toList());
        final List<String> inputs2 = Arrays.stream(FileUtil.readFile("day3_2").get(0).split(",")).collect(Collectors.toList());


        part1(move1(inputs1), move1(inputs2));
        part2(move2(inputs1), move2(inputs2));
    }

    public static void part1(Set<Point> p1, Set<Point> p2) {
        p1.retainAll(p2);
        Point closest = null;
        for (Point p : p1) {
            if (closest == null || p.manhattanDistance() < closest.manhattanDistance()) {
                closest = p;
            }
        }

        assert closest != null;
        System.err.println(closest.manhattanDistance());
    }

    public static void part2(Map<Point, Integer> p1, Map<Point, Integer> p2) {
        Set<Point> s1 = p1.keySet();
        Set<Point> s2 = p2.keySet();
        s1.retainAll(s2);

        Point closest = null;
        int closestDistance = -1;
        for (Point p : s1) {
            int distance = p1.get(p) + p2.get(p);
            if (closest == null || distance < closestDistance) {
                closest = p;
                closestDistance = distance;
            }
        }

        System.err.println(closestDistance);
    }

    private static HashSet<Point> move1(List<String> input) {
        final HashSet<Point> points = new HashSet<>();
        Point point = new Point();
        for (String s : input) {
            char direction = s.charAt(0);
            int distance = Integer.parseInt(s.substring(1));
            switch (direction) {
                case 'L':
                    for (int i = 1; i <= distance; ++i) {
                        point = point.left();
                        points.add(point);
                    }
                    break;

                case 'R':
                    for (int i = 1; i <= distance; ++i) {
                        point = point.right();
                        points.add(right(point));
                    }
                    break;

                case 'U':
                    for (int i = 1; i <= distance; ++i) {
                        point = point.up();
                        points.add(point);
                    }
                    break;

                case 'D':
                    for (int i = 1; i <= distance; ++i) {
                        point = point.down();
                        points.add(point);
                    }
                    break;
            }
        }

        return points;
    }

    private static Map<Point, Integer> move2(List<String> input) {
        Map<Point, Integer> points = new HashMap<>();
        int moves = 0;
        Point point = new Point(0, 0);
        for (String s : input) {
            char direction = s.charAt(0);
            int distance = Integer.parseInt(s.substring(1));
            switch (direction) {
                case 'L':
                    for (int i = 1; i <= distance; ++i) {
                        point = left(point);
                        points.put(point, ++moves);
                    }
                    break;
                case 'R':
                    for (int i = 1; i <= distance; ++i) {
                        point = right(point);
                        points.put(point, ++moves);
                    }
                    break;
                case 'U':
                    for (int i = 1; i <= distance; ++i) {
                        point = up(point);
                        points.put(point, ++moves);
                    }
                    break;
                case 'D':
                    for (int i = 1; i <= distance; ++i) {
                        point = down(point);
                        points.put(point, ++moves);
                    }
                    break;
            }
        }
        return points;
    }

    private static Point right(Point p) {
        return new Point(p.x + 1, p.y);
    }

    private static Point left(Point p) {
        return new Point(p.x - 1, p.y);
    }

    private static Point up(Point p) {
        return new Point(p.x, p.y + 1);
    }

    private static Point down(Point p) {
        return new Point(p.x, p.y - 1);
    }

}

class Point {
    public int x, y;

    public Point() {
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int manhattanDistance() {
        return Math.abs(x) + Math.abs(y);
    }

    public Point right() {
        return new Point(x + 1, y);
    }

    public Point left() {
        return new Point(x - 1, y);
    }

    public Point up() {
        return new Point(x, y + 1);
    }

    public Point down() {
        return new Point(x, y - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}