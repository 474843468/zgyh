package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.base;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 作者：lq7090
 * 创建时间：2016/11/7.
 * 用途跨境专区内容页基础类
 * 多处可点击TextView局需要另外设置
 */
public abstract class BaseVpFragment extends BussFragment implements View.OnClickListener {
    public final int CLICKABLE = 1;//部分可点击TextView
    public final int CLICKED = 2;//全部可点击TextView
    public final int NORMAL = 3;//普通TextView
    String stss[];//该页面所有String
    ArrayList<Integer> colors = new ArrayList<>();//字体颜色
    ArrayList<Integer> styles = new ArrayList<>();//字体
    ArrayList<TextView> clickableAL = new ArrayList<>();//部分可点击TextView
    ArrayList<TextView> clickedAL = new ArrayList<>();//部分可点击TextView
    ArrayList<TextView> normalAL = new ArrayList<>();//普通TextView
    ArrayList<TextView> totalAL = new ArrayList<>();//全部TextView
    HashMap<TextView, String> tvMap = new HashMap<>();//TextView Map
    HashMap<TextView, ArrayList<String>> urlmap = new HashMap<>();//点击跳转map
    HashMap<TextView, ArrayList<Integer>> moreCkickMap = new HashMap<>();//多处可点击跳转map
    HashMap<TextView,ArrayList<Integer>> editableClickMap = new HashMap<>();//设置多段可点击，可点击位置


