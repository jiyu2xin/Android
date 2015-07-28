package com.xiaoqf.common;

import com.xiaoqf.beans.House;
import com.xiaoqf.beans.Project;
import com.xiaoqf.beans.Room;
import com.xiaoqf.beans.Order;
import com.xiaoqf.beans.OrderInfo;

/**
 * @ClassName: Consts
 * @Description: 常量类
 * @version: 1.0
 * @Create: 2015-5-5 10:48
 * @tag
 */
public class Consts {
	// 开发调试模式设置
	// 必须和AndroidManifest.xml中android:debuggable值相同
	public static final boolean isDebugMode = false; // 调试模式
	public static final int DEBUGMODE_WB = 1;
	public static final int DEBUGMODE_TD = 2;
	public static final int DEBUGMODE = DEBUGMODE_WB+2;

	public static int TIME_OUT = 20 * 1000;// 网络超时时间
	public static final String DBG_INFO = "dbgInfo";
	
	public static final String SERVER_URL = isDebugMode ?
//		"http://120.24.227.76:8080" : "http://www.xiaoqf.com";
//	"http://192.168.199.248:8080/xiaoqf/" : "http://app.xiaofq.com";
	"http://120.24.227.76:8080":"http://120.24.227.76:8080";

	public static final String SERVER_URL_CHECK_VERSION = 
		SERVER_URL + "/sys/getAppVersion.json";// 版本检查
	public static final String DOWNLOAD_PATH = "/sdcard/xiaoqfang/download/";
	public static final String SERVER_URL_USER_GET_SECURITYCODE = SERVER_URL +"/user/getVerCode.json";
	public static final String  SERVER_URL_USER_REGISTER = SERVER_URL +"/user/regedit.json";
	public static final String SERVER_URL_USER_LOGIN  = SERVER_URL +"/user/doAppLogin.json";
	public static final String SERVER_URL_USER_LOGOUT = SERVER_URL +"/user/appLoginOut.json";
	public static final String SERVER_URL_PROPERTY_INFO = SERVER_URL +"/project/list.json";
	public static final String SERVER_URL_MODIFY_PASSWORD = SERVER_URL +"/user/updatePwd.json"; 
	public static final String SERVER_URL_FIND_PASSWORD = SERVER_URL +"/user/resetPwd.json";
	public static final String SERVER_URL_ROOM_ORDER_INFO = SERVER_URL +"/order/list.json";
	public static final String SERVER_URL_TRANSACTION_RECORD = SERVER_URL +"/user/detailList.json";
	public static final String SERVER_URL_HOUSELIST = SERVER_URL +"/room/houseList.json";
	public static final String SERVER_URL_ROOMLIST = SERVER_URL +"/room/list.json";
	/** 获取底价 */
	public static final String SERVER_URL_PRICELOWEST = SERVER_URL +"/order/add.json";
	/** 充值 */
	public static final String SERVER_URL_RECHARGE_BY_ALIPAY = SERVER_URL +"/pay/beforePayGet.json";
	public static final String SERVER_URL_RECHARGE_BY_WXPAY = SERVER_URL +"/pay/BeforeWXPay.json";
	public static final String SERVER_URL_ORDER_DETIAL = SERVER_URL +"/order/getOrderDetail.json";
	/** 开通服务城市*/
	public static final String SERVER_URL_LOCATE_OPENEDCITY =SERVER_URL+"/citiy/getOpenCitiyList.json";
	
	public static final String SERVER_URL_FEED_BACK =SERVER_URL+"/main/advise.json";
	
	/** 屏幕宽度 */
	public static int devWidth = 720;
	/** 屏幕高度 */
	public static int devHeight = 1280;
	
	/** 微信分享 */
	public static final String SERVER_URL_WEIXIN_SHARE = SERVER_URL +"/weixin/help.json";
	// APP_ID 替换为你的应用从官方网站申请到的合法appId
    public static final String APP_ID = "wx0e4f2c1890a9a60e";//wxe6fdedb5453c1f19
    /** 订金支付订单，用于微信支付成功后传递数据至回调中 */
    public static Order book_order;
    public static OrderInfo book_orderInfo;
	public static Project project_wx = null;
	public static House house_wx = null;
	public static Room room_wx = null;
    
	// 测试用数据
	public static int showHightSize =0;
	public static String tokenID = null;//test
	public static String userMobile = null;
	public static String orderId = "44";
	public static final String PRICE_CENT = "0.01";//"0.01"
	public static final String CLIENTPHONE = "4008867733";
	
