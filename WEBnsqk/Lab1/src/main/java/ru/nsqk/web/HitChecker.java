package ru.nsqk.web;

/**
 * Геометрия:
 *  Triangle (I): x >= 0, y >= 0, x + y <= R/2
 *  QuarterCircle (II): x <= 0, y >= 0, x^2 + y^2 <= (R/2)^2
 *  Rectangle (III): x <= 0, y <= 0, -R/2 <= x <= 0, -R <= y <= 0
 *  IV: пусто
 */
public class HitChecker {

    public static boolean check(double x, double y, double r) {
        boolean tri = (x >= 0) && (y >= 0) && (x + y <= r / 2.0);
        boolean circle = (x <= 0) && (y >= 0) && (x * x + y * y <= (r * r) / 4.0);
        boolean rect = (x <= 0) && (y <= 0) && (x >= -r / 2.0) && (y >= -r);
        return tri || circle || rect;
    }
}