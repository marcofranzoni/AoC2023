package it.marcofranzoni.aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Day1 {

	public static void main(String[] args) throws Exception {
		Day1 solution = new Day1();
		System.out.println("solution = " + solution.solve());
	}

	private final Map<String, Integer> mapping;

	public Day1() {
		mapping = new HashMap<>();
		mapping.put("1", 1);
		mapping.put("2", 2);
		mapping.put("3", 3);
		mapping.put("4", 4);
		mapping.put("5", 5);
		mapping.put("6", 6);
		mapping.put("7", 7);
		mapping.put("8", 8);
		mapping.put("9", 9);
		mapping.put("one", 1);
		mapping.put("two", 2);
		mapping.put("three", 3);
		mapping.put("four", 4);
		mapping.put("five", 5);
		mapping.put("six", 6);
		mapping.put("seven", 7);
		mapping.put("eight", 8);
		mapping.put("nine", 9);
	}

	public int solve() throws Exception {
		Path path = Path.of("src", "main", "resources", "day1.txt");

		try (Stream<String> stream = Files.lines(path)) {
			return stream.mapToInt(this::calibrate).sum();
		}
	}


	private int calibrate(String line) {
		int firstDigit = 0;
		int lastDigit = 0;
		int firstDigitIndex = Integer.MAX_VALUE;
		int lastDigitIndex = Integer.MIN_VALUE;

		for (var entry : mapping.entrySet()) {
			int firstCurrentDigitIndex = line.indexOf(entry.getKey());
			int lastCurrentDigitIndex = line.lastIndexOf(entry.getKey());

			if (firstCurrentDigitIndex >= 0 && firstCurrentDigitIndex < firstDigitIndex) {
				firstDigit = entry.getValue();
				firstDigitIndex = firstCurrentDigitIndex;
			}

			if (lastCurrentDigitIndex >= 0 && lastCurrentDigitIndex > lastDigitIndex) {
				lastDigit = entry.getValue();
				lastDigitIndex = lastCurrentDigitIndex;
			}
		}

		return firstDigit * 10 + lastDigit;
	}

}
