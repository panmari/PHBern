
import javax.swing.JOptionPane;
import ch.aplu.jgamegrid.*;
import java.awt.Color;
import java.lang.*;

public class Engine extends GameGrid implements GGMouseListener{
    
    Object selectedDifficulty;
    Object[] allDifficulty;
    
    Grid grid;
    int xMax, yMax, mines;
    
    private Field[][] fields;
    private GGField[][] ggfields;
    private ActorTools[][] atools;
    private Tools tools;
    
    GGMouseListener ml;
    
    public Engine(){
        allDifficulty = new Object[]{ "Beginner", "Advanced", "Professional", "Expert", "Custom" };
        selectedDifficulty = JOptionPane.showInputDialog(null, "Bitte wählen Sie den gewünschten Schwierigkeitsgrad aus", "Eingabe", JOptionPane.INFORMATION_MESSAGE, null, allDifficulty, allDifficulty[0]);
        if(selectedDifficulty==allDifficulty[0])
        {/*Beginner*/
            new Engine(7,7,5);
        }
        else if(selectedDifficulty==allDifficulty[1])
        {/*Advanced*/
            new Engine(9,9,10);
        }
        else if(selectedDifficulty==allDifficulty[2])
        {/*Pro*/
            new Engine(12,12,24);
        }
        
        else if(selectedDifficulty==allDifficulty[3])
        {/*Expert*/
            new Engine(16,16,60);
        }
        else if(selectedDifficulty==allDifficulty[4])
        {/*Custom*/
            int x,y,m;
            try
            {
                x = Integer.parseInt(JOptionPane.showInputDialog(null,"X"));
                y = Integer.parseInt(JOptionPane.showInputDialog(null,"Y"));
                m = Integer.parseInt(JOptionPane.showInputDialog(null,"Mines"));
                new Engine(x,y,m);
            }
            catch(NumberFormatException e)
            {
            }
       }
  }
  private Engine(int x,int y, int m)
  {
      super(x, y, 25, null, "sprites/bg.gif", false);
      
      this.xMax = x;
      this.yMax = y;
      this.mines = m;
      
      this.tools = new Tools();
      
      grid = new Grid(x,y,m);
      fields = grid.getGrid();
      
      ggfields = new GGField[x][y];
      atools = new ActorTools[x][y];
      
      for (int i = 0; i < x; i++) {
          for (int j = 0; j < y; j++) {
              ggfields[i][j] = new GGField(11);
              addActor(ggfields[i][j], new Location(i,j));
              atools[i][j] = new ActorTools(ggfields[i][j],fields[i][j]);
          }
      }
      
      addMouseListener(this,GGMouse.lPress|GGMouse.rPress);
      refresh();
      //actAll();
      show();
  }

    @Override
    public boolean mouseEvent(GGMouse ggm) {
        
        Location location = toLocationInGrid(ggm.getX(), ggm.getY());
    if (ggm.getEvent() == GGMouse.lPress)
    {
      fields = tools.uncover(fields, fields[location.x][location.y]);
      ggfields = tools.uncover(fields,fields[location.x][location.y],ggfields);
      /*for (int i = 0; i < xMax; i++) {
          for (int j = 0; j < yMax; j++) {*/
              //getOneActorAt(location).show(ggfields[location.getX()][location.getY()].id);
      //removeActorsAt(location);
              //addActor(ggfields[location.getX()][location.getY()], location);
          /*}
      }*/
      //actAll();
      
    }
    if (ggm.getEvent() == GGMouse.rPress)
    {
      fields = tools.toggleFlag(fields, fields[location.x][location.y]);
      ggfields = tools.toggleFlag(fields,fields[location.x][location.y],ggfields);
      /*for (int i = 0; i < xMax; i++) {
          for (int j = 0; j < yMax; j++) {*/
              getOneActorAt(location).show(ggfields[location.getX()][location.getY()].id);
              //removeActorsAt(location);
              //addActor(ggfields[location.getX()][location.getY()], location);
          /*}
      }*/
      //actAll();
    }
    refresh();
    return true;
    }
}
