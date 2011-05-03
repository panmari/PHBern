import java.awt.Color;
import java.io.*;

import ch.aplu.util.*;
import ch.beattl.labyrinth.*;

class Tiefensuche
{
	GPanel p;
	int laenge, hoehe;
	int startX, startY, zielX, zielY;
	Cell[][] labyrinth;
	
	public void liesDaten()
	{
		File file = new File("Labyrinth.txt");
		
		//Einlesen der Daten aus dem Dokument
		try
		{
			LabyrinthReader myReader = new LabyrinthReader(new FileReader(file));
			laenge = myReader.getXcells();
			hoehe = myReader.getYcells();
			labyrinth = myReader.getLabyrinth();
			startX = myReader.getStartX();
			startY = myReader.getStartY();
			zielX = myReader.getZielX();
			zielY = myReader.getZielY();
			myReader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	void wait(int timeout)
	{
		try
		{
			Thread.sleep(timeout);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	void zeichneLabyrinth()
	{
		int seitenlaenge = Math.max(laenge, hoehe);
		p  = new GPanel(0, seitenlaenge, seitenlaenge, 0);

		// Labyrinth zeichnen
		p.color(Color.black);
		for (int y = 0; y <= hoehe; y++)
		{
			for (int x = 0; x <= laenge; x++)
			{
				if (labyrinth[x][y].isUp())
					p.line(x, y, (x + 1), y);
				if (labyrinth[x][y].isLeft())
					p.line(x, y, x, (y + 1));
			}
		}
		
		// Start- und Zielpunkt zeichnen
		p.color(Color.green);
		p.move(startX+0.5, startY+0.5);
		p.fillCircle(0.45);
		p.color(Color.red);
		p.move(zielX+0.5, zielY+0.5);
		p.fillCircle(0.45);		
	}
	
	void sucheWeg(int x, int y, int dist)
	{
		if(labyrinth[zielX][zielY].isReached() || labyrinth[x][y].isReached())
			return;
		else
		{
			labyrinth[x][y].setReached(true);
			if(x == zielX && y == zielY)
				return;
			else
			{
				// Aktuelle Zelle markieren und beschriften
				wait(50);
				p.color(Color.green);
				p.move(x+.5, y+.5);
				p.fillCircle(0.45);
				p.color(Color.black);
				if(dist < 10)
					p.move(x+.4, y+.65);
				else if (dist < 100)
					p.move(x+.25, y+.65);
				else
					p.move(x+.1, y+.65);
				p.text("" + dist);
				
				// Naechste Zelle bestimmen (rekursiv)
				if(!labyrinth[x][y].isUp())
					sucheWeg(x, y-1, dist+1);
				if(!labyrinth[x][y].isLeft())
					sucheWeg(x-1, y, dist+1);
				if(!labyrinth[x][y+1].isUp())
					sucheWeg(x, y+1, dist+1);
				if(!labyrinth[x+1][y].isLeft())
					sucheWeg(x+1, y, dist+1);
				
				// Falls das Ziel auf diesem Weg nicht erreicht, Zelle hellblau faerben
				if(!labyrinth[zielX][zielY].isReached())
				{
					wait(50);
					p.color(Color.cyan);
					p.move(x+.5, y+.5);
					p.fillCircle(0.45);
				}
			}
		}
	}
	
	Tiefensuche()
	{
		liesDaten();
		zeichneLabyrinth();
		wait(2000);
		sucheWeg(startX, startY, 0);
	}
	
	public static void main(String[] args)
	{
		new Tiefensuche();
	}
}