package util;

import java.io.Serializable;

import Game.Bullet.HeadingBullet;

public class BulletsInfoPack implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public float x;
	public float y;
	public HeadingBullet head;
	public int owner;
	
	public BulletsInfoPack(float x, float y, HeadingBullet head, int owner) {
		this.x = x;
		this.y = y;
		this.head = head;
		this.owner = owner;
	}
	public BulletsInfoPack() {

	}

}
