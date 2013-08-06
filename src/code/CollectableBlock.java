package code;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class CollectableBlock {

	//basic variables to store position, width/height and the x/y ratios of the map
	public float x, y, ratioX, ratioY, width, height;
	//colors for the inside and outline of the platform
	private Color color = Color.black;
	private Color outline = Color.white;
	Image image;
	public CollectableBlock(float x, float y, String path){
		this.x = x;
		this.y = y;
		Texture t = null;
		try {
			t = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path));
		} catch (IOException e) {}
		image = new Image(t);
		width = image.getWidth();
		height = image.getHeight();
	}

	//contains most the logic for this collectable block
	public void update(){

	}

	//draws this collectableblock
	public void draw(Graphics g, int shiftX, int shiftY) {
		g.drawImage(image, x - shiftX, y - shiftY);
    }

	//returns the x coordinate of this platform
	public float getX(){
		return x;
	}

	//returns the y coordinate of this platform
	public float getY(){
		return y;
	}

	//returns the color of this collectable block
	public Color getColor(){
		return color;
	}
}
