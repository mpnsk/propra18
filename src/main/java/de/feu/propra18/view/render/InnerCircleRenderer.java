package de.feu.propra18.view.render;

import de.feu.propra18.innercircle.InnerCircle;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * Zeichnet, referenziert und loescht den Kreis in der Zeichenflaeche
 */
class InnerCircleRenderer {

    private final Pane pane;
    private Circle renderedCircle;

    InnerCircleRenderer(Pane pane) {
        this.pane = pane;
    }

    /**
     * Zeichnet den Kreis in die Zeichenflaeche
     *
     * @param innerCircle der zu zeichnende Kreis
     */
    void render(InnerCircle innerCircle) {
        if (renderedCircle != null) pane.getChildren().remove(renderedCircle);
        renderedCircle = new Circle(innerCircle.x, innerCircle.y, innerCircle.r);
        renderedCircle.setOpacity(0.25);
        renderedCircle.setMouseTransparent(true);
        pane.getChildren().add(renderedCircle);
    }

    /**
     * Entfernt den Kreis aus der Zeichenflaeche
     */
    public void clear() {
        pane.getChildren().remove(renderedCircle);
    }
}
