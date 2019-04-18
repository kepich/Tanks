package Game;
/*
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import Graphics.Sprite;
import Graphics.SpriteSheet;
import Graphics.TextureAtlas;
import IO.input;

public class Enemy extends Entity {
	
	public static final int SPRITE_SCALE		= 16;
	public static final int SPRITES_PER_HEADING = 1;
	public static final int AMOUNT_OF_BULLETS 	= 3;
	public static final int SHOOT_DELAY			= 50;

	private Heading 					heading;
	private HeadingEnemy 				headingE;
	private Map<HeadingEnemy, Sprite> 	spriteMap;
	private int							lastShoot;
	public 	float 						speed;
	public 	TextureAtlas 				atlas;

	public 	int							tempNumber;
	
	public Enemy(float x, float y, float scale, float speed, TextureAtlas atlas, int tempNumber) {
		super(EntityType.Enemy, x, y);
		
		this.tempNumber = tempNumber;
		heading = Heading.NORTH;
		headingE = HeadingEnemy.NORTH;
		spriteMap = new HashMap<HeadingEnemy, Sprite>();
		this.speed = (float) (speed) - 0.9f;
		this.atlas = atlas;
		lastShoot = 0;
		
		for(HeadingEnemy h: HeadingEnemy.values()) {
			SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
			Sprite sprite = new Sprite(sheet, scale);
			spriteMap.put(h, sprite);
		}
	}

	@Override
	public void Update(input Input) {
		float newX = x;
		float newY = y;
		boolean	canToMove;
		if(Input.getKey(KeyEvent.VK_UP)) {
			heading = Heading.NORTH;
			headingE = HeadingEnemy.NORTH;
			canToMove = Moving((int)newX, (int)(newY - speed), heading);
			newY -=  (canToMove) ? speed : 0;
		} else if(Input.getKey(KeyEvent.VK_RIGHT)) {
			heading = Heading.EAST;
			headingE = HeadingEnemy.EAST;
			canToMove = Moving((int)(newX + speed), (int)newY, heading);
			newX += (canToMove) ? speed : 0;
		}
		else if(Input.getKey(KeyEvent.VK_DOWN)) {
			heading = Heading.SOUTH;
			headingE = HeadingEnemy.SOUTH;
			canToMove = Moving((int)newX, (int)(newY + speed), heading);
			newY += (canToMove) ? speed : 0;
		}
		else if(Input.getKey(KeyEvent.VK_LEFT)) {
			heading = Heading.WEST;
			headingE = HeadingEnemy.WEST;
			canToMove = Moving((int)(newX - speed), (int)newY, heading);
			newX -= (canToMove) ? speed : 0;
		}
		else if(Input.getKey(KeyEvent.VK_NUMPAD0)) {
			if(lastShoot == 0) {
				lastShoot = 1;
				for(int i = 0 ; i < AMOUNT_OF_BULLETS; i++)
					if(Game.bullets[5 + i + 5 * tempNumber] == null) {
						switch(heading) {
						case NORTH:
							Game.bullets[5 + i + 5 * tempNumber] = new Bullet(newX + 10, newY, 2, heading, atlas, tempNumber, i);
							break;
						case SOUTH:
							Game.bullets[5 + i + 5 * tempNumber] = new Bullet(newX + 6, newY, 2, heading, atlas, tempNumber , i);
							break;
						case EAST:
							Game.bullets[5 + i + 5 * tempNumber] = new Bullet(newX, newY + 4, 2, heading, atlas, tempNumber, i);
							break;
						case WEST:
							Game.bullets[5 + i + 5 * tempNumber] = new Bullet(newX + SPRITE_SCALE, newY + SPRITE_SCALE / 4, 2, heading, atlas, tempNumber, i);
							break;
						}
						break;
					}
			}
		}
		
		x = newX;
		y = newY;
	}
	
	private boolean Moving(int newX, int newY, Heading head) {
		boolean isCan = false;
	
		int differencial = 3;
		
		int UP_L 	= Game.level[(newY / 16)][((newX + differencial) / 16)];
		int UP_R 	= Game.level[(newY / 16)][((newX - differencial) / 16) + 2];
		int UP_M 	= Game.level[(newY / 16)][((newX) / 16) + 1];
		
		int DOWN_L 	= Game.level[(newY / 16) + 2][((newX + differencial) / 16)];
		int DOWN_R 	= Game.level[(newY / 16) + 2][((newX - differencial) / 16) + 2];
		int DOWN_M 	= Game.level[(newY / 16) + 2][((newX) / 16) + 1];
		
		int LEFT_U 	= Game.level[((newY + differencial) / 16)][((newX) / 16)];
		int LEFT_D 	= Game.level[((newY - differencial) / 16) + 2][((newX) / 16)];
		int LEFT_M 	= Game.level[((newY) / 16) + 1][((newX) / 16)];
		
		int RIGHT_U	= Game.level[((newY + differencial) / 16)][((newX) / 16) + 2];
		int RIGHT_D	= Game.level[((newY - differencial) / 16)+ 2][((newX) / 16) + 2];
		int RIGHT_M	= Game.level[((newY) / 16) + 1][((newX) / 16) + 2];
		
		switch(head) {
		case NORTH:
			isCan = (UP_L == 0 || UP_L == 4) && (UP_R == 0 || UP_R == 4) && (UP_M == 0 || UP_M == 4);
			break;
		case SOUTH:
			isCan = (DOWN_L == 0 || DOWN_L == 4) && (DOWN_R == 0 || DOWN_R == 4) && (DOWN_M == 0 || DOWN_M == 4);
			break;
		case EAST:
			isCan = (RIGHT_U == 0 || RIGHT_U == 4) && (RIGHT_D == 0 || RIGHT_D == 4) && (RIGHT_M == 0 || RIGHT_M == 4);
			break;
		case WEST:
			isCan = (LEFT_U == 0 || LEFT_U == 4) && (LEFT_D == 0 || LEFT_D == 4)  && (LEFT_M == 0 || LEFT_M == 4);
			break;
		}
		
		return isCan;
		
	}

	@Override
	public void Render(Graphics2D g) {
		// TODO Auto-generated method stub
		spriteMap.get(headingE).render(g, x, y);
		
		if(lastShoot != 0)
			lastShoot = (lastShoot + 1) % SHOOT_DELAY;
	}

	public boolean Update() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
*/