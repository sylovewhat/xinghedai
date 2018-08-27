package com.tc.emms.printer;

import java.io.UnsupportedEncodingException;

public class ESCUtil {

	public static final byte ESC = 27;// 换码
	public static final byte FS = 28;// 文本分隔符
	public static final byte GS = 29;// 组分隔符
	public static final byte DLE = 16;// 数据连接换码
	public static final byte EOT = 4;// 传输结束
	public static final byte ENQ = 5;// 询问字符
	public static final byte SP = 32;// 空格
	public static final byte HT = 9;// 横向列表
	public static final byte LF = 10;// 打印并换行（水平定位）
	public static final byte CR = 13;// 归位键
	public static final byte FF = 12;// 走纸控制（打印并回到标准模式（在页模式下） ）
	public static final byte CAN = 24;// 作废（页模式下取消打印数据 ）

	/**
	 * 设置默认行间距
	 */
	public static final byte[] LINE_SPACING_DEFAULT = {0x1B, 0x33};
	// ------------------------打印机初始化-----------------------------

	/**
	 * 打印机初始化
	 * 
	 * @return
	 */
	public static byte[] init_printer() {
		byte[] result = new byte[2];
		result[0] = ESC;
		result[1] = 64;
		return result;
	}

	// ------------------------换行-----------------------------

	/**
	 * 换行
	 * 
	 * @param lineNum要换几行
	 * @return
	 */
	public static byte[] nextLine(int lineNum) {
		byte[] result = new byte[lineNum];
		for (int i = 0; i < lineNum; i++) {
			result[i] = LF;
		}

		return result;
	}

	// ------------------------下划线-----------------------------

