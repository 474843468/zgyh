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
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.boc.bocsoft.mobile.bocmobile.base.widget.moneyruler.ExactBoundaryRulerView.SCALE_WIDTH;

/**
 * 作者：XieDu
 * 创建时间：2016/12/28 9:20
 * 描述：精确边界金额刻度尺
 */
public class ExactBoundaryMoneyRulerWidget extends LinearLayout
        implements MoneyInputDialog.KeyBoardListener, OnItemClickListener {

    private String mCurrencyCode;
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
    private double moneyMin;
    private double moneyMax;
    private String moneyInit;
    private int rulerStartX;//尺子的第一个刻度位于屏幕中间
    private long pageValue;
    private int startPartWidthBetweenShowAndReal;//showmin和realMin之间距离
    private long showMin, showMax;//展示的最小值和最大值刻度
    private long normalMin, normalMax;
    //按照10倍的scaleValue为一个大刻度间距，开头和结尾可能不是完整的大刻度间距，normalMin和normalMax为去除开头和结尾后的中间部分的两端
    private boolean hasPartStart, hasPartEnd;
    private String maxTip;
    private String minTip;

    private boolean isScrolledToStart, isScrolledToEnd;

    private MoneyRulerScrollerListener listener;

    private MoneyInputDialog.KeyBoardListener keyboardListener;

    public interface MoneyRulerScrollerListener {
        public void onMoneyRulerScrollered(BigDecimal money);
    }

    public ExactBoundaryMoneyRulerWidget(Context context) {
        super(context);
        initView();
    }

    public ExactBoundaryMoneyRulerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ExactBoundaryMoneyRulerWidget(Context context, AttributeSet attrs, int defStyleAttr) {
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
        } else {
            result = (hasPartStart ? columnWidthPartStart + (position - 1) * (long) columnWidth
                    : position * (long) columnWidth) - firstVisibleItem.getLeft() + rulerStartX;
        }
        isScrolledToStart = linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
        isScrolledToEnd = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                == linearLayoutManager.getItemCount() - 1;
        return result;
    }

    /**
     * 初始化刻度尺参数
     *
     * @param min 最小值
     * @param max 最大值
     * @param scaleValue 刻度间隔
     * @param currency 币种
     */
    public void initMoneyRuler(double min, double max, int scaleValue, String currency) {
        mCurrencyCode = currency;
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
        showMin = min % scaleValue == 0 ? (long) min : (long) min / scaleValue * scaleValue;
        showMax = max % scaleValue == 0 ? (long) max : (long) (max / scaleValue + 1) * scaleValue;
        //一个横向滑动的recycleView，每个itemview使一个rulerview,10个小刻度画一个大刻度，大刻度上画对应的大刻度值。
        // 问题在于大刻度可能不是rulerview的边界，比如大刻度MaxBig离右边界只有一个小刻度，而MaxBig还没有画完，就切换到下一个rulerview上了，
        // 导致MaxBig永远没有机会画完，所以显示的效果就是在rulerview边界上，MaxBig被截断了。
        //要解决这个问题，就要保证大刻度始终在rulerview边界线上。
        //TODO 因此，刻度尺划分段落时要分为三个阶段——起始阶段（起始值到比min大的第一个大刻度，这个起始阶段rulerview可能不是整大刻度的长度）、
        //TODO  普通阶段（都是整大刻度的长度）、结尾阶段（从比max小的最近的大刻度到max,可能不是整大刻度的长度）。
        normalMin = min % pageValue == 0 ? (long) min : ((long) min / pageValue + 1) * pageValue;
        normalMax = max % pageValue == 0 ? (long) max : (long) (max / pageValue) * pageValue;
        double partStart = normalMin - moneyMin;
        double partNormal = normalMax - normalMin;
        double partEnd = moneyMax - normalMax;
        columnNum = (int) (partNormal / pageValue);
        if (hasPartStart = partStart != 0) {
            columnNum++;
            columnWidthPartStart = (int) (partStart / scaleValue * SCALE_WIDTH);
        }
        if (hasPartEnd = partEnd != 0) {
            columnNum++;
            columnWidthPartEnd = (int) (partEnd / scaleValue * SCALE_WIDTH);
        }
        columnWidth = (int) ((pageValue / scaleValue) * SCALE_WIDTH);
        columnHeight = getContext().getResources().getDisplayMetrics().densityDpi * 140;
        rulerStartX = getContext().getResources().getDisplayMetrics().widthPixels / 2;
        startPartWidthBetweenShowAndReal =
                showMin < moneyMin ? (int) ((moneyMin - showMin) * SCALE_WIDTH / scaleValue) : 0;
        if (min == max) {
            columnWidth = 0;
            columnNum = 1;
        }
        adapter = new RulerWidgetAdapter(getContext());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        inputMoney.setCurrency(currency);
        inputMoney.setMaxLeftNumber(12);
        inputMoney.setInputMoney(moneyMin + "");
    }

    /**
     * 初始化刻度尺参数，最大值默认为12.2位或者12位（取决于币种）
     *
     * @param min 最小值
     * @param scaleValue 刻度间隔
     * @param currency 币种
     */
    public void initMoneyRuler(double min, int scaleValue, String currency) {
        initMoneyRuler(min,
                MoneyUtils.isCurrencyCodeNoPoint(currency) ? MAX_VALUE_NOPOINT : MAX_VALUE,
                scaleValue, currency);
    }

    private void updateMoney() {
        long money1 =
                (getScrollerX() + startPartWidthBetweenShowAndReal) / SCALE_WIDTH * scaleValue;
        double money = money1 + showMin;
        //如果showMin是真实moneyMin左边一点的刻度，即showMin<moneyMin<showMin+scaleValue
        //并且money也处于showMin和showMin+scaleValue之间，则将money设置为moneyMin
        if (showMin < moneyMin && money < showMin + scaleValue) {
            money = moneyMin;
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setRoundingMode(RoundingMode.HALF_UP);
        if (MoneyUtils.isCurrencyCodeNoPoint(mCurrencyCode)) {
            numberFormat.setMaximumFractionDigits(0);
        } else {
            numberFormat.setMaximumFractionDigits(2);
        }
        String inputMoneyString = numberFormat.format(money);
        //如果滑动到尾部，金额设置为moneyMax
        if (isScrolledToEnd) {
            inputMoneyString = numberFormat.format(moneyMax);
        }
        //如果滑动到头部，金额设置为moneyMin
        if (isScrolledToStart) {
            inputMoneyString = numberFormat.format(moneyMin);
        }
        inputMoney.setInputMoney(inputMoneyString);
        if (listener != null) {
            listener.onMoneyRulerScrollered(new BigDecimal(inputMoneyString));
        }
        if (isScrolledToStart && !TextUtils.isEmpty(minTip)) {
            showTip(minTip);
        } else if (isScrolledToEnd && !TextUtils.isEmpty(maxTip)) {
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

    /**
     * 设置初始化金额值，初始化时需要调用。
     */
    public void setInitMoney(String moneyInit) {
        this.moneyInit = moneyInit;
        setCurrentMoney(moneyInit);
    }

    /**
     * 设置当前金额值
     */
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
                        offset = (int) calWidthInRuler(money, moneyMin);
                    } else {//如果输入金额不在开始阶段里
                        position = (int) ((money - normalMin) / pageValue + 1);
                        offset = (int) (calWidthInRuler(money, normalMin) % columnWidth);
                    }
                } else {//如果没有开始阶段
                    position = (int) ((money - moneyMin) / pageValue);
                    offset = (int) (calWidthInRuler(money, moneyMin) % columnWidth);
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

    private long calWidthInRuler(double money, double start) {
        return (long) ((money - start) / scaleValue * SCALE_WIDTH);
    }

    /**
     * 设置最大值边界提示
     */
    public void setMaxTip(String maxTip) {
        this.maxTip = maxTip;
    }

    /**
     * 设置最小值边界提示
     */
    public void setMinTip(String minTip) {
        this.minTip = minTip;
    }

    /**
     * 文字描述
     */
    public void setMoneyLabel(String label) {
        moneyLabel.setText(label);
    }

    /**
     * 获取金额
     */
    public double getInputMoney() {
        if (StringUtils.isEmptyOrNull(inputMoney.getInputMoney())) {
            return 0;
        }
        return Double.parseDouble(inputMoney.getInputMoney());
    }

    /**
     * *获取金额
     */
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
            double money = Double.parseDouble(inputMoney.getInputMoney());
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
            View itemView =
                    View.inflate(getContext(), R.layout.boc_item_money_ruler_exact_boundary, null);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            int width;
            long rulerMoneyMin = i == 0 ? showMin
                    : hasPartStart ? (i - 1) * pageValue + normalMin : i * pageValue + showMin;
            long rulerMoneyMax = i == columnNum - 1 ? showMax
                    : i == 0 && hasPartStart ? normalMin : rulerMoneyMin + pageValue;
            ExactBoundaryRulerView.RulerStyle rulerStyle;
            if (columnNum == 1) {
                rulerStyle = ExactBoundaryRulerView.RulerStyle.BOTH;
                if (hasPartStart) {
                    width = columnWidthPartStart + rulerStartX * 2;
                } else if (hasPartEnd) {
                    width = columnWidthPartEnd + rulerStartX * 2;
                } else {
                    width = columnWidth + rulerStartX * 2;
                }
            } else if (i == 0) {
                rulerStyle = ExactBoundaryRulerView.RulerStyle.START;
                width = (hasPartStart ? columnWidthPartStart : columnWidth) + rulerStartX;
            } else if (i == (columnNum - 1)) {
                rulerStyle = ExactBoundaryRulerView.RulerStyle.END;
                width = (hasPartEnd ? columnWidthPartEnd : columnWidth) + rulerStartX;
            } else {
                rulerStyle = ExactBoundaryRulerView.RulerStyle.NORMAL;
                width = columnWidth;
            }
            if (moneyMin == moneyMax) {
                rulerMoneyMax = rulerMoneyMin = (long) moneyMin;
                rulerStyle = ExactBoundaryRulerView.RulerStyle.BOTH;
                width = columnWidth + rulerStartX * 2;
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
            ExactBoundaryRulerView ruler;
            View itemView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                ruler = (ExactBoundaryRulerView) itemView.findViewById(R.id.money_ruler);
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
