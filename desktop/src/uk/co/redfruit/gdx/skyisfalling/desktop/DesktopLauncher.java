package uk.co.redfruit.gdx.skyisfalling.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.Json;

import uk.co.redfruit.gdx.skyisfalling.SkyIsFalling;
import uk.co.redfruit.gdx.skyisfalling.desktop.scores.HighScores;
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

        public Json highScoreJson = new Json();
        public HighScores highScores;


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
            highScores = highScoreJson.fromJson(HighScores.class, Gdx.files.local("data/scores.json"));
            highScores.scores.add(highScore);
            highScores.scores.sort();
            highScores.scores.reverse();

            //keep the list to only the top 5 values
            if (highScores.scores.size > 5) {
                highScores.scores.truncate(5);
            }

            highScoreJson.toJson(highScores, Gdx.files.local("data/scores.json"));
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

            highScores = highScoreJson.fromJson(HighScores.class
                    , Gdx.files.local("data/scores.json"));

            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "Saved high highScores: " + highScores.scores.size);
                for (Integer i : highScores.scores) {
                    Gdx.app.log(TAG, "Score: " + i);
                }
            }


        }

        @Override
        public boolean isSignedIn() {
            return false;
        }
    }
}
