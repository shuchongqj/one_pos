package com.core.serialPort.extend;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NativeSerPort {
	private static final String TAG = "SerialPort";

	private FileDescriptor mFd;
	private FileInputStream mFileInputStream;
	private FileOutputStream mFileOutputStream;

	public static String[] GetAllports() {
		return new SerialPortFinder().getAllDevicesPath();
	}

	public boolean RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}

	private String _PortName;
	private Context _CurrContext;

	private static String LogTag = "HardSer";

	public static void LogMsg(String msg) {
		Log.d(LogTag, msg);
	}

	public NativeSerPort(String portname, Context context) {
		_PortName = portname;
		_CurrContext = context;
	}

	public boolean Open(int BaudRate) {
		String cmd = "chmod 777 " + _PortName + "\n" + "exit\n";
		if (!RootCommand(cmd)) {
			LogMsg("无法获取Root权限");
			return false;
		}
		mFd = open(_PortName, BaudRate, 0);
		if (mFd == null) {
			LogMsg("无法打开接口");
			return false;
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
		return true;
	}

	// /是否已经连接
	public boolean IsConnected() {
		return mFd != null;
	}

	// 关闭串口
	public void Close() {
		close();
	}

	// /发送数据
	public boolean WriteData(byte[] data) {
		if (!IsConnected()) {
			LogMsg("还未打开就发送");
			return false;
		}
		synchronized (this.WriteQueueLock) {
			try {
				mFileOutputStream.write(data);
			} catch (IOException e) {
				LogMsg("写入失败:" + e.getMessage());
				return false;
			}
			return true;
		}
	}

	// 接收数据
	public byte[] ReadData(int timeout) {
		return ReadData(timeout, 0, false);
	}

	private boolean WaitNewData(int timeout) {
		while (timeout >= 0) {
			try {
				if (mFileInputStream.available() > 0) {
					return true;
				}
			} catch (IOException e) {
				return false;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				return false;
			}
			timeout -= 1;
		}
		return false;
	}

	public byte[] ReadData(int timeoutFirst, int timeoutLast, boolean test) {
		if (!IsConnected()) {
			LogMsg("还未打开就接收");
			return null;
		}
		ByteArrayOutputStream commandbuffer = new ByteArrayOutputStream();
		synchronized (this.ReadQueueLock) {
			boolean hasnewdata = WaitNewData(timeoutFirst);
			while (hasnewdata) {

				try {
					byte[] readdata = new byte[mFileInputStream.available()];
					mFileInputStream.read(readdata);
					commandbuffer.write(readdata);
				} catch (IOException e) {
					break;
				}
				if (timeoutLast > 0) {
					hasnewdata = WaitNewData(timeoutLast);
				} else {
					hasnewdata = false;
				}
			}
			if (commandbuffer.size() == 0) {
				if (!test) {
					LogMsg("接收数据超时");
				}
				return null;
			}
		}
		return commandbuffer.toByteArray();
	}

	protected final Object ReadQueueLock = new Object();
	protected final Object WriteQueueLock = new Object();

	public byte[] SendAndWaitBack(byte[] data, int timeout) {
		return SendAndWaitBackLongData(data, timeout, 0);
	}

	public byte[] SendAndWaitBackLongData(byte[] data, int timeoutfirst,
			int timeoutlast) {
		// /清空接收缓冲数据
		byte[] readdata = ReadData(1, 0, true);
		while (readdata != null) {
			readdata = ReadData(1, 0, true);
		}
		// 写入
		if (!WriteData(data)) {
			return null;
		}
		return ReadData(timeoutfirst, timeoutlast, false);
	}

	public String GetPortName() {
		return _PortName;
	}

	private native static FileDescriptor open(String path, int baudrate, int flags);

	public native void close();

	static {
		System.loadLibrary("native-lib");
	}
}
