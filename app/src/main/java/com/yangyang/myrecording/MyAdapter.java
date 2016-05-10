package com.yangyang.myrecording;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/5/10.
 * ListView的适配器
 */
public class MyAdapter extends ArrayAdapter<MainActivity.Recorder> {

    private int minItemWidth;
    private int maxItemWidth;

    private LayoutInflater mInflater;
    public MyAdapter(Context context, List<MainActivity.Recorder> datas) {
        super(context,-1,datas);

        WindowManager wm= (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        minItemWidth= (int) (dm.widthPixels*0.7f);
        maxItemWidth= (int) (dm.widthPixels*0.15f);

        mInflater=LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder=null;
        if(convertView==null){
            mInflater.inflate(R.layout.item_recorder,parent,false);
            holder=new MyViewHolder();
            holder.length=convertView.findViewById(R.id.recorder_length);
            holder.tx= (TextView) convertView.findViewById(R.id.recorder_time);

            convertView.setTag(holder);
        }else{
            holder= (MyViewHolder) convertView.getTag();
        }
        holder.tx.setText(Math.round(getItemId(position))+"\"");
        ViewGroup.LayoutParams lp=holder.length.getLayoutParams();
        lp.width= (int) (minItemWidth+(maxItemWidth/60f)*getItem(position).time);
        //最长录制时间为60秒
        return convertView;
    }
    private class MyViewHolder{
        TextView tx;
        View length;


    }
}
