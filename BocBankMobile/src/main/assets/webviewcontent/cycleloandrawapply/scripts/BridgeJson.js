function setUpEvent(elem, eventType, handler) {
      return (elem.attachEvent ? elem.attachEvent("on" + eventType, handler)
        : ((elem.addEventListener) ? elem.addEventListener(eventType, handler, false) : null));
    }
function isMobileIOS() {

    var ua = navigator.userAgent;
    if (ua.indexOf('Mobile') > -1 && (ua.indexOf('iPhone') > -1 || ua.indexOf('iPod') > -1 || ua.indexOf('iPad') > -1)) {
        return true;

    }

    return false;
}
window.onload = (function () {
    alert("1212211");  
    var contractContent = {
        borrower: "张三",
        cdtype: "身份证",
        cdcard: "110111111111111111",
        loanamount: "100000",
        loanperiod: "24",
        floatingrate: 0.327372893298,
        reciver: "张三",
        reciveraccount: "111111111111",
        repayment: "张三",
        repaymentaccount: "222222222222",
        primaryaccountlist: [{
            rrimaryaccount: 1111111111111111,
            volumenumber: 111111,
            amountticket: 1111,
            accounttype: "普活",
            curency: "人民币",
            amount: "100000",
            accountmark: "自动转存"
        },
            {
                rrimaryaccount: 2222222222222222,
                volumenumber: 2222,
                amountticket: 2222,
                accounttype: "普活",
                curency: "人民币",
                amount: "200000",
                accountmark: "自动转存"
            },
            {
                rrimaryaccount: 3333333333333333,
                volumenumber: 3333,
                amountticket: 3333,
                accounttype: "普活",
                curency: "人民币",
                amount: "300000",
                accountmark: "自动转存"
            }]


    }


    function formatCurrency(num) {
        num = num.toString().replace(/\$|\,/g, '');
        if (isNaN(num))
            num = "0";
        sign = (num == (num = Math.abs(num)));
        num = Math.floor(num * 100 + 0.50000000001);
        cents = num % 100;
        num = Math.floor(num / 100).toString();
        if (cents < 10)
            cents = "0" + cents;
        for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
            num = num.substring(0, num.length - (4 * i + 3)) + ',' +
                num.substring(num.length - (4 * i + 3));
        return (((sign) ? '' : '-') + num + '.' + cents);
    }

    var contractContent;

    if (isMobileIOS()) {

        setUpEvent(document, 'BOCWebViewJavascriptBridgeReady', function (event) {
            var bridge = event.bridge;

           // alert(bridge);
            bridge.init(function (data, responseCallback) {
            });
            bridge.registerHandler('receiveData', function (data, responseCallback) {
                alert("receiveData");
                alert(data);
                alert(typeof data);
                contractContent = JSON.parse(data);

                alert(contractContent);
                alert(contractContent.borrower);
                var borrower = contractContent.borrower;
                var cdtype = contractContent.cdtype;
                var cdcard = contractContent.cdcard;
                var loanamount = formatCurrency(contractContent.loanamount);
                var loanperiod = contractContent.loanperiod;
                var floatingrate = contractContent.floatingrate.toFixed(2);
                var reciver = contractContent.reciver;
                var reciveraccount = contractContent.reciveraccount;
                var repayment = contractContent.repayment;
                var repaymentaccount = contractContent.repaymentaccount;
                var rrimaryaccount = contractContent.rrimaryaccount;

                if (borrower != "") {
                    document.getElementById("borrower").innerHTML = borrower;
                } else {
                    document.getElementById("borrower").innerHTML = "";
                }

                if (cdtype != "") {
                    document.getElementById("CDType").innerHTML = cdtype;
                } else {
                    document.getElementById("CDType").innerHTML = "";
                }
                if (cdcard != "") {
                    document.getElementById("CDCard").innerHTML = cdcard;
                } else {
                    document.getElementById("CDCard").innerHTML = "";
                }

                if (loanamount != "") {
                    document.getElementById("LoanAmount").innerHTML = loanamount;
                } else {
                    document.getElementById("LoanAmount").innerHTML = "";
                }
                if (loanperiod != "") {
                    document.getElementById("LoanPeriod").innerHTML = loanperiod;
                } else {
                    document.getElementById("LoanPeriod").innerHTML = "";
                }

                if (floatingrate != "") {
                    document.getElementById("FloatingRate").innerHTML = floatingrate;
                } else {
                    document.getElementById("FloatingRate").innerHTML = "";
                }
                if (reciver != "") {
                    document.getElementById("Reciver").innerHTML = reciver;
                } else {
                    document.getElementById("Reciver").innerHTML = "";
                }

                if (reciveraccount != "") {
                    document.getElementById("ReciverAccount").innerHTML = reciveraccount;
                } else {
                    document.getElementById("ReciverAccount").innerHTML = "";
                }
                if (Repayment != "") {
                    document.getElementById("Repayment").innerHTML = Repayment;
                } else {
                    document.getElementById("Repayment").innerHTML = "";
                }

                if (repaymentaccount != "") {
                    document.getElementById("RepaymentAccount").innerHTML = repaymentaccount;
                } else {
                    document.getElementById("RepaymentAccount").innerHTML = "";
                }


                for (var i = 0; i < contractContent.primaryaccountlist.length; i++) {
                    var creatTr = document.getElementById('PrimaryAccountList').insertRow(0);
                    var td1 = "定期一本通主账号：";
                    var td2 = creatTr.insertCell(1);


                    var td1 = creatTr.insertCell(0);
                    var td2 = creatTr.insertCell(1);
                    var td3 = creatTr.insertCell(2);
                    var td4 = creatTr.insertCell(3);
                    var td5 = creatTr.insertCell(4);
                    var td6 = creatTr.insertCell(5);
                    var td7 = creatTr.insertCell(6);
                    td1.innerHTML = contractContent.primaryaccountlist[i].rrimaryaccount;
                    td2.innerHTML = contractContent.primaryaccountlist[i].volumenumber;
                    td3.innerHTML = contractContent.primaryaccountlist[i].amountticket;
                    td4.innerHTML = contractContent.primaryaccountlist[i].accounttype;
                    td5.innerHTML = contractContent.primaryaccountlist[i].curency;
                    td6.innerHTML = contractContent.primaryaccountlist[i].formatCurrency(contractContent.Amount);
                    td7.innerHTML = contractContent.primaryaccountlist[i].accountmark;
                }

            });
        });
        return;
    } else {

        contractContent = JSON.parse(window.contract.getContract());//contract

        var borrower = contractContent.borrower;
        var cdtype = contractContent.cdtype;
        var cdcard = contractContent.cdcard;
        var loanamount = formatCurrency(contractContent.loanamount);
        var loanperiod = contractContent.loanperiod;
        var floatingrate = contractContent.floatingrate.toFixed(2);
        var reciver = contractContent.reciver;
        var reciveraccount = contractContent.reciveraccount;
        var repayment = contractContent.repayment;
        var repaymentaccount = contractContent.repaymentaccount;
        var rrimaryaccount = contractContent.rrimaryaccount;

        if (borrower != "") {
            document.getElementById("borrower").innerHTML = borrower;
        } else {
            document.getElementById("borrower").innerHTML = "";
        }

        if (cdtype != "") {
            document.getElementById("CDType").innerHTML = cdtype;
        } else {
            document.getElementById("CDType").innerHTML = "";
        }
        if (cdcard != "") {
            document.getElementById("CDCard").innerHTML = cdcard;
        } else {
            document.getElementById("CDCard").innerHTML = "";
        }

        if (loanamount != "") {
            document.getElementById("LoanAmount").innerHTML = loanamount;
        } else {
            document.getElementById("LoanAmount").innerHTML = "";
        }
        if (loanperiod != "") {
            document.getElementById("LoanPeriod").innerHTML = loanperiod;
        } else {
            document.getElementById("LoanPeriod").innerHTML = "";
        }

        if (floatingrate != "") {
            document.getElementById("FloatingRate").innerHTML = floatingrate;
        } else {
            document.getElementById("FloatingRate").innerHTML = "";
        }
        if (reciver != "") {
            document.getElementById("Reciver").innerHTML = reciver;
        } else {
            document.getElementById("Reciver").innerHTML = "";
        }

        if (reciveraccount != "") {
            document.getElementById("ReciverAccount").innerHTML = reciveraccount;
        } else {
            document.getElementById("ReciverAccount").innerHTML = "";
        }
        if (rrimaryaccount != "") {
            document.getElementById("Repayment").innerHTML = rrimaryaccount;
        } else {
            document.getElementById("Repayment").innerHTML = "";
        }

        if (repaymentaccount != "") {
            document.getElementById("RepaymentAccount").innerHTML = repaymentaccount;
        } else {
            document.getElementById("RepaymentAccount").innerHTML = "";
        }


        for (var i = 0; i < contractContent.primaryaccountlist.length; i++) {
            var creatTr = document.getElementById('PrimaryAccountList').insertRow(i + 1);
            var td1 = creatTr.insertCell(0);
            var td2 = creatTr.insertCell(1);
            var td3 = creatTr.insertCell(2);
            var td4 = creatTr.insertCell(3);
            var td5 = creatTr.insertCell(4);
            var td6 = creatTr.insertCell(5);
            var td7 = creatTr.insertCell(6);
            td1.innerHTML = contractContent.primaryaccountlist[i].rrimaryaccount;
            td2.innerHTML = contractContent.primaryaccountlist[i].volumenumber;
            td3.innerHTML = contractContent.primaryaccountlist[i].amountticket;
            td4.innerHTML = contractContent.primaryaccountlist[i].accounttype;
            td5.innerHTML = contractContent.primaryaccountlist[i].curency;
            td6.innerHTML = contractContent.primaryaccountlist[i].formatCurrency(contractContent.amount);
            td7.innerHTML = contractContent.primaryaccountlist[i].accountmark;
        }


    }
});