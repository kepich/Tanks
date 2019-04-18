package MainMenu;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Display.Display;
import GameLAN.LANGameConnect;
import GameLAN.LANGameHost;
import Graphics.Sprite;
import Graphics.SpriteSheet;
import Graphics.TextureAtlas;
import IO.input;


public class WaitingConnection implements Runnable {
	
	private Image 								Background;
	private boolean								isCanceled = false;
	private Graphics2D							grap;
	private input 								Inp;
	private static Sprite[]						numbers = new Sprite[10];
	private boolean								isHost;
	
	private static final String 				path1 = "Res/waitingConnection.png";
	
	public WaitingConnection(Graphics2D g, input Input, TextureAtlas atlas, boolean isHost) {
		grap = g;
		Inp = Input;
		this.isHost = isHost;
		try {
			Background	= ImageIO.read(new File(path1));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i < 2; i++)
			for(int j = 0; j < 5; j++) {
				SpriteSheet sh = new SpriteSheet(atlas.Cut(20 * 16 + 8 + j * 8, 16 * 11 + i * 8 + 8, 8, 8), 1, 8);
				numbers[i*5 + j] = new Sprite(sh, 4);
			}
	}
	
	public void Update(input Input) {
		if(Input.getKey(KeyEvent.VK_ENTER)) {
			isCanceled = true;
		}
	}
	
	public void Render(Graphics2D g) {
		Display.clear(); 
		g.drawImage(Background, 0, 0, null);
		int pos = 0;
		int timer = (!isHost) ? LANGameConnect.counter : LANGameHost.counter;
		while(timer != 0) {
			int digit = timer % 10;
			numbers[digit].render(g, 380 - 32 * pos++, 32 * 9);
			timer /= 10;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!isCanceled) {
			Render(grap);
			Display.swapBuffers();
			Update(Inp);
			if(isCanceled || Thread.interrupted())
				return;
		}
	}
}
