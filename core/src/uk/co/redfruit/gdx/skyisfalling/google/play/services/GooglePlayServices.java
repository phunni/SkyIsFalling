package uk.co.redfruit.gdx.skyisfalling.google.play.services;

/**
 * Created by paul on 31/10/16.
 */

public interface GooglePlayServices {

    public void signIn();
    public void signOut();
   // public void unlockAchievement();
   // public void submitScore(int highScore);
   // public void showAchievement();
   // public void showScore();
    public boolean isSignedIn();

}
