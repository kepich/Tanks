package Bonus;

import Game.Entity;
import Game.EntityType;

public abstract class Bonus extends Entity {
	
	protected int 			timeLife = 500;					// ����� �������������
	protected int 			duration = 500;					// ��������
	protected int			tempTime = duration;			// ������� ���������
	protected int			tempLifeTime = timeLife;
	protected EntityType 	type;
	
	protected Bonus(EntityType type, float x, float y) {
		super(type, x, y);
		this.type = type;
	}
}
