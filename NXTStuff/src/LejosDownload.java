// LejosDownload.java
// 14-Feb-2010
// Last modifications by APLU: connect strategy USB/Bluetooth

import java.io.*;
import java.util.*;
import ch.aplu.util.*;
import javax.swing.JOptionPane;
import java.net.*;

public class LejosDownload
{
  private final static boolean debug = true;

  private final static String version = "3.5";
  private final String title = "leJOS Downloader (www.legorobotik.ch)";
  private ModelessOptionPane mop = null;
  private URL iconUrl;
  private final String fs = File.separator;
  private final String legoHome = System.getProperties().getProperty("user.home") +
                                  fs + "legoNXT" + fs;
  private String[] languageArg;
  private int language;
  private String iconResourcePath = "nxt.gif";
  private String bluetoothPropertyFile = "bluetooth.properties";
  private static String osName;

  private final String[] downloadSuccess1 = {"Download von ",""};
  private final String[] downloadSuccess2 = {" erfolgreich."," was successfully downloaded."};
  private final String[] downloadFailed1 = {"NXT ist nicht eingeschaltet, nicht verbunden",
  																	 				"NXT is not turned on, connected or a different"};
  private final String[] downloadFailed2 = {"\noder ein anderes Programm l�uft noch.",
  					  											 				"\nprogram is still running"};
  private final String[] installNXJTools = {"F�hren Sie zuerst die Installation des NXJ-Tools aus.",
  																				  "Please install the NXJ-Tools first."};
  private final String[] downloadTrial1 = {"Download zu ",
                                           "Downloding to "};
  private final String[] downloadTrial2 = {" �ber Bluetooth. Bitte warten...",
                                           " via Bluetooth. Please wait..."};


  public LejosDownload(String[] args)
  {
    System.out.println("Running LejosDownload...");
    ClassLoader loader = getClass().getClassLoader();
    iconUrl = loader.getResource(iconResourcePath);
    
    osName = System.getProperty("os.name");

    String binUrl = args[0];
    int lastIndex = binUrl.lastIndexOf('/', binUrl.length());
    //Get file name from server (e.g. MyOOPautonom.nxj)
    String binDatei = binUrl.substring(lastIndex + 1).trim();
    // Get user home directory
    String userHome = System.getProperty("user.home");
    // Construct qualified filenames
    
	    String dir = userHome + fs + "legoNXT" + fs + "lejosNXJ" + fs;
	    String[] neededFiles = {"bcel.jar", "commons-cli.jar", "pctools.jar", 
	    		"pccomm.jar", "jtools.jar", "bluecove.jar", "jlibnxt.dll"};
	    //TODO: .dll not really needed for linux/mac
	    String classpath = "";
	    for (String s: neededFiles) {
	    	classpath += dir + s + ";";
	    }
	    //cut off last semicolon:
	    classpath = classpath.substring(0, classpath.length() - 1 );
    //get Language from JNLP
    try
    {
      languageArg = args[1].split(" ");
    }
    catch(Exception e) {
      languageArg = new String[1];
      languageArg[0] = "";
    }

    //set Language in LejosDownload
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
    
    String	nxjHome = userHome + fs + "legoNXT" + fs + "lejosNXJ";
    String binFile = nxjHome + fs + binDatei; //Local path to nxj-file (see def. binDatei)
    
    if (debug)
    	System.out.println("binDatei: "+binDatei+", binFile: "+binFile);

    getFile(binUrl, binFile); // copy from server to local drive

    boolean success = true;
    
    if (!connect(true, nxjHome, classpath, binFile))  // Try to connect via USB
    	if (!connect(false, nxjHome, classpath, binFile))  // If failed, try to connect via Bluetooth
    		success = false;  // If failed->error

    String message;
    boolean doExit = true;  //if true the message window closes automatically
    if (success)
      message = downloadSuccess1[language] + binDatei + downloadSuccess2[language];
    else
    {
      message = downloadFailed1[language];
      message += downloadFailed2[language];
      if (debug)
        doExit = false;
    }
    if (mop == null)
    {
      mop = new ModelessOptionPane(message);
      mop.setTitle(title);
    }
    else
      mop.setText(message,false);
    
    LoResAlarmTimer timer = new LoResAlarmTimer(5000000, true);
    while (timer.isRunning()) {}
    File bin = new File(binFile);
    if(bin.exists())
      bin.delete();
    if (doExit)
      System.exit(0);
  }

