import ch.aplu.jgamegrid.*;

public class GGField extends Actor
{
    int id;
  public GGField()
  {
    super("sprites/button.gif",12);
    id = 11;
    show(id);
  }
  public GGField(int id)
  {
    super("sprites/button.gif",12);
    this.id = id;
    show(id);
  }
  public void setID(int id)
  {
      this.id = id;
      show(id);
  }
  public void act()
  {
      //show(id);
  }
} 