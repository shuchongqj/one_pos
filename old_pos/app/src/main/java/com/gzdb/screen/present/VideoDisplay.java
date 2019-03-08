package com.gzdb.screen.present;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.gzdb.basepos.R;
import com.gzdb.screen.BasePresentation;
import com.gzdb.screen.player.IMPlayListener;
import com.gzdb.screen.player.IMPlayer;
import com.gzdb.screen.player.MPlayer;
import com.gzdb.screen.player.MPlayerException;
import com.gzdb.screen.player.MinimalDisplay;

import java.io.File;

/**
 * Created by highsixty on 2018/3/7.
 * mail  gaolulin@sunmi.com
 */

public class VideoDisplay extends BasePresentation {

    private SurfaceView mPlayerView;
    private MPlayer player;
    private final String TAG = "SUNMI";
    private String path;
    private FrameLayout container;
    private Handler myHandle=new Handler(Looper.getMainLooper());

    public VideoDisplay(Context context, Display display, String path) {
        super(context, display);
        this.path = path;
        Log.d(TAG, "VideoDisplay: ------------>" + path);
        File file = new File(path);
        Log.d(TAG, "VideoDisplay: --------->" + file.exists());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vice_video_layout);
        mPlayerView = (SurfaceView) findViewById(R.id.mPlayerView);
        container = (FrameLayout) findViewById(R.id.playerContainer);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != player) {
                    if (player.isPlaying()) {
                        player.onPause();
                    } else {
                        player.onResume();
                    }
                }
            }
        });
        initPlayer();
        Log.d(TAG, "onCreate: ------------>" + (player == null));
        try {
            player.setSource(path, 0);
            player.onResume();
        } catch (MPlayerException e) {
            e.printStackTrace();
            myHandle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        player.setSource(path, 0);
                        player.onResume();
                    } catch (MPlayerException e1) {
                        e1.printStackTrace();
                    }
                }
            },3000);
            Log.d(TAG, "onCreate: ---------->" + e.getMessage());
        }
    }

    private void initPlayer() {
        player = new MPlayer();
        player.setDisplay(new MinimalDisplay(mPlayerView));
        player.setPlayListener(new IMPlayListener() {
            @Override
            public void onStart(IMPlayer player) {

            }

            @Override
            public void onPause(IMPlayer player) {

            }

            @Override
            public void onResume(IMPlayer player) {

            }

            @Override
            public void onComplete(IMPlayer player) {
                try {
                    player.setSource(path, 0);
                } catch (MPlayerException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    protected void onStop() {
        super.onStop();
//        player.onPause();
    }

    @Override
    public void onDisplayRemoved() {
        super.onDisplayRemoved();
//        player.onDestroy();
    }
}

