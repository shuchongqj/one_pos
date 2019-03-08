package com.gzdb.vaservice.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.serialPort.SerialPortManager;
import com.core.serialPort.listener.OnOpenSerialPortListener;
import com.core.serialPort.listener.OnSerialPortDataListener;
import com.core.util.ToastUtil;
import com.google.gson.Gson;
import com.gzdb.basepos.App;
import com.gzdb.basepos.BaseFragment;
import com.gzdb.basepos.BuildConfig;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.bean.BalanceResultBean;
import com.gzdb.sale.dialog.ConfirmDialog;
import com.gzdb.sale.dialog.NoticeDialog;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.sunmi.bluetooth.YctOrderOnTicket;
import com.gzdb.sunmi.util.OpenDrawers;
import com.gzdb.sunmi.util.ScreenUtil;
import com.gzdb.supermarket.been.CreateOrderSuccessBean;
import com.gzdb.supermarket.been.FinishOrderData;
import com.gzdb.supermarket.been.ItemSnapshotsBean;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.gzdb.vaservice.PrintTicket;
import com.gzdb.vaservice.YctRecordActivity;
import com.gzdb.vaservice.bean.Record;
import com.gzdb.vaservice.bean.VasPareBean;
import com.gzdb.vaservice.bean.YctRecordDetail;
import com.gzdb.vaservice.event.FinishVasEvent;
import com.gzdb.yct.data.MsgDataBean;
import com.gzdb.yct.socket.sdk.ConnectionInfo;
import com.gzdb.yct.socket.sdk.OkSocketOptions;
import com.gzdb.yct.socket.sdk.SocketActionAdapter;
import com.gzdb.yct.socket.sdk.bean.IPulseSendable;
import com.gzdb.yct.socket.sdk.bean.ISendable;
import com.gzdb.yct.socket.sdk.bean.OriginalData;
import com.gzdb.yct.socket.sdk.connection.IConnectionManager;
import com.gzdb.yct.socket.sdk.connection.NoneReconnect;
import com.gzdb.yct.util.AESUtil;
import com.gzdb.yct.util.ByteUtil;
import com.gzdb.yct.util.CHexConver;
import com.gzdb.yct.util.DateUtil;
import com.gzdb.yct.util.Encrypt3DES;
import com.gzdb.yct.util.MD5Util;
import com.gzdb.yct.util.ReaderError;
import com.gzdb.yct.util.TUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hdx.HdxUtil;
import okhttp3.Call;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.widget.Toast.LENGTH_SHORT;
import static com.gzdb.yct.socket.sdk.OkSocket.open;

/**
 * A simple {@link Fragment} subclass.
 */
public class YctFragment extends BaseFragment implements OnOpenSerialPortListener {


    @Bind(R.id.flowlayout_price)
    TagFlowLayout flowlayoutPrice;
    @Bind(R.id.et_card_no)
    EditText etCardNo;
    @Bind(R.id.et_balance)
    EditText etBalance;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.et_seller_balance)
    EditText etSellerBalance;
    @Bind(R.id.et_pre_balance)
    EditText etPreBalance;

    private SerialPortManager mSerialPortManager;
    private IConnectionManager mManager;
    private OkSocketOptions mOkOptions;
    private ConnectionInfo mInfo;
    private byte[] rqBytes;
    private byte[] mOpt = {};

    private byte[] mShid = {};
    private byte[] enShid = {};
    private byte[] mSk = {};
    private byte[] mServerTime = {};
    private byte[] mLogin3Data = {};

    private byte[] mPhysicsNo = {};
    private byte[] mLogicNo = {};
    private byte[] mSak = {};
    private byte[] mCardInfo = {};
    private byte[] mSw1sw2 = {};

    private Encrypt3DES encrypt3DES;

    private byte[] mCardInfo79 = {};    //CPU卡取信息返回
    private byte[] mCardStatus = {};
    private byte[] mCardBalance = {};
    private byte[] mSysOrderNo = {};     //系统流水号
    private byte[] mReaderBackStatus = {};
    private byte[] mMoneyLower = {};    //金额下限 1字节
    private byte[] mMoneyUpper = {};    //金额上限 3字节
    private byte[] mRechargeMoney = {};
    private byte[] mRechargeAuthCode = {};
    private byte[] mTranTime = {};

    private byte[] mM1TranInfo = {};
    private byte[] mTranCounter = {};
    private byte[] mM1Random = {};

    private Integer index = 0;

    private boolean first = true;
    private boolean recharge = true;
    private boolean updateRecharge = false;
    private boolean firstReadCard = true;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private static Dialog dialog;
    private VasPareBean chooseBean;//选中的商品
    private TagAdapter<VasPareBean> tempDataAdapter;//填充数据
    private String mOrderId;

    private File device;
    private int baudRate;
    private String mPki = "89006581";
    private String ck = "11223344556677888877665544332211";
    private String psam = "010099992955";

    private String readerId = "880120000509";
    private String mPassword = "00000000000000000000000000000000";
    private String merchantNumber = "0880100000002324";
//    private String serverIp = "121.32.31.2";
//    private int port = 5010;

    private String serverIp = "58.62.173.38";
    private int port = 5006;

    private boolean startRecharge = false;
    private double originalMoney = 0;
    //    private double finalMoney = 0;
    private double mMoney = 0;
    private double mSellerBalance;

    private boolean serialPortOpen = false;
    private boolean signSuccess = false;
    private boolean rechargeStatus = false;

    private NoticeDialog noticeDialog;
    private ConfirmDialog confirmDialog;

    private byte[] sentData = {};

    private String optType = "CPU";

    private int setReaderStep = 1;

    private boolean getCardInfo = false;

    private boolean m1Load = false;                 //M1卡圈存标记
    private boolean cpuLoad = false;                //CPU卡圈存标记
    private boolean cpuLoadSubmit = false;          //CPU卡圈存提交标记
    private boolean m1LoadSubmit = false;           //M1卡圈存提交标记

    private int mTime = 3;

    private int retryTime = 1;

//    private Timer timer = new Timer();

    private Long recordId;

    private boolean updateRes = false;
    private byte[] update = {};

    private boolean isFirst = true;

    private String reqType = "";            //记录当前请求服务器的指令

    private boolean rCpuLoad = false;       //读卡器CPU卡圈存
    private boolean rM1Load = false;        //读卡器M1卡圈存
    private boolean m12cpu = false;         //cpu卡升级标识

    private int timeOut = 1000;
    private boolean ret = false;
    private byte[] readerCom = {};
    private byte tryCtr;
    private int tryTime = 0;

    private double upMoney = 0;
    private double lowMoney = 0;

    private String selectTag = "";

    private boolean serverRq = false;

    int mPayType;


    public YctFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yct, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        dialog = DialogUtil.loadingDialog(mContext, "加载中...");

        device = new File("/dev/ttyHSL1");
        baudRate = 57600;

        mPki = SPUtil.getInstance().getString(mContext, "pkiCardNumber");
        readerId = SPUtil.getInstance().getString(mContext, "terminalNumber");
        psam = SPUtil.getInstance().getString(mContext, "psamCardNumber");

        if (BuildConfig.APP_DEBUG) {
            merchantNumber = "0880100000000023";
            serverIp = "121.32.31.2";
            port = 5010;
        }

        /**
         * 校验参数
         */
        if (!(mPki.length() == 8 && readerId.length() == 12)) {
            noticeDialog = new NoticeDialog(mContext, "读卡器参数配置有误，请检查后重试", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noticeDialog.dismiss();
                    EventBus.getDefault().post(new FinishVasEvent());
                }
            });
            noticeDialog.show();

            return view;
        }

//        LogUtils.e("mPki:" + mPki);
//        LogUtils.e("readerId:" + readerId);
//        LogUtils.e("psam:" + psam);
//        LogUtils.e("mPassword:" + mPassword);
//        LogUtils.e("ck:" + ck);
//        LogUtils.e("merchantNumber:" + merchantNumber);
//        LogUtils.e("serverIp:" + serverIp);
//        LogUtils.e("port:" + port);

        showTempData();

        encrypt3DES = new Encrypt3DES();

        mSerialPortManager = new SerialPortManager();

        mSerialPortManager.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(byte[] bytes) {
                        LogUtils.e(TUtil.bytes2HexString(bytes));
                        LogUtils.e(bytes.length);
                        ret = true;
                        Record record = App.getDaoInstant().getRecordDao().load(recordId);
                        byte[] reader_back_status_ba = TUtil.subBytes(bytes, 3, 1);
                        switch (bytes[2]) {
                            case 0x7B:
                                byte[] mSw1sw2 = TUtil.subBytes(bytes, 8, 2);
                                record.setCard_back_no(TUtil.bytes2HexString(mSw1sw2));
                                record.setReader_back_no(TUtil.bytes2HexString(reader_back_status_ba));
                                App.getDaoInstant().getRecordDao().update(record);
                                mTime = 3;
                                rCpuLoad = true;
                                break;
                            case (byte) 0xBA:
                                record.setStatus("01");     //充值写卡失败
                                record.setReader_back_no(TUtil.bytes2HexString(reader_back_status_ba));
                                App.getDaoInstant().getRecordDao().update(record);
                                mTime = 3;
                                rM1Load = true;
                                if (bytes[3] != 0x00) {
                                    rM1GetCardInfo();
//                                    pM1RollBack();
                                    return;
                                }
                                break;
                        }
                        readerRsp(bytes);
                    }

                    @Override
                    public void onDataSent(byte[] bytes) {
                        readerCom = bytes;
                        LogUtils.e(TUtil.bytes2HexString(bytes));
                        LogUtils.e(bytes.length);
                        ret = false;
                        timeOut = 1000;
                        timeOut();
                    }
                })
                .openSerialPort(device, baudRate);

        App.getInstance().setLength(136);

        initData();

        mManager.connect();

        getBalance();

