package com.gzdb.yct;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.core.serialPort.SerialPortManager;
import com.core.serialPort.listener.OnOpenSerialPortListener;
import com.core.serialPort.listener.OnSerialPortDataListener;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
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
import com.gzdb.yct.util.CHexConver;
import com.gzdb.yct.util.Encrypt3DES;
import com.gzdb.yct.util.MD5Util;
import com.gzdb.yct.util.ReaderError;
import com.gzdb.yct.util.TUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

import static android.widget.Toast.LENGTH_SHORT;
import static com.gzdb.yct.socket.sdk.OkSocket.open;

public class YctActivity extends AppCompatActivity implements OnOpenSerialPortListener {

    private Context mContext;
    private static final String TAG = YctActivity.class.getSimpleName();
    private SerialPortManager mSerialPortManager;
    private IConnectionManager mManager;
    private OkSocketOptions mOkOptions;
    private ConnectionInfo mInfo;
    private byte[] rqBytes;

    private TextView text, balance;

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

    private String mPki = "89006555";
    private String ck = "11223344556677888877665544332211";

    private Encrypt3DES encrypt3DES;

    //    private byte[] card_count_info = {};
    private byte[] mCardInfo79 = {};    //CPU卡取信息返回
    private byte[] mCardStatus = {};
    private byte[] mCardBalance = {};
    private byte[] mSysOrderNo = {};     //系统流水号
    private byte[] mReaderBackStatus = {};
    private byte[] mAuthCode = {};
    private byte[] mMoneyLower = {};    //金额下限 1字节
    private byte[] mMoneyUpper = {};    //金额上限 3字节
    private byte[] mRechargeMoney = {};
    private byte[] mRechargeAuthCode = {};

    private byte[] mM1TranInfo = {};
    private byte[] mTranCounter = {};
    private byte[] mM1Random = {};

    private Integer index = 0;

    private boolean first = true;
    private boolean recharge = true;
    private boolean updateRecharge = false;
    private boolean firstReadCard = true;

    Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yct);
        mContext = this;

        encrypt3DES = new Encrypt3DES();

        text = (TextView) findViewById(R.id.text);
        balance = (TextView) findViewById(R.id.balance);

        mSerialPortManager = new SerialPortManager();

        mSerialPortManager.setOnOpenSerialPortListener(this)
                .setOnSerialPortDataListener(new OnSerialPortDataListener() {
                    @Override
                    public void onDataReceived(byte[] bytes) {
                        Log.i(TAG, "onDataReceived [ byte[] ]: " + Arrays.toString(bytes));
                        Log.i(TAG, "onDataReceived [ String ]: " + new String(bytes));
                        readerRsp(bytes);
                    }

                    @Override
                    public void onDataSent(byte[] bytes) {
                        Log.i(TAG, "onDataSent [ byte[] ]: " + Arrays.toString(bytes));
                        Log.i(TAG, "onDataSent [ String ]: " + new String(bytes));
                    }
                })
                .openSerialPort(new File("/dev/ttyHSL1"), 57600);

        App.getInstance().setLength(136);

        initData();
        setListener();

        mManager.connect();
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
            if (e != null) {
//                logSend("异常断开:" + e.getMessage());
            } else {
//                logSend("正常断开");
            }
        }

        @Override
        public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
            Toast.makeText(context, "连接失败" + e.getMessage(), LENGTH_SHORT).show();
