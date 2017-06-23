/**
 * 常量定义
 * @namespace constant
 * @name constant
 */
define('constant', function() {
	var Constant = {
		/**
		 * P503改造--hs3693
		 */
		currency_code_: {
			"001": window.app.nls.l_currency_code_001,
			"012": window.app.nls.l_currency_code_012,
			"013": window.app.nls.l_currency_code_013,
			"014": window.app.nls.l_currency_code_014,
			"015": window.app.nls.l_currency_code_015,
			"016": window.app.nls.l_currency_code_016,
			"017": window.app.nls.l_currency_code_017,
			"018": window.app.nls.l_currency_code_018,
			"020": window.app.nls.l_currency_code_020,
			"021": window.app.nls.l_currency_code_021,
			"022": window.app.nls.l_currency_code_022,
			"023": window.app.nls.l_currency_code_023,
			"024": window.app.nls.l_currency_code_024,
			"025": window.app.nls.l_currency_code_025,
			"026": window.app.nls.l_currency_code_026,
			"027": window.app.nls.l_currency_code_027,
			"028": window.app.nls.l_currency_code_028,
			"029": window.app.nls.l_currency_code_029,
			"032": window.app.nls.l_currency_code_032,
			"034": window.app.nls.l_currency_code_034,
			"035": window.app.nls.l_currency_code_035,
			"036": window.app.nls.l_currency_code_036,
			"038": window.app.nls.l_currency_code_038,
			"039": window.app.nls.l_currency_code_039,
			"042": window.app.nls.l_currency_code_042,
			"045": window.app.nls.l_currency_code_045,
			"056": window.app.nls.l_currency_code_056,
			"064": window.app.nls.l_currency_code_064,
			"065": window.app.nls.l_currency_code_065,
			"068": window.app.nls.l_currency_code_068,
			"070": window.app.nls.l_currency_code_070,
			"072": window.app.nls.l_currency_code_072,
			"080": window.app.nls.l_currency_code_080,
			"081": window.app.nls.l_currency_code_081,
			"082": window.app.nls.l_currency_code_082,
			"084": window.app.nls.l_currency_code_084,
			"085": window.app.nls.l_currency_code_085,
			"087": window.app.nls.l_currency_code_087,
			"088": window.app.nls.l_currency_code_088,
			"096": window.app.nls.l_currency_code_096,
			"101": window.app.nls.l_currency_code_101,
			"134": window.app.nls.l_currency_code_134,
			"131": window.app.nls.l_currency_code_131,
			"166": window.app.nls.l_currency_code_166,
			"196": window.app.nls.l_currency_code_196,
			"213": window.app.nls.l_currency_code_213,
			"253": window.app.nls.l_currency_code_253,
			"841": window.app.nls.l_currency_code_841,
			"844": window.app.nls.l_currency_code_844,
			"845": window.app.nls.l_currency_code_845
		},
		currency_sign_: {
			"CNY": window.app.nls.l_currency_sign_CNY,
			"GBP": window.app.nls.l_currency_sign_GBP,
			"HKD": window.app.nls.l_currency_sign_HKD,
			"USD": window.app.nls.l_currency_sign_USD,
			"JPY": window.app.nls.l_currency_sign_JPY,
			"CAD": window.app.nls.l_currency_sign_CAD, //加拿大元
			"AUD": window.app.nls.l_currency_sign_AUD, //澳大利亚元
			"EUR": window.app.nls.l_currency_sign_EUR
		},
        currency_sign_1_: {
            "001": window.app.nls.l_currency_CNY, //人民币
            "014": window.app.nls.l_currency_USD, //美元
            "018": window.app.nls.l_currency_SIN, //新加坡元
            "027": window.app.nls.l_currency_JPY, //日元
            "038": window.app.nls.l_currency_EUR, //欧元
            "012": window.app.nls.l_currency_GBP, //英镑
            "013": window.app.nls.l_currency_HKD, //港币
            "028": window.app.nls.l_currency_CAD, //加拿大元
            "029": window.app.nls.l_currency_AUD //澳大利亚元
        },
		/* 
		 * 汇率计算器  货币码定义
		 */
		currency_sign_ratecalc_: {
			"CNY": window.app.nls.l_currency_sign_ratecalc_CNY,
			"HKD": window.app.nls.l_currency_sign_ratecalc_HKD,
			"USD": window.app.nls.l_currency_sign_ratecalc_USD,
			"KRW": window.app.nls.l_currency_sign_ratecalc_KRW,
			"EUR": window.app.nls.l_currency_sign_ratecalc_EUR,
			"JPY": window.app.nls.l_currency_sign_ratecalc_JPY,
			"AUD": window.app.nls.l_currency_sign_ratecalc_AUD,
			"GBP": window.app.nls.l_currency_sign_ratecalc_GBP
		},
		currency_symbol_: {
			"001": window.app.nls.l_currency_symbol_001, //人民币
			"014": window.app.nls.l_currency_symbol_014, //美元
			"027": window.app.nls.l_currency_symbol_027, //日元
			"038": window.app.nls.l_currency_symbol_038, //欧元
			"012": window.app.nls.l_currency_symbol_012, //英镑
			"013": window.app.nls.l_currency_symbol_013, //港币
			"028": window.app.nls.l_currency_symbol_028, //加拿大元
			"029": window.app.nls.l_currency_symbol_029 //澳大利亚元
		},
		cashremit_type_: {
			"00": window.app.nls.l_cashremit_type_00,
			"01": window.app.nls.l_cashremit_type_01,
			"02": window.app.nls.l_cashremit_type_02
		},
		account_type_: {
			00:window.app.nls.ovs_ds_zhgl_lx00,
		01:window.app.nls.ovs_ds_zhgl_lx01,
		02:window.app.nls.ovs_ds_zhgl_lx02,
		03:window.app.nls.ovs_ds_zhgl_lx03,
		05:window.app.nls.ovs_ds_zhgl_lx05,
		06:window.app.nls.ovs_ds_zhgl_lx06,
		07:window.app.nls.ovs_ds_zhgl_lx07,
		08:window.app.nls.ovs_ds_zhgl_lx08,
		101:window.app.nls.ovs_ds_zhgl_lx101,
		103:window.app.nls.ovs_ds_zhgl_lx103,
		104:window.app.nls.ovs_ds_zhgl_lx104,
		105:window.app.nls.ovs_ds_zhgl_lx105,
		106:window.app.nls.ovs_ds_zhgl_lx106,
		119:window.app.nls.ovs_ds_zhgl_lx119,
		140:window.app.nls.ovs_ds_zhgl_lx140,
		150:window.app.nls.ovs_ds_zhgl_lx150,
		152:window.app.nls.ovs_ds_zhgl_lx152,
		170:window.app.nls.ovs_ds_zhgl_lx170,
		188:window.app.nls.ovs_ds_zhgl_lx188,
		190:window.app.nls.ovs_ds_zhgl_lx190,
		201:window.app.nls.ovs_ds_zhgl_lx201,
		202:window.app.nls.ovs_ds_zhgl_lx202,
		203:window.app.nls.ovs_ds_zhgl_lx203,
		204:window.app.nls.ovs_ds_zhgl_lx204,
		205:window.app.nls.ovs_ds_zhgl_lx205,
		206:window.app.nls.ovs_ds_zhgl_lx206,
		207:window.app.nls.ovs_ds_zhgl_lx207,
		208:window.app.nls.ovs_ds_zhgl_lx208,
		209:window.app.nls.ovs_ds_zhgl_lx209,
		210:window.app.nls.ovs_ds_zhgl_lx210,
		211:window.app.nls.ovs_ds_zhgl_lx211,
		212:window.app.nls.ovs_ds_zhgl_lx212,
		213:window.app.nls.ovs_ds_zhgl_lx213,
		214:window.app.nls.ovs_ds_zhgl_lx214,
		215:window.app.nls.ovs_ds_zhgl_lx215,
		216:window.app.nls.ovs_ds_zhgl_lx216,
		217:window.app.nls.ovs_ds_zhgl_lx217,
		218:window.app.nls.ovs_ds_zhgl_lx218,
		219:window.app.nls.ovs_ds_zhgl_lx219,
		220:window.app.nls.ovs_ds_zhgl_lx220,
		221:window.app.nls.ovs_ds_zhgl_lx221,
		222:window.app.nls.ovs_ds_zhgl_lx222,
		223:window.app.nls.ovs_ds_zhgl_lx223,
		224:window.app.nls.ovs_ds_zhgl_lx224,
		225:window.app.nls.ovs_ds_zhgl_lx225,
		226:window.app.nls.ovs_ds_zhgl_lx226,
		227:window.app.nls.ovs_ds_zhgl_lx227,
		228:window.app.nls.ovs_ds_zhgl_lx228,
		229:window.app.nls.ovs_ds_zhgl_lx229,
		401:window.app.nls.ovs_ds_zhgl_lx401,
		402:window.app.nls.ovs_ds_zhgl_lx402,
		403:window.app.nls.ovs_ds_zhgl_lx403,
		404:window.app.nls.ovs_ds_zhgl_lx404,
		405:window.app.nls.ovs_ds_zhgl_lx405,
		406:window.app.nls.ovs_ds_zhgl_lx406,
		407:window.app.nls.ovs_ds_zhgl_lx407,
		408:window.app.nls.ovs_ds_zhgl_lx408,
		409:window.app.nls.ovs_ds_zhgl_lx409,
		410:window.app.nls.ovs_ds_zhgl_lx410,
		},
		account_status_: {
			"00": window.app.nls.l_account_status_00,
			"01": window.app.nls.l_account_status_01,
			"02": window.app.nls.l_account_status_02,
			"03": window.app.nls.l_account_status_03,
			"04": window.app.nls.l_account_status_04,
			"05": window.app.nls.l_account_status_05,
			"06": window.app.nls.l_account_status_06,
			"07": window.app.nls.l_account_status_07,
			"08": window.app.nls.l_account_status_08,
			"09": window.app.nls.l_account_status_09
		},
		risk_id_type_: {
			"1": window.app.nls.l_risk_id_type_1,
			"2": window.app.nls.l_risk_id_type_2,
			"3": window.app.nls.l_risk_id_type_3,
			"4": window.app.nls.l_risk_id_type_4,
			"5": window.app.nls.l_risk_id_type_5,
			"6": window.app.nls.l_risk_id_type_6,
			"7": window.app.nls.l_risk_id_type_7,
			"8": window.app.nls.l_risk_id_type_8,
			"9": window.app.nls.l_risk_id_type_9,
			"10": window.app.nls.l_risk_id_type_10,
			"11": window.app.nls.l_risk_id_type_11,
			"12": window.app.nls.l_risk_id_type_12,
			"13": window.app.nls.l_risk_id_type_13,
			"14": window.app.nls.l_risk_id_type_14,
			"47": window.app.nls.l_risk_id_type_47,
			"48": window.app.nls.l_risk_id_type_48,
			"49": window.app.nls.l_risk_id_type_49,
			"50": window.app.nls.l_risk_id_type_50,
			"99": window.app.nls.l_risk_id_type_99
		},
		risk_level_: {
			"1": window.app.nls.l_risk_level_1,
			"2": window.app.nls.l_risk_level_2,
			"3": window.app.nls.l_risk_level_3,
			"4": window.app.nls.l_risk_level_4,
			"5": window.app.nls.l_risk_level_5
		},
		risk_level_desc_: {
			"1": window.app.nls.l_risk_level_desc_1,
			"2": window.app.nls.l_risk_level_desc_2,
			"3": window.app.nls.l_risk_level_desc_3,
			"4": window.app.nls.l_risk_level_desc_4,
			"5": window.app.nls.l_risk_level_desc_5
		},
	//	account_type_:{
	//		"101":"普通活期",
	//		"119":"长城电子借记卡",
	//	"140":"存本取息",
	//	"150":"零存整取",
	//	"152":"教育储蓄",
	//	"170":"定期一本通",
	//	"188":"活期一本通",
	//	"190":"网上专属理财账户",
	//	"201":"海外普通活期",
	//	"202":"海外普通定期",
	//	"203":"海外支票",
	//	"204":"贷款账户",
	//	"205":"保证金账户",
	//	"206":"二十四小时通知账户",
	//	"207":"现金管理账户",
	//	"208":"7天通知账户",
	//	"209":"30天通知账户",
	//	"210":"纽分行投资账户",
	//	"211":"海外定期一本通",
	//	"212":"海外活期一本通",
	//	"213":"海外借记卡",
	//	"214":"海外信用卡",
	//	"215":"海外对公借记卡",
	//	"216":"货币市场账户",
	//	"217":"IRA退休定期账户",
	//	"218":"基本支票Basic Checking账户",
	//	"219":"移民监管账户",
	//	"220":"购房投资监管账户",
	//	"221":"免税活期存折储蓄账户",
	//	"222":"退休储蓄活期存折储蓄账户",
	//	"223":"零存整取账户",
	//	"224":"定活两便账户",
	//	"225":"免税定期存款账户",
	//	"226":"退休储蓄定期存款账户",
	//	"227":"活期透支账户",
	//	"228":"注册退休储蓄计划零存整取账户",
	//	"229":"三年步步高定期存款"
    //
	//},
		//新加坡转账汇款用途
		sin_use_id_:[
			"Business Expenses",
			"Bonus Payment",
			"Cable TV Bill",
			"Credit Card Payment",
			"Charity Payment",
			"Collection Payment",
			"Commission",
			"Carpark Charges",
			"Cash Disbursement",
			"Debit Card Payment",
			"Dividend",
			"Dental Services",
			"Education",
			"Payment of Fees & Charges",
			"Foreign Worker Levy",
			"Purchase Sale of Goods",
			"Government Insurance",
			"Goods & Services Tax",
			"Hospital Care",
			"Instalment Hire Purchase Agreement",
			"Insurance Premium",
			"Intra Company Payment",
			"Interest",
			"Investment & Securities",
			"Invoice Payment",
			"Loan",
			"Medical Services",
			"Net Income Tax",
			"Other",
			"Telephone Bill",
			"Property Tax",
			"Road Tax",
			"Rebate",
			"Refund",
			"Rent",
			"Salary Payment",
			"Study",
			"Supplier Payment",
			"Tax Payment",
			"Town Council Service Charges",
			"Trade Services",
			"Treasury Payment",
			"Road Pricing",
			"Utilities",
			"With Holding"],
        sin_use_dict:{
            "Business Expenses":"BEXP",
            "Bonus Payment": "BONU",
            "Cable TV Bill": "CBTV",
            "Credit Card Payment": "CCRD",
            "Charity Payment": "CHAR",
            "Collection Payment": "COLL",
            "Commission": "COMM",
            "Carpark Charges": "CPKC",
            "Cash Disbursement": "CSDB",
            "Debit Card Payment": "DCRD",
            "Dividend": "DIVD",
            "Dental Services": "DNTS",
            "Education": "EDUC",
            "Payment of Fees & Charges": "FCPM",
            "Foreign Worker Levy": "FWLV",
            "Purchase Sale of Goods": "GDDS",
            "Government Insurance": "GOVI",
            "Goods & Services Tax": "GSTX",
            "Hospital Care": "HSPC",
            "Instalment Hire Purchase Agreement": "IHRP",
            "Insurance Premium": "INSU",
            "Intra Company Payment": "INTC",
            "Interest": "INTE",
            "Investment & Securities": "INVS",
            "Invoice Payment": "IVPT",
            "Loan": "LOAN",
            "Medical Services": "MDCS",
            "Net Income Tax": "NITX",
            "Other": "OTHR",
            "Telephone Bill": "PHON",
            "Property Tax": "PTTX",
            "Road Tax": "RDTX",
            "Rebate": "REBT",
            "Refund": "REFU",
            "Rent": "RENT",
            "Salary Payment": "SALA",
            "Study": "STDY",
            "Supplier Payment": "SUPP",
            "Tax Payment": "TAXS",
            "Town Council Service Charges": "TCSC",
            "Trade Services": "TRAD",
            "Treasury Payment": "TREA",
            "Road Pricing": "TRPT",
            "Utilities": "UBIL",
            "With Holding": "WHLD",
        },
		sin_company_id_: {
			"AN_KK_1": ["病历号", "Case NO."],
			"AN_KK_2": ["账单号码", "Bill NO."],
			"AN_NTUC_1": ["保单号", "Policy NO."],
			"AN_NTUC_2": ["账单凭证号", "Bill Ref NO."],
			"br3_CGH_1": ["NRIC", "NRIC"],
			"br3_CGH_2": ["MRN NO.", "MRN NO."],
			"br3_CGH_3": ["HRN NO.", "HRN NO."],
			"br3_KK_1": ["NRIC", "NRIC"],
			"br3_KK_2": ["MRN NO.", "MRN NO."],
			"br3_KK_3": ["HRN NO.", "HRN NO."],
			"br3_NHC_1": ["NRIC", "NRIC"],
			"br3_NHC_2": ["External ID", "External ID"],
			"br3_NTUC_1": ["身份证号", "NRIC"],
			"br3_NTUC_2": ["External ID", "External ID"],
			"br4_CGH_1": ["患者姓名", "Patient Name"],
			"br4_CGH_2": ["联系电话", "Contact NO."],
			"br4_kk_1": ["患者姓名", "Patient Name"],
			"br4_kk_2": ["联系电话", "Contact NO."],
			"br4_kk_3": ["国籍", "Nationality"],
			"br4_PI_1": ["发票号码", "Invoice NO."],
			"br4_PI_2": ["联系电话", "Contact NO."]
		},

		trans_status_:{
			'3': window.app.nls.ovs_trans_3,
			'5': window.app.nls.ovs_trans_5,
			'A': window.app.nls.ovs_trans_A,
				'8': window.app.nls.ovs_trans_8,
				'B': window.app.nls.ovs_trans_B,
				'G': window.app.nls.ovs_trans_G,
				'0': window.app.nls.ovs_trans_0,
				'1': window.app.nls.ovs_trans_1,
				'2': window.app.nls.ovs_trans_2,
				'4': window.app.nls.ovs_trans_4,
				'6': window.app.nls.ovs_trans_6,
				'7': window.app.nls.ovs_trans_7,
				'9': window.app.nls.ovs_trans_9,
				'10': window.app.nls.ovs_trans_10,
				'11': window.app.nls.ovs_trans_11,
				'12': window.app.nls.ovs_trans_12,
				'D': window.app.nls.ovs_trans_D,
				'J': window.app.nls.ovs_trans_J,
				'K': window.app.nls.ovs_trans_K,
				'all': window.app.nls.ovs_trans_all,
		},

		sin_company_id_1_: {
			//"ACE Insurance"
			"20143001": {
				"payeeActno": [
					"ovs_bpa_bp_company_1",
				],
				"billerRef3": [
					"ovs_bpa_bp_company_2",
				],
			},
			//"ANZ Credit Cards"
			"20123001": {
				"payeeActno": [
					"ovs_bpa_bp_company_3",
				],
			},
			//"BOC Credit Cards"
				"20176001": {
				"payeeActno": [
					"ovs_bpa_bp_company_3",
				],
			},
			//"Changi General Hospital"
				"20037001": {
				"payeeActno": [
					"ovs_bpa_bp_company_4",
				],
				"billerRef3": [
					"","ovs_bpa_bp_company_5", "ovs_bpa_bp_company_6", "ovs_bpa_bp_company_7",
				],
				"billerRef4": [
					"", "ovs_bpa_bp_company_8", "ovs_bpa_bp_company_9",
				],
			},
			//"CIMB Credit Cards"
				"20204001": {
				"payeeActno": [
					["ovs_bpa_bp_company_3"]
				]
			},
			//"Courts (S) Pte Ltd"
				"20149001": {
				"payeeActno": [
					"ovs_bpa_bp_company_10"
				],
				"billerRef3": [
					"ovs_bpa_bp_company_11"

				]
			},
			//"DBS CREDIT CARDS"
				"20063001": {
				"payeeActno": [
					"ovs_bpa_bp_company_3"
				]
			},
			//"Diners Club"
				"20083001": {
				"payeeActno": [
					"ovs_bpa_bp_company_3"
				]
			},
			//"HSBC Credit Cards"
				"20068001": {
				"payeeActno": [
					"ovs_bpa_bp_company_3"
				]
			},
			//"ICBC - Renminbi Cards"
				"20234001": {
				"payeeActno": [
					"ovs_bpa_bp_company_3"
				]
			},
			//"ICBC-Spore Cards"
				"20234002": {
				"payeeActno": [
					"ovs_bpa_bp_company_3"
				]

			},
			//"IRAS - Income Tax(Tax Ref No.)"
				"20010001": {
				"payeeActno": [
					"ovs_bpa_bp_company_12"
				],
				"billerRef1": [
					"ovs_bpa_bp_company_13"
				],
				"billerRef3": [
					"ovs_bpa_bp_company_14"

				],
				"billerRef4": [
					"ovs_bpa_bp_company_15"
				],
			},
			//"IRAS - Others(Payment Voucher No.)"
				"20010002": {

				"payeeActno": [
					"ovs_bpa_bp_company_10"
				],
				"billerRef1": [
					"ovs_bpa_bp_company_13"
				],
				"billerRef3": [
					"ovs_bpa_bp_company_14"
				],
				"billerRef4": [
					"ovs_bpa_bp_company_15"
				],
			},
			//"KK Women's & Children's Hospital"
				"20040001": {
				"payeeActno": [
					"","ovs_bpa_bp_company_16", "ovs_bpa_bp_company_17_KK"
				],
				"billerRef3":[
					"","ovs_bpa_bp_company_5","ovs_bpa_bp_company_6","ovs_bpa_bp_company_7"
				],
				"billerRef4": [
					"","ovs_bpa_bp_company_18", "ovs_bpa_bp_company_26", "ovs_bpa_bp_company_9"
				],
					//"starbillerRef4":[true, false, false],//不显示*
			},
			//"LTA - Traffic Police"
				"20023001": {
				"payeeActno": [
					"ovs_bpa_bp_company_20"
				],
				"billerRef1": [
					"ovs_bpa_bp_company_21"
				]
			},
			//"Maybank Credit Cards"
				"20081001": {
				"payeeActno": [
					"ovs_bpa_bp_company_3"
				]
			},
			//"Mobile One"
			"20003002": {
				"payeeActno": [
					"ovs_bpa_bp_company_10",
				]
			},
			//"National Heart Centre"
				"20038001": {
				"payeeActno": [
					"ovs_bpa_bp_company_17"
				],
				"billerRef3": [
					"","ovs_bpa_bp_company_5","ovs_bpa_bp_company_22"
				],
				"billerRef4": [
					"ovs_bpa_bp_company_9"
				]
			},
			//"NTUC Income"
				"20103001": {
				"payeeActno": [
					"","ovs_bpa_bp_company_1","ovs_bpa_bp_company_29"
				],
				"billerRef3": [
					"","ovs_bpa_bp_company_5","ovs_bpa_bp_company_19"
				],
				"billerRef4": [
					"ovs_bpa_bp_company_23"
				]
			},
			//"OCBC Credit Cards"
				"20170001": {
				"payeeActno": [
					"ovs_bpa_bp_company_3"
				]
			},
			//"OCBC Plus"
				"20170003": {
				"payeeActno": [
					"ovs_bpa_bp_company_3"
				]
			},
			//"Pacific Internet"
			"20007001": {
				"payeeActno": [
					"ovs_bpa_bp_company_10"
				],
				"billerRef4": [
					"","ovs_bpa_bp_company_24","ovs_bpa_bp_company_11",

				]
			},
			//"Phoenix Communications"
			"20076001": {
				"payeeActno": [
					"ovs_bpa_bp_company_10"
				]
			},
			//"POSB CREDIT CARDS"
				"20063002": {
				"payeeActno": [
					"ovs_bpa_bp_company_3"
				]
			},
			//"SCB Credit Cards"
				"20157001": {
				"payeeActno": [
					"ovs_bpa_bp_company_3"
				]
			},
			//"Singapore Power Services(SP Services)"
				"20029001": {
				"payeeActno": [
					"ovs_bpa_bp_company_10"
				]
			},
			//"Singapore Press Holdings"
				"20106001": {
				"payeeActno": [
					"ovs_bpa_bp_company_10"
				],
				"billerRef3": [
					"ovs_bpa_bp_company_25"
				],
				"billerRef4": [
					"ovs_bpa_bp_company_9"
				]
			},
			//"Singtel"
			"20002001": {
				"payeeActno": [
					"ovs_bpa_bp_company_10"
				],
			},
			//"StarHub"
			"20005001": {
				"payeeActno": [
					"ovs_bpa_bp_company_10"
				],
			},
			//"Sunpage"
				"20008001": {
				"payeeActno": [
					"ovs_bpa_bp_company_10"
				],
			},
			//"UOB Credit"
				"20062001": {
				"payeeActno": [
					"ovs_bpa_bp_company_3"
				],
			},
			//"Zone 1511"
				"20181001": {
				"payeeActno": [
					"ovs_bpa_bp_company_10"
				],
			}
			//KK Women 's & Children's Hospital 页面描述 Country Type Prefix Biller’ s Ref 4 国籍 Nationality
		},

       sin_org_input_sel_type:{
           "empty":"00",//00为空
           "onlyInput":"01",//01为输入框
           "onlySelectD":"02",//02为选择框，选择后动态添加输入框
           "onlySelectS":"03",//为选择框，不动态添加框
           "InputAndSelectS":"04",//多个选择框（不动态添加）和输入框 exp: kk
           "multInput":"05" //多个输入框

       },

        sin_company_id_2_: {

            "20143001": {//  ACE Insurance

                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_policyno",//保单号
                    "validate":"{required:true,minLength:'16',maxLength:'16',reg192:'reg192'}",//验证规则
                    "tips":"ovs_bps_bpm_shoufeibaodanbukong,ovs_bps_bpm_maximumlength16characters,ovs_bps_bpm_maximumlength16characters,ovs_bps_bpm_maximumlength16characters"
                 },
                "billerRef3":{
                    "id":"billerRef3",
                    "type":"01",
                    "text":"ovs_bps_bpm_paymentmonth",//显示输入框文字 缴费年月
                    "validate":"{required:true,minLength:'4',maxLength:'4',reg193:'reg193'}",//验证规则
                    "tips":"ovs_bps_bpm_shoufeijiaofeibukong,ovs_bps_bpm_maximumlength4characters,ovs_bps_bpm_maximumlength4characters,ovs_bps_bpm_maximumlength4characters"
                },

                "billerRef2": {},
                "billerRef1": {},
                "billerRef4": {}

            },
            "20123001": {//20123001 ANZ Credit Cards
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字 信用卡号
                    "validate":"{required:true,minLength:'15',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_anzxinyongbukong,ovs_bps_bpm_anzminimumlength15characters,ovs_bps_bpm_anzminimumlength15characters,ovs_bps_bpm_anzminimumlength15characters"

                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20176001": {// BOC Credit Cards
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字 信用卡号
                    "validate":"{required:true,minLength:'16',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_bocxinyongbukong,ovs_bps_bpm_bocinput16characters,ovs_bps_bpm_bocinput16characters,ovs_bps_bpm_bocinput16characters"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20037001": {// Changi General Hospital

                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_tax",//显示输入框文字 税单号
                    "validate":"{required:true,reg204:'reg204'}",//验证规则
                    "tips":"ovs_bps_bpm_hospitalshuidanbukong,ovs_bps_bpm_input9or10characters"
                },
                "billerRef3":{
                    "id":"billerRef3",
                    "type":"02",
                    "tips":"ovs_bps_bpm_zhengjianleixingbukong",
                    "select":{
                        "text":"ovs_bps_bpm_documenttype",//显示文字
                        "opt":[
                            {
                                "text":"NRIC",//显示输入框文字
                                "validate":"{required:true,maxLength:'9',reg8:'reg8'}",//验证规则
                                "tips":"ovs_bps_bpm_hospitalnricbukong,ovs_bps_bpm_maximumlength9characters,ovs_bps_bpm_maximumlength9characters"
                            },
                            {
                                "text":"MRN NO.",//显示输入框文字
                                "validate":"{required:true,maxLength:'10',reg8:'reg8'}",//验证规则
                                "tips":"ovs_bps_bpm_hospitalmrnbukong,ovs_bps_bpm_mrnmaximumlength10characters,ovs_bps_bpm_mrnmaximumlength10characters"
                            },
                            {
                                "text":"HRN NO.",//显示输入框文字
                                "validate":"{required:true,maxLength:'12',reg8:'reg8'}",//验证规则
                                "tips":"ovs_bps_bpm_hospitalhrnbukong,ovs_bps_bpm_hrnmaximumlength10characters,ovs_bps_bpm_hrnmaximumlength10characters"
                            }
                        ]

                    }
                },

                "billerRef4":{
                    "id":"billerRef4",
                    "type":"05",
                    "text":{
                        "input":[
                            {
                                "text":"ovs_bps_bpm_patientname",//患者姓名
                                "validate":"{required:true,minLength:'1',maxLength:'30'}",//验证规则
                                "tips":"ovs_bps_bpm_hospitalnamebukong,ovs_bps_bpm_maximumlength30characters,ovs_bps_bpm_maximumlength30characters"
                            },
                            {
                                "text":"ovs_bps_bpm_contactno",//联系电话
                                "validate":"{required:true,minLength:'8',maxLength:'8',reg194:'reg194'}",//验证规则
                                "tips":"ovs_bps_bpm_courtsphonebukong,ovs_bps_bpm_inputfrom6or8or9,ovs_bps_bpm_inputfrom6or8or9,ovs_bps_bpm_inputfrom6or8or9"
                            }

                        ]

                    }
                },

                "billerRef2": {},
                "billerRef1": {}

            },
            "20204001": {//20204001 CIMB Credit Cards
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字 信用卡号
                    "validate":"{required:true,minLength:'16',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_cimbxinyongbukong,ovs_bps_bpm_cimbinput16characters,ovs_bps_bpm_cimbinput16characters,ovs_bps_bpm_cimbinput16characters"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20149001": {//20149001 Courts (S) Pte Ltd
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billrefno",//显示输入框文字 付款凭证号
                    "validate":"{required:true,reg206:'reg206',maxLength:'16'}",//验证规则
                    "tips":"ovs_bps_bpm_courtsfukuanbukong,ovs_bps_bpm_input12or16characters,ovs_bps_bpm_input12or16characters"
                },

                "billerRef3":{
                    "id":"billerRef3",
                    "type":"01",
                    "text":"ovs_bps_bpm_contactinformation",//显示输入框文字  联系方式
                    "validate":"{required:true,reg194:'reg194'}",//验证规则
                    "tips":"ovs_bps_bpm_courtsphonebukong,ovs_bps_bpm_inputfrom6or8or9"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": []

            },
            "20063001": {//20063001 DBS CREDIT CARDS
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字
                    "validate":"{required:true,minLength:'15',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_dbsxinyongbukong,ovs_bps_bpm_dbsminimumlength15characters,ovs_bps_bpm_dbsminimumlength15characters,ovs_bps_bpm_dbsminimumlength15characters"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20083001": {//20083001 Diners Club
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字
                    "validate":"{required:true,minLength:'14',maxLength:'14',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_dinersxinyongbukong,ovs_bps_bpm_dinersmaximumlength14characters,ovs_bps_bpm_dinersmaximumlength14characters,ovs_bps_bpm_dinersmaximumlength14characters"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20068001": {//20068001 HSBC Credit Cards
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字
                    "validate":"{required:true,minLength:'16',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_hsbcxinyongbukong,ovs_bps_bpm_hsbcinput16characters,ovs_bps_bpm_hsbcinput16characters,ovs_bps_bpm_hsbcinput16characters"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20234001": {//20234001 ICBC-Renminbi Cards
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字
                    "validate":"{required:true,minLength:'16',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_icbcxinyongbukon,ovs_bps_bpm_icbcinput16characters,ovs_bps_bpm_icbcinput16characters,ovs_bps_bpm_icbcinput16characters"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20234002": {//20234002 ICBC-Spore Cards
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字
                    "validate":"{required:true,minLength:'16',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_icbcsporexinyongbukong,ovs_bps_bpm_icbc2input16characters,ovs_bps_bpm_icbc2input16characters,ovs_bps_bpm_icbc2input16characters"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []

            },
            "20010001": {//20010001 IRAS - Income Tax (Tax Ref No.)

                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_taxrefno",//显示输入框文字 税务号
                    "validate":"{required:true,minLength:'8',maxLength:'13',reg207:'reg207'}",//验证规则
                    "tips":"ovs_bps_bpm_irasshuiwubukong,ovs_bps_bpm_inputfrom8or13,ovs_bps_bpm_inputfrom8or13,ovs_bps_bpm_inputfrom8or13"
                },

                "billerRef1":{
                    "id":"billerRef1",
                    "type":"03",
                    "tips":"ovs_bps_bpm_irasshuileixingbukong",
                    "select":{
                        "text":"ovs_bps_bpm_taxtype",//显示文字
                        "opt":[
                            {
                                "text":"Individual Income Tax",//显示输入框文字
                                "validate":"",//验证规则
                                "tips":""
                            },
                            {
                                "text":"Other Tax Types",//显示输入框文字
                                "validate":"",//验证规则
                                "tips":""
                            },
                            {
                                "text":"Property Tax",//显示输入框文字
                                "validate":"",//验证规则
                                "tips":""
                            },
                            {
                                "text":"Composition Fees (For Late/Non Filing)",//显示输入框文字
                                "validate":"",//验证规则
                                "tips":""
                            }
                        ]

                    }
                },


                "billerRef3": [],
                "billerRef4": [],
                "billerRef2": []

            },
            "20010002": {//20010002 IRAS - Others (Payment Voucher No.)


                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billrefno",//显示输入框文字
                    "validate":"{required:true,reg57:'reg57',minLength:'14',maxLength:'14'}",//验证规则
                    "tips":"ovs_bps_bpm_irasfukuanbukong,ovs_bps_bpm_irasmaximumlength14characters,ovs_bps_bpm_irasmaximumlength14characters,ovs_bps_bpm_irasmaximumlength14characters"
                },
                "billerRef3":{

                },

                "billerRef1":{
                    "id":"billerRef1",
                    "type":"03",
                    "tips":"ovs_bps_bpm_irasshuileixingbukong",
                    "select":{
                        "text":"ovs_bps_bpm_taxtype",//显示文字  税费类型
                        "opt":[
                            {
                                "text":"Individual Income Tax",//显示输入框文字
                                "validate":"",//验证规则
                                "tips":""

                            },
                            {
                                "text":"Other Tax Types",//显示输入框文字
                                "validate":"",//验证规则
                                "tips":""
                            },
                            {
                                "text":"Property Tax",//显示输入框文字
                                "validate":"",//验证规则
                                "tips":""
                            },
                            {
                                "text":"Composition Fees (For Late/Non Filing)",//显示输入框文字
                                "validate":"",//验证规则
                                "tips":""
                            }
                        ]

                    }
                },

                "billerRef2": {},
                "billerRef4": {}

            },
            "20040001": {//KK Women's & Children's Hospital
                "payeeActno":{

                    "id":"payeeActno",
                    "type":"02",
                    "tips":"ovs_bps_bpm_fukuanpingzhengbukong",
                    "select":{
                        "text":"ovs_bps_bpm_billrefno",//显示文字 付款凭证号
                        "opt":[
                                {
                                    "text":"ovs_bps_bpm_caseno",//显示输入框文字
                                    "validate":"{required:true,reg209:'reg209'}",//验证规则
                                    "tips":"ovs_bps_bpm_kkHospitalbinglibukong,ovs_bps_bpm_hospitalbinglihao"
                                },
                                {
                                    "text":"ovs_bps_bpm_billno",//显示输入框文字
                                    "validate":"{required:true,reg209:'reg209'}",//验证规则
                                    "tips":"ovs_bps_bpm_kkHospitalzhangdanbukong,ovs_bps_bpm_hospitalzhangdanhao"
                                 }
                        ]

                    }
                },
                "billerRef3":{
                    "id":"billerRef3",
                    "type":"02",
                    "tips":"ovs_bps_bpm_zhengjianleixingbukong",
                    "select":{
                        "text":"ovs_bps_bpm_documenttype",//显示文字
                        "opt":[
                            {
                                "text":"NRIC",//显示输入框文字
                                "validate":"{required:true,maxLength:'9',reg8:'reg8'}",//验证规则
                                "tips":"ovs_bps_bpm_kkHospitalnricbukong,ovs_bps_bpm_hospitalnric,ovs_bps_bpm_hospitalnric"
                            },
                            {
                                "text":"MRN NO.",//显示输入框文字
                                "validate":"{required:true,maxLength:'10',reg8:'reg8'}",//验证规则
                                "tips":"ovs_bps_bpm_kkHospitalmrnbukong,ovs_bps_bpm_hospitalmrnnumber,ovs_bps_bpm_hospitalmrnnumber"
                            },
                            {
                                "text":"HRN NO.",//显示输入框文字
                                "validate":"{required:true,maxLength:'12',reg8:'reg8'}",//验证规则
                                "tips":"ovs_bps_bpm_kkHospitalhrnbukong,ovs_bps_bpm_hospitalhrnnumber,ovs_bps_bpm_hospitalhrnnumber"
                            }
                        ]

                    }
                },

                "billerRef4":{
                    "id":"billerRef4",
                    "type":"04",
                    "text":{
                        "input":[
							{
								"text": "ovs_bps_bpm_patientname",//显示输入框文字 患者姓名
								"validate": "{required:true,minLength:'1',maxLength:'30'}",//验证规则
								"tips": "ovs_bps_bpm_kkHospitalnamebukong,ovs_bps_bpm_inputfrom1to30,ovs_bps_bpm_inputfrom1to30"
							},
							{
                                "text":"ovs_bps_bpm_contactno",//显示输入框文字
                                "validate":"{maxLength:'8',reg194:'reg194'}",//验证规则
                                "tips":"ovs_bps_bpm_inputfrom6or8or9,ovs_bps_bpm_inputfrom6or8or9"
                            }

                        ]

                    },//显示文字
                    "select":{
                        "text":"ovs_bps_bpm_nationality", //"国籍"
                        "opt":[
                            {
                                "text":"ovs_bps_bpm_singaporean",//显示输入框文字 新加坡国籍
                                "validate":"",//验证规则
                                "tips":""
                            },
                            {
                                "text":"ovs_bps_bpm_foreigner",//显示输入框文字 外国国籍
                                "validate":"",//验证规则
                                "tips":""
                            }

                        ]

                    }
                },

                "billerRef2":{},
                "billerRef1": {}
            },
            "20023001": {// LTA-Traffic Police
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_ticketno",//显示输入框文字 票务号
                    "validate":"{required:true,minLength:'16',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_ltapiaowubukong,ovs_bps_bpm_ltapiaowuhao,ovs_bps_bpm_ltapiaowuhao,ovs_bps_bpm_ltapiaowuhao"
                },
                "billerRef1":{
                    "id":"billerRef1",
                    "type":"01",
                    "text":"ovs_bps_bpm_vehicleno",//显示输入框文字 车牌号码
                    "validate":"{required:true,minLength:'1',maxLength:'8',reg9:'reg9'}",//验证规则
                    "tips":"ovs_bps_bpm_ltachepaibukong,ovs_bps_bpm_ltachepaihao,ovs_bps_bpm_ltachepaihao,ovs_bps_bpm_ltachepaihao"
                },

                "billerRef2": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20081001": {//20081001 Maybank Credit Cards
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字
                    "validate":"{required:true,minLength:'1',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_maybankxinyongbukong,ovs_bps_bpm_maybankxinyongka,ovs_bps_bpm_maybankxinyongka,ovs_bps_bpm_maybankxinyongka"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20003002": {//20003002 Mobile One

                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billrefno",//显示输入框文字  付款凭证号
                    "validate":"{required:true,minLength:'9',maxLength:'9',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_mobilefukuanbukong,ovs_bps_bpm_mobilefukuanhao,ovs_bps_bpm_mobilefukuanhao,ovs_bps_bpm_mobilefukuanhao"
                },

                "billerRef2": {},
                "billerRef1": {},
                "billerRef4": {},
                "billerRef3": {}
            },
            "20038001": {//20038001 National Heart Centre
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billno",//显示输入框文字  账单号
                    "validate":"{required:true,reg205:'reg205'}",//验证规则
                    "tips":"ovs_bps_bpm_nationalzhangdanbukong,ovs_bps_bpm_nationalzhangdan"
                },
                "billerRef3":{
                    "id":"billerRef3",
                    "type":"02",
                    "tips":"ovs_bps_bpm_zhengjianleixingbukong",
                    "select":{
                        "text":"ovs_bps_bpm_documenttype",//显示文字
                        "opt":[
                            {
                                "text":"NRIC",//显示输入框文字
                                "validate":"{required:true,minLength:'9',maxLength:'9',reg8:'reg8'}",//验证规则
                                "tips":"ovs_bps_bpm_nationalnricbukong,ovs_bps_bpm_nationalnric,ovs_bps_bpm_nationalnric,ovs_bps_bpm_nationalnric"
                            },
                            {
                                "text":"External ID",//显示输入框文字
                                "validate":"{required:true,minLength:'12',maxLength:'12',reg8:'reg8'}",//验证规则
                                "tips":"ovs_bps_bpm_nationalidbukong,ovs_bps_bpm_nationalexternal,ovs_bps_bpm_nationalexternal,ovs_bps_bpm_nationalexternal"
                            }
                        ]

                    }
                },

                "billerRef4":{
                    "id":"billerRef4",
                    "type":"01",
                    "text":"ovs_bps_bpm_contactno",//显示输入框文字
					"validate":"{reg194:'reg194'}",//验证规则
					"tips":"ovs_bps_bpm_pacificphone"
                },

                "billerRef2": {},
                "billerRef1": {}

            },
            "20103001": {//20103001 NTUC Income
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"02",
                    "tips":"ovs_bps_bpm_billtypebukong",
                    "select":{
                        "text":"ovs_bps_bpm_billtype",//显示文字
                        "opt":[
                            {
                                "text":"ovs_bps_bpm_policyno",//显示输入框文字  保单号
                                "validate":"{required:true,minLength:'4',maxLength:'10',reg57:'reg57'}",//验证规则
                                "tips":"ovs_bps_bpm_ntucbaodanbukong,ovs_bps_bpm_ntucbaodanhao,ovs_bps_bpm_ntucbaodanhao,ovs_bps_bpm_ntucbaodanhao"
                            },
                            {
                                "text":"ovs_bps_bpm_number",//显示输入框文字 账单凭证号
                                "validate":"{required:true,minLength:'11',maxLength:'11',reg57:'reg57'}",//验证规则
                                "tips":"ovs_bps_bpm_nutcfukuanbukong,ovs_bps_bpm_ntuczhangdanhao,ovs_bps_bpm_ntuczhangdanhao,ovs_bps_bpm_ntuczhangdanhao"
                            }
                        ]

                    }

                },

                "billerRef3":{
                    "id":"billerRef3",
                    "type":"02",
                    "tips":"ovs_bps_bpm_zhengjianleixingbukong",

                    "select":{
                        "text":"ovs_bps_bpm_documenttype",//显示文字
                        "opt":[
                            {
                                "text":"NRIC",//显示输入框文字
                                "validate":"{required:true,minLength:'9',maxLength:'9',reg8:'reg8'}",//验证规则
                                "tips":"ovs_bps_bpm_ntucnricbukong,ovs_bps_bpm_ntucnric,ovs_bps_bpm_ntucnric,ovs_bps_bpm_ntucnric"
                            },
                            {
                                "text":"ovs_bps_bpm_passportno",//显示输入框文字 护照号码
                                "validate":"{required:true,minLength:'8',maxLength:'20',reg8:'reg8'}",//验证规则
                                "tips":"ovs_bps_bpm_ntuchuzhaobukong,ovs_bps_bpm_ntuchuzhaohao,ovs_bps_bpm_ntuchuzhaohao,ovs_bps_bpm_ntuchuzhaohao"
                            }
                        ]

                    }
                },
                "billerRef4":{
                    "id":"billerRef4",
                    "type":"01",
                    "text":"ovs_bps_bpm_client_sname",//显示输入框文字  客户姓名
                    "validate":"{maxLength:'30'}",//验证规则
                    "tips":"ovs_bps_bpm_ntucname3to30"
                },

                "billerRef2": [],
                "billerRef1": []
            },
            "20170001": {//20170001 OCBC Credit Cards
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字
                    "validate":"{required:true,minLength:'16',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_ocbcxinyongbukong,ovs_bps_bpm_ocbcxinyongka,ovs_bps_bpm_ocbcxinyongka,ovs_bps_bpm_ocbcxinyongka"
                },

                "billerRef2": {},
                "billerRef1": {},
                "billerRef4": {},
                "billerRef3": {}
            },
            "20170003": {//20170003 OCBC Plus
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字
                    "validate":"{required:true,minLength:'16',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_ocbcplusxinyongbukong,ovs_bps_bpm_ocbcplusxinyongka,ovs_bps_bpm_ocbcplusxinyongka,ovs_bps_bpm_ocbcplusxinyongka"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20007001": {//20007001 Pacific Internet
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billrefno",//显示输入框文字
                    "validate":"{required:true,minLength:'1',maxLength:'6',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_pacificfukuanbukong,ovs_bps_bpm_pacificfukuanhao,ovs_bps_bpm_pacificfukuanhao,ovs_bps_bpm_pacificfukuanhao"
                },
                "billerRef4":{
                    "id":"billerRef4",
                    "type":"02",
                    "tips":"ovs_bps_bpm_contactinformationkong",
                    "select":{
                        "text":"ovs_bps_bpm_contactinformation",//"ovs_bps_bpm_documenttype",//显示文字
                        "opt":[
                            {
                                "text":"ovs_bps_bpm_invoicenumber",//显示输入框文字  发票号码
                                "validate":"{required:true,minLength:'8',maxLength:'12',reg57:'reg57'}",//验证规则
                                "tips":"ovs_bps_bpm_pacificfapiaobukong,ovs_bps_bpm_pacificfapiaohao,ovs_bps_bpm_pacificfapiaohao,ovs_bps_bpm_pacificfapiaohao"
                            },
                            {
                                "text":"ovs_bps_bpm_contactno",//显示输入框文字 联系电话
                                "validate":"{required:true,reg194:'reg194'}",//验证规则
                                "tips":"ovs_bps_bpm_pacificphonebukong,ovs_bps_bpm_pacificphone"
                            }
                        ]

                    }
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef3": []

            },
            "20076001": {//20076001 Phoenix Communications
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billrefno",//显示输入框文字
                    "validate":"{required:true,minLength:'9',maxLength:'10',reg8:'reg8'}",//验证规则
                    "tips":"ovs_bps_bpm_phoenixfukuanbukong,ovs_bps_bpm_phoenixfukuanhao,ovs_bps_bpm_phoenixfukuanhao,ovs_bps_bpm_phoenixfukuanhao"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20063002": {//20063002 POSB CREDIT CARDS
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字 付款凭证号
                    "validate":"{required:true,minLength:'15',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_posbxinyongbukong,ovs_bps_bpm_posbxinyongka,ovs_bps_bpm_posbxinyongka,ovs_bps_bpm_posbxinyongka"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20157001": {//20157001 SCB Credit Cards
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字
                    "validate":"{required:true,minLength:'16',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_scbxinyongbukong,ovs_bps_bpm_scbxinyongka,ovs_bps_bpm_scbxinyongka,ovs_bps_bpm_scbxinyongka"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20029001": {//20029001 Singapore Power Services (SP Services)
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billrefno",//显示输入框文字 付款凭证号
                    "validate":"{required:true,reg203:'reg203'}",//验证规则
                    "tips":"ovs_bps_bpm_singaporefukuanbukong,ovs_bps_bpm_singaporefukuanhao"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20106001": {//Singapore Press Holdings
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billrefno",//显示输入框文字
                    "validate":"{required:true,reg195:'reg195'}",//验证规则
                    "tips":"ovs_bps_bpm_singaporepressfukuanbukong,ovs_bps_bpm_singaporepressfukuanhao"
                },
                "billerRef3":{
                    "id":"billerRef3",
                    "type":"01",
                    "text":"ovs_bps_bpm_name",//显示输入框文字 姓名
                    "validate":"{required:true,minLength:'2',maxLength:'40'}",//验证规则
                    "tips":"ovs_bps_bpm_singaporenamebukong,ovs_bps_bpm_singaporename,ovs_bps_bpm_singaporename"
                },
                "billerRef4":{
                    "id":"billerRef4",
                    "type":"01",
                    "text":"ovs_bps_bpm_contactno",//显示输入框文字
                    "validate":"{required:true,reg194:'reg194'}",//验证规则
                    "tips":"ovs_bps_bpm_singaporephonebukong,ovs_bps_bpm_singaporephone"
                },

                "billerRef2": [],
                "billerRef1": []
            },
            "20002001": {// Singtel
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billrefno",//显示输入框文字
                    "validate":"{required:true,minLength:'8',maxLength:'8',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_singtelfukuanbukong,ovs_bps_bpm_singtelfukuanhao,ovs_bps_bpm_singtelfukuanhao,ovs_bps_bpm_singtelfukuanhao"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20005001": {//StarHub

                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billrefno",//显示输入框文字
                    "validate":"{required:true,reg202:'reg202'}",//验证规则
                    "tips":"ovs_bps_bpm_starhubfukuanbukong,ovs_bps_bpm_starhubfukuanhao"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },


            "20008001": { //Sunpage
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billrefno",//显示输入框文字
                    "validate":"{required:true,minLength:'8',maxLength:'8',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_sunpagefukuanbukong,ovs_bps_bpm_sunpagefukuanhao,ovs_bps_bpm_sunpagefukuanhao,ovs_bps_bpm_sunpagefukuanhao"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20062001": {//UOB Credit Cards
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_creditcardno",//显示输入框文字
                    "validate":"{required:true,minLength:'15',maxLength:'16',reg57:'reg57'}",//验证规则
                    "tips":"ovs_bps_bpm_uobxinyongbukong,ovs_bps_bpm_uobxinyongka,ovs_bps_bpm_uobxinyongka,ovs_bps_bpm_uobxinyongka"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            },
            "20181001": {//Zone 1511
                "payeeActno":{
                    "id":"payeeActno",
                    "type":"01",
                    "text":"ovs_bps_bpm_billrefno",//显示输入框文字
                    "validate":"{required:true,minLength:'9',maxLength:'9',reg8:'reg8'}",//验证规则
                    "tips":"ovs_bps_bpm_zonefukuanbukong,ovs_bps_bpm_zonefukuanhao,ovs_bps_bpm_zonefukuanhao,ovs_bps_bpm_zonefukuanhao"
                },

                "billerRef2": [],
                "billerRef1": [],
                "billerRef4": [],
                "billerRef3": []
            }
            //KK Women 's & Children's Hospital 页面描述 Country Type Prefix Biller’ s Ref 4 国籍 Nationality
        }


	};
	return Constant;
});