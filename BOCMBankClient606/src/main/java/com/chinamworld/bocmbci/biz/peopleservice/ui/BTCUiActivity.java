package com.chinamworld.bocmbci.biz.peopleservice.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Acc;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.peopleservice.controlcenter.BTCControlCenter;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCMWApplication;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCCommbean;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCElement;
import com.chinamworld.bocmbci.biz.peopleservice.entity.BTCPageData;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IFunction;
import com.chinamworld.bocmbci.biz.peopleservice.utils.IHttpCallBack;
import com.chinamworld.bocmbci.biz.peopleservice.xmlinterface.BTCKxmlParser;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 类功能描述：用于生成用户界面的控件，并展示在页面上。
 * 
 * @author：秦
 * @version： 1.0
 * @see ：com.chinamworld.btwapview.ui 父类：android.app.Activity
 *      相关数据：lLayout：代表整个用户界面 lLinearLayoutTop：代表用户界面的最上面一个区块，通常是当做标题栏用
 *      lLinearLayoutBotton：代表用户界面的最下面一个区块，通常是用于布局工具栏，比如toolbar
 */
@SuppressLint("InflateParams")
public class BTCUiActivity extends PlpsBaseActivity {
	/** 类标识id */
	public static final String id = "ui";
	private static final String TAG = "BTCUiActivity";
	/** 整个页面的内容区 */
	private LinearLayout layout, uitittle;
	/** 页面的标题栏 */
	private RelativeLayout top;
	/** 全局变量对象 */
	private BTCCMWApplication cmwApplication;
	/** 自身对象 */
	private static BTCUiActivity self;

	private LinearLayout lyt_body;
	static String flowFileId="";
	String conversationId = "";
	private Button ib_top_right_login_btn,ib_top_right_btn;
//	private String flowName = "";
	private String isDisp = "";
	
	/** 获取全局变量对象 */
	public static BTCCMWApplication getApp() {
		if(self.cmwApplication == null)
			self.cmwApplication = new BTCCMWApplication();
		return self.cmwApplication;
	}
	
	public static BTCUiActivity getInstance(){
		if(self == null)
			self = new BTCUiActivity();
		return self;
	}
	
