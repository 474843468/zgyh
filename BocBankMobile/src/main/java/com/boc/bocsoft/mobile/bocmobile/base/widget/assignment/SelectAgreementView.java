package com.boc.bocsoft.mobile.bocmobile.base.widget.assignment;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：XieDu
 * 创建时间：2016/9/1 17:18
 * 描述：
 */
public class SelectAgreementView extends LinearLayout implements View.OnClickListener {
    private ImageButton btnCheckbox;
    private TextView tvAgreement;

    private OnClickContractListener mOnClickContractListener;

    public SelectAgreementView(Context context) {
        this(context, null);
    }

    public SelectAgreementView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectAgreementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View rootView = inflate(getContext(), R.layout.boc_view_agreement, this);
        btnCheckbox = (ImageButton) rootView.findViewById(R.id.btn_checkbox);
        btnCheckbox.setOnClickListener(SelectAgreementView.this);
        tvAgreement = (TextView) rootView.findViewById(R.id.tv_agreement);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_checkbox) {
            btnCheckbox.setSelected(!btnCheckbox.isSelected());
        }
    }

    public boolean isSelected() {
        return btnCheckbox.isSelected();
    }

    @Override
    public void setSelected(boolean selected) {
        btnCheckbox.setSelected(selected);
    }

    /**
     * 设置须知内容
     *
     * @param contractName 合同名
     * @param agreementStart 合同名之前的须知内容
     * @param agreementEnd 合同名之后的须知内容
     */
    public void setAgreement(String contractName, String agreementStart, String agreementEnd) {
        MClickableSpan clickableSpanContract = new MClickableSpan();
        clickableSpanContract.setListener(new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {
                if (mOnClickContractListener != null) {
                    mOnClickContractListener.onClickContract(0);
                }
            }
        });
        clickableSpanContract.setString(contractName);
        clickableSpanContract.setColor(getResources().getColor(R.color.boc_text_color_red));
        SpannableString spannableStringContract = new SpannableString(contractName);
        spannableStringContract.setSpan(clickableSpanContract, 0, contractName.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(agreementStart);
        tvAgreement.append(spannableStringContract);
        tvAgreement.append(agreementEnd);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        tvAgreement.setLongClickable(false);
    }

    /**
     * 设置须知内容(只设置合同名字，前后有默认文字)
     *
     * @param contractName 合同名
     */
    public void setContractName(String contractName) {
        setAgreement(contractName, getContext().getString(R.string.boc_agreement_part1),
                getContext().getString(R.string.boc_agreement_part2));
    }

    /**
     * 设置须知内容（其中合同名要有书名号）
     *
     * @param agreement 须知内容
     */
    public void setAgreement(String agreement) {

        Pattern pattern = Pattern.compile("\u300A(.+?)\u300B");
        Matcher matcher = pattern.matcher(agreement);
        List<Integer> indexList = new ArrayList<>();
        while (matcher.find()) {
            indexList.add(matcher.start());
            indexList.add(matcher.end());
        }
        int[] indexArray = new int[indexList.size()];
        for (int i = 0; i < indexList.size(); i++) {
            indexArray[i] = indexList.get(i);
        }
        setAgreement(agreement, indexArray);
    }

    /**
     * 设置须知内容
     *
     * @param agreement 须知内容
     * @param contractNameIndexArray 合同名的起始结束索引位置数组，从小到大排列
     */
    public void setAgreement(String agreement, int... contractNameIndexArray) {
        if (StringUtils.isEmpty(agreement) || contractNameIndexArray == null
                || contractNameIndexArray.length == 0) {
            return;
        }
        tvAgreement.setText("");
        int index = 0;
        while (index < contractNameIndexArray.length - 1) {
            //对于索引位置数组的第一个元素，它左边的文字要首先添加到textview里
            if (index == 0) {
                tvAgreement.append(agreement.substring(0, contractNameIndexArray[index]));
            }
            //现在index只是合同名的起始索引，++index是合同名的结束索引。
            String contractName = agreement.substring(contractNameIndexArray[index],
                    contractNameIndexArray[++index]);
            MClickableSpan clickableSpanContract = new MClickableSpan();
            final int finalIndex = index;
            clickableSpanContract.setListener(new MClickableSpan.OnClickSpanListener() {
                @Override
                public void onClickSpan() {
                    if (mOnClickContractListener != null) {
                        mOnClickContractListener.onClickContract(finalIndex >> 1);
                    }
                }
            });
            clickableSpanContract.setString(contractName);
            clickableSpanContract.setColor(getResources().getColor(R.color.boc_text_color_red));
            SpannableString spannableStringContract = new SpannableString(contractName);
            spannableStringContract.setSpan(clickableSpanContract, 0, contractName.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvAgreement.append(spannableStringContract);
            //现在index指向了结束索引
            //如果index是最后一位索引，则添加从它到agreement结束的文字。并跳出循环。
            //否则，添加该结束索引（index）到下一个起始索引（++index）之间的文字。
            String agreementCommon = agreement.substring(contractNameIndexArray[index],
                    index == contractNameIndexArray.length - 1 ? agreement.length()
                            : contractNameIndexArray[++index]);
            tvAgreement.append(agreementCommon);
        }
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        tvAgreement.setLongClickable(false);
    }

    public void setOnClickContractListener(OnClickContractListener onClickContractListener) {
        mOnClickContractListener = onClickContractListener;
    }

    public interface OnClickContractListener {
        void onClickContract(int index);
    }
}
