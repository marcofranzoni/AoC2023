package it.marcofranzoni.day1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

public class Day1 {

	public static void main(String[] args) throws Exception {
		Day1 solution = new Day1();
		System.out.println("solution = " + solution.solve());
	}

	private int sum = 0;
	private final Map<Integer, String> MAPPING = Map.of(
			1, "one",
			2, "two",
			3, "three",
			4, "four",
			5, "five",
			6, "six",
			7, "seven",
			8, "eight",
			9, "nine"
	);


	public int solve() throws Exception {
		Path path = Path.of("src", "main", "resources", "day1_test.txt");

		try (Stream<String> stream = Files.lines(path)) {
			stream.forEach(this::sum);
		}

		return sum;
	}

	private void sum(String line) {

		String convertedLine = convert(line, 1);
		System.out.println("convertedLine=" + convertedLine);

		int firstDigit = Character.getNumericValue(getFirstDigit(convertedLine));
		int lastDigit = Character.getNumericValue(getLastDigit(convertedLine));

		sum += (firstDigit * 10 + lastDigit);
	}

	private String convert(String line, int digit) {
		if (digit == 9) {
			return line.replaceAll(MAPPING.get(digit), String.valueOf(digit));
		}

		return convert(line.replaceAll(MAPPING.get(digit), String.valueOf(digit)), ++digit);
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
