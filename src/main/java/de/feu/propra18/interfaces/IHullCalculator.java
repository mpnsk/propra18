package de.feu.propra18.interfaces;

import java.io.IOException;

public interface IHullCalculator {
    void addPointsFromFile(String var1) throws IOException;

    void addPointsFromArray(int[][] var1);

    void addPoint(int var1, int var2);

    void clear();

    String getMatrNr();

    String getName();

    String getEmail();

    int[][] getConvexHull();

    double getGEKCenterX();

    double getGEKCenterY();

    double getGEKRadius();
}
