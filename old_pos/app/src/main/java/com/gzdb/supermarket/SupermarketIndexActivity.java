package com.gzdb.supermarket;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.SynthesizerTool;
import com.baidu.tts.client.TtsMode;
import com.core.http.Http;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.util.QRCodeUtil;
import com.core.util.SaveImgUtils;
import com.core.util.ShareCodeUtils;
import com.core.util.ToastUtil;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.google.gson.Gson;
import com.gzdb.basepos.App;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.BuildConfig;
import com.gzdb.basepos.R;
import com.gzdb.basepos.greendao.FinishOrderDataDao;
import com.gzdb.basepos.greendao.GoodBeanDao;
import com.gzdb.basepos.greendao.ItemSnapshotsBeanDao;
import com.gzdb.fresh.activity.FreshActivity;
import com.gzdb.fresh.bean.OrderNumber;
import com.gzdb.mall.activity.MallActivity;
import com.gzdb.printer.GpUsbPrint;
import com.gzdb.printer.PrintUtils;
import com.gzdb.sale.bean.GroupSale;
import com.gzdb.sale.bean.SaleType3;
import com.gzdb.sale.event.RefreshSaleEvent;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.screen.ScreenManager;
import com.gzdb.screen.present.ImageDisplay;
import com.gzdb.screen.present.ImageMenuDisplay;
import com.gzdb.screen.present.TextDisplay;
import com.gzdb.screen.present.VideoDisplay;
import com.gzdb.screen.present.VideoMenuDisplay;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.sunmi.bluetooth.Printer;
import com.gzdb.sunmi.bluetooth.SettlementOnTicket;
import com.gzdb.sunmi.util.ScreenUtil;
import com.gzdb.supermarket.been.AdInfo;
import com.gzdb.supermarket.been.AdText;
import com.gzdb.supermarket.been.DataBean;
import com.gzdb.supermarket.been.FinishOrderData;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.been.ItemSnapshotsBean;
import com.gzdb.supermarket.been.NoticeResulBean;
import com.gzdb.supermarket.been.ScreenFile;
import com.gzdb.supermarket.been.SyncOffOrderResultBean;
import com.gzdb.supermarket.been.UserRechargeBean;
import com.gzdb.supermarket.cache.ShopCart;
import com.gzdb.supermarket.common.SearchView;
import com.gzdb.supermarket.common.TouchLayout;
import com.gzdb.supermarket.dialog.MallOrderDialog;
import com.gzdb.supermarket.dialog.MenuDialog;
import com.gzdb.supermarket.dialog.StampSettingDialog;
import com.gzdb.supermarket.dialog.SupermarketRechargeDialog;
import com.gzdb.supermarket.event.MallOrderEvent;
import com.gzdb.supermarket.event.PrintEvent;
import com.gzdb.supermarket.event.RefreshEvent;
import com.gzdb.supermarket.event.RefreshProductEvent;
import com.gzdb.supermarket.event.SearchOnEvent;
import com.gzdb.supermarket.event.SetupEvent;
import com.gzdb.supermarket.event.StatisticsEvent;
import com.gzdb.supermarket.fragment.FreshDialogFragment;
import com.gzdb.supermarket.scan.ScanGunKeyEventHelper;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.gzdb.supermarket.util.ImageUtil;
import com.gzdb.supermarket.util.MD5Utils;
import com.gzdb.supermarket.util.NoticeMF;
import com.gzdb.supermarket.util.PrintUtil;
import com.gzdb.supermarket.util.ToNewFragEvent;
import com.gzdb.supermarket.util.UrlUtil;
import com.gzdb.supermarket.util.Utils;
import com.gzdb.vaservice.bean.YctConfigBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sunmi.electronicscaleservice.ScaleCallback;
import com.sunmi.scalelibrary.ScaleManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sunmi.ds.DSKernel;
import sunmi.ds.callback.ISendCallback;

import static com.gzdb.sunmi.Sunmi.PAY_CODE_IMG_ID;
import static com.gzdb.sunmi.util.ScreenUtil.SCREEN_TYPE;

/**
 * Created by zhumg on 7/15.
 */
public class SupermarketIndexActivity extends BaseActivity implements ScanGunKeyEventHelper.OnScanSuccessListener, SpeechSynthesizerListener, StampSettingDialog.StampSettingHost {

    //刷新左边列表
    public static final int requestCode_UPDATE_LEFT_ITEMTYPE = 100;//刷新左边商品类型列表
    public static final int requestCode_PAY_RESULT = 101;//支付结果
    public static final int requestCode_UPDATE_CART = 102;//刷新购物车
    public static final int requestCode_UPDATE_CENTER_ITEMS = 103;//刷新中间商品类型列表
    public static final int requestCode_UPDATE_ALL = 104;//刷新所有
    public static final int requestCode_SUPPLY_PAY_RESULT = 105;//采购支付结果

    public static final int RECHARGE_SYN_TIME = 3 * 1000; // 3秒刷新时间
    public static final int HANDLER_RECHARGE_SYN_CODE = 9001; // 心跳访问付款成功

    private static final String SAMPLE_DIR_NAME = "baiduTTS";
    private static final String SPEECH_FEMALE_MODEL_NAME = "speech_female.dat";
    private static final String TEXT_MODEL_NAME = "etts_text.dat";

