// ModelessOptionPane.java

package ch.aplu.util;

/*
  This software is part of the JEX (Java Exemplarisch) Utility Library.
  It is Open Source Free Software, so you may
    - run the code for any purpose
    - study how the code works and adapt it to your needs
    - integrate all or parts of the code in your own programs
    - redistribute copies of the code
    - improve the code and release your improvements to the public
  However the use of the code is entirely your responsibility.
 */


import javax.swing.*;
import java.awt.event.*;
import java.net.URL;
import java.awt.*;

/**
 * Modeless message dialog using Swing JOptionPane.
 * Useful to show text and/or images while the application continues to run.<br><br>
 * All Swing methods are invoked in the EDT.
 */
public class ModelessOptionPane
{
  // ---------------- Inner class SetText ----------------
  private class SetText implements Runnable
  // Used in order to call Swing methods from EDT only
  {
    private String text;
    private boolean adjust;

    private SetText(String text, boolean adjust)
    {
      this.text = text;
      this.adjust = adjust;
    }

    public void run()
    {
      optionPane.setMessage(text);
      if (adjust)
        dlg.pack();  // Adapt size to new message
    }
  }

  // ---------------- Inner class SetTitle ----------------
  private class SetTitle implements Runnable
  // Used in order to call Swing methods from EDT only
  {
    private String title;

    private SetTitle(String title)
    {
      this.title = title;
    }

    public void run()
    {
      dlg.setTitle(title);
    }
  }

  // ---------------- End of inner class -----------------

  private JDialog dlg;
  private JOptionPane optionPane;
  private static Cleanable cleanable = null;

