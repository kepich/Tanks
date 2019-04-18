package Game;

import Graphics.TextureAtlas;
import IO.input;

public abstract class Tank extends Entity {
	
	public static final int SPRITE_SCALE 		= 16;
	public static final int SPRITES_PER_HEADING = 1;
	public static final int AMOUNT_OF_BULLETS	= 3;	// Одновременно
	public static final int SHOOT_DELAY			= 50;	// Перезарядка
	protected Heading 		heading;
	protected float 		speed;
	protected int			lastShoot;
	protected TextureAtlas 	atlas;
	
	protected Tank(EntityType type, float x, float y) {
		super(type, x, y);
	}
	public abstract void Update(input Input);
	
	protected boolean Moving(int newX, int newY, Heading head) {					// Можно ли проехать в этом направлении
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
}
