package com.one.pos.service.scane;

import android.os.Handler;
import android.view.KeyEvent;


/**
 * 扫码枪事件解析类
 * @author zhumg
 */
public class ScanGunKeyEventHelper {

    private final static long MESSAGE_DELAY = 100; // 延迟50ms，判断扫码是否完成。
    private StringBuffer mStringBufferResult; // 扫码内容
    private boolean mCaps; // 大小写区分
    private final Handler mHandler;
    private final Runnable mScanningFishedRunnable;
    private OnScanSuccessListener mOnScanSuccessListener;

    public ScanGunKeyEventHelper(OnScanSuccessListener onScanSuccessListener) {
        mOnScanSuccessListener = onScanSuccessListener;
        mStringBufferResult = new StringBuffer();
        mHandler = new Handler();

        mScanningFishedRunnable = new Runnable() {
            @Override
            public void run() {
                performScanSuccess();
            }
        };
    }

    /**
     * 返回扫码成功后的结果
     */
    private void performScanSuccess() {
        String barcode = mStringBufferResult.toString();
        if (mOnScanSuccessListener != null) {
            mOnScanSuccessListener.onScanSuccess(barcode);
        }
        mStringBufferResult.setLength(0);
    }

    /**
     * 扫码枪事件解析
     *
     * @param event
     */
    public void analysisKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        // 字母大小写判断
        checkLetterStatus(event);

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            char aChar = getInputCode(event);
            if (aChar != 0) {
                mStringBufferResult.append(aChar);
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                // 若为回车键，直接返回
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.post(mScanningFishedRunnable);
            } else {
                // 延迟post，若50ms内，有其他事件
                mHandler.removeCallbacks(mScanningFishedRunnable);
                mHandler.postDelayed(mScanningFishedRunnable, MESSAGE_DELAY);
            }
        }
    }

    // 检查shift键
    private void checkLetterStatus(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            // 按着shift键，表示大写
            // 松开shift键，表示小写
            mCaps = (event.getAction() == KeyEvent.ACTION_DOWN);
        }
    }

    // 获取扫描内容
    private char getInputCode(KeyEvent event) {
        int keyCode = event.getKeyCode();
        char aChar;
        if (keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_Z) {
            // 字母
            aChar = (char) ((mCaps ? 'A' : 'a') + keyCode - KeyEvent.KEYCODE_A);
        } else if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {
            // 数字
            aChar = (char) ('0' + keyCode - KeyEvent.KEYCODE_0);
        } else {
            // 其他符号
            switch (keyCode) {
                case KeyEvent.KEYCODE_PERIOD:
                    aChar = '.';
                    break;
                case KeyEvent.KEYCODE_MINUS:
                    aChar = mCaps ? '_' : '-';
                    break;
                case KeyEvent.KEYCODE_SLASH:
                    aChar = '/';
                    break;
                case KeyEvent.KEYCODE_BACKSLASH:
                    aChar = mCaps ? '|' : '\\';
                    break;
                default:
                    aChar = 0;
                    break;
            }
        }
        return aChar;
    }

    public interface OnScanSuccessListener {
        void onScanSuccess(String barcode);
    }

    public void onDestroy() {
        mHandler.removeCallbacks(mScanningFishedRunnable);
        mOnScanSuccessListener = null;
    }
}