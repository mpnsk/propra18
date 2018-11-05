package de.feu.propra18.innercircle;

/**
 * Kreis im einfachsten Sinne. Fuer die berechnung des groessten Kreises enthalten in einem konvexen Polygon.
 */
public class InnerCircle implements Comparable<InnerCircle> {
    public double x, y, r;

    public InnerCircle(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    /**
     * Ordnet {@link InnerCircle} aufgrund seines Radius
     *
     * @param o anderer {@link InnerCircle}
     */
    @Override
    public int compareTo(InnerCircle o) {
        if (r < o.r) return -1;
        if (r > o.r) return 1;
        return 0;
    }
}
