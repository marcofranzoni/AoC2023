package it.marcofranzoni.aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day4 {

	public static void main(String[] args) throws Exception {
		Day4 solution = new Day4();
		Path path = Path.of("src", "main", "resources", "day4.txt");
		int partOne = solution.solvePartOne(path);

		System.out.println("partOne=" + partOne);
	}

	private int solvePartOne(Path path) throws IOException {
		try (Stream<String> stream = Files.lines(path)) {
			return stream.mapToInt(this::parseLine).sum();
		}
	}

	private int parseLine(String line) {
		String[] split = line.split(" \\| ");

		var winningNumbers = parseWinningNumbers(split[0]);
		var numbers = parseNumbers(split[1]);

		long matches = numbers.stream().filter(winningNumbers::contains).count();

		return countPoint(matches);
	}

	private int countPoint(long matches) {
		if (matches == 0) {
			return 0;
		}

		return (int) Math.pow(2, matches - 1);
	}

	private List<Integer> parseWinningNumbers(String winningNumberPart) {
		var split = winningNumberPart.split(":\\s+");

		return parseNumbers(split[1]);
	}

	private List<Integer> parseNumbers(String numbersPart) {
		List<Integer> list = new ArrayList<>();

		System.out.println(numbersPart);
		String[] numbers = numbersPart.trim().split("\\s+");
		for (String number : numbers) {
			list.add(Integer.parseInt(number));
		}

		return list;
	}
}
