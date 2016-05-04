package com.epam.training.TicTacToe;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Strategy {

	private int[][] map;
	private final int EMPTY = 0;
	private final int ME = 1;
	private final int ENEMY = 2;

	private static final int ONE_STEP_TO_WIN_PREF = 10_500_000;
	private static final int ONE_STEP_TO_WIN = 3_500_000;
	private static final int ONE_STEP_TO_WIN_LOWER = 1_000_000;

	private static final int TWO_STEP_TO_WIN_PREF = 210_000;
	private static final int TWO_STEP_TO_WIN = 200_000;
	private static final int TWO_STEP_TO_WIN_LOWER = 10_000;

	private static final int THREE_STEP_TO_WIN_PREF = 1_500;
	private static final int THREE_STEP_TO_WIN = 1_000;
	private static final int FOUR_STEP_TO_WIN = 0;
	// private int[][] map;

	private Point enemyLastMove;

	public Point getEnemyLastMove() {
		return enemyLastMove;
	}

	public void setEnemyLastMove(Point enemyLastMove) {
		this.enemyLastMove = enemyLastMove;
	}

	private final static int TEST = 0;

	GameMap myMap;

	public Strategy(GameMap map) {
		myMap = map;
	}

	public Point nextStep() {

		map = myMap.getMap();
		printMap();

		if (firstRound()) {
			return new Point(15, 15);
		}

		if (secondRound()) {
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					if (map[i][j] != 0) {
						Random r = new Random();
						int rand = r.nextInt(10) + 1;

						switch (rand) {
						case 1:
							return new Point(i - 1, j - 1);
						case 2:
							return new Point(i, j - 1);
						case 3:
							return new Point(i + 1, j - 1);

						case 4:
							return new Point(i + 1, j);
						case 5:
							return new Point(i - 1, j);

						case 6:
							return new Point(i, j + 1);
						case 7:
							return new Point(i + 1, j + 1);
						case 8:
							return new Point(i - 1, j + 1);

						case 9:
							return new Point(i + 2, j + 1);
						case 10:
							return new Point(i - 2, j - 1);
						}

						return new Point(i + 1, j);
					}
				}
			}
		}

		int enemyStateBeforeWePick = calculateState(ENEMY, ME);
		int myStateBeforeWePick = calculateState(ME, ENEMY);

		//

		System.out.println("enemy state before pick : " + enemyStateBeforeWePick);
		System.out.println("ny state before pick : " + myStateBeforeWePick);

		// if (enemyStateBeforeWePick < ONE_STEP_TO_WIN_PREF) {
		//// if (enemyStateBeforeWePick >= TWO_STEP_TO_WIN) {
		// for (int i = 0; i < map.length; i++) {
		// for (int j = 0; j < map[0].length; j++) {
		// if (map[i][j] == EMPTY) {
		// map[i][j] = ENEMY;
		// int calculateStateEnemy = calculateState(ENEMY, ME);
		//
		// if (calculateStateEnemy >= TWO_STEP_TO_WIN*2) {
		// System.out.println(i+" . "+j);
		// return new Point(i, j);
		// }
		// map[i][j] = EMPTY;
		//
		// }
		// }
		// }
		//// }
		// }
		// //

		System.out.println("Enemys state : " + enemyStateBeforeWePick + " bound is : " + TWO_STEP_TO_WIN);
		System.out.println("Our state : " + myStateBeforeWePick + " bound is : " + ONE_STEP_TO_WIN_LOWER);

		// if (enemyStateBeforeWePick >= TWO_STEP_TO_WIN && (myStateBeforeWePick
		// < ONE_STEP_TO_WIN_LOWER)) {
		if (enemyStateBeforeWePick >= TWO_STEP_TO_WIN && (myStateBeforeWePick < ONE_STEP_TO_WIN_LOWER)) {
			System.out.println("Defend!");
			ArrayList<Point> possiblePositions = new ArrayList<>();

			int maxStateToEnemy = enemyStateBeforeWePick;
			Point ruinPoint = new Point(0, 0);

			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[0].length; j++) {
					if (map[i][j] == EMPTY) {
						map[i][j] = ENEMY;
						int calculateStateEnemy = calculateState(ENEMY, ME);

						if (calculateStateEnemy > maxStateToEnemy) {
							maxStateToEnemy = calculateStateEnemy;
							ruinPoint = new Point(i, j);
						}
						map[i][j] = EMPTY;
					}
				}
			}

			if (maxStateToEnemy >= TWO_STEP_TO_WIN * 2) {
				System.out.println("Ruin defense");
				return ruinPoint;
			}

			int myBestState = 0;
			Point optimalPoint = new Point(0, 0);

			for (Point actualPoint : possiblePositions) {
				map[actualPoint.x][actualPoint.y] = ME;
				int myNewState = calculateState(ME, ENEMY);

				if (myNewState > myBestState) {
					myBestState = myNewState;
					optimalPoint = actualPoint;
				}

				map[actualPoint.x][actualPoint.y] = EMPTY;
			}

			if (optimalPoint.x == 0 && optimalPoint.y == 0) {
				int min = Integer.MAX_VALUE;
				Point minPoint = new Point(0, 0);

				for (int i = TEST; i < map.length - TEST; i++) {
					for (int j = TEST; j < map[0].length - TEST; j++) {
						if (map[i][j] == EMPTY) {
							map[i][j] = ME;

							int enemyNewState = calculateState(ENEMY, ME);

							if (enemyNewState < min) {
								min = enemyNewState;
								minPoint = new Point(i, j);
								possiblePositions.add(new Point(i, j));
							}
							map[i][j] = EMPTY;
						}
					}
				}
				return minPoint;
			}
			return optimalPoint;
		}

		int maximum = 0;
		Point maximumPoint = new Point();


		int maxStateToEnemy = enemyStateBeforeWePick;
		int maxMyStateNow = myStateBeforeWePick;

		Point ruinPoint = new Point(0, 0);

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == EMPTY) {
					map[i][j] = ENEMY;
					int calculateStateEnemy = calculateState(ENEMY, ME);

					if (calculateStateEnemy > maxStateToEnemy) {
						maxStateToEnemy = calculateStateEnemy;
						ruinPoint = new Point(i, j);
					}
					map[i][j] = EMPTY;
					map[i][j] = ME;

					int calculateStateMe = calculateState(ME, ENEMY);
					if (calculateStateMe > maxMyStateNow) {
						maxMyStateNow = calculateStateMe;
					}
					map[i][j] = EMPTY;
				}
			}
		}

		if (maxStateToEnemy > maxMyStateNow) {
			if (maxStateToEnemy >= TWO_STEP_TO_WIN * 2) {
				System.out.println("Ruin defense");
				return ruinPoint;
			}
		}
		
		System.out.println("Attack!");
		ArrayList<Point> possiblePositions = new ArrayList<>();
		for (int i = TEST; i < map.length - TEST; i++) {
			for (int j = TEST; j < map[0].length - TEST; j++) {
				if (map[i][j] == EMPTY) {
					map[i][j] = ME;

					boolean isDeadLine = false;
					if (!isDeadLine) {
						int myNewState = calculateState(ME, ENEMY);
						if (myNewState > maximum) {
							maximum = myNewState;

							maximumPoint.x = i;
							maximumPoint.y = j;

							possiblePositions.clear();
							possiblePositions.add(maximumPoint);
						} else if (myNewState == maximum) {
							possiblePositions.add(maximumPoint);
						}
						map[i][j] = EMPTY;
					} else {
						map[i][j] = EMPTY;
					}
				}
			}
		}
		int enemyWorstState = enemyStateBeforeWePick;
		Point optimalPoint = maximumPoint;
		System.out.println("Possible attack steps : " + possiblePositions.size());

		for (Point actualPoint : possiblePositions) {
			map[actualPoint.x][actualPoint.y] = ME;
			int enemyNewState = calculateState(ENEMY, ME);
			map[actualPoint.x][actualPoint.y] = EMPTY;

			if (enemyNewState <= enemyWorstState) {
				enemyWorstState = enemyNewState;
				optimalPoint = actualPoint;
			}
		}
		return optimalPoint;

	}

	private boolean firstRound() {
		return stepIndex() == 0;
	}

	private boolean secondRound() {
		return stepIndex() == 1;
	}

	private int[][] createNewMap() {
		int[][] copyMap = new int[map.length][map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				copyMap[i][j] = map[i][j];
			}
		}
		return copyMap;
	}

	private int stepIndex() {
		int elements = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] != 0) {
					elements++;
				}
			}
		}
		return elements;
	}

	public int calculateState(int myToe, int enemyToe) {

		int state = 0;

		for (int i = 0; i < map.length - 4; i++) {
			for (int j = 0; j < map[0].length - 4; j++) {
				if (isWinMove(myToe, i, j)) {
					return Integer.MAX_VALUE;
				}

				if (isDoubleThreeThreat(myToe, i, j) >= 2) {
					state += TWO_STEP_TO_WIN;
					if (myToe == ME) {
						return TWO_STEP_TO_WIN_PREF;
					}
				}

				int countOpenGridThree = countOpenGridThree(myToe, i, j);

				if (countOpenGridThree == 1) {
					state += THREE_STEP_TO_WIN;
				}
				if (countOpenGridThree >= 2) {
					state += TWO_STEP_TO_WIN_PREF;
				}

			}
		}

		int[] points = new int[9];

		points[0] = countOneStepsToWinStatesPreferred(myToe) * ONE_STEP_TO_WIN_PREF; // open
		points[1] = countOneStepsToWinStates(myToe, enemyToe) * ONE_STEP_TO_WIN; // closed
		points[2] = countOneStepsToWinLowerPiority(myToe) * ONE_STEP_TO_WIN_LOWER; // grid

		points[3] = countTwoStepsToWinStatesPref(myToe) * TWO_STEP_TO_WIN_PREF; // open
		if(points[3]>=2){
			state+=ONE_STEP_TO_WIN_LOWER;
		}
		
		points[4] = countTwoStepsToWinStates(myToe) * TWO_STEP_TO_WIN; // open 3
		points[5] = countClosedThrees(myToe, enemyToe) * TWO_STEP_TO_WIN_LOWER;

		points[6] = countOpenThreeStepsStates(myToe) * THREE_STEP_TO_WIN_PREF;
		points[7] = countOpenFourStepsStates(myToe) * FOUR_STEP_TO_WIN;

		for (int i = 0; i < points.length; i++) {
			state += points[i];
		}

		return state;
	}

	public boolean isWinMove(int myToe, int x, int y) {

		if (map[x][y + 1] == myToe && map[x][y + 2] == myToe && map[x][y + 3] == myToe && map[x][y + 4] == myToe
				&& map[x][y] == myToe)
			return true;
		if (map[x + 1][y] == myToe && map[x + 2][y] == myToe && map[x + 3][y] == myToe && map[x + 4][y] == myToe
				&& map[x][y] == myToe)
			return true;
		if (map[x + 1][y + 1] == myToe && map[x + 2][y + 2] == myToe && map[x + 3][y + 3] == myToe
				&& map[x + 4][y + 4] == myToe && map[x][y] == myToe)
			return true;
		if (x - 4 >= 0) {
			if (map[x - 1][y + 1] == myToe && map[x - 2][y + 2] == myToe && map[x - 3][y + 3] == myToe
					&& map[x - 4][y + 4] == myToe && map[x][y] == myToe)
				return true;
		}
		return false;
	}

	public int isDoubleThreeThreat(int myToe, int x, int y) {
		int threats = 0;

		if (map[x][y] == EMPTY) {
			// jobbra
			if (map[x][y + 1] == myToe && map[x][y + 2] == myToe && map[x][y + 3] == EMPTY) {
				threats++;
			}
			// balra
			if (y - 3 >= 0) {
				if (map[x][y - 1] == myToe && map[x][y - 2] == myToe && map[x][y - 3] == EMPTY) {
					threats++;
				}
			}
			// le
			if (map[x + 1][y] == myToe && map[x + 2][y] == myToe && map[x + 3][y] == EMPTY) {
				threats++;
			}
			// fel
			if (x - 3 >= 0) {
				if (map[x - 1][y] == myToe && map[x - 2][y] == myToe && map[x - 3][y] == EMPTY) {
					threats++;
				}
			}
			// 4
			if (map[x + 1][y + 1] == myToe && map[x + 2][y + 2] == myToe && map[x + 3][y + 3] == EMPTY) {
				threats++;
			}

			if (x - 3 >= 0 && y - 3 >= 0) {
				if (map[x - 1][y - 1] == myToe && map[x - 2][y - 2] == myToe && map[x - 3][y - 3] == EMPTY) {
					threats++;
				}
			}

			if (x - 3 >= 0) {
				if (map[x - 1][y + 1] == myToe && map[x - 2][y + 2] == myToe && map[x - 3][y + 3] == EMPTY) {
					threats++;
				}
			}

			if (y - 3 >= 0) {
				if (map[x + 1][y - 1] == myToe && map[x + 2][y - 2] == myToe && map[x + 3][y - 3] == EMPTY) {
					threats++;
				}
			}

		}

		return threats;
	}

	public int countOpenGridThree(int myToe, int x, int y) {
		int count = 0;

		if (map[x][y] == EMPTY) {

			if (x - 2 >= 0 && map[x - 1][y] == myToe && map[x + 1][y] == myToe && map[x - 2][y] == EMPTY
					&& map[x + 2][y] == EMPTY) {
				count++;
			}

			if (y - 2 >= 0 && map[x][y - 1] == myToe && map[x][y + 1] == myToe && map[x][y - 2] == EMPTY
					&& map[x][y + 2] == EMPTY) {
				count++;
			}

			if (x - 2 >= 0 && y - 2 >= 0 && map[x - 1][y - 1] == myToe && map[x + 1][y + 1] == myToe
					&& map[x - 2][y - 2] == EMPTY && map[x + 2][y + 2] == EMPTY) {
				count++;
			}

			if (x - 2 >= 0 && y - 2 >= 0 && map[x - 1][y + 1] == myToe && map[x + 1][y - 1] == myToe
					&& map[x - 2][y + 2] == EMPTY && map[x + 2][y - 2] == EMPTY) {
				count++;
			}

		}

		return count;
	}

	private int findClosedDiagonal(int myToe, int otherToe, int x, int y, int rangeParam) {

		int continousFields = 0;
		int range = rangeParam + 1;
		if (map[x][y] == EMPTY) {
			for (int i = 1; i < range; i++) {
				if (map[x + i][y + i] == myToe) {
					continousFields++;
				} else {
					break;
				}
			}
			if (continousFields == rangeParam && map[x + range][y + range] == otherToe) {
				return 1;
			}
		} else {
			if (map[x][y] == otherToe) {
				for (int i = 1; i < range; i++) {
					if (map[x + i][y + i] == myToe) {
						continousFields++;
					} else {
						break;
					}
				}
				if (continousFields == rangeParam && map[x + range][y + range] == EMPTY) {

					return 1;
				}
			}
		}

		return 0;
	}

	private int findClosedDiagonalMajor(int myToe, int otherToe, int x, int y, int rangeParam) {

		int continousFields = 0;
		int range = rangeParam + 1;
		if (map[x][y] == EMPTY) {
			for (int i = 1; i < range && x - i >= 0; i++) {
				if (map[x - i][y + i] == myToe) {
					continousFields++;
				} else {
					return 0;
				}
			}
			if (continousFields == rangeParam && x - range >= 0 && map[x - range][y + range] == otherToe) {
				return 1;
			}
		} else {
			if (map[x][y] == otherToe) {
				for (int i = 1; i < range && x - i >= 0; i++) {
					if (map[x - i][y + i] == myToe) {
						continousFields++;
					} else {
						break;
					}
				}
				if (continousFields == rangeParam && map[x - range][y + range] == EMPTY) {
					return 1;
				}
			}
		}

		return 0;
	}

	private int findClosedVertical(int myToe, int otherToe, int x, int y, int rangeParam) {
		int continousFields = 0;
		int range = rangeParam + 1;
		if (map[x][y] == EMPTY) {
			for (int i = 1; i < range; i++) {
				if (map[x][y + i] == myToe) {
					continousFields++;
				} else {
					break;
				}
			}
			if (continousFields == rangeParam && map[x][y + range] == otherToe) {
				return 1;
			}
		} else {
			if (map[x][y] == otherToe) {
				for (int i = 1; i < range; i++) {
					if (map[x][y + i] == myToe) {
						continousFields++;
					} else {
						break;
					}
				}
				if (continousFields == rangeParam && map[x][y + range] == EMPTY) {
					return 1;
				}
			}
		}

		return 0;
	}

	private int findClosedHorizontal(int myToe, int otherToe, int x, int y, int rangeParam) {

		int continousFields = 0;
		int range = rangeParam + 1;
		if (map[x][y] == EMPTY) {
			for (int i = 1; i < range; i++) {
				if (map[x + i][y] == myToe) {
					continousFields++;
				} else {
					break;
				}
			}
			if (continousFields == rangeParam && map[x + range][y] == otherToe) {
				return 1;
			}
		} else {
			if (map[x][y] == otherToe) {
				for (int i = 1; i < range; i++) {
					if (map[x + i][y] == myToe) {
						continousFields++;
					} else {
						break;
					}
				}
				if (continousFields == rangeParam && map[x + range][y] == EMPTY) {
					return 1;
				}
			}
		}
		return 0;
	}

	private int countOneStepsToWinStates(int myToe, int otherToe) {
		int all = 0;
		for (int i = 0; i < map.length - 4; i++) {
			for (int j = 0; j < map[0].length - 4; j++) {

				int findClosedHorizontal = findClosedHorizontal(myToe, otherToe, i, j, 4);
				int findClosedVertical = findClosedVertical(myToe, otherToe, i, j, 4);
				int findClosedDiagonal = findClosedDiagonal(myToe, otherToe, i, j, 4);
				int findClosedDiagonalMajor = findClosedDiagonalMajor(myToe, otherToe, i, j, 4);
				all += findClosedHorizontal + findClosedDiagonal + findClosedVertical + findClosedDiagonalMajor;
			}
		}
		return all;
	}

	private int countClosedThrees(int myToe, int otherToe) {
		int all = 0;
		for (int i = 0; i < map.length - 4; i++) {
			for (int j = 0; j < map[0].length - 4; j++) {

				int findClosedHorizontal = findClosedHorizontal(myToe, otherToe, i, j, 3);
				int findClosedVertical = findClosedVertical(myToe, otherToe, i, j, 3);
				int findClosedDiagonal = findClosedDiagonal(myToe, otherToe, i, j, 3);
				int findClosedDiagonalMajor = findClosedDiagonalMajor(myToe, otherToe, i, j, 3);
				all += findClosedHorizontal + findClosedDiagonal + findClosedVertical + findClosedDiagonalMajor;
			}
		}
		return all;
	}

	private int countOneStepsToWinLowerPiority(int myToe) {
		int all = 0;
		for (int i = 0; i < map.length - 4; i++) {
			for (int j = 0; j < map[0].length - 4; j++) {

				int countGridFoursE1 = countGridFoursE1(myToe, i, j);
				int countGridFoursE2 = countGridFoursE2(myToe, i, j);
				int countGridFoursE3 = countGridFoursE3(myToe, i, j);

				all += countGridFoursE1 + countGridFoursE2 + countGridFoursE3;

			}
		}

		return all;
	}

	private int countOneStepsToWinStatesPreferred(int myToe) {
		int all = 0;
		for (int i = 0; i < map.length - 4; i++) {
			for (int j = 0; j < map[0].length - 4; j++) {
				int findOpenHorizontal = findOpenHorizontal(myToe, i, j, 4);
				int findOpenVertical = findOpenVertical(myToe, i, j, 4);
				int findOpenDiagonal = findOpenDiagonal(myToe, i, j, 4);
				int findOpenDiagonalMajor = findOpenDiagonalMajor(myToe, i, j, 4);
				all += findOpenDiagonal + findOpenDiagonalMajor + findOpenHorizontal + findOpenVertical;
			}
		}

		return all;
	}

	private int countTwoStepsToWinStates(int myToe) {

		int all = 0;
		for (int i = 0; i < map.length - 4; i++) {
			for (int j = 0; j < map[0].length - 4; j++) {

				if (i < map.length - 5 && j < map[0].length - 5) {
					int findGridHorizontal = findGridHorizontal(myToe, i, j);
					int findGridVertical = findGridVertical(myToe, i, j);
					int findGridDiagonal = findGridDiagonal(myToe, i, j);
					int findGridMajorDiagonal = findGridMajorDiagonal(myToe, i, j);

					all += findGridDiagonal + findGridHorizontal + findGridMajorDiagonal + findGridVertical;
				}
				// all += findOpenDiagonal + findOpenDiagonalMajor +
				// findOpenHorizontal + findOpenVertical;
			}
		}

		return all;
	}

	private int countTwoStepsToWinStatesPref(int myToe) {

		int all = 0;
		for (int i = 0; i < map.length - 4; i++) {
			for (int j = 0; j < map[0].length - 4; j++) {
				int findOpenHorizontal = findOpenHorizontal(myToe, i, j, 3);
				int findOpenVertical = findOpenVertical(myToe, i, j, 3);
				int findOpenDiagonal = findOpenDiagonal(myToe, i, j, 3);
				int findOpenDiagonalMajor = findOpenDiagonalMajor(myToe, i, j, 3);

				all += findOpenDiagonal + findOpenDiagonalMajor + findOpenHorizontal + findOpenVertical;
			}
		}

		return all;
	}

	private int countOpenThreeStepsStates(int myToe) {
		int all = 0;
		for (int i = 0; i < map.length - 4; i++) {
			for (int j = 0; j < map[0].length - 4; j++) {
				int findOpenHorizontal = findOpenHorizontal(myToe, i, j, 2);
				int findOpenVertical = findOpenVertical(myToe, i, j, 2);
				int findOpenDiagonal = findOpenDiagonal(myToe, i, j, 2);
				int findOpenDiagonalMajor = findOpenDiagonalMajor(myToe, i, j, 2);

				int allThreats = findOpenDiagonal + findOpenDiagonalMajor + findOpenHorizontal + findOpenVertical;
				all += allThreats;
			}
		}
		return all;
	}

	private int countOpenFourStepsStates(int myToe) {
		int all = 0;
		for (int i = 0; i < map.length - 4; i++) {
			for (int j = 0; j < map[0].length - 4; j++) {
				int findOpenHorizontal = findOpenHorizontal(myToe, i, j, 1);
				int findOpenVertical = findOpenVertical(myToe, i, j, 1);
				int findOpenDiagonal = findOpenDiagonal(myToe, i, j, 1);
				int findOpenDiagonalMajor = findOpenDiagonalMajor(myToe, i, j, 1);

				int allThreats = findOpenDiagonal + findOpenDiagonalMajor + findOpenHorizontal + findOpenVertical;
				all += allThreats;
			}
		}
		return all;
	}

	// OPEN

	public int countGridFoursE1(int myToe, int x, int y) {

		int all = 0;

		boolean b1 = (map[x][y] == myToe);
		boolean b2 = (map[x + 1][y] == myToe);
		boolean b3 = (map[x + 2][y] == EMPTY);
		boolean b4 = (map[x + 3][y] == myToe);
		boolean b5 = (map[x + 4][y] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		b1 = (map[x][y] == myToe);
		b2 = (map[x][y + 1] == myToe);
		b3 = (map[x][y + 2] == EMPTY);
		b4 = (map[x][y + 3] == myToe);
		b5 = (map[x][y + 4] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		b1 = (map[x][y] == myToe);
		b2 = (map[x + 1][y + 1] == myToe);
		b3 = (map[x + 2][y + 2] == EMPTY);
		b4 = (map[x + 3][y + 3] == myToe);
		b5 = (map[x + 4][y + 4] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		b1 = (map[x][y] == myToe);
		b2 = (map[x + 1][y + 1] == myToe);
		b3 = (map[x + 2][y + 2] == EMPTY);
		b4 = (map[x + 3][y + 3] == myToe);
		b5 = (map[x + 4][y + 4] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		if (x - 4 >= 0) {

			b1 = (map[x][y] == myToe);
			b2 = (map[x - 1][y + 1] == myToe);
			b3 = (map[x - 2][y + 2] == EMPTY);
			b4 = (map[x - 3][y + 3] == myToe);
			b5 = (map[x - 4][y + 4] == myToe);

			if (b1 && b2 && b3 && b4 && b5) {
				all++;
			}
		}

		return all;

	}

	public int countGridFoursE2(int myToe, int x, int y) {

		int all = 0;

		boolean b1 = (map[x][y] == myToe);
		boolean b2 = (map[x + 1][y] == EMPTY);
		boolean b3 = (map[x + 2][y] == myToe);
		boolean b4 = (map[x + 3][y] == myToe);
		boolean b5 = (map[x + 4][y] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		b1 = (map[x][y] == myToe);
		b2 = (map[x][y + 1] == EMPTY);
		b3 = (map[x][y + 2] == myToe);
		b4 = (map[x][y + 3] == myToe);
		b5 = (map[x][y + 4] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		b1 = (map[x][y] == myToe);
		b2 = (map[x + 1][y + 1] == EMPTY);
		b3 = (map[x + 2][y + 2] == myToe);
		b4 = (map[x + 3][y + 3] == myToe);
		b5 = (map[x + 4][y + 4] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		b1 = (map[x][y] == myToe);
		b2 = (map[x + 1][y + 1] == EMPTY);
		b3 = (map[x + 2][y + 2] == myToe);
		b4 = (map[x + 3][y + 3] == myToe);
		b5 = (map[x + 4][y + 4] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		if (x - 4 >= 0) {

			b1 = (map[x][y] == myToe);
			b2 = (map[x - 1][y + 1] == EMPTY);
			b3 = (map[x - 2][y + 2] == myToe);
			b4 = (map[x - 3][y + 3] == myToe);
			b5 = (map[x - 4][y + 4] == myToe);

			if (b1 && b2 && b3 && b4 && b5) {
				all++;
			}
		}

		return all;

	}

	public int countGridFoursE3(int myToe, int x, int y) {

		int all = 0;

		boolean b1 = (map[x][y] == myToe);
		boolean b2 = (map[x + 1][y] == myToe);
		boolean b3 = (map[x + 2][y] == myToe);
		boolean b4 = (map[x + 3][y] == EMPTY);
		boolean b5 = (map[x + 4][y] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		b1 = (map[x][y] == myToe);
		b2 = (map[x][y + 1] == myToe);
		b3 = (map[x][y + 2] == myToe);
		b4 = (map[x][y + 3] == EMPTY);
		b5 = (map[x][y + 4] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		b1 = (map[x][y] == myToe);
		b2 = (map[x + 1][y + 1] == myToe);
		b3 = (map[x + 2][y + 2] == myToe);
		b4 = (map[x + 3][y + 3] == EMPTY);
		b5 = (map[x + 4][y + 4] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		b1 = (map[x][y] == myToe);
		b2 = (map[x + 1][y + 1] == myToe);
		b3 = (map[x + 2][y + 2] == myToe);
		b4 = (map[x + 3][y + 3] == EMPTY);
		b5 = (map[x + 4][y + 4] == myToe);

		if (b1 && b2 && b3 && b4 && b5) {
			all++;
		}

		if (x - 4 >= 0) {

			b1 = (map[x][y] == myToe);
			b2 = (map[x - 1][y + 1] == myToe);
			b3 = (map[x - 2][y + 2] == myToe);
			b4 = (map[x - 3][y + 3] == EMPTY);
			b5 = (map[x - 4][y + 4] == myToe);

			if (b1 && b2 && b3 && b4 && b5) {
				all++;
			}
		}

		return all;

	}

	private int findOpenDiagonal(int myToe, int x, int y, int rangeParam) {

		int continousFields = 0;

		int range = rangeParam + 1;

		if (map[x][y] == EMPTY) {
			for (int i = 1; i < range; i++) {
				if (map[x + i][y + i] == myToe) {
					continousFields++;
				} else {
					break;
				}
			}
		}
		if (continousFields == rangeParam && map[x + range][y + range] == EMPTY) {
			return 1;
		}
		return 0;

	}

	private int findOpenDiagonalMajor(int myToe, int x, int y, int rangeParam) {

		int continousFields = 0;

		int range = rangeParam + 1;

		if (map[x][y] == EMPTY) {
			for (int i = 1; (i < range); i++) {
				if ((x - i > 0)) {
					if (map[x - i][y + i] == myToe) {
						continousFields++;
					} else {
						return 0;
					}
				}
			}
		}
		if (continousFields == rangeParam && map[x - range][y + range] == EMPTY) {
			return 1;
		}
		return 0;

	}

	private int findOpenVertical(int myToe, int x, int y, int rangeParam) {

		int continousFields = 0;
		int range = rangeParam + 1;
		if (map[x][y] == EMPTY) {
			for (int i = 1; i < range; i++) {
				if (map[x][y + i] == myToe) {
					continousFields++;
				} else {
					break;
				}
			}
		}
		if (continousFields == rangeParam && map[x][y + range] == EMPTY) {
			return 1;
		}
		return 0;
	}

	private int findOpenHorizontal(int myToe, int x, int y, int rangeParam) {
		int continousFields = 0;
		int range = rangeParam + 1;
		if (map[x][y] == EMPTY) {
			for (int i = 1; i < range; i++) {
				if (map[x + i][y] == myToe) {
					continousFields++;
				} else {
					break;
				}
			}
		}
		if (continousFields == rangeParam && map[x + range][y] == EMPTY) {
			return 1;
		}
		return 0;
	}

	// GRID
	public int findThreatGrid(int myToe) {
		return 0;
		//
		// int points = 0;
		//
		// for (int i = 0; i < map.length - 5; i++) {
		// for (int j = 0; j < map[0].length - 5; j++) {
		//
		// boolean findGridHorizontal = findGridHorizontal(myToe, i, j);
		// boolean findGridVertical = findGridVertical(myToe, i, j);
		// boolean findGridDiagonal = findGridDiagonal(myToe, i, j);
		// boolean findGridMajorDiagonal = findGridMajorDiagonal(myToe, i, j);
		//
		// if (findGridMajorDiagonal) {
		// points += MUST_REACT;
		// }
		//
		// if (findGridDiagonal) {
		// points += MUST_REACT;
		// }
		//
		// if (findGridVertical) {
		// points += MUST_REACT;
		// }
		//
		// if (findGridHorizontal) {
		// points += MUST_REACT;
		// }
		// }
		// }
		// return points;

	}

	private int findGridHorizontal(int myToe, int x, int y) {
		boolean b1 = (map[x][y] == EMPTY);
		boolean b2 = (map[x + 1][y] == myToe);
		boolean b3 = (map[x + 2][y] == EMPTY);
		boolean b4 = (map[x + 3][y] == myToe);
		boolean b5 = (map[x + 4][y] == myToe);
		boolean b6 = (map[x + 5][y] == EMPTY);

		if (b1 && b2 && b3 && b4 && b5 && b6) {
			return 1;
		}

		b1 = (map[x][y] == EMPTY);
		b2 = (map[x + 1][y] == myToe);
		b3 = (map[x + 2][y] == myToe);
		b4 = (map[x + 3][y] == EMPTY);
		b5 = (map[x + 4][y] == myToe);
		b6 = (map[x + 5][y] == EMPTY);

		if (b1 && b2 && b3 && b4 && b5 && b6) {
			return 1;
		}

		return 0;
	}

	private int findGridVertical(int myToe, int x, int y) {
		boolean b1 = (map[x][y] == EMPTY);
		boolean b2 = (map[x][y + 1] == myToe);
		boolean b3 = (map[x][y + 2] == EMPTY);
		boolean b4 = (map[x][y + 3] == myToe);
		boolean b5 = (map[x][y + 4] == myToe);
		boolean b6 = (map[x][y + 5] == EMPTY);

		if (b1 && b2 && b3 && b4 && b5 && b6) {
			return 1;
		}

		b1 = (map[x][y] == EMPTY);
		b2 = (map[x][y + 1] == myToe);
		b3 = (map[x][y + 2] == myToe);
		b4 = (map[x][y + 3] == EMPTY);
		b5 = (map[x][y + 4] == myToe);
		b6 = (map[x][y + 5] == EMPTY);

		if (b1 && b2 && b3 && b4 && b5 && b6) {
			return 1;
		}
		return 0;
	}

	private int findGridDiagonal(int myToe, int x, int y) {

		boolean b1 = (map[x][y] == EMPTY);
		boolean b2 = (map[x + 1][y + 1] == myToe);
		boolean b3 = (map[x + 2][y + 2] == EMPTY);
		boolean b4 = (map[x + 3][y + 3] == myToe);
		boolean b5 = (map[x + 4][y + 4] == myToe);
		boolean b6 = (map[x + 5][y + 5] == EMPTY);

		if (b1 && b2 && b3 && b4 && b5 && b6) {
			return 1;
		}

		b1 = (map[x][y] == EMPTY);
		b2 = (map[x + 1][y + 1] == myToe);
		b3 = (map[x + 2][y + 2] == myToe);
		b4 = (map[x + 3][y + 3] == EMPTY);
		b5 = (map[x + 4][y + 4] == myToe);
		b6 = (map[x + 5][y + 5] == EMPTY);

		if (b1 && b2 && b3 && b4 && b5 && b6) {
			return 1;
		}

		return 0;

	}

	private int findGridMajorDiagonal(int myToe, int x, int y) {

		if (x - 5 < 0) {
			return 0;
		}

		boolean b1 = (map[x][y] == EMPTY);
		boolean b2 = (map[x - 1][y + 1] == myToe);
		boolean b3 = (map[x - 2][y + 2] == EMPTY);
		boolean b4 = (map[x - 3][y + 3] == myToe);
		boolean b5 = (map[x - 4][y + 4] == myToe);
		boolean b6 = (map[x - 5][y + 5] == EMPTY);
		if (b1 && b2 && b3 && b4 && b5 && b6) {
			return 1;
		}

		b1 = (map[x][y] == EMPTY);
		b2 = (map[x - 1][y + 1] == myToe);
		b3 = (map[x - 2][y + 2] == myToe);
		b4 = (map[x - 3][y + 3] == EMPTY);
		b5 = (map[x - 4][y + 4] == myToe);
		b6 = (map[x - 5][y + 5] == EMPTY);

		if (b1 && b2 && b3 && b4 && b5 && b6) {
			return 1;
		}
		return 0;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	private void printMap() {
		for (int j = 0; j < map[0].length; j++) {
			for (int i = 0; i < map.length; i++) {
				if (map[i][j] == 0) {
					System.out.print(" . ");
				}
				if (map[i][j] == 1) {
					System.out.print(" O ");
				}
				if (map[i][j] == 2) {
					System.out.print(" X ");
				}

			}
			System.out.println();
		}

	}

}
