package com.boc.bocsoft.mobile.bocmobile.base.widget.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 标签选择控件
 * 
 * @author yuht
 *
 */
public class TableLabelView extends FrameLayout {

	private RadioGroup radioGroup = null;
	private Context context;

	private ITabClickListener mTabListener = null;

	public TableLabelView(Context context) {
		super(context);
		initView(context, null);
	}

	public TableLabelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs);
	}

	public void setTabClickListener(ITabClickListener listener){
		mTabListener = listener;
	}

	private void initView(Context context, AttributeSet attrs) {
		this.context = context;
		radioGroup = new RadioGroup(context);
		radioGroup.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		radioGroup.setOrientation(LinearLayout.HORIZONTAL);
		super.addView(radioGroup);

		TypedArray t = context.obtainStyledAttributes(attrs,
				R.styleable.TableLabelView);
		int id;
		for (int i = 0; i < t.getIndexCount(); i++) {
			id = t.getIndex(i);
			if (id == R.styleable.TableLabelView_tableLabelContentText){
				createView(t.getString(id));
			}
		}
		t.recycle();

		setCurSelectedIndex(0);
	}

	private void createView(String contentText) {
		if (StringUtils.isEmpty(contentText) == true) {
			return;
		}
		radioGroup.removeAllViews();
		String[] tmp = contentText.split(";");
		int nIndex = 0;
		View rb = null;
		for (int i = 0; i < tmp.length; i++) {
			nIndex = i;
			if (i == tmp.length - 1) {
				nIndex = -1;
			}
			rb = createRadioButton(nIndex, tmp[i]);
			rb.setTag(i);
			radioGroup.addView(rb);
		}
	}

	private View createRadioButton(int nIndex, String text) {
		ViewGroup rb = (ViewGroup) LayoutInflater.from(context).inflate(
				R.layout.tablelabelview_new_layout, this, false);
		RadioButton v = null;
		if (nIndex == 0)
			v = (RadioButton) rb.findViewById(R.id.rb_RadioButton1);
		else if (nIndex == -1) {
			v = (RadioButton) rb.findViewById(R.id.rb_RadioButton3);
		} else {
			v = (RadioButton) rb.findViewById(R.id.rb_RadioButton2);
		}
		v.setText(text);
		
		v.setOnClickListener(clickListener);
		if (v != null)
			rb.removeView(v);
		rb = null;
		return v;
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			curSelectedIndex = (Integer) v.getTag();
			refleshPressBackground();
			if (onClickListener != null) {
				onClickListener.onClick(v);
			}

			if(mTabListener != null){
				mTabListener.onClickTab(curSelectedIndex);
			}
		}

	};

	/**
	 * 设置文本内容
	 * 
	 * @param text
	 */
	public void setContentText(String text) {
		createView(text);
	}

	private OnClickListener onClickListener = null;

	/**
	 * 设置标签点击事件
	 */
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	private int curSelectedIndex = -1;

	/**
	 * 获得当前选中的标签
	 * 
	 * @return
	 */
	public int getCurSelectedIndex() {
		return curSelectedIndex;
	}

	/**
	 * 设置当亲选中标签
	 * 
	 * @param curSelectedIndex
	 */
	public void setCurSelectedIndex(int curSelectedIndex) {
		if (radioGroup.getChildCount() <= curSelectedIndex) {
			return;
		}
		this.curSelectedIndex = curSelectedIndex;
		refleshPressBackground();

	}

	private void refleshPressBackground() {
		RadioButton rb;
		if (isShowPressBackground == true) {
			rb = (RadioButton) radioGroup.getChildAt(curSelectedIndex);
			rb.setChecked(true);
		} else {
			for (int i = 0; i < radioGroup.getChildCount(); i++) {
				rb = (RadioButton) radioGroup.getChildAt(i);
				rb.setChecked(false);
			}
		}

	}

	private boolean isShowPressBackground = true;

	/**
	 * 选中摸某一个标签时，是否需要显示选中背景
	 * 
	 * @param isShowPressBackground
	 */
	public void setIsShowPressBackground(boolean isShowPressBackground) {
		this.isShowPressBackground = isShowPressBackground;
	}

	public interface ITabClickListener{
		void onClickTab(int tabIndex);
	}
}

