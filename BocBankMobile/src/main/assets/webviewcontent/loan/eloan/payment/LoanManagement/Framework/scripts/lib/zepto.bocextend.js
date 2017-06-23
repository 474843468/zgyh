/**
 * zepto的非官方扩展，所有第三方、项目组对zepto的扩展以及一些基础方法的实现，请在此文件中实现 
 * @author ZJD
 */
/**
 * zepto扩展
 * @namespace Zepto.BocExtend
 * @name Zepto.BocExtend
 */
define("zeptoExt",["zepto"], function($){
	(function($) {
		/**
		 * 内部mixin方法，为扩展zepto使用
		 * @private
		 * @param (Object) target - 要扩展的对象
		 * @param (Object) source - 扩展内容
		 * @param (bool) deep - 是否深拷贝
		 */
		function extend(target, source, deep) {
			for (key in source)deep && (isPlainObject(source[key]) || isArray(source[key])) ? (isPlainObject(source[key]) && !isPlainObject(target[key]) && (target[key] = {}), isArray(source[key]) && !isArray(target[key]) && (target[key] = []), extend(target[key], source[key], deep)) : source[key] !== undefined && (target[key] = source[key])
		}
		//扩展zepto公用方法
		extend($, {
			locale: navigator.language == 'zh-CN' ? 'zh_cn' : 'en_us',
            localeResource: {},
            /**
			 * 继承类正在初始化标志
			 * @private
			 * @type bool
			 */
			baseClassinitializing: false,
			/**
			 * 空基类
			 * @private
			 * @type Function
			 */
			BaseClass: function(){},
			/**
			 * 类继承方法
			 * @memberof Zepto.BocExtend
			 * @function
			 * @name extendCls
			 * @param (Class) baseClass - 基类，可以不传，直接以prop生成新类
			 * @param (Object) prop - 子类自己的属性、方法集合，若方法集合中有_constructor方法，将在new的时候被自动执行，可以看做是构造函数。属性、方法集合中与基类中相同的，可以得到重载，此时需要调用父类的方法，可通过 this.base.fun加以调用，注意请不要将base覆盖了。
			 * @returns (Class) 返回继承于基类、扩展了prop的新的类
			 * @example 
			 * //没有基类，直接生成新类
			 * var BaseClass = $.extendCls({
			 *     _constructor:function(){
			 *         this.name = "I am from Base Class";
			 *     }
			 * });
			 * var obj = new BaseClass();
			 * 
			 * //基于基类扩展prop得到新的类
			 * var SubClass = $.extendCls(BaseClass, {
			 *     _constructor:function(){
			 *         this.age = 26;
			 *     }
			 * });
			 * var obj = new SubClass();
			 * console.log(obj.name); //I am from Base Class
			 */
			extendCls: function(baseClass, prop){
				if ( typeof (baseClass) === "object") {
					prop = baseClass;
					baseClass = null;
				}
				function F() {
					if (!$.baseClassinitializing) {
						if (baseClass) {
							this.baseprototype = baseClass.prototype;
						}
						this._constructor && this._constructor.apply(this, arguments);
					}
				}
			
				if (baseClass) {
					$.baseClassinitializing = true;
					F.prototype = new baseClass();
					F.prototype.constructor = F;
					$.baseClassinitializing = false;
				}
				for (var name in prop) {
					if (prop.hasOwnProperty(name)) {
						if (baseClass && typeof (prop[name]) === "function" && typeof (F.prototype[name]) === "function") {
							F.prototype[name] = (function(name, fn) {
								return function() {
									this.base = baseClass.prototype[name];
									return fn.apply(this, arguments);
								};
							})(name, prop[name]);
			
						} else {
							F.prototype[name] = prop[name];
						}
					}
				}
				return F;
			},
			/**
			 * 按指定格式格式化时间对象
			 * @memberof Zepto.BocExtend
			 * @function
			 * @name formatDateToString
			 * @param (Date) dateObj 时间对象
			 * @param (String) dateFormat 格式
			 * @example 
			 * $.formatDateToString(new Date("1409104518000"),"%Y-%m-%d %H:%M:%S");
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
			funAop: function(obj, handlers, overwrite){
				for(var methodName in handlers){
					var _handlers = handlers[methodName];
					for(var handler in _handlers){
						if( (handler == "before" || handler == "after") && typeof _handlers[handler] == "function"){
							eval(handler)(obj, methodName, _handlers[handler]);
						}
					}
				}
				
				function before(obj, method, f){
					var original = obj[method] || obj.prototype[method];
					if(!original){
						throw new Error("需要AOP的方法" + method + "不存在")
					}
					obj[method] = function(){
						f.apply(this, arguments);
						return original.apply(this, arguments);
					}
                    obj.prototype && (obj.prototype[method] = obj[method]);
				}
				
				function after(obj, method, f){
					var original = obj[method] || obj.prototype[method];
					if(!original){
						throw new Error("需要AOP的方法" + method + "不存在")
					}
					obj[method] = function(){
						var res = original.apply(this, arguments),
						    res2 = f.apply(this, [res, arguments]);
						return overwrite ? res2 : res;
					}
                    obj.prototype && (obj.prototype[method] = obj[method]);
				}
				
				return obj;
			},
			/**
			 * 订阅事件 
			 * @memberof Zepto.BocExtend
			 * @function
			 * @name subscribe
			 * @param (Object) obj 订阅者
			 * @param (String) type 订阅者类型
			 * @param (String) topic 订阅事件
			 * @param (Function) evtFun 订阅成功时执行的方法
			 * @example 
			 * $.subscribe(this, "window.app", "showorderdetail", function(param){
			 *   this.showOrderDetail(param.message);
			 * });
			 */
			subscribe: function(obj, type, topic, evtFun){
				$._topics = $._topics || [];
				$._topics[type + "_" + topic] = $._topics[type + "_" + topic] || [];
				obj && evtFun && $._topics[type + "_" + topic].push({
					"o": obj,
					"evt": evtFun
				});
			},
			/**
			 * 发布订阅 
			 * @memberof Zepto.BocExtend
			 * @function
			 * @name publish
			 * @param (String) type 指定订阅者类型
			 * @param (String) topic 要触发的订阅事件
			 * @param (object) args 携带参数
			 * @example 
			 * $.publish("window.app", "showorderdetail", {
			 *   from: "Tom",
			 *   message: "to yours message"
			 * });
			 */
			publish: function(type, topic, args){
				var toc = $._topics[type + "_" + topic];
				if(!toc){
					return false;
				}
				for(var item in toc){
					toc[item].evt.apply(toc[item].o, [args]);
				}
			},
            /**
             * 终端类型判断
             */
            terminal:(function() {
                var u = navigator.userAgent;//app = navigator.appVersion;
                return {//浏览器版本信息
                    trident : u.indexOf('Trident') > -1,
                    presto : u.indexOf('Presto') > -1,
                    webKit : u.indexOf('AppleWebKit') > -1,
                    gecko : u.indexOf('Gecko') > -1 && u.indexOf('KHTML') > -1,
                    mobile : !! u.match(/AppleWebKit.*Mobile.*/),
                    ios : !! u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
                    android : u.indexOf('Android') > -1 || u.indexOf('Linux') > -1,
                    iPhone : u.indexOf('iPhone') > -1,
                    iPad : u.indexOf('iPad') > -1,
                    webApp : u.indexOf('Safari') > -1,
                    weixin: u.indexOf('MicroMessenger') > -1 && u.replace(/.*(MicroMessenger\/[^\s]*).*/, "$1").replace("MicroMessenger/", "") || false
                };
            })(),
            isMinorScreen: function(){
                return $(window).width() < 800;
            },
            _widget_renders:{},
            
            _pwdRandoms: {},
            /**
             * 分析模板小部件中的客户化属性
             * @param wtml
             */
            analyzeWidgetProperties: function(wdgtpl){
                var properties = ['class', 'style', 'id', 'name', 'disabled', 'required', 'validator', 'regExp', 'size', 'value', 'maxyear', 'minyear', 'date_mode','vldurl'];
                var args = {}, key;
                $.each(properties, function(i, item){
                   var regexp = new RegExp(item + '=(\'|")[^\'"]+(\'|")');
                    item == 'class' && (key = 'classes') || (key = item);
                    args[key] = wdgtpl.match(regexp);
                });
//                var args = {
//                    classes: wdgtpl.match(/class="[^>"]+"/),
//                    style: wdgtpl.match(/style="[^>"]+"/),
//                    id: wdgtpl.match(/id="[^>"]+"/),
//                    name: wdgtpl.match(/name="[^>"]+"/),
//                    disabled: wdgtpl.match(/disabled="[^>"]+"/),
//                    required: wdgtpl.match(/required="[^>"]+"/),
//                    validator: wdgtpl.match(/validator="[^>"]+"/),
//                    regExp: wdgtpl.match(/regExp="[^>"]+"/),
//                    size: wdgtpl.match(/size="[^>"]+"/),
//                    value: wdgtpl.match(/value="[^>"]+"/),
//                    maxyear: wdgtpl.match(/maxyear="[^>"]+"/),
//                    minyear: wdgtpl.match(/minyear="[^>"]+"/),
//                    date_mode: wdgtpl.match(/date_mode="[^>"]+"/)
//                }
                for(var f in args){
                    var reg1 = new RegExp((f=="classes" ? "class" : f) + "=(\"|')"), reg2 = new RegExp("'|\"");
                    args[f] = args[f] ? args[f][0] : "";
                    args[f] = args[f].replace(reg1, "").replace(reg2, "");
                }
                return args;
            },
            isTmplContainsCtrl: function(element, widgettype){
                //element为要插入的内容
                //判断插入的内容是否有password控件，若无，则直接返回
                var regexp = new RegExp('widget_type=(\'|")' + widgettype, 'g');
                if(typeof element[0] == 'string'){
                    if(!element[0].match(regexp)||element[0].match(regexp).length==0){
                        return false;
                    }
                }
                if($.isArray(element[0])){
                    var flag = false;
                    $.each(element[0], function(i, item){
                        item = typeof item == 'string'? item : item.innerHTML;
                        if(item.match(regexp) && item.match(regexp).length!=0){
                            flag = true;
                        }
                    });
                    if(!flag){
                        return false;
                    }
                }
                return true;
            },
            xmlParser: {
                //load xml from string
                loadXML: function(xmlString){
                    var xmlDoc=null;
                    //判断浏览器的类型
                    //支持IE浏览器
                    if(!window.DOMParser && window.ActiveXObject){   //window.DOMParser 判断是否是非ie浏览器
                        var xmlDomVersions = ['MSXML.2.DOMDocument.6.0','MSXML.2.DOMDocument.3.0','Microsoft.XMLDOM'];
                        for(var i=0;i<xmlDomVersions.length;i++){
                            try{
                                xmlDoc = new ActiveXObject(xmlDomVersions[i]);
                                xmlDoc.async = false;
                                xmlDoc.loadXML(xmlString); //loadXML方法载入xml字符串
                                break;
                            }catch(e){
                            }
                        }
                    }
                    //支持Mozilla浏览器
                    else if(window.DOMParser && document.implementation && document.implementation.createDocument){
                        try{
                            /* DOMParser 对象解析 XML 文本并返回一个 XML Document 对象。
                             * 要使用 DOMParser，使用不带参数的构造函数来实例化它，然后调用其 parseFromString() 方法
                             * parseFromString(text, contentType) 参数text:要解析的 XML 标记 参数contentType文本的内容类型
                             * 可能是 "text/xml" 、"application/xml" 或 "application/xhtml+xml" 中的一个。注意，不支持 "text/html"。
                             */
                            domParser = new  DOMParser();
                            xmlDoc = domParser.parseFromString(xmlString, 'text/xml');
                        }catch(e){
                        }
                    }
                    else{
                        return null;
                    }
                    return xmlDoc;
                },
                /**
                 * Changes XML node to JSON
                 **/
                nodeToJson: function(node){
                    // Create the return object
                    var obj = {};
                    if (node.nodeType == 1) { // element
                        // do attributes
						obj["@attributes"] = {};
                        if (node.attributes.length > 0) {
                            for (var j = 0; j < node.attributes.length; j++) {
                                var attribute = node.attributes.item(j);
                                obj["@attributes"][attribute.nodeName] = attribute.value || attribute.nodeValue;
                            }
                        }
						obj["@attributes"].xmlNodeTagName = node.nodeName;
                    } else if (node.nodeType == 3) { // text
                        obj = node.nodeValue;
                    }
                    //do children
                    if (node.hasChildNodes()) {
                        for(var i = 0; i < node.childNodes.length; i++) {
                            var item = node.childNodes.item(i);
                            var nodeName = item.nodeName;
							//过滤注释
							if(item.nodeType == 8){
								continue;
							}
                            if (typeof(obj[nodeName]) == "undefined") { //还不存在同类节点
								if(item.nodeValue){
									if(item.nodeValue.replace(/^[\n\r\t\s]*(\w*)[\n\r\t\s]*/g, '$1') != ""){
										obj[nodeName] = this.nodeToJson(item);
									}
								}else{
									obj[nodeName] = this.nodeToJson(item);
								}
                            } else { //已存在同类节点
								var nodeName = obj[nodeName]["@attributes"].xmlNodeTagName, count=0;
								for(var f in obj){
									if(obj[f]["@attributes"] && obj[f]["@attributes"].xmlNodeTagName == nodeName){
										count++;
									}
								}
                                // if (!(typeof(obj[nodeName]) == 'object' && obj[nodeName].length != undefined)) { //还不是数组，将现有节点变为数组
                                //     var old = obj[nodeName];
                                //     obj[nodeName] = [];
                                //     obj[nodeName].push(old);
                                // }
								if(item.nodeValue){ //值节点时
									//过滤空字符串节点
									if(item.nodeValue.replace(/^[\n\r\t\s]*(\w*)[\n\r\t\s]*/g, '$1') != ""){
										obj[nodeName+count] = this.nodeToJson(item);
									}
								}else{ //element节点时
									obj[nodeName+count] = this.nodeToJson(item);
								}
                            }
                        }
                    }
                    return obj;
                },
                /**
                 * transform xml document to json
                 */
                xmlToJson: function(xmlstr){
                    var xmldoc = this.loadXML(xmlstr);
                    if(!xmldoc) return null;
                    //取得根节点
                    var root = xmldoc.childNodes[0];
                    if(!root){
                        return null;
                    }
                    return {root: this.nodeToJson(root)};
                },
                loadFile: function(xml, rstr){
                    // Cretes a instantce of XMLHttpRequest object
                    var xhttp = (window.XMLHttpRequest) ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
                    // sets and sends the request for calling "xml"
                    xhttp.open("GET", xml ,false);
                    xhttp.send(null);

                    // gets the JSON string
                    var json_str = jsontoStr(setJsonObj(xhttp.responseXML));

                    // sets and returns the JSON object, if "rstr" undefined (not passed), else, returns JSON string
                    return (typeof(rstr) == 'undefined') ? JSON.parse(json_str) : json_str;
                }
            }
		});
		//扩展zepto的dom操作方法
		extend($.fn, {
			/**
			 * 删除selector指定的所有节点
			 * @memberof Zepto.BocExtend
			 * @function
			 * @name delcld
			 * @param (String) selector - 需要删除的节点的选择器
			 * @returns (Object) this 使得可以支持链式调用
			 * @example 
			 * $("body").delcld(".ratecalc").append(new EJS({url:_t.EJSPATH._MAIN}).render());
			 */
			delcld : function(selector){
				$(selector).forEach(function(el){
					$("body")[0].removeChild(el);
				});
				return this;
			}
		}, false);

        /**
         * 重写String的trim方法
         */
        String.prototype.trim = function(){
            return this.replace(/(^\s*)|(\s*$)/g,"");
        }

		/**
		 * String扩展
		 * @namespace String
		 * @name String
		 */
		/**
		 * 账号屏蔽处理：将本字符串使用"*"替换账号中间部分
		 * @memberof String
		 * @function
		 * @name maskAccount
		 * @returns (String) 处理过后的字符串
		 * @example var actTxt = "6000123400005678".maskAccount(); //6000******5678
		 */
		if(!$.isFunction(String.prototype.maskAccount)){
			String.prototype.maskAccount = function() {
				if(this.length > 0){
					return this.substr(0, 4) + "******" + this.substr(this.length - 4, 4);
				}else{
					return this;
				}
			};
		};
		
		/**
		 * 账号美化处理：将账号每4位间用空格分开
		 * @memberof String
		 * @function
		 * @name beautyDisplay
		 * @returns (String) 处理过后的账号
		 * @example var actTxt = "6000123400005678".beautyDisplay(); //6000 1234 0000 5678
		 */
		if (!$.isFunction(String.prototype.beautyDisplay)) {
			String.prototype.beautyDisplay = function() {
				if (this.length <= 4) {
					return this;
				} else {
					var ary = [];
					for (var i = 0; i < this.length; i = i + 4) {
						ary.push(this.substr(i, 4));
					}
					return ary.join(" ");
				}
			}
		};
		
//		/**
//		 * 账号美化处理：将账号每4位间用空格分开
//		 * @memberof String
//		 * @function
//		 * @name beautyDisplay
//		 * @returns (String) 处理过后的账号
//		 * @example var actTxt = "6000123400005678".beautyDisplay(); //6000 1234 0000 5678
//		 */
//		if (!$.isFunction(String.prototype.getConstantValue)) {
//			require(["constant"], function(Constant) {
//				String.prototype.getConstantValue = function(perfix) {
//					return Constant[perfix][this];
//				};
//			});
//		};
		
		/**
		 * 金额格式化：将金额以对应币种格式化
		 * @memberof String
		 * @function
		 * @name formatMoney
		 * @param (String) currency 金额币种如："001"
		 * @returns (Number) 格式化后的金额
		 * @example var fAmout = "3015".formatMoney("014"); //3015.00
		 */
		if (!$.isFunction(String.prototype.formatMoney)) {
			String.prototype.formatMoney = function(currency) {
				function appendZero(str, len) {
					if (!str){str = "";}
					if (len < 1){return "";}
					var _len = str.length;
					if (_len > len){return str.substr(0, len);}
					_len = len - _len;
					for (var _tmp = "", i = 0; i < _len; i++) {
						_tmp += "0";
					};
					return str + _tmp;
				}
				if (!currency) {
					currency = '001';
				}
				var decimal;
				switch (currency) {
					case "027":
						decimal = 0;
						break;
					case "025":
						decimal = 0;
						break;
					case "026":
						decimal = 0;
						break;
					case "088"://韩元
						decimal = 0;
						break;
					default:
						decimal = 2;
						break;
				}
				var arr = this.split('.');
				if (arr[1] && arr[1].length > decimal) {
					return Number(this).toFixed(decimal);
				}
				var num = Number(this.replace(/\,/g, '')).toFixed(decimal).toString(),arr = num.split('.');
				arr[0] = arr[0].replace(/(\d{1,2}?)((\d{3})+)$/, "$1,$2").replace(/(\d{3})(?=\d)/g, "$1,");
				return decimal < 1 ? arr[0] : arr[0] + '.'+ appendZero(arr[1], decimal);
			};
		};
		
		/**
		 * json字符串转换为json对象
		 * @memberof String
		 * @function
		 * @name toObject
		 * @returns (Json) 由字符串转换成的Json对象
		 * @example var obj = "{'a':'1'}".toObject(); //{"a":"1"}
		 */
		if (!$.isFunction(String.prototype.toObject)) {
			String.prototype.toObject = function() {
				return new (Function("return " + this.toString() + ";"))();
			};
		};
		
		/**
		 * 获取错误消息
		 * @memberof String
		 * @function
		 * @name toObject
		 * @returns (Json) 由字符串转换成的Json对象
		 * @example 
		 */
		if (!$.isFunction(String.prototype.getErrMessageByCode)) {
			require(["errMessage"], function(ErrMessage) {
				String.prototype.getErrMessageByCode = function() {
					return ErrMessage[this];
				};
			});
		};
		
		/**
		 * 用指定值(数组)替换字符串中（多个）占位符,原字符串必须以{i}表示第i个参数占位
		 * @memberof String
		 * @function
		 * @name toObject
		 * @returns (String) 设置变量值之后的字符串
		 * @example
		 * var str = "尊敬的客户{0}{1}，您好！";
		 * str.setVarText(["张三", "先生"]);
		 */
		if (!$.isFunction(String.prototype.setVarText)) {
			String.prototype.setVarText = function(vals) {
				var s = this.toString();
				if($.isArray(vals)){
					for(var i=0; i<vals.length; i++){
						s = s.replace(/\{\d+\}/, vals[i]);
					}
				}else if(typeof vals === "string"){
					s = s.replace(/\{\d+\}/, vals);
				}
				return s;
			};
		};
		
		/**
		 * Number扩展
		 * @namespace Number
		 * @name Number
		 */
		/**
		 * 小数点处理：取一位小数，剩余部分小数以四舍五入方式取舍
		 * @memberof Number
		 * @function
		 * @name toDecimal
		 * @returns (Number) 处理过后的字符串
		 * @example var num = 222.645;
		 * num = num.toDecimal(); //222.6
		 * num = 222.66;
		 * num = num.toDecimal(); //222.7
		 */
		if(!$.isFunction(Number.prototype.toDecimal)){
			Number.prototype.toDecimal = function(percentage) {
				var f = parseFloat(percentage);
				if(isNaN(f)){
					return;
				}
				f = Math.round(percentage*10)/10;
				return f;
			};
		};
		
		/**
		 * 金额格式化：将金额以对应币种格式化
		 * @memberof Number
		 * @function
		 * @name formatNum
		 * @returns (Number) 处理过后的金额
		 * @example var fAmout = 3015.formatMoney("014"); //3015.00
		 */
		if (!$.isFunction(Number.prototype.formatNum)) {
			Number.prototype.formatNum = function(currency) {
				return this.toString().formatMoney(currency);
			};
		};
		if(!$.isFunction(String.prototype.formatDecimalPart)){
			String.prototype.formatDecimalPart = function(curCode){
				var curDec, str=this.toString();
				if(curCode=="JPY"){
					curDec=0;
				}else{
					curDec=2;
				}
				if (str.indexOf(".") == -1) {
					var tmp = "";
					if (curDec == 0)
						return str;
					for(var i = 0; i < curDec; i++) {
						tmp += "0";
					}
					return str + "." + tmp;
				} else {
					var strArr = str.split(".");
					var decimalPart = strArr[1];
					if(curDec==0){
						return strArr[0]
					}
					if(decimalPart.length>curDec){
						//如果输入的小数位超长;
						decimalPart = decimalPart.substring(0,curDec);
					}
					while(decimalPart.length < curDec) {
						decimalPart += "0";
					}
					return strArr[0] + "." + decimalPart;
				}
			}
		}
		if(!$.isFunction(String.prototype.changePartition)){
			String.prototype.changePartition = function(curCode){
				var minus = "", str = this.toString();
				if(str.substring(0,1) == "-") {
					str = str.substring(1);
					minus = "-";
				}
				str = str.formatDecimalPart(curCode);
				var twopart = str.split(".");
				var decimal_part = twopart[1];
				//format integer part
				var integer_part="0";
				var intlen=twopart[0].length;
				if(intlen>0) {
					var i=0;
					integer_part="";
					while(intlen>3) {
						integer_part=","+twopart[0].substring(intlen-3,intlen)+integer_part;
						i=i+1;
						intlen=intlen-3;
					}
					integer_part=twopart[0].substring(0,intlen)+integer_part;
				}
				if (!decimal_part)
					return minus + integer_part;
				else
					return minus + integer_part + "." + decimal_part
			}
		}
		//cc,新加坡列表页－日月年
		if(!$.isFunction(String.prototype.getSinDdMmYyyy)){
			String.prototype.getSinDdMmYyyy = function(curCode){
				var str = this.toString();
				var newStr = '';
				if(str != '' && str != null){
					for(var i = str.split('/').length-1;i >= 0;i--){
						if(i == 0){
							newStr += str.split('/')[i];
						}else{
							newStr += str.split('/')[i]+'/';
						}
					}
				}
				return newStr;
			}
		}
	})($);
    return $;
});