    /**
     * 是否显示标题栏，默认显示
     * 子类可以重写此方法，控制是否显示标题栏
     */
    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void beforeInitView() {

        setStss();
        setColors();//配置颜色
        super.beforeInitView();
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void initData() {
        super.initData();
        setTotalAL();//配置全局TextView ArrayList
        setTVMap();//配置全局TextView Map
        setUrlmap();//配置可点击部分UrL
        setClickableTextString();//配置部分可点击TextView
        setClickedTextString();//配置可点击TextView
        setNormalTextString();//配置普通lqTextView
    }

    @Override
//    统一在此处设置点击监听
    public void onClick(View v) {

       if (urlmap.get(v) == null) {
            showErrorDialog("暂时无法跳转");
        } else {
            if (v instanceof TextView) {
                TextView t = (TextView) v;//强制类型转换
                if (moreCkickMap.get(v) == null)
                {
                    if(urlmap.get(v).get(0)==null)
                        showErrorDialog("暂时无法跳转");
                    else
                        startFragment(urlmap.get(v).get(0));
                }
                else if (moreCkickMap.get(v).size() > 0) {

                    int selection = t.getSelectionStart();//点击处起点
                    boolean flag = editableClickMap.get(v)== null;//是否设置了可点击部分，如没设置按第一段不可点击，后续都可点击处理
                    int selectNum;
                    if(flag)selectNum = moreCkickMap.get(v).indexOf(selection);
                    else selectNum = editableClickMap.get(v).indexOf(moreCkickMap.get(v).indexOf(selection)+2);

                    if (selectNum < urlmap.get(v).size()&&selectNum!=-1) {
                        if (urlmap.get(v).get(selectNum) == null) {
                            showErrorDialog("暂时无法跳转");
                        } else
                            startFragment(urlmap.get(v).get(selectNum));
                    }

                }
            }
        }
    }


    /**
     * 跳转链接方法
     *
     * @param url
     */

    public void startFragment(final String url) {
        if (StringUtil.isNullOrEmpty(url))
            return;

//        Log.i("TAG", "WebURL: url="+url);

        if (url.startsWith("http://")||url.startsWith("https://")){
            //是url路径 跳转web界面
            ContractFragment contractFragment = new ContractFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            bundle.putString("content", null);
            bundle.putBoolean("loadWithOverviewMode", true);
            contractFragment.setArguments(bundle);

            start(contractFragment);
        }else{
            //是模块id 跳转对应界面
            //如果要跳转的是跨境汇款 则需先登陆
            if (!ApplicationContext.isLogin()&&
                    (ModuleCode.MODULE_CROSS_BORDER_REMIT_0400.equals(url)||ModuleCode.MODULE_CROSS_BORDER_REMIT_0100.equals(url))){
                ModuleActivityDispatcher.startToLogin(mActivity, new LoginCallback() {
                    @Override
                    public void success() {
                        ModuleActivityDispatcher.dispatch(mActivity,url);
                    }
                });
            }else{
                ModuleActivityDispatcher.dispatch(mActivity,url);
            }
        }
    }

    /**
     * 设置 一处部分可点击TextView
     * clickableMap.get(c)两部分用';'分隔
     * 对于中间有部分不可点击情况设定
     * 多处可点击要单独设置
     */
    public void setClickableTextString() {
        if (clickableAL.size() != 0)
            for (TextView c : clickableAL) {
                c.setMovementMethod(LinkMovementMethod.getInstance());//在fragment中需要给TextView设置部分可点击
                c.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
                c.setTextColor(colors.get(0));//不可点击部分字体颜色
                String ss[] = tvMap.get(c).split(";");

                if (ss.length == 2) {

                    c.setText(SpannableStringUtils.getClickableForeSpan(tvMap.get(c), this, android.graphics.Typeface.BOLD, colors.get(1)));//设置可点击部分字体样式
                }

                if (ss.length > 2) {
                  boolean flag = editableClickMap.get(c)== null;//是否设置了可点击部分，如没设置按第一段不可点击，后续都可点击处理
                    ArrayList<Integer> a = new ArrayList<>();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0;i< ss.length;i++) {
                        sb.append(ss[i]);
                        a.add(sb.length());
                    }
                    final SpannableString spanableInfo1 = new SpannableString(sb);

                    for (int i = 0; i < a.size() - 1; i++) {
                       if(flag||editableClickMap.get(c).indexOf(i+2)!=-1) {
                           spanableInfo1.setSpan(new ClickSpan(this), a.get(i), a.get(i + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置可点击区域
                           spanableInfo1.setSpan(new ForegroundColorSpan(this.getColors().get(1)), a.get(i), a.get(i + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//为可点击部分设置背景色
                           spanableInfo1.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), a.get(i), a.get(i + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                       }
                    }

                    c.setText(spanableInfo1);
                    moreCkickMap.put(c, a);
                }
            }
    }

    /**
     * 设置可点击TextView
     */
    public void setClickedTextString() {

        if (clickedAL.size() != 0)
            for (TextView c : clickedAL) {
                c.setText(tvMap.get(c));
                c.setOnClickListener(this);
                c.setTextColor(colors.get(2));
                c.setTypeface(Typeface.DEFAULT_BOLD);
//                if (colors.size() > 3)
//                    c.setBackgroundColor(colors.get(3));

            }
    }


    /**
     * 设置普通textView
     */
    public void setNormalTextString() {
        if (normalAL.size() != 0)
            for (TextView c : normalAL) {
                c.setText(tvMap.get(c));
                c.setTextColor(colors.get(0));

            }
    }


    /**
     * 构建tvMap
     */
    public void setTVMap() {
        int i = 0;
        if (totalAL.size() > 0)
            for (TextView t : totalAL) {
                tvMap.put(t, stss[i++]);
            }
    }



    /**
     * 实现此方法用于设置颜色，顺序依次为 普通颜色，部分可点击字体颜色，可点击字体颜色，可点击字体背景色
     * 操作colors
     */
    public abstract void setColors();

    /**
     * 设置三种String（不可点击，部分可点击，全可点击）字体颜色
     *
     * @param color 字体颜色
     */
    public void addColor(int color) {
        colors.add(color);
    }


    /**
     * 实现此方法用于配置可全部TextView列表
     * 操作totalAL
     */
    public abstract void setTotalAL();

    /**
     *TextView 按顺序添加到列表中
     * @param t
     * @param flag CLICKABLE,CLICKED,NORMAL 分别代表部分可点击，全部可点击，普通三种TextView
     */

    public void addTotalAL(TextView t,int flag) {
        totalAL.add(t);
        if(flag == CLICKABLE) clickableAL.add(t);
        if(flag == CLICKED ) clickedAL.add(t);
        if(flag == NORMAL) normalAL.add(t);


    }

    /**
     *TextView 按顺序添加到列表中
     * @param t
     * @param flag CLICKABLE,CLICKED,NORMAL 分别代表部分可点击，全部可点击，普通三种TextView
     *@param a 设置需要设为部分可点击的位置
     */

    public void addTotalAL(TextView t,int flag,int... a) {
        totalAL.add(t);
        if(flag == CLICKABLE) clickableAL.add(t);
        ArrayList<Integer> l = new ArrayList<>();
        for(int i: a) l.add(i);
        editableClickMap.put(t,l);

    }

    /**
     * 实现此方法用于配置TextView点击跳转URL
     * 操作urlMap
     */
    public abstract void setUrlmap();

    /**
     * 设置点击跳转链接
     *
     * @param t Textview
     * @param s url
     */
    public void putUrlmap(TextView t, String s) {

        if (urlmap.get(t) == null) {
            ArrayList<String> url = new ArrayList<>();
            url.add(s);
            urlmap.put(t, url);
        } else {
            urlmap.get(t).add(s);
        }
    }


    /**
     * 实现此方法以设置stss[]
     */
    public abstract void setStss();

    public ArrayList<Integer> getColors() {
        return colors;
    }

    public ArrayList<Integer> getStyles() {
        return styles;
    }

    public void setStss(String[] stss) {
        this.stss = stss;
    }

    public void setStyles(ArrayList<Integer> styles) {
        this.styles = styles;
    }


}
