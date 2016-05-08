package com.yangyang.myrecording.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.yangyang.myrecording.R;

/**
 * Created by asus on 2016/5/8.
 * 自定义录音按钮，有三种状态
 */
public class MyRecordingButton extends Button{
    private static final int DISTANCE_Y_CANCEL=50;//这里应该转换dp和px
    private static final int STATE_NORMAL=1;
    private static final int STATE_RECORDING=2;
    private static final int STATE_WANTCANCEL=3;
    private int mCueState=STATE_NORMAL;
    private boolean isRecording=false;
    private DialogManager mDialogManager;

    public MyRecordingButton(Context context) {
        this(context, null);
    }

    public MyRecordingButton(Context context, AttributeSet attrs) {

        super(context, attrs);
        mDialogManager=new DialogManager(getContext());

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //TODO 真正的显示应该在Audio准备就绪之后
                mDialogManager.showRecordingDialog();
                isRecording=true;
                return false;
            }
        });
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
                    mDialogManager.dismissDialog();
                }else if(mCueState==STATE_WANTCANCEL){
                    mDialogManager.dismissDialog();
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
