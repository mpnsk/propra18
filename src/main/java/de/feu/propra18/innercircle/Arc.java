package de.feu.propra18.innercircle;

import java.util.List;

/**
 * Kante eines Polygons definiert durch zwei Punkte a und b.
 */
public class Arc implements Comparable<Arc> {
    public Point a, b;
    public InnerCircle innerCircle;
    List<Arc> neighbours;

    /**
     * Vergleicht zwei Linien nach Groesse des Inkreises
     * den die Linie als Dreieck mit ihren Nachbarn im Polygon einschliesst. <br>
     *
     * @param o die andere Linie
     * @return -1 wenn diese Linie den kleineren Inkreis beschreibt, <br>
     * 1 wenn diese Linie den groesseren Inkreis beschreibt, <br>
     * 0 wenn die Kreise gleich gross sind.
     */
    @Override
    public int compareTo(Arc o) {
        innerCircle = calculateCircle();
        o.innerCircle = o.calculateCircle();
        if (innerCircle.r < o.innerCircle.r) return -1;
        if (innerCircle.r > o.innerCircle.r) return 1;
        return 0;

    }

    /**
     * Berechnet den Inkreis den die Linie mit ihren naechsten zwei Nachbarlinien einschliessen.
     *
     * @return den Inkreis
     */
    InnerCircle calculateCircle() {
        int index = neighbours.indexOf(this);
        int pre = neighbours.size() - 1;
        int preIndex = index == 0 ? pre : index - 1;
        int postIndex = index == pre ? 0 : index + 1;
        if (preIndex == postIndex) {
            innerCircle = new InnerCircle(a.x, a.y, 0);
            return innerCircle;
        }
        Arc preArc = neighbours.get(preIndex);
        Arc postArc = neighbours.get(postIndex);
        innerCircle = GreatedInnerCircleCalculator.calculate(preArc.a, preArc.b, a, b, postArc.a, postArc.b);
        return innerCircle;
    }

}
