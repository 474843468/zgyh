package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PinYinUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.index.QuickIndexBar1;
import com.boc.bocsoft.mobile.bocmobile.base.widget.stickylistheaders.StickyListHeadersListView;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.adapter.BankAdapter1;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.BankEntity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnOtherBankQueryForTransToAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.presenter.ChooseBankPresenter;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment：选择所属银行（带悬浮效果）
 * Created by zhx on 2016/7/20
 * <p/>
 * // header_choose_bank.xml还木有删除 todo
 *
 * 0表示全部
 * 1表示常用银行里减去4个银行，国家开发银行、中国进出口银行、中国农业发展银行、其他银行
 * 2表示去掉字母，只留常用银行
 */
public class ChooseBankFragment1 extends BussFragment implements ChooseBankContact.View, View.OnLayoutChangeListener {
    public static final String KEY_BANK = "bank";
    public static final String KEY_CHOOSE_BANK_TYPE = "choose_bank_type"; // 0表示全部，1表示，2表示

    private View mRootView;
    private ChooseBankPresenter chooseBankPresenter;

    private PsnOtherBankQueryForTransToAccountViewModel otherBank;
    private boolean isHotBankLoadSuccess = false;
    private boolean isOtherBankLoadSuccess = false;

    private QuickIndexBar1 quickIndexBar;
    private StickyListHeadersListView lv_bank;
    private TextView currentWord;
    private List<BankEntity> mHotBankEntityList;
    private List<BankEntity> mBankEntityList = new ArrayList<BankEntity>();
    private List<BankEntity> mBankEntityFilterList = new ArrayList<BankEntity>(); // （根据选择类型choose_bank_type）过滤后的银行列表
    private ApplicationContext mApplicationContext;
    private EditText et_search_content;
    private BankAdapter1 mBankAdapter;
    private TextView tv_search_no_data; // 当搜索没有想要的数据时，显示“无结果”字样
    private int screenHeight;
    private int keyHeight;

