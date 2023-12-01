package it.marcofranzoni.day1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Day1 {

	public static void main(String[] args) throws Exception {
		Day1 solution = new Day1();
		System.out.println("solution = " + solution.solve());
	}

	private int sum = 0;

	public int solve() throws Exception {
		Path path = Path.of("src", "main", "resources", "day1.txt");

		try (Stream<String> stream = Files.lines(path)) {
			stream.forEach(this::sum);
		}

		return sum;
	}

	private void sum(String line) {

		int firstDigit = Character.getNumericValue(getFirstDigit(line));
		int lastDigit = Character.getNumericValue(getLastDigit(line));

		sum += (firstDigit * 10 + lastDigit);
	}

	private int getFirstDigit(String line) {
		for (int i = 0; i < line.length(); i++) {
			if (Character.isDigit(line.charAt(i)))
				return line.charAt(i);
		}

		return 0;
	}

	private int getLastDigit(String line) {
		for (int i = line.length() - 1; i >= 0; i--) {
			if (Character.isDigit(line.charAt(i)))
				return line.charAt(i);
		}

		return 0;
	}
}
