package core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class AbilitySelectionButton extends Actor {
	
	private Sprite sprite;
	private Texture selected, notSelected;
	private AbilityRenderer abilityRenderer;
	private Ability ability;
	
	public AbilitySelectionButton(AbilityRenderer ar, int x, int y, Texture s, Texture ns, Ability a) {
		
		this.setBounds(x, y, Constants.abilityButtonSelectionWidth, Constants.abilityButtonSelectionHeight);
		abilityRenderer = ar;
		ability = a;
		selected = s;
		notSelected = ns;
		sprite = new Sprite(notSelected);
		sprite.setBounds(getX(), getY(), getWidth(), getHeight());
		
		ar.addActor(this);
		
		this.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, 
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				if(isOver()) {
					select();
				}
			}
		});
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(sprite != null) {
			sprite.draw(batch);
		}
	}
	
	public Ability getAbility() {return ability;}
	
	public void select() {
		if(abilityRenderer.getAbilities().remove(ability)) {
			System.out.println("removed");
			sprite.setTexture(notSelected);
			return;
		}
		if(abilityRenderer.getAbilities().size() < 3) {
			abilityRenderer.getAbilities().add(ability);
			sprite.setTexture(selected);
		}
	}
	
}
