// NxtRobot.java

/*
This software is part of the NxtJLib library.
It is Open Source Free Software, so you may
- run the code for any purpose
- study how the code works and adapt it to your needs
- integrate all or parts of the code in your own programs
- redistribute copies of the code
- improve the code and release your improvements to the public
However the use of the code is entirely your responsibility.
 */
package ch.aplu.nxt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Vector;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import ch.aplu.bluetooth.BluetoothFinder;
import ch.aplu.bluetooth.BluetoothResponder;
import ch.aplu.bluetooth.BtDeviceInfo;
import ch.aplu.nxt.platform.ConnectPanel;
import ch.aplu.nxt.platform.DebugConsole;
import ch.aplu.nxt.platform.NxtProperties;
import ch.aplu.nxt.platform.NxtThread;
import ch.aplu.nxt.platform.PlatformTools;
import ch.aplu.nxt.platform.ShowError;
import ch.aplu.nxt.platform.ShowException;
import ch.aplu.util.Cleanable;

/**
 * Class that represents a NXT robot brick. Parts (e.g. motors, sensors) may
 * be assembled into the robot to make it doing the desired job. <br><br>
 *
 * More than one instance may be created. They are identified by different
 * Bluetooth names (or addresses).<br><br>
 *
 * The Bluetooth communication uses the Bluecove library and is fully standalone
 * and portable. No serial port emulation (RFCOMM) and no Bluetooth Control Center (BCC)
 * is used. The Bluecove library supports only interfaces with Widcomm compatibility.
 * If more than one brick is used at the same time, the Bluetooth interface
 * must be capable to establish more than one Bluetooth link (e.g. Anycom USB adapter,
 * Part.No: CC-3036).<br><br>
 *
 * NxtJlib implements the usual Java event handling model using event listeners
 * and adapters. The sensors are polled at a regular interval from a internal
 * thread and the registered callback method is invoked, when the event condition
 * is fullfilled (e.g. sensor value crosses a given trigger level).<br><br>
 *
 * Many library options are defined in a property file 'nxtjlib.properties' that
 * may be modified as needed. The property file is searched in the following order:<br>
 * - Application directory (user.dir)<br>
 * - Home directory (user.home)<br>
 * - NxtJLib.jar (distribution)<br><br>
 *
 * As soon as the property file is found, the search is cancelled. This allows
 * to use a personalized property file without deleting or modifing the distributed
 * file in NxtJLib.jar. Consult the distributed file for more information.
 * Be careful to keep the original formatting.<br><br>
 *
 * If the property KeepAlive is set to 1, a thread is started that sends keep
 * alive commands every 500 s in order to prevent the automatic shutdown of the NXT.
 */
public class NxtRobot implements SharedConstants
{
  // ---------------- Inner class MyCleanable -----------------
  private class MyCleanable implements Cleanable
  {
    public void clean()
    {
      new Thread()
      {
        public void run()
        {
          exit();
        }
      }.start();
    }
  }

  // ---------------- Inner class MyBluetoothResponder -----------------
  private class MyBluetoothResponder implements BluetoothResponder
  {
    public void notifyBluetoothDeviceSearch(Vector deviceTable)
    {
      if (deviceTable.size() == 0)
      {
        isDeviceFound = false;
        PlatformTools.wakeUp();
        return;
      }
      isDeviceFound = true;
      di = (BtDeviceInfo)deviceTable.elementAt(0);
      dev = di.getRemoteDevice();
      btAddressStr = dev.getBluetoothAddress();
      btAddressLong = Long.parseLong(btAddressStr, 16);
      PlatformTools.wakeUp();
    }

    public void notifyBluetoothServiceSearch(Vector serviceTable)
    {
    }
  }

  // ---------------- Inner class KeepAliveThread ----------------------
  private class KeepAliveThread extends NxtThread
  {
    private NxtRobot robot;
    private final long lifeTime = 540000; // ms. Aftere 600s the NXT is off
    private volatile boolean isRunning = false;

