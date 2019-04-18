package Bonus;

import Game.Entity;
import Game.EntityType;

public abstract class Bonus extends Entity {
	
	protected int 			timeLife = 500;					// Время существования
	protected int 			duration = 500;					// Действия
	protected int			tempTime = duration;			// Текущее состояние
	protected int			tempLifeTime = timeLife;
	protected EntityType 	type;
	
	protected Bonus(EntityType type, float x, float y) {
		super(type, x, y);
		this.type = type;
	}
}
