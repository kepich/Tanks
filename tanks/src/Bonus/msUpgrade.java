package Bonus;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Game.EntityType;
import Game.Game;
import Graphics.Sprite;
import Graphics.SpriteSheet;
import Graphics.TextureAtlas;

public class msUpgrade extends Bonus {
	public static float 		MOVESPEED_UP = 1.5f;			// Увеличение скорости
	private Sprite			 	sprite;
	public static boolean		isActive = false;				// Флаг активности
	private float				tempSpeed;
	
	public msUpgrade(float x, float y,  TextureAtlas atlas) {
		super(EntityType.Bonus, x, y);
		BufferedImage s = atlas.Cut(17 * 16, 7 * 16, 16, 16);
		SpriteSheet sprSheet =  new SpriteSheet(s, 1, 16);
		sprite = new Sprite(sprSheet, 2);
		isActive = false;
	}

	@Override
	public boolean Update() {
		if(!isActive) {
			if(((Game.player.getX() + 32) >= this.x) && (Game.player.getX() <= (this.x + 32)) && ((Game.player.getY() + 32) >= this.y) && (Game.player.getY() <= (this.y + 32))) {
				isActive = true;
				tempSpeed = Game.player.getSpeed();
				Game.player.setSpeed(tempSpeed * MOVESPEED_UP);
			}else {
				tempLifeTime--;
				if(tempLifeTime == 0) {
					Game.moveSpeedUpgrade = null;
				}
			}
		} else {
			tempTime--;
			if(tempTime == 0) {
				isActive = false;
				Game.player.setSpeed(tempSpeed);
				Game.moveSpeedUpgrade = null;
			}
		}
		return false;
	}

	@Override
	public void Render(Graphics2D g) {
		if(!isActive)
			sprite.render(g, this.x, this.y);
	}

}
