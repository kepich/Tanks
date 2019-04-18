package MainMenu;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Game.Game;
import Game.Entity;
import Game.EntityType;
import IO.input;

public class MenuLAN extends Entity {
	private static final String 	path1 = "Res/lanMenu.png";
	private static final String 	path2 = "Res/menuLines.png";
	private static final String 	path3 = "Res/vafer.png";

	private int 					tempPos;
	private int						lastChange;
	
	private Image background;
	private Image menuLines;
	private Image vafer;
	
	public MenuLAN(){
		super(EntityType.MenuLAN, 0, 0);
		
		
		try {
			background = ImageIO.read(new File(path1));
			menuLines = ImageIO.read(new File(path2));
			vafer = ImageIO.read(new File(path3));
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
				case 0: Game.GameStatusSet(6); break;
				case 1: Game.GameStatusSet(5); break;
				case 2: Game.GameStatusSet(3); break;
				}
			}
			lastChange = 1;
		}
	}
	
	public void Render(Graphics2D g) {
		if(lastChange != 0)
			lastChange = (lastChange + 1) % CHANGE_DELAY;
		g.drawImage(background, 0, 8, null);
		g.drawImage(vafer, 345, 140 + 75 * tempPos, null);
		g.drawImage(menuLines, 350, 130, null);
	}

	@Override
	public boolean Update() {
		// TODO Auto-generated method stub
		return false;
	}
}

