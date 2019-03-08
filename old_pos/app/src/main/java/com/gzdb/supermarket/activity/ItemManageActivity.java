package com.gzdb.supermarket.activity;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.tts.tools.CommonUtility;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.okgo.callback.JsonCallback;
import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.gzdb.basepos.App;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.basepos.greendao.GoodBeanDao;
import com.gzdb.printer.DeviceConnFactoryManager;
import com.gzdb.printer.PrintUtils;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.sunmi.bluetooth.BluetoothUtils;
import com.gzdb.sunmi.bluetooth.PrinterBarCode;
import com.gzdb.supermarket.LoginActivity;
import com.gzdb.supermarket.MyService;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.adapter.ItemManageAdapter;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.been.GoodListBean;
import com.gzdb.supermarket.been.GoodTypeResultBean;
import com.gzdb.supermarket.been.GoodTypesBean;
import com.gzdb.supermarket.dialog.DiscountDialog;
import com.gzdb.supermarket.dialog.SelectDialog;
import com.gzdb.supermarket.dialog.SelectPrintDialog;
import com.gzdb.supermarket.dialog.StampSettingDialog;
import com.gzdb.supermarket.dialog.TipClickListener;
import com.gzdb.supermarket.dialog.TipDialog;
import com.gzdb.supermarket.event.PrintTypeEvent;
import com.gzdb.supermarket.event.RefreshEvent;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
import static com.gzdb.printer.Constant.ACTION_USB_PERMISSION;
import static com.gzdb.printer.DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE;
import static com.gzdb.printer.DeviceConnFactoryManager.CONN_STATE_FAILED;

/**
 *
 * @author liubolin
 * @date 2018/5/16
 */

public class ItemManageActivity extends BaseActivity implements SelectDialog.SelectDialogHost, View.OnClickListener, ItemManageAdapter.ItemManageAdapterHost, StampSettingDialog.StampSettingHost {

    public static final int CODE_UPDATE_ALL = 104;//刷新所有
    // 打印
    public static final int MESSAGE_CONNECT = 1;
    public static final String CONNECT_STATUS = "connect.status";
    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
    @Bind(R.id.back)
    View back;
    @Bind(R.id.all_select_img)
    ImageView all_select_img;
    @Bind(R.id.all_select)
    View all_select;
    @Bind(R.id.tv_item_type)
    TextView tv_item_type;
    @Bind(R.id.ll_item_type)
    LinearLayout ll_item_type;
    @Bind(R.id.et_item_search)
    EditText et_item_search;
    @Bind(R.id.btn_search)
    View btn_search;
    @Bind(R.id.btn_stamp)
    TextView btn_stamp;
    @Bind(R.id.btn_stamp_connect)
    public TextView btn_stamp_connect;
    @Bind(R.id.item_list)
    ListView item_list;
    Dialog dialog;
    TipDialog deleteDialog;
    TipDialog stampDialog;
    TipDialog stampParamDialog;
    SelectDialog mSelectDialog;
    StampSettingDialog mStampSettingDialog;
    ItemManageAdapter mAdapter;
    List<String> itemType = new ArrayList<>();
    List<GoodBean> itemLists = new ArrayList<>();
    List<GoodTypesBean> itemTypes = new ArrayList<>();
    boolean isAll = false;
    private int curentTypeIndex = -1;
    Dialog wheelViewDialog;

