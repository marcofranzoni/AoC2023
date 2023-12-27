package it.marcofranzoni.aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 {

	private static final Pattern LINE_PATTERN = Pattern.compile("(.*) = \\((.*), (.*)\\)");
	private static final Map<String, NextStep> STEPS = new HashMap<>();

	public static void main(String[] args) throws IOException {
		Day8 solution = new Day8();
		Path path = Path.of("src", "main", "resources", "day8.txt");

		List<String> lines = solution.readInputFile(path);

		String steps = getSteps(lines);
		createStepMap(lines);

		int partOne = solution.partOne(steps);
		System.out.println("partOne=" + partOne);
		System.out.println(partOne == 17263);

		int partTwo = solution.partTwo(steps);
		System.out.println("partTwo=" + partTwo);
	}

	private int partOne(String steps) {
		int i = 0;
		int totalSteps = 0;
		String nextElement;
		NextStep nextStep = STEPS.get("AAA");

		do {
			char instruction = steps.charAt(i);
			nextElement = nextStep.get(instruction);
			nextStep = STEPS.get(nextElement);
			totalSteps++;
			i = (i + 1) % steps.length();
		} while (!nextElement.equals("ZZZ"));

		return totalSteps;
	}

	private List<String> readInputFile(Path path) throws IOException {
		return Files.readAllLines(path);
	}

	private static void createStepMap(List<String> lines) {

		for (int i = 1; i < lines.size(); i++) {
			Matcher matcher = LINE_PATTERN.matcher(lines.get(i));

			if (matcher.matches()) {
				STEPS.put(matcher.group(1), new NextStep(matcher.group(2), matcher.group(3)));
			}
		}
	}

	private static String getSteps(List<String> lines) {
		return lines.getFirst();
	}

	private int partTwo(String steps) {

		int i = 0;
		int totalSteps = 0;

		List<String> nextElements = STEPS.keySet().stream().filter(nextStep -> nextStep.endsWith("A")).toList();
		List<NextStep> nextSteps = getNextSteps(nextElements);

		do {
			char instruction = steps.charAt(i);
			nextElements = getNextElements(nextSteps, instruction);
			nextSteps = getNextSteps(nextElements);
			totalSteps++;
			i = (i + 1) % steps.length();
		} while (!nextElements.stream().allMatch(str -> str.endsWith("Z")));

		return totalSteps;
	}

	private List<String> getNextElements(List<NextStep> nextSteps, char instruction) {
		List<String> outcome = new ArrayList<>();

		for (NextStep nextStep : nextSteps) {
			outcome.add(nextStep.get(instruction));
		}

		return outcome;
	}

	private List<NextStep> getNextSteps(List<String> nextElements) {
		List<NextStep> nextSteps = new ArrayList<>();

		for (String node : nextElements) {
			nextSteps.add(STEPS.get(node));
		}

		return nextSteps;
	}

	private record NextStep(String left, String right) {
		String get(char instruction) {
			return instruction == 'L' ? left : right;
		}
	}


}