//        byte[] reason = {0x00};
//        LogUtils.e(mSerialPortManager.sendAndWaitBack(reason,500));

        return view;
    }

    /**
     * 串口打开成功
     *
     * @param device 串口
     */
    @Override
    public void onSuccess(File device) {
        serialPortOpen = true;
//        Toast.makeText(getContext(), String.format("串口 [%s] 打开成功", device.getPath()), Toast.LENGTH_SHORT).show();
    }

    /**
     * 串口打开失败
     *
     * @param device 串口
     * @param status status
     */
    @Override
    public void onFail(File device, Status status) {
        switch (status) {
            case NO_READ_WRITE_PERMISSION:
                break;
            case OPEN_FAIL:
            default:
                break;
        }
    }

    public void showTempData() {
        final List<VasPareBean> beenList = new ArrayList<>();
        VasPareBean vasPareBean1 = new VasPareBean();
        vasPareBean1.setItemName("50元");
        vasPareBean1.setSalesPrice(50);
        beenList.add(vasPareBean1);
        VasPareBean vasPareBean2 = new VasPareBean();
        vasPareBean2.setItemName("100元");
        vasPareBean2.setSalesPrice(100);
        beenList.add(vasPareBean2);
        VasPareBean vasPareBean3 = new VasPareBean();
        vasPareBean3.setItemName("150元");
        vasPareBean3.setSalesPrice(150);
        beenList.add(vasPareBean3);
        VasPareBean vasPareBean4 = new VasPareBean();
        vasPareBean4.setItemName("200元");
        vasPareBean4.setSalesPrice(200);
        beenList.add(vasPareBean4);
        VasPareBean vasPareBean5 = new VasPareBean();
        vasPareBean5.setItemName("300元");
        vasPareBean5.setSalesPrice(300);
        beenList.add(vasPareBean5);
        VasPareBean vasPareBean6 = new VasPareBean();
        vasPareBean6.setItemName("全部");
        vasPareBean6.setSalesPrice(-1);
        beenList.add(vasPareBean6);
        tempDataAdapter = new TagAdapter<VasPareBean>(beenList) {
            @Override
            public View getView(FlowLayout parent, int position, VasPareBean vasPareBean) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.vas_pare_item,
                        flowlayoutPrice, false);
                tv.setText(vasPareBean.getItemName());

                return tv;
            }

        };

        flowlayoutPrice.setAdapter(tempDataAdapter);

        flowlayoutPrice.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                chooseBean = beenList.get(position);
                if (chooseBean.getItemName().equals("全部")) {
                    tvPrice.setText("-- --");
                } else {
                    tvPrice.setText(chooseBean.getItemName());
                }
                selectTag = chooseBean.getItemName();
                mMoney = chooseBean.getSalesPrice();
                return false;
            }
        });

    }

    private SocketActionAdapter adapter = new SocketActionAdapter() {

        @Override
        public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
//            Toast.makeText(context, "连接成功", LENGTH_SHORT).show();
            if (first) {
                rInit();
            }
        }

        @Override
        public void onSocketDisconnection(Context context, ConnectionInfo info, String action, Exception e) {
//            Toast.makeText(context, "连接中断" + e.getMessage(), LENGTH_SHORT).show();
            if (e != null) {
            } else {
            }

//            switch (reqType) {
//                case "AA85":
//                    List<Record> records = App.getDaoInstant().getRecordDao().loadAll();
//                    Record record=records.get(0);
//                    if (record != null && retryTime <= 3) {
//                        showDialog("第" + retryTime + "次重新提交数据...");
//                        pCpuLoadReSubmit(record);
//                        retryTime++;
//                    }
//                    break;
//                case "AA65":
//                    Record record2 = App.getDaoInstant().getRecordDao().load(recordId);
//                    pM1RollBack(record2);
//                    break;
//            }
        }

        @Override
        public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
            Toast.makeText(context, "与服务器连接失败", LENGTH_SHORT).show();

        }

        @Override
        public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
            super.onSocketReadResponse(context, info, action, data);
            serverRsp(data.getBodyBytes());
            reqType = "";     //成功返回数据后，将请求指令清除掉
            retryTime = 1;
        }

        @Override
        public void onSocketWriteResponse(Context context, ConnectionInfo info, String action, ISendable data) {
            super.onSocketWriteResponse(context, info, action, data);
            LogUtils.e(data.parse().length);
            LogUtils.e(TUtil.bytes2HexString(data.parse()));
            sentData = data.parse();
            reqType = TUtil.bytes2HexString(TUtil.subBytes(data.parse(), 0, 2));
            serverRq = true;
        }

        @Override
        public void onPulseSend(Context context, ConnectionInfo info, IPulseSendable data) {
            super.onPulseSend(context, info, data);
            LogUtils.e(TUtil.bytes2HexString(data.parse()));
        }

        @Override
        public void onSocketIOThreadShutdown(Context context, String action, Exception e) {
            super.onSocketIOThreadShutdown(context, action, e);

//            if (dialog != null) {
//                dialog.dismiss();
//            }
            switch (e.getMessage()) {
                case "java.net.SocketException: sendto failed: EPIPE (Broken pipe)":
                    ToastUtil.showToast(mContext, "网络连接失败，请重试");
                    break;
                case "com.gzdb.yct.socket.impl.exceptions.ReadException: action_server_return_empty":
                    ToastUtil.showToast(mContext, "服务器处理失败，请重试");
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    break;
                default:
//                    Toast.makeText(context, "线程异常中断" + e.getMessage(), LENGTH_SHORT).show();
                    ToastUtil.showToast(mContext, "服务器处理失败，请重试");
            }

            /**
             * 异常处理 主要是处理圈存提交和冲正提交时服务器或网络出错导致的问题，这里可以做重新请求处理
             */
            List<Record> records = App.getDaoInstant().getRecordDao().loadAll();
            Record record = records.get(0);
            switch (reqType) {
                case "AA83":    //如果 P_CPU_LOAD 返回数据异常发起圈存提交
                    if (record != null) {
                        record.setVoucher_no(TUtil.bytes2HexString(TUtil.zeros(4)));
                        App.getDaoInstant().getRecordDao().update(record);
                    }
                    cpuReadNoRspOrServerNoRsp();
                    break;
                case "AA63":    //如果 P_M1_LOAD 报文没有发送成功，则终端立即删除该不完整交易记录
                    if (record != null) {
                        record.setVoucher_no(TUtil.bytes2HexString(TUtil.zeros(4)));
                        App.getDaoInstant().getRecordDao().update(record);
                    }
                    m1ReadNoRspOrServerNoRsp();
                    break;
                case "AA85":
                    if (record != null && retryTime <= 3) {
                        showDialog("第" + retryTime + "次重新提交数据...");
                        pCpuLoadReSubmit();
                        retryTime++;
                    } else {
                        ((TextView) dialog.findViewById(R.id.progress_content)).setText("请求服务器...");
                    }
                    break;
                case "AA65":
                    if (record != null && retryTime <= 3) {
                        showDialog("第" + retryTime + "次重新提交数据...");
                        pM1RollBack();
                        retryTime++;
                    } else {
                        ((TextView) dialog.findViewById(R.id.progress_content)).setText("请求服务器...");
                    }
                    break;
            }
        }
    };

    private void initData() {

        mInfo = new ConnectionInfo(serverIp, port);
        mOkOptions = new OkSocketOptions.Builder()
                .setReconnectionManager(new NoneReconnect())
                .setWritePackageBytes(1024)
                .build();
        //将新的修改后的参配设置给连接管理器
        mManager = open(mInfo).option(mOkOptions);

        mManager.registerReceiver(adapter);
    }

    /**
     * 处理服务器返回
     *
     * @param bytes
     */
    private void serverRsp(byte[] bytes) {
        if (bytes[1] == 0x00) {
            return;
        }
        LogUtils.e(TUtil.bytes2HexString(bytes));
        LogUtils.e(bytes.length);
        String command = TUtil.bytes2HexString(TUtil.subBytes(bytes, 0, 2));
        if (!command.equals("FE02") && !command.equals("FE04")) {   //统一处理序列号 排除签到的两次返回
            mManager.disconnect();
            index++;
        }
        switch (command) {
            case "FE02":
                rReaderSign2(bytes);
                break;

            case "FE04":
                byte[] signResult = TUtil.subBytes(bytes, 0, 9);
                if (signResult[8] == 0x00) {
                    signSuccess = true;
                    ToastUtil.showSuccess(mContext, "设备签到成功");
                    index = 0;
                    mOpt = new byte[]{0x00};
                    rPubQryPhysicsInfo();
                } else {
                    signSuccess = false;
                    ToastUtil.showEorr(mContext, "设备签到失败，请重试");
                }
                dialog.dismiss();
                mManager.disconnect();

                break;

            case "AA52":      //P_TRAN_QRY
//                showDialog("查询交易类型...");
                byte[] deByte52 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 96), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e("交易类型查询返回：" + TUtil.bytes2HexString(deByte52));
                byte[] tranType = TUtil.subBytes(deByte52, 4, 1);
                LogUtils.e("交易类型：" + TUtil.bytes2HexString(TUtil.subBytes(deByte52, 4, 1)));
                byte[] tran_info = TUtil.subBytes(deByte52, 5, 48);
                LogUtils.e("查询信息" + TUtil.bytes2HexString(tran_info));
                /**
                 * 00：M1 钱包（查询余额、 圈存和撤销）
                 02：M1 用户账户充值
                 20：CPU 钱包充值（查询余额、圈存和撤销）
                 21：OC3.CPU 圈存（M1余额转移）
                 22：OC3.CPU 圈存（CPU用户账户充值）
                 30：执行扩展应用操作
                 31：执行扩展应用查询
                 40：黑名单处理
                 60：CPU 卡同步
                 */
                switch (tranType[0]) {
                    case 0x00:
                        optType = "M1";
                        setReaderStep = 1;
                        break;
                    case 0x02:
                        break;
                    case 0x20:
                        optType = "CPU";
                        break;
                    case 0x21:
                        optType = "CPU";
                        break;
                    case 0x22:
                        optType = "CPU";
                        break;
                    case 0x40:
                        optType = "BLACK";
                        break;
                    case 0x60:
                        optType = "UPDATE";
                        break;
                }
                LogUtils.e("交易类型：" + TUtil.bytes2HexString(tranType));
                LogUtils.e(optType);
                rSetReadCardInfo(tran_info);
                break;

            case "AA82":    //P_CPU_LOAD_QRY
                showDialog("请求服务器...");
                LogUtils.e("" + TUtil.bytes2HexString(TUtil.subBytes(bytes, 16, 176)));
                byte[] deByte = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 176), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e(TUtil.bytes2HexString(deByte));
                byte[] shid = TUtil.subBytes(bytes, 8, 8);
                mSysOrderNo = TUtil.subBytes(deByte, 0, 8);
                byte[] back_code = TUtil.subBytes(deByte, 8, 4);
                byte[] tran_info1 = TUtil.subBytes(deByte, 12, 64);
                byte[] sys_time = TUtil.subBytes(deByte, 76, 7);
                byte[] remark = TUtil.subBytes(deByte, 83, 48);

                LogUtils.e("返回码：" + TUtil.bytes2HexString(back_code));
                LogUtils.e("系统时间：" + TUtil.bytes2HexString(sys_time));

                if (TUtil.bytes2HexString(back_code).equals("00000000")) {
                    rCpuLoadInit(tran_info1);       //初始化CPU卡
                    updateRecharge = true;
                } else {
                    byte[] error = TUtil.subBytes(deByte, 12, 164);
                    LogUtils.e(TUtil.bytes2HexString(error));
                    String errorStr = TUtil.bytes2HexString(error);
                    String su = errorStr.substring(0, errorStr.indexOf("0000"));
                    LogUtils.e(su);
                    try {
                        ToastUtil.showEorr(mContext, TUtil.hexStr2Str(su));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
                break;
            case "AA84":        //P_CPU_LOAD
                showDialog("提交充值信息...");
                LogUtils.e("" + TUtil.bytes2HexString(TUtil.subBytes(bytes, 16, 208)));
                byte[] deByte84 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 208), TUtil.subBytes(mSk, 0, 16));
                byte[] back_code2 = TUtil.subBytes(deByte84, 8, 4);
                byte[] tran_info2 = TUtil.subBytes(deByte84, 12, 32);
                mTranTime = TUtil.subBytes(deByte84, 44, 7);
                mRechargeMoney = TUtil.subBytes(deByte84, 51, 4);
                mRechargeAuthCode = TUtil.subBytes(deByte84, 55, 4);
                LogUtils.e("交易信息：" + TUtil.bytes2HexString(tran_info2));
                LogUtils.e("返回码：" + TUtil.bytes2HexString(back_code2));
                LogUtils.e("交易金额：" + TUtil.bytes2HexString(mRechargeMoney));
                LogUtils.e("交易认证号：" + TUtil.bytes2HexString(mRechargeAuthCode));
                if (TUtil.bytes2HexString(back_code2).equals("00000000")) {
                    Record record = App.getDaoInstant().getRecordDao().load(recordId);
                    record.setVoucher_no(TUtil.bytes2HexString(mRechargeAuthCode));
                    App.getDaoInstant().getRecordDao().update(record);
                    rCpuLoad(tran_info2);
                } else {
                    //如果终端收到充值服务器返回的错误报文，直接报错提示充值失败,并且删除不完整交易记录,无需后面的流程。
                    LogUtils.e("" + TUtil.bytes2HexString(deByte84));
                    App.getDaoInstant().getRecordDao().deleteAll();      //删除不完整记录
                    byte[] error = TUtil.subBytes(deByte84, 12, 196);
                    LogUtils.e(TUtil.bytes2HexString(error));
                    String errorStr = TUtil.bytes2HexString(error);
                    String su = errorStr.substring(0, errorStr.indexOf("0000"));
                    try {
                        LogUtils.e(TUtil.hexStr2Str(su));
                        ToastUtil.showEorr(mContext, TUtil.hexStr2Str(su));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
                break;
            case "AA86":
                cpuLoadSubmit = false;
                showDialog("等待系统处理...");
                LogUtils.e("" + TUtil.bytes2HexString(TUtil.subBytes(bytes, 16, 64)));
                byte[] deByte86 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 64), TUtil.subBytes(mSk, 0, 16));
                byte[] tran_status = TUtil.subBytes(deByte86, 12, 1);
                byte[] back_code86 = TUtil.subBytes(deByte86, 8, 4);
                if (TUtil.bytes2HexString(back_code86).equals("00000000")) {
                    App.getDaoInstant().getRecordDao().deleteAll();
                    ToastUtil.showSuccess(mContext, "圈存提交成功");
                    if (rechargeStatus && TUtil.bytes2HexString(tran_status).equals("00")) {

                    } else {
                    }

                    if (m12cpu) {
                        if (mMoney > 0) {
                            startRecharge = true;
                            rCpuLoad = false;
                            updateRecharge = false;
                            recharge = true;
                            rPubQryPhysicsInfo();
                        }
                        m12cpu = false;
                        dialog.dismiss();
                        return;
                    }
                } else {
                    byte[] error = TUtil.subBytes(deByte86, 12, 52);
                    LogUtils.e(TUtil.bytes2HexString(error));
                    String errorStr = TUtil.bytes2HexString(error);
                    String su = errorStr.substring(0, errorStr.indexOf("0000"));
                    try {
                        LogUtils.e(TUtil.hexStr2Str(su));
                        ToastUtil.showEorr(mContext, TUtil.hexStr2Str(su));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
//                    payOrder(1);
                }
                rCpuLoad = false;
                if (mOpt[0] == 0x04) {
                    pAcctQry();
                }
                dialog.dismiss();
                break;

            case "AA62":      //P_M1_LOAD_QRY
                showDialog("请求服务器...");
                LogUtils.e("" + TUtil.bytes2HexString(TUtil.subBytes(bytes, 16, 112)));
                byte[] deByte62 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 112), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e(TUtil.bytes2HexString(deByte62));
                mSysOrderNo = TUtil.subBytes(deByte62, 0, 8);
                byte[] back_code62 = TUtil.subBytes(deByte62, 8, 4);
                byte[] sys_time62 = TUtil.subBytes(deByte62, 12, 7);
                byte[] tran_info62 = TUtil.subBytes(deByte62, 19, 48);

                LogUtils.e("系统流水号：" + TUtil.bytes2HexString(mSysOrderNo));
                LogUtils.e("返回码：" + TUtil.bytes2HexString(back_code62));
                LogUtils.e("系统时间：" + TUtil.bytes2HexString(sys_time62));

                if (TUtil.bytes2HexString(back_code62).equals("00000000")) {
                    if (optType.equals("M1")) {
                        rSetReadCardInfo(tran_info62);
                    }
                } else {
                    byte[] error = TUtil.subBytes(deByte62, 12, 100);
                    String errorStr = TUtil.bytes2HexString(error);
                    int index = errorStr.indexOf("00");
                    String substr = errorStr.substring(0, index);
                    LogUtils.e(substr);
                    try {
                        ToastUtil.showEorr(mContext, TUtil.hexStr2Str(substr));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
                break;

            case "AA64":
                showDialog("提交充值信息...");
                LogUtils.e("" + TUtil.bytes2HexString(TUtil.subBytes(bytes, 16, 192)));
                byte[] deByte64 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 192), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e(TUtil.bytes2HexString(deByte64));
                byte[] back_code64 = TUtil.subBytes(deByte64, 8, 4);
                byte[] tran_info64 = TUtil.subBytes(deByte64, 12, 24);
                byte[] tran_time = TUtil.subBytes(deByte64, 36, 7);
                mTranTime = tran_time;
                mRechargeAuthCode = TUtil.subBytes(deByte64, 43, 4);
                LogUtils.e("返回码：" + TUtil.bytes2HexString(back_code64));
                LogUtils.e("系统时间：" + TUtil.bytes2HexString(tran_time));
                if (TUtil.bytes2HexString(back_code64).equals("00000000")) {
                    Record record = App.getDaoInstant().getRecordDao().load(recordId);
                    record.setVoucher_no(TUtil.bytes2HexString(mRechargeAuthCode));
                    App.getDaoInstant().getRecordDao().update(record);
                    rM1Load(tran_time, tran_info64);
                } else {
                    //如果终端收到充值服务器返回的错误报文，直接报错提示充值失败，并且删除不完整交易记录,无需后面的流程。
                    byte[] error = TUtil.subBytes(deByte64, 12, 180);
                    String errorStr = TUtil.bytes2HexString(error);
                    int index = errorStr.indexOf("00");
                    String substr = errorStr.substring(0, index);
                    LogUtils.e(substr);
                    try {
                        ToastUtil.showEorr(mContext, TUtil.hexStr2Str(substr));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    App.getDaoInstant().getRecordDao().deleteAll();     //  删除不完整记录
                    dialog.dismiss();
                }
                break;

            case "AA12":
                showDialog("提交充值信息...");
                byte[] deByte12 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 112), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e(TUtil.bytes2HexString(deByte12));
                mSysOrderNo = TUtil.subBytes(deByte12, 0, 8);
                byte[] back_code12 = TUtil.subBytes(deByte12, 8, 4);
                byte[] tran_info12 = TUtil.subBytes(deByte12, 12, 64);
                LogUtils.e("返回码：" + TUtil.bytes2HexString(back_code12));
                LogUtils.e("交易信息：" + TUtil.bytes2HexString(tran_info12));
                if (TUtil.bytes2HexString(back_code12).equals("00000000")) {

                } else {
                    byte[] error = TUtil.subBytes(deByte12, 12, 50);
                    String errorStr = TUtil.bytes2HexString(error);
                    int index = errorStr.indexOf("00");
                    String substr = errorStr.substring(0, index);
                    LogUtils.e(substr);
                    try {
                        ToastUtil.showEorr(mContext, TUtil.hexStr2Str(substr));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "AB06":  //  P_SET_BLACKLIST  捕获黑名单
                byte[] deByte06 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 64), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e(TUtil.bytes2HexString(deByte06));
                mSysOrderNo = TUtil.subBytes(deByte06, 0, 8);
                byte[] back_no = TUtil.subBytes(deByte06, 8, 4);
                byte[] tran_info06 = TUtil.subBytes(deByte06, 12, 32);
                if (TUtil.bytes2HexString(back_no).equals("00000000")) {
                    rSetBlackList(tran_info06);
                } else {
                    byte[] error = TUtil.subBytes(deByte06, 12, 52);
                    String errorStr = TUtil.bytes2HexString(error);
                    int index = errorStr.indexOf("00");
                    String substr = errorStr.substring(0, index);
                    try {
                        NoticeDialog dialog = new NoticeDialog(mContext, TUtil.hexStr2Str(substr), null);
                        dialog.show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
                break;

            case "AB08":

                break;

            case "AB02":
                showDialog("正在执行卡升级...");
                byte[] deByte02 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 192), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e(TUtil.bytes2HexString(deByte02));
                mSysOrderNo = TUtil.subBytes(deByte02, 0, 8);
                byte[] back_no02 = TUtil.subBytes(deByte02, 8, 4);
                byte[] next_code = TUtil.subBytes(deByte02, 12, 1);
                byte[] instruct = TUtil.subBytes(deByte02, 13, 64);
                LogUtils.e(TUtil.bytes2HexString(back_no02));
                LogUtils.e(TUtil.bytes2HexString(next_code));
                if (TUtil.bytes2HexString(back_no02).equals("00000000")) {
                    /**
                     * 00：卡片同步和余额转移 已完成，请继续普通充值。
                     01：还有后续卡片同步操作（终端重复 AB01 相关操作），先执行同步指令，失败执行充值类型查询，同步指令成功再执行卡片同步操作。
                     02：卡片同步已完成，请终端发起 M1 余额转移，先执行同步指令再执行M1 余额转移操作。
                     03：卡片同步未完成，异常退出。
                     */
                    switch (next_code[0]) {
                        case 0x00:
                            m12cpu = false;
                            if (mMoney > 0) {
                                setReaderStep = 1;
                                startRecharge = true;
                                rCpuLoad = false;
                                updateRecharge = false;
                                recharge = true;
                                rPubQryPhysicsInfo();
                                ToastUtil.showEorr(mContext, "卡片升级完成，继续充值");
                            } else {
                                ToastUtil.showEorr(mContext, "卡片同步完成");
                            }
                            dialog.dismiss();
                            break;
                        case 0x01:
                            rAppCardUpgrade(instruct);
                            break;
                        case 0x02:
                            m12cpu = true;
                            rAppCardUpgrade(instruct);
                            break;
                        case 0x03:
                            dialog.dismiss();
                            ToastUtil.showEorr(mContext, "卡片同步未完成，请重新操作");
                            break;
                    }
                } else {
                    byte[] error = TUtil.subBytes(deByte02, 12, 180);
                    LogUtils.e(TUtil.bytes2HexString(error));
                    String errorStr = TUtil.bytes2HexString(error);
                    String su = errorStr.substring(0, errorStr.indexOf("0000"));
                    LogUtils.e(su);
                    try {
                        ToastUtil.showEorr(mContext, TUtil.hexStr2Str(su));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }

                break;

            case "AB32":
                showDialog("查询充值金余额...");
                byte[] deByte32 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 64), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e(TUtil.bytes2HexString(deByte32));
                mSysOrderNo = TUtil.subBytes(deByte32, 4, 8);
                byte[] back_code32 = TUtil.subBytes(deByte32, 12, 4);
                LogUtils.e(TUtil.bytes2HexString(back_code32));
                if (TUtil.bytes2HexString(back_code32).equals("00000000")) {
                    byte[] balance = TUtil.subBytes(deByte32, 16, 4);
                    byte[] need = TUtil.subBytes(deByte32, 20, 1);
                    byte[] flag = TUtil.subBytes(deByte32, 21, 1);
                    byte[] status = TUtil.subBytes(deByte32, 22, 1);
                    byte[] p = TUtil.subBytes(deByte32, 23, 1);
                    byte[] ed = TUtil.subBytes(deByte32, 24, 4);
                    etPreBalance.setText(Arith.div(Integer.parseInt(TUtil.bytes2HexString(balance), 16), 100) + "");
                    LogUtils.e("余额：" + TUtil.bytes2HexString(balance) + "密码" + TUtil.bytes2HexString(need) + "标识" + TUtil.bytes2HexString(flag) + "状态" + TUtil.bytes2HexString(status) + "频度" + TUtil.bytes2HexString(p) + "额度" + TUtil.bytes2HexString(ed));
                } else {
                    byte[] error = TUtil.subBytes(deByte32, 16, 48);
                    String errorStr = TUtil.bytes2HexString(error);
                    int index = errorStr.indexOf("00");
                    String substr = errorStr.substring(0, index);
                    etPreBalance.setText("0.00");
                    try {
                        LogUtils.e(TUtil.hexStr2Str(substr));
                        ToastUtil.showToast(mContext, TUtil.hexStr2Str(substr));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                dialog.dismiss();

                rPubQryAppInfo();

                if (isFirst) {
                    exceptionHandle();
                    isFirst = false;
                }
                break;

            case "AA66":
                byte[] deByte65 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 80), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e(TUtil.bytes2HexString(deByte65));
                mSysOrderNo = TUtil.subBytes(deByte65, 0, 8);
                byte[] back_no65 = TUtil.subBytes(deByte65, 8, 4);
                if (TUtil.bytes2HexString(back_no65).equals("00000000")) {
                    //冲正完成清除不完整记录
//                    App.getDaoInstant().getRecordDao().deleteAll();
                    ToastUtil.showSuccess(mContext, "冲正完成");
                    payOrder(-1);
                } else {
                    byte[] error = TUtil.subBytes(deByte65, 12, 68);
                    LogUtils.e(TUtil.bytes2HexString(error));
                    String errorStr = TUtil.bytes2HexString(ByteUtil.trimRightBytes(error));
                    LogUtils.e(errorStr);
                    try {
                        ToastUtil.showEorr(mContext, TUtil.hexStr2Str(errorStr));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    payOrder(-1);
                }
                dialog.dismiss();
                break;
        }
    }

    /**
     * 处理读卡器返回结果
     *
     * @param bytes
     */
    private void readerRsp(final byte[] bytes) {
        /**
         * 临时处理升级指令串口分段返回问题
         */
        if (bytes[2] == 0x25) {
            updateRes = true;
            update = bytes;
            return;
        }
        if (updateRes) {
            byte[] newByte = TUtil.byteMergerAll(update, bytes);
            updateRes = false;
            LogUtils.e("卡同步查询" + TUtil.bytes2HexString(newByte));
            mPhysicsNo = TUtil.subBytes(newByte, 8, 8);
            mLogicNo = TUtil.subBytes(newByte, 16, 8);
            byte[] random = TUtil.subBytes(newByte, 24, 8);
            byte[] card_info25 = TUtil.subBytes(newByte, 32, 200);
            LogUtils.e(TUtil.bytes2HexString(mPhysicsNo));
            LogUtils.e(TUtil.bytes2HexString(mLogicNo));
            LogUtils.e(TUtil.bytes2HexString(card_info25));
            pM1ToCpu(random, card_info25);
            return;
        }
        LogUtils.e(rCpuLoad);
        LogUtils.e(mTime);
        if (rCpuLoad) {
            if (TUtil.bytes2HexString(TUtil.subBytes(bytes, 2, 2)).equals("7980") && mTime >= 0) {
                //做 CPU 钱包圈存，圈存指令完毕后， 则 需要重新
                //读取卡余额 （调用 R R _CPU_ GETO _CARDINFO  指令）, , 如果 读卡余额 返回错误码是
                //0x80 ，提示用户再次放卡 ， 读 不到 卡 须 要求重新 读卡 卡 3  次。
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mTime == 0) {
                            mTime = 3;
                            dialog.dismiss();
                            rCpuLoad = false;
                            Record record = App.getDaoInstant().getRecordDao().load(recordId);
                            record.setCard_info(TUtil.bytes2HexString(TUtil.zeros(16)));
                            App.getDaoInstant().getRecordDao().update(record);
                            payOrder(2);
                            pCpuLoadReSubmit();
//                            App.getDaoInstant().getRecordDao().deleteAll();
                        } else {
                            noticeDialog = new NoticeDialog(mContext, "请重新放卡并做读卡操作", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    rCpuGetCardInfo();
                                    mTime--;
                                    noticeDialog.dismiss();
                                }
                            });
                            noticeDialog.show();
                        }
                    }
                });
                return;
            }
