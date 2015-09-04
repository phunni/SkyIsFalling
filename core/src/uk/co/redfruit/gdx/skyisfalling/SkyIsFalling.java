package uk.co.redfruit.gdx.skyisfalling;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uk.co.redfruit.gdx.skyisfalling.game.assets.Assets;
import uk.co.redfruit.gdx.skyisfalling.screens.MenuScreen;
import uk.co.redfruit.gdx.skyisfalling.screens.RedfruitScreen;

public class SkyIsFalling extends Game {


	
	@Override
	public void create () {
		// Set Libgdx log level 
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Assets.getInstance().intit(new AssetManager());
		setScreen(new MenuScreen(this));
	}

}
