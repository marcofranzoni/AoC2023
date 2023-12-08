package it.marcofranzoni.aoc2023;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class Day3Test {

	private Day3 solution;

	@BeforeEach
	void setUp() {
		solution = new Day3();
	}

	@Test
	void retrieveLeftPart() {
		char[] line = new char[]{'.', '1', '2', '3', '4', '.'};
		var leftPart = solution.retrieveLeftPart(line, 2);
		assertEquals("12", leftPart);
	}

	@Test
	void retrieveRightPart() {
		char[] line = new char[]{'.', '1', '2', '3', '4', '.'};
		var rightPart = solution.retrieveRightPart(line, 2);
		assertEquals("34", rightPart);
	}

	@Test
	void retrieveNumberLine() {
		char[] line = new char[]{'.', '1', '2', '3', '4', '.'};
		var numbers = solution.retrieveAdjacentNumbersInLine(line, 2);
		assertEquals(1, numbers.size());
		assertEquals("1234", numbers.getFirst());
	}

	@Test
	void retrieveTwoNumbersLine() {
		char[] line = new char[]{'1', '.', '3', '4'};
		var numbers = solution.retrieveAdjacentNumbersInLine(line, 1);
		assertEquals(2, numbers.size());
		assertTrue(numbers.contains("1"));
		assertTrue(numbers.contains("34"));
	}

	@Test
	void retrieveOnlyLeftNumberLine() {
		char[] line = new char[]{'1', '.', '.', '.'};
		var numbers = solution.retrieveAdjacentNumbersInLine(line, 1);
		assertEquals(1, numbers.size());
		assertEquals("1", numbers.getFirst());
	}

	@Test
	void retrieveOnlyRightNumberLine() {
		char[] line = new char[]{'.', '.', '3', '4'};
		var numbers = solution.retrieveAdjacentNumbersInLine(line, 1);
		assertEquals(1, numbers.size());
		assertEquals("34", numbers.getFirst());
	}
}