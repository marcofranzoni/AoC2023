package it.marcofranzoni.aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day4 {

	public static final Pattern CARD_ID_PATTERN = Pattern.compile("Card\\s+(\\d+).*");

	public static void main(String[] args) throws Exception {
		Day4 solution = new Day4();
		Path path = Path.of("src", "main", "resources", "day4.txt");
		int partOne = solution.solvePartOne(path);

		System.out.println("partOne=" + partOne);

		var cards = solution.solvePartTwo(path);
		System.out.println("partTwo=" + cards);
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

		String[] numbers = numbersPart.trim().split("\\s+");
		for (String number : numbers) {
			list.add(Integer.parseInt(number));
		}

		return list;
	}

	private int solvePartTwo(Path path) throws IOException {
		try (Stream<String> stream = Files.lines(path)) {
			var cards = stream.map(this::toCard).toList();

			for (var card : cards) {
				int matches = card.getMatches();
				int id = card.getId();
				int copies = card.getCopies();

				for (int i = 0; i < matches; i++) {
					int currentCopies = cards.get(id + i).getCopies();
					cards.get(id + i).setCopies(currentCopies + copies);
				}
			}

			return cards.stream().mapToInt(Card::getCopies).sum();
		}
	}

	private Card toCard(String line) {
		String[] split = line.split(" \\| ");

		int cardId = parseId(split[0]);
		var winningNumbers = parseWinningNumbers(split[0]);
		var numbers = parseNumbers(split[1]);

		int matches = (int) numbers.stream().filter(winningNumbers::contains).count();

		return new Card(cardId, 1, matches);
	}

	private int parseId(String winningNumberPart) {
		Matcher matcher = CARD_ID_PATTERN.matcher(winningNumberPart);

		if (matcher.matches()) {
			return Integer.parseInt(matcher.group(1));
		}

		throw new RuntimeException("No match");
	}

	private static final class Card {
		private final int id;
		private int copies;
		private final int matches;

		private Card(int id, int copies, int matches) {
			this.id = id;
			this.copies = copies;
			this.matches = matches;
		}

		public int getId() {
			return id;
		}

		public int getCopies() {
			return copies;
		}

		public int getMatches() {
			return matches;
		}

		public void setCopies(int copies) {
			this.copies = copies;
		}
	}
}
