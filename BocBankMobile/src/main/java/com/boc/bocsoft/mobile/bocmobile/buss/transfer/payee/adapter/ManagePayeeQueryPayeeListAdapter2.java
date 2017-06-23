package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.BankLogoUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.stickylistheaders.StickyListHeadersAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils.getCardPic;

/**
 * 收款人列表适配器（切勿随意修改）
 * Created by zhx on 2016/8/30
 */
public class ManagePayeeQueryPayeeListAdapter2 extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {
    private Context context;
    private String filterWord; // 输入框中填入的过滤文字(zhx_2016-09-02_注释)
    private List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> payeeEntityList; // 全部的列表(zhx_2016-09-02_注释)
    private List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> payeeEntityFilterList = new ArrayList<PsnTransPayeeListqueryForDimViewModel.PayeeEntity>(); // 经过过滤后的列表(zhx_2016-09-02_注释)
    private int[] mSectionIndices;
    private Character[] mSectionLetters;

//    private char  linkedAccountFirstWord;//关联账户的姓名首字母

    public ManagePayeeQueryPayeeListAdapter2(Context context, List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> payeeEntityList) {
        this.context = context;
        this.payeeEntityList = payeeEntityList;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    private int[] getSectionIndices() {
        if (payeeEntityList != null && payeeEntityList.size() > 0) {
            if (TextUtils.isEmpty(filterWord)) { // 没有过滤的情况下
                ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
                char lastFirstChar = payeeEntityList.get(0).getPinyin().charAt(0);
                sectionIndices.add(0);
                for (int i = 1; i < payeeEntityList.size(); i++) {
                    if (payeeEntityList.get(i).getPinyin().charAt(0) != lastFirstChar) {
                        lastFirstChar = payeeEntityList.get(i).getPinyin().charAt(0);
                        sectionIndices.add(i);
                    }
                    //wangyuan 2016-9-2添加，处理置顶的关联账户和第一个非关联账户首字母一样的情况
                    else{
                        if (payeeEntityList.get(i-1).isLinked()&&!payeeEntityList.get(i).isLinked()){
                            lastFirstChar = payeeEntityList.get(i).getPinyin().charAt(0);
                            sectionIndices.add(i);
                        }
                    }
                    //wangyuan 2016-9-2添加，处理置顶的关联账户和第一个非关联账户首字母一样的情况
                }
                int[] sections = new int[sectionIndices.size()];
                for (int i = 0; i < sectionIndices.size(); i++) {
                    sections[i] = sectionIndices.get(i);
                }
                return sections;
            } else { // 过滤的情况下
                ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
                char lastFirstChar = payeeEntityFilterList.get(0).getPinyin().charAt(0);
                sectionIndices.add(0);
                for (int i = 1; i < payeeEntityFilterList.size(); i++) {
                    if (payeeEntityFilterList.get(i).getPinyin().charAt(0) != lastFirstChar) {
                        lastFirstChar = payeeEntityFilterList.get(i).getPinyin().charAt(0);
                        sectionIndices.add(i);
                    }
                    //wangyuan 2016-9-2添加，处理置顶的关联账户和第一个非关联账户首字母一样的情况
                    else{
                        if (payeeEntityFilterList.get(i-1).isLinked()&&!payeeEntityFilterList.get(i).isLinked()){
                            lastFirstChar = payeeEntityFilterList.get(i).getPinyin().charAt(0);
                            sectionIndices.add(i);
                        }
                    }
                    //wangyuan 2016-9-2添加，处理置顶的关联账户和第一个非关联账户首字母一样的情况
                }
                int[] sections = new int[sectionIndices.size()];
                for (int i = 0; i < sectionIndices.size(); i++) {
                    sections[i] = sectionIndices.get(i);
                }
                return sections;
            }
        }

        return null;
    }

    private Character[] getSectionLetters() {
        if (payeeEntityList != null && payeeEntityList.size() > 0) {
            if (TextUtils.isEmpty(filterWord)) { // 没有过滤的情况下
                Character[] letters = new Character[mSectionIndices.length];
                for (int i = 0; i < mSectionIndices.length; i++) {
                    char c = payeeEntityList.get(mSectionIndices[i]).getPinyin().charAt(0);
                    //王园修改 2016-9-2 ，添加判断是否是关联账户
                    if(!payeeEntityList.get(mSectionIndices[i]).isLinked()){
                        if (c == 'z') {
                            letters[i] = '#';
                        } else {
                            letters[i] = c;
                        }
                    }else{
                        letters[i]='我';
                    }
                    //王园修改 2016-9-2 ，
                }
                return letters;
            } else {
                Character[] letters = new Character[mSectionIndices.length];
                for (int i = 0; i < mSectionIndices.length; i++) {
                    char c = payeeEntityFilterList.get(mSectionIndices[i]).getPinyin().charAt(0);
                    //王园修改 2016-9-2 ，
                    if(!payeeEntityFilterList.get(mSectionIndices[i]).isLinked()){
                        if (c == 'z') {
                            letters[i] = '#';
                        } else {
                            letters[i] = c;
                        }
                    }else{
                        letters[i]='我';
                    }
                    //王园修改 2016-9-2 ，
                }
                return letters;
            }
        }

        return null;
    }

    public void setFilterWord(String filterWord) {
        this.filterWord = filterWord;
        notifyDataSetChanged();
    }

    public void setPayeeEntityList(List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> payeeEntityList) {
        this.payeeEntityList = payeeEntityList;
    }

    public List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> getPayeeEntityFilterList() {
        return payeeEntityFilterList;
    }

    @Override
    public int getCount() {
        if (payeeEntityList != null && payeeEntityList.size() > 0) {
            if (!TextUtils.isEmpty(filterWord)) {
                payeeEntityFilterList.clear();
                for (PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity : payeeEntityList) {
                    try {
                        // 如果名称或者名称的拼音包含过滤文字，那么，添加到payeeEntityFilterList中
                        //2016-10-11 王园添加 ***********************
                        if (payeeEntity.isLinked()){
                            if(payeeEntity.getNickName().contains(filterWord)||payeeEntity.getNickNamepinyin().contains(filterWord.toUpperCase())){ // 2016-10-12 zhx此处应该新增一个NickNamePinyin拼音字段,并且该字段参与搜索，请王园修改
                                payeeEntityFilterList.add(payeeEntity);
                            }
                        }
                        //2016-10-11 王园添加*************************
                        else{
                            if (payeeEntity.getAccountName().contains(filterWord) || payeeEntity.getPinyin().contains(filterWord.toUpperCase())) {
                                payeeEntityFilterList.add(payeeEntity);
                            }
                        }
                    } catch (Exception e) {
                        // payeeEntity.getPinyin()可能为null，所以加try...catch...语句块
                    }
                }
                return payeeEntityFilterList.size();
            }
            return payeeEntityList.size();
        }
        return 0;
    }

    @Override
    public PsnTransPayeeListqueryForDimViewModel.PayeeEntity getItem(int position) {
        PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity;
        if (!TextUtils.isEmpty(filterWord)) {
            payeeEntity = payeeEntityFilterList.get(position);
        } else {
            payeeEntity = payeeEntityList.get(position);
        }
        return payeeEntity;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.list_item_payee_section_content, null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);
        PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity = getItem(position); // 界面刚才显示有问题，是因为这里取PayeeEntity出错，改为getItem(position)正确
        if (payeeEntity.isLinked()){
            holder.name.setText(payeeEntity.getNickName());
            Picasso.with(context).load(getCardPic(AccountUtils.getCardType(payeeEntity.getType()))).into(holder.iv_bank_logo);
        }else{
            holder.name.setText(payeeEntity.getAccountName()); // 收款人帐户名称
        }

        holder.tv_account_number.setText(NumberUtils.formatCardNumberStrong(payeeEntity.getAccountNumber())); // 收款人银行帐号

        holder.tv_bank_name.setText(payeeEntity.getBankName()); // 收款人银行名称
        holder.view_separate_line.setVisibility(TextUtils.isEmpty(payeeEntity.getBankName()) ? View.GONE : View.VISIBLE);
        holder.tv_ding_xiang.setVisibility("1".equals(payeeEntity.getIsAppointed()) ? View.VISIBLE : View.GONE); // 设置“定向”的显示与否
        holder.tv_shi_shi.setVisibility("3".equals(payeeEntity.getBocFlag()) ? View.VISIBLE : View.GONE); // 设置“实时”的显示与否

        if (!payeeEntity.isLinked()){
            Integer resId = null;
            if (payeeEntity.getBankName() != null && payeeEntity.getBankName().contains("中国银行")) {
                resId = BankLogoUtil.getLogoResByBankName1("中国银行");
            } else {
                resId = BankLogoUtil.getLogoResByBankName1(payeeEntity.getBankName());
            }
            if (null != payeeEntity.getBocFlag() && payeeEntity.getBocFlag().equals("1")) {
                resId = BankLogoUtil.getLogoResByBankName1("中国银行");
            }

            if (resId != null) {
                holder.iv_bank_logo.setImageResource(resId);
            } else {
                holder.iv_bank_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.other_bank_logo));
            }
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = View.inflate(context, R.layout.list_item_payee_section_letter, null);
            holder.letter = (TextView) convertView.findViewById(R.id.letter);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        // set header text as first char in name
        char headerChar = getItem(position).getPinyin().charAt(0);
        if (headerChar != 'z') {//wangyuan修改，2016-9-2
            if (!getItem(position).isLinked()){
                holder.letter.setText(headerChar + ""); // 这里要加""，不加会报错
            }else{
//                linkedAccountFirstWord=headerChar;
                holder.letter.setText("我");//
            }
        } else {
            holder.letter.setText("#");
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getPinyin().subSequence(0, 1).charAt(0);
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    public void clear() {
        payeeEntityList.clear(); // 清空
        mSectionIndices = new int[0];
        mSectionLetters = new Character[0];
        notifyDataSetChanged();
    }

    public void restore(List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> payeeEntityList) {
        this.payeeEntityList = payeeEntityList;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView name, tv_account_number, tv_bank_name, tv_ding_xiang, tv_shi_shi;
        ImageView iv_bank_logo;
        FrameLayout fl_letter;
        View view_separate_line;

        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.name);
            tv_account_number = (TextView) convertView.findViewById(R.id.tv_account_number);
            tv_bank_name = (TextView) convertView.findViewById(R.id.tv_bank_name);
            tv_ding_xiang = (TextView) convertView.findViewById(R.id.tv_ding_xiang);
            tv_shi_shi = (TextView) convertView.findViewById(R.id.tv_shi_shi);
            iv_bank_logo = (ImageView) convertView.findViewById(R.id.iv_bank_logo);
            fl_letter = (FrameLayout) convertView.findViewById(R.id.fl_letter); // 包裹“字母”组件的布局
            view_separate_line = convertView.findViewById(R.id.view_separate_line); // 银行号码和银行名称中间的分割线
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }

    class HeaderViewHolder {
        TextView letter;
    }
}