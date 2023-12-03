package it.marcofranzoni.aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day2 {

	private static final Pattern PATTERN = Pattern.compile("Game (\\d+): (.*)");
	private static final Pattern GREEN_PATTERN = Pattern.compile("\\s*(\\d+) green");
	private static final Pattern BLUE_PATTERN = Pattern.compile("\\s*(\\d+) blue");
	private static final Pattern RED_PATTERN = Pattern.compile("\\s*(\\d+) red");

	public static void main(String[] args) throws Exception {
		Day2 solution = new Day2();
		var partOne = solution.solvePartOne();
		System.out.println("solution = " + partOne);

		var partTwo = solution.solvePartTwo();
		System.out.println(("solutionPartTwo = " + partTwo));

		System.out.println(partOne == 1734);
		System.out.println(partTwo == 70387);
	}

	public int solvePartOne() throws Exception {
		Path path = Path.of("src", "main", "resources", "day2.txt");

		try (Stream<String> stream = Files.lines(path)) {
			return stream.map(this::toGame)
					.filter(Game::isPossible)
					.mapToInt(Game::id)
					.sum();
		}
	}

	public int solvePartTwo() throws Exception {
		Path path = Path.of("src", "main", "resources", "day2.txt");

		try (Stream<String> stream = Files.lines(path)) {
			return stream.map(this::toGame)
					.mapToInt(Game::power)
					.sum();
		}
	}

	private Game toGame(String line) {
		Matcher matcher = PATTERN.matcher(line);

		int gameId = 0;
		List<GameSet> list = new ArrayList<>();

		if (matcher.matches()) {
			gameId = Integer.parseInt(matcher.group(1));
			String allGameSets = matcher.group(2);

			var gameSets = allGameSets.split(";");
			for (var set : gameSets) {
				int green = 0;
				int blue = 0;
				int red = 0;

				var colors = set.split(",");
				for (var color : colors) {

					matcher = GREEN_PATTERN.matcher(color);
					if (matcher.matches()) {
						green = Integer.parseInt(matcher.group(1));
					}

					matcher = BLUE_PATTERN.matcher(color);
					if (matcher.matches()) {
						blue = Integer.parseInt(matcher.group(1));
					}

					matcher = RED_PATTERN.matcher(color);
					if (matcher.matches()) {
						red = Integer.parseInt(matcher.group(1));
					}
				}

				var gameSet = new GameSet(green, blue, red);
				list.add(gameSet);
			}
		} else {
			System.out.println("Pattern doesn't match the input string.");
		}

		return new Game(gameId, list);
	}

	private record Game(int id, List<GameSet> gameSets) {
		private static final int GREEN_THRESHOLD = 13;
		private static final int BLUE_THRESHOLD = 14;
		private static final int RED_THRESHOLD = 12;

		boolean isPossible() {
			return gameSets.stream()
					.noneMatch(gameSet -> gameSet.green() > GREEN_THRESHOLD
							|| gameSet.blue() > BLUE_THRESHOLD
							|| gameSet.red() > RED_THRESHOLD);
		}

		int power() {
			var green = gameSets.stream().mapToInt(GameSet::green).max().orElse(0);
			var blue = gameSets.stream().mapToInt(GameSet::blue).max().orElse(0);
			var red = gameSets.stream().mapToInt(GameSet::red).max().orElse(0);

			return green * blue * red;
		}

	}

	private record GameSet(int green, int blue, int red) {}
}
