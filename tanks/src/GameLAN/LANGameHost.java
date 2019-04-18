package GameLAN;

import java.awt.Graphics2D;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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


public class LANGameHost extends Entity{
	private LANEnemy 			enemyLan;
	private Hud		 			hud;
	private static ServerSocket server = null;
	private Socket 				socket;
	private ObjectInputStream 	cin;
	private ObjectOutputStream 	cout;
	private TextureAtlas 		atlas;
	
	private Thread 						blocking;
	private final static int			AMOUNT_OF_TRYING_TO_CONNECT 	= 60;
	private final static int			WAITING_CONNECTION_DELAY 		= 1000;
	private ConnectionLostExeption		throwing 						= new ConnectionLostExeption();
	public static int 					counter							= 0;
	
	FileWriter logFile;
	PrintWriter log;
	
	public LANGameHost(TextureAtlas atlas, Graphics2D g, input Input) {
		super(EntityType.LANPlayer, 0, 0);
		this.atlas = atlas;
		System.setProperty("sun.net.useExclusiveBind", "false"); 
		try
			{
				logFile = new FileWriter("logsHOST.log", true);
				log = new PrintWriter((java.io.Writer)logFile);
				
				WaitingConnection waiting = new WaitingConnection(g, Input, atlas, true);
				blocking = new Thread(waiting);
				blocking.start();
				counter = 0;
				
				if(server == null) {
					server = new ServerSocket(3345);
					server.setSoTimeout(WAITING_CONNECTION_DELAY);
				}
				Game.Game.isWaiting = true;
				
				while(counter < AMOUNT_OF_TRYING_TO_CONNECT) {
					if(blocking.isAlive()) {
						try {
							socket = server.accept();
							break;
						} catch(SocketTimeoutException e) {
							System.out.println(counter + ": Failed connection");
							counter++;
						}
					}
					else{
						throw throwing;
					}
				}
				
				if(counter == AMOUNT_OF_TRYING_TO_CONNECT)
					throw throwing;
				
				blocking.interrupt();
				blocking = null;
				
				cout = new ObjectOutputStream(socket.getOutputStream());
				
				Game.Game.player.setX(32 * 7);
				Game.Game.player.setY(32 * 15);
				
				cin = new ObjectInputStream(socket.getInputStream());						// Поток чтения из сокета
				enemyLan = new LANEnemy(32 + 32 * 15, 32, Heading.SOUTH, atlas);
				System.out.println("Client connected to the server");
				hud = new Hud(atlas);
			}
			catch(UnknownHostException e) {
				e.printStackTrace();
				Game.Game.GameStatusSet(4);
			}
			catch(IOException e) {
				System.out.println("Cannot connect to host 3344");
				e.printStackTrace();
				Game.Game.GameStatusSet(4);
			}
			catch (ConnectionLostExeption e) {
				blocking.interrupt();
				blocking = null;
				
				Game.Game.GameStatusSet(4);
				return;
			}
	}
	
	public boolean UpdateRes(input Input) {
		// Sending pack  *************************************************************************************************************
		PlayerInfoPack plr = new PlayerInfoPack(Game.Game.player.getX(), Game.Game.player.getY(), Game.Game.player.getHeading(), LANEnemy.lifes);
		BulletsInfoPack[] buls = new BulletsInfoPack[Game.Game.AM_OF_BULLETS];
		for(int i = 0; i < Game.Game.AM_OF_BULLETS; i++) {
			if(Game.Game.bullets[i] != null)
				buls[i] = new BulletsInfoPack(Game.Game.bullets[i].getX(), Game.Game.bullets[i].getY(), Game.Game.bullets[i].heading, 1);
			else
				buls[i] = null;
		}
		Package result = new Package(buls, plr);
		
		try {
			cout.writeObject(result);
			cout.flush();
		} catch (IOException e) {
			Game.Game.GameStatusSet(0);
			
			try {
				cin.close();
				cout.close();
				socket.close();
				server.close();
				server = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("HOST 1");
			return true;
		}

		
		// Recieving pack ************************************************************************************************************
		Package recieved = null;
		try {
			recieved = (Package)cin.readObject();
		}
		catch(IOException e) {
			Game.Game.GameStatusSet(0);
			return true;
		} catch (ClassNotFoundException e) {
			Game.Game.GameStatusSet(0);
			
			try {
				cin.close();
				cout.close();
				socket.close();
				server.close();
				server = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("HOST 2");
			return true;
		}
		
		BulletsInfoPack []bp	= recieved.bullets;
		
		LANEnemy.x = recieved.enemy.x;
		LANEnemy.y = recieved.enemy.y;
		LANEnemy.heading = recieved.enemy.head;
		
		// Updating  *****************************************************************************************************************
		if(Game.Game.player.getLifes() > 0) {
			Game.Game.player.Update(Input);
		}
		else {
			Game.Game.GameStatusSet(0);
			
			try {
				cin.close();
				cout.close();
				socket.close();
				server.close();
				server = null;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("HOST 3");
			return false;
		}
		
		for(int i = 0; i < Game.Game.AM_OF_BULLETS; i++) {
			if(bp[i] != null)
				if(Game.Game.bullets[i] == null)
					Game.Game.bullets[i] = new Bullet(bp[i].x, bp[i].y, 2, bp[i].head, atlas, bp[i].owner, i);
		}
		
		for(int i = 0 ; i < Game.Game.AM_OF_BULLETS; i++) {
			if(Game.Game.bullets[i] != null) {
				if(Game.Game.bullets[i].KillCheck(LANEnemy.x, LANEnemy.y))
					Game.Game.bullets[i].Update();
				else {
					LANEnemy.lifes--;
				}
			}
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
