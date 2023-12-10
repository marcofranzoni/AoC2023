package it.marcofranzoni.aoc2023;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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

		long partOne = solution.solvePartOne(path);
		System.out.println("partOne=" + partOne);
	}

	private long solvePartOne(Path path) throws IOException {
		List<Seed> seeds = new ArrayList<>();

		try (var br = new BufferedReader(new FileReader(path.toFile()))) {
			String line;
			List<Mapping> mappings = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("seeds:")) {
					seeds = parseSeed(line);
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
			var soilId = getDestinationId(seedToSoil, seed.id());
			var fertilizerId = getDestinationId(soilToFertilizer, soilId);
			var waterId = getDestinationId(fertilizerToWater, fertilizerId);
			var lightId = getDestinationId(waterToLight, waterId);
			var temperatureId = getDestinationId(lightToTemperature, lightId);
			var humidityId = getDestinationId(temperatureToHumidity, temperatureId);
			var locationId = getDestinationId(humidityToLocation, humidityId);

			if (locationId < lowestLocation) {
				lowestLocation = locationId;
			}
		}

		return lowestLocation;
	}

	private long getDestinationId(List<Mapping> mappings, long id) {
		boolean found = false;
		long result = 0;
		for (var mapping : mappings) {
			if (id >= mapping.source && id < mapping.source() + mapping.rangeLength()) {
				found = true;
				result = mapping.destination() + (id - mapping.source());
				break;
			}
		}

		return found ? result : id;
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

	private List<Seed> parseSeed(String line) {
		String[] seeds = line.trim().split("\\s+");
		List<Seed> list = new ArrayList<>();
		for (int i = 1; i < seeds.length; i++) {
			list.add(new Seed(Long.parseLong(seeds[i])));
		}

		return list;
	}

	private record Seed(long id) {
	}

	private record Mapping(long destination, long source, long rangeLength) {
	}
}
