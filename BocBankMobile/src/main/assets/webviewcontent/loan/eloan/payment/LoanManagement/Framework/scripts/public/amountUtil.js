define('amountUtil', ['common'], function(Common) {
	var amountUtil = {
		/*
         * 函数功能：根据货币码格式化辅币位数，不足位的补0
         *
         * Parameter str -- 需处理的带有数字的字符串;
         *           curCode -- 货币码;
         *
         * Return 字符串 -- 格式化后的字符串
         *
         * 例子：formatDecimalPart("12345678","001");
         *       返回字符串：12345678.00
         * 
         */
        formatDecimalPart:function(str,curCode)
        {
        	/*var curCodes = "CNY|GBP|HKD|CHF|SGD|SEK|DKK|NOK|JPY|CAD|AUD|EUR|MOP|PHP|THB";
        	
        	var curCodeArr=curCodes.split("|");
            var curDec;
            
            for(var j = 0; j < curCodeArr.length; j++)
            {
                if (curCodeArr[j] == curCode)
                {
                    curDec = curCodeArr[j][3];
                    break;
                }
            }*/
    		var curDec;
            
            if(curCode=="JPY"){
            	curDec=0;
            }else{
            	curDec=2;
            }
            
            
            
            if (str.indexOf(".") == -1)
            {
                var tmp = "";
                
                if (curDec == 0)
                    return str;
                    
                for(var i = 0; i < curDec; i++)
                {
                    tmp += "0";
                }
                
                return str + "." + tmp;
            }
            else
            {
                var strArr = str.split(".");
                var decimalPart = strArr[1];

                if(curDec==0){
                    return strArr[0]
                }
                if(decimalPart.length>curDec){
                    //如果输入的小数位超长;
                	decimalPart = decimalPart.substring(0,curDec);
                }
                while(decimalPart.length < curDec)
                {
                    decimalPart += "0";
                }
                return strArr[0] + "." + decimalPart;
            }
        },
        /*
         * 函数功能：将带有数字的字符串用","进行分隔,并且加上小数位(处理千分位,分币种)
         *
         * Parameter str -- 需处理的带有数字的字符串;
         *           curCode -- 货币码;
         *
         * Return 字符串 -- 格式化后的字符串
         *
         * 例子：changePartition("12345678");
         *       返回字符串：12,345,678.00
         * 
         */
        changePartition:function(str,curCode)
        {
            var minus = "";
            
            if(str.substring(0,1) == "-")
            {    
                str = str.substring(1);
                minus = "-";
            }

            str = this.formatDecimalPart(str,curCode);
            var twopart = str.split(".");
            var decimal_part = twopart[1];
            

            //format integer part
            var integer_part="0";
            var intlen=twopart[0].length;
            
            if(intlen>0)
            {
                var i=0;
                
                integer_part=""; 
                
                while(intlen>3)
                {
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
        },
        
        /**
         * 格式化金額
         */
        formatMoney:function(txtObj,formatFlag,curCode){
            var money = $(txtObj).val();
            money = this.isnumber(money);
            if (money != "a"){
                money=money.toString();
                if (money.indexOf(",")>0)
                    money = this.replace(money,",","");//对填写过的金额进行修改时，必须过滤掉','	
                s = money;
                if (s.indexOf("　")>=0)
                    s = this.replace(money,"　","");
                if (s.indexOf(" ")>=0)
                    s = this.replace(money," ",""); 
                if (s.length!=0){
                    var str = this.changePartition(s,curCode);
                    if (!formatFlag)
                        str = this.replace(str,",","");
                    $(txtObj).val(str);
                    if (!formatFlag)
                        $(txtObj).select();
                }	
            }
        },
        replace:function(str,str_s,str_d)
        {
            var pos=str.indexOf(str_s);

            if (pos==-1)
                return str;

            var twopart=str.split(str_s);
            var ret=twopart[0];

            for(pos=1;pos<twopart.length;pos++)
                ret=ret+str_d+twopart[pos];
            return ret;
        },

        isnumber:function(onestring)
        {
            if(onestring.length==0)
                return "a";

            if(onestring==".")
                return "a";

            var regex = new RegExp(/(?!^[+-]?[0,]*(\.0{1,})?$)^[+-]?(([1-9]\d{0,2}(,\d{3})*)|([1-9]\d*)|0)(\.\d{1,})?$/);

            if (!regex.test(onestring))
                return "a";
            if (onestring.substring(0,1)==".")
                onestring = "0" + onestring;

            onestring = this.replace(onestring,",","");

            var split_onestr=onestring.split(".");

            if(split_onestr.length>2)
                return "a";

            return onestring;
        },
	}
	return amountUtil;
})