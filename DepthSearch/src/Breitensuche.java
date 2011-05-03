import java.awt.Color;
import java.io.*;
import java.util.LinkedList;

import javax.swing.JFileChooser;

import ch.aplu.util.*;
import ch.beattl.labyrinth.*;

class Breitensuche
{
	GPanel p;
	int laenge, hoehe;
	int startX, startY, zielX, zielY;
	Cell[][] labyrinth;
	
	public void liesDaten()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Auswahl des Labyrinths");
		if(chooser.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
			System.exit(1);
		File file = new File(chooser.getCurrentDirectory(), chooser.getSelectedFile().getName());
		
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
				if (labyrinth[x][y].up)
					p.line(x, y, (x + 1), y);
				if (labyrinth[x][y].left)
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
	
	void sucheWeg()
	{
		Position[][] weg = new Position[laenge][hoehe];
		LinkedList<Position> warteschlange = new LinkedList<Position>();
		labyrinth[startX][startY].dist = 0;
		warteschlange.offer(new Position(startX, startY));
		
		// Breitensuche im Labyrinth
		while(!warteschlange.isEmpty())
		{
			Position pos = warteschlange.poll();
			if(!labyrinth[pos.x][pos.y].reached)
			{
				if(pos.x == zielX && pos.y == zielY)
					break;
				if(!(pos.x == startX && pos.y == startY))
				{
					wait(50);
					p.color(Color.cyan);
					p.move(pos.x+.5, pos.y+.5);
					p.fillCircle(0.45);
				}
				labyrinth[pos.x][pos.y].reached = true;
				if(!labyrinth[pos.x][pos.y].up && !labyrinth[pos.x][pos.y-1].reached)
				{
					labyrinth[pos.x][pos.y-1].dist = labyrinth[pos.x][pos.y].dist + 1;
					weg[pos.x][pos.y-1] = pos;
					warteschlange.offer(new Position(pos.x, pos.y-1));
				}
				if(!labyrinth[pos.x][pos.y].left && !labyrinth[pos.x-1][pos.y].reached)
				{
					labyrinth[pos.x-1][pos.y].dist = labyrinth[pos.x][pos.y].dist + 1;
					weg[pos.x-1][pos.y] = pos;
					warteschlange.offer(new Position(pos.x-1, pos.y));
				}
				if(!labyrinth[pos.x][pos.y+1].up  && !labyrinth[pos.x][pos.y+1].reached)
				{
					labyrinth[pos.x][pos.y+1].dist = labyrinth[pos.x][pos.y].dist + 1;
					weg[pos.x][pos.y+1] = pos;
					warteschlange.offer(new Position(pos.x, pos.y+1));
				}
				if(!labyrinth[pos.x+1][pos.y].left  && !labyrinth[pos.x+1][pos.y].reached)
				{
					labyrinth[pos.x+1][pos.y].dist = labyrinth[pos.x][pos.y].dist + 1;
					weg[pos.x+1][pos.y] = pos;
					warteschlange.offer(new Position(pos.x+1, pos.y));
				}
			}
		}
		
		// Folge Rueckweg zum Start und markiere ihn
		Position pos = weg[zielX][zielY];
		while(pos.x != startX || pos.y != startY)
		{
			wait(50);
			p.color(Color.green);
			p.move(pos.x+.5, pos.y+.5);
			p.fillCircle(0.45);
			p.color(Color.black);
			if(labyrinth[pos.x][pos.y].dist < 10)
				p.move(pos.x+.4, pos.y+.7);
			else if (labyrinth[pos.x][pos.y].dist < 100)
				p.move(pos.x+.25, pos.y+.7);
			else
				p.move(pos.x+.1, pos.y+.7);
			p.text("" + labyrinth[pos.x][pos.y].dist);
			pos = weg[pos.x][pos.y];
		}
		wait(50);
		p.move(pos.x+.4, pos.y+.65);
		p.text("" + labyrinth[pos.x][pos.y].dist);
	}
	
	Breitensuche()
	{
		liesDaten();
		zeichneLabyrinth();
		sucheWeg();
	}
	
	public static void main(String[] args)
	{
		new Breitensuche();
	}
}