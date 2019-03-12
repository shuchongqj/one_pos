package com.one.pos.service.gprint.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gprinter.command.GpCom;
import com.gprinter.io.BluetoothPort;
import com.gprinter.io.GpPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.Vector;

public class BluetoothPortImpl {

    private static final String DEBUG_TAG = "BluetoothService";
    private static final UUID SERIAL_PORT_SERVICE_CLASS_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter mAdapter = null;
    private BluetoothPortImpl.ConnectThread mConnectThread = null;
    private BluetoothPortImpl.ConnectedThread mConnectedThread = null;
    BluetoothDevice mDevice = null;

    public BluetoothPortImpl(int id, BluetoothDevice device, Handler handler) {
        this.mAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mState = 0;
        this.mHandler = handler;
        this.mDevice = device;
        this.mPrinterId = id;
    }


    private void connect() {
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }

        this.mConnectThread = new BluetoothPortImpl.ConnectThread(this.mDevice);
        this.mConnectThread.start();
        this.setState(2);
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        Log.d("BluetoothService", "connected");
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }

        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }

        this.mConnectedThread = new BluetoothPortImpl.ConnectedThread(socket);
        this.mConnectedThread.start();
        Message msg = this.mHandler.obtainMessage(4);
        Bundle bundle = new Bundle();
        bundle.putInt("printer.id", this.mPrinterId);
        bundle.putString("device_name", device.getName());
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
        this.setState(3);
    }

    public synchronized void stop() {
        Log.d("BluetoothService", "stop");
        this.setState(0);
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }

        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }

    }

    public GpCom.ERROR_CODE writeDataImmediately(byte[] datas) {
        GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
        BluetoothPortImpl.ConnectedThread r;
        synchronized (this) {
            if (this.mState != 3) {
                return GpCom.ERROR_CODE.PORT_IS_NOT_OPEN;
            }

            r = this.mConnectedThread;
        }

        retval = r.writeDataImmediately(datas);
        return retval;
    }

    public GpCom.ERROR_CODE writeDataImmediately(Vector<Byte> var1) {
        if (var1 != null && var1.size() > 0) {
            byte[] datas = new byte[var1.size()];
            for (int i = 0; i < var1.size(); i++) {
                datas[i] = var1.get(i);
            }
            return writeDataImmediately(datas);
        }
        return GpCom.ERROR_CODE.SUCCESS;
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            this.mmDevice = device;
            BluetoothSocket tmp = null;

            try {
                tmp = device.createRfcommSocketToServiceRecord(BluetoothPortImpl.SERIAL_PORT_SERVICE_CLASS_UUID);
            } catch (IOException var5) {
                Log.e("BluetoothService", "create() failed", var5);
            }

            this.mmSocket = tmp;
        }

        @Override
        public void run() {
            Log.i("BluetoothService", "BEGIN mConnectThread");
            this.setName("ConnectThread");
            BluetoothPortImpl.this.mAdapter.cancelDiscovery();

            try {
                this.mmSocket.connect();
            } catch (IOException var5) {
                BluetoothPortImpl.this.connectionFailed();

                try {
                    if (this.mmSocket != null) {
                        this.mmSocket.close();
                    }
                } catch (IOException var3) {
                    Log.e("BluetoothService", "unable to close() socket during connection failure", var3);
                }

                BluetoothPortImpl.this.stop();
                return;
            }

            BluetoothPortImpl var1 = BluetoothPortImpl.this;
            synchronized (BluetoothPortImpl.this) {
                BluetoothPortImpl.this.mConnectThread = null;
            }

            BluetoothPortImpl.this.connected(this.mmSocket, this.mmDevice);
        }

        public void cancel() {
            try {
                if (this.mmSocket != null) {
                    this.mmSocket.close();
                }
                this.mmSocket = null;
            } catch (IOException var2) {
                Log.e("BluetoothService", "close() of connect socket failed", var2);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d("BluetoothService", "create ConnectedThread");
            this.mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException var6) {
                Log.e("BluetoothService", "temp sockets not created", var6);
            }

            this.mmInStream = tmpIn;
            this.mmOutStream = tmpOut;
        }

        @Override
        public void run() {
            Log.i("BluetoothService", "BEGIN mConnectedThread");
            BluetoothPortImpl.this.mClosePort = false;

            while (!BluetoothPortImpl.this.mClosePort) {
                try {
                    byte[] ReceiveData = new byte[100];
                    int bytes = this.mmInStream.read(ReceiveData);
                    Log.d("BluetoothService", "bytes " + bytes);
                    if (bytes > 0) {
                        Message msg = BluetoothPortImpl.this.mHandler.obtainMessage(2);
                        Bundle bundle = new Bundle();
                        bundle.putInt("printer.id", BluetoothPortImpl.this.mPrinterId);
                        bundle.putInt("device.readcnt", bytes);
                        bundle.putByteArray("device.read", ReceiveData);
                        msg.setData(bundle);
                        BluetoothPortImpl.this.mHandler.sendMessage(msg);
                    }
                } catch (IOException var5) {
                    BluetoothPortImpl.this.connectionLost();
                    var5.printStackTrace();
                    Log.e("BluetoothService", "disconnected", var5);
                    break;
                } catch (Exception var6) {
                    Log.e("BluetoothService", "disconnected", var6);
                }
            }

            Log.d("BluetoothService", "Closing Bluetooth work");
        }

        public void cancel() {
            try {
                BluetoothPortImpl.this.mClosePort = true;
                this.mmOutStream.flush();
                if (this.mmSocket != null) {
                    this.mmSocket.close();
                }
            } catch (IOException var2) {
                Log.e("BluetoothService", "close() of connect socket failed", var2);
            }

        }

        public GpCom.ERROR_CODE writeDataImmediately(byte[] datas) {
            GpCom.ERROR_CODE retval = GpCom.ERROR_CODE.SUCCESS;
            if (this.mmSocket != null && this.mmOutStream != null) {
                if (datas != null && datas.length > 0) {
                    try {
                        this.mmOutStream.write(datas);
                        this.mmOutStream.flush();
                    } catch (Exception var5) {
                        Log.d("BluetoothService", "Exception occured while sending data immediately: " + var5.getMessage());
                        retval = GpCom.ERROR_CODE.FAILED;
                    }
                }
            } else {
                retval = GpCom.ERROR_CODE.PORT_IS_NOT_OPEN;
            }

            return retval;
        }
    }


    protected synchronized void setState(int state) {
        if (this.mState != state) {
            Log.d("GpPort", "setState() " + this.mState + " -> " + state);
            Log.d("GpPort", "PrinterId() " + this.mPrinterId + " -> " + this.mPrinterId);
            this.mState = state;
            Message msg = this.mHandler.obtainMessage(1);
            Bundle bundle = new Bundle();
            bundle.putInt("printer.id", this.mPrinterId);
            bundle.putInt("device_status", state);
            msg.setData(bundle);
            this.mHandler.sendMessage(msg);
        } else {
            Log.d("GpPort", "STATE NOT CHANGE");
        }

    }

    protected boolean mClosePort;
    protected int mState;
    protected Handler mHandler = null;
    protected int mmBytesAvailable;
    protected int mPrinterId;


    protected int getState() {
        return this.mState;
    }

    protected void connectionFailed() {
        this.setState(0);
    }

    protected void closePortFailed() {
        this.setState(0);
        Log.d("GpPort", "closePortFailed ");
    }

    protected void connectionLost() {
        this.setState(0);
        Log.d("GpPort", "connectionLost ");
    }

    protected void invalidPrinter() {
        this.setState(0);
        Message msg = this.mHandler.obtainMessage(5);
        Bundle bundle = new Bundle();
        bundle.putInt("printer.id", this.mPrinterId);
        bundle.putString("toast", "Please use Gprinter");
        msg.setData(bundle);
        this.mHandler.sendMessage(msg);
    }

    protected void connectionToPrinterFailed() {
        this.setState(0);
        Log.d("GpPort", "Close port failed ");
    }
}
