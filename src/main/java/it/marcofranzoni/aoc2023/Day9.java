package it.marcofranzoni.aoc2023;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day9 {

	public static void main(String[] args) throws IOException {
		Path path = Path.of("src", "main", "resources", "day9.txt");

		var lines = readLines(path);

		Day9 solution = new Day9();
		int partOne = solution.solvePartOne(lines);
		System.out.println("partOne=" + partOne);
		System.out.println(partOne==1861775706);
	}

	private static List<List<Integer>> readLines(Path path) throws IOException {
		List<List<Integer>> lines = new ArrayList<>();

		try (var scanner = new Scanner(path)) {

			while (scanner.hasNextLine()) {
				List<Integer> history = new ArrayList<>();
				String line = scanner.nextLine();

				try (var lineScanner = new Scanner(line)) {
					while (lineScanner.hasNextInt()) {
						int number = lineScanner.nextInt();
						history.add(number);
					}
					lines.add(history);
				}
			}
		}

		return lines;
	}


	private int solvePartOne(List<List<Integer>> lines) {
		int sum = 0;
		for (var line : lines) {
			sum += (line.getLast() + solveLine(line));
		}

		return sum;
	}

	private int solveLine(List<Integer> line) {
		boolean allZero = line.stream().allMatch(n -> n == 0);
		if (allZero) {
			return 0;
		}

		List<Integer> nextLine = new ArrayList<>();
		for (int i = 0; i < line.size() - 1; i++) {
			nextLine.add(line.get(i + 1) - line.get(i));
		}

		return nextLine.getLast() + solveLine(nextLine);
	}
}
