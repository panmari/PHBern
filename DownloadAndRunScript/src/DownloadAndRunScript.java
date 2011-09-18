import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadAndRunScript {

	private static final String fileName = "test1.py";
	private static final String dlURL = "http://clab1.phbern.ch/jOnline/";
	private static final String userHome = System.getProperty("user.home");
	private static final String fs = System.getProperty("file.separator");
	private static final String os = System.getProperty("os.name");

	public static void main(String[] args) {
		System.out.println("This Program is only a little test. It should " +
				"download a small & harmless script on Linux, then run it." +
				"(It's written in Python, though. Bash should behave exactly the same way.)");
		if (!os.equals("Linux"))
			System.out.println("This computer doesn't run Linux -> Abort!");
		copyFile(dlURL + fileName, userHome + fs + fileName);
		
		try {
			Runtime.getRuntime().exec(userHome + fs + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ========================= copyFile() ===========================
	static private boolean copyFile(String srcUrl, String dstFile)
	// copy file 'srcUrl' to local file 'dstFile'
	// e.g. getFile("http://clab1.phbern.ch/Ex1.bin", "c:\scratch\Ex1.bin")
	{
		// System.out.println("Src: " + srcUrl + " Dst: " + dstFile);

		File fdstFile = new File(dstFile);
		if (fdstFile.exists())
			fdstFile.delete();

		InputStream inp = null;
		FileOutputStream out = null;

		try {
			URL url = new URL(srcUrl);
			inp = url.openStream();
			out = new FileOutputStream(dstFile);

			byte[] buff = new byte[8192];
			int count;
			while ((count = inp.read(buff)) != -1)
				out.write(buff, 0, count);
			fdstFile.setExecutable(true);
		} catch (IOException ex) {
			return false;
		} finally {
			try {
				if (inp != null)
					inp.close();
				if (out != null)
					out.close();
			} catch (IOException ex) {
			}
		}
		return true;
	}
}