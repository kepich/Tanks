package Game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

import Bonus.msUpgrade;
import Display.Display;
import GameLAN.LANGameConnect;
import GameLAN.LANGameHost;
import Graphics.TextureAtlas;
import HUD.Hud;
import IO.input;
import MainMenu.Connect;
import MainMenu.EndOfGameMenu;
import MainMenu.MenuLAN;
import MainMenu.MenuStart;
import MainMenu.SubMenu;
import level.Level;
import util.MediaPlayer;
import util.Time;
import util.Utils;

public class Game implements Runnable {
		
	public static final int 		WIDTH 				= 800;
	public static final int			HEIGHT 				= 608;
	public static final String	 	TITLE 				= "Battle Tanks";
	public static final int 		_CLEAR_COLOR 		= 0xff000000;
	public static final int 		NUM_BUFFERS 		= 3;
	public static final int			AM_OF_BULLETS		= 40;
	public static final float		UPDATE_RATE			= 60.0f;
	public static final float		UPDATE_INTERVAL 	= Time.SECOND / UPDATE_RATE;
	public static final long		IDLE_TIME			= 1;
	public static final String 		ATLAS_FILE_NAME 	= "sprite.png";
	public static final int			SPAWN_BOTS_DELAY	= 400;
	public static final int 		AMOUNT_OF_ENE	 	= 10;
	public static final float 		TANK_SPEED 			= 1f;
	public static final int 		AMOUNT_OF_WAVES 	= 5;
	public static boolean 			isWaiting 			= false;
	public static  Level			lvl;
	
	private boolean 				Running;
	private Thread		 			GameThread;
	private Thread					MusicThread;
	private MediaPlayer				music;
	private input					Input;
	private static Graphics2D		graphics;
	private TextureAtlas			atlas;
	private EndOfGameMenu 			endMenu;
	private MenuStart 				startMenu;
	private SubMenu 				startMenuSub;
	private Hud 					hud;
	private MenuLAN 				lanMenu;
	private Connect 				connect;
	private LANGameConnect 			gameLan;
	private LANGameHost 			gameLanHost;
	
	private int						timeToSpawnEnemys 	= 0;
	private static int				amountOfEnemysWave 	= AMOUNT_OF_WAVES;
	private static int				gameStatus;
	
	private static CommandCenter	comCenter;
	public static msUpgrade			moveSpeedUpgrade;
	public static Integer[][]		level;

	public 	static Player			player;
	public 	static EnemyBot[]		enemy 				= new EnemyBot[AMOUNT_OF_ENE];
	public	static Bullet[]			bullets 			= new Bullet[AM_OF_BULLETS];
	
	private static Random			randomizer			= new Random();
	
	public Game() {
		Running 					= false;
		gameStatus 					= 2;
		Display.create(WIDTH, HEIGHT, TITLE, _CLEAR_COLOR, NUM_BUFFERS);
		graphics 					= Display.getGraphics();
		Input 						= new input();
		Display.addInputListener(Input);
		atlas 						= new TextureAtlas(ATLAS_FILE_NAME);
		hud 						= new Hud(atlas);
		level 						= Utils.levelParser("Res/lvl1");
		player 						= new Player(16 * 18, 16 * 15, 2, TANK_SPEED, atlas);
		lvl 						= new Level(atlas, level);
		comCenter 					= new CommandCenter(16 * 19, 16 * 34, atlas);
		moveSpeedUpgrade		 	= null;

		music 						= new MediaPlayer("Res/sfSoundtrack.wav");
		startMenu 					= new MenuStart();
	}
	
	public synchronized void start() {
		if(Running)
			return;
		Running = true;
		GameThread = new Thread(this);
		GameThread.start();
		
		MusicThread = new Thread(music);
		MusicThread.start();
	}
	
	public synchronized void stop() {
		if(!Running)
			return;
		
		Running = false;
		cleanUp();
		try {
			MusicThread.interrupt();
			MusicThread.join();
			GameThread.join();
		}
		catch(InterruptedException i) {
			i.printStackTrace();
		}
		Display.destroy();
	}
	