//            else {
//                mTime = 3;
//                rCpuLoad = false;
//            }
//            else if (bytes[2] == 0x79 && bytes[3] != 0x00) {
//                //读卡器执行 R_CPU_LOAD 指令后读卡器返回结果不是 0x00 或者卡片返
//                //回码不是 0x9000，则表示圈存失败，再重新读取卡余额后，将结果提交给充值
//                //服务器。终端不能打印圈存单据，提示圈存失败。
//                ToastUtil.showWrning(mContext, "充值失败");
//                rCpuGetCardInfo();
////            App.getDaoInstant().getRecordDao().deleteAll();     //删除不完整记录
//            }
        } else if (rM1Load) {       //如果读卡余额返回错误码是0x80，提示用户再次放卡，读不到卡须要求重新读卡 3次。
            if (TUtil.bytes2HexString(TUtil.subBytes(bytes, 2, 2)).equals("B980") && mTime >= 0) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mTime == 0) {
                            //如果读卡余额失败或者读卡仍无响应，则系统默认该笔充值为疑似成功，打小票的时候加*号。
                            mTime = 3;
                            dialog.dismiss();
                            rM1Load = false;
                            payOrder(2);
                        } else {
                            noticeDialog = new NoticeDialog(mContext, "请重新放卡并做读卡操作", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    rM1GetCardInfo();
                                    mTime--;
                                    noticeDialog.dismiss();
                                }
                            });
                            noticeDialog.show();
                        }
                    }
                });
                return;
            }
//            else {
//                mTime = 3;
//                rM1Load = false;
//            }
//            else if (bytes[2] == 0xBA && bytes[3] != 0x00) {
//                //如果读卡器返回状态码不是 0x00，则表示圈存失败，不需打印单据，但需要立即向充值服务器发起冲正（参见后面描述）。
//                ToastUtil.showWrning(mContext, "充值失败");
//                Record record = App.getDaoInstant().getRecordDao().load(recordId);
//                byte[] reader_back_status_ba = TUtil.subBytes(bytes, 3, 1);
//                if (record != null) {
//                    record.setReader_back_no(TUtil.bytes2HexString(reader_back_status_ba));
//                    App.getDaoInstant().getRecordDao().update(record);
//                    pM1RollBack();
//                    return;
//                }
//            }
//            else if (bytes[3] != 0x00) {
//                //如果读卡器返回状态码不是 0x00，则表示圈存失败，不需打印单据，但需要立即向充值服务器发起冲正
////                Record record = App.getDaoInstant().getRecordDao().load(recordId);
//                pM1RollBack();
//                return;
//            }
        } else if (bytes[2] != 0x7B && bytes[2] != 0xB9 && bytes[2] != 0x79 && bytes[2] != 0xBA) {
            if (bytes[3] != 0x00) {
                LogUtils.e(TUtil.bytes2HexString(bytes));
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        ReaderError.code(mContext, bytes[3]);
                    }
                });
                return;
            }
        }
