package core;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import abilities.*;

public class AbilityRenderer extends Stage {
	
	private ZeroSumGame game;
	
	public AbilityRenderer(ZeroSumGame g) {
		super(new FitViewport(Constants.baseWidth, Constants.baseHeight, new OrthographicCamera()));
		
		game = g;
		
		AbilitySelectionButton currentButton;
		Ability currentAbility;
		
		int x = (Constants.baseWidth - 2 * Constants.abilityButtonSelectionWidth)/3;
		
		int y = Constants.baseHeight - Constants.abilitySelectionButtonTopGap;
		
		
		currentAbility = new Flip();
		currentButton = new AbilitySelectionButton(this,x,y,ImageSearcher.getAbility(currentAbility, true), ImageSearcher.getAbility(currentAbility, false), currentAbility);
		currentButton.select();
		y -= Constants.abilityButtonSelectionHeight + Constants.abilityButtonGap;
		
		currentAbility = new Fuse();
		currentButton = new AbilitySelectionButton(this,x,y,ImageSearcher.getAbility(currentAbility, true), ImageSearcher.getAbility(currentAbility, false), currentAbility);
		y -= Constants.abilityButtonSelectionHeight + Constants.abilityButtonGap;
		
		currentAbility = new Reroll();
		currentButton = new AbilitySelectionButton(this,x,y,ImageSearcher.getAbility(currentAbility, true), ImageSearcher.getAbility(currentAbility, false), currentAbility);
		y -= Constants.abilityButtonSelectionHeight + Constants.abilityButtonGap;
		
		currentAbility = new Swap();
		currentButton = new AbilitySelectionButton(this,x,y,ImageSearcher.getAbility(currentAbility, true), ImageSearcher.getAbility(currentAbility, false), currentAbility);
		currentButton.select();
		//y -= Constants.abilityButtonSelectionHeight;
		
		
		Button menuButton = new Button(this, (Constants.baseWidth - Constants.menuButtonWidth)/2, (y - Constants.menuButtonHeight)/2);
		menuButton.setTexture(new Texture(Gdx.files.internal("Menu.png")), new Texture(Gdx.files.internal("Menu2.png")));
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
		
		
		x += x + Constants.abilityButtonSelectionWidth;
		y = Constants.baseHeight - Constants.abilitySelectionButtonTopGap;
		
		currentAbility = new Rewind();
		currentButton = new AbilitySelectionButton(this,x,y,ImageSearcher.getAbility(currentAbility, true), ImageSearcher.getAbility(currentAbility, false), currentAbility);
		y -= Constants.abilityButtonSelectionHeight + Constants.abilityButtonGap;
		
		currentAbility = new Delete();
		currentButton = new AbilitySelectionButton(this,x,y,ImageSearcher.getAbility(currentAbility, true), ImageSearcher.getAbility(currentAbility, false), currentAbility);
		y -= Constants.abilityButtonSelectionHeight + Constants.abilityButtonGap;
		currentButton.select();
		
		currentAbility = new Split();
		currentButton = new AbilitySelectionButton(this,x,y,ImageSearcher.getAbility(currentAbility, true), ImageSearcher.getAbility(currentAbility, false), currentAbility);
		y -= Constants.abilityButtonSelectionHeight + Constants.abilityButtonGap;
		
		currentAbility = new Reset();
		currentButton = new AbilitySelectionButton(this,x,y,ImageSearcher.getAbility(currentAbility, true), ImageSearcher.getAbility(currentAbility, false), currentAbility);
		y -= Constants.abilityButtonSelectionHeight + Constants.abilityButtonGap;
		
		
		

		
	}
	
	public void resize(int width, int height) {
		System.out.println("resizing to " + width + height);
		this.getViewport().update(width, height);
	}
	
	public LinkedList<Ability> getAbilities(){
		return game.abilities;
	}
}
