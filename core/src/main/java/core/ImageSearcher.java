package core;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class ImageSearcher {
	
	private static HashMap<Integer, Texture> tileTextureMap = new HashMap<Integer, Texture>();
	private static HashMap<String,Texture> abilityTextureMap = new HashMap<String,Texture>();
	private static Texture[] numbers = new Texture[10];
	
	public static Texture empty = new Texture(Gdx.files.internal("Empty4.png"));
	
	
	public static Texture getImage(Tile t) {


		return getImage(t.getValue(), t.getTime());
	}
	
	public static Texture getImage(int value, int time) {
		
		if(time < -2) {
			
			time = time % 3;
		}
		
		if(tileTextureMap.containsKey(getTileValue(value,time))) {
			//System.out.println("Reused texture " + value + " " + time);
			return tileTextureMap.get(getTileValue(value,time));
		}
		
		Texture core,background;
		
		try {
			core = new Texture(Gdx.files.internal("center/" + Integer.toString(value) + ".png"));

			if(time > 4) {
				background = new Texture(Gdx.files.internal("border/border4.png"));
			}
			else if(time <= 0) {
				try {
					return new Texture(Gdx.files.internal("expired" + Integer.toString(time) + ".png"));
				}
				catch(Exception e) {	
					return new Texture(Gdx.files.internal("expired0.png"));
				}
			}
			else {
				//System.out.println("border/border" + Integer.toString(t.getValue()) + ".png");
				background = new Texture(Gdx.files.internal("border/border" + Integer.toString(time) + ".png"));
			}
		}
		catch(Exception e) {
			//System.out.println("Failed getting " + t.getValue() + " with time " + t.getTime());
			return null;
		}
		
		core.getTextureData().prepare();
		background.getTextureData().prepare();
		
		Pixmap coreMap = core.getTextureData().consumePixmap();
		Pixmap backgroundMap = background.getTextureData().consumePixmap();
		
		backgroundMap.drawPixmap(coreMap, 0, 0);
		
		Texture texture = new Texture(backgroundMap);
		
		core.dispose();
		background.dispose();
		coreMap.dispose();
		backgroundMap.dispose();
		
		if(!tileTextureMap.containsKey(getTileValue(value,time))) {
			tileTextureMap.put(getTileValue(value,time), texture);
		}
		
		return texture;
	}
	
	private static int getTileValue(int val, int time) {
		if(time <= 0) {
			return 0;
		}
		int value = time * 10 + Math.abs(val);
		if(val < 0) {
			value *= -1;
		}
		return value;
	}
	
	public static Texture getNumber(int number) {
		try {
			if(numbers[number] != null) {
				return numbers[number];
			}
			Texture t = new Texture(Gdx.files.internal("number/" + Integer.toString(number) + ".png"));
			numbers[number] = t;
			return t;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public static Texture getAbility(Ability a, boolean isOver) {
		String className = a.getClass().toString();
		int lastPoint = className.lastIndexOf(".");
		String filePath = "abilities/" + className.substring(lastPoint+1);
		if(isOver) {
			filePath +=  "2.png";
		}
		else {
			filePath += ".png";
		}
		
		if(abilityTextureMap.containsKey(filePath)) {
			return abilityTextureMap.get(filePath);
		}
		
		try {
			Texture t = new Texture(Gdx.files.internal(filePath));
			abilityTextureMap.put(filePath, t);
			return t;
		}
		catch(Exception e) {
			return null;
		}
	}
	
}