//        else if (m1LoadSubmit && bytes[3] != 0x00) {      //冲正处理
////            Record record = App.getDaoInstant().getRecordDao().load(recordId);
//            pM1RollBack();
//        }

        switch (bytes[2]) {
            case 0x10:      //  R_INIT ( 初始化 )
                LogUtils.e(TUtil.bytes2HexString(bytes));
                LogUtils.e(bytes.length);
                rReaderSign(bytes);
//                rPubQryPhysicsInfo();
                break;
            case (byte) 0xC8:   //  R_AC_LOGIN_1( 签到步骤 1)
                LogUtils.e(TUtil.bytes2HexString(bytes));
                LogUtils.e(bytes.length);
                rqBytes = TUtil.subBytes(bytes, 4, 136);
                rServerSign1(rqBytes);
//                LogUtils.e(rqBytes.length);
//                LogUtils.e(TUtil.bytes2HexString(rqBytes));
                break;
            case (byte) 0xC9:   //  R_AC_LOGIN_2( 签到步骤 2)
                LogUtils.e(TUtil.bytes2HexString(bytes));
                LogUtils.e(bytes.length);
                mShid = TUtil.subBytes(bytes, 4, 8);
                mSk = TUtil.subBytes(bytes, 12, 32);
                mServerTime = TUtil.subBytes(bytes, 44, 6);
                mLogin3Data = TUtil.subBytes(bytes, 50, 136);

                enShid = encrypt3DES.encrypt(mShid, 16, CHexConver.hexStringToBytes(ck), 32, 1);         //握手流水号 8字节 加密
                rServerSign2(mLogin3Data);
//                rCpuGetCardInfo();
                break;

            case (byte) 0x93:   //  R_PUB_QRY_PHYSICS_INFO( 查询卡物理信息)
                LogUtils.e(TUtil.bytes2HexString(bytes));
                LogUtils.e(bytes.length);
                mPhysicsNo = TUtil.subBytes(bytes, 4, 8);
                mLogicNo = TUtil.subBytes(bytes, 12, 8);
                mSak = TUtil.subBytes(bytes, 20, 1);
                mCardInfo = TUtil.subBytes(bytes, 21, 32);
                mSw1sw2 = TUtil.subBytes(bytes, 53, 2);

                LogUtils.e(TUtil.bytes2HexString(mSak));

                if (setReaderStep == 1) {
                    pTranQry();
//                    pAcctQry();
                    return;
                }

                //M1卡圈存第二次读卡
                if (optType.equals("M1") && setReaderStep == 2) {
                    pM1LoadQry(mCardInfo);
                }
                break;

            case (byte) 0x95:   //  R_PUB_SET_READCARDINFO( 设置读卡信息)
                LogUtils.e(TUtil.bytes2HexString(bytes));
                LogUtils.e(bytes.length);
                byte[] sw1sw2_95 = TUtil.subBytes(bytes, 4, 2);
                LogUtils.e("设置卡信息返回sw1sw2:" + TUtil.bytes2HexString(sw1sw2_95));
                switch (optType) {
                    case "M1":
                        if (mOpt[0] == 0x00) {
                            rM1GetCardInfo();
                        } else if (setReaderStep == 1) {
                            rPubQryPhysicsInfo();
                            setReaderStep = 2;
                        } else if (setReaderStep == 2) {
                            rM1GetCardInfo();
                        }
                        break;
                    case "CPU":
                        rCpuGetCardInfo();
                        break;
                    case "BLACK":
                        rSetBlackListInit();
                        break;
                    case "UPDATE":
                        rCardQryUpgrade();
                        break;
                }

                break;

            case 0x79:      //  R_CPU_GET_CARDINFO (CPU  卡取信息)
                LogUtils.e(TUtil.bytes2HexString(bytes));
                LogUtils.e(bytes.length);
                if (bytes.length != 52 || !TUtil.bytes2HexString(TUtil.subBytes(bytes, 3, 1)).equals("00")) {
                    if (rCpuLoad) {
                        mTime = 3;
                    }
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            noticeDialog = new NoticeDialog(mContext, "请重新放卡并做读卡操作", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    rCpuGetCardInfo();
                                    noticeDialog.dismiss();
                                }
                            });
                            noticeDialog.show();
                        }
                    });
                    return;
                }
                mPhysicsNo = TUtil.subBytes(bytes, 8, 8);
                mLogicNo = TUtil.subBytes(bytes, 16, 8);
                mCardBalance = TUtil.subBytes(bytes, 24, 4);
                mCardStatus = TUtil.subBytes(bytes, 32, 1);
                mCardInfo79 = TUtil.subBytes(bytes, 33, 16);
                mMoneyLower = TUtil.subBytes(bytes, 28, 1);
                mMoneyUpper = TUtil.subBytes(bytes, 29, 3);
                byte[] sw1sw2_79 = TUtil.subBytes(bytes, 49, 2);

                LogUtils.e("CPU卡余额：" + TUtil.bytes2HexString(mCardBalance) + " 卡状态：" + TUtil.bytes2HexString(mCardStatus) + "CPU卡计数信息：" + TUtil.bytes2HexString(mCardInfo79) + "CPU卡取信息sw1sw2：" + TUtil.bytes2HexString(sw1sw2_79));
                LogUtils.e("逻辑卡号：" + TUtil.bytes2HexString(mLogicNo));
                LogUtils.e("物理卡号：" + TUtil.bytes2HexString(mPhysicsNo));

//                finalMoney = Integer.parseInt(TUtil.bytes2HexString(mCardBalance), 16);
                showCardInfo(TUtil.bytes2HexString(mLogicNo),Arith.del(Arith.div(Integer.parseInt(TUtil.bytes2HexString(mCardBalance), 16), 100), lowMoney)+"");
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        etCardNo.setText(TUtil.bytes2HexString(mLogicNo));
                        etBalance.setText(Arith.del(Arith.div(Integer.parseInt(TUtil.bytes2HexString(mCardBalance), 16), 100), lowMoney) + "");
//                        etCardNo.setText(Integer.parseInt(TUtil.bytes2HexString(mLogicNo),16)+"");
                    }
                });

                //查询余额的中断后续操作
                if (mOpt[0] == 0x00) {
                    pAcctQry();
                    return;
                }

                //记录充值前的余额 之后对比余额+充值金额是否等于充值后的余额 如果相当说明充值成功 否则充值失败
                if (startRecharge) {
                    originalMoney = Integer.parseInt(TUtil.bytes2HexString(mCardBalance), 16);
                    startRecharge = false;
                }

                if (recharge) {         //开始CPU卡圈存查询
                    pCpuLoadQry();
                    recharge = false;
                }

                if (updateRecharge) {   //开始CPU卡圈存提交
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
//                            if (bytes[3] == 0x80) {
//                                rCpuLoad = true;
//                                mTime = 3;
//                                noticeDialog = new NoticeDialog(mContext, "请重新放卡并做读卡操作", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        rCpuGetCardInfo();
//                                        noticeDialog.dismiss();
//                                    }
//                                });
//                                noticeDialog.show();
//                                return;
//                            }
//                            if (!TUtil.bytes2HexString(TUtil.subBytes(bytes, 3, 1)).equals("00")) {
//                                rCpuLoad = true;
//                                mTime = 3;
//                                noticeDialog = new NoticeDialog(mContext, "请重新放卡并做读卡操作", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        rCpuGetCardInfo();
//                                        noticeDialog.dismiss();
//                                    }
//                                });
//                                noticeDialog.show();
//                                return;
//                            }
                            if (m12cpu) {
                                if (mMoney > 0) {
                                    ToastUtil.showSuccess(mContext, "卡片同步完成，继续充值");
                                } else {
                                    ToastUtil.showSuccess(mContext, "卡片同步完成");
                                }
                            } else if (Arith.add(originalMoney, Arith.mul(mMoney, 100)) == Integer.parseInt(TUtil.bytes2HexString(mCardBalance), 16)) {
                                //读卡器执行 R_CPU_LOAD 指令后，读卡器返回状态码是 0x00，卡片返回
                                //码是 0x9000，并且比对写卡前后的余额加值正确，则表示圈存成功，终端打印
                                //圈存成功单据。

                                //2、读卡器执行 R_CPU_LOAD 指令后读卡器无响应，再重新读取卡余额后，
                                //如果读余额成功，并且加值正确，则表示圈存成功，终端打印圈存成功单据。
                                ToastUtil.showSuccess(mContext, "充值成功");
                                payOrder(1);
                            } else {
                                payOrder(-1);
                                ToastUtil.showEorr(mContext, "充值失败,请重新充值");
                            }
                        }
                    });

                    cpuLoad = false;
                    mTime = 0;
                    Record record = App.getDaoInstant().getRecordDao().load(recordId);
                    record.setCard_info(TUtil.bytes2HexString(mCardInfo79));
                    App.getDaoInstant().getRecordDao().update(record);
                    pCpuLoadReSubmit();
                }
                break;

            case 0x7A:      //  R_CPU_LOAD_INIT (CPU  钱包圈存初始化)
                LogUtils.e(TUtil.bytes2HexString(bytes));
                LogUtils.e(bytes.length);
                byte[] reader_back_status = TUtil.subBytes(bytes, 3, 1);
                byte[] card_info = TUtil.subBytes(bytes, 6, 80);
                byte[] sw1sw2 = TUtil.subBytes(bytes, 86, 2);
                LogUtils.e("读卡器状态：" + TUtil.bytes2HexString(reader_back_status));
                LogUtils.e("卡信息：" + TUtil.bytes2HexString(card_info));
                LogUtils.e("SW1SW2：" + TUtil.bytes2HexString(sw1sw2));

                mReaderBackStatus = reader_back_status;
                mCardInfo = card_info;
                mSw1sw2 = sw1sw2;
                pCpuLoad(card_info, mSw1sw2, mReaderBackStatus);
                break;

            case 0x7B:      //  R_CPU_LOAD (CPU  钱包圈存)
                /**
                 * 读卡器执行 R_CPU_LOAD 指令后，读卡器返回状态码是 0x00，卡片返回码是 0x9000，并且比对写卡前后的余额加值正确，则表示圈存成功，终端打印圈存成功单据。
                 */
                mReaderBackStatus = TUtil.subBytes(bytes, 3, 1);
                mSw1sw2 = TUtil.subBytes(bytes, 8, 2);
                LogUtils.e("mSw1sw2_7B" + TUtil.bytes2HexString(mSw1sw2));
                if (TUtil.bytes2HexString(mSw1sw2).equals("9000")) {
                    rechargeStatus = true;
                } else {
                    rechargeStatus = false;
//                    ToastUtil.showEorr(mContext, "充值失败");
                }
                cpuLoad = true;
                rCpuLoad = true;
                mTime = 3;
                rCpuGetCardInfo();
                break;

            case (byte) 0xB9:       //R_M1_GET_CARDINFO
                LogUtils.e(TUtil.bytes2HexString(bytes));
                LogUtils.e(bytes.length);
                if (bytes.length != 127 || !TUtil.bytes2HexString(TUtil.subBytes(bytes, 3, 1)).equals("00")) {
                    if (rM1Load) {
                        mTime = 3;
                    }
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            noticeDialog = new NoticeDialog(mContext, "请重新放卡并做读卡操作", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    rM1GetCardInfo();
                                    noticeDialog.dismiss();
                                }
                            });
                            noticeDialog.show();
                        }
                    });
                    return;
                }
                mPhysicsNo = TUtil.subBytes(bytes, 8, 8);
                mLogicNo = TUtil.subBytes(bytes, 16, 8);
                mM1TranInfo = TUtil.subBytes(bytes, 24, 88);
                mCardBalance = TUtil.subBytes(bytes, 112, 4);
                mTranCounter = TUtil.subBytes(bytes, 116, 2);
                mM1Random = TUtil.subBytes(bytes, 118, 8);
                LogUtils.e("M1卡交易信息：" + TUtil.bytes2HexString(mM1TranInfo) + " M1卡余额：" + Arith.div(TUtil.height2low(mCardBalance), 100) + " M1卡计数信息：" + TUtil.bytes2HexString(mTranCounter) + " 随机数：" + TUtil.bytes2HexString(mM1Random));
                LogUtils.e("逻辑卡号：" + TUtil.bytes2HexString(mLogicNo));
                LogUtils.e("物理卡号：" + TUtil.bytes2HexString(mPhysicsNo));
                showCardInfo(TUtil.bytes2HexString(mLogicNo),Arith.del(Arith.div(TUtil.height2low(mCardBalance), 100), lowMoney)+"");
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        etBalance.setText(Arith.del(Arith.div(TUtil.height2low(mCardBalance), 100), lowMoney) + "");
                        etCardNo.setText(TUtil.bytes2HexString(mLogicNo));
                    }
                });

                //查询余额的中断后续操作
                if (mOpt[0] == 0x00) {
                    pAcctQry();
                    return;
                }

                if (recharge) {         //开始M1卡圈存
                    pM1Load();
                    recharge = false;
                    originalMoney = TUtil.height2low(mCardBalance);
                } else {
//                    Record record = App.getDaoInstant().getRecordDao().load(recordId);
                    final double finalMoney = Arith.add(originalMoney, Arith.mul(mMoney, 100));
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            LogUtils.e(finalMoney);
                            LogUtils.e(TUtil.height2low(mCardBalance));
                            m1Load = false;
                            mTime = 0;

//                            if (bytes[3] == 0x80) {
//                                rM1Load = true;
//                                mTime = 3;
//                                noticeDialog = new NoticeDialog(mContext, "请重新放卡并做读卡操作", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        rM1GetCardInfo();
//                                        noticeDialog.dismiss();
//                                    }
//                                });
//                                noticeDialog.show();
//                                return;
//                            }

                            if (Arith.add(originalMoney, Arith.mul(mMoney, 100)) == TUtil.height2low(mCardBalance)) {
                                payOrder(1);
                                ToastUtil.showSuccess(mContext, "充值成功");
                                App.getDaoInstant().getRecordDao().deleteAll();
                                if (mOpt[0] == 0x04) {
                                    pAcctQry();
                                }
                            } else {
                                ToastUtil.showEorr(mContext, "充值失败,请重新充值");
                                Record record = App.getDaoInstant().getRecordDao().load(recordId);
                                record.setCard_info(TUtil.bytes2HexString(mM1TranInfo));
                                App.getDaoInstant().getRecordDao().update(record);
                                pM1RollBack();
                            }
                        }
                    });
                }
                break;

            case (byte) 0xBA:       //R_M1_LOAD(M1  充值)
                LogUtils.e(TUtil.bytes2HexString(bytes));
