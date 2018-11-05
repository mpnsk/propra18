package de.feu.propra18.view.render.edge;

import de.feu.propra18.hull.Point;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

/**
 * Hilfsklasse zum malen der {@link RenderedEdge}s
 */
public class EdgeRenderer {

    private final Pane pane;
    private List<Circle> circles = new ArrayList<>();

    public EdgeRenderer(Pane pane) {
        this.pane = pane;
    }

    /**
     * Erstellt eine {@link RenderedEdge} aus einem {@link Point}
     *
     * @param point
     * @return
     */
    public RenderedEdge create(Point point) {
        RenderedEdge renderedEdge = new RenderedEdge(pane, point);
        renderedEdge.setCircle(createCircle(point));
        return renderedEdge;
    }

    /**
     * generiert einen {@link Circle} aus den Koordinaten des {@link Point}
     *
     * @param p {@link Point} mit x- und y-Koordinaten
     * @return {@link Circle} mit den Koodinaten des {@link Point}
     */
    private Circle createCircle(Point p) {
        Circle circle = new Circle();
        double x = p.getX().doubleValue();
        circle.setCenterX(x);
        double y = p.getY().doubleValue();
        circle.setCenterY(y);
        circle.setRadius(10);
        circles.add(circle);
        return circle;
    }

    /**
     * Entfernt alle Kreise von der Zeichenflaeche
     */
    public void clear() {
        pane.getChildren().removeAll(circles);
    }

}
