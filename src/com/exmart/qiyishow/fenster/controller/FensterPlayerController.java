package com.exmart.qiyishow.fenster.controller;

import com.exmart.qiyishow.fenster.play.FensterPlayer;


public interface FensterPlayerController {

    void setMediaPlayer(FensterPlayer fensterPlayer);

    void setEnabled(boolean value);

    void show(int timeInMilliSeconds);

    void show();

    void hide();

    void setVisibilityListener(FensterPlayerControllerVisibilityListener visibilityListener);

}