	/**
	 * 绘制下划线（1点宽）
	 * 
	 * @return
	 */
	public static byte[] underlineWithOneDotWidthOn() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 45;
		result[2] = 1;
		return result;
	}

	/**
	 * 绘制下划线（2点宽）
	 * 
	 * @return
	 */
	public static byte[] underlineWithTwoDotWidthOn() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 45;
		result[2] = 2;
		return result;
	}

	/**
	 * 取消绘制下划线
	 * 
	 * @return
	 */
	public static byte[] underlineOff() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 45;
		result[2] = 0;
		return result;
	}

	// ------------------------加粗-----------------------------

	/**
	 * 选择加粗模式
	 * 
	 * @return
	 */
	public static byte[] boldOn() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 69;
		result[2] = 0xF;
		return result;
	}

	/**
	 * 取消加粗模式
	 * 
	 * @return
	 */
	public static byte[] boldOff() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 69;
		result[2] = 0;
		return result;
	}

	// ------------------------对齐-----------------------------

	/**
	 * 左对齐
	 * 
	 * @return
	 */
	public static byte[] alignLeft() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 97;
		result[2] = 0;
		return result;
	}

	/**
	 * 居中对齐
	 * 
	 * @return
	 */
	public static byte[] alignCenter() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 97;
		result[2] = 1;
		return result;
	}

	/**
	 * 右对齐
	 * 
	 * @return
	 */
	public static byte[] alignRight() {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 97;
		result[2] = 2;
		return result;
	}

	/**
	 * 水平方向向右移动col列
	 * 
	 * @param col
	 * @return
	 */
	public static byte[] set_HT_position(byte col) {
		byte[] result = new byte[4];
		result[0] = ESC;
		result[1] = 68;
		result[2] = col;
		result[3] = 0;
		return result;
	}
	// ------------------------字体变大-----------------------------

	/**
	 * 字体变大为标准的n倍
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] fontSizeSetBig(int num) {
		byte realSize = 0;
		switch (num) {
			case 1 :
				realSize = 0;
				break;
			case 2 :
				realSize = 17;
				break;
			case 3 :
				realSize = 34;
				break;
			case 4 :
				realSize = 51;
				break;
			case 5 :
				realSize = 68;
				break;
			case 6 :
				realSize = 85;
				break;
			case 7 :
				realSize = 102;
				break;
			case 8 :
				realSize = 119;
				break;
		}
		byte[] result = new byte[3];
		result[0] = 29;
		result[1] = 33;
		result[2] = realSize;
		return result;
	}

	// ------------------------字体变小-----------------------------

	/**
	 * 字体取消倍宽倍高
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] fontSizeSetSmall(int num) {
		byte[] result = new byte[3];
		result[0] = ESC;
		result[1] = 33;

		return result;
	}

	// ------------------------切纸-----------------------------

	/**
	 * 进纸并全部切割
	 * 
	 * @return
	 */
	public static byte[] feedPaperCutAll() {
		byte[] result = new byte[4];
		result[0] = GS;
		result[1] = 86;
		result[2] = 65;
		result[3] = 0;
		return result;
	}

	/**
	 * 进纸并切割（左边留一点不切）
	 * 
	 * @return
	 */
	public static byte[] feedPaperCutPartial() {
		byte[] result = new byte[4];
		result[0] = GS;
		result[1] = 86;
		result[2] = 66;
		result[3] = 0;
		return result;
	}

	// ------------------------切纸-----------------------------
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	public static byte[] byteMerger(byte[][] byteList) {

		int length = 0;
		for (int i = 0; i < byteList.length; i++) {
			length += byteList[i].length;
		}
		byte[] result = new byte[length];

		int index = 0;
		for (int i = 0; i < byteList.length; i++) {
			byte[] nowByte = byteList[i];
			for (int k = 0; k < byteList[i].length; k++) {
				result[index] = nowByte[k];
				index++;
			}
		}
		for (int i = 0; i < index; i++) {
			// CommonUtils.LogWuwei("", "result[" + i + "] is " + result[i]);
		}
		return result;
	}

	// --------------------
	public static byte[] generateMockData(String orderInfo) {
		try {
			byte[] boldOff = ESCUtil.boldOff();
			byte[] left = ESCUtil.alignLeft();
			byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);
			byte[] nextLine = ESCUtil.nextLine(1);
			byte[] marginLine = LINE_SPACING_DEFAULT;
			byte[] breakPartial = ESCUtil.feedPaperCutPartial();
			byte[] temp = null;

			String[] arr = orderInfo.split("\r\n");
			int len = arr.length * 2 + 12;
			byte[][] cmdBytes = new byte[len][];
			cmdBytes[0] = boldOff;
			cmdBytes[1] = left;
			cmdBytes[2] = fontSize1Small;
			cmdBytes[3] = marginLine;
			int i = 0;
			int j = 3;
			//boolean isRMB = false;
			for (i = 0; i < arr.length - 1; i++) {
				if(arr[i].contains("RMB")){
					String [] sub = arr[i].split("RMB");
					String sub1 = sub[0];
					String sub2 = sub[1] + "元";
					temp = sub1.getBytes("gb2312");
					cmdBytes[++j] = temp;
					cmdBytes[++j] = ESCUtil.boldOn();
					cmdBytes[++j] = ESCUtil.alignCenter();
					cmdBytes[++j] = ESCUtil.fontSizeSetBig(2);
					temp = sub2.getBytes("gb2312");
					cmdBytes[++j] = temp;
					cmdBytes[++j] = boldOff;
					cmdBytes[++j] = left;
					cmdBytes[++j] = fontSize1Small;
				}else{
					temp = arr[i].getBytes("gb2312");
					cmdBytes[++j] = temp;
				}
				cmdBytes[++j] = nextLine;
				/*
				temp = arr[i].getBytes("gb2312");
				Log.v("", "ESCUtil temp:" + temp);
				if (arr[i] != null && arr[i].contains("RMB")) {
					Log.v("", "ESCUtil arr[i]:" + arr[i]);
					cmdBytes[++j] = ESCUtil.boldOn();
					cmdBytes[++j] = ESCUtil.alignCenter();
					cmdBytes[++j] = ESCUtil.fontSizeSetBig(4);
					isRMB = true;
				}else{
					if(isRMB){
						isRMB = false;
						
						cmdBytes[++j] = boldOff;
						cmdBytes[++j] = left;
						cmdBytes[++j] = fontSize1Small;
					}
				}
				cmdBytes[++j] = temp;
				cmdBytes[++j] = nextLine;
				*/
			}

			cmdBytes[++j] = nextLine;
			cmdBytes[++j] = nextLine;// 走2行纸
			cmdBytes[++j] = breakPartial;

			byte[] data = ESCUtil.byteMerger(cmdBytes);
			return data;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}