	/** 进入民生服务标签解析 */
	public static void IntentToParseXML(Context context, String prvcShortName,String flowFileId,String catName,String menuName,String flowFileName,String isDisp, String stepName, Map<String, Object> dispMap,String conversationId){
		PlpsDataCenter.getInstance().setCatName(catName);
		PlpsDataCenter.getInstance().setMenuName(menuName);
		PlpsDataCenter.getInstance().setFlowFileId(flowFileId);
		PlpsDataCenter.getInstance().setFlowFileName(flowFileName);
		PlpsDataCenter.getInstance().setPrvcShortName(prvcShortName);
		PlpsDataCenter.getInstance().setIsDisp(isDisp);
		PlpsDataCenter.getInstance().setStepName(stepName);
		PlpsDataCenter.getInstance().setDispMap(dispMap);
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.CONVERSATION_ID,conversationId);
		Intent intent = new Intent(context,BTCUiActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public String menuName="";
	public String itemName = "";
	public String flowFileName = "" ;
	public String title="";
	// List<BTCElement> widgetList=new ArrayList<BTCElement>(); // 存放标签信息
	 
	/**
	 * 方法功能说明 ：重写父类（Activity）的该方法。 在这个方法中需要进行一些初始化操作，根据界面控件列表生成控件，并在界面上展现出来。
	 * 
	 * @param savedInstanceState
	 * @return void
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.uimain);
//		inflateLayout(R.layout.uimain);
		addView(R.layout.uimain);
		if(!"".equals(PlpsDataCenter.getInstance().getCatName())&&PlpsDataCenter.getInstance().getCatName()!=null){
			title = PlpsDataCenter.getInstance().getCatName();
		}
		setTitle(title);
		itemName = PlpsDataCenter.getInstance().getCatName();
		menuName = PlpsDataCenter.getInstance().getMenuName();
		flowFileName = PlpsDataCenter.getInstance().getFlowFileName();
		BTCCMWApplication.dispMap = PlpsDataCenter.getInstance().getDispMap();
		BTCCMWApplication.isDisp = PlpsDataCenter.getInstance().getIsDisp();
		conversationId =  (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
		BTCCMWApplication.conversationId = conversationId;
		flowFileId =PlpsDataCenter.getInstance().getFlowFileId();
		BTCCMWApplication.flowFileId= flowFileId;
		self = this;
		BTCCMWApplication.selfs = this;
		cmwApplication = new BTCCMWApplication();
		
		// setTheme(R.style.CCBTheme);
		// 获取管理ActivityGroup的manager，用来控制所有activity的生命周期和跳转
//				manager = getLocalActivityManager();

		uitittle = (LinearLayout) findViewById(R.id.uitittle);
		layout = (LinearLayout) findViewById(R.id.uimain);
		ib_top_right_login_btn = (Button) findViewById(R.id.ib_top_right_login_btn);
		ib_top_right_btn = (Button) findViewById(R.id.ib_top_right_btn);
		lyt_body = (LinearLayout)this.findViewById(R.id.lyt_body);
//		query();
		
		if ((LinearLayout) findViewById(R.id.lyt_body) != null) {
			((LinearLayout) findViewById(R.id.lyt_body)).setPadding(0, 0,
					0, 0);
		}
		
		
		if ((LinearLayout) findViewById(R.id.uimain) != null) {
			((LinearLayout) findViewById(R.id.uimain)).setPadding(0, 0,
					0, 0);
		}
//		findViewById(R.id.ib_back).setOnClickListener(this);
//		querySecond();
		
//		queryPeopleService();
		requestCommConversationId();
	}

//	int i = 0;
	@Override
	protected void onResume() {
		super.onResume();
//		hineLeftSideMenu();
		ib_top_right_login_btn.setVisibility(View.GONE);
		ib_top_right_btn.setVisibility(View.GONE);
	}
	
	/**
	 * 获取页面的标题栏
	 * 
	 * @return
	 */
	public static RelativeLayout getTop() {
		return self.top;
	}

	/** 获得当前页面的根容器  */
	public View getActivityView(BTCElement rootElement)
	{
		uitittle.setVisibility(View.GONE);
		lyt_body.setVisibility(View.VISIBLE);
		// 清空整个页面
		layout.removeAllViews();
		LinearLayout layoutpage = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.page, null);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		layoutpage.setLayoutParams(layoutParams);
		
		layout.addView(layoutpage);
		LinearLayout page = (LinearLayout) layoutpage.findViewById(R.id.page);
		
		if ( page != null) {
			page.setPadding(0, 0,0, 0);
		}
		return page;
//		// 遍历标签列表，画出相应的控件，并布局到页面上
//		try {
//			BTCDrawLable btcDrawLable = rootElement.getBTCDrawLable();
//			if (btcDrawLable != null)
//				btcDrawLable.drawLable(null, page);			
//		} catch (Exception e) {// 解析页面出错时给出提示框
//			LogGloble.e(TAG,e.toString());
//		}
	}
	
	/**
	 * 方法功能说明 ：每次切换页面时调用（包括生成第一个页面时，也就是创建BTCUiActivity时）。重新生成页面。
	 */
	public void navigationToActivity(BTCElement rootElement) {
//		uitittle.setVisibility(View.GONE);
//		lyt_body.setVisibility(View.VISIBLE);
//		// 清空整个页面
//		layout.removeAllViews();
//		LinearLayout layoutpage = (LinearLayout) LayoutInflater.from(this)
//				.inflate(R.layout.page, null);
//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//				ViewGroup.LayoutParams.FILL_PARENT,
//				ViewGroup.LayoutParams.FILL_PARENT);
//		layoutpage.setLayoutParams(layoutParams);
//		
//		layout.addView(layoutpage);
//		LinearLayout page = (LinearLayout) layoutpage.findViewById(R.id.page);
//		
//		if ( page != null) {
//			page.setPadding(0, 0,0, 0);
//		}
		// 遍历标签列表，画出相应的控件，并布局到页面上
		try {
			BTCDrawLable btcDrawLable = rootElement.getBTCDrawLable();
			if (btcDrawLable != null)
				btcDrawLable.drawLable(null, null);			
		} catch (Exception e) {// 解析页面出错时给出提示框
			LogGloble.e(TAG,e.toString());
		}
	}
	
	
	