    private KeepAliveThread(NxtRobot robot)
    {
      this.robot = robot;
    }

    private void startThread()
    {
      isRunning = true;
      robot.keepAlive();
      Tools.delay(200);
      start();
    }

    public void run()
    {
      if (NxtRobot.getDebugLevel() >= DEBUG_LEVEL_LOW)
        DebugConsole.show("KeepAliveThread started (lifetime 600 s)");
      while (isRunning)
      {
        Tools.delay(lifeTime);
        if (isRunning)
        {
          if (NxtRobot.getDebugLevel() >= DEBUG_LEVEL_LOW)
            DebugConsole.show("\nKeepAlive requested (lifetime 600 s)");
          robot.keepAlive();
        }
      }
    }

    protected void stopThread()
    {
      isRunning = false;
      interrupt();  // Take it out of delay()
      try
      {
        joinX(1000);
      }
      catch (InterruptedException ex)
      {
      }
      if (NxtRobot.getDebugLevel() >= DEBUG_LEVEL_LOW)
        if (isAlive())
          DebugConsole.show("DEBUG: KeepAlive stopping failed");
        else
          DebugConsole.show("KeepAliveThread stopped");
    }
  }
// ---------------- End of inner classes -----------------------------

  static
  {
    props = new NxtProperties();
  }
  private BtDeviceInfo di;
  private RemoteDevice dev;
  private String btName = null;
  private long btAddressLong = 0;
  private String btAddressStr = "";
  private boolean isConnected = false;
  private InputStream is = null;
  private OutputStream os = null;
  private int channel = 1;
  private StreamConnection conn = null;
  private String msg;
  private boolean isKeepAlive;
  private KeepAliveThread kat;
  private Vector parts = new Vector();  // Compatible for J2ME
  private static int debugLevel;
  private static NxtProperties props;
  private boolean isConnectPane;
  private boolean isAnnounce;
  private volatile boolean isDeviceFound = false;

  /**
   * Creates a NxtRobot instance with given Bluetooth name.
   * If connect is true, a connection trial is engaged while an information pane is shown.
   * If the connection fails, the calling thread will be put in a wait state and the program hangs.
   * The user can then terminate the application by closing the information pane.
   * established at this time.<br><br>
   * If connect = false, no connection trial is engaged at this time and connect() should be used.
   * If any method that needs the connection is invoked before connect() is called, the
   * connection is automatically established.<br><br>
   * If btName = null, a input dialog is displayed where the Bluetooth name can be entered.<br><br>
   * No connection pane is shown, if connect = false and connect(false) is called.
   * @param btName the Bluetooth friendly name of the brick, e.g. "NXT"
   * @param connect if true, a connection trial is engaged
   */
  public NxtRobot(String btName, boolean connect)
  {
    if (btName == null)
      this.btName = ConnectPanel.askBtName();
    else
      this.btName = btName;
    debugLevel = props.getIntValue("DebugLevel");
    if (debugLevel > DEBUG_LEVEL_OFF)
      DebugConsole.showTimed("DebugLevel: " + debugLevel, 3000);
    isKeepAlive = props.getIntValue("KeepAlive") == 1 ? true : false;
    if (connect)
      if (!connect(true))
        PlatformTools.putSleep();
  }

  /**
   * Creates a NxtRobot instance with given Bluetooth name.
   * A connection trial is engaged while an information pane is shown.
   * If the connection fails, the calling thread will be put in a wait state and the program hangs.
   * The user can then terminate the application by closing the information pane.<br><br>
   * If btName = null, a input dialog is displayed where the Bluetooth name can be entered.
   * @param btName the Bluetooth friendly name of the brick, e.g. "NXT"
   */
  public NxtRobot(String btName)
  {
    this(btName, true);
  }

