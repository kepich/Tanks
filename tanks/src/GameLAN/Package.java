package GameLAN;

import java.io.Serializable;
import util.BulletsInfoPack;
import util.PlayerInfoPack;

public class Package implements Serializable {
	private static final long serialVersionUID = 1L;
	BulletsInfoPack[] 		bullets;
	PlayerInfoPack			enemy;
	int[][]  				level;
	
	public Package(BulletsInfoPack[] bull, Integer[][] lvl, PlayerInfoPack enemy) {
		bullets 		= bull;
		
		if(lvl != null) {
			level = new int[38][40];
			for(int i = 0; i < 38; i++) {
				for(int j = 0; j < 40; j++) {
					level[i][j] = lvl[i][j];
				}
			}
		}
		else level = null;
		this.enemy 		= enemy;
	}
	
	public Package(BulletsInfoPack[] bull, PlayerInfoPack enemy) {
		bullets 		= bull;
	
		level = new int[38][40];
		for(int i = 0; i < 38; i++) {
			for(int j = 0; j < 40; j++) {
				level[i][j] = Game.Game.level[i][j];
			}
		}
		
		this.enemy 		= enemy;
	}
}