	//String  flagElementName;
	public void resettittle(BTCElement rootElement) {
		lyt_body.setVisibility(View.GONE);
		uitittle.setVisibility(View.VISIBLE);
		
	//	cmwApplication = new BTCCMWApplication();
		// 遍历标签列表，画出相应的控件，并布局到页面上
		try {
			BTCDrawLable btcDrawLable;

			BTCCMWApplication.hiddenboxmap.clear();
			
			
			//btcDrawLable = BTCLableFactory.createLableInstance(
			//			self,BTCUiActivity.this, rootElement.getElementName());
			btcDrawLable = rootElement.getBTCDrawLable();
		//if (btcDrawLable != null) {
				//if (rootElement.getElementName().equals(BTCLable.MENU)) {
					uitittle.setVisibility(View.VISIBLE);
					uitittle.removeAllViews();
					btcDrawLable.drawLable(null, uitittle);
		//	}
	//	rootElement.getBTCDrawLable().execute(uitittle);
		//	IntentToActivity(rootElement.getBTCDrawLable(),uitittle);
		} catch (Exception e) {// 解析页面出错时给出提示框
//			Toast.makeText(this, e.toString(), 2);
			LogGloble.e(TAG, e.toString());
		}
	}
	/**
	 * add by lst 12 12.25 增加通讯录导入功能， 当requestCode = 101 的时候处理通讯录点击的数据 并且将
	 * 数据填入到cpinput标签内
	 */

	public static BTCUiActivity Instance() {
	
		return self;
	}

	@Override
	public void onClick(View arg0) {
		if(arg0.getId() == R.id.ib_back){
			BTCCMWApplication.security= null;
			BTCCMWApplication.isContainSecurityInit = false;
			BTCCMWApplication.isContainSecurityActive = false;
			BTCCMWApplication.listradio.clear();
			BTCCMWApplication.listDynamictextbox.clear();
			BTCCMWApplication.factorList = null;
			BTCCMWApplication.optionValue.clear();
			if(getActivityCount() > 1) {
				BTCPageData pageData =BTCActivityManager.getInstance().getPageData();
				if(pageData!=null){
					ResetActivityManager();
					resettittle(BTCActivityManager.getInstance().getPageData().getRootElement());
				}
			}
			else {
				ResetActivityManager();
				BTCCMWApplication.listDynamictextbox.clear();
				BTCCMWApplication.listradio.clear();
				BTCUiActivity.this.finish();
				
			}
			
		}

	}
	
	protected void requestPeopleService(BTCCommbean request) {
		BaseHttpEngine.showProgressDialog();
		BTCCMWApplication.flag = true;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod("PsnPlpsSendAllFlowFileReq");//PsnPlpsSendAllFlowFileReq
		biiRequestBody.setParams(request.getParams());//{CUS_ID=_CIFNUMBER, isSendCsp=0}
		HttpManager.requestBii(biiRequestBody, this, "requestPeopleService");
		
		
	}

	private IHttpCallBack httpCallBack;
	private IHttpCallBack httpRandomCallBack;
	private IHttpCallBack httpMoblieCallBack;
	protected void requestPeopleService(BTCCommbean request,IHttpCallBack callBack){
		httpCallBack = callBack;
		requestPeopleService(request);
		
	}
	