//                byte[] reader_back_status_ba = TUtil.subBytes(bytes, 3, 1);
//                Record record = App.getDaoInstant().getRecordDao().load(recordId);
//                record.setReader_back_no(TUtil.bytes2HexString(reader_back_status_ba));
//                App.getDaoInstant().getRecordDao().update(record);
                m1Load = true;
                mTime = 3;
                rM1GetCardInfo();
                break;

            case (byte) 0x94:   //R_PUB_QRY_APP_INFO( 查询卡应用信息)
                LogUtils.e(TUtil.bytes2HexString(bytes));
                mPhysicsNo = TUtil.subBytes(bytes, 4, 8);
                mLogicNo = TUtil.subBytes(bytes, 12, 8);
                mSak = TUtil.subBytes(bytes, 20, 1);
                byte[] cardAppInfo = TUtil.subBytes(bytes, 21, 40);
                mCardBalance = TUtil.subBytes(bytes, 61, 4);
                mMoneyLower = TUtil.subBytes(bytes, 65, 1);
                mMoneyUpper = TUtil.subBytes(bytes, 66, 3);
                LogUtils.e(TUtil.bytes2HexString(mCardBalance));

                LogUtils.e("卡余额" + TUtil.bytes2HexString(mCardBalance) + "上限" + TUtil.bytes2HexString(mMoneyUpper) + "下限" + TUtil.bytes2HexString(mMoneyLower));
                if (optType.equals("CPU")) {
                    upMoney = Arith.div(Integer.parseInt(TUtil.bytes2HexString(mMoneyUpper), 16), 100);
                    lowMoney = Integer.parseInt(TUtil.bytes2HexString(mMoneyLower), 16);
                    LogUtils.e("卡类型" + optType + "卡余额" + Arith.div(Integer.parseInt(TUtil.bytes2HexString(mCardBalance), 16), 100) + "上限" + upMoney + "下限" + lowMoney);
                } else {
                    upMoney = Integer.parseInt(TUtil.bytes2HexString(mMoneyUpper), 16);
                    lowMoney = Integer.parseInt(TUtil.bytes2HexString(mMoneyLower), 16);
                    LogUtils.e("卡类型" + optType + "卡余额" + Arith.div(Integer.parseInt(TUtil.bytes2HexString(mCardBalance), 16), 100) + "上限" + Integer.parseInt(TUtil.bytes2HexString(mMoneyUpper), 16) + "下限" + Arith.div(Integer.parseInt(TUtil.bytes2HexString(mMoneyLower), 16), 100));
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        etBalance.setText(Arith.div(Integer.parseInt(TUtil.bytes2HexString(mCardBalance), 16), 100) + "");
//                        etCardNo.setText(TUtil.bytes2HexString(mLogicNo));
                        dialog.dismiss();
                        getCardInfo = false;
                    }
                });
                break;

            case 0x21:      //  R_SET_BLACKLIST_INIT( 置黑名单卡初始化)
                mPhysicsNo = TUtil.subBytes(bytes, 4, 8);
                mLogicNo = TUtil.subBytes(bytes, 12, 8);
                byte[] blackCardInfo = TUtil.subBytes(bytes, 20, 16);
                byte[] sw1sw221 = TUtil.subBytes(bytes, 36, 2);
                pSetBlackList(blackCardInfo);
                break;

            case 0x22:      //  R_SET_BLACKLIST( 置黑名卡)
                byte[] reader_status = TUtil.subBytes(bytes, 3, 1);
                byte[] sw1sw222 = TUtil.subBytes(bytes, 4, 2);
                pBlackListSubmit(sw1sw222, reader_status);
                break;

            case 0x25:      //  R_CARD_QRY_UPGRADE( CPU  卡同步查询)
                byte[] newByte = TUtil.byteMergerAll(update, bytes);
                updateRes = false;
//                if (bytes.length < 228) {
//                    mainHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            ToastUtil.showEorr(mContext, "串口返回长度不对");
//                        }
//                    });
//                    return;
//                }
                LogUtils.e("卡同步查询" + TUtil.bytes2HexString(newByte));
                mPhysicsNo = TUtil.subBytes(newByte, 4, 8);
                mLogicNo = TUtil.subBytes(newByte, 12, 8);
                byte[] random = TUtil.subBytes(newByte, 20, 8);
                byte[] card_info25 = TUtil.subBytes(newByte, 28, 200);
                pM1ToCpu(random, card_info25);
                break;

            case 0x26:  //R_APP_CARD_ UPGRADE
                if (m12cpu) {
                    /**
                     * 卡间余额转移和同步过程中的 M1 余额转移过程中不需要执行交易类型查询
                     */
                    recharge = true;
                    rCpuGetCardInfo();
                    return;
                }
                rCardQryUpgrade();
                break;

            default:

        }
    }

    /**
     * 终端->读卡器 初始化
     * R_INIT
     */
    private void rInit() {
        dialog.show();
        byte[] sendContentBytes = {(byte) 0xBA, 0x11, 0x10, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xB9};
        boolean res = mSerialPortManager.sendBytes(sendContentBytes);
//        LogUtils.e(TUtil.bytes2HexString(mSerialPortManager.sendAndWaitBack(sendContentBytes, 1000)));
        first = false;
    }

    /**
     * 终端->读卡器 签到步骤1
     * R_AC_LOGIN_1
     *
     * @param bytes
     */
    private void rReaderSign(byte[] bytes) {
//        LogUtils.e(bytesToHexString(bytes));
        byte[] head = {(byte) 0xBA, 0x13, (byte) 0xC8};
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = df.format(new Date());
        byte[] a = TUtil.addBytes(head, TUtil.subBytes(bytes, 4, 4));
        byte[] b = TUtil.addBytes(a, TUtil.addBytes(TUtil.hexString2Bytes(readerId), TUtil.hexString2Bytes(date)));
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(b);
        byte[] send = TUtil.addBytes(b, or);
        LogUtils.e(TUtil.bytes2HexString(send));
        mSerialPortManager.sendBytes(send);
    }

    /**
     * 终端->读卡器 签到步骤2
     * R_AC_LOGIN_2
     *
     * @param bytes
     */
    private void rReaderSign2(byte[] bytes) {
        LogUtils.e(TUtil.bytes2HexString(bytes));
        byte[] head = {(byte) 0xBA, (byte) 0x8a, (byte) 0xC9};
        byte[] b = TUtil.addBytes(head, bytes);
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(b);
        byte[] send = TUtil.addBytes(b, or);
        LogUtils.e(TUtil.bytes2HexString(send));
        LogUtils.e(send.length);
        mSerialPortManager.sendBytes(send);
    }

    /**
     * 终端->读卡器 CPU卡信息
     * R_CPU_GET_CARDINFO
     */
    private void rCpuGetCardInfo() {
        byte[] sendContentBytes = {(byte) 0xBA, (byte) 0x02, 0x79};
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(sendContentBytes);
        byte[] send = TUtil.addBytes(sendContentBytes, or);
        LogUtils.e(TUtil.bytes2HexString(send));
        mSerialPortManager.sendBytes(send);
    }

    /**
     * 终端->读卡器 查询物理卡信息
     * R_PUB_QRY_PHYSICS_INFO
     */
    private void rPubQryPhysicsInfo() {

        byte[] sendContentBytes = {(byte) 0xBA, (byte) 0x02, (byte) 0x93};
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(sendContentBytes);
        byte[] send = TUtil.addBytes(sendContentBytes, or);
        LogUtils.e(TUtil.bytes2HexString(send));
        mSerialPortManager.sendBytes(send);
    }

    /**
     * 终端->读卡器 CPU卡圈存初始化
     * R_CPU_LOAD_INIT
     */
    private void rCpuLoadInit(byte[] bytes) {
        LogUtils.e("交易信息1：" + TUtil.bytes2HexString(bytes));
        byte[] head = {(byte) 0xBA, (byte) 0x42, (byte) 0x7A};
        byte[] tran_info = bytes;
        byte[] sendContentBytes = TUtil.byteMergerAll(head, tran_info);
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(sendContentBytes);
        byte[] send = TUtil.addBytes(sendContentBytes, or);
        LogUtils.e(TUtil.bytes2HexString(send));
        mSerialPortManager.sendBytes(send);
    }

    /**
     * 终端->读卡器 CPU卡圈存
     * R_CPU_LOAD
     */
    private void rCpuLoad(byte[] bytes) {
        LogUtils.e(TUtil.bytes2HexString(bytes));
        byte[] head = {(byte) 0xBA, (byte) 0x22, (byte) 0x7B};
        byte[] tran_info = bytes;
        byte[] sendContentBytes = TUtil.byteMergerAll(head, tran_info);
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(sendContentBytes);
        byte[] send = TUtil.addBytes(sendContentBytes, or);
        LogUtils.e(TUtil.bytes2HexString(send));
        boolean res = mSerialPortManager.sendBytes(send);
        rCpuLoad = true;

        if (!res) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    cpuReadNoRspOrServerNoRsp();
                    ToastUtil.showWrning(mContext, "读卡器写入失败，充值失败...");
                }
            });
        }
    }

    /**
     * 终端->读卡器 设置读卡器信息
     * R_PUB_SET_READCARDINFO
     */
    private void rSetReadCardInfo(byte[] bytes) {
        byte[] head = {(byte) 0xBA, (byte) 0x3a, (byte) 0x95};
        byte[] sendContentBytes = TUtil.byteMergerAll(head, mLogicNo, bytes);
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(sendContentBytes);
        byte[] send = TUtil.addBytes(sendContentBytes, or);
        LogUtils.e(TUtil.bytes2HexString(send));
        mSerialPortManager.sendBytes(send);
    }

    /**
     * 终端->读卡器 M1  取卡信息
     * R_M1_GET_CARDINFO
     */
    private void rM1GetCardInfo() {
        byte[] sendContentBytes = {(byte) 0xBA, (byte) 0x02, (byte) 0xB9};
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(sendContentBytes);
        byte[] send = TUtil.addBytes(sendContentBytes, or);
        mSerialPortManager.sendBytes(send);
    }

    /**
     * 终端->读卡器 M1  充值
     * R_M1_LOAD
     */
    private void rM1Load(byte[] tran_time, byte[] tran_info) {
        byte[] sendContentBytes = {(byte) 0xBA, 0x21, (byte) 0xBA};
        byte[] content = TUtil.byteMergerAll(sendContentBytes, tran_time, tran_info);
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(content);
        byte[] send = TUtil.addBytes(content, or);
        boolean res = mSerialPortManager.sendBytes(send);
        LogUtils.e(TUtil.bytes2HexString(send));
        rM1Load = true;
        //如果发送指令不成功，发起冲正
        if (!res) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    m1ReadNoRspOrServerNoRsp();
                    ToastUtil.showWrning(mContext, "读卡器写入失败，充值失败...");
                }
            });
        }
    }

    /**
     * 查询卡应用信息
     * R_PUB_QRY_APP_INFO
     */
    private void rPubQryAppInfo() {
        byte[] sendContentBytes = {(byte) 0xBA, 0x02, (byte) 0x94};
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(sendContentBytes);
        byte[] send = TUtil.addBytes(sendContentBytes, or);
        mSerialPortManager.sendBytes(send);
        LogUtils.e(TUtil.bytes2HexString(send));
    }

    /**
     * 置黑名单卡初始化
     * R_SET_BLACKLIST_INIT
     */
    private void rSetBlackListInit() {
        byte[] sendContentBytes = {(byte) 0xBA, 0x02, (byte) 0x21};
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(sendContentBytes);
        byte[] send = TUtil.addBytes(sendContentBytes, or);
        mSerialPortManager.sendBytes(send);
        LogUtils.e(TUtil.bytes2HexString(send));
    }

    /**
     * 置黑名卡
     * R_SET_BLACKLIST
     */
    private void rSetBlackList(byte[] bytes) {
        byte[] sendContentBytes = {(byte) 0xBA, 0x22, (byte) 0x22};
        byte[] content = TUtil.byteMergerAll(sendContentBytes, bytes);
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(content);
        byte[] send = TUtil.addBytes(content, or);
        mSerialPortManager.sendBytes(send);
        LogUtils.e(TUtil.bytes2HexString(send));
    }

    /**
     * CPU  卡同步查询
     * R_CARD_QRY_UPGRADE
     */
    private void rCardQryUpgrade() {
        byte[] sendContentBytes = {(byte) 0xBA, 0x02, (byte) 0x25};
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(sendContentBytes);
        byte[] send = TUtil.addBytes(sendContentBytes, or);
        mSerialPortManager.sendBytes(send);
        LogUtils.e(TUtil.bytes2HexString(send));
    }

    /**
     * 卡同步应用
     * R_APP_CARD_UPGRADE
     */
    private void rAppCardUpgrade(byte[] bytes) {
        byte[] sendContentBytes = {(byte) 0xBA, 0x42, (byte) 0x26};
        byte[] content = TUtil.byteMergerAll(sendContentBytes, bytes);
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(content);
        byte[] send = TUtil.addBytes(content, or);
        mSerialPortManager.sendBytes(send);
        LogUtils.e(TUtil.bytes2HexString(send));
    }

    /**
     * 用户卡信息认证
     * R_CARD_VOUCHER
     */
    private void rCardVoucher() {
        byte[] sendContentBytes = {(byte) 0xBA, 0x22, (byte) 0x2D};
        byte[] content = TUtil.byteMergerAll(sendContentBytes, TUtil.zeros(32));
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(content);
        byte[] send = TUtil.addBytes(content, or);
        mSerialPortManager.sendBytes(send);
        LogUtils.e(TUtil.bytes2HexString(send));
    }


    /**
     * 终端->服务器 签到步骤1
     * P_LOGIN
     *
     * @param bytes
     */
    private void rServerSign1(final byte[] bytes) {
        App.getInstance().setLength(136);
        MsgDataBean msgDataBean = new MsgDataBean(bytes);
        mManager.send(msgDataBean);
    }

    /**
     * 终端->服务器 签到步骤2
     * P_LOGIN
     *
     * @param bytes
     */
    private void rServerSign2(byte[] bytes) {
        App.getInstance().setLength(9);
        //将新的修改后的参配设置给连接管理器
        MsgDataBean msgDataBean = new MsgDataBean(bytes);
        mManager.send(msgDataBean);
    }

    /**
     * 终端->服务器 交易类型查询
     * P_TRAN_QRY
     */
    private void pTranQry() {
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        byte[] head = {(byte) 0xAA, 0x51, 0x00, 0x03, 0x02, (byte) 0x80, 0x00, (byte) 0x68};
        byte[] shid = mShid;
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] sak = mSak;
        byte[] card_info = mCardInfo;
        byte[] operation = mOpt;
        byte[] rfu = TUtil.zeros(34);
        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, physics_no, logic_no, sak, card_info, operation, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);
        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, physics_no, logic_no, sak, card_info, operation, rfu, check_no);
        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        sentData(112, all);
    }

    /**
     * 终端->服务器 CPU卡圈存查询
     * P_CPU_LOAD_QRY
     */
    private void pCpuLoadQry() {
        /**
         * 圈存金额，单位：分，如果应用类型是 00、01，填入充值金额，金额是 50的 整 数 倍 。
         * 20 填0x00000000 表示充值金账户所有余额充值，
         * 22 填0x00000000 表示签约额度充值，可以填入充值金额，金额是 50 的整数倍。
         应用类型为 0x10、0x30或 0x40 时，该域全填0x00
         */
        byte[] optType = {0x00};
        byte[] tran_money = TUtil.hexString2Bytes(getMoney(mMoney));
        if (mOpt[0] == 0x04) {
            optType = new byte[]{0x20};
        }
        if (m12cpu) {
            optType = new byte[]{0x10};
            tran_money = new byte[]{0x00, 0x00, 0x00, 0x00};
        }
        LogUtils.e("卡类型：" + TUtil.bytes2HexString(mCardStatus));
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        LogUtils.e("卡信息：" + TUtil.bytes2HexString(mCardInfo79));
        LogUtils.e("握手流水号：" + TUtil.bytes2HexString(mShid));
        LogUtils.e("卡状态：" + TUtil.bytes2HexString(mCardStatus));
        LogUtils.e("mPki：" + mPki);
        byte[] head = {(byte) 0xAA, (byte) 0x81, 0x00, 0x01, 0x02, (byte) 0x80, 0x00, (byte) 0x88};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] card_status = mCardStatus;    //卡状态 1字节  00 M1卡 01CPU卡 02CUP M1未锁定 03CUP M1锁定
        byte[] card_info = mCardInfo79;   //卡信息
        byte[] type = optType;    //卡状态 1字节  00 商户圈存 01自助圈存 10M1余额转移 ...
        byte[] money = tran_money;    //交易金额 4字节
        byte[] bank_card_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //银行卡账号 16字节
        byte[] mark = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //附加信息 16字节
        byte[] extend_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //扩展流水号 8字节
        byte[] password = TUtil.hexString2Bytes(mPassword);    //充值密码 16字节
        byte[] rfu = TUtil.zeros(22);
        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, physics_no, logic_no, card_status, card_info, type, money, bank_card_no, mark, extend_no, password, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, physics_no, logic_no, card_status, card_info, type, money, bank_card_no, mark, extend_no, password, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        LogUtils.e(TUtil.bytes2HexString(body));

        sentData(192, all);

    }

    /**
     * 终端->服务器 圈存
     * P_CPU_LOAD
     */
    private void pCpuLoad(byte[] cardInfo, byte[] sw1sw2, byte[] back_status) {
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        LogUtils.e("AA82系统流水号：" + TUtil.bytes2HexString(mSysOrderNo));
        byte[] head = {(byte) 0xAA, (byte) 0x83, 0x00, 0x01, 0x02, (byte) 0x80, 0x00, (byte) 0x98};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] sys_order_no = mSysOrderNo;
        byte[] card_info = cardInfo;   //卡信息
        byte[] card_back_no = sw1sw2;
        byte[] reader_back_no = back_status;
        byte[] rfu = TUtil.zeros(25);

        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, physics_no, logic_no, sys_order_no, card_info, card_back_no, reader_back_no, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, physics_no, logic_no, sys_order_no, card_info, card_back_no, reader_back_no, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        /**
         * 在终端向充值服务器发送圈存请求报文（P_CPU_LOAD）前，终端须先记录不完整交易记录
         */
        Record record = new Record();
        record.setOrder_id(mOrderId);
        record.setType("CPU");
        record.setAuth_code("00000000");
        record.setStatus("00");
        record.setMoney(getMoney(mMoney));
        record.setMoney_upper(TUtil.bytes2HexString(mMoneyUpper));
        record.setMoney_lower(TUtil.bytes2HexString(mMoneyLower));
        record.setBalance(TUtil.bytes2HexString(mCardBalance));
        record.setHead(TUtil.bytes2HexString(head));
        record.setShid(TUtil.bytes2HexString(mShid));
        record.setSerial_no(TUtil.bytes2HexString(serial_no));
        record.setPki_no(TUtil.bytes2HexString(pki_no));
        record.setPhysics_no(TUtil.bytes2HexString(mPhysicsNo));
        record.setLogic_no(TUtil.bytes2HexString(mLogicNo));
        record.setSys_order_no(TUtil.bytes2HexString(sys_order_no));
        record.setCard_info(TUtil.bytes2HexString(card_info));
