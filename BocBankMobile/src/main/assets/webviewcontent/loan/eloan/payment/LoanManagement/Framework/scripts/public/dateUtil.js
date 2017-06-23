/**
 * 日历控件
 * @namespace  DateUtil
 * @name DateUtil
 * @requires model, routesUtil, common, calendar
 */
define('dateUtil', ['zepto', 'model', 'routesUtil', 'common'], function($, Model, RoutesUtil, Common) {
	var DateUtil = {
		/**
		 * 获取当前系统时间
		 * @memberof  DateUtil
		 * @function
		 * @name getCurrentTime
		 * @param (Function) callback - 回调方法
		 */
		getCurrentTime : function(callback) {
			Model.interaction({
				method: RoutesUtil.method.BII.querySystemDateTime,
	            params: {}
			},"BII",function(data) {
				if (data) {
					var datetime = data.dateTme.split(" ");
					callback({
						date : datetime[0],
						time : datetime[1]
					});
				}
			});
		},
		/**
		 * 按指定格式格式化时间对象
		 * @memberof DateUtil
		 * @function
		 * @name formatDateToString
		 * @param (Date) dateObj 时间对象
		 * @param (String) dateFormat 格式
		 * @example 
		 * DateUtil.formatDateToString(new Date("1409104518000"),"%Y-%m-%d %H:%M:%S");
		 */
		formatDateToString : function(dateObj, dateFormat) {
			var m = dateObj.getMonth() + 1, d = dateObj.getDate(), minute = dateObj.getMinutes(), s = dateObj.getSeconds(), h = dateObj.getHours(), dateFormat = dateFormat || "%Y/%m/%d";
			month = m < 10 ? "0" + m : "" + m;
			day = d < 10 ? "0" + d : "" + d;
			//H:小时，M:分钟，S秒
			var hours, minutes, seconds;
			hours = h < 10 ? "0" + h : "" + h;
			minutes = minute < 10 ? "0" + minute : "" + minute;
			seconds = s < 10 ? "0" + s : "" + s;
			return dateFormat.replace("%Y", dateObj.getFullYear()).replace("%m", month).replace("%d", day).replace("%H", hours).replace("%M", minutes).replace("%S", seconds);
		},
		/**
		 * 时间字符串转换为时间对象
		 * @memberof DateUtil
		 * @function
		 * @name getDateObj
		 */
		getDateObj : function(str) {
			return new Date(str.replace(/\-/g, '/'));
		},
		/**
		 * 时间字符串转换日期形式
		 * @param {Object} str
		 * getDateString("20160112011122")-->2016/01/12
		 */
		getDateString : function(str){
			if(str == '' && str == null){
				return '';
			}else{
				return str.substr(0,4)+"/"+str.substr(4,2)+"/"+str.substr(6,2);
			}
		},
		/**
		 * 倒计时
		 * @memberof DateUtil
		 * @function
		 * @name countTime
		 * @param (Number) leftTime - 从什么时候开始倒计时，以秒为单位
		 * @param (Object) obj - 倒计时界面元素
		 */
		countTime: function(leftTime,obj){
			if(!leftTime)
				return;
			var isinerval=setInterval(function(){
				leftTime--;
				if(leftTime<1){
					obj.html('重发');
					clearInterval(isinerval);			
				}else{
					obj.html("重发："+leftTime);
				}	
			}, 1000);
		},
		/**
		 * 在指定日期上按周期类型增加指定周期值,并用该日期初始化选择器指定的日期控件元素
		 * @memberof DateUtil
		 * @function
		 * @name renderDateSingle
		 * @param (Date) date - 从什么时候开始倒计时，以秒为单位
		 * @param (Object) obj - 日历控件界面对象
		 * @example
		 * DateUtil.renderDateSingle(new Date("2014-08-09"), $("#startDate"));
		 */
		//renderDateSingle : function(date, obj) {
		//	var retDate;
		//	if (!obj.periodNum) {
		//		obj.periodNum = 0;
		//	}
		//	if (!obj.periodType) {
		//		obj.periodType = 'D';
		//	}
		//	if (obj.periodType === 'D') {
		//		retDate = this.addDays(date, obj.periodNum);
		//	} else if (obj.periodType === 'M') {
		//		retDate = this.addMonths(date, obj.periodNum);
		//	} else if (obj.periodType === 'Y') {
		//		retDate = this.addYears(date, obj.periodNum);
		//	} else {
		//		throw new Error("The 'obj' Parameter Attribute Type Error...");
		//	}
		//	obj.el.val(retDate);
		//	if (Common.terminal.android) {
		//		obj.el.attr("type", "text");
		//		obj.el.attr("readonly", "readonly");
		//		$(obj.el).on("focus", function(){
		//			$(this).calendar('show');
		//		});
		//	}
		//},

		/**
		 * 在指定日期上按周期类型增加指定周期值,并用该日期初始化选择器指定的日期控件元素
		 * @memberof DateUtil
		 * @function
		 * @name renderDateSingle
		 * @param (Date) date - 从什么时候开始倒计时，以秒为单位
		 * @param (Object[]) array - [日历控件界面对象]
		 * @example
		 * DateUtil.renderDateMore(new Date("2014-08-09"), [$("#startDate"), $("#endDate")]);
		 */
		renderDateMore : function(date, array) {
			if (array.constructor !== Array) {
				throw new Error("The 'array' Parameter Type Error...");
			}
			var length = array.length;
			for (var i = 0; i < length; i++) {
				this.renderDateSingle(date, array[i]);
			}
		},

		/**
		 * 日期往前或往后推移n天
		 * @memberof DateUtil
		 * @function
		 * @name addDays
		 * @param (Date/String) date - 传入的 Date对象
		 * @param (Number) days - 天数,可以是正数,也可以是负数
		 * @returns (Date) 推移days天后的日期
		 */
		addDays : function(date, days) {
			var ret;
			if ( typeof date === "string") {
				ret = this.getDateObj(date);
				ret.setTime(ret.getTime() + days * 3600 * 24 * 1000);
			} else if (date.constructor === Date) {
				ret = new Date();
				ret.setTime(date.getTime() + days * 3600 * 24 * 1000);
			} else {
				throw new Error("The 'date' Parameter Type Error...");
			}
			return ret;
		},
		/**
		 * 日期往前或往后推移n月
		 * @memberof DateUtil
		 * @function
		 * @name addMonths
		 * @param (Date/String) date - 传入的 Date对象
		 * @param (Number) months - 月数,可以是正数,也可以是负数
		 * @returns (Date) 推移months月后的日期
		 */
		addMonths : function(date, months) {
			var ret;
			if ( typeof date === "string") {
				ret = this.getDateObj(date);
				ret.setMonth(ret.getMonth() + months);
			} else if (date.constructor === Date) {
				ret = new Date();
				ret.setTime(date.getTime());
				ret.setMonth(date.getMonth() + months);
			} else {
				throw new Error("The 'date' Parameter Type Error...");
			}
			return ret;
		},
		/**
		 * 日期往前或往后推移n年
		 * @memberof DateUtil
		 * @function
		 * @name addYears
		 * @param (Date/String) date - 传入的 Date对象
		 * @param (Number) years - 年数,可以是正数,也可以是负数
		 * @returns (Date) 推移years年后的日期
		 */
		addYears : function(date, years) {
			var ret;
			if ( typeof date === "string") {
				ret = this.getDateObj(date);
				ret.setFullYear(ret.getFullYear() + years);
			} else if (date.constructor === Date) {
				ret = new Date();
				ret.setTime(date.getTime());
				ret.setMonth(date.getMonth());
				ret.setFullYear(date.getFullYear() + years);
			} else {
				throw new Error("The 'date' Parameter Type Error...");
			}
			return ret;
		},
		/**
		 * date1+n 后与date2比较
		 * @memberof DateUtil
		 * @function
		 * @name dateLimit
		 * @param (String) date1 - 传入的 Date对象
		 * @param (String) date2 - 传入的 Date对象
		 * @param (String) type - Y-在年份上加；M-在月份上加
		 * @param (Number) n - 增加的数字
		 * @returns (bool) date1+n <= date2 返回true，否则false
		 */
		dateLimit : function(date1, date2, type, n) {
			date1 = this.getDateObj(date1);
			date2 = this.getDateObj(date2);
			if ('Y' == type) {
				date2.setFullYear(date2.getFullYear() + n);
				date2.setDate(date2.getDate());
			} else if ('M' == type) {
				date2.setMonth(date2.getMonth() + n);
				date2.setDate(date2.getDate());
			}
			//既然是比较大小，为何还需要除以1000*3600*24
			var tmp = (date1 - date2) / (1000 * 3600 * 24);
			if (tmp > 0) {
				return false;
			}
			return true;
		},
		/**
		 * 判断是否在一月内
		 * @memberof DateUtil
		 * @function
		 * @name dateLimit
		 * @param (String) date1 - 传入的 Date对象
		 * @param (String) date2 - 传入的 Date对象
		 * @param (String) lan1 - 提示信息
		 * @returns (bool) 是否在一月内
		 */
		inOneMonth : function(date1, date2, lan1) {
			if (!this.dateLimit(date1, date2, 'M', 1)) {
				Common.showMessage(lan1 + this._getTip('dt0002'));
				return false;
			}
			return true;
		},
		/**
		 * 判断是否在三月内
		 * @memberof DateUtil
		 * @function
		 * @name dateLimit
		 * @param (String) date1 - 传入的 Date对象
		 * @param (String) date2 - 传入的 Date对象
		 * @param (String) lan1 - 提示信息
		 * @param (String) lan2 - 提示信息
		 * @returns (bool) 是否在三月内
		 */
		inThreeMonth : function(date1, date2, lan1, lan2) {
			if (!this.dateLimit(date1, date2, 'M', 3)) {
				Common.showMessage(lan1 + this._getTip('dt0003') + lan2 + this._getTip('dt0004'));
				return false;
			}
			return true;
		},
		/**
		 * 判断是否在六月内
		 * @memberof DateUtil
		 * @function
		 * @name dateLimit
		 * @param (String) date1 - 传入的 Date对象
		 * @param (String) date2 - 传入的 Date对象
		 * @param (String) lan1 - 提示信息
		 * @returns (bool) 是否在六月内
		 */
		inSixMonth : function(date1, date2, lan1) {
			if (!this.dateLimit(date1, date2, 'M', 6)) {
				Common.showMessage(lan1 + this._getTip('dt0005'));
				return false;
			}
			return true;
		},
		/**
		 * 判断是否在一年内
		 * @memberof DateUtil
		 * @function
		 * @name dateLimit
		 * @param (String) date1 - 传入的 Date对象
		 * @param (String) date2 - 传入的 Date对象
		 * @param (String) lan1 - 提示信息
		 * @returns (bool) 是否在一年内
		 */
		inOneYear : function(date1, date2, lan1) {
			if (!this.dateLimit(date1, date2, 'Y', 1)) {
				Common.showMessage(lan1 + this._getTip('dt0006'));
				return false;
			}
			return true;
		},

		/**
		 * 日期比较
		 * @memberof DateUtil
		 * @function
		 * @name compare
		 * @param (String) date1
		 * @param (String) date2
		 * @param (String) lan1  date1国际化key
		 * @param (String) lan2  date2国际化key
		 * @param (Number) type 1：date1不应晚于date2、 2：date1不应等于date2、3：date1不应早于date2、4：date1不应晚于或等于date2、5：date1不应早于或等于date2
		 */
		compare : function(date1, date2, lan1, lan2, type) {
			var sDate = Date.parse(date1.replace(/\-/g, '/')), eDate = Date.parse(date2.replace(/\-/g, '/')), tmp = (sDate - eDate) / (1000 * 3600 * 24);
			if (1 === type && tmp < 0) {
				Common.showMessage(lan1 + this._getTip('dt0009') + lan2);
				return false;
			} else if (2 === type && tmp == 0) {
				Common.showMessage(lan1 + this._getTip('dt0008') + lan2);
				return false;
			} else if (3 === type && tmp > 0) {
				Common.showMessage(lan1 + this._getTip('dt0007') + lan2);
				return false;
			} else if (4 === type && tmp <= 0) {
				Common.showMessage(lan1 + this._getTip('dt0011') + lan2);
				return false;
			} else if (5 === type && tmp >= 0) {
				Common.showMessage(lan1 + this._getTip('dt0010') + lan2);
				return false;
			} else if (!type) {
				throw new Error("Not Support Compare...");
			}
			return true;
		},
		/**
		 * 日期提示信息
		 * @memberof DateUtil
		 * @function
		 * @name _getTip
 		 * @param (Object) key - key值
 		 * @returns (String) 提示信息
		 */
		_getTip : function(key) {
			var tips = {
				'dt0001' : "只能是",
				'dt0002' : "可以设置为未来一个月内的任意一天",
				'dt0003' : "和",
				'dt0004' : "范围为三个自然月",
				'dt0005' : "可以设置为未来六个月内的任意一天",
				'dt0006' : "可以设置为未来一年内的任意一天",
				'dt0007' : "不应晚于",
				'dt0008' : "不能等于",
				'dt0009' : "不应早于",
				'dt0010' : "不应晚于或等于",
				'dt0011' : "不应早于或等于"
			};
			return tips[key];
		},
		format : function (date, format) {
			// get the helper functions object
			var formatLogic = this.formatLogic;

			// check if the AM/PM option is used
			var isAmPm = (format.indexOf("a") !== -1) || (format.indexOf("A") !== -1);

			// prepare all the parts of the date that can be used in the format
			var parts = [];
			parts['d'] = date.getDate();
			parts['dd'] = formatLogic.pad(parts['d'], 2);
			parts['ddd'] = formatLogic.i18n.shortDayNames[date.getDay()];
			parts['dddd'] = formatLogic.i18n.dayNames[date.getDay()];
			parts['M'] = date.getMonth() + 1;
			parts['MM'] = formatLogic.pad(parts['M'], 2);
			parts['MMM'] = formatLogic.i18n.shortMonthNames[parts['M'] - 1];
			parts['MMMM'] = formatLogic.i18n.monthNames[parts['M'] - 1];
			parts['yyyy'] = date.getFullYear();
			parts['yyy'] = formatLogic.pad(parts['yyyy'], 2) + 'y';
			parts['yy'] = formatLogic.pad(parts['yyyy'], 2);
			parts['y'] = 'y';
			parts['H'] = date.getHours();
			parts['hh'] = formatLogic.pad(isAmPm ? formatLogic.convertTo12Hour(parts['H']) : parts['H'], 2);
			parts['h'] = isAmPm ? formatLogic.convertTo12Hour(parts['H']) : parts['H'];
			parts['HH'] = formatLogic.pad(parts['H'], 2);
			parts['m'] = date.getMinutes();
			parts['mm'] = formatLogic.pad(parts['m'], 2);
			parts['s'] = date.getSeconds();
			parts['ss'] = formatLogic.pad(parts['s'], 2);
			parts['z'] = date.getMilliseconds();
			parts['zz'] = parts['z'] + 'z';
			parts['zzz'] = formatLogic.pad(parts['z'], 3);
			parts['ap'] = parts['H'] < 12 ? 'am' : 'pm';
			parts['a'] = parts['H'] < 12 ? 'am' : 'pm';
			parts['AP'] = parts['H'] < 12 ? 'AM' : 'PM';
			parts['A'] = parts['H'] < 12 ? 'AM' : 'PM';

			// parse the input format, char by char
			var i = 0;
			var output = "";
			var token = "";
			while (i < format.length) {
				token = format.charAt(i);

				while ((i + 1 < format.length) && parts[token + format.charAt(i + 1)] !== undefined) {
					token += format.charAt(++i);
				}

				if (parts[token] !== undefined) {
					output += parts[token];
				}
				else {
					output += token;
				}

				i++;
			}

			// return the parsed result
			return output;
		},
		formatLogic : {
			// left-pad the provided number with zeros
			pad: function (value, digits) {
				var max = 1;
				var zeros = "";

				if (digits < 1) {
					return "";
				}

				for (var i = 0; i < digits; i++) {
					max *= 10;
					zeros += "0";
				}

				var output = value;

				output = zeros + value;
				output = output.substring(output.length - digits);

				return output;
			},

			// convert the 24 hour style value to a 12 hour style value
			convertTo12Hour: function (value) {
				return value % 12 === 0 ? 12 : value % 12;
			},

			// internationalization settings
			i18n: {
				dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
				shortDayNames: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
				monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
				shortMonthNames: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
			}
		},
		parse : function (value, format) {
		var output = new Date(2000, 0, 1);
			var parts = [];
			parts['d'] = '([0-9][0-9]?)';
			parts['dd'] = '([0-9][0-9])';

			parts['M'] = '([0-9][0-9]?)';
			parts['MM'] = '([0-9][0-9])';

			parts['yyyy'] = '([0-9][0-9][0-9][0-9])';
			parts['yyy'] = '([0-9][0-9])[y]';
			parts['yy'] = '([0-9][0-9])';
			parts['H'] = '([0-9][0-9]?)';
			parts['hh'] = '([0-9][0-9])';
			parts['h'] = '([0-9][0-9]?)';
			parts['HH'] = '([0-9][0-9])';
			parts['m'] = '([0-9][0-9]?)';
			parts['mm'] = '([0-9][0-9])';
			parts['s'] = '([0-9][0-9]?)';
			parts['ss'] = '([0-9][0-9])';
			parts['z'] = '([0-9][0-9]?[0-9]?)';
			parts['zz'] = '([0-9][0-9]?[0-9]?)[z]';
			parts['zzz'] = '([0-9][0-9][0-9])';
			parts['ap'] = '([ap][m])';
			parts['a'] = '([ap][m])';
			parts['AP'] = '([AP][M])';
			parts['A'] = '([AP][M])';

			var _ = this.parseLogic;

			// parse the input format, char by char
			var i = 0;
			var regex = "";
			var outputs = new Array("");
			var token = "";

			// parse the format to get the extraction regex
			while (i < format.length) {
				token = format.charAt(i);
				while ((i + 1 < format.length) && parts[token + format.charAt(i + 1)] !== undefined) {
					token += format.charAt(++i);
				}

				if (parts[token] !== undefined) {
					regex += parts[token];
					outputs[outputs.length] = token;
				}
				else {
					regex += token;
				}

				i++;
			}

			// extract matches
			var r = new RegExp(regex);
			var matches = value.match(r);

			if (matches === undefined || matches.length !== outputs.length) {
				return undefined;
			}

			// parse each match and update the output date object
			for (i = 0; i < outputs.length; i++) {
				if (outputs[i] !== '') {
					switch (outputs[i]) {
						case 'yyyy':
						case 'yyy':
							output.setYear(_.parseInt(matches[i]));
							break;

						case 'yy':
							output.setYear(2000 + _.parseInt(matches[i]));
							break;

						case 'MM':
						case 'M':
							output.setMonth(_.parseInt(matches[i]) - 1);
							break;

						case 'dd':
						case 'd':
							output.setDate(_.parseInt(matches[i]));
							break;

						case 'hh':
						case 'h':
						case 'HH':
						case 'H':
							output.setHours(_.parseInt(matches[i]));
							break;

						case 'mm':
						case 'm':
							output.setMinutes(_.parseInt(matches[i]));
							break;

						case 'ss':
						case 's':
							output.setSeconds(_.parseInt(matches[i]));
							break;

						case 'zzz':
						case 'zz':
						case 'z':
							output.setMilliseconds(_.parseInt(matches[i]));
							break;

						case 'AP':
						case 'A':
						case 'ap':
						case 'a':
							if ((matches[i] === 'PM' || matches[i] === 'pm') && (output.getHours() < 12)) {
								output.setHours(output.getHours() + 12);
							}

							if ((matches[i] === 'AM' || matches[i] === 'am') && (output.getHours() === 12)) {
								output.setHours(0);
							}
							break;
					}
				}
			}

			return output;
		},
		parseLogic : {
			unpad: function (value) {
				var output = value;

				while (output.length > 1) {
					if (output[0] === '0') {
						output = output.substring(1, output.length);
					}
					else {
						break;
					}
				}

				return output;
			},
			parseInt: function (value) {
				return parseInt(this.unpad(value), 10);
			}
		}

	};
	return DateUtil;
});