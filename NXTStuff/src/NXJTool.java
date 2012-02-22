// NXJTool.java
// Achtung: Benoetigt auch aplu5.jar sowie jnlp.jar (im nxjTools.jnlp als 
// Resource angegeben

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.jnlp.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import ch.aplu.util.Console;
import com.apple.mrj.MRJFileUtils;

/**
 * Diese Klasse stellt ein GUI zur Verfügung für den Datenaustausch (Firmware) mit dem
 * NXT. Sämtliche Funktionalitäten basieren auf den Werkzeugen von lejos.
 * 
 * Mit dem boolean webStart kann entschieden werden ob das Tool lokal oder online getestet werden soll
 * (wird es auf den Server geladen, muss webStart auf true gesetzt werden!)
 * 
 * Ist man am entwickeln, kann es von Vorteil sein das boolean debug auf true zusetzten. So gibt es in der
 * Console viel mehr ausgaben. Jedesmal wenn man eine System.out.println nur in der Entwicklung braucht sollte
 * ein if(debug) vorne darn sein.
 * 
 *  
 * @author Andreas Marti, Daniel Studer und Adrian Marthaler, Aegidius Pluess
 */

class NXJTool extends JFrame
{
  private static final String applicationVersion = "20120220";  // Current date
  private static final boolean debug = false;  // Writes debug info to Java console
  
