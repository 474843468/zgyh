/**
 * Created by Administrator on 2016/3/22 0022.
 */
define('bpayOrcCheck', ['common', 'algorit'], function(Common, algorit) {

    var BpayCheck = {};
    BpayCheck.check = function(postData){
        var billerCode = postData.billerCode;
        if(billerCode == "20040001"){
            //var ref3Code =  postData.typeMap['billerRef3'];
            var ref3Value = postData.billerRef3;

            return algorit.Algo_KK_Hospital_NRICNo(ref3Value);

        }

        return true;

    }

    return BpayCheck;
});