package uk.co.redfruit.gdx.skyisfalling.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import uk.co.redfruit.gdx.skyisfalling.SkyIsFalling;
import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class DesktopLauncher {

    private static final String TAG = "DesktopLauncher";

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "The Sky is Falling";
        config.width = 800;
        config.height = 480;
        config.addIcon("images/desktop_icon.png", Files.FileType.Internal);
        new LwjglApplication(new SkyIsFalling(new DesktopGooglePlayServices()), config);
    }

    public static class DesktopGooglePlayServices implements GooglePlayServices {

        @Override
        public void signIn() {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "Sign in called for GPGS");
            }
        }

        @Override
        public void signOut() {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "Sign out called for GPGS");
            }
        }

        @Override
        public void unlockAchievement(String achievement) {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "Achievement unlocked: " + achievement);
            }
        }

        @Override
        public void submitScore(int highScore) {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "submitScore called for GPGS");
            }
        }

        @Override
        public void showAchievements() {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "showAchievments called for GPGS");
            }
        }

        @Override
        public void showScore() {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "showScore called for GPGS");
            }
        }

        @Override
        public boolean isSignedIn() {
            return false;
        }
    }
}
