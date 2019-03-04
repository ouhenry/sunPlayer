package com.exmart.qiyishow.fenster.play;

public interface FensterVideoStateListener {

    void onFirstVideoFrameRendered();

    void onPlay();

    void onBuffer();

    boolean onStopWithExternalError(int position);

}
