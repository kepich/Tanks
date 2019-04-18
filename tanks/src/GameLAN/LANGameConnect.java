package GameLAN;

import java.awt.Graphics2D;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import Game.Bullet;
import Game.Entity;
import Game.EntityType;
import Game.Heading;
import Graphics.TextureAtlas;
import HUD.Hud;
import IO.input;
import MainMenu.WaitingConnection;
import util.BulletsInfoPack;
import util.ConnectionLostExeption;
import util.PlayerInfoPack;

public class LANGameConnect extends Entity{
	private LANEnemy 					enemyLan;
	private Hud		 					hud;
	private Socket 						socket;
	private ObjectInputStream 			cin;
	private ObjectOutputStream 			cout;
	private TextureAtlas 				atlas;
	private String						ip;
	private Thread 						blocking;
	private final static int			AMOUNT_OF_TRYING_TO_CONNECT 	= 5;
	private final static int			WAITING_CONNECTION_DELAY 		= 1;
	private ConnectionLostExeption		throwing 						= new ConnectionLostExeption();
	public static boolean				isDenied;
	FileWriter 							logFile;
	PrintWriter 						log;
	public static int 					counter							= 0;
	
	public LANGameConnect(TextureAtlas atlas, String ip, Graphics2D g, input Input) {
		super(EntityType.LANPlayer, 0, 0);
		System.setProperty("sun.net.useExclusiveBind", "false"); 
		this.ip = ip;
		this.atlas = atlas;
		try
			{
				logFile = new FileWriter("logsCON.log", true);
				log = new PrintWriter((java.io.Writer)logFile);
				WaitingConnection waiting = new WaitingConnection(g, Input, atlas, false);
				blocking = new Thread(waiting);
				blocking.start();
				counter = 0;
				
				while(counter < AMOUNT_OF_TRYING_TO_CONNECT) {
					if(blocking.isAlive()) {
						try {
							socket = new Socket(this.ip, 3345);
							break;
						}catch(IOException e) {
							System.out.println(counter + ": Failed connect");
							try {
								Thread.sleep(WAITING_CONNECTION_DELAY);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							counter++;
						}
					}
					else
						throw throwing;
				}
				
				if(counter == AMOUNT_OF_TRYING_TO_CONNECT)
					throw throwing;
				
				blocking.interrupt();
				blocking = null;
				
				cout = new ObjectOutputStream(socket.getOutputStream()); 
				Game.Game.player.setX(32 + 32 * 15);
				Game.Game.player.setX(32);
				Game.Game.player.setHeading(Heading.SOUTH);
				
				cin = new ObjectInputStream(socket.getInputStream());						// Поток чтения из сокета
				enemyLan = new LANEnemy(32 * 7, 32 * 15, Heading.NORTH, atlas);
				
				System.out.println("Client connected to the server");
				hud = new Hud(atlas);
			}
			catch(UnknownHostException e) {
				e.printStackTrace();
				
				if(blocking != null) {
					blocking.interrupt();
					blocking = null;
				}
				
				Game.Game.GameStatusSet(5);
			}
			catch(IOException e) {
				System.out.println("Cannot connect to host 3344");
				
				blocking.interrupt();
				blocking = null;
				
				Game.Game.GameStatusSet(5);
			}
			catch(ConnectionLostExeption e) {
				blocking.interrupt();
				blocking = null;
				
				Game.Game.GameStatusSet(5);
				return;
			}
	}

	public boolean UpdateRes(input Input) {
		// Recieving ******************************************************************************************
		Package recieved = null;
		try {
			recieved = (Package)cin.readObject();
		}
		catch(IOException e) {
			Game.Game.GameStatusSet(0);
			
			try {
				cin.close();
				cout.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("CLIENT 1");
			return true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		for(int i = 0; i < 38; i++) {
			for(int j = 0; j < 40; j++) {
				Game.Game.level[i][j] = recieved.level[i][j];
			}

		}
		
		BulletsInfoPack []bp	= recieved.bullets;
		
		LANEnemy.x 			= recieved.enemy.x;
		LANEnemy.y 			= recieved.enemy.y;
		LANEnemy.heading 	= recieved.enemy.head;
		Game.Game.player.setLifes(recieved.enemy.hp);
		
		for(int i = 0; i < Game.Game.AM_OF_BULLETS; i++) {
			if(bp[i] != null)
				if(Game.Game.bullets[i] != null) {
					Game.Game.bullets[i].setX(bp[i].x);
					Game.Game.bullets[i].setY(bp[i].y);
					Game.Game.bullets[i].heading 	= bp[i].head;
				}
				else {
					Game.Game.bullets[i] = new Bullet(bp[i].x, bp[i].y, 2, bp[i].head, atlas, -1, i);
				}
			else {
				Game.Game.bullets[i] = null;
			}
		}

		// Sending *****************************************************************************************************************
		if(Game.Game.player.getLifes() > 0) {
			Game.Game.player.Update(Input);
		}
		else {
			Game.Game.GameStatusSet(0);
			try {
				cin.close();
				cout.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("CLIENT 2");
			return false;
		}
		
		PlayerInfoPack plr = new PlayerInfoPack(Game.Game.player.getX(), Game.Game.player.getY(), Game.Game.player.getHeading(), 3);
		for(int i = 0; i < Game.Game.AM_OF_BULLETS; i++) {
			if(Game.Game.bullets[i] != null)
				if(bp[i] == null)
					bp[i] = new BulletsInfoPack(Game.Game.bullets[i].getX(), Game.Game.bullets[i].getY(), Game.Game.bullets[i].heading, -2);
		}
		Package result = new Package(bp, null, plr);
		try {
			cout.writeObject(result);
			cout.flush();
		} catch (IOException e) {
			Game.Game.GameStatusSet(0);
			try {
				cin.close();
				cout.close();
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("CLIENT 3");
			return true;
		}
		
		return true;
	}

	@Override
	public boolean Update() {
		return false;
	}

	@Override
	public void Render(Graphics2D graphics) {
		Game.Game.lvl.Render(graphics);
		if(Game.Game.player.getLifes() > 0)
			Game.Game.player.Render(graphics);

		enemyLan.Render(graphics);
		
		Game.Game.lvl.RenderGrass(graphics);
		for(int i = 0 ; i < Game.Game.AM_OF_BULLETS; i++)
			if(Game.Game.bullets[i] != null)
				Game.Game.bullets[i].Render(graphics);
		hud.Render(graphics);
	}

}
