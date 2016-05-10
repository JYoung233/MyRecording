package com.yangyang.myrecording.view;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangyang.myrecording.R;

/**
 * Created by asus on 2016/5/8.
 * 用于管理Dialog的变化
 */
public class DialogManager {
    private Dialog mDialog;
    private ImageView mRecorder;
    private ImageView mVoice;
    private TextView mLabel;
    private Context context;

    public DialogManager(Context context) {
        this.context = context;
    }
    public void showRecordingDialog(){
        mDialog=new Dialog(context, R.style.Theme_AudioDialog);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dialog_recorder,null);
        mDialog.setContentView(view);
        mLabel = (TextView) mDialog.findViewById(R.id.id_label);
        mRecorder= (ImageView) mDialog.findViewById(R.id.id_recorder);
        mVoice= (ImageView) mDialog.findViewById(R.id.id_voice);
        mDialog.show();

        //展示默认的对话框
    }
    public void Recording(){
        if(mDialog!=null&&mDialog.isShowing()){
            mLabel.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mRecorder.setVisibility(View.VISIBLE);
            mVoice.setImageResource(R.mipmap.v1);
            mRecorder.setImageResource(R.mipmap.recorder);
            mLabel.setText(R.string.hint_recording);
        }
    }
    public void wantToCancel(){
        //取消录音
        if(mDialog!=null&&mDialog.isShowing()){
            mLabel.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mRecorder.setVisibility(View.VISIBLE);
            mRecorder.setImageResource(R.mipmap.cancel);
            mLabel.setText(R.string.hint_cancel);
        }
    }
    public void TooShort(){
        //时间太短
        if(mDialog!=null&&mDialog.isShowing()){
            mLabel.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mRecorder.setVisibility(View.VISIBLE);
            mRecorder.setImageResource(R.mipmap.voice_to_short);
            mLabel.setText(R.string.hint_timeshort);
        }
    }
    public void dismissDialog(){
        //隐藏对话框
        if(mDialog!=null&&mDialog.isShowing()){
            mDialog.dismiss();
            mDialog=null;
        }
    }

    /**
     * 更新图片，level为1——7
     * @param level
     */
    public void UpdateVoiceLevel(int level){
        //更新声音
        if(mDialog!=null&&mDialog.isShowing()){
//            mLabel.setVisibility(View.VISIBLE);
//            mVoice.setVisibility(View.VISIBLE);
//            mRecorder.setVisibility(View.VISIBLE);
            Log.v("infor","音量改变"+level);
            int resID=context.getResources().getIdentifier("v"+level,"mipmap",context.getPackageName());
            mVoice.setImageResource(resID);
        }
    }
}
