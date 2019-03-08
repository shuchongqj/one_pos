package com.core.serialPort.extend;

import java.io.File;

public interface ISerPort {
	boolean openSerialPort(File device, int baudRate);

	boolean isConnected();

	void closeSerialPort();

	boolean writeData(byte[] data);

	byte[] readData(int timeout);

	byte[] readData(int timeoutFirst, int timeoutLast, boolean test);

	byte[] sendAndWaitBack(byte[] data, int timeout);

	byte[] sendAndWaitBackLongData(byte[] data, int timeoutfirst,
                                   int timeoutlast);
}
