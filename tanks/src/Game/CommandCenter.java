package Game;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Graphics.Sprite;
import Graphics.SpriteSheet;
import Graphics.TextureAtlas;

public class CommandCenter extends Entity {
	private Sprite			 	sprite;
	
	protected CommandCenter(float x, float y, TextureAtlas atlas) {
		super(EntityType.CommandCenter, x, y);
		BufferedImage s = atlas.Cut(19 * 16, 2 * 16, 16, 16);
		SpriteSheet sprSheet =  new SpriteSheet(s, 1, 16);
		sprite = new Sprite(sprSheet, 2);
	}


	@Override
	public boolean Update() {
		int lu = Game.level[(int) (this.y / 16)][(int) (this.x / 16)];
		int ru = Game.level[(int) (this.y / 16)][(int) (this.x / 16) + 1];
		int ld = Game.level[(int) (this.y / 16) + 1][(int) (this.x / 16)];
		int rd = Game.level[(int) (this.y / 16) + 1][(int) (this.x / 16) + 1];
		if((lu == 0) || (ru == 0) || (ld == 0) || (rd == 0))
			return false;
		else
			return true;
	}

	@Override
	public void Render(Graphics2D g) {
		sprite.render(g, this.x, this.y);
	}

}
