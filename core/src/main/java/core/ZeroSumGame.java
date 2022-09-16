package core;

import java.util.LinkedList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ZeroSumGame extends ApplicationAdapter {
	
	public LinkedList<Ability> abilities = new LinkedList<Ability>();
	
	private BoardRenderer boardRenderer;
	private MenuRenderer menuRenderer;
	private AbilityRenderer abilityRenderer;
	private Stage currentStage;


	@Override
	public void create() {
		abilityRenderer = new AbilityRenderer(this);
		boardRenderer = new BoardRenderer(this);
		menuRenderer = new MenuRenderer(this);

		
		//boardRenderer.setDebugAll(true);
//		menuRenderer.setDebugAll(true);
//		abilityRenderer.setDebugAll(true);
		
		currentStage = menuRenderer;
		
		Gdx.input.setInputProcessor(currentStage);
		
		
		Constants.tileFont.getData().setScale(2);
		
		Gdx.gl.glClearColor(32/255f, 117/255f, 103/255f, 1);
//		batch = new SpriteBatch();
//		image = new Texture("libgdx.png");
	}

	@Override
	public void render() {
		super.render();
		
		if(currentStage == boardRenderer && boardRenderer.transparentVisible()) {
			Gdx.gl.glClearColor(13/255f, 47/255f, 41/255f, 1);
		}
		else {
			Gdx.gl.glClearColor(32/255f, 117/255f, 103/255f, 1);
		}
		//Gdx.gl.glClearColor(0, .66f, .42f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		currentStage.getBatch().setProjectionMatrix(boardRenderer.getCamera().combined);
		currentStage.act();
		
		currentStage.draw();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		boardRenderer.resize(width, height);
		menuRenderer.resize(width, height);
		abilityRenderer.resize(width, height);
	}
	
	public void startGame() {
		if(currentStage != boardRenderer) {
			currentStage = boardRenderer;
			Gdx.input.setInputProcessor(boardRenderer);
			boardRenderer.reset();
		}
	}
	
	public void endGame() {
		if(currentStage != menuRenderer) {
			currentStage = menuRenderer;
			Gdx.input.setInputProcessor(menuRenderer);
		}
	}
	
	public void toAbilities() {
		if(currentStage != abilityRenderer) {
			currentStage = abilityRenderer;
			Gdx.input.setInputProcessor(abilityRenderer);
		}
	}

	@Override
	public void dispose() {
//		batch.dispose();
//		image.dispose();
	}
}