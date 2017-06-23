package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.SavePayerViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter.SavePayerPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * Fragment：保存付款人
 * Created by zhx on 2016/7/12
 */
public class AddPayerFragment extends BussFragment implements SavePayerContract.View {
    public static final int RESULT_CODE_ADD_PAYER_SUCCESS = 110;
    private View mRootView;
    private EditText etPayerName;
    private EditText etPayerPhoneNum;
    private Button btnSubmit; // 提交按钮
    private SavePayerPresenter savePayerPresenter;
    private ImageView ivSelectFromPhone;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_add_payer, null);
        return mRootView;
    }

    @Override
    public void initView() {
        etPayerName = (EditText) mRootView.findViewById(R.id.et_payer_name);
        etPayerPhoneNum = (EditText) mRootView.findViewById(R.id.et_payer_phone_num);
        btnSubmit = (Button) mRootView.findViewById(R.id.btn_submit);
        ivSelectFromPhone = (ImageView) mRootView.findViewById(R.id.iv_select_from_phone);

        //        savePayerPresenter = new SavePayerPresenter(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void setListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = checkInputData();
                if (flag) { // 如果数据检查没有问题，那么开始保存付款人
                    SavePayerViewModel savePayerViewModel = new SavePayerViewModel();
                    savePayerViewModel.setPayerName(etPayerName.getText().toString());
                    savePayerViewModel.setPayerMobile(etPayerPhoneNum.getText().toString());
                    savePayerPresenter.savePayer(savePayerViewModel);
                }
            }
        });
        //        btnSubmit.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                Bundle bundle = new Bundle();
        //                TestBean testBean = new TestBean();
        //                testBean.name = etPayerName.getText().toString().trim();
        //                testBean.mobilePhone = etPayerPhoneNum.getText().toString().trim();
        //                bundle.putParcelable("payer", testBean);
        //                setFramgentResult(RESULT_CODE_ADD_PAYER_SUCCESS, bundle);
        //                pop();
        //            }
        //        });

        ivSelectFromPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), 0);
            }
        });

    }

    // 检查输入的数据
    private boolean checkInputData() {
        if (TextUtils.isEmpty(etPayerName.getText().toString().toString())) {
            showErrorDialog("付款人名称不能为空");
            return false;
        }

        if (TextUtils.isEmpty(etPayerPhoneNum.getText().toString().trim())) {
            showErrorDialog("付款人手机号不能为空");
            return false;
        }

        if (!NumberUtils.checkMobileNumber(etPayerPhoneNum.getText().toString().trim())) {
            showErrorDialog("付款人手机号：11位数字");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            ContentResolver contentResolver = mActivity.getContentResolver();
            Uri contactData = data.getData();
            Cursor cursor = contentResolver.query(contactData, null, null, null, null);
            if (cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                etPayerName.setText(name);
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
                if (phones.moveToFirst()) {
                    String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    etPayerPhoneNum.setText(phone);
                }
                phones.close();
            }
            cursor.close();
        }
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "添加付款人";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void savePayerSuccess(SavePayerViewModel savePayerViewModel) {

        Log.e("ljljlj", "添加成功");
    }

    @Override
    public void savePayerFail(BiiResultErrorException biiResultErrorException) {

        Log.e("ljljlj", "添加失败");
    }

    @Override
    public void setPresenter(SavePayerContract.Presenter presenter) {

    }
}