  /**
   * Show a modeless message dialog at given position (upper left corner)
   * containing given text and given icon image (gif or jpg).
   * iconUrl is the URL for the icon resource. The iconUrl is normally
   * retrieved by calling<br><br>
   * <code>
   * ClassLoader loader = getClass().getClassLoader();<br>
   * URL iconUrl = loader.getResource(iconResource);
   * </code><br><br>
   * where iconResource is a '/'-separated path name that identifies the resource.
   * If the resource is not found or iconUrl == null, the default icon (exclamation)
   * is displayed.
   * When the titlebar's close button is hit, the application will
   * terminate by calling System.exit(0).
   * To change this behaviour, register the cleanable interface.<br><br>
   * Runs in the Event Dispatch Thread (EDT).
 */
  public ModelessOptionPane(final int ulx, final int uly, final String text, final URL iconUrl)
  {
    if (EventQueue.isDispatchThread())
      createModelessOptionPane(ulx, uly, text, iconUrl);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
            createModelessOptionPane(ulx, uly, text, iconUrl);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createModelessOptionPane(int ulx, int uly, String text, URL iconUrl)
  {
    ImageIcon icon;
    if (iconUrl == null)
      icon = null;  // Will display defaut icon
    else
      icon = new ImageIcon(iconUrl);
    init(text, icon, true);
    dlg.setLocation(ulx, uly);
    dlg.setVisible(true);
  }

  /**
   * Same as ModelessOptionPane(ulx, uly, text, iconUrl) with no icon.
   */
  public ModelessOptionPane(final int ulx, final int uly, final String text)
  {
    if (EventQueue.isDispatchThread())
      createModelessOptionPane(ulx, uly, text);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
            createModelessOptionPane(ulx, uly, text);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createModelessOptionPane(int ulx, int uly, String text)
  {
    init(text, null, false);
    dlg.setLocation(ulx, uly);
    dlg.setVisible(true);
  }

  /**
   * Same as ModelessOptionPane(ulx, uly, text, iconUrl) but centered
   * in middle of the screen.
   */
  public ModelessOptionPane(final String text, final URL iconUrl)
  {
    if (EventQueue.isDispatchThread())
      createModelessOptionPane(text, iconUrl);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
            createModelessOptionPane(text, iconUrl);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createModelessOptionPane(String text, URL iconUrl)
  {
    ImageIcon icon;
    if (iconUrl == null)
      icon = null;  // Will display defaut icon
    else
      icon = new ImageIcon(iconUrl);
    init(text, icon, true);
    int wWidth = dlg.getWidth();
    int wHeight = dlg.getHeight();
    Fullscreen fs = new Fullscreen();
    int ulx = (int)((fs.getWidth() - wWidth) / 2.0);
    int uly = (int)((fs.getHeight() - wHeight) / 2.0);
    dlg.setLocation(ulx, uly);
    dlg.setVisible(true);
  }

  /**
   * Same as ModelessOptionPane(text, iconUrl) with no icon.
   */
  public ModelessOptionPane(final String text)
  {
    if (EventQueue.isDispatchThread())
      createModelessOptionPane(text);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
            createModelessOptionPane(text);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  private void createModelessOptionPane(String text)
  {
    init(text, null, false);
    int wWidth = dlg.getWidth();
    int wHeight = dlg.getHeight();
    Fullscreen fs = new Fullscreen();
    int ulx = (int)((fs.getWidth() - wWidth) / 2.0);
    int uly = (int)((fs.getHeight() - wHeight) / 2.0);
    dlg.setLocation(ulx, uly);
    dlg.setVisible(true);
  }

  private void init(String text, ImageIcon icon, boolean isIcon)
  {
    dlg = new JDialog()
    {
      protected void processWindowEvent(WindowEvent e)
      {
        if (e.getID() == WindowEvent.WINDOW_CLOSING)
        {
          if (cleanable != null)
            cleanable.clean();
          else
            System.exit(0);
        }
      }
    };
    optionPane = new JOptionPane(text);
    optionPane.setOptionType(JOptionPane.DEFAULT_OPTION);
    optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
    optionPane.setOptions(new Object[] {});
    optionPane.setInitialSelectionValue(null);
    if (isIcon)
      optionPane.setIcon(icon);
    else
      optionPane.setIcon(new ImageIcon("...."));  // Not existing, so no icon displayed
    dlg.setContentPane(optionPane);
    dlg.pack();
  }

  /**
   * Display the given text.
   * Adjust size of dialog to length if text.
   */
  public void setText(String text)
  {
    setText(text, true);
  }

  /**
   * Same as setText() but select whether to adjust size of dialog.
   */
  public void setText(String text, boolean adjust)
  {
    if (SwingUtilities.isEventDispatchThread())
    {
      optionPane.setMessage(text);
      if (adjust)
        dlg.pack();  // Adapt size to new message
    }
    else
    {
      try
      {
        SwingUtilities.invokeAndWait(new SetText(text, adjust));
      }
      catch (Exception ex) {}
    }
  }

  /**
   * Show the given title in the title bar.
   */
  public void showTitle(String title)
  {
    if (SwingUtilities.isEventDispatchThread())
      dlg.setTitle(title);
    else
    {
      try
      {
        SwingUtilities.invokeAndWait(new SetTitle(title));
      }
      catch (Exception ex) {}
    }
  }

  /**
   * Same as showTitle().
   */
  public void setTitle(String title)
  {
    showTitle(title);
  }

  /**
   * Register a class with a method clean() that will be called when
   * the title bar's close button is hit.
   */
  public void addCleanable(Cleanable cl)
  {
    cleanable = cl;
  }


  /**
   * Dispose the dialog
   */
  public void dispose()
  {
    if (EventQueue.isDispatchThread())
      dlg.dispose();
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
            dlg.dispose();
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }

  /**
   * Return the dialog.
   */
   public JDialog getDialog()
   {
     return dlg;
   }

   /**
    * Show/hide the dialog.
    * @param visible if true, the dialog is shown, otherwise hidden.
    */
    public void setVisible(final boolean visible)
    {
    if (EventQueue.isDispatchThread())
      dlg.setVisible(visible);
    else
    {
      try
      {
        EventQueue.invokeAndWait(new Runnable()
        {

          public void run()
          {
            dlg.setVisible(visible);
          }
        });
      }
      catch (Exception ex)
      {
      }
    }
  }
}