  private static String[] languageArg; //Parameter beim Aufruf von NXTInstaller; gibt die Sprache an
  private String fs = File.separator;
  private String ps = File.pathSeparator;
  private String legoHome;           //Pfad, in dem sich das Programm installiert
  private String osName;             //Name des Betriebsystems
  private String osVersion;          //Version des Betriebsystems
  private String osArch;
  private String jnlpName;
  private String nxtName;
  private int language;
  private JPanel panel = new JPanel();
  private JLabel logo = new JLabel();
  private ScrollPane logScrollPane = new ScrollPane();
  private JTextArea logTextPane = new JTextArea();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private MenuBar menu = new MenuBar();
  private Menu setup = new Menu();
  private MenuItem nxtDriverItem = new MenuItem();
  private MenuItem nxtUpdateItem = new MenuItem();
  private MenuItem firmwareItem = new MenuItem();
  //MenuItem resetItem = new MenuItem();
  private MenuItem closeItem = new MenuItem();
  private MenuItem renameItem = new MenuItem();
  private JButton nxtDriver = new JButton();
  private JButton nxtUpdate = new JButton();
  private JButton firmware = new JButton();
  //JButton reset = new JButton();
  private JButton close = new JButton();
  private Process pFirmware;
  private boolean installed;
  private boolean connected;
  private boolean resetFirst;
  private boolean name;
  
  
  //Alle möglichen Textanzeigen in Deutsch und Englisch
  private String[] firmwareStart = {"Firmware wird installiert... Bitte haben Sie etwas Geduld.\n", 
                                    "Installing firmware... Please be patient.\n"};
  private String[] firmwareSuccess = {"Übertragung erfolgreich.\n" +
                                      "Sie erkennen nun auf dem NXT-Display die neue Menüführung von leJOS.\n" +
                                      "Unter \"Files\" können Sie später die bereits auf den NXT geladenen\n" +
                                      "Programme erneut aufrufen und starten. Viel Spass!\n"+
                                      "Falls sich der NXT immer noch im Reset-Modus befinden sollte, entfernen Sie\n" +
                                      "die Battery für 3 Sekunden. Die neue Firmware sollte nun von selbst starten.",
                                      "Installation installed successfully.\n" +
                                      "You can see the new setup of the leJOS-Firmware on the NXT-Display.\n" +
                                      "After downloading a file to the NXT you can find it and also restart it by navigating\n" +
                                      "to \"Files\" on your NXT-Display. Have fun!\n" +
                                      "Should the NXT still be in the reset-mode, simply remove the battery for 3 seconds.\n" +
                                      "The new firmware should then start by itself."};
  private String[] firmwareBattery = {"Übertragung erfolgreich.\n" +
                                      "Bitte entfernen Sie kurz die Batterie des NXT und legen Sie diese anschliessend\n" +
                                      "wieder ein. Die Java-Firmware sollte automatisch starten. Unter \"Files\" können\n" +
                                      "Sie später die bereits auf den NXT geladenen Programme erneut aufrufen und starten.",
                                      "Firmware installed successfully.\n" +
                                      "Please quickly remove the battery and put it back in. The Java-Firmware should\n" +
                                      "start automatically. After downloading a file to the NXT you can find it and also\n" +
                                      "restart it by navigating into \"Files\" on your NXT-Display. Have fun!"};
  private String[] firmwareFailed = {"Übertragung fehlgeschlagen.\n" +
                                     "Kontrollieren Sie, ob der NXT resettet und eingeschaltet\n" +
                                     "und via USB-Kabel mit dem Computer verbunden ist.",
                                     "Installation failed.\n" +
                                     "Check if the NXT is resetted, turned on and connected\n"+
                                     "to the computer via USB."};
  private String[] firmwareReset = {"Übertragung fehlgeschlagen.\n" +
                                    "Bitte entfernen Sie zuerst die Firmware und versuchen Sie es erneut.",
                                    "Installation failed.\n" +
                                    "Please remove the Firmware first!"};
  private String[] firmwareDriver = {"Übertragung fehlgeschlagen.\n" +
                                     "Bitte überprüfen Sie ob die Treiber korrekt installiert wurden und starten Sie\n" +
                                     "eventuell den PC neu. Versuchen Sie zudem die Treiber erneut zu installieren.",
                                     "Installation failed.\n" +
                                     "Please check if the drivers were installed correctly. In some cases it might be\n" +
                                     "necessary to restart your computer before the drivers function correctly. Also\n" +
                                     "try to reinstall the drivers."};
  private String[] filesNotFound = {"Benötigte Dateien konnten nicht gefunden werden. Bitte neu installieren!\n",
                                    "Necessary files not found. Please restart NXJ-Tool!\n"};
  private String[] startInstall = {"Benötigte Dateien werden auf dem Rechner installiert... Bitte warten.\n",
                                   "Necessary files are being installed locally... Please be patient.\n"};
  private String[] installSuccess = {"Wählen Sie die gewünschte Option um diese auszuführen oder\n" +
                                     "klicken Sie auf  \"NXJ-Tool beenden\" um das Tool zu beenden.\n",
                                     "Choose any option to run it or click \"Quit NXJ-Tool\" to quit this tool.\n"};
  private String[] installNXTDriver = {"NXT-Treiber wird installiert... \n" +
                                       "Bitte folgen Sie den einzelnen Schritten des Installationsprogramms. Falls\n" +
                                       "Sie die Meldung \"No software will be installed or removed.\" erhalten wurde\n" +
                                       "der NXT-Treiber bereits installiert. Beenden Sie die Installation mit \"Cancel\".",
                                       "Installing NXT-Diver...\n" +
                                       "Please follow each step of the installer. If you get the message \"No software\n" +
                                       "will be installed or removed.\" the NXT-Driver is already installed on your\n" +
                                       "computer. Just click \"Cancel\" to quit the installer."};
  private String[] nxtDriverSuccess = {"Der NXT-Treiber wurde erfolgreich installiert.\n" +
                                       "Verbinden Sie jetzt den NXT via USB-Kabel mit dem Computer.",
                                       "NXT-Driver installed successfully.\n" +
                                       "Please connect the NXT to your computer via USB."};
  private String[] installNXTDriverVista = {"NXT-Treiber installieren \n" +
																			      "Suchen Sie unter \"..\\Dokumente und Einstellungen\\IHR_USER\\legoNXT\\Vista\"\n" +
																			      "nach \"LegoMindstormsNXTdriver32\" oder \"-64\" und doppelklicken " + 
																			      "Sie es um die\nInstallation zu starten.\n" +
																			      "Verbinden Sie anschliessend den NXT via USB-Kabel mit dem Computer.",
																			      "Installing NXT-Diver\n" +
																			      "Search for the file \"LegoMindstormsNXTdriver32\" or \"-64\" in \n" +
																			      "\"..\\Documents and Settings\\YOUR_USER\\legoNXT\\Vista\" " +
																			      "and double-click it to\nstart the installation.\n" +
																			      "You can now connect your NXT to the computer via USB."};
  private String[] driverFailed = {"Bei der Installation ist ein Fehler aufgetreten.\n" +
                                   "Vermutlich ist der Treiber bereits installiert.\n" +
                                   "Ansonsten versuchen Sie es bitte erneut.",
                                   "Installation failed. Please try again.\n"};
  private String[] installNXTDriverMac = {"NXT-Treiber wird installiert... \n" +
                                          "Bitte folgen Sie den einzelnen Schritten des Installationsprogramms.",
                                          "Installing NXT-Diver...\n" +
                                          "Please follow each step of the installer."};
  private String[] nxtDriverSuccessMac = {"Der NXT-Treiber wurde erfolgreich installiert.\n" +
                                          "Verbinden Sie jetzt den NXT via USB-Kabel mit dem Computer.\n" +
                                          "Falls Sie mit Firefox arbeiten, suchen Sie im Download-Ordner nach " +
                                          "\"legodriver.pkg\" und doppelklicken Sie es um die Installation zu starten.",
                                          "NXT-Driver installed successfully.\n" +
                                          "Please connect the NXT to your computer via USB.\n" +
                                          "If you are using Firefox, please go to your download folder and \n" +
                                          "double-click on \"legodriver.pkg\" to start the installation."};
  private String[] nxtDriverSuccessMac_10_5 = {"Der NXT-Treiber wurde erfolgreich installiert.\n" +
                                               "Bitte installieren sie das Firmware Update für OS X 10.5.x oder höher.\n" +
                                               "Falls Sie mit Firefox arbeiten, suchen Sie im Download-Ordner nach " +
                                               "\"legodriver.pkg\" und doppelklicken Sie es um die Installation zu starten.",
                                               "NXT-Driver installed successfully.\n" +
                                               "Please install the firmware update for OS X 10.5.x or higher.\n" +
                                               "If you are using Firefox, please go to your download folder and \n" +
                                               "double-click on \"legodriver.pkg\" to start the installation."};
  private String[] installNxtUpdate = {"Firmware Update für OS X 10.5.x oder höher wird installiert...\n" +
                                       "Bitte folgen Sie den einzelnen Schritten des Installationsprogramms.",
                                       "Installing firmware update for OS X 10.5.x or higher...\n" +
                                       "Please follow each step of the installer."};
  private String[] nxtUpdateSuccess = {"Das Firmware Update wurde erfolgreich installiert.\n" +
                                       "Verbinden Sie jetzt den NXT via USB-Kabel mit dem Computer.\n" +
                                       "Falls Sie mit Firefox arbeiten, suchen Sie im Download-Ordner nach " +
                                       "\"legodriver10_5.pkg\" und doppelklicken Sie es um die Installation zu starten.",
                                       "Firmware update installed successfully.\n" +
                                       "Please connect the NXT to your computer via USB.\n" +
                                       "If you are using Firefox, please go to your download folder and \n" +
                                       "double-click on \"legodriver10_5.pkg\" to start the installation."};
  private String[] installNXTDriverLinux = {"NXT-Treiber wird installiert... \n" +
																			      "Der Treiber muss die Rechte für den USB-Port um den NXT erweitern.\n" +
																			      "Dies muss eventuell mit dem Root-Passwort einem Eingabefenster " +
																			      "bestätigt\nwerden.",
																			      "Installing NXT-Diver...\n" +
																			      "The driver expends the USB port rules for the NXT.\n" + 
																			      "This maybe needs to be approved with the root password in a newly " +
																			      "opened input window."};
  
