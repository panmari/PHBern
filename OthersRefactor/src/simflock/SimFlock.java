package simflock;

import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.util.*;
import java.lang.Math;

/**
* Based on the file newton.java from http://www.gamegrid.ch (Jarka Arnold)
* Simulation of the behavior of a flock of birds and raptors on the surface of a torus
* Based on the JGameGrid-Framework from Aegidius Plüss (http://www.aplu.ch)
* @author Martin Schellenberg (baal@gmx.net) 2011
*/
@SuppressWarnings("serial")
public class SimFlock extends GameGrid
{ 
// Change game parameters /////////////////////////////////////////
  private static final int height = 600;                // Number of cells: Height of playground
  private static final int width = 1000;               // Number of cells: Width of playground;
  private static final int nbFlockBirds = 100;         // Number of FlockBirds
  private static final int nbRaptors = 1;                    // Number of raptors
  protected static final double maxro = Math.sqrt(width*width+height*height);   // Maximum Radius of observation
  private static final int randomSeed=123;               // For initializing birds locations
  protected static final  boolean drawTrace = true;  // Draw traces of FlockBirds/raptors
  protected static final  double  timeFactor = 4;     // Descrete Timestep for calculation of acc/vel/pos
  // Change behavior of birds /////////////////////////////////////////
  protected static final double magnBird = 1;            // Magnitude of velocity: FlockBirds
  protected static final double roFb = 50;//50;             // Radius of observation: FlockBirds
  protected static final double rcrit = 20;//20;           // Critical Radius: FlockBirds
  protected static final double cohesionFactor = 0.001;//0.001; 
  protected static final double separationFactor = 0.1;//0.05;
  protected static final double alignmentFactor = 0.05;//0.05;
  protected static final double escapeFactor = 0.001; //0.001;
  // Change behavior of raptors ////////////////////////////////////////
  protected static final double magnRaptor = 1.1;  //1.1          // Magnitude of velocity: raptors
  protected static final double roR = maxro;              // Radius of observation: raptors
  protected static final double aggressionFactor = 0.001;
  ///////////////////////////////////////////////////////////////////// 

  public SimFlock()
  { 
    super(width, height, 1, null, true);
    setSimulationPeriod(50);
    Random rand = new Random(randomSeed);
    
    for (int i = 0; i < nbFlockBirds; i++)  // Generate FlockBirds
    {
      GGVector startVelocity = new GGVector(magnBird,0);
      startVelocity.rotate(rand.nextInt(360));  
      FlockBird fb = new FlockBird(startVelocity, magnBird);
      addActor(fb, new Location(rand.nextInt(width), rand.nextInt(height)));
    }
    for (int i = 0; i < nbRaptors; i++)  // Generate Raptors
    {
      GGVector startVelocity = new GGVector(magnRaptor,0);
      startVelocity.rotate(rand.nextInt(360));  
      Raptor raptor = new Raptor(startVelocity, magnRaptor);
      addActor(raptor, new Location(rand.nextInt(width), rand.nextInt(height)));
    }
    show();
  }
  
  public void reset() {
	  getBg().clear();
  }

  public static void main(String[] args)
  {
    new SimFlock();
  }
}

/**
 * An abstract class, that should be extended any kind of bird in the simulation.
 * Supplies many torus related methods.
 * By overwriting the method <code> setAcceleration() </code> you can change
 * the moving behavior of the child bird.
 */
abstract class Bird extends Actor 
{
  protected Location oldLocation = new Location(-1, -1);
  protected GGVector startVelocity;
  protected GGVector velocity;
  protected GGVector position;
  protected boolean  borderCrossed;
  protected double velocityMagnitude;

  public Bird(GGVector startVelocity, String sprite, double velocityMagnitude)
  {
    super(true, sprite); 
	this.startVelocity = startVelocity;
	this.velocity=startVelocity;
	this.borderCrossed=false;
	this.velocityMagnitude = velocityMagnitude;
  }
  
  protected abstract GGVector setAcceleration();
  protected abstract Color getColor();
  protected abstract void tryToEat();
  
