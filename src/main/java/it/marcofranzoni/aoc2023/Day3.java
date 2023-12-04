package it.marcofranzoni.aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day3 {

	public static void main(String[] args) throws Exception {
		Day3 solution = new Day3();
		var partOne = solution.solvePartOne();
		System.out.println("solution = " + partOne);

		System.out.println(partOne == 539713);

	}

	private int solvePartOne() throws Exception {
		Path path = Path.of("src", "main", "resources", "day3.txt");

		var symbols = findAllSymbols(path);

		var adjacentNumbers = adjacentNumbers(path, symbols);

		System.out.println(symbols);

		System.out.println(adjacentNumbers);

		return adjacentNumbers.stream().mapToInt(Integer::parseInt).sum();
	}

	private List<Coordinate> findAllSymbols(Path path) throws IOException {
		List<Coordinate> symbols = new ArrayList<>();

		int line = 0;
		for (String currentLine : Files.readAllLines(path)) {
			symbols.addAll(findSymbolsInLine(currentLine, line++));
		}
		return symbols;
	}

	private List<Coordinate> findSymbolsInLine(String line, int yAxis) {
		List<Coordinate> symbols = new ArrayList<>();
		for (int i=0; i<line.length(); i++) {
			char c = line.charAt(i);
			if (!Character.isLetterOrDigit(c) && c != '.') {
				symbols.add(new Coordinate(i, yAxis));
			}
		}

		return symbols;
	}

	/**
	 *     y-1   x-1 x x+1
	 *      y    x-1 o x+1
	 *     y+1   x-1 x x+1
	 *
	 */
	private boolean isAdjacentToSymbol(int x, int y, List<Coordinate> symbols) {
		return symbols.contains(new Coordinate(x-1, y-1))
		|| symbols.contains(new Coordinate(x, y-1))
		|| symbols.contains(new Coordinate(x+1, y-1))
		|| symbols.contains(new Coordinate(x-1, y))
		|| symbols.contains(new Coordinate(x+1, y))
		|| symbols.contains(new Coordinate(x-1, y+1))
		|| symbols.contains(new Coordinate(x, y+1))
		|| symbols.contains(new Coordinate(x+1, y+1));
	}

	private List<String> adjacentNumbers(Path path, List<Coordinate> symbols) throws IOException {
		int y = 0;
		List<String> adjacentNumbers = new ArrayList<>();

		boolean isAdjacentToSymbol = false;
		StringBuilder number = new StringBuilder();
		for (String line : Files.readAllLines(path)) {
			for (int x=0; x<line.length(); x++) {
				char c = line.charAt(x);
				if (Character.isDigit(c)) {
					isAdjacentToSymbol |= isAdjacentToSymbol(x, y, symbols);
					number.append(c);
				}
				if (!Character.isDigit(c) || x==line.length()-1) {
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

	private record Coordinate(int x, int y) {}

}
