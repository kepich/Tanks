package MainMenu;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Game.Game;
import IO.input;


public class EndOfGameMenu {
	
	private Image 								winBackground;
	private Image 								loseBackground;
	private boolean								isWin;
	
	private static final String 				path1 = "Res/YouLose.png";
	private static final String 				path2 = "Res/YouWin.png";
	
	public EndOfGameMenu(boolean gameStatus) {
		this.isWin = gameStatus;
		try {
			loseBackground	= ImageIO.read(new File(path1));
			winBackground  	= ImageIO.read(new File(path2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Update(input Input) {
		if(Input.getKey(KeyEvent.VK_ENTER)) {
			Game.GameStatusSet(2);
		}
	}
	
	public void Render(Graphics2D g) {
		if(isWin == true)
			g.drawImage(winBackground, 0, 0, null);
		else
			g.drawImage(loseBackground, 0, 0, null);
	}
}
