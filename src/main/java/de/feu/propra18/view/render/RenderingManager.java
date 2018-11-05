package de.feu.propra18.view.render;

import de.feu.propra18.hull.Point;
import de.feu.propra18.innercircle.InnerCircle;
import de.feu.propra18.undo.AddPoint;
import de.feu.propra18.undo.BulkAdd;
import de.feu.propra18.undo.DeletePoint;
import de.feu.propra18.undo.UndoManager;
import de.feu.propra18.view.render.edge.EdgeRenderer;
import de.feu.propra18.view.render.edge.RenderedEdge;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Schnittpunkt zwischen GodClass und View.
 * Verwaltet die graphische Darstellung nach logischen Aenderungen in der Oberflaeche
 */
public class RenderingManager {

    private final InnerCircleRenderer innerCircleRenderer;
    private final RenderedPolygon renderedPolygon;
    private final EdgeRenderer edgeFactory;
    private final Runnable redrawGraph;
    private final UndoManager undoManager;
    private final Pane pane;
    private final List<Point> points;

    public RenderingManager(Pane pane, Runnable redrawGraph, UndoManager undoManager, List<Point> points) {
        this.innerCircleRenderer = new InnerCircleRenderer(pane);
        this.renderedPolygon = new RenderedPolygon(pane);
        this.edgeFactory = new EdgeRenderer(pane);
        this.redrawGraph = redrawGraph;
        this.undoManager = undoManager;
        this.pane = pane;
        this.points = points;
    }

    /**
     * Zeichnet die Konvexe Huelle mit groesstem Inkreis in die Zeichenflaeche
     *
     * @param hull   Konvxe Huelle als Stapel
     * @param innerCircle Inkreis, kann Null sein
     */
    public void render(List<Point> hull, InnerCircle innerCircle) {
        renderedPolygon.render(hull);
        if (innerCircle != null) innerCircleRenderer.render(innerCircle);
    }

    /**
     * Fuegt einen Punkt der Zeichenflaeche hinzu
     *
     * @param point
     */
    public void add(Point point) {
        RenderedEdge renderedEdge = edgeFactory.create(point);
        renderedEdge.makeDraggable(undoManager, redrawGraph, (edge) -> undoManager.execute(new DeletePoint(edge, points)));
        undoManager.execute(new AddPoint(renderedEdge, points));
    }


    /**
     * Fuegt eine Menge an Punkten der Zeichenflaeche hinzu.
     * Gesondert von {@link #add} um undo-{@link BulkAdd} zu nutzen
     *
     * @param buffer Liste an Punkten
     */
    public void bulkAdd(List<Point> buffer) {
        List<RenderedEdge> renderedEdges = buffer.stream().map(point -> {
            RenderedEdge renderedEdge = edgeFactory.create(point);
            renderedEdge.makeDraggable(undoManager, redrawGraph,
                    (edge) -> undoManager.execute(new DeletePoint(edge, points)));
            return renderedEdge;
        }).collect(Collectors.toList());
        undoManager.execute((new BulkAdd(renderedEdges, points, pane)));
    }

    /**
     * Kompletter Clear
     * Loescht alle Punkte, die Konvexe Huelle und den Kreis von der Zeichenflaeche
     */
    public void completeClear() {
        edgeFactory.clear();
        innerCircleRenderer.clear();
        redrawGraph.run();
    }
}
