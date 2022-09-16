package core;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Tile extends Actor {
	
	
	private BoardRenderer boardRenderer;
	private int width;
	private int height;
	private Sprite sprite, baseSprite;
	private int value, tempValue; //0 is empty
	private int time;
	
	public Tile(BoardRenderer br, int w, int h) {
		boardRenderer = br;
		width = w;
		height = h;
		value = 0;
		tempValue = 0;
		time = Constants.tileTime;
		

		
		br.addActor(this);
		this.setBounds(Constants.boardGap + w * Constants.tileDimension,
				Constants.boardGap + h * Constants.tileDimension,
				Constants.tileDimension, Constants.tileDimension);
		
		try {
			sprite = new Sprite(ImageSearcher.getImage(this));
			sprite.setBounds(this.getX(), this.getY(),this.getWidth(), this.getHeight());
		}
		catch(Exception e) {
			sprite = null;
		}
		
		baseSprite = new Sprite(ImageSearcher.empty);
		baseSprite.setBounds(getX(), getY(), getWidth(), getHeight());
		
		this.addListener(new ClickListener() {
			
			@Override
			public void enter(InputEvent event, float x, float y, 
					int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				Tile.this.enter();
			}
			
			@Override
			public void exit(InputEvent event, float x, float y, 
					int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				Tile.this.exit();
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, 
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if(isOver()) {
					Tile.this.click();
				}
			}
		});
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		//Constants.tileFont.draw(batch, Integer.toString(getValue()), this.getX(), this.getY() + this.getHeight());
		//baseSprite.draw(batch);
		if(sprite != null && sprite.getTexture() != null) {
			sprite.draw(batch);
		}
	}
	
	public void drawBackground(Batch batch) {
		baseSprite.draw(batch);
	}
	
	@Override
	protected void positionChanged() {
		super.positionChanged();
		if (sprite != null) {
			sprite.setPosition(getX(), getY());
		}

	}
	
	public void click() {
		if(GameStatus.status == GameStatus.PROMPTING) {
			boardRenderer.addTargetTile(this);
			return;
		}
		
		if(value == 0 && boardRenderer.getLastTile() == null) {
			int rand = boardRenderer.getCurrentValue();
			value = rand;
			updateImage();
			boardRenderer.addInverse(rand);
			boardRenderer.updateNextValue();
			boardRenderer.endTurn();
		}
		else if(boardRenderer.canLink(this)) {
			//System.out.println("Linking");
			boardRenderer.addLink(this);
		}
		else {
			//System.out.println("clear link");
			boardRenderer.clearLink();
		}
	}
	
	public void enter() {
		if(GameStatus.status == GameStatus.PROMPTING) {
			return;
		}
		
		if(value == 0 && boardRenderer.getLastTile() == null) {
			tempValue = boardRenderer.getCurrentValue();
			updateImage();
		}
		else if(boardRenderer.canLink(this)) {
			boardRenderer.setNextTile(this);
		}
	}
	
	public void exit() {
		tempValue = 0;
		updateImage();
		boardRenderer.setNextTile(null);
	}
	
	private void updateImage() {
		if(sprite != null) {
			sprite.setTexture(ImageSearcher.getImage(this));
		}
		else {
			try {
				sprite = new Sprite(ImageSearcher.getImage(this));
				sprite.setBounds(this.getX(), this.getY(),this.getWidth(), this.getHeight());
			}
			catch(Exception e) {}
		}
	}
	
	public void setTime(int t) {
		time = t;
		updateImage();
	}
	
	public void changeTime(int deltaTime) {
		time += deltaTime;
//		if(time <= -3) {
//			time = 0;
//		}
		if(time % 3 == 0 && time <= 0) {
			boardRenderer.decreasePoint(Constants.pointDeduction);
		}
		updateImage();
	}
	
	public int getTime() {return time;}
	
	public boolean isPassable(){return time > 0;}
	
	public int getValue() {return value + tempValue;}
	
	public int getBoardWidth() {return width;}
	
	public int getBoardHeight() {return height;}
	
	public void setValue(int val) {
		if(value == val) {
			return;
		}
		value = val;
		time = Constants.tileTime;
		if(val == 0) { //Clearing the tile
			boardRenderer.removedOpenTiles();
		}
		updateImage();
	}
}
