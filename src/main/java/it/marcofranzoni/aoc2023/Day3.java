package it.marcofranzoni.aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Day3 {

	public static void main(String[] args) throws Exception {
		Day3 solution = new Day3();
		Path path = Path.of("src", "main", "resources", "day3.txt");
		int partOne = solution.solvePartOne(path);

		System.out.println("partOne = " + partOne);
		System.out.println(partOne == 539713);

		int partTwo = solution.solvePartTwo(path);
		System.out.println("partTwo = " + partTwo);

	}

	private int solvePartOne(Path path) throws Exception {
		var symbols = findAllSymbols(path, Day3::isSymbol);
		var adjacentNumbers = adjacentNumbers(path, symbols);

		return adjacentNumbers.stream().mapToInt(Integer::parseInt).sum();
	}

	private List<Coordinate> findAllSymbols(Path path, Predicate<Character> check) throws IOException {
		List<Coordinate> symbols = new ArrayList<>();
		int line = 0;

		for (String currentLine : Files.readAllLines(path)) {
			symbols.addAll(findSymbolsInLine(currentLine, line++, check));
		}

		return symbols;
	}

	private List<Coordinate> findSymbolsInLine(String line, int yAxis, Predicate<Character> check) {
		List<Coordinate> symbols = new ArrayList<>();

		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (check.test(c)) {
				symbols.add(new Coordinate(i, yAxis));
			}
		}

		return symbols;
	}

	private static boolean isSymbol(char c) {
		return !Character.isLetterOrDigit(c) && c != '.';
	}

	/**
	 * y-1   x-1 x x+1
	 * y    x-1 o x+1
	 * y+1   x-1 x x+1
	 */
	private boolean isAdjacentToSymbol(int x, int y, List<Coordinate> symbols) {
		return symbols.contains(new Coordinate(x - 1, y - 1))
				|| symbols.contains(new Coordinate(x, y - 1))
				|| symbols.contains(new Coordinate(x + 1, y - 1))
				|| symbols.contains(new Coordinate(x - 1, y))
				|| symbols.contains(new Coordinate(x + 1, y))
				|| symbols.contains(new Coordinate(x - 1, y + 1))
				|| symbols.contains(new Coordinate(x, y + 1))
				|| symbols.contains(new Coordinate(x + 1, y + 1));
	}

	private List<String> adjacentNumbers(Path path, List<Coordinate> symbols) throws IOException {
		int y = 0;
		List<String> adjacentNumbers = new ArrayList<>();

		boolean isAdjacentToSymbol = false;
		StringBuilder number = new StringBuilder();
		for (String line : Files.readAllLines(path)) {
			for (int x = 0; x < line.length(); x++) {
				char c = line.charAt(x);
				if (Character.isDigit(c)) {
					isAdjacentToSymbol |= isAdjacentToSymbol(x, y, symbols);
					number.append(c);
				}
				if (!Character.isDigit(c) || x == line.length() - 1) {
					if (isAdjacentToSymbol) {
						adjacentNumbers.add(number.toString());
					}
					isAdjacentToSymbol = false;
					number.setLength(0);
				}
			}

			y++;
		}

		return adjacentNumbers;
	}

	private int solvePartTwo(Path path) throws Exception {
		var stars = findAllSymbols(path, Day3::isStar);
		var matrix = buildMatrix(path);

		return totalRatio(matrix, stars);
	}

	private int totalRatio(char[][] matrix, List<Coordinate> stars) {
		int totalRatio = 0;
		for (Coordinate coordinate : stars) {
			totalRatio += calculateRatio(matrix, coordinate);
		}
		return totalRatio;
	}

	private char[][] buildMatrix(Path path) throws IOException {
		List<String> fileContent = Files.readAllLines(path);
		int totalLines = fileContent.size();

		char[][] matrix = new char[totalLines][];
		for (int y = 0; y < totalLines; y++) {
			String line = fileContent.get(y);
			char[] row = new char[line.length()];
			for (int x = 0; x < line.length(); x++) {
				row[x] = line.charAt(x);
			}
			matrix[y] = row;
		}

		return matrix;
	}

	private static boolean isStar(char c) {
		return c == '*';
	}

	private int calculateRatio(char[][] matrix, Coordinate star) {
		int x = star.x;
		int y = star.y;

		List<String> adjacentNumbers = new ArrayList<>();

		if (y > 0) {
			adjacentNumbers.addAll(retrieveAdjacentNumbersInLine(matrix[y - 1], x));
		}
		if (y < matrix.length - 1) {
			adjacentNumbers.addAll(retrieveAdjacentNumbersInLine(matrix[y + 1], x));
		}
		adjacentNumbers.addAll(retrieveAdjacentNumbersInLine(matrix[y], x));

		var isGear = adjacentNumbers.size() == 2;
		if (isGear) {
			return Integer.parseInt(adjacentNumbers.getFirst()) *
					Integer.parseInt(adjacentNumbers.getLast());
		}

		return 0;
	}

	List<String> retrieveAdjacentNumbersInLine(char[] line, int x) {
		List<String> adjacentNumbers = new ArrayList<>();
		if (Character.isDigit(line[x])) {
			var leftPart = retrieveLeftPart(line, x);
			var rightPart = retrieveRightPart(line, x);
			adjacentNumbers.add(leftPart + rightPart);
		} else {
			var leftPart = retrieveLeftPart(line, x);
			var rightPart = retrieveRightPart(line, x);

			if (!leftPart.isEmpty()) {
				adjacentNumbers.add(leftPart);
			}

			if (!rightPart.isEmpty()) {
				adjacentNumbers.add(rightPart);
			}

		}
		return adjacentNumbers;
	}

	String retrieveLeftPart(char[] line, int x) {
		StringBuilder number = new StringBuilder();

		for (int i = x; i >= 0; i--) {
			char c = line[i];
			if (Character.isDigit(c)) {
				number.insert(0, c);
			} else if (!Character.isDigit(c) && i != x) {
				break;
			}
		}

		return number.toString();
	}

	String retrieveRightPart(char[] line, int x) {
		StringBuilder number = new StringBuilder();
		for (int i = x + 1; i < line.length; i++) {
			char c = line[i];
			if (Character.isDigit(c)) {
				number.append(c);
			} else {
				break;
			}
		}

		return number.toString();
	}

	private record Coordinate(int x, int y) {
	}

}
