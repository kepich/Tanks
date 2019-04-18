package MainMenu;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Game.Entity;
import Game.EntityType;
import Game.Game;
import IO.input;

public class SubMenu extends Entity {
	private static final String 	path1 = "Res/PlayMenu1.png";
	private static final String 	path2 = "Res/PlayMenu2.png";
	private static final String 	path3 = "Res/PlayMenu3.png";
	private static Image[] 			backgroundImage = new Image[3];

	private int 					tempPos;
	private int						lastChange;
	
	public SubMenu(){
		super(EntityType.SubMenu, 0, 0);
		try {
			backgroundImage[0] = ImageIO.read(new File(path1));
			backgroundImage[1] = ImageIO.read(new File(path2));
			backgroundImage[2] = ImageIO.read(new File(path3));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tempPos = 0;
		lastChange = CHANGE_DELAY / 5;
	}
	
	public void Update(input Input) {
		if(lastChange == 0) {
			if(Input.getKey(KeyEvent.VK_DOWN)) {
				tempPos = (tempPos + 1) % 3;
			}
			else if(Input.getKey(KeyEvent.VK_UP)) {
				tempPos = Math.abs(tempPos - 1) % 3;
			}
			else if(Input.getKey(KeyEvent.VK_ENTER)) {
				switch(tempPos) {
				case 0:	Game.GameStatusSet(1); break;
				case 1: Game.GameStatusSet(4); break;
				case 2: Game.GameStatusSet(2); break;
				}
			}
			lastChange = 1;
		}
	}
	
	public void Render(Graphics2D g) {
		if(lastChange != 0)
			lastChange = (lastChange + 1) % CHANGE_DELAY;
		g.drawImage(backgroundImage[tempPos], 0, 0, null);
	}

	@Override
	public boolean Update() {
		// TODO Auto-generated method stub
		return false;
	}
}