  /**
   * Creates a NxtRobot instance with given Bluetooth address.
   * If connect is true, a connection trial is engaged while an information pane is shown.
   * If the connection fails, the calling thread will be put in a wait state and the program hangs.
   * The user can then terminate the application by closing the information pane.
   * If connect = false, no connection trial is engaged at this time and connect() should be used.
   * If any method that needs the connection is invoked before connect() is called, the
   * connection is automatically established.<br><br>
   * No connection pane is shown, if connect = false and connect(false) is called.
   * @param btAddress the Bluetooth address of the brick, e.g. 0x0000A762B001
   * @param connect if true, a connection trial is engaged
   */
  public NxtRobot(long btAddress, boolean connect)
  {
    btAddressLong = btAddress;
    try
    {
      btAddressStr = Long.toString(btAddress, 16).toUpperCase();
    }
    catch (NumberFormatException ex)
    {
      new ShowException(this).show(ex);
    }
    debugLevel = props.getIntValue("DebugLevel");
    if (connect)
      if (!connect(true))
        PlatformTools.putSleep();
  }

  /**
   * Creates a NxtRobot instance with given Bluetooth address.
   * A connection trial is engaged while an information pane is shown.
   * If the connection fails, the calling thread will be put in a wait state and the program hangs.
   * The user can then terminate the application by closing the information pane.
   * @param btAddress the Bluetooth address of the brick, e.g. 0x0000A762B001
   */
  public NxtRobot(long btAddress)
  {
    this(btAddress, true);
  }

  /**
   * Asks for the Bluetooth name and creates a NxtRobot instance.
   * The initial value for the name is read from the nxtjlib.properties file.
   * In J2ME the modified name is stored in the RMS and reread at the next program execution.
   * A connection trial is engaged while an information pane is shown.
   * If the connection fails, the calling thread will be put in a wait state and the program hangs.
   * The user can then terminate the application by closing the information pane.
   */
  public NxtRobot()
  {
    this(null, true);
  }

  /**
   * Sets the Bluetooth channel (default: 1). Used only when calling connect().
   * @param channel the channel number
   */
  public void setChannel(int channel)
  {
    this.channel = channel;
  }

  /**
   * Returns the properties from the NxtJLib property file.
   * @return the reference to the NxtPropeties
   */
  public static NxtProperties getProperties()
  {
    return props;
  }

  /**
   * Returns the current debug level.
   * @return the debug level set in the properties file.
   */
  public static int getDebugLevel()
  {
    return debugLevel;
  }

  /**
   * Assembles the given part into the robot.
   * If already connected, initialize the part.
   * @param part the part to assemble
   */
  public void addPart(Part part)
  {
    part.setRobot(this);
    parts.addElement(part);
    if (isConnected)
      part.init();
  }

  /**
   * Connects the host to the robot via Bluetooth.
   * Initializes all parts already assembled.
   * Shows a information pane while connecting.
   * A connect/disconnect melody is played.
   * @return true, if successful or already connected; false,
   * if connection fails or already connected
   */
  public boolean connect()
  {
    return connect(true);
  }

  /**
   * Connects the host to the robot via Bluetooth.
   * Initializes all parts already assembled.
   * If isConnectPane = true, an information pane is shown while connecting.
   * @param isConnectPane if true an information pane is shown
   * @return true, if successful or already connected; false,
   * if connection fails or already connected
   */
  public boolean connect(boolean isConnectPane)
  {
    return connect(isConnectPane, true);
  }

