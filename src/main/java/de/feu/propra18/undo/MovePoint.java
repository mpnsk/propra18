package de.feu.propra18.undo;

import de.feu.propra18.hull.Point;
import de.feu.propra18.view.render.edge.RenderedEdge;

import java.math.BigInteger;

/**
 * Bewegt eine {@link RenderedEdge} auf der {@link javafx.scene.layout.Pane} von x1,y1 nach x2,y2
 * Die Umkehrung bewegt ihn wieder an seinen Ursprung.
 */
public class MovePoint implements Undoable {

    private final RenderedEdge renderedEdge;
    private final BigInteger[] oldXY;
    private final BigInteger[] newXY;

    public MovePoint(RenderedEdge point, BigInteger[] oldXY, BigInteger[] newXY) {
        this.renderedEdge = point;
        this.oldXY = oldXY;
        this.newXY = newXY;
    }

    @Override
    public void execute() {
        Point point = renderedEdge.getPoint();
        point.setX(newXY[0]);
        point.setY(newXY[1]);
        renderedEdge.setCircleToPoint();
    }

    @Override
    public void undo() {
        Point point = renderedEdge.getPoint();
        point.setX(oldXY[0]);
        point.setY(oldXY[1]);
        renderedEdge.setCircleToPoint();
    }

    @Override
    public void redo() {
        execute();
    }
}
