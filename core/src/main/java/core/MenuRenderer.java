package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MenuRenderer extends Stage {
	
	private ZeroSumGame game;
	
	private Button startButton, abilityButton;
	private Sprite title;
	
	public MenuRenderer(ZeroSumGame g) {
		super(new FitViewport(Constants.baseWidth, Constants.baseHeight, new OrthographicCamera()));
		
		game = g;
		
		setMenuButtons();
	}

	private void setMenuButtons() {
		int start = (Constants.baseWidth - Constants.menuButtonWidth)/2;
		int y = Constants.baseHeight - Constants.topMenuButtonHeight;
		
		title = new Sprite(new Texture(Gdx.files.internal("Title2.png")));
		title.setBounds(0, 0, Constants.titleNumberWidth * 65f/8, Constants.titleNumberWidth * Constants.numberRatio * 28f/12);
		title.setPosition((Constants.baseWidth - title.getWidth())/2, Constants.baseHeight - Constants.topMenuButtonHeight + Constants.menuButtonHeight + Constants.menuButtonGap);
		
		startButton = new Button(this, start, y);
		startButton.setTexture(new Texture(Gdx.files.internal("Play.png")), new Texture(Gdx.files.internal("Play2.png")));
		startButton.addListener(new ClickListener() {
			

			
			
			@Override
			public void touchUp(InputEvent event, float x, float y, 
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if(isOver()) {
					startButton.sprite.setTexture(startButton.baseTexture);
					game.startGame();
				}
			}
		});
		y -= Constants.menuButtonHeight + Constants.menuButtonGap;
		
		abilityButton = new Button(this, start, y);
		
		abilityButton.setTexture(new Texture(Gdx.files.internal("Abilities.png")), new Texture(Gdx.files.internal("Abilities2.png")));
		abilityButton.addListener(new ClickListener() {	
			
			@Override
			public void touchUp(InputEvent event, float x, float y, 
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if(isOver()) {
					abilityButton.sprite.setTexture(abilityButton.baseTexture);
					game.toAbilities();
				}
			}
		});
	}
	
	@Override
	public void draw() {
		super.draw();
		
		getBatch().begin();
		title.draw(getBatch());
		getBatch().end();
	}
	
	public void resize(int width, int height) {
		this.getViewport().update(width, height);
	}
	

}
