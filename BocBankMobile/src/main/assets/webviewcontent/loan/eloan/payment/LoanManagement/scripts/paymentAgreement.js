/**
 * Created by lsy7104 on 2016/8/18.
 */
function setUpEvent(elem, eventType, handler) {
    return (elem.attachEvent ? elem.attachEvent("on" + eventType, handler)
        : ((elem.addEventListener) ? elem.addEventListener(eventType, handler, false) : null));
}
    
function isMobileIOS(){
    var ua = navigator.userAgent;
    if(ua.indexOf('Mobile') > -1 && (ua.indexOf('iPhone') > -1 || ua.indexOf('iPod') > -1 || ua.indexOf('iPad') > -1)){
        return true;
    }
    return false;
}


function contractInfoProcess(contractInfo){
    var headStart = contractInfo.contract.toString().indexOf("<html>");
    var headEnd = contractInfo.contract.toString().indexOf('<p style=\"text-align: center;\">');
    var header = contractInfo.contract.substring(headStart,headEnd);
    var content = contractInfo.contract.replace(header,"");
    content = content.replace(/\\n/g,"");//去除所有换行
    content = content.replace(/\\"/g,'"');//去除所有引号前的斜线
    content = content.replace(/_+/g,"_");//将多个连续下划线变为1个
    content = content.replace(/_/g,"<u>_</u>");//给下划线做<u>标签
    content = content.replace(/_/g,"&nbsp&nbsp");//将所有下划线替换为空格，以避免手机端出现两条下划线的现象
    content = content.replace("</body>","");
    content = content.replace("</html>","");
    var contentUpdate=content.replace("{custName}",contractInfo.custName).replace("{custCerNo}",contractInfo.custCerNo)
        .replace("{relDebitAccount}",contractInfo.relDebitAccount).replace("{preference}",contractInfo.preference)
        .replace("{minLoanAmount}",contractInfo.minLoanAmount);
    if(contractInfo.dealType=="PLCF" || contractInfo.dealType=="WLCF"){
        contentUpdate=contentUpdate.replace("{limitNo}",contractInfo.limitNo).replace("{repayMode}",contractInfo.repayMode)
            .replace("{timeLimit}",contractInfo.timeLimit);
    }
    else if(contractInfo.dealType=="PLFB"){
        contentUpdate=contentUpdate.replace("{loanAccount}",contractInfo.loanAccount);
    }
    return contentUpdate;
}

$(function() {
    var contractInfo;
    if(isMobileIOS()){
        setUpEvent(document,'BOCWebViewJavascriptBridgeReady',function(event){
            var bridge = event.bridge;
            bridge.init(function(data,responseCallback){
            });
            bridge.registerHandler('receiveData', function(data, responseCallback) {
                contractInfo = JSON.parse(data);
                $("body").html(contractInfoProcess(contractInfo));
            });
        });
    }
    else{
        contractInfo = JSON.parse(window.contract.getContract());
        $("body").html(contractInfoProcess(contractInfo));
    }
});