//            logSend("连接失败");
        }

        @Override
        public void onSocketReadResponse(Context context, ConnectionInfo info, String action, OriginalData data) {
            super.onSocketReadResponse(context, info, action, data);
            serverRsp(data.getBodyBytes());
        }

        @Override
        public void onSocketWriteResponse(Context context, ConnectionInfo info, String action, ISendable data) {
            super.onSocketWriteResponse(context, info, action, data);
            Toast.makeText(YctActivity.this, "发送请求...", Toast.LENGTH_LONG).show();
            LogUtils.e(data.parse().length);
            LogUtils.e(TUtil.bytes2HexString(data.parse()));
//            String str = new String(data.parse(), Charset.forName("utf-8"));
//            logSend(TUtil.bytes2HexString(data.parse()));
        }

        @Override
        public void onPulseSend(Context context, ConnectionInfo info, IPulseSendable data) {
            super.onPulseSend(context, info, data);
            LogUtils.e(TUtil.bytes2HexString(data.parse()));
//            logSend(str);
        }
    };

    private void initData() {

        mInfo = new ConnectionInfo("121.32.31.2", 5010);
        mOkOptions = new OkSocketOptions.Builder()
                .setReconnectionManager(new NoneReconnect())
                .setWritePackageBytes(1024)
                .build();
        //设置自定义解析头
//        OkSocketOptions.Builder okOptionsBuilder = new OkSocketOptions.Builder(mOkOptions);
//        okOptionsBuilder.setHeaderProtocol(new IHeaderProtocol() {
//            @Override
//            public int getHeaderLength() {
//                //返回自定义的包头长度,框架会解析该长度的包头
//                return 0;
//            }
//
//            @Override
//            public int getBodyLength(byte[] header, ByteOrder byteOrder) {
//                //从header(包头数据)中解析出包体的长度,byteOrder是你在参配中配置的字节序,可以使用ByteBuffer比较方便解析
//                return 0;
//            }
//        });
        //将新的修改后的参配设置给连接管理器
        mManager = open(mInfo).option(mOkOptions);


    }

    private void setListener() {
        mManager.registerReceiver(adapter);
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rInit();
            }
        });

        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mManager == null) {
                    return;
                }
                if (!mManager.isConnect()) {
                    mManager.connect();
                } else {
                    mManager.disconnect();
                }
            }
        });

//        findViewById(R.id.btn_sent).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mManager == null) {
//                    return;
//                }
////
//            }
//        });

        findViewById(R.id.btn_common).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TUtil.bytes2HexString(mCardStatus).equals("00")) {
                    pM1Load();
                } else {
                    pCpuLoad(mCardInfo, mSw1sw2, mReaderBackStatus);
                }
            }
        });

        findViewById(R.id.btn_recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pTranQry();
                recharge = true;
            }
        });

        findViewById(R.id.btn_reconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mManager.connect();
            }
        });

        findViewById(R.id.btn_charge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TUtil.bytes2HexString(mCardStatus).equals("00")) {
                    pM1LoadQry();
                } else {
                    pCpuLoadQry();
                }

            }
        });

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pCpuLoadSubmit();
            }
        });


    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        if (null != mSerialPortManager) {
            mSerialPortManager.closeSerialPort();
            mSerialPortManager = null;
            mManager.disconnect();
            mManager = null;
        }
        super.onDestroy();
    }

    /**
     * 串口打开成功
     *
     * @param device 串口
     */
    @Override
    public void onSuccess(File device) {
//        Toast.makeText(getApplicationContext(), String.format("串口 [%s] 打开成功", device.getPath()), Toast.LENGTH_SHORT).show();
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
//                showDialog(device.getPath(), "没有读写权限");
                break;
            case OPEN_FAIL:
            default:
                showDialog(device.getPath(), "串口打开失败");
                break;
        }
    }

    /**
     * 显示提示框
     *
     * @param title   title
     * @param message message
     */
    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private Toast mToast;

    /**
     * Toast
     *
     * @param content content
     */
    private void showToast(String content) {
        if (null == mToast) {
            mToast = Toast.makeText(getApplicationContext(), null, Toast.LENGTH_SHORT);
        }
        mToast.setText(content);
        mToast.show();
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
        switch (bytes[1]) {
            case 0x02:
                rReaderSign2(bytes);
                break;
            case 0x00:
//                mManager.disconnect();
                break;
            case 0x04:
                byte[] signResult = TUtil.subBytes(bytes, 0, 9);
                if (signResult[8] == 0x00) {
                    Toast.makeText(YctActivity.this, "签到成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(YctActivity.this, "签到失败，请重试", Toast.LENGTH_LONG).show();
                }
                mManager.disconnect();
//                mManager.unRegisterReceiver(adapter);
//                mManager=null;
                break;
            case 0x52:      //P_TRAN_QRY
                byte[] deByte52 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 96), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e("交易类型查询返回：" + TUtil.bytes2HexString(deByte52));
                LogUtils.e("交易类型：" + TUtil.bytes2HexString(TUtil.subBytes(deByte52, 4, 1)));
                byte[] tran_info = TUtil.subBytes(deByte52, 5, 48);
                LogUtils.e("查询信息" + TUtil.bytes2HexString(tran_info));
                rSetReadCardInfo(tran_info);
                mManager.disconnect();
                index++;
                break;

            case (byte) 0x82:
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
                    mManager.disconnect();
                    index++;
                    updateRecharge = true;
                }
                break;
            case (byte) 0x84:
                LogUtils.e("" + TUtil.bytes2HexString(TUtil.subBytes(bytes, 16, 208)));
                byte[] deByte84 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 208), TUtil.subBytes(mSk, 0, 16));
