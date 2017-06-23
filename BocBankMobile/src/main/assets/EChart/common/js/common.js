
/**
 * jQuery url get parameters function [获取URL的GET参数值]
 * @character_set UTF-8
 * @author Jerry.li(lijian@dzs.mobi)
 * @version 1.2012.12.11.1400
 *  Example
 * 	<code>
 *      var parameters = $.urlGet(); //获取URL的Get参数
 *      var id = parameters["id"]; //取得id的值
 * 	</code>
 */
(function($) {
	$.extend({
		/**
		* url get parameters
		* @public
		* @return array()
		*/
		urlGet:function() {
			var currentUrl = window.location.href;
			var aGET = new Array();
			if(currentUrl.indexOf("?") > -1) {
				var parametersStr = currentUrl.substring(currentUrl.indexOf("?") + 1);
				var parametersArr = parametersStr.split("&");
				for(var i = 0; i < parametersArr.length; i ++) {
					var currentParameterStr = parametersArr[i];
					var currentKey = currentParameterStr.substring(0, currentParameterStr.indexOf("="));
					var currentValue = currentParameterStr.substring(currentParameterStr.indexOf("=") + 1);
					while(currentValue.substring(currentValue.length - 1) === "#") {
						currentValue = currentValue.substring(0, currentValue.length - 1);
					}
					aGET[currentKey] = currentValue;
				}
			}
			return aGET;
		},
	});
})(jQuery);
