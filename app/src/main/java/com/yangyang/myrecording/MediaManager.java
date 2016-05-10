package com.yangyang.myrecording;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by asus on 2016/5/10.
 *控制播放录音
 */
public class MediaManager {
    private static MediaPlayer mediaplay;
    private static boolean ispause=false;


    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
        if (mediaplay == null) {
            mediaplay = new MediaPlayer();
            mediaplay.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mediaplay.reset();
                    return false;
                }
            });
        } else {
            mediaplay.reset();
        }
        try {
            mediaplay.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaplay.setOnCompletionListener(onCompletionListener);
            mediaplay.setDataSource(filePath);
            mediaplay.prepare();
            mediaplay.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void Pause(){
        if(mediaplay!=null&&mediaplay.isPlaying()){
            mediaplay.pause();
            ispause=true;
        }
    }
    public static void Resume(){
        if(mediaplay!=null&&ispause){
            mediaplay.start();
            ispause=false;
        }

    }
    public static void Release(){
        if(mediaplay!=null){
            mediaplay.release();
            mediaplay=null;
        }
    }
}