//        record.setCard_back_no(TUtil.bytes2HexString(card_back_no));
//        record.setReader_back_no(TUtil.bytes2HexString(reader_back_no));
        record.setRfu(TUtil.bytes2HexString(rfu));

        App.getDaoInstant().getRecordDao().deleteAll();
        recordId = App.getDaoInstant().getRecordDao().insertOrReplace(record);

        LogUtils.e(App.getDaoInstant().getRecordDao().load(recordId));

        LogUtils.e(recordId);

        sentData(224, all);
    }

    /**
     * 终端->服务器 圈存提交
     * P_CPU_LOAD_SUBMIT
     */
    private void pCpuLoadSubmit() {
        byte[] head = {(byte) 0xAA, (byte) 0x85, 0x00, 0x01, 0x02, (byte) 0x80, 0x00, (byte) 0x78};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] sys_order_no = mSysOrderNo;
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] status = {0x00};
        byte[] auth_code = {0x00, 0x00, 0x00, 0x00};    //认证码 4字节
        byte[] card_back_no = mSw1sw2;
        byte[] reader_back_no = mReaderBackStatus;
        byte[] voucher_no = mRechargeAuthCode;    //凭证号 4字节
        byte[] money = mRechargeMoney;    //交易金额 4字节
        byte[] balance = mCardBalance;    //交易金额 4字节
        byte[] money_lower = mMoneyLower;    //金额下限 1字节
        byte[] money_upper = mMoneyUpper;    //金额上限 3字节
        byte[] card_info = mCardInfo79;   //计数信息
        byte[] rfu = TUtil.zeros(36);

        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, sys_order_no, physics_no, logic_no, status, auth_code, card_back_no, reader_back_no, voucher_no, money, balance, money_lower, money_upper, card_info, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, sys_order_no, physics_no, logic_no, status, auth_code, card_back_no, reader_back_no, voucher_no, money, balance, money_lower, money_upper, card_info, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);


        sentData(80, all);

        cpuLoadSubmit = true;

//        cupRetryCommit();
    }

    /**
     * 终端->服务器 圈存提交（提交不完整记录）
     * P_CPU_LOAD_SUBMIT
     */
    private void pCpuLoadReSubmit() {

        List<Record> records = App.getDaoInstant().getRecordDao().loadAll();
        if (records.size() == 0) {
            dialog.dismiss();
            return;
        }
        Record record = records.get(0);

        LogUtils.e(TUtil.bytes2HexString(mShid));
        LogUtils.e(TUtil.bytes2HexString(enShid));
        byte[] head = {(byte) 0xAA, (byte) 0x85, 0x00, 0x01, 0x02, (byte) 0x80, 0x00, (byte) 0x78};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] sys_order_no = TUtil.hexString2Bytes(record.getSys_order_no());
        byte[] physics_no = TUtil.hexString2Bytes(record.getPhysics_no());
        byte[] logic_no = TUtil.hexString2Bytes(record.getLogic_no());
        byte[] status = TUtil.hexString2Bytes(record.getStatus());
        byte[] auth_code = {0x00, 0x00, 0x00, 0x00};    //认证码 4字节
        byte[] card_back_no = TUtil.hexString2Bytes(record.getCard_back_no());
        byte[] reader_back_no = TUtil.hexString2Bytes(record.getReader_back_no());
        byte[] voucher_no = TUtil.hexString2Bytes(record.getVoucher_no());    //凭证号 4字节
        byte[] money = TUtil.hexString2Bytes(record.getMoney());    //交易金额 4字节
        byte[] balance = TUtil.hexString2Bytes(record.getBalance());    //交易金额 4字节
        byte[] money_lower = TUtil.hexString2Bytes(record.getMoney_lower());    //金额下限 1字节
        byte[] money_upper = TUtil.hexString2Bytes(record.getMoney_upper());    //金额上限 3字节
        byte[] card_info = TUtil.hexString2Bytes(record.getCard_info());   //计数信息
        byte[] rfu = TUtil.zeros(36);

        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, sys_order_no, physics_no, logic_no, status, auth_code, card_back_no, reader_back_no, voucher_no, money, balance, money_lower, money_upper, card_info, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, sys_order_no, physics_no, logic_no, status, auth_code, card_back_no, reader_back_no, voucher_no, money, balance, money_lower, money_upper, card_info, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