//                mSysOrderNo = TUtil.subBytes(deByte84, 0, 8);
                byte[] back_code2 = TUtil.subBytes(deByte84, 8, 4);
                byte[] tran_info2 = TUtil.subBytes(deByte84, 12, 32);
                mRechargeMoney = TUtil.subBytes(deByte84, 51, 4);
                mRechargeAuthCode = TUtil.subBytes(deByte84, 55, 4);
                LogUtils.e("交易信息：" + TUtil.bytes2HexString(tran_info2));
                LogUtils.e("返回码：" + TUtil.bytes2HexString(back_code2));
                LogUtils.e("交易金额：" + TUtil.bytes2HexString(mRechargeMoney));
                LogUtils.e("交易认证号：" + TUtil.bytes2HexString(mRechargeAuthCode));
                if (TUtil.bytes2HexString(back_code2).equals("00000000")) {
                    rCpuLoad(tran_info2);
                    mManager.disconnect();
                    index++;
                }
                break;
            case (byte) 0x86:
                LogUtils.e("" + TUtil.bytes2HexString(TUtil.subBytes(bytes, 16, 64)));
                byte[] deByte86 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 64), TUtil.subBytes(mSk, 0, 16));
                byte[] tran_status = TUtil.subBytes(deByte86, 12, 1);
                if (TUtil.bytes2HexString(tran_status).equals("00")) {
                    Toast.makeText(YctActivity.this, "交易成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(YctActivity.this, "交易失败", Toast.LENGTH_LONG).show();
                }
                index++;
                break;

            case 0x62:
                LogUtils.e("" + TUtil.bytes2HexString(TUtil.subBytes(bytes, 16, 112)));
                byte[] deByte62 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 112), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e(TUtil.bytes2HexString(deByte62));
//                byte[] shid = TUtil.subBytes(bytes, 8, 8);
                mSysOrderNo = TUtil.subBytes(deByte62, 0, 8);
                byte[] back_code62 = TUtil.subBytes(deByte62, 8, 4);
                byte[] sys_time62 = TUtil.subBytes(deByte62, 12, 7);
                byte[] tran_info62 = TUtil.subBytes(deByte62, 19, 48);

                LogUtils.e("系统流水号：" + TUtil.bytes2HexString(mSysOrderNo));
                LogUtils.e("返回码：" + TUtil.bytes2HexString(back_code62));
                LogUtils.e("系统时间：" + TUtil.bytes2HexString(sys_time62));

                if (TUtil.bytes2HexString(back_code62).equals("00000000")) {
                    mManager.disconnect();
                    index++;
                }
                break;

            case 0x64:
                LogUtils.e("" + TUtil.bytes2HexString(TUtil.subBytes(bytes, 16, 192)));
                byte[] deByte64 = AESUtil.decrypt(TUtil.subBytes(bytes, 16, 192), TUtil.subBytes(mSk, 0, 16));
                LogUtils.e(TUtil.bytes2HexString(deByte64));
                byte[] back_code64 = TUtil.subBytes(deByte64, 8, 4);
                if (TUtil.bytes2HexString(back_code64).equals("00000000")) {
                    Toast.makeText(YctActivity.this, "交易成功了吗", Toast.LENGTH_LONG).show();
                    mManager.disconnect();
                    index++;
                } else {
                    byte[] error = TUtil.subBytes(deByte64, 12, 180);
                    String errorStr = TUtil.bytes2HexString(error);
                    int index = errorStr.indexOf("00");
                    String substr = errorStr.substring(0, index);
                    LogUtils.e(substr);
                }
                break;
        }
    }

    /**
     * 处理读卡器返回结果
     *
     * @param bytes
     */
    private void readerRsp(final byte[] bytes) {
        if (bytes[3] != 0x00) {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    ReaderError.code(mContext, bytes[3]);
                }
            });
            return;
        }
        switch (bytes[2]) {
            case 0x10:      //  R_INIT ( 初始化 )
                LogUtils.e(TUtil.bytes2HexString(bytes));
                LogUtils.e(bytes.length);
                rReaderSign(bytes);
                rPubQryPhysicsInfo();
                rCpuGetCardInfo();
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
//                LogUtils.e(TUtil.bytes2HexString(shid));
//                LogUtils.e(TUtil.bytes2HexString(sk));
//                LogUtils.e(TUtil.bytes2HexString(server_time));
//                LogUtils.e(TUtil.bytes2HexString(mLogin3Data));
                rServerSign2(mLogin3Data);
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

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        text.setText("卡号：" + TUtil.bytes2HexString(mLogicNo));
                    }
                });

