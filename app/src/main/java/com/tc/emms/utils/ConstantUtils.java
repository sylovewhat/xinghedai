package com.tc.emms.utils;

import java.util.List;

import android.graphics.Bitmap;
import android.view.View;

import com.tc.emms.model.TitleBar;
import com.tc.emms.widget.X5WebView;

/**
 * 公用的常用参数
 * 
 * @author xiaoben
 */
public class ConstantUtils {
	
	/*模式  false:项目版本，true:基座调试*/
	public static final boolean IS_OPEN_DEBUG = true;
	/*****
	 * 软件类型
	 *****/
	public static final int APP_VERSION_TYPE = 12;
	public static final String APP_VERSION_TYPE_KEY = "APP_VERSION_TYPE";
	/* 当前版本唯一标识  */
	public static final String IS_NOW_TYPEONLY = "PAYEE_ADMIN";
	public static final String IS_FRIST_OPEN_APP = "IS_FRIST_OPEN_APP";
	public static final String APP_TYPE_KEY = "APP_TYPE";
	public static final String APP_TYPE = "AND";
	/* 推送设备版本 Android:1; IOS:2  */
	public static final String PUSH_DEVICE_TYPE = "1";
	/* 是否需要注册百度文字识别  */
	public static final boolean REGIST_OCR = false;
	/* 是否支持刷卡  */
	public static final boolean IS_POS_CARD = false;
	/* 默认当前APP包名 */
	public static final String APP_PACKAGE = "com.tc.emms";
	/*****
	 * source_id
	 *****/
	public static final String SOURCE_ID = "12";
	/* 映射类包名路径 */
	public static final String HTML_CLASS_BEGIN = AppUtils.getPackage() + ".html.";
	/* 1 短信验证码 */
	public static final int ACT_GET_MSM = 00001;
	/**************************************更新 **************************************/
	/* 是否安装新版本 */
	public final static String IS_NEW_VERSION = "IS_NEW_VERSION";
	/* App是否更新 */
	public final static String IS_UPDATE = "IS_UPDATE";
	public final static String VERSION_NO = "VERSION_NO";
	public final static String UPDATE_DES = "UPDATE_DES";
	public final static String UPDATE_FLAG = "UPDATE_FLAG";
	/* APP默认下载更新名称   */
	public static String U_SOFT_NAME = "PayeeAdmin.apk";
	/* 资源文件默认下载更新名称   */
	public static String U_RES_ZIP = "PayeeHtml.zip";
	/* 资源文件默认下载更新名称   */
	public static String U_RES_NAME = "img.zip";
	public static final int U_DOWN_PROCESS = 0X6666;
	public static final int U_DOWN_COM = 0X7777;
	public static final int U_DOWN_ERR = 0X5555;
	/**************************************HTML **************************************/
	/* 数据库名称*/
	public static final String DB_NAME = "personinfo.sqlite";
	/* 用户信息表名*/
	public static final String TABLE_NAME_CONFIG = "app_config";
	/* 未完成订单流水表名*/
	public static final String TABLE_NAME_FLOWS = "app_flows";
	/* 通知表名*/
	public static final String TABLE_NAME_NEWS = "app_news";
	/* 通道表名*/
	public static final String TABLE_NAME_TONGDAO = "app_tongdao";
	
	/* 初始html */
	public final static String FIRST_HTML = "default.html";
	/* 登陆html */
	public final static String LOGIN_HTML = "login.html";
	
	/* HTML更新表名 */
	public static String HTML_UPDATE_TABLE_NAME = "app_html5";

	/* HTML更新名称字段名 */
	public static String HTML_UPDATE_NAME = "filename";
	/* HTML版本 */
	public static String HTML_UPDATE_VERSIONNO = "versionno";
	/* HTML更新字段名 */
	public static String HTML_UPDATE_KEY = "isUpdate";
	/* HTML更新时间 */
	public static String HTML_UPDATE_TIME = "updatetime";
	public static String HTML_UPDATE_VERNO = "totalVerNo";
	public static String HTML_UPDATE_DES = "updatedes";
	public static String HTML_UPDATE_NOTE = "type";
	/* NEW.000000 更新HTML 完成 */
	public static final int ACT_HTML_COM = 0X1100;
	/* NEW.99999 更新HTML 下载失败 */
	public static final int ACT_HTML_FAILE = 0X1101;
	public static final int ACT_FOOT_VIEW = 0X1102;
	public static final int ACT_FOOT_GONE = 0X1103;
	public static final int CAPTURE_CODE = 0X2200;
	public static final int IMAGE_CODE = 0X2201;
	