	public void  requestPeopleService(Object result){
//		BaseHttpEngine.dissMissProgressDialog();
		if(httpCallBack != null) {
			httpCallBack.requestHttpSuccess(result);
		}
		return;
		
	}
	
	
	/**
	 * 获取字典与xml
	 */
	protected void queryPeopleService(String converId) {
		BaseHttpEngine.showProgressDialog();
		BTCCMWApplication.flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flowFileId", flowFileId);
		map.put("prvcShortName", PlpsDataCenter.getInstance().getPrvcShortName());
		map.put("_refresh", "0");
		Random random = new Random();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(converId);
		biiRequestBody.setId(String.valueOf(random.nextInt(1000000000)));
		biiRequestBody.setMethod("PsnPlpsQueryFlowFileAndLang");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "callPeopleService");
	}
	
	public void callPeopleService(Object result){
		BaseHttpEngine.dissMissProgressDialog();
		try {
			BiiResponse biiResponse = (BiiResponse) result;
			List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
			BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
			
			BTCCMWApplication.results= biiResponseBody.getResult().toString();
			//发送PsnPlpsSaveBocServiceLog请求
			requestPSNGetToken();
			Map<String, Object> resultstr=(Map<String, Object>) biiResponseBody.getResult();
			if (resultstr.containsKey("flowFileLangMap")) {
				Map<String, Object> fileMap = (Map<String, Object>) resultstr.get("flowFileLangMap");
				for(Map.Entry<String, Object> item : fileMap.entrySet()){
					String value =(String) item.getValue();
//					if(value.contains("\n")){
//						value = value.replace("\n", "");
//					}
					if(value.contains("&nbsp")){
						value = value.replace("&nbsp", "");
					}
					if(value.contains("\t")){
						value = value.replace("\t", "");
					}
					if(value.contains("\b")){
						value = value.replace("\b", "");
					}
					if(value.contains("\r")){
						value = value.replace("\r", "");
					}
					if(value.contains("\\t")){
						value = value.replace("\\t", "");
					}
					if(value.contains("\\b")){
						value = value.replace("\\b", "");
					}
					if(value.contains("\\r")){
						value = value.replace("\\r", "");
					}
					if(value.contains("\\n")){
						value = value.replace("\\n", "\n");
					}
					if(value.contains("<br />")){
						value = value.replace("<br />","");
					}
					if(value.contains("</b>")){
						value = value.replace("</b>","");
					}
					if(value.contains("<b>")){
						value = value.replace("<b>","");
					}
					if(value.contains("</br>")){
						value = value.replace("</br>","");
					}
//					if(value.contains("<font")){
//						int starPosition = value.indexOf("<");
//						int endposition = value.indexOf(">");
//						int lastend  = value.indexOf("</");
//						value =value.substring(endposition+1, lastend);
//					}
					while(value.contains("<font")){
						int starPosition = value.indexOf("<font");
						int endposition = value.indexOf(">",starPosition);
						if(endposition == -1){
							break;
						}
						value = value.substring(0, starPosition) + value.substring(endposition+1);
					}
					value = value.replace("</font>","");
					fileMap.put(item.getKey(), value);
				}
				cmwApplication.setFlowFileLangMap(fileMap);
				
			}
			if (resultstr.containsKey("flowFileXmlStream")) {
				LogGloble.i("info", " flowFileXmlStream======"+ result.toString());
				String xml=(String)resultstr.get("flowFileXmlStream");
				byte bb[] = xml.getBytes();
				ByteArrayInputStream in = new ByteArrayInputStream(bb);
				handler.sendMessage(handler.obtainMessage(1,getRootElement(in)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
	}
	
	/**
	 * 登录后的tokenId
	 */
	public void requestPSNGetToken() {
		BTCCMWApplication.flag = true;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.PSN_GETTOKENID);
		biiRequestBody.setConversationId(BTCCMWApplication.conversationIdLang);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPSNGetTokenIdCall");
	}
	
	public void requestPSNGetTokenIdCall(Object resultObj) {
	
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String token = (String) biiResponseBody.getResult();
		BaseDroidApp.getInstanse().getBizDataMap()
				.put(ConstantGloble.TOKEN_ID, token);
		String tokens = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.TOKEN_ID);
		requestServiceLog(tokens);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
		
	}
	
	/**
	 * 获取记录网银服务日志
	 */
	protected void requestServiceLog(String token) {
		BTCCMWApplication.flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("menuName", menuName);
		map.put("itemName", itemName);
		
		map.put("flowFileName", flowFileName);
		map.put("token", token);
		Random random = new Random();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(BTCCMWApplication.conversationIdLang);
		biiRequestBody.setId(String.valueOf(random.nextInt(1000000000)));
		biiRequestBody.setMethod("PsnPlpsSaveBocServiceLog");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "callbackServiceLog");
	}
	
	
	public void callbackServiceLog(Object result){
		BaseHttpEngine.dissMissProgressDialog();
		try {
						
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	@SuppressLint("HandlerLeak")
	private  Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Element root = (Element) msg.obj;
			if (msg.what == -1) {
			
			} else if(msg.what == 1){
				ResetActivityManager();
				BTCControlCenter.createPage(BTCUiActivity.this, root);
				BTCElement rootElement = BTCActivityManager.getInstance().getPageData().getRootElement();
				//加入
				if(BTCCMWApplication.isDisp != null &&!"".equals(BTCCMWApplication.isDisp) ){
				BTCElement flowElement =rootElement.getBTCDrawLable().findNearElemntBy(new IFunction(){

						@Override
						public <T> boolean func(T t) {
							String name = ((BTCElement)t).getName(null);
							
							if(name != null &&  name.equals(flowFileName)) {
								return true;
							}
							return false;
						}});
					
					if(flowElement!=null && !"".equals(flowElement))
					{
					navigationToActivity(flowElement);
					}else{
						resettittle(rootElement);
					}
				}else {
					resettittle(rootElement);
				}

				
			}
			
		}
	};
	
	
	protected Element getRootElement(InputStream is) throws IOException,
			SAXException, ParserConfigurationException,
			FactoryConfigurationError {
		BufferedReader br = new BufferedReader(new InputStreamReader(is), 8096);
		String line = null;
		StringBuffer sb = new StringBuffer("");
		while ((line = br.readLine()) != null) {
			sb.append(line);
			LogGloble.i(TAG, line);
		}
		is.close();
		br.close();
		LogGloble.i(TAG, sb.toString());
		String s = BTCKxmlParser.charParse(sb.toString());
		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new ByteArrayInputStream(s.getBytes("utf-8")))
				.getDocumentElement();
	}
	
	
