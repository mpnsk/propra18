package de.feu.propra18.view.render.edge;

import de.feu.propra18.hull.Point;
import de.feu.propra18.undo.MovePoint;
import de.feu.propra18.undo.UndoManager;
import de.feu.propra18.undo.Undoable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.math.BigInteger;
import java.util.function.Consumer;

/**
 * View-Object zur Darstelung eines Punktes auf der Zeichenflaeche.
 * Verwaltet hauptsaechlich die Listeners fuer Bewegungen der Punkte
 */
public class RenderedEdge {
    private final Point point;
    private final Pane pane;
    private Circle circle;
    private double[] lastDragPosition = new double[2];
    private double[] dragEnter = new double[2];
    private boolean dragMovementCausedByRightClick = false;

    RenderedEdge(Pane pane, Point point) {
        this.pane = pane;
        this.point = point;
    }

    public Pane getPane() {
        return pane;
    }

    public Point getPoint() {
        return point;
    }

    public Circle getCircle() {
        return circle;
    }

    void setCircle(Circle circle) {
        this.circle = circle;
    }

    /**
     * Registriert alle noetigen Listener zur Bewegung eines Punktes in der Zechenebene
     *
     * @param undoManager      Verwalter fuer {@link Undoable} Punktebewegung
     * @param updateConvexHull {@link Runnable} der die Konvexe Huelle neu berechnet
     * @param removePoint      {@link Consumer< RenderedEdge >} der den Punkt als {@link Undoable} entfernt
     */
    public void makeDraggable(UndoManager undoManager, Runnable updateConvexHull, Consumer<RenderedEdge> removePoint) {
        Runnable userWantsToDeletePoint = () -> removePoint(updateConvexHull, removePoint);
        circle.setOnMousePressed(event -> {
            event.consume();
            dragEnter = new double[]{event.getX(), event.getY()};
            switch (event.getButton()) {
                case PRIMARY:
                    if (event.isControlDown()) {
                        userWantsToDeletePoint.run();
                    } else {
                        startDrag(event);
                    }
                    break;
                case SECONDARY:
                    userWantsToDeletePoint.run();
                    break;
            }
        });
        circle.setOnMouseDragged(event -> {
            renderEdgeAtNewPosition(updateConvexHull, event);
        });
        circle.setOnMouseReleased(event -> {
                    if (dragMovementCausedByRightClick) {
                        dragMovementCausedByRightClick = false;
                        return;
                    }
                    registerPointMove(undoManager);
                }
        );
    }

    /**
     * Registriert nach Beendung der Mausgeste die neue Position als {@link Undoable} Bewegung
     *
     * @param undoManager Verwalter fuer {@link Undoable} Punktebewegung
     */
    private void registerPointMove(UndoManager undoManager) {
        BigInteger[] newXY = {
                point.getX(),
                point.getY()};
        BigInteger[] oldXY = {
                BigInteger.valueOf((long) (dragEnter[0])),
                BigInteger.valueOf((long) (dragEnter[1]))};
        Undoable movePoint = new MovePoint(this, oldXY, newXY);
        undoManager.execute(movePoint);
    }

    /**
     * Zeichnet den {@link Circle} an einer neuen Position
     *
     * @param updateConvexHull {@link Runnable} der die Konvexe Huelle neu berechnet
     * @param event            {@link MouseEvent} mit neuen x/y Koordinaten
     */
    private void renderEdgeAtNewPosition(Runnable updateConvexHull, MouseEvent event) {
        int deltaX = (int) event.getX();
        int deltaY = (int) (lastDragPosition[1] - event.getSceneY());
        point.setX(deltaX);
        point.setY(deltaY);
        setCircleToPoint();
        updateConvexHull.run();
    }

    /**
     * Speichert x-/y-Koodinaten des Cursors um ein Punktbewegung zu registrieren
     *
     * @param event {@link MouseEvent} mit neuen x/y Koordinaten
     */
    private void startDrag(MouseEvent event) {
        lastDragPosition[0] = circle.getCenterX();
        lastDragPosition[1] = circle.getCenterY() + event.getSceneY();
    }

    /**
     * Loescht den Punkt von der Zeichenflaeche und ruft die notwendigen Handler auf
     *
     * @param updateConvexHull {@link Runnable} der die Konvexe Huelle neu berechnet
     * @param removePoint      {@link Consumer< RenderedEdge >} der den Punkt als {@link Undoable} entfernt
     */
    private void removePoint(Runnable updateConvexHull, Consumer<RenderedEdge> removePoint) {
        pane.getChildren().remove(circle);
        removePoint.accept(this);
        updateConvexHull.run();
//      haesslicher hack um eine Punktbewegung zu verhindern die durch jede Rechtsclick-Loeschung bewirkt wird
        dragMovementCausedByRightClick = true;
//      haesslicher hack ende
    }

    /**
     * Aktualisiert den Punkt in der Zeichenflaeche nach einem Bewegung
     */
    public void setCircleToPoint() {
        circle.setCenterX(point.getX().doubleValue());
        circle.setCenterY(point.getY().doubleValue());
    }

}
