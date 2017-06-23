package com.chinamworld.bocmbci.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.widget.adapter.MenuFunctionListAdapter;

public class MenuPopWindow extends RelativeLayout {
	/** 设备上下文 */
	private Context context;
	/** 快捷功能框 */
	public PopupWindow mainPopWindow;// 添加按钮popwindow
	/** list功能列表框 */
	public PopupWindow functionPopwindow;// 添加功能popwindow

	/** 快捷功能list */
	private ListView functionList;// 添加功能popwindow中listview
	/** 二级菜单数据 */
	private ArrayList<ArrayList<String>> functionsData = new ArrayList<ArrayList<String>>();// 二级菜单综合体
	/** 一级list数据 */
	private ArrayList<String> functionListData = new ArrayList<String>();// 大方面的添加功能名称

	private Button btn_pop;// 添加按钮popwindow的按钮
	// private Button del_view;//按钮添加功能之后可以删除按钮
	/** 返回按钮 */
	private Button backBtn;// 添加功能popwindow中返回按钮
	/** 退出按钮 */
	private Button exitBtn;// 添加功能popwindow中退出按钮
	private TextView functionText;// 添加功能popwindow中标题
	// private View popView; // 获取自定义布局文件foot_main_popwindow.xml的视图
	private LinearLayout footMenu = null;
	/** 快捷方式点击 */
	public ShotCutClick shotcutClick;

	public interface ShotCutClick {
		public void shotcutclick(View v);
	}

	public MenuPopWindow(Context context) {
		super(context, null);
	}

	public MenuPopWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	/**
	 * 展示弹出popwindow添加功能界面
	 */
	public void showPopup() {
		// 获取自定义布局文件foot_main_popwindow.xml的视图
		View popView = (View) LayoutInflater.from(context).inflate(
				R.layout.foot_main_popwindow, null);
		int pop_width = LayoutParams.FILL_PARENT;
		int pop_height = LayoutParams.WRAP_CONTENT;
		// 创建PopupWindow实例,pop_width,pop_height分别是宽度和高度
		mainPopWindow = new PopupWindow(popView, pop_width, pop_height);
		// 设置popwindow显示位置
		mainPopWindow.setAnimationStyle(R.style.PopupAnimation);
		footMenu = (LinearLayout) findViewById(R.id.rl_menu);
		mainPopWindow.showAtLocation(footMenu, Gravity.RIGHT | Gravity.BOTTOM,
				0, footMenu.getHeight() - 50);
		/*
		 * 菜单栏上面4个快捷按钮
		 */
		int btn_id[] = { R.id.btn_pop_1, R.id.btn_pop_2, R.id.btn_pop_3,
				R.id.btn_pop_4 };
		for (int id : btn_id) {
			// 配置参数用来记录是否添加了快捷方式
			String shareString = BaseDroidApp.getInstanse()
					.getSharedPrefrences().getString(String.valueOf(id), null);
			Boolean shotcut = BaseDroidApp
					.getInstanse()
					.getSharedPrefrences()
					.getBoolean(String.valueOf(id) + ConstantGloble.SHOT_CUT,
							false);
			Button btn = (Button) popView.findViewById(id);
			if (shotcut == true) {
				btn.setText(shareString);
				btn.setBackgroundResource(0);
			}
			btn.setOnLongClickListener(mylongclicklistener);
			btn.setOnClickListener(clicklistener);
		}
	}

	// 隐藏弹出popwindow添加功能界面
	public void closePopup() {
		if (mainPopWindow != null && mainPopWindow.isShowing()) {
			mainPopWindow.dismiss();
		}
	}

