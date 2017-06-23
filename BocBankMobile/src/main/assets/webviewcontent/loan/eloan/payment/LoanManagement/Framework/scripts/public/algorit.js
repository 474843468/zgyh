/**
 * Created by lxw4566 on 2016/3/9.
 */
/*
 * bill payment number algorithm
 */
define("algorit", ['zepto'], function($) {
Algorithm={

    Algo_Courts: function(accountNo){
        // Contact no
        // The accepted algorithm is Singapore contact no.
        var result = false,
            len = accountNo.length,
            reg = /^\d{3}[0-6]\d{8}$/,
            reg2 = /^60357801\d{8}$/;
        // Case length of Account no is 12
        if(reg.test(accountNo)){
            // The 4th character must between 0 to 6
            // Multiply value: 9,8,4,10,1,6,3,5,2,7,1,0
            var multiply = ["9", "8", "4", "10", "1", "6", "3", "5", "2", "7", "1", "0"],
                total = 0;
            // Total = Account No. X multiply value
            for(var i = 0; i < len; i++){
                total += (parseInt(accountNo.charAt(i)) * parseInt(multiply[i]));
            }
            // Find remainder from total % 11
            var remainder = total % 11;
            // If remainder is 0 then it’s a valid Account No
            if(remainder == 0){
                result = true;
            }
        }else if(reg2.test(accountNo)){
            // Case length of Account no is 16:
            // The first 8th characters must be “60357801” then it’s a valid Account No
            result = true;
        }
        return result;
    },

    Algo_M1: function(accountNo){
        /*
         a)Multiply each numeral from left to right by the constant.
         Digit:          N1  N2  N3  N4  N5  N6  N7  N8
         Multiply by:    7   8   5   6   2   4   1   3
         b)Sum (S1) the results.
         c)Divide the Sum (S1) by 10 giving the reminder (R1).
         d)Multiply R1 by 7 giving the result (S2).
         e)Divide the Sum (S2) by 10 giving the reminder (R2).
         f)If Y = R2, valid = true
         g)Else, valid = false
         h)If Y = “.”, valid = false
         i)If N1 = 0, valid = false
         h和i可以先验证*/
        var result = false,
            len = accountNo.length,
            multiply = ["7", "8", "5", "6", "2", "4", "1", "3"],
            total = 0,
            reg = /^[1-9]\d{8}$/;
        // Length = 9
        if(reg.test(accountNo)){
            for(var i = 0, l = len - 1; i < l; i++){
                total += (parseInt(accountNo.charAt(i)) * parseInt(multiply[i]));
            }
            var remainder = total % 10 * 7 % 10;
            if(remainder == accountNo.charAt(len - 1)){
                result = true;
            }
        }
        return result;
    },

    Algo_SingTel: function(accountNo){
        /*
         Format: NNNNNNNY, NNNNNNN = 7 numerals, Y = check_digit (numeral)
         Check digit calculation:
         a)Multiply each numeral from left to right by the constant.
         Digit:          N   N   N   N   N   N   N
         Multiply by:    128 64  32  16  8   4   2
         Result:         M1  M2  M3  M4  M5  M6  M7
         b)Each numeral divide by 11 giving the remainder (R1-R7).
         Digit:      M1  M2  M3  M4  M5  M6  M7
         Divide by:  11  11  11  11  11  11  11
         Result:     R1  R2  R3  R4  R5  R6  R7
         c)Sum (S) the results
         d)Divide the Sum (S) by 10 giving the reminder (R).
         e)If R = 0, H is 0
         f)If R not = 0, H is 10 – R
         g)If H = Y, valid = true
         h)Else, valid = false */
        var result = false,
            multiply = ["128", "64", "32", "16", "8", "4", "2"],
            total = 0,
            len = accountNo.length,
            reg = /^\d{8}$/;
        if(reg.test(accountNo)){
            for(var i = 0, l = len - 1; i < l; i++){
                total += (parseInt(accountNo.charAt(i)) * parseInt(multiply[i]) % 11);
            }
            var remainder = total % 10;
            if(remainder){
                remainder = 10 - remainder;
            }
            if(accountNo.charAt(len - 1) == remainder){
                result = true;
            }
        }
        return result;
    },

    Algo_Starhub_Ltd: function(accountNo){
        var result = false,
            len = accountNo.length,
            arr = accountNo.substring(0, len - 1).split(".");
        if(arr.length > 1){
            var account = {
                G: arr[0],
                A: arr[1],
                B: arr[2],
                C: arr[3],
                T: arr[4],
                Z: accountNo.charAt(len - 1)
            };
            if(account.G == "1"){
                if(account.A >= 10000000 && account.A <= 99999999){
                    result = true;
                }
            }else if(account.G >= 2 && account.G <= 8){
                var begin = Math.pow(10, parseInt(account.G) - 1),
                    end = Math.pow(10, parseInt(account.G)) - 1;
                if(account.A >= begin && account.A <= end){
                    if(account.B){
                        if(account.B == "00" || (account.B >= 10 && account.B <= 99)){
                            if(account.C){
                                if(account.C == "00" || account.C >= 10 && account.C <= 99){
                                    if(account.T){
                                        if(account.T > 100000 && account.T < 999999){
                                            result = true;
                                        }else{
                                            result = false;
                                        }
                                    }else{
                                        result = true;
                                    }
                                }else{
                                    result = false;
                                }

                            }else{
                                result = true;
                            }
                        }else{
                            result = false;
                        }
                    }else{
                        result = true;
                    }
                }
            }
            if(result){
                var acc = accountNo.substring(0, len - 1).replace(/\./g, ""),
                    acc_len = acc.length;
                multiply = ["2", "9", "8", "7", "6", "5", "4", "3", "2", "1", "2", "9", "8", "7", "6", "5", "4", "3", "2", "1"],
                    mul_len = multiply.length,
                    total = 0;
                for(var i = 1; i <= acc_len; i++){
                    total += (parseInt(acc.charAt(acc_len - i)) * parseInt(multiply[mul_len - i]));
                }
                var remainder = total % 11,
                    checkDigit = ["S", "T", "A", "R", "H", "U", "B", "L", "I", "N", "K"];
                if(checkDigit[remainder] == account.Z){
                    result = true;
                }else{
                    result = false;
                }
            }

        }
        return result;
    },

    Algo_SunPage_NexwaveTelecom: function(accountNo){
        var result = false,
            len = accountNo.length,
            total = "",
            sum = 0;
        if(len == 8){
            for(var i = 0, l = len; i < l; i++){
                total += (accountNo.charAt(i)) * (2 - i % 2).toString();
            }
            for(var i = 0, l = total.length; i < l; i++){
                sum += Number(total.charAt(i));
            }
            if(sum % 10 == 0){
                result = true;
            }
        }
        return result;
    },

    ALGO_ZONE1511: function(accountNo){
        var result = false,
            len = accountNo.length,
            multiply = ["8", "7", "6", "5", "4", "3", "2", "1"],
            checkDigit = ["A", "B", "C", "D", "E", "F", "G", "H"],
            total = 0;
        if(len == 9){
            for(var i = 0, l = len - 1; i < l; i++){
                total += (parseInt(accountNo.charAt(i)) * parseInt(multiply[i]));
            }
            var remainder = total % 8;
            if(checkDigit[7 - remainder] == accountNo.charAt(len - 1)){
                result = true;
            }
        }
        return result;
    },

    //SP_Serivces
    Algo_SP_Services: function(accountNo){
        var result = false,
            len = accountNo.length,
            start = accountNo.charAt(0),
            end = accountNo.charAt(len - 1);
        if(len == 10 && (start >= 1 && start <= 9) && ((end >= 0 && end <= 9) || end == "-") &&
            !((accountNo >= 8900000000 && accountNo <= 8999999999) || (accountNo >= 9100000000 && accountNo <= 9409999999))){
            var multiply = ["4", "3", "2", "7", "6", "5", "4", "3", "2"],
                total = 0;
            for(var i = 0, l = len - 1; i < l; i++){
                total += (parseInt(accountNo.charAt(i)) * parseInt(multiply[i]));
            }
            var remainder = 11 - total % 11;
            if(remainder > 0 && remainder < 10){
                result = parseInt(end) == remainder;
            }else if(remainder == 10){
                result = end == "-";
            }else{
                result = parseInt(end) == 0;
            }
        }
        if(!result && !(len == 10 && accountNo.substring(0, 3) == "930") && end >= 0 && end <= 9){
            if(len == 12 && accountNo.substring(0, 2) == "00"){
                accountNo = accountNo.substring(2);
                len = accountNo.length;
                start = accountNo.charAt(0);
            }
            if(len == 10 && ((accountNo >= 8900000000 && accountNo <= 8999999999) || (accountNo >= 9100000000 && accountNo <= 9409999999))){
                var total = "",
                    sum = 0;
                for(var i = 0, l = len - 1; i < l; i++){
                    total += (accountNo.charAt(i)) * (2 - i % 2).toString();
                }
                for(var i = 0, l = total.length-1; i <= l; i++){
                    sum += Number(total.charAt(i));
                }
                var remainder = 10 - sum % 10;
                if(remainder == 10){
                    result = parseInt(end) == 0;
                }else{
                    result = parseInt(end) == remainder;
                }
            }
        }
        return result;
    },

    Algo_NTUC_Income: function(biiReferenceNo){
        var result = false,
            reg = /^\d{11}$/,
            total = 0;
        if(reg.test(biiReferenceNo)){
            var multiply = [ "7", "1", "3", "7", "1", "3", "7", "1", "3", "7"];
            for(var i = 0,l = biiReferenceNo.length - 1; i < l; i++){
                total += (parseInt(biiReferenceNo.charAt(i)) * parseInt(multiply[i]));
            }
            var remainder = total % 10;
            if(remainder){
                remainder = 10 - remainder;
            }
            result = remainder == biiReferenceNo.charAt(biiReferenceNo.length - 1);
        }
        return result;
    },

    Algo_Traffic_Police: function(noticeNo){
        var result = false,
            reg = /^\d{16}$/,
            total = 0,
            multiply = "345678912345678";
        if(reg.test(noticeNo)){
            for(var i = 0, l = noticeNo.length - 1; i < l; i++){
                total += (parseInt(noticeNo.charAt(i)) * parseInt(multiply.charAt(i)));
            }
            var remainder = total % 11;
            if(remainder == 10){
                remainder = 0;
            }
            result = remainder == noticeNo.charAt(noticeNo.length - 1);
        }
        return result;
    },
    //新加坡kk证件号
    ALGO_NRIC: function(nricNo){
        var result = false,
            reg = /^[ST]\d{7}[ABCDEFGHIZJ]$/;
        reg1 = /^[ST][ABCDEFGHIZJ]\d{7}$/;
        if(reg.test(nricNo)){
            var multiply = ["2", "7", "6", "5", "4", "3", "2"],
                total = 0;
            for(var i = 1; i <= 7; i++){
                total += parseInt(nricNo.charAt(i)) * parseInt(multiply[i - 1]);
            }
            if(nricNo.charAt(0) == "T"){
                total += 4;
            }
            var remainder = 11 - total % 11,
                checkDigit = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "Z", "J"];
            if(checkDigit[remainder - 1] == nricNo.charAt(nricNo.length - 1)){
                result = true;
            }
        }else if(reg1.test(nricNo)){
            var multiply = ["2", "7", "6", "5", "4", "3", "2"],
                total = 0;
            for(var i = 1; i <= 7; i++){
                total += parseInt(nricNo.charAt(i+1)) * parseInt(multiply[i - 1]);
            }
            if(nricNo.charAt(0) == "T"){
                total += 4;
            }
            var remainder = 11 - total % 11,
                checkDigit = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "Z", "J"];
            if(checkDigit[remainder - 1] == nricNo.charAt(1)){
                result = true;
            }
        }
        return result;
    },
    //证件号
    Algo_Changi_General_Hospita_NRICNo: function(no){
        var result = false,
            len = no.length,
            reg1 = /^[STFG][a-zA-Z]\d{7}$/,
            reg2 = /^[STFG]\d{7}[a-zA-Z]$/,
            reg3 = /^[XY][a-zA-Z](\d{8}|\d{10})$/,
            reg4 = /^[XY](\d{8}|\d{10})[a-zA-Z]$/;
        if(reg1.test(no)){
            if(no.charAt(0) == "F" || no.charAt(0) == "G"){
                return this.ALGO_FIN(no);
            }else{
                return this.ALGO_NRIC(no);
            }

//            result = true;
        }else if(reg2.test(no)){
            if(no.charAt(0) == "F" || no.charAt(0) == "G"){
                return this.ALGO_FIN(no);
            }else{
                return this.ALGO_NRIC(no);
            }
            //result = this.ALGO_NRIC(no);
//        	result = true;
        }else if(reg3.test(no)){
            no = no.charAt(0) + no.substring(2) + no.charAt(1);
        }
        if(!result && reg4.test(no)){
            if(len == 10){
                no = no.charAt(0) + "04" + no.substring(1);
            }
            len = no.length;
            var Z1 = 0,
                N4 = no.charAt(3),
                N5 = no.charAt(4),
                start = no.charAt(0),
                end = no.charAt(len - 1);
            if(start == "X"){
                Z1 = 1711 * (parseInt(N4) + 1) * 4;
            }else{
                Z1 = 1811 * (parseInt(N4) + 1) * 4;
            }
            var N1 = (parseInt(N5) + 1) * 45 * 2,
                S1 = 0,
                multiply = ["7", "6", "5", "4", "3", "2"];
            for(var i = 0; i < 6; i++){
                S1 += (parseInt(multiply[i]) * parseInt(no.charAt(5 + i)));
            }
            var T = 11 - (Z1 + N1 + S1) % 11;
            var checkDigit = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "Z", "J"];
            result = end == checkDigit[T - 1];
        }
        return result;
    },
    //税单号
    Algo_Changi_General_Hospita_TaxNo: function(taxNo,year){
        var result = false,
            start = taxNo.substring(0, 2);
        if(start == "69"){
            var reg = /^\d{6}[a-zA-Z]$/,
                Y = taxNo.substring(2, 4),
                end = taxNo.substring(4);
            result = (/^\d{2}$/.test(Y) && parseInt("20"+Y) < parseInt(year.substring(0,4)) && reg.test(end));
        }else if(start == "PH"){
            var reg = /^PH\d{7,9}$/;
            result = reg.test(taxNo);
        }else if(start == "70"){
            var reg = /^\d{6}[a-zA-Z]\d{4}$/,
                Y = taxNo.substring(2, 4),
                end = taxNo.substring(4);
            result = (/^\d{2}$/.test(Y) && parseInt("20"+Y) < parseInt(year.substring(0,4)) && reg.test(end));
        }else{
            var reg = /^20\d{8}[a-zA-Z]$/,
                Y = taxNo.substring(0, 4);
            //end = taxNo.substring(4);
            result = (/^\d{4}$/.test(Y) && parseInt(Y) < parseInt(year.substring(0,4)) && reg.test(taxNo));
        }
        return result;
    },
    //账单号
    Algo_National_Heart_Center: function(biiNo,year){
        var result = false;
        if(biiNo.substring(0, 2) == "HC"){
            var Y = biiNo.substring(2,4),
                end = biiNo.substring(4),
                reg = /^\d{5}-?\d{2}$/;
            if(/^\d{2}$/.test(Y) && parseInt("20"+Y) < parseInt(year.substring(0,4)) && reg.test(end)){
                if(parseInt(biiNo.substring(biiNo.length - 2,biiNo.length)) <= 10){
                    result = true;
                }
            }
        }else if(biiNo.substring(0, 2) == "H1"){
            var Y = biiNo.substring(2,4),
                end = biiNo.substring(4),
                reg = /^\d{6}[a-jzA-JZ]-?\d{4}(-?\d{2})?$/;
            if(/^\d{2}$/.test(Y) && parseInt("20"+Y) < parseInt(year.substring(0,4)) && (reg.test(end))){
                result = true;
            }
        }else if(biiNo.substring(0, 3) == "PHC"){
            var Y = biiNo.substring(3,5),
                end = biiNo.substring(5),
                reg = /^\d{5}-?\d{2}$/;
            if(/^\d{2}$/.test(Y) && parseInt("20"+Y) < parseInt(year.substring(0,4)) && reg.test(end)){
                if(parseInt(biiNo.substring(biiNo.length - 2,biiNo.length)) <= 10){
                    result = true;
                }
            }
        }
        return result;
    },
    //NRIC or External ID
    Algo_National_Heart_Center_NRICNo: function(no){
        var result = false,
            len = no.length,
            reg1 = /^[STFG][a-zA-Z]\d{7}$/,
            reg2 = /^[STFG]\d{7}[a-zA-Z]$/,
            reg3 = /^[XY][a-zA-Z](\d{8}|\d{10})$/,
            reg4 = /^[XY](\d{8}|\d{10})[a-zA-Z]$/;
        if(reg1.test(no)){
            result = true;
        }else if(reg2.test(no)){
            //result = this.ALGO_NRIC(no);
            result = true;
        }else if(reg3.test(no)){
            no = no.charAt(0) + no.substring(2) + no.charAt(1);
        }
        if(!result && reg4.test(no)){
            if(len == 10){
                no = no.charAt(0) + "04" + no.substring(1);
            }
            len = no.length;
            var Z1 = 0,
                N4 = no.charAt(3),
                N5 = no.charAt(4),
                start = no.charAt(0),
                end = no.charAt(len - 1);
            if(start == "X"){
                Z1 = 1711 * (parseInt(N4) + 1) * 4;
            }else{
                Z1 = 1811 * (parseInt(N4) + 1) * 4;
            }
            var N1 = (parseInt(N5) + 1) * 43 * 2,
                S1 = 0,
                multiply = ["7", "6", "5", "4", "3", "2"];
            for(var i = 0; i < 6; i++){
                S1 += (parseInt(multiply[i]) * parseInt(no.charAt(5 + i)));
            }
            var T = 11 - (Z1 + N1 + S1) % 11;
            var checkDigit = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "Z", "J"];
            result = end == checkDigit[T - 1];
        }
        return result;
    },

    //no
    Algo_KK_Hospital_NRICNo: function(no){
        var reg = /^([a-zA-Z]{2}\d{10})|([a-zA-Z]\d{10}[a-zA-Z])|([a-zA-Z]{2}\d{8})|([a-zA-Z]\d{8}[a-zA-Z])|(70\d{7})$/;
        return reg.test(no);
    },
    //病例号 or 账单号
    Algo_KK_Hospital_TaxNo: function(taxNo){
        var result = false;
        if(taxNo.charAt(0) == "B"){
            var reg = /^B\d{7}$/;
            result = reg.test(taxNo);
        }else if(taxNo.substring(0, 2) == "PH"){
            //其中的空格要自动转换为-
            var reg = /^PH\d{8}[0-9a-zA-Z-]?\d{2}$/;
            result = reg.test(taxNo);
        }else if(taxNo.length == 11){
            var reg = /^\d{10}[A-JZ]$/;
            result = reg.test(taxNo);
        }else if(taxNo.length > 11){
            var reg = /^\d{10}[a-zA-Z]\d+$/;
            result = reg.test(taxNo);
        }
        return result;
    },

    Algo_IRAS_ITR: function(no){
        var result = false;
        if(no == "" || no == undefined)
            return result;

        var multiply = [8,7,6,5,4,3,2], splitITR = no.split("-"),sum = 0;
        var reg = /^\d{7}$/;
        var checkDigitForClassQ = ["A","C","E","G","J","L","P","R","T","V","Y"];
        var checkDigit = ["A","C","E","G","J","L","N","P","R","T","Y"];
        var checkDigit2ForClassQ = [1,3,5,7,10,12,16,18,20,22,25];
        var checkDigit2 = [1,3,5,7,10,12,14,16,18,22,25];
        var checkDigit3 = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
        if(splitITR.length != 3){
            return result;
        }
        if(splitITR[0].length != 2){
            return result;
        }
        //数字部分
        var partOfNumeral = splitITR[1];
        if(partOfNumeral.length != 7 || !reg.test(partOfNumeral)){
            return result;
        }
//        if(splitITR[2].length != 2){
//        	return result;
//        }

        var c1c2 = splitITR[0];
        for(var i = 0 ; i < partOfNumeral.length ; i++){
            sum += parseInt(partOfNumeral[i]) * multiply[i];
        }
        //if the first alphabet is Q,add 9 to sum
        if(c1c2.charAt(0) == "Q"){
            sum += 9;
        }
        var remainder = sum % 11, p = 11 - remainder;
        var w = '',checkChar = splitITR[2].charAt(0);
        if(/^\s$/.test(splitITR[2].charAt(1)) || "" == splitITR[2].charAt(1)){
            w = 0;
        }else{
            if(isNaN(splitITR[2].charAt(1))){
                return result;
            }
            w = parseInt(splitITR[2].charAt(1));
        }
        //check digit(class is Q and w is zero)
        if(w == 0){
            if(c1c2.charAt(0) == "Q"){
                result = (checkChar == checkDigitForClassQ[p - 1]);
            }else{
                result = (checkChar == checkDigit[p - 1]);
            }
        }else{// w is not zero
            if(c1c2.charAt(0) == "Q"){
                var p1 = w + w * checkDigit2ForClassQ[p - 1];
                if(p1 > 26){
                    p1 -= 26;
                }
                result = (checkChar == checkDigit3[p1 - 1]);
            }else{
                var p1 = w + w * checkDigit2[p - 1];
                if(p1 > 26){
                    p1 -= 26;
                }
                result = (checkChar == checkDigit3[p1 - 1]);
            }
        }
        return result;
    },

    //ASSIGNED ID
    Algo_IRAS_ASSIGNED_ID: function(no){
        var result = false;
        var reg = /^\d{7}$/,multiply = [7,6,2,5,4,3,2],sum = 0;
        var checkDigit = ["A","B","C","D","E","F","G","H","I","Z","J"];
        if(!no){
            return result;
        }
        //if the start alphabet is not 'A' of the string,return false
        if(no.charAt(0) != "A"){
            return result;
        }
        //if the length of string is not 9,return false
        if(no.length != 9){
            return false;
        }
        var regStr = no.substr(1,no.length - 2);
        if(!reg.test(regStr)){
            return result;
        }
        for(var i = 0 ; i < multiply.length ; i++){
            sum += parseInt(regStr.charAt(i)) * multiply[i];
        }
        var remainder = sum % 11,p = 11 - remainder;
        var checkChar = no.substr(no.length - 1);
        result = (checkChar == checkDigit[p - 1]);
        return result;
    },

    Algo_IRAS_Others: function(no){
        var result = false,
            len = no.length,
            reg = /^\d{14}$/;
        multiply = [13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 57],
            checkdigit1 = ["4", "2", "1", "0", "9", "8", "7", "6", "5", "4", "3"],
            checkdigit2 = ["3", "9", "8", "7", "6", "5", "4", "3", "2", "1", "0"];
        if(reg.test(no)){
            var total = 0;
            for(var i = 0,l = len - 2; i < l; i++){
                total += parseInt(no.charAt(i)) * parseInt(multiply[i]);
            }
            var remainder = 11 - total % 11;
            if(checkdigit1[remainder - 1] + checkdigit2[remainder - 1] == no.substring(len - 2)){
                result = true;
            }
        }
        return result;
    },

    Algo_IRAS_PropTaxRefNo: function(no){
        var result = false,
            reg = /^\d{7}[a-zA-Z]$/,
            len = no.length;
        if(reg.test(no)){
            var multiply = ["5", "7", "1", "2", "3", "5", "7"],
                checkDigit = ["U", "S", "R", "P", "N", "K", "J", "G", "E", "W", "A"],
                total = 0;
            for(var i = 0; i < len - 1; i++){
                total += parseInt(no.charAt(i)) * parseInt(multiply[i]);
            }
            var remainder = 11 - total % 11;
            result = no.substring(len - 1) == checkDigit[remainder - 1];
        }
        return result;
    },

    ALGO_FIN: function(finNo){
        var result = false,
            reg = /^[FG]\d{7}[KLMNPQRTUWX]$/;
        if(reg.test(finNo)){
            var multiply = ["2", "7", "6", "5", "4", "3", "2"],
                total = 0;
            for(var i = 1; i <= 7; i++){
                total += parseInt(finNo.charAt(i)) * parseInt(multiply[i - 1]);
            }
            if(finNo.charAt(0) == "G"){
                total += 4;
            }
            var remainder = 11 - total % 11,
                checkDigit = ["K", "L", "M", "N", "P", "Q", "R", "T", "U", "W", "X"];
            if(checkDigit[remainder - 1] == finNo.charAt(finNo.length - 1)){
                result = true;
            }
        }
        return result;
    },

    ALGO_WORK_PERMIT_NO: function(workPermitNo){
        var result = false,
            reg = /^(\d[ ](\d{8}|\d{7}-))$/;
        if(reg.test(workPermitNo)){
            var multiply = ["3", "", "2", "7", "6", "5", "4", "3", "2"],
                total = 0;
            for(var i = 0; i <= 8; i++){
                if(i != 1){
                    total += parseInt(workPermitNo.charAt(i)) * parseInt(multiply[i]);
                }
            }
            total += 11;
            var remainder = total % 11,
                checkDigit = ["0", "-", "9", "8", "7", "6", "5", "4", "3", "2", "1"];
            if(checkDigit[remainder] == workPermitNo.charAt(workPermitNo.length - 1)){
                result = true;
            }
        }
        return result;
    },

    ALGO_IRAS_TAX_NO: function(taxNo){
        return this.ALGO_NRIC(taxNo) || this.ALGO_FIN(taxNo)
            || this.ALGO_WORK_PERMIT_NO(taxNo) || this.Algo_IRAS_ITR(taxNo) || this.Algo_IRAS_ASSIGNED_ID(taxNo) || this.Algo_IRAS_PropTaxRefNo(taxNo);
    },
    //no
    Credit_Luhn_NO:function(creditNo){
        if(creditNo.length == 9){
            return this.OCBC_Nine_Credit(creditNo);
        }else{
            var len = creditNo.length,check_digit = creditNo.charAt(len-1),subString = creditNo.substring(0,len-1),revString=[],count=1,sum=0;
            for(var i=len-2;i>=0;i--){
                if(count%2 == 1){
                    var digit = subString.charAt(i)*2-9;
                    if(digit>0){
                        revString.push(digit);
                    }else{
                        revString.push(subString.charAt(i)*2);
                    }
                }else{
                    var digit1 = subString.charAt(i)-9;
                    if(digit1>0){
                        revString.push(digit1);
                    }else{
                        revString.push(subString.charAt(i));
                    }
                }
                count++;
            }
            for(var i=0;i<=len-2;i++){
                sum+=parseInt(revString[i]);
            }
            return sum%10==check_digit;
        }
    },

    OCBC_Nine_Credit:function(creditNo){
        var len = creditNo.length,check_digit = creditNo.charAt(len-1),multiply = ["1", "2", "3", "5", "7", "8", "9", "2"],total=0,Reminder;
        for(var i = 0; i <= 7; i++){
            total += parseInt(creditNo.charAt(i)) * parseInt(multiply[i]);
        }
        Reminder = total % 11;
        if(Reminder == 0 || Reminder == 1){
            return check_digit==0;
        }else{
            return check_digit==(11-Reminder);
        }
    }
};
    return Algorithm;
});
