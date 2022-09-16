package core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Button extends Actor {
	
	protected Sprite sprite;
	protected Texture baseTexture, hoverTexture;
	
	public Button(Stage mr, int x, int y) {
		//TODO change parameters to contain images or make setImages
		mr.addActor(this);
		this.setBounds(x, y, Constants.menuButtonWidth, Constants.menuButtonHeight);
		this.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, 
					int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				sprite.setTexture(hoverTexture);
			}
			
			@Override
			public void exit(InputEvent event, float x, float y, 
					int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				sprite.setTexture(baseTexture);
			}
		});
	}
	
	public void setTexture(Texture base, Texture hover) {
		baseTexture = base;
		hoverTexture = hover;
		if(sprite == null) {
			sprite = new Sprite(baseTexture);
			sprite.setBounds(getX(), getY(), getWidth(), getHeight());
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(sprite != null) {
			sprite.draw(batch);
		}
	}
	
	
}
