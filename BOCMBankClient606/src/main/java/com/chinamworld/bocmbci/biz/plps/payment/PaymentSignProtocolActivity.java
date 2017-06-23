package com.chinamworld.bocmbci.biz.plps.payment;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.Inves;
import com.chinamworld.bocmbci.bii.constant.Plps;
import com.chinamworld.bocmbci.biz.plps.PlpsBaseActivity;
import com.chinamworld.bocmbci.biz.plps.PlpsDataCenter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
/**
 * 签约协议页面
 * @author panwe
 *
 */
public class PaymentSignProtocolActivity extends PlpsBaseActivity{
	private String isChecked;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("签约协议");
		inflateLayout(R.layout.plps_payment_protocol);
		Intent intent = getIntent();
		isChecked = intent.getStringExtra("check");
		((TextView)findViewById(R.id.contract_title_tv))
			.setText(Html.fromHtml("<b>中国银行股份有限公司个人委托扣款协议书</b>"));
		((TextView) findViewById(R.id.text_tip))
				.setText(Html
						.fromHtml("甲方（客户）授权乙方（中国银行股份有限公司）从甲方指定银行账号（以下简称“签约账号”）内自动转账支付收费单位指定收费，双方达成如下协议：<br>"
								+"一、甲方授权乙方按收费单位向乙方提供的收费指令，从甲方指定的签约账号内向收费单位指定账户自动转账支付费用，或甲方授权收费单位向第三方支付机构发送收费指令，经由第三方支付机构向乙方发送扣款指令，乙方收到扣款指令后从甲方指定的签约账号内向第三方支付机构指定账户自动转账支付费用。收费金额可能包括往月（期）欠费和滞纳金、违约金等。收费单位是指在本协议项下签约委托扣款项目费用的费用收取单位。<br>"
								+"<b>二、甲方应保证所提供的扣款信息，包括缴费用户号、户名、地址、手机号码、签约账号等信息真实、准确、完整、有效。由于因甲方提供的信息不真实、不准确、不完整所造成的交易失败的风险或交易损失，由甲方自行承担。<br></b>"
								+"三、如甲方签约账号发生同时需支付多项收费款项时，由乙方按照乙方获得收费单位收费信息指令的先后次序向收费单位指定账户自动转账支付。<br>"
								+"<b>四、甲方应确保签约账号内有足够支付收费单位费用的款项，如因甲方签约账号余额不足或因签约账号被挂失、销户等异常情况导致甲方支付相关费用不成功而造成的风险和损失，由甲方自行承担。<br></b>"
								+"\t\t\t\t信用卡一般不支持透支扣款，经乙方与收费单位双方确认同意的信用卡透支代扣项目除外，具体以实际开办项目为准。在签约信用卡账户余额不足时，银行在信用卡额度内透支扣款。客户须遵循《中国银行股份有限公司信用卡领用合约》、《中国银行股份有限公司信用卡章程》约定的相关规则，及时归还透支本息。<br>"
								+"五、乙方负责按照收费单位提供的收费信息进行扣款，各项委托扣款信息的真实性、准确性、合法性由收费单位负责。甲方如对收费款项有疑义时，应及时向收费单位查询。<br>"
								+"<b>六、甲方办理委托扣款业务应遵循收费单位的相关规定。如因不符合收费单位规定，或甲方提供的信息不全、不实等原因造成签约不成功、扣款不成功及其他风险，乙方不承担责任。<br></b>"
								+"七、甲方如变更签约账户等本协议项下信息，需先解除原签约后重新办理签约，相关变更自新签约经甲方系统确认后生效。<br>"
								+"八、本协议自甲方在乙方电子渠道签署后生效，至甲方解除签约时本协议终止，终止前乙方按本协议约定向收费单位支付相关费用仍为有效。本协议项下交易情况以乙方系统记录为准。<br>"
								+"九、甲方使用本协议项下服务应遵守相关法律法规和监管规定，不得利用本协议项下服务从事洗钱、欺诈等违法、违规行为，否则乙方有权中止或终止提供服务。<br>"
								+"十、甲方双方在履行本协议过程中如发生争议，应协商解决；协商不成的，可向甲方签约账号开户行所在地人民法院提起诉讼。"));
		((TextView) findViewById(R.id.tvFirst)).setText((String) ((Map<String, Object>)
				BaseDroidApp.getInstanse().getBizDataMap()
				.get(ConstantGloble.BIZ_LOGIN_DATA)).get(Inves.CUSTOMERNAME));
		
	}
	/**
	 * 同意*/
	public void acceptBtnListener(View v){
		setResult(Plps.PROTOCOL);
		finish();
	}
	/**不同意*/
	public void notAcceptBtnListener(View v){
		finish();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.ib_back) {
			if (isChecked.equals("true")) {
				setResult(Plps.PROTOCOL);
				finish();
			}else {
				finish();
			}
		}
		else if(v.getId() == R.id.ib_top_right_btn){
			PlpsDataCenter.getInstance().clearAllData();
			ActivityTaskManager.getInstance().removeAllActivity();
		}
	}

}
