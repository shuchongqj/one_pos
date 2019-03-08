package com.gzdb.supermarket.fragment;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzdb.basepos.R;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.sunmi.data.DataModel;
import com.gzdb.sunmi.data.UPacketFactory;
import com.gzdb.sunmi.util.SharedPreferencesSunmiUtil;
import com.gzdb.supermarket.util.DialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import sunmi.ds.DSKernel;
import sunmi.ds.callback.ICheckFileCallback;
import sunmi.ds.callback.ISendFilesCallback;

/**
 * A simple {@link Fragment} subclass.
 */
public class BannerFragment extends Fragment {


    @Bind(R.id.aa)
    TextView aa;
    @Bind(R.id.close_btn)
    ImageView closeBtn;
    @Bind(R.id.edit)
    EditText edit;
    @Bind(R.id.send2)
    ImageView send2;
    @Bind(R.id.img_one)
    ImageView imgOne;
    @Bind(R.id.de_img_one)
    ImageView deImgOne;
    @Bind(R.id.ry_one)
    RelativeLayout ryOne;
    @Bind(R.id.img_two)
    ImageView imgTwo;
    @Bind(R.id.de_img_two)
    ImageView deImgTwo;
    @Bind(R.id.ry_two)
    RelativeLayout ryTwo;
    @Bind(R.id.img_three)
    ImageView imgThree;
    @Bind(R.id.de_img_three)
    ImageView deImgThree;
    @Bind(R.id.ry_three)
    RelativeLayout ryThree;
    @Bind(R.id.img_four)
    ImageView imgFour;
    @Bind(R.id.de_img_four)
    ImageView deImgFour;
    @Bind(R.id.ry_four)
    RelativeLayout ryFour;
    @Bind(R.id.img_five)
    ImageView imgFive;
    @Bind(R.id.de_img_five)
    ImageView deImgFive;
    @Bind(R.id.ry_five)
    RelativeLayout ryFive;
    @Bind(R.id.send_img)
    Button sendImg;

    private Dialog dialog;

    DSKernel mDSKernel;

    public static final String SCROLL_IMGS_IDS = "SIMGS";

    List<String> imgsPath = new ArrayList<>();
    List<ImageView> imgUi = new ArrayList<>();
    List<RelativeLayout> rlys = new ArrayList<>();

    public long taskId_sendImgsText;

