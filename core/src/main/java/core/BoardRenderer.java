package core;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import abilities.Delete;
import abilities.Flip;
import abilities.Reroll;
import abilities.Reset;
import abilities.Rewind;

public class BoardRenderer extends Stage {
	
	private ZeroSumGame game;
	private Tile[][] board;
	
	private ArrayList<Sprite> turnSprites;
	private AbilityButton[] abilityButtons;
	private Button menuButton,retryButton;
	private Sprite currentTileSprite,nextTileSprite;
	private Sprite negative,tenNumber, oneNumber;
	private Sprite hundredPoint,tenPoint,onePoint,pts;
	private Sprite boardBorder,turn, gameOver;
	private Image transparentLayer;
	private Image backgroundLayer;
	private LinkedList<Tile> sumList;
	private Tile hoverTile;
	
	private boolean displayNegative;
	private LinkedList<Tile> targetList;
	private Ability targetAbility;
	private int openTiles, currentValue, nextValue;
	private int points;
	private int turnCount,maxValue;
	private float ratio;
	
	public BoardRenderer(ZeroSumGame g) {
		super(new FitViewport(Constants.baseWidth, Constants.baseHeight, new OrthographicCamera()));
		game = g;
		setUp();
	}
	
	private void setUp() {
		board = new Tile[Constants.boardDimension][Constants.boardDimension];
		for(int i = 0; i < Constants.boardDimension; i++) {
			for(int j = 0 ; j < Constants.boardDimension; j++) {
				board[i][j] = new Tile(this,i,j);
			}
		}
		openTiles = 0;
		turnCount = 0;
		points = Constants.startingPoints;
		maxValue = Constants.startMaxValue;
		targetList = new LinkedList<Tile>();
		turnSprites = new ArrayList<Sprite>(10);
		
		if(Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth() > Constants.baseHeight/(float)Constants.baseWidth) {
			ratio = Gdx.graphics.getWidth()/(float)Constants.baseWidth;
		}
		else {
			ratio = Gdx.graphics.getHeight()/(float)Constants.baseHeight;
		}
		
		Pixmap coloredLayer = new Pixmap((int)this.getWidth(), (int)this.getHeight(), Pixmap.Format.RGBA8888);
		coloredLayer.setColor(32/255f, 117/255f, 103/255f, 1);
		coloredLayer.fillRectangle(0, 0, (int)this.getWidth(), (int)this.getHeight());

		backgroundLayer = new Image(new Texture(coloredLayer));
		coloredLayer.dispose();		
		
		//Transparent Layer Start
		Pixmap shadedLayer = new Pixmap((int)this.getWidth(), (int)this.getHeight(), Pixmap.Format.RGBA8888);
		shadedLayer.setColor(Color.BLACK);
		shadedLayer.fillRectangle(0, 0, (int)this.getWidth(), (int)this.getHeight());
		
		transparentLayer = new Image(new Texture(shadedLayer));
		shadedLayer.dispose();

		
		transparentLayer.setSize(this.getWidth(), this.getHeight());
		transparentLayer.getColor().a = Constants.transparencyRatio;
		this.addActor(transparentLayer);
		transparentLayer.setVisible(false);
		transparentLayer.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, 
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if(GameStatus.status == GameStatus.PLAYING) {
					clearLink();
				}
				else if(GameStatus.status == GameStatus.PROMPTING) {
					System.out.println("Status is now prompting");
					setTargetAbility(null,null);
				}
			}
		});
		
		//Constants.shapeRenderer.setColor(35/255f, 45/255f, 65/255f, 1);
