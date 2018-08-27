package com.tc.emms.printer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.tc.emms.utils.LogUtils;

public class BluetoothUtil {

	private static String TAG = "BluetoothUtil";
	
	private static final UUID PRINTER_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	// 商米内置打印机地址 "00:11:22:33:44:55";

	public static BluetoothAdapter getBTAdapter() {
		return BluetoothAdapter.getDefaultAdapter();
	}

	public static BluetoothDevice getDevice(BluetoothAdapter bluetoothAdapter,
			String add_print) {
		BluetoothDevice innerprinter_device = null;
		Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
		for (BluetoothDevice device : devices) {
			if (device.getAddress().equals(add_print)) {
				innerprinter_device = device;
				break;
			}
		}
		return innerprinter_device;
	}

	public static BluetoothSocket getSocket(BluetoothDevice device)
			throws IOException {
		BluetoothSocket socket = device
				.createRfcommSocketToServiceRecord(PRINTER_UUID);
		socket.connect();
		return socket;
	}

	@SuppressLint("NewApi") 
	public static void sendData(byte[] bytes, BluetoothSocket socket)
			throws IOException {
		OutputStream out = socket.getOutputStream();
		out.write(bytes, 0, bytes.length);
		out.close();
		LogUtils.eLog(TAG, "PPPPPP sendData 蓝牙状态:" + socket.isConnected());
	}

}