	public static String responseInfo_result_roomsList =
		"{'status':'1','list':[{'roomLayer':1,'roomList':[{'roomId':6,'roomNo':101,'roomStatus':0,'roomAmount':400000.00,'roomLayer':1,'roomUnit':'1'},{'roomId':7,'roomNo':102,'roomStatus':1,'roomAmount':300000.00,'roomLayer':1,'roomUnit':'1'},{'roomId':8,'roomNo':103,'roomStatus':1,'roomAmount':200000.00,'roomLayer':1,'roomUnit':'1'},{'roomId':9,'roomNo':104,'roomStatus':0,'roomAmount':500000.00,'roomLayer':1,'roomUnit':'1'},{'roomId':10,'roomNo':105,'roomStatus':1,'roomAmount':500000.00,'roomLayer':1,'roomUnit':'1'},{'roomId':11,'roomNo':106,'roomStatus':1,'roomAmount':500000.00,'roomLayer':1,'roomUnit':'1'},{'roomId':12,'roomNo':107,'roomStatus':0,'roomAmount':500000.00,'roomLayer':1,'roomUnit':'1'},{'roomId':13,'roomNo':108,'roomStatus':0,'roomAmount':500000.00,'roomLayer':1,'roomUnit':'1'}]},{'roomLayer':2,'roomList':[{'roomId':14,'roomNo':201,'roomStatus':0,'roomAmount':500000.00,'roomLayer':2,'roomUnit':'1'},{'roomId':15,'roomNo':202,'roomStatus':0,'roomAmount':500000.00,'roomLayer':2,'roomUnit':'1'},{'roomId':16,'roomNo':203,'roomStatus':0,'roomAmount':500000.00,'roomLayer':2,'roomUnit':'1'},{'roomId':17,'roomNo':204,'roomStatus':0,'roomAmount':500000.00,'roomLayer':2,'roomUnit':'1'},{'roomId':18,'roomNo':205,'roomStatus':0,'roomAmount':500000.00,'roomLayer':2,'roomUnit':'1'},{'roomId':19,'roomNo':206,'roomStatus':0,'roomAmount':500000.00,'roomLayer':2,'roomUnit':'1'},{'roomId':20,'roomNo':207,'roomStatus':1,'roomAmount':500000.00,'roomLayer':2,'roomUnit':'1'},{'roomId':21,'roomNo':208,'roomStatus':1,'roomAmount':500000.00,'roomLayer':2,'roomUnit':'1'}]},{'roomLayer':3,'roomList':[{'roomId':29,'roomNo':301,'roomStatus':0,'roomAmount':500000.00,'roomLayer':3,'roomUnit':'1'},{'roomId':22,'roomNo':302,'roomStatus':1,'roomAmount':500000.00,'roomLayer':3,'roomUnit':'1'},{'roomId':23,'roomNo':303,'roomStatus':0,'roomAmount':500000.00,'roomLayer':3,'roomUnit':'1'},{'roomId':24,'roomNo':304,'roomStatus':0,'roomAmount':500000.00,'roomLayer':3,'roomUnit':'1'},{'roomId':25,'roomNo':305,'roomStatus':0,'roomAmount':500000.00,'roomLayer':3,'roomUnit':'1'},{'roomId':26,'roomNo':306,'roomStatus':0,'roomAmount':500000.00,'roomLayer':3,'roomUnit':'1'},{'roomId':27,'roomNo':307,'roomStatus':0,'roomAmount':500000.00,'roomLayer':3,'roomUnit':'1'},{'roomId':28,'roomNo':308,'roomStatus':0,'roomAmount':500000.00,'roomLayer':3,'roomUnit':'1'}]},{'roomLayer':4,'roomList':[{'roomId':88,'roomNo':401,'roomStatus':-1,'roomAmount':5.00,'roomLayer':4,'roomUnit':'1'},{'roomId':30,'roomNo':402,'roomStatus':0,'roomAmount':500000.00,'roomLayer':4,'roomUnit':'1'},{'roomId':31,'roomNo':403,'roomStatus':0,'roomAmount':500000.00,'roomLayer':4,'roomUnit':'1'},{'roomId':32,'roomNo':404,'roomStatus':0,'roomAmount':500000.00,'roomLayer':4,'roomUnit':'1'},{'roomId':33,'roomNo':405,'roomStatus':0,'roomAmount':500000.00,'roomLayer':4,'roomUnit':'1'},{'roomId':34,'roomNo':406,'roomStatus':0,'roomAmount':500000.00,'roomLayer':4,'roomUnit':'1'},{'roomId':89,'roomNo':407,'roomStatus':-1,'roomAmount':500000.00,'roomLayer':4,'roomUnit':'1'},{'roomId':90,'roomNo':408,'roomStatus':-1,'roomAmount':500000.00,'roomLayer':4,'roomUnit':'1'}]},{'roomLayer':5,'roomList':[{'roomId':45,'roomNo':501,'roomStatus':0,'roomAmount':500000.00,'roomLayer':5,'roomUnit':'1'},{'roomId':38,'roomNo':502,'roomStatus':1,'roomAmount':500000.00,'roomLayer':5,'roomUnit':'1'},{'roomId':39,'roomNo':503,'roomStatus':0,'roomAmount':500000.00,'roomLayer':5,'roomUnit':'1'},{'roomId':40,'roomNo':504,'roomStatus':1,'roomAmount':500000.00,'roomLayer':5,'roomUnit':'1'},{'roomId':41,'roomNo':505,'roomStatus':0,'roomAmount':500000.00,'roomLayer':5,'roomUnit':'1'},{'roomId':42,'roomNo':506,'roomStatus':0,'roomAmount':500000.00,'roomLayer':5,'roomUnit':'1'},{'roomId':43,'roomNo':507,'roomStatus':0,'roomAmount':500000.00,'roomLayer':5,'roomUnit':'1'},{'roomId':44,'roomNo':508,'roomStatus':0,'roomAmount':500000.00,'roomLayer':5,'roomUnit':'1'}]},{'roomLayer':6,'roomList':[{'roomId':53,'roomNo':601,'roomStatus':0,'roomAmount':500000.00,'roomLayer':6,'roomUnit':'1'},{'roomId':46,'roomNo':602,'roomStatus':0,'roomAmount':500000.00,'roomLayer':6,'roomUnit':'1'},{'roomId':47,'roomNo':603,'roomStatus':0,'roomAmount':500000.00,'roomLayer':6,'roomUnit':'1'},{'roomId':48,'roomNo':604,'roomStatus':0,'roomAmount':500000.00,'roomLayer':6,'roomUnit':'1'},{'roomId':49,'roomNo':605,'roomStatus':0,'roomAmount':500000.00,'roomLayer':6,'roomUnit':'1'},{'roomId':50,'roomNo':606,'roomStatus':0,'roomAmount':500000.00,'roomLayer':6,'roomUnit':'1'},{'roomId':51,'roomNo':607,'roomStatus':0,'roomAmount':500000.00,'roomLayer':6,'roomUnit':'1'},{'roomId':52,'roomNo':608,'roomStatus':0,'roomAmount':500000.00,'roomLayer':6,'roomUnit':'1'}]},{'roomLayer':7,'roomList':[{'roomId':61,'roomNo':701,'roomStatus':0,'roomAmount':500000.00,'roomLayer':7,'roomUnit':'1'},{'roomId':54,'roomNo':702,'roomStatus':0,'roomAmount':500000.00,'roomLayer':7,'roomUnit':'1'},{'roomId':55,'roomNo':703,'roomStatus':0,'roomAmount':500000.00,'roomLayer':7,'roomUnit':'1'},{'roomId':56,'roomNo':704,'roomStatus':0,'roomAmount':500000.00,'roomLayer':7,'roomUnit':'1'},{'roomId':57,'roomNo':705,'roomStatus':0,'roomAmount':500000.00,'roomLayer':7,'roomUnit':'1'},{'roomId':58,'roomNo':706,'roomStatus':0,'roomAmount':500000.00,'roomLayer':7,'roomUnit':'1'},{'roomId':59,'roomNo':707,'roomStatus':0,'roomAmount':500000.00,'roomLayer':7,'roomUnit':'1'},{'roomId':70,'roomNo':708,'roomStatus':0,'roomAmount':500000.00,'roomLayer':7,'roomUnit':'1'}]},{'roomLayer':8,'roomList':[{'roomId':69,'roomNo':801,'roomStatus':0,'roomAmount':500000.00,'roomLayer':8,'roomUnit':'1'},{'roomId':62,'roomNo':802,'roomStatus':0,'roomAmount':500000.00,'roomLayer':8,'roomUnit':'1'},{'roomId':63,'roomNo':803,'roomStatus':0,'roomAmount':500000.00,'roomLayer':8,'roomUnit':'1'},{'roomId':64,'roomNo':804,'roomStatus':0,'roomAmount':500000.00,'roomLayer':8,'roomUnit':'1'},{'roomId':65,'roomNo':805,'roomStatus':0,'roomAmount':500000.00,'roomLayer':8,'roomUnit':'1'},{'roomId':66,'roomNo':806,'roomStatus':0,'roomAmount':500000.00,'roomLayer':8,'roomUnit':'1'},{'roomId':67,'roomNo':807,'roomStatus':0,'roomAmount':500000.00,'roomLayer':8,'roomUnit':'1'},{'roomId':68,'roomNo':808,'roomStatus':0,'roomAmount':500000.00,'roomLayer':8,'roomUnit':'1'}]},{'roomLayer':9,'roomList':[{'roomId':78,'roomNo':901,'roomStatus':0,'roomAmount':500000.00,'roomLayer':9,'roomUnit':'1'},{'roomId':71,'roomNo':902,'roomStatus':0,'roomAmount':500000.00,'roomLayer':9,'roomUnit':'1'},{'roomId':72,'roomNo':903,'roomStatus':0,'roomAmount':500000.00,'roomLayer':9,'roomUnit':'1'},{'roomId':73,'roomNo':904,'roomStatus':0,'roomAmount':500000.00,'roomLayer':9,'roomUnit':'1'},{'roomId':74,'roomNo':905,'roomStatus':0,'roomAmount':500000.00,'roomLayer':9,'roomUnit':'1'},{'roomId':75,'roomNo':906,'roomStatus':0,'roomAmount':500000.00,'roomLayer':9,'roomUnit':'1'},{'roomId':76,'roomNo':907,'roomStatus':0,'roomAmount':500000.00,'roomLayer':9,'roomUnit':'1'},{'roomId':77,'roomNo':908,'roomStatus':0,'roomAmount':500000.00,'roomLayer':9,'roomUnit':'1'}]},{'roomLayer':10,'roomList':[{'roomId':87,'roomNo':1001,'roomStatus':0,'roomAmount':500000.00,'roomLayer':10,'roomUnit':'1'},{'roomId':79,'roomNo':1002,'roomStatus':0,'roomAmount':500000.00,'roomLayer':10,'roomUnit':'1'},{'roomId':81,'roomNo':1003,'roomStatus':0,'roomAmount':500000.00,'roomLayer':10,'roomUnit':'1'},{'roomId':82,'roomNo':1004,'roomStatus':0,'roomAmount':500000.00,'roomLayer':10,'roomUnit':'1'},{'roomId':83,'roomNo':1005,'roomStatus':0,'roomAmount':500000.00,'roomLayer':10,'roomUnit':'1'},{'roomId':84,'roomNo':1006,'roomStatus':0,'roomAmount':500000.00,'roomLayer':10,'roomUnit':'1'},{'roomId':85,'roomNo':1007,'roomStatus':0,'roomAmount':500000.00,'roomLayer':10,'roomUnit':'1'},{'roomId':86,'roomNo':1008,'roomStatus':0,'roomAmount':500000.00,'roomLayer':10,'roomUnit':'1'}]}]}";
	
	public static String responseInfo_result_housesList =
		"{'status':'1','projectId':8,'projectName':'恒大雅苑','houseList':[{'houseId':11,'projectId':8,'estateId':23,'houseNo':1,'houseName':'1栋','crateTime':'2015-05-22 10:47:59.0','estateName':'恒大雅苑','projectName':'恒大雅苑'},{'houseId':12,'projectId':8,'estateId':23,'houseNo':2,'houseName':'2栋','crateTime':'2015-05-22 10:48:33.0','estateName':'恒大雅苑','projectName':'恒大雅苑'},{'houseId':13,'projectId':8,'estateId':23,'houseNo':3,'houseName':'3栋','crateTime':'2015-05-26 00:51:27.0','estateName':'恒大雅苑','projectName':'恒大雅苑'},{'houseId':14,'projectId':8,'estateId':23,'houseNo':4,'houseName':'4栋','crateTime':'2015-05-26 00:51:48.0','estateName':'恒大雅苑','projectName':'恒大雅苑'}]}";
	
	//定位相关
	public static String City_Name = "长沙市";
	public static int City_SelectedPosition = 0;
}

