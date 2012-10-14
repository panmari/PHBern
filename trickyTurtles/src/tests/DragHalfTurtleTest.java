package tests;

import static org.junit.Assert.*;
import generator.DragHalfTurtle;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class DragHalfTurtleTest {

	List<DragHalfTurtle> availableTurtles;
	@Before
	public void setUp() {
		availableTurtles = new LinkedList<DragHalfTurtle>();
		availableTurtles.add(new DragHalfTurtle("bf", "sprites/blau_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("bb", "sprites/blau_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("gf", "sprites/gruen_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("gb", "sprites/gruen_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("rf", "sprites/braun_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("rb", "sprites/braun_hinten.png"));
		availableTurtles.add(new DragHalfTurtle("yf", "sprites/br_bl_vorne.png"));
		availableTurtles.add(new DragHalfTurtle("yb", "sprites/br_bl_hinten.png"));	
	}
	
	@Test
	public void shouldFindIdOfCounterPartWhenClone() {
		DragHalfTurtle ht = availableTurtles.get(3).clone();
		assertEquals(2, ht.getIdOfCounterpart());
	}
}
