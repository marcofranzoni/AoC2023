package it.marcofranzoni.aoc2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day7 {
	private static final Pattern LINE_PATTERN = Pattern.compile("(.*)\\s+(\\d+)");
	private static final Map<String, Integer> CARDS = new HashMap<>();
	private static final int HAND_LENGTH = 5;

	public static void main(String[] args) throws Exception {
		Day7 solution = new Day7();
		Path path = Path.of("src", "main", "resources", "day7.txt");

		var partOne = solution.partOne(path);
		System.out.println("partOne=" + partOne);
		System.out.println(partOne==249204891);

		var partTwo = solution.partTwo(path);
		System.out.println("partTwo=" + partTwo);
		System.out.println(partTwo==249666369);

	}

	private int partOne(Path path) throws IOException {
		buildMapPartOne();

		int sum;
		try (Stream<String> stream = Files.lines(path)) {
			List<Hand> hands = stream.map(this::toHand)
					.sorted(new HandRankingComparatorPartOne())
					.toList();

			sum = IntStream.range(0, hands.size())
					.map(i -> hands.get(i).bid * (i + 1))
					.sum();

		}

		return sum;
	}

	private int partTwo(Path path) throws IOException {
		buildMapPartTwo();

		int sum;
		try (Stream<String> stream = Files.lines(path)) {
			List<Hand> hands = stream.map(this::toHand)
					.sorted(new HandRankingComparatorPartTwo())
					.toList();

			System.out.println(hands);

			sum = IntStream.range(0, hands.size())
					.map(i -> hands.get(i).bid * (i + 1))
					.sum();

		}

		return sum;
	}

	private Hand toHand(String line) {
		var matcher = LINE_PATTERN.matcher(line);

		Hand hand = null;
		if (matcher.matches()) {
			String value = matcher.group(1);
			int bid = Integer.parseInt(matcher.group(2));

			hand = new Hand(value, bid);
		} else {
			System.out.println("Unable to parse line " + line);
		}

		return hand;

	}

	private static void buildMapPartOne() {
		CARDS.put("2", 2);
		CARDS.put("3", 3);
		CARDS.put("5", 5);
		CARDS.put("4", 4);
		CARDS.put("6", 6);
		CARDS.put("7", 7);
		CARDS.put("8", 8);
		CARDS.put("9", 9);
		CARDS.put("T", 10);
		CARDS.put("J", 11);
		CARDS.put("Q", 12);
		CARDS.put("K", 13);
		CARDS.put("A", 14);
	}

	private static void buildMapPartTwo() {
		CARDS.put("J", 1);
	}

	private record Hand(String value, int bid) {
		long getStrengthPartOne() {

			return buildOccurrenceMap().values().stream()
					.filter(isGreaterThanOne())
					.mapToLong(l -> buildStrengthMap().get(l))
					.sum();
		}

		long getStrengthPartTwo() {
			long numberOfJ = value.chars().filter(ch -> ch == 'J').count();

			Map<Character, Long> occurrenceMap = buildOccurrenceMap();

			Optional<Map.Entry<Character, Long>> maxEntry = occurrenceMap.entrySet().stream()
					.filter(e -> e.getKey() != 'J')
					.max(Map.Entry.comparingByValue());

			if (numberOfJ > 0 && numberOfJ < 5) {
				maxEntry.ifPresent(entry -> {
					long updatedValue = entry.getValue() + numberOfJ;
					occurrenceMap.put(entry.getKey(), updatedValue);
				});
				occurrenceMap.put('J', 0L);
			}

			return occurrenceMap.values()
					.stream()
					.filter(isGreaterThanOne())
					.mapToLong(l -> buildStrengthMap().get(l))
					.sum();
		}

		/**
		 * Output map
		 * <table>
		 *   <tr>
		 *     <th>Cards</th>
		 *     <th>Strength</th>
		 *   </tr>
		 *   <tr>
		 *     <td>2</td>
		 *     <td>1</td>
		 *   </tr>
		 *   <tr>
		 *     <td>2 2</td>
		 *     <td>2</td>
		 *   </tr>
		 *   <tr>
		 *     <td>3</td>
		 *     <td>3</td>
		 *   </tr>
		 *   <tr>
		 *     <td>2 3</td>
		 *     <td>4</td>
		 *   </tr>
		 *   <tr>
		 *     <td>4</td>
		 *     <td>5</td>
		 *   </tr>
		 *   <tr>
		 *     <td>5</td>
		 *     <td>6</td>
		 *   </tr>
		 * </table>
		 *
		 * @return map of the points
		 */
		private Map<Long, Long> buildStrengthMap() {
			return Map.of(2L, 1L,
					3L, 3L,
					4L, 5L,
					5L, 6L);
		}

		private Predicate<Long> isGreaterThanOne() {
			return v -> v > 1;
		}

		private Map<Character, Long> buildOccurrenceMap() {
			return value.chars().mapToObj(c -> (char) c)
					.collect(Collectors.groupingBy(
							Function.identity(),
							Collectors.counting()
					));
		}
	}

	private static class HandRankingComparatorPartOne implements Comparator<Hand> {

		@Override
		public int compare(Hand first, Hand second) {

			if (first.getStrengthPartOne() == second.getStrengthPartOne()) {
				for (int i = 0; i < HAND_LENGTH; i++) {
					if (first.value.charAt(i) != second.value.charAt(i)) {
						return CARDS.get(String.valueOf(first.value.charAt(i))) - CARDS.get(String.valueOf(second.value.charAt(i)));
					}
				}
			}

			return Long.compare(first.getStrengthPartOne(), second.getStrengthPartOne());

		}

	}

	private static class HandRankingComparatorPartTwo implements Comparator<Hand> {

		@Override
		public int compare(Hand first, Hand second) {

			long firstStrength = first.getStrengthPartTwo();
			long secondStrength = second.getStrengthPartTwo();

			if (firstStrength == secondStrength) {
				for (int i = 0; i < HAND_LENGTH; i++) {
					if (first.value.charAt(i) != second.value.charAt(i)) {
						return CARDS.get(String.valueOf(first.value.charAt(i))) - CARDS.get(String.valueOf(second.value.charAt(i)));
					}
				}
			}

			return Long.compare(firstStrength, secondStrength);

		}

	}

}
