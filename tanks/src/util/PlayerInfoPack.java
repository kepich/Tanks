package util;

import java.io.Serializable;

import Game.Heading;

public class PlayerInfoPack implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public float 		x;
	public float 		y;
	public Heading 		head;
	public int 			hp;
	
	public PlayerInfoPack(float x, float y, Heading head, int hp) {
		this.x = x;
		this.y = y;
		this.head = head;
		this.hp = hp;
	}
}