  private String[] startRename = {"NXT-Umbennenung wird gestartet... Bitte warten.",
                                  "Starting NXT-Renamer... Please be patient."};
  private String[] renamingNXT = {"Im Anzeigefenster sehen Sie den alten NXT-Namen.\n"+
                                  "Bitte benennen Sie den NXT in den gewünschten Namen um.\n" +
                                  "Bitte beachten Sie, dass der Name eine maximale Länge von 6 Zeichen aufweisen darf.",
                                  "The NXT-Renamer shows you the old NXT-Name. Please give the NXT a new name\n" +
                                  "of your choice.\n" +
                                  "Please note, that the name cannot contain more than 6 characters."};
  private String[] doRenameNXT1 = {"Umbennenung von \"", "Successfully renamed \""};
  private String[] doRenameNXT2 = {"\" in \"", "\" to \" "};
  private String[] doRenameNXT3 = {"\" erfolgreich.\n" +
                                   "Falls der neue Namen nicht auf dem Display erscheinen sollte,\n" +
                                   "entfernen Sie bitte die Batterie für 3 Sekunden.", 
                                   "\".\n" +
                                   "In case the new Name does not appear on the display, simply remove\n" +
                                   "the battary for 3 seconds."};
  private String[] renameFailed1 = {"Umbennenung des NXT fehlgeschlagen. Versuchen Sie es erneut.",
                                    "Renaming failed. Please try again."};
  private String[] rename6Char = {"Der Name darf maximal 6 Zeichen lang sein.\n"+
                                  "Bitte versuchen Sie es erneut.",
                                  "The new NXT-Name cannot contain more than 6 characters.\n" +
                                  "Please try again."};
  private String[] renameNoName = {"Leider wurde kein Name eingegeben.\n"+
                                   "Bitte versuchen Sie es erneut.",
                                   "No name was entered. Please try again."};
  private String[] renameFailed2 = {"NXT konnte nicht umbennent werden.\n" +
                                    "Kontrollieren Sie ob der NXT eingeschaltet und via\n" +
                                    "USB-Kabel mit dem Computer verbunden ist.",
                                    "Renaming failed.\n" +
                                    "Check if the NXT is turned on and connected to the\n" +
                                    "computer via USB."};
  
  //Alle Bottuns und MenuItems in Deutsch und Englisch
  private String[] buttonNXTDriver = {"    NXT-Treiber installieren      ", "          Install NXT-Driver         "};
  private String[] buttonFirmware = {"Java-Firmware auf NXT laden", "Load Java-Firmware to NXT "};
  //private String[] buttonReset = {"   Lego-Firmware entfernen    ", "    Remove Lego-Firmware     "};
  private String[] buttonQuit = {"          NXJ-Tool beenden           ", "              Quit NXJ-Tool              "};
  private String[] buttonNXTDriverMac = {"    NXT-Treiber installieren   ", "        Install NXT-Driver         "};
  private String[] buttonNXTUpdateMac = {"Firmware Update OS X < 10.5", "Firmware Update OS X < 10.5"};
  private String[] buttonFirmwareMac = {" Java-Firmware auf NXT laden", "  Load Java-Firmware to NXT  "};
  //private String[] buttonResetMac = {"    Lego-Firmware entfernen   ", "     Remove Lego-Firmware     "};
  private String[] buttonQuitMac = {"         NXJ-Tool beenden         ", "           Quit NXJ-Tool             "};
  private String[] buttonNXTDriverLinux = {"     NXT-Treiber installieren   ", "         Install NXT-Driver         "};
  private String[] buttonFirmwareLinux = {"Java-Firmware auf NXT laden", "  Load Java-Firmware to NXT "};
  //private String[] buttonResetLinux = {"    Lego-Firmware entfernen   ", "     Remove Lego-Firmware     "};
  private String[] buttonQuitLinux = {"         NXJ-Tool beenden        ", "           Quit NXJ-Tool             "};
  private String[] menuTab = {"Menü", "Setup"};
  private String[] menuNXTDriver = {"NXT-Treiber installieren", "Install NXT-Driver"};
  private String[] menuNXTUpdate = {"Firmware Update OS X < 10.5", "Firmware Update OS X < 10.5"};
  private String[] menuFirmware = {"Java-Firmware auf NXT laden", "Load Java-Firmware to NXT"};
  //private String[] menuReset = {"Lego-Firmware entfernen", "Remove Lego-Firmware"};
  private String[] menuRename = {"NXT umbennenen", "Rename NXT"};
  private String[] menuQuit = {"NXJ-Tool beenden", "Quit NXJ-Tool"};
  private String[] option1 = {"Ja", "Yes"};
  private String[] option2 = {"Nein", "Cancel"};
  private String[] confirmClosing1 = {"Sind Sie sicher, dass Sie das NXJ-Tool beenden möchten?",
                                      "Do you really want to quit the NXJ-Tool?"};
  private String[] confirmClosing2 = {"NXJ-Tool beenden?", "Quit NXJ-Tool?"};
  private String[] confirmFirmware1 = {"Sind sie sicher, dass sie die Firmware\nauf den NXT laden möchten?",
                                       "Do you really want to load the firmware to the NXT?"};
  private String[] confirmFirmware1_10_5 = {"Haben sie das Firmware Update für OS X 10.5.x\noder höher bereits installiert?",
                                            "Did you install the firmware update for OS X 10.5.x or higher?"};
  private String[] confirmFirmware2 = {"Firmware auf NXT laden?", "Loading firmware to NXT?"};
  private String[] confirmNXTDriver1 = {"Sind Sie sicher, dass Sie die NXT-Treiber\ninstallieren möchten?",
                                        "Do you really want to install the NXT-Driver?"};
  private String[] confirmNXTDriver2 = {"NXT-Treiber installieren?", "Install NXT-Driver?"};
  private String[] confirmNXTUpdate1 = {"Sind Sie sicher, dass Sie das Firmware Update\ninstallieren möchten?",
                                        "Do you really want to install the firmware update?"};
  private String[] confirmNXTUpdate2 = {"Firmware Update installieren?", "Install firmware update?"};
  private String[] confirmRename1 = {"Sind Sie sicher, dass Sie den NXT\numbennenen möchten?",
                                     "Do you really want to rename the NXT?"};
  private String[] confirmRename2 = {"NXT umbenennen?", "Rename NXT?"};
  
