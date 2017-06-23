package com.chinamworld.bocmbci.biz.crcd.view;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Crcd;
import com.chinamworld.bocmbci.biz.crcd.CrcdBaseActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.PopupWindowUtils;
import com.chinamworld.bocmbci.utils.QueryDateUtils;
import com.chinamworld.bocmbci.utils.StringUtil;

public class InflaterViewDialog {
	private static final String TAG = "InflaterViewDialog";

	private Activity currentContent;
	private View contentView;

	public InflaterViewDialog(Activity currentContent) {
		this.currentContent = currentContent;
	}

	/**
	 * 信用卡账户详情弹出框
	 * 
	 * @param bankAccountList
	 *            账户详情列表
	 * @param exitAccDetailClick
	 *            关闭详情监听事件
	 * @param updateNicknameClick
	 *            更新账号别名事件
	 * @return
	 */
	public View initCrcdMessageDialogView(Map<String, Object> crcdAccount,
			Map<String, Object> mapResult, View.OnClickListener crcdSetupClick,
			View.OnClickListener exitAccDetailClick,
			final OnClickListener updatenicknameClick,
			final OnClickListener renmibiClick,
			final OnClickListener dollerClick, final String currency,
			String currency1, String currency2, String cardType,
			final OnClickListener gotoTransfer,
			final OnClickListener greatwallListener) {
		contentView = LayoutInflater.from(currentContent).inflate(
				R.layout.crcd_mycrcd_detail, null);

		final FrameLayout fl_acc_nickname = (FrameLayout) contentView
				.findViewById(R.id.fl_nickname);

		TextView tv_prodCode_detail = (TextView) contentView
				.findViewById(R.id.tv_prodCode_detail);
		final TextView acc_accountnickname_value = (TextView) contentView
				.findViewById(R.id.acc_accountnickname_value);
		TextView tv_curCode_detail = (TextView) contentView
				.findViewById(R.id.tv_curCode_detail);
		// 账面余额
		TextView tv_buyPrice_detail = (TextView) contentView
				.findViewById(R.id.tv_buyPrice_detail);
		// 账面余额---字段
		TextView tv_buyPrice_name = (TextView) contentView
				.findViewById(R.id.tv_buyPrice_name);
		// 总可用额
		TextView tv_prodTimeLimit_detail = (TextView) contentView
				.findViewById(R.id.tv_prodTimeLimit_detail);
		TextView tv_applyObj_detail = (TextView) contentView
				.findViewById(R.id.tv_applyObj_detail);

		TextView tv_status_detail = (TextView) contentView
				.findViewById(R.id.tv_status_detail);
		TextView tv_prodRisklvl_detail = (TextView) contentView
				.findViewById(R.id.tv_prodRisklvl_detail);
		TextView tv_periodical_detail = (TextView) contentView
				.findViewById(R.id.tv_periodical_detail);
		// 可转账余额
		TextView tv_loanBalanceLimit = (TextView) contentView
				.findViewById(R.id.tv_billdivide_loanBalanceLimit);
		ImageView img_exit = (ImageView) contentView
				.findViewById(R.id.img_exit_accdetail);
		final ImageView img_update_accnickname = (ImageView) contentView
				.findViewById(R.id.img_acc_update_nickname);

		Button img_crcd_setup = (Button) contentView
				.findViewById(R.id.img_crcd_setup);

		final EditText et_acc_accountnickname = (EditText) contentView
				.findViewById(R.id.et_acc_nickname);
		Button btn_updatenickname = (Button) contentView
				.findViewById(R.id.btn_update_nickname);

		// LinearLayout ll_beedtype = (LinearLayout)
		// contentView.findViewById(R.id.ll_beedtype);
		// LinearLayout ll_billType = (LinearLayout)
		// contentView.findViewById(R.id.ll_billType);
		// TextView tv_bill_type = (TextView)
		// contentView.findViewById(R.id.tv_bill_type);

		Button btn_renminbi = (Button) contentView
				.findViewById(R.id.btn_renminbi);
		btn_renminbi.setOnClickListener(renmibiClick);
		Button btn_waibi = (Button) contentView.findViewById(R.id.btn_waibi);
		btn_waibi.setOnClickListener(dollerClick);

		TextView tv_billdivide_keyong = (TextView) contentView
				.findViewById(R.id.tv_billdivide_keyong);
		TextView tv_score = (TextView) contentView.findViewById(R.id.tv_score);

		LinearLayout ll_cun_lixi = (LinearLayout) contentView
				.findViewById(R.id.ll_cun_lixi);
		LinearLayout ll_lixi_tax = (LinearLayout) contentView
				.findViewById(R.id.ll_lixi_tax);
		TextView tv_cun_lixi = (TextView) contentView
				.findViewById(R.id.tv_cun_lixi);
		TextView tv_lixitax = (TextView) contentView
				.findViewById(R.id.tv_lixitax);
		Button btn_xinyonghuan = (Button) contentView
				.findViewById(R.id.btn_xinyonghuan);
		Button btn_gohuihuan = (Button) contentView
				.findViewById(R.id.btn_gohuihuan);
		Button btn_more = (Button) contentView.findViewById(R.id.btn_more);

		// 信用卡类型
		String acc_type = String.valueOf(crcdAccount
				.get(Crcd.CRCD_ACCOUNTTYPE_RES));
		String strAccountType = "";
		if (!StringUtil.isNull(acc_type)) {
			strAccountType = LocalData.AccountType.get(acc_type);
			tv_prodCode_detail.setText(strAccountType);
		}
		// 仅长城信用卡
		if (CrcdBaseActivity.GREATWALL.equals(acc_type)) {
			// 存款利息
			ll_cun_lixi.setVisibility(View.VISIBLE);
			// 存款利息税
			ll_lixi_tax.setVisibility(View.VISIBLE);
		}
		if (CrcdBaseActivity.SINGLEWAIBI.equals(acc_type)) {
			// 单外币信用卡----显示第一个按钮，隐藏第二个按钮
			btn_renminbi.setVisibility(View.VISIBLE);
			btn_waibi.setVisibility(View.GONE);
			if (!StringUtil.isNull(currency1)) {
				String strCurrency = LocalData.Currency.get(currency1);
				btn_renminbi.setText(strCurrency);

			}
		} else {

			// 币种2为空,币种1不为空
			if (!StringUtil.isNull(currency1) && StringUtil.isNull(currency2)) {
				String strCurrency = LocalData.Currency.get(currency1);
				btn_renminbi.setVisibility(View.VISIBLE);
				btn_waibi.setVisibility(View.GONE);
				btn_renminbi.setText(strCurrency);
			}
			// 币种1不为空且币种2不为空
			else if (!StringUtil.isNull(currency1)
					&& !StringUtil.isNull(currency2)) {
				btn_renminbi.setVisibility(View.VISIBLE);
				btn_waibi.setVisibility(View.VISIBLE);
				String strCurrency1 = LocalData.Currency.get(currency1);
				btn_renminbi.setText(strCurrency1);
				String strCurrency2 = LocalData.Currency.get(currency2);
				btn_waibi.setText(strCurrency2);
			}

		}

		int black = currentContent.getResources().getColor(R.color.black);
		int gray = currentContent.getResources().getColor(R.color.gray);
		int red = currentContent.getResources().getColor(R.color.red);

		// 标题按钮颜色
		if (!StringUtil.isNull(currency1) && currency.equals(currency1)) {
			// 第一个按钮被选中
			btn_renminbi.setBackgroundResource(R.drawable.acc_top_left);
			btn_renminbi.setTextColor(black);
			btn_waibi.setBackgroundResource(R.drawable.acc_top_right);
			btn_waibi.setTextColor(gray);
		} else if (!StringUtil.isNull(currency2) && currency.equals(currency2)) {
			// 第二个按钮被选中
			btn_renminbi.setBackgroundResource(R.drawable.acc_top_right);
			btn_renminbi.setTextColor(gray);
			btn_waibi.setBackgroundResource(R.drawable.acc_top_left);
			btn_waibi.setTextColor(black);
		}

		Button btn_description_buydetail = (Button) contentView
				.findViewById(R.id.btn_description_buydetail);
		String strCurrency1 = LocalData.Currency.get(currency1);
		btn_description_buydetail.setText(strCurrency1);

		Button btn_buy_buydetail = (Button) contentView
				.findViewById(R.id.btn_buy_buydetail);
		if (!StringUtil.isNull(currency2)) {
			String strCurrency2 = LocalData.Currency.get(currency2);
			btn_buy_buydetail.setText(strCurrency2);
		}
		String nickname = String.valueOf(crcdAccount
				.get(Crcd.CRCD_NICKNAME_RES));
		if (!StringUtil.isNull(acc_type)) {
			acc_accountnickname_value.setText(nickname);
		}
		// 账号
		String acc_accountnumber = String.valueOf(crcdAccount
				.get(Crcd.CRCD_ACCOUNTNUMBER_RES));
		if (!StringUtil.isNull(acc_accountnumber)) {
			tv_curCode_detail.setText(StringUtil
					.getForSixForString(acc_accountnumber));
		}

		Map<String, Object> mapDetail = (Map<String, Object>) crcdAccount
				.get(Crcd.CRCD_DETAILIST);
		if (!StringUtil.isNullOrEmpty(mapDetail)) {
			// 账户详情信息

			// 账面余额
			tv_buyPrice_detail
					.setText(StringUtil.parseStringCodePattern(currency, String
							.valueOf(mapDetail.get(Crcd.CRCD_CURRENTBALANCE)),
							2));
			tv_buyPrice_detail.setTextColor(red);
			// 总可用额度
			tv_prodTimeLimit_detail.setText(StringUtil.parseStringCodePattern(
					currency,
					String.valueOf(mapDetail.get(Crcd.CRCD_TOTALBALANCE)), 2));
			// 总授信额度
			tv_applyObj_detail.setText(StringUtil.parseStringCodePattern(
					currency,
					String.valueOf(mapDetail.get(Crcd.CRCD_TOTALLIMIT)), 2));
			// 取现额度
			tv_status_detail.setText(StringUtil.parseStringCodePattern(
					currency,
					String.valueOf(mapDetail.get(Crcd.CRCD_CASHLIMIT)), 2));
			// 取现可用额
			tv_prodRisklvl_detail.setText(StringUtil.parseStringCodePattern(
					currency,
					String.valueOf(mapDetail.get(Crcd.CRCD_CASHBALANCE)), 2));
			// 分期额度
			tv_periodical_detail.setText(StringUtil.parseStringCodePattern(
					currency,
					String.valueOf(mapDetail.get(Crcd.CRCD_INSTALLMENTLIMIT)),
					2));
			// 分期可用额
			tv_billdivide_keyong.setText(StringUtil.parseStringCodePattern(
					currency, String.valueOf(mapDetail
							.get(Crcd.CRCD_INSTALLLMENTBALANCE)), 2));
			// 积分为整数
			String integral = null;
			if (StringUtil.isNullOrEmpty(mapDetail
					.get(Crcd.CRCD_COSUMPTITONPOINT))) {
				integral = "-";
			} else {
				integral = String.valueOf(mapDetail
						.get(Crcd.CRCD_COSUMPTITONPOINT));
			}
			tv_score.setText(integral);
			// 存款利息
			tv_cun_lixi
					.setText(StringUtil.parseStringCodePattern(currency, String
							.valueOf(mapDetail.get(Crcd.CRCD_SAVINGINTEREST)),
							2));
			// 存款利息税
			tv_lixitax.setText(String.valueOf(mapDetail
					.get(Crcd.CRCD_SAVINGINTERESTTAX)));

		} else {
			/** 账户详情信息 */
			List<Map<String, String>> crcdAccountDetailList = (List<Map<String, String>>) mapResult
					.get(Crcd.CRCD_CRCDACCOUNTDETAILLIST);
			Map<String, String> map = crcdAccountDetailList.get(0);

			String currentBalance = String.valueOf(map
					.get(Crcd.CRCD_CURRENTBALANCE));
			String balance = null;
			if (!StringUtil.isNull(currentBalance)) {
				balance = StringUtil.parseStringCodePattern(currency,
						currentBalance, 2);
			} else {
				balance = "-";
			}
			tv_buyPrice_detail.setText(balance);
			String currentBalanceflag = String.valueOf(map
					.get(Crcd.CRCD_CURRENTBALANCEFLAG));
			if (!StringUtil.isNull(currentBalanceflag)) {
				if (ConstantGloble.CRCD_SEARCH_ZERO.equals(currentBalanceflag)) {
					tv_buyPrice_name.setText(ConstantGloble.TOUZHI);
				} else if (ConstantGloble.CRCD_SEARCH_ONE
						.equals(currentBalanceflag)) {
					tv_buyPrice_name.setText(ConstantGloble.JIEYU);
				} else if (ConstantGloble.CRCD_SEARCH_TWO
						.equals(currentBalanceflag)) {
					tv_buyPrice_name.setVisibility(View.GONE);
				}
			}

			tv_prodTimeLimit_detail.setText(StringUtil.parseStringCodePattern(
					currency, String.valueOf(map.get(Crcd.CRCD_TOTALBALANCE)),
					2));
			tv_applyObj_detail
					.setText(StringUtil.parseStringCodePattern(currency,
							String.valueOf(map.get(Crcd.CRCD_TOTALLIMIT)), 2));
			tv_status_detail.setText(StringUtil.parseStringCodePattern(
					currency, String.valueOf(map.get(Crcd.CRCD_CASHLIMIT)), 2));
			tv_prodRisklvl_detail
					.setText(StringUtil.parseStringCodePattern(currency,
							String.valueOf(map.get(Crcd.CRCD_CASHBALANCE)), 2));
			tv_periodical_detail.setText(StringUtil.parseStringCodePattern(
					currency,
					String.valueOf(map.get(Crcd.CRCD_INSTALLMENTLIMIT)), 2));
			tv_billdivide_keyong.setText(StringUtil.parseStringCodePattern(
					currency,
					String.valueOf(map.get(Crcd.CRCD_INSTALLLMENTBALANCE)), 2));
			// 积分为整数

			String integral = null;
			if (StringUtil.isNullOrEmpty(map.get(Crcd.CRCD_COSUMPTITONPOINT))) {
				integral = "-";
			} else {
				integral = String.valueOf(map.get(Crcd.CRCD_COSUMPTITONPOINT));
			}

			tv_score.setText(integral);

			tv_cun_lixi.setText(StringUtil.parseStringCodePattern(currency,
					String.valueOf(map.get(Crcd.CRCD_SAVINGINTEREST)), 2));
			tv_lixitax.setText(StringUtil.parseStringCodePattern(currency,
					String.valueOf(map.get(Crcd.CRCD_SAVINGINTERESTTAX)), 2));
			tv_buyPrice_detail.setTextColor(red);
			// 可转账余额
			String loanBalanceLimit = map.get(Crcd.CRCD_LOANBALANCELIMIT);
			String loanBalance = null;
			if (!StringUtil.isNull(loanBalanceLimit)) {
				loanBalance = StringUtil.parseStringCodePattern(currency,
						loanBalanceLimit, 2);
			} else {
				loanBalance = "-";
			}
			tv_loanBalanceLimit.setText(loanBalance);
		}

		// 修改账户别名点击事件
		img_update_accnickname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fl_acc_nickname.setVisibility(View.VISIBLE);
				acc_accountnickname_value.setVisibility(View.GONE);
				img_update_accnickname.setVisibility(View.GONE);
				et_acc_accountnickname.setText(acc_accountnickname_value
						.getText().toString());
				et_acc_accountnickname.setSelection(et_acc_accountnickname
						.length());
			}
		});

		// 信用卡设定
		img_crcd_setup.setOnClickListener(crcdSetupClick);

		// 退出账户详情点击事件
		img_exit.setOnClickListener(exitAccDetailClick);

		btn_updatenickname.setOnClickListener(updatenicknameClick);

		btn_description_buydetail.setOnClickListener(renmibiClick);
		btn_buy_buydetail.setOnClickListener(dollerClick);

		if (currency1.equals(currency)) {
			btn_description_buydetail
					.setBackgroundResource(R.drawable.left_two_red);
			btn_description_buydetail.setTextColor(Color.WHITE);
			btn_buy_buydetail.setBackgroundResource(R.drawable.right_two_white);
			btn_buy_buydetail.setTextColor(Color.BLACK);
		} else if (!StringUtil.isNull(currency2) && currency2.equals(currency)) {
			btn_description_buydetail
					.setBackgroundResource(R.drawable.left_two_white);
			btn_description_buydetail.setTextColor(Color.BLACK);
			btn_buy_buydetail.setBackgroundResource(R.drawable.right_two_red);
			btn_buy_buydetail.setTextColor(Color.WHITE);
		}

		// 中信、长城
		if (CrcdBaseActivity.ZHONGYIN.equals(cardType)
				|| CrcdBaseActivity.GREATWALL.equals(cardType)) {
			LogGloble.d(TAG + " ", currency + "------");
			if (!StringUtil.isNull(currency1) && currency.equals(currency1)) {
				// 当前币种是币种1
				btn_more.setTag(cardType);
				if (ConstantGloble.FOREX_RMB_TAG1.equals(currency1)
						|| ConstantGloble.FOREX_RMB_CNA_TAG2.equals(currency1)) {
					// 币种1为人民币
					// 人民币显示---信用卡还款
					btn_xinyonghuan.setVisibility(View.VISIBLE);
					btn_gohuihuan.setVisibility(View.GONE);
				} else {
					// 币种1为外币
					btn_gohuihuan.setVisibility(View.VISIBLE);
					btn_xinyonghuan.setVisibility(View.GONE);
				}
			} else if (!StringUtil.isNull(currency2)
					&& currency.equals(currency2)) {
				// 当前币种是币种2
				btn_more.setTag(cardType);
				if (ConstantGloble.FOREX_RMB_TAG1.equals(currency2)
						|| ConstantGloble.FOREX_RMB_CNA_TAG2.equals(currency2)) {
					// 币种2为人民币
					// 人民币显示---信用卡还款
					btn_xinyonghuan.setVisibility(View.VISIBLE);
					btn_gohuihuan.setVisibility(View.GONE);
				} else {
					// 币种2为外币
					btn_gohuihuan.setVisibility(View.VISIBLE);
					btn_xinyonghuan.setVisibility(View.GONE);
				}
			}

		} else if (CrcdBaseActivity.SINGLEWAIBI.equals(cardType)) {
			// 单外币
			btn_gohuihuan.setVisibility(View.VISIBLE);
			btn_xinyonghuan.setVisibility(View.GONE);
			btn_more.setTag(cardType);
		}

		btn_xinyonghuan.setOnClickListener(gotoTransfer);
		btn_gohuihuan.setOnClickListener(gotoTransfer);
		String[] selectors = null;
		String xiaofei = currentContent
				.getString(R.string.mycrcd_xiaofei_fuwu_setup);
		String billservice = currentContent
				.getString(R.string.mycrcd_to_bill_service_setup);
		String cutmoney = currentContent
				.getString(R.string.mycrcd_cut_money_setup);
		String fushu = currentContent
				.getString(R.string.mycrcd_fushu_service_setup);
		String guishi = currentContent
				.getString(R.string.mycrcd_creditcard_guashi_title);
		selectors = new String[] { xiaofei, billservice, cutmoney, fushu,
				guishi };
		// 402
		if (CrcdBaseActivity.GREATWALL.equals(cardType)) {
			if (!StringUtil.isNull(currency)
					&& LocalData.Currency.get(currency).equals(
							ConstantGloble.ACC_RMB)) {
				selectors = new String[] { xiaofei, billservice, cutmoney,
						fushu, guishi, ConstantGloble.ACC_CRCD_TRAN };
			} else {
				selectors = new String[] { xiaofei, billservice, cutmoney,
						fushu, guishi, ConstantGloble.ACC_TRAN_REMIT };
			}
		}
		if (CrcdBaseActivity.ZHONGYIN.equals(cardType)) {
			if (!StringUtil.isNull(currency)
					&& LocalData.Currency.get(currency).equals(
							ConstantGloble.ACC_RMB)) {
				selectors = new String[] { xiaofei, billservice, cutmoney,
						fushu, guishi };
			} else {
				selectors = new String[] { xiaofei, billservice, cutmoney,
						fushu, guishi, ConstantGloble.ACC_TRAN_REMIT };
			}
		}

		PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
				currentContent, btn_more, selectors, greatwallListener);
		return contentView;
	}

	// 我的虚拟信用卡------虚拟信用卡详情
	public View initVirtualBCListDialogView(Map<String, Object> virCardItem,
			View.OnClickListener exitVirtualDetailClick,
			View.OnClickListener gotoSutepClick,
			View.OnClickListener gotoSmsClick, OnClickListener popListener) {
		contentView = LayoutInflater.from(currentContent).inflate(
				R.layout.crcd_virtualcard_detail, null);

		TextView tv_creditcatdaccount = (TextView) contentView
				.findViewById(R.id.tv_creditcatdaccount);
		TextView tv_creditcatdtype = (TextView) contentView
				.findViewById(R.id.tv_creditcatdtype);
		TextView tv_account = (TextView) contentView
				.findViewById(R.id.tv_account);
		TextView tv_jiankacannel = (TextView) contentView
				.findViewById(R.id.tv_jiankacannel);
		TextView tv_starttime = (TextView) contentView
				.findViewById(R.id.tv_starttime);
		TextView leftText = (TextView) contentView.findViewById(R.id.left_text);
		PopupWindowUtils.getInstance().setOnShowAllTextListener(currentContent,
				leftText);
		TextView tv_endtime = (TextView) contentView
				.findViewById(R.id.tv_endtime);

		TextView tv_totalmoney = (TextView) contentView
				.findViewById(R.id.tv_totalmoney);
		TextView tv_singlemoney = (TextView) contentView
				.findViewById(R.id.tv_singlemoney);
		// 已累计交易金额
		TextView tv_hastotalmoney = (TextView) contentView
				.findViewById(R.id.tv_hastotalmoney);
		tv_hastotalmoney.setText(StringUtil.parseStringPattern(
				String.valueOf(virCardItem.get(Crcd.CRCD_ATOTALEAMT)), 2));

		tv_creditcatdaccount.setText(StringUtil.getForSixForString(String
				.valueOf(virCardItem.get(Crcd.CRCD_CREDITCARDNO))));
		tv_creditcatdtype.setText(currentContent
				.getString(R.string.mycrcd_xuni_crcd));
		tv_account.setText(StringUtil.getForSixForString(String
				.valueOf(virCardItem.get(Crcd.CRCD_VIRTUALCARDNO))));
		String channel = String
				.valueOf(virCardItem.get(Crcd.CRCD_CREATCHANNEL));
		String strChannel = LocalData.channelType.get(channel);
		tv_jiankacannel.setText(strChannel);

		String startDate = String.valueOf(virCardItem.get(Crcd.CRCD_STARTDATE));
		String endDate = String.valueOf(virCardItem.get(Crcd.CRCD_ENDDATE));

		String changeStartDate = QueryDateUtils.getInstance().getCurDate(
				Long.parseLong(startDate));
		String changeEndDate = QueryDateUtils.getInstance().getCurDate(
				Long.parseLong(endDate));

		tv_starttime.setText(changeStartDate);
		tv_endtime.setText(changeEndDate);
		String total = String.valueOf(virCardItem.get(Crcd.CRCD_TOTALEAMT));
		tv_totalmoney.setText(StringUtil.parseStringPattern(total, 2));
		String single = String.valueOf(virCardItem.get(Crcd.CRCD_SINGLEEMT));
		tv_singlemoney.setText(StringUtil.parseStringPattern(single, 2));
		// 虚拟卡设定
		Button img_crcd_setup = (Button) contentView
				.findViewById(R.id.img_crcd_setup);
		// 短信通知
		Button img_sms_toast = (Button) contentView
				.findViewById(R.id.img_sms_toast);
		// 更多
		Button btn_more = (Button) contentView.findViewById(R.id.btn_more);

		String isRelatedNetwork = String.valueOf(virCardItem
				.get(Crcd.CRCD_ISRELATENETWORK));
		// 虚拟信用卡状态
		String status = String.valueOf(virCardItem.get(Crcd.CRCD_STATUS));
		if (ConstantGloble.CRCD_STATUS_ZERO.equals(status)) {
			// 已注销的虚拟卡不显示按钮
			img_crcd_setup.setVisibility(View.GONE);
			img_sms_toast.setVisibility(View.GONE);
			btn_more.setVisibility(View.GONE);
		} else {
			int btnSize = 2;
			if ("1".equals(isRelatedNetwork)) {
				// "CSR".equals(channel) 此条件不在使用
				// CSR=电话银行 关联网银状态0-失败，1-成功
				// 显示关联网银
				btnSize = 3;
				btn_more.setVisibility(View.VISIBLE);
				img_sms_toast.setVisibility(View.GONE);

			} else {
				btnSize = 2;
				btn_more.setVisibility(View.GONE);
				img_sms_toast.setVisibility(View.VISIBLE);
			}
		}
		ImageView img_exit = (ImageView) contentView
				.findViewById(R.id.img_exit_accdetail);

		img_crcd_setup.setOnClickListener(gotoSutepClick);
		img_sms_toast.setOnClickListener(gotoSmsClick);
		String[] selectors = new String[] {
				currentContent
						.getString(R.string.mycrcd_virtualcard_sms_tongzhi),
				currentContent.getString(R.string.mycrcd_creditcard_guanlian) };
		PopupWindowUtils.getInstance().setshowMoreChooseUpListener(
				currentContent, btn_more, selectors, popListener);

		// 退出账户详情点击事件
		img_exit.setOnClickListener(exitVirtualDetailClick);

		return contentView;
	}
}
