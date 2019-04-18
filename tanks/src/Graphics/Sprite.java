package Graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import util.Utils;

public class Sprite {
	private SpriteSheet 	sheet;
	private float 			scale;
	private BufferedImage 	image;
	
	public Sprite(SpriteSheet sheet, float scale) {
		this.sheet = sheet;
		this.scale = scale;
		image = this.sheet.getSprite(0);
		image = Utils.resize(image, (int)(image.getWidth() * this.scale), (int)(image.getHeight() * this.scale));
	}
	
	public void render(Graphics2D g, float x, float y){
		g.drawImage(image, (int)(x), (int)(y), null);
	}
}