//	public static void changeActivity(String id, Intent intent) {
//	container.removeAllViews();
//	View v = manager.startActivity(id, intent).getDecorView();
//	container.addView(v);
//
//	}
	private IHttpCallBack httpAccountListCallBack;
	/**
	 * 查询可用缴费帐户列表   actcombo标签中用到
	 */
	protected void queryAvaliableAccountList(List<String> type) {
		BaseHttpEngine.showProgressDialog();
		BTCCMWApplication.flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flowFileId", flowFileId);
		map.put("prvcShortName", PlpsDataCenter.getInstance().getPrvcShortName());
		map.put("accountType", type);
		
		Random random = new Random();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setId(String.valueOf(random.nextInt(1000000000)));
		biiRequestBody.setMethod("PsnPlpsQueryAvaliableAccountList");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "callBackQueryAvaliableAccountList");
	}
	
	protected void callBackQueryAvaliableAccountList(List<String> type,IHttpCallBack callBack){
		httpAccountListCallBack = callBack;
		queryAvaliableAccountList(type);
	}
	
	
	public void callBackQueryAvaliableAccountList(Object result){
		
		if(httpAccountListCallBack != null) {
			httpAccountListCallBack.requestHttpSuccess(result);
		}
		return;
		
		
	}
	
	/**
	 * 请求安全因子的随机数
	 */
	protected void requestRandom() {
		BaseHttpEngine.showProgressDialog();
		BTCCMWApplication.flag = true;
//		Map<String, Object> map = new HashMap<String, Object>();
		Random random = new Random();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(conversationId);
		biiRequestBody.setId(String.valueOf(random.nextInt(1000000000)));
		biiRequestBody.setMethod("PSNGetRandom");
		biiRequestBody.setParams(null);
		HttpManager.requestBii(biiRequestBody, this, "requestRandomCallBack");
	}
	
	protected void requestRandomCallBack(IHttpCallBack callBack){
		httpRandomCallBack = callBack;
		requestRandom();
	}
	
	
	public void requestRandomCallBack(Object result){
		if(httpRandomCallBack != null) {
			httpRandomCallBack.requestHttpSuccess(result);
		}
	}
	
	
	// 发送短信验证码
	public void sendMSCToMobile() {
		BTCCMWApplication.flag = true;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
//		biiRequestBody.setMethod(Setting.SET_SENDMSC);
		biiRequestBody.setMethod("PsnSendSMSCodeToMobile");//PsnSendSMSCodeToMobile  PsnSendSMSCodeAfterLogin
		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
		biiRequestBody.setParams(null);
		BiiHttpEngine.showProgressDialog();
		HttpManager.requestBii(biiRequestBody, this, "sendMSCToMobileCallback");
	}

	protected void sendMSCToMobileCallback(IHttpCallBack callBack){
		httpMoblieCallBack = callBack;
		sendMSCToMobile();
	}
	
	public void sendMSCToMobileCallback(Object resultObj) {
		// 通讯结束,关闭通讯框
//		BiiHttpEngine.dissMissProgressDialog();
		if(httpMoblieCallBack != null) {
			httpMoblieCallBack.requestHttpSuccess(resultObj);
		}
		return;
	}
	
	
	private IHttpCallBack httpSecurityFactorCallBack;
	/**
	 * 获取安全因子组合
	 * 
	 * @param serviceId 服务码
	 */
	
	public void requestGetSecurityFactorFromAction(IHttpCallBack callBack,String serviceId) {
		// 通讯结束,关闭通讯框
		httpSecurityFactorCallBack = callBack;
		super.requestGetSecurityFactor(serviceId);
	}
	
	/**
	 * 处理获取安全因子组合获取
	 * 
	 * @param resultObj 服务器返回数据
	 */
	@Override
	public void requestGetSecurityFactorCallBack(Object resultObj) {
		super.requestGetSecurityFactorCallBack(resultObj);
		if(httpSecurityFactorCallBack != null) {
			httpSecurityFactorCallBack.requestHttpSuccess(resultObj);
		}
		return;
		// return BaseDroidApp.getInstanse().getTokenNameList();
	}
	
	/**
	 * 请求系统时间
	 * @param callBack
	 */
	public void requestFromMenuSystemDateTime(IHttpCallBack callBack) {
		httpSecurityFactorCallBack = callBack;
		BaseHttpEngine.showProgressDialog();
		super.requestSystemDateTime();
	}
	
	
	@Override
	public void requestSystemDateTimeCallBack(Object resultObj) {
		super.requestSystemDateTimeCallBack(resultObj);
		BaseHttpEngine.dissMissProgressDialog();
		BTCCMWApplication.flowFileLangMap.put("_SYSTEMDATETIME", dateTime);
		LogGloble.i("infowuhan", "dateTime =="+ dateTime);
		if(httpSecurityFactorCallBack != null) {
			httpSecurityFactorCallBack.requestHttpSuccess(resultObj);
		}
		return;
	}
	
