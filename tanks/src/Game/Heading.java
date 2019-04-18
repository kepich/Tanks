package Game;

import java.awt.image.BufferedImage;

import Graphics.TextureAtlas;

public enum Heading{
	NORTH(0 * 16, 0 * 16, 1 * 16, 1 * 16),
	EAST(6 * 16, 0 * 16, 16, 16),
	SOUTH(4 * 16, 0 * 16, 16, 16),
	WEST(2 * 16, 0 * 16, 16, 16);
	
	private int x, y, h, w;
	Heading(int x, int y, int h, int w){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public BufferedImage texture(TextureAtlas atlas) {
		return 	atlas.Cut(x, y, w, h);
	}
}