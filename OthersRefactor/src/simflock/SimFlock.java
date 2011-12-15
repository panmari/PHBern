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
public class SimFlock extends GameGrid
{ private static final long serialVersionUID = -1113582265865921787L; // avoids warning in Eclipse: The serializable class...
// Change game parameters /////////////////////////////////////////
  protected static final int nh = 600;                // Number of cells: Height of playground
  protected static final int nw = 1000;               // Number of cells: Width of playground;
  private static final int nb = 100;                  // Number of birds
  private static final int nr = 1;                    // Number of raptors
  protected static final double rom = Math.sqrt(nw*nw+nh*nh);   // Maximum Radius of observation
  private static final int rseed=123;               // Random Seed
  protected static final  boolean drawTrace = false;  // Draw traces of birds/raptors
  protected static final  double  timeFactor = 4;     // Descrete Timestep for calculation of acc/vel/pos
  // Change behavior of birds /////////////////////////////////////////
  protected static final double vbird = 1;            // Magnitude of velocity: birds
  protected static final double rob = 50;//50;             // Radius of observation: birds
  protected static final double rcrit = 20;//20;           // Critical Radius: birds
  protected static final double cohesionFactor = 0.001;//0.001; 
  protected static final double separationFactor = 0.1;//0.05;
  protected static final double alignmentFactor = 0.05;//0.05;
  protected static final double escapeFactor = 0.001; //0.001;
  // Change behavior of raptors ////////////////////////////////////////
  protected static final double vraptor = 1.1;  //1.1          // Magnitude of velocity: raptors
  protected static final double ror = rom;              // Radius of observation: raptors
  protected static final double aggressionFactor = 0.001;
  /////////////////////////////////////////////////////////////////////
  protected final FlockBird[] birds = new FlockBird[nb];
  protected final Raptor[] raptors = new Raptor[nr];
  

  public SimFlock()
  { 
    super(nw,nh, 1, null, true);
    setSimulationPeriod(50);
    Random rand = new Random(rseed);   // use Random(rseed) for testing: change value of static final rseed
    
    for (int i = 0; i < nb; i++)  // Generate birds
    {
      GGVector startVelocity = new GGVector(vbird,0);
      startVelocity.rotate(rand.nextInt(360));  
      birds[i] = new FlockBird(startVelocity);
      addActor(birds[i], new Location(rand.nextInt(nw), rand.nextInt(nh)));
    }
    for (int i = 0; i < nr; i++)  // Generate birds
    {
      GGVector startVelocity = new GGVector(vraptor,0);
      startVelocity.rotate(rand.nextInt(360));  
      raptors[i] = new Raptor(startVelocity);
      addActor(raptors[i], new Location(rand.nextInt(nw), rand.nextInt(nh)));
    }
    show();
  }

  public static void main(String[] args)
  {
    new SimFlock();
  }
}

//////////////////////////////////////CLASS Bird ///////////////////////////////////////////////////////
class Bird extends Actor 
{
  protected Location oldLocation = new Location(-1, -1);
  protected GGVector startVelocity;
  protected GGVector velocity;
  protected GGVector position;
  protected boolean  borderCrossed;

  public Bird(GGVector startVelocity, String sprite)
  {
    super(true, sprite); 
	this.startVelocity = startVelocity;
	this.velocity=startVelocity;
	this.borderCrossed=false;
  }
  
  @Deprecated
  protected GGVector setAccelerationToZero()
  {
	return new GGVector(0,0);
  }
  
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
		{ x = x + SimFlock.nw; borderCrossed=true;}
	else if (x>SimFlock.nw-1) 
		{ x = x - SimFlock.nw; borderCrossed=true;}
	if (y < 0) 
		{y = y + SimFlock.nh; borderCrossed=true;}
	else if (y > SimFlock.nh-1) 
		{y = y - SimFlock.nh; borderCrossed=true;}
    return new GGVector(x,y);
  }
  
  protected GGVector getPositionVecDiffOnTorus(Bird bird)
  {   GGVector delta = new GGVector();
	  double mag; double magmin = SimFlock.rom;
      int imin = 0; int jmin = 0;
      
	  for (int i = -1; i < 2; i++)
	  {
		  for (int j = -1; j < 2; j++)
		  {
			delta.x = i*SimFlock.nw; delta.y =j*SimFlock.nh;
			mag = position.sub(bird.getPosition().add(delta)).magnitude();
			if (mag<magmin) {magmin = mag; imin = i; jmin=j;}
		  }
	  }	  
	  delta.x = imin*SimFlock.nw; delta.y =jmin*SimFlock.nh;
	  return position.sub(bird.getPosition().add(delta)); 
  }
}

