// InstallEmul.java
// Installs Android Emulator

import java.util.*;
import java.util.zip.*;
import ch.aplu.util.ProgressPane;
import java.io.*;
import java.net.*;

public class InstallEmul
{
  public enum Platform
  {
    Mac, Win, Linux
  }
  private String VERSION = "3.6";
  private Platform platform;
  private final String fs = System.getProperty("file.separator");
  private final String userHome = System.getProperty("user.home");
  private final String emulFolder = userHome + fs + ".jdroidemul";
  private final String tmpDir = System.getProperty("java.io.tmpdir");
  private final String iconResourcePath = "res/android.png";
  private final int zipFileSize = 58000000; // in bytes
  private final String os = System.getProperty("os.name");
  private ProgressPane mop;

  public InstallEmul(String zipUrlStandard)
  {
    ClassLoader loader = getClass().getClassLoader();
    URL iconUrl = loader.getResource(iconResourcePath);
    String emptyMsg = "                                                     "
      + "                                             ";
    mop = new ProgressPane(50, 50, emptyMsg, iconUrl);
    mop.setTitle("Android Emulator Installer V" + VERSION
      + " (www.aplu.ch)");

    File fEmulFolder = new File(emulFolder);
    if (!fEmulFolder.exists())
    {
      System.out.print("Need to create  " + emulFolder + "...");
      boolean rc = fEmulFolder.mkdir();
      if (!rc)
      {
        System.out.println("failed.");
        mop.setText("Can't create folder " + emulFolder);
        return;
      }
    }

    FileLog.open(emulFolder + fs + "InstallEmul.log");
 
    final String zipUrl;
    final String zipPath;
    if (os.equals("Linux"))
    {
      platform = Platform.Linux;
      zipUrl = zipUrlStandard.replace(".zip", "_linux.zip");
      zipPath = tmpDir + fs + "jdroidemul.zip";
    }
    else if (os.equals("Mac OS X"))
    {
      platform = Platform.Mac;
      zipUrl = zipUrlStandard.replace(".zip", "_mac.zip");
      zipPath = tmpDir + fs + "jdroidemul.zip";
    }
    else
    {
      platform = Platform.Win;
      zipUrl = zipUrlStandard.replace(".zip", "_win.zip");
      zipPath = tmpDir + "jdroidemul.zip";
    }

    String msg = "Downloading distribution zip.  Be patient...";
    mop.setText(msg, false);
    FileLog.println("Downloading distribution zip " + zipUrl + "...");
    Thread t = new Thread()
    {
      public void run()
      {
        copyFile(zipUrl, zipPath);
      }
    };
    t.start();
    try
    {
      t.join();
    }
    catch (InterruptedException ex)
    {
    }
    msg = "Download finished. Installing now...";
    FileLog.println(msg);
    mop.setText(msg, false);
    try
    {
      unzip(zipPath, emulFolder);
    }
    catch (IOException ex)
    {
      msg = "Fatal error: Can't unzip downloaded archive.";
      FileLog.println(msg);
      mop.setText(msg, false);
      return;
    }

    // Make startup file executable
    if (platform != Platform.Win)
    {
      FileLog.println("Make startup jar executable");
      new File(emulFolder + fs + "ExecEmul.jar").setExecutable(true);
      FileLog.println("Make mksdcard executable");
      new File(emulFolder + fs + "tools" + fs + "mksdcard").setExecutable(true);
    }

    String folder = userHome + fs + ".android";
    if (!createFolder(folder))
    {
      mop.setText("Can't create folder " + folder);
      return;
    }

    folder = userHome + fs + ".android" + fs + "avd";
    if (!createFolder(folder))
    {
      mop.setText("Can't create folder " + folder);
      return;
    }

    folder = userHome + fs + ".android" + fs + "avd" + fs
      + "Slim-Emulator.avd";
    if (!createFolder(folder))
    {
      mop.setText("Can't create folder " + folder);
      return;
    }

    // Create Slim-Emulator.ini
    // Only needed on Linux platform, but done anyway
    String slimEmulator_ini = userHome + fs + ".android" + fs + "avd" + fs
      + "Slim-Emulator.ini";

    FileLog.println("Creating " + slimEmulator_ini);
    PrintWriter out = null;
    try
    {
      out = new PrintWriter(new BufferedWriter(new FileWriter(
        slimEmulator_ini)));
      out.println("target=android-8");
      out.println("path=" + userHome + fs + ".android" + fs + "avd" + fs
        + "Slim-Emulator.avd");
    }
    catch (IOException ex)
    {
      FileLog.println("Can't create " + slimEmulator_ini);
    }

    try
    {
      out.close();
    }
    catch (Exception ex)
    {
    }

    // Create config.ini
    String config_ini = userHome + fs + ".android" + fs + "avd" + fs
      + "Slim-Emulator.avd" + fs + "config.ini";

    FileLog.println("Creating " + config_ini);
    out = null;
    try
    {
      out = new PrintWriter(
        new BufferedWriter(new FileWriter(config_ini)));
      out.println("hw.lcd.density=160");
      out.println("sdcard.size=100M");
      out.println("skin.name=HVGA");
      out.println("skin.path=platforms" + fs + "android-8" + fs + "skins"
        + fs + "HVGA");
      out.println("hw.cpu.arch=arm");
      out.println("abi.type=armeabi");
      out.println("vm.heapSize=24");
      out.println("snapshot.present=true");
      out.println("image.sysdir.1=platforms" + fs + "android-8" + fs
        + "images" + fs);
    }
    catch (IOException ex)
    {
      FileLog.println("Can't create " + slimEmulator_ini);
    }

    try
    {
      out.close();
    }
    catch (Exception ex)
    {
    }

    FileLog.println("Creating virtual sdcard now...");
    //TODO: changes here
    String imagePath = userHome + "\\.android\\avd\\Slim-Emulator.avd\\sdcard.img";
    String cmd = emulFolder + fs + "tools" + fs + "mksdcard 102400K " + imagePath;
    try
    {
      Runtime.getRuntime().exec(cmd);
    }
    catch (IOException ex)
    {
      FileLog.println("Failed to spawn command '" + cmd + "'. Exception:");
      FileLog.println(ex.getMessage());
      mop.setText("Failed to create virtual sdcard.", false);
      FileLog.close();
      return;
    }
    FileLog.println("All done");
    FileLog.close();
    mop.setText("Installation successful.", false);
    delay(3000);
    System.exit(0);
  }

