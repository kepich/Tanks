package GameLAN;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import Game.Entity;
import Game.EntityType;
import Game.Heading;
import Graphics.Sprite;
import Graphics.SpriteSheet;
import Graphics.TextureAtlas;

public class LANEnemy extends Entity implements Serializable {
	private static final long serialVersionUID = 1L;
	public static float 			x;		// Статические для ускорения обработки
	public static float 			y;
	public static Heading 			heading;
	private Map<Heading, Sprite> 	spriteMap;
	public static int 				lifes;

	protected LANEnemy(float x, float y, Heading heading, TextureAtlas atlas) {
		super(EntityType.LANEnemy, x, y);
		spriteMap = new HashMap<Heading, Sprite>();
		LANEnemy.x = x;
		LANEnemy.y = y;
		LANEnemy.heading = heading;
		lifes = 3;
		
		for(Heading h: Heading.values()) {
			SpriteSheet sheet = new SpriteSheet(h.texture(atlas), 1, 16);
			Sprite sprite = new Sprite(sheet, 2);
			spriteMap.put(h, sprite);
		}
	}


	@Override
	public boolean Update() {
		return false;
	}

	@Override
	public void Render(Graphics2D g) {
		spriteMap.get(heading).render(g, x, y);
	}

}
