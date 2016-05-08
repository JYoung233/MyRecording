package com.yangyang.myrecording.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by asus on 2016/5/8.
 * 自定义录音按钮，有三种状态
 */
public class MyRecordingButton extends Button{
    private static final int STATE_NORMAL=1;
    private static final int STATE_RECORDING=2;
    private static final int STATE_WANTCANCEL=3;
    private int mCueState=STATE_NORMAL;
    public boolean isRecording=false;

    public MyRecordingButton(Context context) {
        this(context, null);
    }

    public MyRecordingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
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
                if(mCueState==STATE_RECORDING){

                }else if(mCueState==STATE_WANTCANCEL){

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
        isRecording=false;
        ChangeState(STATE_NORMAL);
    }

    private boolean wantCancel(int x, int y) {
        return false;
    }

    private void ChangeState(int stateRecording) {

    }


}
