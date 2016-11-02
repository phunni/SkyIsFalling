package uk.co.redfruit.gdx.skyisfalling.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import uk.co.redfruit.gdx.skyisfalling.SkyIsFalling;
import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "The Sky is Falling";
		config.width = 800;
		config.height = 480;
        config.addIcon("images/desktop_icon.png", Files.FileType.Internal);
        new LwjglApplication(new SkyIsFalling(), config);
	}

	public static class DesktopGooglePlayServices implements GooglePlayServices {

        @Override
        public void signIn() {

        }

        @Override
        public void signOut() {

        }

        @Override
        public boolean isSignedIn() {
            return false;
        }
    }
}
