package MainMenu;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Game.Entity;
import Game.EntityType;
import Game.Game;
import IO.input;

public class Connect extends Entity {
	private static final String 	path1 = "Res/ipMenu.png";
	private static final String 	path2 = "Res/ipSprites.png";
	private static final String 	path3 = "Res/vafer.png";

	private int 					tempPos;
	private int						lastChange;
	
	private BufferedImage spritesIm;
	private Image menuLines;
	private Image vafer;
	private Image pointer;
	private Image sprites[] = new Image[10];
	
	private int []ip = {0, 0, 0, 0};
	
	private byte ipPos;
	
	public Connect(){
		super(EntityType.Connect, 0, 0);
		ipPos = 0;
		try {
			menuLines = ImageIO.read(new File(path1));
			spritesIm = ImageIO.read(new File(path2));
			vafer = ImageIO.read(new File(path3));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tempPos = 0;
		lastChange = CHANGE_DELAY / 5;
		
		for(int i = 0; i < 10; i++)
			sprites[i] = spritesIm.getSubimage(19 * i + 2, 0 , 19, 35);
		pointer = spritesIm.getSubimage(0, 70 , 19, 5);
	}
	
	public void Update(input Input) {
		if(lastChange == 0) {
			if(Input.getKey(KeyEvent.VK_DOWN)) {
				tempPos = (tempPos + 1) % 2;
			}
			else if(Input.getKey(KeyEvent.VK_UP)) {
				tempPos = Math.abs(tempPos - 1) % 2;
			}
			else if(Input.getKey(KeyEvent.VK_ENTER)) {
				switch(tempPos) {
				case 0: Game.GameStatusSet(7); break;
				case 1: Game.GameStatusSet(4); break;
				}
			}
			else if(Input.getKey(KeyEvent.VK_LEFT)) {
				ipPos = (byte) Math.abs(ipPos - 1);
			}
			else if(Input.getKey(KeyEvent.VK_RIGHT)) {
				ipPos = (byte) ((ipPos + 1) % 4);
			}
			else if(Input.getKey(KeyEvent.VK_BACK_SPACE)) {
				ipDelete();
			}
			else if(Input.getKey(KeyEvent.VK_0)) {
				ipMake(0);
			}
			else if(Input.getKey(KeyEvent.VK_1)) {
				ipMake(1);		
			}
			else if(Input.getKey(KeyEvent.VK_2)) {
				ipMake(2);
			}
			else if(Input.getKey(KeyEvent.VK_3)) {
				ipMake(3);
			}
			else if(Input.getKey(KeyEvent.VK_4)) {
				ipMake(4);
			}
			else if(Input.getKey(KeyEvent.VK_5)) {
				ipMake(5);
			}
			else if(Input.getKey(KeyEvent.VK_6)) {
				ipMake(6);
			}
			else if(Input.getKey(KeyEvent.VK_7)) {
				ipMake(7);
			}
			else if(Input.getKey(KeyEvent.VK_8)) {
				ipMake(8);
			}
			else if(Input.getKey(KeyEvent.VK_9)) {
				ipMake(9);		
			}
			lastChange = 1;
		}
	}
	
	public void Render(Graphics2D g) {
		if(lastChange != 0)
			lastChange = (lastChange + 1) % CHANGE_DELAY;
		g.drawImage(vafer, 270, 260 + 80 * tempPos, null);
		g.drawImage(menuLines, 0, 0, null);
		//g.drawImage(pointer, 297 + 19, 197, null);
		for(int i = 0; i < 3; i++)
			g.drawImage(pointer, 297 + 19 * i + ipPos * (6 + 19 * 3), 232, null);
		
		for(int i = 0; i < 4; i++) {
			int buf = ip[i];
			for(int j = 0; j < 3; j++) {
				g.drawImage(sprites[Math.abs(buf % 10)], 278 + 19 * (3 - j) + i * (6 + 19 * 3) + 2, 197, null);
				buf /= 10;
			}
		}
	}

	@Override
	public boolean Update() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void ipMake(int tempDigit) {
		if(ip[ipPos] * 10 + tempDigit < 256)
			ip[ipPos] = (ip[ipPos] * 10 + tempDigit);
	}
	
	private void ipDelete() {
		ip[ipPos] /= 10;
	}
	
	public String getIp() {
		return ip[0] + "." + ip[1] + "." + ip[2] + "." + ip[3];
	}
}

