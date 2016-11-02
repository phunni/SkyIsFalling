package uk.co.redfruit.gdx.skyisfalling.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import uk.co.redfruit.gdx.skyisfalling.SkyIsFalling;
import uk.co.redfruit.gdx.skyisfalling.android.google.GameHelper;
import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class AndroidLauncher extends AndroidApplication {

    private static final String TAG = "AndroidLauncher";

    private GameHelper gameHelper;
    private final static int REQUEST_CODE = 1;

    private RelativeLayout layout;
    private View gameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(Constants.DEBUG);
        //gameHelper.setMaxAutoSignInAttempts(0);


        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
            @Override
            public void onSignInFailed() {
                Gdx.app.log("hunnisett", "Sign in failed");
            }

            @Override
            public void onSignInSucceeded() {
            }
        };

        gameHelper.setup(gameHelperListener);


        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        config.useAccelerometer = false;
        config.useCompass = false;
        //gameView = initializeForView(new SkyIsFalling(), config);
        initialize(new SkyIsFalling(), config);

        /*layout = new RelativeLayout(this);
        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(layout);*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        gameHelper.onStart(this);
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        gameHelper.onStop();
    }*/

    public class AndroidGooglePlayServices implements GooglePlayServices {

        @Override
        public void signIn() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameHelper.beginUserInitiatedSignIn();
                    }
                });
            } catch (Exception e) {
                Gdx.app.log("hunnisett", "Log in failed: " + e.getMessage());
            }
        }

        @Override
        public void signOut() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameHelper.signOut();
                    }
                });
            } catch (Exception e) {
                Gdx.app.log(TAG, "Log out failed: " + e.getMessage());
            }
        }

        @Override
        public boolean isSignedIn() {
            return false;
        }
    }
}
