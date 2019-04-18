package level;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Game.Game;
import Game.TileType;
import Graphics.TextureAtlas;

public class Level {

	public static final int 					TILE_SCALE 			= 8;
	public static final int 					TILE_IN_GAME_SCALE 	= 2;
	public static final int 					SCALED_TILE_SIZE	= TILE_SCALE * TILE_IN_GAME_SCALE;
	public static final int 					TILES_IN_WIDTH 		= Game.WIDTH / (TILE_SCALE * TILE_IN_GAME_SCALE);
	public static final int 					TILES_IN_HEIGHT 	= Game.HEIGHT / (TILE_SCALE * TILE_IN_GAME_SCALE);
	
	private Integer[][] 						tileMap;
	private Map<TileType, Tile>					tiles;
	private List<Point> grassCords;
	
	public Level(TextureAtlas atlas, Integer [][] tileMap) {
		tiles = new HashMap<TileType, Tile>();
		tiles.put(TileType.BRICK, new Tile(atlas.Cut(32 * TILE_SCALE, 0 * TILE_SCALE, TILE_SCALE, TILE_SCALE), 
				TILE_IN_GAME_SCALE, TileType.BRICK));
		tiles.put(TileType.METAL, new Tile(atlas.Cut(32 * TILE_SCALE, 2 * TILE_SCALE, TILE_SCALE, TILE_SCALE), 
				TILE_IN_GAME_SCALE, TileType.METAL));
		tiles.put(TileType.WATER, new Tile(atlas.Cut(32 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE), 
				TILE_IN_GAME_SCALE, TileType.WATER));
		tiles.put(TileType.GRASS, new Tile(atlas.Cut(34 * TILE_SCALE, 4 * TILE_SCALE, TILE_SCALE, TILE_SCALE), 
				TILE_IN_GAME_SCALE, TileType.GRASS));
		tiles.put(TileType.EMPTY, new Tile(atlas.Cut(36 * TILE_SCALE, 6 * TILE_SCALE, TILE_SCALE, TILE_SCALE), 
				TILE_IN_GAME_SCALE, TileType.EMPTY));
		tiles.put(TileType.BASE, new Tile(atlas.Cut(36 * TILE_SCALE, 6 * TILE_SCALE, TILE_SCALE, TILE_SCALE), 
				TILE_IN_GAME_SCALE, TileType.BASE));
		
		this.tileMap = tileMap;
		grassCords = new ArrayList<Point>();
		
		for(int i = 0; i < tileMap.length; i++) {
			for(int j = 0; j < tileMap[i].length; j++) {
				Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));
				if(tile.type() == TileType.GRASS) 
					grassCords.add(new Point(j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE));
			}
		}
	}
	
	public void Render(Graphics2D g) {
		for(int i = 0; i < tileMap.length; i++) {
			for(int j = 0; j < tileMap[i].length; j++){
				Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));
				if(tile.type() != TileType.GRASS) 
					tile.Render(g, j * SCALED_TILE_SIZE, i * SCALED_TILE_SIZE);
			}
		}
	}
	
	public void RenderGrass(Graphics2D g) {
		for(Point p: grassCords) {
			tiles.get(TileType.GRASS).Render(g, p.x, p.y);
		}
		
	}
	
}