	private void update() {
		/* GameStatus
		 * -1	- Close
		 * 1	- Game
		 * 0 	- End Menu
		 * 2 	- Main menu
		 * 3 	- Sub Menu
		 * 4	- Lan SubMenu
		 * 5	- Lan Connection
		 * 6	- LAN GameHost
		 * 7	- LAN GameClient
			*/
		if(gameStatus == 1) {
			boolean isAllDead = true;
			
			if(player.getLifes() > 0 && comCenter.Update()) {
				player.Update(Input);
			}
			else {
				gameStatus = 0;
				endMenu = new EndOfGameMenu(false);
			}
			
			timeToSpawnEnemys++;
			timeToSpawnEnemys %= SPAWN_BOTS_DELAY;
			if(timeToSpawnEnemys == 0)
				addNewEnemy();

			for(int i = 0 ; i < AMOUNT_OF_ENE;i++) {
				if(enemy[i] != null) {
					isAllDead = false;
					enemy[i].Update();
				}
			}
			
			for(int i = 0 ; i < AM_OF_BULLETS; i++)
				if(bullets[i] != null)
					bullets[i].Update();
			if(moveSpeedUpgrade != null)
				moveSpeedUpgrade.Update();
			else {
				if(Math.abs(randomizer.nextInt()) % 1000 == 55) {
					Point pos = posSearch();
					System.out.println(pos.x + " " + pos.y);
					moveSpeedUpgrade 		= new msUpgrade(16 * pos.y, 16 * pos.x, atlas);
				}
			}
			if((amountOfEnemysWave == 0) && isAllDead) {
				gameStatus = 0;
				endMenu = new EndOfGameMenu(true);
			}
		}
		else if(gameStatus == 0)
			endMenu.Update(Input);
		else if(gameStatus == 2)
			startMenu.Update(Input);
		else if(gameStatus == 3) {
			if(startMenuSub == null) {
				startMenuSub = new SubMenu();
			}
			else startMenuSub.Update(Input);
			
			if(gameStatus == 1)
				newGame();
			else if(gameStatus == 4)
				lanMenu = new MenuLAN();
		} 
		else if(gameStatus == 4) {
			if(lanMenu != null)
				lanMenu.Update(Input);
			
			if(gameStatus == 5)
				connect = new Connect();
			else if(gameStatus == 6) {
				newGame();
				gameLanHost = new LANGameHost(atlas, graphics, Input);
			}
		}
		else if(gameStatus == 5) {
			if(connect != null)
				connect.Update(Input);
			
			if(gameStatus == 4)
				lanMenu = new MenuLAN();
			else if(gameStatus == 7) {
				newGame();
				gameLan = new LANGameConnect(atlas, connect.getIp(), graphics, Input);
			}
		}
		else if(gameStatus == 6) {
			boolean k = gameLanHost.UpdateRes(Input);
			if(gameStatus == 0)
				endMenu = new EndOfGameMenu(k);
		}
		else if(gameStatus == 7) {
			boolean k = gameLan.UpdateRes(Input);
			if(gameStatus == 0)
				endMenu = new EndOfGameMenu(k);
		}
		else if(gameStatus == -1)
			stop();
		

	}
	
	private void render() {
		Display.clear(); 
		if(gameStatus == 1) {
			lvl.Render(graphics);
			if(player.getLifes() > 0)
				player.Render(graphics);
			for(int i = 0 ; i < AMOUNT_OF_ENE;i++) {
				if(enemy[i] != null)
					enemy[i].Render(graphics);
			}
			lvl.RenderGrass(graphics);
			for(int i = 0 ; i < AM_OF_BULLETS; i++)
				if(bullets[i] != null)
					bullets[i].Render(graphics);
			hud.Render(graphics);
			if(moveSpeedUpgrade != null)
				moveSpeedUpgrade.Render(graphics);
			comCenter.Render(graphics);
		}
		else if(gameStatus == 0)
			endMenu.Render(graphics);
		else if(gameStatus == 2) {
			startMenu.Render(graphics);
		}
		else if(gameStatus == 3) {
			if(startMenuSub != null)
				startMenuSub.Render(graphics);
		}
		else if(gameStatus == 4) {
			lanMenu.Render(graphics);
		}
		else if(gameStatus == 5)
			connect.Render(graphics);
		else if(gameStatus == 6) {
			gameLanHost.Render(graphics);
		}
		else if(gameStatus == 7) {
			gameLan.Render(graphics);
		}
		

		Display.swapBuffers();
	}
	
