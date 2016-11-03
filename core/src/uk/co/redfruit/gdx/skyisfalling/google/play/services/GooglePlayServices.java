package uk.co.redfruit.gdx.skyisfalling.google.play.services;

/**
 * Created by paul on 31/10/16.
 */

public interface GooglePlayServices {

    void signIn();

    void signOut();

    void unlockAchievement(String achievement);

    void submitScore(int highScore);

    //public void showAchievement();
    void showScore();

    boolean isSignedIn();

}
