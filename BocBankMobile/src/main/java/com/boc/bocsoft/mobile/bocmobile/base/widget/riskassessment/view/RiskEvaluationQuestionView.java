package com.boc.bocsoft.mobile.bocmobile.base.widget.riskassessment.view;


/**
 * Created by taoyongzhen on 2016/11/26.
 * 风险评估答题
 * 该view需要和题目对象questions组合使用
 * 基本实现思路：根据json文件动态确定题目、分值；将选项作为问题本身的属性，保存在题目内部，view只是更加题目属性
 * 进行展示。
 *
 */

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.riskassessment.bean.FundQuestions;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class RiskEvaluationQuestionView extends LinearLayout {

    private View rootView;
    private Context mContext;
    private TextView titleView;
    private TableLayout tableLayout;//绘制题目view的容器
    private TextView textUp;//上一题
    private TextView textNum;//题目数
    private FinishedQuestionsListener finishedQuestionsListener;//答题完成的回调
    private FundQuestions fundQuestions;
    private int questionIndex = 0;
    private final static int DELAY_TIME = 300;//延迟300ms，使得按钮出现点击效果
    private String DEFAULT_QUESTION_FILE_PATH = "risk/question.json";
    private Handler handler = new Handler();

    public RiskEvaluationQuestionView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public RiskEvaluationQuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public RiskEvaluationQuestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_view_riskevaluation_question, this);
        titleView = (TextView) rootView.findViewById(R.id.title);
        tableLayout = (TableLayout) rootView.findViewById(R.id.tableLayout);
        textUp = (TextView) rootView.findViewById(R.id.tv_up);
        textNum = (TextView) rootView.findViewById(R.id.tv_num);
        textUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                questionIndex--;
                initView();
            }
        });
    }

    /**
     * 问题文件路径
     * @param questionPath
     */
    public void initQuestions(String questionPath){
        String json = "";
        InputStream inputStream = null;
        try {
            inputStream = mContext.getAssets().open(questionPath);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            inputStream.close();
            json = new String(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Gson gson = new Gson();
            FundQuestions jsonBean = gson.fromJson(json, FundQuestions.class);
            setFundQuestions(jsonBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //缺省文件位于资源文件下"risk/question.json"
    public void initQuestions(){
        initQuestions(DEFAULT_QUESTION_FILE_PATH);
    }


    private void initView() {
        initQuestionView();
        initTextNum();
        initTextUp();
    }

    /**
     * 初始化题目索引
     */
    private void initTextNum() {
        String result = (questionIndex + 1) + "/" + fundQuestions.getList().size();
        textNum.setText(result);
    }

    private void initTextUp() {
        if (questionIndex == 0) {
            textUp.setVisibility(View.GONE);
        } else {
            textUp.setVisibility(VISIBLE);
        }

    }

    private void initQuestionView() {
        initTitleView();
        initContentView();
    }

    /**
     * 初始化题干
     */
    private void initTitleView() {
        titleView.setText(fundQuestions.getQuestionTitle(questionIndex));
    }

    /**
     * 初始化题支
     * tableLayout：对应题目所有题支的容器
     * TableRow：对应题目的题支，包括：CheckBox button;//标记选中题支对应的checkbox；TextView textView;//标记选中题支对应的textview
     */
    private void initContentView() {
        tableLayout.removeAllViews();
        final FundQuestions.Question question = fundQuestions.getQuestion(questionIndex);
        if (question == null || question.getOptionhead().size() <= 0) {
            return;
        }
        for (int i = 0; i < question.getOptionhead().size(); i++) {
            TableRow tableRow = new TableRow(tableLayout.getContext());
            tableRow.setClickable(true);
            tableRow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    refrshTableLayout(question,v);
                    questionIndex++;
                    handler.removeCallbacksAndMessages(null);
                    if (questionIndex >= fundQuestions.getList().size()) {
                        questionIndex--;
                        if (finishedQuestionsListener != null) {

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    String totalScore = String.valueOf(fundQuestions.getTotalScore());
                                    String selectedOptions = fundQuestions.getSelectedOptions();
                                    finishedQuestionsListener.finshedQuestion(totalScore,selectedOptions);
                                }
                            },DELAY_TIME);
                        }
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initView();
                            }
                        },DELAY_TIME);
                    }

                }
            });
            tableRow.setPadding(0,
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_30px),
                    0,
                    mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px));
            CheckBox checkBox = addCheckBox(tableRow, question.getSelectedIndex(), i);
            TextView textView = addOption(tableRow, question.getSelectedIndex(),question.getOptioncontent().get(i),i);
            tableRow.setTag(new ViewHolder(i,checkBox,textView));
            tableLayout.addView(tableRow);
        }

    }

    public interface FinishedQuestionsListener {
        void finshedQuestion(String score, String riskAnswer);
    }

    public void setFinishedQuestionsListener(FinishedQuestionsListener finishedQuestionsListener) {
        this.finishedQuestionsListener = finishedQuestionsListener;
    }

    public FundQuestions getFundQuestions() {
        return fundQuestions;
    }

    /**
     * 为答题页面绑定数据
     * @param fundQuestions
     */
    public void setFundQuestions(FundQuestions fundQuestions) {
        this.fundQuestions = fundQuestions;
        initView();
    }

    private CheckBox addCheckBox(TableRow tableRow, int selectedIndex, int index) {
        CheckBox button = new CheckBox(tableRow.getContext());
        button.setClickable(false);
        button.setTextAppearance(tableRow.getContext(),R.style.RiskCheckboxTheme);
        button.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.bg_radio_group_selector));
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_VERTICAL;
        button.setLayoutParams(lp);
        tableRow.addView(button);
        if (selectedIndex == index) {
            button.setSelected(true);
            button.setChecked(true);
        }
        return button;

    }

    private TextView addOption(TableRow tableRow,int selectedIndex,String content,int index) {
        TextView textView = new TextView(tableRow.getContext());
        textView.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.boc_space_between_20px), 0, 0, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.boc_space_between_26px));
        textView.setText(content);
        textView.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_common_gray));
        if (index == selectedIndex){
            textView.getPaint().setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        tableRow.addView(textView);
        return textView;

    }

    static class ViewHolder {
        int index;//标记选中题支
        CheckBox button;//标记选中题支对应的checkbox
        TextView textView;//标记选中题支对应的textview

        public ViewHolder(int index, CheckBox button,TextView textView) {
            this.index = index;
            this.button = button;
            this.textView = textView;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    private void refrshTableLayout(FundQuestions.Question question, View view){
        for (int i=0;i<tableLayout.getChildCount();i++){
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            tableRow.setClickable(false);
            if (tableRow.getTag() != null ){
                ViewHolder viewHolder = (ViewHolder) tableRow.getTag();
                ViewHolder viewHolderSelected = (ViewHolder) view.getTag();
                if (viewHolderSelected != null && i == viewHolderSelected.index){
                    question.setSelectedIndex(viewHolder.index);
                    viewHolder.textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    viewHolder.button.setChecked(true);
                    viewHolder.button.setSelected(true);
                }else{
                    viewHolder.textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    viewHolder.button.setChecked(false);
                    viewHolder.button.setSelected(false);
                }
            }

        }
    }
}
