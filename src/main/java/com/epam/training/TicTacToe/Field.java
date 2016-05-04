package com.epam.training.TicTacToe;

import java.awt.Point;

public class Field {

    private Point coordinates;
    private int symbol;

    public Field(Point coordinates, int symbol) {
        super();
        this.coordinates = coordinates;
        this.symbol = symbol;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

}