	public void run() {
		long 	lastTime 	= Time.get();
		float 	delta		= 0;
		boolean render		= false;
		
		long count = 0;
		
		int FPS = 0;
		int UPD = 0;
		int UPD_L = 0;
		
		while(Running) {
			long now = Time.get();
			long elapsedTime = now - lastTime;
			lastTime = now;
			
			count += elapsedTime;
			delta += ((float)elapsedTime / UPDATE_INTERVAL);
			
			render = false;
			while(delta > 1) {
				update();
				UPD++;
				delta--;
				if(render)
					UPD_L++;
				else
					render = true;
			}
			if(render) {
				render();
				if(isWaiting) {
					lastTime = Time.get();
					isWaiting = false;
				}
				FPS++;
			}
			else{
				try {
					Thread.sleep(IDLE_TIME);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(count >= Time.SECOND) {
				Display.setTitle(TITLE + " || FPS " + FPS + " | UPD " + UPD + " | UPDL " + UPD_L + ((gameStatus == 6) ? " HOST " : ((gameStatus == 7) ? " CLIENT " : "")));
				UPD = 0;
				UPD_L = 0;
				FPS = 0;
				count = 0;
			}
		}
	}
	
	private void newGame() {
		player 					= new Player(32 * 7, 32 * 15, 2, TANK_SPEED, atlas);
		level 					= Utils.levelParser("Res/lvl1");
		lvl 					= new Level(atlas, level);
		timeToSpawnEnemys 		= SPAWN_BOTS_DELAY - 1;
		amountOfEnemysWave 		= AMOUNT_OF_WAVES;
		comCenter 				= new CommandCenter(16 * 19, 16 * 34, atlas);
		
		Point pos = posSearch();
		System.out.println(pos.x + " " + pos.y);
		moveSpeedUpgrade 		= new msUpgrade(16 * pos.y, 16 * pos.x, atlas);
		enemy 					= new EnemyBot[AMOUNT_OF_ENE];
		bullets 				= new Bullet[AM_OF_BULLETS];
		hud 					= new Hud(atlas);
	}
	
	private void addNewEnemy() {
		Random a = new Random();
		for(int i = 0 ; (i < AMOUNT_OF_ENE) && (amountOfEnemysWave != 0); i ++) {
			if(enemy[i] == null) {
				amountOfEnemysWave--;
				enemy[i] = new EnemyBot(32 + 32 *(Math.abs(a.nextInt()) % 18), 32, 2, TANK_SPEED * 1f, atlas, i);
				break;
			}
		}
	}
	
	private Point posSearch() {	// Ќахождение позиции дл€ спавна бонуса
		int x = 0, y = 0;
		boolean isSearched = false;
		
		while(!isSearched) {
			x = Math.abs(randomizer.nextInt()) % 37;
			y = Math.abs(randomizer.nextInt()) % 35;
			
			if((level[y][x] == 0) && (level[y + 1][x] == 0) && (level[y][x + 1] == 0) && (level[y + 1][x + 1] == 0))
				isSearched = true;
		}
		
		return new Point(x, y);
		
	}
	
	private void cleanUp() {
		Display.destroy();
	}
	
	public static int getAmountOfEnemysWave() {
		return amountOfEnemysWave;
	}
	
	public static void GameStatusSet(int status) {
		gameStatus = status;
	}
}
