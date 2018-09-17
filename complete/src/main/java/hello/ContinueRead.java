package hello;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.xml.crypto.Data;

import gnu.io.*;

public class ContinueRead extends Thread implements SerialPortEventListener { // SerialPortEventListener
	static CommPortIdentifier portId;
	static Enumeration<?> portList;
	InputStream inputStream;
	static OutputStream outputStream;
	static SerialPort serialPort;
	public String waternums;
	private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();

	public static String toHexString1(byte[] b) {
		StringBuffer buffer = new StringBuffer(b.length);
		String str = new String();

		for (int i = 0; i < b.length; ++i) {
			str = Integer.toHexString(0xFF & b[i]);
			if (str.length() == 1) {
				buffer.append(0 + str);
			} else {
				buffer.append(str.toUpperCase());

			}

		}
		return buffer.toString().toUpperCase();
	}
	public static String getSubString(String text, String left, String right) {
		String result = "";
		int zLen;
		if (left == "" || left.isEmpty()) {
			zLen = 0;
		} else {
			zLen = text.indexOf(left);
			if (zLen > -1) {
				zLen += left.length();
			} else {
				zLen = 0;
			}
		}
		int yLen = text.indexOf(right, zLen);
		if (yLen < 0 || right == "" || right.isEmpty()) {
			yLen = text.length();
		}
		result = text.substring(zLen, yLen);
		return result;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {//

		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:

			byte[] readBuffer = new byte[17];
			Txt file = new Txt();
			try {
				int numBytes = -1;
				StringBuffer buf = new StringBuffer();
				while (inputStream.available() > 0) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					numBytes = inputStream.read(readBuffer);
					if (numBytes > 0) {
						buf.append(toHexString1(readBuffer));
						readBuffer = new byte[20];

					} else {
						msgQueue.add("zhenshishuju----");
					}
				}

				Date date = new Date();
				DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss EE");
				String waterrnumber = getSubString(buf.toString(), "1F01", "2C00");
				if (waterrnumber != "") {

					serialPort.close();
					waterrnumber = getnumber(waterrnumber);
					waterrnumber = df2.format(date) + "----" + waterrnumber;
					this.waternums = waterrnumber;
					Txt.writer_me("E:\\dd.txt", waterrnumber);

				}
				serialPort.close();
			} catch (IOException e) {
			}
			break;
		}
	}

	public int startComPort() {
		portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();

			System.out.println("类型--->" + portId.getPortType());
			System.out.println("端口---->" + portId.getName());
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals("COM5")) {
					try {
						serialPort = (SerialPort) portId.open("COM5", 2000);

					} catch (PortInUseException e) {
						serialPort.close();
						System.out.println("串口已被使用");
						e.printStackTrace();
						return 0;
					}
					try {
						inputStream = serialPort.getInputStream();
						outputStream = serialPort.getOutputStream();
					} catch (IOException e) {
						e.printStackTrace();
						return 0;
					}
					try {
						serialPort.addEventListener(this);
					} catch (TooManyListenersException e) {
						e.printStackTrace();
						return 0;
					}
					// ֪
					serialPort.notifyOnDataAvailable(true);

					try {
						serialPort.setSerialPortParams(2400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_EVEN);
					} catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
						return 0;
					}

					return 1;
				}
			}
		}
		return 0;
	}

	@Override
	public void run() {

		try {
			System.out.println("--------------任务线程开启--------------");
			while (true) {

				if (msgQueue.size() > 0) {

					msgQueue.take();

				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static byte[] itob(int[] intarr) {

		byte[] bt = new byte[intarr.length];
		for (int i = 0; i < intarr.length; i++) {
			bt[i] = (byte) ((intarr[i]) & 0b1111_1111);
		}
		return bt;
	}

	public void setwaternums(String waternums) {
		this.waternums = waternums;
	}

	public String getnumber(String number) {
		StringBuffer n = new StringBuffer();
		char[] a = number.toCharArray();
		n.append(a[6]);
		n.append(a[7]);
		n.append(a[4]);
		n.append(a[5]);
		n.append(a[2]);
		n.append(a[3]);
		n.append(".");
		n.append(a[0]);
		n.append(a[1]);
		return n.toString();
	}
}