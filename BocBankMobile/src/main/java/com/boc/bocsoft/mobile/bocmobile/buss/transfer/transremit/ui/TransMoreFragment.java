package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui;

import android.annotation.SuppressLint;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.widget.more.BaseMoreFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Menu;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.PayeeManageFragment2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.ui.PhoneEditPageFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.prerecordquery.ui.PreRecordFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui.QrcodeMeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui.QrcodeScanFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.ui.TransferRecordFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页更多
 * Created by wangyuan on 2016/9/6.
 */
@SuppressLint("ValidFragment")
public class TransMoreFragment extends BaseMoreFragment {

    @Override
    protected List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        Menu menu = ApplicationContext.getInstance().getMenu();
        //我要转账
//        items.add(menu.findItemById(ModuleCode.MODULE_TRANSER_0000));
        // 二维码转账
        items.add(menu.findItemById(ModuleCode.MODULE_TRANSER_0100));
        //手机号转账
        items.add(menu.findItemById(ModuleCode.MODULE_TRANSER_0200));
        // 设置默认账户
        items.add(menu.findItemById(ModuleCode.MODULE_TRANSER_0700));
        //转账记录
        items.add(menu.findItemById(ModuleCode.MODULE_TRANSER_0300));
        //预约管理
        items.add(menu.findItemById(ModuleCode.MODULE_TRANSER_0400));
        //收款人管理
        items.add(menu.findItemById(ModuleCode.MODULE_TRANSER_0500));
        //我的二维码
        items.add(menu.findItemById(ModuleCode.MODULE_TRANSER_0600));
        return items;
    }

    @Override
    public void onClick(String id) {
        switch (id) {
            // 二维码转账
            case ModuleCode.MODULE_TRANSER_0100:
                start(new QrcodeScanFragment());
                break;
            //手机号转账
            case ModuleCode.MODULE_TRANSER_0200:
                start(new PhoneEditPageFragment());
                break;
            //转账记录
            case ModuleCode.MODULE_TRANSER_0300:
                start(new TransferRecordFragment());
                break;
            //预约管理
            case ModuleCode.MODULE_TRANSER_0400:
                start(new PreRecordFragment());
                break;
            //收款人管理
            case ModuleCode.MODULE_TRANSER_0500:
                start(new PayeeManageFragment2());
                break;
            //我的二维码
            case ModuleCode.MODULE_TRANSER_0600:
                start(new QrcodeMeFragment());
                break;
            //设置默认账户
            case ModuleCode.MODULE_TRANSER_0700:
                ModuleActivityDispatcher.dispatch(mActivity, ModuleCode.MODULE_TRANSER_0700, null);
                break;
        }
    }

    //    @Override
//    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
//        super.onFragmentResult(requestCode, resultCode, data);
//        if (QrcodeTransFragment.QRCODE_SCAN_REQUEST_CODE == requestCode) {
//            if (QrcodeScanFragment.RESULT_DECODE_SUCCESS == resultCode) {
//                ScanResultAccountModel accountModel = data.getParcelable(QrcodeScanFragment.RESULT_KEY);
//                QrcodeTransFragment fragment = new QrcodeTransFragment();
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(QrcodeScanFragment.RESULT_KEY, accountModel);
//                fragment.setArguments(bundle);
//                start(fragment);
//            }
//        }
//    }
    private boolean mIsRefresh = false;//是否刷新我要转账页面

    public void setRefresh(boolean isRefresh) {
        mIsRefresh = isRefresh;
    }

    @Override
    protected void titleLeftIconClick() {
        handBackFreash();
    }

    @Override
    public boolean onBack() {
        handBackFreash();
        return false;
    }

    /**
     * 处理后退刷新
     */
    private void handBackFreash() {
        if (mIsRefresh) {
            // 对我要转账刷新
            popToAndReInit(TransRemitBlankFragment.class);
            mIsRefresh = false;
        } else {
            pop();
        }

    }
}
