// NXTSetName.java
// Change Bluetooth name using NXJ Version 0.85
// Needed library: pccomm.jar from leJOS distribution
// 18 february 2010, AP

import lejos.pc.comm.*;
import lejos.nxt.remote.*;
import javax.swing.*;
import java.io.*;
import ch.aplu.util.ModelessOptionPane;
import java.net.URL;

public class NXTSetName
{
  URL iconUrl = getClass().getClassLoader().getResource("gif/nxt.gif");
  private final static String fs = File.separator;
  private final static String legoHome =
    System.getProperties().getProperty("user.home") + fs + "legoNXT" + fs;
  private final static String nxtHome = legoHome + "lejosNXJ" + fs;
  static String[] languageArg; // Parameter from JNLP file
  private String[] nxtSearching1 =
  {
    "Suche des NXT am USB port",
    "Search NXT at USB port"
  };
  private String[] nxtSearching2 =
  {
    "Bitte einen Moment Geduld...",
    "Please be patient..."
  };
  private String[] nxtNotFound1 =
  {
    "Kein NXT gefunden.\n" +
    "Ueberpruefen Sie, ob der NXT eingeschaltet und via USB verbunden ist.",
    "No NXT found.\n" +
    "Is it switched on and plugged in (for USB)?"
  };
  private String[] nxtNotFound2 =
  {
    "Suche nach NXT fehlgeschlagen.",
    "Error while searching for NXT."
  };
  private String[] connectionFailed1 =
  {
    "Die Verbindung zum NXT ist fehlgeschlagen.",
    "Failed to connect to NXT."
  };
  private String[] connectionFailed2 =
  {
    "Verbindung zu NXT fehlgeschlagen.",
    "Error while connecting to NXT."
  };
  private String[] enterName =
  {
    "Neuen NXT-Namen eingeben:",
    "Enter new NXT name:"
  };
  private String[] noChange1 =
  {
    "Der NXT-Name '",
    "NXT name '"
  };
  private String[] noChange2 =
  {
    "' wurde nicht geaendert.",
    "' not modified."
  };
  private String[] noChange3 =
  {
    "Information",
    "Information"
  };
  private String[] canNotModifyName1 =
  {
    "Der NXT-Name kann nicht geaendert werden.",
    "Cannot modify NXT name."
  };
  private String[] canNotModifyName2 =
  {
    "Fehler bei der NXT-Umbennenung.",
    "Error while setting new NXT name."
  };
  private String[] newNameSet1 =
  {
    "Der NXT wurde zu '",
    "New NXT-name set to '"
  };
  private String[] newNameSet2 =
  {
    "' umbenannt.\n" +
    "Bitte schalten Sie den NXT aus und wieder ein.",
    "'. Please restart the NXT."
  };
  private String[] newNameSet3 =
  {
    "Information",
    "Information"
  };

  public NXTSetName()
  {
    String newName;
    String osName;
    int language;
    System.out.println("NXTSetName starting...");
    System.out.print("Getting language: ");
    if (languageArg[0].equals("English"))
    {
      language = 1;
      System.out.println("English");
    }
    else
    {
      language = 0;
      System.out.println("Deutsch");
    }

    osName = System.getProperty("os.name");

    if (osName.equals("Mac OS X"))
      System.load(nxtHome + "bin" + fs + "libjfantom.dylib");
    else
      System.load(nxtHome + "fantom.dll");


    ModelessOptionPane mop = 
      new ModelessOptionPane(nxtSearching2[language], iconUrl);
    mop.setTitle(nxtSearching1[language]);
    System.out.println("Searching NXT at USB port...");
    NXTConnector conn = new NXTConnector();

    final NXTInfo[] nxts = conn.search(null, null, NXTCommFactory.USB);

    if (nxts.length == 0)
    {
      JOptionPane.showMessageDialog(null,
        nxtNotFound1[language],
        nxtNotFound2[language],
        JOptionPane.ERROR_MESSAGE,
        null);
      System.exit(1);
    }

    System.out.println("NXT found. Getting Bluetooth friendly name...");
    String btName = nxts[0].name;
    System.out.println("Current name: " + btName);

    boolean isOpen = false;
    System.out.print("Opening connection...");
    NXTCommand nxtCommand = new NXTCommand();
    try
    {
      NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
      isOpen = nxtComm.open(nxts[0], NXTComm.LCP);
      nxtCommand.setNXTComm(nxtComm);
    }
    catch (NXTCommException e)
    {
      isOpen = false;
    }

    mop.dispose();
    if (!isOpen)
    {
      System.out.println("failed");
      JOptionPane.showMessageDialog(null,
        connectionFailed1[language],
        connectionFailed2[language],
        JOptionPane.ERROR_MESSAGE,
        null);
      System.exit(1);
    }
    else
    {
      System.out.println("successfull");
    }

    newName = "";
    String prompt = enterName[language];
    boolean done = false;
    while (!done)
    {
      newName = JOptionPane.showInputDialog(null, prompt, btName);
      if (newName == null)
        System.exit(0);
      if (newName.equals(btName))
        done = true;
      else
      {
        newName = newName.trim();
        if (newName.length() > 0 && newName.length() <= 16)
          done = true;
      }
    }

    if (newName.equals(btName))
    {
      JOptionPane.showMessageDialog(null,
        noChange1[language] + btName + noChange2[language],
        noChange3[language],
        JOptionPane.INFORMATION_MESSAGE,
        null);
      System.exit(0);
    }

    try
    {
      nxtCommand.setFriendlyName(newName);
    }
    catch (IOException ex)
    {
      JOptionPane.showMessageDialog(null,
        canNotModifyName1[language],
        canNotModifyName2[language],
        JOptionPane.ERROR_MESSAGE,
        null);
      System.exit(1);
    }

    JOptionPane.showMessageDialog(null,
      newNameSet1[language] + newName + newNameSet2[language],
      newNameSet3[language],
      JOptionPane.INFORMATION_MESSAGE,
      null);
    System.exit(0);
  }

  public static void main(String args[])
  {
//  get Language from JNLP
    try
    {
      languageArg = args[0].split(" ");
    }
    catch (Exception e)
    {
      languageArg = new String[1];
      languageArg[0] = "";
    }

    new NXTSetName();
  }
}