//		linkLayer.setColor(35/255f, 45/255f, 65/255f, 1);

		
		sumList = new LinkedList<Tile>();
		
		currentTileSprite = new Sprite(new Texture(Gdx.files.internal("expired0.png")));
		currentTileSprite.setBounds(Constants.boardGap + Constants.tileDimension * Constants.boardDimension +
				(Constants.baseWidth - (Constants.boardGap + Constants.tileDimension * Constants.boardDimension) - 
						Constants.currentTileDimension - Constants.nextTileDimension - Constants.nextTileGap)/2, 
				Constants.baseHeight - Constants.currentTileVerticalGap - Constants.currentTileDimension,
				Constants.currentTileDimension, Constants.currentTileDimension);
		
		nextTileSprite = new Sprite(new Texture(Gdx.files.internal("expired0.png")));
		nextTileSprite.setBounds(currentTileSprite.getX() + currentTileSprite.getWidth() + Constants.nextTileGap,
				currentTileSprite.getY(), Constants.nextTileDimension, Constants.nextTileDimension);
		
		nextValue = (int)(Math.random() * maxValue) + 1;
		if((int)(Math.random() * 2) == 1){
			nextValue = -nextValue;
		}
		
		updateNextValue();
		
		
		float x = Constants.boardGap + Constants.boardDimension * Constants.tileDimension + 
				(Constants.baseWidth - (Constants.boardGap + Constants.boardDimension * Constants.tileDimension) - Constants.abilityButtonWidth)/2;
		float startY =  currentTileSprite.getY() - Constants.abilityButtonTileGap - Constants.abilityButtonHeight;
		
		//For Point amount before startY changes
		float y = startY + Constants.abilityButtonHeight + (Constants.abilityButtonTileGap - Constants.pointWidth * Constants.numberRatio)/2;  
		
		abilityButtons = new AbilityButton[3];
		
		for(int i = 0; i < 3; i++) {
			abilityButtons[i] = new AbilityButton(this,x,startY);
			abilityButtons[i].setAbility(game.abilities.get(i));
			startY -= Constants.abilityButtonGap + Constants.abilityButtonHeight;
		}
		
		negative = new Sprite(new Texture(Gdx.files.internal("number/negative.png")));
		negative.setBounds(0, 0, Constants.numberWidth/2, Constants.numberWidth * Constants.numberRatio);
		tenNumber = new Sprite(new Texture(Gdx.files.internal("number/0.png")));
		tenNumber.setBounds(0, 0, Constants.numberWidth, Constants.numberWidth * Constants.numberRatio);
		oneNumber = new Sprite(new Texture(Gdx.files.internal("number/0.png")));
		oneNumber.setBounds(0, 0, Constants.numberWidth, Constants.numberWidth * Constants.numberRatio);
		
		float startX = Constants.boardGap + Constants.boardDimension * Constants.tileDimension +
				(Constants.baseWidth - (Constants.boardGap + Constants.boardDimension * Constants.tileDimension) -
				6 * Constants.pointWidth)/2;
		
		pts = new Sprite(new Texture(Gdx.files.internal("pts.png")));
		pts.setBounds(startX, y, 3*Constants.pointWidth, Constants.pointWidth * Constants.numberRatio);
		startX += Constants.pointWidth * 3; 
		
		hundredPoint = new Sprite(new Texture(Gdx.files.internal("number/0.png")));
		hundredPoint.setBounds(startX, y, Constants.pointWidth, Constants.pointWidth * Constants.numberRatio);
		startX += Constants.pointWidth;
		
		tenPoint = new Sprite(new Texture(Gdx.files.internal("number/0.png")));
		tenPoint.setBounds(startX, y, Constants.pointWidth, Constants.pointWidth * Constants.numberRatio);
		startX += Constants.pointWidth;
		
		onePoint = new Sprite(new Texture(Gdx.files.internal("number/0.png")));
		onePoint.setBounds(startX, y, Constants.pointWidth, Constants.pointWidth * Constants.numberRatio);
		
		boardBorder = new Sprite(new Texture(Gdx.files.internal("BoardBorder.png")));
		boardBorder.setBounds(board[0][0].getX() - Constants.tileDimension * 5 / 100f , 
				board[0][0].getY()  - Constants.tileDimension * 5 / 100f, 
				Constants.tileDimension * 5 * 102 / 100f, Constants.tileDimension * 5 * 102 / 100f) ;
		
		int start = (Constants.baseWidth - Constants.menuButtonWidth)/2;
		int yPos = Constants.baseHeight - Constants.topEndGameButtonHeight;
		menuButton = new Button(this, start, yPos);
		menuButton.setTexture(new Texture(Gdx.files.internal("Menu.png")), new Texture(Gdx.files.internal("Menu2.png")));
		menuButton.setVisible(false);
		menuButton.addListener(new ClickListener() {
			

			
			
			@Override
			public void touchUp(InputEvent event, float x, float y, 
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if(isOver()) {
					menuButton.sprite.setTexture(menuButton.baseTexture);
					game.endGame();
				}
			}
		});
		yPos -= Constants.menuButtonHeight + Constants.menuButtonGap;
		
		retryButton = new Button(this, start, yPos);
		
		retryButton.setTexture(new Texture(Gdx.files.internal("Retry.png")), new Texture(Gdx.files.internal("Retry2.png")));
		retryButton.setVisible(false);
		retryButton.addListener(new ClickListener() {	
			
			@Override
			public void touchUp(InputEvent event, float x, float y, 
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if(isOver()) {
					retryButton.sprite.setTexture(retryButton.baseTexture);
					reset();
				}
			}
		});
		
		turn = new Sprite(new Texture(Gdx.files.internal("Turn.png")));
		turn.setBounds(0, 0, Constants.turnNumberWidth * 43f/8, Constants.turnNumberWidth * Constants.numberRatio);
		
		gameOver = new Sprite(new Texture(Gdx.files.internal("GameOver3.png")));
		gameOver.setBounds(0, 0, Constants.gameOverNumberWidth * 75f/8, Constants.gameOverNumberWidth * Constants.numberRatio);
		gameOver.setPosition((Constants.baseWidth - gameOver.getWidth())/2, Constants.baseHeight - Constants.gameOverVerticalGap);
		
		
		updateSumDisplay();
		updatePoint();
	}
	
	public void reset() {
		for(int i = 0; i < Constants.boardDimension; i++) {
			for(int j = 0 ; j < Constants.boardDimension; j++) {
				board[i][j].setValue(0);
			}
		}
		openTiles = Constants.boardDimension * Constants.boardDimension;
		turnCount = 0;
		points = Constants.startingPoints;
		maxValue = Constants.startMaxValue;
		targetList.clear();
		sumList.clear();
		GameStatus.status = GameStatus.PLAYING;
		transparentLayer.setVisible(false);
		menuButton.setVisible(false);
		retryButton.setVisible(false);
		
		Ability temp;
		for(int i = game.abilities.size()-1; i >= 0; i--) {
			for(int j = 0; j < i; j++) {
				if(game.abilities.get(j).getCost() > game.abilities.get(j+1).getCost()) {
					temp = game.abilities.get(j);
					game.abilities.set(j, game.abilities.get(j+1));
					game.abilities.set(j+1, temp);
				}
			}
		}
		
		for(int i = 0; i < game.abilities.size(); i++) {
			abilityButtons[i].setAbility(game.abilities.get(i));
		}
		
		for(int i = game.abilities.size(); i < 3; i++) {
			abilityButtons[i].setAbility(null);
		}
		
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		updateSumDisplay();
		updatePoint();
	}

	public void resize(int width, int height) {
		this.getViewport().update(width, height);
	}
	
	@Override
	public void draw() {
		
		getBatch().begin();
		backgroundLayer.draw(getBatch(), 1);
		currentTileSprite.draw(getBatch());
		nextTileSprite.draw(getBatch());
		pts.draw(getBatch());
		hundredPoint.draw(getBatch());
		tenPoint.draw(getBatch());
		onePoint.draw(getBatch());
		
		if(GameStatus.status != GameStatus.PLAYING) {
			if(displayNegative) {
				negative.draw(getBatch());
			}
			tenNumber.draw(getBatch());
			oneNumber.draw(getBatch());
		}
		
		boardBorder.draw(getBatch());
		
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				board[i][j].drawBackground(getBatch());
			}
		}
		
		getBatch().end();
		
		super.draw();
		
		getBatch().begin();
		//System.out.println(currentTileSprite.getTexture());
		
		if(GameStatus.status == GameStatus.PLAYING) {
			if(displayNegative) {
				negative.draw(getBatch());
			}
			tenNumber.draw(getBatch());
			oneNumber.draw(getBatch());
		}
		else if (GameStatus.status == GameStatus.END) {
			turn.draw(getBatch());
			gameOver.draw(getBatch());
			int length = Integer.toString(turnCount).length();
			for(int i = 0; i < length ; i++) {
				turnSprites.get(i).draw(getBatch());
			}
		}
		

		getBatch().end();
		
		drawListLinks();
	}

	
	private void setEnd() {
		//long time = System.nanoTime();
		if(turnCount < 0) {//Just in case
			turnCount = 0;
		}
		String number = Integer.toString(turnCount);
		int yPos = Constants.baseHeight - Constants.topEndGameButtonHeight + Constants.menuButtonHeight + 2* Constants.menuButtonGap;
		float width = number.length() * Constants.turnNumberWidth + turn.getWidth();
		float xPos = (Constants.baseWidth - width)/2;
		
		turn.setPosition(xPos, yPos);
		//turn.draw(getBatch());
		
		xPos += turn.getWidth();
		
		for(int i = 0; i < number.length(); i++) {
			//System.out.println("Size " + turnSprites.size());
			if(i >= turnSprites.size()) {
				turnSprites.add(new Sprite(ImageSearcher.getNumber(0)));
				turnSprites.get(i).setBounds(0, 0, Constants.turnNumberWidth, Constants.turnNumberWidth * Constants.numberRatio);
			}
			turnSprites.get(i).setTexture(ImageSearcher.getNumber(Integer.parseInt(number.substring(i, i+1))));
			turnSprites.get(i).setPosition(xPos, yPos);
			//turnSprites.get(i).draw(getBatch());
			xPos += Constants.turnNumberWidth;
			
		}
		
		//System.out.println("Time it took to draw end turn " + (System.nanoTime() - time));
		
	}

	public void endTurn() {
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				if(board[i][j].getValue() != 0) {
					board[i][j].changeTime(-1);
				}
			}
		}
		updatePoint();
		turnCount++;
		//System.out.println("Turn " + turnCount);
	}
	
	//Tile methods
	public boolean canLink(Tile t) {
		if(!t.isPassable()) { //Cannot linked with expired tile
			//System.out.println("Expired tile");
			return false;
		}
		if(sumList.isEmpty()) { //Make a link of 1
			//System.out.println("Link of 1");
			return true;
		}
		if(sumList.contains(t) || t.getValue() == 0) { //Can't link with itself/empty tile
			//System.out.println("Empty/same tile");
			return false;
		}
		//System.out.println("Can link");
		if(getLastTile().getBoardWidth() == t.getBoardWidth()) { //Making a vertical link
			//System.out.println("Verital Linking");
			int deltaY = (getLastTile().getBoardHeight() > t.getBoardHeight())? -1:1;
			for(int i = getLastTile().getBoardHeight() + deltaY; i != t.getBoardHeight(); i += deltaY) {
				//System.out.println("Checking position (" + t.getBoardWidth() + "," + i + ")");
				if(!board[t.getBoardWidth()][i].isPassable()) {
					//System.out.println("Can't make vertical link");
					return false;
				}
			}
			return true;
		}
		else if(getLastTile().getBoardHeight() == t.getBoardHeight()) {
			//System.out.println("Horizontal Linking");
			int deltaX = (getLastTile().getBoardWidth() > t.getBoardWidth())? -1:1;
			for(int i = getLastTile().getBoardWidth() + deltaX; i != t.getBoardWidth(); i += deltaX) {
				//System.out.println("Checking position (" +  i + "," + t.getBoardHeight()   + ")");
				if(!board[i][t.getBoardHeight()].isPassable()) {
					//System.out.println("Can't make horizontal link");
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public Tile getLastTile() {
		if(sumList == null || sumList.isEmpty()) {
			return null;
		}
		return sumList.getLast();
	}
	
	public void addLink(Tile tile) {
		sumList.add(tile);
		if(sum() == 0) { //Successful clear list
			assignPoints();
			resolveList();
			endTurn();	
		}
		else {
			transparentLayer.toFront();
			transparentLayer.setVisible(true);
			for(int i = 0 ; i < board.length; i++) {
				for(int j = 0 ; j < board[i].length; j++) {
					if(canLink(board[i][j]) || sumList.contains(board[i][j])) {
						board[i][j].toFront();
					}
				}
			}
			updateSumDisplay();
		}
	}
	
	public void clearLink() {
		while(!sumList.isEmpty()) {
			//System.out.println("clearing link");
			sumList.remove();
		}
		hoverTile = null;
		transparentLayer.setVisible(false);
		updateSumDisplay();
	}
	
	private void assignPoints() {
		LinkedList<Integer> variety = new LinkedList<Integer>();
		int sum = 0, length = 0;
		for(Tile t: sumList) {
			if(!variety.contains(Math.abs(t.getValue()))) {
				variety.add(Math.abs(t.getValue()));
			}
			if(t.getValue() > 0) {
				sum += t.getValue();
			}
			length ++;
		}
		points += (variety.size()) + (length-2)/2 + sum/4;	
		if(points > 999) {
			points = 999;;
		}
		updatePoint();
	}
	
	public void decreasePoint() {
		points -= Constants.pointDeduction;
		if(points < 0) {
			//System.out.println("Game over by point deduction");
			endGame();
		}
		//updatePoint();
	}
	
	public void decreasePoint(int amount) {
		points -= amount;
		if(points < 0) {
			
			//System.out.println("Game over by point deduction");
			endGame();
		}
		updatePoint();
	}
	
	private void updatePoint() {
		if(points < 0) {
			hundredPoint.setTexture(ImageSearcher.getNumber(0));
			tenPoint.setTexture(ImageSearcher.getNumber(0));
			onePoint.setTexture(ImageSearcher.getNumber(0));
			return;
		}
		hundredPoint.setTexture(ImageSearcher.getNumber(points/100));
		tenPoint.setTexture(ImageSearcher.getNumber(points%100/10));
		onePoint.setTexture(ImageSearcher.getNumber(points%10));
	}

	public void resolveList() {
		int sum = sum();
		if(sum == 0) {
			for(Tile t: sumList) {
				t.setValue(0);
				//openTiles++;  //Now included in setValue 0
			}
		}
		clearLink();
		updateSumDisplay();
	}
	
	private int sum() {
		int sum = 0;
		for(Tile t: sumList) {
			sum += t.getValue();
		}
		return sum;
	}

	//Board Methods
	
	public void updateNextValue() {
		if(turnCount % Constants.incrementTime == 0 && maxValue < 9) {
			maxValue = Constants.startMaxValue + Constants.incrementAmount * (turnCount/Constants.incrementTime);
			System.out.println("Max value changed to " + maxValue);
		}
		currentValue = nextValue;
		nextValue = (int)(Math.random() * maxValue) + 1;
		if((int)(Math.random() * 2) == 1){
			nextValue = -nextValue;
		}
		
		System.out.println("next value " + nextValue);
		
		if(nextValue > 9) {
			nextValue = 9;
		}
		else if(nextValue < -9) {
			nextValue = -9;
		}
		
		
		//System.out.println("current Value " + currentValue + " nextValue " + nextValue);
		
		currentTileSprite.setTexture(ImageSearcher.getImage(currentValue, Constants.tileTime));
		nextTileSprite.setTexture(ImageSearcher.getImage(nextValue, Constants.tileTime));
		
	}
	
	public void changeCurrentValue(int newVal) {
		currentValue = newVal;
		currentTileSprite.setTexture(ImageSearcher.getImage(currentValue, Constants.tileTime));
	}
	
	public void addInverse(int value) {
		openTiles --;
		if(openTiles <= 0) { // You need 2 spaces to play a tile
			//System.out.println("End Game");
			endGame();
			return; //TODO Lose the game
		}
		int rand = (int)(Math.random() * openTiles) + 1; //Bad stuff if we happen to get 0
		for(int i = 0; i < Constants.boardDimension; i++) {
			for(int j = 0 ; j < Constants.boardDimension; j++) {
				if(board[i][j].getValue() == 0) {
					rand--;
				}
				if(rand == 0) {
					board[i][j].setValue(-1 * value);
					openTiles--;
					if(openTiles <= 0) { // You need 2 spaces to play a tile
						//System.out.println("End Game");
						endGame();
						return; //TODO Lose the game
					}
					return;
				}
			}
		}
		if(openTiles <= 0) { // You need 2 spaces to play a tile
			//System.out.println("End Game");
			endGame();
			return; //TODO Lose the game
		}

	}
	
	
	private void drawListLinks() {
		Constants.shapeRenderer.begin(ShapeType.Filled);
		//Constants.shapeRenderer.setColor(35/255f, 45/255f, 65/255f, 1);
		Tile previousTile = null, currentTile = null;
		
		for(Tile t: sumList) {
			currentTile = t;
			if(previousTile != null) {
				drawLine(previousTile, currentTile);
			}
			previousTile = currentTile;
		}
		if(hoverTile != null && currentTile != null) {
			drawLine(currentTile, hoverTile);
		}
		Constants.shapeRenderer.end();
	}
	
	private void drawLine(Tile previousTile, Tile currentTile) {
		
		float width = Constants.tileDimension/40f;
		
		if(previousTile.getX() == currentTile.getX()) {
			if(previousTile.getY() < currentTile.getY()) {
				//System.out.println("Case 1");
				Constants.shapeRenderer.rectLine((previousTile.getX() + previousTile.getWidth()/2f) * ratio,
						(previousTile.getY() + previousTile.getHeight()/2f) * ratio,
						(currentTile.getX() + currentTile.getWidth()/2f) * ratio,
						(currentTile.getY() + currentTile.getHeight()/2f + width) * ratio,
						width);
			}
			else {
				//System.out.println("Case 2");
				Constants.shapeRenderer.rectLine((previousTile.getX() + previousTile.getWidth()/2f) * ratio,
						(previousTile.getY() + previousTile.getHeight()/2f) * ratio,
						(currentTile.getX() + currentTile.getWidth()/2f) * ratio,
						(currentTile.getY() + currentTile.getHeight()/2f - width) * ratio,
						width);
			}
		}
		else {
			if(previousTile.getX() < currentTile.getX()) {
				//System.out.println("Case 3");
				Constants.shapeRenderer.rectLine((previousTile.getX() + previousTile.getWidth()/2f) * ratio,
						(previousTile.getY() + previousTile.getHeight()/2f) * ratio,
						(currentTile.getX() + currentTile.getWidth()/2f + width) * ratio,
						(currentTile.getY() + currentTile.getHeight()/2f) * ratio,
						width);
			}
			else {
				//System.out.println("Case 4");
				Constants.shapeRenderer.rectLine((previousTile.getX() + previousTile.getWidth()/2f) * ratio,
						(previousTile.getY() + previousTile.getHeight()/2f) * ratio,
						(currentTile.getX() + currentTile.getWidth()/2f - width) * ratio,
						(currentTile.getY() + currentTile.getHeight()/2f) * ratio,
						width);
			}
		}
	}
	
	private void updateSumDisplay() {
		int sum = (hoverTile == null)? sum():sum() + hoverTile.getValue();;
		float width = (sum < 0) ? 5*Constants.numberWidth/2:2 * Constants.numberWidth;
		float startX = Constants.boardGap + Constants.boardDimension * Constants.tileDimension + 
				(Constants.baseWidth - (Constants.boardGap + Constants.boardDimension * Constants.tileDimension) - width)/2;
		if(sum < 0) {
			negative.setPosition(startX, Constants.boardGap);
			startX += negative.getWidth();
			displayNegative = true;
		}
		else {
			displayNegative = false;
		}
		
		if(Math.abs(sum) > 99) {//Doubt this will happen but just in case
			tenNumber.setTexture(ImageSearcher.getNumber(9));
			oneNumber.setTexture(ImageSearcher.getNumber(9));
		}
		else {
			tenNumber.setTexture(ImageSearcher.getNumber(Math.abs(sum)/10));
			oneNumber.setTexture(ImageSearcher.getNumber(Math.abs(sum)%10));
		}
		
		tenNumber.setPosition(startX, Constants.boardGap);
		startX+=tenNumber.getWidth();
		oneNumber.setPosition(startX, Constants.boardGap);
		
	}
	
	public void setTargetAbility(AbilityButton ab, Ability a) {
		
		targetAbility = a;
		if(a != null) {
			//System.out.println("Changing game status to prompting");
			GameStatus.status = GameStatus.PROMPTING;
			
			transparentLayer.toFront();
			transparentLayer.setVisible(true);
			
			if(ab != null) {
				ab.toFront();
			}
			//ab.toFront();
			
			if(a.targets > 0) {
				for(int i = 0 ; i < board.length; i++) {
					for(int j = 0 ; j < board[i].length; j++) {
						if(a.isApplicable(this, board[i][j]) && !targetList.contains(board[i][j])) {
							board[i][j].toFront();
						}
					}
				}
			}
		}
		else {
			//System.out.println("Changing game status to playing");
			GameStatus.status = GameStatus.PLAYING;
			while(!targetList.isEmpty()) {
				targetList.remove(0);
			}
			transparentLayer.setVisible(false);
		}
	}
	
	public void addTargetTile(Tile t) {
		targetList.add(t);
		if(targetAbility != null) {
			if(targetAbility.activate(this)){
				setTargetAbility(null, null);
			}
			else {
				setTargetAbility(null,targetAbility);
			}
		}
	}
	
	public LinkedList<Tile> getTargetTiles(){
		return targetList;
	}
	
	public void endGame() {
		transparentLayer.toFront();
		transparentLayer.setVisible(true);
		GameStatus.status = GameStatus.END;
		menuButton.setVisible(true);
		menuButton.toFront();
		retryButton.setVisible(true);
		retryButton.toFront();
		setEnd();
	}
	
	public int getPoints() {return points;}
	
	public int getCurrentValue() {return currentValue;}
	
	public int getOpenTiles() {return openTiles;}
	
	public Tile[][] getBoard() {return board;}
	
	public void removedOpenTiles() {
		//System.out.println("Increasing open tiles by 1"); 
		openTiles++;
		//System.out.println(openTiles);
	}
	
	public void addToOpenTile() {
		openTiles--;
	}
	
	public void setNextTile(Tile t) {
		hoverTile = t;
		updateSumDisplay();
	}

	public boolean transparentVisible() {
		return transparentLayer.isVisible();
	}


}
