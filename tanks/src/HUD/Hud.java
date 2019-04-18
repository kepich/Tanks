package HUD;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Bonus.msUpgrade;
import Game.Entity;
import Game.EntityType;
import Graphics.Sprite;
import Graphics.SpriteSheet;
import Graphics.TextureAtlas;

public class Hud extends Entity{
	private BufferedImage 				background;
	
	private static final String 		backgroundPath = "Res/hud.png";
	private static Sprite[]				numbers = new Sprite[10];
	private static Sprite				hp;
	private static Sprite				SpeedUp;
	
	public Hud(TextureAtlas atlas) {
		super(EntityType.Hud, 608, 0);
		try {
			background = ImageIO.read(new File(backgroundPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < 2; i++)
			for(int j = 0; j < 5; j++) {
				SpriteSheet sh = new SpriteSheet(atlas.Cut(20 * 16 + 8 + j * 8, 16 * 11 + i * 8 + 8, 8, 8), 1, 8);
				numbers[i*5 + j] = new Sprite(sh, 4);
			}
		SpriteSheet sh = new SpriteSheet(atlas.Cut(20 * 16 + 8, 16 * 12 + 8, 8, 8), 1, 8);
		hp = new Sprite(sh, 4);
		
		sh =  new SpriteSheet(atlas.Cut(17 * 16, 7 * 16, 16, 16), 1, 16);
		SpeedUp = new Sprite(sh, 3);
	}


	@Override
	public boolean Update() {
		return false;
	}

	@Override
	public void Render(Graphics2D g) {
		g.drawImage(background, 640, 0, null);
		numbers[Game.Game.getAmountOfEnemysWave()].render(g, 640 + 32 * 2, 32 * 5);
		
		for(int i = 0; i < Game.Game.player.getLifes(); i++) {
			hp.render(g, 640 + 24 + 32 * i + i * 8, 32 * 2);
		}
		
		if(msUpgrade.isActive)
			SpeedUp.render(g, 640 + 16, 32 * 8 + 16);
			
	}

}
