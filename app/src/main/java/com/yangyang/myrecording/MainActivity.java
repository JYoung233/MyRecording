package com.yangyang.myrecording;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ViewAnimator;

import com.yangyang.myrecording.view.MyRecordingButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private MyAdapter mArrayAdapter;
    private List<Recorder> mDatas=new ArrayList<Recorder>();
    private View animView;

    private MyRecordingButton myRecordingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView= (ListView) findViewById(R.id.list1);
        myRecordingButton= (MyRecordingButton) findViewById(R.id.recorder_button);
        mArrayAdapter=new MyAdapter(this,mDatas);
        myRecordingButton.setOnAudioFinishListener(new MyRecordingButton.AudioFinishListener() {
            @Override
            public void onFinish(float seconds, String filepath) {
                Recorder recorder=new Recorder(filepath,seconds);
                mDatas.add(recorder);
                mArrayAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size()-1);

            }
        });

        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //播放动画
                if(animView!=null){
                    animView.setBackgroundResource(R.mipmap.adj);
                    animView=null;
                }
                animView=view.findViewById(R.id.recorder_anim);
                animView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable am= (AnimationDrawable) animView.getBackground();
                am.start();

                //播放音频
                MediaManager.playSound(mDatas.get(position).filePath, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        animView.setBackgroundResource(R.mipmap.adj);
                    }
                });

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.Pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.Resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.Release();
    }

    class Recorder{
        float time;
        String filePath;

        public Recorder(String filePath, float time) {
            super();
            this.filePath = filePath;
            this.time = time;
        }

        public float getTime() {
            return time;
        }

        public void setTime(float time) {
            this.time = time;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}
