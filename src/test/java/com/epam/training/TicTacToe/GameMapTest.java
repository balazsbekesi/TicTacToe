package com.epam.training.TicTacToe;

import java.awt.Point;

public class GameMapTest {

    public static void main(String[] args) {
        GameMap gameMap = new GameMap();

        gameMap.add(new Field(new Point(-20, -10), 1));
        gameMap.add(new Field(new Point(-10, 1), 1));
        gameMap.add(new Field(new Point(-5, 5), 1));
        gameMap.add(new Field(new Point(12, 14), 1));
        gameMap.add(new Field(new Point(6, 8), 1));
        gameMap.add(new Field(new Point(9, -20), 2));
        gameMap.add(new Field(new Point(23, 2), 2));
        gameMap.add(new Field(new Point(4, 25), 2));
        gameMap.add(new Field(new Point(-6, -8), 2));
        gameMap.add(new Field(new Point(6, -8), 2));

        int[][] gameField = GameMap.getMap();

        printMap(gameField);
    }

    public static void printMap(int[][] gameField) {
        System.out.print("  ");
        for (int i = 0; i < gameField.length; i++) {
            System.out.print(" " + i % 10);
        }
        System.out.println();

        for (int i = 0; i < gameField.length; i++) {
            System.out.print(i % 10 + " ");
            for (int j = 0; j < gameField[0].length; j++) {
                if (gameField[i][j] == 1) {
                    System.out.print(" O");
                }

                if (gameField[i][j] == 2) {
                    System.out.print(" X");
                }

                if (gameField[i][j] == 0) {
                    System.out.print(" .");
                }

            }
            System.out.println();
        }
    }

}
