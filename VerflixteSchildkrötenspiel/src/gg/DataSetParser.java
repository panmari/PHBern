package gg;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DataSetParser {

	Scanner parseScanner;
	
	public DataSetParser(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		parseScanner = new Scanner(file);
		parseScanner.useDelimiter("\\r?\\n");
	}

	/**
	 * TODO: Do not allow more than 9 cards (or do we want to allow that?)
	 * @return
	 */
	public List<TurtleCard> parse() {
		LinkedList<TurtleCard> tcList = new LinkedList<TurtleCard>();
		TurtleCardFactory tf = TurtleCardFactory.getInstance();
		while (parseScanner.hasNextLine()) {
			String line = parseScanner.nextLine();
			int firstSpace = line.indexOf(" ");
			String tcString = line.substring(0, firstSpace);
			String sprite = line.substring(firstSpace + 1);
			tcList.add(tf.makeTurtleCard(tcString, sprite));
		}
		return tcList;
	}
}
