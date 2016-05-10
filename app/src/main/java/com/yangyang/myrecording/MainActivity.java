package com.yangyang.myrecording;

import android.graphics.drawable.AnimationDrawable;
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

    private MyRecordingButton myRecordingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView= (ListView) findViewById(R.id.list1);
        myRecordingButton= (MyRecordingButton) findViewById(R.id.recorder_button);
        myRecordingButton.setOnAudioFinishListener(new MyRecordingButton.AudioFinishListener() {
            @Override
            public void onFinish(float seconds, String filepath) {
                Recorder recorder=new Recorder(filepath,seconds);
                mDatas.add(recorder);
                mArrayAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size()-1);

            }
        });
        mArrayAdapter=new MyAdapter(this,mDatas);
        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //播放动画
                View animView=view.findViewById(R.id.recorder_anim);
                animView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable am= (AnimationDrawable) animView.getBackground();
                am.start();

                //播放音频

            }
        });


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