//////////////////////////////////////CLASS FlockBird ///////////////////////////////////////////////////////

class FlockBird extends Bird
{	  
  public FlockBird(GGVector startVelocity)
  {
    super(startVelocity,"sprites/bird.gif");
  }
  
  private GGVector setAcceleration()  //Behavior of the bird: acceleration
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
	  
	   if ((distVec.magnitude() <= SimFlock.rob) && (distVec.magnitude() > SimFlock.rcrit))
	      {cohDistSumVec.x+=distVec.x;cohDistSumVec.y+=distVec.y; ccounter++;}
	   if (distVec.magnitude() <= SimFlock.rcrit) 
	      {sepDistSumVec.x+=distVec.x;sepDistSumVec.y+=distVec.y; scounter++;}
	   if (distVec.magnitude() <= SimFlock.rob) 
	      {aligVelSumVec.x+=bird.velocity.x; aligVelSumVec.y+=bird.velocity.y; acounter++;}
	  }
	  
	  for (Actor neighbour : neighbourRaptors)
	  {
	   Raptor raptor = (Raptor)neighbour;
	   distVec = getPositionVecDiffOnTorus(raptor);
	   if (distVec.magnitude() <= SimFlock.rob) 
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
  
  public void act()
  {
	  GGVector acceleration = setAcceleration();
	  velocity = velocity.add(acceleration.mult(SimFlock.timeFactor));
	  velocity.normalize();
	  velocity = velocity.mult(SimFlock.vbird);
	  position = position.add(velocity.mult(SimFlock.timeFactor));
	  position = toTorusPosition(position);
	  Location location = toLocation(position);
	  if (borderCrossed) {oldLocation.x = -1; oldLocation.y = -1;}
	  setDirection(Math.toDegrees(velocity.getDirection()));
	  setLocation(location);
	  
	  if (SimFlock.drawTrace)
	    {
		  getBackground().setPaintColor(Color.blue);
	      if (oldLocation.x != -1)
	        getBackground().drawLine(oldLocation.x, oldLocation.y, location.x, location.y);
	        oldLocation.x = location.x; oldLocation.y = location.y;
	    }
  }
}

////////////////////////////////////// CLASS RAPTOR ///////////////////////////////////////////////////////

class Raptor extends Bird
{
	  
  public Raptor(GGVector startVelocity)
  {
    super(startVelocity,"sprites/raptor.gif"); 
  }
  
  private GGVector setAcceleration()  //Behavior of the RAPTOR: acceleration
  {
	  GGVector distVec = new GGVector();
	  GGVector nearestDistVecBird = new GGVector(0,0);
	  GGVector accAggression = new GGVector(0,0);
	  GGVector acceleration = new GGVector();
	  double mag; double minmag = SimFlock.rom;
	  ArrayList<Actor> neighbourBirds = gameGrid.getActors(FlockBird.class);
	  
	  for (Actor neighbour : neighbourBirds)
	  {
	   FlockBird bird = (FlockBird)neighbour;
	   distVec = getPositionVecDiffOnTorus(bird);
	   mag = distVec.magnitude();
	   if (mag <= SimFlock.ror) 
	      {if (mag<minmag) {minmag = mag; nearestDistVecBird = distVec.clone();}}
	  }
	  accAggression = nearestDistVecBird.mult(-SimFlock.aggressionFactor);
	  acceleration.x = accAggression.x; 
	  acceleration.y = accAggression.y;
	return acceleration;
  }
  
  public void act()
  {
	  GGVector acceleration = setAcceleration();
	  velocity = velocity.add(acceleration.mult(SimFlock.timeFactor));
	  velocity.normalize();
	  velocity = velocity.mult(SimFlock.vraptor);
	  position = position.add(velocity.mult(SimFlock.timeFactor));
	  position = toTorusPosition(position);
	  Location location = toLocation(position);
	  if (borderCrossed) {oldLocation.x = -1; oldLocation.y = -1;}
	  setDirection(Math.toDegrees(velocity.getDirection()));
	  setLocation(location);
	  
	  if (SimFlock.drawTrace)
	    {
		  getBackground().setPaintColor(Color.blue);
	      if (oldLocation.x != -1)
	        getBackground().drawLine(oldLocation.x, oldLocation.y, location.x, location.y);
	        oldLocation.x = location.x; oldLocation.y = location.y;
	    }
  }
}