    private SelectPrintDialog selectPrintDialog;
    private int printType;
    private DiscountDialog discountDialog;
    private BluetoothAdapter mBluetoothAdapter;
    @Bind(R.id.ll_body)
    LinearLayout mBody;
    @Bind(R.id.fl_body)
    FrameLayout fl_body;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_manage);
        ButterKnife.bind(this);
        BluetoothUtils.mContext = mContext;
        EventBus.getDefault().register(this);
        PrinterBarCode.intClass(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            mBody.removeAllViews();
            for (final BluetoothDevice device : pairedDevices) {
                View view_items = LayoutInflater.from(ItemManageActivity.this).inflate(R.layout.item_bluetooth, null, false);
                TextView tv_title = (TextView) view_items.findViewById(R.id.tv_title);
                TextView tv_mac = (TextView) view_items.findViewById(R.id.tv_mac);
                tv_title.setText(device.getName());
                tv_mac.setText(device.getAddress());
                if (device.getName().toUpperCase().indexOf("INNERPRINTER") == -1) {
                    view_items.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fl_body.setVisibility(View.GONE);
                            SPUtil.getInstance().putString(mContext, "stamp_code", device.getAddress());
                            try {
                                startService(new Intent(ItemManageActivity.this, MyService.class));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    mBody.addView(view_items);
                }
                //  mPairedDevicesArrayAdapter.add(device.getName() + "\n"+ device.getAddress());
            }
        }
        printType = SPUtil.getInstance().getInt(mContext, "print_type");

        dialog = DialogUtil.loadingDialog(this, "加载中...");
        dialog.show();
        mSelectDialog = new SelectDialog(this, this);

        deleteDialog = DialogUtil.createTipDialog(this, null, "确认删除此商品吗？", "取消", "确定", null);
        stampDialog = DialogUtil.createTipDialog(this, "打印机未连接", "我知道了");

        stampParamDialog = DialogUtil.createTipDialog(this, "打印机正在打印中...", "确定");
        stampParamDialog.getRightBtn().setVisibility(View.GONE);
        stampParamDialog.setCancelable(false);

        mStampSettingDialog = new StampSettingDialog(this);
        mStampSettingDialog.setHost(this);

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.equals("\n") || source.equals("\r")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        et_item_search.setFilters(new InputFilter[]{filter});

        et_item_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pinyinSearch(s.toString());
            }
        });

        et_item_search.setOnClickListener(this);

        mAdapter = new ItemManageAdapter(this);
        item_list.setAdapter(mAdapter);
        mAdapter.setHost(this);

        back.setOnClickListener(this);
        all_select.setOnClickListener(this);
        ll_item_type.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_stamp.setOnClickListener(this);
        btn_stamp_connect.setOnClickListener(this);
        initStatus();

//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        // Get a set of currently paired devices
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//        // If there are paired devices, add each one to the ArrayAdapter
//        if (pairedDevices.size() > 0) {
//            String stampCode = SPUtil.getInstance().getString(this, "stamp_code");
//            if (!TextUtils.isEmpty(stampCode)) {
//                initPrint(stampCode);
//            }else {
//                for (BluetoothDevice device : pairedDevices) {
//                    Log.d("hello",device.getName() + "\n"
//                            + device.getAddress());
//                    if(device.getName().indexOf("InnerPrinter")==-1){
//                        SPUtil.getInstance().putString(ItemManageActivity.this,"stamp_code",device.getAddress());
//                        initPrint(device.getAddress());
//                    }
//                }
//            }
//
//        }


        refreshFromNet();
