package tests;

import generator.CardGenerator;

public class GeneratorSolvableScenarioTest {

	/**
	 * A bit more sophisticated test for the generator:
	 * Creates a random turtle for all positions, except
	 * the one that have to match for a solution to exist.
	 */
	public static void main(String[] args) {
		CardGenerator cg = new CardGenerator();
		cg.initiateWithSolvableScenario();
	}
}
