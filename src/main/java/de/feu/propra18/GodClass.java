package de.feu.propra18;

import de.feu.propra18.hull.HullCalculator;
import de.feu.propra18.hull.Point;
import de.feu.propra18.persistence.Persistence;
import de.feu.propra18.undo.UndoManager;
import de.feu.propra18.view.render.RenderingManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Nimmt Input der Benutzeroberfläche an. Verwaltet die Darstellung der Konvexen Hülle. <br>
 * Gedacht als MVC Controller
 */
public class GodClass {
    public Button undo, redo;
    @FXML
    Pane pane;
    private HullCalculator hullCalculator = new HullCalculator();
    private File file;
    private UndoManager undoManager;
    private RenderingManager renderingManager;

    /**
     * JavaFX Konstruktor, musss statt Java-Konstruktor verwendet werden.
     */
    public void initialize() {
        undoManager = new UndoManager(this::redrawGraph, undo, redo);
        renderingManager = new RenderingManager(pane, this::redrawGraph, undoManager, hullCalculator.getPoints());
    }

    /**
     * Nimmt Mausklick entgegen, lässt an der Stelle des Klicks einen Punkt eintragen und zeichnen.
     * Ctrl-klicks sollen Punkte loeschen und werden deshalb ignoriert
     *
     * @param mouseEvent
     */
    public void createPointByMouse(MouseEvent mouseEvent) {
        if (mouseEvent.isControlDown()) return;
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();
        drawPointAt(x, y);
    }

    /**
     * Zeichnet einen Punkt in der Ebene
     *
     * @param x Abszissenwert
     * @param y Ordinatenwert
     */
    private void drawPointAt(int x, int y) {
        Point point = new Point(x, y);
        renderingManager.add(point);
        redrawGraph();
    }


    public void savePoints(ActionEvent actionEvent) {
        if (file != null) save(hullCalculator.getPoints());
        else savePointsTo(actionEvent);
    }

    /**
     * Speichert die aktuellen Punkte in einer vom Nutzer ausgewählten Datei
     *
     * @param actionEvent
     */
    public void savePointsTo(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Speichern unter:");
        file = fileChooser.showSaveDialog(pane.getScene().getWindow());
        save(hullCalculator.getPoints());
    }

    private void save(List<Point> points) {
        List<String> pointsAsString = points.stream().map(point -> point.getX() + " " + point.getY()).collect(Collectors.toList());
        Persistence.getInstance().saveToFile(file.getAbsolutePath(), pointsAsString);
    }

    /**
     * Läd Punkte aus einer vom Nutzer ausgewählten Datei
     *
     * @param actionEvent
     */
    public void loadPoints(ActionEvent actionEvent) {
        clear(actionEvent);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Datei laden:");
        FileChooser.ExtensionFilter point_file = new FileChooser.ExtensionFilter("Point file", "*.points");
        FileChooser.ExtensionFilter any_file = new FileChooser.ExtensionFilter("any file", "*");
        fileChooser.getExtensionFilters().addAll(point_file, any_file);
        String absolutePath = fileChooser.showOpenDialog(pane.getScene().getWindow()).getAbsolutePath();
        hullCalculator.addPointsFromFile(absolutePath);
        redrawGraph();
    }

    /**
     * Erstellt eine Menge von zufällig platzierten Punkten in der Ebene
     *
     * @param n Anzahl an Punkten
     */
    private void createRandomPoint(int n) {
        List<Point> buffer = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = (int) (Math.random() * pane.getWidth());
            int y = (int) (Math.random() * pane.getHeight());
            buffer.add(new Point(x, y));
        }
        renderingManager.bulkAdd(buffer);
    }

    public void createRandomPoints10(ActionEvent actionEvent) {
        createRandomPoint(10);
    }

    public void createRandomPoints50(ActionEvent actionEvent) {
        createRandomPoint(50);
    }

    public void createRandomPoints100(ActionEvent actionEvent) {
        createRandomPoint(100);
    }

    public void createRandomPoints500(ActionEvent actionEvent) {
        createRandomPoint(500);
    }

    public void createRandomPoints1000(ActionEvent actionEvent) {
        createRandomPoint(1000);
    }

    /**
     * Erstellt und zeigt einen Hilfe-Dialog der die Benuztung des Programmes beschreibt
     *
     * @param actionEvent
     */
    public void showHelp(ActionEvent actionEvent) {
        Window window = pane.getScene().getWindow();


        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(window);
        VBox dialogVbox = new VBox();
        String helpText =
                "Punkte werden hinzugefügt durch Mausklick. \n" +
                        "Entfernen der Punkte durch Rechtsklick oder Ctrl-Click. \n" +
                        "Zum verschieben der Punkte die linke Maustaste gedrückt halten, " +
                        "die Konvexe Huelle wird automatisch neu berechnet!\n" +
                        "\n" +
                        "Verschiebung, Hinzufuegen und Loeschen von Punkten kann mit Hilfe von" +
                        " \"Undo\" und \"Redo\" rueckgaengig gemacht werden." +
                        "";

        dialogVbox.getChildren().add(new Text(helpText));
        Button ok = new Button("ok");
        ok.setOnAction(event -> dialog.close());
        dialogVbox.getChildren().add(ok);
        Scene dialogScene = new Scene(dialogVbox);
        dialog.setTitle("Hilfe");
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Loescht alle Punkte und laesst die Zeichenebene neu machen
     *
     * @param actionEvent
     */
    public void clear(ActionEvent actionEvent) {
        hullCalculator.clear();
        renderingManager.completeClear();
        redrawGraph();
    }

    /**
     * Zeichnet die konvexe Hülle als Polygon in die Ebene
     */
    private void redrawGraph() {
        renderingManager.render(hullCalculator.getConvexHullAsList(), hullCalculator.getInnerCircle());
    }

    /**
     * Nimmt Nutzer-Aktion "undo" entgegen
     */
    public void undo(ActionEvent actionEvent) {
        undoManager.undo();
    }

    /**
     * Nimmt Nutzer-Aktion "redo" entgegen
     */
    public void redo(ActionEvent actionEvent) {
        undoManager.redo();
    }
}
