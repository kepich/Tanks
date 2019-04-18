package Game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import Graphics.Sprite;
import Graphics.SpriteSheet;
import Graphics.TextureAtlas;

public class Bullet extends Entity{
	public static final int SPRITE_SCALE = 8;
	public static final int SPRITES_PER_HEADING = 1;
	public static final int BULLET_SPEED = 4;
	
	public enum HeadingBullet{
		UP(20 * SPRITE_SCALE * 2 + 2, 6 * SPRITE_SCALE * 2 + 2, 1 * SPRITE_SCALE, 1 * SPRITE_SCALE),
		RIGHT(20 * SPRITE_SCALE * 2 + 3 * SPRITE_SCALE, 6 * SPRITE_SCALE * 2 + 2, SPRITE_SCALE, SPRITE_SCALE),
		DOWN(20 * SPRITE_SCALE * 2 + 2 * SPRITE_SCALE, 6 * SPRITE_SCALE * 2 + 2, SPRITE_SCALE, SPRITE_SCALE),
		LEFT(20 * SPRITE_SCALE * 2 + 1 * SPRITE_SCALE, 6 * SPRITE_SCALE * 2 + 2, SPRITE_SCALE, SPRITE_SCALE);
		
		private int x, y, h, w;
		
		HeadingBullet(int x, int y, int h, int w){
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
		
		protected BufferedImage texture(TextureAtlas atlas) {
			return 	atlas.Cut(x, y, w, h);
		}
	}

	private Map<HeadingBullet, Sprite> 		spriteMap;
	private float 							scale;
	private float 							speed;
	public HeadingBullet					heading;
	public Heading							headingP;
	private int								ParentNumber;
	private int								Number;

	protected Bullet(float x, float y, float scale, Heading heading, TextureAtlas atlas, int parent, int number) {
		super(EntityType.Bullet, x, y);
		
		ParentNumber = parent;
		Number = number;
		headingP = heading;
		switch(heading) {
			case NORTH: this.heading = HeadingBullet.UP; break;
			case SOUTH: this.heading = HeadingBullet.DOWN; break;
			case EAST: this.heading = HeadingBullet.RIGHT; break;
			case WEST: this.heading = HeadingBullet.LEFT; break;
		}
		
		spriteMap = new HashMap<HeadingBullet, Sprite>();
		this.scale = scale;
		this.speed = BULLET_SPEED;
		
		for(HeadingBullet h: HeadingBullet.values()) {
			SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
			Sprite sprite = new Sprite(sheet, scale);
			spriteMap.put(h, sprite);
		}
	}

	public Bullet(float x, float y, float scale, HeadingBullet heading, TextureAtlas atlas, int parent, int number) {
		super(EntityType.Bullet, x, y);
		
		ParentNumber = parent;
		Number = number;
		this.heading = heading;
		
		spriteMap = new HashMap<HeadingBullet, Sprite>();
		this.scale = scale;
		this.speed = BULLET_SPEED;
		
		for(HeadingBullet h: HeadingBullet.values()) {
			SpriteSheet sheet = new SpriteSheet(h.texture(atlas), SPRITES_PER_HEADING, SPRITE_SCALE);
			Sprite sprite = new Sprite(sheet, scale);
			spriteMap.put(h, sprite);
		}
	}

	@Override
	public boolean Update() {
		// TODO Auto-generated method stub
		
		float newX = x;
		float newY = y;
		boolean isBullet = true;
		boolean isCan = false;
		
		if(heading == HeadingBullet.UP) {
			isCan = Moving((int)newX, (int)(newY - speed), heading) && KillCheck();
			newY -= (isCan) ? speed : 0;
			isBullet = isCan;
		} else if(heading == HeadingBullet.RIGHT) {
			isCan = Moving((int)(newX + speed), (int)(newY), heading) && KillCheck();
			newX += (isCan) ? speed : 0;
			isBullet = isCan;
		}
		else if(heading == HeadingBullet.DOWN) {
			isCan = Moving((int)(newX), (int)(newY + speed), heading) && KillCheck();
			newY += (isCan) ? speed : 0;
			isBullet = isCan;
		}
		else if(heading == HeadingBullet.LEFT) {
			isCan = Moving((int)(newX - speed), (int)(newY), heading) && KillCheck();
			newX -= (isCan) ? speed : 0;
			isBullet = isCan;
		}
		
		if(newX < 0) {
			isBullet = false;
		}else if(newX >= Game.WIDTH - SPRITE_SCALE * scale - 1) {
			isBullet = false;
		}
		
		if(newY < 0) {
			isBullet = false;
		}else if(newY >= Game.HEIGHT - SPRITE_SCALE * scale - 1) {
			isBullet = false;
		}
		
		x = newX;
		y = newY;
		
		if(!isBullet) {
			if(ParentNumber < 0)
				Game.bullets[Number] = null;
			else
				Game.bullets[5 + ParentNumber * 5 + Number] = null;
		};
		
		return true;
	}

	@Override
	public void Render(Graphics2D g) {
		// TODO Auto-generated method stub
		spriteMap.get(heading).render(g, x, y);
	}
	
