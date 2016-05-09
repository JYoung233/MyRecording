package com.yangyang.myrecording.view;

import android.media.MediaRecorder;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by asus on 2016/5/9.
 * 控制录音
 */
public class AudioManager {
    private MediaRecorder mediaRecorder;
    private String mDir;
    private String mCurrentFilePath;
    private boolean isprepared;
    //使用单例模式小区不一致问题
    private static AudioManager mInstance;
    private AudioManager(String file){
        mDir=file;
    }//构造方法

    /**
     * 提供准备就绪的回调接口
     */
    public interface AudioStateListener{
        void WellPrepared();
    }
    public AudioStateListener mListener;
    public void SetOnAudioState(AudioStateListener listener){
        mListener=listener;
    }
    /**
     * 静态工厂方法
     */
    public static AudioManager getInstance(String file){
        if(mInstance==null){
            synchronized (AudioManager.class){//保证线程安全
                if(mInstance==null){
                    mInstance=new AudioManager(file);
                }
            }
        }
        return mInstance;

    }
    public void prepareAudio(){
        try {
            isprepared=false;
            File dir=new File(mDir);
            if(!dir.exists()) dir.mkdirs();
            String FileName=generateFileName();

            File file=new File(dir,FileName);//根据路径和文件名创建文件
            mCurrentFilePath=file.getAbsolutePath();
            mediaRecorder=new MediaRecorder();
            //设置输出路径
            mediaRecorder.setOutputFile(file.getAbsolutePath());
            //设置音频源为麦克风
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            //设置音频编码
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isprepared=true;
            if(mListener!=null){
                mListener.WellPrepared();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    /**
     * 随机生成文件名称
     * @return
     */
    private String generateFileName() {
        return UUID.randomUUID().toString()+".amr";
    }

    /**
     * mediaRecorder.getMaxAmplitude()取值范围在1——32767
     * @param level 最高等级
     * @return
     */
    public int getVoiceLevel(int level){
        if(isprepared){
            try {//捕获异常，保证获取的信息不会挂掉
                return mediaRecorder.getMaxAmplitude()/32768+1;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        }
        return 1;
    }
    public void release(){
        mediaRecorder.release();
        mediaRecorder.stop();
        mediaRecorder=null;

    }

    /**
     * 释放资源+删除保存的音频文件
     */
    public void cancel(){
       release();
        if(mCurrentFilePath!=null) {
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath=null;
        }

    }



}
