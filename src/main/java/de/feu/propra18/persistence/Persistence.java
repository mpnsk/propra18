package de.feu.propra18.persistence;

import de.feu.propra18.hull.Point;
import de.feu.propra18.utils.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Sorgt für das speichern und laden
 */
public class Persistence {
    private static Persistence instance;

    private Persistence() {
    }

    /**
     * Singleton Getter
     * @return Singleton Instanz
     */
    public static Persistence getInstance() {
        if (instance == null)
            instance = new Persistence();
        return instance;

    }

    /**
     * Schreibt Daten in eine Datei
     *
     * @param fileName Pfad der Datei
     * @param data     Daten als List von Strings
     */
    public void saveToFile(String fileName, List<String> data) {
        try {
            Path path = Paths.get(fileName);
            Files.write(path, data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Liest eine gewählte Datei und parst aus ihr eine Menge von Punkten
     *
     * @param fileName Pfad der Datei
     * @return Liste von Punkten
     */
    public List<Point> loadPointsFromFile(String fileName) {
        List<Point> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] split = line.split(" ");
                if (split.length == 2 && StringUtils.isNumeric(split[0]) && StringUtils.isNumeric(split[1])) {
                    int x = Integer.parseInt(split[0]);
                    int y = Integer.parseInt(split[1]);
                    list.add(new Point(x, y));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}