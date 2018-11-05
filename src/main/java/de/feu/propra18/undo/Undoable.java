package de.feu.propra18.undo;

/**
 * Schnittstelle eines umkehrbaren oder auch undo-redo Befehls. Implementiert nach dem Command-pattern
 */
public interface Undoable {

    /**
     * Fuehrt den Befehlt aus
     */
    void execute();

    /**
     * Macht den Befehlt rueckgaengig mit allem was dazu gehoert
     */
    void undo();

    /**
     * Fuehrt {@link #execute()} ein zweites mal aus, theoretisch ist dies der Platz um einen Cache zu implementieren
     */
    void redo();
}
