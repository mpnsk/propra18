package de.feu.propra18.undo;

import de.feu.propra18.hull.Point;
import de.feu.propra18.view.render.edge.RenderedEdge;

import java.util.List;

/**
 * Entfernt einen {@link RenderedEdge} von der {@link javafx.scene.layout.Pane}.
 * Die Referenz muss fuer den Redo gehalten werden!
 */
public class DeletePoint implements Undoable {

    private final RenderedEdge edge;
    private final List<Point> points;

    public DeletePoint(RenderedEdge edge, List<Point> points) {
        this.edge = edge;
        this.points = points;
    }

    @Override
    public void execute() {
        edge.getPane().getChildren().remove(edge.getCircle());
        points.remove(edge.getPoint());
    }

    @Override
    public void undo() {
        edge.getPane().getChildren().add(edge.getCircle());
        points.add(edge.getPoint());
    }

    @Override
    public void redo() {
        execute();
    }
}