    private boolean isSearchNoDataState = false; // 是否是搜索无数据状态
    private boolean isKeyBoardUp = false; // 键盘是否弹起
    private ImageView iv_delete_text;
    private int choose_bank_type;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_choose_bank1, null);
        // 获取屏幕高度
        screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        // 阀值设置为屏幕高度的1/3（监听键盘弹起事件）
        keyHeight = screenHeight / 3;
        return mRootView;
    }

    @Override
    public void initView() {
        quickIndexBar = (QuickIndexBar1) mRootView.findViewById(R.id.quickIndexBar);
        lv_bank = (StickyListHeadersListView) mRootView.findViewById(R.id.listview);
        currentWord = (TextView) mRootView.findViewById(R.id.currentWord);
        et_search_content = (EditText) mRootView.findViewById(R.id.et_search_content);
        iv_delete_text = (ImageView) mRootView.findViewById(R.id.iv_delete_text);
        tv_search_no_data = (TextView) mRootView.findViewById(R.id.tv_search_no_data);
    }

    @Override
    public void initData() {
        choose_bank_type = getArguments().getInt(KEY_CHOOSE_BANK_TYPE, 0);
        // 从应用中获取
        mApplicationContext = (ApplicationContext) mActivity.getApplicationContext();
        mBankEntityList.addAll(mApplicationContext.getBankEntityList());
        mHotBankEntityList = mApplicationContext.getHotBankEntityList();

        // 从xml中获取热门银行列表
        if (mHotBankEntityList.size() == 0) {
            InputStream in = null;
            try {
                in = mActivity.getAssets().open("hotbank.txt");
                mHotBankEntityList = parserXml(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mApplicationContext.setHotBankEntityList(mHotBankEntityList);
        }

        if (mBankEntityList.size() == 0) {
            chooseBankPresenter = new ChooseBankPresenter(this);
            PsnOtherBankQueryForTransToAccountViewModel viewModel = new PsnOtherBankQueryForTransToAccountViewModel();
            viewModel.setType("OTHER");
            chooseBankPresenter.psnOtherBankQueryForTransToAccount(viewModel);
        } else {
            mBankEntityList.addAll(0, mHotBankEntityList);
            setAdatpterOrNority();
        }
    }

    @Override
    public void setListener() {
        iv_delete_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search_content.setText("");
            }
        });
        lv_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideSoftInput(); // 记得如果键盘弹出来，先把键盘隐藏掉

                BankEntity bankEntity = mBankAdapter.getItem(position);
                setChooseBankResult(bankEntity);
            }
        });

        quickIndexBar.setOnTouchLetterListener(new QuickIndexBar1.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String word) {

                if ("常用".equals(word)) { // 如果滑动到“常用”，那么把ListView的位置置为1
                    currentWord.setVisibility(View.GONE);
                    lv_bank.setSelection(0);
                    return;
                }

                showWord(word);

                if (TextUtils.isEmpty(et_search_content.getText().toString().trim())) { // 如果search_content为空，mBankEntityList；如果不为空，mBankAdapter.getBankEntityFilterList()
                    // 如果列表为null，返回
                    if (mBankEntityFilterList == null) {
                        return;
                    }

                    // 遍历所有的bean，找到首字母为触摸字母的条目，然后将其设置到屏幕顶端
                    for (int i = 0; i < mBankEntityFilterList.size(); i++) {
                        String currentFirstWord = mBankEntityFilterList.get(i).getBankNamePinyin().charAt(0) + "";
                        if (currentFirstWord.equals(word)) {
                            // 将第一个首字母为word设置到屏幕顶端
                            lv_bank.setSelection(i); // 此处是一个崩溃点
                            break;
                        }
                    }
                } else {
                    // 如果列表为null，返回
                    if (mBankAdapter.getBankEntityFilterList() == null) {
                        return;
                    }

                    // 遍历所有的bean，找到首字母为触摸字母的条目，然后将其设置到屏幕顶端
                    for (int i = 0; i < mBankAdapter.getBankEntityFilterList().size(); i++) {
                        String currentFirstWord = mBankAdapter.getBankEntityFilterList().get(i).getBankNamePinyin().charAt(0) + "";
                        if (currentFirstWord.equals(word)) {
                            // 将第一个首字母为word设置到屏幕顶端
                            lv_bank.setSelection(i); // 此处
                            break;
                        }
                    }
                }
            }

            @Override
            public void onTouchUp() {
                currentWord.setVisibility(View.GONE);
            }
        });

        et_search_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = et_search_content.getText().toString().trim();
                mBankAdapter.setFilterWord(str);
                setAdatpterOrNority();

                // -----设置“无结果”字样的显示与否-------
                if (!TextUtils.isEmpty(str)) {
                    iv_delete_text.setVisibility(View.VISIBLE);
                    iv_delete_text.setClickable(true);

                    if (mBankAdapter.getCount() == 0) {
                        isSearchNoDataState = true;
                        quickIndexBar.setVisibility(View.GONE);
                        tv_search_no_data.setVisibility(View.VISIBLE);
                    } else {
                        isSearchNoDataState = false;
                        if (!isKeyBoardUp) {
                            quickIndexBar.setVisibility(View.VISIBLE);
                        }
                        tv_search_no_data.setVisibility(View.GONE);
                    }
                } else {
                    iv_delete_text.setVisibility(View.INVISIBLE);
                    iv_delete_text.setClickable(false);

                    isSearchNoDataState = false;
                    tv_search_no_data.setVisibility(View.GONE);

                    if (!isKeyBoardUp) {
                        quickIndexBar.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        mRootView.addOnLayoutChangeListener(this);
    }

    // 设置选择银行返回上页面的结果
    private void setChooseBankResult(BankEntity bankEntity) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_BANK, bankEntity);
        setFramgentResult(Activity.RESULT_OK, bundle);
        pop();
    }

    /**
     * 显示当前触摸字母
     *
     * @param word
     */
    private void showWord(String word) {
        currentWord.setVisibility(View.VISIBLE);
        currentWord.setText(word);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "选择所属银行";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void psnOtherBankQueryForTransToAccountSuccess(PsnOtherBankQueryForTransToAccountViewModel result, String type) {
        isOtherBankLoadSuccess = true;
        otherBank = result;

        mBankEntityList = transResultToBankEntity(result, type);
        mApplicationContext.setBankEntityList(mBankEntityList);
        mBankEntityList.addAll(0, mHotBankEntityList);

        setAdatpterOrNority();
    }

    // 设置Adatpter或刷新列表
    private void setAdatpterOrNority() {


        try {
            // TODO 做刷新界面的事情
            Log.e("ljljlj", "mHotBankEntityList = " + mHotBankEntityList.size());
            Log.e("ljljlj", "mBankEntityList = " + mBankEntityList.size());

            mBankEntityFilterList.clear();
            mBankEntityFilterList.addAll(mBankEntityList);

            switch (choose_bank_type) {
                case 0: // 全部
                    break;
                case 1: // 热门减4
                    List<BankEntity> removeList = new ArrayList<BankEntity>();

                    for (BankEntity bankEntity : mBankEntityFilterList) {
                        if ("国家开发银行".equals(bankEntity.getBankName()) || "中国进出口银行".equals(bankEntity.getBankName()) || "中国农业发展银行".equals(bankEntity.getBankName()) || "其他银行".equals(bankEntity.getBankName())) {
                            removeList.add(bankEntity);
                        }
                    }

                    mBankEntityFilterList.removeAll(removeList);
                    break;
                case 2: // 去掉字母
                    mBankEntityFilterList.clear();
                    mBankEntityFilterList.addAll(mHotBankEntityList);
                    break;
            }

            if (mBankAdapter == null) {
                mBankAdapter = new BankAdapter1(mActivity, mBankEntityFilterList);
                lv_bank.setAdapter(mBankAdapter);
            } else {
                mBankAdapter.restore(mBankEntityFilterList);
            }
        } catch (Exception e) {
        }
    }

    // 解析XMl，得到hot银行列表
    private List<BankEntity> parserXml(InputStream is) {
        List<BankEntity> bankEntityList = new ArrayList<BankEntity>();
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();

            BankEntity bankEntity = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if ("com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.BankEntity".equals(name)) {
                        bankEntity = new BankEntity();
                    } else if ("bankAlias".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankAlias(text);
                    } else if ("bankBpm".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankBpm(text);
                    } else if ("bankBtp".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankBtp(text);
                    } else if ("bankCcpc".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankCcpc(text);
                    } else if ("bankClr".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankClr(text);
                    } else if ("bankCode".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankCode(text);
                    } else if ("bankName".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankName(text);
                    } else if ("bankStatus".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankStatus(text);
                    } else if ("bankType".equals(name)) {
                        parser.next();
                        String text = parser.getText();
                        bankEntity.setBankType(text);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if ("com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.BankEntity".equals(name)) {
                        bankEntity.setBankNamePinyin("9");
                        bankEntity.setBankNamePinyin1(PinYinUtil.getPinYin(bankEntity.getBankAlias()));
                        bankEntity.setHot(true);
                        bankEntityList.add(bankEntity);
                    }
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bankEntityList;
    }

    private List<BankEntity> transResultToBankEntity(PsnOtherBankQueryForTransToAccountViewModel result, String type) {
        List<BankEntity> bankEntityList = new ArrayList<BankEntity>();
        List<PsnOtherBankQueryForTransToAccountViewModel.AccountOfBankListEntity> entityList = result.getAccountOfBankList();
        for (PsnOtherBankQueryForTransToAccountViewModel.AccountOfBankListEntity entity : entityList) {
            BankEntity bankEntity = new BankEntity();
            String bankName = entity.getBankName();
            bankEntity.setBankName(bankName);
            bankEntity.setBankCode(entity.getBankCode());
            if(bankName.contains("长")) {
                String replace = bankName.replace("长", "CHANG");
                bankEntity.setBankNamePinyin(PinYinUtil.getPinYin(replace));
            } else if(bankName.contains("重")) {
                String replace = bankName.replace("重", "CHONG");
                bankEntity.setBankNamePinyin(PinYinUtil.getPinYin(replace));
            } else {
                bankEntity.setBankNamePinyin(PinYinUtil.getPinYin(bankName));
            }
            bankEntity.setBankAlias(entity.getBankAlias());
            bankEntity.setBankType(entity.getBankType());
            bankEntity.setBankBpm(entity.getBankBpm());
            bankEntity.setBankCcpc(entity.getBankCcpc());
            bankEntity.setBankBtp(entity.getBankBtp());
            bankEntity.setBankClr(entity.getBankClr());
            bankEntity.setBankStatus(entity.getBankStatus());

            // 判断下，不添加“江苏银行”
            if ("313301099999".equals(bankEntity.getBankCode())) {
                // 如果是江苏银行，那么不添加
            } else {
                bankEntityList.add(bankEntity);
            }
        }

        if ("OTHER".equals(type)) {
            Collections.sort(bankEntityList);
        }

        return bankEntityList;
    }

    @Override
    public void psnOtherBankQueryForTransToAccountFailed(BiiResultErrorException biiResultErrorException) {
        Log.e("ljljlj", "biiResultErrorException");
    }

    @Override
    public void setPresenter(ChooseBankContact.Presenter presenter) {
    }

    @Override
    public boolean onBack() {
        hideSoftInput();
        return super.onBack();
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Log.e("ljljlj", "left = " + left + " ,top = " + top + " ,right = " + right + " ,bottom = " + bottom);
        Log.e("ljljlj", "oldLeft = " + oldLeft + " ,oldTop = " + oldTop + " ,oldRight = " + oldRight + " ,oldBottom = " + oldBottom);

        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom) > keyHeight) { // 键盘弹起
            isKeyBoardUp = true;
            quickIndexBar.setVisibility(View.GONE);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom) > keyHeight) { // 键盘关闭
            isKeyBoardUp = false;
            if (isSearchNoDataState == false) {
                quickIndexBar.setVisibility(View.VISIBLE);
            }
        }
    }
}