    public BannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        ButterKnife.bind(this, view);
        mDSKernel = Sunmi.getDSKernel();
        dialog = DialogUtil.loadingDialog(getContext(), "正在处理，请稍候...");
        initView();
        return view;
    }

    void initView() {
        sendImg.setVisibility(View.GONE);
        imgUi.add(imgOne);
        imgUi.add(imgTwo);
        imgUi.add(imgThree);
        imgUi.add(imgFour);
        imgUi.add(imgFive);
        rlys.add(ryOne);
        rlys.add(ryTwo);
        rlys.add(ryThree);
        rlys.add(ryFour);
        rlys.add(ryFive);

        checkFile();
    }

    private void checkFile() {
        long fileId = SharedPreferencesSunmiUtil.getLong(getContext(), SCROLL_IMGS_IDS);
        if (fileId != -1)
            checkImg(fileId, SCROLL_IMGS_IDS);
    }

    private void checkImg(long fileId, final String key) {
        mDSKernel.checkFileExist(fileId, new ICheckFileCallback() {
            @Override
            public void onCheckFail() {
//				Toast.makeText(SendImgTextAct.this, key + " 文件不存在", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResult(boolean b) {
                if (!b) {
                    SharedPreferencesSunmiUtil.put(getContext(), key, -1);
                } else {
//					Toast.makeText(SendImgTextAct.this, key + " 文件存在", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void send() {//发送图片


        JSONObject json = new JSONObject();
        if (!TextUtils.isEmpty(edit.getText().toString().trim())) {
            try {
                json.put("rotation_time", Integer.parseInt(edit.getText().toString()) * 1000);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (String path : imgsPath) {
            File file = new File(path);
            if (!file.exists()) {
                Toast.makeText(getContext(), path + "目录文件不存在", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        taskId_sendImgsText = mDSKernel.sendFiles(DSKernel.getDSDPackageName(), json.toString(), imgsPath, new ISendFilesCallback() {
            @Override
            public void onAllSendSuccess(long fileId) {
                showImgs();
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {//所有图片发送成功
//						Toast.makeText(SendImgTextAct.this," 所有图片发送成功!", Toast.LENGTH_LONG).show();

                    }
                });
                SharedPreferencesSunmiUtil.put(getContext(), SCROLL_IMGS_IDS, taskId_sendImgsText);
            }

            @Override
            public void onSendSuccess(final String s, final long l) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getContext(), " 发送成功!", Toast.LENGTH_LONG).show();
//                        closeRy();
//                        send2.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onSendFaile(final int i, final String s) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getContext(), " 发送失败!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onSendFileFaile(String s, int i, String s1) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getContext(), " 发送文件失败!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onSendProcess(final String s, final long l, final long l1) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                    }
                });
            }
        });
    }


    private void image() {//选择图片
        GalleryFinal.openGalleryMuti(1, 50, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

                if (resultList.size() > 0) {
                    if (resultList.size() > 5) {
                        Toast.makeText(getContext(), "您最多可以选择5张", Toast.LENGTH_LONG).show();
                        for (int i = 5; i < resultList.size(); i++) {
                            resultList.remove(resultList.get(i));
                            i--;
                        }
                    }
                    sendImg.setVisibility(View.VISIBLE);
                    imgsPath.clear();

                    for (int i = 0; i < resultList.size(); i++) {
                        imgsPath.add(resultList.get(i).getPhotoPath());
                        File file = new File(resultList.get(i).getPhotoPath());
                        if (file.exists()) {
                            //将路径转换成可以装到imgview
                            Bitmap bm = BitmapFactory.decodeFile(resultList.get(i).getPhotoPath());
                            imgUi.get(i).setImageBitmap(bm);
                            rlys.get(i).setVisibility(View.VISIBLE);//装完显示
                        }
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
            }
        });
    }

    //刷新ui
    private void showImgUi(int as) {
        closeRy();
        imgsPath.remove(imgsPath.get(as));
        if (imgsPath.size() <= 0) {
            send2.setVisibility(View.VISIBLE);
            sendImg.setVisibility(View.GONE);
        }
        for (int i = 0; i < imgUi.size(); i++) {

            if (i < imgsPath.size()) {

                File file = new File(imgsPath.get(i));
                if (file.exists()) {
                    //将路径转换成可以装到imgview
                    Bitmap bm = BitmapFactory.decodeFile(imgsPath.get(i));
                    imgUi.get(i).setImageBitmap(bm);
                    rlys.get(i).setVisibility(View.VISIBLE);//装完显示
                }
            }
        }
    }

    private void closeRy() {
        ryOne.setVisibility(View.GONE);
        ryTwo.setVisibility(View.GONE);
        ryThree.setVisibility(View.GONE);
        ryFour.setVisibility(View.GONE);
        ryFive.setVisibility(View.GONE);
    }

    private void showImgs() {
        String json = UPacketFactory.createJson(DataModel.IMAGES, "");
        mDSKernel.sendCMD(DSKernel.getDSDPackageName(), json, taskId_sendImgsText, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.de_img_one, R.id.de_img_two, R.id.de_img_three, R.id.de_img_four, R.id.de_img_five, R.id.send2, R.id.send_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.de_img_one:
                showImgUi(0);
                break;
            case R.id.de_img_two:
                showImgUi(1);
                break;
            case R.id.de_img_three:
                showImgUi(2);
                break;
            case R.id.de_img_four:
                showImgUi(3);
                break;
            case R.id.de_img_five:
                showImgUi(4);
                break;
            case R.id.send2:
                image();
                imgsPath.clear();
                send2.setVisibility(View.GONE);
                break;
            case R.id.send_img:
                send();
                break;
        }
    }
}