  /**
   * Connects the host to the robot via Bluetooth.
   * Initializes all parts already assembled.
   * If isConnectPane = true, an information pane is shown while connecting.
   * If isAnnounce = true, a melody is by the brick when the connection is established
   * and when the connection is closed.
   * @param isConnectPane if true, an information pane is shown
   * @param isAnnounce if true, a connect/disconnect melody is played
   * @return true, if successful or already connected;
   * false, if connection fails or already connected
   */
  public boolean connect(boolean isConnectPane, boolean isAnnounce)
  {
    this.isAnnounce = isAnnounce;
    if (isConnected)
      return true;

    this.isConnectPane = isConnectPane;
    
    //Hack for Mac OS X 10.8: Load custom drivers:
    if (System.getProperty("os.name").equalsIgnoreCase("Mac OS X") &&
    		System.getProperty("os.version").contains("10.8")) {
	    String driverLocation =  System.getProperty("user.home") + 
				File.separator + "legoNXT" + 
				File.separator + "lejosNXJ" +
				File.separator + "lib" +
				File.separator + "bluetooth";
		File f = new File(driverLocation, "IOBluetooth");
		System.out.println("Trying to load custom library from: " + f.getAbsolutePath());
		if (f.exists()) {
			try {
				Runtime.getRuntime().exec("export DYLD_LIBRARY_PATH=" + driverLocation);
				System.out.println("done!");
			} catch (Exception e) {
				e.printStackTrace();
			} 			
		} else {
			System.out.println("Could not find driver file, please run installation first.");
			return false;
		}
    }
    if (btName != null)
    {
      if (isConnectPane)
      {
        msg = "Connecting to " + btName + "...";
        ConnectPanel.show(msg);
      }
      int[] uuids = null;  // No service search
      System.out.println("Searching for device " + btName + "...");
      new BluetoothFinder(btName, uuids, false, new MyBluetoothResponder());
      PlatformTools.putSleep();  // Wait for notification from callback
      if (!isDeviceFound)
      {
        System.out.println("Device " + btName + " not found.");
        if (isConnectPane)
        {
          msg = "Device " + btName + " not found.";
          ConnectPanel.show(msg);
        }
        return false;
      }
      System.out.println("Device " + btName + " found.");
    }
    else // Address given
    {
      msg = "Connecting to " + btAddressStr + "...";
      if (isConnectPane)
        ConnectPanel.show(msg);
      isDeviceFound = true;
    }

    doConnect();
    if (!isConnected)
      return false;

    // Start keep alive thread
    if (isKeepAlive)
    {
      kat = new KeepAliveThread(this);
      kat.startThread();
    }

    // Init sensors
    for (int i = 0; i < parts.size(); i++)
      ((Part)(parts.elementAt(i))).init();

    if (isAnnounce)
      playConnectMelody();

    // Wait until the connection is up and running
    // Needed when used as Bluetooth client
    Tools.delay(100);
    return true;
  }

  /**
   * Closes the Bluetooth communication.
   * Delegates cleaning to a cleanup thread.
   * Waits for thread termination for 5 seconds and returns true, if
   * cleanup successful. If cleanup fails, returns false. If not connected
   * immediately returns true.
   * @return true if cleanup successful or not connected; otherwise false
   */
  public boolean disconnect()
  {
    if (!isConnected)
      return true;
    Thread cleanupThread = new Thread()
    {
      public void run()
      {
        cleanup();  // May block
      }
    };
    cleanupThread.start();
    try
    {
      cleanupThread.join(5000);
    }
    catch (InterruptedException ex)
    {}
    if (cleanupThread.isAlive())
      return false;
    else
      return true;
  }

  private void cleanup()
  {
    // Stop keep alive thread
    if (kat != null)
      kat.stopThread();

    // Clean up sensors
    for (int i = 0; i < parts.size(); i++)
      ((Part)(parts.elementAt(i))).cleanup();

    // Stop all motors
    byte ALL_MOTORS = (byte)0xFF;
    setOutputState(ALL_MOTORS, (byte)0, 0x00,
      REGULATION_MODE_IDLE, 0,
      MOTOR_RUN_STATE_IDLE, 0);

    if (isAnnounce)
      playDisconnectMelody();

    // Close streams
    try
    {
      is.close();
      os.close();
      conn.close();
    }
    catch (Exception ex)
    {
    }
  }

  /**
   * Closes the Bluetooth communication and terminates the program.
   */
  public void exit()
  {
    disconnect();
    PlatformTools.exit();
  }

  /**
   * Plays a standard connect melody.
   */
  public synchronized void playConnectMelody()
  {
    Tools.delay(1000);
    playTone(600, 100);
    Tools.delay(100);
    playTone(900, 100);
    Tools.delay(100);
    playTone(750, 200);
    Tools.delay(200);
  }