    public static SupermarketIndexActivity indexActivity;
    public static boolean isSyncOffOrder;
    public static SoundPool soundPool;
    public static int SOUNDID_ADDSHOPCAR;
    public static int SOUNDID_PAYSUCCESS;
    public static int SOUNDID_PAYFAULIE;
    public static int SOUNDID_NEW_FRESH_ORDER;
    //百度语音合成相关
    public static SpeechSynthesizer mSpeechSynthesizer;
    static boolean init_bool = false;
    private static Dialog dialog;
    @Bind(R.id.merchantName)
    TextView merchantName;
    @Bind(R.id.userName)
    TextView userName;
    @Bind(R.id.udata_btn)
    TextView udata_btn;//刷新
    @Bind(R.id.e_search)
    SearchView e_search;
    @Bind(R.id.supermarket_frame)
    FrameLayout supermarketFrame;
    @Bind(R.id.drap_layout)
    FrameLayout drapLayout;
    @Bind(R.id.touchLayout)
    TouchLayout touchLayout;
    @Bind(R.id.marqueeView)
    MarqueeView marqueeView;//跑马灯组件
    @Bind(R.id.bt_fresh)
    Button bt_fresh;
    @Bind(R.id.o1_txt_num)
    TextView o1_txt_num;

    //中间布局
    SupermarketShopView supermarketShopView;
    ScanGunKeyEventHelper scanGunKeyEventHelper = null;//扫码
    boolean code_search = false;
    @Bind(R.id.btn_spread)
    Button btnSpread;
    private MarqueeFactory marqueeFactory;//跑马灯数据工厂
    private String mSampleDirPath;
    //记录用户首次点击返回键的时间
    private long firstTime = 0;
    private boolean mIsRecharge = false;

    //生鲜订单数量
    private int freshOrderSize = 0;
    //生鲜订单数量是否初始化过，如果第一次，就算不是0也不提示
    private boolean freshOrderSizeInit = false;

    private Handler mHander;
    private boolean search = false;
    // 续租付款dialog
    private SupermarketRechargeDialog supermarketRechargeDialog;
    DSKernel mDSKernel;
    private boolean spread = false;
    private int fileIndex = 0;
    private int files = 0;
    private List<String> filePaths = new ArrayList<>();

    public VideoDisplay videoDisplay = null;
    private ScreenManager screenManager = ScreenManager.getInstance();
    public VideoMenuDisplay videoMenuDisplay = null;
    public ImageMenuDisplay imageMenuDisplay = null;
    public TextDisplay textDisplay = null;
    public ImageDisplay imageDisplay = null;

    private MallOrderDialog mallOrderDialog;

    private FreshDialogFragment dialogFragment;

    private ScaleManager mScaleManager;
    public static int net;

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    //int OverhangNum;
    int RefundNum;

