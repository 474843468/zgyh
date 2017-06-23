package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.cardemulation.CardEmulation;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;

/**
 * NFC识别
 * Created by gwluo on 2016/9/13.
 */
public class NFCRecognizeActivity extends BussActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_nfc_recognize);
    }

    @Override
    public void initData() {
        super.initData();
        checkIsDefaultApp(this);
    }

    @SuppressLint("NewApi")
    public void checkIsDefaultApp(Context context) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (nfcAdapter == null) {
            ToastUtils.show("设备不支持nfc");
            finish();
            return;
        }
        CardEmulation cardEmulationManager = CardEmulation
                .getInstance(nfcAdapter);
        ComponentName paymentServiceComponent = new ComponentName(
                context.getApplicationContext(),
                HostApduService.class.getCanonicalName());

        if (!cardEmulationManager.isDefaultServiceForCategory(
                paymentServiceComponent, CardEmulation.CATEGORY_PAYMENT)) {
            Intent intent = new Intent(CardEmulation.ACTION_CHANGE_DEFAULT);
            intent.putExtra(CardEmulation.EXTRA_CATEGORY,
                    CardEmulation.CATEGORY_PAYMENT);
            intent.putExtra(CardEmulation.EXTRA_SERVICE_COMPONENT,
                    paymentServiceComponent);
            ((Activity) context).startActivityForResult(intent, 0);
            ToastUtils.show("默认nfc");
        } else {
            ToastUtils.show("不是默认");
        }
    }
}
