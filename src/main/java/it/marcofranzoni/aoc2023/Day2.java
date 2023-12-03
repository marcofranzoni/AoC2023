package it.marcofranzoni.aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day2 {
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
		Pattern pattern = Pattern.compile("Game (\\d+): (.*)");
		Pattern greenPattern = Pattern.compile("\\s*(\\d+) green");
		Pattern bluePattern = Pattern.compile("\\s*(\\d+) blue");
		Pattern redPattern = Pattern.compile("\\s*(\\d+) red");
		Matcher matcher = pattern.matcher(line);

		int gameId = 0;
		List<Set> list = new ArrayList<>();

		if (matcher.matches()) {
			gameId = Integer.parseInt(matcher.group(1));
			String sets = matcher.group(2);

			var gameSets = sets.split(";");
			for (var set : gameSets) {
				int green = 0;
				int blue = 0;
				int red = 0;

				var colors = set.split(",");
				for (var color : colors) {

					matcher = greenPattern.matcher(color);
					if (matcher.matches()) {
						green = Integer.parseInt(matcher.group(1));
					}

					matcher = bluePattern.matcher(color);
					if (matcher.matches()) {
						blue = Integer.parseInt(matcher.group(1));
					}

					matcher = redPattern.matcher(color);
					if (matcher.matches()) {
						red = Integer.parseInt(matcher.group(1));
					}
				}

				var gameSet = new Set(green, blue, red);
				list.add(gameSet);
			}
		} else {
			System.out.println("Pattern doesn't match the input string.");
		}

		return new Game(gameId, list);
	}

	private record Game(int id, List<Set> sets) {
		boolean isPossible() {
			System.out.print("gameId=" + id );
			for (var set : sets) {
				if (set.green() > 13 || set.blue() > 14 || set.red() > 12) {
					System.out.println(", isPossible=false");
					return false;
				}
			}

			System.out.println(", isPossible=true");
			return true;
		}

		int power() {
			var green = sets.stream().mapToInt(Set::green).max().orElse(0);
			var blue = sets.stream().mapToInt(Set::blue).max().orElse(0);
			var red = sets.stream().mapToInt(Set::red).max().orElse(0);

			return green * blue * red;
		}

	}

	private record Set(int green, int blue, int red) {}
}
