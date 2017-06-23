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
import com.boc.bocsoft.mobile.bocmobile.base.utils.BankLogoUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.stickylistheaders.StickyListHeadersAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.BankEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 银行列表适配器（带悬浮效果）
 * Created by zhx on 2016/7/19
 */
public class BankAdapter1 extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer {
    private Context context;
    private String filterWord;
    private List<BankEntity> bankEntityList;
    private List<BankEntity> bankEntityFilterList = new ArrayList<BankEntity>();

    private int[] mSectionIndices;
    private Character[] mSectionLetters;

    public BankAdapter1(Context context, List<BankEntity> bankEntityList) {
        this.context = context;
        this.bankEntityList = bankEntityList;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    private int[] getSectionIndices() {
        if (bankEntityList != null && bankEntityList.size() > 0) {
            if (TextUtils.isEmpty(filterWord)) { // 没有过滤的情况下
                ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
                char lastFirstChar = bankEntityList.get(0).getBankNamePinyin().charAt(0);
                sectionIndices.add(0);
                for (int i = 1; i < bankEntityList.size(); i++) {
                    if (bankEntityList.get(i).getBankNamePinyin().charAt(0) != lastFirstChar) {
                        lastFirstChar = bankEntityList.get(i).getBankNamePinyin().charAt(0);
                        sectionIndices.add(i);
                    }
                }
                int[] sections = new int[sectionIndices.size()];
                for (int i = 0; i < sectionIndices.size(); i++) {
                    sections[i] = sectionIndices.get(i);
                }
                return sections;
            } else { // 过滤的情况下
                ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
                char lastFirstChar = bankEntityFilterList.get(0).getBankNamePinyin().charAt(0);
                sectionIndices.add(0);
                for (int i = 1; i < bankEntityFilterList.size(); i++) {
                    if (bankEntityFilterList.get(i).getBankNamePinyin().charAt(0) != lastFirstChar) {
                        lastFirstChar = bankEntityFilterList.get(i).getBankNamePinyin().charAt(0);
                        sectionIndices.add(i);
                    }
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
        if (bankEntityList != null && bankEntityList.size() > 0) {
            if (TextUtils.isEmpty(filterWord)) { // 没有过滤的情况下
                Character[] letters = new Character[mSectionIndices.length];
                for (int i = 0; i < mSectionIndices.length; i++) {
                    char c = bankEntityList.get(mSectionIndices[i]).getBankNamePinyin().charAt(0);
                    if (c == 'z') {
                        letters[i] = '#';
                    } else {
                        letters[i] = c;
                    }
                }
                return letters;
            } else {
                Character[] letters = new Character[mSectionIndices.length];
                for (int i = 0; i < mSectionIndices.length; i++) {
                    char c = bankEntityFilterList.get(mSectionIndices[i]).getBankNamePinyin().charAt(0);
                    if (c == 'z') {
                        letters[i] = '#';
                    } else {
                        letters[i] = c;
                    }
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

    public List<BankEntity> getBankEntityFilterList() {
        return bankEntityFilterList;
    }

    public void setBankEntityFilterList(List<BankEntity> bankEntityFilterList) {
        this.bankEntityFilterList = bankEntityFilterList;
    }

    @Override
    public int getCount() {
        if (bankEntityList != null && bankEntityList.size() > 0) {
            if (!TextUtils.isEmpty(filterWord)) {
                bankEntityFilterList.clear();

                for (BankEntity payeeEntity : bankEntityList) {
                    try {
                        if (payeeEntity.isHot()) {
                            // 如果名称或者名称的拼音包含过滤文字，那么，添加到bankEntityFilterList中
                            if (payeeEntity.getBankName().contains(filterWord.toUpperCase()) || payeeEntity.getBankNamePinyin1().contains(filterWord.toUpperCase())) {
                                bankEntityFilterList.add(payeeEntity);
                            }
                        } else {
                            // 如果名称或者名称的拼音包含过滤文字，那么，添加到bankEntityFilterList中
                            if (payeeEntity.getBankName().contains(filterWord.toUpperCase()) || payeeEntity.getBankNamePinyin().contains(filterWord.toUpperCase())) {
                                bankEntityFilterList.add(payeeEntity);
                            }
                        }
                    } catch (Exception e) {
                        // payeeEntity.getBankNamePinyin()可能为null，所以加try...catch...语句块
                    }
                }
                return bankEntityFilterList.size();
            }
            return bankEntityList.size();
        }
        return 0;
    }

    @Override
    public BankEntity getItem(int position) {
        BankEntity payeeEntity;
        if (!TextUtils.isEmpty(filterWord)) {
            payeeEntity = bankEntityFilterList.get(position);
        } else {
            payeeEntity = bankEntityList.get(position);
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
                    R.layout.list_item_bank1, null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);
        BankEntity bankEntity = getItem(position); // 界面刚才显示有问题，是因为这里取PayeeEntity出错，改为getItem(position)正确

        if (bankEntity.isHot()) {
            Integer logoResId = BankLogoUtil.getLogoResId(bankEntity.getBankAlias());
            if (logoResId != null) {
                holder.iv_bank_logo.setImageResource(logoResId); // 设置银行的logo
            }
            holder.tv_bank_name.setText(bankEntity.getBankAlias());
        } else {
            holder.iv_bank_logo.setImageResource(R.drawable.other_bank_logo);
            holder.tv_bank_name.setText(bankEntity.getBankName());
        }

        return convertView;
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
        char headerChar = getItem(position).getBankNamePinyin().charAt(0);
        if (headerChar == 'z') { //wangyuan修改，2016-9-2
            holder.letter.setText("#");
        } else if (headerChar == '9') {
            holder.letter.setText("常用");
        } else {
            holder.letter.setText(headerChar + ""); // 这里要加""，不加会报错
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getBankNamePinyin().subSequence(0, 1).charAt(0);
    }

    public void clear() {
        bankEntityList.clear(); // 清空
        mSectionIndices = new int[0];
        mSectionLetters = new Character[0];
        notifyDataSetChanged();
    }

    public void restore(List<BankEntity> bankEntityList) {
        this.bankEntityList = bankEntityList;
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tv_bank_name, letter;
        ImageView iv_bank_logo;
        FrameLayout fl_letter;

        public ViewHolder(View convertView) {
            iv_bank_logo = (ImageView) convertView.findViewById(R.id.iv_bank_logo);
            tv_bank_name = (TextView) convertView.findViewById(R.id.tv_bank_name);
            letter = (TextView) convertView.findViewById(R.id.letter);
            fl_letter = (FrameLayout) convertView.findViewById(R.id.fl_letter);
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