  /**
   * Plays a standard disconnect melody.
   */
  public synchronized void playDisconnectMelody()
  {
    Tools.delay(1000);
    playTone(900, 100);
    Tools.delay(100);
    playTone(750, 100);
    Tools.delay(100);
    playTone(600, 200);
    Tools.delay(200);
  }

  /**
   * Returns the StreamConnection reference of the Bluetooth connection.
   * @return StreamConnection or null, if not connected
   */
  public StreamConnection getStreamConnection()
  {
    if (!isConnected)
      return null;
    return conn;
  }

  /**
   * Returns the InputStream reference of the Bluetooth connection.
   * @return InputStream or null, if not connected
   */
  public InputStream getInputStream()
  {
    if (!isConnected)
      return null;
    return is;
  }

  /**
   * Returns the OutputStream reference of the Bluetooth connection.
   * @return OutputStream or null, if not connected
   */
  public OutputStream getOutputStream()
  {
    if (!isConnected)
      return null;
    return os;
  }

  private void doConnect()
  {
    String connectionUrl = "btspp://" + btAddressStr + ":" + channel;
    if (debugLevel == DEBUG_LEVEL_MEDIUM)
      DebugConsole.show("DEBUG: Connection URL:\n" + connectionUrl);
    try
    {
      System.out.print("Opening Connector and Streams...");
      conn = (StreamConnection)Connector.open(connectionUrl);
      is = conn.openInputStream();
      os = conn.openOutputStream();
      System.out.println("done.");
    }
    catch (IOException ex)
    {
      System.out.println("failed.");
      if (isConnectPane)
      {
        if (btName != null) // Name given
          msg = "Connection to " + btName + " failed.";
        else
          msg = "Connection to " + btAddressStr + " failed.";
        ConnectPanel.show(msg);
      }
      PlatformTools.wakeUp();
      return;
    }

    // Connection established
    if (btName == null) // Address given, get friendly name
      btName = getFriendlyName();
    if (isConnectPane)
    {
      msg = "Connection to " + btName + " established.\n"
        + "Bluetooth address = " + btAddressStr + " (hex)\n"
        + "Application running...\n"
        + "(Close the window to terminate.)";
      ConnectPanel.show(msg);
      ConnectPanel.getMop().addCleanable(new MyCleanable());
      Tools.delay(3000);
    }
    isConnected = true;
    PlatformTools.wakeUp();
  }

  /**
   * Sends a request to the brick to get the Bluetooth friendly name.
   * @return the Bluetooth friendly name
   */
  public String getFriendlyName()
  {
    StringBuffer sb = null;
    byte[] request =
    {
      SYSTEM_COMMAND_REPLY, GET_DEVICE_INFO
    };
    byte[] reply = requestData(request);
    sb =
      new StringBuffer(new String(reply)).delete(18, 33).delete(0, 3);
    // Cut trailing 0's
    int i;
    for (i = 0; i
      < sb.length(); i++)
    {
      if ((int)sb.charAt(i) == 0)
        break;
    }

    sb.delete(i, sb.length());
    return sb.toString();
  }

  /**
   * Combines sendData() and readData() in synchronized block.
   * @param request the data to send
   * @return the brick's reply
   */
  public synchronized byte[] requestData(byte[] request)
  {
    sendData(request);
    return readData();
  }

  /**
   * Sends a direct command to the brick.
   * @param command the data to send
   */
  public synchronized void sendData(byte[] command)
  {
    if (debugLevel == DEBUG_LEVEL_HIGH)
    {
      DebugConsole.show("DEBUG: sendData(byte[] command)\ncommand:");
      for (int i = 0; i
        < command.length; i++)
        DebugConsole.show("  " + command[i]);
    }

    int lenMSB = command.length >> 8;
    int lenLSB = command.length;
    try
    {
      os.write((byte)lenLSB);
      os.write((byte)lenMSB);
      os.write(command);
    }
    catch (IOException ex)
    {
//      System.out.println("send error");
    }
  }

