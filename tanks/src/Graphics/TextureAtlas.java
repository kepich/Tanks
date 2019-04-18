package Graphics;

import java.awt.image.BufferedImage;

import util.ResourceLoader;

public class TextureAtlas {
	
	BufferedImage image;
	
	public TextureAtlas(String ImageName) {
		image = ResourceLoader.loadImage(ImageName);
	}
	
	public BufferedImage Cut(int x, int y, int w, int h) {
		return image.getSubimage(x, y, w, h);
	}
}
