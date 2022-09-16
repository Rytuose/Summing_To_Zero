package core;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AbilityButton extends Actor{
	
	private Ability ability;
	private Sprite sprite;
	private BoardRenderer boardRenderer;
	
	public AbilityButton(BoardRenderer br, float x, float y) {
		boardRenderer = br;
		
		boardRenderer.addActor(this);
		this.setBounds(x, y, Constants.abilityButtonWidth, Constants.abilityButtonHeight);
		
		this.addListener(new ClickListener() {
			
			@Override
			public void enter(InputEvent event, float x, float y, 
					int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
//				if(GameStatus.status == GameStatus.PLAYING) {
//					sprite.setTexture(ImageSearcher.getAbility(ability, true));
//				}
				sprite.setTexture(ImageSearcher.getAbility(ability, true));
			}
			
			@Override
			public void exit(InputEvent event, float x, float y, 
					int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				sprite.setTexture(ImageSearcher.getAbility(ability, false));
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y, 
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if(isOver()) {
					release();
				}
			}
		});
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		if(sprite != null) {
			sprite.draw(batch);
		}
	}
	
	public void setAbility(Ability a) {
		ability = a;
		
		if(ability == null) {
			this.setVisible(false);
			return;
		}
		this.setVisible(true);
		
		if(sprite == null) {
			sprite = new Sprite(ImageSearcher.getAbility(a,false));
			sprite.setBounds(this.getX(), this.getY(), getWidth(), getHeight());
		}
		else {
			sprite.setTexture(ImageSearcher.getAbility(a,false));
		}
		
	}
	
	public void release() {
		if(GameStatus.status == GameStatus.PROMPTING) {
			boardRenderer.setTargetAbility(AbilityButton.this, null);
			return;
		}
		
		if(boardRenderer.getPoints() < ability.cost 
				|| Constants.boardDimension * Constants.boardDimension - boardRenderer.getOpenTiles() < 
				 ability.targets) { //Nothing should happen
			return;
		}
		//Enter ability mode
		boardRenderer.setTargetAbility(this, ability);
		if(ability.activate(boardRenderer)) {
			boardRenderer.setTargetAbility(this, null);
		}
	}

}
