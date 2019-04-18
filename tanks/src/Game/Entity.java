package Game;

import java.awt.Graphics2D;

public abstract class Entity {
	
	public final EntityType type;
	
	protected float 				x;					// ����������
	protected float 				y;
	protected static final int		CHANGE_DELAY = 7;	// �������� ��� ����
	
	protected Entity(EntityType type, float x, float y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public abstract boolean Update();					// ���������� ���������
	
	public abstract void Render(Graphics2D g);			// ���������
}
