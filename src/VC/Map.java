package VC;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Map{
	//info about how the map is rendered. not sure how this fits with the new setup
	int blockWidth = 10;
	int blockHeight = 10;
	//used to position the player
	public int playerX, playerY;

	//game objects that belong to the map
	public List<Platform> platforms = new ArrayList<Platform>();
	public List<CollectableBlock> collectableBlocks = new ArrayList<>();

	//map settings
	Image bg;
	int backgroundX, backgroundY;
	public int width, height;

	/**
	 * Updates the map, and all game objects that are part of the map:
	 * {Platform, CollectableBlock}
	 *
	 * @param player the map uses the player to react given the circumestances
	 */
	public void update(Player player){
		for(Platform p : platforms){
			double distance = Math.sqrt(Math.pow((p.x + p.width/2) - (player.x + player.width/2), 2) + Math.pow((p.y + p.height/2) - (player.y + player.height/2), 2));
			if(!p.isRandom && distance < 16){
				p.setRandomColor(System.currentTimeMillis());
			}else if(p.isRandom && distance >= 16)
				p.resetColor();

			p.update();
		}
	}

	/**
	 * 	draws the map and all contained objects
	 * {Platform, CollectableBlock}
	 *
	 * @param g
	 * @param viewportX
	 * @param viewportY
	 */
	public void draw(Graphics g, int viewportX, int viewportY){

		for(Platform p : platforms)
			p.draw(g, viewportX, viewportY);
		for(CollectableBlock b : collectableBlocks)
			b.draw(g, viewportX, viewportY);
	}

	public void placeCollectableBlock(Player p, Color c){
		//do check for collision with other world items
		CollectableBlock cb =  new CollectableBlock(p.x, p.y, GameplayState.VIEWPORT_RATIO_X, GameplayState.VIEWPORT_RATIO_Y, c);
		p.y -= p.height +15;
		collectableBlocks.add(cb);
	}

	/**
	 * Key for the objects referred to in a map
	 *  Input: .map, located in the /res/directory. If you don't have one, here is the project structure
	 *
	 *  	platformer
	 *  		src
	 *  			VS
	 *  				map.java
	 *  				... etc
	 *  			Res
	 *  				level_n_n.map
	 *  				<player>_settings.xml??
	 *  				players.txt
	 *  
	 *  Output: Diagnostics
	 *
	 *
	 *
	 *
	 *  1-platform(1x1)
	 * 	2-player(1x1)
	 *  3-checkpoint(variable)
	 * TODO implement checkpoint variability
	 *  4-
	 *  5-
	 * @param filename				map filename
	 * @return						true if the map is loaded, false otherwise
	 * @throws SlickException
	 * @throws IOException
	 */
	public boolean loadMap(String path) throws SlickException, IOException{

		//reads in the .map file and stores it in a string.
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String s = new String();
		String line;
		while((line = reader.readLine()) != null){
				s += line;
			}

		//seperate the map image, the dimensions and the board itself
		/*
		 *** DIMENSTIONS AND SETTINGS
		 */
		s = s.substring(s.indexOf("\"") + 1);
		String bgname = s.substring(0, s.indexOf("\""));
		//this is for a potential background image to be loaded in.
		//bg = new Image(bgname);
		s = s.substring(s.indexOf(",") + 1);
		s = s.substring(s.indexOf("?") + 1);
		String dimensions = s.substring(0, s.indexOf("?"));
		//actual dimensions
		width = Integer.parseInt(dimensions.substring(0, s.indexOf(":")));
		height = Integer.parseInt(dimensions.substring(s.indexOf(":") + 1));

		/*
		 * *** BOARD
		 */
		s = s.substring(s.indexOf(";") + 1);
		String[] mapRows = s.split(";");
		//the actual mapping of things
		String[][] map = new String[mapRows.length][];
		for(int i = 0; i < mapRows.length; i++){
			map[i] = mapRows[i].split(",");
		}

		/*THIS IS WHERE THE BOARD IS ACTUALLY PARSED
		 * .map files will have a nxn array in it
		 *  0 = space
		 *  1 = block
		 *  2 =
		 *  3 =
		 *  4 =
		 *  5 =
		 *  6 =
		 *  7 =
		 *  8 =
		 *  9 = player
		 *
		 */
		for(int r = 0; r < map.length; r++){
			for(int c = 0; c < map[r].length; c++){
				map[r][c] = map[r][c].trim();
				//get the current number from the array
				//int currentDigit = Integer.parseInt(map[r][c]);
				//the x and y coordinates are the row & column * 18.
				int x = c * 10;
				int y = r * 10;

				//Will create the object at the top left corner of the !!RECTANGULAR!! map representation.
				if(map[r][c].equals("0")){

				}else if(map[r][c].equals("1")){
					//Image texture = new Image("res/brown_block_reg.png");
					platforms.add(new Platform(x*GameplayState.VIEWPORT_RATIO_X, y*GameplayState.VIEWPORT_RATIO_Y,
							GameplayState.VIEWPORT_RATIO_X, GameplayState.VIEWPORT_RATIO_Y));
				}else if(map[r][c].equals("r")){

					collectableBlocks.add(new CollectableBlock(x*GameplayState.VIEWPORT_RATIO_X, y*GameplayState.VIEWPORT_RATIO_Y,
							GameplayState.VIEWPORT_RATIO_X, GameplayState.VIEWPORT_RATIO_Y, Color.blue));
				}else if(map[r][c].equals("3")){
					//TODO count the rows & columns
					//TODO store it
					//TODO use it in isTopLeft :)
				}else if(map[r][c].equals("4")){

				}else if(map[r][c].equals("5")){

				}else if(map[r][c].equals("6")){

				}else if(map[r][c].equals("7")){

				}else if(map[r][c].equals("8")){

				}else if(map[r][c].equals("9")){
					playerX = x;
					playerY = y;
				}
			}
		}

		return true;
	}

	/***
	 * This method checks if the item is in topleft, this is a sort of "multi block" structures
	 *
	 * @deprecated
	 * @param item which item you are using 0 = blank space, 2 = block, 3 = block
	 * @param r
	 * @param c
	 * @param map
	 * @return
	 */
	private boolean isTopLeft(int item, int r, int c, String[][] map){
		//width and height measured in # of map "steps" (10px ea)
		int width = 1;
		int height = 1;

		if(item == 0){

		}else if(item == 1){
			width = blockWidth/10;
			height = blockHeight/10;
		}else if(item == 2){
			width = blockWidth/10;
			height = blockHeight/10;
		}else if(item == 3){

		}else if(item == 4){

		}else if(item == 5){

		}else if(item == 6){

		}else if(item == 7){

		}else if(item == 8){

		}else if(item == 9){

		}

		//counts backwards and upwards to see how many blocks of the same type preceed it.
		int curR = r - 1;
		int prevWidthCount = 0;
		int prevHeightCount = 0;
		while(curR >= 0 && map[curR][c].equals("" + item)){
			curR -= 1;
			prevWidthCount += 1;
		}
		int curC = c - 1;
		while(curC >= 0 && map[r][curC].equals("" + item)){
			curC -= 1;
			prevHeightCount += 1;
		}


		if(prevWidthCount % width == 0 && prevHeightCount % height == 0){
			return true;
		}

		return false;

	}

	public int countObjectsLeft(String[][] map, int r, int c){
		int counter = 0;
		while(c >= 0){
			if(isTopLeft(Integer.parseInt(map[r][c]), r, c, map))
				counter++;
			c -= 1;
		}
		return counter;
	}
	public int countObjectsUp(String[][] map, int r, int c){
		int counter = 0;
		while(r >= 0){
			if(isTopLeft(Integer.parseInt(map[r][c]), r, c, map))
				counter++;
			r -= 1;
		}
		return counter;
	}

}