//        enShid = encrypt3DES.encrypt(shid, 16, CHexConver.hexStringToBytes(ck), 32, 1);         //握手流水号 8字节 加密
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        sentData(80, all);

        cpuLoadSubmit = true;
    }

    /**
     * 终端->服务器 M1卡圈存查询
     * P_M1_LOAD_QRY
     */
    private void pM1LoadQry(byte[] mCardInfo) {
        byte[] optType = {0x00};
        if (mOpt[0] == 0x04) {
            optType = new byte[]{0x20};
        }
//        LogUtils.e("mSk：" + TUtil.bytes2HexString(mSk));
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        LogUtils.e("卡信息：" + TUtil.bytes2HexString(mCardInfo));
        LogUtils.e("握手流水号：" + TUtil.bytes2HexString(mShid));
        byte[] head = {(byte) 0xAA, (byte) 0x61, 0x00, 0x01, 0x02, (byte) 0x80, 0x00, (byte) 0x98};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
//        byte[] card_status = mCardStatus;    //卡状态 1字节  00 M1卡 01CPU卡 02CUP M1未锁定 03CUP M1锁定
        byte[] card_info = mCardInfo;   //卡信息
        byte[] type = optType;    //卡状态 1字节  00 商户圈存 01自助圈存 10M1余额转移 ...
        byte[] money = TUtil.hexString2Bytes(getMoney(mMoney));    //交易金额 4字节
        byte[] bank_card_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //银行卡账号 16字节
        byte[] mark = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //附加信息 16字节
        byte[] extend_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //扩展流水号 8字节
        byte[] password = TUtil.hexString2Bytes(mPassword);    //充值密码 16字节
        byte[] rfu = TUtil.zeros(23);
        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, physics_no, logic_no, card_info, type, money, bank_card_no, mark, extend_no, password, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, physics_no, logic_no, card_info, type, money, bank_card_no, mark, extend_no, password, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        LogUtils.e(TUtil.bytes2HexString(body));

        sentData(128, all);

    }

    /**
     * 终端->服务器 M1卡圈存
     * P_M1_LOAD
     */
    private void pM1Load() {
        LogUtils.e(TUtil.bytes2HexString(mPhysicsNo));
        LogUtils.e(TUtil.bytes2HexString(mSysOrderNo));
        LogUtils.e(TUtil.bytes2HexString(mM1TranInfo));

        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        byte[] head = {(byte) 0xAA, (byte) 0x63, 0x00, 0x01, 0x02, (byte) 0x80, 0x00, (byte) 0x98};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = mPhysicsNo;
        byte[] sys_order_no = mSysOrderNo;
        byte[] tran_info = mM1TranInfo;   //卡信息
        byte[] rfu = TUtil.zeros(28);

        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, physics_no, sys_order_no, tran_info, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, physics_no, sys_order_no, tran_info, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        LogUtils.e(TUtil.bytes2HexString(TUtil.byteMergerAll(head, shid, signBody)));

        /**
         * 终端向充值服务器发送圈存报文（P_M1_LOAD）前，终端须先记录不完整交易记录
         */
        Record record = new Record();
        record.setOrder_id(mOrderId);
        record.setType("M1");
        record.setAuth_code("00000000");
        record.setMoney(getMoney(mMoney));
        record.setStatus("02");
        record.setMoney_upper(TUtil.bytes2HexString(mMoneyUpper));
        record.setMoney_lower(TUtil.bytes2HexString(mMoneyLower));
        record.setBalance(TUtil.bytes2HexString(mCardBalance));
        record.setHead(TUtil.bytes2HexString(head));
        record.setShid(TUtil.bytes2HexString(mShid));
        record.setSerial_no(TUtil.bytes2HexString(serial_no));
        record.setPki_no(TUtil.bytes2HexString(pki_no));
        record.setPhysics_no(TUtil.bytes2HexString(mPhysicsNo));
        record.setLogic_no(TUtil.bytes2HexString(mLogicNo));
        record.setSys_order_no(TUtil.bytes2HexString(sys_order_no));
        record.setCard_info(TUtil.bytes2HexString(tran_info));
//        record.setCard_back_no(TUtil.bytes2HexString(card_back_no));
//        record.setReader_back_no(TUtil.bytes2HexString(mReaderBackStatus));
        record.setRfu(TUtil.bytes2HexString(rfu));

        App.getDaoInstant().getRecordDao().deleteAll();
        recordId = App.getDaoInstant().getRecordDao().insertOrReplace(record);

        sentData(208, all);
    }

    /**
     * 冲正
     * P_M1_ROLLBACK
     */
    private void pM1RollBack() {
        List<Record> records = App.getDaoInstant().getRecordDao().loadAll();
        if (records.size() == 0) {
            return;
        }
        Record record = records.get(0);
        if (record == null) {
            return;
        }
        LogUtils.e(record);
        if (record.getVoucher_no() == null) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    App.getDaoInstant().getRecordDao().deleteAll();
                    dialog.dismiss();
                }
            });
            return;
        }
        if (record.getReader_back_no() == null || record.getReader_back_no().equals("")) {
            App.getDaoInstant().getRecordDao().deleteAll();
            dialog.dismiss();
            return;
        }
        byte[] head = {(byte) 0xAA, (byte) 0x65, 0x00, 0x01, 0x02, (byte) 0x80, 0x00, (byte) 0xa8};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] sys_order_no = TUtil.hexString2Bytes(record.getSys_order_no());
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = TUtil.hexString2Bytes(record.getPhysics_no());
        byte[] logic_no = TUtil.hexString2Bytes(record.getLogic_no());
        byte[] reason = TUtil.hexString2Bytes(record.getStatus());
        byte[] back_no = TUtil.hexString2Bytes(record.getReader_back_no());
        byte[] money = TUtil.hexString2Bytes(record.getMoney());    //交易金额 4字节
        byte[] voucher_no = TUtil.hexString2Bytes(record.getVoucher_no());    //凭证号 4字节
        byte[] tran_info = TUtil.hexString2Bytes(record.getCard_info());    //交易信息 88字节
        byte[] rfu = TUtil.zeros(26);

        byte[] body = TUtil.byteMergerAll(shid, serial_no, sys_order_no, pki_no, physics_no, logic_no, reason, back_no, money, voucher_no, tran_info, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, sys_order_no, pki_no, physics_no, logic_no, reason, back_no, money, voucher_no, tran_info, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        LogUtils.e(TUtil.bytes2HexString(TUtil.byteMergerAll(head, shid, signBody)));

        sentData(96, all);

        m1LoadSubmit = true;

//        m1RetryCommit();
    }

    /**
     * 捕获黑名单
     * P_SET_BLACKLIST
     */
    private void pSetBlackList(byte[] bytes) {
        byte[] head = {(byte) 0xAB, (byte) 0x05, 0x00, 0x01, 0x02, (byte) 0x80, 0x00, (byte) 0x48};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] black_info = bytes;
        byte[] rfu = TUtil.zeros(20);

        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, physics_no, logic_no, black_info, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, physics_no, logic_no, black_info, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        LogUtils.e(TUtil.bytes2HexString(TUtil.byteMergerAll(head, shid, signBody)));

        sentData(80, all);
    }

    /**
     * 黑名单提交
     * P_BLACKLIST_SUBMIT
     */
    private void pBlackListSubmit(byte[] sw1sw2, byte[] status) {
        byte[] head = {(byte) 0xAB, (byte) 0x07, 0x00, 0x01, 0x02, (byte) 0x80, 0x00, (byte) 0x48};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] sys_order_no = mSysOrderNo;
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] back_no = sw1sw2;
        byte[] reader_back_no = status;
        byte[] rfu = TUtil.zeros(25);

        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, sys_order_no, physics_no, logic_no, back_no, reader_back_no, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, sys_order_no, physics_no, logic_no, back_no, reader_back_no, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        LogUtils.e(TUtil.bytes2HexString(TUtil.byteMergerAll(head, shid, signBody)));

        sentData(80, all);
    }

    /**
     * CPU 卡同步
     * P_M1_TO_CPU
     */
    private void pM1ToCpu(byte[] random, byte[] cardInfo) {
        byte[] head = {(byte) 0xAB, (byte) 0x01, 0x00, 0x01, 0x02, (byte) 0x80, 0x01, (byte) 0x18};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] random_no = random;
        byte[] card_info = cardInfo;
        byte[] rfu = TUtil.zeros(36);

        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, physics_no, logic_no, random_no, card_info, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, physics_no, logic_no, random_no, card_info, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        LogUtils.e(TUtil.bytes2HexString(TUtil.byteMergerAll(head, shid, signBody)));

        sentData(208, all);
    }

    /**
     * 终端->服务器 充值金账户查询
     * P_ACCT_QRY
     */
    private void pAcctQry() {
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        byte[] head = {(byte) 0xAB, 0x31, 0x00, 0x01, 0x02, (byte) 0x80, 0x00, (byte) 0x48};
        byte[] shid = mShid;
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] type = {0x02};
        byte[] logic_no = mLogicNo;
        byte[] physics_no = mPhysicsNo;
        byte[] rfu = TUtil.zeros(35);
        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, type, logic_no, physics_no, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);
        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, type, logic_no, physics_no, rfu, check_no);
        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        sentData(80, all);

    }


    /**
     * 发送数据
     *
     * @param length
     * @param bytes
     */
    private void sentData(final int length, final byte[] bytes) {
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        int timeOut = 3000;
                        if (!mManager.isConnect()) {
                            mManager.connect();
                        }
                        do {
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                            }
                            timeOut--;
                        } while (!mManager.isConnect() && timeOut > 0);
                        if (timeOut == 0) {
                            mManager.disconnect();
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String rq = TUtil.bytes2HexString(TUtil.subBytes(bytes, 0, 2));
                                    //如果 P_CPU_LOAD 报文没有发送成功，则终端立即删除该不完整交易记录
                                    //如果 P_M1_LOAD 报文没有发送成功，则终端立即删除该不完整交易记录
                                    switch (rq) {
                                        case "AA83":
                                            ToastUtil.showToast(mContext, "与服务器连接中断，请重新操作");
                                            App.getDaoInstant().getRecordDao().deleteAll();
                                            break;

                                        case "AA63":
                                            ToastUtil.showToast(mContext, "与服务器连接中断，请重新操作");
                                            App.getDaoInstant().getRecordDao().deleteAll();
                                            break;
                                        default:
                                            ToastUtil.showToast(mContext, "正在重新请求...");
                                            if (!mManager.isConnect()) {
                                                mManager.connect();
                                            }
                                            do {
                                                try {
                                                    Thread.sleep(1);
                                                } catch (InterruptedException e) {
                                                }
                                            } while (!mManager.isConnect());
                                            App.getInstance().setLength(length);
                                            MsgDataBean msgDataBean = new MsgDataBean(bytes);
                                            mManager.send(msgDataBean);
                                            serverRq = false;
                                            serverTimeOut(bytes);
                                    }
                                    dialog.dismiss();
                                }
                            });
                        }
                        App.getInstance().setLength(length);
                        MsgDataBean msgDataBean = new MsgDataBean(bytes);
                        mManager.send(msgDataBean);
                        serverRq = false;
                        serverTimeOut(bytes);

                    }
                });