	/* HTML 原生交互Log标识 */
	public static String HTML_LOG = "from html post";
	/**************************************文件存储 **************************************/
	/* 文件存储目录 */
	/*public static String STORE_PATH = ConstantHelper.get_STORE_PATH();*/
	public static String STORE_PATH = "data/data/" + AppUtils.getPackage() + "/";
	public static String FILE_PATH = "html/";
	public static String HTML_PATH = "PayeeHtml/";
	/* SD文件存储目录 */
	public static String SD_FILE_PATH = "payee/";
	public static String UPLOAD_PATH = SD_FILE_PATH + "upload/";
	public static String SCREEN_SHOT_PATH = SD_FILE_PATH + "screen/";
	public static String SHARE_PATH = SD_FILE_PATH + "share/";
	public static String NORMAL = "nomarl.png";
	/* 需要创建的文件夹列表*/
	public static String[] SD_FILES = {"css","img","js","fonts","libs"};
	/* 是否登录【记住密码】 */
	public final static String IS_SAVE_LOGIN = "IS_SAVE_LOGIN";
	/* 是否服务商【默认勾选】 */
	public final static String IS_FWS = "IS_FWS";
	public final static String IS_USER_LOGIN = "IS_USER_LOGIN";
	/* SharedPreferences【用户名】 */
	public final static String USERACCOUNT = "USERACCOUNT";
	/* SharedPreferences【密码】 */
	public final static String USERPASSWORD = "USERPASSWORD";
	/* handler 回调参数 */
	public static final int HANDLER_SECCESS = 0X1104;
	public static final int HANDLER_FAILE = 0X1105;
	/**************************************webview **************************************/
	/* webview key */
	public static final String HTML_KEY = "APPJS";
	/* 跳转传递H5文件名 */
	public static final String HTML_NAME = "HTML_NAME";
	public static final String HTML_URL = "HTML_URL";
	public static String IS_CHECK = "IS_CHECK";
	public static final String SET_HTML = "init_setting.html";
	/* HTML文件名 */
	public static String U_BASE = "file:/";
	public static String U_IMG = "img/";
	/**************************************服务器 **************************************/
	/* MD5 KEY */
	public static final String MD5KEY = "3ff20e909dc7663ec7a7063f967e887d";
	/* 服务器地址 */
	public static String U_MAIN = "http://8.498.net";
	public static String UMAIN = U_MAIN + "/appdo.php?";
	public static String UKEY = "json";
	/* 正式环境 */
	public static String SERVER_ADDRESSTES = "http://pay.498.net/api3.0/api.php";
	public static String URSAKEY = "s";
	/* 短信验证码 */
	public static String USMS = "http://pay.498.net/SMS/api.php";
	/* 一码付 */
	public static String U_PAYYMF = "http://pay.498.net/qr/ymf.php?";
	/*本地测试更新地址*/
	public static String UPDATE_LOCAL_MAIN = "UPDATE_LOCAL_MAIN";
	/* 检查更新 */
	public static String UPDATE_MAIN = "http://8.498.net";
	public static String U_MAIN_UPDATE = UPDATE_MAIN + "/appdo.php?";
	public static String U_DOWN = UPDATE_MAIN + "/soft_update/app_h5/";
	public static String SERVICE_APP_VERSION_PATH = "Pay_MAIN_SH_APP_2017";
	public static String SERVICE_APP_VERSION_PATH_KEY = "SERVICE_APP_VERSION_PATH";
	
	public static String TOTAL_VERSION_APP = "TOTAL_VERSION_APP";
	public static String TOTAL_VERSION_H5 = "TOTAL_VERSION_H5";
	public static String TOTAL_VERSION_IMG = "TOTAL_VERSION_IMG";
	public static String DOWNLOAD_F_PATH = "DOWNLOAD_F_PATH";
	/**************************************其他 **************************************/
	/*APP是否全屏显示*/
	public static String APP_fULL_SCREEN = "APP_fULL_SCREEN";

	/*标识首页全屏，其他页不全屏 暂时没有用到*/
	public static String APP_fULL_SCREEN_STRAT = "APP_fULL_SCREEN_STRAT";