    private OrderNumber orderNumber;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getAcceptOrderCount();
            handler.postDelayed(this, 1000 * 10);// 间隔10秒
        }
    };


    public static SupermarketIndexActivity getMe() {
        return indexActivity;
    }

    public static void showNotice(String text) {
        DialogUtil.OkAndCancle(indexActivity, "通知", text).show();

    }

    public static void doSyncOffOder() {
        if (!Http.isNetworkConnected(indexActivity) || isSyncOffOrder) {
            return;
        }
        Observable.from(App.getDaoInstant().getFinishOrderDataDao().queryBuilder().limit(20).list())
                .map(new Func1<FinishOrderData, FinishOrderData>() {
                    @Override
                    public FinishOrderData call(FinishOrderData finishOrderData) {
                        LogUtils.e("同步离线订单");
                        finishOrderData.setItemSnapshots(App.getDaoInstant().getItemSnapshotsBeanDao().queryBuilder().where(ItemSnapshotsBeanDao.Properties.OffLineOrderId.eq(finishOrderData.getAutoId())).list());
                        return finishOrderData;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<FinishOrderData>>() {
                    @Override
                    public void call(List<FinishOrderData> finishOrderDatas) {
                        if (finishOrderDatas.size() == 0) {
                            return;
                        }
                        isSyncOffOrder = true;
                        OkGo.<NydResponse<SyncOffOrderResultBean>>post(Contonts.URL_SYNC_OFFLINE_ORDER)
                                .params("orders", new Gson().toJson(finishOrderDatas))
                                .execute(new JsonCallback<NydResponse<SyncOffOrderResultBean>>() {
                                    @Override
                                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<SyncOffOrderResultBean>> response) {
                                        if (response.body().response.getDatas().size() != 0) {
                                            LogUtils.e(response.body().response.getDatas());
                                            Observable.from(response.body().response.getDatas())
                                                    .flatMap(new Func1<FinishOrderData, Observable<ItemSnapshotsBean>>() {
                                                        @Override
                                                        public Observable<ItemSnapshotsBean> call(FinishOrderData finishOrderData) {
                                                            LogUtils.e(App.getDaoInstant().getItemSnapshotsBeanDao().queryBuilder().where(ItemSnapshotsBeanDao.Properties.LineId.eq(finishOrderData.getId())).list());
                                                            App.getDaoInstant().getFinishOrderDataDao().delete(App.getDaoInstant().getFinishOrderDataDao().queryBuilder().where(FinishOrderDataDao.Properties.Id.eq(finishOrderData.getId())).unique());
                                                            return Observable.from(App.getDaoInstant().getItemSnapshotsBeanDao().queryBuilder().where(ItemSnapshotsBeanDao.Properties.LineId.eq(finishOrderData.getId())).list());
                                                        }

                                                    })
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Subscriber<ItemSnapshotsBean>() {
                                                        @Override
                                                        public void onCompleted() {
                                                            isSyncOffOrder = false;
                                                            LogUtils.e("SyncDing");
                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {
                                                            LogUtils.e(e.getMessage());
                                                            isSyncOffOrder = false;
                                                        }

                                                        @Override
                                                        public void onNext(ItemSnapshotsBean itemSnapshotsBean) {
                                                            App.getDaoInstant().getItemSnapshotsBeanDao().delete(itemSnapshotsBean);
                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });
    }

    public static void showLoadDialog() {
        ((TextView) dialog.findViewById(R.id.progress_content)).setText("加载数据中...");
        dialog.show();
    }

    public static void showWaitDialog() {
        ((TextView) dialog.findViewById(R.id.progress_content)).setText("请稍后...");
        dialog.show();
    }

    public static void showDialog(String msg) {
        ((TextView) dialog.findViewById(R.id.progress_content)).setText(msg);
        dialog.show();
    }

    public static void setDialogMsg(String msg) {
        ((TextView) dialog.findViewById(R.id.progress_content)).setText(msg);
    }

    public static void closeDialog() {
        dialog.dismiss();
    }

    public static void speak(String text) {
        int result = mSpeechSynthesizer.speak(text);
        if (result < 0) {
            LogUtils.e("error,please look up error code in doc or URL:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    public TouchLayout getTouchLayout() {
        return touchLayout;
    }

    public FrameLayout getDrapLayout() {
        return drapLayout;
    }

    public SupermarketShopView getSupermarketShopView() {
        return supermarketShopView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarketndex);
        indexActivity = this;
        ButterKnife.bind(this);
        mDSKernel = Sunmi.getDSKernel();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Printer.intClass(this);
        SettlementOnTicket.intClass(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 5);

        SOUNDID_ADDSHOPCAR = soundPool.load(this, R.raw.addcar, 1);
        SOUNDID_PAYSUCCESS = soundPool.load(this, R.raw.paysuccess, 1);
        SOUNDID_PAYFAULIE = soundPool.load(this, R.raw.unpay, 1);
        SOUNDID_NEW_FRESH_ORDER = soundPool.load(this, R.raw.one_new_order, 1);

        scanGunKeyEventHelper = new ScanGunKeyEventHelper(this);
        supermarketShopView = new SupermarketShopView(this);
        e_search.setSupermarketShopView(supermarketShopView);
        //添加进去
        supermarketFrame.addView(supermarketShopView.getView());
        //清除购物车
        if (ShopCart.nowShopCart != null) {
            ShopCart.nowShopCart.clear();
        }
        //设置店铺和用户数据
        setData();
        handler.postDelayed(runnable, 1000 * 10);// 间隔10秒
        dialog = DialogUtil.loadingDialog(this, "加载中...");

        if (!init_bool) {

            init_bool = true;
            showLoadDialog();
            //下一帧加载数据
            merchantName.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //加载商品数据
                    supermarketShopView.loadData();
                }
            }, 1000);
        } else {
            supermarketShopView.refreshShopCart();
        }
        try {
            //同步离线订单
            syncOffOrder();
        } catch (Exception e) {
            LogUtils.e(e);
        }

        touchLayout.setShopView(supermarketShopView);//放进去给他下架商品的时候调用刷新

//        SettingActivity.passStock = SPUtil.getInstance().getBoolean(this, "stock", true);
        mHander = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HANDLER_RECHARGE_SYN_CODE:
                        mHander.removeMessages(HANDLER_RECHARGE_SYN_CODE);
                        getRecharge();//加载过期付款
                        break;
                    default:
                        break;
                }
            }
        };

        mHander.sendEmptyMessage(HANDLER_RECHARGE_SYN_CODE);

        Long imgId = SPUtil.getInstance().getLong(mContext, PAY_CODE_IMG_ID, 0);
        if ((Sunmi.viceScreenMode && imgId != 0) || Build.MODEL.equals("S2")) {
            btnSpread.setVisibility(View.VISIBLE);
        }

        GpUsbPrint.use = SPUtil.getInstance().getBoolean(this, "usbPrint2", true);
        GpUsbPrint.initPrinter(this);
        if (GpUsbPrint.use) {
            GpUsbPrint.connPrinter();
        }

        //加载通告
        getNotice();

        //百度语音合成
        initialEnv();
        initialTts();

        //优惠配置
        getSale();
        getSale2();

        //羊城通配置
        getYctConfig();

        //广告配置
        if (Build.MODEL.equals("S2") || Sunmi.viceScreenMode) {
            getAd();
            getScreenFile();
        }

        getAdText();

        mallOrderDialog = new MallOrderDialog(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MallActivity.class));
                mallOrderDialog.dismiss();
            }
        });

        connectScaleService();
        getAcceptOrderCount();

        if (!PrintUtils.isPrint) {
            PrintUtils.initPrintParam(SupermarketIndexActivity.this);
        }

    }

    public void setMarqueeViewData(List<String> strings) {
        if (marqueeFactory == null) {
            marqueeFactory = new NoticeMF(this);
            marqueeFactory.setData(strings);
            marqueeView.setInterval(5000);
            marqueeView.setMarqueeFactory(marqueeFactory);
            marqueeView.startFlipping();
        } else {
            marqueeFactory.resetData(strings);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (marqueeView != null) {
            marqueeView.startFlipping();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (marqueeView != null) {
            marqueeView.stopFlipping();
        }
    }

    private void setData() {
        if (app.getCurrentUser() != null) {
            if (app.currentUser.getShowName().length() > 10) {
                String n = app.currentUser.getShowName().substring(0, 10);
                merchantName.setText(n + "...");
            } else {
                merchantName.setText(app.currentUser.getShowName());
            }
            userName.setText(app.currentUser.getAccountTypeName() + ":" + app.currentUser.getRealName());
        }
    }

    public void syncOffOrder() {
        Observable.interval(5, 30, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        doSyncOffOder();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable); //停止刷新
        supermarketShopView.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        mHander.removeMessages(HANDLER_RECHARGE_SYN_CODE);
        //强制杀死，背显消失
        if (!Sunmi.viceScreenMode) {
            try {
                App.mSerialPortOperaion.WriteData(0xC);
            } catch (Exception e) {
            }
        }
        GpUsbPrint.getInstance().close();
        PrintUtils.onDestroy();
        super.onDestroy();
    }

    private long lastScan = 0L;
    private static final int FAST_CLICK_DELAY_TIME = 350;  // 扫码回调速度

    //扫码成功回调
    @Override
    public void onScanSuccess(final String barcode) {
        if (search) {
            return;
        }
        if (System.currentTimeMillis() - lastScan < FAST_CLICK_DELAY_TIME) {
            ToastUtil.showToastBig(this, "您扫得太快了，请放慢速度~");
            return;
        }

        String code = barcode.replace(" ", "");
        lastScan = System.currentTimeMillis();

        List<GoodBean> list = null;

        double discount = 0;
        double weight = 0;
        long itemId = 0;

        //如果第一位是9，则是称重类条码
        if (code.charAt(0) == '9') {
            String[] barcodes = ShareCodeUtils.explainPrintItemBarcode(code);
            if (barcodes != null) {
                try {
                    itemId = Long.parseLong(barcodes[0]);
                    weight = Double.parseDouble(barcodes[1]);
                    discount = Double.valueOf(barcodes[2]);
                    list = App.getDaoInstant().getGoodBeanDao().queryBuilder().where(GoodBeanDao.Properties.Id.eq(itemId)).list();
                } catch (Exception e) {
                    //ToastUtil.showToastBig(mContext, "商品不存在，或者条码有误，code=2");
                }
            }
        }

        if (list == null || list.size() < 1) {
            list = App.getDaoInstant().getGoodBeanDao().queryBuilder().where(GoodBeanDao.Properties.Barcode.eq(code)).list();
        }

        //标品
        if (list == null || list.size() < 1) {
            ToastUtil.showToastBig(mContext, "商品不存在，或者条码有误，code=3");
            return;
        }

        final GoodBean bean = list.get(0);
        if (weight > 0) {
            ShopCart.nowShopCart.addFreshItem(bean, weight, discount);
        } else if (bean.getItemType() == 2) {
            showFreshDialog(bean);
        } else if (!ShopCart.nowShopCart.addItem(bean)) {
            ToastUtil.showToast(this, "库存不足");
            return;
        }
        if (!Sunmi.viceScreenMode) {
            try {
                App.mSerialPortOperaion.WriteData(0xC);
                String str = bean.getSalesPrice() + "";
                App.mSerialPortOperaion.WriteData(27, 81, 65);
                App.mSerialPortOperaion.WriteData(Utils.input(str));
                App.mSerialPortOperaion.WriteData(13);
                //单价
                App.mSerialPortOperaion.WriteData(0X1B, 0X73, 0X31);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //刷新购物车
        supermarketShopView.refreshShopCart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == requestCode_UPDATE_CART) {
            supermarketShopView.centerItemAdapter.notifyDataSort();
            touchLayout.clearDrag();
        } else if (resultCode == requestCode_PAY_RESULT) {
            //修改库存
            List<ShopCart.ShopCartItem> scitems = ShopCart.nowShopCart.gets();
            List<GoodBean> changeBeen = new ArrayList<>();
            for (int i = 0; i < scitems.size(); i++) {
                ShopCart.ShopCartItem sci = scitems.get(i);
                //扣除购买的数量
                sci.item.setRepertory(sci.item.getRepertory() - sci.count);
                changeBeen.add(sci.item);
            }
            App.getDaoInstant().getGoodBeanDao().insertOrReplaceInTx(changeBeen);
            //清空购物车
            ShopCart.nowShopCart.clear();

            supermarketShopView.refreshShopCart();
            FinishOrderData fdata = null;
            try {
                fdata = new Gson().fromJson(data.getStringExtra("data"), FinishOrderData.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (resultCode == requestCode_UPDATE_LEFT_ITEMTYPE) {
            LogUtils.e("刷新商品类型");
            supermarketShopView.refreshFromNet();
        } else if (resultCode == requestCode_UPDATE_ALL) {
            LogUtils.e("刷新所有数据");
            supermarketShopView.refreshCahce();
            supermarketShopView.refreshShopCart();
        } else if (resultCode == requestCode_SUPPLY_PAY_RESULT) {
            supermarketShopView.refreshShopCart();
            supermarketShopView.centerItemAdapter.notifyDataSort();
        }
    }

    @OnClick({R.id.btn_menu, R.id.udata_btn, R.id.img_chargeRecord, R.id.btn_spread, R.id.bt_fresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_menu: {
                MenuDialog dialog = new MenuDialog();
                dialog.show(getSupportFragmentManager(), "menuDialog");
                break;
            }
            case R.id.udata_btn: {
                if (!Http.isNetworkConnected(this)) {
                    ToastUtil.showToast(this, "当前没联网，无法刷新");
                    return;
                }
                supermarketShopView.refreshFromNet();
                break;
            }
            case R.id.img_chargeRecord://交易纪录
                EventBus.getDefault().post(new ToNewFragEvent(ChargeRecordFragment.class.getName(), new ChargeRecordFragment(), null, false, false));
                break;
            case R.id.btn_spread:
                if (spread) {
                    spread = false;
                    btnSpread.setText("推广会员");
                    if (ShopCart.nowShopCart.getAllCount() > 0) {
                        Sunmi.showCardImg(0, 0, imageMenuDisplay);
                    } else {
                        ScreenUtil.getInstance().showScreen(imageDisplay);
                    }
                } else {
                    btnSpread.setText("关闭推广");
                    spread = true;
                    Sunmi.showScreenAd(imageDisplay);
                }
                break;
            case R.id.bt_fresh:
                OkGo.<NydResponse<Object>>post(Contonts.FRESH_ORDER_LIST)
                        .params("supplier_id", App.getInstance().currentUser.getPassportId())
                        .params("status", 1)
                        .params("deliver_status", 0)
                        .execute(new JsonCallback<NydResponse<Object>>() {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<NydResponse<Object>> response) {
                                try {
                                    if (response.body().code == 0) {
                                        Intent intent = new Intent(SupermarketIndexActivity.this, FreshActivity.class);
                                        intent.putExtra("OverhangNum", freshOrderSize);
                                        intent.putExtra("RefundNum", RefundNum);
                                        startActivity(intent);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                break;
            default:
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            return super.dispatchKeyEvent(event);
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return super.dispatchKeyEvent(event);
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            return false;
        } else {
            scanGunKeyEventHelper.analysisKeyEvent(event);
        }

        return super.dispatchKeyEvent(event);
    }

    //点击搜索框以外的地方键盘消失
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (Utils.isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                e_search.setFocusable(false);
                e_search.setFocusableInTouchMode(false);
                e_search.requestFocus();
                e_search.setHibe_delete_btn(false);
                e_search.search_Input.setText("");
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新
        supermarketShopView.centerItemAdapter.notifyDataSort();
    }

    public void setRefreshCount(int count) {
        udata_btn.setText("刷新(" + count + ")");
    }

    public void getNotice() {
        OkGo.<NydResponse<NoticeResulBean>>post(Contonts.URL_NOTICE_LIST)
                .execute(new JsonCallback<NydResponse<NoticeResulBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<NoticeResulBean>> response) {
                        if (response.body().response.getDatas().size() > 0) {
                            NoticeResulBean.DatasBean bean = response.body().response.getDatas().get(0);
                            DialogUtil.OkAndCancle(mContext, bean.getTitle(), bean.getContent()).show();
                        }
                    }
                });
    }

    public void getSale() {
        OkGo.<NydResponse<DataBean<SaleType3>>>post(Contonts.SALE_TYPE_3)
                .execute(new JsonCallback<NydResponse<DataBean<SaleType3>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<DataBean<SaleType3>>> response) {
                        LogUtils.e(response.body().response);
                        App.getDaoInstant().getSaleType3Dao().deleteAll();
                        for (int i = 0; i < response.body().response.getDatas().size(); i++) {
                            App.getDaoInstant().getSaleType3Dao().insert(response.body().response.getDatas().get(i));
                        }
                    }
                });
    }

    public void getSale2() {
        OkGo.<NydResponse<List<GroupSale>>>post(Contonts.SALE_TYPE_2)
                .execute(new JsonCallback<NydResponse<List<GroupSale>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<List<GroupSale>>> response) {
                        Gson gson = new Gson();
                        SPUtil.getInstance().putString(mContext, "group_sale", gson.toJson(response.body().response));
                    }
                });
    }

    public void getAd() {
        OkGo.<NydResponse<AdInfo>>post(Contonts.AD_INFO)
                .execute(new JsonCallback<NydResponse<AdInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<AdInfo>> response) {
                        if (response.body().response == null) {
                            return;
                        }
                        paycodeConfig(response.body().response);
                    }
                });
    }

    public void getAdText() {
        OkGo.<NydResponse<List<AdText>>>post(Contonts.AD_TEXT)
                .execute(new JsonCallback<NydResponse<List<AdText>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<List<AdText>>> response) {
                        if (response.body().response == null) {
                            return;
                        }
                        List<AdText> adTexts = response.body().response;
                        List<String> datas = new ArrayList<>();
                        for (int i = 0; i < adTexts.size(); i++) {
                            datas.add(adTexts.get(i).getValue());
                        }
                        setMarqueeViewData(datas);
                    }
                });
    }

    public void getRecharge() {
        // app.getCurrentUser().getPassportId()
        OkGo.<NydResponse<DataBean<UserRechargeBean>>>post(Contonts.URL_RECHARGE)
                .execute(new JsonCallback<NydResponse<DataBean<UserRechargeBean>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<DataBean<UserRechargeBean>>> response) {
                        if (null != response.body()) {
                            try {
                                if (response.body().response.getDatas() == null) {
                                    return;
                                }
                                long differ = response.body().response.getDatas().get(0).getDiffer();
                                if (differ >= 0) {
                                    mIsRecharge = false;
                                    if (null != supermarketRechargeDialog && supermarketRechargeDialog.isShowing()) {
                                        supermarketRechargeDialog.dismiss();
                                        ToastUtil.showToast(SupermarketIndexActivity.this, "续租付款成功！");
                                    }
                                    mHander.removeMessages(HANDLER_RECHARGE_SYN_CODE);
                                } else {
                                    if (null == supermarketRechargeDialog) {
                                        supermarketRechargeDialog = new SupermarketRechargeDialog(SupermarketIndexActivity.this, response.body().response.getDatas());
                                    }
                                    if (!supermarketRechargeDialog.isShowing()) {
                                        supermarketRechargeDialog.setTime(response.body().response.getDatas().get(0).getRenewTime());
                                        supermarketRechargeDialog.show();
                                    }
                                    mIsRecharge = true;
                                    mHander.sendEmptyMessageDelayed(HANDLER_RECHARGE_SYN_CODE, RECHARGE_SYN_TIME);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }


    private void getYctConfig() {
        OkGo.<NydResponse<YctConfigBean>>post(Contonts.YCT_CONFIG)
                .execute(new JsonCallback<NydResponse<YctConfigBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<YctConfigBean>> response) {
                        if (response.body().response == null) {
                            SPUtil.getInstance().putInt(mContext, "openYct", 0);
                            return;
                        }
                        YctConfigBean yctConfig = response.body().response;
                        SPUtil.getInstance().putInt(mContext, "openYct", yctConfig.getIsState());
                        SPUtil.getInstance().putString(mContext, "pkiCardNumber", yctConfig.getPkiCardNumber());
                        SPUtil.getInstance().putString(mContext, "terminalNumber", yctConfig.getTerminalNumber());
                        SPUtil.getInstance().putString(mContext, "psamCardNumber", yctConfig.getPsamCardNumber());
                    }
                });
    }

    private void getScreenFile() {
        OkGo.<NydResponse<List<ScreenFile>>>post(Contonts.SCREEN_FILE)
                .execute(new JsonCallback<NydResponse<List<ScreenFile>>>() {
                    @Override
                    public void onSuccess(Response<NydResponse<List<ScreenFile>>> response) {
                        List<String> filePaths = response.body().response.get(0).getUrls();
                        files = filePaths.size();
                        int time = response.body().response.get(0).getTime();
                        if (time <= 0) {
                            time = 5 * 1000;
                        } else {
                            time = time * 1000;
                        }
                        for (int i = 0; i < filePaths.size(); i++) {
                            loadScreenFile(filePaths.get(i), response.body().response.get(0).getType(), time);
                        }

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mallOrderEvent(MallOrderEvent event) {
        if (!mallOrderDialog.isShowing()) {
            mallOrderDialog.show();
        }
    }

    /**
     * 提交折扣使用统计
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void statistics(StatisticsEvent event) {
        OkGo.<String>post(Contonts.ORDER_STATISTICS)
                .params("orderId", event.getOrderId())
                .params("orderType", 7)
                .params("actualPrice", event.getActualPrice())
                .params("totalPrice", event.getTotalPrice())
                .params("totaldiscountPrice", event.getTotaldiscountPrice())
                .params("discountPercent", event.getDiscountPercent())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ToNewFragEvent toNewFragEvent) {
        switchDetailFragment(toNewFragEvent.getFragment(), toNewFragEvent.getBundle());
    }

    /**
     * 修改折扣优惠后重新加载商品
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshSaleEvent(RefreshSaleEvent event) {
        supermarketShopView.refreshFromNet();
        getSale();
        getSale2();
    }

    /**
     * 刷新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshEvent(RefreshEvent event) {
        supermarketShopView.refreshFromNet();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void searchEvent(SearchOnEvent event) {
        search = event.isSearch();
    }

    public void switchDetailFragment(Fragment fragment, Bundle bundle) {

        if (fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_conteainer, fragment,
                fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void print(PrintEvent event) {
        PrintUtil.print(event.getName(), event.getCode(), event.getItemId(), event.getPrice(), event.getVipPrice(), event.getWeight(), event.getTotalPrice(), event.getDiscount());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setup(SetupEvent event) {
//        SettingActivity.passStock = SPUtil.getInstance().getBoolean(this, "stock", true);
    }

    //添加完商品，刷新商品列表
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshProductEvent(RefreshProductEvent event) {
        supermarketShopView.refreshFromNet();
    }

    //以下为百度语音合成初始化相关
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 2000) {
                        Toast.makeText(mContext, "再按一次退出!", Toast.LENGTH_SHORT).show();
                        firstTime = secondTime;
                        return true;
                    } else {
                        System.exit(0);
                    }
                }

                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
//        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = getResources().getAssets().open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initialTts() {
        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
        this.mSpeechSynthesizer.setContext(this);
        this.mSpeechSynthesizer.setSpeechSynthesizerListener(this);
        // 文本模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
                + TEXT_MODEL_NAME);
        // 声学模型文件路径 (离线引擎使用)
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
                + SPEECH_FEMALE_MODEL_NAME);
//        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license文件时需要进行设置，如果在[应用管理]中开通了正式离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
//        // 如果合成结果出现临时授权文件将要到期的提示，说明使用了临时授权文件，请删除临时授权即可。
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/"
//                + LICENSE_FILE_NAME);
        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
        this.mSpeechSynthesizer.setAppId("9879541"/*这里只是为了让Demo运行使用的APPID,请替换成自己的id。*/);
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        if (BuildConfig.APP_DEBUG) {
            this.mSpeechSynthesizer.setApiKey("miu5GUi2n7GU6qLMffGoAOhe",
                    "47bf40d26760bb0fe3c8b21a238a362c"/*这里只是为了让Demo正常运行使用APIKey,请替换成自己的APIKey*/);
            //发音人（在线引擎），可用参数为0,1,2,3。。。（服务器端会动态增加，各值含义参考文档，以文档说明为准。0--普通女声，1--普通男声，2--特别男声，3--情感男声。。。）
            this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        }

        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "10");
        // 设置Mix模式的合成策略
        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
//        // 授权检测接口(只是通过AuthInfo进行检验授权是否成功。)
//        // AuthInfo接口用于测试开发者是否成功申请了在线或者离线授权，如果测试授权成功了，可以删除AuthInfo部分的代码（该接口首次验证时比较耗时），不会影响正常使用（合成使用时SDK内部会自动验证授权）
//        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
//
//        if (authInfo.isSuccess()) {
//            L.e("auth success");
//        } else {
//            String errorMsg = authInfo.getTtsError().getDetailMessage();
//            L.e("auth failed errorMsg=" + errorMsg);
//        }

        // 初始化tts
        mSpeechSynthesizer.initTts(TtsMode.MIX);
        // 加载离线英文资源（提供离线英文合成功能）
//        int result =
//                mSpeechSynthesizer.loadEnglishModel(mSampleDirPath + "/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath
//                        + "/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
//        toPrint("loadEnglishModel result=" + result);

        //打印引擎信息和model基本信息
        printEngineInfo();
    }

    /**
     * 打印引擎so库版本号及基本信息和model文件的基本信息
     */
    private void printEngineInfo() {
        LogUtils.e("EngineVersioin=" + SynthesizerTool.getEngineVersion());
        LogUtils.e("EngineInfo=" + SynthesizerTool.getEngineInfo());
        String textModelInfo = SynthesizerTool.getModelInfo(mSampleDirPath + "/" + TEXT_MODEL_NAME);
        LogUtils.e("textModelInfo=" + textModelInfo);
        String speechModelInfo = SynthesizerTool.getModelInfo(mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        LogUtils.e("speechModelInfo=" + speechModelInfo);
    }

    @Override
    public void onSynthesizeStart(String s) {

    }

    @Override
    public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

    }

    @Override
    public void onSynthesizeFinish(String s) {

    }

    @Override
    public void onSpeechStart(String s) {

    }

    @Override
    public void onSpeechProgressChanged(String s, int i) {

    }

    @Override
    public void onSpeechFinish(String s) {

    }

    @Override
    public void onError(String s, SpeechError speechError) {
        LogUtils.e(s, speechError.description);
    }

    private void paycodeConfig(AdInfo adInfo) {
        String adStr = ".";
        if (adInfo.getContext() != null) {
            adStr = adInfo.getContext();
        }
        String payCodeStr = SPUtil.getInstance().getString(mContext, "pay_code");
        if (!payCodeStr.equals("")) {
            Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_pay_code);
            Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(Contonts.PAY_CODE + "/" + payCodeStr, 190, 190, "UTF-8", "H", "0", Color.BLACK, Color.WHITE);

            Bitmap alterBitmap = Bitmap.createBitmap(bgBitmap.getWidth(),
                    bgBitmap.getHeight(), bgBitmap.getConfig());
            Canvas canvas = new Canvas(alterBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);

            canvas.drawBitmap(bgBitmap, new Matrix(), paint);

            canvas.drawBitmap(bitmap, bgBitmap.getWidth() / 2 + 60, ((bgBitmap.getHeight() - bitmap.getHeight()) / 2) + 24, paint);

            Bitmap payBitmap = ImageUtil.drawTextToTopCenter(mContext, alterBitmap, adStr, 14, getResources().getColor(R.color.white));

            SaveImgUtils.saveImage(mContext, payBitmap, "pay_code.png");

            String payCode = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pos/pay_code.png";
            SPUtil.getInstance().putString(mContext, "payCode", payCode);
            setPayCodeImage(payCode);
            if (adInfo.getUrl() == null) {
                return;
            }
            SPUtil.getInstance().putString(mContext, "screen_ad", adInfo.getUrl());
            if (Sunmi.viceScreenMode) {
                //设置副屏广告图
                OkGo.<Bitmap>get(adInfo.getUrl()).execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<Bitmap> response) {
                        SaveImgUtils.saveImage(mContext, response.body(), "screen_ad.png");

                        final String posAd = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pos/screen_ad.png";

                        mDSKernel.sendFile(DSKernel.getDSDPackageName(), posAd, new ISendCallback() {
                            @Override
                            public void onSendSuccess(long l) {
                                SPUtil.getInstance().putLong(mContext, Sunmi.POS_AD_ID, l);
                            }

                            @Override
                            public void onSendFail(int i, String s) {
                                ToastUtil.showToast(mContext, s);
                            }

                            @Override
                            public void onSendProcess(long l, long l1) {
                            }
                        });
                    }
                });
            }
        } else {
            Bitmap payBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_pay_code);
            SaveImgUtils.saveImage(mContext, payBitmap, "pay_code.png");
            String payCode = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pos/pay_code.png";
            SPUtil.getInstance().putString(mContext, "payCode", payCode);
            setPayCodeImage(payCode);
        }
        if (Build.MODEL.equals("S2")) {
            initScreen();
            if (imageDisplay != null) {
                try {
                    imageDisplay.show();
                } catch (Exception e) {
                }

            }
        }
    }

    private void setPayCodeImage(String filePath) {
        if (!Sunmi.viceScreenMode) {
            return;
        }
        mDSKernel.sendFile(DSKernel.getDSDPackageName(), filePath, new ISendCallback() {
            @Override
            public void onSendSuccess(long l) {
                SPUtil.getInstance().putLong(mContext, PAY_CODE_IMG_ID, l);
            }

            @Override
            public void onSendFail(int i, String s) {
                LogUtils.e(s);
                ToastUtil.showToast(mContext, s);
            }

            @Override
            public void onSendProcess(long l, long l1) {
            }
        });
    }

    private void loadScreenFile(String url, final int type, final int time) {
        SPUtil.getInstance().putInt(mContext, SCREEN_TYPE, type);
        String extension = UrlUtil.getFileExtension(url);
        String fileName = MD5Utils.encode(url) + "." + extension;
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pos/" + fileName;
        filePaths.add(filePath);
        if (Utils.fileIsExists(filePath)) {
            fileIndex++;
            if (fileIndex == files) {
                if (type == 0) {
                    ScreenUtil.getInstance().setPictures(time, filePaths);
                } else {
                    ScreenUtil.getInstance().setVideos(filePaths);
                }
            }
            return;
        }
        OkGo.<File>get(url).execute(new FileCallback(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "pos", fileName) {
            @Override
            public void onSuccess(Response<File> response) {
                fileIndex++;
                if (fileIndex == files) {
                    if (type == 0) {
                        ScreenUtil.getInstance().setPictures(time, filePaths);
                    } else {
                        ScreenUtil.getInstance().setVideos(filePaths);
                    }
                }
            }

            @Override
            public void onError(Response<File> response) {
                super.onError(response);
            }
        });

    }

    private void initScreen() {
        screenManager.init(this);
        Display[] displays = screenManager.getDisplays();
        LogUtils.e("屏幕数量" + displays.length);
        for (int i = 0; i < displays.length; i++) {
            LogUtils.e("屏幕" + displays[i]);
        }
        if (displays.length > 1) {
            videoDisplay = new VideoDisplay(this, displays[1], Environment.getExternalStorageDirectory().getPath() + "/video_04.mp4");
            videoMenuDisplay = new VideoMenuDisplay(this, displays[1], Environment.getExternalStorageDirectory().getPath() + "/video_04.mp4");
            imageMenuDisplay = new ImageMenuDisplay(this, displays[1], SPUtil.getInstance().getString(mContext, "payCode"));
            textDisplay = new TextDisplay(this, displays[1]);
            imageDisplay = new ImageDisplay(this, displays[1]);
        }
    }


    @Override
    public void connectStamp(String code) {
    }

    private void connectScaleService() {
        mScaleManager = ScaleManager.getInstance(mContext);
        mScaleManager.connectService(new ScaleManager.ScaleServiceConnection() {
            @Override
            public void onServiceConnected() {
                getScaleData();
            }

            @Override
            public void onServiceDisconnect() {
            }
        });
    }

    private void getScaleData() {
        try {
            mScaleManager.getData(new ScaleCallback.Stub() {
                @Override
                public void getData(final int i, int i1, final int i2) throws RemoteException {
                    net = i;
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (dialogFragment != null && dialogFragment.isVisible()) {
                                dialogFragment.update(i2, i);
                            }
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void showFreshDialog(final GoodBean bean) {
        dialogFragment = new FreshDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("itemId", Long.parseLong(bean.getId()));
        bundle.putString("name", bean.getItemName());
        bundle.putString("code", bean.getBarcode());
        bundle.putDouble("price", bean.getSalesPrice());
        bundle.putDouble("vipPrice", bean.getMembershipPrice());
        dialogFragment.setArguments(bundle);

        dialogFragment.show(getSupportFragmentManager(), "FreshDialogFragment");
        dialogFragment.setListener(new FreshDialogFragment.AddListener() {
            @Override
            public void onAddResult(GoodBean goodBean, String total, String name, double weight, double discount) {
                if (goodBean == null) {
                    if (!ShopCart.nowShopCart.addFreshItem(bean, weight, discount)) {
                        ToastUtil.showToast(mContext, "库存不足");
                        return;
                    }
                } else {
                    if (!ShopCart.nowShopCart.addFreshItem(goodBean, weight, discount)) {
                        ToastUtil.showToast(mContext, "库存不足");
                        return;
                    }
                }
                //刷新购物车
                indexActivity.getSupermarketShopView().refreshShopCart();
            }
        });
        dialogFragment.setFunctionListener(new FreshDialogFragment.FunctionListener() {
            @Override
            public void onClick(int type) {
                if (type == 0) {
                    try {
                        if (mScaleManager != null) {
                            mScaleManager.zero();
                        }
                        ToastUtil.showToast(mContext, "电子秤清零成功");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        ToastUtil.showToast(mContext, "电子秤清零失败");
                    }
                } else {
                    try {
                        if (mScaleManager != null) {
                            mScaleManager.tare();
                        }
                        ToastUtil.showToast(mContext, "电子秤去皮成功");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        ToastUtil.showToast(mContext, "电子秤去皮失败");
                    }
                }
            }
        });
    }

    //获取生鲜系统的订单数
    void getAcceptOrderCount() {
        OkGo.<NydResponse<OrderNumber>>post(Contonts.FRESH_ORDER_NUMBER)
                .params("supplier_id", App.getInstance().currentUser.getPassportId())
                .params("order_type", -2)////订单类型：-2 pos/生鲜订单 0普通订单
                .execute(new JsonCallback<NydResponse<OrderNumber>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<OrderNumber>> response) {
                        if (response.body().code == 0) {
                            orderNumber = response.body().response;

                            int orderCount = orderNumber.getOrder_count();
                            if (orderCount > 0) {
                                o1_txt_num.setVisibility(View.VISIBLE);
                                o1_txt_num.setText(String.valueOf(orderCount));
                            } else {
                                o1_txt_num.setVisibility(View.GONE);
                            }

                            if (orderCount > 0) {
                                if (!freshOrderSizeInit) {
                                    freshOrderSizeInit = true;
                                } else {
                                    if (freshOrderSize < orderCount) {
                                        //播放声音
                                        soundPool.play(SupermarketIndexActivity.SOUNDID_NEW_FRESH_ORDER, 1, 1, 0, 0, 1);
                                    }
                                }

                            }

                            freshOrderSize = orderCount;

                            RefundNum = orderNumber.getRefund_apply_count();

                        }

                    }
                });

    }


}



