package tests;

import generator.CardGenerator;

public class GeneratorSolvableScenarioTest {

	/**
	 * A somewhat crude test for the generator:
	 * Creates a random turtle for all positions so a
	 * set can be instantly generated.
	 */
	public static void main(String[] args) {
		CardGenerator cg = new CardGenerator();
		cg.initiateWithSolvableScenario();
	}
}
