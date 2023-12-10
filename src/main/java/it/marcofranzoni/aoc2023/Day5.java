package it.marcofranzoni.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Day5 {

	private static final Pattern MAP_LINE_PATTERN = Pattern.compile("(\\d+)\\s+(\\d+)\\s+(\\d+)");

	private final List<Mapping> seedToSoil = new ArrayList<>();
	private final List<Mapping> soilToFertilizer = new ArrayList<>();
	private final List<Mapping> fertilizerToWater = new ArrayList<>();
	private final List<Mapping> waterToLight = new ArrayList<>();
	private final List<Mapping> lightToTemperature = new ArrayList<>();
	private final List<Mapping> temperatureToHumidity = new ArrayList<>();
	private final List<Mapping> humidityToLocation = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		Day5 solution = new Day5();
		Path path = Path.of("src", "main", "resources", "day5.txt");

		long partOne = solution.solve(path, Day5::parseSeedPartOne);
		System.out.println("partOne=" + partOne);

		long partTwo = solution.solve(path, Day5::parseSeedPartTwo);
		System.out.println("partTwo=" + partTwo);
	}

	private long solve(Path path, Function<String, List<Seed>> function) throws IOException {
		List<Seed> seeds = new ArrayList<>();

		try (var br = new BufferedReader(new FileReader(path.toFile()))) {
			String line;
			List<Mapping> mappings = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("seeds:")) {
					seeds = function.apply(line);
					continue;
				}

				if (line.isBlank()) {
					mappings = null;
					continue;
				}

				if (mappings == null) {
					mappings = defineMapping(line);
					continue;
				}

				populateMapping(mappings, line);
			}
		}

		return findLowestLocation(seeds);
	}

	private long findLowestLocation(List<Seed> seeds) {
		long lowestLocation = Long.MAX_VALUE;

		for (var seed : seeds) {
			for (long i = 0; i < seed.rangeLength(); i++) {
				long seedId = seed.id() + i;
				long locationId = calculateLocationId(seedId);
				lowestLocation = Math.min(lowestLocation, locationId);
			}
		}

		return lowestLocation;
	}

	private long calculateLocationId(long seedId) {
		long soilId = getDestinationId(seedToSoil, seedId);
		long fertilizerId = getDestinationId(soilToFertilizer, soilId);
		long waterId = getDestinationId(fertilizerToWater, fertilizerId);
		long lightId = getDestinationId(waterToLight, waterId);
		long temperatureId = getDestinationId(lightToTemperature, lightId);
		long humidityId = getDestinationId(temperatureToHumidity, temperatureId);

		return getDestinationId(humidityToLocation, humidityId);
	}

	private long getDestinationId(List<Mapping> mappings, long id) {
		for (var mapping : mappings) {
			if (id >= mapping.source && id < mapping.source() + mapping.rangeLength()) {
				return mapping.destination() + (id - mapping.source());
			}
		}

		return id;
	}

	private static void populateMapping(List<Mapping> currentMapping, String line) {
		var matcher = MAP_LINE_PATTERN.matcher(line);

		if (matcher.matches()) {
			long destinationRangeStart = Long.parseLong(matcher.group(1));
			long sourceRangeStart = Long.parseLong(matcher.group(2));
			long rangeLength = Long.parseLong(matcher.group(3));

			currentMapping.add(new Mapping(destinationRangeStart, sourceRangeStart, rangeLength));
		}
	}

	private List<Mapping> defineMapping(String line) {
		String[] values = line.trim().split("\\s+");

		return switch (values[0]) {
			case "seed-to-soil" -> seedToSoil;
			case "soil-to-fertilizer" -> soilToFertilizer;
			case "fertilizer-to-water" -> fertilizerToWater;
			case "water-to-light" -> waterToLight;
			case "light-to-temperature" -> lightToTemperature;
			case "temperature-to-humidity" -> temperatureToHumidity;
			case "humidity-to-location" -> humidityToLocation;
			default -> throw new RuntimeException("Map not found");
		};
	}

	private static List<Seed> parseSeedPartOne(String line) {
		String[] seeds = line.trim().split("\\s+");
		List<Seed> list = new ArrayList<>();
		for (int i = 1; i < seeds.length; i++) {
			list.add(new Seed(Long.parseLong(seeds[i])));
		}

		return list;
	}

	private static List<Seed> parseSeedPartTwo(String line) {
		String[] seeds = line.trim().split("\\s+");
		List<Seed> list = new ArrayList<>();
		for (int i = 1; i < seeds.length - 1; i += 2) {
			list.add(new Seed(Long.parseLong(seeds[i]), Long.parseLong(seeds[i + 1])));
		}

		return list;
	}

	private record Seed(long id, long rangeLength) {
		private Seed(long id) {
			this(id, 1);
		}
	}

	private record Mapping(long destination, long source, long rangeLength) {
	}
}