  /**
   * Interne Klasse für das Handling der Button-Events
   * Falls eine Button gedrückt wird wird die dazugehörige Confirmation-Methode aufgerufen.
   * Diese macht nichts anderes als Frage ob die ausgewählte Funktion wirklich ausgeführt
   * werden soll.
   */
  class MyButtonListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      if(e.getSource().equals(firmware))
      {
        confirmFirmware();
      }
      else if(e.getSource().equals(close))
      {
        confirmClosing();
      }
      else if(e.getSource().equals(nxtDriver))
      {
        confirmNXTDriver();
      }
      else if(e.getSource().equals(nxtUpdate))
      {
        confirmNXTUpdate();
      }
    }
  }

  /**
   * Interne Klasse für das Handling der Menü-Events
   * Siehe Erklärung bei MyButtonListener
   */
  class MyMenuListener implements ActionListener
  {
    public void actionPerformed(ActionEvent e)
    {
      MenuItem i = (MenuItem)e.getSource();
      if(i.equals(firmwareItem))
      {
        confirmFirmware();
      }
      else if(i.equals(nxtDriverItem))
      {
        confirmNXTDriver();
      }
      else if(i.equals(nxtUpdateItem))
      {
        confirmNXTUpdate();
      }
      else if(i.equals(renameItem))
      {
        confirmRename();
      }
      else
      {
        confirmClosing();
      }
    }
  }

  /**
   * Interne Klasse für Multithreading. Installiert das NXJ-Tool, führt den Firmwaredownload aus, 
   * installiert die nötigen Treiber und startet in Windows die Namensänderung
   */
  class ExternalProcess extends Thread
  {
    String s;

    ExternalProcess(String s)
    {
      this.s = s;
    }

    public void run()
    {
      //lädt Firmware auf NXT
      if (s.equals("firmware"))
      {
        deleteLogTextPane();
        logTextPane.setText(logTextPane.getText() + firmwareStart[language]);
        connected = false;
        resetFirst = true;
        
        Vector logVector = new Vector();
        /* Bei Mac und Linux wird der Firmwaredowload über die bin-Datei nxjflash ausgeführt
         * Zudem muss eine Umgebungsvariable auf das lejosNXJ gesetzt werden (das lokal benötigte
         * NXJ_HOME)
         */
        
        if (osName.equals("Linux"))
        {
          String cmdFirmware[] = {legoHome + "lejosNXJ" + fs + "bin" + fs + "nxjflashg"}; 
          try
          {
            pFirmware = Runtime.getRuntime().exec(cmdFirmware);
          } 
          catch (IOException ex)
          {
            if (debug)
              System.out.println("Error while spawing " + cmdFirmware);
          }
        }    
        
        if (osName.equals("Mac OS X"))
        {
          String cmdFirmware[] = {legoHome + "lejosNXJ" + fs + "bin" + fs + "nxjflash"};
          String envpFirmware[] = {"NXJ_HOME=" + legoHome + "lejosNXJ"};
          try
          {
            pFirmware = Runtime.getRuntime().exec(cmdFirmware, envpFirmware);
          }
          catch (IOException ex)
          {
            if (debug)
            	System.out.println("Error while spawing " + cmdFirmware);
          }
     
        }
        /* Bei Windows wird die Firmware direkt aus der Klasse NXJFlash im pctools.jar gestartet
         * (deshalb müssen diese und die anderen benötigten Datein im classpath angeben sein).
         * Wie bei Mac und Linux muss eine Umgebungsvariabel auf lejosNXJ gesetzt werden
         */
        else
        {
          String nxjRoot = legoHome + "lejosNXJ" + fs;
          String nxjHome = legoHome + "lejosNXJ";
          String dJava = "-Djava.library.path=" + nxjHome + fs + "bin";
          String param =  nxjRoot + "bcel.jar;" +
                          nxjRoot + "commons-cli.jar;" +
                          nxjRoot + "pctools.jar;" +
                          nxjRoot + "pccomm.jar;" +
                          nxjRoot + "jtools.jar;" +
                          nxjRoot + "bluecove.jar";
                             
          String cmdFirmware[] = {"java ", dJava, "-classpath", param, "lejos.pc.tools.NXJFlash"};
          String envpFirmware[] = {"NXJ_HOME=" + legoHome + "lejosNXJ"};
          
          try
          {
            File workingDir = new File(legoHome + "lejosNXJ");  
            // Needs to execute in  this directory, 
            // otherwise .bin are not found (since leJOS 0.91)
            pFirmware = Runtime.getRuntime().exec(cmdFirmware, envpFirmware, workingDir);
          }
          catch (IOException ex)
          {
          	if (debug)
          		System.out.println("Error while spawing " + cmdFirmware);
          }
        }
        
         
        /* Falls der NXT nicht im Resetmodus ist und die LegoFirmware oder mindestens die Lejos-Firmware 0.7
         * drauf gibt einem nxjflash die Möglichkeit den Firmwaredownload abzubrechen oder doch durchzuführen
         * (Eingabe 0 oder 1). Hier wird die Consoleausgabe von nxjflash abgefangen und falls "Select the
         * device" drin vorkommt wird 2 Sekunden gewartet und dann der char-Wert von 1 an die Console 
         * geschickt.
         * Gewartet wird mit dem Befehl Console.delay() von Aegid (deshalb muss ch mitverpackt werden)
         */
        
        try
        {
          InputStream is = pFirmware.getInputStream();
          BufferedReader br = new BufferedReader(new InputStreamReader(is));
                  
          String line;
          while ((line = br.readLine()) != null)
          {
            if (line.contains("Select the device"))
            {
            	
            	if (debug)
            		System.out.println("Selecting device...");
            	
            	//wait 2 seconds and if nothing happens give a console input
              OutputStream outStream = pFirmware.getOutputStream();

              Console.delay(2000);
              
              int choice = (int)'1';
              try
              {
                outStream.write(choice);
                outStream.flush();
                outStream.close();
                
                if (debug)
                	System.out.println("Choice writen: " + choice + " (char value of: " + (char)choice + ")");
              }
              catch (IOException ex)
              {
              	if (debug)
              		System.out.println("NxjFlash needs no input");
              }
            }
            logVector.addElement(line);
            if (debug)
            	System.out.println("out: " + line);
          }
            
            InputStream err = pFirmware.getErrorStream();
            BufferedReader bre = new BufferedReader(new InputStreamReader(err));
            
            while ((line = bre.readLine()) != null)
            {
              logVector.addElement(line);
              if (debug)
              	System.out.println("error: " + line);
            }
            
            //Liest die Console Eingabe und reagiert entsprechend
            try
            {
              for (int x = 0; x < logVector.size(); x++) 
              {
                if ((logVector.elementAt(x).toString()).contains("No NXT found"))
                {
                  deleteLogTextPane();
                  logTextPane.setText(logTextPane.getText() + firmwareFailed[language]);
                }
                //Ab Version 0.85 nicht mehr nötig
                /*else if ((logVector.elementAt(x).toString()).contains("Select the device"))
                {
                  deleteLogTextPane();
                  logTextPane.setText(logTextPane.getText() + firmwareReset[language]);
                }*/
                else if ((logVector.elementAt(x).toString()).contains("Restarting the device"))
                {
                  deleteLogTextPane();
                  logTextPane.setText(logTextPane.getText() + firmwareSuccess[language]);
                }
                else if ((logVector.elementAt(x).toString()).contains("Cannot load a comm driver"))
                {
                  deleteLogTextPane();
                  logTextPane.setText(logTextPane.getText() + firmwareDriver[language]);
                }
              }
            }
            catch(Exception e) {}
          
            pFirmware.waitFor();
            
            if (pFirmware.exitValue() == 0)
            {
              System.out.println("Install ok");
            }
            else
            {
              System.out.println("Install failed");
              deleteLogTextPane();
              logTextPane.setText(logTextPane.getText() + firmwareFailed[language]);
            }
          }
          catch(Exception ex)
          {
            ex.printStackTrace();
            logTextPane.setText(logTextPane.getText() + filesNotFound[language]);
          }
        //}
      }
      /* installiert NXJ-Tool auf Rechner, d.h. die benötigten Dateien aus NXJTools (auf Server) werden
       * heruntergeladen und entpackt
       */
      if (s.equals("install")) {
        logTextPane.setText(startInstall[language] + logTextPane.getText());
        try{
          Unzipper unzip = new Unzipper();
          URL serverURL = createServerURL();
          URL urlLejos;
          URL urlTools;
          URL urlVistaDriver;
          boolean nxjDeleted;
          String outputNXT = legoHome;
     
          File nxjHome = new File(legoHome + "lejosNXJ");
          
          if (nxjHome.exists()) {
            nxjDeleted = deleteDir(nxjHome);
          }
          
          else {
            nxjDeleted = true;
          }
            
          /* installation mit Mac OS X => Daten müssen im tar-Format sein und durch macFileDownload(...)
           * heruntergladen werden und mit Hilfe des Bash-Scripts unpackNXJForMac.sh geöffnet werden!
           */ 
          if (osName.equals("Mac OS X")) 
          {
            URL urlUnpack = new URL(serverURL.toString() + "NXJTools/unpackNXJForMac.sh");
            
            urlLejos = new URL(serverURL.toString() +
                               "NXJTools/lejosNXJ_Mac.tar");
              
            macFileDownload(urlLejos, "lejosNXJ_Mac.tar");  
            macFileDownload(urlUnpack, "unpackNXJForMac.sh");
            
            String envp[] = {"HOME=" + System.getProperties().getProperty("user.home")};
            String cmd[] = {"bash", legoHome + "unpackNXJForMac.sh"};
            Process p = Runtime.getRuntime().exec(cmd, envp);
            p.waitFor();
          }
          
          /* installation mit Linux => siehe Mac OS X installation
           * Zudem wird noch die Datei setNxtUdev.sh heruntergeladen, welche für die Treiberinstallation
           * notwendig ist (sie setzt die Udev-Reglen für den NXT)
           */ 
          else if (osName.equals("Linux"))
          {
          	URL urlUnpack = new URL(serverURL.toString() + "NXJTools/unpackNXJForLinux.sh");
          	URL urlScript = new URL(serverURL.toString() + "NXJTools/setNxtUdev.sh");
            
            urlLejos = new URL(serverURL.toString() +
                               "NXJTools/lejosNXJ_Linux.tar");
              
            macFileDownload(urlLejos, "lejosNXJ_Linux.tar");  
            macFileDownload(urlUnpack, "unpackNXJForLinux.sh");
            
            String envp[] = {"HOME=" + System.getProperties().getProperty("user.home")};
            String cmd[] = {"bash", legoHome + "unpackNXJForLinux.sh"};
            Process p = Runtime.getRuntime().exec(cmd, envp);
            
            //Gibt die Console-Outputs des unpackNXJForLinux.sh zurück
            try
            {
            	InputStream is = p.getInputStream();
              BufferedReader br = new BufferedReader(new InputStreamReader(is));
                      
              String line;
              int x = 1;
              while ((line = br.readLine()) != null)
              {
              	if (debug)
              	{
              		System.out.println("LinuxInstall Line " + x + ": " + line);
              		x++;
              	}
              }
              InputStream err = p.getErrorStream();
              BufferedReader bre = new BufferedReader(new InputStreamReader(err));
              
              while ((line = bre.readLine()) != null)
              {
                if (debug)
                {
                	System.out.println("LinuxInstall Line " + x + ": " + line);
                	x++;
                }
              }
            }
            catch (Exception ex)
            {
            	ex.printStackTrace();
            }
            p.waitFor();
            
            //lädt setNxtUdev.sh herunter (verwendet es aber noch nicht)
            macFileDownload(urlScript, "lejosNXJ" + fs + "setNxtUdev.sh");
          }
          
          //entpackt die nötigen zip-Dateie für Windows
          else {
            urlLejos = new URL(serverURL.toString() +
                               "NXJTools/lejosNXJ.zip");
            urlTools = new URL(serverURL.toString() +
                               "NXJTools/NXJTools.zip");
            urlVistaDriver = new URL(serverURL.toString() +
            												 "NXJTools/Vista.zip");
            
            //download and unzip
            if (nxjDeleted) {
              unzip.unzip(urlLejos.toString(), outputNXT);
            }
            
            unzip.unzip(urlTools.toString(), outputNXT);
            if (osName.contains("Vista"))
            	unzip.unzip(urlVistaDriver.toString(), outputNXT);
          }
        }
        catch(Exception e){
          e.printStackTrace();
        }
        deleteLogTextPane();
        
        logTextPane.setText(logTextPane.getText() + installSuccess[language]);
          
        /* alle Buttons werden aktivert und mit einem ActionListnere versehen, d.h. sie können nun gedrückt
         * werden und es geschieht wirklich etwas ;-)
         */
        nxtDriver.enable();
        nxtUpdate.enable();
        firmware.enable();
        //reset.enable();
        close.enable();
        nxtDriverItem.enable();
        firmwareItem.enable();
        //resetItem.enable();
        closeItem.enable();
        renameItem.enable();
          
        nxtDriver.addActionListener(new MyButtonListener());
        nxtUpdate.addActionListener(new MyButtonListener());
        firmware.addActionListener(new MyButtonListener());
        //reset.addActionListener(new MyButtonListener());
        nxtDriverItem.addActionListener(new MyMenuListener());
        firmwareItem.addActionListener(new MyMenuListener());
        //resetItem.addActionListener(new MyMenuListener());
        renameItem.addActionListener(new MyMenuListener());
          
        repaint();
          

        close.addActionListener(new MyButtonListener());
        closeItem.addActionListener(new MyMenuListener()); 
      }

      /* installiert den NXT-Treiber (bei Win XP - Mac, Linux und Vista sind anders und werden noch 
       * genauer erklärt)
       */
      if (s.equals("nxtDriver"))
      {
        deleteLogTextPane();
        if (osName.equals("Mac OS X"))
          logTextPane.setText(logTextPane.getText() + installNXTDriverMac[language]);
        else if (osName.contains("Vista"))
        {
          logTextPane.setText(logTextPane.getText() + installNXTDriverVista[language]);
        }
        else if (osName.contains("Linux"))
        {
        	logTextPane.setText(logTextPane.getText() + installNXTDriverLinux[language]);
        }
        else
          logTextPane.setText(logTextPane.getText() + installNXTDriver[language]);
        try
        {
          /* Bei Mac werden die Treiberpackete direkt aus dem Ordner NXTDriver_Mac im Ordner NXJTools vom 
           * Server heruntergeladen und im Safari ausgeführt. Bei Firefox muss der User die Packet aus dem
           * Downloadordner direkt starten
           */
          if (osName.equals("Mac OS X"))
          {
            String urlNXT = createServerURL().toString() + "NXJTools/NXTDriver_Mac/legodriver.pkg.zip";
            MRJFileUtils.openURL(urlNXT);
            
            deleteLogTextPane();
            if (osVersion.contains("10.5") || osVersion.contains("10.6"))
              logTextPane.setText(logTextPane.getText() + nxtDriverSuccessMac_10_5[language]);
            else
              logTextPane.setText(logTextPane.getText() + nxtDriverSuccessMac[language]);
          }
          
          /* Bei Linux wird nicht wirklich ein Treiber installiert, sondern nur mit Hilfe des Bash-Scripts
           * setNxtUdev.sh die udev-Regeln für den NXT gesetzt
           */
          else if (osName.contains("Linux"))
          {
          	String cmd[] = {"bash", legoHome + "lejosNXJ" + fs + "setNxtUdev.sh"};
            Process p = Runtime.getRuntime().exec(cmd);
            
            boolean success = true;
            boolean exists = false;
            
            try
            {
              InputStream is = p.getInputStream();
              BufferedReader br = new BufferedReader(new InputStreamReader(is));
                      
              String line;
              int x = 1;
              while ((line = br.readLine()) != null)
              {
              	if (debug)
                {
              		System.out.println("LinuxDriver Line " + x + ": " + line);
              		x++;
                }
              }
              InputStream err = p.getErrorStream();
              BufferedReader bre = new BufferedReader(new InputStreamReader(err));
              
              while ((line = bre.readLine()) != null)
              {
                if (debug)
                {
                	System.out.println("LinuxDriver Line " + x + ": " + line);
                	x++;
                }
              }
            }
            catch (Exception ex)
            {
            	ex.printStackTrace();
            }
            
            if (debug)
            {
            	for (int i = 0; i < cmd.length; i++)
            		System.out.println("CMD Linux " + i + ": " + cmd[i]);
            }
            p.waitFor();
            
            if(p.exitValue() == 0)
            {
              deleteLogTextPane();
              logTextPane.setText(logTextPane.getText() + nxtDriverSuccess[language]);
            }
            else
            {
              deleteLogTextPane();
              logTextPane.setText(logTextPane.getText() + driverFailed[language]);
            }
          }
          /* Bei Vista werden die Treiber vom user selbst installiert, es gibt nur eine Textanzeig in der
           * Messagebox. Bei Win XP wird der Treiberinstalation ausgeführt.
           */
          else if (osName.contains("XP")|| osName.contains("7"))
          {
            String[] cmdNXT = {legoHome + "NXTDriver" + fs + "setup.exe"};
          
            Process pNXT = Runtime.getRuntime().exec(cmdNXT);
            pNXT.waitFor();
            if(pNXT.exitValue() == 0)
            {
              deleteLogTextPane();
              logTextPane.setText(logTextPane.getText() + nxtDriverSuccess[language]);
            }
            else
            {
              deleteLogTextPane();
              logTextPane.setText(logTextPane.getText() + driverFailed[language]);
            }
          }
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
          logTextPane.setText(logTextPane.getText() + filesNotFound[language]);
        }
      }
      
      // Bei Mac ist noch ein Firmware- resp. Treiberupdate nötig (funktioniert gleich wie Treiberinstallation)
      if (s.equals("nxtUpdate"))
      {
        deleteLogTextPane();
        logTextPane.setText(logTextPane.getText() + installNxtUpdate[language]);
        
        try
        {
          if (osName.equals("Mac OS X") && (osVersion.contains("10.5") || osVersion.contains("10.6")))
          {
            String urlUpdate = createServerURL().toString() + "NXJTools/NXTDriver_Mac/legodriver10_5.pkg.zip";
            MRJFileUtils.openURL(urlUpdate);  
            
            deleteLogTextPane();
            logTextPane.setText(logTextPane.getText() + nxtUpdateSuccess[language]);
          }
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
          logTextPane.setText(logTextPane.getText() + filesNotFound[language]);
        }
      }
                
      //startet NXTSetName.jar via jnlp. Funktioniert nur für Windows
      if(s.equals("rename"))
      {
      	try
      	{
      		deleteLogTextPane();
      		logTextPane.setText(logTextPane.getText() + startRename[language]);
      		
      		String cmd[] = {"javaws", createServerURL().toString() + jnlpName};
      		Process p = Runtime.getRuntime().exec(cmd);
        
      		p.waitFor();
        
      		if(p.exitValue()==0)
      		{
      			System.out.println("JNLP started");
      		}
      		else
      		{
      			System.out.println("JNLP failed");
      		}
      		
      		deleteLogTextPane();
      		logTextPane.setText(logTextPane.getText() + renamingNXT[language]);
      	}
      	catch (Exception e)
      	{
      		e.printStackTrace();
      	}

      }
    }
  }
  
  //wird benötigt um die Treiberpacket herunterzuladen und auszuführen auf Mac OS X und Linux
  private void macFileDownload(URL aurl, String outputName)
  {
    try
    {
      int BUFFER = 4096;
      URL url = new URL(aurl.toString());
      URLConnection c = url.openConnection();
      c.setDoInput(true);
      c.setUseCaches(false);
      c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      InputStream in = c.getInputStream();
      String lejosForMac = legoHome + outputName;
      byte data[] = new byte[4096];
      FileOutputStream fos = new FileOutputStream(lejosForMac);
      BufferedOutputStream dest = new BufferedOutputStream(fos, 4096);
      int i;
      while((i = in.read(data, 0, 4096)) != -1)
      {
        dest.write(data, 0, i);
      }
      dest.flush();
      dest.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }


  //zeigt an ob NXT-Tool im Web oder lokal gestartet werden soll (false = lokal)
  private boolean useWebStart = true;

  //started NXT-Tool lokal oder im Web
  private  URL createServerURL()
  {
    URL serverURL = null;
    try
    {
      if (useWebStart)
      {

        javax.jnlp.BasicService basicService = (javax.jnlp.BasicService)
          javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
        serverURL =  basicService.getCodeBase();
        
        if(language == 1)
        {
          jnlpName = "nxtsetnameEnglish.jnlp";
        }
        else
        {
          jnlpName = "nxtsetname.jnlp";
        }
      }
      else
      {

        serverURL =  new URL("file:" + fs + fs + fs + "E:" + fs + "legoAndEclipse" + fs + "projects" + fs +
                "NXJTool"+fs);
        
        if(language == 1)
        {
          jnlpName = "setNameEnglish_local.jnlp";
        }
        else
        {
          jnlpName = "setName_local.jnlp";
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(1);
    }
    return serverURL;
  }

  private void copyFileFromServer(URL src, File dest)
  {
    try
    {
      InputStream in = src.openStream();
      dest.createNewFile();
      FileOutputStream fos = new FileOutputStream(dest);
      int chr = in.read();
      while(chr != -1)
      {
        fos.write(chr);
        chr = in.read();
      }
      fos.close();
      in.close();
    }
    catch(Exception e){}
  }

  public NXJTool()
  {
    super("NXJ-Tool (Version " + applicationVersion + ")");
    legoHome = System.getProperties().getProperty("user.home")+ fs + "legoNXT" + fs; //erzeugt einen String mit dem Pfad zum "lego"-Ordner.
    osName = System.getProperty("os.name"); //erzeugt einen String mit dem Namen des Betriebssystems.
    osVersion = System.getProperty("os.version"); //erzeugt einen String mit der Version des Betriebssystems.
    osArch = System.getProperty("os.arch");
    
    //set Language in NXTTool
    System.out.print("Getting language: ");
    if(languageArg[0].equals("English"))
    {
      language = 1;
      System.out.println("English");
    }
    else
    {
      language = 0;
      System.out.println("Deutsch");
    }
    
    System.out.println("OSName: " + osName + ", OSVersion: " + osVersion + ", OSArch: " + osArch);
    
        setDefaultLookAndFeelDecorated(true);
        setSize(530, 400);
        File f = new File(legoHome);
        
        /* falls der Ordner LegoNXT wird dieser noch erzeugt, ansonsten wird bloss install() ausgeführt
         * und das GUI angezeigt
         */
        if(!f.exists())
        {
          f.mkdir();
          install();
          installed = true;
        }
        else
        {
          install();
          installed = true;
        }
        createAndShowGUI();

        setVisible(true);
  }


  public void createAndShowGUI()
  {
    SwingUtilities.invokeLater(new Runnable(){
      public void run(){
        try
        {
          jbInit();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    });
  }

  //hier wird das ganze Layout gemacht. Anpassungen immer zuerst lokal überprüfen!!!
  private void jbInit() throws Exception
  {
    panel.setLayout(gridBagLayout1);
    panel.setSize(530, 400);
    ClassLoader loader = getClass().getClassLoader();
    //Achtung: Gross- / Kleinschreibung von RCX.GIF ist wichtig!! Muss übereinstimmen
    //mit dem Namen im Jar File
    URL urlimage = loader.getResource("NXT.GIF");
    ImageIcon icon = new ImageIcon(urlimage);
    logo = new JLabel(icon);
    this.setResizable(false);
    if (osName.equals("Mac OS X")) {
      nxtDriver.setText(buttonNXTDriverMac[language]);
      nxtUpdate.setText(buttonNXTUpdateMac[language]);
      firmware.setText(buttonFirmwareMac[language]);
      //reset.setText(buttonResetMac[language]);
      close.setText(buttonQuitMac[language]);
    }
    else if (osName.equals("Linux"))
    {
    	nxtDriver.setText(buttonNXTDriverLinux[language]);
      firmware.setText(buttonFirmwareLinux[language]);
      //reset.setText(buttonReset[language]);
      close.setText(buttonQuitLinux[language]);
    }
    else {
      nxtDriver.setText(buttonNXTDriver[language]);
      firmware.setText(buttonFirmware[language]);
      //reset.setText(buttonReset[language]);
      close.setText(buttonQuit[language]);
    }
    setup.setLabel(menuTab[language]);
    nxtDriverItem.setLabel(menuNXTDriver[language]);
    nxtUpdateItem.setLabel(menuNXTUpdate[language]);
    firmwareItem.setLabel(menuFirmware[language]);
    //resetItem.setLabel(menuReset[language]);
    closeItem.setLabel(menuQuit[language]);
    renameItem.setLabel(menuRename[language]);

    if (osName.equals("Mac OS X"))
    {
      this.getContentPane().add(panel);
      panel.add(logo,         new GridBagConstraints(0, 0, 1, 5, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      panel.add(logScrollPane,         new GridBagConstraints(0, 5, 3, 1, 1.0, 1.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
      panel.add(nxtDriver,       new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      if (osVersion.contains("10.5") || osVersion.contains("10.6"))
      {
        panel.add(nxtUpdate,       new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                  ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        panel.add(firmware,  new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
                  ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        panel.add(close,    new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
                  ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      }
      else
      {
        panel.add(firmware,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                  ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
        panel.add(close,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
                  ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      }
      logScrollPane.add(logTextPane, null);
      menu.add(setup);
      setup.add(nxtDriverItem);
      if (osVersion.contains("10.5") || osVersion.contains("10.6"))
        setup.add(nxtUpdateItem);
      setup.addSeparator();
      //setup.add(resetItem);
      setup.add(firmwareItem); 
      //setup.addSeparator();
      //setup.add(renameItem);
      setup.addSeparator();
      setup.add(closeItem);
      setMenuBar(menu);
    }
    else if (osName.equals("Linux"))
    {
    	this.getContentPane().add(panel);
      panel.add(logo,         new GridBagConstraints(0, 0, 1, 5, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      panel.add(logScrollPane,         new GridBagConstraints(0, 5, 3, 1, 1.0, 1.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
      panel.add(nxtDriver,       new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
          			,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      /*panel.add(reset,      new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
     							,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));*/
      panel.add(firmware,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      panel.add(close,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      logScrollPane.add(logTextPane, null);
      menu.add(setup);
      setup.add(nxtDriverItem);
      setup.addSeparator();
      //setup.add(resetItem);
      setup.add(firmwareItem); 
      //setup.addSeparator();
      //setup.add(renameItem);
      setup.addSeparator();
      setup.add(closeItem);
      setMenuBar(menu);
    }
    else
    {
      this.getContentPane().add(panel);
      panel.add(logo,         new GridBagConstraints(0, 0, 1, 5, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      panel.add(logScrollPane,         new GridBagConstraints(0, 5, 3, 1, 1.0, 1.0
                ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));
      panel.add(nxtDriver,       new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      /*panel.add(reset,      new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));*/
      panel.add(firmware,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      panel.add(close,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
                ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0));
      logScrollPane.add(logTextPane, null);
      menu.add(setup);
      setup.add(nxtDriverItem);
      setup.addSeparator();
      //setup.add(resetItem);
      setup.add(firmwareItem); 
      setup.addSeparator();
      setup.add(renameItem);
      setup.addSeparator();
      setup.add(closeItem);
      setMenuBar(menu);
    }
    
    //Die Buttons werden erst aktiviert wenn das Tool fertig installiert ist, deshalb hier zuerst disabled
    nxtDriver.disable();
    nxtUpdate.disable();
    firmware.disable();
    //reset.disable();
    close.disable();
    nxtDriverItem.disable();
    firmwareItem.disable();
    //resetItem.disable();
    closeItem.disable();
    renameItem.disable();
    
    validate();
  }

  //je nach Entscheid wird hier das Thread der ExtenalProcesses gestartet
  public void install()
  {
    Thread t = new ExternalProcess("install");
    t.start();
  }

  public void nxtDriver()
  {
    Thread t = new ExternalProcess("nxtDriver");
    t.start();
  }
  
  public void nxtUpdate()
  {
    Thread t = new ExternalProcess("nxtUpdate");
    t.start();
  }

  public void firmware()
  {
    Thread t = new ExternalProcess("firmware");
    t.start();
  }

  /*public void reset()
  {
    Thread t = new ExternalProcess("reset");
    t.start();
  }*/
  
  public void rename()
  {
    Thread t = new ExternalProcess("rename");
    t.start();
  }

  public void windowClosing(WindowEvent e)
  {
    System.exit(0);
  }

  public void processWindowEvent(WindowEvent e)
  {
    if(e.getID() == e.WINDOW_CLOSING)
    {
      confirmClosing();
    }
  }

  //überprüft ob das Tool benedet und geschlossen werden darf
  public void confirmClosing()
  {
    File f = new File(legoHome);
    Object[] options = {option1[language], option2[language]};
    int n = JOptionPane.showOptionDialog(this, confirmClosing1[language], confirmClosing2[language]
                                         , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    if(n == 0)
    {
      try
      {
        pFirmware.destroy();
      }
      catch(Exception e)
      {
      	if (debug)
      		System.out.println("Unable to destroy nxjFlash-Prozess.");
      }
      
      try
      {
        if(!installed)
        {
          if(!f.exists())
          {
            f.mkdir();
          }
        }
        
        /* alle nicht mehr benötigten Dateien, welche zu Installation, etc herunter geladen werden,
         * werden von der lokalen Maschine gelöscht
         */
        else
        {
          File fNext = new File(legoHome + "NeXTTool.exe");
          File fDriver = new File(legoHome + "NXTDriver");
          File fVista = new File(legoHome + "Vista");
          File fBin = new File(legoHome + "lejosNXJ" + fs + "bin");
          if(fNext.exists())
          {
            fNext.delete();
          }
          if(fBin.exists() && osName.contains("Win"))
          {
          	
            deleteDir(fBin);
          }
          if(fDriver.exists())
          {
            deleteDir(fDriver);
          }
          if(fVista.exists())
          {
            deleteDir(fVista);
          }
        }
      }
      catch(Exception e)
      {
        e.printStackTrace();
        System.err.println("Unable to write to file");
        System.exit(-1);
      }
      System.exit(0);
    }
  }

  //wird zum löschen der Dateien und Ordner gebraucht (ist rekursiv)
  public boolean deleteDir(File dir)
  {
    if(dir.isDirectory())
    {
      String children[] = dir.list();
      for(int i = 0; i < children.length; i++)
      {
        boolean success = deleteDir(new File(dir, children[i]));
        if(!success)
        {
          return false;
        }
      }
    }
    return dir.delete();
  }

  //überprüft ob die Firmare installiert werden soll
  public void confirmFirmware()
  {
    Object[] options = {option1[language], option2[language]};
    int n = 0;
    
    if (osName.equals("Mac OS X") && (osVersion.contains("10.5") || osVersion.contains("10.6")))
      n = JOptionPane.showOptionDialog(this, confirmFirmware1_10_5[language], confirmFirmware2[language]
                                           , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    else
      n = JOptionPane.showOptionDialog(this, confirmFirmware1[language], confirmFirmware2[language]
                                           , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    if(n == 0)
    {
      firmware();
    }
  }
  
  //überprüft ob der NXT umbenennt werden soll
  public void confirmRename()
  {
    Object[] options = {option1[language], option2[language]};
    int n = JOptionPane.showOptionDialog(this, confirmRename1[language], confirmRename2[language]
                                         , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    if(n == 0)
    {
      rename();
    }
  }
  
  //überprüft ob die Treiber installiert werden sollen
  public void confirmNXTDriver()
  {
    Object[] options = {option1[language], option2[language]};
    int n = JOptionPane.showOptionDialog(this, confirmNXTDriver1[language], confirmNXTDriver2[language]
                                         , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    if(n == 0)
    {
      nxtDriver();
    }
  }
  
  //überprüft ob die Treiberupdates für Mac gamacht werden sollen
  public void confirmNXTUpdate()
  {
    Object[] options = {option1[language], option2[language]};
    int n = JOptionPane.showOptionDialog(this, confirmNXTUpdate1[language], confirmNXTUpdate2[language]
                                         , JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    if(n == 0)
    {
      nxtUpdate();
    }
  }

  public void deleteLogTextPane()
  {
    logTextPane.setText("");
  }

  public static void main(String[] args)
  {
    //get Language from JNLP
    try
    {
      languageArg = args[0].split(" ");
    }
    catch(Exception e) {
      languageArg = new String[1];
      languageArg[0] = "";
    }
    
    new NXJTool();
  }
}
