package Game;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import Graphics.Sprite;
import Graphics.SpriteSheet;
import Graphics.TextureAtlas;
import IO.input;

public class EnemyBot extends Tank {

	private HeadingEnemy 				headingE;
	private Map<HeadingEnemy, Sprite> 	spriteMap;
	
	private AIBot						controller;			// Мозги танка
	public 	int							tempNumber;			// Позиция  в массиве
	
	public EnemyBot(float x, float y, float scale, float speed, TextureAtlas atlas, int tempNumber) {
		super(EntityType.Enemy, x, y);
		
		this.tempNumber = tempNumber;
		heading = Heading.NORTH;
		headingE = HeadingEnemy.NORTH;
		spriteMap = new HashMap<HeadingEnemy, Sprite>();
		this.speed = speed;
		this.atlas = atlas;
		lastShoot = 0;
		controller = new AIBot(x, y);
		
		for(HeadingEnemy h: HeadingEnemy.values()) {
			SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
			Sprite sprite = new Sprite(sheet, scale);
			spriteMap.put(h, sprite);
		}
	}

	@Override
	public boolean Update()  {
		float newX = x;
		float newY = y;
		boolean	canToMove;
		int direction = controller.getMove(x, y, speed);
		if(direction == 0) {
			heading = Heading.NORTH;
			headingE = HeadingEnemy.NORTH;
			canToMove = Moving((int)newX, (int)(newY - speed), heading);
			newY -=  (canToMove) ? speed : 0;
		} else if(direction == 1) {
			heading = Heading.EAST;
			headingE = HeadingEnemy.EAST;
			canToMove = Moving((int)(newX + speed), (int)newY, heading);
			newX += (canToMove) ? speed : 0;
		}
		else if(direction == 2) {
			heading = Heading.SOUTH;
			headingE = HeadingEnemy.SOUTH;
			canToMove = Moving((int)newX, (int)(newY + speed), heading);
			newY += (canToMove) ? speed : 0;
		}
		else if(direction == 3) {
			heading = Heading.WEST;
			headingE = HeadingEnemy.WEST;
			canToMove = Moving((int)(newX - speed), (int)newY, heading);
			newX -= (canToMove) ? speed : 0;
		}
		else if(direction == -1) {
			
		}
		
		if((Game.player.x > this.x - 16) && (Game.player.x < this.x + 32)) {
			if(lastShoot == 0) {
				lastShoot = 1;
				for(int i = 0 ; i < AMOUNT_OF_BULLETS; i++)
					if(Game.bullets[5 + i + 5 * tempNumber] == null) {
						if((heading == Heading.NORTH) && (y > Game.player.y))
							Game.bullets[5 + i + 5 * tempNumber] = new Bullet(newX + 10, newY, 2, heading, atlas, tempNumber, i);
						else if((heading == Heading.SOUTH) && (y < Game.player.y))
							Game.bullets[5 + i + 5 * tempNumber] = new Bullet(newX + 6, newY, 2, heading, atlas, tempNumber , i);
						
						break;
					}
			}
		} else if((Game.player.y > this.y - 16) && (Game.player.y < this.y + 32)) {
			if(lastShoot == 0) {
				lastShoot = 1;
				for(int i = 0 ; i < AMOUNT_OF_BULLETS; i++)
					if(Game.bullets[5 + i + 5 * tempNumber] == null) {
						if((heading == Heading.EAST) && (x < Game.player.x))
							Game.bullets[5 + i + 5 * tempNumber] = new Bullet(newX, newY + 4, 2, heading, atlas, tempNumber, i);
						else if((heading == Heading.WEST) && (x > Game.player.x))
							Game.bullets[5 + i + 5 * tempNumber] = new Bullet(newX + SPRITE_SCALE, newY + SPRITE_SCALE / 4, 2, heading, atlas, tempNumber, i);
						
						break;
					}
			}
		}
		
		x = newX;
		y = newY;
		
		return true;
	}

	@Override
	public void Render(Graphics2D g) {
		// TODO Auto-generated method stub
		spriteMap.get(headingE).render(g, x, y);
		
		if(lastShoot != 0)
			lastShoot = (lastShoot + 1) % SHOOT_DELAY;
	}

	public void Update(input Input){
		// TODO Auto-generated method stub
	}
	
}
