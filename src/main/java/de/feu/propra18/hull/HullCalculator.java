package de.feu.propra18.hull;

import de.feu.propra18.innercircle.GreatedInnerCircleCalculator;
import de.feu.propra18.innercircle.InnerCircle;
import de.feu.propra18.interfaces.IHullCalculator;
import de.feu.propra18.persistence.Persistence;
import de.feu.propra18.utils.MathUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

/**
 * Berechnet die Konvexe Hülle und deren groessten Inkreis einer Punktemenge.
 * Gedacht als pures MVC Model ohne Zugriff auf View oder Controller
 */
public class HullCalculator implements IHullCalculator {
    private GreatedInnerCircleCalculator greatedInnerCircleCalculator = new GreatedInnerCircleCalculator();

    private List<Point> points = new ArrayList<>();
    private InnerCircle innerCircle;

    public List<Point> getPoints() {
        return points;
    }

    /**
     * Herz des Graham Scans. Umrandet eine vorsortierte Liste von Punkten durch stetes links-halten mit Backtracking.
     *
     * @param points Liste von perspektivisch vorsortierten Punkten
     * @return Konvexe Hülle als Stack
     */
    private Stack<Point> calculateHull(List<Point> points) {
        Stack<Point> stack = new Stack<>();
        int k1;
        for (k1 = 1; k1 < points.size(); k1++)
            if (!points.get(0).equals(points.get(k1))) break;
        if (k1 == points.size()) {
            stack.push(points.get(0));
            return stack;
        }
        int k2;
        for (k2 = k1 + 1; k2 < points.size(); k2++)
            if (MathUtils.links(points.get(0), points.get(k1), points.get(k2)).compareTo(BigInteger.ZERO) != 0) break;

        stack.push(points.get(0));
        stack.push(points.get(k2 - 1));

        for (int i = k2; i < points.size(); i++) {
            Point top = stack.pop();
            while (MathUtils.links(stack.peek(), top, points.get(i)).compareTo(BigInteger.ZERO) <= 0)
                top = stack.pop();
            stack.push(top);
            stack.push(points.get(i));
        }
        return stack;
    }

    /**
     * Sortiert Punkte nach dem Winkel zu einem genannten Referenzpunkt.
     * Sortierung ist aufsteigend von rechts nach links.
     *
     * @param reference Punkt von dem die Perspektive ausgeht
     * @param points    Liste von zu sortierenden Punkten
     */
    void sortRightToleftFromPerspective(Point reference, List<Point> points) {
        points.sort((o1, o2) -> {
            if (o1.equals(reference))
                return -1;
            if (o2.equals(reference))
                return 1;
            double atan1 = Math.atan2(
                    o1.getY().subtract(reference.getY()).longValueExact(),
                    o1.getX().subtract(reference.getX()).longValueExact());
            double atan2 = Math.atan2(
                    o2.getY().subtract(reference.getY()).longValueExact(),
                    o2.getX().subtract(reference.getX()).longValueExact());
            boolean pointsAreCollinear = Double.compare(atan1, atan2) == 0;
            if (pointsAreCollinear) {
                Function<Point, BigInteger> distanceToReference = point -> {
                    BigInteger deltaXSquare = reference.getX().subtract(point.getX()).pow(2);
                    BigInteger deltaYSquare = reference.getY().subtract(point.getY()).pow(2);
                    return MathUtils.sqrt(deltaXSquare.add(deltaYSquare));
                };
                return distanceToReference.apply(o1).compareTo(distanceToReference.apply(o2));
            }
            return Double.compare(atan1, atan2);
        });
    }

    /**
     * Findet den Punkt mit der niedrigsten y-Koordinate / Ordinate
     * Bei Gleichstand entscheidet der niedrigere x-Wert / Abszisse
     *
     * @param points Liste von Punkten
     * @return Point mit niedrigster Ordinate
     */
    Point getLowestYPoint(List<Point> points) {
        Point result = points.get(0);
        for (Point point : points) {
            boolean resultLowerThanPoint = point.getY().compareTo(result.getY()) < 0;
            if (resultLowerThanPoint)
                result = point;
            else {
                boolean pointYEqualsResultY = point.getY().compareTo(result.getY()) == 0;
                boolean pointXLowerResutX = point.getX().compareTo(result.getX()) < 0;
                if (pointYEqualsResultY && pointXLowerResutX)
                    result = point;
            }
        }
        return result;
    }

    /**
     * Führt den Graham Scan Algorithmus aus.
     * Abstrahiert in die Schritte: <br>
     * 1. niedgristen Punkt finden <br>
     * 2. Perspektivische Sortierung von diesem Punkt aus <br>
     * 3. Finden der Konvexen Hülle durch stetes links-halten <br>
     *
     * @return Konvexe Hülle als Stack
     */
    Stack<Point> grahamScan() {
        if (points.isEmpty()) return new Stack<>();
        Point lowestPoint = getLowestYPoint(points);
        sortRightToleftFromPerspective(lowestPoint, points);
        Stack<Point> stack = calculateHull(points);
        innerCircle = greatedInnerCircleCalculator.calculateCircle(new ArrayList<>(stack));
        return stack;
    }

    @Override
    public void addPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    @Override
    public void addPointsFromArray(int[][] pointArray) {
        List<Point> points = new ArrayList<>();
        for (int[] point : pointArray) {
            points.add(new Point(point[0], point[1]));
        }
    }

    @Override
    public void addPointsFromFile(String fileName) {
        points.addAll(Persistence.getInstance().loadPointsFromFile(fileName));
    }

    @Override
    public void clear() {
        points.clear();
        innerCircle = null;
    }

    @Override
    public int[][] getConvexHull() {
        if (points.isEmpty())
            return new int[0][0];
        Stack<Point> stack = grahamScan();
        int[][] result = new int[stack.size()][2];
        for (int i = 0; !stack.empty(); i++) {
            Point p = stack.pop();
            result[i][0] = p.getX().intValueExact();
            result[i][1] = p.getY().intValueExact();
        }
        return result;
    }

    /**
     * Komfort-methode. Gibt die Konvexe Huelle zurueck. Eigentliche Berechnung in {@link HullCalculator#grahamScan()}
     *
     * @return Konvexe Huelle als Liste von {@link Point}
     */
    public List<Point> getConvexHullAsList() {
        return new ArrayList<>(grahamScan());
    }

    @Override
    public double getGEKCenterX() {
        return innerCircle.x;
    }

    @Override
    public double getGEKCenterY() {
        return innerCircle.y;
    }

    @Override
    public double getGEKRadius() {
        return innerCircle.r;
    }

    public InnerCircle getInnerCircle() {
        return innerCircle;
    }

    @Override
    public String getEmail() {
        return "manuel.paunoski@studium.fernuni-hagen.de";
    }

    @Override
    public String getMatrNr() {
        return "q8909806";
    }

    @Override
    public String getName() {
        return "Manuel Paunoski";
    }

}