  public void reset()
  {
	position = toPosition(getLocationStart());
	velocity = startVelocity;
    setDirection(Math.toDegrees(startVelocity.getDirection()));
    oldLocation.x = -1; oldLocation.y = -1;
  }
  
  public GGVector getPosition()
  {
    return position.clone();
  }

  public GGVector getVelocity()
  {
    return velocity.clone();
  }
  
  private GGVector toPosition(Location location)
  {
    return new GGVector(location.x, location.y);
  }

  protected Location toLocation(GGVector position)
  {
    return new Location((int)(position.x), (int)(position.y));
  }

  protected GGVector toTorusPosition(GGVector position)
  {
    double x=position.x; double y=position.y;  borderCrossed=false;
	if (x < 0) 
		{ x = x + getNbHorzCells(); borderCrossed=true;}
	else if (x > getNbHorzCells()-1) 
		{ x = x - getNbHorzCells(); borderCrossed=true;}
	if (y < 0) 
		{y = y + getNbVertCells(); borderCrossed=true;}
	else if (y > getNbVertCells()-1) 
		{y = y - getNbVertCells(); borderCrossed=true;}
    return new GGVector(x,y);
  }
  
  protected GGVector getPositionVecDiffOnTorus(Bird bird)
  {   GGVector delta = new GGVector();
	  double mag; double magmin = SimFlock.maxro;
      int imin = 0; int jmin = 0;
      
	  for (int i = -1; i < 2; i++)
	  {
		  for (int j = -1; j < 2; j++)
		  {
			delta.x = i*getNbHorzCells(); delta.y =j*getNbVertCells();
			mag = position.sub(bird.getPosition().add(delta)).magnitude();
			if (mag<magmin) {magmin = mag; imin = i; jmin=j;}
		  }
	  }	  
	  delta.x = imin*getNbHorzCells(); delta.y =jmin*getNbVertCells();
	  return position.sub(bird.getPosition().add(delta)); 
  }
  
  public void act()
  {
	  GGVector acceleration = setAcceleration();
	  velocity = velocity.add(acceleration.mult(SimFlock.timeFactor));
	  velocity.normalize();
	  velocity = velocity.mult(velocityMagnitude);
	  position = position.add(velocity.mult(SimFlock.timeFactor));
	  position = toTorusPosition(position);
	  Location location = toLocation(position);
	  if (borderCrossed) {oldLocation.x = -1; oldLocation.y = -1;}
	  setDirection(Math.toDegrees(velocity.getDirection()));
	  setLocation(location);
	  
	  tryToEat();
	  
	  if (SimFlock.drawTrace)
	    {
		  getBackground().setPaintColor(getColor());
	      if (oldLocation.x != -1)
	        getBackground().drawLine(oldLocation.x, oldLocation.y, location.x, location.y);
	        oldLocation.x = location.x; oldLocation.y = location.y;
	    }
  }
}

//////////////////////////////////////CLASS FlockBird ///////////////////////////////////////////////////////

class FlockBird extends Bird
{	  
  public FlockBird(GGVector startVelocity, double vbird)
  {
    super(startVelocity,"sprites/bird.gif", vbird);
  }
  
