package uk.co.redfruit.gdx.skyisfalling.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;

import uk.co.redfruit.gdx.skyisfalling.SkyIsFalling;
import uk.co.redfruit.gdx.skyisfalling.android.google.GameHelper;
import uk.co.redfruit.gdx.skyisfalling.google.play.services.GooglePlayServices;
import uk.co.redfruit.gdx.skyisfalling.utils.Constants;

public class AndroidLauncher extends AndroidApplication {

    private static final String TAG = "GPGS";
    private final static int REQUEST_CODE = 1;
    private static final int REQUEST_ACHIEVEMENTS = 2;
    private GameHelper gameHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(Constants.DEBUG);
        gameHelper.setMaxAutoSignInAttempts(0);


        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener() {
            @Override
            public void onSignInFailed() {
                Gdx.app.log(TAG, "Sign in failed");
            }

            @Override
            public void onSignInSucceeded() {
                Gdx.app.log(TAG, "Sign in failed");
            }
        };

        gameHelper.setup(gameHelperListener);


        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        config.useAccelerometer = false;
        config.useCompass = false;
        View gameView = initializeForView(new SkyIsFalling(new AndroidGooglePlayServices()), config);

        RelativeLayout layout = new RelativeLayout(this);
        layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(layout);


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

    @Override
    protected void onStop() {
        super.onStop();
        gameHelper.onStop();
    }

    public class AndroidGooglePlayServices implements GooglePlayServices {

        @Override
        public void signIn() {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "signIn called");
            }
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameHelper.beginUserInitiatedSignIn();
                    }
                });
            } catch (Exception e) {
                Gdx.app.log(TAG, "Log in failed: " + e.getMessage());
            }
        }

        @Override
        public void signOut() {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "signOut called");
            }
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
        public void unlockAchievement(String achievement_id) {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "unlockAchievement called: " + achievement_id);
            }
            if (gameHelper.isSignedIn()) {
                String achievement = "";
                if ("achievement_first_wave".equals(achievement_id)) {
                    achievement = getString(R.string.achievement_first_wave);
                } else if ("achievement_1k_club".equals(achievement_id)) {
                    achievement = getString(R.string.achievement_1k_club);
                } else if ("achievement_fifth_wave".equals(achievement_id)) {
                    achievement = getString(R.string.achievement_fifth_wave);
                } else if ("achievement_7th_wave".equals(achievement_id)) {
                    achievement = getString(R.string.achievement_7th_wave);
                } else if ("achievement_5k_club".equals(achievement_id)) {
                    achievement = getString(R.string.achievement_5k_club);
                } else if ("achievement_10k_club".equals(achievement_id)) {
                    achievement = getString(R.string.achievement_10k_club);
                }
                Games.Achievements.unlock(gameHelper.getApiClient(), achievement);
            } else {
                if (Constants.DEBUG) {
                    Gdx.app.log(TAG, "Achievement not unlocked because user not signed in");
                }
            }
        }

        @Override
        public void submitScore(int highScore) {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "submitScore called: " + highScore);
            }
            if (gameHelper.isSignedIn()) {
                Games.Leaderboards.submitScore(gameHelper.getApiClient()
                        , getString(R.string.leaderboard_most_awesome_players)
                        , highScore);
            } else {
                if (Constants.DEBUG) {
                    Gdx.app.log(TAG, "Could not submit score because user is not signed in");
                }
            }
        }

        @Override
        public void showAchievements() {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "showAchievements called");
            }
            if (gameHelper.isSignedIn()) {
                startActivityForResult(Games.Achievements
                        .getAchievementsIntent(gameHelper.getApiClient()), REQUEST_ACHIEVEMENTS);
            } else {
                Gdx.app.log(TAG, "Attempt to access achievements while not connected/signed in");
            }
        }

        @Override
        public void showScore() {
            if (Constants.DEBUG) {
                Gdx.app.log(TAG, "showScore called");
            }
            if (gameHelper.getApiClient().isConnected() && gameHelper.isSignedIn()) {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient()
                        , getString(R.string.leaderboard_most_awesome_players))
                        , REQUEST_CODE);
            } else {
                Gdx.app.log(TAG, "Attempt to access leaderboard while not connected/signed in");
            }
        }

        @Override
        public boolean isSignedIn() {
            return false;
        }
    }
}