//                LogUtils.e(TUtil.bytes2HexString(physics_no));
//                LogUtils.e(TUtil.bytes2HexString(logic_no));
//                LogUtils.e(TUtil.bytes2HexString(sak));
//                LogUtils.e(TUtil.bytes2HexString(card_info));
//                LogUtils.e(TUtil.bytes2HexString(sw1sw2));
                break;

            case (byte) 0x95:   //  R_PUB_SET_READCARDINFO( 设置读卡信息)
                LogUtils.e(TUtil.bytes2HexString(bytes));
                LogUtils.e(bytes.length);
                byte[] sw1sw2_95 = TUtil.subBytes(bytes, 4, 2);
                LogUtils.e("设置卡信息返回sw1sw2:" + TUtil.bytes2HexString(sw1sw2_95));
                if (TUtil.bytes2HexString(mCardStatus).equals("00")) {
                    rM1GetCardInfo();
                } else {
                    rCpuGetCardInfo();
                }

                break;

            case 0x79:      //  R_CPU_GET_CARDINFO (CPU  卡取信息)
                LogUtils.e(TUtil.bytes2HexString(bytes));
                LogUtils.e(bytes.length);
                mCardBalance = TUtil.subBytes(bytes, 24, 4);
                mCardStatus = TUtil.subBytes(bytes, 32, 1);
                mCardInfo79 = TUtil.subBytes(bytes, 33, 16);
                mMoneyLower = TUtil.subBytes(bytes, 28, 1);
                mMoneyUpper = TUtil.subBytes(bytes, 29, 3);
                byte[] sw1sw2_79 = TUtil.subBytes(bytes, 49, 2);

                LogUtils.e("CPU卡余额：" + TUtil.bytes2HexString(mCardBalance) + " 卡状态：" + TUtil.bytes2HexString(mCardStatus) + "CPU卡计数信息：" + TUtil.bytes2HexString(mCardInfo79) + "CPU卡取信息sw1sw2：" + TUtil.bytes2HexString(sw1sw2_79));

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        balance.setText(Integer.parseInt(TUtil.bytes2HexString(mCardBalance), 16) + "");
                    }
                });
                if (firstReadCard) {    //第一次读卡不进行以下操作
                    firstReadCard = false;
                    return;
                }

                if (recharge) {         //开始CPU卡圈存查询
                    pCpuLoadQry();
                    recharge = false;
                }

                if (updateRecharge) {   //开始CPU卡圈存提交
                    pCpuLoadSubmit();
                }

                LogUtils.e(TUtil.bytes2HexString(mCardInfo79));
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
                pCpuLoad(mCardInfo, mSw1sw2, mReaderBackStatus);
                break;

            case 0x7B:      //  R_CPU_LOAD (CPU  钱包圈存)
                mReaderBackStatus = TUtil.subBytes(bytes, 3, 1);
                mAuthCode = TUtil.subBytes(bytes, 4, 4);
                mSw1sw2 = TUtil.subBytes(bytes, 8, 2);
                rCpuGetCardInfo();
                break;

            case (byte) 0xB9:
                LogUtils.e(TUtil.bytes2HexString(bytes));
                mPhysicsNo = TUtil.subBytes(bytes, 8, 8);
                mLogicNo = TUtil.subBytes(bytes, 16, 8);
                mM1TranInfo = TUtil.subBytes(bytes, 24, 88);
                mCardBalance = TUtil.subBytes(bytes, 112, 4);
                mTranCounter = TUtil.subBytes(bytes, 116, 2);
                mM1Random = TUtil.subBytes(bytes, 118, 8);
                LogUtils.e("M1卡交易信息：" + TUtil.bytes2HexString(mM1TranInfo) + " M1卡余额：" + TUtil.bytes2HexString(mCardBalance) + " M1卡计数信息：" + TUtil.bytes2HexString(mTranCounter) + " 随机数：" + TUtil.bytes2HexString(mM1Random));

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        balance.setText(TUtil.bytes2HexString(mCardBalance));
                    }
                });
                break;
            default:

        }
    }

    /**
     * 终端->读卡器 初始化
     * R_INIT
     */
    private void rInit() {
        byte[] sendContentBytes = {(byte) 0xBA, 0x11, 0x10, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xB9};
        mSerialPortManager.sendBytes(sendContentBytes);
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
        byte[] b = TUtil.addBytes(a, TUtil.addBytes(TUtil.hexString2Bytes("880120000503"), TUtil.hexString2Bytes(date)));
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
        LogUtils.e(bytesToHexString(bytes));
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
        mSerialPortManager.sendBytes(send);
    }

    /**
     * 终端->读卡器 设置读卡器信息
     * R_PUB_SET_READCARDINFO
     */
    private void rSetReadCardInfo(byte[] bytes) {
        byte[] head = {(byte) 0xBA, (byte) 0x3a, (byte) 0x95};
        byte[] tran_info = bytes;
        byte[] sendContentBytes = TUtil.byteMergerAll(head, mLogicNo, tran_info);
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(sendContentBytes);
        byte[] send = TUtil.addBytes(sendContentBytes, or);
        LogUtils.e(TUtil.bytes2HexString(send));
        mSerialPortManager.sendBytes(send);
    }

    /**
     * 终端->读卡器 CPU卡信息
     * R_CPU_GET_CARDINFO
     */
    private void rM1GetCardInfo() {
        byte[] sendContentBytes = {(byte) 0xBA, (byte) 0x02, (byte) 0xB9};
        byte[] or = new byte[1];
        or[0] = TUtil.getXor(sendContentBytes);
        byte[] send = TUtil.addBytes(sendContentBytes, or);
        LogUtils.e(TUtil.bytes2HexString(send));
        mSerialPortManager.sendBytes(send);
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
        byte[] head = {(byte) 0xAA, 0x51, 00, 03, 0x02, (byte) 0x80, 0x00, (byte) 0x68};
        byte[] shid = enShid;
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] sak = mSak;
        byte[] card_info = mCardInfo;
        byte[] operation = {0x00};
        byte[] rfu = TUtil.zeros(34);
        byte[] body = TUtil.byteMergerAll(mShid, serial_no, pki_no, physics_no, logic_no, sak, card_info, operation, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);
        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, physics_no, logic_no, sak, card_info, operation, rfu, check_no);
        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, shid, signBody2);

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
                App.getInstance().setLength(112);
                MsgDataBean msgDataBean = new MsgDataBean(all);
                mManager.send(msgDataBean);
            }
        });

        LogUtils.e("SK：" + TUtil.bytes2HexString(TUtil.subBytes(mSk, 0, 16)));

    }

    /**
     * 终端->服务器 CPU卡圈存查询
     * P_CPU_LOAD_QRY
     */
    private void pCpuLoadQry() {
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        LogUtils.e("卡信息：" + TUtil.bytes2HexString(mCardInfo79));
        LogUtils.e("握手流水号：" + TUtil.bytes2HexString(mShid));
        LogUtils.e("卡状态：" + TUtil.bytes2HexString(mCardStatus));
        byte[] head = {(byte) 0xAA, (byte) 0x81, 00, 01, 0x02, (byte) 0x80, 0x00, (byte) 0x88};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] card_status = mCardStatus;    //卡状态 1字节  00 M1卡 01CPU卡 02CUP M1未锁定 03CUP M1锁定
        byte[] card_info = mCardInfo79;   //卡信息
        byte[] type = {0x00};    //卡状态 1字节  00 商户圈存 01自助圈存 10M1余额转移 ...
        byte[] money = {0x00, 0x00, 0x00, 0x50};    //交易金额 4字节
        byte[] bank_card_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //银行卡账号 16字节
        byte[] mark = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //附加信息 16字节
        byte[] extend_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //扩展流水号 8字节
        byte[] password = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //充值密码 16字节
        byte[] rfu = TUtil.zeros(22);
        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, physics_no, logic_no, card_status, card_info, type, money, bank_card_no, mark, extend_no, password, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, physics_no, logic_no, card_status, card_info, type, money, bank_card_no, mark, extend_no, password, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        LogUtils.e(TUtil.bytes2HexString(body));

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

    /**
     * 终端->服务器 圈存
     * P_CPU_LOAD
     */
    private void pCpuLoad(byte[] cardInfo, byte[] sw1sw2, byte[] back_status) {
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        LogUtils.e("AA82系统流水号：" + TUtil.bytes2HexString(mSysOrderNo));
        byte[] head = {(byte) 0xAA, (byte) 0x83, 00, 01, 0x02, (byte) 0x80, 0x00, (byte) 0x98};
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
                App.getInstance().setLength(224);
                MsgDataBean msgDataBean = new MsgDataBean(all);
                mManager.send(msgDataBean);
            }
        });
    }

    /**
     * 终端->服务器 圈存提交
     * P_CPU_LOAD_SUBMIT
     */
    private void pCpuLoadSubmit() {
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        LogUtils.e("充值凭证号：" + TUtil.bytes2HexString(mRechargeAuthCode));
        byte[] head = {(byte) 0xAA, (byte) 0x85, 00, 01, 0x02, (byte) 0x80, 0x00, (byte) 0x78};
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
                App.getInstance().setLength(80);
                MsgDataBean msgDataBean = new MsgDataBean(all);
                mManager.send(msgDataBean);
            }
        });

        LogUtils.e(TUtil.bytes2HexString(TUtil.byteMergerAll(head, enShid, signBody)));
    }

    /**
     * 终端->服务器 撤销查询
     */
    private void pCpuCancelQry() {
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        byte[] head = {(byte) 0xAA, (byte) 0x91, 00, 01, 0x02, (byte) 0x80, 0x00, (byte) 0x68};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = {(byte) 0x89, 0x00, 0x65, 0x55};
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] card_balance = {0x00, 0x00, 0x00, 0x00};    //交易金额 4字节
        byte[] money_lower = {0x00};    //金额下限 1字节
        byte[] card_info = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //卡计数信息 16字节
        byte[] money = {0x00, 0x00, 0x00, 0x00};    //交易金额 4字节
        byte[] sys_order_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        byte[] voucher_no = {0x00, 0x00, 0x00, 0x00};    //凭证号 4字节
        byte[] rfu = TUtil.zeros(31);
    }

    /**
     * 终端->服务器 撤销
     */
    private void pCpuCancel() {
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        byte[] head = {(byte) 0xAA, (byte) 0x91, 00, 01, 0x02, (byte) 0x80, 0x00, (byte) 0x68};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] sys_order_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        byte[] pki_no = {(byte) 0x89, 0x00, 0x65, 0x55};
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
        byte[] card_balance = {0x00, 0x00, 0x00, 0x00};    //交易金额 4字节
        byte[] money_lower = {0x00};    //金额下限 1字节
        byte[] card_info = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //卡计数信息 16字节
        byte[] money = {0x00, 0x00, 0x00, 0x00};    //交易金额 4字节
        byte[] order_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //流水号 8字节
        byte[] voucher_no = {0x00, 0x00, 0x00, 0x00};    //凭证号 4字节
        byte[] rfu = TUtil.zeros(31);
    }

    /**
     * 终端->服务器 M1卡圈存查询
     * P_M1_LOAD_QRY
     */
    private void pM1LoadQry() {
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        LogUtils.e("卡信息：" + TUtil.bytes2HexString(mCardInfo));
        LogUtils.e("握手流水号：" + TUtil.bytes2HexString(mShid));
        LogUtils.e("卡状态：" + TUtil.bytes2HexString(mCardStatus));
        byte[] head = {(byte) 0xAA, (byte) 0x61, 00, 01, 0x02, (byte) 0x80, 0x00, (byte) 0x98};
        byte[] shid = mShid;         //握手流水号 8字节
        byte[] serial_no = serialNo();    //报文序号 4字节
        byte[] pki_no = TUtil.hexString2Bytes(mPki);
        byte[] physics_no = mPhysicsNo;
        byte[] logic_no = mLogicNo;
//        byte[] card_status = mCardStatus;    //卡状态 1字节  00 M1卡 01CPU卡 02CUP M1未锁定 03CUP M1锁定
        byte[] card_info = mCardInfo;   //卡信息
        byte[] type = {0x00};    //卡状态 1字节  00 商户圈存 01自助圈存 10M1余额转移 ...
        byte[] money = {0x00, 0x00, 0x00, 0x50};    //交易金额 4字节
        byte[] bank_card_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //银行卡账号 16字节
        byte[] mark = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //附加信息 16字节
        byte[] extend_no = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //扩展流水号 8字节
        byte[] password = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};    //充值密码 16字节
        byte[] rfu = TUtil.zeros(23);
        byte[] body = TUtil.byteMergerAll(shid, serial_no, pki_no, physics_no, logic_no, card_info, type, money, bank_card_no, mark, extend_no, password, rfu);
        byte[] check_no = TUtil.subBytes(MD5Util.getMD5(body), 0, 4);

        byte[] signBody = TUtil.byteMergerAll(serial_no, pki_no, physics_no, logic_no, card_info, type, money, bank_card_no, mark, extend_no, password, rfu, check_no);

        byte[] signBody2 = AESUtil.encrypt(signBody, TUtil.subBytes(mSk, 0, 16));
        final byte[] all = TUtil.byteMergerAll(head, enShid, signBody2);

        LogUtils.e(TUtil.bytes2HexString(body));

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
                App.getInstance().setLength(128);
                MsgDataBean msgDataBean = new MsgDataBean(all);
                mManager.send(msgDataBean);
            }
        });

    }

    /**
     * 终端->服务器 M1卡圈存
     * P_M1_LOAD
     */
    private void pM1Load() {
        if (mPhysicsNo.length == 0) {
            ToastUtil.showEorr(mContext, "物理卡号为空");
            return;
        }
        if (mSysOrderNo.length == 0) {
            ToastUtil.showEorr(mContext, "系统序列号为空");
            return;
        }
        if (mM1TranInfo.length == 0) {
            ToastUtil.showEorr(mContext, "M1卡信息为空");
            return;
        }
        LogUtils.e("报文序号：" + TUtil.bytes2HexString(serialNo()));
        byte[] head = {(byte) 0xAA, (byte) 0x63, 00, 01, 0x02, (byte) 0x80, 0x00, (byte) 0x98};
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
                App.getInstance().setLength(208);
                MsgDataBean msgDataBean = new MsgDataBean(all);
                mManager.send(msgDataBean);
            }
        });
    }

    private byte[] serialNo() {
        String hex = index.toHexString(index);
        String sub = "";
        for (int i = 0; i < 8 - hex.length(); i++) {
            sub += "0";
        }
        return TUtil.hexString2Bytes(sub + hex);
    }
}