	private boolean Moving(int newX, int newY, HeadingBullet head) {				// Можно ли двигаться в направлении head
		boolean isCan = false;
		
		if(newX < 0) {
			return false;
		}else if(newX >= Game.WIDTH - SPRITE_SCALE * scale) {
			return false;
		}
		
		if(newY < 0) {
			return false;
		}else if(newY >= Game.HEIGHT - SPRITE_SCALE * scale) {
			return false;
		}
		
		int UP_L 	= Game.level[((newY + SPRITE_SCALE / 2) / 16)][((newX + 4) / 16)];
		int UP_R 	= Game.level[((newY + SPRITE_SCALE / 2) / 16)][((newX + 8) / 16)];
		
		int DOWN_L 	= Game.level[((newY + SPRITE_SCALE * 2) / 16)][((newX + 4) / 16)];
		int DOWN_R 	= Game.level[((newY + SPRITE_SCALE * 2) / 16)][((newX + 8) / 16)];
		
		int LEFT_U 	= Game.level[((newY + 4) / 16)][((newX) / 16)];
		int LEFT_D	= Game.level[((newY) / 16) + 1][((newX) / 16)];
		
		int RIGHT_U	= Game.level[((newY + 4) / 16)][((newX) / 16) + 1];
		int RIGHT_D	= Game.level[((newY) / 16) + 1][((newX) / 16) + 1];
		
		switch(head) {
		case UP:
			isCan = ((UP_L != 1 && UP_L != 2 && UP_L != 5) && (UP_R != 1 && UP_R != 2 && UP_R != 5));
			if(!isCan) {
				if(UP_L == 1 || UP_L == 5)
					Game.level[(newY + SPRITE_SCALE / 2) / 16][(newX + 4) / 16] = 0;
				else if (UP_R == 1 || UP_R == 5)
					Game.level[(newY + SPRITE_SCALE / 2) / 16][(newX + 8) / 16] = 0;
			}
			break;
		case DOWN:
			isCan = ((DOWN_L != 2  &&  DOWN_L != 1 &&  DOWN_L != 5) && (DOWN_R != 1 && DOWN_R != 2 && DOWN_R != 5));
			if(!isCan) {
				if(DOWN_L == 1 || DOWN_L == 5)
					Game.level[((newY + SPRITE_SCALE * 2) / 16)][((newX + 4) / 16)] = 0;
				else if(DOWN_R == 1 || DOWN_R == 5)
					 Game.level[((newY + SPRITE_SCALE * 2) / 16)][((newX + 8) / 16)] = 0;
			}
			break;
		case RIGHT:
			isCan = (RIGHT_U != 1  &&  RIGHT_D != 2 &&  RIGHT_D != 5) && (RIGHT_D != 1  &&  RIGHT_D != 2 &&  RIGHT_D != 5);
			if(!isCan) {
				if(RIGHT_U == 1 || RIGHT_U == 5)
					Game.level[((newY + 4) / 16)][((newX) / 16) + 1] = 0;
				else if(RIGHT_D == 1 || RIGHT_D == 5)
					Game.level[((newY) / 16) + 1][((newX) / 16) + 1] = 0;
			}
			break;
		case LEFT:
			isCan = ((LEFT_U != 1  &&  LEFT_U != 2 &&  LEFT_U != 5) && (LEFT_D != 1  &&  LEFT_D != 2 &&  LEFT_D != 5));
			if(!isCan) {
				if(LEFT_U == 1 || LEFT_U == 5)
					Game.level[((newY + 4) / 16)][((newX) / 16)] = 0;
				else if(LEFT_D == 1 || LEFT_D == 5) 
					Game.level[((newY) / 16) + 1][((newX) / 16)] = 0;
			}
			break;
		}
		
		return isCan;
	}
	
	private boolean KillCheck() {											// Проверка на нанесение урона танкам
		if(ParentNumber == -1) {
			for(int i = 0; i < Game.AMOUNT_OF_ENE; i++) {
				if(Game.enemy[i] != null && i != ParentNumber) {
					if(this.x > Game.enemy[i].x && this.x < (Game.enemy[i].x + Player.SPRITE_SCALE * 2) && 
							this.y > Game.enemy[i].y && this.y < (Game.enemy[i].y + Player.SPRITE_SCALE * 2 - Player.SPRITE_SCALE / 1.5)) {
						Game.enemy[i] = null;
						return false;
					}
				}
			}
		}
		else {
			if(this.x > Game.player.x && this.x < (Game.player.x + Player.SPRITE_SCALE * 2) && 
					this.y > Game.player.y && this.y < (Game.player.y + Player.SPRITE_SCALE * 2  - Player.SPRITE_SCALE / 1.5)) {
				Game.player.decreaseLifes();;
				return false;
			}
		}
		return true;
	}
	
	public boolean KillCheck(float x, float y) {
		if(ParentNumber == -1) {
					if(this.x > x && this.x < (x + Player.SPRITE_SCALE * 2) && 
							this.y > y && this.y < (y + Player.SPRITE_SCALE * 2 - Player.SPRITE_SCALE / 1.5)) {
						Game.bullets[this.Number] = null;
						return false;
					}
					else return true;
		}
		else return true;
	}
}
