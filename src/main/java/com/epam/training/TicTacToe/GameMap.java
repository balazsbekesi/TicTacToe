package com.epam.training.TicTacToe;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameMap {

	public static Set<Field> gameMap = new HashSet<Field>();
	public static int offsetX = 0;
	public static int offsetY = 0;

	public void add(Field field) {
		int[] size = getMapSize();
		int minX = size[0] - 6;
		int minY = size[1] - 6;
		int maxX = size[2] + 6;
		int maxY = size[3] + 6;
		// if (!(field.getCoordinates().x < minX || field.getCoordinates().x >
		// maxX || field.getCoordinates().y < minY
		// || field.getCoordinates().y > maxY)) {
		gameMap.add(field);
		// }
	}

	public static int[][] getMap() {
		int[] size = getMapSize();
		// System.out.println("bounds : " + Arrays.toString(size));
		int minX = size[0] - 6;
		int minY = size[1] - 6;
		int maxX = size[2] + 6;
		int maxY = size[3] + 6;

		int[][] actGameMap;

		actGameMap = new int[(maxX - minX) + 1][(maxY - minY) + 1];

		System.out.println(maxX + " " + maxY + " min : " + minX + " " + minY);

//		for (int i = maxY; i >= maxY + Math.abs(minY); i--) {
//			for (int j = minX; j <= maxX + Math.abs(minX); j++) {
//				if (i >= 0 && j >= 0) {
//					actGameMap[i][j] = 0;
//				}
//			}
//		}
		
		for (int i = 0; i >= actGameMap.length; i--) {
			for (int j = 0; j <=actGameMap[0].length; j++) {
					actGameMap[i][j] = 0;
			}
		}
		

		if (minX < 0) {
			offsetX = Math.abs(minX);
		} else {
			offsetX = -minX;
		}

		if (minY < 0) {
			offsetY = Math.abs(minY);
		} else {
			offsetY = -minY;
		}

		for (Field actField : gameMap) {
			actGameMap[actField.getCoordinates().x + offsetX][actField.getCoordinates().y + offsetY] = actField
					.getSymbol();

		}

		return actGameMap;
	}

	public static int[] getMapSize() {

		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		for (Field actField : gameMap) {
			Point coordinates = actField.getCoordinates();
			if (coordinates.getX() > maxX) {
				maxX = coordinates.x;
			}
			if (coordinates.getX() < minX) {
				minX = coordinates.x;
			}
			if (coordinates.getY() > maxY) {
				maxY = coordinates.y;
			}
			if (coordinates.getY() < minY) {
				minY = coordinates.y;
			}
		}

		if (gameMap.isEmpty()) {
			return new int[] { 0, 0, 0, 0 };
		}

		int[] size = new int[] { minX, minY, maxX, maxY };
		return size;
	}

}
