package uk.co.redfruit.gdx.skyisfalling.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by paul on 04/10/16.
 */
public class GamePreferences {

    private static final GamePreferences instance = new GamePreferences();
    public boolean music;
    public boolean sfx;
    public float musicVolume;
    public float sfxVolume;
    public boolean autoShoot;
    public boolean showFPS;
    private Preferences preferences;


    private GamePreferences() {
        preferences = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    public static GamePreferences getInstance() {
        return instance;
    }

    //methods start
    public void load() {
        music = preferences.getBoolean("music", true);
        sfx = preferences.getBoolean("sfx", true);
        musicVolume = MathUtils.clamp(preferences.getFloat("musicVolume", 1), 0.0f, 1.0f);
        sfxVolume = MathUtils.clamp(preferences.getFloat("sfxVolume", 1), 0.0f, 1.0f);
        autoShoot = preferences.getBoolean("autoShoot", false);
        showFPS = preferences.getBoolean("Show FPS", false);
    }

    public void save() {
        preferences.putBoolean("music", music);
        preferences.putBoolean("sfx", sfx);
        preferences.putFloat("musicVolume", musicVolume);
        preferences.putFloat("sfxVolume", sfxVolume);
        preferences.putBoolean("autoShoot", autoShoot);
        preferences.putBoolean("showFPS", showFPS);
        preferences.flush();
    }
//methods end

}