  /**
   * Reads the data returned as a reply from the brick.
   * @return the brick's reply
   */
  public synchronized byte[] readData()
  {
    byte[] reply = null;
    int length = -1;
    int lenMSB;
    int lenLSB;

    try
    {
      do
        lenLSB = is.read();
      while (lenLSB < 0);

      lenMSB =
        is.read(); // MSB of reply length
      length =
        (0xFF & lenLSB) | ((0xFF & lenMSB) << 8);

      reply =
        new byte[length];
      // Rest of packet
      is.read(reply);
    }
    catch (IOException ex)
    {
//      System.out.println("read error");
    }

    if (debugLevel == DEBUG_LEVEL_HIGH)
    {
      DebugConsole.show("DEBUG: readData() returned:");
      for (int i = 0; i
        < reply.length; i++)
        DebugConsole.show("  " + reply[i]);
    }

    return reply;
  }

  /**
   * Sets the output state of a specific sensor port.
   */
  protected synchronized void setOutputState(int portId, byte power, int mode,
    int regulationMode, int turnRatio,
    int runState, long tachoLimit)
  {
    if (debugLevel >= DEBUG_LEVEL_MEDIUM)
      DebugConsole.show("DEBUG: setOutputState("
        + portId + ", "
        + power + ", "
        + mode + ", "
        + regulationMode + ", "
        + turnRatio + ", "
        + runState + ", "
        + tachoLimit + ")");
    byte[] request =
    {
      DIRECT_COMMAND_NOREPLY, SET_OUTPUT_STATE, (byte)portId,
      power, (byte)mode, (byte)regulationMode,
      (byte)turnRatio, (byte)runState, (byte)tachoLimit,
      (byte)(tachoLimit >>> 8), (byte)(tachoLimit >>> 16),
      (byte)(tachoLimit >>> 24)
    };
    sendData(request);
  }

  /**
   * Retrieves the current output state of a specific port.
   * @param port  (0,..,3)
   * @return OutputState an OutputState reference
   */
  protected synchronized OutputState getOutputState(int port)
  {
    // !! Needs to check port to verify they are correct ranges.
    byte[] request =
    {
      DIRECT_COMMAND_REPLY, GET_OUTPUT_STATE, (byte)port
    };
    byte[] reply = requestData(request);

    if (reply[1] != GET_OUTPUT_STATE)
      new ShowError("Error in NxtRobot.getOutputState()\n"
        + "Return data did not match request");
    OutputState outputState = new OutputState(port);
    outputState.status = reply[2];
    outputState.outputPort = reply[3];
    outputState.powerSetpoint = reply[4];
    outputState.mode = reply[5];
    outputState.regulationMode = reply[6];
    outputState.turnRatio = reply[7];
    outputState.runState = reply[8];
    outputState.tachoLimit = (0xFF & reply[9])
      | ((0xFF & reply[10]) << 8)
      | ((0xFF & reply[11]) << 16)
      | ((0xFF & reply[12]) << 24);
    outputState.tachoCount = (0xFF & reply[13])
      | ((0xFF & reply[14]) << 8)
      | ((0xFF & reply[15]) << 16)
      | ((0xFF & reply[16]) << 24);
    outputState.blockTachoCount = (0xFF & reply[17])
      | ((0xFF & reply[18]) << 8)
      | ((0xFF & reply[19]) << 16)
      | ((0xFF & reply[20]) << 24);
    outputState.rotationCount = (0xFF & reply[21])
      | ((0xFF & reply[22]) << 8)
      | ((0xFF & reply[23]) << 16)
      | ((0xFF & reply[24]) << 24);
    return outputState;
  }

  protected void setInputMode(int portId, int sensorType, int sensorMode)
  {
    if (debugLevel >= DEBUG_LEVEL_MEDIUM)
      DebugConsole.show("DEBUG: setInputMode("
        + portId + ", "
        + sensorType + ", "
        + sensorMode + ")");
    byte[] request =
    {
      DIRECT_COMMAND_NOREPLY, SET_INPUT_MODE, (byte)portId,
      (byte)sensorType, (byte)sensorMode
    };
    sendData(request);
  }

