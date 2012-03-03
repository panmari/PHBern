// FileLog.java

import java.io.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class FileLog
{
  private static PrintWriter pw = null;
  private static FileLog fileLog;

  private FileLog()  // private ctor
  {
  }

  public static FileLog open(String filename)
  {
    fileLog = new FileLog();
    try
    {
      pw = new PrintWriter(
        new BufferedWriter(new FileWriter(new File(filename))));
    }
    catch (IOException ex)
    {
      System.out.println("Can't open log file for writing. Filename: " + filename);
    }
    println("Log started at " + now());
    return fileLog;
  }

  public synchronized static void close()
  {
    if (pw != null)
    {
      pw.close();
      pw = null;
    }
  }

  public synchronized static void print(String msg)
  {
    if (pw != null)
    {
      pw.print(msg);
      pw.flush();
    }
  }

  public synchronized static void println(String msg)
  {
    if (pw != null)
    {
      pw.println(msg);
      pw.flush();
    }
  }
  
   static String now()
  {
    String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
    synchronized (sdf)
    {
      return sdf.format(cal.getTime());
    }
  }

}
