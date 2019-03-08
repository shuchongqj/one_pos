package com.gzdb.supermarket.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *该类主要用于catch中的异常写入文件流中
 */
public class LogUtils {
    /**
     * 将文件保存到本地
     */
    private File file;
    private BufferedOutputStream os = null;
    public void saveToPhone() {
        File fileDirectory = new File(Environment.getExternalStorageDirectory()+ "/点吧科技/");
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                fileDirectory.mkdir();
                if (file==null) {
                    file=new File(fileDirectory.getAbsoluteFile()+ "/ErrorMessage.txt");
                }
                os = new BufferedOutputStream(new FileOutputStream(file));
            } else {
                if (file==null) {
                    file= new File("\\sdcard\\点吧科技\\ErrorMessage.txt");
                }
                os = new BufferedOutputStream(new FileOutputStream(file));
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void writeErrotTxTToFile(){

    }

    public static void setLog(String msg) {
        Log.i("System", msg);
    }
}
