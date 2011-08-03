package schwarzerPeter;

// BlinkingName.java

import ch.aplu.jgamegrid.*;
import java.awt.*;

public class BlinkingName extends TextActor
{
  private boolean isBlinkingEnabled = false;

  public BlinkingName(String name)
  {
    super(true, name, Color.white,
        new Color(255, 255, 255, 0), new Font("Arial", Font.PLAIN, 16));
    setSlowDown(20);
 }

  public void act()
  {
    if (!isBlinkingEnabled)
    {
      if (!isVisible())
        show();
      return;
    }
    if (isVisible())
      hide();
    else
      show();
  }

  public void setBlinkingEnabled(boolean enable)
  {
    isBlinkingEnabled = enable;
  }

}
