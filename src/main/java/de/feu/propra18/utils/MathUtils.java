package de.feu.propra18.utils;


import de.feu.propra18.hull.Point;

import java.math.BigInteger;

/**
 * Hilfsfunktionen zur Berechnung der konvexen Huelle
 */
public class MathUtils {

    /**
     * Berchnet ob die Punktefolge a-b-c eine Links- Rechtswendung beschreibt (vom Punkt a aus gesehen).
     *
     * @param a Perspektivischer Anfang
     * @param b Mitte
     * @param c Ende
     * @return positiv wenn c links von der Strecke a nach b, <br>
     * negativ wenn c rechts der Strecke a nach b, <br>
     * 0 wenn colinear
     */
    public static BigInteger links(Point a, Point b, Point c) {
        // A x (B y − C y ) + B x (C y − A y ) + C x (A y − B y )
        BigInteger axByCy = a.getX().multiply(b.getY().subtract(c.getY()));
        BigInteger bxCyAy = b.getX().multiply(c.getY().subtract(a.getY()));
        BigInteger cxAyBy = c.getX().multiply(a.getY().subtract(b.getY()));
        return axByCy.add(bxCyAy).add(cxAyBy);
    }

    /**
     * Berechnet die Quadratwurzel eines {@link BigInteger}
     * @param x als {@link BigInteger}
     * @return Quadratwurzel von x
     */
    public static BigInteger sqrt(BigInteger x) {
        BigInteger div = BigInteger.ZERO.setBit(x.bitLength() / 2);
        BigInteger div2 = div;
        // Loop until we hit the same value twice in a row, or wind
        // up alternating.
        for (; ; ) {
            BigInteger y = div.add(x.divide(div)).shiftRight(1);
            if (y.equals(div) || y.equals(div2))
                return y;
            div2 = div;
            div = y;
        }
    }
}
