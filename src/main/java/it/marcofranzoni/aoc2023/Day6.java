package it.marcofranzoni.aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 {

	private static final Pattern LINE_PATTERN_PART_ONE = Pattern.compile(".*:\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");
	private static final Pattern LINE_PATTERN_PART_TWO = Pattern.compile(".*:(.*)");
	private static final int NUMBER_OF_RACES = 4;
	private static final int FILE_ROWS = 2;

	public static void main(String[] args) throws Exception {
		Day6 solution = new Day6();
		Path path = Path.of("src", "main", "resources", "day6.txt");

		var partOne = solution.toRaces(path).stream().mapToInt(Day6::calculateWaysToBeatRecord)
				.reduce(1, (a, b) -> a * b);

		System.out.println("partOne=" + partOne);

		var partTwo = calculateWaysToBeatRecord(solution.toRace(path));
		System.out.println("partTwo=" + partTwo);

	}

	private List<Race> toRaces(Path path) throws IOException {
		List<String> lines = Files.readAllLines(path);
		var matrix = createMatrix(lines);

		List<Race> races = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_RACES; i++) {
			races.add(new Race(matrix[0][i], matrix[1][i]));
		}

		return races;
	}

	private static int[][] createMatrix(List<String> lines) {
		int[][] matrix = new int[FILE_ROWS][NUMBER_OF_RACES];

		for (int i = 0; i < lines.size(); i++) {
			Matcher matcher = LINE_PATTERN_PART_ONE.matcher(lines.get(i));

			if (matcher.matches()) {
				for (int j = 0; j < NUMBER_OF_RACES; j++) {
					matrix[i][j] = Integer.parseInt(matcher.group(j + 1));
				}
			}
		}

		return matrix;
	}

	private static int calculateWaysToBeatRecord(Race race) {
		int hold = 1;
		int result = 0;
		while (hold <= race.time) {
			long evaluatedDistance = (race.time() - hold) * hold;
			if (evaluatedDistance > race.distance()) {
				result++;
			}

			hold++;
		}

		return result;
	}

	private Race toRace(Path path) throws IOException {
		List<String> lines = Files.readAllLines(path);

		retrieveNumber(lines.getFirst());
		retrieveNumber(lines.getLast());

		var race = new Race(retrieveNumber(lines.getFirst()), retrieveNumber(lines.getLast()));

		System.out.println(race);

		return race;
	}

	private static long retrieveNumber(String line) {
		var matcher = LINE_PATTERN_PART_TWO.matcher(line);
		if (matcher.matches()) {
			return Long.parseLong(matcher.group(1).replace(" ", ""));
		}

		return 0;
	}

	private record Race(long time, long distance) {
	}

}