//	public  void get(View views)
//	{
//		View view =getWindow().peekDecorView();
//		//如果view为null则没有弹出虚拟键盘
//		//如果view不为null则弹出了虚拟键盘
////        if (view == null)
////        {
//            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////            inputmanger.showSoftInput(views, InputMethodManager.SHOW_IMPLICIT);
//            inputmanger.showSoftInput(views,InputMethodManager.SHOW_FORCED);
////        }
//	}
	public void requestCommConversationId() {
		BaseHttpEngine.showProgressDialog();
		BTCCMWApplication.flag = true;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Comm.CONVERSATION_ID_API);
		HttpManager.requestBii(biiRequestBody, this, "requestCommConversationIdCallBack");
	}
	
	
	public void requestCommConversationIdCallBack(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		BiiResponse biiResponse = (BiiResponse) resultObj;
		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
		String commConversationId = (String) biiResponseBody.getResult();
		BTCCMWApplication.conversationIdLang = commConversationId;
		queryPeopleService(commConversationId);
	}
	
	private IHttpCallBack requestCallBack;
	
	/**
	 * 上送缴费成功后接口
	 */
	protected void queryRegisterDbRecord(String isDisp,String argsKey,String argsVal,
			String summary,String flowName,String stepName,String token) {
		BaseHttpEngine.showProgressDialog();
		BTCCMWApplication.flag = true;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flowFileId", flowFileId);
//		map.put("prvcShortName", PlpsDataCenter.getInstance().getPrvcShortName());
//		map.put("_refresh", "0");
		map.put("isDisp", isDisp);
		map.put("argsKey", argsKey);
		map.put("argsVal", argsVal);
		map.put("summary", summary);
		map.put("flowName", flowName);
		map.put("stepName", stepName);
		map.put("token", token);
//		token
		Random random = new Random();
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setConversationId(BTCCMWApplication.conversationIdLang);
		biiRequestBody.setId(String.valueOf(random.nextInt(1000000000)));
		biiRequestBody.setMethod("PsnPlpsRegisterDbRecord");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this, "RegisterDbRecordCallBack");
	}
	
	
	protected void RegisterDbRecordCallBack(String isDisp,String argsKey,String argsVal,
			String summary,String flowName,String stepName,String token,IHttpCallBack callBack){
		requestCallBack = callBack;
		queryRegisterDbRecord(isDisp,argsKey,argsVal,summary,flowName,stepName,token);
	}
	
	
	public void RegisterDbRecordCallBack(Object result){
		BaseHttpEngine.dissMissProgressDialog();
		if(requestCallBack != null) {
			requestCallBack.requestHttpSuccess(result);
		}
		return;
	}
	

	/**
	 *Token
	 * 
	 * @param
	 */
	public void requestRegisterDbTokenCallBack(IHttpCallBack callBack) {
		// 通讯结束,关闭通讯框
		requestCallBack = callBack;
		super.requestPSNGetTokenId(BTCCMWApplication.conversationIdLang);
	}
	
	/**
	 * token
	 * 
	 * @param resultObj 服务器返回数据
	 */
	@Override
	public void requestPSNGetTokenIdCallBack(Object resultObj) {
		super.requestPSNGetTokenIdCallBack(resultObj);
		if(requestCallBack != null) {
			requestCallBack.requestHttpSuccess(resultObj);
		}
	}
	
	
	
	
	/** 解析Activity页面堆栈管理  */
	private List<BTCDrawLable> activityManager = new ArrayList<BTCDrawLable>();
	
	public void IntentToActivity(BTCDrawLable rootElement,View view){
		int nIndex = activityManager.indexOf(rootElement);
		if(nIndex >= 0){
			while(activityManager.size() > nIndex){
		    	activityManager.remove(activityManager.get(activityManager.size() -1));
		    }
		}
	    activityManager.add(rootElement);
	}
	
	public int getActivityCount(){
		return activityManager.size();
	}
	
	
	public void ResetActivityManager(){
		activityManager.clear();
	}
	
	public void dis (){
		View view = getWindow().peekDecorView();
		//如果view为null则没有弹出虚拟键盘
		//如果view不为null则弹出了虚拟键盘
        if (view != null)
        {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);//强行隐藏输入法
        }
	}
	
	/*拦截错误信息*/
	public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
		List<BiiResponseBody> biiResponseBodyList = response.getResponse();
		if(StringUtil.isNullOrEmpty(biiResponseBodyList) == true)
			return false;
		for (BiiResponseBody body : biiResponseBodyList) {
			if(body.getMethod().equals("PsnPlpsRegisterDbRecord")){
				BaseHttpEngine.dissMissProgressDialog();
				return false;
			}else{
				if (ConstantGloble.STATUS_SUCCESS.equals(body.getStatus())) continue;
				// 消除通信框
				BaseHttpEngine.dissMissProgressDialog();
			
				BiiError biiError = body.getError();
				// 判断是否存在error
				if (biiError == null) {
					BaseDroidApp.getInstanse().dismissErrorDialog();
					// 避免没有错误信息返回时给个默认的提示
					BaseDroidApp.getInstanse().createDialog("", getResources().getString(R.string.request_error),
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									BaseDroidApp.getInstanse().dismissErrorDialog();
//											if (BaseHttpEngine.canGoBack) {
//												finish();
//												BaseHttpEngine.canGoBack = false;
//											}
								}
							});
					return true;
				}

				//过滤错误
				LocalData.Code_Error_Message.errorToMessage(body);
				if (biiError.getCode() != null) {
					if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
						showTimeOutDialog(biiError.getMessage());
// BaseDroidApp.getInstanse().showMessageDialog(biiError.getMessage(),
//								new OnClickListener() {
//
//									@Override
//									public void onClick(View v) {
//										BaseDroidApp.getInstanse().dismissErrorDialog();
//										ActivityTaskManager.getInstance().removeAllActivity();
//										ActivityTaskManager.getInstance().removeAllSecondActivity();
////										Intent intent = new Intent();
////										intent.setClass(BTCUiActivity.this, LoginActivity.class);
////										startActivityForResult(intent, ConstantGloble.ACTIVITY_RESULT_CODE);
//										BaseActivity.getLoginUtils(BTCUiActivity.this).exe(new LoginTask.LoginCallback() {
//
//											@Override
//											public void loginStatua(boolean isLogin) {
//
//											}
//										});
//									}
//								});
					} else {// 非会话超时错误拦截
//						if (!StringUtil.isNullOrEmpty(listShieldErrorCode)) {
//							if (listShieldErrorCode.contains(biiError.getCode())) {
//								doShieldErrorCode();
//								return true;
//							}
//						}
						BaseDroidApp.getInstanse().createDialog(biiError.getCode(), biiError.getMessage(),
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										BaseDroidApp.getInstanse().dismissErrorDialog();
//													if (BaseHttpEngine.canGoBack) {
//														finish();
//														BaseHttpEngine.canGoBack = false;
//													}
									}
								});
					}
					return true;
				}
				// 弹出公共的错误框
				BaseDroidApp.getInstanse().dismissErrorDialog();
				BaseDroidApp.getInstanse().createDialog("", biiError.getMessage(), new OnClickListener() {
					@Override
					public void onClick(View v) {
						BaseDroidApp.getInstanse().dismissErrorDialog();
//									if (BaseHttpEngine.canGoBack) {
//										finish();
//										BaseHttpEngine.canGoBack = false;
//									}
					}
				});
			

				return true;
			}
			
		}
	

		return false;
	}
	
	/**
	 * 请求账户详情
	 */
	public void requestAccBankAccountDetail(String accountId) {
		BaseHttpEngine.showProgressDialog();
		BTCCMWApplication.flag = true;
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod(Acc.QRY_ACC_BALANCE_API);
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put(Acc.DETAIL_ACC_ACCOUNTID_REQ, accountId);
		biiRequestBody.setParams(paramsmap);
		HttpManager.requestBii(biiRequestBody, this, "accBankAccountDetailCallback");
	}
	
	protected void requestAccBankAccountDetail(String accountId,IHttpCallBack callBack){
		requestCallBack = callBack;
		requestAccBankAccountDetail(accountId);
	}
	
	public void accBankAccountDetailCallback(Object result){
		if(requestCallBack != null) {
			requestCallBack.requestHttpSuccess(result);
		}
		return;
	}
}

