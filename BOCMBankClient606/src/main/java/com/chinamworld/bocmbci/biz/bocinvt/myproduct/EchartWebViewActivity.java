package com.chinamworld.bocmbci.biz.bocinvt.myproduct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.ViewUtils;

@SuppressLint("SetJavaScriptEnabled")
public class EchartWebViewActivity extends BaseActivity {
	private View view;
	private WebView webView = null;
	private String dates = "";
	private String values = "";
	/** 当天净值 */
	private String value;
	/** 净值日期 */
	private String valueDate;
	/** 产品代码 */
	private String productCode;
	private String period;
	/** 查询条件 */
	private Spinner boci_time;
	/** 净值 */
	private TextView tv_topright;
	/** 净值日期 */
	private TextView tv_date;
	/** 无历史净值 */
	private RelativeLayout empty_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 基金走势图横屏
		productCode = getIntent().getStringExtra("productCode");
		period = getIntent().getStringExtra("period");
		value = getIntent().getStringExtra("value");
		valueDate = getIntent().getStringExtra("valueDate");
		// 为界面标题赋值
		// setTitle(this.getString(R.string.boci_myproduct_title));
		// setText(this.getString(R.string.go_main));
		// 添加布局
		setContentView(R.layout.bocinvt_echart);
		// view = addView(R.layout.bocinvt_echart);
		// requestPsnXpadNetHistoryQueryList(productCode, period);
		((Button) findViewById(R.id.ib_back))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		empty_layout = (RelativeLayout) findViewById(R.id.empty);
		((TextView) findViewById(R.id.tv_title)).setText("历史净值");
		tv_topright = (TextView) findViewById(R.id.tv_topright);
		tv_date = (TextView) findViewById(R.id.tv_date2);
		tv_topright.setText(StringUtil.parseStringPattern(value,4));
		tv_date.setText(valueDate);
		boci_time = (Spinner) findViewById(R.id.boci_time);
		ArrayAdapter<String> transactionAdapter = new ArrayAdapter<String>(
				this, R.layout.custom_spinner_item, getResources()
						.getStringArray(R.array.bocinvt_query_time_type));
		transactionAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		boci_time.setAdapter(transactionAdapter);
		webView = (WebView) findViewById(R.id.webView);
		boci_time.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				switch (position) {
				case 0:// 最近三个月
					dates = "";
					values = "";
					requestPsnXpadNetHistoryQueryList(productCode, "0");
					
					break;
				case 1:// 最近一年
					dates = "";
					values = "";
					requestPsnXpadNetHistoryQueryList(productCode, "1");
					break;
				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public ActivityTaskType getActivityTaskType() {
		// TODO Auto-generated method stub
		return ActivityTaskType.TwoTask;

	}

	/**
	 * 历史净值查询
	 * 
	 * @param productCode
	 *            产品代码
	 * @param period
	 *            查询范围 0：三个月（默认）1:一年
	 */
	private void requestPsnXpadNetHistoryQueryList(String productCode,
			String period) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("productCode", productCode);
		map.put("period", period);
		BiiRequestBody biiRequestBody = new BiiRequestBody();
		biiRequestBody.setMethod("PsnXpadNetHistoryQuery");
		biiRequestBody.setParams(map);
		HttpManager.requestBii(biiRequestBody, this,
				"requestPsnXpadNetHistoryQueryListCallback");
		BaseHttpEngine.showProgressDialog();
	}