  public static String[] runCommand(String cmd) throws IOException
  {
    // set up list to capture command output lines
    ArrayList list = new ArrayList();
    
    Process proc;
    
    
    // start command running
    proc = Runtime.getRuntime().exec(cmd);
    
    // get command's output stream and
    // put a buffered reader input stream on it
    InputStream estr = proc.getErrorStream();
    BufferedReader ebr = new BufferedReader(new InputStreamReader(estr));

    // read output lines from command
    String strE;
    while ((strE = ebr.readLine()) != null)
    {
      list.add(strE);
      if (debug)
      	System.out.println(strE);
    }
    
    InputStream istr = proc.getInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(istr));

    // read output lines from command
    String str;
    while ((str = br.readLine()) != null)
    {
      list.add(str);
      if (debug)
      	System.out.println(str);
    }

    // wait for command to terminate
    try
    {
      proc.waitFor();
    }
    catch (InterruptedException e)
    {
      System.err.println("process was interrupted");
    }

    // check its exit value
    if (proc.exitValue() != 0)
      System.err.println("exit value was non-zero");

    // close stream
    br.close();

    // return list of strings to caller
    return (String[])list.toArray(new String[0]);
  }


  public void getFile(String binUrl, String binFile)
  // get content of binUrl and copy to local file binFile
  // e.g. getFile("http://clab1.phbern.ch/Ex1.bin", "c:\scratch\Ex1.bin")
  {
    File fbin = new File(binFile);
    if (fbin.exists())
      fbin.delete();
    try
    {
      fbin.createNewFile();

      URL url = new URL(binUrl);
      InputStream is = url.openStream();
      FileOutputStream fos = new FileOutputStream(fbin);
      int c;
      do
      {
        c = is.read();
        fos.write(c);
      }
      while(c != -1);
      is.close();
      fos.close();
    }
    catch(IOException ex)
    {
    	if(ex.toString().contains("Das System kann den angegebenen Pfad nicht finden")  ||
    	   ex.toString().contains("The System cannot find the path specified"))
    	{
      	JOptionPane.showMessageDialog(null, installNXJTools[language],
                                 			"Exception", JOptionPane.INFORMATION_MESSAGE);
      }
    	else
    	{
    		JOptionPane.showMessageDialog(null, ex.toString(),
       																"Exception", JOptionPane.INFORMATION_MESSAGE);
    	}
      System.exit(0);
    }
  }