  private boolean createFolder(String folder)
  {
    File folderFile = new File(folder);
    if (!folderFile.exists())
    {
      FileLog.print("Need to create  " + folder + "...");
      boolean rc = folderFile.mkdir();
      if (rc)
        FileLog.println("successful");
      else
      {
        FileLog.println("failed");
        return false;
      }
    }
    else
      FileLog.println("Use existing folder  " + folder);
    return true;
  }

  public void unzip(String zipFileName, String destFolder) throws IOException
  // Precondition: root must contain at least one file
  {
    FileLog.println("Unzipping " + zipFileName);
    ZipFile zipFile = null;
    InputStream inputStream = null;

    File inputFile = new File(zipFileName);
    try
    {
      // Wrap the input file with a ZipFile to iterate through
      // its contents
      zipFile = new ZipFile(inputFile);
      Enumeration<? extends ZipEntry> oEnum = zipFile.entries();
      while (oEnum.hasMoreElements())
      {
        ZipEntry zipEntry = oEnum.nextElement();
        if (zipEntry.isDirectory())
        {
          File destDirFile = new File(destFolder + fs
            + zipEntry.getName());
          destDirFile.mkdirs();
        }
        else
        {
          File destFile = new File(destFolder + fs
            + zipEntry.getName());
          inputStream = zipFile.getInputStream(zipEntry);
          write(inputStream, destFile);
          if (platform != Platform.Win)
          {
            if (zipEntry.getName().contains("emulator")
              || zipEntry.getName().contains("adb"))
            {
              destFile.setExecutable(true);
              FileLog.println("Set executable permission for " + destFile);
            }
          }
        }
      }
    }
    catch (IOException ioException)
    {
      throw ioException;
    }
    finally
    {
      try
      {
        if (zipFile != null)
          zipFile.close();
        if (inputStream != null)
          inputStream.close();
      }
      catch (IOException ex)
      {
        FileLog.println("Can't cleanup unzipper");
      }
    }
  }

  public static void write(InputStream inputStream, File fileToWrite)
    throws IOException
  {
    BufferedInputStream buffInputStream = new BufferedInputStream(
      inputStream);
    FileOutputStream fos = new FileOutputStream(fileToWrite);
    BufferedOutputStream bos = new BufferedOutputStream(fos);

    int byteData;
    while ((byteData = buffInputStream.read()) != -1)
      bos.write((byte)byteData);
    bos.close();
    fos.close();
    buffInputStream.close();
  }

  // ========================= copyFile() ===========================
  private void copyFile(String srcUrl, String dstFile)
  // copy file 'srcUrl' to local file 'dstFile'
  // e.g. getFile("http://clab1.phbern.ch/Ex1.bin", "c:\scratch\Ex1.bin")
  {
    FileLog.println("Src: " + srcUrl + " Dst: " + dstFile);

    File fdstFile = new File(dstFile);
    if (fdstFile.exists())
      fdstFile.delete();

    InputStream inp = null;
    FileOutputStream out = null;

    try
    {
      URL url = new URL(srcUrl);
      inp = url.openStream();
      out = new FileOutputStream(dstFile);

      byte[] buff = new byte[8192];
      int count;
      int value = 0;
      int size = zipFileSize; // bytes
      while ((count = inp.read(buff)) != -1)
      {
        out.write(buff, 0, count);
        value += count;
        mop.setBarValue((int)(100.0 * value / size));
      }
    }
    catch (IOException ex)
    {
      FileLog.println("Error while copying " + srcUrl);
      System.exit(1);
    }
    finally
    {
      try
      {
        if (inp != null)
          inp.close();
        if (out != null)
          out.close();
      }
      catch (IOException ex)
      {
      }
    }
  }

  private void delay(long timeout)
  {
    try
    {
      Thread.currentThread().sleep(timeout);
    }
    catch (InterruptedException ex)
    {
    }
  }

  public static void main(String[] args)
  {
    if (args.length != 1)
    {
      System.out.println("Usage: InstallEmul <url of the zip to download without platform specificaton>");
      return;
    }
    new InstallEmul(args[0]);
  }
}
