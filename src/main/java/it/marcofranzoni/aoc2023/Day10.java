package it.marcofranzoni.aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day10 {

	private static final int MAP_SIZE = 140;
	private static char[][] matrix;
	private Coordinate start;
	private final List<Coordinate> loop = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		Path path = Path.of("src", "main", "resources", "day10.txt");

		Day10 solution = new Day10();
		solution.createMatrix(path);

		int partOne = solution.solvePartOne();
		System.out.println("partOne=" + partOne);
		System.out.println(partOne == 6968);

		int partTwo = solution.solvePartTwo();
		System.out.println("partTwo=" + partTwo);
		System.out.println(partTwo == 413);
	}

	private void createMatrix(Path path) throws IOException {
		List<String> lines = Files.readAllLines(path);

		matrix = new char[MAP_SIZE][MAP_SIZE];

		for (int i = 0; i < MAP_SIZE; i++) {
			for (int j = 0; j < MAP_SIZE; j++) {
				char sign = lines.get(i).charAt(j);
				matrix[i][j] = sign;

				if (sign == 'S') {
					start = new Coordinate(j, i);
				}
			}
		}
	}

	private int solvePartOne() {
		return findPathLength(start, Direction.ANY, 0) / 2;
	}

	private int findPathLength(Coordinate coordinate, Direction direction, int numberOfSteps) {
		if (matrix[coordinate.y()][coordinate.x()] == Tile.START.getSign() && numberOfSteps > 0) {
			return numberOfSteps;
		}

		loop.add(coordinate);

		var nextMove = nextMove(coordinate, direction);
		var newDirection = getDirection(coordinate, nextMove);

		return findPathLength(nextMove, newDirection, numberOfSteps + 1);
	}

	private Coordinate nextMove(Coordinate coordinate, Direction inputDirection) {
		Tile tile = Tile.getTile(getSign(coordinate));
		if (coordinate.y() > 0 && tile.getDirections().contains(Direction.NORTH)) {
			if (inputDirection != Direction.SOUTH) {
				char sign = matrix[coordinate.y() - 1][coordinate.x()];
				if (sign == Tile.VERTICAL_PIPE.getSign()
						|| sign == Tile.SEVEN_BEND.getSign()
						|| sign == Tile.F_BEND.getSign()
						|| sign == Tile.START.getSign()) {
					return new Coordinate(coordinate.x(), coordinate.y() - 1);
				}
			}
		}

		if (coordinate.x() > 0 && tile.getDirections().contains(Direction.WEST)) {
			if (inputDirection != Direction.EAST) {
				char sign = matrix[coordinate.y()][coordinate.x() - 1];
				if (sign == Tile.HORIZONTAL_PIPE.getSign()
						|| sign == Tile.F_BEND.getSign()
						|| sign == Tile.L_BEND.getSign()
						|| sign == Tile.START.getSign()) {
					return new Coordinate(coordinate.x() - 1, coordinate.y());
				}
			}
		}

		if (coordinate.x() + 1 < MAP_SIZE && tile.getDirections().contains(Direction.EAST)) {
			if (inputDirection != Direction.WEST) {
				char sign = matrix[coordinate.y()][coordinate.x() + 1];
				if (sign == Tile.HORIZONTAL_PIPE.getSign()
						|| sign == Tile.J_BEND.getSign()
						|| sign == Tile.SEVEN_BEND.getSign()
						|| sign == Tile.START.getSign()) {
					return new Coordinate(coordinate.x() + 1, coordinate.y());
				}
			}
		}

		if (coordinate.y() + 1 < MAP_SIZE && tile.getDirections().contains(Direction.SOUTH)) {
			if (inputDirection != Direction.NORTH) {
				char sign = matrix[coordinate.y() + 1][coordinate.x()];
				if (sign == Tile.VERTICAL_PIPE.getSign()
						|| sign == Tile.L_BEND.getSign()
						|| sign == Tile.J_BEND.getSign()
						|| sign == Tile.START.getSign()) {
					return new Coordinate(coordinate.x(), coordinate.y() + 1);
				}
			}
		}

		throw new RuntimeException("No next move for coordinate " + coordinate);
	}

	private char getSign(Coordinate coordinate) {
		return matrix[coordinate.y()][coordinate.x()];
	}

	private int solvePartTwo() {
		int sum = 0;
		for (int i = 0; i < MAP_SIZE; i++) {
			for (int j = 0; j < MAP_SIZE; j++) {
				var evaluatingCoordinate = new Coordinate(j, i);
				if (!loop.contains(evaluatingCoordinate) && isInsideLoop(new Coordinate(j, i))) {
					sum++;
				}
			}
		}

		return sum;
	}

	private boolean isInsideLoop(Coordinate coordinate) {
		List<Coordinate> points = loop.stream().filter(c -> c.y() == coordinate.y())
				.filter(c -> coordinate.x() > c.x())
				.toList();

		int boudaries = 0;
		for (var point : points) {
			char sign = matrix[point.y()][point.x()];
			if (sign == Tile.VERTICAL_PIPE.getSign()
					|| sign == Tile.L_BEND.getSign()
					|| sign == Tile.J_BEND.getSign()) {
				boudaries++;
			}

		}

		return boudaries % 2 != 0;
	}

	private record Coordinate(int x, int y) {
	}

	private enum Tile {
		VERTICAL_PIPE('|', List.of(Direction.NORTH, Direction.SOUTH)),
		HORIZONTAL_PIPE('-', List.of(Direction.WEST, Direction.EAST)),
		L_BEND('L', List.of(Direction.NORTH, Direction.EAST)),
		J_BEND('J', List.of(Direction.NORTH, Direction.WEST)),
		SEVEN_BEND('7', List.of(Direction.SOUTH, Direction.WEST)),
		F_BEND('F', List.of(Direction.SOUTH, Direction.EAST)),
		START('S', List.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST));

		private final char sign;
		private final List<Direction> directions;

		Tile(char sign, List<Direction> directions) {
			this.sign = sign;
			this.directions = directions;
		}

		public char getSign() {
			return sign;
		}

		public List<Direction> getDirections() {
			return directions;
		}

		public static Tile getTile(char sign) {
			for (var tile : Tile.values()) {
				if (tile.getSign() == sign) {
					return tile;
				}
			}
			throw new RuntimeException("Unknown sign");
		}
	}

	private enum Direction {
		NORTH, SOUTH, WEST, EAST, ANY
	}

	private static Direction getDirection(Coordinate first, Coordinate second) {

		if (first.x() == second.x() && first.y() < second.y()) {
			return Direction.SOUTH;
		} else if (first.x() == second.x() && first.y() > second.y()) {
			return Direction.NORTH;
		} else if (first.y() == second.y() && first.x() < second.x()) {
			return Direction.EAST;
		} else {
			return Direction.WEST;
		}

	}
}