  /**
   * Reads the values from given a sensor port.
   * @param portId the Id of the sensor port (0,..3)
   */
  protected synchronized InputValues getInputValues(int portId)
  {
    byte[] request =
    {
      DIRECT_COMMAND_REPLY, GET_INPUT_VALUES, (byte)portId
    };
    InputValues inputValues = new InputValues();
    byte[] reply = requestData(request);
    if (reply != null)
    {
      inputValues.inputPort = reply[3];
      // 0 is false, 1 is true.
      inputValues.valid = (reply[4] != 0);
      // 0 is false, 1 is true.
      inputValues.isCalibrated = (reply[5] == 0);
      inputValues.sensorType = reply[6];
      inputValues.sensorMode = reply[7];
      inputValues.rawADValue = (0xFF & reply[8]) | ((0xFF & reply[9]) << 8);
      inputValues.normalizedADValue = (0xFF & reply[10]) | ((0xFF & reply[11]) << 8);
      inputValues.scaledValue = (short)((0xFF & reply[12]) | (reply[13] << 8));
      inputValues.calibratedValue = (short)((0xFF & reply[14]) | (reply[15] << 8));
      if (debugLevel >= DEBUG_LEVEL_MEDIUM)
      {
        DebugConsole.show("DEBUG: getInputValues() returned:");
        inputValues.printValues();
      }
    }

    return inputValues;
  }

  /**
   * Returns the connection state.
   * @return true, if connected, otherwise false
   */
  public boolean isConnected()
  {
    return isConnected;
  }

  /**
   * Plays a tone with given frequency (in Hertz) and duration (in seconds).
   */
  public void playTone(int frequency, int duration)
  {
    if (!isConnected)
      connect();

    byte[] request =
    {
      DIRECT_COMMAND_NOREPLY, PLAY_TONE, (byte)frequency,
      (byte)(frequency >>> 8), (byte)duration, (byte)(duration >>> 8)
    };
    sendData(request);
  }

  /**
   * Returns the battery level.
   * @return voltage (in Volt)
   */
  public double getBatteryLevel()
  {
    if (!isConnected)
      connect();

    int batteryLevel = 0;
    byte[] request =
    {
      DIRECT_COMMAND_REPLY, GET_BATTERY_LEVEL
    };
    byte[] reply = requestData(request);
    batteryLevel =
      (0xFF & reply[3]) | ((0xFF & reply[4]) << 8);
    return batteryLevel / 1000.0;
  }

  /**
   * Returns the Bluetooth address (hex).
   * @return Bluetooth address as string in hex format
   */
  public String getBtAddress()
  {
    if (!isConnected)
      connect();

    return btAddressStr;
  }

  /**
   * Returns the Bluetooth name
   * @return Bluetooth friendly name
   */
  public String getBtName()
  {
    if (!isConnected)
      connect();

    return btName;
  }

  /**
   * Returns the Bluetooth address (long).
   * @return Bluetooth address as long.
   */
  public long getBtAddressLong()
  {
    if (!isConnected)
      connect();

    return btAddressLong;
  }

  /**
   * Returns library version information.
   * @return library version
   */
  public static String getVersion()
  {
    return VERSION;
  }

  /**
   * Returns copywrite information.
   * @return copywrite information
   */
  public static String getAbout()
  {
    return ABOUT;
  }

  /**
   * Sends a keep alive command to prevent the automatic shutdown of
   * the brick.
   * This may be automated by setting KeepAlive = 1 in the NxtJLib
   * properties file.
   * @see NxtRobot
   */
  public void keepAlive()
  {
    if (!isConnected)
      connect();

    byte[] request =
    {
      DIRECT_COMMAND_NOREPLY, KEEP_ALIVE
    };
    sendData(request);
  }

