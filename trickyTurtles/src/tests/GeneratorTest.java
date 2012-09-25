package tests;

import generator.CardGenerator;

public class GeneratorTest {

	/**
	 * Creates a random turtle set in generator
	 */
	public static void main(String[] args) {
		CardGenerator cg = new CardGenerator();
		cg.initiateWithRandomTurtles();
	}
}