/******************* Connection trial strategy **********************************
     - If USB: download without any dialog
     - If Bluetooth: (properties file 'bluetooth.properties')
         - if properties file does not exist, dialog with entry 'NXT'
         - if properties file exists,
           read property 'BluetoothName', put value it in dialog
         - When the OK button is hit:
             if entry empty: redisplay dialog
             else create properties file or
                  modify property NxtName with new value

     First try USB:
     nxtupload -r -u <filename>
       Reponses:
            - brick is off
                an error occurred: No NXT found - is it switched on and plugged in (for USB)?
            - brick is on, USB is plugged in
                Found nxt name NXT address 0016530017E0
                leJOS NXJ> Upload successful in 921 milliseconds
            - brick is on, USB is not plugged in
                same as if the brick is off
            ===>> search 'successfull', if found->return true
                                        else try Bluetooth, display dialog, when OK hit

      nxtupload -r -n <BluetoothName>
        Responses:
            - brick is off or brick with this name is not found
               BlueCove version 2.0.2 on widcomm
               an error occurred: No NXT found - is it switched on and plugged in (for USB)?
               BlueCove stack shutdown completed

            - brick with given name is found
               BlueCove version 2.0.2 on widcomm
               Found: <name>
               leJOS NXJ> Upload successful in 1763 milliseconds
               BlueCove stack shutdown completed
            ===>> search 'successfull', if found->return true
                                        else return false

     ****************************************************************************/

  private boolean connect(boolean tryUSB, String nxjHome, String classpath, String binFile)
  {
    String btName = null;

    if (!tryUSB)  // try Bluetooth connection or set Name for Mac connection
    {
      Properties props = new Properties();

      File pFile = new File(nxjHome + fs + bluetoothPropertyFile);
      try
      {
        if (!pFile.isFile())
          pFile.createNewFile();

        props.load(new FileInputStream(pFile));
        btName = props.getProperty("BluetoothName");

        if (btName == null)  // not found
          btName = "NXT";

        String prompt = "Enter Bluetooth Name";
        String value = null;
        do
        {
          value = JOptionPane.showInputDialog(null, prompt, btName);
          if (value == null)
            System.exit(0);
        }
        while (value.trim().length() == 0);
        btName = value.trim().toUpperCase();  // New btName
        props.setProperty("BluetoothName", btName);
        props.store(new FileOutputStream(pFile), "Used by LejosDownload");
        mop = new ModelessOptionPane(downloadTrial1[language] + btName +
                                      downloadTrial2[language]);
        mop.setTitle(title);
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
        return false;
      }
    }
    
    String[] outlist;
    
    if (osName.equals("Mac OS X") || osName.contains("Linux"))
    {
    	String command = nxjHome + fs + "bin" + fs + "nxjupload -r " +
    									(tryUSB ? "-u " : "-n " + btName + " ") + binFile;
    	if (debug)
      	System.out.println("MAC - command: " + command + ", \nTryUSB = " + tryUSB);
    	
    	//	run the command
      try
      {
      	outlist = runCommand(command);
      }
      catch (IOException ex)
      {
      	ex.printStackTrace();
      	return false;
      }
    }
    else
    {
    	String command = "java \"-Djava.library.path=" + nxjHome + "\"" +
    											" -classpath \"" + classpath +
    											"\" lejos.pc.tools.NXJUpload" +
    											" -r " +
    											(tryUSB ? "-u " : "-n " + btName + " ") +
    											"\"" + binFile + "\"";

    	if (debug)
    		System.out.println("WIN - command: " + command + ", \nTryUSB = " + tryUSB);
    	
    	//	run the command
      try
      {
      	outlist = runCommand(command);
      }
      catch (IOException ex)
      {
      	ex.printStackTrace();
      	return false;
      }
    }
    
    if (debug)
    {
    	System.out.println("\n\nrunCommand() returned: \n");
    	for (int i = 0; i < outlist.length; i++)
    		System.out.println("line " + i + ": " + outlist[i]);
    }

    int listLen = outlist.length;

    boolean success = false;
    for (int x = 0; x < listLen && !success; x++)
    {
    	if (listLen > 0 && (outlist[x].indexOf("Upload successful") != -1))
    	{
    		success = true;
    	}
    }
    return success;
    
    /*String command = "java \"-Djava.library.path=" + nxjHome + "\"" +
                     " -classpath \"" + classpath +
                     "\" lejos.pc.tools.NXJUpload " +
                     " -r " +
                     (tryUSB ? "-u " : "-n " + btName + " ") +
                     "\"" + binFile + "\"";

    if (debug)
      System.out.println("command: " + command);

    String message;
    String[] outlist;

    // run the command
    try
    {
      outlist = runCommand(command);
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
      return false;
    }

    if (debug)
    {
      System.out.println("\n\nrunCommand() returned: \n");
      for (int i = 0; i < outlist.length; i++)
        System.out.println("line " + i + ": " + outlist[i]);
    }

    int listLen = outlist.length;

    boolean success = false;
    for (int x = 0; x < listLen && !success; x++)
    {
      if (listLen > 0 && (outlist[x].indexOf("Upload successful") != -1))
        success = true;
    }
    return success;*/
  }


  public static void main(String[] args)
  {
	  //TODO DEBUG:
	  String[] debugArgs = {"http://clab1.phbern.ch/online/archive/0/classes/MotorEx1.nxj","English"};
	  new LejosDownload(debugArgs);
   /* if (args.length != 2)
    {
      System.out.println("LejosDownload Version " + version +
                         "\nUsage: LejosDownload <urlbin> <langString>");
      return;
    }
    new LejosDownload(args);
    */
  }
}