	/**
	 * 历史净值查询回调
	 * 
	 * @param resultObj
	 */
	@SuppressWarnings("unchecked")
	public void requestPsnXpadNetHistoryQueryListCallback(Object resultObj) {
		BaseHttpEngine.dissMissProgressDialog();
		Map<String, Object> map = HttpTools.getResponseResult(resultObj);
		String recordNumber = (String) map.get(BocInvt.PROGRESS_RECORDNUM);
		List<Map<String, Object>> list = (List<Map<String, Object>>) map
				.get(BocInvt.BOCI_LIST_RES);
		if (StringUtil.isNullOrEmpty(list)) {
			webView.setVisibility(View.GONE);
			empty_layout.setVisibility(View.VISIBLE);
			return;
		} else {
			empty_layout.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
			list = (List<Map<String, Object>>) map.get(BocInvt.BOCI_LIST_RES);
			for (Map<String, Object> mapInfo : list) {
				String dates2 = (String) mapInfo.get("valueDate");
				dates = dates + dates2 + "\",\"";
				values = values + (String) mapInfo.get("netValue") + "\",\"";
			}
			StringBuffer datastemp = new StringBuffer(dates);
			StringBuffer valuestemp = new StringBuffer(values);
			datastemp.delete(datastemp.length() - 2, datastemp.length());
			valuestemp.delete(valuestemp.length() - 2, datastemp.length());
			dates = datastemp.toString();
			values = valuestemp.toString();
		}
		ViewUtils.initWebView(webView);
		// 设置字符集编码
		webView.getSettings().setDefaultTextEncodingName("UTF-8");
		// 开启JavaScript支持
		webView.getSettings().setJavaScriptEnabled(true);
		// 加载assets目录下的文件
		webView.loadUrl("file:///android_asset/EChart/index.html");

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// String dates =
				// "[\"2013/1/24\", \"2013/1/25\", \"2013/1/28\", \"2013/1/29\", \"2013/1/30\", \"2013/1/31\", \"2013/2/1\", \"2013/2/4\", \"2013/2/5\", \"2013/2/6\", \"2013/2/7\", \"2013/2/8\", \"2013/2/18\", \"2013/2/19\", \"2013/2/20\", \"2013/2/21\", \"2013/2/22\", \"2013/2/25\", \"2013/2/26\", \"2013/2/27\", \"2013/2/28\", \"2013/3/1\", \"2013/3/4\", \"2013/3/5\", \"2013/3/6\", \"2013/3/7\", \"2013/3/8\", \"2013/3/11\", \"2013/3/12\", \"2013/3/13\", \"2013/3/14\", \"2013/3/15\", \"2013/3/18\", \"2013/3/19\", \"2013/3/20\", \"2013/3/21\", \"2013/3/22\", \"2013/3/25\", \"2013/3/26\", \"2013/3/27\", \"2013/3/28\", \"2013/3/29\", \"2013/4/1\", \"2013/4/2\", \"2013/4/3\", \"2013/4/8\", \"2013/4/9\", \"2013/4/10\", \"2013/4/11\", \"2013/4/12\", \"2013/4/15\", \"2013/4/16\", \"2013/4/17\", \"2013/4/18\", \"2013/4/19\", \"2013/4/22\", \"2013/4/23\", \"2013/4/24\", \"2013/4/25\", \"2013/4/26\", \"2013/5/2\", \"2013/5/3\", \"2013/5/6\", \"2013/5/7\", \"2013/5/8\", \"2013/5/9\", \"2013/5/10\", \"2013/5/13\", \"2013/5/14\", \"2013/5/15\", \"2013/5/16\", \"2013/5/17\", \"2013/5/20\", \"2013/5/21\", \"2013/5/22\", \"2013/5/23\", \"2013/5/24\", \"2013/5/27\", \"2013/5/28\", \"2013/5/29\", \"2013/5/30\", \"2013/5/31\", \"2013/6/3\", \"2013/6/4\", \"2013/6/5\", \"2013/6/6\", \"2013/6/7\", \"2013/6/13\"]";
				// String values =
				// "[[2500.26,2302.6,2287.3,2362.94],[2300,2291.3,2288.26,2308.38],[2295.35,2346.5,2295.35,2346.92],[2347.22,2358.98,2337.35,2363.8],[2360.75,2382.48,2347.89,2383.76],[2383.43,2385.42,2371.23,2391.82],[2377.41,2419.02,2369.57,2421.15],[2425.92,2428.15,2417.58,2440.38],[2411,2433.13,2403.3,2437.42],[2432.68,2434.48,2427.7,2441.73],[2430.69,2418.53,2394.22,2433.89],[2416.62,2432.4,2414.4,2443.03],[2441.91,2421.56,2415.43,2444.8],[2420.26,2382.91,2373.53,2427.07],[2383.49,2397.18,2370.61,2397.94],[2378.82,2325.95,2309.17,2378.82],[2322.94,2314.16,2308.76,2330.88],[2320.62,2325.82,2315.01,2338.78],[2313.74,2293.34,2289.89,2340.71],[2297.77,2313.22,2292.03,2324.63],[2322.32,2365.59,2308.92,2366.16],[2364.54,2359.51,2330.86,2369.65],[2332.08,2273.4,2259.25,2333.54],[2274.81,2326.31,2270.1,2328.14],[2333.61,2347.18,2321.6,2351.44],[2340.44,2324.29,2304.27,2352.02],[2326.42,2318.61,2314.59,2333.67],[2314.68,2310.59,2296.58,2320.96],[2309.16,2286.6,2264.83,2333.29],[2282.17,2263.97,2253.25,2286.33],[2255.77,2270.28,2253.31,2276.22],[2269.31,2278.4,2250,2312.08],[2267.29,2240.02,2239.21,2276.05],[2244.26,2257.43,2232.02,2261.31],[2257.74,2317.37,2257.42,2317.86],[2318.21,2324.24,2311.6,2330.81],[2321.4,2328.28,2314.97,2332],[2334.74,2326.72,2319.91,2344.89],[2318.58,2297.67,2281.12,2319.99],[2299.38,2301.26,2289,2323.48],[2273.55,2236.3,2232.91,2273.55],[2238.49,2236.62,2228.81,2246.87],[2229.46,2234.4,2227.31,2243.95],[2234.9,2227.74,2220.44,2253.42],[2232.69,2225.29,2217.25,2241.34],[2196.24,2211.59,2180.67,2212.59],[2215.47,2225.77,2215.47,2234.73],[2224.93,2226.13,2212.56,2233.04],[2236.98,2219.55,2217.26,2242.48],[2218.09,2206.78,2204.44,2226.26],[2199.91,2181.94,2177.39,2204.99],[2169.63,2194.85,2165.78,2196.43],[2195.03,2193.8,2178.47,2197.51],[2181.82,2197.6,2175.44,2206.03],[2201.12,2244.64,2200.58,2250.11],[2236.4,2242.17,2232.26,2245.12],[2242.62,2184.54,2182.81,2242.62],[2187.35,2218.32,2184.11,2226.12],[2213.19,2199.31,2191.85,2224.63],[2203.89,2177.91,2173.86,2210.58],[2170.78,2174.12,2161.14,2179.65],[2179.05,2205.5,2179.05,2222.81],[2212.5,2231.17,2212.5,2236.07],[2227.86,2235.57,2219.44,2240.26],[2242.39,2246.3,2235.42,2255.21],[2246.96,2232.97,2221.38,2247.86],[2228.82,2246.83,2225.81,2247.67],[2247.68,2241.92,2231.36,2250.85],[2238.9,2217.01,2205.87,2239.93],[2217.09,2224.8,2213.58,2225.19],[2221.34,2251.81,2210.77,2252.87],[2249.81,2282.87,2248.41,2288.09],[2286.33,2299.99,2281.9,2309.39],[2297.11,2305.11,2290.12,2305.3],[2303.75,2302.4,2292.43,2314.18],[2293.81,2275.67,2274.1,2304.95],[2281.45,2288.53,2270.25,2292.59],[2286.66,2293.08,2283.94,2301.7],[2293.4,2321.32,2281.47,2322.1],[2323.54,2324.02,2321.17,2334.33],[2316.25,2317.75,2310.49,2325.72],[2320.74,2300.59,2299.37,2325.53],[2300.21,2299.25,2294.11,2313.43],[2297.1,2272.42,2264.76,2297.1],[2270.71,2270.93,2260.87,2276.86],[2264.43,2242.11,2240.07,2266.69],[2242.26,2210.9,2205.07,2250.63],[2190.1,2148.35,2126.22,2190.1]]";

				// String str=
				// "{\"tooltip\": {\"trigger\":\"axis\"}, \"calculable\":false,\"dataZoom\":{\"show\":true,\"realtime\":true,\"start\":0,\"end\":100},\"grid\":{\"x\":40,\"y\":40,\"x2\":40}, \"xAxis\":[{\"type\":\"category\",\"boundaryGap\":false,\"data\":[\"2-1\",\"2-2\",\"2-3\",\"2-4\",\"2-5\",\"2-6\",\"2-7\",\"2-8\",\"2-9\",\"2-10\",\"2-11\",\"2-12\",\"2-13\",\"2-14\",\"2-15\"]}],\"yAxis\":[{\"type\":\"value\"}],\"series\":[{\"name\":\"单位净值\",\"type\":\"line\",\"data\":[5,1,2.5,3,4,6,5,4.5,4.88,5,5.5,5.8,6.55,5,4.5],\"showAllSymbol\":true}]}";
				String str = "{\"tooltip\": {\"trigger\":\"axis\"}, \"calculable\":false,\"dataZoom\":{\"show\":true,\"realtime\":true,\"start\":0,\"end\":100},\"grid\":{\"x\":40,\"y\":40,\"x2\":40}, \"xAxis\":[{\"type\":\"category\",\"boundaryGap\":false,\"data\":[\""
						+ dates
						+ "]}],\"yAxis\":[{\"type\":\"value\"}],\"series\":[{\"name\":\"单位净值\",\"type\":\"line\",\"data\":[\""
						+ values + "],\"showAllSymbol\":true}]}";
				// 页面加载完成后再调用JS方法
				if (newProgress == 100) {
					webView.loadUrl("javascript:draw('" + str + "','"
							+ "default" + "')");
				}
				super.onProgressChanged(view, newProgress);
			}

		});
	}
}