  /*
   * Starts the given program contained in the brick's file system.
   * @param filename of the file to run.
   * @return true, if successful, otherwise false
   */
  public boolean startProgram(String filename)
  {
    if (!isConnected)
      return false;

    if (!fileExists(filename))
      return false;

    byte[] request =
    {
      DIRECT_COMMAND_NOREPLY, START_PROGRAM
    };
    request =
      appendString(request, filename);
    sendData(request);
    return true;
  }

  /**
   * Start file enumeration in the brick's file system.
   * For a file enumeration do the following<br>
   * Call findFirst(). It returns the FileInfo reference of the first
   * file. If it returns null, there is no file and you can't proceed.<br>
   * Keep calling findNext() until it returns null, meaning there are no more files
   * @return FileInfo reference giving details of the first file found or null if the search fails
   */
  public FileInfo findFirst()
  {
    byte[] request =
    {
      SYSTEM_COMMAND_REPLY, NXJ_FIND_FIRST
    };
    request =
      appendString(request, "*.*");

    byte[] reply = requestData(request);
    FileInfo fileInfo = null;
    if (reply[2] == 0 && reply.length == 32)
    {
      StringBuffer name = new StringBuffer(new String(reply)).delete(0, 4);
      int lastPos = name.toString().indexOf('\0');
      if (lastPos < 0 || lastPos > 20)
        lastPos = 20;
      name.delete(lastPos, name.length());
      fileInfo =
        new FileInfo(name.toString());
      fileInfo.status = 0;
      fileInfo.fileHandle = reply[3];
      fileInfo.fileSize = (0xFF & reply[24])
        | ((0xFF & reply[25]) << 8)
        | ((0xFF & reply[26]) << 16)
        | ((0xFF & reply[27]) << 24);
      fileInfo.startPage = (0xFF & reply[28])
        | ((0xFF & reply[29]) << 8)
        | ((0xFF & reply[30]) << 16)
        | ((0xFF & reply[31]) << 24);
    }

    return fileInfo;
  }

  /**
   * Searches the next file in a file enumeration.
   * @param handle the handle of the previous found file
   * @return FileInfo reference giving details of the file or null if the search fails
   */
  public FileInfo findNext(
    byte handle)
  {
    byte[] request =
    {
      SYSTEM_COMMAND_REPLY, NXJ_FIND_NEXT, handle
    };

    byte[] reply = requestData(request);
    FileInfo fileInfo = null;
    if (reply[2] == 0 && reply.length == 32)
    {
      StringBuffer name = new StringBuffer(new String(reply)).delete(0, 4);
      int lastPos = name.toString().indexOf('\0');
      if (lastPos < 0 || lastPos > 20)
        lastPos = 20;
      name.delete(lastPos, name.length());
      fileInfo =
        new FileInfo(name.toString());
      fileInfo.status = 0;
      fileInfo.fileHandle = reply[3];
      fileInfo.fileSize = (0xFF & reply[24])
        | ((0xFF & reply[25]) << 8)
        | ((0xFF & reply[26]) << 16)
        | ((0xFF & reply[27]) << 24);
      fileInfo.startPage = (0xFF & reply[28])
        | ((0xFF & reply[29]) << 8)
        | ((0xFF & reply[30]) << 16)
        | ((0xFF & reply[31]) << 24);
    }

    return fileInfo;
  }

  /**
   * Searches for given file in the brick's file system.
   * @return true, if file exists, otherwise false
   */
  public boolean fileExists(String filename)
  {
    FileInfo fi = findFirst();
    if (fi == null)
      return false;
    else
    {
      if (fi.fileName.equals(filename))
        return true;
      while ((fi = findNext(fi.fileHandle)) != null)
      {
        if (fi.fileName.equals(filename))
          return true;
      }

      return false;
    }

  }

  private byte[] appendString(byte[] command, String str)
  {
    byte[] buff = new byte[command.length + str.length() + 1];
    for (int i = 0; i
      < command.length; i++)
      buff[i] = command[i];
    for (int i = 0; i
      < str.length(); i++)
      buff[command.length + i] = (byte)str.charAt(i);
    buff[command.length + str.length()] = 0;
    return buff;
  }
}
