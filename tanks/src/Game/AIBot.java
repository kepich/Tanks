package Game;

import java.util.LinkedList;
import java.util.Queue;

import util.pair;

public class AIBot {
	private final 	int			COL = 40;			// Размеры экрана
	private final	int			LIN = 38;
	private		int [][]		map;				// Карта
	
	private float		moveCounter;				//Счетчик ходов
	private int			lastMove;					//Для корректности
	private int			tempMove;
	private boolean		isOverstep;
	
	public AIBot(float x, float y) {
		map = new int[LIN][COL];
		lastMove = 0;
		tempMove = 0;
		isOverstep = false;
		moveCounter = 0;
	}
	
	public int getMove(float x, float y, float speed) {	// Возвращает направление
		
		int xPos, yPos;
		xPos = (int) ((x + 1) / 16);
		yPos = (int) ((y + 1) / 16);
			
		for(int i = 0; i < LIN; i++)
			for(int j = 0; j < COL; j++)
				map[i][j] = 0;
		
		if(moveCounter == 0) {
			bfsStep(new pair(xPos, yPos), new pair((int)(Game.player.x / 16), (int)(Game.player.y / 16)));
			
			if(isOverstep) {
				isOverstep = false;
				lastMove = tempMove;
			}
			
			tempMove = map[yPos][xPos];
			
			if(tempMove == -1)
				return -1;
			
			if(((tempMove == -5) || (tempMove == -3)) && lastMove == -2) {
				isOverstep = true;
			}
			else if(((tempMove == -2) || (tempMove == -4)) && lastMove == -5) {
				isOverstep = true;
			}
			else {
				lastMove = tempMove;
			}
			
			moveCounter = (8f / speed) - 1;

			if(lastMove == -2)
				return 0;
			else if(lastMove == -3)
				return 1;
			else if(lastMove == -4)
				return 2;
			else return 3;
		} else {
			moveCounter--;
			if(lastMove == -2)
				return 0;
			else if(lastMove == -3)
				return 1;
			else if(lastMove == -4)
				return 2;
			else return 3;
		}
	}
	
	private boolean bfsStep(pair tempPos, pair enemyPos) {		// поиск крачайешего пути через BFS
		Queue<pair> q = new LinkedList<pair>();
		q.add(tempPos);
		map[tempPos.y][tempPos.x] = 1;
		pair buf;
		
		while(!q.isEmpty()) {
			buf = q.poll();
			if((buf.x != enemyPos.x) || (buf.y != enemyPos.y)) {
				if(map[buf.y - 1][buf.x] == 0)
					if(((Game.level[buf.y - 1][buf.x] == 0) || (Game.level[buf.y - 1][buf.x] == 4)) &&
						((Game.level[buf.y - 1][buf.x + 1] == 0) || (Game.level[buf.y - 1][buf.x + 1] == 4))){
						// Можно ехать вверх
						map[buf.y - 1][buf.x] = map[buf.y][buf.x] + 1;
						q.add(new pair(buf.x, buf.y - 1));
					}
				if(map[buf.y][buf.x + 1] == 0)
					if(((Game.level[buf.y][buf.x + 2] == 0) || (Game.level[buf.y][buf.x + 2] == 4)) &&
						((Game.level[buf.y + 1][buf.x + 2] == 0) || (Game.level[buf.y + 1][buf.x + 2] == 4))){
						// Можно ехать вправо
						map[buf.y][buf.x + 1] = map[buf.y][buf.x] + 1;
						q.add(new pair(buf.x + 1, buf.y));
					}
				if(map[buf.y + 1][buf.x] == 0)
					if(((Game.level[buf.y + 2][buf.x] == 0) || (Game.level[buf.y + 2][buf.x] == 4)) &&
						((Game.level[buf.y + 2][buf.x + 1] == 0) || (Game.level[buf.y + 2][buf.x + 1] == 4))){
						// Можно ехать вниз
						map[buf.y + 1][buf.x] = map[buf.y][buf.x] + 1;
						q.add(new pair(buf.x, buf.y + 1));
					}
				if(map[buf.y][buf.x - 1] == 0)
					if(((Game.level[buf.y][buf.x - 1] == 0) || (Game.level[buf.y][buf.x - 1] == 4)) &&
						((Game.level[buf.y + 1][buf.x - 1] == 0) || (Game.level[buf.y + 1][buf.x - 1] == 4))){
						// Можно ехать влево
						map[buf.y][buf.x - 1] = map[buf.y][buf.x] + 1;
						q.add(new pair(buf.x - 1, buf.y));
					}
				}
			else {
				int i = map[buf.y][buf.x];
				map[buf.y][buf.x] = -1;
				while(i-- != 1) {
					if(map[buf.y - 1][buf.x] == i) {
						map[--buf.y][buf.x] = -4;
					}else if(map[buf.y][buf.x + 1] == i) {
						map[buf.y][++buf.x] = -5;
					}else if(map[buf.y + 1][buf.x] == i) {
						map[++buf.y][buf.x] = -2;
					}else {
						map[buf.y][--buf.x] = -3;
					}
				}
				return true;
			}
			
			if(q.isEmpty())
				return false;
		}
		
		return false;
	}
}
