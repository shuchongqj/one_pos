package com.gzdb.screen.present;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.screen.BasePresentation;
import com.gzdb.screen.player.IMPlayListener;
import com.gzdb.screen.player.IMPlayer;
import com.gzdb.screen.player.MPlayer;
import com.gzdb.screen.player.MPlayerException;
import com.gzdb.screen.player.MinimalDisplay;
import com.gzdb.screen.view.ImgHeadTwoView;
import com.gzdb.screen.view.ImgStatementTwoView;
import com.gzdb.screen.view.ImgStatementTwoView1;
import com.gzdb.screen.view.ImgTextAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by highsixty on 2018/3/7.
 * mail  gaolulin@sunmi.com
 */

public class VideoMenuDisplay extends BasePresentation {

    private SurfaceView mPlayerView;
    private MPlayer player;
    private final String TAG = "SUNMI";
    private String path;
    private FrameLayout container;

    private LinearLayout llyRight;
    private TextView mTitle;
    private ImgHeadTwoView mImgHeadviewTw0;
    private ListView mLv;
    private ImgTextAdapter mImgTextAdapter;
    private ImgStatementTwoView isv;
    private ImgStatementTwoView1 isv1;
    private ArrayList<ArrayList<String>> listData = new ArrayList<ArrayList<String>>();
    private String jsonStr = "";


    public VideoMenuDisplay(Context context, Display display, String path) {
        super(context, display);
        this.path = path;
        Log.d(TAG, "VideoDisplay: ------------>" + path);
        File file = new File(path);
        Log.d(TAG, "VideoDisplay: --------->" + file.exists());
    }

    public void update(String json) {
        this.jsonStr = json;
        initData(json);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vice_video_menu_layout);
        initView();
        initPlayer();
        try {
            player.setSource(path, 0);
            player.onResume();
        } catch (MPlayerException e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: ---------->" + e.getMessage());
        }

    }

    private void initView() {
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
        llyRight = (LinearLayout) findViewById(R.id.lly_right);
        mTitle = (TextView) findViewById(R.id.title);
        mImgHeadviewTw0 = (ImgHeadTwoView) findViewById(R.id.imgheadview);
        mLv = (ListView) findViewById(R.id.lv);
        mImgTextAdapter = new ImgTextAdapter(this.getContext(), listData);
        mLv.setAdapter(mImgTextAdapter);
        isv = (ImgStatementTwoView) findViewById(R.id.isv);
        isv1 = (ImgStatementTwoView1) findViewById(R.id.isv1);
    }

    private void initData(String jsonStr) {
        Log.d(TAG, "initData: ----------->" + jsonStr);
        try {
            JSONObject json = new JSONObject(jsonStr);
            String title = json.getString("title");
            setTitle(title);
            JSONObject head = json.getJSONObject("head");
            setHeadview(head);
            JSONArray lists = json.getJSONArray("list");
            setlistView(lists);
            JSONArray statement = json.getJSONArray("KVPList");
            setSMView(statement);
        } catch (Exception e) {
            e.printStackTrace();
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

    private void setHeadview(JSONObject json) {
        List<String> valueLists = getValueListByJsonObject(json);
        mImgHeadviewTw0.refreshView(valueLists);
    }

    /**
     * 动态解析jsonObject获取值列表
     *
     * @param json
     * @return
     */
    private ArrayList<String> getValueListByJsonObject(JSONObject json) {
        ArrayList<String> valueLists = new ArrayList<String>();
        try {
            Iterator it = json.keys();
            while (it.hasNext()) {
                String value = json.getString(it.next().toString());
                valueLists.add(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueLists;
    }

    /**
     * 设置表内容
     *
     * @param jsonArray
     */
    private void setlistView(JSONArray jsonArray) {

        listData.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ArrayList<String> list = getValueListByJsonObject(json);
                listData.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mImgTextAdapter.notifyDataSetChanged(listData);
        mLv.setSelection(listData.size());
        mLv.smoothScrollToPosition(listData.size());
    }

    /**
     * 设置结算
     */
    private void setSMView(JSONArray statement) {
        float scale = this.getResources().getDisplayMetrics().density;
        int width = (int) (448 * scale + 0.5f);
        isv.refreshView(statement, width);
        isv.setVisibility(View.VISIBLE);
        isv1.setVisibility(View.GONE);
    }
}

