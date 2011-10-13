import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GameGrid;


public class ComputerPlayer {
	
	protected int[] pearlArrangement;
	private final int dualMax = 4;
	private int vertCells;
	protected GameGrid gg;
	protected boolean misere;
	private boolean changeStrat;

	public ComputerPlayer(GameGrid pearlGG, boolean misere) {
		this.gg = pearlGG;
		this.vertCells = gg.getNbVertCells();
		this.misere = misere;
	}
	/**
	 * updates the Pearl Arrangement. Only the row taken from
	 * and the amount (+1/-1 usually) is necessary.
	 * 
	 * The Position in the array
	 * corresponds to the getY of the Pearls.
	 * @param row
	 * @param amount
	 */
	public void updatePearlArrangement(int row, int amount) {
		pearlArrangement[row] += amount;
	}
	
	public void makeMove() {
		int removeRow = 0;
		int nbToRemoveMatches = 0;
		shouldIChangeStrat();
		// if optimal Strategy is not possible, do something random.
		if (!isUSituation(pearlArrangement) && !changeStrat) {
			ArrayList<Actor> pearls = gg.getActors(Pearl.class);
			System.out.println("Doing something random");
			// from a random (not empty!) row
			Collections.shuffle(pearls);
			removeRow = pearls.get(0).getY();
			// take a random amount (at least 1)
			nbToRemoveMatches = (int) ((pearlArrangement[removeRow] - 1) * Math.random() + 1);
		} else {
			// list for saving all possible solutions
			List<int[]> solutions = new ArrayList<int[]>();
			int[] tgPearls = new int[vertCells];
			// Try all possible situations and add them to "solutions if they're
			// good.
			for (int y = 0; y < vertCells; y++) {
				System.arraycopy(pearlArrangement, 0, tgPearls, 0, vertCells);
				for (int i = 0; i < pearlArrangement[y]; i++) {
					tgPearls = makeSituation(tgPearls, y);
					if (isUSituation(tgPearls) == changeStrat) {
						solutions.add((new int[] { y, i + 1 }));
					}
				}
			}
			// choose a random solution
			Collections.shuffle(solutions);
			removeRow = solutions.get(0)[0];
			nbToRemoveMatches = solutions.get(0)[1];
			System.out.println("Number of solutions: " + solutions.size());
		}
		removePearls(removeRow, nbToRemoveMatches);
	}
	
	private void shouldIChangeStrat() {
		if (changeStrat || !misere)
			return;
		boolean oneHeapBigger2 = false;
		for (int heap: pearlArrangement) {
			if (heap > 1) {
				if (oneHeapBigger2)
					return;
				oneHeapBigger2 = true;
			}
		}
		System.out.println("changing strategy for misere!");
		changeStrat = true;
	}
	
	private void miserify(int[] sit) {
		if (!misere)
			return;
		else {
			for (int heap: sit) {
				if (heap > 1)
					return;
			}
			changeStrat = true;
		}
	}
	
	private void removePearls(int removeRow, int nbToRemoveMatches) {
		updatePearlArrangement(removeRow, -nbToRemoveMatches);

		List<Actor> removeCandidates = new ArrayList<Actor>();

		for (Actor p: gg.getActors(Pearl.class)) {
			if (p.getY() == removeRow)
				removeCandidates.add(p);
		}
		Collections.shuffle(removeCandidates);
		while (nbToRemoveMatches > 0) {
			Actor removedPearl = removeCandidates.remove(0);
			removedPearl.removeSelf();
			nbToRemoveMatches--;
		}
	}

	// for debugging only
	private String toString(int[] k) {
		String output = "";
		for (int i = 0; i < k.length; i++)
			output = (output + k[i] + ", ");
		return output;
	}

	/*
	 * removes a match from a situation given the: "sit", the original situation
	 * "row" the row where a match should be removed from
	 */

	private int[] makeSituation(int[] sit, int row) {
		sit[row] = sit[row] - 1;
		return sit;
	}

	// Check if its a U-Situation

	private Boolean isUSituation(int[] sit) {
		int[] allDuals = new int[dualMax];
		int[] oneDual = new int[dualMax];
		for (int y = 0; y < vertCells; y++) {
			oneDual = toDual(sit[y]);
			for (int i = 0; i < allDuals.length; i++) {
				allDuals[i] = allDuals[i] + oneDual[i];
			}
		}
		for (int i = 0; i < allDuals.length; i++) {
			if (allDuals[i] % 2 == 1)
				return true;
		}
		return false;
	}

	/**
	 * gives an integer back as binary (as array of integer) only works for
	 * input < 16, or arraylength "dualMax" must be changed
	 */
	private int[] toDual(int input) {
		int[] output = new int[dualMax]; // 4 dualstellen
		int x = 0;
		while (input != 0) {
			output[x] = input % 2;
			input = input / 2;
			x++;
		}
		return output;
	}
	
	public void reset() {
		pearlArrangement = new int[vertCells];
		changeStrat = false;
	}
}