  @Override
  protected GGVector setAcceleration()  //Behavior of the bird: acceleration
  {
	  GGVector distVec = new GGVector();
	  GGVector cohDistSumVec = new GGVector(0,0);
	  GGVector sepDistSumVec = new GGVector(0,0);
	  GGVector escDistSumVec = new GGVector(0,0);
	  GGVector aligVelSumVec = new GGVector(0,0);
	  GGVector acceleration = new GGVector(0,0);
	  GGVector accCohesion = new GGVector(0,0);
	  GGVector accSeparation = new GGVector(0,0);
	  GGVector accAlignment = new GGVector(0,0);
	  GGVector accEscape = new GGVector(0,0);
	  int ccounter = 0; int scounter = 0; int acounter = 0; int rcounter = 0;
	  ArrayList<Actor> neighbourBirds = gameGrid.getActors(FlockBird.class);
	  neighbourBirds.remove(this);  // Remove self
	  ArrayList<Actor> neighbourRaptors = gameGrid.getActors(Raptor.class);
	  
	  for (Actor neighbour : neighbourBirds)
	  {
	   FlockBird bird = (FlockBird)neighbour;
	   distVec = getPositionVecDiffOnTorus(bird);
	  
	   if ((distVec.magnitude() <= SimFlock.roFb) && (distVec.magnitude() > SimFlock.rcrit))
	      {cohDistSumVec.x+=distVec.x;cohDistSumVec.y+=distVec.y; ccounter++;}
	   if (distVec.magnitude() <= SimFlock.rcrit) 
	      {sepDistSumVec.x+=distVec.x;sepDistSumVec.y+=distVec.y; scounter++;}
	   if (distVec.magnitude() <= SimFlock.roFb) 
	      {aligVelSumVec.x+=bird.velocity.x; aligVelSumVec.y+=bird.velocity.y; acounter++;}
	  }
	  
	  for (Actor neighbour : neighbourRaptors)
	  {
	   Raptor raptor = (Raptor)neighbour;
	   distVec = getPositionVecDiffOnTorus(raptor);
	   if (distVec.magnitude() <= SimFlock.roFb) 
	      {escDistSumVec.x+=distVec.x;escDistSumVec.y+=distVec.y; rcounter++;}
	  }
	  
	  if (ccounter != 0) {cohDistSumVec.mult(1/ccounter);}
	  if (scounter != 0) {sepDistSumVec.mult(1/scounter);}
	  if (rcounter != 0) {escDistSumVec.mult(1/rcounter);}
	  if (acounter != 0) {aligVelSumVec.mult(1/acounter);}
	  accCohesion = cohDistSumVec.mult(-SimFlock.cohesionFactor);
	  accSeparation = sepDistSumVec.mult(SimFlock.separationFactor);
	  accAlignment =  velocity.sub(aligVelSumVec).mult(-SimFlock.alignmentFactor);
	  accEscape = escDistSumVec.mult(SimFlock.escapeFactor);
	  acceleration.x = accCohesion.x + accSeparation.x + accAlignment.x;
	  acceleration.y = accCohesion.y + accSeparation.y + accAlignment.y;
	  if (rcounter > 0) {acceleration.x = accEscape.x; acceleration.y = accEscape.y;}
	return acceleration;
  }

@Override
protected Color getColor() {
	return Color.BLUE;
}

@Override
protected void tryToEat() {
	//A Flockbird doesn't need to eat
}
}

////////////////////////////////////// CLASS RAPTOR ///////////////////////////////////////////////////////

class Raptor extends Bird
{
	  
  public Raptor(GGVector startVelocity, double vraptor)
  {
    super(startVelocity,"sprites/raptor.gif", vraptor); 
  }
  
  @Override 
  protected GGVector setAcceleration()  //Behavior of the RAPTOR: acceleration
  {
	  GGVector distVec = new GGVector();
	  GGVector nearestDistVecBird = new GGVector(0,0);
	  GGVector accAggression = new GGVector(0,0);
	  GGVector acceleration = new GGVector();
	  double mag; double minmag = SimFlock.maxro;
	  ArrayList<Actor> neighbourBirds = gameGrid.getActors(FlockBird.class);
	  
	  for (Actor neighbour : neighbourBirds)
	  {
	   FlockBird bird = (FlockBird)neighbour;
	   distVec = getPositionVecDiffOnTorus(bird);
	   mag = distVec.magnitude();
	   if (mag <= SimFlock.roR) 
	      {if (mag<minmag) {minmag = mag; nearestDistVecBird = distVec.clone();}}
	  }
	  accAggression = nearestDistVecBird.mult(-SimFlock.aggressionFactor);
	  acceleration.x = accAggression.x; 
	  acceleration.y = accAggression.y;
	return acceleration;
  }
  
  @Override
  protected Color getColor() {
  	return Color.RED;
  }
  
  @Override
  protected void tryToEat() {
	ArrayList<Actor> victim = getNeighbours(2, FlockBird.class);
  	if (!victim.isEmpty()) {
  		victim.get(0).removeSelf();
  }
  }
}

