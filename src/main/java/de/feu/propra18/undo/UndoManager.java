package de.feu.propra18.undo;

import javafx.scene.control.Button;

import java.util.Stack;

/**
 * Verwalter von undo-redo-Funktionalitaet.
 * Jede undo-redo-bare Funktion ist ein {@link Undoable}.
 * Der UndoManager sollte der einzige Ansprechpartner mit Zugriff auf {@link Undoable}s sein.
 * Enthaelt direkt die {@link Button}s die den undo-redo verursachen. Das is zwar unsauber aber einfacher.
 */
public class UndoManager {
    private final Button undoButton;
    private final Button redoButton;
    private Runnable redrawGraph;
    private Stack<Undoable> undoStack;
    private Stack<Undoable> redoStack;

    public UndoManager(Runnable redrawGraph, Button undoButton, Button redoButton) {
        this.redrawGraph = redrawGraph;
        this.undoButton = undoButton;
        this.redoButton = redoButton;
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        updateButtons();
    }

    /**
     * Fuehrt einen {@link Undoable} aus und speichert ihn fuer spaeteren undo
     *
     * @param undoable Auszufuehrender {@link Undoable}
     */
    public void execute(Undoable undoable) {
        undoable.execute();
        undoStack.push(undoable);
        redoStack.clear();
        updateButtons();
        redrawGraph.run();
    }

    /**
     * Macht den letzten {@link Undoable} rueckgaengig
     */
    public void undo() {
        Undoable undoable = undoStack.pop();
        undoable.undo();
        redoStack.push(undoable);
        updateButtons();
        redrawGraph.run();
    }

    /**
     * Wiederholt den zuletzt rueckgaengig gemachten {@link Undoable}
     */
    public void redo() {
        Undoable undoable = redoStack.pop();
        undoable.redo();
        undoStack.push(undoable);
        updateButtons();
        redrawGraph.run();
    }

    /**
     * Disabled den {@link Button} sobald undo/redo nicht emhr moeglich ist
     */
    private void updateButtons(){
        undoButton.setDisable(undoStack.isEmpty());
        redoButton.setDisable(redoStack.isEmpty());
    }
}
