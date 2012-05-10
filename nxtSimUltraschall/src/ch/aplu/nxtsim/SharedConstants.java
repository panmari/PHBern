// SharedConstants.java

/*
This software is part of the NxtSim library.
It is Open Source Free Software, so you may
- run the code for any purpose
- study how the code works and adapt it to your needs
- integrate all or parts of the code in your own programs
- redistribute copies of the code
- improve the code and release your improvements to the public
However the use of the code is entirely your responsibility.

Author: Aegidius Pluess, www.aplu.ch
*/

/* History:
 * V1.00 - Aug 2010: - First official release, all basic features implemented
 * V1.01 - Aug 2010: - Modified rotation speed when motors turn with same speed
                     - Added: automatic call of _init()
                     - Modified NxtContext.usePlayground() to useObstacle()
                     - Added: NxtRobot.getVersion()
 * V1.02 - Sep 2010: - Added: class SoundSensor, class SoundListener
                     - Modified NxtContext.usePlayground() to useObstacle()
                     - Added: NxtRobot.getVersion()
 * V1.03 - Oct 2010  - Added: Gear.isMoving(), Motor.isMoving()
 * V1.04 - Oct 2010  - Added: class SoundAdapter
 * V1.05 - Oct 2010  - LightSensor, SoundSensor event callbacks in own thread
                     - NxtRobot: critical methods synchronized
 * V1.06 - Oct 2010  - Move window to upper left screen corner
                     - NxtRobot.exit() stops all movement now
 * V1.07 - Nov 2010  - Added LightSensor.addLightListener() with default level
                     - Added LightSensor.setTriggerLevel()
                     - Added SoundSensor.addLightListener() with default level
                     - Added SoundSensor.setTriggerLevel()
 * V1.08 - Oct 2011  - Modified: Nxt.getPartLocation() is public now
 * V1.09 - Oct 2011  - Modified: turn angular speed when two motors have same speed is
 *                     speed dependent now
 *                   - Added NxtRobot.reset() to set robot at start position/direction
 * V1.10 - Oct 2011  - Modified: Turn rotation step is 1 degree now (instead of 6)
 * V1.11 - Feb 2012  - Fixed: LightSensor bright(), dark() now return port correctly
 * V1.12 - Mar 2012  - Added: Button.isDown() for compatiblity with leJOS 0.91
 *                   - Fixed: Blocking methods wait with Thread.sleep(1) now
 *                   - Modified: polling methods calls Thread.sleep(1) now
 */

package ch.aplu.nxtsim;  

interface SharedConstants
{
  int DEBUG_LEVEL_OFF = 0;
  int DEBUG_LEVEL_LOW = 1;
  int DEBUG_LEVEL_MEDIUM = 2;
  int DEBUG_LEVEL_HIGH = 3;

  int DEBUG = DEBUG_LEVEL_OFF;

  int simulationPeriod = 30;
  int nbSteps = 3;  // Number of pixels advances per simulation period
  double motTurnAngle = 15;  // Angle per simulation period (in degrees) when motors have same speed of 50
  double gearTurnAngle = 22.5; // Angle per simulation period (in degrees) when gear turns around center
  double motorRotIncFactor = 2.0;  // Factor that determines motor rotation speed
  double gearRotIncFactor = 3.0;   // Factor that determines gear rotation speed
  int pixelPerMeter = 200; // Distance corresponding to 1 meter
  int defaultSpeed = 50;  // Default gear and motor speed


  String ABOUT =
    "2003-2012 Aegidius Pluess\n" +
    "OpenSource Free Software\n" +
    "http://www.aplu.ch\n" +
    "All rights reserved";
  String VERSION = "1.12 - March 2012";
}
