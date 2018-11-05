package de.feu.propra18.view.render;

import de.feu.propra18.hull.Point;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

/**
 * Verwaltet die Konvexer Huelle als Menge von Linien in der Zeichenflaeche
 */
class RenderedPolygon {

    private final Pane pane;
    private List<Line> lines = new ArrayList<>();

    RenderedPolygon(Pane pane) {
        this.pane = pane;
    }

    /**
     * Zeichnet eine neue Konvexe Huelle ale Menge von Linien in die Zeichenflaeche
     *
     * @param hull die Konvexe Huelle als Menge von Endpunkten
     */
    void render(List<Point> hull) {
        dropAllLines();

        if (!hull.isEmpty()) {
            Point first = hull.get(0);
            Point current = first;
            for (Point next : hull) {
                drawLine(current, next);
                current = next;
            }
            drawLine(current, first);
        }
    }

    /**
     * Entfernt die Konvexe Huelle von der Zeichenflaeche
     */
    private void dropAllLines() {
        for (Line l : lines) pane.getChildren().remove(l);
    }

    /**
     * Zeichnet eine einzelne Line
     * @param start Anfang der Linie als {@link Point}
     * @param end Ende der Linie als {@link Point}
     */
    private void drawLine(Point start, Point end) {
        Line line = new Line(
                start.getX().intValueExact(),
                start.getY().intValueExact(),
                end.getX().intValueExact(),
                end.getY().intValueExact()
        );
        line.setFill(Color.BLUE);
        line.setStrokeWidth(1);
        line.setStroke(Color.BLUE);
        line.setMouseTransparent(true);
        lines.add(line);
        pane.getChildren().add(line);
    }
}