//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                if (!mManager.isConnect()) {
//                    mManager.connect();
//                }
//                do {
//                } while (!mManager.isConnect());
//                subscriber.onNext("");
//            }
//        }).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                App.getInstance().setLength(length);
//                MsgDataBean msgDataBean = new MsgDataBean(bytes);
//                mManager.send(msgDataBean);
//            }
//        });
    }


    /**
     * 获取序列号
     *
     * @return
     */
    private byte[] serialNo() {
        String hex = index.toHexString(index);
        String sub = "";
        for (int i = 0; i < 8 - hex.length(); i++) {
            sub += "0";
        }
        return TUtil.hexString2Bytes(sub + hex);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
//        timer.cancel();
        if (null != mSerialPortManager) {
            mSerialPortManager.closeSerialPort();
            mSerialPortManager = null;
            mManager.disconnect();
            mManager = null;
        }
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * 创建订单
     */
    public void creatOrder() {

        if (chooseBean == null) {
            ToastUtil.showWrning(mContext, "请选择充值金额");
            return;
        }
        List<ItemSnapshotsBean> orderItemBeen = new ArrayList<>();

        ItemSnapshotsBean bean = new ItemSnapshotsBean();
        bean.setItemId(chooseBean.getId());
        bean.setItemTemplateId(chooseBean.getItemTemplateId());
        bean.setItemName("羊城通充值" + chooseBean.getItemName());
        bean.setItemTypeId(chooseBean.getPosTypeId());
        bean.setItemTypeName(chooseBean.getPosTypeName());
        bean.setItemTypeUnitId(chooseBean.getItemUnitId());
        bean.setItemTypeUnitName(chooseBean.getItemUnitName());
        bean.setItemBarcode(chooseBean.getBarcode());
        bean.setCostPrice(chooseBean.getSalesPrice());
        bean.setTotalPrice(chooseBean.getSalesPrice());
        bean.setNormalPrice(chooseBean.getSalesPrice());
        bean.setNormalQuantity(1);
        orderItemBeen.add(bean);

        HttpParams httpParams = new HttpParams();

        httpParams.put("orderType", 9);
        httpParams.put("actualPrice", mMoney);//应收金额
        httpParams.put("totalPrice", mMoney);//实收金额
        httpParams.put("items", new Gson().toJson(orderItemBeen));
        httpParams.put("phoneNumber", etCardNo.getText().toString());

        OkGo.<NydResponse<CreateOrderSuccessBean>>post(Contonts.URL_CREATE_ORDER)
                .tag(Contonts.URL_CREATE_ORDER)
                .params(httpParams)
                .execute(new JsonCallback<NydResponse<CreateOrderSuccessBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<CreateOrderSuccessBean>> response) {
                        mOrderId = response.body().response.getOrderId();
                        dialog.show();
//                        pTranQry();
                        setReaderStep = 1;
                        updateRecharge = false;
                        recharge = true;
                        rPubQryPhysicsInfo();
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<CreateOrderSuccessBean>> response) {
                        super.onError(response);
                        ToastUtil.showEorr(mContext, "网络故障，无法进行充值操作");
                    }
                });
    }

    /**
     * 订单支付
     */
    private void payOrder(final int status) {
        String cardNumber = etCardNo.getText().toString();
        String money = mMoney + "";
        String balance = etBalance.getText().toString();
        if (status != 1) {
            /**
             * 记录不完整订单并删除
             */
            List<Record> records = App.getDaoInstant().getRecordDao().loadAll();
            if (records.size() > 0) {
                Record record = records.get(0);
                mOrderId = record.getOrder_id();
                cardNumber = record.getLogic_no();
                money = Arith.div(Integer.parseInt(record.getMoney(), 16), 100) + "";
                if (record.getType().equals("CPU")) {
                    balance = Arith.div(Integer.parseInt(record.getBalance(), 16), 100) + "";
                    if (status == 2) {
                        originalMoney = Double.valueOf(balance);
                        balance = Arith.add(Double.valueOf(balance), Double.valueOf(money)) + "";
                        originalMoney = Arith.mul(originalMoney, 100);
                    }
                } else {
                    balance = Arith.div(TUtil.height2low(TUtil.hexString2Bytes(record.getBalance())), 100) + "";
                }
                if (record.getType().equals("M1")) {
                    if (status == 2) {
                        originalMoney = Double.valueOf(balance);
                        balance = Arith.add(Double.valueOf(balance), Double.valueOf(money)) + "";
                        originalMoney = Arith.mul(originalMoney, 100);
                    }
                    App.getDaoInstant().getRecordDao().deleteAll();
                }
            } else {
                return;
            }
        }
        /**
         * 根据充值类型区分订单类型  如果是预存金充值则不需要从商家余额中扣款
         */
        mPayType = 1;
        if (mOpt[0] == 0x04) {
            mPayType = 2;
        }
        OkGo.<NydResponse<FinishOrderData>>post(Contonts.YCT_PAY_ORDER)
                .params("paymentTypeKey", "BALANCE")
                .params("paymentPassword", "123456")
                .params("orderId", mOrderId)
                .params("orderStatus", status)
                .params("productName", "羊城通")
                .params("cardNumber", cardNumber)
                .params("createTimeString", tranTime(mTranTime))
                .params("prebalance", originalMoney)
                .params("amount", money)
                .params("balance", balance)
                .params("merchantNumber", merchantNumber)
                .params("terminalNumber", readerId)
                .params("type", mPayType)
                .params("psam", psam)
                .params("flowNumber", Integer.parseInt(TUtil.bytes2HexString(mSysOrderNo), 16) + "")
                .params("voucherNo", Integer.parseInt(TUtil.bytes2HexString(mRechargeAuthCode), 16) + "")
                .execute(new JsonCallback<NydResponse<FinishOrderData>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<FinishOrderData>> response) {
                        dialog.dismiss();
                        if (mOpt[0] != 0x04) {
                            getBalance();
                        }
                        if (status != -1) {
                            getData(response.body().response.getId());
                        }
                        //清除金额选中状态
                        mMoney = 0;
                        tempDataAdapter.notifyDataChanged();

                        if (mPayType == 1) {
                            //打开钱箱
                            try {
                                HdxUtil.SetV12Power(0);
                                HdxUtil.SetV12Power(1);
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                            }
                            try {
                                OpenDrawers.openMoneyBox();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    /**
     * 获取订单详情
     *
     * @param id
     */
    private void getData(String id) {
        OkGo.<NydResponse<YctRecordDetail>>post(Contonts.YCT_RECORD_DETAIL)
                .tag(getClass().getSimpleName())
                .params("orderId", id)
                .execute(new JsonCallback<NydResponse<YctRecordDetail>>() {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<YctRecordDetail>> response) {
                        EventBus.getDefault().post(new PrintTicket(response.body().response, 1));
                    }
                });
    }

    /**
     * 获取商家余额
     */
    public void getBalance() {
        OkGo.<NydResponse<BalanceResultBean>>post(Contonts.URL_GET_MERCHANT_BALANCE)
                .execute(new JsonCallback<NydResponse<BalanceResultBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<BalanceResultBean>> response) {
                        LogUtils.e(response.body().response);
                        for (BalanceResultBean.DatasBean bean : response.body().response.getDatas()) {
                            if (bean.getType().equals("1")) {
                                mSellerBalance = bean.getCurrentAmount();
                                etSellerBalance.setText(bean.getCurrentAmount() + "");
                            }
                        }
                    }
                });
    }

    /**
     * 解析交易时间
     *
     * @param tranTime
     * @return
     */
    private String tranTime(byte[] tranTime) {
        if (tranTime.length == 0) {
            return DateUtil.getTime();
        }
        return TUtil.bytes2HexString(TUtil.subBytes(tranTime, 0, 2)) + "-" + TUtil.bytes2HexString(TUtil.subBytes(tranTime, 2, 1)) + "-" + TUtil.bytes2HexString(TUtil.subBytes(tranTime, 3, 1))
                + " " + TUtil.bytes2HexString(TUtil.subBytes(tranTime, 4, 1)) + ":" + TUtil.bytes2HexString(TUtil.subBytes(tranTime, 5, 1)) + ":" + TUtil.bytes2HexString(TUtil.subBytes(tranTime, 6, 1)) + ":";
    }

    @OnClick({R.id.btn_record, R.id.btn_get_card, R.id.btn_sign, R.id.btn_notice, R.id.btn_recharge, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_record:
                startActivity(new Intent(mContext, YctRecordActivity.class));
                break;
            case R.id.btn_get_card:
                setReaderStep = 1;
                firstReadCard = true;
////                rCpuGetCardInfo();
                getCardInfo = true;
                showDialog("正在获取卡信息...");
                mOpt = new byte[]{0x00};
                rPubQryPhysicsInfo();
                break;
            case R.id.btn_sign:
                showDialog("正在重新签到...");
                setReaderStep = 1;
                firstReadCard = true;
                if (mManager != null) {
                    if (!mManager.isConnect()) {
                        mManager.connect();
                    }
                    do {
                    } while (!mManager.isConnect());
                }
                rInit();
                break;
            case R.id.btn_notice:
                String text = "<big>商户须知：</big><br />羊城通充值成功后会扣除充值商家的商家余额,建议商家与用户使用现金结算羊城通充值<br /><br /><big>使用操作：</big><br />请让用户将羊城通卡置于感应器上方便于读取卡号，然后点击充值对应金额按钮，最后点击确认充值";
                DialogUtil.OkAndCancle(getContext(), "使用说明", text).show();
                break;
            case R.id.btn_submit:
                retryTime = 1;
                if (exceptionHandle()) {
                    ToastUtil.showToast(mContext, "需要先处理异常订单，请稍后重试");
                    return;
                }
                //先处理未完成订单
                if (selectTag.equals("全部")) {
                    noticeDialog = new NoticeDialog(getContext(), "此选择只适用于预存金充值", null);
                    noticeDialog.show();
                    return;
                }
                if (mMoney == 0) {
                    noticeDialog = new NoticeDialog(getContext(), "请先选择充值金额", null);
                    noticeDialog.show();
                    return;
                }
                if (mMoney > mSellerBalance) {
                    noticeDialog = new NoticeDialog(getContext(), "您的商家余额不足，无法完成充值！", null);
                    noticeDialog.show();
                    return;
                }
                if (etBalance.getText().toString().equals("")) {
                    ToastUtil.showWrning(mContext, "获取不到商家余额");
                    return;
                }
                double balance = Double.valueOf(etBalance.getText().toString());
                if (Arith.add(balance, mMoney) > upMoney) {
                    noticeDialog = new NoticeDialog(getContext(), "卡余额不能大于" + upMoney + "元，请重新选择充值金额", null);
                    noticeDialog.show();
                    return;
                }
                if (!signSuccess) {
                    noticeDialog = new NoticeDialog(getContext(), "设备初始化失败，请重新初始化", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            firstReadCard = true;
                            rInit();
                            noticeDialog.dismiss();
                        }
                    });
                    noticeDialog.show();
                    return;
                }
                confirmDialog = new ConfirmDialog(getContext(), "此次操作会从您的商家余额内扣除" + mMoney + "元，确认执行吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOpt = new byte[]{0x01};
                        startRecharge = true;
                        rM1Load = false;
                        rCpuLoad = false;
                        creatOrder();
                        confirmDialog.dismiss();
                    }
                });
                confirmDialog.show();
                break;

            case R.id.btn_recharge:
                retryTime = 1;
                if (mMoney == -1) {
                    String preBalance = etPreBalance.getText().toString();
                    if (!preBalance.equals("")) {
                        mMoney = Double.valueOf(preBalance);
                    } else {
                        ToastUtil.showWrning(mContext, "获取不到用户预存金金额，请重新读卡");
                        return;
                    }
                    if (mMoney == 0) {
                        ToastUtil.showWrning(mContext, "用户预存金金额为0，无法进行此操作");
                        return;
                    }
                }
                if (mMoney == 0) {
                    noticeDialog = new NoticeDialog(getContext(), "请先选择充值金额", null);
                    noticeDialog.show();
                    return;
                }
                if (etPreBalance.getText().toString().equals("")) {
                    ToastUtil.showWrning(mContext, "未读取到充值金金额，请重新读卡");
                }
                if (mMoney > Double.valueOf(etPreBalance.getText().toString())) {
                    noticeDialog = new NoticeDialog(getContext(), "您的充值金余额小于充值金额，无法完成充值！", null);
                    noticeDialog.show();
                    return;
                }
                double balance2 = Double.valueOf(etBalance.getText().toString());
                if (Arith.add(balance2, mMoney) > upMoney) {
                    noticeDialog = new NoticeDialog(getContext(), "卡余额不能大于" + upMoney + "元，请重新选择充值金额", null);
                    noticeDialog.show();
                    return;
                }
                if (!signSuccess) {
                    noticeDialog = new NoticeDialog(getContext(), "设备初始化失败，请重新初始化", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            firstReadCard = true;
                            rInit();
                            noticeDialog.dismiss();
                        }
                    });
                    noticeDialog.show();
                    return;
                }
                confirmDialog = new ConfirmDialog(getContext(), "此次操作为预存金充值，不需要收取用户费用！", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOpt = new byte[]{0x04};
                        startRecharge = true;
                        rM1Load = false;
                        rCpuLoad = false;
                        creatOrder();
                        confirmDialog.dismiss();
                    }
                });
                confirmDialog.show();
                break;
        }
    }

    /**
     * 转换充值金额
     *
     * @param m
     * @return
     */
    private String getMoney(double m) {
        Integer money = (int) (m * 100);
        String hex = money.toHexString(money);
        String sub = "";
        for (int i = 0; i < 8 - hex.length(); i++) {
            sub += "0";
        }
        LogUtils.e(sub + hex);
        return sub + hex;
    }

    /**
     * 处理打印凭据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void printTicket(PrintTicket event) {
        try {
            YctOrderOnTicket.intClass(getActivity());
            YctOrderOnTicket.outcomeTacket(event.getYctRecordDetail(), event.getType());//打印小票
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void showDialog(String msg) {
        ((TextView) dialog.findViewById(R.id.progress_content)).setText(msg);
        dialog.show();
    }

    /**
     * 终端->服务器 撤销
     * P_PURCHASE_QRY
     */
    private void pPurchaseQry() {
        byte[] head = {(byte) 0xE0, (byte) 0x11, 0x00, 0x01, 0x02, (byte) 0x80, 0x00, (byte) 0x78};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] type = {0x00};
        byte[] money = TUtil.hexString2Bytes(getMoney(mMoney));    //交易金额 4字节
        byte[] card_info = mCardInfo;   //计数信息
        byte[] extend_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //扩展流水号 8字节
        byte[] rfu = TUtil.zeros(39);
        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, physics_no, logic_no, type, money, card_info, extend_no, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, physics_no, logic_no, type, money, card_info, extend_no, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        LogUtils.e(signBody);
        LogUtils.e(signBody2);
        LogUtils.e(enShid);
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!mManager.isConnect()) {
                    mManager.connect();
                }
                do {
                } while (!mManager.isConnect());
                subscriber.onNext("");
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                App.getInstance().setLength(192);
                MsgDataBean msgDataBean = new MsgDataBean(all);
                mManager.send(msgDataBean);
            }
        });
    }

    private boolean exceptionHandle() {
        /**
         * 处理异常订单
         */
        List<Record> records = App.getDaoInstant().getRecordDao().loadAll();
        LogUtils.e(records);
        if (records.size() > 0) {
            showDialog("正在处理异常订单...");
            Record record = records.get(0);
            switch (record.getType()) {
                case "CPU":
                    pCpuLoadReSubmit();
                    cpuLoad = true;
                    break;
                case "M1":
                    pM1RollBack();
                    m1Load = true;
                    break;
            }
            return true;
        } else {
            return false;
        }
    }

    private void timeOut() {
        while (timeOut > 0 && ret == false) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
            timeOut -= 1;
        }
        if (readerCom[2] == 0x7B && timeOut == 0 && ret == false) {
            //无响应重新读卡信息，对比余额
            //无响应重新读卡信息，对比余额
            rCpuLoad = true;
            Record record = App.getDaoInstant().getRecordDao().load(recordId);
            if (record != null) {
                record.setCard_back_no("0000");
                record.setReader_back_no("FF");
                App.getDaoInstant().getRecordDao().update(record);
            }
            rCpuGetCardInfo();
            return;
        }
        if (readerCom[2] == 0xBA && timeOut == 0 && ret == false) {
            //无响应重新读卡信息，对比余额
            Record record = App.getDaoInstant().getRecordDao().load(recordId);
            if (record != null) {
                record.setStatus("01");     //未写卡
                record.setReader_back_no("00");
            }
            rM1Load = true;
            rM1GetCardInfo();
            return;
        }
        if (timeOut == 0 && ret == false) {
            dialog.dismiss();
            if (tryTime < 3) {
                noticeDialog = new NoticeDialog(mContext, "读卡器返回超时，请检查卡片是否放好并重试", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mSerialPortManager.sendBytes(readerCom);
                        noticeDialog.dismiss();
                    }
                });
                noticeDialog.show();
                tryCtr = readerCom[2];
                if (tryCtr == readerCom[2]) {
                    tryTime++;
                }
            } else {
                if (rCpuLoad && readerCom[2] == 0x79) {
                    //如果读卡余额失败或者读卡仍无响应，则系统默认该笔充值为疑似成功，打小票的时候加*号。
                    Record record = App.getDaoInstant().getRecordDao().load(recordId);
                    record.setCard_info(TUtil.bytes2HexString(TUtil.zeros(16)));
                    App.getDaoInstant().getRecordDao().update(record);
                    payOrder(2);
                    pCpuLoadReSubmit();
                    ToastUtil.showWrning(mContext, "疑似充值成功，请商家核对是否充值成功");
                } else if (rM1Load && readerCom[2] == 0xB9) {
                    ToastUtil.showWrning(mContext, "疑似充值成功，请商家核对是否充值成功");
                    payOrder(2);
                }
                noticeDialog = new NoticeDialog(mContext, "已重试3次，读卡器未响应，请重新充值流程", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        noticeDialog.dismiss();
                        tryTime = 0;
                    }
                });
                noticeDialog.show();
            }
        }
        if (tryCtr != readerCom[2]) {
            tryTime = 0;
        }
    }


    private void serverTimeOut(byte[] bytes) {
        new Thread(new TimerThread(bytes)).start();
    }

    public class TimerThread implements Runnable {
        private byte[] bytes;

        public TimerThread(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public void run() {
            int timeOut = 3000;
            while (timeOut > 0 && serverRq == false) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
                timeOut -= 1;
            }
            if (timeOut == 0 && serverRq == false) {
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);

                String rq = TUtil.bytes2HexString(TUtil.subBytes(bytes, 0, 2));
                //如果 P_CPU_LOAD 报文没有发送成功，则终端立即删除该不完整交易记录
                //如果 P_M1_LOAD 报文没有发送成功，则终端立即删除该不完整交易记录
                switch (rq) {
                    case "AA83":
                        App.getDaoInstant().getRecordDao().deleteAll();
                        break;

                    case "AA63":
                        App.getDaoInstant().getRecordDao().deleteAll();
                        break;
                }
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mManager.disconnect();
                ToastUtil.showToast(mContext, "与服务器连接中断，请重新操作");
                dialog.dismiss();
            }
        }
    };

    /**
     * CPU充值读卡器或服务器未响应，发起圈存提交
     */
    private void cpuReadNoRspOrServerNoRsp() {
        Record record = App.getDaoInstant().getRecordDao().load(recordId);
        if (record != null) {
            record.setStatus("01");
            record.setCard_back_no("0000");
            record.setReader_back_no("FF");
            record.setCard_info(TUtil.bytes2HexString(TUtil.zeros(16)));
            App.getDaoInstant().getRecordDao().update(record);
            pCpuLoadReSubmit();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /**
     * M1充值读卡器或服务器未响应，发起冲正
     */
    private void m1ReadNoRspOrServerNoRsp() {
        Record record = App.getDaoInstant().getRecordDao().load(recordId);
        if (record != null) {
            record.setStatus("02");
            record.setCard_back_no("0000");
            record.setReader_back_no("00");
            record.setCard_info(TUtil.bytes2HexString(TUtil.zeros(88)));
            App.getDaoInstant().getRecordDao().update(record);
            pM1RollBack();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private void showCardInfo(String cardNo, String balance) {
//        String text = "卡号：" + cardNo + "余额：" + balance;
//        try {
//            ScreenUtil.getInstance().showAllPrice(text);
//        }catch (Exception e){
//        }
    }
}
