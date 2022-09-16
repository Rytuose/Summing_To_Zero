package core;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Constants {
	
	public static BitmapFont tileFont = new BitmapFont();
	public static ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	public static final int menuButtonWidth = 750;
	public static final int menuButtonHeight = 120;
	public static final int topMenuButtonHeight = 700;
	public static final int menuButtonGap = 35;
	
	public static final int baseWidth = 1280;
	public static final int baseHeight = 960;
	public static final int boardDimension = 5;
	public static final int tileDimension = 175;
	public static float boardGap = (baseHeight - (tileDimension * boardDimension))/2;
	public static float transparencyRatio = .6f;
	public static int startingPoints = 0;
	public static int pointDeduction = 1; //Might need to make an increment if game can go infinite
	public static int topEndGameButtonHeight = 700;

	public static final float currentTileVerticalGap = boardGap;
	public static final int currentTileDimension = 175;
	public static final int nextTileDimension = 125;
	public static final int nextTileGap = 20;
	
	public static final int turnNumberWidth = 100;
	public static final float numberWidth = 120;
	public static final float numberRatio = 3f/2f;
	public static final int pointWidth = 50;
	
	public static final int tileTime = 5;
	
	public static final int startMaxValue = 3;
	public static final int incrementTime = 15;
	public static final int incrementAmount = 2;
	
	public static final int abilitySelectionButtonGap = 75;
	public static final int abilitySelectionButtonTopGap = 225;
	public static final int abilityButtonSelectionWidth = 450;
	public static final int abilityButtonSelectionHeight = 150;
	
	
	//public static final int ablilityButtonTopGap = 50;
	public static final int abilityButtonWidth = 300;
	public static final int abilityButtonHeight = 100;
	public static final int abilityButtonTileGap = 125;
	public static final int abilityButtonGap = 30;
	
	public static final int gameOverVerticalGap = 300;
	public static final int gameOverNumberWidth = 130;
	
	public static final int titleNumberWidth = 150;
	public static final int titleVerticalGap = 300;
	
	
}
