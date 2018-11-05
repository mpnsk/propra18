package de.feu.propra18.innercircle;

public class Point {
    public double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {
    }

    /**
     * Abstand zwischen zwei karthesischen Punkten wie definiert durch den Satz des Pythagoras
     * (x1 − x2 )^2 + (y1 − y2)^2
     *
     * @param a
     * @param b
     * @return Abstand zwischen a und b
     */
    static double distance(Point a, Point b) {
        double deltaX = a.x - b.x;
        double deltaY = a.y - b.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Komfort-methode fuer den Abstand zwischen zwei Punkten
     * @param other Punkt als {@link Point}
     * @return Distanz als double
     */
    public double distance(Point other) {
        return Point.distance(this, other);
    }
}
