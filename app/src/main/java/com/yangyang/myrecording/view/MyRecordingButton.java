package com.yangyang.myrecording.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.yangyang.myrecording.R;

/**
 * Created by asus on 2016/5/8.
 * 自定义录音按钮，有三种状态
 */
public class MyRecordingButton extends Button implements AudioManager.AudioStateListener {
    private static final int DISTANCE_Y_CANCEL=50;//这里应该转换dp和px
    private static final int STATE_NORMAL=1;
    private static final int STATE_RECORDING=2;
    private static final int STATE_WANTCANCEL=3;
    private int mCueState=STATE_NORMAL;
    private boolean isRecording=false;
    private DialogManager mDialogManager;
    private AudioManager audioManager;
    private float mTime=0;//用于记录录音的时长
    private boolean mReady;//是否长按

    public MyRecordingButton(Context context) {
        this(context, null);
    }

    public MyRecordingButton(Context context, AttributeSet attrs) {

        super(context, attrs);
        mDialogManager=new DialogManager(getContext());

            String file = Environment.getExternalStorageDirectory() + "/yangyang";
            audioManager=AudioManager.getInstance(file);
            audioManager.SetOnAudioState(this);





        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mReady=true;
                audioManager.prepareAudio();
                return false;
            }
        });
    }
    public interface AudioFinishListener{
        void onFinish(float seconds,String filepath);
    }
    private AudioFinishListener mListener;
    public void setOnAudioFinishListener(AudioFinishListener listener){
        mListener=listener;
    }

    private Runnable mGetVoiceRunnable=new Runnable(){

        @Override
        public void run() {
            while(isRecording){
                try {
                    Thread.sleep(100);
                    mTime+=0.1f;//每0.1秒都会启动线程
                    myHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };
    private static final int MSG_AUDIO_PREPARED=0X110;//这里只需要是不冲突的常量即可
    private static final int MSG_VOICE_CHANGE=0X111;
    private static final int MSG_DIALOG_DIMISS=0X112;

    private Handler myHandler=new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case MSG_AUDIO_PREPARED:
                    isRecording = true;
                    new Thread(mGetVoiceRunnable).start();
                    mDialogManager.showRecordingDialog();


                    break;
                case MSG_VOICE_CHANGE:
                    mDialogManager.UpdateVoiceLevel(audioManager.getVoiceLevel(7));
                    break;
                case MSG_DIALOG_DIMISS:
                    mDialogManager.dismissDialog();
                    break;
                default:break;
            }
        }
    };


    @Override
    public void WellPrepared() {
           myHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        int x = (int) event.getX();
        int y= (int) event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                ChangeState(STATE_RECORDING);
                break;

            case MotionEvent.ACTION_MOVE:

                if(isRecording){
                    if(wantCancel(x,y)){
                        ChangeState(STATE_WANTCANCEL);
                    }else{
                        ChangeState(STATE_RECORDING);
                    }

                }
                break;

            case MotionEvent.ACTION_UP:
                if(!mReady){
                    reset();
                    return super.onTouchEvent(event);
                }
                if(!isRecording||mTime<0.6f){//prepare没有完成已经up
                    mDialogManager.TooShort();
                    audioManager.cancel();
                    myHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS,1300);//延迟显示1.3秒

                }else if(mCueState==STATE_RECORDING) {//正常录制的情况
                    mDialogManager.dismissDialog();
                    audioManager.release();
                    if (mListener != null) {
                        mListener.onFinish(mTime, audioManager.getCurrentFilePath());
                    }



                }else if(mCueState==STATE_WANTCANCEL){
                    mDialogManager.dismissDialog();
                    audioManager.cancel();
                }
                reset();
                break;
            default:break;

        }


        return super.onTouchEvent(event);
    }

    /**
     * 恢复标志位
     */
    private void reset() {

        mReady=false;
        isRecording=false;
        mTime=0;
        ChangeState(STATE_NORMAL);
    }

    private boolean wantCancel(int x, int y) {
        if(x<0||x>getWidth()){
            //超出button的范围
            return true;
        }
        if(y<-DISTANCE_Y_CANCEL||y>getHeight()+DISTANCE_Y_CANCEL){
            return true;
        }
        return false;
    }

    private void ChangeState(int state) {
        if(mCueState!=state){
            mCueState=state;
            switch (state){
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_normal);
                    setText(R.string.str_btn_recording);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_btn_send);
                    if(isRecording){
                       mDialogManager.Recording();
                    }
                    break;
                case STATE_WANTCANCEL:
                    setBackgroundResource(R.drawable.btn_recording);
                    setText(R.string.str_btn_cancel);
                    mDialogManager.wantToCancel();
                    break;

            }
        }

    }



}