	/**
	 * mainPopwindow 按钮监听
	 */
	private OnClickListener clicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Boolean shotcut = BaseDroidApp
					.getInstanse()
					.getSharedPrefrences()
					.getBoolean(
							String.valueOf(v.getId()) + ConstantGloble.SHOT_CUT,
							false);
			if (shotcut == true) {
				if (shotcutClick != null) {
					shotcutClick.shotcutclick(v);
				}
			} else {
				if (functionPopwindow == null || !functionPopwindow.isShowing()) {
					showFunctionListPopwindow(v);
				}
			}
		}
	};

	// 实现接口
	public void setshotcutClick(ShotCutClick shotclick) {
		this.shotcutClick = shotclick;
	}

	// 弹出添加功能界面
	private void showFunctionListPopwindow(View v) {
		// 获取自定义布局文件show_add_function.xml的视图
		View functionListView = (View) LayoutInflater.from(context).inflate(
				R.layout.show_add_function, null);
		int pop_width = LayoutParams.FILL_PARENT;
		int pop_height = 600;
		// 创建PopupWindow实例,pop_width,pop_height分别是宽度和高度
		functionPopwindow = new PopupWindow(functionListView, pop_width,
				pop_height);
		// 为弹出添加功能点击的按钮赋值
		btn_pop = (Button) v;
		// 设置popwindow显示位置
//		functionPopwindow.showAtLocation(footMenu, Gravity.RIGHT
//				| Gravity.BOTTOM, 0,
//				findViewById(R.id.rl_menu).getHeight() + 100);
		functionPopwindow.setOutsideTouchable(false);// 这里没有效果
		functionPopwindow.setFocusable(true);

		functionList = (ListView) functionListView
				.findViewById(R.id.lv_add_function);
		backBtn = (Button) functionListView.findViewById(R.id.btn_fanhui);
		functionText = (TextView) functionListView
				.findViewById(R.id.tv_add_function);
		backBtn.setVisibility(View.INVISIBLE);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addFunction();
			}
		});
		exitBtn = (Button) functionListView.findViewById(R.id.btn_exit);
		exitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitFunction();
			}
		});
		// 为添加功能列表赋Adapter
		MenuFunctionListAdapter adapter = new MenuFunctionListAdapter(context,
				functionListData);

		adapter.setOnFunctionItemClickListener(functionItemClickListener);
		functionList.setAdapter(adapter);

	}

	private OnLongClickListener mylongclicklistener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(final View v) {
			Boolean shotcut = BaseDroidApp
					.getInstanse()
					.getSharedPrefrences()
					.getBoolean(
							String.valueOf(v.getId()) + ConstantGloble.SHOT_CUT,
							false);

			btn_pop = (Button) v;

			if (shotcut == true) {
				String message = BaseDroidApp.getContext().getResources()
						.getString(R.string.delete_shotcut_confirm);
				BaseDroidApp.getInstanse().showErrorDialog(message,
						R.string.delete, R.string.negative, onclicklistener);
			} else {

			}

			return true;
		}
	};

	// 详细功能Adapter
	private class FunctionAdapter extends BaseAdapter {
		List<String> add_function_itemname;

		@Override
		public int getCount() {
			return add_function_itemname.size();
		}

		@Override
		public Object getItem(int position) {
			return add_function_itemname.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = (View) LayoutInflater.from(context).inflate(
					R.layout.add_function_lv_item, null);
			RelativeLayout rl_item = (RelativeLayout) view
					.findViewById(R.id.rl_item);
			Button btn_go_function = (Button) view
					.findViewById(R.id.btn_go_function);
			btn_go_function.setVisibility(View.INVISIBLE);// 隐藏界面右边可进入按钮
			TextView tv_function_name = (TextView) view
					.findViewById(R.id.tv_add_name);
			tv_function_name.setText(add_function_itemname.get(position));// 详细功能名称
			// 详细选择功能时的点击item时事件
			rl_item.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 为之前点击的要添加的那个按钮功能键赋值，并设置背景为空
					// 配置参数用来记录是否添加了快捷方式
					SharedPreferences.Editor editor = BaseDroidApp
							.getInstanse().getSharedPrefrences().edit();
					editor.putString(String.valueOf(btn_pop.getId()),
							add_function_itemname.get(position));
					editor.putBoolean(String.valueOf(btn_pop.getId())
							+ ConstantGloble.SHOT_CUT, true);
					editor.commit();
					btn_pop.setText(add_function_itemname.get(position));
					btn_pop.setBackgroundResource(0);
					// 隐藏掉添加功能界面
					functionPopwindow.dismiss();
				}
			});
			return view;
		}

	}

	// 详细选择功能时的返回按钮
	private void addFunction() {
		functionText.setVisibility(View.VISIBLE);
		backBtn.setVisibility(View.INVISIBLE);
		MenuFunctionListAdapter adapter = new MenuFunctionListAdapter(context,
				functionListData);
		adapter.setOnFunctionItemClickListener(functionItemClickListener);
		functionList.setAdapter(adapter);
	}

	// 退出添加功能界面
	private void exitFunction() {
		if (functionPopwindow != null && functionPopwindow.isShowing()) {
			functionPopwindow.dismiss();
		}
	}

	public ArrayList<ArrayList<String>> getFunctionsData() {
		return functionsData;
	}

	public void setFunctionsData(ArrayList<ArrayList<String>> functionsData) {
		this.functionsData = functionsData;
	}

	public ArrayList<String> getFunctionListData() {
		return functionListData;
	}

	public void setFunctionListData(ArrayList<String> functionListData) {
		this.functionListData = functionListData;
	}

	OnItemClickListener functionItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position,
				long id) {
			functionText.setVisibility(View.INVISIBLE);
			backBtn.setVisibility(View.VISIBLE);
			// 为添加详细功能赋Adapter
			FunctionAdapter adapter = new FunctionAdapter();
			adapter.add_function_itemname = functionsData.get(position);
			functionList.setAdapter(adapter);
		}
	};

	OnClickListener onclicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (Integer.parseInt(v.getTag() + "")) {
			case CustomDialog.TAG_SURE:
				// 配置参数用来记录是否添加了快捷方式
				SharedPreferences.Editor editor = BaseDroidApp.getInstanse()
						.getSharedPrefrences().edit();
				editor.putString(String.valueOf(btn_pop.getId()), null);
				editor.putBoolean(String.valueOf(btn_pop.getId())
						+ ConstantGloble.SHOT_CUT, false);
				editor.commit();
				btn_pop.setText("");
				BaseDroidApp.getInstanse().dismissMessageDialog();
				break;
			case CustomDialog.TAG_CANCLE:
				BaseDroidApp.getInstanse().dismissMessageDialog();
				break;

			default:
				break;
			}
		}
	};
}
