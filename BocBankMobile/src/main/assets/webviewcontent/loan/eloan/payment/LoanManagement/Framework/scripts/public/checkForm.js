/**
 * 表单验证
 * @author public 
 */
/**
 *  表单验证
 * @namespace CheckForm
 * @name CheckForm
 * @requires zepto， common
 */
define("checkForm", ['zepto', 'common'], function($, Common) {
	/**
	 *  正则集合,私有类，CheckForm内部使用
	 * @namespace CheckForm.Validater
	 * @name Validater
	 * @class
	 */
	var Validater = function() {
		var obj = new Object();
		obj["reg01"] = { regExp: /^([1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}[0-9Xx]{1}$)$/, validator: function(str){
			var w = [7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1];
			var a = [1,0,'X',9,8,7,6,5,4,3,2];
			str = str.toUpperCase();
			var s=0, a1;
			for(var i=0; i<17; i++){
				s += w[i]*str[i]
			}
			a1 = a[s%11];
			if(str[17] == a1){
				return true;
			}
			return false;
		}};//身份证
		
		obj["reg02"] = { regExp: /^[a-zA-Z0-9\u4e00-\u9fa5\-\(\)]{1,32}$/};//其他证件类型
		
		//obj["reg_Name"]={regExp:/^[\w\u4E00-\u9FA5]{1,24}$/};//用户姓名
		obj["reg_Name"]={regExp:/^[a-zA-Z\u4E00-\u9FA5]{1,20}$/};//用户姓名,1-20位的数字，字母，汉字
		obj["regSms"]={regExp: /^[0-9]*$/};//短信（只能输入数字）
		obj["regMoney"]={regExp: /^(([1-9]\d{0,9})|0)(\.\d{1,2})?$/};
		obj["regCode"]={regExp: /^[A-Za-z0-9]+$/};//只能输入数字和英文字母
		obj["regPhoneNumber"]={regExp:/^1\d{10}$/};//手机号码
		obj["regName_fg"]={regExp:/^[0-9a-zA-Z\u4E00-\u9FA5]{1,}[0-9a-zA-Z\u4E00-\u9FA5·.,\s]{0,}$/};//自助填单用户名，在这里不限长度
		obj["regFirstName_fg"]={regExp:/^[0-9a-zA-Z\u4E00-\u9FA5·.,\s]{0,}$/};//自助填单用户名字，在这里不限长度
		obj["regAmount_fg"]={regExp: /^(([1-9]\d{0,11})|0)(\.\d{1,2})?$/};//金额，12位整数，2位小数
		obj["regAmountZheng_fg"]={regExp: /^(([1-9]\d{0,11})|0)(\.\d{1,})?$/};//金额，12位整数
		obj["regAmountXiao_fg"]={regExp: /^(([1-9]\d{0,})|0)(\.\d{1,2})?$/};//金额，2位小数
		obj["pyName_fg"]={regExp:/^[a-zA-Z\s]{1,}$/};//自助填单用户姓名拼音，在这里不限长度
		obj["osRegNameAddr"] = {regExp:/^[A-Z0-9\{\}\(\)\-\+\:\'\,\./\?\ ]{0,}$/};//境外汇款用户名和地址[A-Z0-9\(\)\-\+\:\'\,\./\?\ ]
		obj["noZero_sf"] = {regExp:/^\d*[1-9]\d*(\.\d+)?$|^0+(\.\d*[1-9]+\d*)$/};//现汇购汇其他金额不能为0
		obj["payeeAccNum_sf"] = {regExp:/^[^\[^\]^\^^\$^\\^\~^\@^\#^\%^\&^\<^\>^\{^\}]{0,}$/};//境外汇账号不包含特殊字符
        obj["reg57"] = { regExp: /^[0-9]*$/ };//tips0266 请输入0-9的数字
        obj["reg15"] = { regExp: /^[A-Za-z0-9\/\-+?().,' ]+$/ };//tips0224 请输入字母、数字，可包含/-+?().,'
		obj["reg517"] = { regExp: /^(?!^0*(\.0{1,2})?$)^\d{1,13}(\.\d{1,2})?$/ };//tips0342 请输入不超过13位整数，2位小数的金额
        obj["reg42"] = { regExp: /^[1-9]\d{0,7}(\.\d{1,2})?$|(?!^0.0$)(?!^0.00$)^0\.(\d{1,2})$/ };//请输入不超过8位整数，2位小数的金额
        obj["reg212"] = { regExp: /^[A-Za-z0-9 ]+$/ };//tips0733 请输入字母、数字，可包含空格
        obj["reg115"] = { regExp: /^[a-zA-Z0-9\u2E80-\u9FFF ]*$/ };//tips0320 请输入字母、数字、中文、空格的组合
        obj["reg209"] = { regExp: /^([0-9a-zA-Z-]{8}|[0-9a-zA-Z-]{11,16})$/ };//tips0725 请输入8或11-16位的数字、字母或“-”
        obj["reg203"] = { regExp: /^([0-9-]{10}|[0-9-]{12})$/ };//tips0717 请输入10或12位的数字，允许字符"-"
        obj["reg204"] = { regExp: /^([0-9a-zA-Z]{9}|[0-9a-zA-Z]{10}|[0-9a-zA-Z]{11}|[0-9a-zA-Z]{15})$/ };//tips0718 请输入9、10、11或15位的数字或字母
        obj["reg205"] = { regExp: /^([0-9a-zA-Z-]{11,13}|[0-9a-zA-Z-]{15,19})$/ };//tips0719 请输入11-13或15-19位的数字或字母，允许字符"-"
        obj["reg206"] = { regExp: /^([0-9]{12}|[0-9]{16})$/ };//tips0720 请输入12或16位数字
        obj["reg207"] = { regExp: /^[ a-zA-Z0-9-]*$/ };//tips0723 请输入字母、数字，可包含-和空格
        obj["reg192"] = { regExp: /^[a-zA-Z0-9]{5}00000[a-zA-Z0-9]{6}$/ };//tips0698 请输入5位数字或大小写字母+00000+6位数或字大小写字母
        obj["reg193"] = { regExp: /^((0[1-9])|(1[0|1|2]))\d{2}$/ };//tips0699 请输入月份和年份的后两位数字（格式为MMYY）
        obj["reg194"] = { regExp: /^(6|8|9)\d{7}$/ };//tips0702 请输入以6或8或9开头的8位数字
        obj["reg195"] = { regExp: /^06\d{8}$/ };//tips0703 请输入以06开头的数字
        obj["reg8"] = { regExp: /^[0-9A-Za-z]*$/ };//tips0214 请输入大小写字母或数字
        obj["reg9"] = { regExp: /^[a-zA-Z0-9-]*$/ };//tips0218 请输入字母、数字，可包含-
		obj["reg63"] = { regExp: /^[a-zA-Z0-9\/+\-?()\s.,']*$/ };//tips0225 请输入字母、数字，可包含/-+?().,'和空格
		obj["reg200"] = { regExp: /^[A-Za-z0-9\/\-+?().,' ]{1,70}$/ };//tips0224 请输入字母、数字，可包含/-+?().,' 70
		obj["reg201"] = { regExp: /^[A-Za-z0-9 ]{1,70}$/ };//tips0733 请输入字母、数字，可包含空格
		obj["reg202"] = { regExp: /(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[.])^[1-8][0-9a-zA-Z.]*$/ };//tips0716 请输入以1-8开头的数字字母和“.”的组合
		obj["reg24"] = { regExp: /^[A-Z0-9]*$/ };//tips0234 请输入大写字母或数字
		obj["reg600"] = { regExp: /^[A-Z0-9]{8}([A-Z0-9]{3})?$/ };//tips0425 请输入8位或11位的大写字母或数字
        obj["reg601"] = {regExp: /^[^\[\]',\^\$\~:;!@?#%&<>''""【】，￥？‘’“”&《》#%！@：；……~·]+$/}; //特殊字符过滤
        obj["reg602"] = {regExp: /^\d{11}$/}; //11位手机号
        obj["reg603"] = {regExp: /^[A-Za-z0-9]{4}$/}; //4位验证码
        obj["reg604"] = { regExp: /^[0-9A-Z]*$/ };//tips0214 护照
        obj["reg605"] = { regExp: /^[a-zA-Z0-9\u2E80-\u9FFF',\.\-\/\(\)]*$/ };//tips0232 请输入字母、数字、中文，可包含',.-/()
        this.regGroup = obj;
	};
	
	Validater.prototype = {
		/**
		 * 执行整体规则校验
		 * @memberof Validater
		 * @function
		 * @name execute
		 * @param (Dom) target - 需要校验的表单元素
		 * @param (bool) ignoreFlag - 是否忽略required校验
		 * @returns (bool) 校验通过返回true，否则false
		 */
		execute : function(target, ignoreFlag) {
			var t = this;
			isPass = true;
			if (target.attr("validate")) {
				var val = (target.val().trim() || ""), tips = (target.attr("tips")||'').split(","), jsn = new Function("return (" + target.attr("validate") + ")")(), i = 0;
                //如果是金额，先去掉‘,’在验证
                if(target.hasClass("money")){
                    val = val.replace(/,/g,"");
                }
				for (var k in jsn) {
					isPass = t.check(val, k, jsn[k], ignoreFlag);
					if (!isPass) {
						Common.showMessage(tips[i] || '请正确输入各项');
						break;
					}
					i++;
				}
			}
			return isPass;
		},
		/**
		 * 逐项规则校验
		 * @memberof Validater
		 * @function
		 * @name check
		 * @param (String) val - 表单元素的值
		 * @param (String) regKey - 校验项
		 * @param (String) regVal - 校验项的值
		 * @param (bool) ignoreFlag - 是否忽略required校验
		 * @returns (bool) 校验通过返回true，否则false
		 */
		check : function(val, regKey, regVal, ignoreFlag) {
			if (regKey) {
				var isPass = true;
				switch (regKey) {
					case "required":
						if (regVal && !ignoreFlag) {
							isPass = val.length > 0;
						}
						break;
					case "maxLength":
						isPass = this.strlen(val) <= regVal;
						break;
					case "minLength":
						isPass = this.strlen(val) >= regVal;
						break;
                    case "dateformat":
                        isPass = /^\d{4}(\/|-)((0[1-9])|(1[012]))(\/|-)((0[1-9])|([12]\d)|(3[01]))$/.test(val);
                        break;
                    case "nonlicetchar":
                        isPass = /^[^\[\]\^\$\~@#%\&\<\>\{\}:'"]$/.test(val);
                        break;
                    case "rangelength":
                        isPass = this.strlen(val) >= regVal[0] && this.strlen(val) <= regVal[1];
                        break;
                    case "datecompare": //['startDate', 'limitDate', 3, -12]
                        var sdate = $('[name="' + regVal[0] + '"]').val();
                        if((new Date(sdate) - new Date(val))/1000/60/60/24/30 > regVal[2]){
                            isPass = false;
                            break;
                        }
                        if(regVal[3]){
                            if(regVal[3] > 0){
                                if(new Date(sdate) < new Date() || (new Date(sdate) - new Date())/1000/60/60/24/30 > regVal[3]){
                                    isPass = false;
                                    break;
                                }
                                if(new Date(val) < new Date() || (new Date(val) - new Date())/1000/60/60/24/30 > regVal[3]){
                                    isPass = false;
                                    break;
                                }
                            }else{
                                if(new Date(sdate) > new Date() || (new Date(sdate) - new Date())/1000/60/60/24/30 < regVal[3]){
                                    isPass = false;
                                    break;
                                }
                                if(new Date(val) > new Date() || (new Date(val) - new Date())/1000/60/60/24/30 < regVal[3]){
                                    isPass = false;
                                    break;
                                }
                            }
                        }
					case "equalsTo":
						isPass = val == $('#' + regVal).val() || val == $('[name="'+regVal+'"]').val();
					default:
						if (val.length > 0) {
							if (this.regGroup[regKey]) {
								isPass = this.regGroup[regKey].regExp.test(val);
								if(this.regGroup[regKey].validator){
									isPass = isPass && this.regGroup[regKey].validator(val);
								}
							} else {
								isPass = regVal.test(val);
							}
						};
						break;
				}
				return isPass;
			}
		},
		/**
		 * 计算字符长度,中文为2,英文为1
		 * @function
		 * @memberof Validater
		 * @name strlen
		 * @param (String) str - 要计算的额字符串
		 * @returns (Number) 返回字符长度
		 */
		strlen : function(str) {
			var len = 0;
			for (var i = 0; i < str.length; i++) {
				len += str.charAt(i).match(/[\u0391-\uFFE5]/) ? 2 : 1;
			}
			return len;
		}
	};
	
	/**
	 *  表单校验类
	 * @namespace CheckForm.CheckForm
	 * @name CheckForm
	 * @class
	 */
	var CheckForm = function() {
		this.valid = null;
		this.init();
	};

	CheckForm.prototype = {
		/**
		 * 初始化CheckForm对象
		 * @memberof CheckForm
		 * @function
		 * @name init
		 */
		init : function() {
			var t = this;
			//验证类
			t.valid = new Validater();
			//范围
			 t.selector = ["input[validate]","textarea[validate]:visible","select[validate]:visible"];
			//验证时不忽略必输
			t.ignoreRequired = false;
		},
		/**
		 * 校验target内所有表单元素，校验不通过，显示各自的tips属性指定的消息
		 * @memberof CheckForm
		 * @function
		 * @name checkAll
		 * @param (Dom Object/String) target - 要校验表单的根节点的dom对象或者其id等相关选择器
		 * @param (bool) ignoreFlag - 是否忽略required校验
		 * @example
		 * //在html表单元素中指定校验规则, reg01是在Validator集合中定义的校验规则
		 * <div id="target">
		 *     <input type="text" validate="{'reg': 'reg01'}" tips="请正确输入18位身份证" />
		 * </div>
		 * 
		 * //在业务逻辑中调用checkAll进行校验
		 * if(!CheckForm.checkAll("#target", false)){
		 *     return false;
		 * }
		 */
		checkAll : function(target, ignoreFlag) {
			var isPass = true,t = this,targets = [];
			for(var i=0;i<t.selector.length;i++){
                var temp = $(target).find(t.selector[i]);
                temp.each(function(){
                    targets.push($(this));
                });
            }
            t.ignoreRequired = !ignoreFlag ? false : ignoreFlag;
            $.each(targets, function(i, el) {
                if($(el).attr('disabled')){
                    return true;
                }
                if($(el).attr('widget_type') == 'password'){
                    return $(el).checkMatchRegex();
                }
                isPass = t.valid.execute($(el), t.ignoreRequired);
                if (!isPass) {
					$(el).addClass('errorform');
					$(el).on('focus', function(){
						$(el).removeClass('errorform');
					})
                    return isPass;
                }
            });
			return isPass;
		},
		/**
		 * 校验page页面中输入内容的合法性，可见的或者有valid-param属性的
		 */
		checkAllFromPage:function(page) {
			page = $(page);
			var isPass = true,t = this,targets = [];
			targets = page.find("input, textarea, select");
            $.each(targets, function(i, el) {
            	if($(el).hasClass("valid-param") || $(el).is(":visible")){
            		isPass = t.valid.execute($(el), t.ignoreRequired);
                    if (!isPass) {
                        return isPass;
                    }
            	}
            });
			return isPass;
		}
	};
	return new CheckForm();
});
