// CarloFrame.java

import ch.aplu.jgamegrid.*;
import javax.swing.*;
import java.awt.*;

public class CarloFrame extends JFrame implements GGToggleButtonListener
{
  private GGToggleButton tb1 =
    new GGToggleButton("sprites/commutator.gif", true);
  private TextActor ta1 = new TextActor(true, "Bonsoir",
    Color.white, new Color(0, 0, 0, 0), new Font("SansSerif", Font.BOLD, 32));
  private GGTextField tf1;
  private Clownfish fish;
 
  public CarloFrame()
  {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);
    GameGrid gg1 = new GameGrid();
    gg1.setSimulationPeriod(100);
    gg1.setBgImagePath("sprites/gg1.png");
    gg1.setCellSize(60);
    gg1.setNbHorzCells(10);
    gg1.setNbHorzCells(10);
    getContentPane().add(gg1, BorderLayout.WEST);
    gg1.doRun();

    GameGrid gg2 = new GameGrid();
    gg2.setSimulationPeriod(200);
    gg2.setBgImagePath("sprites/gg2.png");
    gg2.setCellSize(1);
    gg2.setNbHorzCells(200);
    gg2.setNbVertCells(600);
    getContentPane().add(gg2, BorderLayout.EAST);
    gg2.doRun();

    pack();  // Must be called before actors are added!

    gg2.addActor(tb1, new Location(50, 270));
    tb1.addToggleButtonListener(this);
    gg2.addActor(ta1, new Location(20, 200));
    tf1 = new GGTextField(gg2, "#turns: 0", new Location(20, 400), false);
    tf1.setFont(new Font("Arial", Font.BOLD, 32));
    tf1.show();


    fish = new Clownfish(this);
    gg1.addActor(fish, new Location(2, 6));
    
    gg2.addActor(new Dice(), new Location(100, 320));

  }

  protected void setTurns(int nbTurns)
  {
    tf1.setText("#turns: " + nbTurns);
  }

  public void buttonToggled(GGToggleButton button, boolean toggled)
  {
    if (button == tb1)
      fish.setLocation(new Location(fish.getX(), toggled ? 4 : 6));
  }

  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run()
      {
        new CarloFrame().setVisible(true);
      }

    });
  }

}