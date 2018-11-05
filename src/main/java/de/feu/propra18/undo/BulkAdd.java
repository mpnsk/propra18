package de.feu.propra18.undo;

import de.feu.propra18.hull.Point;
import de.feu.propra18.view.render.edge.RenderedEdge;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Fuegt der {@link Pane} gleich mehrere {@link RenderedEdge}s hinzu. Vergleiche {@link AddPoint}
 */
public class BulkAdd implements Undoable {

    private final List<RenderedEdge> renderedEdges;
    private final List<Point> points;
    private final Pane pane;

    public BulkAdd(List<RenderedEdge> renderedEdges, List<Point> points, Pane pane) {
        this.renderedEdges = renderedEdges;
        this.points = points;
        this.pane = pane;
    }

    @Override
    public void execute() {
        for (RenderedEdge edge : renderedEdges) {
            pane.getChildren().add(edge.getCircle());
            points.add(edge.getPoint());
        }
    }

    @Override
    public void undo() {
        for (RenderedEdge edge : renderedEdges) {
            pane.getChildren().remove(edge.getCircle());
            points.remove(edge.getPoint());
        }
    }

    @Override
    public void redo() {
        execute();
    }
}
