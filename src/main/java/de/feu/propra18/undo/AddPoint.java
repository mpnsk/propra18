package de.feu.propra18.undo;

import de.feu.propra18.hull.Point;
import de.feu.propra18.view.render.edge.RenderedEdge;

import java.util.List;

/**
 * Fuegt der {@link javafx.scene.layout.Pane} eine {@link RenderedEdge} hinzu.
 * Haelt direkte Referenz zur Liste der Punkte
 */
public class AddPoint implements Undoable {

    private final RenderedEdge renderedEdge;
    private final List<Point> points;

    public AddPoint(RenderedEdge point, List<Point> points) {
        this.renderedEdge = point;
        this.points = points;
    }

    @Override
    public void execute() {
        renderedEdge.getPane().getChildren().add(renderedEdge.getCircle());
        points.add(renderedEdge.getPoint());
    }

    @Override
    public void undo() {
        renderedEdge.getPane().getChildren().remove(renderedEdge.getCircle());
        points.remove(renderedEdge.getPoint());
    }

    @Override
    public void redo() {
        execute();
    }
}