//        if(!PrintUtils.isPrint) {
//            PrintUtils.disConn();
//            PrintUtils.initPrintParam(ItemManageActivity.this);
//        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {

                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                    switch (state) {
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            if (PrintUtils.id == deviceId) {
                                PrintUtils.isPrint = false;
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            PrintUtils.isPrint = true;
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            PrintUtils.isPrint = true;
                            initStatus();
                            break;
                        case CONN_STATE_FAILED:
                            PrintUtils.isPrint = false;
                            break;
                        default:
                            break;
                    }

                    break;
                default:
                    break;
            }
        }
    };

    private void initStatus() {
        if (PrintUtils.isPrint) {
            btn_stamp_connect.setText("已连接");
        } else {
            btn_stamp_connect.setText("未连接");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_QUERY_PRINTER_STATE);
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void refreshFromNet() {
        ((TextView) dialog.findViewById(R.id.progress_content)).setText("加载数据中...");
        dialog.show();
        app.getDaoInstant().getGoodBeanDao().deleteAll();
        app.getDaoInstant().getGoodUnitBeanDao().deleteAll();
        app.getDaoInstant().getGoodTypesBeanDao().deleteAll();
        getType();
    }

    public void getType() {
        setDialogMsg("数据加载中" + "...");
        OkGo.<NydResponse<GoodTypeResultBean>>post(Contonts.URL_GET_GOODTYPELIST)
                .execute(new JsonCallback<NydResponse<GoodTypeResultBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<GoodTypeResultBean>> response) {
                        setDialogMsg("数据加载中" + "...");
                        app.getDaoInstant().getGoodTypesBeanDao().insertOrReplaceInTx(response.body().response.getItemTypes());
                        app.getDaoInstant().getGoodUnitBeanDao().insertOrReplaceInTx(response.body().response.getItemUnitList());
                        itemTypes = response.body().response.getItemTypes();
                        initType();
                        getGoodList("");
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<GoodTypeResultBean>> response) {
                        super.onError(response);
                        dialog.dismiss();
                    }
                });
    }

    private void initType() {
        itemType.clear();
        itemType.add("全部");
        for (int i = 0; i < itemTypes.size(); i++) {
            itemType.add(itemTypes.get(i).getTitle());
        }
        mSelectDialog.setListData(itemType);
    }

    public void setDialogMsg(String msg) {
        ((TextView) dialog.findViewById(R.id.progress_content)).setText(msg);
    }

    public void getGoodList(final String typeId) {
        HttpParams params = new HttpParams();
        if (!BaseUtils.isEmpty(typeId) && !"BBBBB".equals(typeId)) {
            params.put("itemTypeId", typeId);
        }
        setDialogMsg("数据加载中" + "...");
        OkGo.<NydResponse<GoodListBean>>post(Contonts.URL_GET_GOODLIST)
                .params(params)
                .execute(new JsonCallback<NydResponse<GoodListBean>>() {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<GoodListBean>> response) {
                        setDialogMsg("数据加载中" + "...");
                        if (response.body().response.getDatas().size() == 0) {
                            mAdapter.clearList();
                            mAdapter.notifyDataSort();
                            app.getDaoInstant().getGoodBeanDao().deleteAll();
                        } else {
                            app.getDaoInstant().getGoodBeanDao().insertOrReplaceInTx(response.body().response.getDatas());
                            itemLists = response.body().response.getDatas();
                            mAdapter.setList(itemLists);
                            mAdapter.notifyDataSort();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<GoodListBean>> response) {
                        super.onError(response);
                        dialog.dismiss();
                    }
                });
    }

    public void pinyinSearch(String key) {
        List<GoodBean> items = null;

        if (curentTypeIndex == 0 || curentTypeIndex == -1) {
            items = app.getDaoInstant().getGoodBeanDao().queryBuilder()
                    .whereOr(GoodBeanDao.Properties.Barcode.like("%" + key + "%"), GoodBeanDao.Properties.ItemName.like("%" + key + "%")).list();
        } else {
            items = app.getDaoInstant().getGoodBeanDao().queryBuilder().where(GoodBeanDao.Properties.ItemTypeId.eq(itemTypes.get(curentTypeIndex - 1).getItemTypeId()))
                    .whereOr(GoodBeanDao.Properties.Barcode.like("%" + key + "%"), GoodBeanDao.Properties.ItemName.like("%" + key + "%")).list();
        }

        if (items != null) {
            mAdapter.setList(items);
        } else {
            mAdapter.clearList();
        }
        mAdapter.notifyDataSort();
    }

    public void refreshCahce() {
        if (curentTypeIndex == 0 || -1 == curentTypeIndex) {
            getGoodList("");
        } else {
            getGoodList(itemTypes.get(curentTypeIndex - 1).getItemTypeId());
        }
    }

    @Override
    public void finish() {
        setResult(SupermarketIndexActivity.requestCode_UPDATE_ALL);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        unregisterReceiver(receiver);
//        if (conn != null) {
//            unbindService(conn); // unBindService
//        }
//        if (PrinterStatusBroadcastReceiver != null) {
//            try {
//                unregisterReceiver(PrinterStatusBroadcastReceiver);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void printEvent(PrintTypeEvent event) {
        printType = SPUtil.getInstance().getInt(mContext, "print_type");
    }

    @Override
    public void selectDialog(int position) {
        curentTypeIndex = position;
        if (position == 0) {
            mAdapter.setList(app.getDaoInstant().getGoodBeanDao().loadAll());
        } else {
            mAdapter.setList(app.getDaoInstant().getGoodBeanDao().queryBuilder().where(GoodBeanDao.Properties.ItemTypeId.eq(itemTypes.get(position - 1).getItemTypeId())).list());
        }
        tv_item_type.setText(itemType.get(position));
        mAdapter.notifyDataSort();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.back) {
            EventBus.getDefault().post(new RefreshEvent());
            finish();
        } else if (id == R.id.all_select) {
            isAll = !isAll;
            mAdapter.selectAll(isAll);
            if (isAll) {
                all_select_img.setImageDrawable(getResources().getDrawable(R.mipmap.btn_blue));
            } else {
                all_select_img.setImageDrawable(getResources().getDrawable(R.mipmap.btn_normal));
            }
        } else if (id == R.id.ll_item_type) {
            mSelectDialog.showViewSize(ll_item_type);
            mSelectDialog.showAsDropDown(ll_item_type);
        } else if (id == R.id.btn_search) {
            String st = et_item_search.getText().toString();
            pinyinSearch(st);

        } else if (id == R.id.btn_stamp) {
            if (printType == 6 || printType == 7) {
                stampSelectAll();
                return;
            } else if (PrintUtils.isPrint) {
                stampSelectAll();
            } else {

                stampDialog.show();
            }
        } else if (id == R.id.btn_stamp_connect) {
            //   mStampSettingDialog.show();
            // PrintUtils.onDestroy();
            fl_body.setVisibility(View.VISIBLE);
            if (PrintUtils.isPrint) {
                PrintUtils.disConn();
                PrintUtils.initPrintParam(ItemManageActivity.this);
            }

            // }
        } else if (id == R.id.et_item_search) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
        }
    }

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MESSAGE_CONNECT:
                    //  connectOrDisConnectToDevice(message.arg1);
            }
            return false;
        }
    });

    private void stampSelectAll() {
        final List<ItemManageAdapter.ItemManageDao> sList = mAdapter.getSelectList();
        if (0 == sList.size()) {
            ToastUtil.showToast(this, "请选择打印商品！");
            return;
        }
        stampParamDialog.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stampParamDialog.dismiss();
            }
        }, 1000 * sList.size());
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                GoodBean item;
                if (printType == 6) {
                    for (int i = 0; i < sList.size(); i++) {
                        item = sList.get(i).item;
                        PrinterBarCode.outcomeTacket(item);
                    }
                    return;
                }
                for (int i = 0; i < sList.size(); i++) {
                    item = sList.get(i).item;
                    if (PrintUtils.isPrint) {
                        String vipPrice = "";
                        if (item.getMembershipPrice() > 0) {
                            vipPrice = String.valueOf(item.getMembershipPrice());
                        }
                        switch (printType) {
                            case 0:
                                sendLabelPos(printType, item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                                break;
                            case 1:
                                sendLabel2(printType, item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                                break;
                            case 2:
                                sendLabelPos(printType, item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                                break;
                            case 3:
                                sendLabel2(printType, item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                                break;
                            case 4:
                                sendLabelPos3(printType, item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                                break;
                            case 5:
                                sendLabel(item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                                break;
                            case 7:
                                sendLabelPosMin(item.getItemName(), item.getBarcode());
                                break;
                        }
                    } else {
                        stampDialog.show();
                        return;
                    }
                }
            }
        });
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CODE_UPDATE_ALL) {
            refreshCahce();
        }
    }

    @Override
    public void editItem(GoodBean item) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra("itemId", item.getId());
        startActivityForResult(intent, CODE_UPDATE_ALL);
    }

    @Override
    public void stampItem(GoodBean item) {
        if (printType == 6) {
            PrinterBarCode.outcomeTacket(item);
            return;
        }
        //  if (PrintUtils.isPrint) {
        et_item_search.setText("");
        et_item_search.setFocusable(true);
        et_item_search.setFocusableInTouchMode(true);
        et_item_search.requestFocus();
        String vipPrice = "";
        if (item.getMembershipPrice() > 0) {
            vipPrice = String.valueOf(item.getMembershipPrice());
        }
        switch (printType) {
            case 0:
                sendLabelPos(printType, item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                break;
            case 1:
                sendLabel2(printType, item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                break;
            case 2:
                sendLabelPos(printType, item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                break;
            case 3:
                sendLabel2(printType, item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                break;
            case 4:
                sendLabelPos3(printType, item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                break;
            case 5:
                sendLabel(item.getItemName(), item.getItemUnitName(), item.getBarcode(), String.valueOf(item.getSalesPrice()), vipPrice, "");
                break;
            case 7:

                sendLabelPosMin(item.getItemName(), item.getBarcode());
                break;
        }
//        } else {
//            stampDialog.show();
//        }
    }

    @Override
    public void deleteItem(final GoodBean item) {
        deleteDialog.setTipClickListener(new TipClickListener() {
            @Override
            public void onClick(boolean left) {
                if (!left) {
                    OkGo.<NydResponse<GoodTypeResultBean>>post(Contonts.URL_DELETE_GOOD)
                            .params("posItemId", item.getId())
                            .execute(new JsonCallback<NydResponse<GoodTypeResultBean>>() {
                                @Override
                                public void onSuccess(com.lzy.okgo.model.Response<NydResponse<GoodTypeResultBean>> response) {
                                    ToastUtil.showSuccess(mContext, "删除成功！");
                                    app.getDaoInstant().getGoodBeanDao().delete(item);
                                    refreshCahce();
                                }
                            });
                }
            }
        });
        deleteDialog.show();
    }


    void sendLabelPos(int type, String title, String until, String encode, String price, String memberPrice, String code) {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(60, 35); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(25); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(75, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区

        // 绘制简体中文
        tsc.addText(130, 45, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                title);

        tsc.addText(115, 85, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                code);

        tsc.addText(300, 90, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                until);
        tsc.addText(105, 135, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                encode);
        if (type == 0) {
            tsc.addText(145, 180, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    price);
            tsc.addText(145, 225, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    memberPrice);
        } else {
            tsc.addText(145, 170, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    price);
            tsc.addText(145, 225, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    memberPrice);
        }
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        //  tsc.addCutter(EscCommand.ENABLE.ON);
        PrintUtils.sendLabelPos(tsc);

    }

    void sendLabelPos3(int type, String title, String until, String encode, String price, String memberPrice, String code) {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(60, 35); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(25); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(75, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区

        // 绘制简体中文
        tsc.addText(80, 40, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                title);

//        tsc.addText(115, 50, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
//                code);
        tsc.addText(80, 90, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                encode);
        tsc.addText(250, 135, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                until);
        if (type == 0) {
            tsc.addText(35, 215, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    price);
            tsc.addText(320, 225, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    memberPrice);
        } else {
            tsc.addText(10, 195, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    price);
            tsc.addText(245, 200, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    memberPrice);
        }
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        //tsc.addCutter(EscCommand.ENABLE.ON);
        PrintUtils.sendLabelPos(tsc);

    }

    void sendLabelPosMin(String title, String encode) {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(40, 30); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(25); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区

        // 绘制简体中文
        tsc.addText(10, 15, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
                title);

        String str = getCutCode(encode);
        tsc.add1DBarcode(10, 80, LabelCommand.BARCODETYPE.CODE128, 95, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, str);
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        //tsc.addCutter(EscCommand.ENABLE.ON);
        PrintUtils.sendLabelPos(tsc);

    }

    private String getCutCode(String encode) {
//        if (encode.length() > 11) {
//            return encode.substring(encode.length() - 10, encode.length());
//        } else {
//            return encode;
//        }
        return encode;
    }

    void sendLabelPosMinLable(String encode) {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(40, 30); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(25); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        // tsc.add1DBarcode(25, 85, LabelCommand.BARCODETYPE.CODE128, 205, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, encode);
        tsc.add1DBarcode(40, 40, LabelCommand.BARCODETYPE.CODE128, 180, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, encode);

        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        //tsc.addCutter(EscCommand.ENABLE.ON);

        PrintUtils.sendLabelPos(tsc);

    }

    void sendLabel(String title, String until, String encode, String price, String memberPrice, String code) {

        LabelCommand tsc = new LabelCommand();
        tsc.addSize(60, 35); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(25); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(75, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区

        // 绘制简体中文
        tsc.addText(130, 40, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                title);

        tsc.addText(115, 85, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                code);

        tsc.addText(300, 90, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                until);
        tsc.addText(105, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                encode);
        tsc.addText(145, 175, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_3, LabelCommand.FONTMUL.MUL_3,
                price);
//        tsc.addText(145, 225, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
//                memberPrice);


        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        //  tsc.addCutter(EscCommand.ENABLE.ON);
        PrintUtils.sendLabelPos(tsc);


    }

    void sendLabel2(int type, String title, String until, String encode, String price, String memberPrice, String code) {

        LabelCommand tsc = new LabelCommand();
        tsc.addSize(60, 35); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(25); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(75, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区

        // 绘制简体中文
        tsc.addText(130, 50, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                title);

        tsc.addText(115, 85, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                code);

        tsc.addText(300, 90, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                until);
        tsc.addText(105, 135, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                encode);
        if (type == 1) {
            tsc.addText(145, 175, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    price);
            tsc.addText(145, 210, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    memberPrice);
        } else {
            tsc.addText(145, 168, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    price);
            tsc.addText(145, 220, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    memberPrice);
        }

        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        //     tsc.addCutter(EscCommand.ENABLE.ON);
        PrintUtils.sendLabelPos(tsc);

    }

    @OnClick({R.id.btn_vip_price, R.id.btn_type, R.id.btn_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_vip_price:
                discountDialog = new DiscountDialog(mContext, "会员价快捷设置", "此功能可以为所有的商品设置相同的会员价折扣（设置0.0折可恢复原价）", 9.5, new DiscountDialog.PriorityListener() {
                    @Override
                    public void refreshPriorityUI(double discount) {
                        discountDialog.dismiss();
                        setVipPrice(discount);
                    }
                });
                discountDialog.show();
                break;
            case R.id.btn_type:
                selectPrintDialog = new SelectPrintDialog(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectPrintDialog.dismiss();
                    }
                });
                selectPrintDialog.show();
                break;
            case R.id.btn_print:
                wheelViewDialog = createWheelViewDialog();
                wheelViewDialog.show();
                break;
        }
    }

    public Dialog createWheelViewDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog);
        try {

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = View.inflate(ItemManageActivity.this, R.layout.location_dialog,
                    null);
            TextView tv_dialog_close = (TextView) view.findViewById(R.id.tv_dialog_close);
            TextView tv_dialog_ok = (TextView) view.findViewById(R.id.tv_dialog_ok);

            final EditText edit_location01 = (EditText) view.findViewById(R.id.edit_location01);
            final EditText edit_location02 = (EditText) view.findViewById(R.id.edit_location02);
            final EditText edit_location03 = (EditText) view.findViewById(R.id.edit_location03);
            final EditText edit_location04 = (EditText) view.findViewById(R.id.edit_location04);

            tv_dialog_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wheelViewDialog.dismiss();

                }
            });

            tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wheelViewDialog.dismiss();

                    String l1 = edit_location01.getText().toString();
                    String l2 = edit_location02.getText().toString();
                    String l3 = edit_location03.getText().toString();
                    String l4 = edit_location04.getText().toString();
                    sendLabelPosMinLable(l1 + "-" + l2 + "-" + l3 + "-" + l4);

                }
            });

            dialog.setContentView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    @Override
    public void connectStamp(String code) {

    }

    private void setVipPrice(double discountPercent) {
        OkGo.<NydResponse<Object>>post(Contonts.SET_VIP_PRICE)
                .params("discountPercent", discountPercent)
                .execute(new DialogCallback<NydResponse<Object>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<Object>> response) {
                        refreshFromNet();
                    }
                });
    }

}
