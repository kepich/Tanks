package Game;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import Graphics.Sprite;
import Graphics.SpriteSheet;
import Graphics.TextureAtlas;
import IO.input;

public class Player extends Tank {
	
	public static final int AMOUNT_OF_HEARTS	= 3;
	private Map<Heading, Sprite> 	spriteMap;
	private int						lifes;
	
	public Player(float x, float y, float scale, float speed, TextureAtlas atlas) {
		super(EntityType.Player, x, y);
		lifes = AMOUNT_OF_HEARTS;
		this.x = x;
		this.y = y;
		lastShoot = 0;
		
		heading = Heading.NORTH;
		spriteMap = new HashMap<Heading, Sprite>();
		this.speed = speed;
		this.atlas = atlas;
		
		for(Heading h: Heading.values()) {
			SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
			Sprite sprite = new Sprite(sheet, scale);
			spriteMap.put(h, sprite);
		}
	}

	@Override
	public void Update(input Input) {
		if(lastShoot != 0)
			lastShoot = (lastShoot + 1) % SHOOT_DELAY;
		
		float newX = x;
		float newY = y;
		boolean	canToMove;
		if(Input.getKey(KeyEvent.VK_W)) {								// ”правление
			heading = Heading.NORTH;
			canToMove = Moving((int)newX, (int)(newY - speed), heading);
			newY -=  (canToMove) ? speed : 0;
		} else if(Input.getKey(KeyEvent.VK_D)) {
			heading = Heading.EAST;
			canToMove = Moving((int)(newX + speed), (int)newY, heading);
			newX += (canToMove) ? speed : 0;
		}
		else if(Input.getKey(KeyEvent.VK_S)) {
			heading = Heading.SOUTH;
			canToMove = Moving((int)newX, (int)(newY + speed), heading);
			newY += (canToMove) ? speed : 0;
		}
		else if(Input.getKey(KeyEvent.VK_A)) {
			canToMove = Moving((int)(newX - speed), (int)newY, heading);
			newX -= (canToMove) ? speed : 0;
			heading = Heading.WEST;
		}
		else if(Input.getKey(KeyEvent.VK_SPACE)) {
			if(lastShoot == 0) {
				lastShoot = 1;
				for(int i = 0; i < AMOUNT_OF_BULLETS; i++)
					if(Game.bullets[i] == null) {
						switch(heading) {
						case NORTH:
							Game.bullets[i] = new Bullet(newX + 10, newY, 2, heading, atlas, -1, i);
							break;
						case SOUTH:
							Game.bullets[i] = new Bullet(newX + 6, newY, 2, heading, atlas,  -1, i);
							break;
						case EAST:
							Game.bullets[i] = new Bullet(newX, newY + 4, 2, heading, atlas,  -1, i);
							break;
						case WEST:
							Game.bullets[i] = new Bullet(newX + SPRITE_SCALE, newY + SPRITE_SCALE / 4, 2, heading, atlas,  -1, i);
							break;
						}
						break;
					}
			}
		}
		
		x = newX;
		y = newY;
	}

	@Override
	public void Render(Graphics2D g) {
		spriteMap.get(heading).render(g, x, y);
	}

	@Override
	public boolean Update() {
		return false;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float newSpeed) {
		speed = newSpeed;
	}
	
	public Heading getHeading() {
		return heading;
	}
	
	public void setHeading(Heading newHeading) {
		heading = newHeading;
	}
	
	public int getLifes() {
		return lifes;
	}
	
	public void decreaseLifes() {
		lifes--;
	}
	public void setLifes(int lifes) {
		this.lifes = lifes;
	}
}
