package com.gzdb.sunmi.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gzdb.basepos.R;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.sunmi.data.DataModel;
import com.gzdb.sunmi.data.UPacketFactory;
import com.gzdb.sunmi.util.SharedPreferencesSunmiUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import sunmi.ds.DSKernel;
import sunmi.ds.callback.ICheckFileCallback;
import sunmi.ds.callback.ISendFilesCallback;

/**
 * $desc
 * @author    Zxy
 *
 *		发送图片与文字的Activity
 */

public class SendImgTextAct extends Activity implements View.OnClickListener{

	DSKernel mDSKernel;
	Button send_img;//发送选择的图片.
	ImageView sendImgsText; //选择图片
	EditText edit; //图片轮换间隔时间

	RelativeLayout ryOne,ryTwo,ryThree,ryFour,ryFive;
	ImageView deImgOne,deImgTwo,deImgThree,deImgFour,deImgFive,imgOne,imgTwo,imgThree,imgFour,imgFive,closeBtn;

	public long taskId_sendImgsText;

	private static final String IMG_TEXT_ID = "IMG_TEXT_ID";
	private static final String IMGS_ID = "IMG_TEXT_ID";

	List<String> imgsPath = new ArrayList<>();
	List<ImageView> imgUi = new ArrayList<>();
	List<RelativeLayout> rlys = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.banner_view);
		initView();
		mDSKernel = Sunmi.getDSKernel();
		checkFile();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(imgsPath.size() <= 0){
			sendImgsText.setVisibility(View.VISIBLE);
		}
	}

	private void checkFile(){
		long fileId = SharedPreferencesSunmiUtil.getLong(SendImgTextAct.this, IMG_TEXT_ID);
		long fileId1 = SharedPreferencesSunmiUtil.getLong(SendImgTextAct.this, IMGS_ID);
		if(fileId!=-1L)
			checkImg(fileId, IMG_TEXT_ID);
		if(fileId1!=-1L) {
			checkImg(fileId1, IMGS_ID);
		}
	}

	private void checkImg(long fileId, final String key){

		mDSKernel.checkFileExist(fileId, new ICheckFileCallback() {
			@Override
			public void onCheckFail() {
//				Toast.makeText(SendImgTextAct.this, key + " 文件不存在", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onResult(boolean b) {
				if(!b){
					SharedPreferencesSunmiUtil.put(SendImgTextAct.this, key, -1L);
				}else{
//					Toast.makeText(SendImgTextAct.this, key + " 文件存在", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	private void initView() {
		edit = (EditText)findViewById(R.id.edit);
		send_img = (Button) findViewById(R.id.send_img);
		send_img.setVisibility(View.GONE);
		sendImgsText = (ImageView) findViewById(R.id.send2);

		ryOne = (RelativeLayout) this.findViewById(R.id.ry_one);
		ryTwo = (RelativeLayout) this.findViewById(R.id.ry_two);
		ryThree = (RelativeLayout) this.findViewById(R.id.ry_three);
		ryFour = (RelativeLayout) this.findViewById(R.id.ry_four);
		ryFive = (RelativeLayout) this.findViewById(R.id.ry_five);

		deImgOne = (ImageView) this.findViewById(R.id.de_img_one);
		deImgTwo = (ImageView) this.findViewById(R.id.de_img_two);
		deImgThree = (ImageView) this.findViewById(R.id.de_img_three);
		deImgFour = (ImageView) this.findViewById(R.id.de_img_four);
		deImgFive = (ImageView) this.findViewById(R.id.de_img_five);

		imgOne = (ImageView) this.findViewById(R.id.img_one);
		imgTwo = (ImageView) this.findViewById(R.id.img_two);
		imgThree = (ImageView) this.findViewById(R.id.img_three);
		imgFour = (ImageView) this.findViewById(R.id.img_four);
		imgFive = (ImageView) this.findViewById(R.id.img_five);

		closeBtn = (ImageView) this.findViewById(R.id.close_btn);

		sendImgsText.setOnClickListener(this);
		send_img.setOnClickListener(this);

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

		deImgOne.setOnClickListener(this);
		deImgTwo.setOnClickListener(this);
		deImgThree.setOnClickListener(this);
		deImgFour.setOnClickListener(this);
		deImgFive.setOnClickListener(this);
		closeBtn.setOnClickListener(this);

	}

	private void send(){//发送图片


		JSONObject json = new JSONObject();
		if(!TextUtils.isEmpty(edit.getText().toString().trim())){
			try {
				json.put("rotation_time", Integer.parseInt(edit.getText().toString())*1000);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		for(String path:imgsPath){
			File file = new File(path);
			if(!file.exists()){
				Toast.makeText(getApplicationContext(),path+ "目录文件不存在", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		taskId_sendImgsText = mDSKernel.sendFiles(DSKernel.getDSDPackageName(), json.toString(), imgsPath, new ISendFilesCallback() {
			@Override
			public void onAllSendSuccess(long fileId) {
				showImgs();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {//所有图片发送成功
//						Toast.makeText(SendImgTextAct.this," 所有图片发送成功!", Toast.LENGTH_LONG).show();

					}
				});
				SharedPreferencesSunmiUtil.put(getApplicationContext(),IMGS_ID,taskId_sendImgsText);
			}

			@Override
			public void onSendSuccess(final String s, final long l) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(SendImgTextAct.this," 发送成功!", Toast.LENGTH_LONG).show();
						closeRy();
						sendImgsText.setVisibility(View.VISIBLE);
					}
				});
			}

			@Override
			public void onSendFaile(final int i, final String s) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(SendImgTextAct.this," 发送失败!", Toast.LENGTH_LONG).show();
					}
				});
			}

			@Override
			public void onSendFileFaile(String s, int i, String s1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(SendImgTextAct.this," 发送文件失败!", Toast.LENGTH_LONG).show();
					}
				});
			}

			@Override
			public void onSendProcess(final String s, final long l, final long l1) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
					}
				});
			}
		});
	}


	private void image(){//选择图片
		GalleryFinal.openGalleryMuti(1, 50,new GalleryFinal.OnHanlderResultCallback() {
			@Override
			public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

				if (resultList.size() > 0) {
					if (resultList.size() > 5){
						Toast.makeText(SendImgTextAct.this,"您最多可以选择5张", Toast.LENGTH_LONG).show();
						for (int i = 5; i < resultList.size();i++){
							resultList.remove(resultList.get(i));
							i--;
						}
					}
					send_img.setVisibility(View.VISIBLE);
					imgsPath.clear();

					for (int i = 0; i< resultList.size();i++){
						imgsPath.add(resultList.get(i).getPhotoPath());
						File file = new File(resultList.get(i).getPhotoPath());
						if(file.exists()) {
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
	private void showImgUi(int as){
		closeRy();
		imgsPath.remove(imgsPath.get(as));
		if (imgsPath.size() <= 0){
			sendImgsText.setVisibility(View.VISIBLE);
			send_img.setVisibility(View.GONE);
		}
		for (int i = 0; i< imgUi.size();i++){

			if (i < imgsPath.size()){

				File file = new File(imgsPath.get(i));
				if(file.exists()) {
					//将路径转换成可以装到imgview
					Bitmap bm = BitmapFactory.decodeFile(imgsPath.get(i));
					imgUi.get(i).setImageBitmap(bm);
					rlys.get(i).setVisibility(View.VISIBLE);//装完显示
				}
			}
		}
	}
	private void closeRy(){
		ryOne.setVisibility(View.GONE);
		ryTwo.setVisibility(View.GONE);
		ryThree.setVisibility(View.GONE);
		ryFour.setVisibility(View.GONE);
		ryFive.setVisibility(View.GONE);
	}

	private void showImgs() {
		String json = UPacketFactory.createJson(DataModel.IMAGES,"");
		mDSKernel.sendCMD(DSKernel.getDSDPackageName(),json,taskId_sendImgsText,null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.send2://添加图片
				image();
				imgsPath.clear();
				sendImgsText.setVisibility(View.GONE);
			break;
			case R.id.send_img://保存
				send();
				this.finish();
				break;
			case R.id.de_img_one://删除图片
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
			case R.id.close_btn://关闭此界面
				closeKey();
				this.finish();
				break;
		}
	}

	public void closeKey(){
		/**隐藏软键盘**/
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
}

