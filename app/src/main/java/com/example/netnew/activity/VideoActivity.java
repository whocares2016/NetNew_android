package com.example.netnew.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netnew.R;

import java.util.Random;

public class VideoActivity extends AppCompatActivity {
    private VideoView mVideo;
    public static  int[] lj = {R.raw.yue};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mVideo = (VideoView) findViewById(R.id.video);
        mVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + lj[new Random().nextInt(lj.length)]));
        MediaController controller = new MediaController(this);
        mVideo.setMediaController(controller);
        mVideo.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void back(View view){
        finish();
    }
}