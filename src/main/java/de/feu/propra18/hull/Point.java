package de.feu.propra18.hull;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Komfort-Klasse eines 2D Punktes mit {@link BigInteger} Koordinaten
 */
public class Point {
    private BigInteger x, y;

    public Point(int x, int y) {
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(x, point.x) &&
                Objects.equals(y, point.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public BigInteger getX() {
        return x;
    }

    public void setX(int x) {
        this.x = BigInteger.valueOf(x);
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(int y) {
        this.y = BigInteger.valueOf(y);
    }

    public void setY(BigInteger y) {
        this.y = y;
    }
}
