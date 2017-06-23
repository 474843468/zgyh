package com.boc.bocsoft.mobile.bocmobile.base.widget.moneyruler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputTextView;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseRecycleViewAdapter;
import com.boc.bocsoft.mobile.framework.widget.listview.OnItemClickListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wangtong on 2016/10/8.
 * 修改者：谢端阳
 */
public class MoneyRulerWidget extends LinearLayout
        implements MoneyInputDialog.KeyBoardListener, OnItemClickListener {

    private static final double MAX_VALUE = 999999999999.99;
    //非日元币种数据最大12.2位，靠金额键盘限制，所以MAX_VALUE不可能达到
    private static final long MAX_VALUE_NOPOINT = 999999999999L;
    //日元币种数据最大12位，靠金额键盘限制，所以MAX_VALUE不可能达到

    protected MoneyInputTextView inputMoney;
    protected TextView moneyLabelTip;
    protected TextView moneyLabel;
    private View rootView;
    private RecyclerView recyclerView;
    private RulerWidgetAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private int columnNum;
    private int columnWidth;
    private int columnWidthPartStart, columnWidthPartEnd;
    private int columnHeight;
    private int scaleValue;
    private long moneyMin;
    private double moneyMax;
    private String moneyInit;
    private int rulerStartX;//尺子的第一个刻度位于屏幕中间
    private long pageValue;

    private long showMin, showMax;//展示的最小值和最大值刻度
    private long normalMin, normalMax;
    //按照10倍的scaleValue为一个大刻度间距，开头和结尾可能不是完整的大刻度间距，normalMin和normalMax为去除开头和结尾后的中间部分的两端
    private boolean hasPartStart, hasPartEnd;
    private String maxTip;
    private String minTip;

    private boolean isScrolledToStart;

    private MoneyRulerScrollerListener listener;

    private MoneyInputDialog.KeyBoardListener keyboardListener;

    public interface MoneyRulerScrollerListener {
        public void onMoneyRulerScrollered(BigDecimal money);
    }

    public MoneyRulerWidget(Context context) {
        super(context);
        initView();
    }

    public MoneyRulerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MoneyRulerWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        rootView = View.inflate(getContext(), R.layout.boc_layout_money_ruler_layout, null);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        inputMoney = (MoneyInputTextView) rootView.findViewById(R.id.input_money);
        inputMoney.setOnKeyBoardDismiss(this);
        moneyLabelTip = (TextView) rootView.findViewById(R.id.money_label_tip);
        moneyLabel = (TextView) rootView.findViewById(R.id.money_label);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        addView(rootView);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updateMoney();
            }
        });
    }

    public void setOnMoneyRulerScrollerListener(MoneyRulerScrollerListener listener) {
        this.listener = listener;
    }

    private long getScrollerX() {
        long result;
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisibleItem = linearLayoutManager.findViewByPosition(position);
        if (position == 0) {
            result = -firstVisibleItem.getLeft();
            isScrolledToStart = result == 0;
        } else {
            isScrolledToStart = false;
            result = (hasPartStart ? columnWidthPartStart + (position - 1) * (long) columnWidth
                    : position * (long) columnWidth) - firstVisibleItem.getLeft() + rulerStartX;
        }
        return result;
    }

    public void initMoneyRuler(long min, double max, int scaleValue, String currency) {
        if (scaleValue == 0) {//兼容处理scaleValue传入0的情况
            scaleValue = 1;
        }
        this.scaleValue = scaleValue;
        if (scaleValue < 10) {
            //最大值12位，最大值除以1000为9位，在int范围内。
            //TODO  如果最大值13位，这里就得是10000了
            pageValue = 1000 * scaleValue;
        } else if (scaleValue < 100) {
            pageValue = 100 * scaleValue;
        } else {
            pageValue = 10 * scaleValue;
        }
        moneyMax = max;
        moneyMin = min;
        //2016/11/16  min和max与刻度间距比较，如果不是整数倍，就取比min小一点的刻度整数倍为showmin,取比max大一点的刻度整数倍为showmax
        showMin = min % scaleValue == 0 ? min : min / scaleValue * scaleValue;
        showMax = max % scaleValue == 0 ? (long) max : (long) (max / scaleValue + 1) * scaleValue;
        //一个横向滑动的recycleView，每个itemview使一个rulerview,10个小刻度画一个大刻度，大刻度上画对应的大刻度值。
        // 问题在于大刻度可能不是rulerview的边界，比如大刻度MaxBig离右边界只有一个小刻度，而MaxBig还没有画完，就切换到下一个rulerview上了，
        // 导致MaxBig永远没有机会画完，所以显示的效果就是在rulerview边界上，MaxBig被截断了。
        //要解决这个问题，就要保证大刻度始终在rulerview边界线上。
        //TODO 因此，刻度尺划分段落时要分为三个阶段——起始阶段（起始值到比min大的第一个大刻度，这个起始阶段rulerview可能不是整大刻度的长度）、
        //TODO  普通阶段（都是整大刻度的长度）、结尾阶段（从比max小的最近的大刻度到max,可能不是整大刻度的长度）。
        normalMin = min % pageValue == 0 ? min : (min / pageValue + 1) * pageValue;
        normalMax = max % pageValue == 0 ? (long) max : (long) (max / pageValue) * pageValue;
        long partStart = normalMin - showMin;
        long partNormal = normalMax - normalMin;
        long partEnd = showMax - normalMax;
        columnNum = (int) (partNormal / pageValue);
        if (hasPartStart = partStart != 0) {
            columnNum++;
            columnWidthPartStart = (int) (partStart / scaleValue * RulerView.SCALE_WIDTH);
        }
        if (hasPartEnd = partEnd != 0) {
            columnNum++;
            columnWidthPartEnd = (int) (partEnd / scaleValue * RulerView.SCALE_WIDTH);
        }
        columnWidth = (int) ((pageValue / scaleValue) * RulerView.SCALE_WIDTH);
        columnHeight = getContext().getResources().getDisplayMetrics().densityDpi * 140;
        rulerStartX = getContext().getResources().getDisplayMetrics().widthPixels / 2;

        adapter = new RulerWidgetAdapter(getContext());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        inputMoney.setCurrency(currency);
        inputMoney.setMaxLeftNumber(12);
        inputMoney.setInputMoney(moneyMin + "");
    }

    public void initMoneyRuler(long min, int scaleValue, String currency) {
        initMoneyRuler(min,
                MoneyUtils.isCurrencyCodeNoPoint(currency) ? MAX_VALUE_NOPOINT : MAX_VALUE,
                scaleValue, currency);
    }

    private void updateMoney() {
        long money1 = (getScrollerX() / RulerView.SCALE_WIDTH) * scaleValue;
        //long money2 = (getScrollerX() % RulerView.SCALE_WIDTH) * scaleValue / RulerView.SCALE_WIDTH;
        long money = money1 + showMin;
        if (money > moneyMax) {
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setGroupingUsed(false);
            String moneyMaxFormat = numberFormat.format(moneyMax);
            inputMoney.setInputMoney(moneyMaxFormat);
            if (listener != null) {
                listener.onMoneyRulerScrollered(new BigDecimal(moneyMaxFormat));
            }
        } else {
            if (money < moneyMin) {
                money = moneyMin;
            }
            inputMoney.setInputMoney(money + "");
            if (listener != null) {
                listener.onMoneyRulerScrollered(new BigDecimal(money));
            }
        }
        if ((isScrolledToStart) && !TextUtils.isEmpty(minTip)) {
            showTip(minTip);
        } else if (money >= moneyMax && !TextUtils.isEmpty(maxTip)) {
            showTip(maxTip);
        } else {
            dismissTip();
        }
    }

    private void showTip(String tip) {
        moneyLabelTip.setText(tip);
        moneyLabelTip.setVisibility(View.VISIBLE);
        Observable.timer(2, TimeUnit.SECONDS)
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Action1<Long>() {
                      @Override
                      public void call(Long aLong) {
                          moneyLabelTip.setVisibility(GONE);
                      }
                  });
    }

    private void dismissTip() {
        moneyLabelTip.setVisibility(View.GONE);
    }

    public void setInitMoney(String moneyInit) {
        this.moneyInit = moneyInit;
        setCurrentMoney(moneyInit);
    }

    public void setCurrentMoney(String moneyString) {
        if (!TextUtils.isEmpty(moneyString)) {
            double money = Double.parseDouble(moneyString);
            if (money > moneyMax) {
                money = moneyMax;
            }
            int position, offset;
            //如果金额等于最小值，说明达到左边界
            if (money == moneyMin) {
                position = 0;
                offset = 0;
            } else if (money == moneyMax) {  //钱等于最大值，说明达到右边界
                position = columnNum;
                offset = 0;
            } else {
                if (hasPartStart) {//如果有开始阶段rulerview的话
                    if (money < normalMin) {//判断如果输入金额在开始阶段里
                        position = 0;
                        offset = (int) calWidthInRuler(money, showMin);
                    } else {//如果输入金额不在开始阶段里
                        position = (int) ((money - normalMin) / pageValue + 1);
                        offset = (int) (calWidthInRuler(money, normalMin) % columnWidth);
                    }
                } else {//如果没有开始阶段
                    position = (int) ((money - showMin) / pageValue);
                    offset = (int) (calWidthInRuler(money, showMin) % columnWidth);
                }
            }

            if (position == columnNum) {
                linearLayoutManager.scrollToPositionWithOffset(columnNum - 1, -columnWidth);
            } else if (position == 0) {
                linearLayoutManager.scrollToPositionWithOffset(0, -offset);
            } else {
                linearLayoutManager.scrollToPositionWithOffset(position, rulerStartX - offset);
            }
            if (listener != null) {
                listener.onMoneyRulerScrollered(new BigDecimal(money));
            }
            inputMoney.setInputMoney(moneyString);
        }
    }

    private long calWidthInRuler(double money, long start) {
        return (long) ((money - start) / scaleValue * RulerView.SCALE_WIDTH);
    }

    public void setMaxTip(String maxTip) {
        this.maxTip = maxTip;
    }

    public void setMinTip(String minTip) {
        this.minTip = minTip;
    }

    public void setMoneyLabel(String label) {
        moneyLabel.setText(label);
    }

    public double getInputMoney() {
        if (StringUtils.isEmptyOrNull(inputMoney.getInputMoney())) {
            return 0;
        }
        return Double.parseDouble(inputMoney.getInputMoney());
    }

    public BigDecimal getMoney() {
        if (StringUtils.isEmptyOrNull(inputMoney.getInputMoney())) {
            return new BigDecimal(0.00);
        }

        return new BigDecimal(inputMoney.getInputMoney());
    }

    @Override
    public void onKeyBoardDismiss() {
        if (TextUtils.isEmpty(inputMoney.getInputMoney())) {
            showTip(getResources().getString(R.string.boc_purchase_product_amount_hint_empty));
            setCurrentMoney(moneyInit);
        } else {
            long money = (long) Double.parseDouble(inputMoney.getInputMoney());
            if (money < moneyMin) {
                setCurrentMoney(moneyMin + "");
                if (!TextUtils.isEmpty(minTip)) {
                    showTip(minTip);
                }
            } else if (money > moneyMax) {
                setCurrentMoney(moneyMax + "");
                if (!TextUtils.isEmpty(maxTip)) {
                    showTip(maxTip);
                }
            } else {
                setCurrentMoney(inputMoney.getInputMoney());
                dismissTip();
            }
        }
        if (keyboardListener != null) {
            keyboardListener.onKeyBoardDismiss();
        }
    }

    @Override
    public void onKeyBoardShow() {
        if (keyboardListener != null) {
            keyboardListener.onKeyBoardShow();
        }
    }

    @Override
    public void onItemClick(View itemView, int position) {
        inputMoney.onClick(inputMoney);
    }

    class RulerWidgetAdapter extends
            BaseRecycleViewAdapter<RulerWidgetAdapter.DataHolder, RulerWidgetAdapter.MyViewHolder> {

        public RulerWidgetAdapter(Context context) {
            super(context);
        }

        @Override
        public int getItemCount() {
            return columnNum;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = View.inflate(getContext(), R.layout.boc_item_money_ruler, null);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            int width;
            long rulerMoneyMin = i == 0 ? showMin
                    : hasPartStart ? (i - 1) * pageValue + normalMin : i * pageValue + showMin;
            long rulerMoneyMax = i == columnNum - 1 ? showMax
                    : i == 0 && hasPartStart ? normalMin : rulerMoneyMin + pageValue;
            RulerView.RulerStyle rulerStyle;
            if (columnNum == 1) {
                rulerStyle = RulerView.RulerStyle.BOTH;
                if (hasPartStart) {
                    width = columnWidthPartStart + rulerStartX * 2;
                } else if (hasPartEnd) {
                    width = columnWidthPartEnd + rulerStartX * 2;
                } else {
                    width = columnWidth + rulerStartX * 2;
                }
            } else if (i == 0) {
                rulerStyle = RulerView.RulerStyle.START;
                width = (hasPartStart ? columnWidthPartStart : columnWidth) + rulerStartX;
            } else if (i == (columnNum - 1)) {
                rulerStyle = RulerView.RulerStyle.END;
                width = (hasPartEnd ? columnWidthPartEnd : columnWidth) + rulerStartX;
            } else {
                rulerStyle = RulerView.RulerStyle.NORMAL;
                width = columnWidth;
            }
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(width, columnHeight);
            myViewHolder.itemView.setLayoutParams(params);
            myViewHolder.ruler.setScaleValue(scaleValue)
                              .setRulerMoneyMin(rulerMoneyMin)
                              .setRulerMoneyMax(rulerMoneyMax)
                              .setRealMoneyMin(moneyMin)
                              .setRealMoneyMax(moneyMax)
                              .setRulerStyle(rulerStyle)
                              .update();
            //myViewHolder.ruler.setMoneyStart(moneyMin);
            //myViewHolder.ruler.setMoneyEnd(moneyMax);
        }

        class MyViewHolder extends BaseRecycleViewAdapter.BaseViewHolder {
            RulerView ruler;
            View itemView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                ruler = (RulerView) itemView.findViewById(R.id.money_ruler);
            }
        }

        class DataHolder {
            long min;
            long max;
        }
    }

    public MoneyInputTextView getMoneyInputTextView() {
        return inputMoney;
    }

    /**
     * 设置键盘消失监听
     */
    public void setOnKeyBoardDismiss(MoneyInputDialog.KeyBoardListener listener) {
        keyboardListener = listener;
    }
}
