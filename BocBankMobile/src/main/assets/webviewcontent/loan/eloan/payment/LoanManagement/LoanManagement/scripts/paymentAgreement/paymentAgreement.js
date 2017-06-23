/**
 * Created by lsy7104 on 2016/8/18.
 */
define(['zepto', 'common', 'model', 'viewcontroller', 'stackCtrl', 'checkForm', 'ejs'], function($, comm, model, vc, stackCtrl, checkForm, ejs){

    // var contractInfo={
    //     "custName":"张三",
    //     "custCerNo":"611111111111111111",
    //     "limitNo":"50000",
    //     "relDebitAccount":"6222222222222222",
    //     "preference":"先用贷款",
    //     "timeLimit":"20160808",
    //     "repayMode":"自动还款",
    //     "minLoanAmount":"1000",
    //     "contractNo":"11112025",
    //     "dealType":"WLCF",
    //     "loanAccount":"33333333",
    //     "contract":"<html>\n<head>\n\t<title></title>\n</head>\n<body>\n<p style=\"text-align: center;\"><span style=\"font-size: 26px;\"><strong>中国银行股份有限公司</strong></span></p>\n\n<p style=\"text-align: center;\"><span style=\"font-size: 20px;\"><strong>中银E贷关联借记卡支付协议</strong></span></p>\n\n<p style=\"text-align: center;\"><strong>（适用WLCF、PLCF产品，功能测试版）</strong></p>\n\n<p>&nbsp;</p>\n\n<p><strong>借款人：<u>______</u></strong><u style=\"font-weight: bold;\"><span style=\"color: rgb(255, 0, 0);\">{custName}</span></u><strong><u>_______</u></strong></p>\n\n<p><strong>​身份证件号码：<u>_____</u></strong><u style=\"font-weight: bold;\"><span style=\"color: rgb(255, 0, 0);\">{custCerNo}</span></u><strong><u>______</u></strong></p>\n\n<p>&nbsp;</p>\n\n<p><strong>贷款人：<u>_中国银行股份有限公司_</u></strong></p>\n\n<p>&nbsp;</p>\n\n<p>&nbsp; &nbsp; 借款人、贷款人根据有关法律、法规，在平等、自愿、诚信的基础上，经协商一致达成如下协议。</p>\n\n<p>&nbsp; &nbsp; 1、借款人同意将额度号为____<u style=\"font-weight: bold;\"><span style=\"color: rgb(255, 0, 0);\">{limitNo}</span></u>____的中银E贷额度与借款人名下的卡号为__<u style=\"font-weight: bold;\"><span style=\"color: rgb(255, 0, 0);\">{relDebitAccount}</span></u>__的借记卡进行关联，在以下场景下相关借记卡交易可触发放款（贷款人可根据实际情况增加或减少触发放款的场景）：</p>\n\n<p>&nbsp; &nbsp; （1）使用该借记卡在POS机刷卡；</p>\n\n<p>&nbsp; &nbsp; （2）使用该借记卡通过贷款人的网银进行支付；</p>\n\n<p>&nbsp; &nbsp; （3）使用该借记卡进行快捷支付；</p>\n\n<p>&nbsp; &nbsp; （4）使用该借记卡通过ATM、CSR等设备进行转账、取现；</p>\n\n<p>&nbsp; &nbsp; （5）使用该借记卡通过贷款人的网银等电子渠道进行跨行转账等。</p>\n\n<p>&nbsp; &nbsp; 2、借款人选择的用款偏好为___<u style=\"font-weight: bold;\"><span style=\"color: rgb(255, 0, 0);\">{preference}</span></u>___；</p>\n\n<p>&nbsp; &nbsp; （1）若选择的用款偏好为&ldquo;先用贷款&rdquo;，意味着优先使用中银E贷进行支付；</p>\n\n<p>&nbsp; &nbsp; （2）若选择的用款偏好为&ldquo;先用存款&rdquo;，意味着优先使用借记卡中的存款进行支付。</p>\n\n<p>&nbsp; &nbsp; 3、该协议下的借记卡交易触发放款的贷款期限统一为__<u style=\"font-weight: bold;\"><span style=\"color: rgb(255, 0, 0);\">{timeLimit}</span></u>__，还款方式统一为__<u style=\"font-weight: bold;\"><span style=\"color: rgb(255, 0, 0);\">{repayMode}</span></u>__；</p>\n\n<p>&nbsp; &nbsp; 4、在以下场景相关借记卡交易不会触发中银E贷放款：</p>\n\n<p>&nbsp; &nbsp; （1）若触发放款的金额小于__<u style=\"font-weight: bold;\"><span style=\"color: rgb(255, 0, 0);\">{minLoanAmount}</span></u>__元，则不触发中银E贷放款，而是使用借记卡的存款进行支付；</p>\n\n<p>&nbsp; &nbsp; （2）贷款人的相关系统正处于投产或批量处理等阶段，不支持放款；</p>\n\n<p>&nbsp; &nbsp; （3）支付商户在贷款人的限制名单中；</p>\n\n<p>&nbsp; &nbsp; （4）借款人额度已失效或被冻结。</p>\n\n<p>&nbsp; &nbsp; 5、借款人可以使用贷款人提供的电子服务渠道对该协议的内容进行修改，或者解除该协议，相应的系统记录可作为协议修改或解除的依据。</p>\n\n<p>&nbsp; &nbsp; 6、双方同意，本协议自借款人在贷款人电子服务渠道页面通过身份认证工具验证的方式点击接受本协议并经贷款人系统确认成功后生效。</p>\n\n<p>&nbsp;</p>\n\n<p><strong>借款人声明：本协议内容经与本人协商并经贷款人充分告知、说明，本人已理解并同意本协议的全部内容</strong></p>\n</body>\n</html>\n"
    // };

    var contractInfo=JSON.parse(window.contract.getContract());
    
    var paymentAgreementView = $.extendCls(vc, {
        services: [],
        ejs: '../LoanManagement/views/paymentAgreement/paymentAgreement.ejs',

        onLoadFinish:function () {
            var $body=$("body");
            var contentUpdate=$body.html().replace("{custName}",contractInfo.custName).replace("{custCerNo}",contractInfo.custCerNo)
                .replace("{relDebitAccount}",contractInfo.relDebitAccount).replace("{preference}",contractInfo.preference)
                .replace("{minLoanAmount}",contractInfo.minLoanAmount);
            if(contractInfo.dealType=="PLCF" || contractInfo.dealType=="WLCF"){
                contentUpdate=contentUpdate.replace("{limitNo}",contractInfo.limitNo).replace("{repayMode}",contractInfo.repayMode)
                    .replace("{timeLimit}",contractInfo.timeLimit);
            }
            else if(contractInfo.dealType=="PLFB"){
                contentUpdate=contentUpdate.replace("{loanAccount}",contractInfo.loanAccount);
            }

            $body.html(contentUpdate);
        }
    });


    return comm.Module.sub({
        init: function(){
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
            var view = new paymentAgreementView({
                id: 'paymentAgreement',
                el: 'body',
                eloper: "html",
                data: $.extend(contractInfo,{contract : content})
            });
            view.load();
        }
    })
});