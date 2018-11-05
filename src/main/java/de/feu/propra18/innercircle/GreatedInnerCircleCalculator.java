package de.feu.propra18.innercircle;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Berechnet den groessten {@link InnerCircle} enthalten in einem konvexen Polygon
 */
public class GreatedInnerCircleCalculator {

    /**
     * Takes as argument 3 lines defined by 6 points.
     * Of course the lines have to meet somewhere but not necessarily in these points.
     *
     * @param a start first line
     * @param b end first line
     * @param c start second line
     * @param d end second line
     * @param e start third line
     * @param f end second line
     * @return {@link InnerCircle} contained within the three lines
     */
    static InnerCircle calculate(Point a, Point b, Point c, Point d, Point e, Point f) {
        double dAB = a.distance(b);
        double dCD = c.distance(d);
        double dEF = e.distance(f);

        double alpha1 = dAB * (d.y - c.y) - dCD * (b.y - a.y);
        double alpha2 = dCD * (f.y - e.y) - dEF * (d.y - c.y);

        double beta1 = dCD * (b.x - a.x) - dAB * (d.x - c.x);
        double beta2 = dEF * (d.x - c.x) - dCD * (f.x - e.x);

        double gamma1 = dAB * (c.x * (d.y - c.y) - c.y * (d.x - c.x))
                + dCD * (a.y * (b.x - a.x) - a.x * (b.y - a.y));
        double gamma2 = dCD * (e.x * (f.y - e.y) - e.y * (f.x - e.x))
                + dEF * (c.y * (d.x - c.x) - c.x * (d.y - c.y));

        double dH = alpha1 * beta2 - alpha2 * beta1;
        double dU = gamma1 * beta2 - gamma2 * beta1;
        double dV = alpha1 * gamma2 - alpha2 * gamma1;

        Point m = new Point();
        m.x = dU / dH;
        m.y = dV / dH;
        double r = ((a.x - m.x) * (b.y - a.y) + (a.y - m.y) * (a.x - b.x)) / dAB;

        InnerCircle innerCircle = new InnerCircle(m.x, m.y, r);
        return innerCircle;
    }

    /**
     * Looks for the {@link Arc} corresponding to the smallest {@link InnerCircle} contained in the
     * polygon defined by the list of points and removes it.
     * With all limiting Arcs removed the method returns the largest InnerCircle.
     *
     * @param points defining a polygon
     * @return greatest InnerCircle contained in that polygon
     */
    InnerCircle arcElimination(List<Point> points) {
        if (points.size() == 0) return null;
        if (points.size() == 1) return new InnerCircle(points.get(0).x, points.get(0).y, 0);
        List<Arc> arcs = pointsToArcs(points);

        while (arcs.size() > 3) {
            Arc min = Collections.min(arcs);
            arcs.remove(min);
        }

        return arcs.get(0).calculateCircle();
    }

    /**
     * Konvertiert eine Liste von {@link Point} zur Liste vom Typ {@link Arc}
     * Bequemlichkeitsmethode um Arc auf dieses Paket zu beschraenken
     *
     * @param points Liste von Point
     * @return Liste von  Arc
     */
    private List<Arc> pointsToArcs(List<Point> points) {
        List<Arc> arcs = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Arc arc = new Arc();
            arc.neighbours = arcs;
            arc.a = points.get(i);
            int nextIndex = i + 1 >= points.size() ? 0 : i + 1;
            arc.b = points.get(nextIndex);
            arcs.add(arc);
        }
        return arcs;
    }

    /**
     * Nach aussen gerichtete Methode fuer Typkonvertierung
     *
     * @param points
     * @return
     */
    public InnerCircle calculateCircle(List<de.feu.propra18.hull.Point> points) {
        return arcElimination(
                points.stream()
                        .map(bigIntegerPoint -> new Point(
                                bigIntegerPoint.getX().doubleValue(),
                                bigIntegerPoint.getY().doubleValue()))
                        .collect(Collectors.toList()));
    }
}