	/*APP主题颜色标识*/
	public static final String APP_BACKGROUD_COLOR = "APP_BACKGROUD_COLOR";

	/*标题栏是否显示*/
	public static final String IS_TITLE_BAR = "IS_TITLE_BAR";


	/*APP是否弹出log调试信息*/
	public static final String LOG_SWICH = "LOG_SWICH";
	/*头部页面按钮*/
	public static List<TitleBar> heardListText;//头部布局
	/*底部页面按钮*/
	public static List<View> footListText;//尾部布局
	public static List<X5WebView> WebViews;
	public final static String CHOOSE_IMG_TYPE = "CHOOSE_IMG_TYPE";
	public final static String MAIN_SHOW_INDEX = "MAIN_SHOW_INDEX";
	/*讯飞语音合成*/
	public static final String XUNFEI_ID = "appid=58ae926e";
	/* 语音播报是否开启 */
	public static String MSC_SPEAK = "MSC_SPEAK";
	/* 阿里云推送设备ID */
	public static String ALI_DEVICE_ID = "ALI_DEVICE_ID";
	/* 服务器推送设备注册 */
	public static String REGIST_SERVER_PUSHID = "REGIST_SERVER_PUSHID";
	/* 百度文字识别 */
	public static boolean hasGotToken = false;
	public static String API_KEY = "PFcGZgDl9hSLtU7UXW4zXbRZ";
	public static String SECRET_KEY = "snYD1CMEETyDIs3RfM4H3he0pDlrAfRv";
	/* 微信分享ID */
    public static final String WEIXIN_ID = "wxec0e47790bf0e7ef";
    public static final int THUMB_SIZE = 150;
	/* 图片类返回使用 */
	public static X5WebView ChooseImgWebView;
	public static String returnMethodName;
	public static int compression;
	public static String savePath;
	public static Bitmap saveBitmap;
	public final static String ID_CARD_TYPE = "ID_CARD_TYPE";
	/* 扫码页面传递类型 */
	public static String CAPTURE_SCAN_TYPE = "CAPTURE_SCAN_TYPE";
	/**** ======================================  商户版    ====================================== */
	/* RSA_KEY 商户密钥 */
	public final static String RSA_KEY = "RSA_KEY";
	/* 商户号 */
	public final static String BUSINESS_NO = "BUSINESS_NO";
	/* 商户ID */
	public final static String MEMBER_ID = "MEMBER_ID";
	/* 商户名称 */
	public final static String MEMBER_NAME = "MEMBER_NAME";
	/* 门店ID */
	public final static String S_ID = "S_ID";
	/* 操作员ID */
	public final static String I_ID = "I_ID";
	/* 设备号 */
	public final static String TN = "TN";
	/* MD5密钥 */
	public final static String MD5 = "MD5";
	
	/* 支付页面金额传递 */
	public static String PAY_MONEY = "PAY_MONEY";
	public static boolean IS_WEIXIN_ID;
	public static int CODE_WIDTH = 300;
	public static int CODE_HEIGHT = 300;
	/*保存扫码页面默认值*/
	public static String SCAN_INDEX = "SCAN_INDEX";
	public static String PAY_INDEX = "PAY_INDEX";
	
	/* 通知无未查看 完成 */
	public static final int ACT_NOTICE_DONE = 99998;
	/* 通知未查看 替换图标 */
	public static final int ACT_NOTICE_NORMAIL = 99997;
	/*页面刷新控制*/
	public static String IS_MAIN_REFRESH = "IS_MAIN_REFRESH";
	public static String IS_SJ_REFRESH = "IS_SJ_REFRESH";
	public static String IS_TZ_REFRESH = "IS_TZ_REFRESH";
	public static String IS_ME_REFRESH = "IS_ME_REFRESH";
	/* 消息推送到达执行X5WebView */
	public static X5WebView PushWebView;
	/* 自动登录记录的X5WebView */
	public static X5WebView LoginWebView;
	public static String CookieStr = "";
	
	/*下载地址路径*/
	public static String SETH5UPDATEURL = "SETH5UPDATEURL";
	public static String NORMAIL_DOWNURL = "http://112.74.125.175:80/upload/app/";
	
	//返回键是否启用
	public static final String IS_OPEN_BACK = "IS_OPEN_BACK";
}
